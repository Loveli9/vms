package com.icss.mvp.entity;

import java.util.Date;

public class SvnLogFileModifyNum {
	private String id;//主键id
	private String svnLogId;//关联表id
	private String fileType;//文件类型
	private int fileModifynum;//代码量
	private String author;//工号
	private Date commitTime;//提交时间
	private String messageAuthor;//提交信息
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSvnLogId() {
		return svnLogId;
	}
	public void setSvnLogId(String svnLogId) {
		this.svnLogId = svnLogId;
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
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}
	public String getMessageAuthor() {
		return messageAuthor;
	}
	public void setMessageAuthor(String messageAuthor) {
		this.messageAuthor = messageAuthor;
	}
	
}
