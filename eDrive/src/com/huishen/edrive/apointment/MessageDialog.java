package com.huishen.edrive.apointment;

import com.huishen.edrive.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义消息弹出框
 * @author zhanghuan
 *
 */
public class MessageDialog extends Dialog implements View.OnClickListener{
    private MassageListener msgListener ;
    private String title , content ;
    private boolean isShowImg ,hideCancel=false;
    private TextView titleTv ,contentTv ;
    private Button commit  ,cancel;
    private LinearLayout tiplay ;
	public MessageDialog(Context context,String title ,String content ,boolean showImg ,MassageListener msgListener) {
		super(context,R.style.dataselectstyle);
		this.title = title ;
		this.content = content ;
		this.isShowImg = showImg ;
		this.msgListener = msgListener ;
		
		
	}

	public MessageDialog(Context context, int theme) {
		super(context,R.style.dataselectstyle);
	}

	public MessageDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context,R.style.dataselectstyle);
	}
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        this.setContentView(R.layout.dialog_message);
	        registView();
	        init() ;
	    }
    
	private void registView() {
		titleTv = (TextView)findViewById(R.id.dialog_msg_title);
		contentTv = (TextView)findViewById(R.id.order_detail_content);
		commit = (Button)findViewById(R.id.dialog_msg_commit);
		cancel = (Button)findViewById(R.id.dialog_msg_cancel);
		tiplay = (LinearLayout)findViewById(R.id.dialog_msg_img);
	}

	/**
	 * 隐藏取消按钮
	 */
	public void setCancelHide(){
		
		hideCancel = true ;
	}
	
	private void init() {
		if(!isShowImg){
			tiplay.setVisibility(View.GONE);
			
		}
		titleTv.setText(title);
		contentTv.setText(content);
		 if(msgListener != null){
			 commit.setOnClickListener(this) ;
			 cancel.setOnClickListener(this) ;
		 }
		 if(hideCancel){
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(cancel.getLayoutParams());  
				lp.setMargins(0, 0, 0, 0);  
				cancel.setLayoutParams(lp); 
//				hideCancel = true ;
				cancel.setVisibility(View.GONE); 
		 }
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.dialog_msg_commit:
			msgListener.setCommitClick();
			dismiss();
			break ;
		case R.id.dialog_msg_cancel:
			msgListener.setCancelClick();
			dismiss();
			break ;
		}
	}

}
