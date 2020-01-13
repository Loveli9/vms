package com.icss.mvp.entity;

import java.util.Date;

public class CodeQualityInfo
{
    private String no;
    
    private String staticmonthn;
    
    private int codetotal;
    
    private String sai;
    
    private int codeccnum;
    
    private int scheckalarmnum;
    
    private String codereprate;
    
    private int avglapcomplexity;
    
    public String getNo()
	{
		return no;
	}

	public void setNo(String no)
	{
		this.no = no;
	}

	public String getStaticmonthn()
	{
		return staticmonthn;
	}

	public void setStaticmonthn(String staticmonthn)
	{
		this.staticmonthn = staticmonthn;
	}

	public int getCodetotal()
	{
		return codetotal;
	}

	public void setCodetotal(int codetotal)
	{
		this.codetotal = codetotal;
	}

	public String getSai()
	{
		return sai;
	}

	public void setSai(String sai)
	{
		this.sai = sai;
	}

	public int getCodeccnum()
	{
		return codeccnum;
	}

	public void setCodeccnum(int codeccnum)
	{
		this.codeccnum = codeccnum;
	}

	public int getScheckalarmnum()
	{
		return scheckalarmnum;
	}

	public void setScheckalarmnum(int scheckalarmnum)
	{
		this.scheckalarmnum = scheckalarmnum;
	}

	public String getCodereprate()
	{
		return codereprate;
	}

	public void setCodereprate(String codereprate)
	{
		this.codereprate = codereprate;
	}

	public int getAvglapcomplexity()
	{
		return avglapcomplexity;
	}

	public void setAvglapcomplexity(int avglapcomplexity)
	{
		this.avglapcomplexity = avglapcomplexity;
	}

	public Date getProcesstime()
	{
		return processtime;
	}

	public void setProcesstime(Date processtime)
	{
		this.processtime = processtime;
	}

	private Date processtime;
    
}
