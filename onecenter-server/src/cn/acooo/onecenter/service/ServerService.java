package cn.acooo.onecenter.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import cn.acooo.onecenter.App;
import cn.acooo.onecenter.server.Server;

public class ServerService extends Service{

	private String TAG = App.TAG;
	private IBinder binder = new ServerService.MyBind();
	
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
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return this.binder;
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	public class MyBind extends Binder{
		public ServerService getServerService(){
			return ServerService.this;
		}
	}
}
