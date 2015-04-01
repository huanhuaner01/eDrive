package com.huishen.edrive.apointment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.SplashActivity;
import com.huishen.edrive.R.layout;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.widget.LoadingDialog;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 解除绑定页面
 * @author zhanghuan
 *
 */
public class UnBindCoachActivity extends Activity implements OnItemClickListener{
    private String TAG = "UnBindCoachActivity" ;
    private ListView list ;
    private EditText edit ;
    private Button commit ;
    private TextView title ;
    private ImageButton back ;
    private int checkposition =-1;
    private UnbindListAdapter adapter ;
    private String[] contents = new String[]{"科目二和科目三教练不一样","教练学员太多，让另外一个教练带练","已于教练达成协议","绑定电话输入错误，不是我的教练"};
    private LoadingDialog loading ;
    
	/***************************腾讯统计相关框架*************************************/
	StatLogger logger = SplashActivity.getLogger();
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
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
		setContentView(R.layout.activity_un_bind_coach);
		android.os.Debug.startMethodTracing("MTAUnBindCoachActivity");
		registView();
		initView();
	}
	
	/**
	 * 注册页面
	 */
	private void registView(){
		title = (TextView)findViewById(R.id.header_title);
		back = (ImageButton)findViewById(R.id.header_back) ;
		edit = (EditText)findViewById(R.id.undind_edit);
		list = (ListView)findViewById(R.id.unbind_list);
		commit = (Button)findViewById(R.id.unbind_btn);
	}
	
	/**
	 * 初始化
	 */
	private void initView(){
		title.setText("解除绑定");
		loading = new LoadingDialog(this);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
		//设置单选模式
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		String[] from = new String[]{"unbindcontent"};
		int[] to = new int[]{R.id.item_unbind_tv};
		
		ArrayList<HashMap<String ,String>> data = new ArrayList<HashMap<String ,String>>();
		for(int i = 0 ;i<contents.length;i++){
			HashMap<String ,String> map = new HashMap<String ,String>();
			map.put("unbindcontent", contents[i]);
		    data.add(map);
		}
		adapter = new UnbindListAdapter(this,data,R.layout.item_unbind ,from ,to );
		list.setAdapter(adapter);
		// 注册监听器
		list.setOnItemClickListener(this);
		commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				commitUnbind();
			}
			
		});
	}
	/**
	 * 解绑接口
	 */
   private void commitUnbind(){
	   if(checkposition<0 &&edit.getText().toString().equals("")){
		   AppUtil.ShowShortToast(this, "请选择理由或者填写其他理由");
		   return ;
	   }
	   commit.setEnabled(false);
	   if(!loading.isShowing()){
		   loading.show();
	   }
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("content", contents[checkposition]+","+edit.getText().toString());
//		map.put(SRL.Param.PARAM_LATITUDE, lat+"");
		NetUtil.requestStringData(SRL.Method.METHOD_UNBIND_COACH, map, new Response.Listener<String>() {

			@Override
			public void onResponse(String result) {
				loading.dismiss();
				Log.i(TAG, result);
				if(result == null || result.equals("")){
					AppUtil.ShowShortToast(getApplicationContext(), "获取数据异常");
				}else{
					try{
						JSONObject json = new JSONObject(result);
						if(json.optInt("code", -1) == 1){
							AppUtil.ShowShortToast(UnBindCoachActivity.this, "请求成功，请耐心等待教练回复");
							finish();
						}
						else{
							AppUtil.ShowShortToast(UnBindCoachActivity.this, "学员未绑定教练，或者已解除绑定");
						}
					}catch(Exception e){
						e.printStackTrace();
						AppUtil.ShowShortToast(UnBindCoachActivity.this, "返回数据错误");
					}
				}
			}
			
		}, new DefaultErrorListener(this,commit ,loading)) ;
   }
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		adapter.notifyDataSetInvalidated();
		checkposition = arg2;
	}
	
	class UnbindListAdapter extends SimpleAdapter{

		public UnbindListAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View root = super.getView(position, convertView, parent);
			ImageView img = (ImageView)root.findViewById(R.id.item_unbind_ic);
			LinearLayout lay = (LinearLayout)root.findViewById(R.id.item_unbind_lay);
			//设置选中状态
			  if (position ==checkposition) {
				  img.setImageResource(R.drawable.radio_press);
				  lay.setBackgroundResource(R.drawable.shape_red_radius_rect);
		        } else {
		      	    img.setImageResource(R.drawable.selector_radio);
		      	    lay.setBackgroundResource(R.drawable.selector_complain);
		        }
			  
			return root;
		}
		
		
	}
}
