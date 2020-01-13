package com.icss.mvp.entity;

public class ProjectMaturityAssessment {
	private String no; // 项目编号
	private String bu; // 业务线
	private String pdu; // 事业部
	private String du; // 交付部
	private String hwpdu; // 产品线
	private String hwzpdu; // 子产品线
	private String pduSpdt; // PDU/SPDT
	private String proName; // 项目名称
	private String hwVersion; // 华为版本号
	private String type; // 商务模式
	private String area; // 地域
	private String pmName; // PM姓名
	private String pmNo; // PM工号
	private String qaName; // QA姓名
	private String qaNo; // QA工号
	private String proResource; // 项目总人力
	private String needInterface; // 需求及接口明确
	private String acceptStandard; // 验收标准明确
//	private String baseVersion; // 基础版本健康
	private String workloadNeed; // 工作量估计与需求澄清
	private String sowReview; // SOW评审
	private String plan; // 计划
//	private String need; // 需求分析
//	private String scheme; // 方案设计
	private String rpReview; // RP评审
	private String testCase; // 测试用例
	private String codeReview; // 代码检视
	private String developerTest; // 开发者测试
	private String testEvaluation; // 转测试评估
	private String iteratorTest; // 迭代测试
	private String iteraorExport; // 迭代出口评估
	private String sdvTest; // SDV测试
	private String sitTest; // SIT测试
	private String uatTest; // UAT测试
	private String rrTest; // RR评审
	private String proPlan; // 项目计划
	private String structureChart; // 结构图
	private String strategyPlan; // 测试策略与计划
	private String scenariosCase; // 测试方案与用例实现
	private String testProcedure; // 测试过程
	private String testDeliverables; // 测试交付件
	private String scheduleManagement; // 进度管理
	private String requirementsManagement; // 端到端需求管理
	private String changeManagement; // 变更管理
	private String riskManagement; // 风险管理
//	private String engineAbility; // 工程能力
	private String costCare; //成本看护
	private String keyRoleStability; // 关键角色稳定
	private String pm; // PM
	private String baSe; // BA/SE
	private String mde; // MDE
	private String tse; // TSE
	private String tc; // TC
	private String keyRole; // 关键角色
	private String phaseAccept; // 阶段验收
	private String deliveryPreparation; // 交付件准备情况
	private String processTrace; // 流程跟踪
	private String projectAccept; // 结项验收
	private String totalScore; // 总分(自动计算)
	private String difference; // 项目实际运作与独立交付差距注：黄、红灯必填
	private String mark; // 标记
	private String startTime;//测评开始时间
	private String endTime;//测评结束时间

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
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

	public String getDu() {
		return du;
	}

