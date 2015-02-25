package com.huishen.edrive.center;

import com.huishen.edrive.R;
import com.huishen.edrive.util.AppController;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 显示带有标题的列表
 * @author 张欢
 *
 */
public class CoachListActivity extends FragmentActivity {
	//常量
    public static String STATUS_KEY = "status" ;
    public static String ID_KEY = "id" ;
    public static final int STATUS_JUDGE = 0 ;
    public static final int STATUS_SETMEAL= 1 ;
    
    private int status = 0 ;
    private int value = 0 ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach_list);
		AppController.getInstance().addActivity(this) ;
		//获取传递进来的数据
		status = this.getIntent().getIntExtra(STATUS_KEY, 0);
		value = this.getIntent().getIntExtra(ID_KEY, 0);
		
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
			JudgeListFragment fragment = new JudgeListFragment(this ,0) ;
		    
	        tx.add(R.id.container, f_jldetail,"f_jldetail");  
	        
			break ;
		case STATUS_SETMEAL:
			break ;
			
		}
		tx.commit();
		
	}
}
