package com.huishen.edrive;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.huishen.edrive.widget.CalendarFragment;
import com.huishen.edrive.widget.CalendarResult;
import com.huishen.edrive.widget.CalendarUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ApointmentFragment extends Fragment implements CalendarResult{
	private FragmentActivity activity ;
	public ApointmentFragment(FragmentActivity activity) {
		this.activity = activity ;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_appointment, null);
		 FragmentManager fm = activity.getSupportFragmentManager();  
	        FragmentTransaction tx = fm.beginTransaction();  
	        String begindate = getToday();
	        String enddate = gettest() ;
	        int[] colors = new int[]{R.drawable.day_bg,R.drawable.day_bg,R.drawable.day_exists ,R.drawable.day_bg,
	        		R.drawable.day_bg,R.drawable.day_full,R.drawable.day_full
	        };
	        		
	        CalendarFragment calendar = new CalendarFragment(this.activity ,(CalendarResult)this ,true,begindate,enddate,colors ) ;	
			tx.replace(R.id.appointment_calendar,calendar);  
	        tx.commit();
		return rootView ;
	}
	/**
	 * 获得+6的日期
	 * @return 格式为yyyy-MM-dd的字符
	 */
	public String gettest(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,6); 
		Date date = cal.getTime() ;
		SimpleDateFormat sf = new SimpleDateFormat(CalendarUtil.DATE_FORMAT);
		return sf.format(date) ;
	}
	/**
	 * 获得今天的日期
	 * @return 格式为yyyy-MM-dd的字符
	 */
	public String getToday(){
		Calendar cal=Calendar.getInstance();
		Date date = cal.getTime() ;
		SimpleDateFormat sf = new SimpleDateFormat(CalendarUtil.DATE_FORMAT);
		return sf.format(date) ;
	}
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void setResult(String day) {
		
	}

}
