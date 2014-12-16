package cn.acooo.onecenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import cn.acooo.onecenter.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.auto.OneCenterProtos.SCQueryApps;

public class MyPhoneActivity extends BaseActivity{
	
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_phone);
		textView = (TextView)findViewById(R.id.showArea);
		if(App.selectedPhoneClient != null){
			textView.setText(App.selectedPhoneClient.getIp());
		}
	}

	@Override
	public Callback getActivityCallBack() {
		return new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				try{
					switch(msg.what){
					case MessageType.MSG_ID_APPS_VALUE:
						byte[] data = (byte[])msg.obj;
						
						SCQueryApps builder = SCQueryApps.parseFrom(data);
						Log.i(TAG, builder.toString());
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
