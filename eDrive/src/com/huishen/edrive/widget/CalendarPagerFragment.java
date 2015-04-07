package com.huishen.edrive.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.huishen.edrive.ApointmentFragment;
import com.huishen.edrive.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class CalendarPagerFragment extends Fragment {

	public static final String INDEX = "index", DATE_BEGIN = "begin",
			DATE_END = "end" ,COLORS="colors";
	private String TAG = "CalendarPagerFragment";
	private int mMonthIndex;
	private CalendarGridViewAdapter adapter;
	private Date beginDate, endDate; // 日历控件的可选区间
	private int[] colors ; //可选区域的颜色值
	private boolean isSection = false;
    private CalendarResult listener ;
	
	public static CalendarPagerFragment create(int monthIndex) {
		CalendarPagerFragment fragment = new CalendarPagerFragment();
		Bundle args = new Bundle();
		args.putInt(INDEX, monthIndex);
		fragment.setArguments(args);
		return fragment;
	}

	public static CalendarPagerFragment create(int monthIndex,
			String beginDate, String endDate ,int[] colors) {
		CalendarPagerFragment fragment = new CalendarPagerFragment();
		Bundle args = new Bundle();
		args.putInt(INDEX, monthIndex);
		args.putString(DATE_BEGIN, beginDate);
		args.putString(DATE_END, endDate);
		args.putIntArray(COLORS, colors) ;
		fragment.setArguments(args);
		return fragment;
	}

//	public CalendarPagerFragment(CalendarResult fragment, int position,
//			String beginDate, String endDate, int[] colors) {
//		mMonthIndex = position;
//		this.listener = fragment ;
//		if (beginDate == null
//				&& endDate == null) {
//			return;
//
//		}
//		setSection(beginDate, endDate ,colors);
//	}
//	public CalendarPagerFragment() {
//		
//	}
//	public CalendarPagerFragment(CalendarResult fragment, int position) {
//		this.listener = fragment ;
//		mMonthIndex = position;
//		
//	}

	/**
	 * 设置可选区间
	 */
	public void setSection(String beginDate, String endDate,int[] colors) {

		this.beginDate = CalendarUtil.getDate(beginDate);
		this.endDate = CalendarUtil.getDate(endDate);
		if (this.beginDate == null) {
			this.beginDate = new GregorianCalendar(CalendarUtil.MIN_YEAR, 0, 1)
					.getTime();
		}
		if (this.endDate == null) {
			this.endDate = new GregorianCalendar(CalendarUtil.MAX_YEAR, 11, 31)
					.getTime();
		}
//		Log.i(TAG, "endDate is " + this.endDate);
		this.isSection = true;
		this.colors = colors ;
		
	}

	/**
	 * 判断日期是否在可选区域
	 */
	private boolean dayisSection(Date date) {
//		Log.i(TAG, "begindate is :" + beginDate.toString());
		int i = date.compareTo(beginDate);
		int j = date.compareTo(endDate);
		if (i >= 0 && j <= 0) {
			return true;
		}
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMonthIndex = getArguments().getInt(INDEX);
		if (getArguments().getString(DATE_BEGIN) == null
				&& getArguments().getString(DATE_END) == null) {
			return;
		}
		
		setSection(getArguments().getString(DATE_BEGIN), getArguments()
				.getString(DATE_END) ,getArguments().getIntArray(COLORS));
	}
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.fragment_calendar, container, false);
		GridView grid = (GridView) view.findViewById(R.id.gridview);
//		Log.i(TAG, "set calendar gridView");
		adapter = new CalendarGridViewAdapter(container.getContext(),
				getDays(), R.layout.calendar_day, new String[] { "day" },
				new int[] { R.id.day });
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

				HashMap<String, Object> map = (HashMap<String, Object>) view
						.getTag();
//				Log.i(TAG, map.toString());
				if (Integer.parseInt(map.get("status").toString()) < 0) {
					return;
				}
				adapter.selectOption(position);
				Date date = CalendarUtil.getDate(mMonthIndex);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-");
				String d = df.format(date);
				listener.setResult(d+map.get("day")) ;
