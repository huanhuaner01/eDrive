/**
 * @encoding UTF-8
 */
package com.huishen.edrive.umeng;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.huishen.edrive.R;
import com.huishen.edrive.center.ListActivity;
import com.huishen.edrive.db.AppMessage;
import com.huishen.edrive.db.MeaasgeDbManager;
import com.huishen.edrive.util.AppController;
import com.huishen.edrive.util.Const;
import com.huishen.edrive.util.Prefs;
import com.huishen.edrive.widget.CalendarUtil;
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
		Log.i(LOG_TAG, "custom message received.extra="+msg.extra+",custom="+msg.custom);
		Map<String, String> args = resolveCustomMsg(msg.custom);
		if (args!=null){
			Log.d(LOG_TAG, args.toString());
			dispatchMessage(context, args);	
		}
    }
	
	@Override
	public void dealWithNotificationMessage(Context context, UMessage msg){
		//友盟v1.7之后自定义参数extra方式只对notification生效，因此需要手动解析字符串。
		Log.i(LOG_TAG, "Notification message received.extra="+msg.extra+",msg="+msg.toString());
//		super(context,msg);
//		Map<String, String> args = resolveCustomMsg(msg.custom);
//		if (args!=null){
//			Log.d(LOG_TAG, args.toString());
//			dispatchMessage(context, args);	
//		}
		simpleNotice(context,msg);
		insertDB(context,msg);
		Intent intent = new Intent(UmengPushConst.Action.ACTION_MSG);
	    
		intent.putExtra("msg_type", msg.extra.get("msgType"));
		if(msg.extra.get("msgType").equals("2002")){
			if(msg.extra.get("isBind").equals("1")){
				 
	             Prefs.writeString(context, Const.USER_COACH_ID, msg.extra.get("coachId"));
			}else{
				 Prefs.writeString(context, Const.USER_COACH_ID, "");
			}
			intent.putExtra("isbind",msg.extra.get("isBind"));
		}
		//解绑消息
		if(msg.extra.get("msgType").equals("2003")){
			if(msg.extra.get("unbindStatus").equals("2")){
				 Prefs.writeString(context, Const.USER_COACH_ID, "");
				 Log.i(LOG_TAG, "kwyi");
			}
			intent.putExtra("unbindStatus", msg.extra.get("unbindStatus"));
		}
		Prefs.writeString(context, Const.NEW_MSG, 1+"");
		context.sendOrderedBroadcast(intent, null);
	}
	
	/**
	 * 保存数据库
	 * @param context
	 * @param msg
	 */
	private void insertDB(Context context, UMessage msg){
		Log.i(LOG_TAG, "massage is "+msg.toString());
		MeaasgeDbManager db = new MeaasgeDbManager(context);
		AppMessage appmsg = new AppMessage();
		appmsg.setContent(msg.text); //消息内容
		appmsg.setTitle(msg.title); //消息标题
		if(msg.extra.get("headline") != null&&!msg.extra.get("headline").equals("")){
			appmsg.setTitle(msg.extra.get("headline")); //消息标题
		}
		appmsg.setType(Integer.parseInt(msg.extra.get("msgType"))); //消息类型
		if(msg.extra.get("img")!= null &&!msg.extra.get("headline").equals("")){
		  appmsg.setIconPath(msg.extra.get("img")); //图片路径
		}
		appmsg.setTime(CalendarUtil.getCurrentTime()) ; //消息时间
//		db.clearAllMessages(); //测试删除
		if(db.saveAppMessage(appmsg)){
			Log.i(LOG_TAG, "msg is insert to db! id is "+appmsg.getId());
		}else{
			Log.i(LOG_TAG, "msg is not insert to db");
		}
		
	}
	  /**
	   * 显示在通知栏
	   * @param context
	   * @param msg
	   */
	   public void simpleNotice(Context context, UMessage msg) {
		    NotificationManager nm = (NotificationManager) AppController.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
		    Builder mBuilder = new Builder(context);
	        mBuilder.setTicker(msg.ticker);
	        mBuilder.setSmallIcon(R.drawable.ic_launcher);
	        mBuilder.setContentTitle(msg.title);
	        mBuilder.setContentText(msg.text);
	        mBuilder.setDefaults(Notification.DEFAULT_ALL);
	        //设置点击一次后消失（如果没有点击事件，则该方法无效。）
	        mBuilder.setAutoCancel(true); 
	        //点击通知之后需要跳转的页面
	        Intent resultIntent = new Intent(context, ListActivity.class);
	        
	        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        PendingIntent pIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	        mBuilder.setContentIntent(pIntent);
	        // mId allows you to update the notification later on.
	        nm.notify(2, mBuilder.build());
		   
		 //Notification的滚动提示  
//		   String tickerText = msg.ticker;  
//		   //Notification的图标，一般不要用彩色的  
//		   int icon =R.drawable.ic_launcher;  
//		      
//		   //contentTitle和contentText都是标准的Notification View的内容  
//		   //Notification的内容标题，拖下来后看到的标题  
//		   String contentTitle=msg.title;  
//		   //Notification的内容  
//		   String contentText=msg.text;  
//		      
//		   //Notification的Intent，即点击后转向的Activity  
//		   Intent notificationIntent = new Intent(context, ListActivity.class);  
////		   notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);  
//		   PendingIntent contentIntent = PendingIntent.getActivity(context, 0,   
//		           notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);  
//		      
//		   //创建Notifcation  
//		   Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());  
//		   //设定Notification出现时的声音，一般不建议自定义  
//		   notification.defaults = Notification.DEFAULT_ALL;   
//		   //指定Flag，Notification.FLAG_AUTO_CANCEL意指点击这个Notification后，立刻取消自身  
//		   //这符合一般的Notification的运作规范  
//		   notification.flags|=Notification.FLAG_AUTO_CANCEL;  
//		   notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);  
//		     
//		   //显示这个notification  
//		   nm.notify(1, notification); 
	       
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
		case PushData.TYPE_ORDER_SUCCEESS:
			//直接弹出
			intent = new Intent(UmengPushConst.Action.ACTION_ORDER_SUCCCESS_PUSHDATA);
			data = (T) new OrderSuccessPushData(extra);
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
