package com.huishen.edrive.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

/**
 * @author Muyangmin
 * @create 2015-3-4
 */
public class SimpleAudioPlayer {
	private static final String LOG_TAG="SimpleRingPlayer";
	private final OnPreparedListener prelistener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			mp.start();
		}
	};
	private final OnCompletionListener comlistener = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			mp.reset();
		}
	};
	
	private static SimpleAudioPlayer instance;
	private final MediaPlayer mPlayer = new MediaPlayer();
	
	private SimpleAudioPlayer(){
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mPlayer.setOnCompletionListener(comlistener);
		mPlayer.setOnPreparedListener(prelistener);
	}
	
	public static final SimpleAudioPlayer getInstance(){
		if (instance==null){
			synchronized (SimpleAudioPlayer.class) {
				if (instance==null) {
					instance = new SimpleAudioPlayer();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 播放一个音频文件。
	 * @param context 上下文
	 * @param abspath 目标文件的绝对路径
	 * @lastModify 2014-4-13 by Muyangmin
	 * @description (optional)
	 */
	public final void playFileAudio(Context context, final String abspath){
		try {
			mPlayer.setDataSource(abspath);
			mPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			Log.w(LOG_TAG, "failed to play ring:"+abspath+";"+e.getMessage());
		} catch (IOException e) {
			Log.w(LOG_TAG, "failed to play ring:"+abspath+";"+e.getMessage());
		}
	}
	/**
	 * 播放Assets下的音效。
	 * @param context 上下文信息。
	 * @param assets 要播放的音频文件名。
	 */
	public final void playAssetsAudio(Context context, final String assets){
		try {
			AssetManager am = context.getAssets();
			AssetFileDescriptor afd = am.openFd(assets);
			mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			Log.w(LOG_TAG, "failed to play ring:"+assets+";"+e.getMessage());
		} catch (IOException e) {
			Log.w(LOG_TAG, "failed to play ring:"+assets+";"+e.getMessage());
		}
	}
}
