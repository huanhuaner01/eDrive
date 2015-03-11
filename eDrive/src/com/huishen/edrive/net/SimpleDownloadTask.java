package com.huishen.edrive.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

/**
 * 执行下载任务。下载成功则结果为true，否则返回false。
 * 子类也可以通过覆写 {@link #onErrorOccured(Exception)}的方式来处理异常。
 * @author Muyangmin
 * @create 2015-3-6
 */
public class SimpleDownloadTask extends AsyncTask<Void, Integer, Boolean>{
	
	private File file;
	private String url;
	
	public SimpleDownloadTask(File file, String url) {
		super();
		this.file = file;
		this.url = url;
	}

	@Override
	protected final Boolean doInBackground(Void... params) {
		try {
			download(file, url);
		} catch (Exception e) {
			onErrorOccured(e);
			return false;
		}
		return true;
	}
	
	private void download(File file, String path) throws Exception {
		URL url = new URL(path);	//throws MalformedURLException
		URLConnection conn = url.openConnection();
		conn.connect();
		BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		if (!file.exists()){
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);

		int length = conn.getContentLength();
		int hasdownload = 0;
		byte[] buf = new byte[1024];
		int read;
		while ( (read = bis.read(buf))>0 ) {
			fos.write(buf, 0, read);
			hasdownload += read;
			publishProgress(0, 100, (int)(hasdownload/(double)length *100));
		}
		bis.close();
		fos.close();
	}
	
	/**
	 * 实际参数共有三个，依次为：min, max, progress. 
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
	/**
	 * 希望处理下载异常的子类可以重写该方法。
	 */
	protected void onErrorOccured(Exception e){
		e.printStackTrace();
	}
}
