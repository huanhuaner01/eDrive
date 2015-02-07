package com.huishen.edrive;

import com.huishen.edrive.demand.DemandActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 应用管理类
 * @author zhanghuan
 *
 */
public class MainActivity extends FragmentActivity implements OnCheckedChangeListener{
	
	//UI相关
    private SlidingPaneLayout panelayout ;
    private ImageButton header_menu ;
    private TextView menu1 ;
    private RadioGroup tabGroup ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registView();
        init() ;
    }
    
    private void registView(){
    	panelayout = (SlidingPaneLayout)findViewById(R.id.slidepanel);
    	header_menu = (ImageButton)findViewById(R.id.header_menu);
    	menu1 = (TextView)findViewById(R.id.menu1);
    	tabGroup = (RadioGroup)findViewById(R.id.main_tab_button_group);
    }
    
    private void init(){
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
    }

    //radiogroup监听
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.main_tab_demand:
			 Intent i = new Intent(MainActivity.this ,DemandActivity.class);
			 startActivity(i);
			 group.check(R.id.main_tab_appointment);
			 break ;
		case R.id.main_tab_appointment:
			ApointmentFragment wesu = new ApointmentFragment() ;
		    FragmentManager fm = this.getSupportFragmentManager();  
	        FragmentTransaction tx = fm.beginTransaction();  
	        tx.add(R.id.main_center, wesu);  
	        tx.commit();
			break ;
		case R.id.main_tab_center:
			break ;
		}
	}
}
