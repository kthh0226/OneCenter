package cn.acooo.onecenter.server;

import cn.acooo.onecenter.server.model.PhoneClient;
import android.app.Application;
import android.os.Handler;

public class App extends Application{
	
	public static final String TAG = "ONE";
	private static App app;
	public static volatile boolean serverServiceIsRun = false;
	public static PhoneClient selectedPhoneClient;
	public static Handler handler;
	
	public static App getInstance(){
		return app;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
	}
}
