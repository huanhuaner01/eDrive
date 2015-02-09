package com.huishen.edrive.widget;

import java.util.List;
import java.util.Map;

import com.huishen.edrive.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@SuppressLint("NewApi") 
public class CalendarGridViewAdapter extends SimpleAdapter {
    private List<? extends Map<String,Object>> data ;
    private int currentChecked = -1 ; //记录目前选中的item的下标 。-1为没有选择
    private int currentpreStatus = 0 ; //记录目前选中的item之前的状态
    private Context context ;
    public CalendarGridViewAdapter(Context context,
			List<? extends Map<String, Object>> data, int resource, String[] from,
			int[] to ) {
		super(context, data, resource, from, to);
		this.data = data ;
		this.context = context ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view =  super.getView(position, convertView, parent);
		TextView tv = (TextView)view.findViewById(R.id.day) ;
		TextView bottomtv = (TextView)view.findViewById(R.id.day_bottomtv) ;
		setTvStatus(bottomtv ,tv ,Integer.parseInt(this.data.get(position).get("status").toString()),position );
		//设置背景颜色，在可选状态可能会有多种颜色值
		if(this.data.get(position).get("color") != null){
		view.setBackgroundResource(Integer.parseInt(this.data.get(position).get("color").toString()));
		}
		view.setTag(this.data.get(position));
		return view ;
	}
	
	/**
	 * 选择后界面的更改
	 * @param position 选中的下标
	 * 
	 */
	public void selectOption(int position){
		this.data.get(currentChecked).put("status",this.currentpreStatus) ;
		this.currentpreStatus = Integer.parseInt(this.data.get(position).get("status").toString()) ;
		
		this.data.get(position).put("status", 1);
		this.notifyDataSetChanged() ;
		
	}
	private void setTvStatus(TextView bottomtv,TextView tv , int status ,int position){
		
		switch(status){
		case 0:  //可选
			tv.setEnabled(true)  ;
			tv.setSelected(false);
			break ;
		case 1:  //选中
			tv.setEnabled(true)  ;
			tv.setSelected(true);
			this.currentChecked = position ;
			break ;
			
		case 2:  //今天
			tv.setEnabled(true)  ;
			bottomtv.setText("今日") ;
			bottomtv.setTextColor(this.context.getResources().getColor(R.color.main_color)) ;
			if(this.currentChecked == -1){
				tv.setSelected(true);
				this.currentChecked = position ;
				this.currentpreStatus = 2 ;
			}else{
				tv.setSelected(false);
			}
			break ;
			
		case -1: //不可选
			tv.setEnabled(false) ;
			tv.setSelected(false);
			tv.setBackground(null);
			tv.setTextColor(this.context.getResources().getColor(R.color.Grey_line)) ;
			break ;
		case -2: //今天不可选
			tv.setEnabled(false)  ;
			tv.setSelected(false);
			tv.setTextColor(this.context.getResources().getColor(R.color.Grey_line)) ;
			bottomtv.setText("今日") ;
			bottomtv.setTextColor(this.context.getResources().getColor(R.color.main_color)) ;
			break ;
			
		}
	}
    
}
