package com.huishen.edrive;

import java.util.HashMap;

import com.android.volley.Response;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.huishen.edrive.apointment.ApointmentActivity;
import com.huishen.edrive.center.ListActivity;
import com.huishen.edrive.demand.DemandActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.umeng.UmengServiceProxy;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.CalendarResult;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * 
 * 应用管理类
 * @author zhanghuan
 * 
 */
public class MainActivity extends FragmentActivity implements OnCheckedChangeListener,CalendarResult{
	StatLogger logger = SplashActivity.getLogger();
	private String TAG = "MainActivity" ;
	//UI相关
    private SlidingPaneLayout panelayout ;
    private ImageButton header_menu ;
    private RadioGroup tabGroup ;
    private String coachId = null ,result;
    private LinearLayout prolay ,infolay ,hospitallay ,standardlay;
    private ImageButton msg ; //订单消息
    private ImageView msgTag ; //订单状态
    //初始化数据
    private boolean isFirstMain = true ;
    
    //fragment管理
    private ApointmentFragment apointf ; //约课界面
    private CenterFragment centerf ; //个人中心
    private AppointmentNoCoachFragment noapointf ; //没有教练约课页面
    
    @Override
	protected void onResume() {
		coachId = Prefs.readString(this, Const.USER_COACH_ID);
		Log.i(TAG, "MainActivity OnResume()");
		
		if(Prefs.readString(this, Const.NEW_MSG).equals("1")){
			msgTag.setVisibility(View.VISIBLE);
		}else{
			msgTag.setVisibility(View.GONE);
		}
			Log.i(TAG, "正在刷新");
	    switchFramgnet();
	    
		super.onResume();
		StatService.onResume(this);
	}
    
    @Override
	protected void onPause() {
    	Log.i(TAG, "MainActivity onPause()");
		super.onPause();
		StatService.onPause(this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "MainActivity onDestroy()");
		super.onDestroy();
		android.os.Debug.stopMethodTracing();
	}
	private void checkAppoint(){
    	   FragmentManager fm = this.getSupportFragmentManager();  
           FragmentTransaction tx = fm.beginTransaction(); 
           coachId = Prefs.readString(this, Const.USER_COACH_ID);
       	if(coachId == null || coachId.equals("")){
			AppointmentNoCoachFragment nocoachfragment = new AppointmentNoCoachFragment();
			tx.replace(R.id.main_center,nocoachfragment); 
		}else{
			
			ApointmentFragment apointment = ApointmentFragment.create(result) ;	
			tx.replace(R.id.main_center,apointment); 
		}
        tx.commitAllowingStateLoss();
    }
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //将Activity添加进入栈
        AppController.getInstance().addActivity(this) ;
        AppController.getInstance().addMain(this) ;
        //-------------获取传递过来的数据------------------
          isFirstMain = this.getIntent().getBooleanExtra("main", true);
        //-------------获取传递过来的数据结束！--------------
        registView();
        init() ;
    }
	
	/**
	 * 获取数据
	 */
	private void switchFramgnet(){
			if(tabGroup.getCheckedRadioButtonId()==R.id.main_tab_appointment){
			    tabGroup.check(R.id.main_tab_appointment);
			}
			else if(tabGroup.getCheckedRadioButtonId()!=R.id.main_tab_appointment && tabGroup.getCheckedRadioButtonId()!=R.id.main_tab_center){
				 tabGroup.check(R.id.main_tab_appointment);
			}
	
	}
	
    private void registView(){
    	panelayout = (SlidingPaneLayout)findViewById(R.id.slidepanel);
    	header_menu = (ImageButton)findViewById(R.id.header_menu);
//    	menu1 = (TextView)findViewById(R.id.menu1);
    	tabGroup = (RadioGroup)findViewById(R.id.main_tab_button_group);
    	prolay = (LinearLayout)findViewById(R.id.menu_pro);
    	infolay = (LinearLayout)findViewById(R.id.menu_info);
    	hospitallay = (LinearLayout)findViewById(R.id.menu_hospital);
    	standardlay = (LinearLayout)findViewById(R.id.menu_charge_standard);
		msg = (ImageButton)findViewById(R.id.main_btn_msg);
		msgTag = (ImageView)findViewById(R.id.have_message_tag);
    }
    
    private void init(){
    	//注册广播  
        registerBoradcastReceiver();  
        if(isFirstMain){
        	UmengServiceProxy.startPushService(this);
        }
    	checkCoach();
    	header_menu.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				panelayout.openPane();
			}    		
    	});
    	msg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent in = new Intent(MainActivity.this,ListActivity.class);
				in.putExtra(ListActivity.STATUS_KEY,ListActivity.STATUS_MSGLIST);
				startActivity(in);
			}
    		
    	});
