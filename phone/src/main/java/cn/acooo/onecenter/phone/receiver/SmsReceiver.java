package cn.acooo.onecenter.phone.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.utils.MyContant;
import cn.acooo.onecenter.phone.App;

/**
 * Created by kthh on 15/1/8.
 */
public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MyContant.SMS_SEND_ACTION)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    break;
                default:
                    Object[] objs = (Object[]) intent.getExtras().get("pdus");
                    for (Object obj : objs) {
                        SmsMessage msg = SmsMessage.createFromPdu((byte[]) obj);
                        String address = msg.getOriginatingAddress();
                        String body = msg.getMessageBody();
                        OneCenterProtos.CSErrorSms.Builder builder = OneCenterProtos.CSErrorSms.newBuilder();
                        OneCenterProtos.SmsInfo.Builder smsInfo = OneCenterProtos.SmsInfo.newBuilder();
                        smsInfo.setBody(body);
                        smsInfo.setAddress(address);
                        builder.addInfo(smsInfo);
                        App.send(OneCenterProtos.MessageType.MSG_ID_SMS_ERROE, builder);
                    }
                    break;
            }
        }else if (intent.getAction().equals(MyContant.SMS_RECEIVED)){
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage msg = SmsMessage.createFromPdu((byte[]) obj);
                String address = msg.getOriginatingAddress();
                String body = msg.getMessageBody();
                OneCenterProtos.CSNewSms.Builder builder = OneCenterProtos.CSNewSms.newBuilder();
                OneCenterProtos.SmsInfo.Builder smsInfo = OneCenterProtos.SmsInfo.newBuilder();
                smsInfo.setBody(body);
                smsInfo.setAddress(address);
                smsInfo.setType(1);
                smsInfo.setDate(System.currentTimeMillis());
                builder.addInfo(smsInfo);
                App.send(OneCenterProtos.MessageType.MSG_ID_NEW_SMS, builder);
            }
        }

    }
}
