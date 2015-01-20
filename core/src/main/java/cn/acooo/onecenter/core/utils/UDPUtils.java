package cn.acooo.onecenter.core.utils;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import cn.acooo.onecenter.core.BaseActivity;
import cn.acooo.onecenter.core.auto.OneCenterProtos;

/**
 * Created by kthh on 15/1/16.
 */
public class UDPUtils {
    /**
     * udp的广播地址
     */
    public final static String BROADCAST_IP =  "224.0.0.1";

    /**
     * 如果ip传null，就是广播地址
     * @param type
     * @param ip
     * @param port
     */
    public static void send(OneCenterProtos.UDPType type,String ip,Integer port){

        if(ip == null){
            ip = BROADCAST_IP;
        }
        if(type == null || port == null){
            throw new IllegalArgumentException("type or port is null,udp send error");
        }

        //发送的数据包，局网内的所有地址都可以收到该数据包
        MulticastSocket ms = null;
        try {
            ms = new MulticastSocket();
            ms.setTimeToLive(4);
            OneCenterProtos.UDPMessage.Builder builder = OneCenterProtos.UDPMessage.newBuilder();
            builder.setType(type);
            Log.i(BaseActivity.TAG,"send message="+builder);
            byte[] data = builder.build().toByteArray();
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket dataPacket = new DatagramPacket(data, data.length, address,port);
            ms.send(dataPacket);
        } catch (Exception e) {
            Log.e("ONE","udp send error",e);
        }finally{
            if(ms != null){
                ms.close();
            }
        }
    }
}
