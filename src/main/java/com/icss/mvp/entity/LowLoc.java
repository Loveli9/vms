package com.icss.mvp.entity;

public class LowLoc {
	private String projNo;
	private String pdu;
	private String name;
	private String account;
	private Integer loc;
	private String sureLowloc;
	private String lowLocReason;
	private String remark;
	private String month;
	private String standard;

	public String getProjNo() {
		return projNo;
	}

	public void setProjNo(String projNo) {
		this.projNo = projNo;
	}

	public String getPdu() {
		return pdu;
	}

	public void setPdu(String pdu) {
		this.pdu = pdu;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getLoc() {
		return loc;
	}

	public void setLoc(Integer loc) {
		this.loc = loc;
	}

	public String getLowLocReason() {
		return lowLocReason;
	}

	public void setLowLocReason(String lowLocReason) {
		this.lowLocReason = lowLocReason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSureLowloc() {
		return sureLowloc;
	}

	public void setSureLowloc(String sureLowloc) {
		this.sureLowloc = sureLowloc;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}
}