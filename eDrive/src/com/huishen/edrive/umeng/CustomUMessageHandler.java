/**
 * @encoding UTF-8
 */
package com.huishen.edrive.umeng;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * 继承自友盟默认消息处理器，主要负责处理自定义消息、定制通知栏样式等。
 * @author Muyangmin
 * @create 2014-10-30
 * @version 1.0
 */
public final class CustomUMessageHandler extends UmengMessageHandler {
	
	private static final String LOG_TAG = "CustomUMessageHandler";
	
	/**
	 * 处理自定义消息。
	 */
	@Override
    public void dealWithCustomMessage(final Context context, final UMessage msg) {
		//友盟v1.7之后自定义参数extra方式只对notification生效，因此需要手动解析字符串。
		Log.d(LOG_TAG, "custom message received.extra="+msg.extra+",custom="+msg.custom);
		Map<String, String> args = resolveCustomMsg(msg.custom);
		if (args!=null){
			Log.d(LOG_TAG, args.toString());
			dispatchMessage(context, args);	
		}
    }
	
	/**
	 * 解析服务器下发的参数信息。
	 * @param custom 自定义消息字符串，必须为JSON字符串。
	 * @return 返回解析好的key-value对。
	 */
	private Map<String, String> resolveCustomMsg(String custom){
		try {
			JSONObject json = new JSONObject(custom);
			HashMap<String, String> map = new HashMap<String, String>();
			Iterator<String> iterator = json.keys();
			String key;
			while (iterator.hasNext()){
				key = iterator.next();
				map.put(key, json.getString(key));
			}
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends PushData> void dispatchMessage(Context context, Map<String, String> extra){
		Intent intent = new Intent();
		T data;	//附加数据，通过具体消息类型实例化其子类
		switch (PushData.getPushDataType(extra)){
		case PushData.TYPE_NEWORDER:
			//直接弹出
			intent = new Intent(UmengPushConst.Action.ACTION_NEWORDER_PUSHDATA);
			data = (T) new NewOrderPushData(extra);
			break;
			default:
				data = null;
				intent = new Intent(UmengPushConst.Action.ACTION_UNKNOWN_PUSHDATA);
				break;
		}
		intent.putExtra(UmengPushConst.EXTRA_PUSHDATA, data);
		context.sendOrderedBroadcast(intent, null);
		Log.d(LOG_TAG, "dispatch message : send broadcast completed.");
		// 更新数量广播，否则，在“我的”页面，收到了信息，却未更新数量
//		Intent ai = new Intent(Const.TDB_HP_NUM_REFRESH_ACTION);
//		context.sendBroadcast(ai);
	}
}
