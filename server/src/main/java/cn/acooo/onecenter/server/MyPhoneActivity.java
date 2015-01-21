package cn.acooo.onecenter.server;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.acooo.onecenter.core.BaseActivity;
import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.core.auto.OneCenterProtos.SCQueryApps;
import cn.acooo.onecenter.core.auto.OneCenterProtos.SCQueryContacts;
import cn.acooo.onecenter.core.model.ContactsInfo;
import cn.acooo.onecenter.core.model.ConversationInfo;
import cn.acooo.onecenter.core.model.SmsInfo;
import cn.acooo.onecenter.core.model.TalksInfo;
import cn.acooo.onecenter.core.utils.CommonsUtil;
import cn.acooo.onecenter.core.utils.ConversationUtils;
import cn.acooo.onecenter.core.utils.MyContant;
import cn.acooo.onecenter.server.ViewHolder.EmptyFragment;
import cn.acooo.onecenter.server.ViewHolder.LoadingFragment;
import cn.acooo.onecenter.server.adapter.ContactsAdapter;
import cn.acooo.onecenter.server.adapter.ConversationAdapter;
import cn.acooo.onecenter.server.adapter.ListSmsAdapter;
import cn.acooo.onecenter.server.adapter.MyAppListAdapter;
import cn.acooo.onecenter.server.adapter.SmsAdapter;
import cn.acooo.onecenter.server.adapter.TalksAdapter;
import cn.acooo.onecenter.server.model.PhoneMenus;

public class MyPhoneActivity extends BaseActivity implements MyPhoneFeatureListFragment.Callbacks{
	
	private AppDetailListFragment appDetail;
    private ContactsDetailListFragment contactsDetail;
    private SmsListFragment smsDetail;
    private TalksListFragment talksDetail;
    private ConversationListFragment conversationDetail;
    private ListView lv;
    private ListSmsAdapter listSmsAdapter;
    private boolean isShowConversation = false;
    private PopupWindow window;
    private TextView tv_sms;
    private Map<Integer, SmsInfo> cacheSms;
    private Fragment page_loading;
    private Fragment page_empty;

