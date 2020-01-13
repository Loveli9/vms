package com.icss.mvp.util;

public enum IterationReport {
	/**
	 * 进度偏差
	 */
	schedule("迭代", "迭代进度偏差"),
	/**
	 * 工时偏差
	 */
	workingHours("迭代", "迭代工作量偏差"),
	/**
	 * 迭代问题解决
	 */
	bugSolve("迭代", "迭代问题解决"),
	/**
	 * 测试报告
	 */
	testReport("迭代", "测试报告"),
	/**
	 * 遗留DI
	 */
	leftOverDI("迭代", "遗留DI"),
	/**
	 * 遗留DI
	 */
	iterationDevelopment("迭代", "迭代研发效率"),

	/**
	 * 迭代测试效率
	 */
	iterationCeshi("迭代", "迭代测试效率"),

	/**
	 * 需求稳定度
	 */
	demandStability("需求", "需求稳定度"),
	/**
	 * 需求交付率
	 */
	demandDeliveryRate("需求", "需求交付率"),
	/**
	 * 需求生产率
	 */
	demandProductivity("需求", "需求生产率"),
	/**
	 * 需求文档质量
	 */
	demandDocQuality("需求", "需求文档质量"),

	/*
	 * 代码检视
	 */
	developeCodeCheck("开发", "代码检视"),
	/*
	 * LIT代码覆盖率
	 */
	developeLLT("开发", "LLT代码覆盖率"),
	/*
	 * 开发自测试
	 */
	developeTestSelf("开发", "开发自测试"),

	/*
	 * 代码质量
	 */
	developeCodeQuality("开发", "代码质量"),

	/*
	 * 测试缺陷密度
	 */
	testBugDensity("测试", "测试缺陷密度"),
	/*
	 * 用例发现缺陷比例
	 */
	testYlqxRate("测试", "用例发现缺陷比例"),
	/*
	 * 
	 * 测试漏测
	 */
	testLeaveOut("测试", "测试漏测"),
	/*
	 * 测试漏测率
	 */
	testLeaveOutRate("测试", "测试漏测"),
	/*
	 * 转测试缺陷密度
	 */
	testDefectsRate("测试", "测试缺陷密度"),
	/*
	 * 脚本检视缺陷密度
	 */
	automationDensity("自动化", "脚本检视缺陷密度"),
	/*
	 * 已完成自动化用例个数
	 */
	automationRate("自动化", "自动化率"),
	/*
	 * 自动化用例执行率
	 */
	automationImplRate("自动化", "自动化用例执行率"),
	/*
	 * 入厂前工厂验收通过率
	 */
	automationRcqRate("自动化", "入厂前工厂验收通过率"),
	/*
	 * 全量自动化脚本连跑通过率
	 */
	automationQLLPRate("自动化", "全量自动化脚本连跑通过率"),
	/*
	 * 工厂脚本连跑失败分析完成率
	 */
	automationLPsblRate("自动化", "工厂脚本连跑失败分析完成率"),
	/*
	 * 自动化率
	 */
	automationRates("自动化", "自动化率"),
	/*
	 * 脚本检视缺陷密度
	 */
	scriptsViewDefectDensity("自动化", "脚本检视缺陷密度"),
	/*
	 * 入厂前工厂验收通过率
	 */
	factoryAcceptanceRate("自动化", "入厂前工厂验收通过率"),
	/*
	 * 全量自动化脚本连跑通过率
	 */
	fullAutomaticScriptPassRate("自动化", "全量自动化脚本连跑通过率"),

	/*
	 * 开发迭代验收
	 */
	checkDevelopDd("验收", "开发迭代验收"),
	/*
	 * 自动化迭代验收
	 */
	checkDevelopAuto("验收", "自动化迭代验收"),
	/*
	 * 开发结项验收
	 */
	checkDevelopJX("验收", "开发结项验收"),
	/*
	 * 开发遗留验收
	 */
	checkDevelopYl("验收", "开发遗留验收"),
	/*
	 * 测试结项验收
	 */
	checkTestjxYs("验收", "测试结项验收"),
	/*
	 * 迭代验收发现缺陷密度
	 */
	iterativeAcceptanceDefectsRate("验收", "开发迭代验收"),
	/*
	 * 自动化脚本验收缺陷密度
	 */
	autoAcceDefectsRate("验收", "自动化迭代验收"),
	/*
	 * 结项验收发现缺陷密度
	 */
	closeAcceDefectsRate("验收", "开发结项验收"),
	/*
	 * 结项遗留DI值
	 */
	closeLeftOverDI("验收", "开发遗留验收"),

	// 消费者业务线
	/*
	 * 效率度量指标
	 */
	xfzysxl("验收", "效率度量指标"),
	/*
	 * 累计效率源数据
	 */
	xfzysysj("验收", "累计效率源数据"),
	/*
	 * TOP过程质量问题
	 */
	xfzystopzl("验收", "TOP过程质量问题"),
	/*
	 * TOP结果质量问题
	 */
	xfzystopjgzl("验收", "TOP结果质量问题"),
	/*
	 * 端到端研发效率
	 */
	Endandend("E2E", "研发效率"),

