package com.huishen.edrive;

import com.huishen.edrive.apointment.BindCoachActivity;
import com.huishen.edrive.widget.BaseFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 未绑定教练预约界面显示（提醒用户绑定教练）
 * @author Administrator
 *
 */
public class AppointmentNoCoachFragment extends BaseFragment {

   private Button bindCoach ; //绑定教练按钮	

	public AppointmentNoCoachFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_appointment_notcoach, null);
		this.setTag("AppointmentNoCoachFragment");
		this.bindCoach = (Button)rootView.findViewById(R.id.f_appoint_btn);
		this.bindCoach.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(),BindCoachActivity.class);
				getActivity().startActivity(i);
			}
			
		});
		return rootView;
	}

}