//    	menu1.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//				panelayout.closePane();
//			}
//			
//    	});
    	prolay.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this ,InfoActivity.class);
				i.putExtra("title", "学车流程");
				i.putExtra("url", SRL.Method.METHOD_GET_MENU_PRO);
				startActivity(i);
			}
    		
    	});
    	infolay.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this ,InfoActivity.class);
				i.putExtra("title", "考项说明");
				i.putExtra("url", SRL.Method.METHOD_GET_MENU_INFO);
				startActivity(i);
			}
    		
    	});
    	hospitallay.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this ,InfoActivity.class);
				i.putExtra("title", "体检医院");
				i.putExtra("url",  SRL.Method.METHOD_GET_MENU_HOSPITAL);
				startActivity(i);
			}
    		
    	});
    	standardlay.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this ,InfoActivity.class);
				i.putExtra("title", "收费标准");
				i.putExtra("url",  SRL.Method.METHOD_GET_MENU_STANDARD);
				startActivity(i);
			}
    		
    	});
    	//页面切换
//    	getWebData();
    	tabGroup.setOnCheckedChangeListener(MainActivity.this);
    	
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
        hideFragments(tx);
		switch(checkedId){
		
		case R.id.main_tab_demand:
			 Intent i = new Intent(MainActivity.this ,DemandActivity.class);
			 startActivity(i);
			 group.clearCheck();
			 break ;
		case R.id.main_tab_appointment:
			 coachId = Prefs.readString(this, Const.USER_COACH_ID);
			if(coachId == null || coachId.equals("")){
				if(noapointf == null){
				noapointf = new AppointmentNoCoachFragment();
				tx.add(R.id.main_center,noapointf); 
				}else{
					tx.show(noapointf);
				}
				
			}else{
				if(apointf == null){
				  apointf = ApointmentFragment.create(result) ;	
				  tx.add(R.id.main_center,apointf); 
				}else{
				  tx.show(apointf);	
				}
				
			}
			break ;
		case R.id.main_tab_center:
			if(centerf == null){
			    centerf = new CenterFragment() ;
			    tx.add(R.id.main_center, centerf);  
			}else{
				tx.show(centerf);
			}
	        
			break ;
	
		}
		 tx.commitAllowingStateLoss();
	}
	
	// 当fragment已被实例化，就隐藏起来
	  public void hideFragments(FragmentTransaction ft) {
	    if (noapointf != null)
	      ft.hide(noapointf);
	    if (apointf != null)
	      ft.hide(apointf);
	    if (centerf != null)
	      ft.hide(centerf);
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
	 
	   private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  

			@Override
			public void onReceive(Context arg0, Intent intent) {
				String action = intent.getAction();  
				Log.i(TAG, "来了 action "+action+" msgtype:"+intent.getStringExtra("msg_type"));
	            if(action.equals("com.huishen.edrive.MSG")){   
	                msgTag.setVisibility(View.VISIBLE);
	            } 
	            if(intent.getStringExtra("msg_type").equals("2002")){
	            	switchFramgnet();
	            }
	            if(intent.getStringExtra("msg_type").equals("2003")){
	            	switchFramgnet();
	            }
			}  
	          
	    }; 
	    
	    public void registerBoradcastReceiver(){  
	        IntentFilter myIntentFilter = new IntentFilter();  
	        myIntentFilter.addAction("com.huishen.edrive.MSG");  
	        //注册广播 
	        registerReceiver(mBroadcastReceiver, myIntentFilter);  
	    }

		@Override
		public void setResult(String day) {
			Log.i("apointmentFragment", "获取了时间"+day);
			Intent i = new Intent(this, ApointmentActivity.class);
			i.putExtra("lessonDate", day);
			this.startActivity(i);
		} 
}
