package com.huishen.edrive.net;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;

/**
 * 放置与网络相关的快捷方法，并对外隐藏服务器的根地址。
 * 
 * @author Muyangmin
 * @create 2015-2-7
 */
public final class NetUtil {

	/**
	 * 公用的网络请求日志标签。供匿名类等没有自己的TAG字段的对象使用。
	 */
	private static final String LOG_TAG = "NetRequest";
	
	/**
	 * 将相对路径转换为绝对路径。
	 * @param relativePath 相对路径
	 * @return 绝对路径
	 */
	private static final String getAbsolutePath(String relativePath){
		String absPath = ServerAddressProvider.getServerAddress()
				+ relativePath;
		Log.d(LOG_TAG, "requesting " + absPath);
		return absPath;
	}

	/**
	 * 获取MobileFlag。
	 * 
	 * @return 如果没有获取到，则返回null。
	 */
	private static final String getMobileFlag() {
		return Prefs.readString(AppController.getInstance().getApplicationContext(),
				Const.USER_MOBILEFLAG);
	}
	
	/**
	 * 手动解析Cookie中的JESSIONID字段，过滤其他无关的信息。
	 * @param rawCookie 原始Cookie字符串
	 * @return 返回解析后的字符串，格式为 【JSESSIONID=[A-Z0-9]+】。
	 */
	private static final String resolveUsefulCookie(String rawCookie){
		if (rawCookie==null){
			return null;
		}
		//以;结尾
		Pattern pattern = Pattern.compile("JSESSIONID=[A-Z0-9]+");
		Matcher matcher = pattern.matcher(rawCookie);
		if (matcher.find()){
			String finalCookie = matcher.group();
			Log.d(LOG_TAG, "final Cookie:" + finalCookie);
			return finalCookie;
		}
		else{
			Log.e(LOG_TAG, "cannnot parse well formatted cookie.");
			return null;
		}
	}

	/**
	 * 提交String数据请求。
	 * 
	 * @param relativePath
	 *            资源的相对位置
	 * @param listener
	 *            回调监听器。强烈建议使用 {@link ResponseListener}而不是原始的  {@link Listener}。
	 */
	public static final void requestStringData(String relativePath,
			Listener<String> listener) {
		if (relativePath == null || listener == null) {
			throw new NullPointerException("params cannot be null!");
		}
		HashMap<String, String> params = new HashMap<String, String>();
		// 添加标记
		String flag = getMobileFlag();
		if (flag != null) {
			params.put(SRL.Param.PARAM_MOBILE_FLAG, flag);
		}
		Log.d(LOG_TAG, "request params:"+params);
		AppController.getInstance().addNetworkRequest(
				new AbsStringRequest(getAbsolutePath(relativePath), listener){
					@Override
					protected void onReadCookie(String cookie) {
						Log.d(LOG_TAG, "RawCookie:"+cookie);
						AppController.getInstance().setSessionId(resolveUsefulCookie(cookie));
					}
					@Override
					public Map<String, String> getHeaders() throws AuthFailureError {
						String cookie = AppController.getInstance().getSessionId();
						if (cookie==null){
							return super.getHeaders();
						}
						HashMap<String, String> localHashMap = new HashMap<String, String>();	
						localHashMap.put("Cookie", cookie);
						return localHashMap;
					}
				});
	}

	/**
	 * 提交带有参数的String数据请求，忽略异常信息。
	 * 
	 * @param relativePath
	 *            资源的相对位置
	 * @param params
	 *            要提交的参数
	 * @param listener
	 *            回调监听器。强烈建议使用 {@link ResponseListener}而不是原始的  {@link Listener}。
	 */
	public static final void requestStringData(String relativePath,
			final Map<String, String> params, Listener<String> listener) {
		if (relativePath == null || params == null || listener == null) {
			throw new NullPointerException("params cannot be null!");
		}
		//添加标记
		String flag = getMobileFlag();
		if (flag!=null){
			params.put(SRL.Param.PARAM_MOBILE_FLAG, flag);
		}
		Log.d(LOG_TAG, "request params:"+params);
		AppController.getInstance().addNetworkRequest(
				new AbsStringRequest(getAbsolutePath(relativePath), listener) {
					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
						return params;
					}
					@Override
					protected void onReadCookie(String cookie) {
						Log.d(LOG_TAG, "RawCookie:"+cookie);
						AppController.getInstance().setSessionId(resolveUsefulCookie(cookie));
					}
					@Override
					public Map<String, String> getHeaders() throws AuthFailureError {
						String cookie = AppController.getInstance().getSessionId();
						if (cookie==null){
							return super.getHeaders();
						}
						HashMap<String, String> localHashMap = new HashMap<String, String>();	
						localHashMap.put("Cookie", cookie);
						return localHashMap;
					}
				});
	}

	/**
	 * 提交带有参数的String数据请求，并处理异常信息。
	 * 
	 * @param relativePath
	 *            资源的相对位置
	 * @param params
	 *            要提交的参数
	 * @param listener
	 *            回调监听器。强烈建议使用 {@link ResponseListener}而不是原始的  {@link Listener}。
	 * @param errlisListener
	 *            网络异常监听器
	 */
	public static final void requestStringData(String relativePath,
			final Map<String, String> params, Listener<String> listener,
			ErrorListener errlisListener) {
		if (relativePath == null || params == null || listener == null) {
			throw new NullPointerException("params cannot be null!");
		}
		// 添加标记
		String flag = getMobileFlag();
		if (flag != null) {
			params.put(SRL.Param.PARAM_MOBILE_FLAG, flag);
		}
		Log.d(LOG_TAG, "request params:"+params);
		AppController.getInstance().addNetworkRequest(
				new AbsStringRequest(getAbsolutePath(relativePath), listener, errlisListener) {
					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
						return params;
					}
					@Override
					protected void onReadCookie(String cookie) {
						Log.d(LOG_TAG, "RawCookie:"+cookie);
						AppController.getInstance().setSessionId(resolveUsefulCookie(cookie));
					}
					@Override
					public Map<String, String> getHeaders() throws AuthFailureError {
						String cookie = AppController.getInstance().getSessionId();
						if (cookie==null){
							return super.getHeaders();
						}
						HashMap<String, String> localHashMap = new HashMap<String, String>();	
						localHashMap.put("Cookie", cookie);
						return localHashMap;
					}
				});
	}

	/**
	 * 请求上传文件。
	 * 
	 * @param file
	 *            要上传的文件
	 * @param relativePath
	 *            服务器的目标相对路径
	 * @param listener
	 *            回调监听器
	 */
	public static final void requestUploadFile(final File file,
			final String relativePath, final UploadResponseListener listener) {
		if (relativePath == null || listener == null) {
			throw new NullPointerException("params cannot be null!");
		}
		new HttpUploadTask(file, getAbsolutePath(relativePath), listener).execute();
	}
}
