package com.huishen.edrive.center;

import java.io.File;

import com.huishen.edrive.R;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.util.AppController;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ModifyUserInfoActivity extends Activity {
    private String LOG_TAG = "ModifyUserInfoActivity" ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_user_info);
		AppController.getInstance().addActivity(this);
	}
	
//	private final void takePhotoOrFromAlbum() {
//		new AlertDialog.Builder(this)
//				.setTitle(R.string.str_register_photo_select_source)
//				.setItems(R.array.str_register_photo_source,
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//								if (which == 0) { // 拍照
//									// 将拍得的照片先保存在本地，指定照片保存路径（SD卡）
//									Uri imageUri = Uri
//											.fromFile(getAvatarFile());
//									Intent openCameraIntent = new Intent(
//											MediaStore.ACTION_IMAGE_CAPTURE);
//									openCameraIntent.putExtra(
//											MediaStore.EXTRA_OUTPUT, imageUri);
//									startActivityForResult(openCameraIntent,
//											REQUEST_CODE_TAKE_PHOTO);
//								} else { // 从相册选择
//									Intent openAlbumIntent = new Intent(
//											Intent.ACTION_GET_CONTENT);
//									openAlbumIntent.setType("image/*");
//									startActivityForResult(openAlbumIntent,
//											REQUEST_CODE_FROM_ALBUM);
//								}
//							}
//						}).create().show();
//
//	}
	
//	private final File getAvatarFile() {
//		File target = new File(FileUtil.getTemporaryPhotoPath(), "avatar.jpg");
//		if (!target.exists()) {
//			Log.d(LOG_TAG, "target doesnot exist.create it.");
//			target.getParentFile().mkdirs(); // 避免将末尾的文件名命名为文件夹
//			try {
//				target.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return target;
//	}
}
