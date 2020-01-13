package com.icss.mvp.entity;

public class ViewRoleResource {
    private String roleId;
    private String resourceId;

    public ViewRoleResource(String roleId, String resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
