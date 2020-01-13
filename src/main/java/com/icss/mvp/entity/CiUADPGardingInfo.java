package com.icss.mvp.entity;

public class CiUADPGardingInfo {
	
	private int id;
	
	private String projectName;
	
	private String owner;
	
	private double sAI;
	
	private double sAIDefect;
	
	private double consistencyDefect;
	
	private double codeNum;
	
	private String action;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public double getsAI() {
		return sAI;
	}

	public void setsAI(double sAI) {
		this.sAI = sAI;
	}

	public double getsAIDefect() {
		return sAIDefect;
	}

	public void setsAIDefect(double sAIDefect) {
		this.sAIDefect = sAIDefect;
	}

	public double getConsistencyDefect() {
		return consistencyDefect;
	}

	public void setConsistencyDefect(double consistencyDefect) {
		this.consistencyDefect = consistencyDefect;
	}

	public double getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(double codeNum) {
		this.codeNum = codeNum;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	

}
