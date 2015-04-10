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
	private String title ; 
	private int type;
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	@Override
	public String toString() {
		return "id:"+getId()+" content:"+getContent()+" time:"+getTime()
				+" iconpath:"+getIconPath()+" type:"+getType()+" title:"+getTitle();
	}
	
}