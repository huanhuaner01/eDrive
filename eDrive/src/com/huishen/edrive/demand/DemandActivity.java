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
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.huishen.edrive.R;
import com.huishen.edrive.R.drawable;
import com.huishen.edrive.R.id;
import com.huishen.edrive.R.layout;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DemandActivity extends Activity {
	//常量
	private int STATUS_INPUT = 0 ; //文字输入
	private int STATUS_SOUND = 1 ; //语音输入
	
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		this.switchbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				switchStatus() ;
			}
			
		}) ;
		
		this.back.setImageResource(R.drawable.back_ic) ;
		this.back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				DemandActivity.this.finish();
			}
			
		}) ;
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
					mBaiduMap.addOverlay(ooA);
					x = x + 0.001 ;
					y = y + 0.001 ;  
				}
				mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener(){

					@Override
					public boolean onMarkerClick(Marker marker) {
						View view = LayoutInflater.from(DemandActivity.this).inflate(R.layout.demand_info_window, null);
						InfoWindow mInfoWindow = new InfoWindow(view, marker.getPosition(), -50);
						mBaiduMap.showInfoWindow(mInfoWindow);
						return false;
					}
					
				});
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

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
}
