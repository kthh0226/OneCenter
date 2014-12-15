package cn.acooo.onecenter.adapter;

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
import cn.acooo.onecenter.R;
import cn.acooo.onecenter.ViewHolder.AppViewHolder;
import cn.acooo.onecenter.model.AppItem;

public class MyAppListAdapter extends BaseAdapter {
	public static final String TAG = "MyAppListAdapter";
	private LayoutInflater mInflater;
	private List<AppItem> appItems = new ArrayList<AppItem>();

	private void initTestData(){
		for(int i =0;i<10;i++){
			AppItem appItem = new AppItem();
			appItem.setDes("desc~~~~~"+i);
			appItem.setName("name====+"+i);
			appItem.setIcon(R.drawable.i1);
			appItems.add(appItem);
		}
	}
	
	public MyAppListAdapter(Context context) {
		this.initTestData();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		
		AppViewHolder holder = null;
		if (convertView == null) {
			holder = new AppViewHolder();  
			convertView = mInflater.inflate(R.layout.app_item, null);
			holder.icon = (ImageView)convertView.findViewById(R.id.img);
			holder.name = (TextView)convertView.findViewById(R.id.title);
			holder.des = (TextView)convertView.findViewById(R.id.info);
			holder.viewBtn = (Button)convertView.findViewById(R.id.view_btn);
			convertView.setTag(holder);
		}else {
			holder = (AppViewHolder)convertView.getTag();
		}
		
		
		holder.icon.setBackgroundResource((Integer)appItems.get(position).getIcon());
		holder.name.setText((String)appItems.get(position).getName());
		holder.des.setText((String)appItems.get(position).getDes());
		
		holder.viewBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInfo(v.getContext());					
			}
		});
		
		
		return convertView;
	}
	/**
	 * listview中点击按键弹出对话框
	 */
	public void showInfo(Context context){
		new AlertDialog.Builder(context)
		.setTitle("我的listview")
		.setMessage("介绍...")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
	}
}
