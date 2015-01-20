package cn.acooo.onecenter.phone.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import cn.acooo.onecenter.core.BaseActivity;
import cn.acooo.onecenter.core.server.HttpServer;
import cn.acooo.onecenter.phone.App;

public class SocketService extends Service{
	
	public final static String KEY_IP = "ip";
	public final static String KEY_PORT = "port";
	private final static String TAG = "SocketService";
    private final static int NETTY_PORT = 9999;
    private final static int JETTY_PORT = 9090;
	private IBinder binder = new SocketService.MyBind();

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "socketService is onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.i(TAG, "socketService is onStartCommand");
		if(!App.socketIsRun){
			String ip = intent.getStringExtra(KEY_IP);
			int port = intent.getIntExtra(KEY_PORT,NETTY_PORT);
			Log.i(TAG, "connect server ip:"+ip+",port:"+port);
			new Thread(new Client(ip,port)).start();
		}else{
			App.handler.sendEmptyMessage(BaseActivity.UI_MSG_ID_AREADY_CONNECTED);
		}
        Log.i(TAG,"httpServer=="+App.jettyServer);
		if(App.jettyServer == null || !App.jettyServer.isRunning()){
            Log.i(TAG,"jetty begin start");
            HttpServer httpServer = new HttpServer(JETTY_PORT);
            Thread t = new Thread(httpServer);
            t.setDaemon(true);
            t.start();
            App.jettyServer = httpServer.getServer();
        }


		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return this.binder;
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy＝＝＝＝＝＝＝＝＝＝＝＝＝");
		App.disconnect();
        if(App.jettyServer != null && App.jettyServer.isRunning()){
            try {
                App.jettyServer.stop();
            } catch (Exception e) {
               Log.e(TAG,"jetty http server stop error",e);
            }
        }
	}

	public class MyBind extends Binder{
		public SocketService getSocketService(){
			return SocketService.this;
		}
	}
}
