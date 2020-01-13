package com.icss.mvp.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SysViewRole {
    private String roleId;

    private String name;

    private Date createDate;
    private List<SysResource> sysResources;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<SysResource> getSysResources() {
        return sysResources;
    }

    public void setSysResources(List<SysResource> sysResources) {
        this.sysResources = sysResources;
    }
}