package com.huishen.edrive.util;

import org.json.JSONObject;

import com.huishen.edrive.login.VerifyPhoneActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AppUtil {
    
	/**
	 * 启动登录注册接口
	 * @param activity
	 */
	public static void intentRegistActivity(Activity activity){
		Intent i = new Intent(activity ,VerifyPhoneActivity.class);
		activity.startActivityForResult(i, VerifyPhoneActivity.LOGIN_CODE); ;
	}
    
	/**
	 * 提示信息（简短的方式）
	 * @param context
	 * @param text
	 */
	public static void ShowShortToast(Context context , String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 提示信息（long的方式）
	 * @param context
	 * @param text
	 */
	public static void ShowLongToast(Context context , String text){
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 保存用户信息
	 * @param context
	 * @param result
	 */
	public static void saveUserInfo(Context context ,String result){
		try { 
			JSONObject json = new JSONObject(result);
			
			
			JSONObject stuInfojson = json.optJSONObject("stuInfo");
			if(stuInfojson == null){
				return ;
			}
			preLoginSave(context ,stuInfojson);
		    
		}catch(Exception e){
			e.printStackTrace() ;
		}
	}
	
	public static void preLoginSave(Context context ,JSONObject stuInfojson){
		Log.i("AppUtil",stuInfojson.toString() );
		try{
		JSONObject baseUserjson = stuInfojson.optJSONObject("baseUser");
		JSONObject userInfojson = stuInfojson.optJSONObject("userInfo");
		JSONObject coachInfojson = stuInfojson.optJSONObject("coachInfo") ;
		if(baseUserjson != null){
			String tel = baseUserjson.optString("phone" ,"");
			String mobleFlag = baseUserjson.getString(Const.USER_MOBILEFLAG) ;
			Log.i("AppUtil", "mobleFlag:"+mobleFlag) ;
			Prefs.writeString(context,Const.USER_MOBILEFLAG, mobleFlag);
			Prefs.writeString(context,Const.USER_PHONE, tel);
			Prefs.writeString(context,Const.USER_BASEUSER, baseUserjson.toString());
		}
		if(userInfojson != null){
			
			String addr = userInfojson.optString("address" ,"");
			String stuname = userInfojson.optString("stuName" ,"");
			int id = userInfojson.getInt(Const.USER_ID) ;
			Prefs.writeString(context,Const.USER_ID, id+"");
			Prefs.writeString(context,Const.USER_ADDR, addr);
			Prefs.writeString(context,Const.USER_NAME, stuname);
			Prefs.writeString(context,Const.USER_USERINFO, userInfojson.toString());
		}
		if(coachInfojson != null){
			String coachId = coachInfojson.optString("coachId" ,"") ;
			Prefs.writeString(context,Const.USER_COACH_ID, coachId);
		}
		}catch(Exception e){
			e.printStackTrace() ;
		}
	}
	
	public static void removeAllData(Context context){
		context.getSharedPreferences(Const.PREFS_APP,
			     Context.MODE_PRIVATE).edit().remove(Const.USER_MOBILEFLAG).commit();
		context.getSharedPreferences(Const.PREFS_APP,
			     Context.MODE_PRIVATE).edit().remove(Const.USER_PHONE).commit();
		context.getSharedPreferences(Const.PREFS_APP,
			     Context.MODE_PRIVATE).edit().remove(Const.USER_COACH_ID).commit();
		context.getSharedPreferences(Const.PREFS_APP,
			     Context.MODE_PRIVATE).edit().remove(Const.USER_USERINFO).commit();
		context.getSharedPreferences(Const.PREFS_APP,
			     Context.MODE_PRIVATE).edit().remove(Const.USER_NAME).commit();
		context.getSharedPreferences(Const.PREFS_APP,
			     Context.MODE_PRIVATE).edit().remove(Const.USER_ADDR).commit();
		context.getSharedPreferences(Const.PREFS_APP,
			     Context.MODE_PRIVATE).edit().remove(Const.USER_BASEUSER).commit();
		context.getSharedPreferences(Const.PREFS_APP,
			     Context.MODE_PRIVATE).edit().remove(Const.USER_ID).commit();
		
	}
	
}
