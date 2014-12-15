package cn.acooo.onecenter;

import android.os.Bundle;
import android.os.Handler.Callback;
import android.widget.TextView;

public class MyPhoneActivity extends BaseActivity{
	
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_phone);
		textView = (TextView)findViewById(R.id.showArea);
		if(App.selectedPhoneClient != null){
			textView.setText(App.selectedPhoneClient.getIp());
		}
	}

	@Override
	public Callback getActivityCallBack() {
		// TODO Auto-generated method stub
		return null;
	}
}
