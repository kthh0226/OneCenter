package cn.acooo.onecenter.core.server;

import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import cn.acooo.onecenter.core.BaseActivity;
import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.utils.UDPUtils;

/**
 * Created by kthh on 15/1/15.
 * 目前实现了广播发送ip,以后有时间的时候优化成Upnp协议的数据包
 */
public class UDPServer implements Runnable{
    private MulticastSocket ms;
    private boolean isRun = true;
    public void setIsRun(boolean v){
        this.isRun = v;
    }
    private int port = 9998;

    public UDPServer(int port){
        this.port = port;
    }
    public UDPServer(){

    }

    @Override
    public void run() {
        try{
            ms = new MulticastSocket(port);
            InetAddress receiveAddress = InetAddress.getByName(UDPUtils.BROADCAST_IP);
            ms.joinGroup(receiveAddress);
            byte[] buf = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buf,1024);
            Log.i(BaseActivity.TAG,"udpServer is started...");
            while (isRun){
                ms.receive(dp);
                try{
                    OneCenterProtos.UDPMessage message = OneCenterProtos.UDPMessage.parseFrom(dp.getData());
                    switch (message.getType()){
                        case SEARCH_ONEBOARD:{
                            String clientIp = dp.getAddress().getHostAddress();
                            dp.setAddress(InetAddress.getByName(clientIp));
                            OneCenterProtos.UDPMessage.Builder builder = OneCenterProtos.UDPMessage.newBuilder();
                            builder.setType(OneCenterProtos.UDPType.IS_ONEBOARD);
                            dp.setData(builder.build().toByteArray());
                            ms.send(dp);
                            break;
                        }
                        case IS_ONEBOARD:{
                            Log.i(BaseActivity.TAG,"scan new oneBoard,ip="+dp.getAddress());
                            break;
                        }
                        case IS_PHONE:{
                            break;
                        }
                    }
                }catch (InvalidProtocolBufferException e){
                    Log.d(BaseActivity.TAG,"invalid package");
                    continue;
                }
            }
        }catch (Exception e){
            Log.e(BaseActivity.TAG,"",e);
        }finally {
            ms.close();
            Log.i(BaseActivity.TAG,"udpServer is shutdown...");
        }
    }
}
