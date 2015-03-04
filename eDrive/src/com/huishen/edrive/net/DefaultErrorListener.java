/**
 * 
 */
package com.huishen.edrive.net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;

/**
 * @author zhanghuan
 *
 */
public final class DefaultErrorListener implements ErrorListener {
	
	private Context context = AppController.getInstance().getApplicationContext();
	
	@Override
	public void onErrorResponse(VolleyError arg0) {
		if (arg0.networkResponse == null) {
			Toast.makeText(context, "网络连接断开",
					Toast.LENGTH_SHORT).show();
			Log.i("Splash", arg0.toString());
			return ;
		}
		if(arg0.networkResponse.statusCode == 320){
			AppUtil.ShowShortToast(context, "用户已经下线，请重新验证手机");
		}else{
			AppUtil.ShowShortToast(context, "访问连接异常");
		}
	}

}