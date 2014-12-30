package cn.acooo.onecenter.phone.logic;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        try{
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type"};
            Uri uri = Uri.parse(SMS_URI_ALL);
            Cursor cur = cr.query(uri, projection, null, null, "date desc");

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

            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
        }
        return smsBuilder.toString();
    }

    public List<String> getLocalContactsInfos(Context context) {
        ContentResolver cr = context.getContentResolver();
        String str[] = { Phone.CONTACT_ID, Phone.DISPLAY_NAME, Phone.NUMBER,
                Phone.PHOTO_ID };
        Cursor cur = cr.query(Phone.CONTENT_URI, str, null,null, null);

        if (cur != null) {
            while (cur.moveToNext()) {
//                contactsInfo = new ContactsInfo();
//                contactsInfo.setContactsPhone(cur.getString(cur
//                        .getColumnIndex(Phone.NUMBER)));// 得到手机号码
//                contactsInfo.setContactsName(cur.getString(cur
//                        .getColumnIndex(Phone.DISPLAY_NAME)));
//                // contactsInfo.setContactsPhotoId(cur.getLong(cur.getColumnIndex(Phone.PHOTO_ID)));
//                long contactid = cur.getLong(cur
//                        .getColumnIndex(Phone.CONTACT_ID));
//                long photoid = cur.getLong(cur.getColumnIndex(Phone.PHOTO_ID));
//                // 如果photoid 大于0 表示联系人有头像 ，如果没有给此人设置头像则给他一个默认的
//                if (photoid > 0) {
//                    Uri uri = ContentUris.withAppendedId(
//                            ContactsContract.Contacts.CONTENT_URI, contactid);
//                    InputStream input = ContactsContract.Contacts
//                            .openContactPhotoInputStream(cr, uri);
//                    contactsInfo.setBitmap(BitmapFactory.decodeStream(input));
//                } else {
//                    contactsInfo.setBitmap(BitmapFactory.decodeResource(
//                            context.getResources(), R.drawable.ic_launcher));
//                }
//
//                System.out.println("---------联系人电话--"
//                        + contactsInfo.getContactsPhone());
//                localList.add(contactsInfo);

            }
        }
        cur.close();
        return null;

    }
    public List<String> getSIMContactsInfos(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        System.out.println("---------SIM--------");
        ContentResolver cr = context.getContentResolver();
        final String SIM_URI_ADN = "content://icc/adn";// SIM卡

        Uri uri = Uri.parse(SIM_URI_ADN);
        Cursor cursor = cr.query(uri, null, null, null, null);
        while (cursor.moveToFirst()) {
//            SIMContactsInfo = new ContactsInfo();
//            SIMContactsInfo.setContactsName(cursor.getString(cursor
//                    .getColumnIndex("name")));
//            SIMContactsInfo
//                    .setContactsPhone(cursor.getString(cursor
//                            .getColumnIndex("number")));
//            SIMContactsInfo
//                    .setBitmap(BitmapFactory.decodeResource(
//                            context.getResources(),
//                            R.drawable.ic_launcher));
//            SIMList.add(SIMContactsInfo);
        }
        cursor.close();
        return null;
    }
}
