package com.icss.mvp.entity.member;

import java.util.Date;

import com.icss.mvp.entity.common.CommonEntity;

/**
 * Created by Ray on 2019/2/15.
 * 
 * @author Ray
 * @date 2019/2/15
 */
public class MemberEntity extends CommonEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    private String            name;

    /**
     * (中软)工号
     */
    private String            account;

    /**
     * 客户(华为)工号
     */
    private String            clientAccount;

    /**
     * 关联账号，svn，git，dts，tmss 的对应账号
     */
    private String            relatedAccount;

    /**
     * 岗位职责
     */
    private String            duty;

    /**
     * 到岗/入项时间
     */
    private Date              arrivalDate;

    /**
     * 离岗/出项时间
     */
    private Date              departureDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getClientAccount() {
        return clientAccount;
    }

    public void setClientAccount(String clientAccount) {
        this.clientAccount = clientAccount;
    }

    public String getRelatedAccount() {
        return relatedAccount;
    }

    public void setRelatedAccount(String relatedAccount) {
        this.relatedAccount = relatedAccount;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }
}
