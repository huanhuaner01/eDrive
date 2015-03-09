package com.huishen.edrive.center;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.huishen.edrive.R;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.login.VerifyPhoneActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 修改个人信息
 * @author zhanghuan
 *
 */
public class ModifyUserInfoSecendActivity extends Activity implements OnClickListener {
	private int tag = 0; // 传入0代表电话号码修改，1昵称 ，2真实姓名，3地址
	private String TAG = "ModifyUserInfoSecendActivity";
	private EditText phone, code, nickname, realname, addr;
	private Button getcode, commit, addrbtn;
	private TextView title;
	private ImageButton back;
    private LinearLayout nicknamelay ,realnamelay ,addrlay ;
 // 定位相关
    private LocationClient mLocationClient ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_user_info_secend);
		AppController.getInstance().addActivity(this);
		// -------------------获取传入数据----------------------
		tag = this.getIntent().getIntExtra("tag", 0);

		// -------------------获取传入数据结束！----------------------
		initView();
	}

	private void initView() {
		this.title = (TextView) findViewById(R.id.header_title);
		this.back = (ImageButton) findViewById(R.id.header_back);
		this.commit = (Button) findViewById(R.id.modify_user_secend_commit);
		switch (tag) {
//		case 0 :
//			Log.i(TAG, "tag is "+tag);
//			initPhone();
//			break ;
		case 1:
			initNickName();
			break ;
		case 2:
			initRealName();
			break ;
		case 3:
			initAddr();
			break ;
		}
		this.back.setOnClickListener(this);
	}

	private void initNickName(){
		nickname = (EditText)findViewById(R.id.modify_user_se_edit_nickname);
		nicknamelay = (LinearLayout)findViewById(R.id.modify_user_secend_nickname);
		nicknamelay.setVisibility(View.VISIBLE);
		this.title.setText("修改昵称");
		nickname.setText(this.getIntent().getStringExtra("nickname"));
		this.commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				String value = nickname.getText().toString() ;
				if(!value.equals("")){
					sendToUpdate("stuName",value);
				}else{
					AppUtil.ShowShortToast(getApplicationContext(), "昵称不能为空");
				}
			}
			
		});
	
	}
	
	private void initRealName(){
		nickname = (EditText)findViewById(R.id.modify_user_se_edit_nickname);
		nicknamelay = (LinearLayout)findViewById(R.id.modify_user_secend_nickname);
		TextView note = (TextView)findViewById(R.id.modify_user_se_nickpre);
		note.setText("真实姓名");
		nickname.setHint("请输入您的真实姓名");
		nicknamelay.setVisibility(View.VISIBLE);
		this.title.setText("修改真实姓名");
		nickname.setText(this.getIntent().getStringExtra("realname"));
		this.commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				String value = nickname.getText().toString() ;
				if(!value.equals("")){
					sendToUpdate("stuRealName",value);
				}else{
					AppUtil.ShowShortToast(getApplicationContext(), "真实姓名不能为空");
				}
			}
			
		});
	}
	private void initAddr(){
		addr = (EditText)findViewById(R.id.modify_user_se_edit_addr);
		addrlay = (LinearLayout)findViewById(R.id.modify_user_secend_addr);
		addrbtn = (Button)findViewById(R.id.modify_user_se_addrbtn);
		addr.setHint("请输入您的地理位置信息");
		addrlay.setVisibility(View.VISIBLE);
		this.title.setText("修改常用地址");
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
		addr.setText(this.getIntent().getStringExtra("addr"));
		this.commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				String value = addr.getText().toString() ;
				if(!value.equals("")){
					sendToUpdate("address",value);
				}else{
					AppUtil.ShowShortToast(getApplicationContext(), "地址不能为空");
				}
			}
			
		});
		addrbtn.setOnClickListener(this);
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
					addr.setText(location.getAddrStr());
					mLocationClient.stop() ;
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
	
	
//	/**
//	 * 初始化修改电话页面
//	 */
//	private void initPhone() {
//		Log.i(TAG, "tag is "+tag);
//		phone = (EditText) findViewById(R.id.modify_user_se_edit_phone);
//		code = (EditText) findViewById(R.id.modify_user_se_edit_code);
//		getcode = (Button) findViewById(R.id.modify_user_se_getcode);
//		commint.setEnabled(false);
//		// 用于限制验证码的长度
//		code.addTextChangedListener(new TextWatcher() {
//
//			final int validLength = getResources().getInteger(
//					R.integer.verifycode_valid_length);
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				commint.setEnabled((s.length() == validLength) ? true : false);
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//			}
//		});
//		getcode.setOnClickListener(this);
//	}
	

