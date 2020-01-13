package com.icss.mvp.entity;

import java.util.Date;

public class Qmsproblem {
	private String order; //序号
	private Integer pid;// 主键
	private String name;// qms数据统计名称
	private String typeLevel; // 类型级别
	private String source;// 来源
	private String question;// 问题
	private String mainProblems;// 整体项目主要问题
	private String reason; // 整体项目原因分析
	private String improvement;// 整体项目改进措施
	private Integer number;// 数量
	private String proportion;// 项目占比
	private Date modifyTime;//修改时间
	private String level;// 问题级别
	private Date creationTime;//创建时间
	private String type;//快照(0)/发布(1)/当前数据(3)

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getTypeLevel() {
		return typeLevel;
	}

	public void setTypeLevel(String typeLevel) {
		this.typeLevel = typeLevel;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getMainProblems() {
		return mainProblems;
	}

	public void setMainProblems(String mainProblems) {
		this.mainProblems = mainProblems;
	}

	public String getImprovement() {
		return improvement;
	}

	public void setImprovement(String improvement) {
		this.improvement = improvement;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getProportion() {
		return proportion;
	}

	public void setProportion(String proportion) {
		this.proportion = proportion;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
}