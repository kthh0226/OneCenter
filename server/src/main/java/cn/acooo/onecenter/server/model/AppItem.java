package cn.acooo.onecenter.server.model;

import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.protobuf.ByteString;

import cn.acooo.onecenter.core.auto.OneCenterProtos.AppInfo;
import cn.acooo.onecenter.core.utils.ImageUtil;
import cn.acooo.onecenter.server.App;


public class AppItem {
	private Bitmap appIcon;
	private String appName;
	private String appSize;
	private String appVersion;
	private String appLocalVersion;
	private String packageName;
	
	public AppItem(AppInfo appInfo , PackageInfo packageInfo){
		ByteString bs = appInfo.getIcon();
		Log.d(App.TAG, "bs.size======="+bs.size());
		this.appIcon = ImageUtil.Bytes2Bimap(appInfo.getIcon().toByteArray());
		this.appName = appInfo.getName();
		this.appSize = appInfo.getPackageSize();
		this.appVersion = appInfo.getVersion();
		this.appLocalVersion = packageInfo == null ? "未安装":packageInfo.versionName;
		this.packageName = appInfo.getPackageName();
	}
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Bitmap getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Bitmap appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppSize() {
		return appSize;
	}
	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getAppLocalVersion() {
		return appLocalVersion;
	}
	public void setAppLocalVersion(String appLocalVersion) {
		this.appLocalVersion = appLocalVersion;
	}

	
	@Override
	public String toString() {
		return "AppItem [appIcon=" + appIcon + ", appName=" + appName
				+ ", appSize=" + appSize + ", appVersion=" + appVersion
				+ ", appLocalVersion=" + appLocalVersion + ", packageName="
				+ packageName + "]";
	}
}
