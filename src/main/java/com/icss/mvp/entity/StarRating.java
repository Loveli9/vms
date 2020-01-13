package com.icss.mvp.entity;

import java.util.Date;


public class StarRating {
	private String id;//流水号
	private String name; //项目名称
	private String no; //项目编号
	private String level;//项目星级
	private String cycle; //项目周期
	private int status;//审批状态（0：未通过1：已通过）
	private String peopleNum;//项目总人数
	private String bonusDate;//奖金申请月份
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPeopleNum() {
		return peopleNum;
	}
	public void setPeopleNum(String peopleNum) {
		this.peopleNum = peopleNum;
	}
	public String getBonusDate() {
		return bonusDate;
	}
	public void setBonusDate(String bonusDate) {
		this.bonusDate = bonusDate;
	}
}
