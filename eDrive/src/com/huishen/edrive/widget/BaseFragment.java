package com.huishen.edrive.widget;
import com.huishen.edrive.net.NetUtil;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    public String TAG = "BaseFragment" ;
	
	public void setTag(String tag){
		this.TAG = tag ;
	}
	@Override
	public void onDestroy() {
		NetUtil.cancelRequest(TAG);
		super.onDestroy();
	}
	
    
}
