package com.icss.mvp.entity;

import java.util.Date;

public class ReportProblemAnalysis extends ProjectInfo{

	private String department;
	
	private String closed;
	
	private Integer delay = 0;
	
	private Integer problem = 0;
	
	private Integer aar = 0;
	
	private Integer back = 0;
	
	private Integer audit = 0;
	
    private double problemClosed = 0;
	
	private Integer problemDelay = 0;
	
    private double aarClosed = 0;
	
	private Integer aarDelay = 0;
	
    private double backClosed = 0;
	
	private Integer backDelay = 0;
	
    private double auditClosed = 0;
	
	private Integer auditDelay = 0;
	
	private Date finishTime;
	
	private Date actualTime;
	
	private String state;
	
	private String collection;

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Date getActualTime() {
		return actualTime;
	}

	public void setActualTime(Date actualTime) {
		this.actualTime = actualTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getClosed() {
		return closed;
	}

	public void setClosed(String closed) {
		this.closed = closed;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Integer getProblem() {
		return problem;
	}

	public void setProblem(Integer problem) {
		this.problem = problem;
	}

	public Integer getAar() {
		return aar;
	}

	public void setAar(Integer aar) {
		this.aar = aar;
	}

	public Integer getBack() {
		return back;
	}

	public void setBack(Integer back) {
		this.back = back;
	}

	public Integer getAudit() {
		return audit;
	}

	public void setAudit(Integer audit) {
		this.audit = audit;
	}

	public double getProblemClosed() {
		return problemClosed;
	}

	public void setProblemClosed(double problemClosed) {
		this.problemClosed = problemClosed;
	}

	public Integer getProblemDelay() {
		return problemDelay;
	}

	public void setProblemDelay(Integer problemDelay) {
		this.problemDelay = problemDelay;
	}

	public double getAarClosed() {
		return aarClosed;
	}

	public void setAarClosed(double aarClosed) {
		this.aarClosed = aarClosed;
	}

	public Integer getAarDelay() {
		return aarDelay;
	}

	public void setAarDelay(Integer aarDelay) {
		this.aarDelay = aarDelay;
	}

	public double getBackClosed() {
		return backClosed;
	}

	public void setBackClosed(double backClosed) {
		this.backClosed = backClosed;
	}

	public Integer getBackDelay() {
		return backDelay;
	}

	public void setBackDelay(Integer backDelay) {
		this.backDelay = backDelay;
	}

	public double getAuditClosed() {
		return auditClosed;
	}

	public void setAuditClosed(double auditClosed) {
		this.auditClosed = auditClosed;
	}

	public Integer getAuditDelay() {
		return auditDelay;
	}

	public void setAuditDelay(Integer auditDelay) {
		this.auditDelay = auditDelay;
	}
}
