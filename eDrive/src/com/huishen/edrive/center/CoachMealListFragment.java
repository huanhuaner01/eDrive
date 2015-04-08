package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.widget.TitleListFragment;

public class CoachMealListFragment extends TitleListFragment {
	private int coachId ;
	private ArrayList<HashMap<String ,String>> mGroupData = null;
	private ArrayList<ArrayList<String>>   mData = null;
	public CoachMealListFragment(Context context, String titlestr, String url ,int coachId) {
		super(context, titlestr, url);
		this.coachId = coachId ;
		this.setTag("CoachMealListFragment");
	}

	public CoachMealListFragment(Context context, Object tag,
			String titlestr, String url ,int coachId) {
		super(context, tag, titlestr, url);
		this.coachId = coachId ;
		this.setTag("CoachMealListFragment");
	}

	@Override
	public void getWebData() {
		this.showExpandableList(true) ;
		if(coachId == -1){
			AppUtil.ShowShortToast(this.context,"获取数据异常") ;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", coachId+"");
		NetUtil.requestStringData(SRL.Method.METHOD_GET_COACH_MEAL, TAG ,map, new Response.Listener<String>() {

			@Override
			public void onResponse(String result) {
				if(result == null || result.equals("")){
					AppUtil.ShowShortToast(context.getApplicationContext(), "获取数据异常");
				}else{
					setList(result,CoachMealListFragment.this.expandablelist);
				}
			}
			
		}, new DefaultErrorListener(this.getActivity() ,null ,loading ,mSwipeLayout)) ;
	}

	@Override
	public void setList(String data, ListView list) {
		
	}

	@Override
	public void setList(String data, ExpandableListView list) {
		if(loading.getVisibility() == View.VISIBLE){
			loading.setVisibility(View.GONE);
		}
		//设置列表项
		mGroupData = new ArrayList<HashMap<String ,String>>();
		mData = new ArrayList<ArrayList<String>>();
		try{
			//[{"carTypeContent":"桑塔拉","cash":50,"cohId":1,"content":"接送",
//			   "createDate":"2015-03-01","id":1,"schoolId":1,"title":"开学季","type":2},
//			{"carTypeContent":"捷达","cash":50,"cohId":1,"content":"保送","createDate":"2015-03-18",
//			"id":2,"schoolId":1,"title":"大学生优惠套餐","type":2}]
			JSONArray array = new JSONArray(data);
			for(int i = 0 ;i<array.length() ;i++){
		    JSONObject json = array.getJSONObject(i);
			HashMap<String ,String> map = new HashMap<String ,String>();
			map.put("mealname", json.optString(SRL.ReturnField.COACH_MEAL_TITLE ,"暂无"));
			map.put("mealprize",json.optString(SRL.ReturnField.COACH_MEAL_PRIZE ,"暂无"));
			mGroupData.add(map);
			ArrayList<String> childmap = new ArrayList<String>();
			childmap.add("套餐内容包含服务："+json.optString(SRL.ReturnField.COACH_MEAL_CONTENT ,"暂无")) ;
			childmap.add("驾驶车型："+json.optString(SRL.ReturnField.COACH_MEAL_CARTYPE ,"暂无")) ;
			childmap.add("班        型："+json.optString(SRL.ReturnField.COACH_MEAL_CLASSTYPE ,"暂无")) ;
			childmap.add("训练类型："+json.optString(SRL.ReturnField.COACH_MEAL_LICENSETYPE ,"暂无")) ;
			mData.add(childmap);
		}
		
		CoachMealListExpandAdapter judgeAdapter = new CoachMealListExpandAdapter(this.context,mGroupData,mData);
		list.setAdapter(judgeAdapter);
		if(mGroupData.size()>0){
		list.expandGroup(0);
		}
		}catch(Exception e){
			e.printStackTrace() ;
		}
		if(this.mSwipeLayout.isRefreshing()){
			mSwipeLayout.setRefreshing(false);
		}
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
