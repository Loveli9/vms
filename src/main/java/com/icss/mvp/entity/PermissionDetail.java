package com.icss.mvp.entity;

import java.io.Serializable;

public class PermissionDetail implements Serializable{
	private static final long serialVersionUID = 1L;
    //
    private Long id;
    //权限id
    private String permissionid;
    //权限名称
    private String permissionName;
    //权限级别
    private Integer perLevel;
    //上级权限id
    private String parentperid;
    //序列
    private Integer seq;
    //显示名称
    private String remark;
    //是否启用
    private String enable;
    //创建时间
    private String creattime;
    //创建人
    private String creator;
    //最后修改时间
    private String updatetime;
    //最后修改人
    private String updateor;
    //标记是否显示
    private String sign;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPermissionid() {
		return permissionid;
	}
	public void setPermissionid(String permissionid) {
		this.permissionid = permissionid;
	}
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public Integer getPerLevel() {
		return perLevel;
	}
	public void setPerLevel(Integer perLevel) {
		this.perLevel = perLevel;
	}
	public String getParentperid() {
		return parentperid;
	}
	public void setParentperid(String parentperid) {
		this.parentperid = parentperid;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getCreattime() {
		return creattime;
	}
	public void setCreattime(String creattime) {
		this.creattime = creattime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getUpdateor() {
		return updateor;
	}
	public void setUpdateor(String updateor) {
		this.updateor = updateor;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
    
}
