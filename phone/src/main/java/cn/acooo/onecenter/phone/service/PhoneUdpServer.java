package cn.acooo.onecenter.phone.service;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import cn.acooo.onecenter.core.BaseActivity;
import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.server.UDPServer;
import cn.acooo.onecenter.core.utils.UDPUtils;
import cn.acooo.onecenter.phone.App;

/**
 * Created by kthh on 15/1/19.
 */
public class PhoneUdpServer extends UDPServer {
    private Set<String> oneBoards = new HashSet<String>();
    public PhoneUdpServer(int port){
        setPort(port);
    }
    private Thread scanThread = null;
    private volatile boolean scanState = false;

    public String getRandomIP() {
        for (String s : oneBoards) {
            return s;
        }
        return "192.168.1.";
    }

    @Override
    public void scanNewOneBoard(String ip) {
        if(oneBoards.add(ip)){
            Log.i(App.TAG,"has scan oneBoards:"+oneBoards.toString());
        }
        App.handler.sendEmptyMessage(BaseActivity.UI_MSG_ID_NEW_ONEBOARD);
    }

    /**
     * 扫描oneBoard,每次广播间隔1秒，调用本函数后，注意一定要调用取消扫描，不然一直在发广播
     * 3分钟内自动停止扫描，这个是保险。
     */
    public void scanOneBoard(){
        if(scanThread == null){
            Log.i(App.TAG,"start scan oneboard");
            scanThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int count = 0;
                    scanState = true;
                    try{
                        while(count < 180 && scanState){
                            UDPUtils.send(OneCenterProtos.UDPType.SEARCH_ONEBOARD, null, 9998);
                            count++;
                            TimeUnit.SECONDS.sleep(1);
                        }

                    }catch (InterruptedException e){
                        Log.e(App.TAG,"sleep error",e);
                    }

                }
            });
            scanThread.setDaemon(true);
            scanThread.start();
        }else{
            Log.i(App.TAG,"already is scaning..");
        }
    }

    /**
     * 取消扫描
     */
    public void cancelScanOneBoard(){
        scanState = false;
        scanThread = null;
    }
}
