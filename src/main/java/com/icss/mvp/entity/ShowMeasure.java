package com.icss.mvp.entity;

/**
 * 指标配置信息封装类
 * @author chengchenhui
 *
 */
public class ShowMeasure {
    //主键ID
    private Integer measureId; //指标ID
    //指标名称
    private String measureName;
    //类目
    private String measureCategory;
    
	private String measureOrder;//排序
	
	private String upper;//上限值
	
	private String challenge;//挑战值

	private String target;//目标值
	
	private String lower;//下限值
	
	private String unit;//单位
	
	private String isSelect;//该流程是否选中此指标
	
	private String labelId;//流程ID
	
	private String collectType;//采集方式

	private String computeRule;//优先方式
	
	public String getCollectType() {
		return collectType;
	}

	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public Integer getMeasureId() {
		return measureId;
	}

	public void setMeasureId(Integer measureId) {
		this.measureId = measureId;
	}

	public String getMeasureName() {
		return measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public String getMeasureCategory() {
		return measureCategory;
	}

	public void setMeasureCategory(String measureCategory) {
		this.measureCategory = measureCategory;
	}

	public String getMeasureOrder() {
		return measureOrder;
	}

	public void setMeasureOrder(String measureOrder) {
		this.measureOrder = measureOrder;
	}

	public String getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}

	public String getUpper() {
		return upper;
	}

	public void setUpper(String upper) {
		this.upper = upper;
	}
	public String getChallenge() {
		return challenge;
	}
	
	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getLower() {
		return lower;
	}

	public void setLower(String lower) {
		this.lower = lower;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getComputeRule() {
		return computeRule;
	}

	public void setComputeRule(String computeRule) {
		this.computeRule = computeRule;
	}
}
