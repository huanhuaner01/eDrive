package com.huishen.edrive.apointment;

import com.huishen.edrive.R;
import com.huishen.edrive.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BindCoachWriteActivity extends Activity {
 private int key = 1 ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_coach_write);
		
		//----------获取上一个activity传递的数据--------------
		key = this.getIntent().getIntExtra("key", 1);
		//----------获取上一个activity传递的数据结束！--------------
		
		
	}
}
