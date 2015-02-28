package com.huishen.edrive.apointment;

import java.util.ArrayList;
import java.util.HashMap;

import com.huishen.edrive.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
	
	public AppointmentSubExListApdater(Context ctx,
			ArrayList<HashMap<String, String>> mGData,
			ArrayList<ArrayList<HashMap<String , String>>> list) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroupData = mGData;
		mData = list;
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
//		if (convertView == null) {
//			convertView = mInflater
//					.inflate(R.layout.coach_meal_list_item, null);
//		}
//		GroupViewHolder holder = new GroupViewHolder();
//		holder.mealName = (TextView) convertView.findViewById(R.id.class_time);
//		holder.mealPrize = (TextView) convertView.findViewById(R.id.class_status);
//		holder.mealIcon = (ImageView) convertView.findViewById(R.id.sub_ic);
//		if (isExpanded) {
//			holder.mealIcon.setImageResource(R.drawable.ic_expand);
//		} else {
//			holder.mealIcon.setImageResource(R.drawable.ic_shrink);
//		}
//		// ----------------添加列表数据------------------
//		HashMap<String, String> data = mGroupData.get(groupPosition);
//		if (data.get("mealname") != null) {
//			holder.mealName.setText(data.get("mealname").toString());
//		}
//		if (data.get("mealprize") != null) {
//			holder.mealPrize.setText(data.get("mealprize").toString());
//		}
//		// ----------------添加列表数据结束！----------------
		return convertView;
	}
//
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
//		Log.i("CoachMealListExpandAdapter",
//				mData.get(groupPosition).get(childPosition).toString());
//		if (convertView == null) {
//			convertView = mInflater.inflate(R.layout.coach_meal_listchild_item,
//					null);
//		}
//		ChildViewHolder holder = new ChildViewHolder();
//		holder.mContent = (TextView) convertView
//				.findViewById(R.id.coach_meal_listchild_tv);
//
//		if (mData.get(groupPosition).get(childPosition) != null) {
//			holder.mContent.setText(mData.get(groupPosition).get(childPosition)
//					.toString());
//		}
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
