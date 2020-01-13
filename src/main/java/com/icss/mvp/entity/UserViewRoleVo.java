package com.icss.mvp.entity;

import java.util.List;

public class UserViewRoleVo {
    private String userId;
    private String userName;
    private List<SysViewRole> sysViewRoles;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<SysViewRole> getSysViewRoles() {
        return sysViewRoles;
    }

    public void setSysViewRoles(List<SysViewRole> sysViewRoles) {
        this.sysViewRoles = sysViewRoles;
    }
}
