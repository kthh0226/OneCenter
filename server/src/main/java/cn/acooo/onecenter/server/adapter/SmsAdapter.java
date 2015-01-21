package cn.acooo.onecenter.server.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.model.ContactsInfo;
import cn.acooo.onecenter.core.model.SmsInfo;
import cn.acooo.onecenter.core.utils.MyContant;
import cn.acooo.onecenter.server.App;
import cn.acooo.onecenter.server.R;
import cn.acooo.onecenter.server.ViewHolder.SmsViewHolder;

/**
 * Created by ly580914 on 15/1/15.
 */
public class SmsAdapter extends MyBaseAdapter{
    private LayoutInflater mLayoutInflater;
    private List<SmsInfo> datas = new ArrayList<SmsInfo>();
    public void clearData(){
        this.datas.clear();
    }
    public void addSms(SmsInfo info){
        this.datas.add(info);
        this.notifyDataSetChanged();
    }
    public SmsAdapter(Context context){
        mLayoutInflater = LayoutInflater.from(context);
    }

    public List<SmsInfo> getDatas(){
        return datas;
    }
    public void setDatas(List<SmsInfo> datas){
        this.datas = datas;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SmsViewHolder holder;
        if (convertView == null){
            holder = new SmsViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.sms_item,null);
            holder.tv_address = (TextView)convertView.findViewById(R.id.sms_address);
            holder.tv_body = (TextView)convertView.findViewById(R.id.sms_body);
            holder.tv_date = (TextView)convertView.findViewById(R.id.sms_date);
            holder.tv_type = (TextView)convertView.findViewById(R.id.sms_type);
            holder.bt_delete = (Button)convertView.findViewById(R.id.bt_delete);
            convertView.setTag(holder);
        }
        else{
            holder = (SmsViewHolder)convertView.getTag();
        }
        final SmsInfo info = datas.get(position);
        holder.tv_address.setText(info.getAddress());
        holder.tv_date.setText(info.getDate()+"");
        holder.tv_type.setText(info.getType()+"");
        holder.tv_body.setText(info.getBody());
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneCenterProtos.CSDelete.Builder builder = OneCenterProtos.CSDelete.newBuilder();
                builder.setId(info.getId()+"");
                builder.setType(MyContant.DELETE_SMS);
                App.selectedPhoneClient.send(OneCenterProtos.MessageType.MSG_ID_DELETE,builder);
            }
        });
        return convertView;
    }
}
