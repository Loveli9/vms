package com.icss.mvp.entity.system;

import com.icss.mvp.entity.common.CommonEntity;

/**
 * Created by Ray on 2018/7/27.
 */
public class DepartmentEntity extends CommonEntity {

    /**
     * default serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 部门名称
     */
    private String            name;

    /**
     * 部门编码，三位数字一级，如：001 011 001为三级部门，其父级部门001 011，一级部门001
     */
    private String            code;

    /**
     * 部门层级，华为3级结构，中软4-5级
     */
    private int               level;

    /**
     * 父级部门编码
     */
    private String            parentCode;

    /**
     * 部门所属机构，hw：华为，op：中软
     */
    private String            type;

    /**
     * 排序
     */
    private int               rank;

    /**
     * op_department 的有效/失效标识，1为有效，0为失效
     */
    private int               enable           = 1;

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = decode(name);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = decode(parentCode);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getEnable() {
        return enable;
    }

    protected void setEnable(int enable) {
        if (enable < 0) {
            this.enable = -1;
            super.setIsDeleted(-1);
        } else {
            enable = enable == 1 ? 1 : 0;
            this.enable = enable;
            this.setIsDeleted(enable == 0 ? 1 : 0);
        }
    }

    @Override
    public void setIsDeleted(int isDeleted) {
        if (isDeleted < 0) {
            this.enable = -1;
            super.setIsDeleted(-1);
        } else {
            isDeleted = isDeleted == 1 ? 1 : 0;
            super.setIsDeleted(isDeleted);
            this.enable = isDeleted == 0 ? 1 : 0;
        }
    }
}
