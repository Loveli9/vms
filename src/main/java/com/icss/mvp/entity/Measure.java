package com.icss.mvp.entity;

public class Measure {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	// 主键ID
	private Long id;
	//
	private String wdId;
	// 创建时间
	private String createTime;
	// 修改日期
	private String modifyTime;
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
	// 版本
	private String version;
	// 迭代
	private String iteration;

	private String order;// 指标排序

	private String wdName;// 维度名称

	private Integer rowNum;// 序列号

	private String collectType;// 采集方式

	private String lmcId;

	private String copyDate;

	private String value;

	private Integer labelId;

	private int times;
	
	private String proNo;
	
	private Integer computeRule;
	
	

	public Integer getComputeRule() {
		return computeRule;
	}

	public void setComputeRule(Integer computeRule) {
		this.computeRule = computeRule;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWdId() {
		return wdId;
	}

	public void setWdId(String wdId) {
		this.wdId = wdId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUpper() {
		return upper;
	}

	public void setUpper(String upper) {
		this.upper = upper;
	}

	public String getLower() {
		return lower;
	}

	public void setLower(String lower) {
		this.lower = lower;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIteration() {
		return iteration;
	}

	public void setIteration(String iteration) {
		this.iteration = iteration;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWdName() {
		return wdName;
	}

	public void setWdName(String wdName) {
		this.wdName = wdName;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getCollectType() {
		return collectType;
	}

	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}

	public String getLmcId() {
		return lmcId;
	}

	public void setLmcId(String lmcId) {
		this.lmcId = lmcId;
	}

	public String getCopyDate() {
		return copyDate;
	}

	public void setCopyDate(String copyDate) {
		this.copyDate = copyDate;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getLabelId() {
		return labelId;
	}

	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public String getProNo() {
		return proNo;
	}

	public void setProNo(String proNo) {
		this.proNo = proNo;
	}

}
