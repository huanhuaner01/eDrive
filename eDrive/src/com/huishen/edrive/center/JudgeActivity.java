package com.huishen.edrive.center;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.MainActivity;
import com.huishen.edrive.R;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class JudgeActivity extends Activity implements OnRatingBarChangeListener{
	private String TAG = "JudgeActivity";
	private TextView title ,note,attitude , field ,tf ; //标题,态度，场地，交通
	private EditText jugdecontent ; //评价内容
	private RatingBar rb_at,rb_fd,rb_tf ;//星条
	private ImageButton back;
	
	private int coachId = -1;
	private int tag = 1; //1评价，追评。
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_judge);
		AppController.getInstance().addActivity(this);
		//-------------------获取资料---------------------
		tag = this.getIntent().getIntExtra("tag",1);
		coachId = this.getIntent().getIntExtra("coachId", -1);
		
		registView();
		initView();
	}
	
	private void registView() {

		rb_at = (RatingBar) findViewById(R.id.jugde_rb_at);
		rb_fd = (RatingBar) findViewById(R.id.jugde_rb_fd);
		rb_tf = (RatingBar) findViewById(R.id.jugde_rb_tf);
		
        title = (TextView) findViewById(R.id.header_title);
        attitude = (TextView) findViewById(R.id.jugde_tv_at);
        field = (TextView) findViewById(R.id.jugde_tv_fd);
        tf = (TextView) findViewById(R.id.jugde_tv_tf);
        note = (TextView)findViewById(R.id.header_note);
        
        back = (ImageButton)findViewById(R.id.header_back);
     
        jugdecontent = (EditText) findViewById(R.id.judge_content);
	}
	private void initView() {
		this.title.setText("评价");
		this.back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
			  finish();
			}
		});
		rb_at.setOnRatingBarChangeListener(this);
		rb_fd.setOnRatingBarChangeListener(this);
		rb_tf.setOnRatingBarChangeListener(this);
		note.setText("确定");
		note.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
			
				if(tag ==2){
					plusJudge();
				}else{
					jugdeCommit();
				}
			}
			
		});
		if(tag == 2){
			LinearLayout  lay = (LinearLayout)findViewById(R.id.judge_rating);
			lay.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
            switch(ratingBar.getId()){
            case R.id.jugde_rb_at:
            	if(rating == 0){
            		attitude.setText("0分");
            	}else{
            		attitude.setText(Html.fromHtml("<font color =\"#ff0000\">"+rating+"分</font>"));
            	}
            	break;
            case R.id.jugde_rb_fd:
            if(rating == 0){
            	field.setText("0分");
        	}else{
        		field.setText(Html.fromHtml("<font color =\"#ff0000\">"+rating+"分</font>"));
        	}
            	break;
            case R.id.jugde_rb_tf:
            	 if(rating == 0){
            		 tf.setText("0分");
             	}else{
             		tf.setText(Html.fromHtml("<font color =\"#ff0000\">"+rating+"分</font>"));
             	}
            	break;
            };
	}
	
	/**
	 * 提交按钮事件响应
	 */
	public void plusJudge(){
		if(jugdecontent.getText().toString().equals("")){
			Toast.makeText(this, "请填写评价内容", Toast.LENGTH_SHORT).show();
			return;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId",coachId+"");
		map.put("commentId", this.getIntent().getIntExtra("commentId", -1)+"");
		map.put("content", jugdecontent.getText().toString());
		map.put("serviceScore", rb_at.getRating()+"");
		map.put("qualityScore", rb_fd.getRating()+"");
		map.put("ruleScore", rb_tf.getRating()+"");
		NetUtil.requestStringData(SRL.Method.METHOD_PLUS_JUDGE, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if(result == null || result.equals("")){
							AppUtil.ShowShortToast(JudgeActivity.this, "服务器繁忙");
						}else{
							try{
								JSONObject json = new JSONObject(result);
								if(json.getInt("status")==1){
									AppUtil.ShowShortToast(JudgeActivity.this, "评价成功");
									finish();
								}
									
							}catch(Exception e){
								
							}
						}
					}
	},new DefaultErrorListener(this));
}
	
	/**
	 * 提交按钮事件响应
	 */
	public void jugdeCommit(){

		if(rb_at.getRating() == 0){
			Toast.makeText(this, "请对态度评分", Toast.LENGTH_SHORT).show();
			return;
		}
		if(rb_fd.getRating() == 0){
			Toast.makeText(this, "请对场地评分", Toast.LENGTH_SHORT).show();
			return;
		}
		if(rb_tf.getRating() == 0){
			Toast.makeText(this, "请对交通评分", Toast.LENGTH_SHORT).show();
			return;
		}
		if(jugdecontent.getText().toString().equals("")){
			Toast.makeText(this, "请填写评价内容", Toast.LENGTH_SHORT).show();
			return;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId",coachId+"");
		map.put("content", jugdecontent.getText().toString());
		map.put("serviceScore", rb_at.getRating()+"");
		map.put("qualityScore", rb_fd.getRating()+"");
		map.put("ruleScore", rb_tf.getRating()+"");
		NetUtil.requestStringData(SRL.Method.METHOD_JUDGE, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if(result == null || result.equals("")){
							AppUtil.ShowShortToast(JudgeActivity.this, "服务器繁忙");
						}else{
							try{
								JSONObject json = new JSONObject(result);
								if(json.getInt("status")==1){
									AppUtil.ShowShortToast(JudgeActivity.this, "评价成功");
									finish();
								}
									
							}catch(Exception e){
								
							}
						}
					}
	},new DefaultErrorListener(this));
}
}
