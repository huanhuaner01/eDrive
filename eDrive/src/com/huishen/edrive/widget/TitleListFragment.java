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
import android.widget.ExpandableListView;
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
	public SwipeRefreshLayout mSwipeLayout;
	/** 返回上一个fragment */
	public int BACK_FRAGMENT = 0 ; 
	/** 返回上一个activity **/
	public int BACK_ACTIVITY =1 ; 
	private View RootView ;  //根组件
	private TextView title ; //标题,位于右边的文字
	public ListView list ; //列表
	public ExpandableListView expandablelist ; //扩展列表，默认隐藏
	private ImageButton back ; //返回键
    private String titlestr ;
    private String url ;
    private String data ; //数据列表
    
    //初始化相关
    public Context context ;
    public Object tag ;
    
	public TitleListFragment(Context context,String titlestr , String url) {
		this.context = context ;
		this.titlestr = titlestr ;
		this.url = url ;
	}

	public TitleListFragment(Context context, Object tag,String titlestr , String url  ) {
		this.context = context ;
		this.titlestr = titlestr ;
		this.url = url ;
		this.tag = tag ;
	}
	
	@Override
	public void onResume() {
		//获取网络数据
		getWebData();
		super.onResume();
		
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
		this.expandablelist = (ExpandableListView)RootView.findViewById(R.id.titlelist_expandablelist);
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
		
		
		setBack(back);
	
	}
	
	/**
	 * 访问网络，获取网络数据
	 */
	public abstract void getWebData();
	
	/**
	 * 设置普通列表
	 * @param data
	 * @param list
	 */
	public abstract void setList(String data,ListView list);
	
	/**
	 * 设置可扩展列表
	 * @param data
	 * @param list
	 */
	public abstract void setList(String data,ExpandableListView list);
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
	/**
	 * 展示可扩展列表
	 * @param isshow 是否展示。true展示，false 不展示
	 */
	public void showExpandableList(boolean isshow){
		if(isshow){
			this.expandablelist.setVisibility(View.VISIBLE);
			this.list.setVisibility(View.GONE);
		}else{
			this.expandablelist.setVisibility(View.GONE);
			this.list.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onRefresh() {
		getWebData() ;
	}
	
}
