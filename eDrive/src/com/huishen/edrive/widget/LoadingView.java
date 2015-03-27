package com.huishen.edrive.widget;


import com.huishen.edrive.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 加载页面组件
 * @author zhanghuan
 *
 */
public class LoadingView extends LinearLayout {
    private LinearLayout loading ,loadingFail ;
    private TextView failTv;
    private Button reloading ;
    private int width ,height ;
    private ProgressBar pro;
    
	public LoadingView(Context context) {
		super(context);
		init(context);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context){
		   LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   inflater.inflate(R.layout.layout_loading, this);
		   loading = (LinearLayout)findViewById(R.id.ly_loading_nor);
		   loadingFail = (LinearLayout)findViewById(R.id.ly_loading_fail);
		   failTv = (TextView)findViewById(R.id.ly_loading_tv_fail);
		   reloading = (Button)findViewById(R.id.ly_loading_reBtn);
		   pro = (ProgressBar)findViewById(R.id.ly_loading_nor_pb);
		   loadingFail.setVisibility(View.GONE);
		   pro.setLayoutParams(getImageScaleParams(context ,R.drawable.loading_f1));
		
	}
	
	/**
	 * ���õ�����԰�ť�ļ���
	 * @param listener
	 */
	public void setReLoadingListener(OnClickListener listener){
		if(listener != null){
			reloading.setOnClickListener(listener);
		}
	}
	
	/**
	 * ��ʾ����ҳ��
	 */
	public void showLoading(){
		loading.setVisibility(View.VISIBLE);
		loadingFail.setVisibility(View.GONE);
	}
	
	/**
	 * ��ʾ����ʧ��ҳ��
	 */
	public void showFailLoadidng(){
		loading.setVisibility(View.GONE);
		loadingFail.setVisibility(View.VISIBLE);
	}
	
	/**
	 * ��ʾ����ʧ��ҳ��
	 */
	public void showFailLoadidng(String failReason){
		failTv.setText(failReason);
		loading.setVisibility(View.GONE);
		loadingFail.setVisibility(View.VISIBLE);
	}
	/**
	 * ��ȡͼƬ���ź�Ĵ�С����Ϊ����ڵĿ�
	 * @param context ����baseActivity
	 * @param id ͼƬid (ͼƬ����Դ�ļ���)
	 * @return LinearLayout.LayoutParams
	 */
	public LinearLayout.LayoutParams getImageScaleParams(Context context ,int id){
		if(context == null || id == 0){
			return null ;
		}
		LinearLayout.LayoutParams r_params = null ;
		try{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 1;
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id,options);/* ���ﷵ�ص�bmp��null */
           int width=options.outWidth;
           int height=options.outHeight;
           Log.i("ImageSize", width+" "+height);
        r_params = new LinearLayout.LayoutParams(
        		width,height);
		}catch(Exception e){
			e.printStackTrace() ;
			return null ;
		}
		return r_params;	
	}
}
