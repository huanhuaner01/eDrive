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
import com.huishen.edrive.widget.CalendarFragment;
import com.huishen.edrive.widget.CalendarResult;
import com.huishen.edrive.widget.CalendarUtil;
import com.huishen.edrive.widget.RoundImageView;

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
public class ApointmentFragment extends Fragment implements CalendarResult {
	private String TAG = "ApointmentFragment";

	private FragmentActivity activity;
	private int coachId;
	private String telnum;
	private TextView name, carnum;
	private ImageButton tel;
	private RoundImageView photo;
	private int[] colors;
	private String result;

	public ApointmentFragment(FragmentActivity activity, String result) {
		this.activity = activity;
		this.result = result;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_appointment, null);
		registView(rootView);
		initView();

		return rootView;
	}

	/**
	 * 初始化
	 */
	private void initView() {
		setData(result);

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
	}

	@Override
	public void setResult(String day) {
		Log.i("apointmentFragment", "获取了时间"+day);
		Intent i = new Intent(this.getActivity(), ApointmentActivity.class);
		i.putExtra("lessonDate", day);
		this.getActivity().startActivity(i);
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
			FragmentManager fm = activity.getSupportFragmentManager();
			FragmentTransaction tx = fm.beginTransaction();
			CalendarFragment calendar = new CalendarFragment(this.activity,
					(CalendarResult) this, true, begindate, enddate, colors);
			tx.replace(R.id.appointment_calendar, calendar);
			tx.commit();
		} catch (Exception e) {

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
