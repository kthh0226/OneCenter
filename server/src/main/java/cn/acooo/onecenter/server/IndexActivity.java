package cn.acooo.onecenter.server;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import cn.acooo.onecenter.core.auto.OneCenterProtos.CSQueryApps;
import cn.acooo.onecenter.core.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.server.adapter.PhonesAdapter;
import cn.acooo.onecenter.server.model.PhoneClient;
import cn.acooo.onecenter.server.service.ServerService;

public class IndexActivity extends BaseActivity{
	
	private ListView listView;
	private Button openButton;
	private Button disconnectButton;
	private PhonesAdapter phonesAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "into index activity oncreate===========================");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		listView = (ListView)super.findViewById(R.id.phones);
		openButton = (Button)super.findViewById(R.id.open);
		disconnectButton = (Button)super.findViewById(R.id.disconnect);
		
		openButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				PhoneClient pc = phonesAdapter.getSelectedPhoneClient();
				if(pc == null){
					showInfo("提示", "没有任何连接的设备");
				}else{
					Intent intent = new Intent(IndexActivity.this, MyPhoneActivity.class);
					startActivity(intent);
					pc.send(MessageType.MSG_ID_APPS,CSQueryApps.newBuilder());
				}
				
			}
		});
		
		disconnectButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				phonesAdapter.removeSelectedItem();
				phonesAdapter.notifyDataSetChanged();
			}
		});
		
		phonesAdapter = new PhonesAdapter(this);
		listView.setAdapter(phonesAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(App.TAG, "position========"+position);
				phonesAdapter.setSelectItem(position);
				phonesAdapter.notifyDataSetInvalidated(); 
				
			}
		});
		
		if(!App.serverServiceIsRun){
			Intent intent = new Intent(this, ServerService.class);
			super.startService(intent);
		}

	}
	
	@Override
	public Callback getActivityCallBack() {
		return new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				Log.i(TAG, "into indexActivity handle message,msg="+msg);
				switch(msg.what){
					case UI_MSG_ID_NEW_PHONE:
						Log.i(TAG, "into UI_MSG_ID_NEW_PHONE handle message,msg="+msg);
						phonesAdapter.reloadPhoneClient();
						phonesAdapter.notifyDataSetChanged();
						return true;
				}
				return false;
			}
		};
	}
}
