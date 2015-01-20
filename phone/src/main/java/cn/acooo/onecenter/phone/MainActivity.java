package cn.acooo.onecenter.phone;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.protobuf.ByteString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.acooo.onecenter.core.BaseActivity;
import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.core.auto.OneCenterProtos.SCQueryApps;
import cn.acooo.onecenter.core.model.ContactsInfo;
import cn.acooo.onecenter.core.model.ConversationInfo;
import cn.acooo.onecenter.core.model.SmsInfo;
import cn.acooo.onecenter.core.model.TalksInfo;
import cn.acooo.onecenter.core.utils.CommonsUtil;
import cn.acooo.onecenter.core.utils.ConversationUtils;
import cn.acooo.onecenter.core.utils.ImageUtil;
import cn.acooo.onecenter.core.utils.MyContant;
import cn.acooo.onecenter.core.utils.SmsUtils;
import cn.acooo.onecenter.core.utils.TalksUtils;
import cn.acooo.onecenter.phone.logic.AppLogic;
import cn.acooo.onecenter.phone.service.SocketService;
import cn.acooo.onecenter.phone.service.UDPService;

public class MainActivity extends BaseActivity {
	private EditText ip;
	private EditText port;
	private Intent socketServiceIntent;


    @Override
    protected void initHandler() {
        App.handler = myHandler;
    }

    Handler handler = new Handler(){
        OneCenterProtos.SCQueryContacts.Builder scQueryContacts = OneCenterProtos.SCQueryContacts.newBuilder();

        @Override
        public void handleMessage(Message msg) {
            ContactsInfo info = new ContactsInfo();
            OneCenterProtos.ContactsInfo.Builder contactsInfo = OneCenterProtos.ContactsInfo.newBuilder();
            info = (ContactsInfo)msg.obj;
            contactsInfo.setId(info.getId());
            contactsInfo.setName(info.getName());
            contactsInfo.setNumber(info.getNumber());
            contactsInfo.setType(info.getType());
            if(info.getType() != 0 && info.getIcon() != null){
                contactsInfo.setIcon(ByteString.copyFrom(ImageUtil.Bitmap2Bytes(info.getIcon())));
            }
            scQueryContacts.addInfos(contactsInfo);
            App.send(MessageType.MSG_ID_QUERY_CONTACTS,scQueryContacts);
        }

    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"into phone MainActivity onCreate=====");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button connectButton = (Button) findViewById(R.id.connect);

        if(App.phoneUdpServer == null || !App.phoneUdpServer.isRun()){
            Intent udpServiceIntent = new Intent(this,UDPService.class);
            startService(udpServiceIntent);
        }

		ip = (EditText)findViewById(R.id.ip);
		port = (EditText)findViewById(R.id.port);
		socketServiceIntent = new Intent(this,SocketService.class);

		connectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				socketServiceIntent.putExtra(SocketService.KEY_IP, ip.getText().toString());
				socketServiceIntent.putExtra(SocketService.KEY_PORT, Integer.parseInt(port.getText().toString()));
				Log.i(App.TAG, "startService..."+socketServiceIntent.getExtras());
				startService(socketServiceIntent);
			}
		});
		
		Button disconnectButton = (Button) findViewById(R.id.disconnect);
		disconnectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(App.TAG, "stopService..."+socketServiceIntent.getExtras());
				if(App.socketIsRun){
					stopService(socketServiceIntent);
				}else{
					myHandler.sendEmptyMessage(UI_MSG_ID_NOT_CONNECTED);
				}
			}
		});

        Button scanButton = (Button)findViewById(R.id.scan);
        scanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                App.phoneUdpServer.scanOneBoard();
            }
        });

        Button cancelScanButton = (Button)findViewById(R.id.cancelScan);
        cancelScanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                App.phoneUdpServer.cancelScanOneBoard();
            }
        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	@Override
	public Callback getActivityCallBack() {
		return new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				try{
                    Log.d(TAG,msg.toString());
					switch(msg.what){
                    case BaseActivity.UI_MSG_ID_NEW_ONEBOARD:{
                        ip.setText(App.phoneUdpServer.getRandomIP());
                        return true;
                        }
					case OneCenterProtos.MessageType.MSG_ID_APPS_VALUE:
						Log.i(TAG, "into UI_MSG_ID_NEW_PHONE handle message,msg="+msg);

						List<PackageInfo> ps = CommonsUtil.getAllApps(MainActivity.this);
						for(PackageInfo info : ps){
                            SCQueryApps.Builder builder = SCQueryApps.newBuilder();
							OneCenterProtos.AppDetail.Builder appInfoBuilder = OneCenterProtos.AppDetail.newBuilder();
							appInfoBuilder.setName(info.applicationInfo.loadLabel(  
						            getPackageManager()).toString());
							appInfoBuilder.setPackageName(info.packageName);
							appInfoBuilder.setVersion(info.versionName == null ? "":info.versionName);
							String publicSourceDir = info.applicationInfo.publicSourceDir;
                            appInfoBuilder.setPublicSourceDir(publicSourceDir);
							appInfoBuilder.setPackageSize(new File(publicSourceDir).length());
							Drawable icon = info.applicationInfo.loadIcon(getPackageManager());
							ByteString byteString = ByteString.copyFrom(ImageUtil.drawableToBytes(icon));
							appInfoBuilder.setIcon(byteString);
							builder.addApps(appInfoBuilder);
                            App.send(MessageType.MSG_ID_APPS,builder);
						}

						return true;
                    case MessageType.MSG_ID_QUERY_CONTACTS_VALUE:
                        List<ContactsInfo> contacts = AppLogic.getInstance().getAllContacts(MainActivity.this);

                        for(ContactsInfo info : contacts){
                            OneCenterProtos.SCQueryContacts.Builder scQueryContacts = OneCenterProtos.SCQueryContacts.newBuilder();
                            OneCenterProtos.ContactsInfo.Builder contactsInfo = OneCenterProtos.ContactsInfo.newBuilder();
                            contactsInfo.setId(info.getId());
                            contactsInfo.setName(info.getName());
                            contactsInfo.setNumber(info.getNumber());
                            contactsInfo.setType(info.getType());
                            if(info.getType() != 0 && info.getIcon() != null){
                                contactsInfo.setIcon(ByteString.copyFrom(ImageUtil.Bitmap2Bytes(info.getIcon())));
                            }
                            scQueryContacts.addInfos(contactsInfo);
                            App.send(MessageType.MSG_ID_QUERY_CONTACTS,scQueryContacts);
                        }

                        return  true;
                    case MessageType.MSG_ID_QUERY_CONVERSATION_VALUE:
                        List<ConversationInfo> conversationInfos = ConversationUtils.getAllConversation(MainActivity.this);

                        for (ConversationInfo info : conversationInfos){
                           OneCenterProtos.SCQueryConversation.Builder scqueryConv = OneCenterProtos.SCQueryConversation.newBuilder();
                           OneCenterProtos.ConversationInfo.Builder convinfo = OneCenterProtos.ConversationInfo.newBuilder();
                           convinfo.setId(info.getId());
                           convinfo.setDate(info.getDate());
                           convinfo.setName(info.getName());
                           convinfo.setCount(info.getCount());
                           convinfo.setSnippet(info.getSnippet());
                           convinfo.setRecipient(info.getRecipient());
                           scqueryConv.addConversation(convinfo);
                           App.send(MessageType.MSG_ID_QUERY_CONVERSATION,scqueryConv);
                        }
                        return true;
                    case MessageType.MSG_ID_QUERY_SMS_VALUE:
                        OneCenterProtos.CSQuerySmsById querySms = OneCenterProtos.CSQuerySmsById.parseFrom((byte[])msg.obj);
                        Log.i("llllllllllllllllllllllllllll","id:"+ querySms.getId() );
                        List<SmsInfo> smsInfos = SmsUtils.getSmsById(MainActivity.this, querySms.getId());
                        Log.i("llllllllllllllllllllllllllll","size" + smsInfos.size());
                         for (SmsInfo info : smsInfos){
                            OneCenterProtos.SCQuerySms.Builder scquerysms = OneCenterProtos.SCQuerySms.newBuilder();
                            OneCenterProtos.SmsInfo.Builder smsinfo = OneCenterProtos.SmsInfo.newBuilder();
                            smsinfo.setDate(info.getDate());
                            smsinfo.setAddress(info.getAddress());
                            smsinfo.setType(info.getType());
                            smsinfo.setBody(info.getBody());
                            smsinfo.setName(info.getName() == null?"无名":info.getName());
                            scquerysms.addInfos(smsinfo);
                            App.send(MessageType.MSG_ID_SHOW_MSG,scquerysms);
                          }

                            return true;

//                    case MessageType.MSG_ID_SHOW_MSG_VALUE:
//                        List<SmsInfo> smsInfos2 = SmsUtils.getSms(MainActivity.this);
//
//                        for (SmsInfo info : smsInfos2){
//                            OneCenterProtos.SCQuerySms.Builder scquerysms = OneCenterProtos.SCQuerySms.newBuilder();
//                            OneCenterProtos.SmsInfo.Builder smsinfo = OneCenterProtos.SmsInfo.newBuilder();
//                            smsinfo.setDate(info.getDate());
//                            smsinfo.setAddress(info.getAddress() == null ? "" : info.getAddress());
//                            smsinfo.setType(info.getType());
//                            smsinfo.setBody(info.getBody());
//                            scquerysms.addInfos(smsinfo);
//                            App.send(MessageType.MSG_ID_SHOW_MSG,scquerysms);
//                        }

//                        return true;
                    case MessageType.MSG_ID_QUERY_TALKS_VALUE:
                        List<TalksInfo> talksInfos = TalksUtils.getTalksInfo(MainActivity.this);
                        for (TalksInfo info : talksInfos){
                            OneCenterProtos.SCQueryTalks.Builder scquerytalks = OneCenterProtos.SCQueryTalks.newBuilder();
                            OneCenterProtos.TalksInfo.Builder talksInfo = OneCenterProtos.TalksInfo.newBuilder();
                            talksInfo.setDate(info.getDate());
                            talksInfo.setType(info.getType());
                            talksInfo.setNumber(info.getNumber());
                            talksInfo.setDuration(info.getDuration());
                            talksInfo.setName(info.getName() == null ? "" : info.getName());
                            scquerytalks.addTalks(talksInfo);
                            App.send(MessageType.MSG_ID_QUERY_TALKS,scquerytalks);
                        }

                        return true;
                    case MessageType.MSG_ID_CALL_VALUE:
                        OneCenterProtos.CSCallPhone csCallPhone = OneCenterProtos.CSCallPhone.parseFrom((byte[])msg.obj);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + csCallPhone.getNumber()));
                        MainActivity.this.startActivity(intent);
                        return true;

                    case MessageType.MSG_ID_SEND_VALUE:
                        OneCenterProtos.CSSendSms sendSms = OneCenterProtos.CSSendSms.parseFrom((byte[])msg.obj);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(sendSms.getNumber(),null, sendSms.getContent(), null, null);
                        return true;

                    case MessageType.MSG_ID_DELETE_VALUE:
                        OneCenterProtos.CSDelete delete = OneCenterProtos.CSDelete.parseFrom((byte[])msg.obj);
                        switch (delete.getType()){
                            case MyContant.DELETE_CONTACT:
                                delete(Uri.parse(MyContant.URI_CANTACTS),delete.getId(),delete.getType());
                                return true;
                            case MyContant.DELETE_SMS:
                                delete(Uri.parse(MyContant.URI_SMS),delete.getId(),delete.getType());
                                return true;
                            case MyContant.DELETE_LOG:
                                delete(Uri.parse(MyContant.URI_LOG),delete.getId(),delete.getType());
                                return true;
                            default:
                                throw new RuntimeException("删除类型匹配不正确" + delete.getType());
                        }
					}
				}catch(Exception e){
					Log.e(TAG, "MainActivityHandler error", e);
					return true;
				}
				return false;
			}
            };}
    private void delete(Uri uri, String id, int type){
        ContentResolver cr = MainActivity.this.getContentResolver();
        int i = cr.delete(uri,"_id=?",new String[]{id});
        if (i > 0){
            OneCenterProtos.SCDelete.Builder builder = OneCenterProtos.SCDelete.newBuilder();
            builder.setId(id);
            builder.setType(type);
            App.send(MessageType.MSG_ID_DELETE,builder);
        }
	}
}
