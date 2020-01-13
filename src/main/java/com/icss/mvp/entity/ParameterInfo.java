package com.icss.mvp.entity;

import java.util.Date;

public class ParameterInfo {
	        
    private int id;
    
    private String name;
    
    private String unit;

	private String big_type_value;

    private String small_type_value;

    private String source_value;

    private String parameter;

    private Date create_date;

    private String creator;

    private Date update_date;

    private String update_user;
    

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBig_type_value() {
		return big_type_value;
	}

	public void setBig_type_value(String big_type_value) {
		this.big_type_value = big_type_value;
	}

	public String getSmall_type_value() {
		return small_type_value;
	}

	public void setSmall_type_value(String small_type_value) {
		this.small_type_value = small_type_value;
	}

	public String getSource_value() {
		return source_value;
	}

	public void setSource_value(String source_value) {
		this.source_value = source_value;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	@Override
	public String toString() {
		return "ParameterInfo [id=" + id + ", name=" + name + ", unit=" + unit
				+ ", big_type_value=" + big_type_value + ", small_type_value="
				+ small_type_value + ", source_value=" + source_value
				+ ", parameter=" + parameter + ", create_date=" + create_date
				+ ", creator=" + creator + ", update_date=" + update_date
				+ ", update_user=" + update_user + "]";
	}

}
