package com.huishen.edrive.login;

import com.huishen.edrive.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class ServiceInfoActivity extends Activity {
    private WebView webview  ; 
    private RadioGroup group ;
    private TextView title ;
    private ImageButton back ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_info);
		registView();
		init();
	}
	
	/**
	 * 注册组件
	 */
	private void registView(){
		title = (TextView) findViewById(R.id.header_title);
		back = (ImageButton) findViewById(R.id.header_back);
		webview = (WebView) findViewById(R.id.service_info_webview);
		group = (RadioGroup) findViewById(R.id.service_info_group);
	}
	
	private void init(){
		this.title.setText("服务条款") ;
		this.back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		webview.getSettings().setJavaScriptEnabled(true);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup g, int checkedId) {
				switch(checkedId){
				case R.id.service_info_se:
					openUrl("http://www.baidu.com") ;
					break ;    
				case R.id.service_info_pri:
					openUrl("http://www.jd.com");
					break ;
				}
			}
			
		});
		group.check(R.id.service_info_se);
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
