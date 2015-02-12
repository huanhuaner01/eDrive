package com.huishen.edrive.demand;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.huishen.edrive.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;

/**
 * 定位对话框
 * 
 * @author zhanghuan
 * 
 */
public class PostAddrDialog extends Dialog implements View.OnClickListener{
    private PostDialogInterface listener ;
    private Context context;
    private Button addrBtn ,commit ;
    private EditText addredit ;
    private LocationClient mLocationClient ;
    
    public PostAddrDialog(Context context , PostDialogInterface listener) {
        super(context,R.style.dataselectstyle);
        this.context = context;
        this.listener = listener ;
        mLocationClient = new LocationClient(context.getApplicationContext());     //声明LocationClient类
    }
    
    
    public PostAddrDialog(Context context, int theme ,PostDialogInterface listener){
        super(context, theme);
        this.context = context;
        this.listener = listener ;
        mLocationClient = new LocationClient(context.getApplicationContext());     //声明LocationClient类
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_post_lay);
        registView();
        init() ;
    }
    
    /**
     * 注册组件
     * @param view
     */
    private void registView(){
    	this.addrBtn = (Button) findViewById(R.id.post_dialog_addr);
    	this.addredit = (EditText) findViewById(R.id.post_dialog_edit);
    	this.commit = (Button) findViewById(R.id.post_commit);
    }
    
    /**
     * 初始化
     */
    private void init(){
    	this.addrBtn.setOnClickListener(this) ;
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.post_addr_btn:
			break ;
		case R.id.post_commit:
			break ;
		}
	}
	

}
