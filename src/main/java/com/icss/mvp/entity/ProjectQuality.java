package com.icss.mvp.entity;

public class ProjectQuality {
	
	private int id;
	
	private String  name;
	
	/**
	 * 
	 */
	private int nowmonth;
	
	private int lastmonth;
	
	private int green;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNowmonth() {
		return nowmonth;
	}

	public void setNowmonth(int nowmonth) {
		this.nowmonth = nowmonth;
	}

	public int getLastmonth() {
		return lastmonth;
	}

	public void setLastmonth(int lastmonth) {
		this.lastmonth = lastmonth;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}
	
	

}
