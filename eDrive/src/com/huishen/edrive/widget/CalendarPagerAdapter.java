package com.huishen.edrive.widget;


import java.util.Date;

import com.huishen.edrive.ApointmentFragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * 日历fragment适配器
 * @author zhanghuan
 *
 */
public class CalendarPagerAdapter extends FragmentStatePagerAdapter {
    private String TAG = "CalendarPagerAdapter" ;
    private String beginDate , endDate ; //日历控件的可选区间 
    private int[] colors = null ;
    private boolean isSection  = false ;
    private CalendarResult fragment ;
	public CalendarPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	/**
	 * 设置可选区间
	 */
    public void setSection(CalendarResult fragment,String beginDate ,String endDate ,int[] colors){
    	this.beginDate = beginDate ;
    	this.endDate = endDate ;
    	this.isSection = true ;
    	this.colors = colors ;
    	this.fragment = fragment ;
    } 
    
	@Override
	public Fragment getItem(int position) {
		CalendarPagerFragment fragment = null ;
		if(this.isSection){
			fragment = (new CalendarPagerFragment(this.fragment,position ,this.beginDate ,this.endDate,this.colors)) ;
			return fragment ;
		}
		fragment = new CalendarPagerFragment(this.fragment,position) ;
		return fragment ;
	}
	
	@Override
	public int getCount() {
//		Log.i(TAG, CalendarUtil.getAllMonth()+"") ;
		return CalendarUtil.getAllMonth();
//		return 10;
	}

}
