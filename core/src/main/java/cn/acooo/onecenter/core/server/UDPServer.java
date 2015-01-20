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
    public boolean isRun(){
        return isRun;
    }
    public void setIsRun(boolean v){
        this.isRun = v;
    }
    private int port = 9998;
    public void setPort(int port){
        this.port = port;
    }
    public int getPort(){
        return port;
    }
    private final static String LOCALHOST = "localhost";
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
                    byte[] data = new byte[dp.getLength()];
                    for(int i =0; i< data.length; i++){
                            data[i] = buf[i];
                    }
                    String clientIp = dp.getAddress().getHostAddress();
                    Log.i(BaseActivity.TAG,"recive sender ip==="+clientIp);
                    if(LOCALHOST.equals(clientIp)){
                        continue;//不处理本机发出去的广播
                    }
                    OneCenterProtos.UDPMessage message = OneCenterProtos.UDPMessage.parseFrom(data);
                    switch (message.getType()){
                        case SEARCH_ONEBOARD:{
                            searchedOneBoard(clientIp);
                            break;
                        }
                        case IS_ONEBOARD:{
                            scanNewOneBoard(clientIp);
                            break;
                        }
                        case IS_PHONE:{
                            break;
                        }
                    }
                }catch (InvalidProtocolBufferException e){
                    Log.e(BaseActivity.TAG,"invalid package",e);
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

    /**
     * 自己是OneBoard，并且被发现了，调用本函数
     */
    public void searchedOneBoard(String clientIp){
        Log.i(BaseActivity.TAG,"server recive udp packet,ip="+clientIp);
    }
    /**
     * 当手机端扫描到新的oneboard，调用本函数
     * @param ip
     */
    public void scanNewOneBoard(String ip){
        Log.i(BaseActivity.TAG,"scan new oneBoard,ip="+ip);
    }
}
