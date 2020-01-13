package com.icss.mvp.entity;

import java.util.Date;

public class CodeGainType {
	private Integer id;
	private String type;
	private String no;
	private Integer parameterId;
	private Date createTime;
	private Date modifyTime;
	//项目结项状态
	private Integer closeStatus;
	//项目PO
	private String po;
	//项目名称
	private String name;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Integer getParameterId() {
		return parameterId;
	}
	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}
	public Integer getCloseStatus() {
		return closeStatus;
	}
	public void setCloseStatus(Integer closeStatus) {
		this.closeStatus = closeStatus;
	}
	public String getPo() {
		return po;
	}
	public void setPo(String po) {
		this.po = po;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
