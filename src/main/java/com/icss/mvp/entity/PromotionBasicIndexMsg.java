package com.icss.mvp.entity;

import java.util.List;

public class PromotionBasicIndexMsg {

	private Long id;

	private String name;

	private List<String> years;



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getYears() {
		return years;
	}

	public void setYears(List<String> years) {
		this.years = years;
	}

}