//				Intent intent = new Intent(); 
//				intent.putExtra(CalendarActivity.RESULT_DATA, d + map.get("day"));
//				CalendarPagerFragment.this.getActivity().setResult(CalendarPagerFragment.this.getActivity().RESULT_OK, intent); 
//				CalendarPagerFragment.this.getActivity().finish();
			}

		});
		return view;

	}
    
	/**
	 * 获取父activity,并且转换为CalendarResult
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		      try{  
		    	  listener =(CalendarResult)activity;  
		      }catch(ClassCastException e){  
		          throw new ClassCastException(activity.toString()+"must implement CalendarResult");  
		      } 
	}
	private List<HashMap<String, Object>> getDays() {
		int daynum = CalendarUtil.getDayNum(mMonthIndex);
		int dayweek = CalendarUtil.getDayWeek(mMonthIndex);
		int temp = 0 ;
		Date today = CalendarUtil.getDate(CalendarUtil.getCurrentMouth(),
				CalendarUtil.getCurrentDay());
		List<HashMap<String, Object>> days = new ArrayList<HashMap<String, Object>>();
		for (int i = 1; i < dayweek; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("day", "");
			map.put("status", -1);
			days.add(map);
		}
		for (int i = 1; i <= daynum; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("day", i);
			if (this.isSection) { // 区间选择
				    map.put("status", -1);
         /*****************************第一种判断方法************************************************/
				    Date newDay = CalendarUtil.getDate(this.mMonthIndex, i);
				    if(dayisSection(newDay)){
				    	map.put("status", 0);
				    	map.put("color", colors[temp]) ;
				    	
				    	temp++ ;
				    	if(this.mMonthIndex == CalendarUtil.getCurrentMouth()&&dayisSection(today)){
				    			if(i == CalendarUtil.getCurrentDay()){
				    				map.put("status", 2);
				    				}
				    	}else{
				    		if(this.mMonthIndex == CalendarUtil.getMouthsForDate(this.beginDate)){
			    				if(i == CalendarUtil.getDayForDate(this.beginDate)){
			    					map.put("status", 1);
			    				}
			    			}else{
			    				if(i==1){
			    					map.put("status", 1);
			    				}
			    			}
				    	}
				    }else{
						if (this.mMonthIndex == CalendarUtil.getCurrentMouth()
						&& i == CalendarUtil.getCurrentDay()) { // 判断是否是今天
							map.put("status", -2);
						}
				    }
/*****************************第一种判断方法结束************************************************/	
				    
/*****************************第二种判断方法***************************************************/				    
//					if (this.mMonthIndex == CalendarUtil.getCurrentMouth()
//							&& i == CalendarUtil.getCurrentDay()) { // 判断是否是今天
//						if (dayisSection(today)) {
//							// 如果年月日都和今天日期相同，设置为今天状态
//							map.put("status", 2);
//						} else {
//							// 如果年月日都和今天日期相同，设置为今天不可选状态
//							map.put("status", -2);
//						}
//					} else { //不是今日
//						//判断是否在选区中
//						Date newDay = CalendarUtil.getDate(this.mMonthIndex, i);
//						if(dayisSection(newDay)){
//							map.put("status", 0);
//							if(CalendarUtil.getMouthsForDate(this.beginDate) == CalendarUtil.getCurrentMouth()){
//								if(!dayisSection(today) && i == CalendarUtil.getDayForDate(this.beginDate) 
//										&& this.mMonthIndex ==CalendarUtil.getMouthsForDate(this.beginDate)  ){
//									map.put("status", 1);
//								}
//							}else{
//								if(this.mMonthIndex == CalendarUtil.getMouthsForDate(this.beginDate)){
//									if(i == CalendarUtil.getDayForDate(this.beginDate)){
//										map.put("status", 1);
//									}
//								}else{
//									if(this.mMonthIndex !=CalendarUtil.getCurrentMouth() && i == 1){
//										map.put("status", 1);
//									}
//								}
//							}
//						}
//						}
/*****************************第二种判断方法结束***************************************************/					    
			} else {
				map.put("status", 0);
				if (CalendarUtil.getCurrentDay() > daynum) { // 今天日期大于目前月份的天数
					// 如果是第一天，设置为选中状态
					if (i == 1)
						map.put("status", 1);

				} else { // 今天的日期天不大于本月的天数

					if (this.mMonthIndex == CalendarUtil.getCurrentMouth()
							&& i == CalendarUtil.getCurrentDay()) { // 如果年月日都和今天日期相同，设置为今天状态
						map.put("status", 2);
					} else if (i == CalendarUtil.getCurrentDay()) { // 只有日期和今天日期相同，设置为选中状态
						map.put("status", 1);
					}
				}
			}
//			Log.i(TAG, map.toString());
			days.add(map);
		}
		return days;
	}
}
