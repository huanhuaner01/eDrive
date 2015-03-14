package com.huishen.edrive.util;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.huishen.edrive.MainActivity;
import com.huishen.edrive.demand.OrderFailBroadcastReceiver;
import com.huishen.edrive.umeng.CustomUMessageHandler;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue requestQueue;
	private ImageLoader mImageLoader;
	private String sessionId;			//存放cookie
	private static AppController mInstance;
	//闹钟处理
	private AlarmManager am ;
	private PendingIntent pi ;

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
//			request.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, 1.0f));
			requestQueue.add(request);
		}
	}
   

   ///////////////////////////退出应用相关////////////////////////////////////
	private List<Activity> list = new ArrayList<Activity>();
	private MainActivity main = null ;
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
    /**
     * 结束MainActivity
     * @param context
     */
    public void exitMain(Context context) {
    	main.finish();
    }
    /**
     * 添加mainactivity
     * @param context
     */
    public void addMain(MainActivity maina) {
       main = maina;
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
	
	/////////////////////订单失效处理////////////////////////////////
	/**
	 * 启动订单失效处理
	 */
	public void setAlarm(Activity activity,int orderId){
        // 获得AlarmManager实例
		
        if(am == null){
        	am = (AlarmManager) activity.getSystemService(activity.ALARM_SERVICE);
        }
        
        // 实例化Intent
        Intent intent = new Intent(activity,OrderFailBroadcastReceiver.class);
        intent.putExtra(Const.USER_LAST_ORDER_ID, orderId);
        // 实例化PendingIntent
        pi = PendingIntent.getBroadcast(activity, 0,
                intent, 0);
        //public static final int ELAPSED_REALTIME_WAKEUP
        // 能唤醒系统，用法同ELAPSED_REALTIME，系统值是2 (0x00000002) 。
        //
        Log.i("12131",System.currentTimeMillis()+"");
        am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+Const.FAIL_ORDER_ALARM_TIME, pi);
        
	}
	
	/**
	 * 停止订单
	 */
	public void stopAlarm(){
		if(am != null && pi != null){
		 am.cancel(pi);
		}
	}
}
