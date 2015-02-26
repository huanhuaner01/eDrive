package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.huishen.edrive.widget.TitleListFragment;

/**
 * 教练评价列表
 * @author zhanghuan
 *
 */
public class CoachJudgeListFragment extends TitleListFragment {
    private int coachId ;
	public CoachJudgeListFragment(Context context, String titlestr, String url ,int coachId) {
		super(context, titlestr, url);
		this.coachId = coachId ;
	}

	public CoachJudgeListFragment(Context context, Object tag, String titlestr,
			String url,int coachId) {
		super(context, tag, titlestr, url);
		this.coachId = coachId ;
	}

	@Override
	public void setList(String data, ListView list) {
		//设置列表项
		ArrayList<Map<String ,Object>> judgeListData = new ArrayList<Map<String ,Object>>();
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("name","王教练");
		map.put("rating",4.6);
		map.put("score",4.6);
		judgeListData.add(map);
		for(int i = 1 ; i<5 ; i++){
			map = new HashMap<String , Object>();
			map.put("rating",2.7);
			map.put("stuname","13*****12 12-30 12:30");
			map.put("content","教练真不错");
			judgeListData.add(map);
		}
		CoachJudgeListAdapter judgeAdapter = new CoachJudgeListAdapter(this.context,judgeListData);
		list.setAdapter(judgeAdapter);
	}

	@Override
	public void getWebData() {
		setList(null,this.list);
	}

	@Override
	public void setBack(ImageButton back2) {
		back2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				CoachJudgeListFragment.this.getActivity().finish();
			}
			
		});
	}

	@Override
	public void setList(String data, ExpandableListView list) {
		
	}
	
	

}
