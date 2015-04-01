package com.huishen.edrive;

import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.util.AppController;
import com.tencent.stat.StatService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

public class InfoActivity extends Activity {
	private WebView webview  ;
    private TextView  title ;
    private ImageButton back ;
    private String titlestr ,url ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		AppController.getInstance().addActivity(this);
		//-----------------获取数据 --------------------
		titlestr = this.getIntent().getStringExtra("title") ;
		url = this.getIntent().getStringExtra("url");
		//-------------------获取数据结束！---------------
		registView();
		init();
	}
	/***************************腾讯统计相关框架*************************************/
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	   @Override
		protected void onPause() {
			super.onPause();
			StatService.onPause(this);
		}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		android.os.Debug.stopMethodTracing();
	}
	/***************************腾讯统计基本框架结束*************************************/
	private void registView() {
		title = (TextView) findViewById(R.id.header_title);
		back = (ImageButton) findViewById(R.id.header_back);
		webview = (WebView) findViewById(R.id.service_info_webview);
	}
	private void init() {
		this.title.setText(titlestr) ;
		this.back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		webview.getSettings().setJavaScriptEnabled(true);
		openUrl(NetUtil.getAbsolutePath(url)) ;
	}
	
	/**
	 * 打开一个网站
	 * @param url
	 */
	private void openUrl(String url){
//		Log.i("ServiceInfoActivity",url) ;
		webview.loadUrl(url);
	}
}
