package com.icss.mvp.entity;

import java.io.Serializable;

/**
 * <p>Title: SysHWdept</p>
 * <p>Description:华为部门实体 </p>
 *
 * @author gaoyao
 * @date 2018年4月24日下午6:22:01
 */
public class SysHwdept implements Serializable {

    private static final long serialVersionUID = 1L;

    //id
    private Long    id;
    //部门id
    private Long    deptId;
    //部门名称
    private String  deptName;
    //部门级别
    private Integer deptLevel;
    //上级部门ID
    private Long    parentId;
    //操作时间
    private String  operateTime;
    //操作人员
    private String  operateUser;
    //记录标识
    private Integer ignoe;

    /**
     * 获取 的属性
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置的属性
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 id的属性
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * 设置id的属性
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取 部门名称的属性
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * 设置部门名称的属性
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * 获取 部门级别的属性
     */
    public Integer getDeptLevel() {
        return deptLevel;
    }

    /**
     * 设置部门级别的属性
     */
    public void setDeptLevel(Integer deptLevel) {
        this.deptLevel = deptLevel;
    }

    /**
     * 获取 上级部门ID的属性
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级部门ID的属性
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取 操作时间的属性
     */
    public String getOperateTime() {
        return operateTime;
    }

    /**
     * 设置操作时间的属性
     */
    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * 获取 操作人员的属性
     */
    public String getOperateUser() {
        return operateUser;
    }

    /**
     * 设置操作人员的属性
     */
    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    /**
     * 获取 记录标识的属性
     */
    public Integer getIgnoe() {
        return ignoe;
    }

    /**
     * 设置记录标识的属性
     */
    public void setIgnoe(Integer ignoe) {
        this.ignoe = ignoe;
    }
}
