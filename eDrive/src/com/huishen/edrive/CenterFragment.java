package com.huishen.edrive;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.apointment.BindCoachActivity;
import com.huishen.edrive.center.CoachDetailActivity;
import com.huishen.edrive.center.ListActivity;
import com.huishen.edrive.center.ModifyUserInfoActivity;
import com.huishen.edrive.center.SettingActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.BaseFragment;
import com.huishen.edrive.widget.RoundImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CenterFragment extends BaseFragment implements View.OnClickListener{
    private RoundImageView photoimg; //学员头像
    
    private TextView tel ; //电话号码
    private LinearLayout userinfo ,order,msg ,bindCoach ,share ,setting; //
    private int coachId = -1 ;
	public CenterFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_center, null);
		this.setTag("CenterFragment");
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
		bindCoach.setOnClickListener(this);
		share.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
						Intent sendIntent = new Intent();
						sendIntent.setAction(Intent.ACTION_SEND);
						sendIntent.putExtra(Intent.EXTRA_TEXT, "推荐一个学车新玩法，下载安装即送学车报名抵用卷1000+200元，点击即可下载http://bind.ejxc.com.cn:8083/downloads/stu.html");
						sendIntent.setType("text/plain");
						startActivity(Intent.createChooser(sendIntent, "选择接收者"));
					
			}
			
		});
		setting.setOnClickListener(this);
		tel.setText(Prefs.readString(getActivity(), Const.USER_PHONE));
	}
	
	/**
	 * 获取网络数据
	 */
	private void getWebData(){
	
		HashMap<String, String> map = new HashMap<String, String>();
		NetUtil.requestStringData(SRL.Method.METHOD_GET_CENTER_INFO,TAG , map,
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
				},new DefaultErrorListener(this.getActivity()));
	}
	private void setData(String result){
		/**
		 * {"address":"","baseId":105,
		 * "path":"/attachment/head/thum/stuPic.jpg",
		 * "phone":"18384296843","picid":1,"stuName":"18384296843","stuid":34}
		 */
		try{
			if(this.getActivity().isFinishing()){
				return ;
			}
			JSONObject json = new JSONObject(result);
			if(!json.optString("path", "").equals("")){
		       NetUtil.requestLoadImage(photoimg, json.optString("path", ""), R.drawable.photo_coach_defualt);
			}
			coachId = json.optInt("coachId", -1);
			if(coachId != -1){
				Prefs.writeString(getActivity(), Const.USER_COACH_ID, coachId+"");
			}else{
				Prefs.writeString(getActivity(), Const.USER_COACH_ID, "");
			}
            tel.setText(json.optString("phone", "缺失"));
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		getWebData();
		super.onResume();
		
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
			i = new Intent(this.getActivity(),ListActivity.class);
			i.putExtra(ListActivity.STATUS_KEY,ListActivity.STATUS_ORDERLIST);
			break;
		case R.id.f_center_bindcoach:
			if(coachId != -1){
			i = new Intent(this.getActivity(),CoachDetailActivity.class);
			i.putExtra("tag", 1);
			i.putExtra("id", coachId);
			}else{
				i = new Intent(this.getActivity(),BindCoachActivity.class);
			}
			break ;
		case R.id.f_center_setting:
			i = new Intent(this.getActivity(),SettingActivity.class);
			break;
		}
		startActivity(i);
	}

}
