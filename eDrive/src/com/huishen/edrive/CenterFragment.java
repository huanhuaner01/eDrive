package com.huishen.edrive;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.center.ListActivity;
import com.huishen.edrive.center.ModifyUserInfoActivity;
import com.huishen.edrive.center.OrderActivity;
import com.huishen.edrive.center.SettingActivity;
import com.huishen.edrive.center.ShareActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.RoundImageView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CenterFragment extends Fragment implements View.OnClickListener{
	private String TAG = "CenterFragment" ;
    private RoundImageView photoimg; //学员头像
    
    private TextView tel ; //电话号码
    private LinearLayout userinfo ,order,msg ,bindCoach ,share ,setting; //
	public CenterFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_center, null);
		registView(rootView);
		initView();
		return rootView ;
	}

	/**
	 * 注册
	 */
	private void registView(View v){
		photoimg = (RoundImageView)v.findViewById(R.id.f_center_img_photo);
		tel = (TextView)v.findViewById(R.id.f_center_tv_tel);
		userinfo = (LinearLayout)v.findViewById(R.id.f_center_userinfo);
		order = (LinearLayout)v.findViewById(R.id.f_center_order);
		msg =(LinearLayout)v.findViewById(R.id.f_center_msg);
		bindCoach =(LinearLayout)v.findViewById(R.id.f_center_bindcoach);
		share =(LinearLayout)v.findViewById(R.id.f_center_share);
		setting =(LinearLayout)v.findViewById(R.id.f_center_setting);
	}
	
	/**
	 * 初始化
	 */
	private void initView(){
		userinfo.setOnClickListener(this);
		msg.setOnClickListener(this);
		order.setOnClickListener(this);
//		bindCoach.setOnClickListener(this);
		share.setOnClickListener(this);
		setting.setOnClickListener(this);
		tel.setText(Prefs.readString(getActivity(), Const.USER_PHONE));
		getWebData();
	}
	
	/**
	 * 获取网络数据
	 */
	private void getWebData(){
		HashMap<String, String> map = new HashMap<String, String>();
		NetUtil.requestStringData(SRL.Method.METHOD_GET_CENTER_INFO, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if(result == null || result.equals("")){
							AppUtil.ShowShortToast(getActivity(), "服务器繁忙");
						}else{
							setData(result);
						}
						
					}
				},new DefaultErrorListener());
	}
	private void setData(String result){
		/**
		 * {"address":"","baseId":105,
		 * "path":"/attachment/head/thum/stuPic.jpg",
		 * "phone":"18384296843","picid":1,"stuName":"18384296843","stuid":34}
		 */
		try{
			JSONObject json = new JSONObject(result);
			if(!json.optString("path", "").equals("")){
		       NetUtil.requestLoadImage(photoimg, json.optString("path", ""), R.drawable.photo_coach_defualt);
			}
            tel.setText(json.optString("phone", "缺失"));
		}catch(Exception e){
			
		}

	}
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		getWebData();
	}

	@Override
	public void onClick(View v) {
		Intent i =null;
		switch(v.getId()){
		case R.id.f_center_userinfo:
			i = new Intent(this.getActivity(),ModifyUserInfoActivity.class);
			break ;
		case R.id.f_center_msg:
			i = new Intent(this.getActivity(),ListActivity.class);
			i.putExtra(ListActivity.STATUS_KEY,ListActivity.STATUS_MSGLIST);
			break;
		case R.id.f_center_order:
			i = new Intent(this.getActivity(),OrderActivity.class);
			break;
		case R.id.f_center_bindcoach:
//			i = new Intent(this.getActivity(),ModifyUserInfoActivity.class);
			break ;
		case R.id.f_center_share:
			i = new Intent(this.getActivity(),ShareActivity.class);
			break;
		case R.id.f_center_setting:
			i = new Intent(this.getActivity(),SettingActivity.class);
			break;
		}
		startActivity(i);
	}

}
