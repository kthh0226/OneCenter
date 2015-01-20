package cn.acooo.onecenter.core;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

public abstract class BaseActivity extends Activity {

    public BaseActivity(){
        this.initHandler();
    }

	public static String TAG = "ONE";
	/**
	 * 连接成功
	 */
	public final static int UI_MSG_ID_CONNECTED = 1;
	/**
	 * 连接断开
	 */
	public final static int UI_MSG_ID_DISCONNECTED = 2;
	/**
	 * 已经连接
	 */
	public final static int UI_MSG_ID_AREADY_CONNECTED = 3;
	
	/**
	 * 没有连接到OneBoard
	 */
	public final static int UI_MSG_ID_NOT_CONNECTED = 4;
	
	/**
	 * 有新的电话连接上
	 */
	public final static int UI_MSG_ID_NEW_PHONE = 5;

    /**
     * 发现新的OneBoard
     */
    public final static int UI_MSG_ID_NEW_ONEBOARD = 6;


	protected Handler myHandler = new Handler(getActivityCallBack()) {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UI_MSG_ID_CONNECTED:
				showInfo("网络连接", "连接成功");
				break;
			case UI_MSG_ID_DISCONNECTED:
				showInfo("网络连接", "连接断开");
				break;
			case UI_MSG_ID_AREADY_CONNECTED:
				showInfo("网络连接", "已经连接到OneBoard");
				break;
			case UI_MSG_ID_NOT_CONNECTED:
				showInfo("网络连接", "没有连接到OneBoard");
				break;
			default:
				throw new RuntimeException("not catch ui message id,id="
						+ msg.what);
			}
		}
	};
	
	public void showInfo(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

    /**
     * 处理本页面的消息回掉
     * @return
     */
	protected abstract Handler.Callback getActivityCallBack();

    /**
     * handler已经被实例化,放到app中，方便发送数据
     */
    protected abstract void initHandler();
}
