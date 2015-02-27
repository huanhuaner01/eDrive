package com.huishen.edrive.util;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * 为Preferences相关的操作提供快捷方式，对客户端代码隐藏使用的Preferences文件名和打开方式。
 * @author Muyangmin
 * @create 2015-2-7
 */
public final class Prefs {
	public static String USER_DATE = "user" ;
	/**
	 * 设置key对应的Preferences值。
	 */
	public static final void setBoolean(Context context, String key,
			boolean value) {
		context.getSharedPreferences(Const.PREFS_APP, Context.MODE_PRIVATE)
				.edit().putBoolean(key, value).commit();
	}

	/**
	 * 获取key对应的Preferences值，默认返回false。
	 */
	public static final boolean getBoolean(Context context, String key) {
		return context.getSharedPreferences(Const.PREFS_APP,
				Context.MODE_PRIVATE).getBoolean(key, false);
	}
	/**
	 * 获取key对应的Preferences值，默认返回 defaultValue。
	 */
	public static final boolean getBoolean(Context context, String key, boolean defautValue) {
		return context.getSharedPreferences(Const.PREFS_APP,
				Context.MODE_PRIVATE).getBoolean(key, defautValue);
	}
	
	
	/**
	 * 保存用户数据
	 * @param context 
	 * @param key 关键字
	 * @param value 值
	 */
	public static void writeString(Context context ,String key ,String value){
		context.getSharedPreferences(Const.PREFS_APP,
			     Context.MODE_PRIVATE).edit().putString(key,value).commit();
	}
	
	/**
	 * 获取关键字相关的数据
	 * @param context
	 * @param key
	 * @return 数据
	 */
	public static String readString(Context context ,String key){
		String data = context.getSharedPreferences(Const.PREFS_APP,
				Context.MODE_PRIVATE).getString(key,"");
		return data ;
	}
	/**
	 * 检查用户是否存在
	 * @param context
	 * @return
	 */
	public static boolean checkUser(Context context){
		if(readString(context ,Const.USER_PHONE).equals("")){
			return false ;
		}
		return true ;
	}
	

}
