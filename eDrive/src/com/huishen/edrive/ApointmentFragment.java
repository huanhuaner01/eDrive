package com.huishen.edrive;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huishen.edrive.apointment.ApointmentActivity;
import com.huishen.edrive.center.CoachDetailActivity;
import com.huishen.edrive.login.VerifyPhoneActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.BaseFragment;
import com.huishen.edrive.widget.CalendarFragment;
import com.huishen.edrive.widget.CalendarPagerFragment;
import com.huishen.edrive.widget.CalendarResult;
import com.huishen.edrive.widget.CalendarUtil;
import com.huishen.edrive.widget.RoundImageView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 预约学车
 * 
 * @author zhanghuan
 * 
 */
public class ApointmentFragment extends BaseFragment {

	private FragmentActivity activity;
	private int coachId;
	private String telnum;
	private TextView name, carnum;
	private ImageButton tel;
	private RoundImageView photo;
	private int[] colors;
	private String result;
	public static ApointmentFragment create(String result) {
		ApointmentFragment fragment = new ApointmentFragment();
		Bundle args = new Bundle();
		args.putString("DATA", result);
		fragment.setArguments(args);
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		result = getArguments().getString("DATA");
	}
    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	     try{  
	    	 this.activity =(FragmentActivity)activity;  
	      }catch(ClassCastException e){  
	          throw new ClassCastException(activity.toString()+"must implement FragmentActivity");  
	      } 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_appointment, null);
		this.setTag("ApointmentFragment"); //设置tag
		registView(rootView);
		initView();
        Log.i(TAG, "create apointmentFramgent");
		return rootView;
	}

	/**
	 * 初始化
	 */
	private void initView() {
//		setData(result);
    
	}

	/**
	 * 注册
	 */
	private void registView(View v) {
		name = (TextView) v.findViewById(R.id.f_appoint_coachname);
		carnum = (TextView) v.findViewById(R.id.f_appoint_carnum);
		tel = (ImageButton) v.findViewById(R.id.f_appoint_btn);
		photo = (RoundImageView) v.findViewById(R.id.f_appoint_photo);
	}

	/**
	 * 获得+6的日期
	 * 
	 * @return 格式为yyyy-MM-dd的字符
	 */
	public String gettest() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 6);
		Date date = cal.getTime();
		SimpleDateFormat sf = new SimpleDateFormat(CalendarUtil.DATE_FORMAT);
		return sf.format(date);
	}

	/**
	 * 获得今天的日期
	 * 
	 * @return 格式为yyyy-MM-dd的字符
	 */
	public String getToday() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		SimpleDateFormat sf = new SimpleDateFormat(CalendarUtil.DATE_FORMAT);
		return sf.format(date);
	}

	/**
	 * 获得+num的日期
	 * 
	 * @return 格式为yyyy-MM-dd的字符
	 */
	public String getDate(int num) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, num);
		Date date = cal.getTime();
		SimpleDateFormat sf = new SimpleDateFormat(CalendarUtil.DATE_FORMAT);
		return sf.format(date);
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
	
	
	/**
	 * 获取数据
	 */
	private void getWebData(){
//		if(Prefs.readString(this, Const.USER_COACH_ID).equals("")){
//			if(tabGroup.getCheckedRadioButtonId()==R.id.main_tab_appointment){
//			    tabGroup.check(R.id.main_tab_appointment);
//			}
//			else if(tabGroup.getCheckedRadioButtonId()!=R.id.main_tab_appointment && tabGroup.getCheckedRadioButtonId()!=R.id.main_tab_center){
//				 tabGroup.check(R.id.main_tab_appointment);
//			}
//			
//		}else{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId",Prefs.readString(this.getActivity(), Const.USER_COACH_ID));
		NetUtil.requestStringData(SRL.Method.METHOD_GET_APPOINT,TAG, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						setData(result);
					}
				},new DefaultErrorListener(this.getActivity()));
		
	}

	/**
	 * 修改界面显示
	 */
	private void setData(String result) {
		/**
		 * {"cohInfo":{"busNumber":"蜀K88888","coachName":"雷猴","path":
		 * "/attachment/coh-head/image/IMG_2015021415243200907519.jpg"
		 * ,"phone":"18200390901"} ,"listLessinfo":
		 * [{"coachId":21,"countlessinfo"
		 * :2,"kmcount":36,"lessonDate":"2015-03-10"},
		 * {"coachId":21,"countlessinfo"
		 * :1,"kmcount":36,"lessonDate":"2015-03-11"}],
		 * "stuLessonInfoStatus":[{"lessonDate":"2015-03-10","status":1},
		 * {"lessonDate"
		 * :"2015-03-11","status":0},{"lessonDate":"2015-03-20","status":1}]}
		 */

		if (result == null || result.equals("")) {
			AppUtil.ShowShortToast(getActivity(), "服务器异常");
			return;
		}
		try {
			JSONObject json = new JSONObject(result);
			JSONObject info = json.optJSONObject("cohInfo");
			JSONArray lessarray = json.optJSONArray("listLessinfo");
			JSONArray stuarray = json.optJSONArray("stuLessonInfoStatus");
			name.setText(info.optString("coachName", "暂无"));
			carnum.setText(info.optString("busNumber", "暂无"));
			telnum = info.optString("phone", "");
			if (!info.optString("path", "").equals("")) {
				NetUtil.requestLoadImage(photo, info.optString("path", ""),
						R.drawable.photo_coach_defualt);
			}
			photo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					intentCoachDetail();
				}
			});
			tel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					callTel();
				}

			});
			String begindate = getToday();
			String enddate = gettest();
			colors = new int[] { R.drawable.day_bg, R.drawable.day_bg,
					R.drawable.day_bg, R.drawable.day_bg, R.drawable.day_bg,
					R.drawable.day_bg, R.drawable.day_bg };

			// 设置色块信息
			for (int i = 0; i < 7; i++) {
				String day = getDate(i);
				if (lessarray != null) {
					for (int j = 0; j < lessarray.length(); j++) {
						JSONObject less = lessarray.getJSONObject(j);

						if (day.equals(less.optString("lessonDate", ""))) {
							if (less.optInt("countlessinfo", 0) == less.optInt(
									"kmcount", 0)) {
								Log.i(TAG, "有满课 i="+i);
								colors[i] = R.drawable.day_full;
							}
						}
					}
				}
				if (stuarray != null) {
					for (int m = 0; m < stuarray.length(); m++) {
						JSONObject stu = stuarray.getJSONObject(m);

						if (day.equals(stu.optString("lessonDate", ""))) {
							colors[i] = R.drawable.day_exists;
						}
					}
				}
			}
			
			for(int n = 0 ; n<colors.length ;n++){
				if(colors[n] == R.drawable.day_full){
					Log.i(TAG, "haode有满课");
				}
			}
			Log.i(TAG, "this is begin to create calendar");
			FragmentManager fm = activity.getSupportFragmentManager();
			FragmentTransaction tx = fm.beginTransaction();
			CalendarFragment calendar =CalendarFragment.create(true, begindate, enddate, colors);
			tx.replace(R.id.appointment_calendar, calendar);
			tx.commit();
		} catch (Exception e) {
          e.printStackTrace();
		}
	}

	/**
	 * 拨打电话号码
	 */
	private void callTel() {
		if (telnum == null || telnum.equals("")) {
			AppUtil.ShowShortToast(activity, "对不起，此教练没有电话号码");
		} else {
			Uri uri = Uri.parse("tel:" + telnum);
			Intent it = new Intent(Intent.ACTION_CALL, uri);
			startActivity(it);
		}
	}

	/**
	 * 启动教练详情页面
	 */
	private void intentCoachDetail() {
		Intent i = new Intent(this.getActivity(), CoachDetailActivity.class);
		coachId = Integer.parseInt(Prefs.readString(getActivity(), Const.USER_COACH_ID));
		
		i.putExtra(CoachDetailActivity.COACH_ID,coachId) ;
		this.activity.startActivity(i);
	}

}
