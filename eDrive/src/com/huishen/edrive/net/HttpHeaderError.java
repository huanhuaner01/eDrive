package com.huishen.edrive.net;

import com.android.volley.VolleyError;

/**
 * 代表服务器返回了错误的Http代码值。
 * @author Muyangmin
 * @create 2015-2-27
 */
public final class HttpHeaderError extends VolleyError {
	
	private static final long serialVersionUID = 1L;
	
	private final int statusCode;

	public HttpHeaderError(int statusCode) {
		super();
		this.statusCode = statusCode;
	}

	public final int getStatusCode() {
		return statusCode;
	}
}