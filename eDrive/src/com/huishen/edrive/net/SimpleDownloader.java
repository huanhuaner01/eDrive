package com.huishen.edrive.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

/**
 * 提供文件下载功能。
 * 
 * @author Muyangmin
 * @create 2014-4-10
 * @version 1.0
 */
public class SimpleDownloader {
	private static final String LOG_TAG = "SimpleDownloader";

	private boolean downloading = false;
	private boolean cancelDownload = false;
	private OnProgressChangedListener onProgressChangedListener;
	
	/**
	 * 注册进度监听接口。
	 */
	public void setOnProgressChangedListener(OnProgressChangedListener listener) {
		this.onProgressChangedListener = listener;
	}
	/**
	 * 将指定的URL资源下载并保存为文件。方法需要在异步线程中调用。
	 * 
	 * @param url
	 *            要下载资源的源地址。
	 * @param target
	 *            要保存的目标文件名，注意，必须指定后缀名。
	 * @throws IOException
	 *             如果URL或文件访问错误。
	 */
	public void download(String url, File target) throws IOException {
		try {
			download(new URL(url), target);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将指定的URL资源下载并保存为文件。方法需要在异步线程中调用。
	 * 
	 * @param source
	 *            要下载资源的源地址。
	 * @param target
	 *            要保存的目标文件名，注意，必须指定后缀名。
	 * @throws IOException
	 *             如果URL或文件访问错误。
	 */
	public void download(URL source, File target) throws IOException {
		downloading = true;
		URLConnection conn = source.openConnection();
		conn.connect();
		BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		if (!target.exists()){
			target.getParentFile().mkdirs();
			target.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(target);

		int length = conn.getContentLength();
		int hasdownload = 0;
		byte[] buf = new byte[1024];
		while (!cancelDownload) {
			int read = bis.read(buf);
			if (read == -1) {
				break;
			}
			fos.write(buf, 0, read);
			hasdownload += read;
			if (onProgressChangedListener != null){
				onProgressChangedListener.onProgressChanged(0, 100, (int) (hasdownload / (double) length * 100));
			}
		}
		bis.close();
		fos.close();
		if (onProgressChangedListener!=null){
			onProgressChangedListener.onTaskFinished();
		}
		Log.i(LOG_TAG, "从" + source + "的下载（文件" + target.getName() + "）已完成。");
		// this is the final operation
		resetDownloader();
	}

	// 重置下载器。
	private void resetDownloader() {
		downloading = false;
	}

	/**
	 * 取消正在下载的任务（如果有的话）。
	 */
	public void cancelDownload() {
		if (downloading)
			cancelDownload = true;
	}
}
