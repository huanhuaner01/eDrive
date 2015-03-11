package com.huishen.edrive.center;

import java.util.List;
import java.util.Map;

import com.huishen.edrive.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class JudgeListSimAdapter extends SimpleAdapter {
	private List<? extends Map<String, ?>> data;
	private Context context;

	public JudgeListSimAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data = data;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View root =  super.getView(position, convertView, parent);
		RatingBar rating = (RatingBar)root.findViewById(R.id.ratingbar);
		float ratingnum = (float)Double.parseDouble(data.get(position).get("rating").toString());
		if(ratingnum == -1.0){
			rating.setVisibility(View.GONE);
			TextView tv = (TextView)root.findViewById(R.id.judge_listitem_stuname);
			tv.append("(追评)");
		}else{
			rating.setVisibility(View.VISIBLE);
		    rating.setRating(ratingnum);
		}
		return root ;
	}

}
