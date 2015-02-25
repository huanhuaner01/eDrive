package com.huishen.edrive.center;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
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

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.Activity;

/**
 * 显示教练训练场地的activity
 * @author zhanghuan
 *
 */
public class CoachTrainFieldActivity extends Activity {
	//标题栏相关
	private TextView title ; 
	private ImageButton back ; 
	//地图相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private boolean isFirstLoc = true;//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach_train_field);
	    mCurrentMode = LocationMode.NORMAL;
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		registView();
		initView() ;
	}
	private void registView() {
		
	}
	private void initView() {

		mBaiduMap.setMyLocationEnabled(true);
		
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);//
		option.setCoorType("bd09ll"); //
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
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
							.fromResource(R.drawable.coach_field_ic);
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
//						String id = marker.getExtraInfo().getString("id") ;
//						coachId = Integer.parseInt(id);
//						View view = LayoutInflater.from(DemandActivity.this).inflate(R.layout.demand_info_window, null);
//						TextView tv = (TextView)view.findViewById(R.id.demand_info_coach) ;
//						tv.setText(id+"教练") ;
//						InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view),marker.getPosition() ,-50 ,listener);
//						mBaiduMap.showInfoWindow(mInfoWindow);
						return false;
					}
					
				});
				
				mLocClient.stop();
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
