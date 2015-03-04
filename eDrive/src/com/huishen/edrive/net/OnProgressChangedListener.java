package com.huishen.edrive.net;

/**
 * 监听任务进度。
 * @author Muyangmin
 * @create 2015-3-4
 */
public interface OnProgressChangedListener {
	/**
	 * 进度变化时调用。
	 * @param min 最小进度（一般为0）。
	 * @param max 最大进度（一般为100）。
	 * @param progress 当前进度。
	 */
	void onProgressChanged(int min, int max, int progress);
	/**
	 * 完成操作时调用。
	 */
	void onTaskFinished();
}
