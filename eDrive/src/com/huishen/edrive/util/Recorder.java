package com.huishen.edrive.util;

import java.io.File;  

import android.media.MediaPlayer;  
import android.media.MediaRecorder;  
import android.media.MediaRecorder.OnErrorListener;  
import android.os.Environment;
import android.util.Log;  

public class Recorder {  
	// 录音文件播放  
	private MediaPlayer mediaPlayer;  
	private double mEMA = 0.0;  
	static final private double EMA_FILTER = 0.6;  
	// 录音  
	private MediaRecorder mediaRecorder;  
	// 音频文件保存地址  
	public String sendpath;//    
	public String allName;//存储文件名字（加上路径） 
	public static Recorder recorder;
	public static Recorder getInstance(){
		if(recorder == null){
			synchronized (Recorder.class) {
				if(recorder == null){
					recorder = new Recorder();
				}
			}
		}
		return recorder;
	}
	private Recorder(){  
	}  

	//开始录音                String filePath  
	public void startRecord(String name){  
		//        if(mediaRecorder == null){ 
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			mediaRecorder = new MediaRecorder();  
			mediaRecorder.reset();  
			// 从麦克风源进行录音  
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  
			//给出的实例是44100、22050、11025但不限于这几个参数。例如要采集低质量的音频就可以使用4000、8000等低采样率  
			//音频的采样频率，每秒钟能够采样的次数，采样率越高，音质越高。  
			mediaRecorder.setAudioSamplingRate(44100);      
			//只有AAC，才能使ios和Android两个的语音互相通。  
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); 
			sendpath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"edrive";
			File file = new File(sendpath);
			if(!file.exists()){
				file.mkdirs();
			}
			File file2 = new File(sendpath, name);
			allName = file2.getAbsolutePath();
			mediaRecorder.setOutputFile(allName);

			//  mediaRecorder.setOutputFile(file.getAbsolutePath());  
			//            mediaRecorder.setAudioChannels(1); //1：单声道；2：双声道  

			//            File saveFilePath = new File(filePath);  
			//            mediaRecorder.setOutputFile(filePath);  
			/*try { 
                saveFilePath.createNewFile(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
            }*/  
			// 开始录音  
			try {  
				mediaRecorder.prepare();  
				mediaRecorder.start();  
				mediaRecorder.setOnErrorListener(new OnErrorListener() {  
					@Override  
					public void onError(MediaRecorder arg0, int arg1, int arg2) {  
						mediaRecorder.release();  
						mediaRecorder = null;  
					}  
				});  
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
		}  
	}

	//停止录制并保存  
	public void stopRecord(){  
		try{  
			if(mediaRecorder != null){  
				mediaRecorder.stop();  
				mediaRecorder.release();  
				mediaRecorder = null;  
			}  
			Log.i("Recorder","stopRecord !");  
		}catch (Exception e) {  
			e.printStackTrace();  
		}  

	}  

	//退出  
	public void destory(){  
		if(mediaPlayer != null){  
			if(mediaPlayer.isPlaying()){  
				mediaPlayer.stop();  
				mediaPlayer.release();  
				mediaPlayer = null;  
			}  
		}  
		if(mediaRecorder != null){  
			mediaRecorder.release();  
			mediaRecorder = null;  
		}  

	}  

	public String nowPlayPath;
	//开始播放    
	public void startPlay(String path){  

		Log.i("startPlay", path);  
		nowPlayPath = path;
		if(mediaPlayer == null){
			mediaPlayer = new MediaPlayer(); 
		}
		try {  
			mediaPlayer.reset();//设置为初始状态
			if (mediaPlayer.isPlaying()) {    
				mediaPlayer.stop();    
			}    
			mediaPlayer.setDataSource(path);  
			mediaPlayer.prepare();  
			mediaPlayer.start();  
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()  
			{  
				@Override  
				public void onCompletion(MediaPlayer mPlayer) {  
					mPlayer.release();  
					mediaPlayer = null;  
				}  
			});  

			mediaPlayer.setOnErrorListener(new android.media.MediaPlayer.OnErrorListener() {  
				@Override  
				public boolean onError(MediaPlayer arg0, int arg1, int arg2) {  
					mediaPlayer.release();//当调用了release()方法后，它就处于End状态  
					mediaPlayer = null;  
					return false;  
				}  
			});  
		}catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  

	//停止播放  
	public void stopPlay(){  
		if(mediaPlayer != null){  
			if(mediaPlayer.isPlaying()){  
				mediaPlayer.stop();  
				mediaPlayer.release();  
				mediaPlayer = null;  
			}  
		}  
	}  
	//是否正在播放
	public boolean isPlaying(){ 
		if(mediaPlayer == null){
			return false;
		}
		else{
			return mediaPlayer.isPlaying();
		}
	} 

	public double getAmplitude() {  
		if (mediaRecorder != null)  
			return (mediaRecorder.getMaxAmplitude() / 2700.0);  
		else  
			return 0;  
	}  
//	public double getAmplitude() {  
//		if (mediaRecorder != null)  
//			return (5*mediaRecorder.getMaxAmplitude() / 2700.0);  
//		else  
//			return 0;  
//	}

	public double getAmplitudeEMA() {  
		double amp = getAmplitude();  
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;  
		return mEMA;  
	}  

	public MediaPlayer getMediaPlayer() {  
		return mediaPlayer;  
	}  

	public void setMediaPlayer(MediaPlayer mediaPlayer) {  
		this.mediaPlayer = mediaPlayer;  
	}  

	public MediaRecorder getMediaRecorder() {  
		return mediaRecorder;  
	}  

	public void setMediaRecorder(MediaRecorder mediaRecorder) {  
		this.mediaRecorder = mediaRecorder;  
	}  



} 