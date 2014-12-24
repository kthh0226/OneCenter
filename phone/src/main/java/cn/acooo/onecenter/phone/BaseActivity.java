package cn.acooo.onecenter.phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public abstract class BaseActivity extends Activity {
	protected String TAG = App.TAG;
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
	
	
	public BaseActivity(){
		Log.i(App.TAG, "into BaseActivity===================");
		App.handler = myHandler;
	}
	protected Handler myHandler = new Handler(getActivityCallBack()) {
		public void handleMessage(Message msg) {
			//产生了异常，非正常返回
			if(msg.what > 1000 && msg.arg1 != 1){
				throw new IllegalArgumentException("messageCode error,msg="+msg);
			}
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
			super.handleMessage(msg);
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
	
	public abstract Handler.Callback getActivityCallBack();
}
