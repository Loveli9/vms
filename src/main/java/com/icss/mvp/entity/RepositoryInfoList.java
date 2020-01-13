package com.icss.mvp.entity;

import java.util.List;

public class RepositoryInfoList {
	
	private String projNo;//项目编号
	private String selectedIds;//选择项目
	private String id;//采集id
	private String name;//采集名称
	/**
	 * RepositoryInfo传入，需要
	 * type,url,branch(可为空),scope(可为空),otherAccount
	 */
	private List<RepositoryInfo> repositoryInfos;
	
	public String getProjNo() {
		return projNo;
	}
	public void setProjNo(String projNo) {
		this.projNo = projNo;
	}
	public List<RepositoryInfo> getRepositoryInfos() {
		return repositoryInfos;
	}
	public void setRepositoryInfos(List<RepositoryInfo> repositoryInfos) {
		this.repositoryInfos = repositoryInfos;
	}
	public String getSelectedIds() {
		return selectedIds;
	}
	public void setSelectedIds(String selectedIds) {
		this.selectedIds = selectedIds;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
