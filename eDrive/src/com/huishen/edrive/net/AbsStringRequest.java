package com.huishen.edrive.net;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

/**
 * 继承 {@link StringRequest}类，简化创建对象的参数。
 * <p>
 * 该类型请求的默认创建方式是使用<b>UTF-8</b>进行编码并进行<b>POST</b>传输， 可以通过在构造器中传递参数改变传输方式， 重写
 * {@link #getParamsEncoding()}以改变编码集。
 * </p>
 * 
 * @author Muyangmin
 * @create 2015-2-7
 * @version 1.1 on 2015/02/28 by Muyangmin 修改了超时策略并增加了对Cookie和部分HTTP响应头的处理。</br>
 * 			1.0 基础版本。
 */
// package access
class AbsStringRequest extends StringRequest {
	
	//默认的超时时间
	private static final int REQUEST_TIMEOUT = 15 * 1000;
	
	private static final int HTTP_SUCCESS = 200;

	/**
	 * 默认的异常监听器。由于客户端代码不传递ErrorListener时才会使用这个监听器，
	 * 故认为用户并不关心本次请求中发生的异常，因此只打印堆栈信息，不做其他操作。
	 */
	private static final ErrorListener defaultErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			if (error instanceof HttpHeaderError){
				Log.e("NetRequest", "HttpHeaderError:"+error);
			}
			error.printStackTrace();
		}
	};
	
	/**
	 * 创建一个新的String网络访问请求，使用POST方式提交参数。
	 * 
	 * @param url
	 *            服务器资源的绝对位置。
	 * @param listener
	 *            处理结果的回调监听器。
	 */
	public AbsStringRequest(String url, Listener<String> listener) {
		this(Method.POST, url, listener);
	}

	/**
	 * 创建一个新的String网络访问请求。
	 * 
	 * @param url
	 *            服务器资源的绝对位置。
	 * @param listener
	 *            处理结果的回调监听器。
	 * @param errListener
	 *            处理异常事件的监听器。
	 */
	public AbsStringRequest(String url, Response.Listener<String> listener,
			Response.ErrorListener errListener) {
		super(Method.POST, url, listener, errListener);
	}
	
	/**
	 * 创建一个新的String网络访问请求。
	 * 
	 * @param method
	 *            参数的提交方式，应当使用 {@link Method}中的常量。
	 * @param url
	 *            服务器资源的绝对位置。
	 * @param listener
	 *            处理结果的回调监听器。
	 */
	public AbsStringRequest(int method, String url,
			Response.Listener<String> listener) {
		super(method, url, listener, defaultErrorListener);
	}

	/**
	 * 创建一个新的String网络访问请求。
	 * 
	 * @param method
	 *            参数的提交方式，应当使用 {@link Method}中的常量。
	 * @param url
	 *            服务器资源的绝对位置。
	 * @param listener
	 *            处理结果的回调监听器。
	 * @param errListener
	 *            处理异常事件的监听器。
	 */
	public AbsStringRequest(int method, String url,
			Response.Listener<String> listener,
			Response.ErrorListener errListener) {
		super(method, url, listener, errListener);
	}

	//重写超时及重试策略
	@Override
	public RetryPolicy getRetryPolicy() {
		return new DefaultRetryPolicy(REQUEST_TIMEOUT,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
	}
	
	/**
	 * 重写以获取Cookie。
	 */
	@Override
	protected Response<String> parseNetworkResponse(
			NetworkResponse response) {
		if (response.statusCode != HTTP_SUCCESS){
			return Response.error(new HttpHeaderError(response.statusCode));
		}
		try {
			Map<String, String> responseHeaders = response.headers;
			String rawCookies = responseHeaders.get("Set-Cookie");
			//仅当Cookie不为空的时候才调用回调
			if ((rawCookies != null) && (!rawCookies.equals(""))) {
				onReadCookie(rawCookies);	
			}
			String dataString = new String(response.data, "UTF-8");
			return Response.success(dataString,HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} 
	}

	/**
	 * 希望获取Cookie的子类可以重写该方法。注意:
	 * <ul>
	 * <li>当服务器返回值是null时，该方法将不会被调用。</li>
	 * <li>Volley框架只支持读取第一个Cookie值，对于服务器返回多个Cookie的情况需要修改框架源码。</li>
	 * <ul>
	 * 
	 * @param cookie
	 *            响应头中包含的Cookie，即“Set-Cookie”的值。
	 */
	protected void onReadCookie(String cookie) {
		//empty
	}

	/**
	 * 指定参数的编码，子类通常不要重写该方法。
	 * 
	 * @return 始终返回 {@link HTTP#UTF_8}.
	 * @see com.android.volley.Request#getParamsEncoding()
	 */
	@Override
	protected String getParamsEncoding() {
		return HTTP.UTF_8;
	}
	
}
