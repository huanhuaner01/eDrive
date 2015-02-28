package com.huishen.edrive.demand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.huishen.edrive.R;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.widget.CustomEditText;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * 
 * 文字需求发布界面
 * @author zhanghuan
 *
 */
public class PostTxtActivity extends Activity {
	private String TAG= "PostTxtActivity" ;
    private TextView title ;
    private ImageButton back ;
    private GridView postGrid ; //选择信息
    private Button addrBtn ,commit ; //地理位置按钮，提交按钮
    private PostGridItemAdapter adapter ;
    private ArrayList<Map<String ,Object>> data ; 
    private CustomEditText edit ;
//    private boolean isFrist = true ;
    private PostAddrDialog dialog ;
    
    
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_txt);
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
		this.edit = (CustomEditText) findViewById(R.id.post_edit);
		this.addrBtn = (Button) findViewById(R.id.post_addr_btn);
		this.commit = (Button) findViewById(R.id.post_commit);
	}
	
	/**
	 * 初始化
	 */
	private void initView() {
	
		this.title.setText(this.getResources().getString(R.string.post_title));
		this.addrBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(!dialog.isShowing()){
				dialog = new PostAddrDialog(PostTxtActivity.this,listener);
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
					edit.append(service+",");
				}else{
					edit.setText(edit.getText().toString().replace(service+",", ""));
				}
				adapter.selectOption(position);
			}
			
		}) ;
		dialog = new PostAddrDialog(this,listener);
		dialog.show() ;
		this.commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				sendTxtOrder();
			}
			
		}) ;
		
	}
	
	/**
	 * 发送订单
	 */
	private void sendTxtOrder(){
		
//		stuId=1;//学生ID
//		stuRealName=王成;//直接姓名
//		content=包接包送，对学生友好;//订单需求内容
//		lng=104.065656;//当前学生经度
//		lat=30.577716;//当前学生纬度
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(SRL.Param.PARAM_LATITUDE, "sl");
        map.put(SRL.Param.PARAM_LONGITUDE, "ass");
        map.put(SRL.Param.PARAM_STUID, "ass");
        map.put(SRL.Param.PARAM_CONTENT,"");
//		NetUtil.requestStringData(relativePath, params, listener, errlisListener)
	}
	
	private PostDialogInterface listener= new PostDialogInterface(){

		@Override
		public void result(String result, double longitude, double latitude) {
			addrBtn.setText(result);
			Log.i(TAG, "("+longitude+","+latitude+")");
		}
	} ;
}
