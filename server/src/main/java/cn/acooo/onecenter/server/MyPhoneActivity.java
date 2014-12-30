package cn.acooo.onecenter.server;

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

import java.io.File;
import java.util.Map;

import cn.acooo.onecenter.core.BaseActivity;
import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.auto.OneCenterProtos.AppInfo;
import cn.acooo.onecenter.core.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.core.auto.OneCenterProtos.SCDownloadApk;
import cn.acooo.onecenter.core.auto.OneCenterProtos.SCQueryApps;
import cn.acooo.onecenter.core.utils.CommonsUtil;
import cn.acooo.onecenter.core.utils.FileUtils;
import cn.acooo.onecenter.server.adapter.MyAppListAdapter;
import cn.acooo.onecenter.server.model.AppItem;

public class MyPhoneActivity extends BaseActivity implements MyPhoneFeatureListFragment.Callbacks {
	
	private ListView listView;
//	private MyAppListAdapter myAppListAdapter;
	private AppDetailListFragment appDetail;
    private MyPhoneFeatureListFragment ff;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
        ff = (MyPhoneFeatureListFragment)(getFragmentManager().findFragmentById(R.id.phone_feature_list));
        ff.setActivateOnItemClick(true);
        ff.getListView().setItemChecked(0,true);
        onItemSelected("0");
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
                        MyAppListAdapter myAppListAdapter = (MyAppListAdapter)appDetail.getListView().getAdapter();
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

    @Override
    protected void initHandler() {
        App.handler = myHandler;
    }

    @Override
    public void onItemSelected(String id) {
        Log.i(TAG,"onItemSelected,id========="+id);
        Bundle args = new Bundle();
        if("0".equals(id)){
            App.selectedPhoneClient.send(MessageType.MSG_ID_APPS, OneCenterProtos.CSQueryApps.newBuilder());
            appDetail = appDetail == null? new AppDetailListFragment():appDetail;
            getFragmentManager().beginTransaction()
                    .replace(R.id.phone_feature_detail_container, appDetail).commit();
        }else{
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.phone_feature_detail_container, null).commit();
        }
    }
}
