package com.icss.mvp.entity.prj;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by Ray on 2019/9/10.
 *
 * @author Ray
 * @date 2019/9/10 11:02
 */
public class PrjProject {

    /**
     * <pre>
     *        "pmNo": "0000044619",
     *        "planStartTime": 1548950400000,
     *        "planEndTime": 1577721600000,
     *        "coopType": "-1",
     *        "coopTypeName": null,
     *        "area": "深圳",
     *        "prjDesc": "继承性项目，主要从事CI集群平台开发。",
     *        "prjName": "Test项目13520",
     *        "pmName": "陈雨龙",
     *        "qaNo": "0000158802",
     *        "qaName": "万百栋",
     *        "coName": "华为公司",
     *        "co": 23,
     *        "pdName": "2012实验室",
     *        "pd": 1012,
     *        "pduName": "中央硬件工程院",
     *        "pdu": 1072,
     *        "pdtName": "中央硬件部[D3硬件解决方案技术部]",
     *        "pdt": 1688,
     *        "lobName": "华为业务群",
     *        "lob": "100065",
     *        "buName": "2012实验室业务线",
     *        "bu": "100253",
     *        "duName": "2012实验室事业部",
     *        "du": "101108",
     *        "deptName": "中央硬件交付部",
     *        "dept": "101112",
     *        "prjType": "开发",
     *        "teamName": "Test团队4302",
     *        "isOffshoreName": "否",
     *        "isOffshore": 0,
     *        "pricingModeal": 1,
     *        "pricingModealName": "TM",
     *        "prjStatus": 1,
     *        "prjStatusName": "在行",
     *        "outPoNumber": "",
     *        "curTeamTypeName": "资源型独立编组"
     * </pre>
     */

    private int    prjId;
    private String pmNo;

    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date   planStartTime;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date   planEndTime;

    private String coopType;
    private String coopTypeName;
    private String area;
    private String prjDesc;
    private String prjName;
    private String pmName;
    private String coName;
    private String co;
    private String pdName;
    private String pd;
    private String pduName;
    private String pdu;
    private String pdtName;
    private String pdt;
    private String lobName;
    private String lob;
    private String buName;
    private String bu;
    private String duName;
    private String du;
    private String deptName;
    private String dept;
    private String prjType;
    private String teamName;
    private String isOffshoreName;
    private int    isOffshore;
    private int    pricingModeal;
    private String pricingModealName;
    private int    prjStatus;
    private String prjStatusName;
    private String outPoNumber;
    private String curTeamTypeName;

    private String qaName;
    private String qaNo;

    public int getPrjId() {
        return prjId;
    }

    public void setPrjId(int prjId) {
        this.prjId = prjId;
    }

    public String getPmNo() {
        return pmNo;
    }

    public void setPmNo(String pmNo) {
        this.pmNo = pmNo;
    }

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getCoopType() {
        return coopType;
    }

    public void setCoopType(String coopType) {
        this.coopType = coopType;
    }

    public String getCoopTypeName() {
        return coopTypeName;
    }

    public void setCoopTypeName(String coopTypeName) {
        this.coopTypeName = coopTypeName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPrjDesc() {
        return prjDesc;
    }

    public void setPrjDesc(String prjDesc) {
        this.prjDesc = prjDesc;
    }

    public String getPrjName() {
        return prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName;
    }

    public String getCoName() {
        return coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getPdName() {
        return pdName;
    }

    public void setPdName(String pdName) {
        this.pdName = pdName;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getPduName() {
        return pduName;
    }

    public void setPduName(String pduName) {
        this.pduName = pduName;
    }

    public String getPdu() {
        return pdu;
    }

    public void setPdu(String pdu) {
        this.pdu = pdu;
    }

    public String getPdtName() {
        return pdtName;
    }

    public void setPdtName(String pdtName) {
        this.pdtName = pdtName;
    }

    public String getPdt() {
        return pdt;
    }

    public void setPdt(String pdt) {
        this.pdt = pdt;
    }

    public String getLobName() {
        return lobName;
    }

    public void setLobName(String lobName) {
        this.lobName = lobName;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    public String getDuName() {
        return duName;
    }

    public void setDuName(String duName) {
        this.duName = duName;
    }

    public String getDu() {
        return du;
    }

    public void setDu(String du) {
        this.du = du;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getPrjType() {
        return prjType;
    }

    public void setPrjType(String prjType) {
        this.prjType = prjType;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getIsOffshoreName() {
        return isOffshoreName;
    }

    public void setIsOffshoreName(String isOffshoreName) {
        this.isOffshoreName = isOffshoreName;
    }

    public int getIsOffshore() {
        return isOffshore;
    }

    public void setIsOffshore(int isOffshore) {
        this.isOffshore = isOffshore;
    }

    public int getPricingModeal() {
        return pricingModeal;
    }

    public void setPricingModeal(int pricingModeal) {
        this.pricingModeal = pricingModeal;
    }

    public String getPricingModealName() {
        return pricingModealName;
    }

    public void setPricingModealName(String pricingModealName) {
        this.pricingModealName = pricingModealName;
    }

    public int getPrjStatus() {
        return prjStatus;
    }

    public void setPrjStatus(int prjStatus) {
        this.prjStatus = prjStatus;
    }

    public String getPrjStatusName() {
        return prjStatusName;
    }

    public void setPrjStatusName(String prjStatusName) {
        this.prjStatusName = prjStatusName;
    }

    public String getOutPoNumber() {
        return outPoNumber;
    }

    public void setOutPoNumber(String outPoNumber) {
        this.outPoNumber = outPoNumber;
    }

    public String getCurTeamTypeName() {
        return curTeamTypeName;
    }

    public void setCurTeamTypeName(String curTeamTypeName) {
        this.curTeamTypeName = curTeamTypeName;
    }

    public String getQaName() {
        return qaName;
    }

    public void setQaName(String qaName) {
        this.qaName = qaName;
    }

    public String getQaNo() {
        return qaNo;
    }

    public void setQaNo(String qaNo) {
        this.qaNo = qaNo;
    }


}
