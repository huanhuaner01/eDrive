/**
 * 
 */
package com.huishen.edrive.net;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.huishen.edrive.login.VerifyPhoneActivity;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.widget.LoadingView;

/**
 * @author zhanghuan
 *
 */
public final class DefaultErrorListener implements ErrorListener {
	private String TAG = "DefaultErrorListener" ;
	private Activity context ;
	private Dialog dialog ;
	private View btn ;
	private LoadingView loading ;
	private SwipeRefreshLayout refresh ;
	public DefaultErrorListener(Activity context ,Dialog dialog) {
		
		super();
		this.dialog = dialog ;
		this.context = context ;
	}
    
	public DefaultErrorListener(Activity context ,Dialog dialog,View btn) {
		
		super();
		this.btn = btn ;
		this.dialog = dialog ;
		this.context = context ;
	}
	public DefaultErrorListener(Activity context ,Dialog dialog,LoadingView loading) {
		
		super();
		this.loading =loading ;
		this.dialog = dialog ;
		this.context = context ;
	}
	public DefaultErrorListener(Activity context ,Dialog dialog,LoadingView loading ,SwipeRefreshLayout refresh) {
		
		super();
		this.loading =loading ;
		this.dialog = dialog ;
		this.context = context ;
		this.refresh = refresh ;
	}
	public DefaultErrorListener(Activity context ,View btn) {
		
		super();
		this.btn = btn ;
		this.context = context ;
	}
    public DefaultErrorListener(Activity context ,View btn ,Dialog dialog) {
		
		super();
		this.btn = btn ;
		this.context = context ;
		this.dialog = dialog ;
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
		if(loading != null&&(loading.getVisibility()==View.VISIBLE)){
			loading.showFailLoadidng();
		}
		if(refresh != null&&refresh.isRefreshing()){
			refresh.setRefreshing(false);
		}
		if (arg0.networkResponse == null) {
			Toast.makeText(context, "网络连接断开,请检查网络",
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
