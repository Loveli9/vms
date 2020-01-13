package com.icss.mvp.entity;

import java.io.Serializable;

/**
 * 度量指标表
 *
 * @author zqq
 */
public class ParameterInfoNew implements Serializable {
	// 主键ID

	private Integer id;
	// 创建时间

	private java.util.Date createTime;
	// 修改日期

	private java.util.Date modifyTime;
	// 是否删除1:已删除数据，0:正常数据

	private Integer isDelete;
	// 指标名称

	private String name;
	// 单位

	private String unit;
	// 上限值

	private String upper;
	// 下限值

	private String lower;
	// 指标目标值

	private String target;
	// 计算公式

	private String content;
	// 类目

	private String category;
	// 采集方式

	private String collectType;

	/** 主键ID */
	public void setId(Integer id) {
		this.id = id;
	}

	/** 主键ID */
	public Integer getId() {
		return this.id;
	}

	/** 创建时间 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	/** 创建时间 */
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	/** 修改日期 */
	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/** 修改日期 */
	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}

	/** 是否删除1:已删除数据，0:正常数据 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	/** 是否删除1:已删除数据，0:正常数据 */
	public Integer getIsDelete() {
		return this.isDelete;
	}

	/** 指标名称 */
	public void setName(String name) {
		this.name = name;
	}

	/** 指标名称 */
	public String getName() {
		return this.name;
	}

	/** 单位 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/** 单位 */
	public String getUnit() {
		return this.unit;
	}

	/** 上限值 */
	public void setUpper(String upper) {
		this.upper = upper;
	}

	/** 上限值 */
	public String getUpper() {
		return this.upper;
	}

	/** 下限值 */
	public void setLower(String lower) {
		this.lower = lower;
	}

	/** 下限值 */
	public String getLower() {
		return this.lower;
	}

	/** 指标目标值 */
	public void setTarget(String target) {
		this.target = target;
	}

	/** 指标目标值 */
	public String getTarget() {
		return this.target;
	}

	/** 计算公式 */
	public void setContent(String content) {
		this.content = content;
	}

	/** 计算公式 */
	public String getContent() {
		return this.content;
	}

	/** 类目 */
	public void setCategory(String category) {
		this.category = category;
	}

	/** 类目 */
	public String getCategory() {
		return this.category;
	}

	/** 采集方式 */
	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}

	/** 采集方式 */
	public String getCollectType() {
		return this.collectType;
	}

	@Override
	public String toString() {
		return "ParameterInfoNew [id=" + id + ", createTime=" + createTime + ", modifyTime=" + modifyTime
				+ ", isDelete=" + isDelete + ", name=" + name + ", unit=" + unit + ", upper=" + upper + ", lower="
				+ lower + ", target=" + target + ", content=" + content + ", category=" + category + ", collectType="
				+ collectType + "]";
	}
	
}
