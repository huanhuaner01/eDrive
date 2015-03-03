package com.huishen.edrive.demand;

import java.io.File;
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
import com.huishen.edrive.net.UploadResponseListener;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.util.Recorder;
import com.huishen.edrive.util.SimpleRecorder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PostSoundActivity extends Activity implements OnClickListener ,OnGetGeoCoderResultListener{
	private String TAG= "PostSoundActivity" ;
    private TextView title ;
    private ImageButton back ;
    private GridView postGrid ; //选择信息
    private Button addrBtn ,sound_play ; //地理位置按钮，提交按钮
    private PostGridItemAdapter adapter ;
    private ArrayList<Map<String ,Object>> data ;
    private ImageButton sound_image ;
//    private boolean isFrist = true ;
    private PostAddrDialog dialog ;
    private StringBuffer keybuffer ;
    private Animation animation = null; //动画模式
    private View soundbg ;
    private SimpleRecorder recorder ;
    private File audiofile ;
    
    private GeoCoder mSearch ; // 搜索模块，也可去掉地图模块独立使用
    private double lng ;
    private double lat ;
    private String addr;
    private ProgressDialog MyDialog ;
//    = ProgressDialog.show( MyActivity.this, " " , " Loading. Please wait ... ", true);
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_sound);
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
		this.sound_image = (ImageButton) findViewById(R.id.post_soundimage);
		this.addrBtn = (Button) findViewById(R.id.post_addr_btn);
		this.soundbg = (View) findViewById(R.id.post_imagebg);
		this.sound_play = (Button) findViewById(R.id.post_btn_sound);
		recorder = SimpleRecorder.getInstance();
	}
	
	/**
	 * 初始化
	 */
	private void initView() {
	
		this.title.setText(this.getResources().getString(R.string.post_title));
		addr = Prefs.readString(getApplicationContext(), Const.USER_ADDR);
		this.sound_play.setEnabled(false) ;
		//----------给选择的服务选项添加数据------------------------
		keybuffer = new StringBuffer();
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
					keybuffer.append(service+",");
				}else{
					String replaced = keybuffer.toString().replace(service+",", "");
					keybuffer.delete(0, keybuffer.length());
					keybuffer.append(replaced);
				}
				adapter.selectOption(position);
			}
			
		}) ;
		getService();
		//----------给选择的服务选项添加数据结束！----------------------
		//设置语言组件
		this.sound_image.setEnabled(false) ;
		initScale();
		this.sound_play.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN: //按下事件
					
					sound_image.setEnabled(true);
					soundbg.startAnimation(animation);
					sound_play.setText(PostSoundActivity.this.getResources().getString(R.string.post_sound_btn_press));
