package com.huishen.edrive.login;


import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.huishen.edrive.MainActivity;
import com.huishen.edrive.R;
import com.huishen.edrive.SplashActivity;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.BaseActivity;
import com.huishen.edrive.widget.LoadingDialog;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 验证手机号(这个类只能采用startActivityForResult()启动)
 * 第二参数是 0 ，常量是VerifyPhoneActivity.LOGIN_CODE ,封装在这个类里面 public static
 * @author zhanghuan
 *
 */
public class VerifyPhoneActivity extends BaseActivity implements
		OnClickListener {
    public static int LOGIN_CODE = 1 ;
    public static int LOGIN_RESULT_CODE = 10;
	private EditText editPhoneNumber, editVerifyCode;
	private Button btnVerify, btnStart ;
	private TextView tvProtocal;
	private TextView title ; //标题
	private ImageButton back ;
	private LoadingDialog  dialog ;
	private String addr; //地址
	private double lng ,lat ;//经度，纬度
	 // 定位相关
    private LocationClient mLocationClient ;
    
	/***************************腾讯统计相关框架*************************************/

	@Override
	protected void onDestroy() {
		if(dialog.isShowing()){
			dialog.dismiss();
		}
		super.onDestroy();
	}
	/***************************腾讯统计基本框架结束*************************************/
	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, VerifyPhoneActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_phone);
		AppController.getInstance().addActivity(this);
		this.setTag("VerifyPhoneActivity");
		initWidgets();
	}

	private void initWidgets() {
		editPhoneNumber = (EditText) findViewById(R.id.verify_edit_number);
		editVerifyCode = (EditText) findViewById(R.id.verify_edit_vcode);
		btnVerify = (Button) findViewById(R.id.verify_btn_verify);
		btnStart = (Button) findViewById(R.id.verify_btn_start);
		tvProtocal = (TextView) findViewById(R.id.verify_tv_protocal);
		back = (ImageButton) findViewById(R.id.header_back);
		title = (TextView)findViewById(R.id.header_title);
		title.setText(this.getResources().getString(R.string.str_verify_phone_title));
		tvProtocal.setText(buildProtocalText());
		btnVerify.setOnClickListener(this);
		this.btnStart.setOnClickListener(this) ;
		back.setOnClickListener(this) ;
		
		dialog = new LoadingDialog(this);
		// 用于限制验证码的长度
		editVerifyCode.addTextChangedListener(new TextWatcher() {

			final int validLength = getResources().getInteger(
					R.integer.verifycode_valid_length);

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				btnStart.setEnabled((s.length() == validLength) ? true : false);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.verify_btn_verify:
			sendVerifyRequest();
			break;
		case R.id.verify_btn_start:
			getGps();
//			sendVerifyCode();
			break;
		case R.id.header_back:
			this.finish();
			break ;
		default:
			break;
		}
	}

	/**
	 * 发送验证码再次提交给服务器。
	 */
	private final void sendVerifyCode() {
		Log.i(TAG, "开始") ;
		    String num = editPhoneNumber.getText().toString();
		    String code = editVerifyCode.getText().toString() ;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("phone", num);
            map.put("vcodes", code) ;
            map.put("lng", lng+"");
            map.put("lat", lat+"");
            map.put("addr", addr);
            btnStart.setEnabled(false);
            if(!dialog.isShowing()){
            	dialog.show();
            }
			NetUtil.requestStringData(SRL.Method.METHOD_LOGIN,TAG , map,
					new Response.Listener<String>() {

						@Override
						public void onResponse(String result) {
							Log.i(TAG, result);
							btnStart.setEnabled(true);
							if(dialog.isShowing()){
							dialog.dismiss();
							}
							//判断返回状态
							// ResponseParser.isReturnSuccessCode(arg0);
							actionLogin(result);
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							btnStart.setEnabled(true);
							if(dialog.isShowing()){
							dialog.dismiss();
							}
							if (arg0.networkResponse == null) {
								Toast.makeText(VerifyPhoneActivity.this, "网络连接断开",
										Toast.LENGTH_SHORT).show();
								Log.i("Splash", arg0.toString());
								return ;
							}
							if(arg0.networkResponse.statusCode == 320){
								AppUtil.ShowShortToast(VerifyPhoneActivity.this, "用户已经下线，请重新验证手机");
							}else{
								AppUtil.ShowShortToast(VerifyPhoneActivity.this, "访问连接异常");
							}
						}
					});
		
	}
	
	/**
	 * 
	 * 初始化第一个gps按钮的显示，定位
	 * @param view
	 */
	private void getGps(){
		mLocationClient = new LocationClient(this);     //声明LocationClient类
		Log.i(TAG, "正在定位");
		BDLocationListener myListener = new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				//Receive Location 
				if (location.getLocType() == BDLocation.TypeNetWorkLocation){
					addr=location.getAddrStr();
					lng = location.getLongitude();
					lat = location.getLatitude();
					mLocationClient.stop() ;
					sendVerifyCode();
				}
			}

		};
