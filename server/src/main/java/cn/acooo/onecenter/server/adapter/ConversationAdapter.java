package cn.acooo.onecenter.server.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.model.ConversationInfo;
import cn.acooo.onecenter.core.utils.DateUtils;
import cn.acooo.onecenter.core.utils.MyContant;
import cn.acooo.onecenter.server.App;
import cn.acooo.onecenter.server.R;
import cn.acooo.onecenter.server.ViewHolder.ConversationHolder;

/**
 * Created by ly580914 on 15/1/15.
 */
public class ConversationAdapter extends MyBaseAdapter{
    private LayoutInflater mLayoutInflater;
    private List<ConversationInfo> datas = new ArrayList<ConversationInfo>();
    public void clearData(){
        this.datas.clear();
    }
    public void addConversation(ConversationInfo info){
        this.datas.add(info);
        this.notifyDataSetChanged();
    }
    public ConversationAdapter(Context context){
        mLayoutInflater = LayoutInflater.from(context);
    }

    public List<ConversationInfo> getDatas(){
        return datas;
    }
    public void setDatas(List<ConversationInfo> datas){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        ConversationHolder holder;
        if (convertView == null){

            holder = new ConversationHolder();
            view = mLayoutInflater.inflate(R.layout.conversation_item2,null);
            holder.tv_name = (TextView)convertView.findViewById(R.id.conversation_name);
            holder.tv_date = (TextView)convertView.findViewById(R.id.conversation_date);
            holder.tv_count = (TextView)convertView.findViewById(R.id.conversation_count);
            holder.tv_snippet = (TextView)convertView.findViewById(R.id.conversation_snippet);
            holder.bt_delete = (Button)convertView.findViewById(R.id.bt_delete);
            holder.bt_watch = (Button)convertView.findViewById(R.id.bt_watch);
            view.setTag(holder);
        }
        else{
            view =convertView;
            holder = (ConversationHolder)convertView.getTag();
        }
        final ConversationInfo info = datas.get(position);
        holder.tv_name.setText(info.getName());
        holder.tv_date.setText(DateUtils.formatDate(info.getDate()));
        holder.tv_count.setText(info.getCount()+"条信息");
        holder.tv_snippet.setText(info.getSnippet());
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneCenterProtos.CSDelete.Builder builder = OneCenterProtos.CSDelete.newBuilder();
                builder.setId(info.getId() + "");
                builder.setType(MyContant.DELETE_SMS);
                App.selectedPhoneClient.send(OneCenterProtos.MessageType.MSG_ID_DELETE, builder);
                AdapterAnimation.setAnimation(position,view,ConversationAdapter.this);
            }
        });
        holder.bt_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("llllllllllllllllllllllllllll", "查看点击了");
                OneCenterProtos.CSQuerySmsById.Builder builder = OneCenterProtos.CSQuerySmsById.newBuilder();
                builder.setId(info.getId());
                App.selectedPhoneClient.send(OneCenterProtos.MessageType.MSG_ID_QUERY_SMS, builder);
            }

        });
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OneCenterProtos.CSQuerySmsById.Builder builder = OneCenterProtos.CSQuerySmsById.newBuilder();
//                builder.setId(info.getId());
//                App.selectedPhoneClient.send(OneCenterProtos.MessageType.MSG_ID_QUERY_SMS,builder);
//            }
//        });
        return convertView;
    }
}
