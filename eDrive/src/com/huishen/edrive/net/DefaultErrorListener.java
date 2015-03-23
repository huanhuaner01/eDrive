/**
 * 
 */
package com.huishen.edrive.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.huishen.edrive.center.SettingActivity;
import com.huishen.edrive.login.VerifyPhoneActivity;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;

/**
 * @author zhanghuan
 *
 */
public final class DefaultErrorListener implements ErrorListener {
	
	private Activity context ;
	private ProgressDialog dialog ;
	private View btn ;
	
	public DefaultErrorListener(Activity context ,ProgressDialog dialog) {
		
		super();
		this.dialog = dialog ;
		this.context = context ;
	}
    
	public DefaultErrorListener(Activity context ,ProgressDialog dialog,View btn) {
		
		super();
		this.btn = btn ;
		this.dialog = dialog ;
		this.context = context ;
	}
	public DefaultErrorListener(Activity context ,View btn) {
		
		super();
		this.btn = btn ;
		this.context = context ;
	}
	
	public DefaultErrorListener(Activity context) {
		super();
		this.context = context ;
	}
	
	@Override
	public void onErrorResponse(VolleyError arg0) {
		if(dialog != null&&dialog.isShowing()){
			dialog.dismiss();
		}
		if(btn != null){
			btn.setEnabled(true);
		}
		if (arg0.networkResponse == null) {
			Toast.makeText(context, "网络连接断开",
					Toast.LENGTH_SHORT).show();
			Log.i("DefaultErrorListener", arg0.toString());
		
			return ;
		}
		if(arg0.networkResponse.statusCode == 320){
			AppUtil.ShowShortToast(context, "用户已经下线，请重新验证手机");
			Intent i = new Intent(context, VerifyPhoneActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			context.finish();
		}else{
			AppUtil.ShowShortToast(context, "服务器开小差啦~");
		}
	}

}
