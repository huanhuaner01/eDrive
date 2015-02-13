/**
 * @encoding UTF-8
 */
package com.huishen.edrive.umeng;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

/**
 * 在Activity和友盟服务之间充当中介类，将复杂的注册逻辑和UI变更从主Activity中独立出来。<br/>
 * <h1>设计逻辑</h1>
 * 友盟的注册是<b>一次性</b>的，即设备第一次打开推送开关时进行设备的注册并返回一个DeviceToken。应用得到
 * DeviceToken之后可以进行Alias和Tag的上传。由于DeviceToken是由设备和应用唯一确定的，因此即使卸载应用并重新安装，
 * 只要应用的AppKey不变，则不会进行重复的注册。
 */
public final class UmengServiceProxy {
	private UmengServiceProxy(){}
	
	private static final String LOG_TAG = "UmengServiceProxy";
	
	/**
	 * 打开推送开关，并对用户信息进行注册。
	 */
	public static void startPushService(final Context context){
		Log.d(LOG_TAG, "trying to enable umeng.");
		final PushAgent mPushAgent = PushAgent.getInstance(context);
		//如果已经注册，则直接打开开关
		if (UmengRegistrar.isRegistered(context)){
			Log.d(LOG_TAG, "Device has registered before: " 
					+ UmengRegistrar.getRegistrationId(context));
			mPushAgent.enable();
			return ;
		}
		//否则，进行注册操作，并在注册成功之后上传其TAG和Alias。
		//注意这里的上传操作必须在注册成功之后，因为在拿到DeviceToken之前是无法上传的。
		mPushAgent.enable(new IUmengRegisterCallback() {
			
			@Override
			public void onRegistered(String deviceToken) {
				Log.d(LOG_TAG, "Device successfully registered: "+deviceToken);
				Intent tokenIntent = new Intent(UmengPushConst.ACTION_DEVICETOKEN_AVALIABLE);
				tokenIntent.putExtra(UmengPushConst.EXTRA_DEVICE_TOKEN, deviceToken);
				context.sendBroadcast(tokenIntent);
				try {
					//添加公司、部门为Tag，ID为Alias
//					mPushAgent.getTagManager().add(getPushTag());
//					String alias = getPushAlias();
//					mPushAgent.addAlias( alias, UmengPushConst.UPLOAD_ALIAS_TDB);	
//					Log.d(LOG_TAG, "tag and alias upload completed. current alias:"+alias);
				} catch (Exception e) {
					Log.w(LOG_TAG, "FATAL ERROR: fail to add push tag and/or alias."+e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 关闭推送开关，并执行删除TAG和Alias的操作。
	 */
	public static void stopPushService(final Context context){
		final PushAgent mPushAgent = PushAgent.getInstance(context);
		
		mPushAgent.setUnregisterCallback(new IUmengUnregisterCallback() {
			@Override
			public void onUnregistered(String arg0) {
				Log.d(LOG_TAG, "Unregistered device: "+arg0);
			}
		});
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				try {
//					mPushAgent.getTagManager().delete(getPushTag(data));
//					mPushAgent.removeAlias(getPushAlias(data),
//							UmengPushConst.UPLOAD_ALIAS_TDB);
//					Log.d(LOG_TAG, "tag and alias removed.");
				} catch (Exception e) {
					Log.w(LOG_TAG, "fail to remove tag and alias .");
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(Void result) {
				UmengRegistrar.unregister(context);
			};
			
		}.execute();
		mPushAgent.disable();
	}
}
