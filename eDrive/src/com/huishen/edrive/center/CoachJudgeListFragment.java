package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.widget.TitleListFragment;

/**
 * 教练评价列表
 * @author zhanghuan
 *
 */
public class CoachJudgeListFragment extends TitleListFragment {
    private int coachId ;
    private String coachName ;
    private float score ;
	public CoachJudgeListFragment(Context context, String titlestr, String url ,int coachId ,String coachName ,float score) {
		super(context, titlestr, url);
		this.coachId = coachId ;
		if(coachName!=null){
		this.coachName = coachName ;
		}
		this.score = score ;
		
	}

	public CoachJudgeListFragment(Context context, Object tag, String titlestr,
			String url,int coachId) {
		super(context, tag, titlestr, url);
		this.coachId = coachId ;
	}

	@Override
	public void setList(String data, PullToRefreshListView list) {
//		[{"content":"很好","contentTime":"2015-02-04","phone":"13558657902","score":5.0},
//		  {"content":"21","contentTime":"2015-02-05","phone":"313","score":3.0}]
		Log.i(TAG, data) ;
		//设置列表项
		ArrayList<Map<String ,Object>> judgeListData = new ArrayList<Map<String ,Object>>();
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("name",coachName);
		map.put("rating",this.score);
		map.put("score",this.score);
		judgeListData.add(map);
		try{
			JSONArray array = new JSONArray(data);
			for(int i = 0 ;i<array.length() ;i++){
				HashMap<String , Object> mapa = new HashMap<String , Object>();
				JSONObject json = array.getJSONObject(i);
				mapa.put("rating",(float)json.optDouble("score",0));
				StringBuilder tel = new StringBuilder(json.optString("phone","匿名")) ;
				Log.i(TAG, "before:"+tel.toString()) ;
//				if(!tel.equals("匿名") && !tel.equals("") && tel.length()>10){
//					tel.replace(4, 7, "****");
//				}
				Log.i(TAG, tel.toString()) ;
				mapa.put("stuname",tel.toString()+" "+json.optString("contentTime" ,""));
				mapa.put("content",json.optString("content" ,""));
				judgeListData.add(mapa);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		CoachJudgeListAdapter judgeAdapter = new CoachJudgeListAdapter(this.context,judgeListData);
		list.setAdapter(judgeAdapter);
		list.onRefreshComplete(); 
	}

	@Override
	public void getWebData() {
		if(coachId == -1){
			AppUtil.ShowShortToast(this.context,"获取数据异常") ;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", coachId+"");
		NetUtil.requestStringData(SRL.Method.METHOD_GET_COACH_JUDGE_LIST,TAG , map, new Response.Listener<String>() {

			@Override
			public void onResponse(String result) {
				if(result == null || result.equals("")){
					AppUtil.ShowShortToast(context.getApplicationContext(), "获取数据异常");
				}else{
					if(loading.getVisibility() == View.VISIBLE){
					loading.setVisibility(View.GONE);
					}
					setList(result,CoachJudgeListFragment.this.list);
				}
			}
			
		}, new DefaultErrorListener(this.getActivity(),null ,loading ,list)) ;
		
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

	@Override
	public void onDestroy() {
		NetUtil.cancelRequest(TAG);
		super.onDestroy();
	}

	@Override
	public void getMore() {
		
	}
	
	

}
