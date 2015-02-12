package com.huishen.edrive.util;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.huishen.edrive.login.VerifyPhoneActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppUtil {
    
	public static void intentRegistActivity(Activity activity){
		Intent i = new Intent(activity ,VerifyPhoneActivity.class);
		activity.startActivity(i) ;
	}
	/**
	 * 初始化第一个gps按钮的显示，定位
	 * @param view
	 */
	public static void initgpsView(Context context ,BDLocationListener Listener){
		LocationClient mLocationClient =  new LocationClient(context.getApplicationContext());     //声明LocationClient类 ;
//		final BDLocation mlocation;
//		BDLocationListener Listener = new BDLocationListener() {
//
//			@Override
//			public void onReceiveLocation(BDLocation location) {
//				//Receive Location 
//				
////				StringBuffer sb = new StringBuffer(256);
//				if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//					mlocation = location ;
//				}
//			}
//
//			@Override
//			public void onReceivePoi(BDLocation arg0) {
//			
//			}
//		};
//		    mLocationClient = new LocationClient(this.mContext.getApplicationContext());     //声明LocationClient类
		    mLocationClient.registerLocationListener( Listener );    //注册监听函数
		    
		    LocationClientOption option = new LocationClientOption();
//		    option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		    option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		    option.setOpenGps(true);
		    option.disableCache(true);//禁止启用缓存定位
		    option.setAddrType("all");//返回的定位结果包含地址信息
		    mLocationClient.setLocOption(option); 
		    if(!mLocationClient.isStarted()){
		    mLocationClient.start();
		    }
		    Log.i("BaiduLocationApiDem", "应该在定位");
//		return mlocation ;
	}
}
