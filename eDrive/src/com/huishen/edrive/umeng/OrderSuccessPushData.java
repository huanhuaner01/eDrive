/**
 * 
 */
package com.huishen.edrive.umeng;

import java.util.Map;

/**
 * @author zhanghuan
 *
 */
public final class OrderSuccessPushData extends PushData{
	
	private static final long serialVersionUID = 1L;
	
	public final long tempOrderId;	

	protected OrderSuccessPushData(Map<String, String> map) {
		super(map);
		tempOrderId = getLong(map, "tempOrderId");
	}

	@Override
	public String toString() {
		return "OrderSuccessPushData [tempOrderId=" + tempOrderId + ", msgType="
				+ msgType + "]";
	}
	
}
