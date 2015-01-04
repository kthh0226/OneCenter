package cn.acooo.onecenter.phone.logic;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.acooo.onecenter.core.model.ContactsInfo;

public class AppLogic {
	private final static AppLogic appLogic = new AppLogic();
	private AppLogic(){
		
	}
	public static AppLogic getInstance(){
		return appLogic;
	}
	/** 
	 * 查询手机内非系统应用 
	 * @param context 
	 * @return 
	 */  
	public static List<PackageInfo> getAllApps(Context context) {  
	    List<PackageInfo> apps = new ArrayList<PackageInfo>();  
	    PackageManager pManager = context.getPackageManager();  
	    //获取手机内所有应用  
	    List<PackageInfo> paklist = pManager.getInstalledPackages(0);  
	    for (int i = 0; i < paklist.size(); i++) {  
	        PackageInfo pak = (PackageInfo) paklist.get(i);  
	        //判断是否为非系统预装的应用程序  
	        if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {  
	            // customs applications  
	            apps.add(pak);  
	        }  
	    }  
	    return apps;  
	}

    public String getSmsInPhone(Context context)
    {
        final String SMS_URI_ALL = "content://sms/";
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_SEND = "content://sms/sent";
        final String SMS_URI_DRAFT = "content://sms/draft";

        StringBuilder smsBuilder = new StringBuilder();
        Cursor cur = null;
        try{
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type"};
            Uri uri = Uri.parse(SMS_URI_ALL);
            cur = cr.query(uri, projection, null, null, "date desc");

            if (cur.moveToFirst()) {
                String name;
                String phoneNumber;
                String smsbody;
                String date;
                String type;

                int nameColumn = cur.getColumnIndex("person");
                int phoneNumberColumn = cur.getColumnIndex("address");
                int smsbodyColumn = cur.getColumnIndex("body");
                int dateColumn = cur.getColumnIndex("date");
                int typeColumn = cur.getColumnIndex("type");

                do{
                    name = cur.getString(nameColumn);
                    phoneNumber = cur.getString(phoneNumberColumn);
                    smsbody = cur.getString(smsbodyColumn);

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss");

                    Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
                    date = dateFormat.format(d);

                    int typeId = cur.getInt(typeColumn);
                    if(typeId == 1){
                        type = "接收";
                    } else if(typeId == 2){
                        type = "发送";
                    } else {
                        type = "";
                    }

                    smsBuilder.append("[");
                    smsBuilder.append(name+",");
                    smsBuilder.append(phoneNumber+",");
                    smsBuilder.append(smsbody+",");
                    smsBuilder.append(date+",");
                    smsBuilder.append(type);
                    smsBuilder.append("] ");
                    if(smsbody == null) smsbody = "";
                }while(cur.moveToNext());
            } else {
                smsBuilder.append("no result!");
            }

            smsBuilder.append("getSmsInPhone has executed!");
        } catch(SQLiteException ex) {

            Log.e("SQLiteException in getSmsInPhone", ex.getMessage());
        }finally {
            cur.close();
        }
        return smsBuilder.toString();
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
