package com.huishen.edrive.center;

import com.huishen.edrive.R;

import android.app.Activity;
import android.os.Bundle;

public class ComplainActivity extends Activity {
   private ComplainListAdapter apdater ;
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complain);
	}
}
