package com.huishen.edrive.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import android.os.AsyncTask;
import android.util.Log;

/**
 * 使用HTTP协议进行文件或图片的上传。
 * 
 * @author Muyangmin
 * @create 2015-2-12
 */
final class HttpUploadTask extends AsyncTask<Void, Void, String>{
	private static final String LOG_TAG = "HttpUploader";
	private static final int TIME_OUT = 10 * 1000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码
	
	private File file;
	private String requestURL;
	private UploadResponseListener listener;
	private Map<String, String> requestParams;
	private int errorHttpCode;
	
	//package access
	HttpUploadTask(File file, String requestURL, UploadResponseListener listener) {
		super();
		this.file = file;
		this.requestURL = requestURL;
		this.listener = listener;
	}
	//package access
	HttpUploadTask(File file, String requestURL, Map<String, String> params, UploadResponseListener listener) {
			super();
			this.file = file;
			this.requestURL = requestURL;
			this.requestParams = params;
			this.listener = listener;
		}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result==null){
			listener.onError(errorHttpCode);
		}
		else{
			listener.onSuccess(result);
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		int res = 0;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型

		try {
			//HackUrl
			if (requestParams!=null && requestParams.size()>0){
				StringBuilder sb = new StringBuilder(requestURL).append("?");
				for (String key : requestParams.keySet()){
					sb.append(key).append("=").append(requestParams.get(key));
				}
				requestURL = sb.toString();
				Log.d(LOG_TAG, "Final url:"+requestURL);
			}
			URL url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			Log.d(LOG_TAG, "required properties set completed. now uploading...");
			if (file != null) {
				/**
				 * 当文件不为空时执行上传
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名
				 */

				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				int progress = 0;//记录百分比，用于通知监听器
				int hasFinished=0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
					hasFinished += len;
					int curprg = (int) (hasFinished*100/(double)file.length());
					if (curprg != progress){
						progress = curprg;
						listener.onProgressChanged(progress);
					}
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				res = conn.getResponseCode();
				if (res == 200) {
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					return sb1.toString();
				}
				//else :error, return default null
				else{
					errorHttpCode = res;
					return null;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
