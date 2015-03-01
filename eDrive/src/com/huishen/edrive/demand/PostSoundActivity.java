package com.huishen.edrive.demand;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.huishen.edrive.R;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.Recorder;
import com.huishen.edrive.util.SimpleRecorder;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PostSoundActivity extends Activity implements OnClickListener{
	private String TAG= "PostSoundActivity" ;
    private TextView title ;
    private ImageButton back ;
    private GridView postGrid ; //选择信息
    private Button addrBtn ,sound_play ; //地理位置按钮，提交按钮
    private PostGridItemAdapter adapter ;
    private ArrayList<Map<String ,Object>> data ;
    private ImageButton sound_image ;
//    private boolean isFrist = true ;
    private PostAddrDialog dialog ;
    private StringBuffer keybuffer ;
    private Animation animation = null; //动画模式
    private View soundbg ;
    private SimpleRecorder recorder ;
    private File audiofile ;
	@Override
	protected void onResume() {
		super.onResume();
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_sound);
		registView();
		initView();
	}
	
	/**
	 * 注册组件
	 */
	private void registView() {
		this.title = (TextView) findViewById(R.id.header_title);
		this.back = (ImageButton) findViewById(R.id.header_back);
		this.postGrid = (GridView) findViewById(R.id.post_gridview);
		this.sound_image = (ImageButton) findViewById(R.id.post_soundimage);
		this.addrBtn = (Button) findViewById(R.id.post_addr_btn);
		this.soundbg = (View) findViewById(R.id.post_imagebg);
		this.sound_play = (Button) findViewById(R.id.post_btn_sound);
		recorder = SimpleRecorder.getInstance();
	}
	
	/**
	 * 初始化
	 */
	private void initView() {
	
		this.title.setText(this.getResources().getString(R.string.post_title));
		
		//设置语言组件
		this.sound_image.setEnabled(false) ;
		initScale();
		this.sound_play.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN: //按下事件
					
					sound_image.setEnabled(true);
					soundbg.startAnimation(animation);
					sound_play.setText(PostSoundActivity.this.getResources().getString(R.string.post_sound_btn_press));
//					String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"edrive" ;
					recorder.clearFile() ;
					audiofile = new File(recorder.getPath() ,recorder.now()+recorder.getRandomString(2)+".mp3");
					
					recorder.startRecord(audiofile);
					break ;
				case MotionEvent.ACTION_UP: //弹起事件
					soundbg.clearAnimation();
					sound_play.setText(PostSoundActivity.this.getResources().getString(R.string.post_sound_btn));
					recorder.stopRecord();
				break;
				}
				return false;
			}
		
		}

		);
		this.sound_image.setOnClickListener(this);
		///设置语言组件完成///////////////////////////
		
		
		this.addrBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(!dialog.isShowing()){
				dialog = new PostAddrDialog(PostSoundActivity.this,listener);
				dialog.show() ;
				}
			}
			
		});
		//返回按钮监听事件
		this.back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		//给选择的服务选项添加数据
		keybuffer = new StringBuffer();
		data = new ArrayList<Map<String ,Object>>();
		for(int i = 0 ; i<6 ; i++){
			HashMap<String  , Object> map = new HashMap<String ,Object>();
			map.put("service", "包接包送"+i) ;
			map.put("status", 0) ;
			data.add(map);
		}
		String[] from = new String[]{"service"};
		int[] to = new int[]{R.id.item_post_tv};
		this.adapter = new PostGridItemAdapter(this, data, R.layout.item_post_grid, from, to) ;
		
		this.postGrid.setAdapter(this.adapter) ;
		this.postGrid.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				HashMap<String, Object> map = (HashMap<String, Object>) view
						.getTag();
				Log.i(TAG, map.toString());
				int status = Integer.parseInt(map.get("status").toString()) ;
				String service = map.get("service").toString() ;
				if (status == 0) {
					keybuffer.append(service+",");
				}else{
					String replaced = keybuffer.toString().replace(service+",", "");
					keybuffer.delete(0, keybuffer.length());
					keybuffer.append(replaced);
				}
				adapter.selectOption(position);
			}
			
		}) ;
		dialog = new PostAddrDialog(this,listener);
		dialog.show() ;
		
	}
	
	private void initScale(){
		animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
	}
	
	
	private PostDialogInterface listener= new PostDialogInterface(){

		@Override
		public void result(int tag ,String result, double longitude, double latitude) {
			addrBtn.setText(result);
			Log.i(TAG, "("+longitude+","+latitude+")");
		}
	} ;
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.post_soundimage:
			if(recorder == null || recorder.getAudioFileLength()<1){
				AppUtil.ShowShortToast(this,"请先录音");
			}else{
			recorder.playAudioFile(audiofile);
			}
			break;
		case R.id.post_commit:
			break ;
		}
	}
}
