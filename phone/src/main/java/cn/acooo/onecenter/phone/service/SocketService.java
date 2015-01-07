package cn.acooo.onecenter.phone.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import cn.acooo.onecenter.core.BaseActivity;
import cn.acooo.onecenter.phone.App;
import cn.acooo.onecenter.phone.jetty.HttpServer;

public class SocketService extends Service{
	
	public final static String KEY_IP = "ip";
	public final static String KEY_PORT = "port";
	private final static String TAG = "SocketService";
	private IBinder binder = new SocketService.MyBind();
	private Client client;
	
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
			int port = intent.getIntExtra(KEY_PORT,9999);
			Log.i(TAG, "connect server ip:"+ip+",port:"+port);
			client = new Client(ip,port);
			new Thread(client).start();
		}else{
			App.handler.sendEmptyMessage(BaseActivity.UI_MSG_ID_AREADY_CONNECTED);
		}
        Log.i(TAG,"httpServer=="+App.jettyServer);
		if(App.jettyServer == null || !App.jettyServer.isRunning()){
            Log.i(TAG,"jetty begin start");
            Thread t = new Thread(new HttpServer(9090));
            t.setDaemon(true);
            t.start();
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
		super.onDestroy();
	}

	public class MyBind extends Binder{
		public SocketService getSocketService(){
			return SocketService.this;
		}
	}
}
