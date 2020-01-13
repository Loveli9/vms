package com.icss.mvp.entity;

public class QualifyTrend {
	private Integer measureId;
	private Integer version;
	private String 	createTime;
	private int qualify;
	private String no;
	
	public Integer getMeasureId() {
		return measureId;
	}
	public void setMeasureId(Integer measureId) {
		this.measureId = measureId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getQualify() {
		return qualify;
	}
	public void setQualify(int qualify) {
		this.qualify = qualify;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}
