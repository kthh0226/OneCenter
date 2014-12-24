package cn.acooo.onecenter.phone;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
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

import com.google.protobuf.ByteString;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.auto.OneCenterProtos.AppInfo;
import cn.acooo.onecenter.core.auto.OneCenterProtos.CSDownloadApk;
import cn.acooo.onecenter.core.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.core.auto.OneCenterProtos.SCDownloadApk;
import cn.acooo.onecenter.core.auto.OneCenterProtos.SCQueryApps;
import cn.acooo.onecenter.core.utils.FileUtils;
import cn.acooo.onecenter.core.utils.ImageUtil;
import cn.acooo.onecenter.phone.logic.AppLogic;
import cn.acooo.onecenter.phone.service.SocketService;

public class MainActivity extends BaseActivity {
	private EditText ip;
	private EditText port;
	private Intent socketServiceIntent; 
	private DecimalFormat df = new DecimalFormat("#.00");
	
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	@Override
	public Callback getActivityCallBack() {
		return new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				try{
					switch(msg.what){
					case OneCenterProtos.MessageType.MSG_ID_APPS_VALUE:
						Log.i(TAG, "into UI_MSG_ID_NEW_PHONE handle message,msg="+msg);
						SCQueryApps.Builder builder = SCQueryApps.newBuilder();
						List<PackageInfo> ps = AppLogic.getAllApps(MainActivity.this);
						for(PackageInfo info : ps){
							AppInfo.Builder appInfoBuilder = AppInfo.newBuilder();
							appInfoBuilder.setName(info.applicationInfo.loadLabel(  
						            getPackageManager()).toString());
							appInfoBuilder.setPackageName(info.packageName);
							appInfoBuilder.setVersion("version: "+info.versionName);
							String publicSourceDir = info.applicationInfo.publicSourceDir;
							App.apkPaths.put(info.packageName, publicSourceDir);
							double size = Double.valueOf(new File(publicSourceDir).length());
							appInfoBuilder.setPackageSize("size："+df.format(size/1024/1024)+"M");
							Drawable icon = info.applicationInfo.loadIcon(getPackageManager());
							ByteString byteString = ByteString.copyFrom(ImageUtil.drawableToBytes(icon));
							appInfoBuilder.setIcon(byteString);
							builder.addApps(appInfoBuilder);
						}
						App.send(MessageType.MSG_ID_APPS,builder);
						return true;
					case MessageType.MSG_ID_DOWNLOAD_APK_VALUE:
						CSDownloadApk csDownloadApk = CSDownloadApk.parseFrom((byte[])msg.obj);
						String packageName = csDownloadApk.getPackageName();
						SCDownloadApk.Builder scDownloadApk = SCDownloadApk.newBuilder();
						byte[] bs = FileUtils.toByteArrayByLargeFile(App.apkPaths.get(packageName));
						scDownloadApk.setData(ByteString.copyFrom(bs));
						scDownloadApk.setPackageName(packageName);
						App.send(MessageType.MSG_ID_DOWNLOAD_APK, scDownloadApk);
						return true;
					}
				}catch(Exception e){
					Log.e(TAG, "MainActivityHandler error", e);
					return true;
				}
				return false;
			}
		};
	}
}
