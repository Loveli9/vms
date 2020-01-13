package com.icss.mvp.entity;

import java.util.Date;

public class ProjectComment {
	
	private int id;
	private Date create_time;
	private Date modify_time;
	private int is_deleted;
	private String project_id;
	private String comment;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public int getIs_deleted() {
		return is_deleted;
	}
	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "ProjectComment [id=" + id + ", create_time=" + create_time + ", modify_time=" + modify_time
				+ ", is_deleted=" + is_deleted + ", project_id=" + project_id + ", comment=" + comment + "]";
	}
	
	
	
}
