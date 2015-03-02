package com.huishen.edrive.util;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.huishen.edrive.umeng.CustomUMessageHandler;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue requestQueue;
	private ImageLoader mImageLoader;
	private String sessionId;			//存放cookie
	private static AppController mInstance;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}



	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		requestQueue = Volley.newRequestQueue(this);
		SDKInitializer.initialize(this);
		PushAgent.getInstance(mInstance).setMessageHandler(new CustomUMessageHandler());//设置友盟自定义处理。
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	/**
	 * 提交新的网络请求。
	 */
	public final <T> void addNetworkRequest(Request<T> request){
		if (requestQueue != null){
			requestQueue.add(request);
		}
	}
   

   ///////////////////////////退出应用相关////////////////////////////////////
	private List<Activity> list = new ArrayList<Activity>();
	/**
	 * 添加acitivty到application
	 * @param activity
	 */
    public void addActivity(Activity activity) {
        list.add(activity);
    }
    
    /**
     * 结束全部activity
     * @param context
     */
    public void exit(Context context) {
        for (Activity activity : list) {
            activity.finish();
        }
        System.exit(0);
    }
    
    //////////////////////////////////////////////////////////////

	public ImageLoader getImageLoader() {
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.requestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		requestQueue.add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		requestQueue.add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (requestQueue != null) {
			requestQueue.cancelAll(tag);
		}
	}
}
