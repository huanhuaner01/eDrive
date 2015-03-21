package com.huishen.edrive;

import com.huishen.edrive.util.Packages;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class VersionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version);
		TextView tvVersion = (TextView) findViewById(R.id.aboutus_tv_version);
		ImageButton back = (ImageButton)findViewById(R.id.header_back);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
		TextView title = (TextView)findViewById(R.id.header_title);
		title.setText("版本信息");
		tvVersion.append(Packages.getVersionName(this));
	}
}
