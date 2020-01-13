package com.icss.mvp.entity;

import java.util.Date;

public class OrganizeMgmer
{
	private String name;
	private int level;
	private Integer parentId;
	private Date createDate;
	private String creater;
	private Date updateDate;
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	private int id;
	private String updateUser;
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getLevel()
	{
		return level;
	}
	public void setLevel(int level)
	{
		this.level = level;
	}
	public Date getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	public String getCreater()
	{
		return creater;
	}
	public void setCreater(String creater)
	{
		this.creater = creater;
	}
	public Date getUpdateDate()
	{
		return updateDate;
	}
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}
	public String getUpdateUser()
	{
		return updateUser;
	}
	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}
	
}