	/************************ 高斯指标分类 **********************/
	/**
	 * 开发-过程指标
	 */
	developProcess("开发", "过程指标"),
	/**
	 * 开发-结项指标
	 */
	developKnot("开发", "结项指标"),
	/**
	 * 测试-过程指标
	 */
	testProcess("测试", "过程指标"),
	/**
	 * 测试-结项指标
	 */
	testKnot("测试", "结项指标"),

	/**
	 * 维护-过程指标
	 */
	maintainProcess("维护", "过程指标"),

	/**
	 * 维护-结项指标
	 */
	maintainKnot("维护", "结项指标"),

	/**
	 * 效率-效率指标
	 */
	efficiency("效率", "效率指标"),

	/************************ 消费者业务线指标分类 **********************/
	/**
	 * 结果质量--上网质量
	 */
	InternetQuality("结果质量", "上网质量"),

	/**
	 * 结果质量--生产质量
	 */
	productQuality("结果质量", "生产质量"),

	/**
	 * 结果质量--生产质量
	 */
	deliveryQuality("结果质量", "交付后质量"),

	/**
	 * 结果质量--客户投诉
	 */
	customerComplaints("结果质量", "客户投诉"),

	/**
	 * 需求--需求实现
	 */
	needRealization("需求", "需求实现"),

	/**
	 * 开发--代码质量
	 */
	codeQuqlity("开发", "代码质量"),

	/**
	 * 开发--6+1
	 */
	sixPlusOne("开发", "6+1"),

	/**
	 * 开发--问题处理
	 */
	problemHandling("开发", "问题处理"),

	/**
	 * 开发--资料开发
	 */
	dataDevelopment("开发", "资料开发"),
	/**
	 * 测试--测试设计
	 */
	testDesign("测试", "测试设计"),

	/**
	 * 测试--测试执行
	 */
	testExecute("测试", "测试执行"),

	/**
	 * 测试--测试结果
	 */
	testResult("测试", "测试结果"),

	/**
	 * 测试--测试质量
	 */
	testQuality("测试", "测试质量"),

	/**
	 * 自动化
	 */
	automaticity("自动化", "自动化"),

	/**
	 * 硬件质量
	 */
	ardware("硬件", "硬件质量"),

	/**
	 * 整版本-工作量投入
	 */
	workload("整版本", "工作量投入"),

	/**
	 * 整版本-转测质量
	 */
	transfer("整版本", "转测质量"),

	/**
	 * 整版本-遗留DI
	 */
	legacy("整版本", "遗留DI"),

	/**
	 * 整版本-研发效率
	 */
	Efficiency("整版本", "研发效率"),

	/**
	 * 整版本-测试效率
	 */
	TestEfficiency("整版本", "测试效率"),

	/**
	 * 整版本-版本交付
	 */
	version("整版本", "版本交付"),

	/**
	 * 整版本-验收质量
	 */
	acceptance("整版本", "验收质量"),

	/**
	 * 可信指标-度量指标
	 */
	magnanimity("可信指标", "度量指标"),
	
	/************************ 欧拉指标分类 **********************/
	/**
	 * 需求-验收入口
	 */
	AcceptanceEntry("需求", "验收入口"),
	
	/**
	 * 需求-过程指标
	 */
	ProcessIndicators("需求", "过程指标"),
	
	/**
	 * 开发-过程指标
	 */
	DevelopmentProcessIndicators("开发", "过程指标"),
	
	/**
	 * 开发-验收入口
	 */
	DevelopmentAcceptanceEntry("开发", "验收入口"),
	
	/**
	 * 开发-结项指标
	 */
	DevelopmentNodalIndex("开发", "结项指标"),
	
	/**
	 * 测试-过程指标
	 */
	TestProcessIndicators("测试", "过程指标"),
	
	/**
	 * 测试-验收入口
	 */
	TestAcceptanceEntry("测试", "验收入口"),
	
	/**
	 * 测试-验收复核
	 */
	TestAcceptanceCheck("测试", "验收复核"),
	
	/**
	 * 测试-结项指标
	 */
	TestNodalIndex("测试", "结项指标"),
	
	/**
	 * 安全-验收入口
	 */
	SecurityAcceptanceEntry("安全", "验收入口"),
	
	/**
	 * 安全-验收出口
	 */
	SecurityExportInspection("安全", "验收出口"),
	
	/**
	 * 维护-过程指标
	 */
	MaintainProcessIndicators("维护", "过程指标"),
	
	/**
	 * 效率-过程指标
	 */
	EfficiencyProcessIndicators("效率", "过程指标"),
	
	/**
	 * 可信指标-度量指标
	 */
	CredibleIndexMetrics("可信指标", "度量指标");
	
	private String assortment;
	private String title;

	IterationReport(String assortment, String title) {
		this.assortment = assortment;
		this.title = title;
	}

	public static IterationReport fromType(String assortment, String title) {
		for (IterationReport iterationReport : IterationReport.values()) {
			if (assortment.equals(iterationReport.getAssortment()) && title.equals(iterationReport.getTitle())) {
				return iterationReport;
			}
		}
		return null;
	}

	public String getTitle() {
		return title;
	}

	public String getAssortment() {
		return assortment;
	}
}