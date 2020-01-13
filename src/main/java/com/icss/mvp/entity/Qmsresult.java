package com.icss.mvp.entity;

import java.util.Date;

@SuppressWarnings("serial")
public class Qmsresult extends Qmslist {
	private String no;// 项目编号
	private Integer qmsId;// QMS成熟度问题id
	private String source;// 标签名
	private String involve;// 是否符合
	private String dutyPersonName;// 得分
	private String evidence;// 问题描述
	private String problemType;// 问题类别
	private String majorProblem;// 主要存在问题
	private String improvementMeasure;// 改进措施
	private Date planClosedTime;// 计划闭环时间
	private Date actualClosedTime;// 实际闭环时间
	private String dutyPerson;// 责任人
	private String followPerson;// 跟踪人
	private String progress;// 进展
	private String state;// 状态
	private Date creationTime;//创建时间
	private Date modifyTime;// 修改时间

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Integer getQmsId() {
		return qmsId;
	}

	public void setQmsId(Integer qmsId) {
		this.qmsId = qmsId;
	}

	public String getInvolve() {
		return involve;
	}

	public void setInvolve(String involve) {
		this.involve = involve;
	}

	public String getImprovementMeasure() {
		return improvementMeasure;
	}

	public void setImprovementMeasure(String improvementMeasure) {
		this.improvementMeasure = improvementMeasure;
	}

	public Date getPlanClosedTime() {
		return planClosedTime;
	}

	public void setPlanClosedTime(Date planClosedTime) {
		this.planClosedTime = planClosedTime;
	}

	public String getDutyPerson() {
		return dutyPerson;
	}

	public void setDutyPerson(String dutyPerson) {
		this.dutyPerson = dutyPerson;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getActualClosedTime() {
		return actualClosedTime;
	}

	public void setActualClosedTime(Date actualClosedTime) {
		this.actualClosedTime = actualClosedTime;
	}

	public String getDutyPersonName() {
		return dutyPersonName;
	}

	public void setDutyPersonName(String dutyPersonName) {
		this.dutyPersonName = dutyPersonName;
	}

	public String getEvidence() {
		return evidence;
	}

	public void setEvidence(String evidence) {
		this.evidence = evidence;
	}

	public String getProblemType() {
		return problemType;
	}

	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}

	public String getMajorProblem() {
		return majorProblem;
	}

	public void setMajorProblem(String majorProblem) {
		this.majorProblem = majorProblem;
	}

	public String getFollowPerson() {
		return followPerson;
	}

	public void setFollowPerson(String followPerson) {
		this.followPerson = followPerson;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}