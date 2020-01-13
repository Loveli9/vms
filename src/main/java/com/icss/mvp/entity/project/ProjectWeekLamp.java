package com.icss.mvp.entity.project;

import java.util.List;

public class ProjectWeekLamp {
	
	private String lamp;
	
	private String status;
	
	private List<Object> red;
	
	private List<Object> yellow;
	
	private List<Object> green;
	
	private List<String> monthList;
	
	private List<Integer> echars;
	
	private List<Integer> lastMonth;
	
	private List<String> listMonth1;

	public String getLamp() {
		return lamp;
	}

	public void setLamp(String lamp) {
		this.lamp = lamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Object> getRed() {
		return red;
	}

	public void setRed(List<Object> red) {
		this.red = red;
	}

	public List<Object> getYellow() {
		return yellow;
	}

	public void setYellow(List<Object> yellow) {
		this.yellow = yellow;
	}

	public List<Integer> getEchars() {
		return echars;
	}

	public void setEchars(List<Integer> echars) {
		this.echars = echars;
	}

	public List<Object> getGreen() {
		return green;
	}

	public void setGreen(List<Object> green) {
		this.green = green;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public List<Integer> getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(List<Integer> lastMonth) {
		this.lastMonth = lastMonth;
	}

	public List<String> getListMonth1() {
		return listMonth1;
	}

	public void setListMonth1(List<String> listMonth1) {
		this.listMonth1 = listMonth1;
	}
	
}
