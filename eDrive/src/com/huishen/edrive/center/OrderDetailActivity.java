package com.huishen.edrive.center;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.OnProgressChangedListener;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.SimpleRecorder;
import com.huishen.edrive.widget.RoundImageView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
   private Button judgeBtn ,cancelOrderBtn ;
   private RoundImageView coachphoto ;
   private RatingBar coachrating ;
   private LinearLayout vidiolay  ,coachlay;
   private ListView list ;
   private ArrayList<HashMap<String ,Object>> listdata = new ArrayList<HashMap<String,Object>>();
   private ProgressDialog MyDialog ;
   private File andioFile ;
   private int commentId ;
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
		list = (ListView)findViewById(R.id.order_detail_list);
		judgeBtn = (Button)findViewById(R.id.order_detail_btn_judge);
		cancelOrderBtn = (Button)findViewById(R.id.order_detail_cancelorder);
		
	}
	private void initView() {
		title.setText("订单详情");
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		cancelOrderBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				cancelOrder();
			}
			
		});
		if(coachId != -1){
		titlenote.setText("投诉");
		titlenote.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(OrderDetailActivity.this,ComplainActivity.class);
				i.putExtra("coachId", coachId);
				startActivity(i);
			}
			
		});
		}
		
	}
	
	private void cancelOrder(){
		MyDialog = ProgressDialog.show(this, "提示" , " 取消中... ", true);
		if(!MyDialog.isShowing()){
		MyDialog.show();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", orderId+"");
		NetUtil.requestStringData(SRL.Method.METHOD_CANCEL_ORDER, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if(result == null || result.equals("")){
							AppUtil.ShowShortToast(OrderDetailActivity.this, "服务器繁忙");
						}else{
							try{
								JSONObject json = new JSONObject(result);
								if(json.getInt("status")==1){
									AppUtil.ShowShortToast(OrderDetailActivity.this, "订单已取消");
									finish();
								}
									
							}catch(Exception e){
								
							}
						}
						//关闭进度条
						if(MyDialog != null && MyDialog.isShowing()){
						    MyDialog.dismiss();
						}
					}
				},new DefaultErrorListener(MyDialog));
	}
	/**
	 * 获取订单数据
	 */
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
		 * "schoolName":"蜀娟驾校"},,"commentPlusInfo":{"commentPlusTime":"2015-01-27","content":"213"},
		 * "commtInfo":[{"commentPlusTime":"2015-01-27","content":"您还可以输入150 个字符",
		 * "contentTime":"2015-02-04","score":4.0}],"distance":7147151,"tempBillInfo":
		 * {"audio":"/attachment/audio/34/FILE_2015031015130108025507.mp3",
		 * "content":"能45-60天拿证,考试包接包送,","stuGps":"104.062611,30.578016"}}
		 */
		try{
			JSONObject json =new JSONObject(data);
			JSONObject tempBillInfo = json.optJSONObject("tempBillInfo");
			ordercontent.setText(tempBillInfo.optString("content", ""));
			if(!tempBillInfo.optString("audio", "").equals("")){
				downloadFile(tempBillInfo.optString("audio", ""));
				vidiolay.setVisibility(View.VISIBLE);
				vidio.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						SimpleRecorder recorder = SimpleRecorder.getInstance();
						if(andioFile !=null && andioFile.length()>0){
						    recorder.playAudioFile(andioFile);
						}else{
							AppUtil.ShowShortToast(getApplicationContext(), "语音下载失败");
						}
					}
					
				});
			}else{
				vidiolay.setVisibility(View.GONE);
			}
            JSONObject coach = json.optJSONObject("coachInfo");
            
			if(coach != null){
				coachlay.setVisibility(View.VISIBLE);
				coachdistance.setText("距我"+(json.optInt("distance" ,0)/1000.0)+"k");
				coachname.setText(coach.optString("coachName",""));
				coachfield.append(coach.optString("address" ,"无"));
				coachjudge.setText((float)(coach.optDouble("coachScore" ,5))+"分");
				coachrating.setRating((float)(coach.optDouble("coachScore" ,5)));
	    		if(!coach.optString("path","").equals("")){
	    		NetUtil.requestLoadImage(coachphoto, coach.getString("path"), R.drawable.photo_coach_defualt);
	    		}
			}else{
				coachlay.setVisibility(View.GONE);
				judgeBtn.setVisibility(View.GONE);
			}
			
			JSONArray commtInfo = json.optJSONArray("commtInfo");
			if(commtInfo != null){
				listdata.clear();
				String[] from = new String[]{"time","content"};
				int[] to = new int[]{R.id.judge_listitem_stuname,R.id.judge_listitem_content};
				for(int i = 0 ;i <commtInfo.length() ;i++){
					JSONObject judge = commtInfo.getJSONObject(i);
					HashMap<String ,Object> map = new HashMap<String ,Object>();
					map.put("time", judge.optString("contentTime", ""));
					map.put("content", judge.optString("content", ""));
					map.put("rating", judge.optDouble("score", 5.0));
					listdata.add(map);
					commentId = judge.optInt("commentId", -1);
				}
				if(commtInfo.length() <1){
					judgeBtn.setVisibility(View.VISIBLE);
					judgeBtn.setText("评价");
					judgeBtn.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							intentjudgeActivity(1);
						}
						
					});
				}else{
				JSONObject plusjudge = json.optJSONObject("commentPlusInfo");
				if(plusjudge != null){
					HashMap<String ,Object> map = new HashMap<String ,Object>();
					map.put("time", plusjudge.optString("commentPlusTime", ""));
					map.put("content", plusjudge.optString("content", ""));
					map.put("rating", -1.0);
					listdata.add(map);
					judgeBtn.setVisibility(View.GONE);
				}else{
					judgeBtn.setVisibility(View.VISIBLE);
					judgeBtn.setText("追加评价");
					judgeBtn.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							intentjudgeActivity(2);
						}
						
					});
				}
				}
				JudgeListSimAdapter adapter = new JudgeListSimAdapter(this,listdata,R.layout.judge_list_item,from,to);
				list.setAdapter(adapter);
			}else{
				coachlay.setVisibility(View.GONE);
			}
		}catch(Exception e){
			e.printStackTrace() ;
		}
	
	}
	
	/**
	 * 跳转到评价页面
	 * @param tag
	 */
	private void intentjudgeActivity(int tag){
		Intent i = new Intent(this,JudgeActivity.class);
		i.putExtra("tag", tag);
		i.putExtra("coachId" ,coachId);
		if(commentId != -1){
			i.putExtra("commentId", commentId);
		}
		startActivity(i);
	}
	/**
	 * 下载语音文件
	 */
	private void downloadFile(String path){
		MyDialog = ProgressDialog.show(this, "提示" , " 加载中... ", true);
		MyDialog.show();
		andioFile =new File(Environment.getExternalStorageDirectory()+"/eDrive/audio/"+"andio.mp3");
		NetUtil.requestDownloadFile(path, andioFile, new OnProgressChangedListener() {
			
			@Override
			public void onTaskFinished() {
				//关闭进度条
				if(MyDialog.isShowing()){
				    MyDialog.dismiss();
				}
				AppUtil.ShowShortToast(getApplicationContext(), "语音下载完成");
				
			}
			
			@Override
			public void onTaskFailed() {
				//关闭进度条
				if(MyDialog.isShowing()){
				    MyDialog.dismiss();
				}
				AppUtil.ShowShortToast(getApplicationContext(), "语音下载失败");
				andioFile = null;
			}
			
			@Override
			public void onProgressChanged(int min, int max, int progress) {
				
			}
		});
	}
}
