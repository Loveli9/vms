package com.icss.mvp.entity;

import java.io.Serializable;

/**
 * 【指标配置表表】的对应的Java映射类
 * @author Administrator
 * @time 2018-5-10 15:32:49
*/
public class LabelMeasureConfig implements Serializable{
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
    private Long projectLableId;
    //度量指标ID
    private Long measureId;
    //显示顺序
    private Integer order;
    
    //实际值
    private String actualVal;
   
    /** 获取 主键的属性 */
    public Long getId() {
        return id;
    }
    /** 设置主键的属性 */
    public void setId(Long id) {
        this.id = id;
    }
    /** 获取 创建时间的属性 */
    public String getCreateTime() {
        return createTime;
    }
    /** 设置创建时间的属性 */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    /** 获取 修改时间的属性 */
    public String getModifyTime() {
        return modifyTime;
    }
    /** 设置修改时间的属性 */
    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
    /** 获取 1:已删除数据，0:正常数据的属性 */
    public Integer getIsDeleted() {
        return isDeleted;
    }
    /** 设置1:已删除数据，0:正常数据的属性 */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    /** 获取 流程配置ID的属性 */
    public Long getProjectLableId() {
        return projectLableId;
    }
    /** 设置流程配置ID的属性 */
    public void setProjectLableId(Long projectLableId) {
        this.projectLableId = projectLableId;
    }
    /** 获取 度量指标ID的属性 */
    public Long getMeasureId() {
        return measureId;
    }
    /** 设置度量指标ID的属性 */
    public void setMeasureId(Long measureId) {
        this.measureId = measureId;
    }
    /** 获取 显示顺序的属性 */
    public Integer getOrder() {
        return order;
    }
    /** 设置显示顺序的属性 */
    public void setOrder(Integer order) {
        this.order = order;
    }
	public String getActualVal() {
		return actualVal;
	}
	public void setActualVal(String actualVal) {
		this.actualVal = actualVal;
	}
    
}
