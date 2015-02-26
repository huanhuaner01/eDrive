package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.huishen.edrive.widget.TitleListFragment;

public class CoachMealListFragment extends TitleListFragment {
	private int coachId ;
	private ArrayList<HashMap<String ,String>> mGroupData = null;
	private ArrayList<ArrayList<String>>   mData = null;
	public CoachMealListFragment(Context context, String titlestr, String url ,int coachId) {
		super(context, titlestr, url);
		this.coachId = coachId ;
	}

	public CoachMealListFragment(Context context, Object tag,
			String titlestr, String url ,int coachId) {
		super(context, tag, titlestr, url);
		this.coachId = coachId ;
	}

	@Override
	public void getWebData() {
		this.showExpandableList(true) ;
		setList(null ,this.expandablelist);
	}

	@Override
	public void setList(String data, ListView list) {
		
	}

	@Override
	public void setList(String data, ExpandableListView list) {
		//设置列表项
		mGroupData = new ArrayList<HashMap<String ,String>>();
		mData = new ArrayList<ArrayList<String>>();
		
		for(int i= 0 ;i<4;i++){
			HashMap<String ,String> map = new HashMap<String ,String>();
			map.put("mealname", "套餐"+(i+1));
			map.put("mealprize", "3699");
			mGroupData.add(map);
			ArrayList<String> childmap = new ArrayList<String>();
			for(int j = 0 ;j<4;j++){
				String  content = "测试数据";
				childmap.add(content);
			}
			mData.add(childmap);
		}
		
		CoachMealListExpandAdapter judgeAdapter = new CoachMealListExpandAdapter(this.context,mGroupData,mData);
		list.setAdapter(judgeAdapter);
	}
	
	@Override
	public void setBack(ImageButton back2) {
		back2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				CoachMealListFragment.this.getActivity().finish();
			}
			
		});
	}

}
