package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.SplashActivity;
import com.huishen.edrive.apointment.UnBindCoachActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.BaseActivity;
import com.huishen.edrive.widget.LoadingDialog;
import com.huishen.edrive.widget.LoadingView;
import com.huishen.edrive.widget.RoundImageView;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 教练详情
 * @author zhanghuan
 * 
 */
public class CoachDetailActivity extends BaseActivity implements OnClickListener ,SwipeRefreshLayout.OnRefreshListener{
	private String TAG = "CoachDetailActivity" ;
	private TextView title ,note , coachname , goodnum ,judgescore ,detailContent,stunum;
	private TextView demandnum ,judgenum ,ranking ;
	private RatingBar judgerating ;
    private Button call ; //呼叫教练
    private ImageButton good  , back; //点赞按钮 ,返回按钮
    private LinearLayout field ,fieldimg ,detail , judge ,setmeal ,content ;
    private RoundImageView img ;
    private LoadingView loading ; //加载页
    private LoadingDialog dialog ; //加载弹出框
	//下拉刷新组件
	public SwipeRefreshLayout mSwipeLayout;
    //初始化相关
    private int coachId = -1  ;
    public static String COACH_ID = "id" ;  //传进来的参数key
    private String coachtel ; //教练的电话号码
    private float score = 0 ; //教练评分
    private int tag = 0; //1是绑定教练界面
    //展示训练场图片的弹出框相关
    private CoachFieldImgDialog imgDialog ;
    private ArrayList<String> imgUrls ;
    
	/***************************腾讯统计相关框架*************************************/
    
