/**
 * @encoding UTF-8
 */
package com.huishen.edrive.umeng;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * 定义活动接收者的基类，子类只需要实现所有的抽象方法即可，而注册的时候应该（且必须）使用该类的
 * IntentFiter来注册，不要外部手动添加Action。<br/>
 * 假定 SomeReceiver是本类的一个子类，那么应该这么注册：
 * <code>
 * 		SomeReceiver recv = new SomeReceiver();
 * 		registerReceiver(recv, recv.getIntentFilter());
 * </code><br/>
 * 另外，子类在实现方法的时候应尽可能将{@link #getAction()}和 {@link #getLogTag()}方法标记为final，
 * 以利用内联机制提高性能。
 * <p>关于活动接收者与默认接收者，参见 {@link DefaultPushDataReceiver}。</p>
 * @author Muyangmin
 * @create 2014-10-31
 * @version 1.0
 */
public abstract class AbsActivePushDataReceiver<T extends PushData> extends
		BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String tag = getLogTag();
		if (tag==null){
			tag = getClass().getSimpleName();
			Log.e(tag, "this subclass has return null tag, which is dangerous.Do nothing.");
			return ;
		}
		if (!isOrderedBroadcast()){
			Log.w(tag, "receive a non-oredered broadcast, bust it should be. Do nothing.");
			return ;
		}
		if (shouldAbortBroadcast()){
			abortBroadcast();
		}
		//由于这里并非公有API，因此默认转换是安全的，不加异常处理。
		@SuppressWarnings("unchecked")
		T data = (T) intent.getSerializableExtra(UmengPushConst.EXTRA_PUSHDATA);
		Log.d(tag, "get message:"+data.toString());
		process(data);
	}
	/**
	 * 子类的具体处理逻辑。
	 * @param data 推送消息。
	 */
	protected abstract void process(T data);
	/**
	 * 由子类返回一个LogTag，便于快速定位问题。默认返回子类的类名。
	 */
	protected String getLogTag(){
		return getClass().getSimpleName();
	};
	/**
	 * 要处理的广播的ACTION。
	 */
	protected abstract String getAction();
	
	/**
	 * 决定是否阻断该有序广播的继续发送，默认返回true。
	 */
	protected boolean shouldAbortBroadcast(){
		return true;
	}
	
	/**
	 * 获得当前类的IntentFiter。
	 */
	public IntentFilter getIntentFilter() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(getAction());
		intentFilter.setPriority(UmengPushConst.ACTIVE_RECEIVER_PRIORITY);
		return intentFilter;
	};
}
