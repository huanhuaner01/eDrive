package com.huishen.edrive.demand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.huishen.edrive.R;
import com.huishen.edrive.SplashActivity;
import com.huishen.edrive.login.VerifyPhoneActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.CustomEditText;
import com.huishen.edrive.widget.LoadingDialog;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 
 * 文字需求发布界面
 * @author zhanghuan
 *
 */
public class PostTxtActivity extends Activity implements OnGetGeoCoderResultListener{
	private String TAG= "PostTxtActivity" ;
    private TextView title ;
    private ImageButton back ;
    private GridView postGrid ; //选择信息
    private Button addrBtn ,commit ; //地理位置按钮，提交按钮
    private PostGridItemAdapter adapter ;
    private ArrayList<Map<String ,Object>> data ; 
    private CustomEditText edit ;
//    private boolean isFrist = true ;
    private GeoCoder mSearch ; // 搜索模块，也可去掉地图模块独立使用
    private PostAddrDialog dialog ;
    private double lng ;
    private double lat ;
    private String addr;
    private LoadingDialog loadingDialog ;
	/***************************腾讯统计相关框架*************************************/
	StatLogger logger = SplashActivity.getLogger();
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	   @Override
		protected void onPause() {
			super.onPause();
			StatService.onPause(this);
		}
	@Override
	protected void onDestroy() {
		mSearch.destroy();
		super.onDestroy();
		android.os.Debug.stopMethodTracing();
	}
	/***************************腾讯统计基本框架结束*************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_txt);
		AppController.getInstance().addActivity(this);
		//-----------------------获取数据-------------------------------
		addr = this.getIntent().getStringExtra("addr"); 
		Log.i(TAG, "frist addr "+addr);
		//-----------------------获取数据结束！---------------------------
		registView();
		initView();
	}
	
	/**
	 * 注册组件
	 */
	private void registView() {
		this.title = (TextView) findViewById(R.id.header_title);
		this.back = (ImageButton) findViewById(R.id.header_back);
		this.postGrid = (GridView) findViewById(R.id.post_gridview);
		this.edit = (CustomEditText) findViewById(R.id.post_edit);
		this.addrBtn = (Button) findViewById(R.id.post_addr_btn);
		this.commit = (Button) findViewById(R.id.post_commit);
	}
	
