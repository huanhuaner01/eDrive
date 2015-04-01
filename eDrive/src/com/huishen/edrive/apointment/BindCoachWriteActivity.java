package com.huishen.edrive.apointment;

import com.huishen.edrive.R;
import com.huishen.edrive.SplashActivity;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.login.VerifyPhoneActivity;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Prefs;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 绑定教练填写信息界面
 * @author zhanghuan
 *
 */
public class BindCoachWriteActivity extends Activity {
 private int key = 1 ; //1代表绑定号码 ，2代表填写学员姓名 ，3代表填写密码
 private LinearLayout tellay , namelay ,passlay ;
 private TextView title ,note ;
 private Button commit ;
 private ImageButton back ;
 private EditText coachtel ,stuname , stupass ,constupass ;
	/***************************腾讯统计相关框架*************************************/
	StatLogger logger = SplashActivity.getLogger();
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
		super.onDestroy();
		android.os.Debug.stopMethodTracing();
	}
	/***************************腾讯统计基本框架结束*************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_coach_write);
		AppController.getInstance().addActivity(this);
		//----------获取上一个activity传递的数据--------------
		key = this.getIntent().getIntExtra("key", 1);
		//----------获取上一个activity传递的数据结束！--------------
		switch(key){
		case 1:
			initTel();
			break ;
		case 2:
			initName();
			break ;
		case 3:
			initPass();
			break ;
		}
		
	}

	private void initView(){
		title = (TextView) findViewById(R.id.header_title) ;
		back = (ImageButton) findViewById(R.id.header_back) ;
		commit = (Button) findViewById(R.id.bind_coach_write_commit);
		note = (TextView) findViewById(R.id.bind_coach_write_note) ;
		title.setText(this.getResources().getString(R.string.bind_coach));
		this.back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
	}
	private void initPass() {
		initView();
		passlay = (LinearLayout) findViewById(R.id.bind_coach_w_lay_stupass);
		passlay.setVisibility(View.VISIBLE);
		stupass = (EditText) findViewById(R.id.bind_coach_w_stupass) ;
		constupass = (EditText) findViewById(R.id.bind_coach_w_conpass);
		note.setText(this.getResources().getString(R.string.bind_coach_note_pass));
		
	}

	private void initName() {
		initView();
		namelay = (LinearLayout) findViewById(R.id.bind_coach_w_lay_stuname);
		namelay.setVisibility(View.VISIBLE);
		stuname = (EditText) findViewById(R.id.bind_coach_w_stuname) ;
		commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// check phone
				String name = stuname.getText().toString();
				if (name.equals("")) {
					AppUtil.ShowShortToast(getApplicationContext(), "真实姓名不能为空");
					
				}else{
					Prefs.writeString(getApplicationContext(), SRL.Param.PARAM_BIND_STU_NAME, name);
					finish();
				}
			}
			
		});
	}

	private void initTel() {
		initView();
		tellay = (LinearLayout) findViewById(R.id.bind_coach_w_lay_tel);
		tellay.setVisibility(View.VISIBLE);
		coachtel = (EditText) findViewById(R.id.bind_coach_w_tel) ;
		note.setText(this.getResources().getString(R.string.bind_coach_note_tel));
		commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// check phone
				String num = coachtel.getText().toString();
				if (!num.matches("(86|\\+86)?1\\d{10}")) {
					Toast.makeText(
							BindCoachWriteActivity.this,
							getResources().getString(
									R.string.str_verify_phone_err_not_valid_number),
							Toast.LENGTH_SHORT).show();
					
				}else{
					Prefs.writeString(getApplicationContext(), SRL.Param.PARAM_BIND_COACH_PHONE, num+"");
					finish();
				}
			}
			
		});
	}
	
	
}
