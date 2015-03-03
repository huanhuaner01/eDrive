package com.huishen.edrive.umeng;

import java.io.Serializable;
import java.util.Map;

import android.util.Log;


/**
 * 代表所有推送消息的父类，主要提供基本的框架支持。
 * 该类的子类所有的字段都应该仿效该类作为Final字段，方便操作并可以提升性能。
 * 并且，子类的构造器必须显式调用父类构造器。
 * @author Muyangmin
 * @create 2015-2-28
 */
public abstract class PushData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 未知类型的消息推送。
	 */
	public static final int TYPE_UNKNOWN = -1;
	/**
	 * 新订单推送。
	 */
	public static final int TYPE_NEWORDER = 1001;
	
	public final int msgType; // 消息类型

	public PushData(Map<String, String> map) {
		msgType = Integer.parseInt(map.get(UmengPushConst.PARAM_MSG_TYPE));
	}

	public final int getPushType() {
		return msgType;
	}

	/**
	 * 获取从参数中解析出来的消息类型。
	 * @param extra 友盟参数列表。
	 * @return 返回消息类型，若为 {@link #TYPE_UNKNOWN}，则表示参数有误。
	 */
	public static int getPushDataType(Map<String, String> extra) {
		int type = TYPE_UNKNOWN;
		try {
			type = Integer.parseInt(extra.get(UmengPushConst.PARAM_MSG_TYPE));
		} catch (NumberFormatException e) {
			Log.e(PushData.class.getSimpleName(), "bad format message!");
			e.printStackTrace();
		}
		return type;
	}
}
