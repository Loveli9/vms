package com.icss.mvp.entity;

import java.util.Date;

public class MeasureRange {
	private String id;
	private String mesureId;
	private String label;
	private String category;
	private String upper;
	private String challenge;
	private String target;
	private String lower;
	private Date createTime;
	private Date updateTime;
	private String no;
	private String collectType;
	private String computeRule;//优先方式
	public String getCollectType() {
		return collectType;
	}
	public void setCollectType(String collectType) {
		this.collectType = "1".equals(collectType)?"手工录入":"自动采集";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMesureId() {
		return mesureId;
	}
	public void setMesureId(String mesureId) {
		this.mesureId = mesureId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUpper() {
		return upper;
	}
	public void setUpper(String upper) {
		this.upper = upper;
	}
	public String getChallenge() {
		return challenge;
	}
	public void setChallenge(String challenge) {
		this.challenge = challenge;
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

	public String getComputeRule() {
		return computeRule;
	}

	public void setComputeRule(String computeRule) {
		this.computeRule = computeRule;
	}
}
