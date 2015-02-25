package com.huishen.edrive.center;

import com.huishen.edrive.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class CoachFieldImgDialog extends Dialog {

	public CoachFieldImgDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CoachFieldImgDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public CoachFieldImgDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_post_lay);
        registView();
        init() ;
    }

	private void registView() {
		
	}

	private void init() {
		// TODO Auto-generated method stub
		
	}
}
