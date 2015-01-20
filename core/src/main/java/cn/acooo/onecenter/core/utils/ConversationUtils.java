package cn.acooo.onecenter.core.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.List;

import cn.acooo.onecenter.core.model.ConversationInfo;

/**
 * Created by ly580914 on 15/1/19.
 */
public class ConversationUtils {
    public static List<ConversationInfo> getAllConversation(Context context){
        ContentResolver cr = context.getContentResolver();
        List<ConversationInfo> conversationInfos = new ArrayList<ConversationInfo>();
        Cursor cursor = cr.query(Uri.parse(MyContant.URI_SMS),new String[] { "* from threads--" }, null, null, "date desc");
        while (cursor.moveToNext()){
            ConversationInfo info = new ConversationInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            info.setCount(cursor.getInt(cursor.getColumnIndex("message_count")));
            info.setDate(cursor.getLong(cursor.getColumnIndex("date")));
            info.setRecipient(cursor.getInt(cursor.getColumnIndex("recipient_ids")));
            info.setSnippet(cursor.getString(cursor.getColumnIndex("snippet")));
            info.setName(getNumberByRecipient(context, cursor.getInt(cursor.getColumnIndex("recipient_ids"))));
            conversationInfos.add(info);
        }
        cursor.close();
        return conversationInfos;
    }
    private static String getNumberByRecipient(Context context, int recipient){
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(Uri.parse("content://mms-sms/canonical-addresses"), null, "_id = " + recipient, null, null);//query(Uri.parse(MyContant.URI_SMS),new String[]{"* from canonical_addresses--"},"_id=?",new String[recipient],null);
        cursor.moveToNext();
        String number = cursor.getString(cursor.getColumnIndex("address"));
        if (number.length() > 11){
            number = number.substring(number.length() - 11, number.length());
        }
        cursor.close();
        String name = getContactNameByPhoneNumber(context, number);
        if (name == null){
            return number;
        }else{
            return name;
        }
    }
     public static String getContactNameByPhoneNumber(Context context, String address) {
        String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
                + address + "'",
                null,
                null);
        if (cursor == null) {
            return null;
            }
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int nameFieldColumnIndex = cursor
            .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cursor.getString(nameFieldColumnIndex);
            return name;
            }
        return null;
        }
}
