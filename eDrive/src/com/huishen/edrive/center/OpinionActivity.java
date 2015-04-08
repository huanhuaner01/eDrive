package com.huishen.edrive.center;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.SplashActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.widget.BaseActivity;
import com.huishen.edrive.widget.LoadingDialog;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 
 * @author zhanghuan
 *
 */
public class OpinionActivity extends BaseActivity {
	private String TAG = "OpinionActivity" ;
    private TextView title  ,note;
    private ImageButton back ;
    private EditText edit ;
    private LoadingDialog dialog ; //加载框
	/***************************腾讯统计相关框架*************************************/

	/***************************腾讯统计基本框架结束*************************************/
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opinion);
		AppController.getInstance().addActivity(this);
		this.setTag("OpinionActivity");
		registView();
		initView();
	}
	private void registView() {
		title = (TextView)findViewById(R.id.header_title);
		back = (ImageButton)findViewById(R.id.header_back);
		edit = (EditText)findViewById(R.id.opinion_edit);
		note = (TextView)findViewById(R.id.header_note);
	}
	private void initView() {
		this.title.setText("意见反馈");
		dialog = new LoadingDialog(this);
		this.back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
		this.note.setText("确定");
		this.note.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				sendOpinion();
			}
			
		});
	}
	
	/**
	 * 
	 */
	private void sendOpinion(){
		if(edit.getText().toString().equals("")){
			AppUtil.ShowShortToast(getApplicationContext(), "亲，请填写反馈内容！");
			return ;
		}
		if(!isFinishing()&& !dialog.isShowing()){
			dialog.show();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("content",edit.getText().toString());
		note.setEnabled(false);
		NetUtil.requestStringData(SRL.Method.METHOD_OPINION, TAG ,map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						note.setEnabled(true);
						if(dialog.isShowing()){
							dialog.dismiss();
						}
						Log.i(TAG, result);
						if(result == null || result.equals("")){
							AppUtil.ShowShortToast(OpinionActivity.this, "服务器繁忙");
						}else{
							try{
								JSONObject json = new JSONObject(result);
								if(json.getInt("code")==0){
									AppUtil.ShowShortToast(OpinionActivity.this, "成功");
									finish();
								}
							}catch(Exception e){
								
							}
						}
					}
				},new DefaultErrorListener(this,note,dialog));
		
	}
}
