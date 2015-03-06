package com.huishen.edrive.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.huishen.edrive.ApointmentFragment;
import com.huishen.edrive.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;

/**
 * 日历控件
 * @author zhanghuan
 *
 */
public class CalendarFragment extends Fragment {
	private String TAG = "CalendarFragment" ;
    private TextView title ;
    private ImageButton premouth ,nextmouth ;
    private ViewPager mPager ;
    private String beginDate , endDate ; //日历控件的可选区间 
    private int[] colors = null ; //可选区的颜色值
    private boolean isSection = false ; //是否是选区状态
    private FragmentActivity activity ;
    private CalendarResult fragment ;
    
	public CalendarFragment(FragmentActivity activity ,CalendarResult fragment,boolean isSection ,String beginDate ,String endDate ,int[] colors) {
		super();
	     this.isSection = isSection ;
	        if(isSection){
	        	this.beginDate = beginDate ;
	        	this.endDate = endDate ;
	        }
	        this.activity = activity ;
	        this.fragment = fragment ;
	        this.colors = colors ;
	}
	public CalendarFragment(FragmentActivity activity ,CalendarResult fragment) {
		super();
	        this.activity = activity ;
	        this.fragment = fragment ;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		View rootView = inflater.inflate(R.layout.calendar, null);
		registView(rootView);
	    init();
		return rootView ;
	}
	
    /**
     * 注册组件
     */
    private void registView(View rootView) {
    	 mPager = (ViewPager)rootView.findViewById(R.id.calendarpager) ;
         title = (TextView)rootView.findViewById(R.id.calendar_tv_mouth) ;
         this.premouth = (ImageButton)rootView.findViewById(R.id.calendar_btn_premouth) ;
         this.nextmouth = (ImageButton)rootView.findViewById(R.id.calendar_btn_nextmouth) ;
	}
    
    /**
     * 初始化事件
     */
	private void init() {
	      CalendarPagerAdapter mPagerAdapter = new CalendarPagerAdapter(this.activity.getSupportFragmentManager());
	        
	        /**************选择区间设置*********************/
	       if(this.isSection){
	        mPagerAdapter.setSection(this.fragment ,beginDate, endDate ,this.colors) ;
	       }
	        /**************选择区间设置结束******************/
	        
			mPager.setAdapter(mPagerAdapter);
			mPager.setOnPageChangeListener(new OnPageChangeListener(){

				@Override
				public void onPageScrollStateChanged(int arg0) {
					
				}
	            
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					
				}

				@Override
				public void onPageSelected(int index) {
					Date date = CalendarUtil.getDate(index) ;
					SimpleDateFormat df = new SimpleDateFormat("yyyy/MM");
					String d = df.format(date) ;
					title.setText(d) ;
				}
				
			});
			setToDay();
            this.premouth.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					if(mPager.getCurrentItem()>0){
						mPager.setCurrentItem(mPager.getCurrentItem()-1);
					}else{
						Toast.makeText(activity,"对不起，已经是第一页了", Toast.LENGTH_SHORT).show() ;
					}
				}
            	
            });
            
            this.nextmouth.setOnClickListener(new OnClickListener(){

  				@Override
  				public void onClick(View arg0) {
  					if(mPager.getCurrentItem()!= mPager.getChildCount()-1){
  						mPager.setCurrentItem(mPager.getCurrentItem()+1);
  					}else{
  						Toast.makeText(activity,"对不起，已经是最后一页了", Toast.LENGTH_SHORT).show() ;
  					}
  				}
              	
              });
	}
	
	/**
	 * 设置今天
	 */
	private void setToDay(){
    	Calendar cal=Calendar.getInstance();
    	int year = cal.get(Calendar.YEAR) ;
    	int mouth = cal.get(Calendar.MONTH) ;
    	int day = cal.get(Calendar.DATE) ;
    	int index = (year - 1970)*12+mouth;
    	
//    	Log.i(TAG, "index :"+index+" 现在时刻是"+year+"年"+mouth+"月"+day+"日"+" cal.getttime():"+cal.getTime().toString()) ;
    	if(this.isSection){
    		int begin  = CalendarUtil.getMouthsForDate(this.beginDate) ;
        	int end = CalendarUtil.getMouthsForDate(endDate) ;
    	if(index >= begin && index <= end){
    	this.mPager.setCurrentItem(index) ;
    	}else{
    		this.mPager.setCurrentItem(begin) ;
    	}
    	}else{
    		this.mPager.setCurrentItem(index) ;
    	}
    }
	
	public void setOnDayClick(){
	    	
	}
}
