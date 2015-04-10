package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.huishen.edrive.R;
import com.huishen.edrive.apointment.MassageListener;
import com.huishen.edrive.apointment.MessageDialog;
import com.huishen.edrive.db.AppMessage;
import com.huishen.edrive.db.MeaasgeDbManager;
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
	private ArrayList<Map<String ,Object>> listdata = new ArrayList<Map<String ,Object>>();
	private String[] from  = new String[]{"content","time" ,"title"};
	private int[] to = new int[]{R.id.item_msg_content ,R.id.item_msg_time,R.id.item_msg_title};
	private MsgAdapter adapter ;
	public MsgFragment(Context context, String titlestr, String url) {
		super(context, titlestr, url);
		this.setTag("MsgFragment");
	}

	public MsgFragment(Context context, Object tag, String titlestr, String url) {
		super(context, tag, titlestr, url);
		this.setTag("MsgFragment");
	}

	@Override
	public void getWebData() {
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("target", Prefs.readString(getActivity(), Const.USER_PHONE));
//		NetUtil.requestStringData(SRL.Method.METHOD_GET_MSG_LIST,TAG, map,
//				new Response.Listener<String>() {
//                       
//					@Override
//					public void onResponse(String result) {
//						Log.i(TAG, result);
//						if(result == null || result.equals("")){
//							AppUtil.ShowShortToast(getActivity(), "服务器繁忙");
//						}else{
//							setList(result , list);
//						}
//						
//					}
//				},new DefaultErrorListener(this.getActivity(),null,loading ,mSwipeLayout));
//		setList("" , list);
		this.mSwipeLayout.setEnabled(false);
		getMsg();
	}
     
	/**
	 * 从数据库获取数据
	 */
	private void getMsg(){
		
		if(Prefs.readString(getActivity(), Const.NEW_MSG).equals("1")){
			Prefs.writeString(getActivity(), Const.NEW_MSG,"0");
		}
		MeaasgeDbManager db = new MeaasgeDbManager(this.getActivity());
		ArrayList<AppMessage> lists = db.getAllMessage();
		listdata.clear() ;
		
		for(int i = 0 ;i<lists.size() ;i++){
			HashMap<String ,Object> map = new HashMap<String ,Object>();
			map.put("content", lists.get(i).getContent());
			map.put("time", lists.get(i).getTime());
			map.put("title", lists.get(i).getTitle());
			map.put("path", lists.get(i).getIconPath());
			map.put("id", lists.get(i).getId());
//			Log.i(TAG, "msg's content is :"+lists.get(i).toString());
			listdata.add(map);
		}
		adapter = new MsgAdapter(getActivity(), listdata, R.layout.item_msg_lay, from, to);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
				  Log.i(TAG, "id is "+listdata.get(position).get("id").toString());
                  Intent i = new Intent(getActivity() ,MsgDetailActivity.class);
                  i.putExtra("id", Integer.parseInt(listdata.get(position).get("id").toString()));
                  getActivity().startActivity(i);
			}
		});
		list.setAdapter(adapter);  
		
		if(this.mSwipeLayout.isRefreshing()){
			mSwipeLayout.setRefreshing(false);
		}
		if(loading.getVisibility() == View.VISIBLE){
			loading.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void setList(String data, ListView list) {
		/**
		 * {"total":1,"pageSize":10,"conditions":{"target":"18384296843"},"pageNumber":1,"rows":[{"pcontent":"你好，你已成功绑定了教练，快去预约练车吧"}]}
		 */
//		getMsg();
//		
//		if(loading.getVisibility() == View.VISIBLE){
//			loading.setVisibility(View.GONE);
//		}
//		listdata.clear();
//		if(Prefs.readString(getActivity(), Const.NEW_MSG).equals("1")){
//			Prefs.writeString(getActivity(), Const.NEW_MSG,"0");
//		}
//		try{
//			JSONArray json = new JSONArray(data);
//			Log.i(TAG, json.length()+" 行数据");
//		    if(json.length() == 0){
//		    	loading.setVisibility(View.VISIBLE);
//		    	loading.showFailLoadidng("亲，您没有消息哟~~");
//		    }else{
//			for(int i = 0 ;i < json.length() ; i++){
//				HashMap<String ,Object> map = new HashMap<String ,Object>();
//				map.put("content", json.getJSONObject(i).get("pcontent"));
//				listdata.add(map);
//			}
//		  
//		
//		adapter = new SimpleAdapter(getActivity(), listdata, R.layout.item_msg_lay, from, to);
//		list.setOnItemClickListener(new OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//						long arg3) {
//				dialog = new MessageDialog(getActivity(),"消息内容"
//						,listdata.get(position).get("content").toString(),false ,new MassageListener(){
//
//							@Override
//							public void setCommitClick() {
//								dialog.dismiss();
//							}
//                            
//							@Override
//							public void setCancelClick() {
//								
//							}
//					
//				});
//				dialog.setCancelHide();
//				dialog.show();
//			}
//			
//		});
//		list.setAdapter(adapter);
//		    }
//		}catch(Exception e){
//			e.printStackTrace();
//			loading.setVisibility(View.VISIBLE);
//	    	loading.showFailLoadidng();
//		}finally{
//		if(this.mSwipeLayout.isRefreshing()){
//			mSwipeLayout.setRefreshing(false);
//		}
//		}
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
	/**
	 * 消息列表适配器
	 * @author zhanghuan
	 *
	 */
	class MsgAdapter extends SimpleAdapter{
		private ImageLoader imageLoader ;
		public MsgAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			imageLoader = com.huishen.edrive.util.AppController.getInstance().getImageLoader();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View root = super.getView(position, convertView, parent);
			NetworkImageView img = (NetworkImageView)root.findViewById(R.id.item_msg_icon);
			if(listdata.get(position).get("path")!=null&&!listdata.get(position).get("path").toString().equals("")){
				img.setVisibility(View.VISIBLE);
			    img.setDefaultImageResId(R.drawable.ic_defualt_image);
			    img.setErrorImageResId(R.drawable.ic_error_image);
			    img.setImageUrl(listdata.get(position).get("path").toString(), imageLoader);
			}else{
				img.setVisibility(View.GONE);
			}
			return root ;
		}		
	}
}
