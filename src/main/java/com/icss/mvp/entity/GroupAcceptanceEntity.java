package com.icss.mvp.entity;

/**
 * 组织看板验收
 *
 * @author limingming
 */
public class GroupAcceptanceEntity {
    private String projectNo;
    private String name;
    private String pm;
    private String qa;
    private String hwzpdu;
    private String pduSpdt;

    //组织看板业务群总览表字段
    private int checkedProject;
    private String customerScoring;
    private int executedProject;
    private int riskProject;
    private String issueCloseLoop;
    private String personnelArrival;
    private String personnelStable;
    private int credibleProject;

    //组织看板验收表字段
    private int knotProject;
    private String acceptanceKPI;
    private String knotStarDistribution;
    private String deduction;

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getQa() {
        return qa;
    }

    public void setQa(String qa) {
        this.qa = qa;
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

    public int getCheckedProject() {
        return checkedProject;
    }

    public void setCheckedProject(int checkedProject) {
        this.checkedProject = checkedProject;
    }

    public String getCustomerScoring() {
        return customerScoring;
    }

    public void setCustomerScoring(String customerScoring) {
        this.customerScoring = customerScoring;
    }

    public int getExecutedProject() {
        return executedProject;
    }

    public void setExecutedProject(int executedProject) {
        this.executedProject = executedProject;
    }

    public int getRiskProject() {
        return riskProject;
    }

    public void setRiskProject(int riskProject) {
        this.riskProject = riskProject;
    }

    public String getIssueCloseLoop() {
        return issueCloseLoop;
    }

    public void setIssueCloseLoop(String issueCloseLoop) {
        this.issueCloseLoop = issueCloseLoop;
    }

    public String getPersonnelArrival() {
        return personnelArrival;
    }

    public void setPersonnelArrival(String personnelArrival) {
        this.personnelArrival = personnelArrival;
    }

    public String getPersonnelStable() {
        return personnelStable;
    }

    public void setPersonnelStable(String personnelStable) {
        this.personnelStable = personnelStable;
    }

    public int getCredibleProject() {
        return credibleProject;
    }

    public void setCredibleProject(int credibleProject) {
        this.credibleProject = credibleProject;
    }

    public int getKnotProject() {
        return knotProject;
    }

    public void setKnotProject(int knotProject) {
        this.knotProject = knotProject;
    }

    public String getAcceptanceKPI() {
        return acceptanceKPI;
    }

    public void setAcceptanceKPI(String acceptanceKPI) {
        this.acceptanceKPI = acceptanceKPI;
    }

    public String getKnotStarDistribution() {
        return knotStarDistribution;
    }

    public void setKnotStarDistribution(String knotStarDistribution) {
        this.knotStarDistribution = knotStarDistribution;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }
}
