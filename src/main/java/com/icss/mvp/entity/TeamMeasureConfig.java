package com.icss.mvp.entity;

import java.io.Serializable;

public class TeamMeasureConfig implements Serializable{
	private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //创建时间
    private String createTime;
    //修改时间
    private String modifyTime;
    //1:已删除数据，0:正常数据
    private Integer isDeleted;
    //流程配置ID
    private Long teamLableId;
    //度量指标ID
    private Long measureId;
    //显示顺序
    private Integer order;
    
    //实际值
    private String actualVal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getTeamLableId() {
		return teamLableId;
	}

	public void setTeamLableId(Long teamLableId) {
		this.teamLableId = teamLableId;
	}

	public Long getMeasureId() {
		return measureId;
	}

	public void setMeasureId(Long measureId) {
		this.measureId = measureId;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getActualVal() {
		return actualVal;
	}

	public void setActualVal(String actualVal) {
		this.actualVal = actualVal;
	}

}
