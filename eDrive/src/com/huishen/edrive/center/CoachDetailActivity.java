package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
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
    
    //初始化相关
    private int coachId = -1  ;
    public static String COACH_ID = "id" ;  //传进来的参数key
    //展示训练场图片的弹出框相关
    private CoachFieldImgDialog imgDialog ;
    private ArrayList<String> imgUrls ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach_detail);
		/****************获取传进来的数据***************/
		coachId = this.getIntent().getIntExtra(COACH_ID, -1) ;
		/****************获取传进来的数据结束***************/		
		registView(); //注册组件
		initView();  //初始化组件
		getNetData(); 
	}
	
	/**
	 * 访问网络，获取网络数据
	 * 
	 */
	private void getNetData() {
		if(coachId == -1){
			AppUtil.ShowShortToast(this,"获取数据异常") ;
			this.finish() ;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(COACH_ID, coachId+"");
//		map.put(SRL.Param.PARAM_LATITUDE, lat+"");
		NetUtil.requestStringData(SRL.Method.METHOD_GET_COACH_DETAIL, map, new Response.Listener<String>() {

			@Override
			public void onResponse(String result) {
				AppUtil.ShowShortToast(getApplicationContext(), result);
			}
			
		}, new DefaultErrorListener()) ;
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
			i.putExtra(CoachDetailActivity.COACH_ID, coachId) ;
			this.startActivity(i);
			break ;
		case R.id.coach_detail_fieldimg:
			actionFieldImg();
			break;
		case R.id.coach_detail_lay:
			if(this.detailContent.getVisibility() == View.GONE){
			this.detailContent.setVisibility(View.VISIBLE);
			}else{
				this.detailContent.setVisibility(View.GONE) ;
			}
			break ;	
		case R.id.coach_detail_judge:
			actionJudgeLay() ;
			break;
		case R.id.coach_detail_setmeal:
			actionMealLay() ;
			break;
		case R.id.coach_detail_btn_call:
			Uri uri = Uri.parse("tel:18200390901");
			Intent it = new Intent(Intent.ACTION_CALL, uri);
			startActivity(it);
			break ;
		}
	}
	
	/**
	 * 响应学员评价按钮，跳转到学员评价列表
	 */
	private void actionJudgeLay(){
		Intent i = new Intent(this ,CoachListActivity.class);
		i.putExtra(CoachListActivity.STATUS_KEY, CoachListActivity.STATUS_JUDGE) ;
		i.putExtra(CoachListActivity.ID_KEY, coachId) ;
		this.startActivity(i);
	}
	
	/**
	 * 响应学车套餐按钮，跳转到学员评价列表
	 */
	private void actionMealLay(){
		Intent i = new Intent(this ,CoachListActivity.class);
		i.putExtra(CoachListActivity.STATUS_KEY, CoachListActivity.STATUS_SETMEAL) ;
		i.putExtra(CoachListActivity.ID_KEY, coachId) ;
		this.startActivity(i);
	}
	
	/**
	 * 弹出训练场图片展示框
	 */
	private void actionFieldImg(){
		if(imgUrls == null){
			imgUrls = new ArrayList<String>();
			imgUrls.add("http://i3.dpfile.com/2007-01-25/99540_b.jpg%28700x700%29/thumb.jpg");
			imgUrls.add("http://www.jxcpw.com/photo/20128617552589614.jpg") ;
		}
		if(imgDialog == null){
			imgDialog = new CoachFieldImgDialog(this,imgUrls);
		}
		imgDialog.show() ;
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = imgDialog.getWindow().getAttributes();
		lp.width = (int)(display.getWidth()); //设置宽度
		imgDialog.getWindow().setAttributes(lp);
	
	}
	
	/**
	 * 点击赞响应事件
	 */
	private void actionGood() {
		
	}	
	
}
