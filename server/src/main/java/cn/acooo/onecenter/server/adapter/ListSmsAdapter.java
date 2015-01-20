package cn.acooo.onecenter.server.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.acooo.onecenter.core.model.SmsInfo;
import cn.acooo.onecenter.core.utils.DateUtils;
import cn.acooo.onecenter.server.R;
import cn.acooo.onecenter.server.ViewHolder.ConversationItemHolder;

/**
 * Created by ly580914 on 15/1/19.
 */
public class ListSmsAdapter extends BaseAdapter{
    private List<SmsInfo> datas;
    private Context context;
    public ListSmsAdapter(Context context){
        this.context = context;
    }
    public void setDatas(List<SmsInfo> datas){
        this.datas = datas;
    }

    public void addSms(SmsInfo info){
        datas.add(info);
        notifyDataSetChanged();
    }
    public void clearDatas(){
        datas.clear();
    }
    public List<SmsInfo> getDatas(){
        return datas;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public SmsInfo getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConversationItemHolder holder;
        if (convertView == null){
            holder = new ConversationItemHolder();
            convertView = View.inflate(context, R.layout.listview_item, null);
            holder.ll_left = (LinearLayout)convertView.findViewById(R.id.ll_left);
            holder.ll_right = (LinearLayout)convertView.findViewById(R.id.ll_right);
            holder.tv_left = (TextView)convertView.findViewById(R.id.tv_left);
            holder.tv_right = (TextView)convertView.findViewById(R.id.tv_right);
            holder.tv_date_left = (TextView)convertView.findViewById(R.id.tv_date_left);
            holder.tv_date_right = (TextView)convertView.findViewById(R.id.tv_date_right);
            holder.tv_error = (TextView)convertView.findViewById(R.id.tv_error);
            convertView.setTag(holder);
        }else{
            holder = (ConversationItemHolder)convertView.getTag();
        }
        SmsInfo info = datas.get(position);
        if (info.getType() == 1){
            holder.ll_left.setVisibility(View.VISIBLE);
            holder.ll_right.setVisibility(View.GONE);
            holder.tv_left.setText(info.getBody());
            holder.tv_date_left.setText(DateUtils.formatDate(info.getDate()));
            holder.tv_error.setVisibility(View.VISIBLE);
        }else if (info.getType() == 2){
            holder.ll_left.setVisibility(View.GONE);
            holder.ll_right.setVisibility(View.VISIBLE);
            holder.tv_right.setText(info.getBody());
            holder.tv_date_right.setText(DateUtils.formatDate(info.getDate()));
            if (info.isError()){
                holder.tv_error.setVisibility(View.VISIBLE);
            }
        }else{
            holder.ll_left.setVisibility(View.GONE);
            holder.ll_right.setVisibility(View.GONE);
        }

        return convertView;
    }
}
