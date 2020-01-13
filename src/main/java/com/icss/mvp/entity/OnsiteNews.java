package com.icss.mvp.entity;

import java.util.Date;

public class OnsiteNews {

	private String no;
	
	private String projectName;
	
	private String demand;
	
	private String risk;
	
	private String qms;
	
	private String readTime;

	public String getReadTime() {
		return readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime.substring(0,readTime.length()-2);
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDemand() {
		return demand;
	}

	public void setDemand(String demand) {
		this.demand = demand;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public String getQms() {
		return qms;
	}

	public void setQms(String qms) {
		this.qms = qms;
	}
	
}
