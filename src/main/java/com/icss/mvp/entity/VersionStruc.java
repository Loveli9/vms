package com.icss.mvp.entity;

import java.util.Date;

/**
 * 6+1-版本级构建
 * @author Administrator
 *
 */
public class VersionStruc {
	private String no;//项目编号
	private Date createTime;//创建时间
	private Date modifyTime;//修改时间
	private Integer isDeleted;//删除标识
	
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
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
