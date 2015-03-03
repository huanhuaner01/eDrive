/**
 * @encoding UTF-8
 */
package com.huishen.edrive.umeng;


import com.huishen.edrive.demand.SuccessDialogActivity;
import com.huishen.edrive.util.AppUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 该类实现一个默认的推送数据广播接收者。
 * <p>
 * 目前设计的流程是这样的： <br/>
 * 1.友盟服务器上推送下来自定义消息； <br/>
 * 2. {@link CustomUMessageHandler}根据消息类型进行广播分发，广播类型定义在 <br/>
 * {@link UmengPushConst}接口中。每种类型分别有不同的广播，以降低多个接收者产生的消耗。 <br/>
 * 3.各Activity在自己的生命周期中动态注册的接收者（如果有）接收该广播并阻断之，传播结束； <br/>
 * 4.如果没有处理，则由默认接收者发送一个Notification，点击Notification再执行相应操作。 <br/>
 * </p>
 * 
 * @author Muyangmin
 * @create 2014-10-31
 * @version 1.0
 */
public final class DefaultPushDataReceiver extends BroadcastReceiver {

	private static final String LOG_TAG = "DefaultPushDataReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOG_TAG, "default receiver has get message.");
		PushData data = (PushData) intent
				.getSerializableExtra(UmengPushConst.EXTRA_PUSHDATA);
		Log.d(LOG_TAG, data.toString());
		switch (data.msgType) {
		case PushData.TYPE_NEWORDER:			//直接跳转
			if (data instanceof NewOrderPushData){
//				intent = RealtimeSnapupActivity.getIntent(context, (NewOrderPushData) data);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(intent);
			}
			break;
		case PushData.TYPE_ORDER_SUCCEESS:
			if (data instanceof OrderSuccessPushData){
				intent =  new Intent(context ,SuccessDialogActivity.class);
				intent.putExtra("tempOrderId", ((OrderSuccessPushData)data).tempOrderId);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				
//				AppUtil.ShowShortToast(context, ""+((OrderSuccessPushData)data).tempOrderId);
			}
			break;
		default:
			break;
		}
	}
}

//NotificationManager nm = (NotificationManager) context
//		.getSystemService(Context.NOTIFICATION_SERVICE);
// 发送一个跳转到消息列表的广播即可。
/*
 * 使用消息类型作为该类Notification的Id，以在更新Notification时相区别。
 * 此外，在PendingIntent#getActivity中必须指定这个id,否则会出现多个Notification
 * 同时存在时只有一个生效且数据错乱的情况。
 */
// PendingIntent pi = PendingIntent.getActivity(context, typeid,
// getAssociatedIntent(context, data),
// PendingIntent.FLAG_UPDATE_CURRENT);
// NotificationCompat.Builder builder = new
// NotificationCompat.Builder(context);
// builder.setSmallIcon(R.drawable.notification_icon)
// .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
// R.drawable.ic_launcher))
// .setContentTitle(data.title)
// .setContentText(Utils.getTransferContent(context, data.content))
// .setTicker(data.title)
// .setDefaults(Notification.DEFAULT_ALL)
// .setAutoCancel(true)
// .setContentIntent(pi);
// nm.notify(typeid, builder.build());
