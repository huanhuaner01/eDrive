package com.huishen.edrive.widget;

import com.huishen.edrive.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 带有标题的listFragment
 * @author zhanghuan
 *
 */
public abstract class TitleListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
	//下拉刷新组件
	private SwipeRefreshLayout mSwipeLayout;
	/** 返回上一个fragment */
	public int BACK_FRAGMENT = 0 ; 
	/** 返回上一个activity **/
	public int BACK_ACTIVITY =1 ; 
	private View RootView ;  //根组件
	private TextView title ,note; //标题,位于右边的文字
	private ListView list ; //列表
	private ImageButton back ; //返回键
    private String titlestr ;
    private String url ;
    private int coachId ;
    private String data ; //数据列表
    
    //初始化相关
    public Context context ;
    public Object tag ;
    
	public TitleListFragment(Context context,String titlestr , String url ,int coachId) {
		this.context = context ;
		this.titlestr = titlestr ;
		this.url = url ;
		this.coachId = coachId ;
	}

	public TitleListFragment(Context context, Object tag,String titlestr , String url ,int coachId ) {
		this.context = context ;
		this.titlestr = titlestr ;
		this.url = url ;
		this.tag = tag ;
		this.coachId = coachId ;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		    
		try{
			RootView = inflater.inflate(R.layout.fragment_titlelist, null);
			registView();
			initView();
		  } catch (Exception e) {
		}
		return RootView;
	}

	private void registView() {
		this.title = (TextView)RootView.findViewById(R.id.header_title) ;
		this.list = (ListView)RootView.findViewById(R.id.titlelist_list) ;
		this.back = (ImageButton)RootView.findViewById(R.id.header_back) ;
		this.mSwipeLayout = (SwipeRefreshLayout)RootView.findViewById(R.id.swipe_ly);
	}

	private void initView() {
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorScheme(R.color.color_refresh_1, R.color.color_refresh_2,
				R.color.color_refresh_3, R.color.color_refresh_4);
		//设置标题
		if(titlestr != null){
			this.title.setText(titlestr) ;
		}
		//获取网络数据
		getWebData();
		setBack(back);
	
	}
	
	/**
	 * 访问网络，获取网络数据
	 */
	private void getWebData(){
		//如果url为空或者list接口为空则不访问网络，直接返回
//		if(url == null || url.equals("")||listinterface == null){
//			return ;
//		}
		setList(data, list);
	}
	
	
	public abstract void setList(String data,ListView list);
	/**
	 * 设置标题栏备注备注
	 * @param isShow
	 * @param listener
	 */
	public abstract void setNote(TextView note);
	
	/**
	 * 返回键监听
	 * @param back2
	 */
	public void setBack(ImageButton back2){
		//设置返回键监听
		back2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				 Log.i("TitleListFragment", "点击了返回键");
				 FragmentManager fm = getFragmentManager();  
			     fm.popBackStack();
			}
			
		}) ;
	}
	
	
	@Override
	public void onRefresh() {
		getWebData() ;
	}
	
}
