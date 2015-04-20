package com.huishen.edrive.widget;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 用户指导组件
 * @author zhanghuan
 *
 */
public class GuideView extends View {
    private int xPoint  ,yPoint;//绘制的起点
    private Paint paint ; //绘制空心图形所用画笔
    private int position ; //图片位置
    private int width ,height ; //宽和高
    private int padding =0; //调节
    private int shape ; //透明区域的形状
    private int imgId ; //指导图片id
    private String bgcolor="#99000000" ;
    private Map<String ,Integer> params ; //各种属性
    
    public static final int TOP = 0 ,BUTTOM = 1,LEFT = 2 ,RIGHT =3 ,TOP_LEFT=4 ,BUTTOM_LEFT = 5 ,TOP_RIGHT = 6 ,BUTTOM_RIGHT = 7 ; //图片方位
    public static final int OVER = 0,CRICLE = 1 ,RECT = 2 ; //椭圆，圆，矩形
    
	public GuideView(Context context ,Map<String ,Integer> params) {
		super(context);
		this.params = params ;
		init();
	}

	public GuideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	/**
	 * 初始化画笔
	 */
    private void init(){
    	   paint=new Paint();
           paint.setAlpha(0);
           paint.setStyle(Style.FILL_AND_STROKE);
           paint.setAntiAlias(true);
           paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
    	
    }
    /**
     * 设置值
     */
    public void setAttr(Map<String ,Integer> params){
    	this.params = params ;
    	Log.i("GuideView", params.toString());
    	invalidate();
    }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(params!= null){
			xPoint = params.get("xPoint");
			yPoint = params.get("yPoint");
			position = params.get("position") ;
			width = params.get("width") ;
			height = params.get("height");
			shape = params.get("shape");
			if(params.get("img") !=null && params.get("img")!= 0){
				imgId = params.get("img");
			}
			if(params.get("padding") !=null&&params.get("padding")!=0){
				padding = params.get("padding") ;
			}
			switch(shape){
			 case CRICLE:drawalphaCricle(canvas) ;break;
			 case RECT:drawalphaRect(canvas);break ;
			 case OVER:drawalphaOver(canvas);break ;
			}
		drawImg(canvas);
		}
	}

	 
	/**
	 * 绘制透明圆形
	 * @param canvas
	 */
	private void drawalphaCricle(Canvas canvas){   
        Bitmap bitmap=Bitmap.createBitmap(this.getWidth(), this.getHeight(), Config.ARGB_8888);
        Canvas tempcCanvas=new Canvas(bitmap);
//      drawImg(tempcCanvas);
        tempcCanvas.drawColor(Color.parseColor(bgcolor));
        int radius = (width>height)?(width/2+padding):(height/2+padding) ;
       
        tempcCanvas.drawCircle(xPoint+radius-padding, yPoint+height/2, radius+padding, paint);
        //由于是圆所以重置原点坐标和高（以便后面画指导图片）
        this.yPoint = yPoint-(radius-padding-height/2);
        this.height = (radius-padding)*2 ;
        Log.i("GuideView", "xPoint:"+xPoint+" yPoint:"+yPoint);
        canvas.drawBitmap(bitmap, 0, 0, null);
	}
	
	/**
	 * 绘制透明圆角矩形
	 * @param canvas
	 */
	private void drawalphaRect(Canvas canvas){
	        Bitmap bitmap=Bitmap.createBitmap(this.getWidth(), this.getHeight(), Config.ARGB_8888);
	        Canvas tempcCanvas=new Canvas(bitmap);
//	        drawImg(tempcCanvas);
	     // 新建一个矩形
	        RectF outerRect = new RectF(xPoint-padding,yPoint-padding, xPoint+width+padding, yPoint+height+padding);
	       
	        tempcCanvas.drawColor(Color.parseColor(bgcolor));
	        tempcCanvas.drawRoundRect(outerRect, 20, 20, paint); //圆角的幅度是20
	        canvas.drawBitmap(bitmap, 0, 0, null);
	}
	
	
	/**
	 * 绘制透明椭圆
	 * @param canvas
	 */
	private void drawalphaOver(Canvas canvas){
	        Bitmap bitmap=Bitmap.createBitmap(this.getWidth(), this.getHeight(), Config.ARGB_8888);
	        Canvas tempcCanvas=new Canvas(bitmap);
//	        drawImg(tempcCanvas);
	     // 新建一个矩形
	        RectF oval2 = new RectF(xPoint-padding, yPoint-padding,xPoint+width+padding, yPoint+height+padding);// 设置个新的长方形，扫描测量  
//	        canvas.drawArc(oval2, 200, 130, true, p);  
	        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线  
	        //画椭圆，把oval改一下  
//	        oval2.set(210,100,250,130);  
//	        canvas.drawOval(oval2, p); 
	        tempcCanvas.drawColor(Color.parseColor(bgcolor));
	        tempcCanvas.drawOval(oval2, paint);//画椭圆，参数一是扫描区域，参数二为paint对象；
//	        tempcCanvas.drawCircle(250, 250, 150, paint);
	        canvas.drawBitmap(bitmap, 0, 0, null);
	}
	
	
	/**
	 * 绘制图片
	 * @param canvas
	 */
	private void drawImg(Canvas canvas){
		Paint paint=new Paint();
		paint.setAlpha(255);
		paint.setStyle(Style.FILL_AND_STROKE);
	    paint.setAntiAlias(true);
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),imgId); 
		int x =0 ,y = 0 ;
		int bw = bmp.getWidth() ,bh = bmp.getHeight();
		switch(position){
		case TOP:
				x = xPoint-padding;
				y = yPoint-padding - bh;
				
			break ;
		case BUTTOM:
			x = xPoint-padding;
			y = yPoint+padding+height ;
			break ;
		case LEFT:
			x = xPoint -padding - bw ;
			y = yPoint - padding ;
			break ;
		case RIGHT:
			x = xPoint+padding+width ;
			y = yPoint-padding ;
			break ;
		case TOP_LEFT:
			x = xPoint - padding - bw ;
			y = yPoint - padding - bh ;
		    break;
		case TOP_RIGHT:
			x = xPoint + padding + width ;
			y = yPoint - padding - bh ;
			break ;
		case BUTTOM_LEFT:
			
			x = xPoint - padding - bw ;
			y = yPoint +padding +height ;
			break ;
		case BUTTOM_RIGHT:
			x = xPoint + padding+width ;
			y = yPoint + padding + height ;
			break ;
			
		}
		Log.i("GuideView", "x:"+x+" y:"+y);
		canvas.drawBitmap(bmp, x, y, paint);
	}
}
