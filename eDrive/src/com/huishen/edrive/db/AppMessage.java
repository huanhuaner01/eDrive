package com.huishen.edrive.db;

import java.io.Serializable;

/**
 * @author zhanghuan
 * @create 2015-4-8
 */
public class AppMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String content;
	private String time;
	private String iconPath ;
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public class MsgType{
		public final int SYSTEM_MESSAGE = 0;
//		public final int ARRANG_MESSAGE = 1;
		public final int BINDING_MESSAGE = 2;
	}


	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}