package com.icss.mvp.entity;

import java.util.Date;

public class LogModifyNum {
	
	private String id;//主键id
	private String  url;//关联表id
	private String fileType;//文件类型
	private int fileModifynum;//代码量修改量
	private int fileDelfynum;//代码删除量
	private String proNo;// 项目编号
	private Date commitTime;//提交时间
	private int isdelete = 0;// 标记位    0：正常数据  1：已删除数据
	private Date createtime;// 创建时间
	private Date updatetime;//修改时间
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getFileDelfynum() {
		return fileDelfynum;
	}
	public void setFileDelfynum(int fileDelfynum) {
		this.fileDelfynum = fileDelfynum;
	}
	public int getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public int getFileModifynum() {
		return fileModifynum;
	}
	public void setFileModifynum(int fileModifynum) {
		this.fileModifynum = fileModifynum;
	}
	public String getProNo() {
		return proNo;
	}
	public void setProNo(String proNo) {
		this.proNo = proNo;
	}
	public Date getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}
	
}
