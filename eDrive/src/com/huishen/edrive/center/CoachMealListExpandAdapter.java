package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;

import com.huishen.edrive.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 套餐可扩展列表
 * 
 * @author zhanghuan
 * 
 */
public class CoachMealListExpandAdapter implements ExpandableListAdapter {

	private Context mContext;
	private LayoutInflater mInflater = null;
	private ArrayList<HashMap<String, String>> mGroupData = null;
	private ArrayList<ArrayList<String>> mData = null;

	public CoachMealListExpandAdapter(Context ctx,
			ArrayList<HashMap<String, String>> mGData,
			ArrayList<ArrayList<String>> list) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroupData = mGData;
		mData = list;
		Log.i("CoachMealListExpandAdapter", mData.toString());
	}

	public void setData(ArrayList<ArrayList<String>> list) {
		mData = list;
	}

	@Override
	public int getGroupCount() {
		return mData.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		Log.i("CoachMealListExpandAdapter",
				"getChildrenCount:" + mData.get(groupPosition).size());
		return mData.get(groupPosition).size();
	}

	@Override
	public ArrayList<String> getGroup(int groupPosition) {
		return mData.get(groupPosition);
	}

	@Override
	public String getChild(int groupPosition, int childPosition) {
		return mData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.coach_meal_list_item, null);
		}
		GroupViewHolder holder = new GroupViewHolder();
		holder.mealName = (TextView) convertView.findViewById(R.id.meal_name);
		holder.mealPrize = (TextView) convertView.findViewById(R.id.meal_prize);
		holder.mealIcon = (ImageView) convertView.findViewById(R.id.meal_ic);
		if (isExpanded) {
			holder.mealIcon.setImageResource(R.drawable.ic_expand);
		} else {
			holder.mealIcon.setImageResource(R.drawable.ic_shrink);
		}
		// ----------------添加列表数据------------------
		HashMap<String, String> data = mGroupData.get(groupPosition);
		if (data.get("mealname") != null) {
			holder.mealName.setText(data.get("mealname").toString());
		}
		if (data.get("mealprize") != null) {
			holder.mealPrize.setText(data.get("mealprize").toString());
		}
		// ----------------添加列表数据结束！----------------
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		Log.i("CoachMealListExpandAdapter",
				mData.get(groupPosition).get(childPosition).toString());
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.coach_meal_listchild_item,
					null);
		}
		ChildViewHolder holder = new ChildViewHolder();
		holder.mContent = (TextView) convertView
				.findViewById(R.id.coach_meal_listchild_tv);

		if (mData.get(groupPosition).get(childPosition) != null) {
			holder.mContent.setText(mData.get(groupPosition).get(childPosition)
					.toString());
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		/* 很重要：实现ChildView点击事件，必须返回true */
		return false;
	}

	private class GroupViewHolder {
		TextView mealName;
		TextView mealPrize;
		ImageView mealIcon;
	}

	private class ChildViewHolder {
		TextView mContent;
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getCombinedChildId(long arg0, long arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCombinedGroupId(long arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onGroupCollapsed(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGroupExpanded(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub
		
	}

}
