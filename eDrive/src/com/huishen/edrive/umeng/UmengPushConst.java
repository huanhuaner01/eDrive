package com.huishen.edrive.umeng;

/**
 * 存储推送相关的常量。
 * @author Muyangmin
 * @create 2015-2-28
 */
public final class UmengPushConst {
	/**
	 * 每次当应用启动后DeviceToken第一次可用（注册成功后或启动时检测到已注册后）时发出该广播。
	 * 该广播会附带一个参数 {@link #EXTRA_DEVICE_TOKEN}。
	 */
	public static final String ACTION_DEVICETOKEN_AVALIABLE = "com.huishen.ecoach.DEVICETOKEN_AVALIABLE";
	/**
	 * 数据类型：String。表示友盟的DeviceToken。
	 */
	public static final String EXTRA_DEVICE_TOKEN = "devicetoken";
	/**
	 * 别名的类型。
	 */
	protected static final String ALIAS_TYPE = "stu-client";
	/**
	 * 用于在广播中携带推送消息数据。
	 * @see Action
	 */
	protected static final String EXTRA_PUSHDATA = null;
	/**
	 * 动态广播的优先级。
	 */
	protected static final int ACTIVE_RECEIVER_PRIORITY = 100;
	/**
	 * 用于从推送消息参数列表中取出消息类型。
	 */
	protected static final String PARAM_MSG_TYPE = "msgType"; 
	
	/**
	 * 包含推送信息的广播的Action集合。该类中的所有字段都必须注册到AndroidManifest.xml中。
	 * @author Muyangmin
	 * @create 2015-2-28
	 */
	protected static final class Action{
		/**
		 * 未知推送内容。
		 */
		public static final String ACTION_UNKNOWN_PUSHDATA = "com.huishen.edrive.UNKNOWN_PUSHDATA";
		/**
		 * 新订单提醒。
		 */
		public static final String ACTION_NEWORDER_PUSHDATA = "com.huishen.ecoach.NEW_ORDER";
		/**
		 * 订单成功提醒。
		 */
		public static final String ACTION_ORDER_SUCCCESS_PUSHDATA = "com.huishen.edrive.ORDER_SUCCCESS";
	}
	
	/**
	 * 学员下单推送信息。
	 * @author Muyangmin
	 * @create 2015-2-28
	 */
	protected static final class NewOrder{
		/**
		 * 订单ID。
		 */
		public static final String PARAM_ORDERID = "tempOrderId";
		/**
		 * 订单版本号。
		 */
		public static final String PARAM_ORDERVERSION = "versionUID";
		/**
		 * 下单学员姓名。
		 */
		public static final String PARAM_STUDENT_NAME = "stuName";
		/**
		 * 订单需求内容。
		 */
		public static final String PARAM_CONTENT = "content";
		/**
		 * 订单创建时间。
		 */
		public static final String PARAM_CREATETIME = "createTime";
		/**
		 * 订单截止时间。
		 */
		public static final String PARAM_DEADLINE = "endTime";
		/**
		 * 订单语音文件相对位置。
		 */
		public static final String PARAM_VOICE_PATH = "voicePath";
		/**
		 * 学员所在行政区。
		 */
		public static final String PARAM_CITY = "city";
		/**
		 * 学员详细位置。
		 */
		public static final String PARAM_DETAIL_POSITION = "pos";
		/**
		 * 学员距离远近。
		 */
		public static final String PARAM_DISTANCE = "distance";
	}
	
	
}
