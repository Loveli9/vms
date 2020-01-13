package com.icss.mvp.entity.project;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.icss.mvp.entity.common.CommonEntity;

/**
 * Created by Ray on 2018/9/30.
 */
public class ProjectBaseEntity extends CommonEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 项目编号
     */
    private String            no;

    /**
     * 名称
     */
    private String            name;

    /**
     * 项目经理
     */
    private String            manager;

    /**
     * 项目经理工号
     */
    private String            managerAccount;

    /**
     * 地域
     */
    private String            area;

    /**
     * 地域三字代码
     */
    private String            iataCode;

    /**
     * 华为产品线，一级部门
     */
    private String            hwpdu;

    /**
     * 华为子产品线，二级部门
     */
    private String            hwzpdu;

    /**
     * 华为PDU/SPDU，三级部门
     */
    private String            pduSpdt;

    /**
     * 华为部门编号
     */
    private String            hwDeptCode;

    /**
     * 中软业务线
     */
    private String            bu;

    /**
     * 中软事业部
     */
    private String            pdu;

    /**
     * 中软交付部
     */
    private String            du;

    /**
     * 中软部门编号
     */
    private String            opDeptCode;

    /**
     * 简介
     */
    private String            synopsis;

    /**
     * 外部PO编号
     */
    private String            po;

    /**
     * 运行状态，在行，关闭
     */
    private String            state;

    /**
     * 计划开始时间
     */
    private Date              beginDate;

    /**
     * 计划结束时间
     */
    private Date              finishDate;

    /**
     * 同步时间
     */
    private Date              importDate;

    /**
     * 操作人员
     */
    private String            operator;

    /**
     * 运营模式，FP，TM
     */
    @JsonIgnore
    private String            operatingMode;

    /**
     * 项目类型，开发，测试
     */
    @JsonIgnore
    private String            type;

    /**
     * 合作类型，委托开发，现场定制，产品合作，总部定制
     */
    @JsonIgnore
    private String            cooperationType;

    /**
     * 是否离岸项目
     */
    @JsonIgnore
    private String            isOffshore;

    /**
     * 预警等级
     */
    private String            warningGrade;         // 黄、红、绿类别

    /**
     * 星级评估等级
     */
    private String            starGrade;            // 星级

    private String            pcbCategory;

    private String            pcbStatus;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManagerAccount() {
        return managerAccount;
    }

    public void setManagerAccount(String managerAccount) {
        this.managerAccount = managerAccount;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getHwpdu() {
        return hwpdu;
    }

    public void setHwpdu(String hwpdu) {
        this.hwpdu = hwpdu;
    }

    public String getHwzpdu() {
        return hwzpdu;
    }

    public void setHwzpdu(String hwzpdu) {
        this.hwzpdu = hwzpdu;
    }

    public String getPduSpdt() {
        return pduSpdt;
    }

    public void setPduSpdt(String pduSpdt) {
        this.pduSpdt = pduSpdt;
    }

    public String getHwDeptCode() {
        return hwDeptCode;
    }

    public void setHwDeptCode(String hwDeptCode) {
        this.hwDeptCode = hwDeptCode;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    public String getPdu() {
        return pdu;
    }

    public void setPdu(String pdu) {
        this.pdu = pdu;
    }

    public String getOpDeptCode() {
        return opDeptCode;
    }

    public void setOpDeptCode(String opDeptCode) {
        this.opDeptCode = opDeptCode;
    }

    public String getDu() {
        return du;
    }

    public void setDu(String du) {
        this.du = du;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatingMode() {
        return operatingMode;
    }

    public void setOperatingMode(String operatingMode) {
        this.operatingMode = operatingMode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCooperationType() {
        return cooperationType;
    }

    public void setCooperationType(String cooperationType) {
        this.cooperationType = cooperationType;
    }

    public String getIsOffshore() {
        return isOffshore;
    }

    public void setIsOffshore(String isOffshore) {
        this.isOffshore = isOffshore;
    }

    public String getWarningGrade() {
        return warningGrade;
    }

    public void setWarningGrade(String warningGrade) {
        this.warningGrade = warningGrade;
    }

    public String getStarGrade() {
        return starGrade;
    }

    public void setStarGrade(String starGrade) {
        this.starGrade = starGrade;
    }

    public String getPcbCategory() {
        return pcbCategory;
    }

    public void setPcbCategory(String pcbCategory) {
        this.pcbCategory = pcbCategory;
    }

    public String getPcbStatus() {
        return pcbStatus;
    }

    public void setPcbStatus(String pcbStatus) {
        this.pcbStatus = pcbStatus;
    }
}
