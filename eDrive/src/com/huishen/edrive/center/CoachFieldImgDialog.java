package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.huishen.edrive.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 展示训练场图片的弹出框
 * @author zhanghuan
 *
 */
public class CoachFieldImgDialog extends Dialog implements View.OnClickListener{
    
	private ViewPager viewpager ; //播放图片
	private Button backActivity ; //返回
	private TextView num ; //目前所在图片页数标注
	private ArrayList<View> list ; //图片列表
	private ArrayList<String> imgUrls ; //图片地址
	private ImageLoader imageLoader ;
	private ImgViewPagerAdapter adapter ;
	private Context context ;
	
	
	public CoachFieldImgDialog(Context context , ArrayList<String> imgUrls) {
		super(context,R.style.dataselectstyle);
		this.context = context ;
		this.imgUrls = imgUrls ; 
		imageLoader = com.huishen.edrive.util.AppController.getInstance().getImageLoader();
	}

	public CoachFieldImgDialog(Context context, int theme ,ArrayList<String> imgUrls) {
		super(context, theme);
		this.context = context ;
		this.imgUrls = imgUrls ;
		imageLoader = com.huishen.edrive.util.AppController.getInstance().getImageLoader();
	}

	public CoachFieldImgDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener,ArrayList<String> imgUrls ) {
		super(context, cancelable, cancelListener);
		this.context = context ;
		this.imgUrls = imgUrls ;
		imageLoader = com.huishen.edrive.util.AppController.getInstance().getImageLoader();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_coachfield_img);
        registView();
        init() ;
    }

	private void registView() {
		viewpager = (ViewPager) findViewById(R.id.dialog_coachfield_viewpager);
		backActivity = (Button) findViewById(R.id.dialog_coachfield_back);
		num = (TextView) findViewById(R.id.dialog_coachfield_num);
	}

	private void init() {
		   
	  
		    list = new ArrayList<View>(); 
		    if(imgUrls != null){
		    	if(imgUrls.size()>0){
		    		num.setText("1/"+imgUrls.size());
		    	}
	        for (int i = 0; i < imgUrls.size(); i++) {  
	            View view = LayoutInflater.from(this.context).inflate(  
	                    R.layout.lay_coach_fieldimg_viewpager, null); 
	            NetworkImageView iv = (NetworkImageView) view.findViewById(R.id.list_imageitem_image);  
	            iv.setDefaultImageResId(R.drawable.ic_defualt_image);
	            iv.setErrorImageResId(R.drawable.ic_error_image);
	            iv.setImageUrl(imgUrls.get(i), imageLoader);
	            list.add(view);  
	        }  
		    }
	        adapter = new ImgViewPagerAdapter(list);  
	        viewpager.setAdapter(adapter);  
	        viewpager.setOnPageChangeListener(new OnPageChangeListener(){

				@Override
				public void onPageScrollStateChanged(int arg0) {
					
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					
				}

				@Override
				public void onPageSelected(int position) {
					num.setText((position+1)+"/"+imgUrls.size());
				}
	        	
	        }); 
	        
	        this.backActivity.setOnClickListener(this) ;
	}
	
	/**
	 * 图片展示的adapter
	 * @author zhanghuan
	 *
	 */
	public class ImgViewPagerAdapter extends PagerAdapter {  
	    private List<View> list;  
	  
	    public ImgViewPagerAdapter(List<View> list) {  
	        this.list = list;  
	    }  
	  
	    @Override  
	    public int getCount() {  
	  
	        if (list != null && list.size() > 0) {  
	            return list.size();  
	        } else {  
	            return 0;  
	        }  
	    }  
	  
	    @Override  
	    public boolean isViewFromObject(View arg0, Object arg1) {  
	        return arg0 == arg1;  
	    }  
	  
	    @Override  
	    public void destroyItem(ViewGroup container, int position, Object object) {  
	        container.removeView((View) object);  
	    }  
	  
	    @Override  
	    public Object instantiateItem(ViewGroup container, int position) {  
	        container.addView(list.get(position));  
	        return list.get(position);  
	    }  
	  
	    @Override  
	    public int getItemPosition(Object object) {  
	        return POSITION_NONE;  
	    } 
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.dialog_coachfield_back){
			this.dismiss() ;
		}
	}
}
