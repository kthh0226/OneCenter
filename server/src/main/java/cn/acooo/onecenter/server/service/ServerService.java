package cn.acooo.onecenter.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import cn.acooo.onecenter.server.App;
import cn.acooo.onecenter.server.net.jetty.HttpServer;
import cn.acooo.onecenter.server.net.netty.Server;

public class ServerService extends Service{

	private String TAG = App.TAG;
	private IBinder binder = new ServerService.MyBind();
	private Thread jettyThread;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "socketService is onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.i(TAG, "socketService is onStartCommand");
		new Thread(new Server(9999)).start();
        if(App.jettyServer == null || !App.jettyServer.isRunning()){
            jettyThread = new Thread(new HttpServer(9090));
            jettyThread.setDaemon(true);
            jettyThread.start();
        }

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
                jettyThread.stop();
            } catch (Exception e) {
               Log.e(TAG,"jetty stop error",e);
            }
        }

	}

	public class MyBind extends Binder{
		public ServerService getServerService(){
			return ServerService.this;
		}
	}
}
