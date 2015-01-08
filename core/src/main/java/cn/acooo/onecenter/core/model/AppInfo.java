package cn.acooo.onecenter.core.model;

import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.widget.ProgressBar;

import com.google.protobuf.ByteString;
import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.utils.ImageUtil;


/**
 * TODO：这个类有个bug，但是不着急处理，当文件大小超过2G，也就是int最大值的时候，进度条显示会异常
 */
public class AppInfo {
	private Bitmap appIcon;
	private String appName;
	private Long appSize;
	private String appVersion;
	private String appLocalVersion;
	private String packageName;
	private boolean downloading = false;
    private Long curProgress = 0L;
    private String publicSourceDir;
    private ProgressBar progressBar;

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setAppSize(Long appSize) {
        this.appSize = appSize;
    }

    public String getPublicSourceDir() {
        return publicSourceDir;
    }

    public void setPublicSourceDir(String publicSourceDir) {
        this.publicSourceDir = publicSourceDir;
    }

    public Long getCurProgress() {
        return curProgress;
    }

    public void setCurProgress(Long curProgress) {
        this.curProgress = curProgress;
        this.progressBar.setProgress(curProgress.intValue());
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public AppInfo(OneCenterProtos.AppInfo appInfo, PackageInfo packageInfo){
		ByteString bs = appInfo.getIcon();
		this.appIcon = ImageUtil.Bytes2Bimap(appInfo.getIcon().toByteArray());
		this.appName = appInfo.getName();
		this.appSize = appInfo.getPackageSize();
		this.appVersion = appInfo.getVersion();
		this.appLocalVersion = packageInfo == null ? "未安装":packageInfo.versionName;
		this.packageName = appInfo.getPackageName();
        this.publicSourceDir = appInfo.getPublicSourceDir();
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
	public Long getAppSize() {
		return appSize;
	}
	public void setAppSize(long appSize) {
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
        return "AppInfo{" +
                "appIcon=" + appIcon +
                ", appName='" + appName + '\'' +
                ", appSize=" + appSize +
                ", appVersion='" + appVersion + '\'' +
                ", appLocalVersion='" + appLocalVersion + '\'' +
                ", packageName='" + packageName + '\'' +
                ", downloading=" + downloading +
                ", curProgress=" + curProgress +
                ", publicSourceDir='" + publicSourceDir + '\'' +
                '}';
    }
}
