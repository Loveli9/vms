package com.icss.mvp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 【系统角色表表】的对应的Java映射类
 * @author gaoyao
 * @time 2018-5-29 9:48:07
*/
public class SysRole implements Serializable{
    private static final long serialVersionUID = 1L;
    //角色ID
    private Long roleId;
    //角色名称
    private String roleName;
    //
    private String roleDesc;
    //角色对应权限
    private String roleAuth;
    //管理角色
    private String manageRole;
    //操作时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateTime;
    //操作人员
    private String operateUser;
    //记录标识
    private Integer ignoe;
   
    /** 获取 角色ID的属性 */
    public Long getRoleId() {
        return roleId;
    }
    /** 设置角色ID的属性 */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    /** 获取 角色名称的属性 */
    public String getRoleName() {
        return roleName;
    }
    /** 设置角色名称的属性 */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    /** 获取 的属性 */
    public String getRoleDesc() {
        return roleDesc;
    }
    /** 设置的属性 */
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
    /** 获取 角色对应权限的属性 */
    public String getRoleAuth() {
        return roleAuth;
    }
    /** 设置角色对应权限的属性 */
    public void setRoleAuth(String roleAuth) {
        this.roleAuth = roleAuth;
    }
    /** 获取 管理角色的属性 */
    public String getManageRole() {
        return manageRole;
    }
    /** 设置管理角色的属性 */
    public void setManageRole(String manageRole) {
        this.manageRole = manageRole;
    }
    /** 获取 操作时间的属性 */
    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    /** 获取 操作人员的属性 */
    public String getOperateUser() {
        return operateUser;
    }
    /** 设置操作人员的属性 */
    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }
    /** 获取 记录标识的属性 */
    public Integer getIgnoe() {
        return ignoe;
    }
    /** 设置记录标识的属性 */
    public void setIgnoe(Integer ignoe) {
        this.ignoe = ignoe;
    }
}
