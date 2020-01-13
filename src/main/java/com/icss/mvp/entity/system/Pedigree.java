package com.icss.mvp.entity.system;

import com.icss.mvp.entity.common.BaseEntity;

/**
 * Created by Ray on 2019/1/18.
 */
public class Pedigree extends BaseEntity {

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
     * 部门名称
     */
    private String            branchName;

    /**
     * 部门编码，三位数字一级，如：001 011 001为三级部门，其父级部门001 011，一级部门001
     */
    private String            branchCode;

    /**
     * 部门名称
     */
    private String            trunkName;

    /**
     * 部门编码，三位数字一级，如：001 011 001为三级部门，其父级部门001 011，一级部门001
     */
    private String            trunkCode;

    public Pedigree(){

    }

    public Pedigree(String[] chainName, String[] chainCode, int level){
        if (chainName == null || chainCode == null || chainName.length != chainCode.length || chainName.length == 0) {
            return;
        }

        int deep = chainName.length;
        if (level != deep) {
            return;
        }

        this.setName(chainName[deep - 1]);
        this.setCode(chainCode[deep - 1]);

        if (deep > 1) {
            this.setBranchName(chainName[deep - 2]);
            this.setBranchCode(chainCode[deep - 2]);
        }

        if (deep > 2) {
            this.setTrunkName(chainName[deep - 3]);
            this.setTrunkCode(chainCode[deep - 3]);
        }

        this.level = deep;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getTrunkName() {
        return trunkName;
    }

    public void setTrunkName(String trunkName) {
        this.trunkName = trunkName;
    }

    public String getTrunkCode() {
        return trunkCode;
    }

    public void setTrunkCode(String trunkCode) {
        this.trunkCode = trunkCode;
    }
}
