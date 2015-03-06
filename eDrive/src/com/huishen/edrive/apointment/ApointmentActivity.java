package com.huishen.edrive.apointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.android.volley.Response;
import com.huishen.edrive.MainActivity;
import com.huishen.edrive.R;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

public class ApointmentActivity extends Activity {
	private String TAG = "ApointmentActivity" ;
   private ExpandableListView list ;
   private TextView title ;
   private ImageButton back ;
   
   private AppointmentSubExListApdater adapter ;
	private ArrayList<HashMap<String, String>> mGroupData = null;
	private ArrayList<ArrayList<HashMap<String , String>>> mData = null;
	
	
	private String date ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apointment);
		AppController.getInstance().addActivity(this);
		//---------------------获取数据----------------------
		  date = this.getIntent().getStringExtra("lessonDate");
		  
		//---------------------获取数据结束！-------------------------
		registView();
		init();
	}
	
	
	private void registView() {
		list = (ExpandableListView)findViewById(R.id.appoint_expandablelist);
		title = (TextView)findViewById(R.id.header_title) ;
		back = (ImageButton)findViewById(R.id.header_back) ;
		
	}
	
	private void init(){
		this.title.setText("预约管理") ;
		this.back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
		}) ;
		
		//-------添加约课数据-------
		mGroupData = new ArrayList<HashMap<String, String>>();
		mData = new ArrayList<ArrayList<HashMap<String , String>>>();
		for(int i = 0 ;i<2 ;i++){
			HashMap<String ,String> map = new HashMap<String ,String>();
			map.put("subName", "科目"+(i+1)) ;
			map.put("subStatus", "0/3") ;
			mGroupData.add(map);
			ArrayList<HashMap<String , String>> prms = new ArrayList<HashMap<String , String>>();
			for(int j = 0 ;j<3 ;j++){
				HashMap<String ,String> pmap = new HashMap<String ,String>();
				pmap.put("subName", "时间"+(j+1)) ;
				pmap.put("subStatus", "0/0") ;
				prms.add(pmap);
			}
			mData.add(prms);
		}
		adapter = new AppointmentSubExListApdater(this,mGroupData ,mData);
		list.setAdapter(adapter);
		if(adapter.getGroupCount()>=0){
		list.expandGroup(0);
		}
		getData();
		
	}
	
	private void getData(){
		/**
		 * [[{subName=时间1, subStatus=0/0}, {subName=时间2, subStatus=0/0},
		 *  {subName=时间3, subStatus=0/0}], [{subName=时间1, subStatus=0/0},
		 *  {subName=时间2, subStatus=0/0}, {subName=时间3, subStatus=0/0}]]
		 */
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId",Prefs.readString(this, Const.USER_COACH_ID));
		map.put("lessonDate", date);
		NetUtil.requestStringData(SRL.Method.METHOD_GET_SUBJECT, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
					}
				},new DefaultErrorListener());
	}

	
}