	/**
	 * 初始化
	 */
	private void initView() {
	
		this.title.setText(this.getResources().getString(R.string.post_title));
		loadingDialog = new LoadingDialog(this);
		 // 搜索相关
	    mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		if(!Prefs.readString(getApplicationContext(), Const.USER_ADDR).equals("")&&!Prefs.readString(getApplicationContext(), Const.USER_ADDR).equals("null")){
			
		   addr = Prefs.readString(getApplicationContext(), Const.USER_ADDR);
		   Log.i(TAG,Prefs.readString(getApplicationContext(), Const.USER_ADDR));
		}
		this.commit.setEnabled(false);
		//------------------------给gridView添加数据-------------------------------
		data = new ArrayList<Map<String ,Object>>();
		String[] from = new String[]{"service"};
		int[] to = new int[]{R.id.item_post_tv};
		this.adapter = new PostGridItemAdapter(this, data, R.layout.item_post_grid, from, to) ;
		
		this.postGrid.setAdapter(this.adapter) ;
		this.postGrid.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				HashMap<String, Object> map = (HashMap<String, Object>) view
						.getTag();
				Log.i(TAG, map.toString());
				int status = Integer.parseInt(map.get("status").toString()) ;
				String service = map.get("service").toString() ;
				if (status == 0) {
					edit.append(service+",");
				}else{
					edit.setText(edit.getText().toString().replace(service+",", ""));
				}
				adapter.selectOption(position);
			}
			
		}) ;
		getService();
		//------------------------给gridView添加数据结束-------------------------------
		
		this.addrBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(!dialog.isShowing()){
				dialog = new PostAddrDialog(PostTxtActivity.this,listener);
				dialog.show() ;
				}
			}
			
		});
		//返回按钮监听事件
		this.back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		//给选择的服务选项添加数据

		dialog = new PostAddrDialog(this,listener);
		if(addr == null || addr.equals("")|| addr.equals("null")){
			dialog.show() ;
			Log.i(TAG, "frist addr "+addr);
		}else{
			Log.i(TAG, addr);
			addrBtn.setText(addr);
			// 初始化搜索模块，注册事件监听
		   
			GeoSearch(addr);
		}
		this.commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(Prefs.readString(getApplicationContext(), Const.ORDER_STATUS).equals("1")){
				    AppUtil.ShowShortToast(getApplicationContext(), "亲，操作过于频繁，请隔5分钟再来吧！");
				}else{
					 sendTxtOrder();
				}
			}
			
		}) ;
		
	}
	
	/**
	 * 获取服务项
	 */
	private void getService(){
		if(!loadingDialog.isShowing()){
			loadingDialog.show();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		NetUtil.requestStringData(SRL.Method.METHOD_GET_SERVICE_INFO, map,  new Response.Listener<String>() {
			
			@Override
			public void onResponse(String result) {
				if(loadingDialog.isShowing()){
					loadingDialog.dismiss();
				}
				commit.setEnabled(true);
				Log.i(TAG, result);
				if(result==null ||result.equals("")){
					AppUtil.ShowShortToast(getApplicationContext(), "亲，没有预设服务哟");
				}else{
				    
				try{
					JSONObject json = new JSONObject(result);
					String service = json.optString("service","");
					String[] strs = service.split(",");
					for(int i = 0 ;i < strs.length ;i++){
						HashMap<String  , Object> map = new HashMap<String ,Object>();
						map.put("service", strs[i]) ;
						map.put("status", 0) ;
						data.add(map);
					}
					adapter.notifyDataSetChanged();
					
				}catch(Exception e){
					e.printStackTrace();
				}
				}
			}
			
		}, errorlister);
	}
	/**
	 * 
	 */
	private void GeoSearch(String addr){
		   
	     // Geo搜索
		            Log.i(TAG, "GeoSearch");
	     			mSearch.geocode(new GeoCodeOption().city("")
	     					.address(addr));
	}
	
	/**
	 * 寻找地理位置相关
	 **/
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		Log.i(TAG, "第一次获取地理位置信息");
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		else{
			lat = result.getLocation().latitude ;
			lng = result.getLocation().longitude ;
			mSearch.destroy();
		}
		
	}
	/**
	 * 发送订单
	 */
	private void sendTxtOrder(){
		
//		stuId=1;//学生ID
//		stuRealName=王成;//直接姓名
//		content=包接包送，对学生友好;//订单需求内容
//		lng=104.065656;//当前学生经度
//		lat=30.577716;//当前学生纬度
		Editable edt =edit.getText(); 
		String content = edt.toString();
		if (content.endsWith(",")){
			content = edt.delete(edt.length()-1, edt.length()).toString();
		}
		if(content.equals("")||content.equals(this.getResources().getString(R.string.post_defualt_tv))){
			AppUtil.ShowShortToast(this, "请输入文字或者选择服务项。") ;
			return ;
		}
		if(addr == null || addr.equals("")){
			AppUtil.ShowShortToast(this, "地址信息不能为空") ;
			return ;
		}
		    if(!loadingDialog.isShowing()){
		    	loadingDialog.show();
		    }
		    commit.setEnabled(false);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(SRL.Param.PARAM_LATITUDE, lat+"");
        map.put(SRL.Param.PARAM_LONGITUDE,lng+"");
        map.put(SRL.Param.PARAM_STUID, Prefs.readString(this, Const.USER_ID));
        map.put(SRL.Param.PARAM_CONTENT, content);
        map.put(SRL.Param.PARAM_STUREALNAME,Prefs.readString(this, Const.USER_PHONE) ) ;
        map.put(SRL.Param.PARAM_STUADDR,addr);
		NetUtil.requestStringData(SRL.Method.METHOD_SEND_TXT_ORDER, map,  new Response.Listener<String>() {
		
			@Override
			public void onResponse(String result) {
				    JSONObject json = null ;
				  //关闭进度条
					if(loadingDialog.isShowing()){
						loadingDialog.dismiss();
					}
				    commit.setEnabled(true);
				    try{
				    	json = new JSONObject(result);
				    	
				    	if(json.getInt("code")== 0){
				    		AppUtil.ShowShortToast(getApplicationContext(), "发布成功") ;
				    		Prefs.writeString(getApplicationContext(), Const.ORDER_STATUS,"1") ;
				    		Prefs.writeString(getApplicationContext(), Const.USER_LAST_ORDER_ID,json.getInt(Const.USER_LAST_ORDER_ID)+"") ;
				    		AppController.getInstance().setAlarm(PostTxtActivity.this,json.getInt(Const.USER_LAST_ORDER_ID));
				    		finish();
				    	}else if(json.getInt("code") == 2){//2:此区域附近无教练;;
				    		AppUtil.ShowShortToast(getApplicationContext(), "此区域附近无教练") ;
				    	}else{
				    		AppUtil.ShowShortToast(getApplicationContext(), "发布失败") ;
				    	}
				    	
				    }catch(Exception e){
				    	   e.printStackTrace() ;
				    }
			}
			  
		}, errorlister);
		
	}
	
	private PostDialogInterface listener= new PostDialogInterface(){

		@Override
		public void result(int tag ,String result, double longitude, double latitude) {
			addrBtn.setText(result);
			Log.i(TAG, "("+longitude+","+latitude+")");
			lng = longitude ;
			lat = latitude ;
			addr = result ;
			if(tag == 1){ //需要设置地址
				setWebAddr() ;
			}
		}
	} ;	
	
	/**
	 * 访问网络设置常用地址
	 */
	private void setWebAddr(){
	    if(!loadingDialog.isShowing()){
	    	loadingDialog.show();
	    }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.USER_ID,Prefs.readString(this, Const.USER_ID));
        map.put(Const.USER_ADDR ,addr);
		NetUtil.requestStringData(SRL.Method.METHOD_SET_ADDR, map,  new Response.Listener<String>() {
		
			@Override
			public void onResponse(String result) {
			    if(loadingDialog.isShowing()){
			    	loadingDialog.dismiss();
			    }
				    JSONObject json = null ;
				    
				    try{
				    	json = new JSONObject(result);
				    	
				    	if(json.getInt("status")== 1){
				    		AppUtil.ShowShortToast(getApplicationContext(), "常用地址设置成功") ;
				    		Prefs.writeString(getApplicationContext(), Const.USER_ADDR, addr);
				    	}else{
				    		AppUtil.ShowShortToast(getApplicationContext(), "地址设置失败") ;
				    	}
				    }catch(Exception e){
				    	   e.printStackTrace() ;
				    }
			}
			
		}, new DefaultErrorListener(this,loadingDialog));
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}
	private ErrorListener errorlister = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError arg0) {
		    commit.setEnabled(true);
			if(loadingDialog != null&&loadingDialog.isShowing()){
				loadingDialog.dismiss();
			}
			if (arg0.networkResponse == null) {
				Toast.makeText(PostTxtActivity.this, "网络连接断开",
						Toast.LENGTH_SHORT).show();
				Log.i("DefaultErrorListener", arg0.toString());
				if(loadingDialog != null&&loadingDialog.isShowing()){
					loadingDialog.dismiss();
				}
				return ;
			}
			if(arg0.networkResponse.statusCode == 320){
				AppUtil.ShowShortToast(PostTxtActivity.this, "用户已经下线，请重新验证手机");
				Intent i = new Intent(PostTxtActivity.this, VerifyPhoneActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				PostTxtActivity.this.startActivity(i);
				finish();
			}else{
				AppUtil.ShowShortToast(PostTxtActivity.this, "服务器开小差啦~");
			}
		
		}
	};

}
