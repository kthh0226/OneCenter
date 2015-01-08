package cn.acooo.onecenter.server.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;

import cn.acooo.onecenter.server.App;

/**
 * Created by kthh on 15/1/8.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收广播：系统启动完成后运行程序
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            Intent newIntent = new Intent(context, WatchInstall.class);
//            newIntent.setAction("android.intent.action.MAIN");
//            newIntent.addCategory("android.intent.category.LAUNCHER");
//            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(newIntent);
        }
        //接收广播：设备上新安装了一个应用程序包
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString().substring(8);
            File file = new File(App.DOWNLOAD_PATH + packageName + ".apk");
            if(file != null && file.exists()){
                file.delete();
            }
        }
        //接收广播：设备上删除了一个应用程序包。
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {

        }
    }
}
