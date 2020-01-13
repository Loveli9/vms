package com.icss.mvp.entity;

public class ProjectStateNumber {
	
	private String pdu;
	
	private String pduspt;
	
	private int redState;
	
	private int yellowState;
	
	private int greenState;
	
	private int redState1;
	
	private int yellowState1;
	
	private int greenState1;
	
	private int red;
	
	private int yellow;
	
	private int red1;
	
	private int yellow1;

	private String projectNo;

	private String name;

	private String pm;

	private String qa;


	public String getPduspt() {
		return pduspt;
	}

	public void setPduspt(String pduspt) {
		this.pduspt = pduspt;
	}

	public int getRed1() {
		return red1;
	}

	public void setRed1(int red1) {
		this.red1 = red1;
	}

	public int getYellow1() {
		return yellow1;
	}

	public void setYellow1(int yellow1) {
		this.yellow1 = yellow1;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getYellow() {
		return yellow;
	}

	public void setYellow(int yellow) {
		this.yellow = yellow;
	}

	public String getPdu() {
		return pdu;
	}

	public void setPdu(String pdu) {
		this.pdu = pdu;
	}

	public int getRedState() {
		return redState;
	}

	public void setRedState(int redState) {
		this.redState = redState;
	}

	public int getYellowState() {
		return yellowState;
	}

	public void setYellowState(int yellowState) {
		this.yellowState = yellowState;
	}

	public int getGreenState() {
		return greenState;
	}

	public void setGreenState(int greenState) {
		this.greenState = greenState;
	}

	public int getRedState1() {
		return redState1;
	}

	public void setRedState1(int redState1) {
		this.redState1 = redState1;
	}

	public int getYellowState1() {
		return yellowState1;
	}

	public void setYellowState1(int yellowState1) {
		this.yellowState1 = yellowState1;
	}

	public int getGreenState1() {
		return greenState1;
	}

	public void setGreenState1(int greenState1) {
		this.greenState1 = greenState1;
	}

	@Override
	public String toString() {
		return "ProjectStateNumber [pdu=" + pdu + ", redState=" + redState + ", yellowState=" + yellowState
				+ ", greenState=" + greenState + ", redState1=" + redState1 + ", yellowState1=" + yellowState1
				+ ", greenState1=" + greenState1 + "]";
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getQa() {
		return qa;
	}

	public void setQa(String qa) {
		this.qa = qa;
	}
}
