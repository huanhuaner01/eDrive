package com.huishen.edrive.util;

import com.huishen.edrive.login.VerifyPhoneActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AppUtil {
    
	public static void intentRegistActivity(Activity activity){
		Intent i = new Intent(activity ,VerifyPhoneActivity.class);
		activity.startActivity(i) ;
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
}
