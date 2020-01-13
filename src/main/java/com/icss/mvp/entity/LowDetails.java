package com.icss.mvp.entity;

import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;

public class LowDetails extends SerializableSerializer{

	private static final long serialVersionUID = 1L;
	
	private String staff;//员工姓名
	
	private String account;//工号
	
	private String department;//项目
	
	private String subproduct;//子产品
	
	private String spdu;//pdu
	
	private String project;//部门
	
	private int sum;//总提交量
	
	private int num;//提交次数
	
	private int javaNum;//java代码提交量
	
	private int jsNum;//js代码提交量
	
	private int ccNum;//c语言代码提交量

	public int getJavaNum() {
		return javaNum;
	}

	public void setJavaNum(int javaNum) {
		this.javaNum = javaNum;
	}

	public int getJsNum() {
		return jsNum;
	}

	public void setJsNum(int jsNum) {
		this.jsNum = jsNum;
	}

	public int getCcNum() {
		return ccNum;
	}

	public void setCcNum(int ccNum) {
		this.ccNum = ccNum;
	}

	public String getStaff() {
		return staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSubproduct() {
		return subproduct;
	}

	public void setSubproduct(String subproduct) {
		this.subproduct = subproduct;
	}

	public String getSpdu() {
		return spdu;
	}

	public void setSpdu(String spdu) {
		this.spdu = spdu;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
}
