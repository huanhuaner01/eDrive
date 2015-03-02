package com.huishen.edrive.center;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
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
import com.huishen.edrive.demand.DemandActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.app.Activity;

/**
 * 显示教练训练场地的activity
 * @author zhanghuan
 *
 */
public class CoachTrainFieldActivity extends Activity {
	private String TAG = "CoachTrainFieldActivity" ;
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
	
	//初始化相关
    private int coachId = -1  ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach_train_field);
		/****************获取传进来的数据***************/
		
		coachId = this.getIntent().getIntExtra(CoachDetailActivity.COACH_ID, -1) ;
		/****************获取传进来的数据结束***************/	
	    mCurrentMode = LocationMode.NORMAL;
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		registView();
		initView() ;
	}
	private void registView() {
		this.title = (TextView) this.findViewById(R.id.header_title);
		this.back = (ImageButton) this.findViewById(R.id.header_back) ;
	}
	private void initView() {
        this.title.setText("训练场位置") ;
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
	
	/**
	 * 获取教练训练场数据，并显示到地图上
	 */
	private void getWebData(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(CoachDetailActivity.COACH_ID, coachId+"");
		NetUtil.requestStringData(SRL.Method.METHOD_GET_COACH_FIELD, map,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						try {
//						[{"GPSAddress":"104.073412|30.575186","address":"成都市双流县长江路188号","title":"容安1"}
//                       ,{"GPSAddress":"104.073413|30.575146","address":"成都市双流县长江路188号","title":"蜀娟1"}]
							JSONArray array = new JSONArray(result);
							for(int i = 0 ; i<array.length() ;i++){
								JSONObject json = array.getJSONObject(i);
								String gps = json.optString(SRL.ReturnField.FIELD_GPSADDR);
								String[] gpss = gps.split("\\|");
								Log.i(TAG, gps+" "+gpss[0]+" "+gpss[1]) ;
								if(gpss.length == 2){
								double lat = Double.parseDouble(gpss[1]);
								double lng = Double.parseDouble(gpss[0]) ;
								Log.i(TAG, gps+"") ;
								LatLng point = new LatLng(lat,lng);
								
								BitmapDescriptor bdA = BitmapDescriptorFactory
										.fromResource(R.drawable.coach_field_ic);
								OverlayOptions ooA = new MarkerOptions().position(point).icon(bdA)
										.zIndex(9).draggable(true);
								
								Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
								Bundle bundle = new Bundle();  
						        bundle.putString(SRL.ReturnField.FIELD_ADDR, json.optString(SRL.ReturnField.FIELD_ADDR));
						        bundle.putString(SRL.ReturnField.FIELD_SCHOOL, json.optString(SRL.ReturnField.FIELD_SCHOOL));
						      
								marker.setExtraInfo(bundle) ;
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener(){

							@Override
							public boolean onMarkerClick(Marker marker) {
								View view = LayoutInflater.from(CoachTrainFieldActivity.this).inflate(R.layout.demand_info_window, null);
								TextView name = (TextView)view.findViewById(R.id.demand_info_coach) ;
								ImageView img = (ImageView)view.findViewById(R.id.demand_window_photo) ;
								RatingBar ratingBar = (RatingBar)view.findViewById(R.id.demand_info_ratingbar);
								TextView juedge = (TextView)view.findViewById(R.id.demand_info_judge);
								ratingBar.setVisibility(View.GONE); //评分条
								img.setVisibility(View.GONE) ;
								juedge.setText(marker.getExtraInfo().getString(SRL.ReturnField.FIELD_ADDR)); //分数显示
								name.setText(marker.getExtraInfo().getString(SRL.ReturnField.FIELD_SCHOOL)) ; //教练名称显示
								InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view),marker.getPosition() ,-50 ,null);
								mBaiduMap.showInfoWindow(mInfoWindow);
								return false;
							}
							
						});
					}
				}, new DefaultErrorListener());
	
	
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
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,12);
				Log.w("LocationDemo","地理位置"+location.getLatitude()+","+location.getLongitude());
				mBaiduMap.animateMapStatus(u);
				getWebData();
				
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
