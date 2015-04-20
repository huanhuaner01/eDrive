package com.huishen.edrive.center;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.huishen.edrive.R;
import com.huishen.edrive.net.DefaultErrorListener;
import com.huishen.edrive.net.NetUtil;
import com.huishen.edrive.net.OnProgressChangedListener;
import com.huishen.edrive.net.SRL;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.AppUtil;
import com.huishen.edrive.util.SimpleRecorder;
import com.huishen.edrive.widget.BaseActivity;
import com.huishen.edrive.widget.LoadingDialog;
import com.huishen.edrive.widget.RoundImageView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 订单详情页面
 * 
 * @author zhanghuan
 * 
 */
public class OrderDetailActivity extends BaseActivity {
	private TextView title, ordercontent, coachname, coachfield, coachjudge,
			coachdistance, titlenote;
	private int orderId, coachId;
	private ImageButton vidio, back;
	private Button judgeBtn, cancelOrderBtn;
	private RoundImageView coachphoto;
	private RatingBar coachrating;
	private LinearLayout vidiolay, coachlay;
	private ListView list;
	private ArrayList<HashMap<String, Object>> listdata = new ArrayList<HashMap<String, Object>>();
	private LoadingDialog dialog;
	private File andioFile;
	private int commentId;

	/***************************腾讯统计相关框架*************************************/

