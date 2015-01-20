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
import cn.acooo.onecenter.core.model.TalksInfo;
import cn.acooo.onecenter.core.utils.MyContant;
import cn.acooo.onecenter.server.App;
import cn.acooo.onecenter.server.R;
import cn.acooo.onecenter.server.ViewHolder.TalksViewHolder;

/**
 * Created by ly580914 on 15/1/15.
 */
public class TalksAdapter extends BaseAdapter{
    private LayoutInflater mLayoutInflater;
    private List<TalksInfo> datas = new ArrayList<TalksInfo>();
    public void clearData(){
        this.datas.clear();
    }
    public void addTalks(TalksInfo info){
        this.datas.add(info);
        this.notifyDataSetChanged();
    }
    public List<TalksInfo> getDatas(){
        return datas;
    }
    public void setDatas(List<TalksInfo> datas){
        this.datas = datas;
    }
    public TalksAdapter(Context context){
        mLayoutInflater = LayoutInflater.from(context);
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
        TalksViewHolder holder;
        if (convertView == null){
            holder = new TalksViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.talks_item,null);
            holder.tv_number = (TextView)convertView.findViewById(R.id.talks_number);
            holder.tv_name = (TextView)convertView.findViewById(R.id.talks_name);
            holder.tv_date = (TextView)convertView.findViewById(R.id.talks_date);
            holder.tv_type = (TextView)convertView.findViewById(R.id.talks_type);
            holder.tv_duration = (TextView)convertView.findViewById(R.id.talks_duration);
            holder.bt_delete = (Button)convertView.findViewById(R.id.bt_delete);
            convertView.setTag(holder);
        }
        else{
            holder = (TalksViewHolder)convertView.getTag();
        }
        final TalksInfo info = datas.get(position);
        holder.tv_number.setText(info.getNumber());
        holder.tv_date.setText(info.getDate()+"");
        holder.tv_type.setText(info.getType()+"");
        holder.tv_duration.setText(info.getDuration()+"");
        holder.tv_name.setText(info.getName() == null ? info.getName():"");
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneCenterProtos.CSDelete.Builder delete = OneCenterProtos.CSDelete.newBuilder();
                delete.setId(info.getId()+"");
                delete.setType(MyContant.DELETE_LOG);
                App.selectedPhoneClient.send(OneCenterProtos.MessageType.MSG_ID_DELETE,delete);
            }
        });
        return convertView;
    }
}
