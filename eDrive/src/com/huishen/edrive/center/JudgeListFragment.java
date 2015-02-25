package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;

import com.huishen.edrive.widget.TitleListFragment;

public class JudgeListFragment extends TitleListFragment {

	public JudgeListFragment(Context context, String titlestr, String url) {
		super(context, titlestr, url);
	}

	public JudgeListFragment(Context context, Object tag, String titlestr,
			String url) {
		super(context, tag, titlestr, url);
		
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
	public void setNote(TextView note) {
		
	}

}
