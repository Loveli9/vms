package com.icss.mvp.entity;

import java.io.Serializable;

public class DepartmentTree implements Serializable{
	private static final long serialVersionUID = 1L;
    private Long id;
    /**
	 * id
	 */
    private String departmentId;
	/**
	 * 名称
	 */
    private String departmentName;
	/**
	 * 上级id
	 */
    private String departmentParentId;
	/**
	 * 部门职级
	 */
    private Integer level;
	/**
	 * 部门
	 */
    private Integer seq;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentParentId() {
		return departmentParentId;
	}

	public void setDepartmentParentId(String departmentParentId) {
		this.departmentParentId = departmentParentId;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
