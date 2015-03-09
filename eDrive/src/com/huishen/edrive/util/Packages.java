package com.huishen.edrive.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 封装Android应用（Package）相关的方法。
 * 
 * @author Muyangmin
 * @create 2015-2-20
 */
public final class Packages {
	/**
	 * 获得当前应用的版本名称。
	 * 
	 * @return 返回应用版本号；如果未获取到，返回null。
	 */
	public static final String getVersionName(Context context) {
		return getVersionName(context, context.getPackageName());
	}

	/**
	 * 获得指定应用的版本名称。
	 * 
	 * @param pkgname
	 *            应用包名。
	 * @return 返回应用版本名称；如果未获取到，返回null。
	 */
	public static final String getVersionName(Context context, String pkgname) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(pkgname, 0);
			String version = info.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获得当前应用的版本号。
	 * 
	 * @return 返回应用版本号。
	 */
	public static final int getVersioCode(Context context) {
		return getVersionCode(context, context.getPackageName());
	}

	/**
	 * 获得指定应用的版本号。
	 * 
	 * @param pkgname
	 *            应用包名。
	 * @return 返回应用版本号；如果未获取到，返回-1。
	 */
	public static final int getVersionCode(Context context, String pkgname) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(pkgname, 0);
			int version = info.versionCode;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
