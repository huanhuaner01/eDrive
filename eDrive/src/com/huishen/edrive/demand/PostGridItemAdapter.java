package com.huishen.edrive.demand;

import java.util.List;
import java.util.Map;

import com.huishen.edrive.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 发布需求选择服务项的适配器
 * @author zhanghuan
 * 
 */
public class PostGridItemAdapter extends SimpleAdapter {
	private List<? extends Map<String,Object>> data ;
	public PostGridItemAdapter(Context context,
			List<? extends Map<String, Object>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data = data ;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view =  super.getView(position, convertView, parent);
		TextView posttv = (TextView)view.findViewById(R.id.item_post_tv) ;
		setTvStatus(posttv ,Integer.parseInt(this.data.get(position).get("status").toString()),position );
		//设置背景颜色，在可选状态可能会有多种颜色值
		view.setTag(this.data.get(position));
		return view ;
	}
	
	/**
	 * 选择后界面的更改
	 * @param position 选中的下标
	 * 
	 */
	public void selectOption(int position){
		if(this.data.get(position).get("status").toString().equals("0")){
			this.data.get(position).put("status", 1);
		}else{
			this.data.get(position).put("status",0);
		}
		this.notifyDataSetChanged() ;
		
	}
	
	
	private void setTvStatus(TextView tv , int status ,int position){
		
		switch(status){
		case 0:  //没选中
			tv.setEnabled(true)  ;
			tv.setSelected(false);
			break ;
		case 1:  //选中
			tv.setEnabled(true)  ;
			tv.setSelected(true);
			break ;
	}
	}
}
