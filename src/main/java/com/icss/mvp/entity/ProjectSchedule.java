package com.icss.mvp.entity;

import java.util.Date;

public class ProjectSchedule {
	private String no;//项目编号
    private String node;//节点
    private Date planDate;//计划签发日期
    private Date actualDate;//实际签发日期
    private String deviationRate;//偏差率
    
    
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public Date getActualDate() {
		return actualDate;
	}
	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}
	public String getDeviationRate() {
		return deviationRate;
	}
	public void setDeviationRate(String deviationRate) {
		this.deviationRate = deviationRate;
	}
    
}
