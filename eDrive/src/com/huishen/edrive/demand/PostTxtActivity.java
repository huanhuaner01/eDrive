package com.huishen.edrive.demand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Response;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.huishen.edrive.R;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.CustomEditText;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
    
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_txt);
		AppController.getInstance().addActivity(this);
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
		addr = Prefs.readString(getApplicationContext(), Const.USER_ADDR);
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
		}else{
			Log.i(TAG, addr);
			addrBtn.setText(addr);
			// 初始化搜索模块，注册事件监听
		    // 搜索相关
		    mSearch = GeoCoder.newInstance();
			mSearch.setOnGetGeoCodeResultListener(this);
			GeoSearch(addr);
		}
		this.commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(Prefs.readString(getApplicationContext(), Const.ORDER_STATUS).equals("1")){
				    AppUtil.ShowShortToast(getApplicationContext(), "请，你的订单现在很热门哟！请等会儿再发下一条！");
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
		HashMap<String, String> map = new HashMap<String, String>();
		NetUtil.requestStringData(SRL.Method.METHOD_GET_SERVICE_INFO, map,  new Response.Listener<String>() {
			
			@Override
			public void onResponse(String result) {
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
			
		}, new DefaultErrorListener());
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
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		else{
			lat = result.getLocation().latitude ;
			lng = result.getLocation().longitude ;
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
		if(content.equals("")){
			AppUtil.ShowShortToast(this, "订单内容不能为空") ;
			return ;
		}
		if(addr == null || addr.equals("")){
			AppUtil.ShowShortToast(this, "地址信息不能为空") ;
			return ;
		}
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
				    try{
				    	json = new JSONObject(result);
				    	
				    	if(json.getInt("code")== 0){
				    		AppUtil.ShowShortToast(getApplicationContext(), "发布成功") ;
				    		Prefs.writeString(getApplicationContext(), Const.ORDER_STATUS,"1") ;
				    		Prefs.writeString(getApplicationContext(), Const.USER_LAST_ORDER_ID,json.getInt(Const.USER_LAST_ORDER_ID)+"") ;
				    		AppController.getInstance().setAlarm(PostTxtActivity.this,json.getInt(Const.USER_LAST_ORDER_ID));
				    		finish();
				    	}else{
				    		AppUtil.ShowShortToast(getApplicationContext(), "发布失败") ;
				    	}
				    }catch(Exception e){
				    	   e.printStackTrace() ;
				    }
			}
			  
		}, new DefaultErrorListener());
		
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
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.USER_ID,Prefs.readString(this, Const.USER_ID));
        map.put(Const.USER_ADDR ,addr);
		NetUtil.requestStringData(SRL.Method.METHOD_SET_ADDR, map,  new Response.Listener<String>() {
		
			@Override
			public void onResponse(String result) {
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
			
		}, new DefaultErrorListener());
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
