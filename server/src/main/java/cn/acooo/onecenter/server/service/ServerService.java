package cn.acooo.onecenter.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import cn.acooo.onecenter.core.server.HttpServer;
import cn.acooo.onecenter.server.App;
import cn.acooo.onecenter.server.net.netty.Server;
import cn.acooo.onecenter.core.server.UDPServer;

public class ServerService extends Service{

	private String TAG = App.TAG;
	private IBinder binder = new ServerService.MyBind();
	private Thread jettyThread;
    private UDPServer udpServer;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "socketService is onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.i(TAG, "socketService is onStartCommand");
        //启动nettyServer
		new Thread(new Server(9999)).start();

        //启动jettyServer
        if(App.jettyServer == null || !App.jettyServer.isRunning()){
            HttpServer httpServer = new HttpServer(9090);
            jettyThread = new Thread(httpServer);
            jettyThread.setDaemon(true);
            jettyThread.start();
            App.jettyServer = httpServer.getServer();
        }
        //启动udpServer
        udpServer = new UDPServer();
        udpServer.setIsRun(true);
        Thread udpServerThread = new Thread(udpServer);
        udpServerThread.setDaemon(true);
        udpServerThread.start();

		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return this.binder;
	}
	
	@Override
	public void onDestroy() {
        super.onDestroy();
		Log.i(TAG, "ServerService onDestroy======================");
        if(App.jettyServer != null && App.jettyServer.isRunning()){
            try {
                App.jettyServer.stop();
            } catch (Exception e) {
               Log.e(TAG,"jetty stop error",e);
            }
        }
        udpServer.setIsRun(false);
	}

	public class MyBind extends Binder{
		public ServerService getServerService(){
			return ServerService.this;
		}
	}
}
