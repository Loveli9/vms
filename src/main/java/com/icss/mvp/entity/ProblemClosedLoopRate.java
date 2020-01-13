package com.icss.mvp.entity;

import java.util.List;
import java.util.Set;

public class ProblemClosedLoopRate {

	private Set<String> du;//中软交付部
	
	private List<String> lastMonth;//上月
	
	private List<String> thisMonth;//本月
	
	private String section;
	
	private String timely;
	
	private Integer delayMonth;
	
	private Integer delayYear;
	
    private Integer openMonth;
	
	private Integer openYear;
	
    private Integer closedMonth;
	
	private Integer closedYear;


	public Set<String> getDu() {
		return du;
	}

	public void setDu(Set<String> du) {
		this.du = du;
	}

	public List<String> getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(List<String> lastMonth) {
		this.lastMonth = lastMonth;
	}

	public List<String> getThisMonth() {
		return thisMonth;
	}

	public void setThisMonth(List<String> thisMonth) {
		this.thisMonth = thisMonth;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getTimely() {
		return timely;
	}

	public void setTimely(String timely) {
		this.timely = timely;
	}

	public Integer getDelayMonth() {
		return delayMonth;
	}

	public void setDelayMonth(Integer delayMonth) {
		this.delayMonth = delayMonth;
	}

	public Integer getDelayYear() {
		return delayYear;
	}

	public void setDelayYear(Integer delayYear) {
		this.delayYear = delayYear;
	}

	public Integer getOpenMonth() {
		return openMonth;
	}

	public void setOpenMonth(Integer openMonth) {
		this.openMonth = openMonth;
	}

	public Integer getOpenYear() {
		return openYear;
	}

	public void setOpenYear(Integer openYear) {
		this.openYear = openYear;
	}

	public Integer getClosedMonth() {
		return closedMonth;
	}

	public void setClosedMonth(Integer closedMonth) {
		this.closedMonth = closedMonth;
	}

	public Integer getClosedYear() {
		return closedYear;
	}

	public void setClosedYear(Integer closedYear) {
		this.closedYear = closedYear;
	}
	
}