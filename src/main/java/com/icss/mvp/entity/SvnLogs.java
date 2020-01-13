package com.icss.mvp.entity;

import java.util.Date;
import java.util.Map;

public class SvnLogs
{
    private String ID;
    
    private String AUTHOR;
    
    // private List<SvnLogsPath> CHANGEDPATHS;
    
    private Date COMMITTIME;
    
    private String MESSAGE;
    
    private long REVISION;
    
    private int MODIFYNUM;// 修改行数
    
    private int DELNUM;// 删除行数
    
    private String NO;// 项目编号
    
    private Date COLDATE;// 收集日期
    
    private int FILENUM;
    
    private  Map<String,Integer> fileNumMap;//各个文件修改行数

    private int status;

    public String getID()
    {
        return ID;
    }
    
    public void setID(String iD)
    {
        ID = iD;
    }
    
    public String getAUTHOR()
    {
        return AUTHOR;
    }
    
    public void setAUTHOR(String aUTHOR)
    {
        AUTHOR = aUTHOR;
    }
    
    public String getMESSAGE()
    {
        return MESSAGE;
    }
    
    public void setMESSAGE(String mESSAGE)
    {
        MESSAGE = mESSAGE;
    }
    
    public long getREVISION()
    {
        return REVISION;
    }
    
    public void setREVISION(long rEVISION)
    {
        REVISION = rEVISION;
    }
    
    public int getMODIFYNUM()
    {
        return MODIFYNUM;
    }
    
    public void setMODIFYNUM(int mODIFYNUM)
    {
        MODIFYNUM = mODIFYNUM;
    }
    
    public String getNO()
    {
        return NO;
    }
    
    public void setNO(String nO)
    {
        NO = nO;
    }
    
    public Date getCOLDATE()
    {
        return COLDATE;
    }
    
    public void setCOLDATE(Date cOLDATE)
    {
        COLDATE = cOLDATE;
    }
    
    public int getDELNUM()
    {
        return DELNUM;
    }
    
    public void setDELNUM(int dELNUM)
    {
        DELNUM = dELNUM;
    }
    
    public Date getCOMMITTIME()
    {
        return COMMITTIME;
    }
    
    public void setCOMMITTIME(Date cOMMITTIME)
    {
        COMMITTIME = cOMMITTIME;
    }
    
    public int getFILENUM()
    {
        return FILENUM;
    }
    
    public void setFILENUM(int fILENUM)
    {
        FILENUM = fILENUM;
    }

	public Map<String, Integer> getFileNumMap() {
		return fileNumMap;
	}

	public void setFileNumMap(Map<String, Integer> fileNumMap) {
		this.fileNumMap = fileNumMap;
	}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
