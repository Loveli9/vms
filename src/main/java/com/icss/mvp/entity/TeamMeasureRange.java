package com.icss.mvp.entity;

import java.util.Date;

public class TeamMeasureRange {
	private String id;
	private String mesureId;
	private String upper;
	private String challenge;
	private String target;
	private String lower;
	private Date createTime;
	private Date updateTime;
	private String teamId;
	private String collectType;
	private String  computeRule;
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
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getCollectType() {
		return collectType;
	}
	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}

	public String getComputeRule() {
		return computeRule;
	}

	public void setComputeRule(String computeRule) {
		this.computeRule = computeRule;
	}
}
