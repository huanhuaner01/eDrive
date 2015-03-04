package com.huishen.edrive.center;

import com.huishen.edrive.R;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.util.AppController;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ModifyUserInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_user_info);
		AppController.getInstance().addActivity(this);
	}
}
