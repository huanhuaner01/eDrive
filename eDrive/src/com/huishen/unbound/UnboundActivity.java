package com.huishen.unbound;

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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class UnboundActivity extends Activity {

	// 定位相关
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		private LocationMode mCurrentMode;
		BitmapDescriptor mCurrentMarker;

		MapView mMapView;
		BaiduMap mBaiduMap;

		// UI相关
		boolean isFirstLoc = true;// 是否首次定位

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_unbound);
			mCurrentMode = LocationMode.NORMAL;
			// 地图初始化
			mMapView = (MapView) findViewById(R.id.bmapView);
			mBaiduMap = mMapView.getMap();
			// 开启定位图层
			mBaiduMap.setMyLocationEnabled(true);
			// 定位初始化
			mLocClient = new LocationClient(this);
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(1000);
			mLocClient.setLocOption(option);
			mLocClient.start();
		}

		/**
		 * 定位SDK监听函数
		 */
		public class MyLocationListenner implements BDLocationListener {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// map view 销毁后不在处理新接收的位置
				if (location == null || mMapView == null)
					return;
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);
				if (isFirstLoc) {
					isFirstLoc = false;
					LatLng ll = new LatLng(location.getLatitude(),
							location.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,15);
					Log.w("LocationDemo","定位地理信息："+location.getLatitude()+","+location.getLongitude());
					mBaiduMap.animateMapStatus(u);
					
					//定义Maker坐标点  
					LatLng point = new LatLng(30.578888,104.053333);  
					//构建Marker图标  
					BitmapDescriptor bitmap = BitmapDescriptorFactory  
					    .fromResource(R.drawable.icon_gcoding);  
					//构建MarkerOption，用于在地图上添加Marker  
					OverlayOptions option = new MarkerOptions()  
					    .position(point)  
					    .icon(bitmap);  
					//在地图上添加Marker，并显示  
					mBaiduMap.addOverlay(option);
					mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener(){

						@Override
						public boolean onMarkerClick(Marker marker) {
							
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

		@Override
		protected void onDestroy() {
			// 退出时销毁定位
			mLocClient.stop();
			// 关闭定位图层
			mBaiduMap.setMyLocationEnabled(false);
			mMapView.onDestroy();
			mMapView = null;
			super.onDestroy();
		}

}
