package com.huishen.edrive.demand;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
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
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.huishen.edrive.MainActivity;
import com.huishen.edrive.R;
import com.huishen.edrive.apointment.MassageListener;
import com.huishen.edrive.apointment.MessageDialog;
import com.huishen.edrive.center.CoachDetailActivity;
import com.huishen.edrive.center.ListActivity;
import com.huishen.edrive.login.VerifyPhoneActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.umeng.UmengServiceProxy;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.BaseActivity;
import com.huishen.edrive.widget.GuideView;
import com.huishen.edrive.widget.LoadingDialog;
import com.huishen.edrive.widget.ZoomControlsView;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
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
public class DemandActivity extends BaseActivity implements OnClickListener{
	//常量
	private int STATUS_INPUT = 0 ; //文字输入
	private int STATUS_SOUND = 1 ; //语音输入
	public static String IS_MAIN = "isFirstMain" ; //第一次进入的主页面 
	
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;
    private ImageButton msg ; //订单消息
    private ImageView msgTag ; //订单状态
    
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private MKOfflineMap mOffline = null; //离线地图
	//UI相关
	private ImageButton back ; //标题栏返回键
	private ImageButton switchbtn ;
	private LinearLayout switchlay ;
	private TextView switchtv ;
	private int currentStatus = 0 ; //目前的输入状态
	private boolean isFirstLoc = true;//
	private boolean isFirstMain = false ; //标志，是否是第一次登录
    private int coachId = -1 ; //目前点击的教练的id 默认-1没有
    private String addr  ; //地址信息
	
    private LoadingDialog dialog ; //加载弹出框
    private ProgressDialog progressDialog ;
    private MessageDialog Offlinedialog ; //离线弹出框
    private int cityId ; //城市id,用于离线地图下载
    
    private ZoomControlsView zcvZomm;//缩放控件
    ImageView coach_class_img;
    String intentResult;
    double currentLat; //当前经度
    double currentLng; //当前维度
    private String Province,City,Area;
    
	/***************************腾讯统计相关框架*************************************/
	
	/***************************腾讯统计基本框架结束*************************************/
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //将Activity添加进入栈
        AppController.getInstance().addActivity(this) ;
        this.setTag("DemandActivity");
		/***************获取传递的数据*******************/
        
        isFirstMain = this.getIntent().getBooleanExtra(IS_MAIN, false) ;
        
		 //这里通过key的方式获取值     
		/*******************************************/
    	setContentView(R.layout.activity_demand);	
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showZoomControls(false);//隐藏缩放控件
		//获取缩放控件
        zcvZomm=(ZoomControlsView) findViewById(R.id.zcv_zoom);
        zcvZomm.setMapView(mMapView);//设置百度地图控件
        
		mBaiduMap = mMapView.getMap();
		
		
		dialog = new LoadingDialog(this);
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
		bg = (FrameLayout)findViewById(R.id.demand_bg);
		msg = (ImageButton)findViewById(R.id.main_btn_msg);
		msgTag = (ImageView)findViewById(R.id.have_message_tag);
		
