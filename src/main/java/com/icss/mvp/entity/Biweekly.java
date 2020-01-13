package com.icss.mvp.entity;

import java.util.List;

public class Biweekly {
	
	private List<String> date;
	
	private List<String> thisMonth;

    private List<String> lastMonth;

	public List<String> getDate() {
		return date;
	}

	public void setDate(List<String> date) {
		this.date = date;
	}

	public List<String> getThisMonth() {
		return thisMonth;
	}

	public void setThisMonth(List<String> thisMonth) {
		this.thisMonth = thisMonth;
	}

	public List<String> getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(List<String> lastMonth) {
		this.lastMonth = lastMonth;
	}
}
