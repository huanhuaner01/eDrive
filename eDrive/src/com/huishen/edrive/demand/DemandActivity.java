package com.huishen.edrive.demand;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.huishen.edrive.MainActivity;
import com.huishen.edrive.R;
import com.huishen.edrive.center.CoachDetailActivity;
import com.huishen.edrive.login.VerifyPhoneActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.umeng.UmengServiceProxy;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Prefs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 需求主页面
 * @author zhanghuan
 *
 */
public class DemandActivity extends Activity implements OnClickListener{
	private String TAG = "DemandActivity" ;
	//常量
	private int STATUS_INPUT = 0 ; //文字输入
	private int STATUS_SOUND = 1 ; //语音输入
	public static String IS_MAIN = "isFirstMain" ; //第一次进入的主页面 
	
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	private MapView mMapView;
	private BaiduMap mBaiduMap;
    
	//UI相关
	private ImageButton back ; //标题栏返回键
	private ImageButton switchbtn ;
	private LinearLayout switchlay ;
	private TextView switchtv ;
	private int currentStatus = 0 ; //目前的输入状态
	private boolean isFirstLoc = true;//
	private boolean isFirstMain = false ; //标志，是否是第一次登录
    private int coachId = -1 ; //目前点击的教练的id 默认-1没有
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //将Activity添加进入栈
        AppController.getInstance().addActivity(this) ;
		/***************获取传递的数据*******************/
        
        isFirstMain = this.getIntent().getBooleanExtra(IS_MAIN, false) ;
        
		 //这里通过key的方式获取值     
		/*******************************************/
    	setContentView(R.layout.activity_demand);
		mCurrentMode = LocationMode.NORMAL;
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
	
