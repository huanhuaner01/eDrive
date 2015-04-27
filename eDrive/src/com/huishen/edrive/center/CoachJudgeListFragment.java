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
import com.huishen.edrive.widget.TitleListFragment.GetDataTask;

/**
 * 教练评价列表
 * @author zhanghuan
 *
 */
public class CoachJudgeListFragment extends TitleListFragment {
    private int coachId ;
    private String coachName ;
    private float score ;
    private int pageNumber=0 ,pageTotal =0 ; //分页
    private ArrayList<Map<String ,Object>> judgeListData = new ArrayList<Map<String ,Object>>();
    CoachJudgeListAdapter judgeAdapter ;
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
//		{"conditions":{"id":154},"pageNumber":1,"pageSize":10,
//			"rows":[{"content":"呵呵没事","contentTime":"2015-04-16 09:28:53","phone":"180***112","score":4.0},
//			        {"content":"不是这样好不","contentTime":"2015-04-16 10:35:50","phone":"170***713","score":4.3},
//			        {"content":"51326528","contentTime":"2015-04-16 17:01:59","phone":"158***379","score":4.0},
//			        {"content":"我评价一下","contentTime":"2015-04-21 15:00:06","phone":"183***843","score":5.0}],
//			        "total":0}
		Log.i(TAG, data) ;
		//设置列表项
		judgeListData.clear() ; //清除数据
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("name",coachName);
		map.put("rating",this.score);
		map.put("score",this.score);
		judgeListData.add(map);
		addData(data);
		judgeAdapter = new CoachJudgeListAdapter(this.context,judgeListData);
		list.setAdapter(judgeAdapter);
	}
	
	/**
	 * 解析数据，添加到listdata里面
	 * @param data
	 *        原始数据
	 */
    private void addData(String data){
		try{
			JSONObject jo = new JSONObject(data);
			JSONArray array = jo.getJSONArray("rows");
			pageNumber = jo.getInt("pageNumber");
			if(jo.getInt("pageSize")>0){
			pageTotal = (int) Math.ceil(jo.getInt("total")/(double)jo.getInt("pageSize"));
			}
			
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
				list.onRefreshComplete(); 
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
	public void onDestroy() {
		NetUtil.cancelRequest(TAG);
		super.onDestroy();
	}

	@Override
	public void getMore() {
	if(pageNumber >= pageTotal){
    	    
			AppUtil.ShowShortToast(getActivity(), "没有更多了...");
			new GetDataTask().execute();
//			list.setMode(Mode.PULL_FROM_START);//仅支持下拉
			return ;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("page",(pageNumber+1)+"");
		map.put("id", coachId+"");
		NetUtil.requestStringData(SRL.Method.METHOD_GET_COACH_JUDGE_LIST, TAG ,map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						list.onRefreshComplete(); //停止加载框
						if(result == null || result.equals("")){
							AppUtil.ShowShortToast(getActivity(), "服务器繁忙");
						}else{
							updateList(result);
						}
						
					}
				},new DefaultErrorListener(this.getActivity() ,null ,loading ,list));
	}
	
	
	/**
	 * 加载更多
	 * @param data
	 */
    public void updateList(String data){
    	addData(data);
    	judgeAdapter.notifyDataSetChanged();
    }

}
