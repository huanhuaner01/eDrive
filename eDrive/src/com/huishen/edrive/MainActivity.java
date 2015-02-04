package com.huishen.edrive;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * ึ๗าณ
 * @author zhanghuan
 *
 */
public class MainActivity extends Activity {
    private SlidingPaneLayout panelayout ;
    private ImageButton header_menu ;
    private TextView menu1 ;
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
    }
}
