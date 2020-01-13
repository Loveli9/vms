package com.icss.mvp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 
 * <pre>
 * <b>描述：用户实体</b>
 * <b>任务编号：</b>
 * <b>作者：张鹏飞</b> 
 * <b>创建日期： 2017年5月12日 下午2:57:58</b>
 * </pre>
 */
public class PersonnelInfo {

    private String userid;

    private String pmid;

    private Integer renlinum;

    private Integer renlinums;

    private Integer guanjiannum;

    private Integer guanjiannums;

    private Integer wentinum;

    public Integer getRenlinums() {
        return renlinums;
    }

    public void setRenlinums(Integer renlinums) {
        this.renlinums = renlinums;
    }

    public Integer getGuanjiannums() {
        return guanjiannums;
    }

    public void setGuanjiannums(Integer guanjiannums) {
        this.guanjiannums = guanjiannums;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public Integer getRenlinum() {
        return renlinum;
    }

    public void setRenlinum(Integer renlinum) {
        this.renlinum = renlinum;
    }

    public Integer getGuanjiannum() {
        return guanjiannum;
    }

    public void setGuanjiannum(Integer guanjiannum) {
        this.guanjiannum = guanjiannum;
    }
    public Integer getWentinum() {
        return wentinum;
    }

    public void setWentinum(Integer wentinum) {
        this.wentinum = wentinum;
    }
}
