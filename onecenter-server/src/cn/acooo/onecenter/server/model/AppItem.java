package cn.acooo.onecenter.server.model;

import cn.acooo.onecenter.server.R;
import cn.acooo.onecenter.server.auto.OneCenterProtos.AppInfo;


public class AppItem {
	private Integer appIcon;
	private String appName;
	private String appSize;
	private String appVersion;
	private String appLocalVersion;
	private String packageName;
	
	public AppItem(AppInfo appInfo , String localVersion){
		this.appIcon = R.drawable.ic_launcher;
		this.appName = appInfo.getName();
		this.appSize = appInfo.getPackageSize();
		this.appVersion = appInfo.getVersion();
		this.appLocalVersion = localVersion == null ? "未安装":localVersion;
		this.packageName = appInfo.getPackageName();
	}
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Integer getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Integer appIcon) {
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
