package com.huishen.edrive.center;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.huishen.edrive.R;
import com.huishen.edrive.R.drawable;
import com.huishen.edrive.R.id;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.db.AppMessage;
import com.huishen.edrive.db.MeaasgeDbManager;
import com.huishen.edrive.db.MessageDbHelper;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.widget.BaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 消息详情页面
 * @author zhanghuan
 *
 */
public class MsgDetailActivity extends BaseActivity {
    private TextView title ,msgTitle ,msgTime , msgContent;
    private ImageButton back ;
    private NetworkImageView img ;
    private int id  ;
    private ImageLoader imageLoader ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_detail);
		AppController.getInstance().addActivity(this);
		this.setTag("MsgDetailActivity");
		id = this.getIntent().getIntExtra("id", -1);
		Log.i(TAG, "msgDetail id is "+id);
		this.setTag("MsgDetailActivity");
		registView();
		initView();
	}
	
	/**
	 * 注册组件
	 */
	private void registView(){
		title = (TextView)findViewById(R.id.header_title);
		msgTitle = (TextView)findViewById(R.id.msg_detail_title);
		msgTime = (TextView)findViewById(R.id.msg_detail_time);
		msgContent = (TextView)findViewById(R.id.msg_detail_content);
		back = (ImageButton)findViewById(R.id.header_back);
		img = (NetworkImageView)findViewById(R.id.msg_detail_icon);
	}
	/**
	 * 初始化组件
	 */
	private void initView(){
		title.setText("消息详情");
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
		MeaasgeDbManager db = new MeaasgeDbManager(this);
		AppMessage msg = db.queryMessage(id);
		if(msg == null){
			Log.i(TAG, "this id is not found Message! id is "+id);
			return ;
		}
		Log.i(TAG, msg.toString());
		msgTitle.setText(msg.getTitle());
		msgTime.setText(msg.getTime());
		msgContent.setText(msg.getContent());
		imageLoader = com.huishen.edrive.util.AppController.getInstance().getImageLoader();
		if(msg.getIconPath()!=null&&!msg.getIconPath().equals("")){
			img.setVisibility(View.VISIBLE);
		    img.setDefaultImageResId(R.drawable.ic_defualt_image);
		    img.setErrorImageResId(R.drawable.ic_error_image);
		    img.setImageUrl(NetUtil.getAbsolutePath(msg.getIconPath()), imageLoader);
		}
		else{
			img.setVisibility(View.GONE);
		}
	}
}
