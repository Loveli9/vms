package com.icss.mvp.entity;

import java.util.Date;

public class UserDetailInfo {
	private String  permissionids;
	private Integer  roleId;

	private String bu;
	private String du;
	private String dept;
	private String hwpdu;
	private String hwzpdu;
	private String pduspdt;

	private String USERID;
	
	private String USERNAME;
    
    private String PASSWORD;
    
    private String REPASSWORD;
    
    private String CREATER;
    
    private Date CREATETIME;
    
    private String UPDATER;
    
    private Date UPDATETIME;
    
    private String IDENTITY;

    private String PARMA;
    
    private String usertype;
    
    
    
    public String  getPermissionids() {
		return permissionids;
	}

	public void setPermissionids(String permissionids) {
		this.permissionids = permissionids;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return USERID;
	}

	public void setUserId(String userId) {
		this.USERID = userId;
	}
    
	public String getIdentity() {
		return IDENTITY;
	}

	public void setIdentity(String identity) {
		this.IDENTITY = identity;
	}

	public String getUserName() {
		return USERNAME;
	}

	public void setUserName(String userName) {
		this.USERNAME = userName;
	}

	public String getPassword() {
		return PASSWORD;
	}

	public void setPassword(String password) {
		this.PASSWORD = password;
	}

	public String getRepassword() {
		return REPASSWORD;
	}

	public void setRepassword(String repassword) {
		this.REPASSWORD = repassword;
	}

	public String getCreater() {
		return CREATER;
	}

	public void setCreater(String creater) {
		this.CREATER = creater;
	}

	public Date getCreateData() {
		return CREATETIME;
	}

	public void setCreateData(Date createData) {
		this.CREATETIME = createData;
	}

	public String getUpdater() {
		return UPDATER;
	}

	public void setUpdater(String updater) {
		this.UPDATER = updater;
	}

	public Date getUpdataData() {
		return UPDATETIME;
	}

	public void setUpdataData(Date updataData) {
		this.UPDATETIME = updataData;
	}

	public String getParma(){
		return PARMA;
	}
	
	public void setParma(String parma) {
		this.PARMA = parma;
	}

	public String getBu() {
		return bu;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getDu() {
		return du;
	}

	public void setDu(String du) {
		this.du = du;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getHwpdu() {
		return hwpdu;
	}

	public void setHwpdu(String hwpdu) {
		this.hwpdu = hwpdu;
	}

	public String getHwzpdu() {
		return hwzpdu;
	}

	public void setHwzpdu(String hwzpdu) {
		this.hwzpdu = hwzpdu;
	}

	public String getPduspdt() {
		return pduspdt;
	}

	public void setPduspdt(String pduspdt) {
		this.pduspdt = pduspdt;
	}
	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
}