//		    mLocationClient = new LocationClient(this.mContext.getApplicationContext());     //声明LocationClient类
		    mLocationClient.registerLocationListener( myListener );    //注册监听函数
		    
		    LocationClientOption option = new LocationClientOption();
		    option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		    option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		    option.setOpenGps(true);
//		    option.disableCache(true);//禁止启用缓存定位
		    option.setAddrType("all");//返回的定位结果包含地址信息
		    mLocationClient.setLocOption(option); 
		    if(!mLocationClient.isStarted()){
		      mLocationClient.start();
		    }
		    Log.i(TAG, "应该在定位");
	}
	
	/**
	 * 登录注册获取数据动作
	 * @param result
	 */
	private void actionLogin(String result){
		Log.i(TAG, result);
		if( result.equals("")|| (result == null)){
			Toast.makeText(VerifyPhoneActivity.this, "返回值为空，服务器异常", Toast.LENGTH_SHORT).show() ;
		}
		/**
		 * 注册登录  参数:phone :18388888888,vcodes=1234  返回值:{status:1|0|2|-1|-2} 0: 账号停用，1:注册登陆成功 2 密码不为空 必须输入密码才能登陆 -2 验证码错误 -1 验证码过期
		 */
		else{
			try { 
				JSONObject json = new JSONObject(result);
				int status = json.getInt("status") ;
				switch(status){
				case 1: //注册登陆成功
					this.setResult(LOGIN_RESULT_CODE) ;
					AppUtil.saveUserInfo(this, result) ;
					AppUtil.ShowShortToast(this, "验证登录成功") ;
					int tag = this.getIntent().getIntExtra("tag", 0);
					if(tag == 1){
						Intent i = new Intent(this,MainActivity.class);
						startActivity(i);
					}
					this.finish() ;
					break ;
				case 3: //3 电话号码输入和发送短信号码不匹配
					AppUtil.ShowShortToast(VerifyPhoneActivity.this, "电话号码输入和发送短信号码不匹配");
					break ;
				case 4: //4已经注册了教练
					AppUtil.ShowShortToast(VerifyPhoneActivity.this, "对不起，您已经注册了教练，不能成为学员了");
					break ;
				case 0: //账号停用
					AppUtil.ShowShortToast(VerifyPhoneActivity.this, "账号停用");
					break ;
				case -2: //验证码错误
					AppUtil.ShowShortToast(VerifyPhoneActivity.this, "验证码错误");
					break ;
					
				case -1: //验证码过期
					AppUtil.ShowShortToast(VerifyPhoneActivity.this, "验证码过期");	
					break ; 
				}
			  } catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 完成手机号码的发送和计时等工作。
	 */
	private void sendVerifyRequest() {
		// check phone
		String num = editPhoneNumber.getText().toString();
		if (!num.matches("(86|\\+86)?1\\d{10}")) {
			Toast.makeText(
					VerifyPhoneActivity.this,
					getResources().getString(
							R.string.str_verify_phone_err_not_valid_number),
					Toast.LENGTH_SHORT).show();
			return;
		}
		   //显示加载框
		   if(!dialog.isShowing()){
			   dialog.show();
		   }
		
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("phone", num);
			
			NetUtil.requestStringData(SRL.Method.METHOD_GET_VERIFY_CODE,TAG , map,
					new Response.Listener<String>() {

						@Override
						public void onResponse(String result) {
							Log.i(TAG, result);
							if(dialog.isShowing()){
							dialog.dismiss();
							}
							actionVerifyResult(result);
						
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							if(dialog.isShowing()){
							    dialog.dismiss();
							}
							if (arg0.networkResponse == null) {
								Toast.makeText(VerifyPhoneActivity.this, "网络连接断开",
										Toast.LENGTH_SHORT).show();
								Log.i("Splash", arg0.toString());
								return ;
							}
							if(arg0.networkResponse.statusCode == 320){
								AppUtil.ShowShortToast(VerifyPhoneActivity.this, "用户已经下线，请重新验证手机");
							}else{
								AppUtil.ShowShortToast(VerifyPhoneActivity.this, "访问连接异常");
							}
						}
					});
		
		
		final int validtime = getResources().getInteger(
				R.integer.verifycode_valid_time_seconds);
		// maybe should be a class field to support cancel
		new CountDownTimer(validtime * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				btnVerify.setText(""+(int) (millisUntilFinished / 1000));
				btnVerify.setEnabled(false) ;
			}

			@Override
			public void onFinish() {
				btnVerify.setText(R.string.str_verify_phone_err_request_retry);
				btnVerify.setEnabled(true);
			}
		}.start();
	}
    
	/**
	 * 服务器返回值处理
	 */
	private void actionVerifyResult(String result){
		//判断返回状态
		if( result.equals("")|| (result == null)){
			Toast.makeText(VerifyPhoneActivity.this, "返回值为空，服务器异常", Toast.LENGTH_SHORT).show() ;
		}
		else{
			try {
				JSONObject json = new JSONObject(result);
				int status = json.getInt("status") ;
				switch(status){
				case 1: //成功
					AppUtil.ShowShortToast(VerifyPhoneActivity.this, "验证码发送成功，请耐心等待");
					break ;
				case 2: //失败
					AppUtil.ShowShortToast(VerifyPhoneActivity.this, "验证码发送成功，请耐心等待");
					break ;
				case 0: //电话号码错误
					AppUtil.ShowShortToast(VerifyPhoneActivity.this, "电话号码错误，1分钟后可重发");
					break ;
				case 4: //4已经注册了教练
					AppUtil.ShowShortToast(VerifyPhoneActivity.this, "对不起，您已经注册了教练，不能成为学员了");
					break ;
				}
			  } catch (JSONException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	private final CharSequence buildProtocalText() {
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(getResources().getString(
				R.string.str_verify_phone_protocal_prefix));
		int protocalStartIndex = ssb.length();
		ssb.append(getResources().getString(
				R.string.str_verify_phone_protocal_name));
		ClickableSpan clickableSpan = new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				Log.d("", "clicked.");
				displayProtocal();
			}
		};
		tvProtocal.setMovementMethod(LinkMovementMethod.getInstance());
		ssb.setSpan(clickableSpan, protocalStartIndex, ssb.length(),
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources()
				.getColor(R.color.main_color));
		ssb.setSpan(colorSpan, protocalStartIndex, ssb.length(),
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return ssb;
	}

	
	/**
	 * 展示用户协议
	 */
	private final void displayProtocal() {
		Intent i = new Intent(this ,ServiceInfoActivity.class);
		this.startActivity(i) ;
	}

}
