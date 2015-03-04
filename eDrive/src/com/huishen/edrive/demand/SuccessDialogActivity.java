package com.huishen.edrive.demand;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.center.CoachDetailActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.util.SimpleAudioPlayer;
import com.huishen.edrive.widget.RoundImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class SuccessDialogActivity extends Activity {
	private String TAG = "SuccessDialogActivity" ;
    private long tempOrderId ;
    private TextView coachname ,field ,score ,distance ;
    private Button commit ;
    private RatingBar rating ;
    private RoundImageView img ;
    private long coachId ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_success_dialog);
		AppController.getInstance().addActivity(this);
		setFinishOnTouchOutside(false);
		removeLimit();
		tempOrderId = this.getIntent().getLongExtra("tempOrderId", 0);
		//--------------震动------------------------
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		if (vibrator.hasVibrator()){
			vibrator.vibrate(1000);
		}
		//-----------震动结束！---------------------
		SimpleAudioPlayer.getInstance().playAssetsAudio(this, "ring_newmsg.wav");
		registView();
		initView();
	}
	
	/**
	 * 注销限制
	 */
	private void removeLimit(){
		AppController.getInstance().stopAlarm();
		Prefs.writeString(getApplicationContext(), Const.ORDER_STATUS, "0");
	}
	private void registView() {
		coachname = (TextView) findViewById(R.id.order_coach_name);
		field  = (TextView) findViewById(R.id.order_coach_field) ;
		score = (TextView) findViewById(R.id.order_coach_judge);
		distance = (TextView) findViewById(R.id.order_distance) ;
		commit = (Button)findViewById(R.id.order_success) ;
		rating = (RatingBar)findViewById(R.id.order_coach_ratingbar) ;
		img = (RoundImageView)findViewById(R.id.order_coach_photo);
	}
	
	private void initView() {
		getWebData();
		this.commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
		img.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(coachId >=0){
					Intent i = new Intent(SuccessDialogActivity.this ,CoachDetailActivity.class);
					i.putExtra(CoachDetailActivity.COACH_ID, coachId);
					startActivity(i);
				}else{
					AppUtil.ShowShortToast(getApplicationContext(), "教练数据获取异常");
				}
			}
			
		});
	}
	
	/**
	 * 获取网络数据
	 */
	private void getWebData(){
	    HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", tempOrderId+"");
		NetUtil.requestStringData(SRL.Method.METHOD_GET_SUCCESS_ORDER, map,  new Response.Listener<String>() {
		
			@Override
			public void onResponse(String result) {
				//{"cohInfo":{"address":"成都市双流县长江路188号","coachId":21,"coachName":"雷猴","countcomment":2,
//				"path":"/attachment/coh-head/image/IMG_2015021415243200907519.jpg","schoolName":"蜀娟驾校"},
//				"distance":283}
				Log.i(TAG, result) ;
				    JSONObject json = null ;
				    try{
				    	json = new JSONObject(result);
				    	distance.setText("距我"+(json.optInt("distance" ,0)/1000.0)+"k");
				    	JSONObject sjson = json.getJSONObject("cohInfo");
				    	if(sjson != null){
				    		coachId = sjson.optInt("coachId" ,-1);
				    		coachname.setText(sjson.optString("coachName" ,"无"));
				    		coachname.append("("+sjson.optString("schoolName" ,"无")+")");
				    		field.append(sjson.optString("address" ,"无"));
				    		score.setText((float)(sjson.optDouble("coachScore" ,5))+"分");
				    		rating.setRating((float)(sjson.optDouble("coachScore" ,5)));
				    		NetUtil.requestLoadImage(img, sjson.getString("path"), R.drawable.ic_defualt_image);
				    		
				    	}
				    }catch(Exception e){
				    	   e.printStackTrace() ;
				    }
			}
			
		}, new DefaultErrorListener());
	}

}
