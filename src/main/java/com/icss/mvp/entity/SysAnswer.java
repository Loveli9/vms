package com.icss.mvp.entity;

import java.util.Date;

public class SysAnswer {

	private String id;

	private String questionId;

	private String userId;

	private String name;

	private String email;

	private String phone;

	private Date createDate;

	private String acceptState;

	private String content;

	public void init() {
		this.id = "A" + new Date().getTime();
		this.userId = "123456";
		this.name = "admin";
		this.email = "adminchinasoftinc.com";
		this.phone = "110";
		this.createDate = new Date();
		this.acceptState = "0";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId == null ? null : questionId.trim();
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getAcceptState() {
		return acceptState;
	}

	public void setAcceptState(String acceptState) {
		this.acceptState = acceptState == null ? null : acceptState.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}
}