package com.huishen.edrive.demand;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
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
import com.huishen.edrive.R.drawable;
import com.huishen.edrive.R.id;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.center.CoachDetailActivity;
import com.huishen.edrive.login.VerifyPhoneActivity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 需求主页面
 * @author zhanghuan
 *
 */
public class DemandActivity extends Activity implements OnClickListener{
	//常量
	private int STATUS_INPUT = 0 ; //文字输入
	private int STATUS_SOUND = 1 ; //语音输入
	public static String FIRST_MAIN = "isFirstMain" ;
	
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
	boolean isFirstLoc = true;//
	boolean isFirstMain = false ; //是否是第一次启动的主页
    private int coachId = -1 ; //目前点击的教练的id 默认-1没有
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/***************获取传递的数据*******************/
		//这里获取A传过来的数据     

		isFirstMain  = this.getIntent().getBooleanExtra(this.FIRST_MAIN, false);    
		 //这里通过key的方式获取值     
		/*******************************************/
		
		setContentView(R.layout.activity_demand);
		mCurrentMode = LocationMode.NORMAL;
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		
		mBaiduMap.setMyLocationEnabled(true);
		
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);//
		option.setCoorType("bd09ll"); //
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
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
		this.switchbtn.setOnClickListener(this) ;
		this.switchlay.setOnClickListener(this);
		this.back.setImageResource(R.drawable.back_ic) ;
		
		this.back.setOnClickListener(this) ;
		
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
				double x=30.578888 ;
				double y = 104.053333 ;
				for(int i = 0 ; i<10 ;i++){
					LatLng point = new LatLng(x,y);
					BitmapDescriptor bdA = BitmapDescriptorFactory
							.fromResource(R.drawable.icon_gcoding);
					OverlayOptions ooA = new MarkerOptions().position(point).icon(bdA)
							.zIndex(9).draggable(true);
					
					Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
					Bundle bundle = new Bundle();  
			        bundle.putString("id", i+"");
					marker.setExtraInfo(bundle) ;
					x = x + 0.001 ;
					y = y + 0.001 ;  
				}
				mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener(){

					@Override
					public boolean onMarkerClick(Marker marker) {
						String id = marker.getExtraInfo().getString("id") ;
						coachId = Integer.parseInt(id);
						View view = LayoutInflater.from(DemandActivity.this).inflate(R.layout.demand_info_window, null);
						TextView tv = (TextView)view.findViewById(R.id.demand_info_coach) ;
						tv.setText(id+"教练") ;
						InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view),marker.getPosition() ,-50 ,listener);
						mBaiduMap.showInfoWindow(mInfoWindow);
						return false;
					}
					
				});
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
    
	private OnInfoWindowClickListener listener =   new OnInfoWindowClickListener()  
    {  
		  
        @Override  
        public void onInfoWindowClick()  
        {  
        	if(Prefs.checkUser(DemandActivity.this)){
           Intent i = new Intent(DemandActivity.this,CoachDetailActivity.class);
//           Bundle bundle = new Bundle();
//           bundle.putInt("coachId", coachId) ;
           i.putExtra("coachId", coachId) ;
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
		case R.id.header_back:
			if(isFirstMain){
				Intent i = new Intent(DemandActivity.this,MainActivity.class);
				}else{
					DemandActivity.this.finish() ;
				}
			break ;
		case R.id.demand_btn_switch:
			switchStatus() ;
			break ;
		case R.id.demand_lay_switch:
			intentPostDemand() ;
			break ;
		}
	}
}