		registView();
		initView() ;
	}

	/**
	 * 注册组件
	 */
	private void registView(){
		this.back = (ImageButton)this.findViewById(R.id.header_menu) ;
		this.switchbtn = (ImageButton)this.findViewById(R.id.demand_btn_switch) ;
		this.switchlay = (LinearLayout)this.findViewById(R.id.demand_lay_switch) ;
		this.switchtv = (TextView)this.findViewById(R.id.demand_tv_switch) ;
	}
	
	/**
	 * 初始化组件
	 */
	private void initView(){
	
		
		mBaiduMap.setMyLocationEnabled(true);
		
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);//
		option.setCoorType("bd09ll"); //
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		this.switchbtn.setOnClickListener(this) ;
		this.switchlay.setOnClickListener(this);
		this.back.setImageResource(R.drawable.back_ic) ;
		
		this.back.setOnClickListener(this) ;
		
	}
	
	private void showRoundCoach(double lng ,double lat){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SRL.Param.PARAM_LONGITUDE, lng+"");
		map.put(SRL.Param.PARAM_LATITUDE, lat+"");
		NetUtil.requestStringData(SRL.Method.METHOD_GET_ROUND_COACH, map,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						try {
							//返回值：[{"coachId":1,"coachName":"张三","coachScore":2.1,"issue":20,
//							"lat":30.575699,"lng":104.068216,
//							"path":"/static/img/coachHeader.png","phone":"3558657902"}]
							JSONArray array = new JSONArray(result);
							for(int i = 0 ; i<array.length() ;i++){
								JSONObject json = array.getJSONObject(i);
								Log.i(TAG, json.getDouble(SRL.Param.PARAM_LATITUDE)+"") ;
								LatLng point = new LatLng(json.getDouble(SRL.Param.PARAM_LATITUDE),json.getDouble(SRL.Param.PARAM_LONGITUDE));
								BitmapDescriptor bdA = BitmapDescriptorFactory
										.fromResource(R.drawable.ic_gcoding);
								OverlayOptions ooA = new MarkerOptions().position(point).icon(bdA)
										.zIndex(9).draggable(true);
								
								Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
								Bundle bundle = new Bundle();  
						        bundle.putInt(SRL.ReturnField.FIELD_COACH_ID, json.getInt(SRL.ReturnField.FIELD_COACH_ID));
						        bundle.putString(SRL.ReturnField.FIELD_COACH_NAME, json.optString(SRL.ReturnField.FIELD_COACH_NAME,"")) ;
						        bundle.putFloat(SRL.ReturnField.FIELD_COACH_JUDGE_SCORE, (float)json.optDouble(SRL.ReturnField.FIELD_COACH_JUDGE_SCORE,0)) ;
						        bundle.putString(SRL.ReturnField.FIELD_COACH_PHOTO_PATH, json.optString(SRL.ReturnField.FIELD_COACH_PHOTO_PATH,"")) ;
								marker.setExtraInfo(bundle) ;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener(){

							@Override
							public boolean onMarkerClick(Marker marker) {
								coachId = marker.getExtraInfo().getInt(SRL.ReturnField.FIELD_COACH_ID);
								View view = LayoutInflater.from(DemandActivity.this).inflate(R.layout.demand_info_window, null);
								TextView name = (TextView)view.findViewById(R.id.order_coach_name) ;
								ImageView img = (ImageView)view.findViewById(R.id.order_coach_photo) ;
								RatingBar ratingBar = (RatingBar)view.findViewById(R.id.order_coach_ratingbar);
								TextView juedge = (TextView)view.findViewById(R.id.order_coach_judge);
								ratingBar.setRating(marker.getExtraInfo().getFloat(SRL.ReturnField.FIELD_COACH_JUDGE_SCORE)) ; //评分条
								juedge.setText(marker.getExtraInfo().getFloat(SRL.ReturnField.FIELD_COACH_JUDGE_SCORE)+"分"); //分数显示
								name.setText(marker.getExtraInfo().getString(SRL.ReturnField.FIELD_COACH_NAME)) ; //教练名称显示
								if(!marker.getExtraInfo().getString(SRL.ReturnField.FIELD_COACH_PHOTO_PATH).equals("")){
								NetUtil.requestLoadImage(img,marker.getExtraInfo().getString(SRL.ReturnField.FIELD_COACH_PHOTO_PATH), R.drawable.photo_coach_defualt); //教练头像
								}
								InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view),marker.getPosition() ,-50 ,listener);
								mBaiduMap.showInfoWindow(mInfoWindow);
								return false;
							}
							
						});
						
						//单击地图隐藏弹出框
						mBaiduMap.setOnMapClickListener(new OnMapClickListener(){

							@Override
							public void onMapClick(LatLng arg0) {
					                mBaiduMap.hideInfoWindow();
							}

							@Override
							public boolean onMapPoiClick(MapPoi arg0) {
								// TODO Auto-generated method stub
								return false;
							}
							
						});
					}
				}, new DefaultErrorListener());
	
	
	}
	/**
	 * 输入状态转换
	 */
	private void switchStatus(){
		Log.i("DemandActivity", "currentStatus :"+currentStatus) ;
		   if(currentStatus == STATUS_INPUT){
			    currentStatus = STATUS_SOUND ;
		    	this.switchbtn.setImageResource(R.drawable.demand_input_white);
		    	this.switchlay.setBackgroundResource(R.drawable.demand_order_sound);
		    	this.switchtv.setTextColor(this.getResources().getColor(R.color.white));
		    	Drawable drawable= getResources().getDrawable(R.drawable.demand_sound_small);
		    	/// 这一步必须要做,否则不会显示.
		    	drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		    	this.switchtv.setCompoundDrawables(drawable,null,null,null);
		    	this.switchtv.setText(this.getResources().getString(R.string.demand_bottom_sound));
		    }else{
		    	currentStatus = STATUS_INPUT ;
		    	this.switchbtn.setImageResource(R.drawable.demand_sound);
		    	this.switchlay.setBackgroundResource(R.drawable.demand_order_tv);
		    	this.switchtv.setTextColor(this.getResources().getColor(R.color.black));
		    	Drawable drawable= getResources().getDrawable(R.drawable.demand_input);
		    	/// 这一步必须要做,否则不会显示.
		    	drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		    	this.switchtv.setCompoundDrawables(drawable,null,null,null);
		    	this.switchtv.setText(this.getResources().getString(R.string.demand_bottom_tv));
		    }
	}
	
	/**
	 * 跳转到需求发布界面
	 */
	private void intentPostDemand(){
		Intent i = null ;
		if(currentStatus == STATUS_INPUT){
			i = new Intent(this ,PostTxtActivity.class);
		}else{
			i = new Intent(this ,PostSoundActivity.class);
		}
		this.startActivity(i);
	}
	
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,15);
				Log.w("LocationDemo","地理位置"+location.getLatitude()+","+location.getLongitude());
				mBaiduMap.animateMapStatus(u);
				showRoundCoach(location.getLongitude() ,location.getLatitude());
			}
				
				mLocClient.stop();
			}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			
		}
	}
    
	private OnInfoWindowClickListener listener =   new OnInfoWindowClickListener()  
    {  
		  
        @Override  
        public void onInfoWindowClick()  
        {  
        	if(Prefs.checkUser(DemandActivity.this)){
           Intent i = new Intent(DemandActivity.this,CoachDetailActivity.class);
           i.putExtra(CoachDetailActivity.COACH_ID, coachId) ;
           DemandActivity.this.startActivity(i);
        	}else{
        		AppUtil.intentRegistActivity(DemandActivity.this);
        	}
        }  
    };
	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}
	
	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}
	@Override
	protected void onDestroy() {
		// 
		mLocClient.stop();
		//
		clearOverlay(null);
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	/**
	 * 所有按钮的监听事件
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		
		if(!Prefs.checkUser(DemandActivity.this)){
			AppUtil.intentRegistActivity(DemandActivity.this);
			return ;
		}
		switch(v.getId()){
		case R.id.header_menu:
			this.finish() ;
			break ;
		case R.id.demand_btn_switch:
			switchStatus() ;
			break ;
		case R.id.demand_lay_switch:
			intentPostDemand() ;
			break ;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "requestCode:"+requestCode+" resultCode:"+resultCode);
		if(resultCode == VerifyPhoneActivity.LOGIN_RESULT_CODE){
			//如果登录成功
			if(isFirstMain){
				UmengServiceProxy.startPushService(this);
			this.back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent i  = new Intent(DemandActivity.this,MainActivity.class);
					i.putExtra("main", false);
					DemandActivity.this.startActivity(i);
					DemandActivity.this.finish();
				}
				
			});
			
			}
//			initView();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	///////////////////////退出系统应用//////////////////////////////////////////
	 private int backindex = 0 ;

	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
       if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
    	   if(!Prefs.checkUser(getApplicationContext())){
            if(backindex == 0){
           	 Toast.makeText(DemandActivity.this,"再按一次退出e驾学车!", Toast.LENGTH_SHORT).show();
           	 backindex++;
           	 return false ;
            }
            AppController.getInstance().exit(this.getApplicationContext());
            return true;
    	   }
        }
        return super.onKeyDown(keyCode, event);
    }
	
	
}
