package cn.acooo.onecenter.core.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.acooo.onecenter.core.model.SmsInfo;

/**
 * 查询手机内的短信
 * Created by ly580914 on 15/1/15.
 */
public class SmsUtils {
    public static List<SmsInfo> getSms(Context context){

        List<SmsInfo> smsInfos = new ArrayList<SmsInfo>();
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse("content://sms");
        Cursor cursor = cr.query(uri,new String[] { "_id","address", "date", "type", "body" }, null,null, null);
        while (cursor.moveToNext()){
            SmsInfo info = new SmsInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            info.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            info.setDate(cursor.getLong(cursor.getColumnIndex("date")));
            info.setType(cursor.getInt(cursor.getColumnIndex("type")));
            info.setBody(cursor.getString(cursor.getColumnIndex("body")));
            smsInfos.add(info);
        }
        cursor.close();
        return smsInfos;
    }
    public static List<SmsInfo> getSmsById(Context context, int id){
        List<SmsInfo> smsInfos = new ArrayList<SmsInfo>();
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse("content://sms");
        Cursor cursor = cr.query(uri,new String[] {"address", "date", "type", "body" }, "thread_id=?",new String[]{id+""}, "date desc");
        while (cursor.moveToNext()){
            SmsInfo info = new SmsInfo();
            info.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            info.setDate(cursor.getLong(cursor.getColumnIndex("date")));
            info.setType(cursor.getInt(cursor.getColumnIndex("type")));
            info.setBody(cursor.getString(cursor.getColumnIndex("body")));
            info.setName(ConversationUtils.getContactNameByPhoneNumber(context, cursor.getString(cursor.getColumnIndex("address"))));
            smsInfos.add(info);
        }
        cursor.close();
        return smsInfos;
    }

}
