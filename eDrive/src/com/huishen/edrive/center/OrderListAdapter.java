package com.huishen.edrive.center;

import java.util.List;
import java.util.Map;

import com.huishen.edrive.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 订单列表
 * @author zhanghuan
 *
 */
public class OrderListAdapter extends SimpleAdapter {
    private List<? extends Map<String, ?>> data ;
    private Context context ;
	public OrderListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data = data ;
		this.context = context ;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View root = super.getView(position, convertView, parent);
		TextView time = (TextView)root.findViewById(R.id.item_order_time);
		TextView status = (TextView)root.findViewById(R.id.item_order_status);
		TextView note = (TextView)root.findViewById(R.id.item_order_note);
		if(Integer.parseInt(data.get(position).get("status").toString()) == 3){
			
			time.setTextColor(context.getResources().getColor(R.color.main_color));
			status.setTextColor(context.getResources().getColor(R.color.main_color));
			note.setTextColor(context.getResources().getColor(R.color.main_color));
		}else{
			time.setTextColor(context.getResources().getColor(R.color.tv_gray_color));
			status.setTextColor(context.getResources().getColor(R.color.tv_gray_color));
			note.setTextColor(context.getResources().getColor(R.color.tv_gray_color));
		}
		root.setTag(data.get(position));
		return root ;
	}
	
	

}
