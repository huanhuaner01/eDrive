package com.huishen.edrive.apointment;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.SplashActivity;
import com.huishen.edrive.center.CoachDetailActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.BaseActivity;
import com.huishen.edrive.widget.CalendarUtil;
import com.huishen.edrive.widget.LoadingDialog;
import com.huishen.edrive.widget.RoundImageView;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

public class ApointmentActivity extends BaseActivity {
	private String  telstr;
	private ExpandableListView list;
	private TextView title, coachname, carnum, time, week;
	private ImageButton back, tel;
	private RoundImageView photo;
	private String[] sub = new String[] { "科目一", "科目二", "科目三", "科目四" };
	private String[] classtime = new String[] { "上午：07:00—12:00", "下午：13:00—18:00", "晚上：19:00—22:00" };
	private String[] limit = new String[] { "km2m", "km2a", "km2e", "km3m",
			"km3a", "km3e" };
	private AppointmentSubExListApdater adapter;
	private ArrayList<HashMap<String, String>> mGroupData = null;
	private ArrayList<ArrayList<HashMap<String, String>>> mData = null;

	private MessageDialog dialog;
    private LoadingDialog loading ;
	private String date;
    
	private int crrenNum = 0;
	/***************************腾讯统计相关框架*************************************/
    //继承BaseActivity 自动集成腾讯统计
	/***************************腾讯统计基本框架结束*************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apointment);
		AppController.getInstance().addActivity(this);
		
		// ---------------------获取数据----------------------
		date = this.getIntent().getStringExtra("lessonDate");

		// ---------------------获取数据结束！-------------------------
		this.setTag("ApointmentActivity");
		registView();
		init();
	}

	private void registView() {
		list = (ExpandableListView) findViewById(R.id.appoint_expandablelist);
		title = (TextView) findViewById(R.id.header_title);
		back = (ImageButton) findViewById(R.id.header_back);
		tel = (ImageButton) findViewById(R.id.appoint_coach_img_tel);
		coachname = (TextView) findViewById(R.id.appoint_coach_tv_name);
		carnum = (TextView) findViewById(R.id.appoint_coach_tv_carnum);
		time = (TextView) findViewById(R.id.appoint_tv_time);
		week = (TextView) findViewById(R.id.appoint_tv_week);
		photo = (RoundImageView) findViewById(R.id.appoint_coach_img_photo);
	}

	private void init() {
		this.title.setText("预约管理");
		loading = new LoadingDialog(this);
		this.back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		this.week.setText(CalendarUtil.getDayWeek(date) + "");
		this.time.setText(date);
		this.tel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				callTel();
			}

		});
		this.photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				intentCoachDetail();
			}

		});
		// -------添加约课数据-------
		mGroupData = new ArrayList<HashMap<String, String>>();
		mData = new ArrayList<ArrayList<HashMap<String, String>>>();
//		for (int i = 1; i < 3; i++) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("subName", sub[i]);
//			map.put("subStatus", "0/3");
//			mGroupData.add(map);
//			ArrayList<HashMap<String, String>> prms = new ArrayList<HashMap<String, String>>();
//			for (int j = 0; j < 3; j++) {
//				HashMap<String, String> pmap = new HashMap<String, String>();
//				pmap.put("className", classtime[j]);
//				pmap.put("classStatus", "0/0");
//				pmap.put("code", 0 + "");
//				prms.add(pmap);
//			}
//			mData.add(prms);
//		}
//		adapter = new AppointmentSubExListApdater(this, mGroupData, mData, null);
//		list.setAdapter(adapter);
//		if (adapter.getGroupCount() >= 0) {
//			list.expandGroup(0);
//		}
		getData();

	}

	private void getData() {
        if(!isFinishing()&&!loading.isShowing()){
        	loading.show();
        }
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId", Prefs.readString(this, Const.USER_COACH_ID));
		map.put("lessonDate", date);
		NetUtil.requestStringData(SRL.Method.METHOD_GET_SUBJECT,TAG , map,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if(loading.isShowing()){
						   loading.dismiss();
						}
						if (result == null || result.equals("")) {
							AppUtil.ShowShortToast(ApointmentActivity.this,
									"服务器繁忙");
						} else {
							setData(result);
						}
					}
				}, new DefaultErrorListener(this ,loading));
	}

	/**
	 * 设置数据
	 * 
	 * @param result
	 */
	private void setData(String result) {
		/**
		 * {"cohInfo":{"busNumber":"蜀K88888","coachName":"雷猴","path":
		 * "/attachment/coh-head/image/IMG_2015021415243200907519.jpg"
		 * ,"phone":"18200390901"}, "less":{"lessonCount":
		 * [{"count":0,"lessonTime":1,"subject":1},
		 * {"count":0,"lessonTime":1,"subject":2},
		 * {"count":0,"lessonTime":2,"subject":1},
		 * {"count":0,"lessonTime":2,"subject":2},
		 * {"count":0,"lessonTime":3,"subject":1},
		 * {"count":0,"lessonTime":3,"subject":2}],
		 * "lessonLimit":{"km2a":1,"km2e"
		 * :0,"km2m":1,"km3a":0,"km3e":0,"km3m":0}} ,"stuLessonInfo":
		 * [{"lessonDate":"2015-03-08","lessonTime":1,"subject":1}] }
		 */
		try {
			JSONObject json = new JSONObject(result);
			JSONObject coachinfo = json.getJSONObject("cohInfo");
			JSONObject less = json.getJSONObject("less");
			JSONArray lessonCount = less.getJSONArray("lessonCount");
			JSONObject lessonLimit = less.getJSONObject("lessonLimit");
			JSONArray stuLessonInfo = json.getJSONArray("stuLessonInfo");
			if (!coachinfo.optString("path", "").equals("")) {
				NetUtil.requestLoadImage(photo,
						coachinfo.optString("path", ""),
						R.drawable.photo_coach_defualt);
			}
			coachname.setText(coachinfo.optString("coachName", "暂无"));
			carnum.setText(coachinfo.optString("busNumber", "暂无"));
			telstr = coachinfo.optString("phone", "");
			mGroupData.clear();
			mData.clear();
			for (int i = 1; i < 3; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("subName", sub[i]);
				int total = 0; // 上限
				int count = 0; // 已约总数
				//{"km2a":2,"km3a":1,"km2m":5,"km2e":2,"km3m":1,"km3e":2}
				if (i == 1) {
					total = lessonLimit.optInt("km2a", 0)
							+ lessonLimit.optInt("km2e", 0)
							+ lessonLimit.optInt("km2m", 0);
				} else {
					total = lessonLimit.optInt("km3a", 0)
							+ lessonLimit.optInt("km3m", 0)
							+ lessonLimit.optInt("km3e", 0);
					Log.i(TAG, "km3a:"+lessonLimit.optInt("km3a", 0));
					Log.i(TAG, "km3m:"+lessonLimit.optInt("km3m", 0));
					Log.i(TAG, "km3e:"+lessonLimit.optInt("km3e", 0));
				}
				Log.i(TAG, "lessonLimit:"+lessonLimit.toString());
				Log.i(TAG, "total:"+total);
				ArrayList<HashMap<String, String>> prms = new ArrayList<HashMap<String, String>>();
				for (int j = 0; j < lessonCount.length(); j++) {
					JSONObject subclass = lessonCount.getJSONObject(j);

					if (subclass.getInt("subject") == i) {
						HashMap<String, String> pmap = new HashMap<String, String>();
						pmap.put("className",
								classtime[subclass.getInt("lessonTime") - 1]);
						int classtotal = 0 ;
						if(i==1){
						classtotal = lessonLimit.getInt(limit[i
								* subclass.getInt("lessonTime") - 1]);
						}else{
						classtotal = lessonLimit.getInt(limit[i+subclass.getInt("lessonTime")]);	
						}
						Log.i(TAG, "classtotal's num "+(i
								* subclass.getInt("lessonTime") - 1));
						Log.i(TAG, "classtotal "+classtotal);
						if (classtotal == subclass.optInt("count" ,0)) {
							pmap.put("code", 2 + "");
						} else {
							pmap.put("code", 0 + "");
						}
						crrenNum = stuLessonInfo.length() ;
						for (int n = 0; n < stuLessonInfo.length(); n++) {
							JSONObject stuclass = stuLessonInfo
									.getJSONObject(n);
							if (stuclass.getInt("subject") == subclass
									.getInt("subject")
									&& stuclass.getInt("lessonTime") == subclass
											.getInt("lessonTime")) {
								pmap.put("code", 1 + "");
							}
						}
						pmap.put("classStatus", subclass.optInt("count",0) + "/"
								+ classtotal);
						pmap.put("lessonTime", subclass.getInt("lessonTime")
								+ "");
						pmap.put("subject", subclass.getInt("subject") + "");
						prms.add(pmap);
						//
						count = count + subclass.optInt("count" ,0);
					}
				}
				map.put("subStatus", count + "/" + total);
				mGroupData.add(map);
				mData.add(prms);
			}
			adapter = new AppointmentSubExListApdater(this, mGroupData, mData,
					listener);
			list.setAdapter(adapter);
			if (adapter.getGroupCount() >= 0) {
				list.expandGroup(0);
				list.expandGroup(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private TimeClassApointListener listener = new TimeClassApointListener() {

		@Override
		public void setOnClick(int subject, int lessionDate, int code) {
			Log.i(TAG, "subject:" + subject + " lessionDate:" + lessionDate
					+ " code:" + code);
			if (code == 0) {
				actionAppoint(subject, lessionDate);
			} else {
				actionCancel(subject, lessionDate);
			}
		}

	};

	private void actionAppoint(final int subject, final int lessionDate) {
        if(crrenNum >0){
        	AppUtil.ShowShortToast(getApplicationContext(), "对不起，一天只能预约一节课");
        	return ;
        }
		dialog = new MessageDialog(this, "您确定要预约以下时间吗？", "练习项目：" + sub[subject]
				+ "\n" + "练车时间：" + date + " " + classtime[lessionDate - 1],
				false, new MassageListener() {

					@Override
					public void setCommitClick() {
						sendAppoint(subject, lessionDate);
					}

					@Override
					public void setCancelClick() {

					}

				});
		dialog.show();
	}

	/**
	 * 取消按钮响应
	 */
	private void actionCancel(final int subject, final int lessionDate) {
		dialog = new MessageDialog(this, "您确定取消预约的时间吗？", "", true,
				new MassageListener() {

					@Override
					public void setCommitClick() {
						cancelAppoint(subject, lessionDate);
					}

					@Override
					public void setCancelClick() {
					}

				});
		dialog.show();
	}

	/**
	 * 约课课程
	 * 
	 * @param subject
	 * @param lessionDate
	 */
	private void sendAppoint(int subject, int lessionDate) {
		if(!isFinishing() && !loading.isShowing()){
			loading.show();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId", Prefs.readString(this, Const.USER_COACH_ID));
		map.put("lessonDate", date);
		map.put("lessonTime", lessionDate + "");
		map.put("subject", subject + "");
		NetUtil.requestStringData(SRL.Method.METHOD_SEND_APPOINT,TAG , map,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if(loading.isShowing()){
							loading.dismiss();
						}
						if (result == null || result.equals("")) {
							AppUtil.ShowShortToast(ApointmentActivity.this,
									"服务器繁忙");
						} else {
							try {
								JSONObject json = new JSONObject(result);
								if (json.getInt("status") == 1) {
									AppUtil.ShowShortToast(
											getApplicationContext(), "预约成功");
									getData();
								} else {
									AppUtil.ShowShortToast(
											getApplicationContext(), "失败");
								}
							} catch (Exception e) {
								AppUtil.ShowShortToast(ApointmentActivity.this,
										"服务器繁忙");
							}
							
						}
					}
				}, new DefaultErrorListener(this ,loading));
	}

	/**
	 * 取消预约
	 */
	private void cancelAppoint(int subject, int lessionDate) {
		if(!isFinishing() && !loading.isShowing()){
			loading.show();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId", Prefs.readString(this, Const.USER_COACH_ID));
		map.put("lessonDate", date);
		map.put("lessonTime", lessionDate + "");
		map.put("subject", subject + "");
		NetUtil.requestStringData(SRL.Method.METHOD_CANCEL_APPOINT,TAG, map,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if(loading.isShowing()){
							loading.dismiss();
						}
						if (result == null || result.equals("")) {
							AppUtil.ShowShortToast(ApointmentActivity.this,
									"服务器繁忙");
						} else {
							try {
								JSONObject json = new JSONObject(result);
								if (json.getInt("status") == 1) {
									AppUtil.ShowShortToast(
											getApplicationContext(), "取消成功");
									getData();
								} else {
									AppUtil.ShowShortToast(
											getApplicationContext(), "失败");
								}
							} catch (Exception e) {

							}
						}
					}
				}, new DefaultErrorListener(this ,loading));
	}

	/**
	 * 拨打电话号码
	 */
	private void callTel() {
		if (telstr == null || telstr.equals("")) {
			AppUtil.ShowShortToast(this, "对不起，此教练没有电话号码");
		} else {
			Uri uri = Uri.parse("tel:" + telstr);
			Intent it = new Intent(Intent.ACTION_CALL, uri);
			startActivity(it);
		}
	}

	/**
	 * 启动教练详情页面
	 */
	private void intentCoachDetail() {
		Intent i = new Intent(this, CoachDetailActivity.class);
		int coachId = Integer.parseInt(Prefs.readString(this,
				Const.USER_COACH_ID));

		i.putExtra(CoachDetailActivity.COACH_ID, coachId);
		startActivity(i);
	}

}
