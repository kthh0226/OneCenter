package cn.acooo.onecenter.phone;

import io.netty.channel.Channel;
import android.app.Application;
import android.os.Handler;

public class App extends Application {
	private static App app;
	private static Channel channel;
	public static volatile boolean socketIsRun = false;
	public static final String TAG = "ONE";
	public static Handler handler;
	
	
	public static void setChannel(Channel c){
		channel = c;
		socketIsRun = true;
	}
	
	public static void disconnect(){
		if(channel != null){
			channel.disconnect();
			socketIsRun = false;
		}
	}
	
	public static App getInstance(){
		return app;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
	}
}