		coach_class_img = (ImageView) findViewById(R.id.coach_class_img);
		coach_class_img.setOnClickListener(this);
	}
	
	/**
	 * 初始化组件
	 */
	private void initView(){		
		mBaiduMap.setMyLocationEnabled(true);
		msgTag.setVisibility(View.GONE);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mMapView.removeViewAt(1);
		
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		this.switchbtn.setOnClickListener(this) ;
		this.switchlay.setOnClickListener(this);
		this.back.setImageResource(R.drawable.ic_main) ;
		
		this.back.setOnClickListener(this) ;
		msg.setOnClickListener(this);
		
	}
	/**
	 * 检查是否是第一次启动，如果是则进入知道页面
	 * @return
	 */
	private boolean checkFirstStart() {
		// 首次使用时应为true
		boolean value = Prefs.getBoolean(this, Const.KEY_FIRSTUSE, true);
//		Prefs.setBoolean(this, Const.KEY_FIRSTUSE, false);
		return value;
	}

	private void showRoundCoach(double lng ,double lat){
		if(!isFinishing()&&!dialog.isShowing()){ //显示加载框
		   dialog.show();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SRL.Param.PARAM_LONGITUDE, lng+"");
		map.put(SRL.Param.PARAM_LATITUDE, lat+"");
		
		NetUtil.requestStringData(SRL.Method.METHOD_GET_ROUND_COACH,TAG , map,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String result) {
						if(dialog.isShowing()){
						dialog.dismiss();
						}
//						Log.i("www", result);
						try {
							//返回值：[{"coachId":1,"coachName":"张三","coachScore":2.1,"issue":20,
//							"lat":30.575699,"lng":104.068216,
//							"path":"/static/img/coachHeader.png","phone":"3558657902"}]
							intentResult = result;
							JSONArray array = new JSONArray(result);
							mBaiduMap.clear();
							for(int i = 0 ; i<array.length() ;i++){
								JSONObject json = array.getJSONObject(i);
//								Log.i(TAG, json.getDouble(SRL.Param.PARAM_LATITUDE)+"") ;
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
								ImageView img = (ImageView)view.findViewById(R.id.order_detail_coach_photo) ;
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
				}, new DefaultErrorListener(this ,dialog));
	
	
	}
	/**
	 * 输入状态转换
	 */
	private void switchStatus(){
//		Log.i("DemandActivity", "currentStatus :"+currentStatus) ;
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
		i.putExtra("addr", addr);
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
				Log.i("LocationDemo","地理位置"+location.getLatitude()+","+location.getLongitude());
				currentLat = location.getLatitude();
				currentLng = location.getLongitude();
				mBaiduMap.animateMapStatus(u);
				addr = location.getAddrStr() ;
				Province = location.getProvince();
				City = location.getCity();
				Area = location.getStreet();
//				Log.i("www", ""+addr);
				mLocClient.stop();
				if(location.getCityCode() == null){
					Log.i(TAG, "location.getCityCode() is null");
				}else{
					cityId = Integer.parseInt(location.getCityCode());
				  checkOfflineMap(Integer.parseInt(location.getCityCode()) ,location.getCity());
				  showRoundCoach(location.getLongitude() ,location.getLatitude());
				}
				Log.w(TAG, "demand addr is "+addr);
			}
				
				
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
		if(cityId != 0){
		MKOLUpdateElement temp = mOffline.getUpdateInfo(cityId);
		if (temp != null && temp.status == MKOLUpdateElement.DOWNLOADING) {
			mOffline.pause(cityId);
		}
		}
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
		if(dialog!= null&&dialog.isShowing()){
			dialog.dismiss();
		}
		//  
		mLocClient.stop();
		if(progressDialog!= null && progressDialog.isShowing()){
		progressDialog.dismiss();
		}
		if(mOffline != null){
		mOffline.destroy();
		}
		//
//		clearOverlay(null);
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
		case R.id.main_btn_msg:
			Intent i = new Intent(this,ListActivity.class);
			i.putExtra(ListActivity.STATUS_KEY,ListActivity.STATUS_ORDERLIST);
			startActivity(i);
//			Toast.makeText(DemandActivity.this, "跳转11111111....", Toast.LENGTH_SHORT).show();
			break ;
		case R.id.coach_class_img:
			if(!"".equals(intentResult) && null != intentResult){
				Intent intent = new Intent(DemandActivity.this,ChooseAdressOrCoachActivity.class);
				intent.putExtra("currentLat", currentLat);
				intent.putExtra("currentLng", currentLng);
				intent.putExtra("Province", Province);
				intent.putExtra("City", City);
				intent.putExtra("Area", Area);
				
				startActivity(intent);
			}else{
				Toast.makeText(DemandActivity.this, "正在加载信息，请稍后...", Toast.LENGTH_SHORT).show();
			}
			break;
		
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "requestCode:"+requestCode+" resultCode:"+resultCode);
		if(resultCode == VerifyPhoneActivity.LOGIN_RESULT_CODE){
			//如果登录成功
			if(isFirstMain){
				UmengServiceProxy.startPushService(this);
				
//				sendDeviceToken();
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
    	   if(!Prefs.checkUser(this)){
            if(backindex == 0){
           	 Toast.makeText(DemandActivity.this,"再按一次退出e驾学车!", Toast.LENGTH_SHORT).show();
           	 backindex++;
           	 return false ;
            }
            AppController.getInstance().exit(this);
            return true;
    	   }
        }
        return super.onKeyDown(keyCode, event);
    }
	
	 
	 
	 /**
	     * 检测离线地图
	     */
	    private void checkOfflineMap(final int cityCode ,String cityName) {
	    	if(mOffline == null){
	    		
	        mOffline = new MKOfflineMap();
	        mOffline.init(new MKOfflineMapListener() {
	            @Override
	            public void onGetOfflineMapState(int type, int state) {
	                switch (type) {
	                    case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
	                        // 离线地图下载更新事件类型
	                        MKOLUpdateElement update = mOffline.getUpdateInfo(state);
	                        if(progressDialog == null){
	                        	progressDialog = createDownloadDialog();
	                        	progressDialog.show();
	                        }
	                        progressDialog.setProgress(update.ratio);
	                        if(update.ratio == 100) {
	                            progressDialog.dismiss();
	                            mOffline.destroy();
	                            AppUtil.ShowShortToast(DemandActivity.this,"离线地图下载完成");
	                        }
	                        break;
	                    case MKOfflineMap.TYPE_NEW_OFFLINE:
	                        // 有新离线地图安装
	                        break;
	                    case MKOfflineMap.TYPE_VER_UPDATE:
	                        // 版本更新提示
	                        break;
	                }

	            }
	        });
	    	}
	        boolean hasMap = false;
	    	// 获取已下过的离线地图信息
	        ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();

	        if (localMapList != null) {
	            for (MKOLUpdateElement mkolUpdateElement : localMapList) {
	                if (mkolUpdateElement.cityID == cityCode) {
	                    hasMap = true;
	                }
	            }
	        }

	        if (!hasMap) {
	        	Offlinedialog = new MessageDialog(this ,"离线地图下载提示" ,"您所在城市为" +cityName + "，推荐您下载离线地图" ,
	        			false ,new MassageListener(){

							@Override
							public void setCommitClick() {
								start(cityCode);
							}

							@Override
							public void setCancelClick() {
								Offlinedialog.dismiss();
							}
	        		
	        	});
	        	Offlinedialog.show();
//	            alertText.setText("您所在城市为" + baiduMapProxy.getCachedLocation().getCity() + "，推荐您下载离线地图");
//	            builder.create().show();
	        }

	       // mOffline.remove(cityCode);

	    }
	    
		private ProgressDialog createDownloadDialog(){
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
			dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
			dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
			dialog.setIcon(R.drawable.ic_launcher);// 设置提示的title的图标，默认是没有的
			dialog.setTitle("提示");
			dialog.setMax(100);
			dialog.setMessage("正在努力下载离线地图......");
			return dialog;
		}
	    /**
		 * 开始离线地图下载
		 * 
		 * @param view
		 */
		public void start(int cityId) {
			mOffline.start(cityId);
			Toast.makeText(this, "开始下载离线地图. cityid: " + cityId, Toast.LENGTH_SHORT)
					.show();
			progressDialog = createDownloadDialog();
        	progressDialog.show();
		}
		
		//////////////////加载启动页///////////////////////////
		
		private int bgheight ,bgwidth ,index =1;
		private int backheight ,backwidth ,switchheight ,switchwidth ,tsheight ,tswidth ;
		private FrameLayout bg ;
		private GuideView guide ; //指导页
		private HashMap<String ,Integer> map ;
		@SuppressLint("ClickableViewAccessibility") private void showGuide(){
			if(!checkFirstStart()){
				return ;
	         }
			
			guide = (GuideView)findViewById(R.id.demand_guide);
			guide.setVisibility(View.VISIBLE);
			map = new HashMap<String ,Integer>();
		    map.put("xPoint", this.getResources().getDimensionPixelSize(R.dimen.demand_bottom_padding));
		    map.put("yPoint", bgheight -this.getResources().getDimensionPixelSize(R.dimen.demand_bottom_padding) - switchheight );
		    map.put("width", switchwidth);
		    map.put("height", switchheight);
		    map.put("padding", this.getResources().getDimensionPixelSize(R.dimen.demand_bottom_padding)/2);
		    map.put("position", GuideView.TOP);
		    map.put("shape", GuideView.CRICLE);
		    map.put("img", R.drawable.icon_guide1);
		    guide.setAttr(map);
		    guide.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if(arg1.getAction() == MotionEvent.ACTION_DOWN){
						switch(index){
						case 1:
							showTsGuide();
							index = 2 ;
							break;
						case 2:
							showBackGuide();
							index = 3;
							break ;
						case 3:
							Prefs.setBoolean(DemandActivity.this, Const.KEY_FIRSTUSE, false);
							guide.setVisibility(View.GONE);
							break ;
						}
					}
					return true;
				}
		    	
		    });
			 
		}
		private void showTsGuide(){
			map.put("xPoint", this.getResources().getDimensionPixelSize(R.dimen.demand_bottom_padding)+switchwidth);
		    map.put("yPoint", bgheight -this.getResources().getDimensionPixelSize(R.dimen.demand_bottom_padding) - switchheight );
		    map.put("width", tswidth);
		    map.put("height", tsheight);
		    map.put("padding", this.getResources().getDimensionPixelSize(R.dimen.demand_bottom_padding)/2);
		    map.put("position", GuideView.TOP);
		    map.put("shape", GuideView.RECT);
		    map.put("img", R.drawable.icon_guide2);
		    guide.setAttr(map);
		}
		private void showBackGuide(){
			map.put("xPoint", 0);
		    map.put("yPoint",0);
		    map.put("width", backwidth);
		    map.put("height",backheight);
		    map.put("padding", this.getResources().getDimensionPixelSize(R.dimen.demand_bottom_padding)/2);
		    map.put("position", GuideView.BUTTOM_RIGHT);
		    map.put("shape", GuideView.CRICLE);
		    map.put("img", R.drawable.icon_guide3);
		    guide.setAttr(map);
		}
		/**
		 * 获取组件的高宽
		 */
		 @Override
		   public void onWindowFocusChanged(boolean hasFocus) {
			super.onWindowFocusChanged(hasFocus);
//			Log.i(TAG, "onWindowFocusChanged. height:"+switchheight+" width :"+switchwidth);
			if(hasFocus&&checkFirstStart()){
		       backheight = back.getHeight() ;
		       backwidth = back.getWidth() ;
		       switchheight = switchbtn.getHeight() ;
		       switchwidth = switchbtn.getWidth() ;
		       tsheight = switchlay.getHeight();
		       tswidth = switchlay.getWidth() ;
			   bgheight = bg.getHeight() ;
			   bgwidth = bg.getWidth() ;
			   showGuide();
			   Log.i(TAG, "给我进来");
			}
			
		   }
		 
}
