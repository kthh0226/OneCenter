package cn.acooo.onecenter.phone.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import cn.acooo.onecenter.phone.App;

/**
 * Created by kthh on 15/1/20.
 */
public class UDPService extends Service {
    private final static int UDP_PORT = 9998;
    private PhoneUdpServer udpServer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //启动udpServer
        if(App.phoneUdpServer == null || !App.phoneUdpServer.isRun()){
            udpServer = new PhoneUdpServer(UDP_PORT);
            udpServer.setIsRun(true);
            Thread udpServerThread = new Thread(udpServer);
            udpServerThread.setDaemon(true);
            udpServerThread.start();
            App.phoneUdpServer = udpServer;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        udpServer.setIsRun(false);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
