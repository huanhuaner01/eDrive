package com.huishen.edrive.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;

/**
 * 处理Bitmap相关的操作（旋转、缩放，etc.）。
 * 
 * @author Muyangmin
 * @create 2015-2-12
 */
public final class BitmapUtil {
	/**
	 * 计算Bitmap占用的内存大小，以字节为单位。
	 * @param bitmap 目标位图。
	 * @return 返回Bitmap所占空间的大小。
	 */
	@TargetApi(19)
	public static final int getBitmapSize(Bitmap bitmap){
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){     	//API 19
	        return bitmap.getAllocationByteCount();
	    }
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
	        return bitmap.getByteCount();
	    }
	    return bitmap.getRowBytes() * bitmap.getHeight();               //earlier version
	}
	
	/**
	 * 缩放图片到指定尺寸。
	 * @param bitmap 原图
	 * @param width 目标宽度
	 * @param height 目标高度
	 * @return 缩放后的位图
	 */
	public static final Bitmap scaleBitmap(Bitmap bitmap, int width, int height) {
		if (width <=0 || height <=0 ){
			throw new IllegalArgumentException("target size must be positive!");
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}
	
	/**
	 * 缩放图片到指定比例。
	 * @param bitmap 原图
	 * @param scalew 横向缩放比例
	 * @param scaleh 纵向缩放比例
	 * @return 缩放后的位图
	 */
	public static final Bitmap scaleBitmap(Bitmap bitmap, float scalew,
			float scaleh) {
		if (scalew <=0 || scaleh <=0 ){
			throw new IllegalArgumentException("scale rate must be positive!");
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scalew, scaleh);// 利用矩阵进行缩放不会造成内存溢出
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return newbmp;
	}
	/**
	 * 保存Bitmap到文件。
	 * @param path 文件的绝对路径。
	 * @param bitmap 要保存的Bitmap。
	 */
	public static void saveBitmapToFile(String path, Bitmap bitmap) {
		File dir = new File(path).getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File photoFile = new File(path); // 在指定路径下创建文件
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(photoFile);
			if (bitmap != null) {
				if (bitmap.compress(Bitmap.CompressFormat.PNG, 100,
						fileOutputStream)) {
					fileOutputStream.flush();
				}
			}
		} catch (FileNotFoundException e) {
			photoFile.delete();
			e.printStackTrace();
		} catch (IOException e) {
			photoFile.delete();
			e.printStackTrace();
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
