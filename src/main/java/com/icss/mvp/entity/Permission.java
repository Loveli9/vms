package com.icss.mvp.entity;

import java.io.Serializable;
import java.util.Date;

import org.omg.CORBA.StringHolder;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Permission implements Serializable{
	
	private static final long serialVersionUID = 1L;
    //角色ID
    private Long permissionid;
    //角色名称
    private String perName;
    //
    private String perDesc;
    //角色对应权限
    private String perAuth;
    //管理角色
    private String managePer;
    //创建者
    private String creator;
    // 创建时间
    private Date creattime;
    private String creatstr;
    //修改者
    private String updator;
    // 修改时间
    private Date upDattime;
    //记录标识
    private Integer ignoe;
    
	public String getCreatstr() {
		return creatstr;
	}
	public void setCreatstr(String creatstr) {
		this.creatstr = creatstr;
	}
	public Long getPermissionid() {
		return permissionid;
	}
	public void setPermissionid(Long permissionid) {
		this.permissionid = permissionid;
	}
	public String getPerName() {
		return perName;
	}
	public void setPerName(String perName) {
		this.perName = perName;
	}
	public String getPerDesc() {
		return perDesc;
	}
	public void setPerDesc(String perDesc) {
		this.perDesc = perDesc;
	}
	public String getPerAuth() {
		return perAuth;
	}
	public void setPerAuth(String perAuth) {
		this.perAuth = perAuth;
	}
	public String getManagePer() {
		return managePer;
	}
	public void setManagePer(String managePer) {
		this.managePer = managePer;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreattime() {
		return creattime;
	}
	public void setCreattime(Date creattime) {
		this.creattime = creattime;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public Date getUpDattime() {
		return upDattime;
	}
	public void setUpDattime(Date upDattime) {
		this.upDattime = upDattime;
	}
	public Integer getIgnoe() {
		return ignoe;
	}
	public void setIgnoe(Integer ignoe) {
		this.ignoe = ignoe;
	}
    
}
