package com.icss.mvp.entity;

import java.util.Date;

public class TeamLabel {
	private Integer id; // 主键Id
	private Date createTime; // 创建时间
	private Date modifyTime; // 修改时间
	private Integer isDeleted; // 逻辑删除标识,1:已删除数据，0:正常数据
	private String title; // 显示名称
	private String category; // 类目,各事业部等
	
	private String plId;//流程配置id
	private String order;//流程排序
	private String teamId;//团队编号
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPlId() {
		return plId;
	}
	public void setPlId(String plId) {
		this.plId = plId;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

}
