package com.icss.mvp.entity;

public class MeasureVo{
	private int id;
	private String upper;
	private String lower;
	private String value;
	private String color;
	private String createTime;
	private String no;
	private String projectName;
	private String hwzpdu;
	private String pduSpdt;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getHwzpdu() {
		return hwzpdu;
	}
	public void setHwzpdu(String hwzpdu) {
		this.hwzpdu = hwzpdu;
	}
	public String getPduSpdt() {
		return pduSpdt;
	}
	public void setPduSpdt(String pduSpdt) {
		this.pduSpdt = pduSpdt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpper() {
		return upper;
	}
	public void setUpper(String upper) {
		this.upper = upper;
	}
	public String getLower() {
		return lower;
	}
	public void setLower(String lower) {
		this.lower = lower;
	}
}
