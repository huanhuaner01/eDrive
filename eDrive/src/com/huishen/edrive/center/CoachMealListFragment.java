package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huishen.edrive.R;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.widget.BaseFragment;
import com.huishen.edrive.widget.LoadingView;
import com.huishen.edrive.widget.TitleListFragment;

public class CoachMealListFragment extends BaseFragment {
	private int coachId ;
	private ArrayList<HashMap<String ,String>> mGroupData = null;
	private ArrayList<ArrayList<String>>   mData = null;
	private View RootView ;
	private PullToRefreshExpandableListView list ;
	private TextView title ;
	private ImageButton back ;
	private String titlestr ;
	private LoadingView loading ; //加载页面
	@Override
	public void onResume() {
		//获取网络数据
		getWebData();
		super.onResume();
		
	}
	
	public CoachMealListFragment(Context context, String titlestr, String url ,int coachId) {
		this.coachId = coachId ;
		this.titlestr = titlestr ;
		this.setTag("CoachMealListFragment");
	}

	public CoachMealListFragment(Context context, Object tag,
			String titlestr, String url ,int coachId) {
		this.coachId = coachId ;
		this.titlestr = titlestr ;
		this.setTag("CoachMealListFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try{
			RootView = inflater.inflate(R.layout.fragment_meallist, null);
			registView();
			initView();
		  } catch (Exception e) {
		}
		return RootView;
		
	}

	private void initView() {
		this.title.setText(titlestr);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
			
		});
		list.setMode(Mode.PULL_FROM_START);
		list.setOnRefreshListener(new OnRefreshListener<ExpandableListView>() {

			@Override
			public void onRefresh(
					PullToRefreshBase<ExpandableListView> refreshView) {
				getWebData();
			}
		});
		this.loading.setReLoadingListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				getWebData();
			}
			
		});
	}

	private void registView() {
		list = (PullToRefreshExpandableListView)RootView.findViewById(R.id.f_meallist);
		title = (TextView)RootView.findViewById(R.id.header_title);
		back = (ImageButton)RootView.findViewById(R.id.header_back);
		loading = (LoadingView)RootView.findViewById(R.id.titlelist_loading);
		
	}

	public void getWebData() {
		if(coachId == -1){
			AppUtil.ShowShortToast(this.getActivity(),"获取数据异常") ;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", coachId+"");
		NetUtil.requestStringData(SRL.Method.METHOD_GET_COACH_MEAL, TAG ,map, new Response.Listener<String>() {

			@Override
			public void onResponse(String result) {
				Log.i(TAG, "list is null ? "+(list==null));
				list.onRefreshComplete();
				if(result == null || result.equals("")){
					AppUtil.ShowShortToast(getActivity(), "获取数据异常");
				}else{
					setList(result,list.getRefreshableView());
				}
			}
			
		}, new DefaultErrorListener(this.getActivity() ,null ,loading ,list)) ;
	}
	
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
		
		CoachMealListExpandAdapter judgeAdapter = new CoachMealListExpandAdapter(this.getActivity(),mGroupData,mData);
		list.setAdapter(judgeAdapter);
		if(mGroupData.size()>0){
		list.expandGroup(0);
		}
		}catch(Exception e){
			e.printStackTrace() ;
		}
	}

}
