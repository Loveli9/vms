package com.icss.mvp.entity;

import java.util.Date;

public class TestCaseInput {
	private String proNO;
	private String hwAccount;
	private Date date;
	private Integer testCaseType;
	private String testCaseValue;
	public String getProNO() {
		return proNO;
	}
	public void setProNO(String proNO) {
		this.proNO = proNO;
	}
	public String getHwAccount() {
		return hwAccount;
	}
	public void setHwAccount(String hwAccount) {
		this.hwAccount = hwAccount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getTestCaseType() {
		return testCaseType;
	}
	public void setTestCaseType(Integer testCaseType) {
		this.testCaseType = testCaseType;
	}
	public String getTestCaseValue() {
		return testCaseValue;
	}
	public void setTestCaseValue(String testCaseValue) {
		this.testCaseValue = testCaseValue;
	}

	
}