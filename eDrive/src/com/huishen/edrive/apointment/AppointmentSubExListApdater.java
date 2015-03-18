package com.huishen.edrive.apointment;

import java.util.ArrayList;
import java.util.HashMap;

import com.huishen.edrive.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 约课界面下拉列表
 * @author zhanghuan
 * 
 */
public class AppointmentSubExListApdater implements ExpandableListAdapter{
	
	private Context mContext;
	private LayoutInflater mInflater = null;
	private ArrayList<HashMap<String, String>> mGroupData = null;
	private ArrayList<ArrayList<HashMap<String , String>>> mData = null;
	private TimeClassApointListener listener ;
	public AppointmentSubExListApdater(Context ctx,
			ArrayList<HashMap<String, String>> mGData,
			ArrayList<ArrayList<HashMap<String , String>>> list , TimeClassApointListener listener) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroupData = mGData;
		mData = list;
		this.listener = listener ;
		Log.i("CoachMealListExpandAdapter", mData.toString());
	}

	@Override
	public boolean areAllItemsEnabled() {
		
		return false;
	}

	@Override
	public HashMap<String ,String> getChild(int groupPosition, int childPosition) {
		return mData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		
		return childPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder group = null ;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.item_appointment_sub, null);
			group = new GroupViewHolder();
			group.subName = (TextView)convertView.findViewById(R.id.sub_name) ;
			group.subStatus = (TextView)convertView.findViewById(R.id.sub_status) ;
			group.subIcon = (ImageView)convertView.findViewById(R.id.sub_ic) ;
			convertView.setTag(group) ;
		}else{
			group = (GroupViewHolder)convertView.getTag() ;
		}
		if (isExpanded) {
			group.subIcon.setImageResource(R.drawable.ic_buttom);
		} else {
			group.subIcon.setImageResource(R.drawable.ic_next);
		}
//		// ----------------添加列表数据------------------
		HashMap<String, String> data = mGroupData.get(groupPosition);
		if (data.get("subName") != null) {
			group.subName.setText(data.get("subName").toString());
		}
		if (data.get("subStatus") != null) {
			group.subStatus.setText(data.get("subStatus").toString());
		}
//		// ----------------添加列表数据结束！----------------
		return convertView;
	}
//
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder holder = null ;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_appointment_class,
					null);
			holder = new ChildViewHolder();
			holder.classTime = (TextView) convertView.findViewById(R.id.class_time) ;
			holder.classStatus = (TextView) convertView.findViewById(R.id.class_status) ;
			holder.classBtn = (Button) convertView.findViewById(R.id.class_btn) ;
			convertView.setTag(holder);
		}else{
			holder = (ChildViewHolder)convertView.getTag() ;
		}
//		// ----------------添加列表数据------------------
		final HashMap<String, String> data = mData.get(groupPosition).get(childPosition);
		if (data.get("className") != null) {
			holder.classTime.setText(data.get("className").toString());
		}
		if (data.get("classStatus") != null) {
			holder.classStatus.setText(data.get("classStatus").toString());
		}
		holder.classBtn.setEnabled(true);
		if(Integer.parseInt(data.get("code").toString())==1){//已约
			holder.classBtn.setBackgroundResource(R.drawable.day_full);
			holder.classBtn.setText("取消");
		}else if(Integer.parseInt(data.get("code").toString())==2){ //已满
			holder.classBtn.setBackgroundResource(R.drawable.day_exists);
			holder.classBtn.setText("已满");
			holder.classBtn.setEnabled(false);
		}else{//可约
			holder.classBtn.setBackgroundResource(R.drawable.day_exists);
			holder.classBtn.setText("预约");
			
		}
		holder.classBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
//				arg0.setEnabled(false);
				listener.setOnClick(Integer.parseInt(data.get("subject")), Integer.parseInt(data.get("lessonTime")), Integer.parseInt(data.get("code")));
			}
			
		});
//		// ----------------添加列表数据结束！----------------
		return convertView;
	}
	
	private class GroupViewHolder {
		TextView subName;
		TextView subStatus;
		ImageView subIcon;
	}

	private class ChildViewHolder {
		TextView classTime;
		TextView classStatus;
		Button classBtn;
	}

	@Override
	public int getChildrenCount(int arg0) {
		
		return mData.get(arg0).size();
	}

	@Override
	public long getCombinedChildId(long arg0, long arg1) {
		
		return 0;
	}

	@Override
	public long getCombinedGroupId(long arg0) {
		
		return 0;
	}

	@Override
	public Object getGroup(int arg0) {
		
		return mGroupData.get(arg0);
	}

	@Override
	public int getGroupCount() {
		
		return mGroupData.size();
	}

	@Override
	public long getGroupId(int position) {
		
		return position;
	}

	@Override
	public boolean hasStableIds() {
		
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		
		return true;
	}

	@Override
	public boolean isEmpty() {
		
		return false;
	}

	@Override
	public void onGroupCollapsed(int arg0) {
		
	}

	@Override
	public void onGroupExpanded(int arg0) {
		
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		
	}

}
