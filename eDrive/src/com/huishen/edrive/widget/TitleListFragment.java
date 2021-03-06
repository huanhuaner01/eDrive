package com.huishen.edrive.widget;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import com.huishen.edrive.R;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 带有标题的listFragment
 * @author zhanghuan
 *
 */
public abstract class TitleListFragment extends BaseFragment implements OnRefreshListener2<ListView> {
	//下拉刷新组件
//	public SwipeRefreshLayout mSwipeLayout;
	/** 返回上一个fragment */
	public int BACK_FRAGMENT = 0 ; 
	/** 返回上一个activity **/
	public int BACK_ACTIVITY =1 ; 
	private View RootView ;  //根组件
	private TextView title ; //标题,位于右边的文字
	public LoadingView loading ; //加载页面
	public PullToRefreshListView list ; //列表
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
		this.list = (PullToRefreshListView)RootView.findViewById(R.id.titlelist_list) ;
		this.back = (ImageButton)RootView.findViewById(R.id.header_back) ;
//		this.mSwipeLayout = (SwipeRefreshLayout)RootView.findViewById(R.id.swipe_ly);
		loading = (LoadingView)RootView.findViewById(R.id.titlelist_loading);
	
	}

	private void initView() {
//		mSwipeLayout.setOnRefreshListener(this);
//		mSwipeLayout.setColorScheme(R.color.color_refresh_1, R.color.color_refresh_2,
//				R.color.color_refresh_3, R.color.color_refresh_4);
		list.setOnRefreshListener(this);
		list.setMode(Mode.BOTH);  
		list.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载...");  
	    list.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");  
	    list.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多...");
	    
		//设置标题
		if(titlestr != null){
			this.title.setText(titlestr) ;
		}
		
		this.loading.setReLoadingListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				getWebData();
			}
			
		});
		setBack(back);
	
	}
	
	/**
	 * 访问网络，获取网络数据
	 */
	public abstract void getWebData();
	/**
	 * 访问网络，获取加载更多
	 */
	public abstract void getMore();
	
	/**
	 * 设置普通列表
	 * @param data
	 * @param list
	 */
	public abstract void setList(String data,PullToRefreshListView list);
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
	
//	@Override
//	public void onRefresh() {
//		getWebData() ;
//	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		
//		Toast.makeText(this, "上拉刷新", Toast.LENGTH_SHORT).show();
		getWebData() ;
	}
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		Log.i(TAG, "上拉刷新中。。。");
//		list.onRefreshComplete();
		getMore();
	}
	public class GetDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// Simulates a background job.
			
			return "";
		}

		@Override
		protected void onPostExecute(String result) {

			// Call onRefreshComplete when the list has been refreshed.
			list.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

}
