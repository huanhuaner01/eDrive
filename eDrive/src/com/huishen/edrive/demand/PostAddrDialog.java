package com.huishen.edrive.demand;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.huishen.edrive.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 定位对话框
 * @author zhanghuan
 * 
 */
public class PostAddrDialog extends Dialog implements View.OnClickListener 
,OnGetGeoCoderResultListener{
	private String TAG = "PostAddrDialog" ;
	private PostDialogInterface listener ;
    private Context context;
    private Button addrBtn ,commit ;
    private EditText addredit ;
//    private EditText cityedit ;
    private LocationClient mLocationClient ;
    private String address  ;
    private double  longitude ; //经度
    private double latitude ; //纬度
 // 搜索相关
    private GeoCoder mSearch ; // 搜索模块，也可去掉地图模块独立使用
    
    public PostAddrDialog(Context context , PostDialogInterface listener) {
        super(context,R.style.dataselectstyle);
        this.context = context;
        this.listener = listener ;
        mLocationClient = new LocationClient(context.getApplicationContext());     //声明LocationClient类
    }
    
    
    public PostAddrDialog(Context context, int theme ,PostDialogInterface listener){
        super(context, theme);
        this.context = context;
        this.listener = listener ;
        mLocationClient = new LocationClient(context.getApplicationContext());     //声明LocationClient类
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_post_lay);
        registView();
        init() ;
    }
    
    /**
     * 注册组件
     * @param view
     */
    private void registView(){
    	this.addrBtn = (Button) findViewById(R.id.order_detail_img);
    	this.addredit = (EditText) findViewById(R.id.post_dialog_edit);
//    	this.cityedit = (EditText) findViewById(R.id.post_dialog_cityedit);
    	this.commit = (Button) findViewById(R.id.order_success);
    }
    
    /**
     * 初始化
     */
    private void init(){
    	this.addrBtn.setOnClickListener(this) ;
    	this.commit.setOnClickListener(this) ;
    	//
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
//		setCancelable(false);//取消back键的监听
      //定义Maker坐标点  
//      LatLng point = new LatLng(30.575505, 104.06584); 
     
    }

	@Override
	public void onClick(View v) {
		Log.i(TAG,v.getId()+"") ;
		switch(v.getId()){
		case R.id.order_detail_img:
			gpsClickAction();
			break ;
		case R.id.order_success:
			if(addredit.getText().toString().equals("")){
				Toast.makeText(context, "地址栏不能为空", Toast.LENGTH_SHORT).show() ;
			}else{
			    GeoSearch(addredit.getText().toString());
			}
			break ;
		}
	}
	
	/**
	 * 
	 */
	private void GeoSearch(String addr){
		   
	     // Geo搜索
		            Log.i(TAG, "GeoSearch");
	     			mSearch.geocode(new GeoCodeOption().city("")
	     					.address(this.addredit.getText().toString()));
	}
	
	/**
	 * 
	 * 初始化第一个gps按钮的显示，定位
	 * @param view
	 */
	private void gpsClickAction(){
		
		Log.i(TAG, "正在定位");
		BDLocationListener myListener = new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				//Receive Location 
				if (location.getLocType() == BDLocation.TypeNetWorkLocation){
					address = location.getAddrStr();
					longitude = location.getLongitude();
					latitude = location.getLatitude() ;
					listener.result(0,address, longitude, latitude);
					addredit.setText(address);
					Toast.makeText(context, location.getAddrStr(), Toast.LENGTH_SHORT).show();
			//		mLocationClient.stop() ;
					dismiss();
				}
			}

			@Override
			public void onReceivePoi(BDLocation arg0) {
				// TODO Auto-generated method stub
				
			}

		};
//		    mLocationClient = new LocationClient(this.mContext.getApplicationContext());     //声明LocationClient类
		    mLocationClient.registerLocationListener( myListener );    //注册监听函数
		    
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
		    Log.i(TAG, "应该在定位");
//		return view ;
	}


	@Override
	public void dismiss() {
		if(mLocationClient.isStarted()){
		mLocationClient.stop();
		}
		super.dismiss();
	}


	/**
	 * 寻找地理位置相关
	 **/
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(context, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		else{
			listener.result(1,result.getAddress(), result.getLocation().longitude, result.getLocation().latitude);
			
		    this.dismiss() ;
		}
		
	}


	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
	
	}
	

}
