package com.huishen.edrive.apointment;

/**
 * 
 * 消息弹出框监听事件
 * @author zhanghuan
 *
 */
public interface MassageListener {
	/**
	 * 确定按钮点击事件
	 */
    public void setCommitClick();
    
    /**
     * 取消按钮监听事件
     */
    public void setCancelClick();
}
