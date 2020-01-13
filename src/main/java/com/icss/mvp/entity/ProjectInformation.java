package com.icss.mvp.entity;

public class ProjectInformation {

	private String no;
	
	private String pm;
	
	private String pmid;
	
	private String electronFlow;
	
	private String interestRate;//预算利率
	
	private String actualInterest;//实际利率
	
	private String operate;//本月运营UR
	
	private String ytdOperate;//YTD运营UR
	
	private String op;
	
	private String money;//金额
	
	private String contractStatus;//合同状态

	public String getElectronFlow() {
		return electronFlow;
	}

	public void setElectronFlow(String electronFlow) {
		this.electronFlow = electronFlow;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getPmid() {
		return pmid;
	}

	public void setPmid(String pmid) {
		this.pmid = pmid;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public String getActualInterest() {
		return actualInterest;
	}

	public void setActualInterest(String actualInterest) {
		this.actualInterest = actualInterest;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getYtdOperate() {
		return ytdOperate;
	}

	public void setYtdOperate(String ytdOperate) {
		this.ytdOperate = ytdOperate;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}
}
