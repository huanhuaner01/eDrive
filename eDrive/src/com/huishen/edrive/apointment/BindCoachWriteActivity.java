package com.huishen.edrive.apointment;

import com.huishen.edrive.R;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.util.AppController;

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
	}

	private void initTel() {
		initView();
		tellay = (LinearLayout) findViewById(R.id.bind_coach_w_lay_tel);
		tellay.setVisibility(View.VISIBLE);
		coachtel = (EditText) findViewById(R.id.bind_coach_w_tel) ;
		note.setText(this.getResources().getString(R.string.bind_coach_note_tel));
	}
}
