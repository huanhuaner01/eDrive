package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

/**
 * 消息列表
 * @author zhanghuan
 *
 */
public class MsgFragment extends TitleListFragment { 
	private String TAG = "MsgFragment" ;
	private ArrayList<Map<String ,Object>> listdata = new ArrayList<Map<String ,Object>>();
	private String[] from  = new String[]{"content"};
	private int[] to = new int[]{R.id.item_msg_content};
	private SimpleAdapter adapter ;
	private MessageDialog dialog ;
	public MsgFragment(Context context, String titlestr, String url) {
		super(context, titlestr, url);
	}

	public MsgFragment(Context context, Object tag, String titlestr, String url) {
		super(context, tag, titlestr, url);
	}

	@Override
	public void getWebData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("target", Prefs.readString(getActivity(), Const.USER_PHONE));
		NetUtil.requestStringData(SRL.Method.METHOD_GET_MSG_LIST, map,
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
		 * [{"pcontent":"bbbbbbbbbbbbbbbbbb"},{"pcontent":"aaaaaaaaaaaaaa"}]
		 */
		listdata.clear();
		if(Prefs.readString(getActivity(), Const.NEW_MSG).equals("1")){
			Prefs.writeString(getActivity(), Const.NEW_MSG,"0");
		}
		try{
			JSONArray json = new JSONArray(data);
		
			for(int i = 0 ;i < json.length() ; i++){
				HashMap<String ,Object> map = new HashMap<String ,Object>();
				map.put("content", json.getJSONObject(i).get("pcontent"));
				listdata.add(map);
			}
		    
//		for(int i = 0 ;i< 10 ;i++){
//			HashMap map = new HashMap<String ,Object>();
//			map.put("content", "教练抢到了");
//			map.put("id", i);
//			listdata.add(map);
//		}
		
		adapter = new SimpleAdapter(getActivity(), listdata, R.layout.item_msg_lay, from, to);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				dialog = new MessageDialog(getActivity(),"消息内容"
						,listdata.get(position).get("content").toString(),false ,new MassageListener(){

							@Override
							public void setCommitClick() {
								dialog.dismiss();
							}

							@Override
							public void setCancelClick() {
								// TODO Auto-generated method stub
								
							}
					
				});
				dialog.setCancelHide();
				dialog.show();
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
