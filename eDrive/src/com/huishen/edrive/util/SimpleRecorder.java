package com.huishen.edrive.util;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 实现简单的录音功能。
 * 
 * @author Muyangmin
 * @create 2015-2-13
 */
public final class SimpleRecorder {

	private static final String LOG_TAG = "SimpleRecorder";

	private static SimpleRecorder instance;
	private MediaRecorder mediaRecorder;

	private SimpleRecorder() {
	}

	public static SimpleRecorder getInstance() {
		if (instance == null) {
			synchronized (SimpleRecorder.class) {
				instance = new SimpleRecorder();
			}
		}
		return instance;
	}

	/**
	 * 开始录音。
	 * 
	 * @param file
	 *            要保存的录音文件名称
	 */
	public void startRecord(File file) {
		// 检查文件存在
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (mediaRecorder != null) {
			throw new IllegalStateException(
					"should call stopRecord() before next recorder start.");
		}
		// 初始化录音机
		mediaRecorder = new MediaRecorder();
		// 设置录音的来源为麦克风
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		mediaRecorder.setOutputFile(file.getAbsolutePath());
		try {
			mediaRecorder.prepare();
			mediaRecorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 终止当前进行中的录音。
	 */
	public void stopRecord() {
		if (mediaRecorder != null) {
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
		}
	}

	/**
	 * 播放录音文件。
	 * 
	 * @param file
	 *            要播放的文件
	 */
	public void playAudioFile(final File file) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					final MediaPlayer mPlayer = new MediaPlayer();
					mPlayer.setDataSource(file.getAbsolutePath());
					mPlayer.setOnCompletionListener(new OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							mPlayer.release();
						}
					});
					mPlayer.prepare();
					mPlayer.start();
				} catch (IOException ioe) {
					Log.w(LOG_TAG, "failed to play audio file:" + file);
				}
				return null;
			}
		}.execute();
	}
}
