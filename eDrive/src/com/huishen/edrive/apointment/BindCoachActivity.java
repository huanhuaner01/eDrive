package com.huishen.edrive.apointment;

import com.huishen.edrive.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 绑定教练
 * @author zhanghuan
 *
 */
public class BindCoachActivity extends Activity implements OnClickListener{
   private TextView title  ,tel ,stuname ,stupass;
   private ImageButton back ;
   private LinearLayout telLay ,stunameLay ,stupassLay ;
   
   private Intent intent ; //填写数据的intent
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_coach);
		registView();
		initView();
	}
	private void registView() {
		title = (TextView) findViewById(R.id.header_title) ;
		tel = (TextView) findViewById(R.id.bind_coach_tv_tel) ;
		stuname = (TextView) findViewById(R.id.bind_coach_tv_stuname) ;
		stupass = (TextView) findViewById(R.id.bind_coach_tv_stupass) ;
		back = (ImageButton) findViewById(R.id.header_back) ;
		telLay = (LinearLayout) findViewById(R.id.bind_coach_tel) ;
		stunameLay = (LinearLayout) findViewById(R.id.bind_coach_stuname) ;
		stupassLay = (LinearLayout) findViewById(R.id.bind_coach_stupass);
	}
	private void initView() {
		title.setText(this.getResources().getString(R.string.bind_coach));
		this.back.setOnClickListener(this) ;
		this.telLay.setOnClickListener(this) ;
		this.stunameLay.setOnClickListener(this) ;
		this.stupassLay.setOnClickListener(this) ;
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.header_back:
			this.finish();
			break ;
		case R.id.bind_coach_tel:
			intentActivty(1); //1代表填写电话号码
			break ;
		case R.id.bind_coach_stuname:
			intentActivty(2); //2代表填写学员姓名
			break ;
		case R.id.bind_coach_stupass:
			intentActivty(3); //3代表填写密码
			break ;
		}
	}
	/**
	 * 启动 BindCoachWriteActivity
	 * @param parem
	 */
	private void intentActivty(int parem){
		if(intent == null){
		intent = new Intent(this,BindCoachWriteActivity.class);
		}
		intent.removeExtra("key");
		intent.putExtra("key",parem) ;
		this.startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		initView();
		super.onResume();
		
	}
	
	
}
