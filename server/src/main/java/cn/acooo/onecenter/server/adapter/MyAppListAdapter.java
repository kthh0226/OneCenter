package cn.acooo.onecenter.server.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import cn.acooo.onecenter.core.auto.OneCenterProtos.CSDownloadApk;
import cn.acooo.onecenter.core.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.core.model.AppInfo;
import cn.acooo.onecenter.server.App;
import cn.acooo.onecenter.server.R;
import cn.acooo.onecenter.server.ViewHolder.AppViewHolder;

public class MyAppListAdapter extends BaseAdapter {
	public static final String TAG = "ONE";
	private LayoutInflater mInflater;
	private List<AppInfo> appInfos = new ArrayList<AppInfo>();
    public void clearAppItem(){
        this.appInfos.clear();
    }
	public void addAppItem(AppInfo appInfo){
		if(appInfo != null){
			appInfos.add(appInfo);
		}
	}
	public void setApps(List<AppInfo> appInfos){
		this.appInfos = appInfos;
	}
	
	public MyAppListAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		AppViewHolder holder = null;
		if (convertView == null) {
			holder = new AppViewHolder();  
			convertView = mInflater.inflate(R.layout.app_item, null);
			holder.appIcon = (ImageView)convertView.findViewById(R.id.appIcon);
			holder.appName = (TextView)convertView.findViewById(R.id.appName);
			holder.appVersion = (TextView)convertView.findViewById(R.id.appVersion);
			holder.appLocalVersion = (TextView)convertView.findViewById(R.id.appLocalVersion);
			holder.appSize = (TextView)convertView.findViewById(R.id.appSize);
			holder.btn = (Button)convertView.findViewById(R.id.btn);
			convertView.setTag(holder);
		}else {
			holder = (AppViewHolder)convertView.getTag();
		}
		
		AppInfo ai = appInfos.get(position);
		holder.appIcon.setImageBitmap(ai.getAppIcon());
        Log.i(TAG,"w="+ai.getAppIcon().getWidth()+",h="+ai.getAppIcon().getHeight());
		holder.appName.setText(ai.getAppName());
		holder.appSize.setText(ai.getAppSize());
		holder.appVersion.setText(ai.getAppVersion());
		holder.appLocalVersion.setText(ai.getAppLocalVersion());
		holder.btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppInfo appInfo = appInfos.get(position);
				CSDownloadApk.Builder builder = CSDownloadApk.newBuilder();
				builder.setPackageName(appInfo.getPackageName());
				App.selectedPhoneClient.send(MessageType.MSG_ID_DOWNLOAD_APK, builder);



			}
		});
		
		
		return convertView;
	}
	/**
	 * listview中点击按键弹出对话框
	 */
	public void showInfo(Context context,int position){
		new AlertDialog.Builder(context)
		.setTitle("TIP")
		.setMessage(appInfos.get(position).getPackageName())
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}
    public interface CallBacks{
        public void addAppItem(AppInfo appInfo);
    }


}
