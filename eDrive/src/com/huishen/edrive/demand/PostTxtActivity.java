package com.huishen.edrive.demand;

import com.huishen.edrive.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class PostTxtActivity extends Activity {
    private TextView title ;
    private ImageButton back ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_txt);
		registView();
		initView();
	}
	
	/**
	 * 注册组件
	 */
	private void registView() {
		this.title = (TextView) findViewById(R.id.header_title);
		this.back = (ImageButton) findViewById(R.id.header_back);
	}
	
	/**
	 * 初始化
	 */
	private void initView() {
		this.title.setText(this.getResources().getString(R.string.post_title));
		
		//返回按钮监听事件
		this.back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
