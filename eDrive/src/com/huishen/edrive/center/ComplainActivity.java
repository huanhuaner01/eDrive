package com.huishen.edrive.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ComplainActivity extends Activity {
	private String TAG = "ComplainActivity" ;
   private ComplainListAdapter apdater ;
   private ListView list ;
   private EditText edit;
   private Button btn ;
   private TextView title ;
   private ImageButton back ;
   private List<HashMap<String,Object>> data ;
   private int coachId = -1;
   private String[] comp = new String[]{"抢了单但是不联系","做不到我的学车要求","态度不好,教练是出口成章","教学质量不高，讲的不细，听不懂"};
   private StringBuffer keybuffer ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complain);
		AppController.getInstance().addActivity(this);
		coachId = this.getIntent().getIntExtra("coachId", -1);
		registView();
		initView();
	}
	private void registView() {
		list = (ListView)findViewById(R.id.complain_list);
		edit = (EditText)findViewById(R.id.complain_edit);
		btn = (Button)findViewById(R.id.complain_btn);
		title = (TextView)findViewById(R.id.header_title);
		back = (ImageButton)findViewById(R.id.header_back);
	}
	private void initView() {
		keybuffer = new StringBuffer();
		this.title.setText("投诉");
		this.back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
		data = new ArrayList<HashMap<String,Object>>();
		for(int i = 0 ;i<comp.length;i++ ){
			HashMap<String ,Object> map = new HashMap<String ,Object>();
			map.put("content", comp[i]);
			map.put("status", 0);
			data.add(map);
		}
		String[] from = new String[]{"content"};
		int[] to = new int[]{R.id.item_complain_tv};
		apdater = new ComplainListAdapter(this,data ,R.layout.item_complain ,from,to);
		list.setAdapter(apdater);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				HashMap<String, Object> map = (HashMap<String, Object>) view
						.getTag();
				Log.i(TAG, map.toString());
				int status = Integer.parseInt(map.get("status").toString()) ;
				String service = map.get("content").toString() ;
				if (status == 0) {
					keybuffer.append(service+",");
				}else{
					String replaced = keybuffer.toString().replace(service+",", "");
					keybuffer.delete(0, keybuffer.length());
					keybuffer.append(replaced);
				}
				apdater.selectOption(position);
			}
			
		});
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				sendToComplain();
			}
			
		});
	}
	
	/**
	 * 提交投诉
	 */
	private void sendToComplain(){
		if(edit.getText().toString().equals("")&& keybuffer.toString().toString().equals("")){
			AppUtil.ShowShortToast(getApplicationContext(), "投诉内容不能为空");
			return ;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId",coachId+"");
		map.put("complaintContent", keybuffer.toString()+""+edit.getText().toString());
		NetUtil.requestStringData(SRL.Method.METHOD_COMPLAIN, map,
				new Response.Listener<String>() {
                       
					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if(result == null || result.equals("")){
							AppUtil.ShowShortToast(ComplainActivity.this, "服务器繁忙");
						}else{
							try{
								JSONObject json = new JSONObject(result);
								if(json.getInt("status")==1){
									AppUtil.ShowShortToast(ComplainActivity.this, "投诉成功");
									finish();
								}
									
							}catch(Exception e){
								AppUtil.ShowShortToast(ComplainActivity.this, "服务器繁忙");
							}
						}
					}
	},new DefaultErrorListener());
	}
	
}