    //继承BaseActivity已经集成
	@Override
	protected void onDestroy() {
		if(dialog.isShowing()){
			dialog.dismiss();
		}
		super.onDestroy();
	}
	/***************************腾讯统计基本框架结束*************************************/
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach_detail);
		AppController.getInstance().addActivity(this);
		this.setTag("CoachDetailActivity");
		/****************获取传进来的数据***************/
		coachId = this.getIntent().getIntExtra(COACH_ID, -1) ;
		tag = this.getIntent().getIntExtra("tag", 0);
		/****************获取传进来的数据结束***************/		
		registView(); //注册组件
		initView();  //初始化组件
		getNetData(); 
	}
	
	/**
	 * 访问网络，获取网络数据
	 * 
	 */
	private void getNetData() {
		if(coachId == -1){
			AppUtil.ShowShortToast(this,"获取数据异常") ;
			return ;
		}
	    
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(COACH_ID, coachId+"");
//		map.put(SRL.Param.PARAM_LATITUDE, lat+"");
		NetUtil.requestStringData(SRL.Method.METHOD_GET_COACH_DETAIL,TAG, map, new Response.Listener<String>() {

			@Override
			public void onResponse(String result) {
				if(result == null || result.equals("")){
					AppUtil.ShowShortToast(getApplicationContext(), "获取数据异常");
				}else{
					setViewData(result) ;
				}
				if(mSwipeLayout.isRefreshing()){
					mSwipeLayout.setRefreshing(false);
				}
			}
			
		}, new DefaultErrorListener(this,null ,loading ,mSwipeLayout)) ;
	}
	
	/**
	 * 网络获取数据后设置界面数据
	 * @param result
	 */
	private void setViewData(String result){
//		{"ranking":40,"reputably":95,"result":
//		{"coachName":"雷猴","coachScore":0.0,
//			"id":21,"orderCount":0,"path":"/attachment/coh-head/image/IMG_2015021415243200907519.jpg","phone":"18200390901"}}
		
//		{"countCohInfo":{"orderNum":2,"sort":1},
//		"result":{"coachName":"魏巍","coachScore":0.0,
//		"id":89,"path":"/attachment/coh-head/image/IMG_2015032112163203868727.jpg",
//		"phone":"15310622047"},"stunum":1}
		Log.i(TAG, result) ;
		try{
			JSONObject json = new JSONObject(result);
			JSONObject jsonco = json.optJSONObject("countCohInfo") ;
			if(jsonco != null){
		    demandnum.setText(jsonco.optInt("orderNum")+"") ;
			judgenum.setText(jsonco.optInt("recom" ,100)+"%") ;
			ranking.setText(jsonco.optInt("sort",1)+"");
			}
			stunum.setText(json.optString("stunum" ,"0"));
			JSONObject jsonb = json.getJSONObject("result") ;
			if(jsonb != null){
//				demandnum.setText(jsonb.getInt("orderCount")+"") ;
				coachname.setText(jsonb.optString("coachName" ,"暂无")) ;
				judgescore.setText(jsonb.optDouble("coachScore",0)+"分");
				score = (float)jsonb.optDouble("coachScore",0) ;
				
				judgerating.setRating((float)jsonb.optDouble("coachScore",0));
				
				coachtel = jsonb.optString("phone" ,"");
				if(!jsonb.optString("path" ,"").equals("")){					
				   NetUtil.requestLoadImage(img, jsonb.getString("path"), R.drawable.photo_coach_defualt);
				}
				
			}
			if(content.getVisibility() == View.GONE){
			content.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
			}		
			}catch(Exception e){
				AppUtil.ShowShortToast(getApplicationContext(), "数据解析异常");
				loading.showFailLoadidng();
				e.printStackTrace();
			}
	}
	
	private void registView() {
		title = (TextView)this.findViewById(R.id.header_title);
		note = (TextView)findViewById(R.id.header_note);
		back = (ImageButton)this.findViewById(R.id.header_back) ;
		call = (Button)this.findViewById(R.id.coach_detail_btn_call) ;
		good = (ImageButton)this.findViewById(R.id.coach_detail_btn_good) ;
		field = (LinearLayout)this.findViewById(R.id.coach_detail_field) ;
		fieldimg = (LinearLayout)this.findViewById(R.id.m_center_lay_nickname) ;
		detail = (LinearLayout)this.findViewById(R.id.coach_detail_lay) ;
		judge = (LinearLayout)this.findViewById(R.id.coach_detail_judge) ;
		setmeal = (LinearLayout)this.findViewById(R.id.m_center_lay_addr) ;
		this.detailContent = (TextView)this.findViewById(R.id.coach_detail_tv_content) ;
		loading = (LoadingView)findViewById(R.id.coachdetail_loading);
		content = (LinearLayout)findViewById(R.id.coachdetail_content);
		mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.coachdetail_swipely);
		//教练基本信息
		coachname = (TextView)this.findViewById(R.id.coach_detail_tv_coachname) ;
		goodnum = (TextView)this.findViewById(R.id.coach_detail_tv_good) ;
		judgescore = (TextView)this.findViewById(R.id.coach_detail_judgescore) ;
		judgerating = (RatingBar)this.findViewById(R.id.coach_detail_judge_ratingbar) ;
		demandnum = (TextView)this.findViewById(R.id.coach_detail_demandnum) ;
		judgenum = (TextView)this.findViewById(R.id.coach_detail_judgenum) ;
		ranking = (TextView)this.findViewById(R.id.coach_detail_ranking) ;
		stunum = (TextView)this.findViewById(R.id.coach_detail_stunum);
		img = (RoundImageView)findViewById(R.id.coach_detail_img_photo) ;
	}
	
	
	private void initView() {
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorScheme(R.color.color_refresh_1, R.color.color_refresh_2,
				R.color.color_refresh_3, R.color.color_refresh_4);
		this.title.setText(this.getResources().getString(R.string.coach_detail));
		dialog = new LoadingDialog(this);
		this.back.setOnClickListener(this) ;
		this.good.setOnClickListener(this) ;
		this.field.setOnClickListener(this) ;
		this.fieldimg.setOnClickListener(this) ;
		this.detail.setOnClickListener(this) ;
		this.judge.setOnClickListener(this) ;
		this.setmeal.setOnClickListener(this) ;
	
		this.call.setOnClickListener(this);
		
		//第二版新增分享功能
		Drawable nav_up=getResources().getDrawable(R.drawable.ic_share);
		nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
		note.setCompoundDrawables(null,nav_up, null, null);
		
		note.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent sendIntent = new Intent();
				// 分享内容
				String shareContent = "e驾学车老总发飙了！有钱！任性！转发就领100元学车券！迎娶白富美从此走上人生巅峰" ;
				String shareCoach =shareContent+NetUtil.getAbsolutePath("share/coh")+"?coachId="+coachId; //分享的内容
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, shareCoach);
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, "选择接收者"));
			}
			
		});
		//新增功能结束
		
		if(tag == 1){
			
//			note.setText("投诉");
//			note.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View arg0) {
//					Intent i = new Intent(CoachDetailActivity.this ,ComplainActivity.class);
//					i.putExtra("coachId",coachId);
//					startActivity(i);
//				}
//				
//	  		});
			this.call.setText("解除绑定");
		}
		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.header_back:
			finish();
			break ;
		case R.id.coach_detail_btn_good:
			actionGood();
			break ;
		case R.id.coach_detail_field:
			Intent i = new Intent(this ,CoachTrainFieldActivity.class);
			i.putExtra(CoachDetailActivity.COACH_ID, coachId) ;
			this.startActivity(i);
			break ;
		case R.id.m_center_lay_nickname:
			actionFieldImg();
			break;
		case R.id.coach_detail_lay:
			if(this.detailContent.getVisibility() == View.GONE){
			this.detailContent.setVisibility(View.VISIBLE);
			getDetail();
			}else{
				this.detailContent.setVisibility(View.GONE) ;
			}
			
			break ;	
		case R.id.coach_detail_judge:
			actionJudgeLay() ;
			break;
		case R.id.m_center_lay_addr:
			actionMealLay() ;
			break;
		case R.id.coach_detail_btn_call:
			if(tag == 1){
				Intent ib = new Intent(this ,UnBindCoachActivity.class);
				ib.putExtra("coachId", coachId);
				startActivity(ib);
			}else
			if(coachtel == null || coachtel.equals("")){
				AppUtil.ShowShortToast(getApplicationContext(), "对不起，此教练没有电话号码") ;
			}else{
				
			//第二版功能
				sendtelLog();
		    //第二版功能结束
			Uri uri = Uri.parse("tel:"+coachtel);
			Intent it = new Intent(Intent.ACTION_CALL, uri);
			startActivity(it);
			}
			break ;
		}
	}
	
	/**
	 * 发送通话记录
	 */
	private void sendtelLog(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId", coachId+"");
		map.put("stuId", Prefs.readString(this, Const.USER_ID));
		map.put("fromType", 1+"");
//		map.put(SRL.Param.PARAM_LATITUDE, lat+"");
		NetUtil.requestStringData(SRL.Method.METHOD_CONTROL_PHONE,TAG, map, new Response.Listener<String>() {

			@Override
			public void onResponse(String result) {
				Log.i(TAG, "通话监听返回值："+result);
			      
			}
			
		}, new DefaultErrorListener(this,null ,loading ,mSwipeLayout)) ;
	}
	/**
	 * 响应学员评价按钮，跳转到学员评价列表
	 */
	private void actionJudgeLay(){
		
		Intent i = new Intent(this ,ListActivity.class);
		i.putExtra(ListActivity.STATUS_KEY, ListActivity.STATUS_JUDGE) ;
		
		i.putExtra(ListActivity.ID_KEY, coachId) ;
		i.putExtra("coachName", coachname.getText().toString());
		i.putExtra("score",score);
		this.startActivity(i);
	}
	/**
	 * 点击教练详情，获取教练信息
	 */
	private void getDetail(){
		if(coachId == -1){
			AppUtil.ShowShortToast(this,"获取数据异常") ;
			this.finish() ;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(COACH_ID, coachId+"");
		NetUtil.requestStringData(SRL.Method.METHOD_GET_COACH_INFO,TAG, map,new Response.Listener<String>() {

			@Override
			public void onResponse(String result) {
//				Log.i(TAG, result) ;
				if(result == null || result.equals("")){
					detailContent.setText("暂无");
				}else{
					//{"content":"成都龙泉","coachAge":6,"schoolName":"蜀娟驾校","shuttleAddress":"成都市高新区"}
					//教练教龄，所属驾校，可接送地点，教学特点
					try{
						JSONObject json = new JSONObject(result);
					 	
					 detailContent.setText("教练教龄：" +json.optInt("coachAge",0)+"年"+"\n");
					 detailContent.append("所属驾校：" +json.optString("schoolName","暂无")+"\n");
					 detailContent.append("可接送地点：" +json.optString("shuttleAddress","暂无")+"\n");
					 detailContent.append("教学特点：" +json.optString("content","暂无")+"\n");
					}catch(Exception e){
						
					}
				}
				
			}
			
		}, new DefaultErrorListener(this)) ;
	}
	/**
	 * 响应学车套餐按钮，跳转到学员评价列表
	 */
	private void actionMealLay(){
		Intent i = new Intent(this ,ListActivity.class);
		i.putExtra(ListActivity.STATUS_KEY, ListActivity.STATUS_SETMEAL) ;
		i.putExtra(ListActivity.ID_KEY, coachId) ;
		this.startActivity(i);
	}
	
	/**
	 * 弹出训练场图片展示框
	 */
	private void actionFieldImg(){
		if(imgUrls == null){
			imgUrls = new ArrayList<String>();
		}
		    imgUrls.clear();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(COACH_ID, coachId+"");
			if(!dialog.isShowing()){
				dialog.show();
			}
			NetUtil.requestStringData(SRL.Method.METHOD_GET_COACH_FIELDPIC,TAG , map,new Response.Listener<String>() {

				@Override
				public void onResponse(String result) {
					dialog.dismiss();
					if(result == null || result.equals("")){
						AppUtil.ShowShortToast(getApplicationContext(), "此教练没有图片") ;
					}else{

//                      [{"path":"/static/img/coachHeader.png"},
//                     {"path":"/static/img/coachHeader.png"}]
						try{
							JSONArray array = new JSONArray(result);
							for(int i = 0 ;i<array.length() ;i++){
								JSONObject json = array.getJSONObject(i);
								imgUrls.add(json.optString("path" ,""));
							}
							if(imgDialog == null){
								imgDialog = new CoachFieldImgDialog(CoachDetailActivity.this,imgUrls);
							}else{
								imgDialog.reStart(imgUrls);
								
							}
							imgDialog.show() ;
							WindowManager windowManager = getWindowManager();
							Display display = windowManager.getDefaultDisplay();
							WindowManager.LayoutParams lp = imgDialog.getWindow().getAttributes();
							lp.width = (int)(display.getWidth()); //设置宽度
							imgDialog.getWindow().setAttributes(lp);
						}catch(Exception e){
							e.printStackTrace();
						}
					
					}
				}
				
			}, new DefaultErrorListener(this ,dialog));
	
	}
	/**
	 * 点击赞响应事件
	 */
	private void actionGood() {
		
	}

	@Override
	public void onRefresh() {
		getNetData();
	}	
	
}
