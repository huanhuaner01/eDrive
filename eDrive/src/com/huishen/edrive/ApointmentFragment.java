package com.huishen.edrive;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huishen.edrive.apointment.ApointmentActivity;
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
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 预约学车
 * @author zhanghuan
 *
 */
public class ApointmentFragment extends Fragment implements CalendarResult{
	private String TAG = "ApointmentFragment" ;

	private FragmentActivity activity ;
	private long coachId ;
	private String telnum ; 
	private TextView name ,carnum;
	private ImageButton tel ;
	private RoundImageView photo ;
	private int[] colors ;
	public ApointmentFragment(FragmentActivity activity) {
		this.activity = activity ;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.fragment_appointment, null);
		 registView(rootView);
		 initView();
	
		return rootView ;
	}
	
	/**
	 * 初始化
	 */
	private void initView() {
		getWebData();
	}

	/**
	 * 注册
	 */
	private void registView(View v) {
		name = (TextView) v.findViewById(R.id.f_appoint_coachname);
		carnum = (TextView)v.findViewById(R.id.f_appoint_carnum) ;
		tel = (ImageButton)v.findViewById(R.id.f_appoint_btn);
		photo = (RoundImageView)v.findViewById(R.id.f_appoint_photo);
	}

	/**
	 * 获得+6的日期
	 * @return 格式为yyyy-MM-dd的字符
	 */
	public String gettest(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,6); 
		Date date = cal.getTime() ;
		SimpleDateFormat sf = new SimpleDateFormat(CalendarUtil.DATE_FORMAT);
		return sf.format(date) ;
	}
	/**
	 * 获得今天的日期
	 * @return 格式为yyyy-MM-dd的字符
	 */
	public String getToday(){
		Calendar cal=Calendar.getInstance();
		Date date = cal.getTime() ;
		SimpleDateFormat sf = new SimpleDateFormat(CalendarUtil.DATE_FORMAT);
		return sf.format(date) ;
	}
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		initView();
	}

	@Override
	public void setResult(String day) {
		Log.i("apointmentFragment", "获取了时间") ;
		Intent i = new Intent(this.getActivity(),ApointmentActivity.class);
		this.getActivity().startActivity(i) ;
	}
	
	/**
	 * 获取数据
	 */
	private void getWebData(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId",Prefs.readString(getActivity(), Const.USER_COACH_ID));
		NetUtil.requestStringData(SRL.Method.METHOD_GET_APPOINT, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
//						setData();
					}
				},new DefaultErrorListener());
	}
	
	/**
	 * 修改界面显示
	 */
	private void setData(String result){
		try{
//			    NetUtil.requestLoadImage(photo, relativePath, R.drawable.photo_coach_defualt);
//			    name.setText(text);
//			    carnum.setText(text);
//			    telnum = ;
//			    callTel() ;
			    FragmentManager fm = activity.getSupportFragmentManager();  
		        FragmentTransaction tx = fm.beginTransaction();  
		        String begindate = getToday();
		        String enddate = gettest() ;
		        colors = new int[]{R.drawable.day_bg,R.drawable.day_bg,R.drawable.day_exists ,R.drawable.day_bg,
		        		R.drawable.day_bg,R.drawable.day_full,R.drawable.day_full
		        };	
		        CalendarFragment calendar = new CalendarFragment(this.activity ,(CalendarResult)this ,true,begindate,enddate,colors ) ;	
				tx.replace(R.id.appointment_calendar,calendar);  
		        tx.commit();
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 拨打电话号码
	 */
	private void callTel(){
		if(telnum == null || telnum.equals("")){
			AppUtil.ShowShortToast(activity, "对不起，此教练没有电话号码") ;
		}else{
		Uri uri = Uri.parse("tel:"+telnum);
		Intent it = new Intent(Intent.ACTION_CALL, uri);
		startActivity(it);
		}
	}

}
