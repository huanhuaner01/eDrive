package com.huishen.edrive.umeng;

public final class UmengPushConst {
	/**
	 * 每次当应用启动后DeviceToken第一次可用（注册成功后或启动时检测到已注册后）时发出该广播。
	 * 该广播会附带一个参数 {@link #EXTRA_DEVICE_TOKEN}。
	 */
	protected static final String ACTION_DEVICETOKEN_AVALIABLE = "com.huishen.ecoach.DEVICETOKEN_AVALIABLE";
	/**
	 * 数据类型：String。表示友盟的DeviceToken。
	 */
	protected static final String EXTRA_DEVICE_TOKEN = "devicetoken";
	
	protected static final String UMENG_ALIAS_EDRIVE = "EDRIVER";
}
