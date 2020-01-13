package com.icss.mvp.entity;

import java.util.Date;

public class MeasureLoadEveryInfo {
	private String id;
	private String measureId;
	private Date createTime;
	private Date updateTime;
	private String measureValue;
	private String no;

	public MeasureLoadEveryInfo(String no, Date iteEndDate, String measureId, String measureValue) {
		this.no = no;
		this.measureId = measureId;
		this.measureValue = measureValue;
		if (new Date().after(iteEndDate)) {
			this.createTime = iteEndDate;
		} else {
			this.createTime = new Date();
		}
		this.updateTime = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	public String getMeasureValue() {
		return measureValue;
	}

	public void setMeasureValue(String measureValue) {
		this.measureValue = measureValue;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

}