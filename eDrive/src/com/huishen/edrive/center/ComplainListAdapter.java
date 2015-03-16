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
		}else{
			img.setImageResource(R.drawable.selector_complain_ic);
			check.setVisibility(View.GONE);
		}
		
		root.setTag(this.data.get(position));
		return root ;
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
	
}
