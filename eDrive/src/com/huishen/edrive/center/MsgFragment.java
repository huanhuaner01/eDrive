package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.huishen.edrive.R;
import com.huishen.edrive.db.AppMessage;
import com.huishen.edrive.db.MeaasgeDbManager;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.TitleListFragment;

/**
 * 消息列表
 * 
 * @author zhanghuan
 * 
 */
public class MsgFragment extends TitleListFragment {
	private ArrayList<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();
	private String[] from = new String[] { "content", "time", "title" };
	private int[] to = new int[] { R.id.item_msg_content, R.id.item_msg_time,
			R.id.item_msg_title };
	private MsgAdapter adapter;
	private Dialog canceldialog;

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
		// HashMap<String, String> map = new HashMap<String, String>();
		// map.put("target", Prefs.readString(getActivity(), Const.USER_PHONE));
		// NetUtil.requestStringData(SRL.Method.METHOD_GET_MSG_LIST,TAG, map,
		// new Response.Listener<String>() {
		//
		// @Override
		// public void onResponse(String result) {
		// Log.i(TAG, result);
		// if(result == null || result.equals("")){
		// AppUtil.ShowShortToast(getActivity(), "服务器繁忙");
		// }else{
		// setList(result , list);
		// }
		//
		// }
		// },new DefaultErrorListener(this.getActivity(),null,loading
		// ,mSwipeLayout));
		// setList("" , list);
		this.mSwipeLayout.setEnabled(false);
		getMsg();
	}

	/**
	 * 从数据库获取数据
	 */
	private void getMsg() {

		if (Prefs.readString(getActivity(), Const.NEW_MSG).equals("1")) {
			Prefs.writeString(getActivity(), Const.NEW_MSG, "0");
		}
		MeaasgeDbManager db = new MeaasgeDbManager(this.getActivity());
		ArrayList<AppMessage> lists = db.getAllMessage();
		listdata.clear();

		for (int i = 0; i < lists.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("content", lists.get(i).getContent());
			map.put("time", lists.get(i).getTime());
			map.put("title", lists.get(i).getTitle());
			map.put("path", lists.get(i).getIconPath());
			map.put("id", lists.get(i).getId());
			// Log.i(TAG, "msg's content is :"+lists.get(i).toString());
			listdata.add(map);
		}

		setList("", list);
		if (this.mSwipeLayout.isRefreshing()) {
			mSwipeLayout.setRefreshing(false);
		}
		if (loading.getVisibility() == View.VISIBLE) {
			loading.setVisibility(View.GONE);
		}
	}

	@Override
	public void setList(String data, ListView list) {
		adapter = new MsgAdapter(getActivity(), listdata,
				R.layout.item_msg_lay, from, to);

		// 单击事件
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Log.i(TAG, "id is "
						+ listdata.get(position).get("id").toString());
				Intent i = new Intent(getActivity(), MsgDetailActivity.class);
				i.putExtra(
						"id",
						Integer.parseInt(listdata.get(position).get("id")
								.toString()));
				getActivity().startActivity(i);
			}
		});
		// 长按事件
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Log.i(TAG, "长按事件");
				final int id = Integer.parseInt(arg1.getTag().toString());
				final int index = position;
				canceldialog = new AlertDialog.Builder(getActivity())
						.setTitle("删除选项")
						.setMessage("是否删除该消息吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										deteleMsg(id, index); // 删除消息
									}
								}).setNegativeButton("取消", null).show();

				return true;
			}

		});
		list.setAdapter(adapter);
	}

	/**
	 * 删除一条消息
	 * 
	 * @param id
	 *            需要删除的消息id
	 * @param position
	 *            item所在列表位置
	 */
	private void deteleMsg(int id, int position) {
		MeaasgeDbManager db = new MeaasgeDbManager(this.getActivity());
		db.deleteMessages(id);
		listdata.remove(position);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void setList(String data, ExpandableListView list) {

	}

	@Override
	public void setBack(ImageButton back2) {
		back2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}

		});
	}

	/**
	 * 消息列表适配器
	 * 
	 * @author zhanghuan
	 * 
	 */
	class MsgAdapter extends SimpleAdapter {
		private ImageLoader imageLoader;

		public MsgAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			imageLoader = com.huishen.edrive.util.AppController.getInstance()
					.getImageLoader();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View root = super.getView(position, convertView, parent);
			NetworkImageView img = (NetworkImageView) root
					.findViewById(R.id.item_msg_icon);
			if (listdata.get(position).get("path") != null
					&& !listdata.get(position).get("path").toString()
							.equals("")) {
				img.setVisibility(View.VISIBLE);
				img.setDefaultImageResId(R.drawable.ic_defualt_image);
				img.setErrorImageResId(R.drawable.ic_error_image);
				img.setImageUrl(
						NetUtil.getAbsolutePath(listdata.get(position)
								.get("path").toString()), imageLoader);
			} else {
				img.setVisibility(View.GONE);
			}
			root.setTag(listdata.get(position).get("id"));
			return root;
		}
	}
}
