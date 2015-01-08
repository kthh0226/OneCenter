package cn.acooo.onecenter.server;

import android.app.Application;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import org.eclipse.jetty.server.Server;

import java.io.File;

import cn.acooo.onecenter.server.model.PhoneClient;

public class App extends Application{
	
	public static final String TAG = "ONE";
    public static String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getPath() +"/onecenter/tmp/";
	private static App app;
	public static volatile boolean serverServiceIsRun = false;
	public static PhoneClient selectedPhoneClient;
	public static Handler handler;
	public static Server jettyServer;

	public static App getInstance(){
		return app;
	}

    private void clearTmp(){
        File file = new File(DOWNLOAD_PATH);
        if(file.exists()){
            File[] files = file.listFiles();
            if(files != null){
                for(File f : files){
                    f.delete();
                }
            }
        }else{
            file.mkdirs();
        }
    }

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
        clearTmp();
	}

    @Override
    public void onTerminate() {
        Log.i(TAG,"into App.onTerminate()=========");
        super.onTerminate();
        try {
            jettyServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
