package cn.acooo.onecenter.server.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.acooo.onecenter.auto.OneCenterProtos.CSDownloadApk;
import cn.acooo.onecenter.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.server.App;
import cn.acooo.onecenter.server.R;
import cn.acooo.onecenter.server.ViewHolder.AppViewHolder;
import cn.acooo.onecenter.server.model.AppItem;

public class MyAppListAdapter extends BaseAdapter {
	public static final String TAG = "MyAppListAdapter";
	private LayoutInflater mInflater;
	private List<AppItem> appItems = new ArrayList<AppItem>();
	public void addAppItem(AppItem appItem){
		if(appItem != null){
			appItems.add(appItem);
		}
	}
	public void setApps(List<AppItem> appItems){
		this.appItems = appItems;
	}
	
	public MyAppListAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appItems.size();
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
		
		AppItem ai = appItems.get(position);
		holder.appIcon.setImageBitmap(ai.getAppIcon());
		holder.appName.setText(ai.getAppName());
		holder.appSize.setText(ai.getAppSize());
		holder.appVersion.setText(ai.getAppVersion());
		holder.appLocalVersion.setText(ai.getAppLocalVersion());
		holder.btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppItem appItem = appItems.get(position);
				CSDownloadApk.Builder builder = CSDownloadApk.newBuilder();
				builder.setPackageName(appItem.getPackageName());
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
		.setMessage(appItems.get(position).getPackageName())
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}
}
