package com.icss.mvp.entity;

import java.util.Date;
import java.util.Map;

public class JenkinsEntity {

	private String result;

	private Integer number;

	private Date timeStamp;

	private String fullDisplayName;

	private Map<String, Object> cmetrics;

	private Map<String, Object> findbugs;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getFullDisplayName() {
		return fullDisplayName;
	}

	public void setFullDisplayName(String fullDisplayName) {
		this.fullDisplayName = fullDisplayName;
	}

	public Map<String, Object> getCmetrics() {
		return cmetrics;
	}

	public void setCmetrics(Map<String, Object> cmetrics) {
		this.cmetrics = cmetrics;
	}

	public Map<String, Object> getFindbugs() {
		return findbugs;
	}

	public void setFindbugs(Map<String, Object> findbugs) {
		this.findbugs = findbugs;
	}

}