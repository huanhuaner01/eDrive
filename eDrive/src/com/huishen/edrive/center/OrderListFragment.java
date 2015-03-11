package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.apointment.MassageListener;
import com.huishen.edrive.apointment.MessageDialog;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.TitleListFragment;

public class OrderListFragment extends TitleListFragment {
	private String TAG = "OrderListFragment" ;
	private ArrayList<Map<String ,Object>> listdata = new ArrayList<Map<String ,Object>>();
	private String[] from  = new String[]{"time","statusvalue","content" ,"note"};
	private int[] to = new int[]{R.id.item_order_time,R.id.item_order_status,R.id.item_order_content,R.id.item_order_note};
	private OrderListAdapter adapter ;
	
	
	@Override
	public void onResume() {
		getWebData();
		super.onResume();
	}

	public OrderListFragment(Context context, String titlestr, String url) {
		super(context, titlestr, url);
		// TODO Auto-generated constructor stub
	}

	public OrderListFragment(Context context, Object tag, String titlestr,
			String url) {
		super(context, tag, titlestr, url);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getWebData() {
		HashMap<String, String> map = new HashMap<String, String>();
		NetUtil.requestStringData(SRL.Method.METHOD_GET_ORDER_LIST, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if(result == null || result.equals("")){
							AppUtil.ShowShortToast(getActivity(), "服务器繁忙");
						}else{
							setList(result , list);
						}
						
					}
				},new DefaultErrorListener());
//		setList("" , list);
	}

	@Override
	public void setList(String data, ListView list) {
		/**
		 *[{"audio":"/attachment/audio/34/FILE_2015031015130108025507.mp3",
		 *"coachId":21,"coachName":"雷猴","content":"能45-60天拿证,考试包接包送,",
		 *"createTime":"2015-03-10","id":494,"schoolName":"蜀娟驾校","status":3}
		 */
		listdata.clear();
		try{
			JSONArray array = new JSONArray(data);
			
		for(int i = 0 ;i< array.length() ;i++){
			JSONObject json = array.optJSONObject(i);
			HashMap<String ,Object> map = new HashMap<String ,Object>();
			map.put("time", json.optString("createTime", ""));
			int status = json.optInt("status", 1);
			switch(status){
			case 0:map.put("statusvalue", "已关闭");
				break ;
			case 1:map.put("statusvalue", "");break ;
			case 2:map.put("statusvalue", "已关闭");break ;
			case 3:map.put("statusvalue", "进行中");break ;
			}
            
			map.put("status", json.optInt("status", 1));
			if(json.optString("audio", "").equals("")){
			map.put("content", json.optString("content", ""));
			}else{
				map.put("content", "语音订单");
			}
			int note = json.optInt("coachId" ,-1);
			//0失效，1生效，2取消，3已抢单
			if(note == -1){
				map.put("note", "无人抢单");
			}else{
				
			    map.put("note",json.optString("coachName", "")+"("+json.optString("schoolName", "")+")抢单成功");
			}
			map.put("coachId",json.optInt("coachId", -1) );
			map.put("id", json.optInt("id", 1));
			listdata.add(map);
		}
		
		adapter = new OrderListAdapter(getActivity(), listdata, R.layout.item_orderlist, from, to);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
                HashMap<String ,Object> map = (HashMap<String ,Object>)view.getTag();
                int mapstatus =Integer.parseInt( map.get("status").toString()); 
                if(mapstatus == 1 || mapstatus == 3){
                	Intent i = new Intent(getActivity() , OrderDetailActivity.class);
                	i.putExtra("id", Integer.parseInt(map.get("id").toString()));
                	i.putExtra("coachId", Integer.parseInt(map.get("coachId").toString()));
                	getActivity().startActivity(i);
                }else{
                	AppUtil.ShowShortToast(getActivity(), "该订单已关闭，不能查看详情");
                }
			}
		});
		list.setAdapter(adapter);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		if(this.mSwipeLayout.isRefreshing()){
			mSwipeLayout.setRefreshing(false);
		}
		}
	}

	@Override
	public void setList(String data, ExpandableListView list) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void setBack(ImageButton back2) {
		back2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
			
		});
	}

}
