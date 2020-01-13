package com.icss.mvp.entity;

import java.io.Serializable;

/**
 * 【中软组织结构表】的对应的Java映射类
 * @author gaoyao
 * @time 2018-4-25 11:06:49
*/
public class OpDepartment implements Serializable{
    private static final long serialVersionUID = 1L;
    //
    private Long id;
    //部门ID
    private String deptId;
    //部门名称
    private String deptName;
    //部门级别
    private Integer deptLevel;
    //上级部门ID
    private String parentDeptId;
    //序列
    private Integer seq;
    //显示名称
    private String remark;
    //开启状态
    private String enable;
    //创建时间
    private String creationDate;
    //创建人
    private String createBy;
    //最后修改时间
    private String lastUpdate;
    //最后修改人
    private String lastUpdateBy;
    //标记是否显示
    private String sign;
   
    /** 获取 的属性 */
    public Long getId() {
        return id;
    }
    /** 设置的属性 */
    public void setId(Long id) {
        this.id = id;
    }
    /** 获取 部门ID的属性 */
    public String getDeptId() {
        return deptId;
    }
    /** 设置部门ID的属性 */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    /** 获取 部门名称的属性 */
    public String getDeptName() {
        return deptName;
    }
    /** 设置部门名称的属性 */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    /** 获取 部门级别的属性 */
    public Integer getDeptLevel() {
        return deptLevel;
    }
    /** 设置部门级别的属性 */
    public void setDeptLevel(Integer deptLevel) {
        this.deptLevel = deptLevel;
    }
    /** 获取 上级部门ID的属性 */
    public String getParentDeptId() {
        return parentDeptId;
    }
    /** 设置上级部门ID的属性 */
    public void setParentDeptId(String parentDeptId) {
        this.parentDeptId = parentDeptId;
    }
    /** 获取 序列的属性 */
    public Integer getSeq() {
        return seq;
    }
    /** 设置序列的属性 */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }
    /** 获取 显示名称的属性 */
    public String getRemark() {
        return remark;
    }
    /** 设置显示名称的属性 */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /** 获取 开启状态的属性 */
    public String getEnable() {
        return enable;
    }
    /** 设置开启状态的属性 */
    public void setEnable(String enable) {
        this.enable = enable;
    }
    /** 获取 创建时间的属性 */
    public String getCreationDate() {
        return creationDate;
    }
    /** 设置创建时间的属性 */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    /** 获取 创建人的属性 */
    public String getCreateBy() {
        return createBy;
    }
    /** 设置创建人的属性 */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /** 获取 最后修改时间的属性 */
    public String getLastUpdate() {
        return lastUpdate;
    }
    /** 设置最后修改时间的属性 */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /** 获取 最后修改人的属性 */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }
    /** 设置最后修改人的属性 */
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    /** 获取 标记是否显示的属性 */
    public String getSign() {
        return sign;
    }
    /** 设置标记是否显示的属性 */
    public void setSign(String sign) {
        this.sign = sign;
    }
}
