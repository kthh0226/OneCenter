package cn.acooo.onecenter.core;

public class AppInfo {
	private String icon;
	private String packageName;
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	@Override
	public String toString() {
		return "AppInfo [icon=" + icon + ", packageName=" + packageName + "]";
	}
}
