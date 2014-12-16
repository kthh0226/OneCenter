package cn.acooo.onecenter.phone;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.acooo.onecenter.auto.OneCenterProtos.AppInfo;
import cn.acooo.onecenter.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.auto.OneCenterProtos.SCQueryApps;
import cn.acooo.onecenter.phone.logic.AppLogic;
import cn.acooo.onecenter.phone.service.SocketService;

public class MainActivity extends BaseActivity {
	private EditText ip;
	private EditText port;
	Intent socketServiceIntent; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button connectButton = (Button) findViewById(R.id.connect);
		ip = (EditText)findViewById(R.id.ip);
		port = (EditText)findViewById(R.id.port);
		socketServiceIntent = new Intent(this,SocketService.class);

		connectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				socketServiceIntent.putExtra(SocketService.KEY_IP, ip.getText().toString());
				socketServiceIntent.putExtra(SocketService.KEY_PORT, Integer.parseInt(port.getText().toString()));
				Log.i(App.TAG, "startService..."+socketServiceIntent.getExtras());
				startService(socketServiceIntent);
			}
		});
		
		Button disconnectButton = (Button) findViewById(R.id.disconnect);
		disconnectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(App.TAG, "stopService..."+socketServiceIntent.getExtras());
				if(App.socketIsRun){
					stopService(socketServiceIntent);
				}else{
					myHandler.sendEmptyMessage(UI_MSG_ID_NOT_CONNECTED);
				}
				
				
				
			}
		});

		
		
		
		//File file = new File("/data/app/cn.acooo.onecenter-1.apk");
//		if(file.exists()){
//			Log.i(TAG, "file name======"+file.getName());
//		}else{
//			Log.i(TAG, "file not found!!!!");
//		}


//		Channel channel = App.getInstance().getChannel();
//		for(int i = 0;i<10;i++){
//			FlyProtos.CSLogin.Builder builder = FlyProtos.CSLogin.newBuilder();
//			builder.setPlayerId(10000);
//			byte[] bytes = builder.build().toByteArray();
//			KMessage km = new KMessage(120,bytes);
//			channel.writeAndFlush(km);
//			
//		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Callback getActivityCallBack() {
		return new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				Log.i(TAG, "into MainActivity handle message,msg="+msg);
				switch(msg.what){
				case MessageType.MSG_ID_APPS_VALUE:
						Log.i(TAG, "into UI_MSG_ID_NEW_PHONE handle message,msg="+msg);
						SCQueryApps.Builder builder = SCQueryApps.newBuilder();
						List<PackageInfo> ps = AppLogic.getAllApps(MainActivity.this);
						for(PackageInfo info : ps){
							AppInfo.Builder appInfoBuilder = AppInfo.newBuilder();
							appInfoBuilder.setName(info.applicationInfo.loadLabel(  
						            getPackageManager()).toString());
							appInfoBuilder.setPackageName(info.packageName);
							appInfoBuilder.setVersion(info.versionName);
							appInfoBuilder.setPackageSize(""+Integer.valueOf((int) new File(info.applicationInfo.publicSourceDir).length()));
							appInfoBuilder.setIcon(info.applicationInfo.loadIcon(getPackageManager()).toString());
							builder.addApps(appInfoBuilder);
						}
						App.send(MessageType.MSG_ID_APPS, builder);
						return true;
				}
				return false;
			}
		};
	}
}
