package com.huishen.edrive.net;

import org.apache.http.protocol.HTTP;

import com.android.volley.Response;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
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
 */
// package access
class AbsStringRequest extends StringRequest {

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

	/**
	 * 默认的异常监听器。由于客户端代码不传递ErrorListener时才会使用这个监听器，
	 * 故认为用户并不关心本次请求中发生的异常，因此只打印堆栈信息，不做其他操作。
	 */
	private static final ErrorListener defaultErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
		}
	};

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
