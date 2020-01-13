package com.icss.mvp.entity;

import java.util.Date;

public class CiProjInfo {

	private int id;

	private Date import_time;

	private Date struct_time;
	
	private String creator;

	private String no;

	private double structDuration;
	
	private int flag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getImport_time() {
		return import_time;
	}

	public void setImport_time(Date import_time) {
		this.import_time = import_time;
	}

	public Date getStruct_time() {
		return struct_time;
	}

	public void setStruct_time(Date struct_time) {
		this.struct_time = struct_time;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public double getStructDuration() {
		return structDuration;
	}

	public void setStructDuration(double structDuration) {
		this.structDuration = structDuration;
	}

	public int getFlag() {
		return flag;
	}
	
	public void setFlag(int flag) {
		this.flag = flag;
	}

}
