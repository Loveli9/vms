package com.icss.mvp.entity;

import java.util.Date;

/**
 * 版本级构建时长指标
 * @author Administrator
 *
 */
public class VersionStrucInfo {
	private String id;
	private String no;
	private Date createTime;
	private Date updateTime;
	private Date startTime;
	private Date endTime;
	private String buildType;
	private String buildResult;
	private int isDeleted;
	private String buildContent;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
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
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getBuildType() {
		return buildType;
	}
	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}
	public String getBuildResult() {
		return buildResult;
	}
	public void setBuildResult(String buildResult) {
		this.buildResult = buildResult;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getBuildContent() {
		return buildContent;
	}
	public void setBuildContent(String buildContent) {
		this.buildContent = buildContent;
	}
	
}
