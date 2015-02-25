package com.huishen.edrive.center;

import com.huishen.edrive.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 教练详情
 * @author zhanghuan
 * 
 */
public class CoachDetailActivity extends Activity implements OnClickListener{
	private TextView title , coachname , goodnum ,judgescore ,detailContent;
	private TextView demandnum ,judgenum ,ranking ;
	private RatingBar judgerating ;
    private Button call ; //呼叫教练
    private ImageButton good  , back; //点赞按钮 ,返回按钮
    private LinearLayout field ,fieldimg ,detail , judge ,setmeal ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach_detail);
		registView(); //注册组件
		initView();  //初始化组件
		getNetData(); 
	}
	
	/**
	 * 访问网络，获取网络数据
	 */
	private void getNetData() {
		
	}
	
	private void registView() {
		title = (TextView)this.findViewById(R.id.header_title);
		back = (ImageButton)this.findViewById(R.id.header_back) ;
		call = (Button)this.findViewById(R.id.coach_detail_btn_call) ;
		good = (ImageButton)this.findViewById(R.id.coach_detail_btn_good) ;
		field = (LinearLayout)this.findViewById(R.id.coach_detail_field) ;
		fieldimg = (LinearLayout)this.findViewById(R.id.coach_detail_fieldimg) ;
		detail = (LinearLayout)this.findViewById(R.id.coach_detail_lay) ;
		judge = (LinearLayout)this.findViewById(R.id.coach_detail_judge) ;
		setmeal = (LinearLayout)this.findViewById(R.id.coach_detail_setmeal) ;
		this.detailContent = (TextView)this.findViewById(R.id.coach_detail_tv_content) ;
		
		//教练基本信息
		coachname = (TextView)this.findViewById(R.id.coach_detail_tv_coachname) ;
		goodnum = (TextView)this.findViewById(R.id.coach_detail_tv_good) ;
		judgescore = (TextView)this.findViewById(R.id.coach_detail_judgescore) ;
		judgerating = (RatingBar)this.findViewById(R.id.coach_detail_judge_ratingbar) ;
		demandnum = (TextView)this.findViewById(R.id.coach_detail_demandnum) ;
		judgenum = (TextView)this.findViewById(R.id.coach_detail_judgenum) ;
		ranking = (TextView)this.findViewById(R.id.coach_detail_ranking) ;
	}
	private void initView() {
		this.title.setText(this.getResources().getString(R.string.coach_detail));
		this.back.setOnClickListener(this) ;
		this.good.setOnClickListener(this) ;
		this.field.setOnClickListener(this) ;
		this.fieldimg.setOnClickListener(this) ;
		this.detail.setOnClickListener(this) ;
		this.judge.setOnClickListener(this) ;
		this.setmeal.setOnClickListener(this) ;
		this.call.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.header_back:
			finish();
			break ;
		case R.id.coach_detail_btn_good:
			actionGood();
			break ;
		case R.id.coach_detail_field:
			Intent i = new Intent(this ,CoachTrainFieldActivity.class);
			this.startActivity(i);
			break ;
		case R.id.coach_detail_fieldimg:
			break;
		case R.id.coach_detail_lay:
			if(this.detailContent.getVisibility() == View.GONE){
			this.detailContent.setVisibility(View.VISIBLE);
			}else{
				this.detailContent.setVisibility(View.GONE) ;
			}
			break ;	
		}
	}
	
	/**
	 * 点击赞响应事件
	 */
	private void actionGood() {
		
	}	
	
}
