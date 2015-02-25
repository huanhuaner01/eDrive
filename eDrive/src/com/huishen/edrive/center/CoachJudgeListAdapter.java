package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huishen.edrive.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 评价列表的适配器（带有分数统计项）
 * @author zhanghuan
 *
 */
public class CoachJudgeListAdapter extends BaseAdapter {
	private List<Map<String ,Object>> data ;
//	private Context context ;
	private LayoutInflater inflater;
    private final int TYPE_HEAD = 0 ;
    private final int TYPE_DEFUALT = 1 ;
	public CoachJudgeListAdapter(Context context ,List<Map<String ,Object>> data) {
		if(data == null){
			data = new ArrayList<Map<String ,Object>>();
		}
		this.data = data ;
//		this.context = context ;
		inflater = LayoutInflater.from(context) ;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	// 每个convert view都会调用此方法，获得当前所需要的view样式
	@Override
	public int getItemViewType(int position) {
		int p = position;
		if (p == 0)
			return TYPE_HEAD;
		else 
			return TYPE_DEFUALT ;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup viewgroup) {
		HeadViewHolder headHolder = null ;
		JudgeViewHolder judgeHolder = null ;
		int type = getItemViewType(position);
		if(view == null){
			// 按当前所需的样式，确定new的布局  
            switch (type) { 
            case TYPE_HEAD: //初始化评价列表头组件
            	view = inflater.inflate(R.layout.judgelist_head_item,
        				null);
            	headHolder = new HeadViewHolder();
            	headHolder.name = (TextView)view.findViewById(R.id.list_ji_name);
            	headHolder.ratingbar  = (RatingBar)view.findViewById(R.id.list_ji_ratingbar); //list_ji_ratingbar
        		headHolder.judge = (TextView)view.findViewById(R.id.list_ji_score) ;//list_ji_score
        		Log.i("juedgeListAdapter", "headHolder.ratingbar is null "+(headHolder.ratingbar==null));
            	view.setTag(headHolder) ;
            	break ;
            case TYPE_DEFUALT:
            	view = inflater.inflate(R.layout.judge_list_item,
        				null);
            	judgeHolder = new JudgeViewHolder();
            	judgeHolder.ratingbar = (RatingBar)view.findViewById(R.id.ratingbar); //ratingbar
            	judgeHolder.stuname = (TextView)view.findViewById(R.id.judge_listitem_stuname); //judge_listitem_stuname
            	judgeHolder.judgecon = (TextView)view.findViewById(R.id.judge_listitem_content); //judge_listitem_content
            	view.setTag(judgeHolder);
            	break ;
            }
		}
            else{
            	switch (type) {
            	case TYPE_HEAD:
            		headHolder = (HeadViewHolder)view.getTag();
            		break ;
            	case TYPE_DEFUALT:
            		judgeHolder = (JudgeViewHolder)view.getTag() ;
            		break ;
            	}
            }
		// 设置资源
		switch(type){
		case TYPE_HEAD:
			Log.i("juedgeListAdapter", data.toString());
			try{
			headHolder.name.setText(data.get(position).get("name").toString());
			headHolder.ratingbar.setRating(Float.parseFloat(data.get(position).get("rating").toString()));
			headHolder.judge.setText(data.get(position).get("score").toString()+"分");
			}catch(Exception e){
				e.printStackTrace() ;
			}
    		break ;
    	case TYPE_DEFUALT:
    		try{
    			Log.i("juedgeListAdapter","judgeHolder == null "+(judgeHolder == null));
    			judgeHolder.stuname.setText(data.get(position).get("stuname").toString());
    			judgeHolder.ratingbar.setRating(Float.parseFloat(data.get(position).get("rating").toString()));
    			judgeHolder.judgecon.setText(data.get(position).get("content").toString());
    			}catch(Exception e){
    				e.printStackTrace() ;
    			}
    		break ;
		}
		return view ;
	}
	
	
	/**
	 * 只有文本的列表项
	 * @author zhanghuan
	 * 
	 */
	public class HeadViewHolder {
		TextView name; //list_ji_name
		RatingBar ratingbar ; //list_ji_ratingbar
		TextView judge ;//list_ji_score
	}
	
	/**
	 * 只有图片的列表项
	 * @author zhanghuan
	 * 
	 */
	public class JudgeViewHolder {
		RatingBar ratingbar ; //ratingbar
		TextView stuname ; //judge_listitem_stuname
		TextView judgecon ; //judge_listitem_content
	}

}

