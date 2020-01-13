package com.icss.mvp.entity;

import java.util.Date;

public class CollectionJobInfo {
	
	private Integer id;//主键
	private String no; //项目编号
	private String proName; //项目名称
	private String name; //采集任务名称
	private String types; //对接平台类型队列
	private String ids;//repository表id，;隔开
	private String selectedIds;//选中的repository表id，;隔开
	private String progressLast;//采集完成度
	private String num;//采集完成度
	private Date modifyTime;//采集完成度
	
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getProgressLast() {
		return progressLast;
	}
	public void setProgressLast(String progressLast) {
		this.progressLast = progressLast;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getSelectedIds() {
		return selectedIds;
	}
	public void setSelectedIds(String selectedIds) {
		this.selectedIds = selectedIds;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	
}
