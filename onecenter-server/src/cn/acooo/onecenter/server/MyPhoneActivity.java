package cn.acooo.onecenter.server;

import java.io.File;
import java.util.Map;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import cn.acooo.onecenter.auto.OneCenterProtos.AppInfo;
import cn.acooo.onecenter.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.auto.OneCenterProtos.SCDownloadApk;
import cn.acooo.onecenter.auto.OneCenterProtos.SCQueryApps;
import cn.acooo.onecenter.server.adapter.MyAppListAdapter;
import cn.acooo.onecenter.server.model.AppItem;
import cn.acooo.onecenter.utils.CommonsUtil;
import cn.acooo.onecenter.utils.FileUtils;

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
					case MessageType.MSG_ID_DOWNLOAD_APK_VALUE:
						SCDownloadApk scDownloadApk = SCDownloadApk.parseFrom((byte[])msg.obj);
						byte[] data = scDownloadApk.getData().toByteArray();
						Log.i(TAG, "recive data length=="+data.length);
						String path = Environment.getExternalStorageDirectory().getPath() +"/onecenter/";
						Log.i(TAG, "path===="+path);
						File file = FileUtils.SaveFileFromInputStream(data, path, scDownloadApk.getPackageName()+".apk");
						Intent installIntent = new Intent();
						installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						installIntent.setAction(android.content.Intent.ACTION_VIEW);
						installIntent.setDataAndType(Uri.fromFile(file),
		                                 "application/vnd.android.package-archive");
		                startActivity(installIntent);
		                 return true;
					case MessageType.MSG_ID_APPS_VALUE:
						Log.d(TAG, "into apps============");
						SCQueryApps builder = SCQueryApps.parseFrom((byte[])msg.obj);
						Map<String,PackageInfo> ms = CommonsUtil.getAllAppsByMap(MyPhoneActivity.this);
						for(AppInfo app : builder.getAppsList()){
							PackageInfo pinfo = ms.get(app.getPackageName());
							myAppListAdapter.addAppItem(new AppItem(app,pinfo));
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
