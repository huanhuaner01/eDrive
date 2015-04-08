package com.huishen.edrive.demand;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.SplashActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.BaseActivity;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 订单失效弹出框
 * @author zhanghuan
 * 
 */
public class FailDialogActivity extends BaseActivity {
    private int orderId ; //订单号
    private Button resend ,cancel ;
    
	/***************************腾讯统计相关框架*************************************/

	/***************************腾讯统计基本框架结束*************************************/
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fail_dialog);
		
		AppController.getInstance().addActivity(this);
		this.setTag("FailDialogActivity");
		setFinishOnTouchOutside(false);
		//--------------------获取数据-------------------
		orderId = this.getIntent().getIntExtra(Const.USER_LAST_ORDER_ID, 0);
		//---------------------获取数据结束----------------
		
		//--------------震动------------------------
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		if (vibrator.hasVibrator()){
			vibrator.vibrate(1000);
		}
		//-----------震动结束！---------------------
		setFinishOnTouchOutside(true);
		regsitView();
		initView();
	}
	
	/**
	 * 注册组件
	 */
	private void regsitView() {
		resend = (Button)findViewById(R.id.dialog_msg_commit);
		cancel = (Button) findViewById(R.id.dialog_msg_cancel);
	}
	
	/**
	 * 初始化
	 */
	private void initView() {
		stopLimit();
		resend.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				resendOrder();
			}
			
		});
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
	}
	
	/**
	 * 取消发送订单的限制
	 */
	private void stopLimit(){
		Prefs.writeString(getApplicationContext(), Const.ORDER_STATUS, "0"); //0没有订单，1有订单
		Prefs.writeString(getApplicationContext(), Const.USER_LAST_ORDER_ID, ""); 
		AppController.getInstance().stopAlarm();
	}
	
	/**
	 * 重新发布订单
	 */
	private void resendOrder(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.USER_LAST_ORDER_ID, orderId+"");
		NetUtil.requestStringData(SRL.Method.METHOD_RESEND_ORDER,TAG , map,  new Response.Listener<String>() {
			
			@Override
			public void onResponse(String result) {
				Log.i(TAG, result);
				if(result==null ||result.equals("")){
					AppUtil.ShowShortToast(getApplicationContext(), "服务器无响应");
				}else{
				
				try{
					JSONObject json = new JSONObject(result);
					switch(json.getInt("code")){
					case 0: //重新发布成功
						AppUtil.ShowShortToast(getApplicationContext(), "发布成功");
						Prefs.writeString(getApplicationContext(), Const.USER_LAST_ORDER_ID,orderId+"") ;
						Prefs.writeString(getApplicationContext(), Const.ORDER_STATUS,"1") ;
			    		AppController.getInstance().setAlarm(FailDialogActivity.this,orderId);
						break ;
					case 1: //订单不存在
						AppUtil.ShowShortToast(getApplicationContext(), "订单不存在");
						break ;
					case 2: //订单已被抢，等待消息
						AppUtil.ShowShortToast(getApplicationContext(), "订单已被抢，等待消息");
						break ;
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				}
			}
			
		}, new DefaultErrorListener(this));

		finish();
	}
}
