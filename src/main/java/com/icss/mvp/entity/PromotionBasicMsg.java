package com.icss.mvp.entity;

import java.util.List;

public class PromotionBasicMsg {

	private String title;

	private Integer id;

	private List<PromotionBasicIndexMsg> list;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<PromotionBasicIndexMsg> getList() {
		return list;
	}

	public void setList(List<PromotionBasicIndexMsg> list) {
		this.list = list;
	}

}
