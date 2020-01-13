package com.icss.mvp.entity.system;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.icss.mvp.entity.common.CommonEntity;
import com.icss.mvp.util.MathUtils;

/**
 * Created by Ray on 2018/11/30.
 */
public class UserEntity extends CommonEntity {

    /**
     * default serial version UID
     */
    private static final long serialVersionUID    = 1L;

    /**
     * 显示名，可重复
     */
    private String            name;

    /**
     * 登录密码
     */
    private String            password;

    /**
     * 别称
     */
    private String            nickname;

    // ISONLINE
    /**
     * 类型，0：系统用户，1：华为用户，2：中软用户
     */
    private int               type                = -1;

    private int               departmentId;

    private String            trunk;
    private String            branch;
    private String            dept;

    private String            trunkHW;
    private String            branchHW;
    private String            deptHW;

    private String            trunkOP;
    private String            branchOP;
    private String            deptOP;

    private int               roleId;

    private String            grantedPermissionIds;

    private int               currentPermissionId = -1;

    private String            currentPermissionName;

    private String            auth;

    private List<String>      menus;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getName() {
        return StringUtils.isNotBlank(name) ? name.trim() : this.getId();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getTrunk() {
        return trunk;
    }

    public void setTrunk(String trunk) {
        this.trunk = trunk;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getTrunkHW() {
        return trunkHW;
    }

    public void setTrunkHW(String trunk) {
        if (this.type == 2) {
            this.trunk = trunk;
        }
        this.trunkHW = trunk;
    }

    public String getBranchHW() {
        return branchHW;
    }

    public void setBranchHW(String branch) {
        if (this.type == 2) {
            this.branch = branch;
        }
        this.branchHW = branch;
    }

    public String getDeptHW() {
        return deptHW;
    }

    public void setDeptHW(String dept) {
        if (this.type == 2) {
            this.dept = dept;
        }
        this.deptHW = dept;
    }

    public String getTrunkOP() {
        return trunkOP;
    }

    public void setTrunkOP(String trunk) {
        if (this.type == 1) {
            this.trunk = trunk;
        }
        this.trunkOP = trunk;
    }

    public String getBranchOP() {
        return branchOP;
    }

    public void setBranchOP(String branch) {
        if (this.type == 1) {
            this.branch = branch;
        }
        this.branchOP = branch;
    }

    public String getDeptOP() {
        return deptOP;
    }

    public void setDeptOP(String dept) {
        if (this.type == 1) {
            this.dept = dept;
        }
        this.deptOP = dept;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getGrantedPermissionIds() {
        return grantedPermissionIds;
    }

    public void setGrantedPermissionIds(String grantedPermissionIds) {
        this.grantedPermissionIds = grantedPermissionIds;
    }

    public int getCurrentPermissionId() {
        int current = currentPermissionId;

        String granted;
        if (current < 0 && StringUtils.isNotBlank(granted = grantedPermissionIds)) {
            current = MathUtils.parseIntSmooth(granted.trim().split(",")[0], -1);
            currentPermissionId = current;
        }

        return currentPermissionId;
    }

    public void setCurrentPermissionId(int currentPermissionId) {
        this.currentPermissionId = currentPermissionId;
    }

    public String getCurrentPermissionName() {
        return currentPermissionName;
    }

    public void setCurrentPermissionName(String currentPermissionName) {
        this.currentPermissionName = currentPermissionName;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public List<String> getMenus() {
        return menus;
    }

    public void setMenus(List<String> menus) {
        this.menus = menus;
    }
}
