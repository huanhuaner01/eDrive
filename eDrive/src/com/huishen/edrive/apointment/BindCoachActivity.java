package com.huishen.edrive.apointment;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Prefs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 绑定教练
 * @author zhanghuan
 *
 */
public class BindCoachActivity extends Activity implements OnClickListener{
   private TextView title  ,tel ,stuname ,stupass;
   private ImageButton back ;
   private Button commit ;
   private EditText edit ;
   private LinearLayout telLay ,stunameLay ,stupassLay ;
   
   private Intent intent ; //填写数据的intent
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_coach);
		AppController.getInstance().addActivity(this);
		registView();
		initView();
	}
	private void registView() {
		title = (TextView) findViewById(R.id.header_title) ;
		tel = (TextView) findViewById(R.id.bind_coach_tv_tel) ;
		stuname = (TextView) findViewById(R.id.bind_coach_tv_stuname) ;
		stupass = (TextView) findViewById(R.id.bind_coach_tv_stupass) ;
		back = (ImageButton) findViewById(R.id.header_back) ;
		telLay = (LinearLayout) findViewById(R.id.bind_coach_tel) ;
		stunameLay = (LinearLayout) findViewById(R.id.bind_coach_stuname) ;
		stupassLay = (LinearLayout) findViewById(R.id.bind_coach_stupass);
		commit = (Button) findViewById(R.id.bind_coach_commit);
		edit = (EditText)findViewById(R.id.bind_edit);
	}
	private void initView() {
		title.setText(this.getResources().getString(R.string.bind_coach));
		tel.setText(Prefs.readString(getApplicationContext(), SRL.Param.PARAM_BIND_COACH_PHONE));
		stuname.setText(Prefs.readString(getApplicationContext(), SRL.Param.PARAM_BIND_STU_NAME)) ;
		this.back.setOnClickListener(this) ;
		this.telLay.setOnClickListener(this) ;
		this.stunameLay.setOnClickListener(this) ;
		this.stupassLay.setOnClickListener(this) ;
		commit.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.header_back:
			this.finish();
			break ;
		case R.id.bind_coach_tel:
			intentActivty(1); //1代表填写电话号码
			break ;
		case R.id.bind_coach_stuname:
			intentActivty(2); //2代表填写学员姓名
			break ;
		case R.id.bind_coach_stupass:
			intentActivty(3); //3代表填写密码
			break ;
		case R.id.bind_coach_commit:
			commitInfo();
			break ;
		}
	}
	/**
	 * 启动 BindCoachWriteActivity
	 * @param parem
	 */
	private void intentActivty(int parem){
		if(intent == null){
		intent = new Intent(this,BindCoachWriteActivity.class);
		}
		intent.removeExtra("key");
		intent.putExtra("key",parem) ;
		this.startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		initView();
		super.onResume();
		
	}
	
	private void commitInfo(){
		String tel = Prefs.readString(getApplicationContext(), SRL.Param.PARAM_BIND_COACH_PHONE);
		String name = Prefs.readString(getApplicationContext(), SRL.Param.PARAM_BIND_STU_NAME) ;
		String content = edit.getText().toString() ;
		if(tel.equals("")){
			AppUtil.ShowShortToast(getApplicationContext(), "请填写教练电话号码");
			return ;
		}
		if(name.equals("")){
			AppUtil.ShowShortToast(getApplicationContext(), "请填写学员姓名");
			return ;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SRL.Param.PARAM_BIND_COACH_PHONE, tel);
		
		map.put(SRL.Param.PARAM_BIND_STU_NAME,name );
		map.put(SRL.Param.PARAM_BIND_CONTENT, content);
		NetUtil.requestStringData(SRL.Method.METHOD_BIND_COACH, map, new Response.Listener<String>() {

			@Override
			public void onResponse(String result) {
				if(result == null || result.equals("")){
					AppUtil.ShowShortToast(getApplicationContext(), "获取数据异常");
				}else{
					try{
						JSONObject json = new JSONObject(result);
						int status = json.getInt("code") ;
						switch(status){
						case 0: //绑定成功
						 AppUtil.ShowShortToast(BindCoachActivity.this, "绑定教练信息已经发送给教练，请耐心等待教练回复");
						 finish();
						 break ;
						case 2: //已绑定教练
							AppUtil.ShowShortToast(BindCoachActivity.this, "您已经绑定教练");
							break ;
						case 1: //教练不存在
							AppUtil.ShowShortToast(BindCoachActivity.this, "教练不存在");
							break;
						case 3://请求绑定还未失效
						AppUtil.ShowShortToast(BindCoachActivity.this, "请求绑定还未失效，请耐心等待，失效后可重新绑定");
						break;
						default:
							AppUtil.ShowShortToast(BindCoachActivity.this, "绑定失败");
						}
					}catch(Exception e){
						AppUtil.ShowShortToast(BindCoachActivity.this, "绑定失败");
						e.printStackTrace();
					}
				}
			}
			
		}, new DefaultErrorListener(this)) ;
	}
	
}
