package com.icss.mvp.entity.member;

import java.util.Date;

/**
 * @description: 关注人员列表
 * @author: chengchenhui
 * @create: 2019-12-26 09:51
 **/
public class AttentionPerson {
    private String id;
    private String concernPerson;//关注人
    private String  concernedPerson;//被关注人
    private Date concernedTime;//关注时间
    private int ignore;//0-关注 1-取消关注


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConcernPerson() {
        return concernPerson;
    }

    public void setConcernPerson(String concernPerson) {
        this.concernPerson = concernPerson;
    }

    public String getConcernedPerson() {
        return concernedPerson;
    }

    public void setConcernedPerson(String concernedPerson) {
        this.concernedPerson = concernedPerson;
    }

    public Date getConcernedTime() {
        return concernedTime;
    }

    public void setConcernedTime(Date concernedTime) {
        this.concernedTime = concernedTime;
    }

    public int getIgnore() {
        return ignore;
    }

    public void setIgnore(int ignore) {
        this.ignore = ignore;
    }
}
