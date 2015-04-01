package com.huishen.edrive.center;

import java.io.File;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.huishen.edrive.InfoActivity;
import com.huishen.edrive.R;
import com.huishen.edrive.SplashActivity;
import com.huishen.edrive.VersionActivity;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.login.VerifyPhoneActivity;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.OnProgressChangedListener;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Packages;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author zhanghuan
 *
 */
public class SettingActivity extends Activity {
   private String TAG = "SettingActivity" ;
   private TextView title ;
   private Button logout ;
   private ImageButton back ;
   private LinearLayout softup , opinion ,about ;
   
	/***************************腾讯统计相关框架*************************************/
	StatLogger logger = SplashActivity.getLogger();
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	   @Override
		protected void onPause() {
			super.onPause();
			StatService.onPause(this);
		}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Debug.stopMethodTracing();
	}
	/***************************腾讯统计基本框架结束*************************************/
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		AppController.getInstance().addActivity(this);
		registView();
		initView();
	}
	private void registView() {
		title = (TextView)findViewById(R.id.header_title);
		back = (ImageButton)findViewById(R.id.header_back);
		softup = (LinearLayout)findViewById(R.id.setting_softup);
		opinion = (LinearLayout)findViewById(R.id.setting_opinion);
		about = (LinearLayout)findViewById(R.id.setting_about);
		logout = (Button)findViewById(R.id.setting_logout);
	}
	private void initView() {
		title.setText("设置");
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
		logout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				AppUtil.removeAllData(getApplicationContext());
				AppController.getInstance().exitMain(getApplicationContext());
				Intent i = new Intent(SettingActivity.this, VerifyPhoneActivity.class);
				i.putExtra("tag", 1);
				startActivity(i);
				finish();
			}
			
		});
		softup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				checkSoftwareUpdate();
			}
			
		});
		about.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(SettingActivity.this ,InfoActivity.class);
				i.putExtra("title", "关于e驾");
				i.putExtra("url", SRL.Method.METHOD_ABOUT);
				startActivity(i);
			}
		});
		opinion.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(SettingActivity.this ,OpinionActivity.class);
				startActivity(i);
			}
			
		});
	}
	
	private void checkSoftwareUpdate(){
		HashMap<String , String> hashMap = new HashMap<String, String>();
		hashMap .put(SRL.Param.PARAM_UPDATE_SOFTKEY, "stu_client.apk");
		NetUtil.requestStringData(SRL.Method.METHOD_CHECK_UPDATE, hashMap, getUpdateResponseListener(),
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						AppUtil.ShowShortToast(getApplicationContext(), "服务器繁忙");
					}
				});
	}
	private Listener<String> getUpdateResponseListener(){

		return new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				try {
					final JSONObject json = new JSONObject(arg0);
					int servercode = json.getInt(SRL.ReturnField.FIELD_UPDATE_SERVER_VERSIONCODE);
					int localcode = Packages.getVersioCode(SettingActivity.this);
					Log.d(TAG, "server=" + servercode + ",local=" + localcode);
					if (servercode <= localcode){
						Log.d(TAG, "No avaliable update found.");
						AppUtil.ShowShortToast(getApplicationContext(), "版本已经是最新");
						Intent i = new Intent(SettingActivity.this,VersionActivity.class);
						startActivity(i);
						return;
					}
					final boolean forceUpdate = json.optInt(SRL.ReturnField.FIELD_UPDATE_FORCE_UPDATE)==1?true:false;
					new AlertDialog.Builder(SettingActivity.this)
					.setMessage(getString(R.string.str_checkupdate_update_avaliable))
					.setCancelable(false) 
//					.setMessage(getString(R.string.str_checkupdate_update_description, 
//							json.optString(SRL.ReturnField.FIELD_UPDATE_SERVER_VERSIONNAME), 
//							json.optString(SRL.ReturnField.FIELD_UPDATE_SERVER_VERSIONDESC)))
					.setPositiveButton(R.string.str_checkupdate_update_now, 
							new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.i("splash", json.optString(SRL.ReturnField.FIELD_UPDATE_APK_PATH));
							performUpdate(json.optString(SRL.ReturnField.FIELD_UPDATE_APK_PATH));
						}
					}).setNegativeButton(R.string.str_checkupdate_update_later, 
							new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (forceUpdate){
								dialog.dismiss();
								AppController.getInstance().exit(getApplicationContext());
								finish();
							}
							else{
								dialog.dismiss();
							}
						}
					}).create().show();
				} catch (JSONException e) {
					AppUtil.ShowShortToast(getApplicationContext(), "服务器繁忙");
					e.printStackTrace();
				}
			}
			
		};
	} 
	
	private void performUpdate(String path){
		String state = Environment.getExternalStorageState();
		final File file;
		if (Environment.MEDIA_MOUNTED.equals(state)){
			file = new File(Environment.getExternalStorageDirectory()+File.separator, "ecoach.apk");
		}
		else{
			file = new File(getFilesDir() + File.separator, "edrive.apk");	
		}
		Log.d(TAG, "apk path:"+file.getAbsolutePath());
		final ProgressDialog dialog = createDownloadDialog();
		dialog.show();
		NetUtil.requestDownloadFileUsingAbsPath(path, file, new OnProgressChangedListener() {
			
			@Override
			public void onTaskFinished() {
				dialog.dismiss();
				finish();//避免用户不更新应用而直接返回时看到的无响应状态。
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				startActivity(intent);
			}
			
			@Override
			public void onTaskFailed() {
				dialog.dismiss();
				Toast.makeText(SettingActivity.this, getString(R.string.str_splash_download_fail),
						Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onProgressChanged(int min, int max, int progress) {
				if (min==0 && max==100){
					dialog.setProgress(progress);
				}
				else{
					dialog.setProgress((int) (progress/(double)(max-min)));
				}
			}
		});
	}
	
	private ProgressDialog createDownloadDialog(){
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
		dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
		dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
		dialog.setIcon(R.drawable.ic_launcher);// 设置提示的title的图标，默认是没有的
		dialog.setTitle("提示");
		dialog.setMax(100);
		dialog.setMessage(getString(R.string.str_splash_downloading));
		return dialog;
	}
	
}