    private MyPhoneFeatureListFragment ff;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
        ff = (MyPhoneFeatureListFragment)(getFragmentManager().findFragmentById(R.id.phone_feature_list));
        ff.setActivateOnItemClick(true);
        ff.getListView().setItemChecked(PhoneMenus.APPS.ordinal(),true);
        onItemSelected(PhoneMenus.APPS);
        page_loading = new LoadingFragment();
        page_empty = new EmptyFragment();
	}

	@Override
	public Callback getActivityCallBack() {
		return new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				try{
					switch(msg.what){
					case MessageType.MSG_ID_APPS_VALUE:{
                        MyAppListAdapter myAppListAdapter = appDetail.getMyAppListAdapter();
                        SCQueryApps builder = SCQueryApps.parseFrom((byte[])msg.obj);
                        Map<String,PackageInfo> ms = CommonsUtil.getAllAppsByMap(MyPhoneActivity.this);
                        for(OneCenterProtos.AppDetail app : builder.getAppsList()){
                            myAppListAdapter.addAppItem(app,ms.get(app.getPackageName()));
                        }
                        getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, appDetail).commit();
                        return true;
                    }
                    case MessageType.MSG_ID_QUERY_CONTACTS_VALUE:
                        ContactsAdapter contactsAdapter = (ContactsAdapter)contactsDetail.getListAdapter();
                        SCQueryContacts scQueryContacts = SCQueryContacts.parseFrom((byte[])msg.obj);
                        if (scQueryContacts.getInfosList().size() == 0){
                            getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, page_empty).commit();
                        }else{
                            for(OneCenterProtos.ContactsInfo info : scQueryContacts.getInfosList()){
                                contactsAdapter.addContacts(new ContactsInfo(info));
                            }
                            getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, contactsDetail).commit();
                        }
                        return true;
                    case MessageType.MSG_ID_QUERY_CONVERSATION_VALUE:
                         ConversationAdapter conversationAdapter = (ConversationAdapter)conversationDetail.getListAdapter();
                         OneCenterProtos.SCQueryConversation scQueryConversation =OneCenterProtos.SCQueryConversation.parseFrom((byte[])msg.obj);
                        if (scQueryConversation.getConversationList().size() == 0){
                            getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, page_empty).commit();
                        }else{
                            for(OneCenterProtos.ConversationInfo info : scQueryConversation.getConversationList()){
                                conversationAdapter.addConversation(new ConversationInfo(info));
                            }
                            getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, conversationDetail).commit();
                        }
                        return true;
                    case MessageType.MSG_ID_SHOW_MSG_VALUE:
                        OneCenterProtos.SCQuerySms scQuerySms = OneCenterProtos.SCQuerySms.parseFrom((byte[])msg.obj);
                        Log.i("llllllllllllllllllllllllllll", "服务端size:" + scQuerySms.getInfosList().size());
                        for(OneCenterProtos.SmsInfo info : scQuerySms.getInfosList()){
                            SmsInfo smsInfo = new SmsInfo(info);
                            if (!isShowConversation){
                                isShowConversation = true;
                                showConversationWindow(smsInfo.getName(), smsInfo.getAddress());
                            }
                            listSmsAdapter.addSms(smsInfo);
                        }
                        return true;
                    case MessageType.MSG_ID_QUERY_TALKS_VALUE:
                        TalksAdapter talksAdapter = (TalksAdapter)talksDetail.getListAdapter();
                        OneCenterProtos.SCQueryTalks scQueryTalks = OneCenterProtos.SCQueryTalks.parseFrom((byte[])msg.obj);
                        if (scQueryTalks.getTalksList().size() == 0){
                            getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, page_empty).commit();
                        }else{
                            for(OneCenterProtos.TalksInfo info : scQueryTalks.getTalksList()){
                                talksAdapter.addTalks(new TalksInfo(info));
                            }
                            getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, talksDetail).commit();
                        }
                        return true;
                    case MessageType.MSG_ID_SMS_ERROE_VALUE:
                        if (isShowConversation){
                            OneCenterProtos.CSErrorSms errorSms = OneCenterProtos.CSErrorSms.parseFrom((byte[])msg.obj);
                            OneCenterProtos.SmsInfo smsInfo = errorSms.getInfo(0);
                            Set<Map.Entry<Integer,SmsInfo>> set = cacheSms.entrySet();
                            Iterator<Map.Entry<Integer,SmsInfo>> it = set.iterator();
                            while (it.hasNext()){
                                Map.Entry<Integer,SmsInfo> info = it.next();
                                if (info.getValue().getAddress().equals(smsInfo.getAddress()) && info.getValue().getBody().equals(smsInfo.getBody())){
                                    listSmsAdapter.getItem(info.getKey()).setError(true);
                                    listSmsAdapter.notifyDataSetChanged();
                                    cacheSms.remove(info.getKey());
                                    break;
                                }
                            }
                        }
                        return true;
                    case MessageType.MSG_ID_NEW_SMS_VALUE:
                        if(isShowConversation){
                            OneCenterProtos.CSNewSms newSms = OneCenterProtos.CSNewSms.parseFrom((byte[])msg.obj);
                            OneCenterProtos.SmsInfo newInfo = newSms.getInfo(0);
                            listSmsAdapter.addSms(new SmsInfo(newInfo));
                        }
                        return true;

                    case MessageType.MSG_ID_DELETE_VALUE:
                        OneCenterProtos.SCDelete delete = OneCenterProtos.SCDelete.parseFrom((byte[])msg.obj);
                        switch (delete.getType()){
                            case MyContant.DELETE_CONTACT:
                                ContactsAdapter cAdapter = (ContactsAdapter)contactsDetail.getListAdapter();
                                List<ContactsInfo> contacts= cAdapter.getDatas();
                                Iterator<ContactsInfo> cit = contacts.iterator();
                                while(cit.hasNext()){
                                    ContactsInfo info = cit.next();
                                    if(delete.getId().equals(info.getNumber())){
                                        cit.remove();
                                        break;
                                    }
                                }
                                Log.i(TAG,contacts.toString());
                                cAdapter.notifyDataSetChanged();
                                return true;
                            case MyContant.DELETE_SMS:
                                SmsAdapter sAdapter = (SmsAdapter)smsDetail.getListAdapter();
                                List<SmsInfo> smsInfos = sAdapter.getDatas();
                                Iterator<SmsInfo> sit = smsInfos.iterator();
                                while(sit.hasNext()){
                                    SmsInfo info = sit.next();
                                    if(delete.getId().equals(info.getAddress())){
                                        sit.remove();
                                        break;
                                    }
                                }
                                sAdapter.notifyDataSetChanged();
                                return true;
                            case MyContant.DELETE_LOG:
                                TalksAdapter tAdapter = (TalksAdapter)talksDetail.getListAdapter();
                                List<TalksInfo> talksInfos= tAdapter.getDatas();
                                Iterator<TalksInfo> tit = talksInfos.iterator();
                                while(tit.hasNext()){
                                    TalksInfo info = tit.next();
                                    if(delete.getId().equals(info.getNumber())){
                                        tit.remove();
                                        break;
                                    }
                                }
                                tAdapter.notifyDataSetChanged();
                                return true;
                            default:
                                throw new RuntimeException("删除类型匹配不正确" + delete.getType());
                        }
					case UI_MSG_ID_NEW_PHONE:
						Log.i(TAG, "into UI_MSG_ID_NEW_PHONE handle message,msg="+msg);
						Intent intent = new Intent(MyPhoneActivity.this,IndexActivity.class);
						startActivity(intent);
						return true;
					}
				}catch(Exception e){
					Log.e(TAG, "handler error,msg="+msg, e);
					return true;
				}
				return false;
			}
		};
	}

    @Override
    protected void initHandler() {
        App.handler = myHandler;
    }

    @Override
    public void onItemSelected(PhoneMenus phones) {
        Log.i(TAG,"onItemSelected,id========="+phones);
        switch (phones){
            case APPS:
                if(appDetail == null){
                    appDetail = new AppDetailListFragment();
                    App.selectedPhoneClient.send(MessageType.MSG_ID_APPS, OneCenterProtos.CSQueryApps.newBuilder());
                    getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, page_loading).commit();
                }else {
                    getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, appDetail).commit();
                }
                break;
            case CONTACTS:
                if (contactsDetail == null){
                    contactsDetail = new ContactsDetailListFragment();
                    App.selectedPhoneClient.send(MessageType.MSG_ID_QUERY_CONTACTS,OneCenterProtos.CSQueryContacts.newBuilder());
                }else{
                    getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, contactsDetail).commit();
                }
                break;
            case TALKS:
                if (talksDetail == null){
                    talksDetail= new TalksListFragment();
                    App.selectedPhoneClient.send(MessageType.MSG_ID_QUERY_TALKS,OneCenterProtos.CSQueryTalks.newBuilder());
                }else{
                    getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, talksDetail).commit();
                }
                break;
            case MESSAGE:
                if (conversationDetail == null){
                    conversationDetail = new ConversationListFragment();
                    App.selectedPhoneClient.send(MessageType.MSG_ID_QUERY_CONVERSATION,OneCenterProtos.CSQueryConversation.newBuilder());
                }else{
                    getFragmentManager().beginTransaction().replace(R.id.phone_feature_detail_container, conversationDetail).commit();
                }
                break;
            default:
                Log.e(TAG,"MyPhoneContent error,MyPhoneContent="+phones);
        }
    }
    public void showConversationWindow(String name, String number){
        Log.i("llllllllllllllllllllllllllll", "进入window");
        View contentView = View.inflate(this,R.layout.conversation,null);
        TextView tv_title = (TextView)contentView.findViewById(R.id.tv_title);
        Button bt_close = (Button)contentView.findViewById(R.id.bt_call);
        lv = (ListView)contentView.findViewById(R.id.lv);
        tv_sms = (TextView)contentView.findViewById(R.id.tv_sms);
        Button bt_send = (Button)contentView.findViewById(R.id.bt_send_sms);

        tv_title.setText(name);
        final String address = number;

        listSmsAdapter = new ListSmsAdapter(this);
        lv.setAdapter(listSmsAdapter);

        Log.i("llllllllllllllllllllllllllll", "new window");
        window = new PopupWindow(contentView, -2, -2);
        window.showAtLocation(conversationDetail.getListView(), Gravity.TOP + Gravity.LEFT  ,200,100);

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissWindow();
            }
        });
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsBody = tv_sms.getText().toString();
                if (TextUtils.isEmpty(smsBody)){
                    Toast.makeText(MyPhoneActivity.this,"短息内容不能为空",Toast.LENGTH_SHORT);
                    return;
                }
                SmsManager smsManager = SmsManager.getDefault();
                List<String> messages = smsManager.divideMessage(smsBody);
                for (String message : messages){
                    OneCenterProtos.CSSendSms.Builder builder = OneCenterProtos.CSSendSms.newBuilder();
                    builder.setContent(message);
                    builder.setNumber(address);
                    App.selectedPhoneClient.send(MessageType.MSG_ID_SEND,builder);

                    SmsInfo info = new SmsInfo();
                    info.setAddress(address);
                    info.setBody(smsBody);
                    info.setType(2);
                    info.setDate(System.currentTimeMillis());

                    cacheSms.put(listSmsAdapter.getDatas().size(), info);
                    listSmsAdapter.addSms(info);
                }

            }
        });

    };
    private void dismissWindow(){
        window.dismiss();
        cacheSms.clear();
        listSmsAdapter.clearDatas();
        isShowConversation = false;
    }
}
