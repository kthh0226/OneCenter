package cn.acooo.onecenter.server.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.acooo.onecenter.core.model.ContactsInfo;
import cn.acooo.onecenter.server.R;
import cn.acooo.onecenter.server.ViewHolder.ContactsViewHolder;

/**
 * Created by kthh on 14/12/30.
 */
public class ContactsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ContactsInfo> datas = new ArrayList<>();

    public void clearData(){
        this.datas.clear();
    }
    public void addContacts(ContactsInfo info){
        this.datas.add(info);
    }
    public ContactsAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
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
            convertView.setTag(holder);
        }else {
            holder = (ContactsViewHolder)convertView.getTag();
        }

        ContactsInfo ai = datas.get(position);
        holder.icon.setImageBitmap(ai.getIcon());
        Log.i("one","ai====="+ai);
        holder.type.setText(ai.getType()== 0 ? "sim卡":"手机");
        holder.number.setText(ai.getNumber().toString());
        holder.name.setText(ai.getName());

        return convertView;
    }
}
