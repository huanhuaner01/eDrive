/**
 * @encoding UTF-8
 */
package com.huishen.edrive.umeng;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Response;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

/**
 * 在Activity和友盟服务之间充当中介类，将复杂的注册逻辑和UI变更从主Activity中独立出来。<br/>
 * <h1>设计逻辑</h1> 友盟的注册是<b>一次性</b>的，即设备第一次打开推送开关时进行设备的注册并返回一个DeviceToken。应用得到
 * DeviceToken之后可以进行Alias和Tag的上传。由于DeviceToken是由设备和应用唯一确定的，因此即使卸载应用并重新安装，
 * 只要应用的AppKey不变，则不会进行重复的注册。
 */
public final class UmengServiceProxy {
	// avoid instantiate
	private UmengServiceProxy() {
	}

	private static final String LOG_TAG = "UmengServiceProxy";

	/**
	 * 打开推送开关，并对用户信息进行注册。
	 */
	public static void startPushService(final Context context) {
		Log.d(LOG_TAG, "trying to enable umeng.");
		final PushAgent mPushAgent = PushAgent.getInstance(context);
		// 如果已经注册，则直接打开开关
		if (UmengRegistrar.isRegistered(context)) {
			final String token = UmengRegistrar.getRegistrationId(context);
			sendDeviceRegisterBroadcast(context, token);
			mPushAgent.enable();
			return;
		}
		// 否则，进行注册操作，并在注册成功之后上传其TAG和Alias。
		// 注意这里的上传操作必须在注册成功之后，因为在拿到DeviceToken之前是无法上传的。
		mPushAgent.enable(new IUmengRegisterCallback() {

			@Override
			public void onRegistered(String deviceToken) {
				sendDeviceRegisterBroadcast(context, deviceToken);
			}
		});
	}

	//设计上，这里可以发送一个广播，但是由于现在仅有一个地方需要使用，故将处理代码放在这里，暂不发送广播
	private static final void sendDeviceRegisterBroadcast(final Context context,
			String deviceToken) {
//		Intent intent = new Intent(UmengPushConst.ACTION_DEVICETOKEN_AVALIABLE);
//		intent.putExtra(UmengPushConst.EXTRA_DEVICE_TOKEN, deviceToken);
		// context.sendBroadcast(intent);
		Log.d(LOG_TAG, "Device registered: " + deviceToken);
		Prefs.writeString(context, Const.DEVISE_TOKEN , deviceToken);
		sendDeviceToken();
		//网络操作，在异步线程中执行。
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try {
					// 添加公司、部门为Tag，ID为Alias
					// mPushAgent.getTagManager().add(getPushTag());
					String alias = getPushAlias();
					PushAgent mPushAgent = PushAgent.getInstance(context);
					mPushAgent.addAlias(alias, UmengPushConst.ALIAS_TYPE);
					Log.d(LOG_TAG, "tag and alias upload completed. current alias:"
							+ alias);
				} catch (Exception e) {
					Log.w(LOG_TAG,
							"Warning: fail to add push tag and/or alias."
									+ e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
			;
		}.execute();
		
	}
    
	/**
	 * 发送友盟手机设备号给服务器。
	 */
	private static void sendDeviceToken() {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Const.DEVISE_TOKEN, Prefs.readString(AppController.getInstance(), Const.DEVISE_TOKEN));
            
			NetUtil.requestStringData(SRL.Method.METHOD_SEND_DEVICETOKEN,null, map,
					new Response.Listener<String>() {

						@Override
						public void onResponse(String result) {
							Log.i(LOG_TAG, "发送手机设备号："+result);
							
						}
					});
		
	}
	/**
	 * 关闭推送开关，并执行删除TAG和Alias的操作。
	 */
	public static void stopPushService(final Context context) {
		final PushAgent mPushAgent = PushAgent.getInstance(context);

		mPushAgent.setUnregisterCallback(new IUmengUnregisterCallback() {
			@Override
			public void onUnregistered(String arg0) {
				Log.d(LOG_TAG, "Unregistered device: " + arg0);
			}
		});
		performRemoveTagAndAlias(context);
		mPushAgent.disable();
	}

	/**
	 * 从服务器端移除标签和别名。
	 */
	private static final void performRemoveTagAndAlias(final Context context) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					// mPushAgent.getTagManager().delete(getPushTag(data));
					// mPushAgent.removeAlias(getPushAlias(data),
					// UmengPushConst.UPLOAD_ALIAS_TDB);
					// Log.d(LOG_TAG, "tag and alias removed.");
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
	}

	// private static String[] getPushTag() {
	// return null;
	// }
	//
	// 获取学员的Alias
	private static final String getPushAlias() {
//		Coach coach = MainApp.getInstance().getLoginCoach();
//		return coach.getPhoneNumber();
		//TODO 完成手机号的获取
		return Prefs.readString(AppController.getInstance().getApplicationContext(),Const.USER_PHONE);
		
	}
}
