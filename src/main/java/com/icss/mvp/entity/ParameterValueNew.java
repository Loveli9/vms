package com.icss.mvp.entity;

import java.util.Date;

public class ParameterValueNew
{
	private String no;
	private Date month;
	private int parameterId;
	private double value;

	public String getNo()
	{
		return no;
	}

	public void setNo(String no)
	{
		this.no = no;
	}

	public Date getMonth()
	{
		return month;
	}

	public void setMonth(Date month)
	{
		this.month = month;
	}

	public int getParameterId()
	{
		return parameterId;
	}

	public void setParameterId(int parameterId)
	{
		this.parameterId = parameterId;
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}
}
