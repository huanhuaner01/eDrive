package com.huishen.edrive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class AppointmentNoCoachFragment extends Fragment {

   private Button bindCoach ; //绑定教练按钮	

	public AppointmentNoCoachFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_appointment_notcoach, null);
		this.bindCoach = (Button)rootView.findViewById(R.id.f_appoint_btn);
		this.bindCoach.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
			}
			
		});
		return rootView;
	}

}
