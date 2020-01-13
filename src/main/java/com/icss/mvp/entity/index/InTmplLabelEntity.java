package com.icss.mvp.entity.index;

import com.icss.mvp.entity.common.CommonEntity;

/**
 * 指标流程模板
 * @author: Admin
 */
public class InTmplLabelEntity extends CommonEntity {

	/**部门分类*/
	private String section;
	/** 指标流程*/
	private String label;
	/** 指标分类*/
	private String category;
	/**流程顺序*/
	private String order;



	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
