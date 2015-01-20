package cn.acooo.onecenter.phone.logic;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.model.ContactsInfo;
import cn.acooo.onecenter.phone.App;

public class AppLogic {
	private final static AppLogic appLogic = new AppLogic();
	private AppLogic(){
		
	}
	public static AppLogic getInstance(){
		return appLogic;
	}

    /**
     * 获取全部的联系人
     * @param context
     * @return
     */
    public List<ContactsInfo> getAllContacts(Context context){
        List<ContactsInfo> rs = this.getLocalContacts(context);
        Log.i("ONE","local contacts"+rs.toString());
        return rs;
    }

    /**
     * 获取本地存储的联系人
     * @param context
     * @return
     */
    public List<ContactsInfo> getLocalContacts(Context context) {
        ContentResolver cr = context.getContentResolver();
        List<ContactsInfo> result = new ArrayList<ContactsInfo>();
        String str[] = { Phone.CONTACT_ID, Phone.DISPLAY_NAME, Phone.NUMBER,
                Phone.PHOTO_ID };
        Cursor cur = cr.query(Phone.CONTENT_URI, str, null,null, null);
        try{
            if (cur != null) {
                while (cur.moveToNext()) {
                    ContactsInfo contactsInfo = new ContactsInfo();
                    contactsInfo.setType(1);
                    contactsInfo.setNumber(cur.getString(cur
                            .getColumnIndex(Phone.NUMBER)));// 得到手机号码
                    contactsInfo.setName(cur.getString(cur
                            .getColumnIndex(Phone.DISPLAY_NAME)));
                    // contactsInfo.setContactsPhotoId(cur.getLong(cur.getColumnIndex(Phone.PHOTO_ID)));
                    long contactId = cur.getLong(cur.getColumnIndex(Phone.CONTACT_ID));
                    contactsInfo.setId(contactId);
                    long photoId = cur.getLong(cur.getColumnIndex(Phone.PHOTO_ID));
                    // photoId 大于0 表示联系人有头像 ，如果没有给此人设置头像则给他一个默认的
                    if (photoId > 0) {
                        Uri uri = ContentUris.withAppendedId(
                                ContactsContract.Contacts.CONTENT_URI, contactId);
                        InputStream input = ContactsContract.Contacts
                                .openContactPhotoInputStream(cr, uri);
                        contactsInfo.setIcon(BitmapFactory.decodeStream(input));
                    } else {
//                        contactsInfo.setBitmap(BitmapFactory.decodeResource(
//                                context.getResources(), R.drawable.ic_launcher));
                    }
                    result.add(contactsInfo);
                }
            }
        }finally {
            cur.close();
        }
        return result;
    }
}
