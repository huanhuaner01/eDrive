package com.huishen.edrive.center;

import java.util.Set;

import com.huishen.edrive.R;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * 显示带有标题的列表
 * @author 张欢
 *
 */
public class ListActivity extends FragmentActivity {
	//常量
    public static String STATUS_KEY = "status" ;
    public static String ID_KEY = "id" ;
    public static final int STATUS_JUDGE = 0 ;
    public static final int STATUS_SETMEAL= 1 ;
    public static final int STATUS_MSGLIST = 2; //消息列表
    public static final int STATUS_ORDERLIST = 3; //订单列表
    
    private int status = 0 ;
    private int value = 0 ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);
		AppController.getInstance().addActivity(this) ;
		//获取传递进来的数据
		status = this.getIntent().getIntExtra(STATUS_KEY, 2);
		value = this.getIntent().getIntExtra(ID_KEY, 0);
//		Bundle bun = getIntent().getExtras();
//		if (bun != null)
//		{
//		    Set<String> keySet = bun.keySet();
//		    for (String key : keySet) {
//		        String v = bun.getString(key);
//		        Log.i("listActivity","key:"+key+" value:"+v );
//		    }
//		}
		switchFragment();
	}
	
	/**
	 * 切换布局
	 */
	private void switchFragment(){
		 FragmentManager fm = this.getSupportFragmentManager();  
	        FragmentTransaction tx = fm.beginTransaction(); 
		switch(status){
		case STATUS_JUDGE:
			String name = this.getIntent().getStringExtra("coachName");
			float score = this.getIntent().getFloatExtra("score", 0);
			CoachJudgeListFragment fragment = new CoachJudgeListFragment(this ,"学员评价","",value ,name ,score) ;
	        tx.add(R.id.container, fragment,"f_jljudge");         
			break ;
		case STATUS_SETMEAL:
			CoachMealListFragment sfragment = new CoachMealListFragment(this ,"学车套餐",SRL.Method.METHOD_GET_COACH_MEAL ,value) ;
	        tx.add(R.id.container, sfragment,"f_jlsetmeal");  
			break ;
		case STATUS_MSGLIST:
			MsgFragment msgfragment = new MsgFragment(this ,"消息中心",SRL.Method.METHOD_GET_COACH_MEAL) ;
	        tx.add(R.id.container, msgfragment,"f_jlsetmeal");  
			break ;
		case STATUS_ORDERLIST:
			OrderListFragment orderfragment = new OrderListFragment(this ,"需求订单","") ;
	        tx.add(R.id.container, orderfragment,"order");  
			break ;
		}
		tx.commit();
		
	}
}