//					String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"edrive" ;
					recorder.clearFile() ;
					audiofile = new File(recorder.getPath() ,recorder.now()+recorder.getRandomString(2)+".mp3");
					
					recorder.startRecord(audiofile);
					break ;
				case MotionEvent.ACTION_UP: //弹起事件
					soundbg.clearAnimation();
					sound_play.setText(PostSoundActivity.this.getResources().getString(R.string.post_sound_btn));
					recorder.stopRecord();
				break;
				}
				return false;
			}
		
		}

		);
		this.sound_image.setOnClickListener(this);
		///设置语言组件完成///////////////////////////
		
		
		this.addrBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(!dialog.isShowing()){
				dialog = new PostAddrDialog(PostSoundActivity.this,listener);
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
		
	
		
		dialog = new PostAddrDialog(this,listener);
		if(addr.equals("")){
			dialog.show() ;
		}else{
			
			addrBtn.setText(addr);
			// 初始化搜索模块，注册事件监听
			 // 搜索相关
		    mSearch = GeoCoder.newInstance();
			mSearch.setOnGetGeoCodeResultListener(this);
			GeoSearch(addr);
		}
		
	}
	
	/**
	 * 获取服务项
	 */
	private void getService(){
	    MyDialog = ProgressDialog.show(this, " " , " 加载中... ", true);
	    if(!MyDialog.isShowing()){
	    	MyDialog.show();
	    }
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
					sound_play.setEnabled(true) ;
				}catch(Exception e){
					e.printStackTrace();
				}
				}
				//关闭进度条
				if(MyDialog.isShowing()){
				    MyDialog.dismiss();
				}
			}
			
		}, new DefaultErrorListener(MyDialog));
	}
	
	/**
	 * 发送订单
	 */
	private void sendSoundOrder(){
		
//		stuId=1;//学生ID
//		stuRealName=王成;//直接姓名
//		content=包接包送，对学生友好;//订单需求内容
//		lng=104.065656;//当前学生经度
//		lat=30.577716;//当前学生纬度
//		String content = edt.toString();
		if(addr == null || addr.equals("")){
			AppUtil.ShowShortToast(this, "地址信息不能为空") ;
			return ;
		}
		if(keybuffer.equals("") && (audiofile.length()<=0))
		{
			AppUtil.ShowShortToast(this, "亲，什么都没有，教练不要哟") ;
			return ;
		}
		//显示进度条
		 MyDialog = ProgressDialog.show(this, " " , " 发布中... ", true);
		 if(!MyDialog.isShowing()){
			 MyDialog.show();
		 }
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put(SRL.Param.PARAM_LATITUDE, lat+"");
//        map.put(SRL.Param.PARAM_LONGITUDE,lng+"");
//        map.put(SRL.Param.PARAM_STUID, Prefs.readString(this, Const.USER_ID));
//        map.put(SRL.Param.PARAM_CONTENT, keybuffer.toString());
//        map.put(SRL.Param.PARAM_STUREALNAME,Prefs.readString(this, Const.USER_PHONE) ) ;
//        map.put(SRL.Param.PARAM_STUADDR, addrBtn.getText().toString());
		NetUtil.requestUploadFile(audiofile, SRL.Method.METHOD_SEND_SOUND_ORDER, new UploadResponseListener(){

			@Override
			public void onSuccess(String str) {
				if(str.equals("")){
					AppUtil.ShowShortToast(getApplicationContext(), "发布异常");
				}else{
					sendTextOrder(str);
				}
			}

			@Override
			public void onError(int httpCode) {
				AppUtil.ShowShortToast(getApplicationContext(), "网络错误："+httpCode);
				if(MyDialog.isShowing()){
					MyDialog.dismiss();
				}
			}

			@Override
			public void onProgressChanged(int hasFinished) {
				
			}
			
		});
	}
	
	/**
	 * 上传文字信息
	 * @param result
	 */
	private void sendTextOrder(String result){
	  String audio ="" ;
	  try{
		 JSONObject json = new JSONObject(result); 
		 if(json.getInt("code") != 0){
			 AppUtil.ShowShortToast(getApplicationContext(), "语音上传失败！");
			  if(MyDialog != null&&MyDialog.isShowing()){
				  MyDialog.dismiss();
				}
			  return ;
		 }
		 audio = json.getString("audio");
	  }catch(Exception e){
		  e.printStackTrace();
		  if(MyDialog != null&&MyDialog.isShowing()){
			  MyDialog.dismiss();
			}
		  return ;
	  } 
      HashMap<String, String> map = new HashMap<String, String>();
      map.put(SRL.Param.PARAM_LATITUDE, lat+"");
      map.put(SRL.Param.PARAM_LONGITUDE,lng+"");
      map.put(SRL.Param.PARAM_STUID, Prefs.readString(this, Const.USER_ID));
      map.put(SRL.Param.PARAM_CONTENT, keybuffer.toString());
      map.put(SRL.Param.PARAM_STUREALNAME,Prefs.readString(this, Const.USER_PHONE) ) ;
      map.put(SRL.Param.PARAM_STUADDR, addrBtn.getText().toString());
      map.put("audio",audio );
		NetUtil.requestStringData(SRL.Method.METHOD_SET_ADDR, map,  new Response.Listener<String>() {

			@Override
			public void onResponse(String result) {
			    JSONObject json = null ;
			    try{
			    	json = new JSONObject(result);
			    	
			    	if(json.getInt("code")== 0){
			    		AppUtil.ShowShortToast(getApplicationContext(), "发布成功") ;
			    		finish();
			    	}else{
			    		AppUtil.ShowShortToast(getApplicationContext(), "发布失败") ;
			    	}
			    }catch(Exception e){
			    	   e.printStackTrace() ;
			    }
				if(MyDialog.isShowing()){
					MyDialog.dismiss();
				}
			}
			
		},new DefaultErrorListener(MyDialog));
	
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
	
	
	private void initScale(){
		animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
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
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.post_soundimage:
			if(recorder == null || recorder.getAudioFileLength()<1){
				AppUtil.ShowShortToast(this,"请先录音");
			}else{
			recorder.playAudioFile(audiofile);
			}
			break;
		case R.id.post_commit:
			sendSoundOrder();
			break ;
		}
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
			
		}, new DefaultErrorListener(MyDialog));
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}
}
