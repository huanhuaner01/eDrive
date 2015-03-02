package com.huishen.edrive;

import com.huishen.edrive.demand.DemandActivity;
import com.huishen.edrive.umeng.UmengServiceProxy;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 应用管理类
 * @author zhanghuan
 * 
 */
public class MainActivity extends FragmentActivity implements OnCheckedChangeListener{
	private String TAG = "MainActivity" ;
	//UI相关
    private SlidingPaneLayout panelayout ;
    private ImageButton header_menu ;
    private TextView menu1 ;
    private RadioGroup tabGroup ;
    private String coachId = null;
    
    //初始化数据
    private boolean isFirstMain = true ;
    
    @Override
	protected void onResume() {
		coachId = Prefs.readString(this, Const.USER_COACH_ID);
		if(tabGroup.getCheckedRadioButtonId() == R.id.main_tab_appointment){
			this.tabGroup.check(R.id.main_tab_appointment);
		}
		super.onResume();
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //将Activity添加进入栈
        AppController.getInstance().addActivity(this) ;
        
        //-------------获取传递过来的数据------------------
          isFirstMain = this.getIntent().getBooleanExtra("main", true);
        //-------------获取传递过来的数据结束！--------------
        
        registView();
        init() ;
        UmengServiceProxy.startPushService(this);
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put(SRL.PARAM_USERNAME, "sl");
//        map.put(SRL.PARAM_PASSWORD, "ass");
//        NetUtil.requestStringData(SRL.METHOD_LOGIN, map, new Response.Listener<String>() {
//
//			@Override
//			public void onResponse(String arg0) {
//				ResponseParser.isReturnSuccessCode(arg0);
//			}
//		}, new Response.ErrorListener() {
//
//			@Override
//			public void onErrorResponse(VolleyError arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//        NetUtil.requestUploadFile(new File("as"), SRL.METHOD_UPLOAD_CERTIFICATES,new UploadResponseListener(){
//
//			@Override
//			public void onSuccess(String str) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onError(int httpCode) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onProgressChanged(int hasFinished) {
//				// TODO Auto-generated method stub
//				
//			}});
    }
    
    private void registView(){
    	panelayout = (SlidingPaneLayout)findViewById(R.id.slidepanel);
    	header_menu = (ImageButton)findViewById(R.id.header_menu);
    	menu1 = (TextView)findViewById(R.id.menu1);
    	tabGroup = (RadioGroup)findViewById(R.id.main_tab_button_group);
    }
    
    private void init(){
    	
    
    	checkCoach();
    	header_menu.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				panelayout.openPane();
			}    		
    	});
    	menu1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				panelayout.closePane();
			}
			
    	});
    	
    	//页面切换
    	this.tabGroup.setOnCheckedChangeListener(this);
    	this.tabGroup.check(R.id.main_tab_appointment);
    }

    
    /**
     * 检查教练是否绑定
     */
    private void checkCoach(){
    	//如果用户已经绑定手机，则获取登录数据
		if (Prefs.checkUser(this)) { 
			//获得本地用户数据
				
				coachId =  Prefs.readString(this,Const.USER_COACH_ID) ;
		
		}
		 if(isFirstMain){
		//如果用户教练不存在则跳转到发布需求界面
		if(coachId == null || coachId.equals("")){
			Intent i = new Intent(this , DemandActivity.class);
			this.startActivity(i);
		}
		 }
    }
    //radiogroup监听
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
	    FragmentManager fm = this.getSupportFragmentManager();  
        FragmentTransaction tx = fm.beginTransaction();  
		switch(checkedId){
		
		case R.id.main_tab_demand:
			 Intent i = new Intent(MainActivity.this ,DemandActivity.class);
			 startActivity(i);
			 group.check(R.id.main_tab_appointment);
			 break ;
		case R.id.main_tab_appointment:
			if(coachId == null || coachId.equals("")){
				AppointmentNoCoachFragment nocoachfragment = new AppointmentNoCoachFragment();
				tx.replace(R.id.main_center,nocoachfragment); 
			}else{
				ApointmentFragment apointment = new ApointmentFragment(this) ;	
				tx.replace(R.id.main_center,apointment); 
			}
			break ;
		case R.id.main_tab_center:
			CenterFragment center = new CenterFragment() ;		
	        tx.replace(R.id.main_center, center);  
			break ;
		}
		 tx.commit();
	}
	
	///////////////////////退出系统应用//////////////////////////////////////////
	 private int backindex = 0 ;

	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
             if(backindex == 0){
            	 Toast.makeText(MainActivity.this,"再按一次退出e驾学车!", Toast.LENGTH_SHORT).show();
            	 backindex++;
            	 return false ;
             }
             AppController.getInstance().exit(this.getApplicationContext());
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }
}
