package com.huishen.edrive.center;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.SplashActivity;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.net.UploadResponseListener;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.BitmapUtil;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.util.Uris;
import com.huishen.edrive.widget.LoadingDialog;
import com.huishen.edrive.widget.RoundImageView;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 个人中心，修改个人信息
 * @author zhanghuan
 *
 */
public class ModifyUserInfoActivity extends Activity implements OnClickListener{
	private static final int REQUEST_CODE_TAKE_PHOTO = 0x2001;
	private static final int REQUEST_CODE_FROM_ALBUM = 0x2101;
	private static final int REQUEST_CODE_CROP_PHOTO = 0x2201;
	private String LOG_TAG = "ModifyUserInfoActivity" ;
    private RoundImageView photoimg; //学员头像
    private ImageButton back ;
    private String nicknamestr ,realnamestr ,addrstr ;
    private TextView tel , nickname ,realname ,title; //电话号码
    private LinearLayout photolay ,phonelay, nicknamelay ,realnamelay ,addrlay; //
    
    private LoadingDialog dialog ;
    
    
	/***************************腾讯统计相关框架*************************************/
	StatLogger logger = SplashActivity.getLogger();
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
		getWebData();
	}
	   @Override
		protected void onPause() {
			super.onPause();
			StatService.onPause(this);
		}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Debug.stopMethodTracing();
	}
	/***************************腾讯统计基本框架结束*************************************/
	
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
		photoimg.setOnClickListener(this) ;
		photolay.setOnClickListener(this) ;
		phonelay.setOnClickListener(this) ;
		nicknamelay.setOnClickListener(this) ;
		realnamelay.setOnClickListener(this) ;
		addrlay.setOnClickListener(this) ;
		dialog = new LoadingDialog(this);
	}
	/**
	 * 获取网络数据
	 */
	private void getWebData(){
		if(!dialog.isShowing()){
			dialog.show();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		NetUtil.requestStringData(SRL.Method.METHOD_GET_CENTER_USERINFO, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(LOG_TAG, result);
						dialog.dismiss();
						if(result == null || result.equals("")){
							AppUtil.ShowShortToast(ModifyUserInfoActivity.this, "服务器繁忙");
						}else{
							setData(result);
						}
						
					}
				},new DefaultErrorListener(this ,dialog));
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
            nicknamestr = json.optString("stuName", "");
            realnamestr = json.optString("stuRealName", "") ;
            addrstr = json.optString("address", "") ;
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
			takePhotoOrFromAlbum();
			break ;
		case R.id.m_center_photo:
			takePhotoOrFromAlbum();
			break ;
		case R.id.m_center_lay_nickname:
			intentSecendActivity(1);
			break ;
		case R.id.m_center_lay_realname:
			intentSecendActivity(2);
			break ;
//		case R.id.m_center_lay_userphone:
//			intentSecendActivity(0);
//			break ;
		case R.id.m_center_lay_addr:
			intentSecendActivity(3);
			break ;
		}
	}
	
	
	/**
	 * 启动信息修改页面
	 * @param index
	 */
	private void intentSecendActivity(int index){
		Intent i = new Intent(this,ModifyUserInfoSecendActivity.class);
		i.putExtra("tag", index);
		switch(index){
		case 1:
			i.putExtra("nickname",nicknamestr );
			break ;
		case 2:
			i.putExtra("realname", realnamestr);
			break ;
		case 3:
			i.putExtra("addr", addrstr);
		}
		this.startActivity(i);
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
	private final void cropPhoto(File imgFile) {
		Uri mUri = Uri.fromFile(imgFile);
		Intent intent = new Intent();
		intent.setAction("com.android.camera.action.CROP");
		intent.setDataAndType(mUri, "image/*");// mUri是已经选择的图片Uri
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX",
				getResources().getInteger(R.integer.avatar_width));// 输出图片大小
		intent.putExtra("outputY",
				getResources().getInteger(R.integer.avatar_height));
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
	}
	

	private final void uploadPhoto(File file) {
		//显示进度条
		 if(!dialog.isShowing()){
			 dialog.show();
		 }
		NetUtil.requestUploadFile(file, SRL.Method.METHOD_UPLOAD_PHOTO,
				new UploadResponseListener() {

					@Override
					public void onSuccess(String str) {
						if(dialog.isShowing()){
							dialog.dismiss();
						}
						Log.i(LOG_TAG, str);
						if(str == null || str.equals("")){
							AppUtil.ShowShortToast(getApplicationContext(), "服务器繁忙，请稍后重试");
						}else{
							try{
								JSONObject json = new JSONObject(str);
								if(json.getInt("status") == 1){
									AppUtil.ShowShortToast(ModifyUserInfoActivity.this, "上传成功");
									getWebData();
								}
							}catch(Exception e){
								e.printStackTrace();
								getWebData();
							}
						}
						
					}

					@Override
					public void onError(int httpCode) {
						Log.e(LOG_TAG, "upload fail! "+httpCode);
						AppUtil.ShowShortToast(ModifyUserInfoActivity.this, "上传失败，请重新选择图片");
						if(dialog.isShowing()){
							dialog.dismiss();
						}
						getWebData();
					}

					@Override
					public void onProgressChanged(int hasFinished) {
						Log.d(LOG_TAG, "uploading...finished " + hasFinished
								+ "%");
					}
				});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(LOG_TAG, "requestCode=" + requestCode + ", resultCode="
				+ resultCode);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			// 统一处理
			case REQUEST_CODE_TAKE_PHOTO:
				File origin = getAvatarFile();
				cropPhoto(origin);
				break;
			case REQUEST_CODE_FROM_ALBUM:
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				origin = new File(Uris.getImageFileRealPath(this,
						originalUri));
				cropPhoto(origin);
				break;
			case REQUEST_CODE_CROP_PHOTO:
				// 拿到剪切数据
				Bitmap cropedBitmap = data.getParcelableExtra("data");
				// 显示剪切的图像
				photoimg.setImageBitmap(cropedBitmap);
				
				// 图像保存到文件中
				BitmapUtil.saveBitmapToFile(getAvatarFile().getAbsolutePath(),
						cropedBitmap);
				File cropedFile = getAvatarFile();
				uploadPhoto(cropedFile);
				break;
			default:
				break;
			}
		}
	}
	
}