	/***************************腾讯统计基本框架结束*************************************/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		AppController.getInstance().addActivity(this);
		this.setTag("OrderDetailActivity");
		// ---------------------获取数据-----------------------
		orderId = this.getIntent().getIntExtra("id", -1);
		coachId = this.getIntent().getIntExtra("coachId", -1);
		// ---------------------获取数据结束！--------------------
		registView();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getWebDate();
	}

	private void registView() {
		title = (TextView) findViewById(R.id.header_title);
		titlenote = (TextView) findViewById(R.id.header_note);
		ordercontent = (TextView) findViewById(R.id.order_detail_content);
		coachname = (TextView) findViewById(R.id.order_detail_cname);
		coachfield = (TextView) findViewById(R.id.order_detail_cfield);
		coachdistance = (TextView) findViewById(R.id.order_detail_distance);
		coachjudge = (TextView) findViewById(R.id.order_detail_cjudge);
		vidio = (ImageButton) findViewById(R.id.order_detail_img);
		back = (ImageButton) findViewById(R.id.header_back);
		coachrating = (RatingBar) findViewById(R.id.order_detail_coachrating);
		vidiolay = (LinearLayout) findViewById(R.id.order_detail_imglay);
		coachlay = (LinearLayout) findViewById(R.id.order_detial_coachlay);
		coachphoto = (RoundImageView) findViewById(R.id.order_detail_cphoto);
		list = (ListView) findViewById(R.id.order_detail_list);
		judgeBtn = (Button) findViewById(R.id.order_detail_btn_judge);
		cancelOrderBtn = (Button) findViewById(R.id.order_detail_cancelorder);

	}

	private void initView() {
		title.setText("订单详情");
		dialog = new LoadingDialog(this);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		cancelOrderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cancelOrder();
			}

		});
		if (coachId != -1) {
			titlenote.setText("投诉");
			titlenote.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(OrderDetailActivity.this,
							ComplainActivity.class);
					i.putExtra("coachId", coachId);
					startActivity(i);
				}

			});
		}

		coachphoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(OrderDetailActivity.this,
						CoachDetailActivity.class);
				i.putExtra("id", coachId);
				startActivity(i);
			}

		});
	}

	private void cancelOrder() {
		if(!isFinishing()&&!dialog.isShowing()){
			dialog.show();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", orderId + "");
		cancelOrderBtn.setEnabled(false);
		NetUtil.requestStringData(SRL.Method.METHOD_CANCEL_ORDER,TAG , map,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String result) {
						cancelOrderBtn.setEnabled(true);
						if(dialog.isShowing()){
							dialog.dismiss();
						}
						Log.i(TAG, result);
						if (result == null || result.equals("")) {
							AppUtil.ShowShortToast(OrderDetailActivity.this,
									"服务器繁忙");
						} else {
							try {
								JSONObject json = new JSONObject(result);
								if (json.getInt("status") == 1) {
									AppUtil.ShowShortToast(
											OrderDetailActivity.this, "订单已取消");
									finish();
								}

							} catch (Exception e) {

							}
						}
					}
				}, new DefaultErrorListener(this,cancelOrderBtn,dialog));
	}

	/**
	 * 获取订单数据
	 */
	private void getWebDate() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", orderId + "");
		if (coachId != -1) {
			map.put("coachId", coachId + "");
		}
        if(!isFinishing()&&!dialog.isShowing()){
        	dialog.show();
        }
		NetUtil.requestStringData(SRL.Method.METHOD_GET_ORDER_DETAIL, TAG ,map,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String result) {
						Log.i(TAG, result);
						if(dialog.isShowing()){
							dialog.dismiss();
						}
						if (result == null || result.equals("")) {
							AppUtil.ShowShortToast(OrderDetailActivity.this,
									"服务器繁忙");
						} else {
							setData(result);
						}
					}
				}, new DefaultErrorListener(this,dialog));
	}

	private void setData(String data) {
		/**
		 * {"coachInfo":{"busNumber":"蜀K88888","coachName":"雷猴","coachScore":0.0
		 * , "path":"/attachment/coh-head/image/IMG_2015021415243200907519.jpg",
		 * "schoolName"
		 * :"蜀娟驾校"},,"commentPlusInfo":{"commentPlusTime":"2015-01-27"
		 * ,"content":"213"},
		 * "commtInfo":[{"commentPlusTime":"2015-01-27","content"
		 * :"您还可以输入150 个字符",
		 * "contentTime":"2015-02-04","score":4.0}],"distance":
		 * 7147151,"tempBillInfo":
		 * {"audio":"/attachment/audio/34/FILE_2015031015130108025507.mp3",
		 * "content":"能45-60天拿证,考试包接包送,","stuGps":"104.062611,30.578016"}}
		 * 
		 * {"coachInfo":{"address":"四川省成都市武侯区锦悦西路","busNumber":"4110学",
		 * "coachName":"任涛","coachScore":0.0,"id":63,
		 * "path":"/attachment/coh-head/image/IMG_2015031910151302089758.jpg"},
		 * "commentPlusInfo":{"commentPlusTime":"2015-03-19 20:11:09",
		 * "content":"我对教练的评价，自己看不到"},"commtInfo":
		 * {"commentId":54,"content":"教练人很好"
		 * ,"contentTime":"2015-03-14 11:53:40"},
		 * "distance":715,"tempBillInfo":{
		 * "content":"师傅，我要学车，我需要能45-60天拿证,考试包接包送"}}
		 */

		try {
			JSONObject json = new JSONObject(data);
			JSONObject tempBillInfo = json.optJSONObject("tempBillInfo");
			ordercontent.setText(tempBillInfo.optString("content", ""));
			if (!tempBillInfo.optString("audio", "").equals("")) {
				downloadFile(tempBillInfo.optString("audio", ""));
				vidiolay.setVisibility(View.VISIBLE);
				vidio.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						SimpleRecorder recorder = SimpleRecorder.getInstance();
						if (andioFile != null && andioFile.length() > 0) {
							recorder.playAudioFile(andioFile);
						} else {
							AppUtil.ShowShortToast(getApplicationContext(),
									"语音下载失败");
						}
					}

				});
			} else {
				vidiolay.setVisibility(View.GONE);
			}
			JSONObject coach = json.optJSONObject("coachInfo");
			
			if (coach != null) {
				coachlay.setVisibility(View.VISIBLE);
				judgeBtn.setVisibility(View.VISIBLE);
				coachdistance.setText("距我"
						+ (json.optInt("distance", 0) / 1000.0) + "k");
				coachname.setText(coach.optString("coachName", ""));
				coachfield.setText("训练场：" + coach.optString("address", "无"));
				coachjudge.setText((float) (coach.optDouble("coachScore", 5))
						+ "分");
				coachrating
						.setRating((float) (coach.optDouble("coachScore", 5)));
				if (!coach.optString("path", "").equals("")) {
					NetUtil.requestLoadImage(coachphoto,
							coach.getString("path"),
							R.drawable.photo_coach_defualt);
				}
			} else {
				Log.i(TAG, "空");
				coachlay.setVisibility(View.GONE);
				judgeBtn.setVisibility(View.GONE);
			}

			JSONObject commtInfo = json.optJSONObject("commtInfo");
			if (commtInfo != null) { //判断是否有评价
				listdata.clear();
				String[] from = new String[] { "time", "content" };
				int[] to = new int[] { R.id.judge_listitem_stuname,
						R.id.judge_listitem_content };
				    listdata.clear();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("time", commtInfo.optString("contentTime", ""));
					map.put("content", commtInfo.optString("content", ""));
					map.put("rating", commtInfo.optDouble("score", 5.0));
					listdata.add(map);
					commentId = commtInfo.optInt("commentId", -1);
					JSONObject plusjudge = json
							.optJSONObject("commentPlusInfo");
					if (plusjudge != null) {
						HashMap<String, Object> pram = new HashMap<String, Object>();
						pram.put("time",
								plusjudge.optString("commentPlusTime", ""));
						pram.put("content", plusjudge.optString("content", ""));
						pram.put("rating", -1.0);
						listdata.add(pram);
						judgeBtn.setVisibility(View.GONE);
					   } else {
						judgeBtn.setVisibility(View.VISIBLE);
						judgeBtn.setText("追加评价");
						judgeBtn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								intentjudgeActivity(2);
							}
						});
				}
				JudgeListSimAdapter adapter = new JudgeListSimAdapter(this,
						listdata, R.layout.judge_list_item, from, to);
				list.setAdapter(adapter);
			} 
			else {
				judgeBtn.setText("评价");
				judgeBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						intentjudgeActivity(1);
					}

				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 跳转到评价页面
	 * 
	 * @param tag
	 */
	private void intentjudgeActivity(int tag) {
		Intent i = new Intent(this, JudgeActivity.class);
		i.putExtra("tag", tag);
		i.putExtra("coachId", coachId);
		if (commentId != -1) {
			i.putExtra("commentId", commentId);
		}
		startActivity(i);
	}

	/**
	 * 下载语音文件
	 */
	private void downloadFile(String path) {
       if(!isFinishing()&&!dialog.isShowing()){
    	   dialog.show();
       }
		andioFile = new File(Environment.getExternalStorageDirectory()
				+ "/eDrive/audio/" + "andio.mp3");
		NetUtil.requestDownloadFile(path, andioFile,
				new OnProgressChangedListener() {

					@Override
					public void onTaskFinished() {
						// 关闭进度条
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						AppUtil.ShowShortToast(getApplicationContext(),
								"语音下载完成");

					}

					@Override
					public void onTaskFailed() {
						// 关闭进度条
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						AppUtil.ShowShortToast(getApplicationContext(),
								"语音下载失败");
						andioFile = null;
					}

					@Override
					public void onProgressChanged(int min, int max, int progress) {

					}
				});
	}
}
