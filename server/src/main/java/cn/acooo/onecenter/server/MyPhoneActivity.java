package cn.acooo.onecenter.server;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import java.util.Map;

import cn.acooo.onecenter.core.BaseActivity;
import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.core.auto.OneCenterProtos.SCQueryApps;
import cn.acooo.onecenter.core.auto.OneCenterProtos.SCQueryContacts;
import cn.acooo.onecenter.core.model.AppInfo;
import cn.acooo.onecenter.core.model.ContactsInfo;
import cn.acooo.onecenter.core.utils.CommonsUtil;
import cn.acooo.onecenter.server.adapter.ContactsAdapter;
import cn.acooo.onecenter.server.adapter.MyAppListAdapter;
import cn.acooo.onecenter.server.model.PhoneMenus;

public class MyPhoneActivity extends BaseActivity implements MyPhoneFeatureListFragment.Callbacks {
	
	private ListView listView;
	private AppDetailListFragment appDetail;
    private ContactsDetailListFragment contactsDetail;

    private MyPhoneFeatureListFragment ff;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
        ff = (MyPhoneFeatureListFragment)(getFragmentManager().findFragmentById(R.id.phone_feature_list));
        ff.setActivateOnItemClick(true);
        ff.getListView().setItemChecked(PhoneMenus.APPS.ordinal(),true);
        onItemSelected(PhoneMenus.APPS);
	}

	@Override
	public Callback getActivityCallBack() {
		return new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				try{
					switch(msg.what){
					case MessageType.MSG_ID_APPS_VALUE:{
                        MyAppListAdapter myAppListAdapter = (MyAppListAdapter)appDetail.getListAdapter();
                        SCQueryApps builder = SCQueryApps.parseFrom((byte[])msg.obj);
                        Map<String,PackageInfo> ms = CommonsUtil.getAllAppsByMap(MyPhoneActivity.this);
                        myAppListAdapter.clearAppItem();
                        for(OneCenterProtos.AppInfo app : builder.getAppsList()){
                            PackageInfo pinfo = ms.get(app.getPackageName());
                            myAppListAdapter.addAppItem(new AppInfo(app,pinfo));
                        }
                        myAppListAdapter.notifyDataSetChanged();
                        return true;
                    }
//                    case MessageType.MSG_ID_DOWNLOAD_ICON_VALUE:{
//                        OneCenterProtos.SCPushImage scPushImage = OneCenterProtos.SCPushImage.parseFrom((byte[])msg.obj);
//                        MyAppListAdapter myAppListAdapter = (MyAppListAdapter)appDetail.getListAdapter();
//                        myAppListAdapter.putAppIcon(scPushImage.getImageInfo().getName(),scPushImage.getImageInfo().getImage());
//                        return true;
//                    }

                    case MessageType.MSG_ID_QUERY_CONTACTS_VALUE:
                        ContactsAdapter contactsAdapter = (ContactsAdapter)contactsDetail.getListAdapter();
                        SCQueryContacts scQueryContacts = SCQueryContacts.parseFrom((byte[])msg.obj);
                        contactsAdapter.clearData();
                        for(OneCenterProtos.ContactsInfo info : scQueryContacts.getInfosList()){
                            contactsAdapter.addContacts(new ContactsInfo(info));
                        }
                        contactsAdapter.notifyDataSetChanged();
                        return true;
					case UI_MSG_ID_NEW_PHONE:
						Log.i(TAG, "into UI_MSG_ID_NEW_PHONE handle message,msg="+msg);
						Intent intent = new Intent(MyPhoneActivity.this,IndexActivity.class);
						startActivity(intent);
						return true;
					}
				}catch(Exception e){
					Log.e(TAG, "handler error,msg="+msg, e);
					return true;
				}
				return false;
			}
		};
	}

    @Override
    protected void initHandler() {
        App.handler = myHandler;
    }

    @Override
    public void onItemSelected(PhoneMenus phones) {
        Log.i(TAG,"onItemSelected,id========="+phones);
        switch (phones){
            case APPS:
                if(appDetail == null){
                    appDetail = new AppDetailListFragment();
                }
                App.selectedPhoneClient.send(MessageType.MSG_ID_APPS, OneCenterProtos.CSQueryApps.newBuilder());
                getFragmentManager().beginTransaction()
                        .replace(R.id.phone_feature_detail_container, appDetail).commit();
                break;
            case CONTACTS:
                App.selectedPhoneClient.send(MessageType.MSG_ID_QUERY_CONTACTS,OneCenterProtos.CSQueryContacts.newBuilder());
                contactsDetail = contactsDetail == null? new ContactsDetailListFragment():contactsDetail;
                getFragmentManager().beginTransaction()
                        .replace(R.id.phone_feature_detail_container, contactsDetail).commit();
                break;
            case TALKS:
                break;
            case MESSAGE:
                break;
            default:
                Log.e(TAG,"MyPhoneContent error,MyPhoneContent="+phones);
        }
    }
}
