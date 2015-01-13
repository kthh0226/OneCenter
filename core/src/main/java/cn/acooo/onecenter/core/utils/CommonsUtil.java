package cn.acooo.onecenter.core.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonsUtil {
	/** 
	 * 查询手机内非系统应用 
	 * @param context 
	 * @return 
	 */  
	public static List<PackageInfo> getAllApps(Context context) {  
	    List<PackageInfo> apps = new ArrayList<PackageInfo>();  
	    PackageManager pManager = context.getPackageManager();  
	    //获取手机内所有应用  
	    List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        if(paklist != null){
            for (PackageInfo pak : paklist) {
                //判断是否为非系统预装的应用程序
                if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                    // customs applications
                    apps.add(pak);
                }
            }
        }

	    return apps;  
	} 
	/** 
	 * 查询手机内非系统应用 
	 * @param context 
	 * @return 
	 */  
	public static Map<String,PackageInfo> getAllAppsByMap(Context context) {  
		Map<String,PackageInfo> ms = new HashMap<String, PackageInfo>();
	    PackageManager pManager = context.getPackageManager();  
	    //获取手机内所有应用  
	    List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        if(paklist != null){
            for (PackageInfo pak : paklist) {
                //判断是否为非系统预装的应用程序
                if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                    ms.put(pak.packageName, pak);
                }
            }
        }
	    return ms;
	} 
}
