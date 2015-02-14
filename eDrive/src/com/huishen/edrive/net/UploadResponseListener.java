package com.huishen.edrive.net;

/**
 * 监听上传的结果。
 * 
 * @author Muyangmin
 * @create 2015-2-12
 */
public interface UploadResponseListener {
	/**
	 * 处理上传操作返回的结果。该方法被调用意味着服务器端返回了200 状态码。
	 * 
	 * @param str 结果
	 */
	void onSuccess(String str);

	/**
	 * 处理上传操作返回的结果。
	 * 
	 * @param httpCode HTTP状态码
	 */
	void onError(int httpCode);

	/**
	 * 监听上传百分比。
	 * <p>
	 * <b>注意：不要试图在百分比等于100时做最后的操作，这些操作应该始终放在 {@link #onSuccess(String)}中进行。</b>
	 * </p>
	 * 
	 * @param hasFinished
	 *            已完成的百分比
	 */
	void onProgressChanged(int hasFinished);
}
