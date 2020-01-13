package com.icss.mvp.entity;

import java.util.Date;

public class VisitRecord {
	private String ip;
	private String uName;
	private String uId;
	private int uRole;
	private Date loginTime;
//	private Date outTime;
	
	private long totalNum;//总访问量
	private long currentNum;//当月访问量
	private long lastNum;//上月访问量
	private double growRate;//增长率
	private long lastTotalNum;//上月总访问量
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public int getuRole() {
		return uRole;
	}
	public void setuRole(int uRole) {
		this.uRole = uRole;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public long getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}
	public long getCurrentNum() {
		return currentNum;
	}
	public void setCurrentNum(long currentNum) {
		this.currentNum = currentNum;
	}
	public double getGrowRate() {
		return growRate;
	}
	public void setGrowRate(double growRate) {
		this.growRate = growRate;
	}
	public long getLastNum() {
		return lastNum;
	}
	public void setLastNum(long lastNum) {
		this.lastNum = lastNum;
	}

	public long getLastTotalNum() {
		return lastTotalNum;
	}

	public void setLastTotalNum(long lastTotalNum) {
		this.lastTotalNum = lastTotalNum;
	}
}
