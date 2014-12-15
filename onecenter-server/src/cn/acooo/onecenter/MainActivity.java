package cn.acooo.onecenter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import cn.acooo.onecenter.adapter.MyAppListAdapter;
import cn.acooo.onecenter.service.ServerService;

public class MainActivity extends BaseActivity {
	private ViewPager viewPager;// 页卡内容
	private TextView myAppTextView, myPhoneTextView, myScriptTextView;
	private List<View> views;// Tab页面列表
	private int currIndex = 0;// 当前页卡编号
	public static final String TAG = "MainActivity";
	private View myAppView,myPhoneView, myScriptView;// 各个页卡

	private void startServer() {
		Intent intent = new Intent(this, ServerService.class);
		super.startService(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);
		InitTextView();
		InitViewPager();
		
		//this.startServer();
	}
	
	private List<String> getData(){
		
		List<String> data = new ArrayList<String>();
		data.add("测试数据1");
		data.add("测试数据2");
		data.add("测试数据3");
		data.add("测试数据4");
		data.add("测试数据5");
		data.add("测试数据6");
		data.add("测试数据7");
		data.add("测试数据8");
		data.add("测试数据9");
		data.add("测试数据10");
		data.add("测试数据11");
		data.add("测试数据12");
		data.add("测试数据13");
		data.add("测试数据14");
		data.add("测试数据15");
		return data;
	}
	
	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		views = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		myAppView = inflater.inflate(R.layout.my_app, null);
		ListView apps = (ListView)myAppView.findViewById(R.id.apps);
		apps.setAdapter(new MyAppListAdapter(myAppView.getContext()));
		
		
		myPhoneView = inflater.inflate(R.layout.my_phone, null);
		myScriptView = inflater.inflate(R.layout.my_script, null);
		views.add(myAppView);
		views.add(myPhoneView);
		views.add(myScriptView);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化头标
	 */

	private void InitTextView() {
		myAppTextView = (TextView) findViewById(R.id.my_app);
		myPhoneTextView = (TextView) findViewById(R.id.my_phone);
		myScriptTextView = (TextView) findViewById(R.id.my_script);
		myAppTextView.setOnClickListener(new MyOnClickListener(0));
		myPhoneTextView.setOnClickListener(new MyOnClickListener(1));
		myScriptTextView.setOnClickListener(new MyOnClickListener(2));
	}

	/**
	 * 
	 * 头标点击监听 3
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			Log.i(TAG, "click view,index ="+index);
			viewPager.setCurrentItem(index);
		}

	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View v = mListViews.get(position);
			container.addView(v, 0);
			return v;
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int location) {
			Log.i(TAG, "location ==="+location);
			if(location == 0){
				myAppTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				myAppTextView.setTextColor(Color.parseColor("#FF0000"));
				myPhoneTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
				myPhoneTextView.setTextColor(Color.parseColor("#000000"));
				myScriptTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
				myScriptTextView.setTextColor(Color.parseColor("#000000"));
			}else if (location == 1){
				myAppTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
				myAppTextView.setTextColor(Color.parseColor("#000000"));
				myPhoneTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				myPhoneTextView.setTextColor(Color.parseColor("#FF0000"));
				myScriptTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
				myScriptTextView.setTextColor(Color.parseColor("#000000"));
			}else if(location == 2){
				myAppTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
				myAppTextView.setTextColor(Color.parseColor("#000000"));
				myPhoneTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
				myPhoneTextView.setTextColor(Color.parseColor("#000000"));
				myScriptTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				myScriptTextView.setTextColor(Color.parseColor("#FF0000"));
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Callback getActivityCallBack() {
		// TODO Auto-generated method stub
		return null;
	}
}