	public void setDu(String du) {
		this.du = du;
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

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getHwVersion() {
		return hwVersion;
	}

	public void setHwVersion(String hwVersion) {
		this.hwVersion = hwVersion;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPmName() {
		return pmName;
	}

	public void setPmName(String pmName) {
		this.pmName = pmName;
	}

	public String getPmNo() {
		return pmNo;
	}

	public void setPmNo(String pmNo) {
		this.pmNo = pmNo;
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

	public String getProResource() {
		return proResource;
	}

	public void setProResource(String proResource) {
		this.proResource = proResource;
	}

	public String getNeedInterface() {
		return needInterface;
	}

	public void setNeedInterface(String needInterface) {
		this.needInterface = needInterface;
	}

	public String getAcceptStandard() {
		return acceptStandard;
	}

	public void setAcceptStandard(String acceptStandard) {
		this.acceptStandard = acceptStandard;
	}

//	public String getBaseVersion() {
//		return baseVersion;
//	}
//
//	public void setBaseVersion(String baseVersion) {
//		this.baseVersion = baseVersion;
//	}

	public String getWorkloadNeed() {
		return workloadNeed;
	}

	public void setWorkloadNeed(String workloadNeed) {
		this.workloadNeed = workloadNeed;
	}

	public String getSowReview() {
		return sowReview;
	}

	public void setSowReview(String sowReview) {
		this.sowReview = sowReview;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

//	public String getNeed() {
//		return need;
//	}
//
//	public void setNeed(String need) {
//		this.need = need;
//	}
//
//	public String getScheme() {
//		return scheme;
//	}
//
//	public void setScheme(String scheme) {
//		this.scheme = scheme;
//	}

	public String getRpReview() {
		return rpReview;
	}

	public void setRpReview(String rpReview) {
		this.rpReview = rpReview;
	}

	public String getTestCase() {
		return testCase;
	}

	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}

	public String getCodeReview() {
		return codeReview;
	}

	public void setCodeReview(String codeReview) {
		this.codeReview = codeReview;
	}

	public String getDeveloperTest() {
		return developerTest;
	}

	public void setDeveloperTest(String developerTest) {
		this.developerTest = developerTest;
	}

	public String getSdvTest() {
		return sdvTest;
	}

	public void setSdvTest(String sdvTest) {
		this.sdvTest = sdvTest;
	}

	public String getIteratorTest() {
		return iteratorTest;
	}

	public void setIteratorTest(String iteratorTest) {
		this.iteratorTest = iteratorTest;
	}

	public String getSitTest() {
		return sitTest;
	}

	public void setSitTest(String sitTest) {
		this.sitTest = sitTest;
	}

	public String getTestEvaluation() {
		return testEvaluation;
	}

	public void setTestEvaluation(String testEvaluation) {
		this.testEvaluation = testEvaluation;
	}

	public String getIteraorExport() {
		return iteraorExport;
	}

	public void setIteraorExport(String iteraorExport) {
		this.iteraorExport = iteraorExport;
	}

	public String getUatTest() {
		return uatTest;
	}

	public void setUatTest(String uatTest) {
		this.uatTest = uatTest;
	}

	public String getRrTest() {
		return rrTest;
	}

	public void setRrTest(String rrTest) {
		this.rrTest = rrTest;
	}

	public String getProPlan() {
		return proPlan;
	}

	public void setProPlan(String proPlan) {
		this.proPlan = proPlan;
	}

	public String getStructureChart() {
		return structureChart;
	}

	public void setStructureChart(String structureChart) {
		this.structureChart = structureChart;
	}

	public String getStrategyPlan() {
		return strategyPlan;
	}

	public void setStrategyPlan(String strategyPlan) {
		this.strategyPlan = strategyPlan;
	}

	public String getScenariosCase() {
		return scenariosCase;
	}

	public void setScenariosCase(String scenariosCase) {
		this.scenariosCase = scenariosCase;
	}

	public String getTestProcedure() {
		return testProcedure;
	}

	public void setTestProcedure(String testProcedure) {
		this.testProcedure = testProcedure;
	}

	public String getTestDeliverables() {
		return testDeliverables;
	}

	public void setTestDeliverables(String testDeliverables) {
		this.testDeliverables = testDeliverables;
	}

	public String getScheduleManagement() {
		return scheduleManagement;
	}

	public void setScheduleManagement(String scheduleManagement) {
		this.scheduleManagement = scheduleManagement;
	}

	public String getRequirementsManagement() {
		return requirementsManagement;
	}

	public void setRequirementsManagement(String requirementsManagement) {
		this.requirementsManagement = requirementsManagement;
	}

	public String getChangeManagement() {
		return changeManagement;
	}

	public void setChangeManagement(String changeManagement) {
		this.changeManagement = changeManagement;
	}

	public String getRiskManagement() {
		return riskManagement;
	}

	public void setRiskManagement(String riskManagement) {
		this.riskManagement = riskManagement;
	}

//	public String getEngineAbility() {
//		return engineAbility;
//	}
//
//	public void setEngineAbility(String engineAbility) {
//		this.engineAbility = engineAbility;
//	}

	public String getKeyRoleStability() {
		return keyRoleStability;
	}

	public void setKeyRoleStability(String keyRoleStability) {
		this.keyRoleStability = keyRoleStability;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getBaSe() {
		return baSe;
	}

	public void setBaSe(String baSe) {
		this.baSe = baSe;
	}

	public String getMde() {
		return mde;
	}

	public void setMde(String mde) {
		this.mde = mde;
	}

	public String getTse() {
		return tse;
	}

	public void setTse(String tse) {
		this.tse = tse;
	}

	public String getTc() {
		return tc;
	}

	public void setTc(String tc) {
		this.tc = tc;
	}

	public String getKeyRole() {
		return keyRole;
	}

	public void setKeyRole(String keyRole) {
		this.keyRole = keyRole;
	}

	public String getPhaseAccept() {
		return phaseAccept;
	}

	public void setPhaseAccept(String phaseAccept) {
		this.phaseAccept = phaseAccept;
	}

	public String getDeliveryPreparation() {
		return deliveryPreparation;
	}

	public void setDeliveryPreparation(String deliveryPreparation) {
		this.deliveryPreparation = deliveryPreparation;
	}

	public String getProcessTrace() {
		return processTrace;
	}

	public void setProcessTrace(String processTrace) {
		this.processTrace = processTrace;
	}

	public String getProjectAccept() {
		return projectAccept;
	}

	public void setProjectAccept(String projectAccept) {
		this.projectAccept = projectAccept;
	}

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getCostCare() {
		return costCare;
	}

	public void setCostCare(String costCare) {
		this.costCare = costCare;
	}
}