//	/**
//	 * 完成手机号码的发送和计时等工作。
//	 */
//	private void sendVerifyRequest() {
//		// check phone
//		String num = phone.getText().toString();
//		if (!num.matches("(86|\\+86)?1\\d{10}")) {
//			Toast.makeText(
//					ModifyUserInfoSecendActivity.this,
//					getResources().getString(
//							R.string.str_verify_phone_err_not_valid_number),
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("phone", num);
//			NetUtil.requestStringData(SRL.Method.METHOD_GET_VERIFY_CODE, map,
//					new Response.Listener<String>() {
//
//						@Override
//						public void onResponse(String result) {
//							Log.i(TAG, result);
//							actionVerifyResult(result);
//						}
//					}, new Response.ErrorListener() {
//
//						@Override
//						public void onErrorResponse(VolleyError arg0) {
//							if (arg0.networkResponse == null) {
//								Toast.makeText(ModifyUserInfoSecendActivity.this, "网络连接断开",
//										Toast.LENGTH_SHORT).show();
//								Log.i("Splash", arg0.toString());
//								return ;
//							}
//							if(arg0.networkResponse.statusCode == 320){
//								AppUtil.ShowShortToast(ModifyUserInfoSecendActivity.this, "用户已经下线，请重新验证手机");
//							}else{
//								AppUtil.ShowShortToast(ModifyUserInfoSecendActivity.this, "访问连接异常");
//							}
//						}
//					});
//		
//		
//		final int validtime = getResources().getInteger(
//				R.integer.verifycode_valid_time_seconds);
//		// maybe should be a class field to support cancel
//		new CountDownTimer(validtime * 1000, 1000) {
//
//			@Override
//			public void onTick(long millisUntilFinished) {
//				getcode.setText(""+(int) (millisUntilFinished / 1000));
//				getcode.setEnabled(false) ;
//			}
//
//			@Override
//			public void onFinish() {
//				getcode.setText(R.string.str_verify_phone_err_request_retry);
//				getcode.setEnabled(true);
//			}
//		}.start();
//	}

	@Override
	protected void onStop() {
		if(mLocationClient != null){
		mLocationClient.stop();
		}
		super.onStop();
	}

	/**
	 * 更新数据
	 */
	private void sendToUpdate(String key, String value) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(key, value);
		NetUtil.requestStringData(SRL.Method.METHOD_EDIT_USERINFO, map,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if (result == null || result.equals("")) {
							AppUtil.ShowShortToast(
									ModifyUserInfoSecendActivity.this, "服务器繁忙");
						} else {
							action(result);
						}

					}
				}, new DefaultErrorListener());
	}
//	/**
//	 * 服务器返回值处理
//	 */
//	private void actionVerifyResult(String result) {
//		// 判断返回状态
//		if (result.equals("") || (result == null)) {
//			Toast.makeText(this, "返回值为空，服务器异常", Toast.LENGTH_SHORT).show();
//		} else {
//			try {
//				JSONObject json = new JSONObject(result);
//				int status = json.getInt("status");
//				switch (status) {
//				case 1: // 成功
//					AppUtil.ShowShortToast(this,
//							"验证码发送成功，请耐心等待");
//					break;
//				case 2: // 失败
//					AppUtil.ShowShortToast(this,
//							"验证码发送成功，请耐心等待");
//					break;
//				case 0: // 电话号码错误
//					AppUtil.ShowShortToast(this,
//							"电话号码错误，1分钟后可重发");
//					break;
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}

	private void action(String result) {
		try {
			JSONObject json = new JSONObject(result);
			int status = json.getInt("status");
			if(status == 1){
				AppUtil.ShowShortToast(getApplicationContext(), "修改成功");
				finish();
			}else{
				AppUtil.ShowShortToast(getApplicationContext(), "修改失败");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			AppUtil.ShowShortToast(getApplicationContext(), "修改失败");
		}                                                                                                                                               
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
//		case R.id.modify_user_se_getcode:
//			Log.i(TAG, "点击了发送验证码");
//			sendVerifyRequest();
//			break ;
		case R.id.modify_user_secend_commit:
//			sendToUpdate();
			break ;
		case R.id.modify_user_se_addrbtn:
			gpsClickAction();
			break ;
		case R.id.header_back:
			finish();
			break;
		}
	}
}
