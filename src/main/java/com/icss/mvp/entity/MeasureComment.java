package com.icss.mvp.entity;

import java.util.Date;

public class MeasureComment {
	private int id;
	private int measureId;
	private int labelId;
	private String labelTitle;
	private String proNo;
	private Date startDate;
	private Date endDate;
	private int is_deleted;
	private String target;
	private String lower;
	private String upper;
	private String unit;
	private String measureCategory;
	private String measureName;
	private String measureValue;
	private String comment;
	private String oldValue;
	private String collectType;
	private Date createTime;
	private Date updateTime;
	private String computeRule;
	private String challenge;
	private Integer changeValue;
	private String month;
	private String changeMonth;
	private String light;
	private String historyValue;
	
	
	public String getChangeMonth() {
		return changeMonth;
	}
	public void setChangeMonth(String changeMonth) {
		this.changeMonth = changeMonth;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getId() {
		return id;
	}

	public String getComputeRule() {
		return computeRule;
	}

	public void setComputeRule(String computeRule) {
		this.computeRule = computeRule;
	}

	public String getChallenge() {
		return challenge;
	}
	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMeasureId() {
		return measureId;
	}

	public void setMeasureId(int measureId) {
		this.measureId = measureId;
	}

	public int getLabelId() {
		return labelId;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	public String getLabelTitle() {
		return labelTitle;
	}

	public void setLabelTitle(String labelTitle) {
		this.labelTitle = labelTitle;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}

	public String getProNo() {
		return proNo;
	}

	public void setProNo(String proNo) {
		this.proNo = proNo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getLower() {
		return lower;
	}

	public void setLower(String lower) {
		this.lower = lower;
	}

	public String getUpper() {
		return upper;
	}

	public void setUpper(String upper) {
		this.upper = upper;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMeasureName() {
		return measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public String getMeasureValue() {
		return measureValue;
	}

	public void setMeasureValue(String measureValue) {
		this.measureValue = measureValue;
	}

	public String getMeasureCategory() {
		return measureCategory;
	}

	public void setMeasureCategory(String measureCategory) {
		this.measureCategory = measureCategory;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getCollectType() {
		return collectType;
	}
	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getChangeValue() {
		return changeValue;
	}
	public void setChangeValue(Integer changeValue) {
		this.changeValue = changeValue;
	}
	public String getLight() {
		return  light;
	}
	protected void setLight(String light) {
		this.light = light;
	}

	public String getHistoryValue() {
		return historyValue;
	}

	public void setHistoryValue(String historyValue) {
		this.historyValue = historyValue;
	}
}
