package com.huishen.edrive.center;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.widget.RoundImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ModifyUserInfoActivity extends Activity implements OnClickListener{
    protected static final int REQUEST_CODE_TAKE_PHOTO = 1001;
	protected static final int REQUEST_CODE_FROM_ALBUM = 1002;
	private String LOG_TAG = "ModifyUserInfoActivity" ;
    private RoundImageView photoimg; //学员头像
    private ImageButton back ;
    private TextView tel , nickname ,realname ,title; //电话号码
    private LinearLayout photolay ,phonelay, nicknamelay ,realnamelay ,addrlay; //
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_user_info);
		AppController.getInstance().addActivity(this);
		registView();
		initView();
		
	}
	
	private void registView(){
		photoimg = (RoundImageView)findViewById(R.id.m_center_img_photo);
		back = (ImageButton)findViewById(R.id.header_back);
		title = (TextView)findViewById(R.id.header_title);
		tel = (TextView)findViewById(R.id.m_center_tv_phone);
		nickname = (TextView)findViewById(R.id.m_center_tv_nickname);
		realname = (TextView)findViewById(R.id.m_center_tv_realname);
		photolay = (LinearLayout)findViewById(R.id.m_center_photo);
		phonelay = (LinearLayout)findViewById(R.id.m_center_lay_userphone);
		nicknamelay = (LinearLayout)findViewById(R.id.m_center_lay_nickname);
		realnamelay = (LinearLayout)findViewById(R.id.m_center_lay_realname);
		addrlay = (LinearLayout)findViewById(R.id.m_center_lay_addr);
	}
	private void initView(){
		this.title.setText(getResources().getString(R.string.modify_title));
		this.back.setOnClickListener(this);
		getWebData();
	}
	/**
	 * 获取网络数据
	 */
	private void getWebData(){
		HashMap<String, String> map = new HashMap<String, String>();
		NetUtil.requestStringData(SRL.Method.METHOD_GET_CENTER_USERINFO, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(LOG_TAG, result);
						if(result == null || result.equals("")){
							AppUtil.ShowShortToast(ModifyUserInfoActivity.this, "服务器繁忙");
						}else{
							setData(result);
						}
						
					}
				},new DefaultErrorListener());
	}
	private void setData(String result){
		/**
		 * {"address":"成都信息工程学院",
		 * "path":"/attachment/head/thum/stuPic.jpg",
		 * "phone":"18384296843","stuName":"18384296843","stuRealName":"刘霖晋"}
		 */
		try{
			JSONObject json = new JSONObject(result);
			if(!json.optString("path", "").equals("")){
		       NetUtil.requestLoadImage(photoimg, json.optString("path", ""), R.drawable.photo_coach_defualt);
			}
            tel.setText(json.optString("phone", "缺失"));
            nickname.setText(json.optString("stuName", ""));
            realname.setText(json.optString("stuRealName", ""));
            
		}catch(Exception e){
			
		}

	}
	private final void takePhotoOrFromAlbum() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.str_register_photo_select_source)
				.setItems(R.array.str_register_photo_source,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) { // 拍照
									// 将拍得的照片先保存在本地，指定照片保存路径（SD卡）
									Uri imageUri = Uri
											.fromFile(getAvatarFile());
									Intent openCameraIntent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									openCameraIntent.putExtra(
											MediaStore.EXTRA_OUTPUT, imageUri);
									startActivityForResult(openCameraIntent,
											REQUEST_CODE_TAKE_PHOTO);
								} else { // 从相册选择
									Intent openAlbumIntent = new Intent(
											Intent.ACTION_GET_CONTENT);
									openAlbumIntent.setType("image/*");
									startActivityForResult(openAlbumIntent,
											REQUEST_CODE_FROM_ALBUM);
								}
							}
						}).create().show();

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.header_back:
			finish();
			break ;
		case R.id.m_center_img_photo:
			break ;
		case R.id.m_center_photo:
			break ;
		case R.id.m_center_lay_nickname:
			break ;
		case R.id.m_center_lay_realname:
			break ;
		case R.id.m_center_lay_userphone:
			break ;
		case R.id.m_center_lay_addr:
			break ;
		}
	}
	
	private final File getAvatarFile() {
		File target = new File(Environment.getExternalStorageDirectory()+"/eDrive/picture/", "avatar.jpg");
		if (!target.exists()) {
			Log.d(LOG_TAG, "target doesnot exist.create it.");
			target.getParentFile().mkdirs(); // 避免将末尾的文件名命名为文件夹
			try {
				target.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return target;
	}
}
