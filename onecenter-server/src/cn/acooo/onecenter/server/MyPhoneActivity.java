package cn.acooo.onecenter.server;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import cn.acooo.onecenter.auto.OneCenterProtos.AppInfo;
import cn.acooo.onecenter.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.auto.OneCenterProtos.SCQueryApps;
import cn.acooo.onecenter.server.adapter.MyAppListAdapter;
import cn.acooo.onecenter.server.model.AppItem;

public class MyPhoneActivity extends BaseActivity{
	
	private ListView listView;
	private MyAppListAdapter myAppListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_phone);
		listView = (ListView)findViewById(R.id.showArea);
		myAppListAdapter = new MyAppListAdapter(this);
		listView.setAdapter(myAppListAdapter);
	}

	@Override
	public Callback getActivityCallBack() {
		return new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				try{
					switch(msg.what){
					case MessageType.MSG_ID_APPS_VALUE:
						SCQueryApps builder = SCQueryApps.parseFrom((byte[])msg.obj);
						for(AppInfo app : builder.getAppsList()){
							myAppListAdapter.addAppItem(new AppItem(app,null));
						}
						myAppListAdapter.notifyDataSetChanged();
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
}
