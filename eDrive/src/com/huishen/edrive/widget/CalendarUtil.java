package com.huishen.edrive.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

/**
 * 时间管理工具类
 * @author zhanghuan
 *
 */
public class CalendarUtil {
    public static int MAX_YEAR = 2100 ;
    public static int MIN_YEAR = 1970 ;
    public static String DATE_FORMAT = "yyyy-MM-dd" ;
    public static String[] WEEKS = new String[]{"星期天","星期一","星期二","星期三","星期四","星期五","星期六"};
	public CalendarUtil() {
	}
	/**
	 * 获取系统支持的所有月份
	 * @return
	 */
	public static int getAllMonth(){
		int year = MAX_YEAR - MIN_YEAR +1 ;
		
		return year*12 ;
	}
    
	/**
	 * 获取当前时间节点（格式为yyyy-MM-dd HH:mm）
	 * @return
	 */
	public static String getCurrentTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
		return df.format(new Date());// new Date()为获取当前系统时间
	}
	public static Date getDate(int index){
		int year = MIN_YEAR+index/12;
		int month = index%12;
		Calendar date = new GregorianCalendar(year, month,1);
		Date d=date.getTime(); 
		return d;
	}
	
	public static Date getDate(String dateStr){
		if(dateStr == null || dateStr.equals("")){
			return null ;
		}
		SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
		Date date = null;
		try {
			date = sf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	public static Date getDate(int mouths,int day){
		int year = MIN_YEAR+mouths/12;
		int month = mouths%12;
		Calendar date = new GregorianCalendar(year, month,day);
		Date d=date.getTime(); 
		return d;
	}
	
	
	/**
	 * ����·ݵ�����
	 * @param index fragment�����µ��±�
	 * @return
	 */
	public static int getDayNum(int index){
		
		int year = MIN_YEAR+index/12;
		int month = index%12;
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Calendar date = new GregorianCalendar(year, month,1);
		int daynum = date.getActualMaximum(Calendar.DAY_OF_MONTH);
		SimpleDateFormat df1=new SimpleDateFormat("EEEE");
		Date d=date.getTime(); 
		Log.i("Main", df.format(d));
			 int day_of_week = date.get(Calendar.DAY_OF_WEEK); 
			 Date dt = date.getTime() ;
			 Log.i("Main",df.format(dt)+" "+df1.format(dt)+" DAY_OF_MONTH��"+day_of_week+" date.getday:"+dt.getDay());
		return daynum ;
	}
	
	public static int getDayWeek(int index){
		int year = MIN_YEAR+index/12;
		int month = index%12;
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Calendar date = new GregorianCalendar(year, month,1);
		int dayweek = date.get(Calendar.DAY_OF_WEEK);
		return dayweek ;
		
	}
	
	public static String getDayWeek(String dateStr){
		int dayweek = 0 ;
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = df.parse(dateStr);
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			dayweek = cal.get(Calendar.DAY_OF_WEEK);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return WEEKS[dayweek-1] ;
		
	}
	
	/**
	 * 获取当前月份检索
	 * @return
	 */
	public static int getCurrentMouth(){
		Calendar cal=Calendar.getInstance();
    	int year = cal.get(Calendar.YEAR) ;
    	int mouth = cal.get(Calendar.MONTH) ;
    	int day = cal.get(Calendar.DATE) ;
    	int currentIndex = (year - 1970)*12+mouth;
		return currentIndex ;
	} 
	
	/**
	 * 获取当前天数
	 * @return
	 */
	public static int getCurrentDay(){
		Calendar cal=Calendar.getInstance();
		int day = cal.get(Calendar.DATE) ;
		return day ;
	}
	/**
	 * 获取明天的日期
	 * @return时间格式yyyy-MM-dd
	 */
	public static String getTomorrow(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE, 1); 
		Date date = cal.getTime() ;
		SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
		return sf.format(date) ;
	}
	/**
	 * ��ô��������ڵ�MIN_YEAR�м���·�
	 * @param date
	 * @return
	 */
	public static int getMouthsForDate(Date date){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR) ;
		int month = cal.get(Calendar.MONTH) ;
		int mouths = (year-1970)*12+month ;
		return mouths ;
	}
	
	/**
	 * ��ô��������ڵ�MIN_YEAR�м���·�
	 * @param String �ַ������ڸ�ʽ 2015-01-21
	 * @return
	 */
	public static int getMouthsForDate(String dateStr){
		if(dateStr == null || dateStr.equals("") ){
			Log.i("CalendarUtil", "date is null") ;
			return -1 ;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		int mouths = -1 ;
		try {
			date = format.parse(dateStr);
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR) ;
			int month = cal.get(Calendar.MONTH) ;
			mouths = (year-1970)*12+month ;
			Log.i("CalendarUtil", date.toString()) ;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Log.i("CalendarUtil", mouths+"") ;
		return mouths ;
	}
	/**
	 * ��ô��������ڵ���
	 * @param String �ַ������ڸ�ʽ 2015-01-21
	 * @return
	 */
	public static int getDayForDate(String dateStr){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		int day = 0 ;
		try {
			date = format.parse(dateStr);
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			day = cal.get(Calendar.DAY_OF_MONTH) ;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return day ;
	}
	/**
	 * ��ô��������ڵ���
	 * @param Date 
	 * @return
	 */
	public static int getDayForDate(Date date){
		int day = 0 ;
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			day = cal.get(Calendar.DAY_OF_MONTH) ;
			
		return day ;
	}
	
}
