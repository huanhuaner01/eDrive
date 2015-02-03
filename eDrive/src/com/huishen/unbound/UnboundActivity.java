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

	// ��λ���
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		private LocationMode mCurrentMode;
		BitmapDescriptor mCurrentMarker;

		MapView mMapView;
		BaiduMap mBaiduMap;

		// UI���
		boolean isFirstLoc = true;// �Ƿ��״ζ�λ

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_unbound);
			mCurrentMode = LocationMode.NORMAL;
			// ��ͼ��ʼ��
			mMapView = (MapView) findViewById(R.id.bmapView);
			mBaiduMap = mMapView.getMap();
			// ������λͼ��
			mBaiduMap.setMyLocationEnabled(true);
			// ��λ��ʼ��
			mLocClient = new LocationClient(this);
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// ��gps
			option.setCoorType("bd09ll"); // ������������
			option.setScanSpan(1000);
			mLocClient.setLocOption(option);
			mLocClient.start();
		}

		/**
		 * ��λSDK��������
		 */
		public class MyLocationListenner implements BDLocationListener {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// map view ���ٺ��ڴ����½��յ�λ��
				if (location == null || mMapView == null)
					return;
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);
				if (isFirstLoc) {
					isFirstLoc = false;
					LatLng ll = new LatLng(location.getLatitude(),
							location.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,15);
					Log.w("LocationDemo","��λ������Ϣ��"+location.getLatitude()+","+location.getLongitude());
					mBaiduMap.animateMapStatus(u);
					
					//����Maker�����  
					LatLng point = new LatLng(30.578888,104.053333);  
					//����Markerͼ��  
					BitmapDescriptor bitmap = BitmapDescriptorFactory  
					    .fromResource(R.drawable.icon_gcoding);  
					//����MarkerOption�������ڵ�ͼ�����Marker  
					OverlayOptions option = new MarkerOptions()  
					    .position(point)  
					    .icon(bitmap);  
					//�ڵ�ͼ�����Marker������ʾ  
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
			// �˳�ʱ���ٶ�λ
			mLocClient.stop();
			// �رն�λͼ��
			mBaiduMap.setMyLocationEnabled(false);
			mMapView.onDestroy();
			mMapView = null;
			super.onDestroy();
		}

}
