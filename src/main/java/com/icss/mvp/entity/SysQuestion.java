package com.icss.mvp.entity;

import java.util.Date;

public class SysQuestion {

	private String id;

	private String userId;

	private String name;

	private String email;

	private String phone;

	private String type;

	private Date createDate;

	private String solveState;

	private String stopState;

	private Date stopDate;

	private String stopUser;

	private String content;

	public void init() {
		this.id = "Q" + new Date().getTime();
		this.createDate = new Date();
		this.solveState = "未解决";//解决状态 0:未解决  1:已解决  2:已拒绝
		this.stopState = "1";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getSolveState() {
		return solveState;
	}

	public void setSolveState(String solveState) {
		this.solveState = solveState == null ? null : solveState.trim();
	}

	public String getStopState() {
		return stopState;
	}

	public void setStopState(String stopState) {
		this.stopState = stopState == null ? null : stopState.trim();
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	public String getStopUser() {
		return stopUser;
	}

	public void setStopUser(String stopUser) {
		this.stopUser = stopUser == null ? null : stopUser.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}
}