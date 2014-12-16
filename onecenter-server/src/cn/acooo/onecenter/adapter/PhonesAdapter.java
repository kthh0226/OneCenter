package cn.acooo.onecenter.adapter;

import java.util.ArrayList;
import java.util.Enumeration;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.acooo.onecenter.App;
import cn.acooo.onecenter.R;
import cn.acooo.onecenter.ViewHolder.PhoneViewHolder;
import cn.acooo.onecenter.model.PhoneClient;
import cn.acooo.onecenter.server.ChannelManager;

public class PhonesAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<PhoneClient> phones;
	private int selectItem = -1;
	
	public void setSelectItem(int i){
		this.selectItem = i;
		App.selectedPhoneClient = phones.get(selectItem);
	}
	
	public void removeSelectedItem(){
		if(selectItem != -1){
			PhoneClient phoneClient = phones.remove(selectItem);
			if(phoneClient != null){
				phoneClient.disconnect();
			}
		}
	}
	
	public PhoneClient getSelectedPhoneClient(){
		return phones.get(selectItem);
	}
	
	
	public void reloadPhoneClient(){
		phones = new ArrayList<PhoneClient>();
		Enumeration<PhoneClient> senders = ChannelManager.getSenders();
		while(senders.hasMoreElements()){
			PhoneClient s = senders.nextElement();
			phones.add(s);
		}
	}

	public PhonesAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);
		this.reloadPhoneClient();
	}
	
	@Override
	public int getCount() {
		return phones.size();
	}

	@Override
	public Object getItem(int position) {
		return phones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhoneViewHolder holder = null;
		if (convertView == null) {
			holder = new PhoneViewHolder();  
			convertView = mInflater.inflate(R.layout.phone_item, null);
			holder.setTextView((TextView)convertView.findViewById(R.id.title));
			convertView.setTag(holder);
		}else {
			holder = (PhoneViewHolder)convertView.getTag();
		}
		
		holder.getTextView().setText(phones.get(position).getIp());
		if(position == selectItem){
			convertView.setBackgroundColor(Color.RED);  
		}else{
			convertView.setBackgroundColor(Color.TRANSPARENT);  
		}

		return convertView;
	}

}
