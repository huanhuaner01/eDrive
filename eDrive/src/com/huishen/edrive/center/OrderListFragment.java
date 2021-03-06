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
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huishen.edrive.R;
import com.huishen.edrive.apointment.MessageDialog;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.widget.TitleListFragment;
import com.huishen.edrive.widget.TitleListFragment.GetDataTask;

public class OrderListFragment extends TitleListFragment {
	private ArrayList<Map<String ,Object>> listdata = new ArrayList<Map<String ,Object>>();
	private String[] from  = new String[]{"time","statusvalue","content" ,"note"};
	private int[] to = new int[]{R.id.item_order_time,R.id.item_order_status,R.id.item_order_content,R.id.item_order_note};
	private OrderListAdapter adapter ;
	private MessageDialog dialog ;
	
	private int pageNumber=0 ,pageTotal =0 ; //分页
	
	public OrderListFragment(Context context, String titlestr, String url) {
		super(context, titlestr, url);
		this.setTag("OrderListFragment");
	}

	public OrderListFragment(Context context, Object tag, String titlestr,
			String url) {
		super(context, tag, titlestr, url);
		this.setTag("OrderListFragment");
	}

	@Override
	public void getWebData() {
		HashMap<String, String> map = new HashMap<String, String>();
		NetUtil.requestStringData(SRL.Method.METHOD_GET_ORDER_LIST, TAG ,map,
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
				},new DefaultErrorListener(this.getActivity() ,null ,loading ,list));
//		setList("" , list);
	}

	@Override
	public void setList(String data, PullToRefreshListView lists) {
		/**
		 *[{"audio":"/attachment/audio/34/FILE_2015031015130108025507.mp3",
		 *"coachId":21,"coachName":"雷猴","content":"能45-60天拿证,考试包接包送,",
		 *"createTime":"2015-03-10","id":494,"schoolName":"蜀娟驾校","status":3}
		 *
		 *{"conditions":{"stuId":62},"pageNumber":1,"pageSize":10,
		 *"rows":[
		 *{"coachId":163,"coachName":"魏巍","content":"我要学车","createTime":"2015-04-21 17:14:07",
		 *"id":3044,"schoolName":"慧莘驾校","status":3},
		 *{"content":"包接包送","createTime":"2015-04-21 15:11:01","id":3030,"status":3},
		 *{"content":"师傅，我要学车，我需要能45-60天拿证,考试包接包送","createTime":"2015-04-21 15:03:30","id":3029,"status":1},
		 *{"content":"666666666","createTime":"2015-04-14 16:00:43","id":3015,"status":3},
		 *{"content":"全包，师傅","createTime":"2015-04-14 14:08:42","id":3014,"status":3},
		 *{"content":"学车啊，师傅，能给点力不","createTime":"2015-04-14 14:06:09","id":3013,"status":3},
		 *{"content":"包干","createTime":"2015-04-14 14:03:43","id":3012,"status":3},
		 *{"content":"包干","createTime":"2015-04-14 14:02:44","id":3011,"status":3},
		 *{"content":"一切全包","createTime":"2015-04-14 13:59:03","id":3010,"status":3},
		 *{"content":"一切全包","createTime":"2015-04-14 13:49:47","id":3009,"status":3}],
		 *"total":78}

		 */
		
		listdata.clear();
		try{
			JSONObject jo = new JSONObject(data);
			JSONArray array = jo.getJSONArray("rows");
			pageNumber = jo.getInt("pageNumber");
			if(jo.getInt("pageSize")>0){
			pageTotal = (int) Math.ceil(jo.getInt("total")/(double)jo.getInt("pageSize"));
			}
			   if(array.length() == 0){
			    	loading.setVisibility(View.VISIBLE);
			    	loading.showFailLoadidng("亲，您没有订单哟~");
			    }else{
			    	loading.setVisibility(View.GONE);
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
//		/**
//		 * 添加删除事件
//		 */
//		list.setOnItemLongClickListener(new OnItemLongClickListener(){
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View view,
//					int arg2, long arg3) {
//				   final HashMap<String ,Object> map = (HashMap<String ,Object>)view.getTag();
//	                int mapstatus =Integer.parseInt( map.get("status").toString()); 
//	                if(mapstatus != 1 && mapstatus != 3){
//	                	dialog = new MessageDialog(getActivity(),"是否删除该订单？"
//	    						,"",false ,new MassageListener(){
//
//	    							@Override
//	    							public void setCommitClick() {
//	    								delateOrder(Integer.parseInt(map.get("id").toString()));
//	    							}
//
//	    							@Override
//	    							public void setCancelClick() {
//	    								dialog.dismiss();
//	    							}
//	    					
//	    				});
//	    				dialog.show();
//	                }
//				return false;
//			}
//			
//		});
		list.getRefreshableView().setAdapter(adapter);
		}catch(Exception e){
			e.printStackTrace();
			loading.setVisibility(View.VISIBLE);
	    	loading.showFailLoadidng();
		}finally{
			list.onRefreshComplete();
		}
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
		NetUtil.requestStringData(SRL.Method.METHOD_GET_ORDER_LIST, TAG ,map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
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
    	try{
    			JSONObject jo = new JSONObject(data);
    			JSONArray array = jo.getJSONArray("rows");
    			pageNumber = jo.getInt("pageNumber");
    			if(jo.getInt("pageSize")>0){
    				pageTotal = jo.getInt("total")/jo.getInt("pageSize");
    				}
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
    		adapter.updateData(listdata);
    	}catch(Exception e){
    		e.printStackTrace();
    		AppUtil.ShowShortToast(getActivity(), "数据解析异常");
    	}finally{
    		if(list.isRefreshing()){
    			list.onRefreshComplete();
    		}
    	}
    }
}
