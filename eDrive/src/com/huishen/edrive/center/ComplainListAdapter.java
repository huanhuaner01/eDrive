package com.huishen.edrive.center;

import java.util.List;
import java.util.Map;

import com.huishen.edrive.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ComplainListAdapter extends SimpleAdapter {
	    private List<? extends Map<String,Object>> data ;
	    private int currentChecked = -1 ; //记录目前选中的item的下标 。-1为没有选择
	    private int currentpreStatus = 0 ; //记录目前选中的item之前的状态
	    private Context context ;
	public ComplainListAdapter(Context context,
			List<? extends Map<String, Object>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data = data ;
		this.context = context;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View root = super.getView(position, convertView, parent);
		ImageView img = (ImageView)root.findViewById(R.id.item_complain_ic);
		ImageView check = (ImageView)root.findViewById(R.id.item_complain_check);
		LinearLayout lay = (LinearLayout)root.findViewById(R.id.item_complain_lay);
		int status = Integer.parseInt(this.data.get(position).get("status").toString());
		if(status == 1){
			img.setImageResource(R.drawable.ic_complain_press);
			check.setVisibility(View.VISIBLE);
			lay.setSelected(true);
			currentChecked = position ;
		}else{
			img.setImageResource(R.drawable.selector_complain_ic);
			check.setVisibility(View.GONE);
			lay.setSelected(false);
		}
		root.setTag(this.data.get(position).get("content").toString());
		return root ;
	}


	/**
	 * 选择后界面的更改
	 * @param position 选中的下标
	 * 
	 */
	public void selectOption(int position){
		if(currentChecked != -1){
		this.data.get(currentChecked).put("status",this.currentpreStatus) ;
		}
		this.data.get(position).put("status", 1);
		this.notifyDataSetChanged() ;
		
	}
	
}
