package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.widget.RoundImageView;

import android.app.Activity;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 订单详情页面
 * @author zhanghuan
 *
 */
public class OrderDetailActivity extends Activity {
   private String TAG = "OrderDetailActivity" ;
   private TextView title ,ordercontent ,coachname ,coachfield ,coachjudge ,coachdistance,titlenote ;
   private int orderId ,coachId ;
   private ImageButton vidio ,back ;
   private RoundImageView coachphoto ;
   private RatingBar coachrating ;
   private LinearLayout vidiolay  ,coachlay;
   private ListView list ;
   private ArrayList<HashMap> listdata = new ArrayList<HashMap>();
	@Override
    protected void onResume() {
	 super.onResume();
	 getWebDate();
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		AppController.getInstance().addActivity(this);
		//---------------------获取数据-----------------------
		orderId = this.getIntent().getIntExtra("id", -1);
		coachId = this.getIntent().getIntExtra("coachId", -1);
		//---------------------获取数据结束！--------------------
		registView();
		initView();
	}
	private void registView() {
		title = (TextView)findViewById(R.id.header_title);
		titlenote = (TextView)findViewById(R.id.header_note);
		ordercontent = (TextView)findViewById(R.id.order_detail_content);
		coachname = (TextView)findViewById(R.id.order_detail_cname);
		coachfield = (TextView)findViewById(R.id.order_detail_cfield);
		coachdistance = (TextView)findViewById(R.id.order_detail_distance);
		coachjudge = (TextView)findViewById(R.id.order_detail_cjudge);
		vidio = (ImageButton)findViewById(R.id.order_detail_img);
		back = (ImageButton)findViewById(R.id.header_back);
		coachrating = (RatingBar)findViewById(R.id.order_detail_coachrating);
		vidiolay = (LinearLayout)findViewById(R.id.order_detail_imglay);
		coachlay = (LinearLayout)findViewById(R.id.order_detial_coachlay);
		coachphoto = (RoundImageView)findViewById(R.id.order_detail_cphoto);
	}
	private void initView() {
		title.setText("订单详情");
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		
	}
	private void getWebDate(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", orderId+"");
		if(coachId != -1){
			map.put("coachId", coachId+"");
		}
		NetUtil.requestStringData(SRL.Method.METHOD_GET_ORDER_DETAIL, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if(result == null || result.equals("")){
							AppUtil.ShowShortToast(OrderDetailActivity.this, "服务器繁忙");
						}else{
							setData(result);
						}
						
					}
				},new DefaultErrorListener());
	}
	
	private void setData(String data){
		/**
		 * {"coachInfo":{"busNumber":"蜀K88888","coachName":"雷猴","coachScore":0.0,
		 * "path":"/attachment/coh-head/image/IMG_2015021415243200907519.jpg",
		 * "schoolName":"蜀娟驾校"},"commtInfo"::[{"commentPlusTime":"2015-01-27","content":"您还可以输入150 个字符",
		 * "contentTime":"2015-02-04","score":4.0}],"distance":7147151,"tempBillInfo":
		 * {"audio":"/attachment/audio/34/FILE_2015031015130108025507.mp3",
		 * "content":"能45-60天拿证,考试包接包送,","stuGps":"104.062611,30.578016"}}
		 */
		try{
			JSONObject json =new JSONObject(data);
			JSONObject tempBillInfo = json.optJSONObject("tempBillInfo");
			ordercontent.setText(tempBillInfo.optString("content", ""));
			if(!tempBillInfo.optString("audio", "").equals("")){
				
			}else{
				vidiolay.setVisibility(View.GONE);
			}
            JSONObject coach = json.optJSONObject("coachInfo");
            
			if(coach != null){
				coachdistance.setText("距我"+(json.optInt("distance" ,0)/1000.0)+"k");
				coachname.setText(coach.optString("coachName",""));
				coachfield.append(coach.optString("address" ,"无"));
				coachjudge.setText((float)(coach.optDouble("coachScore" ,5))+"分");
				coachrating.setRating((float)(coach.optDouble("coachScore" ,5)));
	    		if(!coach.optString("path","").equals("")){
	    		NetUtil.requestLoadImage(coachphoto, coach.getString("path"), R.drawable.ic_defualt_image);
	    		}
			}else{
				coachlay.setVisibility(View.GONE);
			}
			
			JSONArray commtInfo = json.optJSONArray("commtInfo");
			if(commtInfo != null){
				listdata.clear();
				String[] from = new String[]{"time",};
//				for(int i = 0 ;i <commtInfo.length() ;i++){
//					HashMap<String ,Object> map = new HashMap<String ,Object>();
//					map.put("", value);
//				}
			}else{
				coachlay.setVisibility(View.GONE);
			}
		}catch(Exception e){
			e.printStackTrace() ;
		}
	
	}
}
