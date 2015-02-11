package com.huishen.edrive.util;

import com.huishen.edrive.login.VerifyPhoneActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class AppUtil {
    
	public static void intentRegistActivity(Activity activity){
		Intent i = new Intent(activity ,VerifyPhoneActivity.class);
		activity.startActivity(i) ;
	}
}
