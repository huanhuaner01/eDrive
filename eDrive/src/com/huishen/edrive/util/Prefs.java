package com.huishen.edrive.util;


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
	 */
	public static void setUser(Context context ,String userData){
		
		 context.getSharedPreferences(Const.PREFS_APP,
	     Context.MODE_PRIVATE).edit().putString(Const.USER_DATE ,userData).commit();;
	}
	
	/**
	 * 检查用户是否存在
	 * @param context
	 * @return
	 */
	public static boolean checkUser(Context context){
//		if(context.getSharedPreferences(Const.PREFS_APP,
//				Context.MODE_PRIVATE).getString(Const.USER_DATE,"").equals("")){
//			return false ;
//		}
	 //TODO 开启测试模式，记得要来改哟
		return true ;
	}
	
	/**
	 * 获得用户数据
	 * @param context
	 * @return
	 */
	public static String getUser(Context context){
		String userData = context.getSharedPreferences(Const.PREFS_APP,
				Context.MODE_PRIVATE).getString(Const.USER_DATE,"") ;	
		return userData ;
	}

}
