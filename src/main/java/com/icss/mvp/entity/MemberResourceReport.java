package com.icss.mvp.entity;


public class MemberResourceReport{
	private String no;//项目编号
    private String projectName;//项目名称
    private String pm;//中软工号
    private String department;
    private String hwzpdu;
	private String pduSpdt;
	private String du;
	private String pdu;
    private String personnelStability;//人员稳定度
    private String personnelStability361;//人员稳定度
    private String collection;//是否为收藏项目
    private double keyArrival;
    private double arrival;
    
    private String keyArrivalRate;//关键角色到位率
    private int keyManpowerDemand;//关键角色人力需求
    private int keyOnduty;//关键角色在岗人数
    private int keyReserve;//关键角色后备人数
    private int keyManpowerGap;//关键角色人力缺口
    private String keyTurnoverRate;//关键角色离职率
    private int keyTurnoverPerson;//关键角色离职人数	
    private int keyPerson;
    
    private String arrivalRate;//全员到位率
    private int manpowerDemand;//全员人力需求
    private int onduty;//全员在岗人数
    private int reserve;//全员后备人数
    private int manpowerGap;//全员人力缺口
    private String turnoverRate;//全员离职率
    private int turnoverPerson;//全员离职人数
    
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getPm() {
		return pm;
	}
	public void setPm(String pm) {
		this.pm = pm;
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
	public String getDu() {
		return du;
	}
	public void setDu(String du) {
		this.du = du;
	}
	public String getPdu() {
		return pdu;
	}
	public void setPdu(String pdu) {
		this.pdu = pdu;
	}
	public String getPersonnelStability() {
		return personnelStability;
	}
	public void setPersonnelStability(String personnelStability) {
		this.personnelStability = personnelStability;
	}
	public String getPersonnelStability361() {
		return personnelStability361;
	}
	public void setPersonnelStability361(String personnelStability361) {
		this.personnelStability361 = personnelStability361;
	}
	public String getKeyArrivalRate() {
		return keyArrivalRate;
	}
	public void setKeyArrivalRate(String keyArrivalRate) {
		this.keyArrivalRate = keyArrivalRate;
	}
	public int getKeyManpowerDemand() {
		return keyManpowerDemand;
	}
	public void setKeyManpowerDemand(int keyManpowerDemand) {
		this.keyManpowerDemand = keyManpowerDemand;
	}
	public int getKeyManpowerGap() {
		return keyManpowerGap;
	}
	public void setKeyManpowerGap(int keyManpowerGap) {
		this.keyManpowerGap = keyManpowerGap;
	}
	public String getKeyTurnoverRate() {
		return keyTurnoverRate;
	}
	public void setKeyTurnoverRate(String keyTurnoverRate) {
		this.keyTurnoverRate = keyTurnoverRate;
	}
	public int getKeyTurnoverPerson() {
		return keyTurnoverPerson;
	}
	public void setKeyTurnoverPerson(int keyTurnoverPerson) {
		this.keyTurnoverPerson = keyTurnoverPerson;
	}
	public String getArrivalRate() {
		return arrivalRate;
	}
	public void setArrivalRate(String arrivalRate) {
		this.arrivalRate = arrivalRate;
	}
	public int getManpowerDemand() {
		return manpowerDemand;
	}
	public void setManpowerDemand(int manpowerDemand) {
		this.manpowerDemand = manpowerDemand;
	}
	public int getManpowerGap() {
		return manpowerGap;
	}
	public void setManpowerGap(int manpowerGap) {
		this.manpowerGap = manpowerGap;
	}
	public String getTurnoverRate() {
		return turnoverRate;
	}
	public void setTurnoverRate(String turnoverRate) {
		this.turnoverRate = turnoverRate;
	}
	public int getTurnoverPerson() {
		return turnoverPerson;
	}
	public void setTurnoverPerson(int turnoverPerson) {
		this.turnoverPerson = turnoverPerson;
	}
	public int getKeyOnduty() {
		return keyOnduty;
	}
	public void setKeyOnduty(int keyOnduty) {
		this.keyOnduty = keyOnduty;
	}
	public int getOnduty() {
		return onduty;
	}
	public void setOnduty(int onduty) {
		this.onduty = onduty;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public int getKeyReserve() {
		return keyReserve;
	}
	public void setKeyReserve(int keyReserve) {
		this.keyReserve = keyReserve;
	}
	public int getReserve() {
		return reserve;
	}
	public void setReserve(int reserve) {
		this.reserve = reserve;
	}
	public int getKeyPerson() {
		return keyPerson;
	}
	public void setKeyPerson(int keyPerson) {
		this.keyPerson = keyPerson;
	}
	public double getKeyArrival() {
		return keyArrival;
	}
	public void setKeyArrival(double keyArrival) {
		this.keyArrival = keyArrival;
	}
	public double getArrival() {
		return arrival;
	}
	public void setArrival(double arrival) {
		this.arrival = arrival;
	}

}
