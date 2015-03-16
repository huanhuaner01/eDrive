package com.huishen.edrive.center;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;

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
public class OpinionActivity extends Activity {
	private String TAG = "OpinionActivity" ;
    private TextView title  ,note;
    private ImageButton back ;
    private EditText edit ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opinion);
		AppController.getInstance().addActivity(this);
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
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("content",edit.getText().toString());
		NetUtil.requestStringData(SRL.Method.METHOD_OPINION, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
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
				},new DefaultErrorListener());
		
	}
}
