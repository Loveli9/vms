package com.icss.mvp.entity;

import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;

public class LowOutputEntity extends SerializableSerializer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String no;//项目编号

	private String department;//部门
	
	private String projectId;
	
	private int lowMemberCount;//低产出人数

	private int zeroMemberCount;//无产出人数
	
	private int totalMemberCount;//总人数
	
	private String lowRate;//低产出占比
	
	private String zeroRate;//无产出占比

	public String getLowRate() {
		return lowRate;
	}

	public void setLowRate(String lowRate) {
		this.lowRate = lowRate;
	}

	public String getZeroRate() {
		return zeroRate;
	}

	public void setZeroRate(String zeroRate) {
		this.zeroRate = zeroRate;
	}

	private String reason;//低产出或无产出原因

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public int getLowMemberCount() {
		return lowMemberCount;
	}

	public void setLowMemberCount(int lowMemberCount) {
		this.lowMemberCount = lowMemberCount;
	}

	public int getZeroMemberCount() {
		return zeroMemberCount;
	}

	public void setZeroMemberCount(int zeroMemberCount) {
		this.zeroMemberCount = zeroMemberCount;
	}

	public int getTotalMemberCount() {
		return totalMemberCount;
	}

	public void setTotalMemberCount(int totalMemberCount) {
		this.totalMemberCount = totalMemberCount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
