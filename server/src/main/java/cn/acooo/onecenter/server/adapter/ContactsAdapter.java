package cn.acooo.onecenter.server.adapter;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.Descriptors;

import java.util.ArrayList;
import java.util.List;

import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.model.ContactsInfo;
import cn.acooo.onecenter.core.utils.MyContant;
import cn.acooo.onecenter.server.App;
import cn.acooo.onecenter.server.MyPhoneActivity;
import cn.acooo.onecenter.server.R;
import cn.acooo.onecenter.server.ViewHolder.ContactsViewHolder;

/**
 * Created by kthh on 14/12/30.
 */
public class ContactsAdapter extends BaseAdapter {
    final MyPhoneActivity context;

    private LayoutInflater mInflater;
    private List<ContactsInfo> datas = new ArrayList<>();

    public void clearData(){
        this.datas.clear();
    }
    public void addContacts(ContactsInfo info){
        this.datas.add(info);
        this.notifyDataSetChanged();
    }
    public ContactsAdapter(Context context){
        this.context = (MyPhoneActivity)context;
        this.mInflater = LayoutInflater.from(context);
    }
    public List<ContactsInfo> getDatas(){
        return datas;
    }
    public void setDatas(List<ContactsInfo> datas){
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
        return datas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactsViewHolder holder = null;
        if (convertView == null) {
            holder = new ContactsViewHolder();
            convertView = mInflater.inflate(R.layout.contacts_item, null);
            holder.icon = (ImageView)convertView.findViewById(R.id.contacts_icon);
            holder.name = (TextView)convertView.findViewById(R.id.contacts_name);
            holder.number = (TextView)convertView.findViewById(R.id.contacts_number);
            holder.type = (TextView)convertView.findViewById(R.id.contacts_type);
            holder.bt_call = (Button)convertView.findViewById(R.id.bt_call);
            holder.bt_send = (Button)convertView.findViewById(R.id.bt_send_sms);
            holder.bt_delete = (Button)convertView.findViewById(R.id.bt_delete);
            convertView.setTag(holder);
        }else {
            holder = (ContactsViewHolder)convertView.getTag();
        }

        final ContactsInfo ai = datas.get(position);
        holder.icon.setImageBitmap(ai.getIcon());
        Log.i("one","ai====="+ai);
        holder.type.setText(ai.getType()== 0 ? "sim卡":"手机");
        holder.number.setText(ai.getNumber().toString());
        holder.name.setText(ai.getName());
        holder.bt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneCenterProtos.CSCallPhone.Builder builder= OneCenterProtos.CSCallPhone.newBuilder();
                builder.setNumber(ai.getNumber().toString());
                App.selectedPhoneClient.send(OneCenterProtos.MessageType.MSG_ID_CALL,builder);
            }
        });
        holder.bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               context.showConversationWindow(ai.getName(),ai.getNumber());
            }
        });
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneCenterProtos.CSDelete.Builder delete = OneCenterProtos.CSDelete.newBuilder();
                delete.setId(ai.getId()+"");
                delete.setType(MyContant.DELETE_CONTACT);
                App.selectedPhoneClient.send(OneCenterProtos.MessageType.MSG_ID_DELETE, delete);
            }
        });
        return convertView;
    }
}
