package com.icss.mvp.entity;

import java.util.Date;

public class ProjectConfigInfo {
	
	private Integer id;//主键
	private Integer isDeleted;//逻辑删除标识 1:已删除数据，0:正常数据
	private String projectId;//项目编号NO
	private Integer repositoryId;//仓库、平台地址id
	
	private Date createTime;
	private Date modifyTime;
	
	
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Integer getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(Integer repositoryId) {
		this.repositoryId = repositoryId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
