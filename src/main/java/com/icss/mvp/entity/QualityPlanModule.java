package com.icss.mvp.entity;

import com.icss.mvp.entity.common.request.BaseRequest;
import com.icss.mvp.util.UUIDUtil;

@SuppressWarnings("serial")
public class QualityPlanModule extends BaseRequest {
	private String no;
	private String module;
	private String moduleName;
	private Integer order;

	public void init() {
		this.moduleName = UUIDUtil.getNew();
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
}
