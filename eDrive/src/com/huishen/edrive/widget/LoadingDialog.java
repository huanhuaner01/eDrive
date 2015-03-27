package com.huishen.edrive.widget;

import com.huishen.edrive.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class LoadingDialog extends Dialog {

	public LoadingDialog(Context context) {
		super(context,R.style.dataselectstyle);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	public LoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        this.setContentView(R.layout.dailog_loading);
//	        registView();
//	        init() ;
	    }

}
