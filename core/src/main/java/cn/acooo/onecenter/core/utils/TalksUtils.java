package cn.acooo.onecenter.core.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import cn.acooo.onecenter.core.model.TalksInfo;

/**
 * 获取通话记录
 * Created by ly580914 on 15/1/15.
 */
public class TalksUtils {
    public static List<TalksInfo> getTalksInfo(Context context){
        List<TalksInfo> talksInfos = new ArrayList<TalksInfo>();
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse("content://call_log/calls");
        Cursor cursor = cr.query(uri,new String[] { "_id","number", "date", "duration", "name","type" }, null,null, null);
        while (cursor.moveToNext()){
            TalksInfo info = new TalksInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            info.setType(cursor.getInt(cursor.getColumnIndex("type")));
            info.setDate(cursor.getLong(cursor.getColumnIndex("date")));
            info.setDuration(cursor.getLong(cursor.getColumnIndex("duration")));
            info.setName(cursor.getString(cursor.getColumnIndex("name")));
            info.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            talksInfos.add(info);
        }
        cursor.close();
        return talksInfos;
    }
}
