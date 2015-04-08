package com.huishen.edrive.widget;


import com.huishen.edrive.net.NetUtil;
import com.tencent.stat.StatService;

import android.app.Activity;

public class BaseActivity extends Activity {
	public String TAG = "BaseActivity";
    @Override
    protected void onResume() {
        super.onResume();
        // 页面开始
        StatService.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 页面结束
        StatService.onPause(this);
    }
    
    public void setTag(String tag){
    	TAG = tag ;
    }
	@Override
	protected void onDestroy() {
		NetUtil.cancelRequest(TAG);
		super.onDestroy();
	}
    

}
