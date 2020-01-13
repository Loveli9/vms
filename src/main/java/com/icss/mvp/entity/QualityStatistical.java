package com.icss.mvp.entity;

/**
 * 质量统计
 * @author fanpeng
 *
 */
public class QualityStatistical {
	
	private String hwzpdu;//华为子产品线
	
	public String getHwzpdu() {
		return hwzpdu;
	}
	public void setHwzpdu(String hwzpdu) {
		this.hwzpdu = hwzpdu;
	}
	
	private String no;//项目编号
	private String id; //序号
	private String pduSpdt;//PDU/SPDT
	private String name; //外包项目名称
	private String supplier;//供应商
	private String area; //地域
	private String pm;//合作方经理
	private String hwpdu;//华为产品线
	private String qa;//合作方QA
	private String humanNeeds;//人力需求
	private String nowHuman;//当前人力
	private String metrics;//度量项
	private String accDevCode;//累计开发代码规模(Kloc)
	private String accDevCase;//累计开发用例个数
	private String accFindPro;//累计发现问题单数
	private String accDealPro;//累计处理问题数
	private String accPutHuman;//累计投入人月
	private String accCodePdt;//代码累计生产率(loc/人天)  46
	private String accCasePdt;//用例累计生产率(个/人天)
	private String testFindEffPro;//测试发现有效问题效率(个/人天)
	private String dealProEff;//处理问题效率(个/人天)  27
	private String codeReview;//代码检视缺陷密度(个/Kloc)  
	private String testCase;//测试用例密度(个/Kloc)  68
	private String testDefects;//测试缺陷密度(个/Kloc)67
	private String versionToTest;//版本转测试不通过(累计)
	private String proReturn;//问题单回归不通过(累计)
	private String codeRepeat;//代码重复率  162
	private String maxComplex;//代码最大圈复杂度163
	private String averageComplex;//平均圈复杂度 164
	private String LLTcover;//	LLT覆盖率  
	private String pcLintErrorReset;//静态检查Pc-lintError\warning级别清零  165
	private String lineErrorReset;//静态检查FindBugsHigh级别清零 166
    private String coverity;//静态检查Coverity
    private String sai;//SAI架构度量
    private String completeAutomaticCase;//已完成自动化用例个数
    private String proceedAutomaticCase;//计划进行自动化的用例个数
    private String automaticCaseExcute;//自动化用例执行率
    private String automaticFactoryExcute;//自动化工厂执行用例数
    private String automaticFactoryTotal;//自动化工厂总用例数
    private String totalAutomaticScript;//全量自动化脚本连跑通过率
    private String onceAutomaticExcuteSuccess;//一次自动化连跑执行成功总用例数
    private String onceAutomaticExcuteCase;//一次自动化连跑执行总用例数
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPduSpdt() {
		return pduSpdt;
	}
	public void setPduSpdt(String pduSpdt) {
		this.pduSpdt = pduSpdt;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
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
	public String getHumanNeeds() {
		return humanNeeds;
	}
	public void setHumanNeeds(String humanNeeds) {
		this.humanNeeds = humanNeeds;
	}
	public String getNowHuman() {
		return nowHuman;
	}
	public void setNowHuman(String nowHuman) {
		this.nowHuman = nowHuman;
	}
	public String getMetrics() {
		return metrics;
	}
	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}
	public String getAccDevCode() {
		return accDevCode;
	}
	public void setAccDevCode(String accDevCode) {
		this.accDevCode = accDevCode;
	}
	public String getAccDevCase() {
		return accDevCase;
	}
	public void setAccDevCase(String accDevCase) {
		this.accDevCase = accDevCase;
	}
	public String getAccFindPro() {
		return accFindPro;
	}
	public void setAccFindPro(String accFindPro) {
		this.accFindPro = accFindPro;
	}
	public String getAccDealPro() {
		return accDealPro;
	}
	public void setAccDealPro(String accDealPro) {
		this.accDealPro = accDealPro;
	}
	public String getAccPutHuman() {
		return accPutHuman;
	}
	public void setAccPutHuman(String accPutHuman) {
		this.accPutHuman = accPutHuman;
	}
	public String getAccCodePdt() {
		return accCodePdt;
	}
	public void setAccCodePdt(String accCodePdt) {
		this.accCodePdt = accCodePdt;
	}
	public String getAccCasePdt() {
		return accCasePdt;
	}
	public void setAccCasePdt(String accCasePdt) {
		this.accCasePdt = accCasePdt;
	}
	public String getTestFindEffPro() {
		return testFindEffPro;
	}
	public void setTestFindEffPro(String testFindEffPro) {
		this.testFindEffPro = testFindEffPro;
	}
	public String getDealProEff() {
		return dealProEff;
	}
	public void setDealProEff(String dealProEff) {
		this.dealProEff = dealProEff;
	}
	public String getCodeReview() {
		return codeReview;
	}
	public void setCodeReview(String codeReview) {
		this.codeReview = codeReview;
	}
	public String getTestCase() {
		return testCase;
	}
	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}
	public String getTestDefects() {
		return testDefects;
	}
	public void setTestDefects(String testDefects) {
		this.testDefects = testDefects;
	}
	public String getVersionToTest() {
		return versionToTest;
	}
	public void setVersionToTest(String versionToTest) {
		this.versionToTest = versionToTest;
	}
	public String getProReturn() {
		return proReturn;
	}
	public void setProReturn(String proReturn) {
		this.proReturn = proReturn;
	}
	public String getCodeRepeat() {
		return codeRepeat;
	}
	public void setCodeRepeat(String codeRepeat) {
		this.codeRepeat = codeRepeat;
	}
	public String getMaxComplex() {
		return maxComplex;
	}
	public void setMaxComplex(String maxComplex) {
		this.maxComplex = maxComplex;
	}
	public String getAverageComplex() {
		return averageComplex;
	}
	public void setAverageComplex(String averageComplex) {
		this.averageComplex = averageComplex;
	}
	public String getLLTcover() {
		return LLTcover;
	}
	public void setLLTcover(String lLTcover) {
		LLTcover = lLTcover;
	}
	public String getPcLintErrorReset() {
		return pcLintErrorReset;
	}
	public void setPcLintErrorReset(String pcLintErrorReset) {
		this.pcLintErrorReset = pcLintErrorReset;
	}
	public String getLineErrorReset() {
		return lineErrorReset;
	}
	public void setLineErrorReset(String lineErrorReset) {
		this.lineErrorReset = lineErrorReset;
	}
	public String getCoverity() {
		return coverity;
	}
	public void setCoverity(String coverity) {
		this.coverity = coverity;
	}
	public String getSai() {
		return sai;
	}
	public void setSai(String sai) {
		this.sai = sai;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getCompleteAutomaticCase() {
		return completeAutomaticCase;
	}
	public void setCompleteAutomaticCase(String completeAutomaticCase) {
		this.completeAutomaticCase = completeAutomaticCase;
	}
	public String getProceedAutomaticCase() {
		return proceedAutomaticCase;
	}
	public void setProceedAutomaticCase(String proceedAutomaticCase) {
		this.proceedAutomaticCase = proceedAutomaticCase;
	}
	public String getAutomaticCaseExcute() {
		return automaticCaseExcute;
	}
	public void setAutomaticCaseExcute(String automaticCaseExcute) {
		this.automaticCaseExcute = automaticCaseExcute;
	}
	public String getAutomaticFactoryExcute() {
		return automaticFactoryExcute;
	}
	public void setAutomaticFactoryExcute(String automaticFactoryExcute) {
		this.automaticFactoryExcute = automaticFactoryExcute;
	}
	public String getAutomaticFactoryTotal() {
		return automaticFactoryTotal;
	}
	public void setAutomaticFactoryTotal(String automaticFactoryTotal) {
		this.automaticFactoryTotal = automaticFactoryTotal;
	}
	public String getTotalAutomaticScript() {
		return totalAutomaticScript;
	}
	public void setTotalAutomaticScript(String totalAutomaticScript) {
		this.totalAutomaticScript = totalAutomaticScript;
	}
	public String getOnceAutomaticExcuteSuccess() {
		return onceAutomaticExcuteSuccess;
	}
	public void setOnceAutomaticExcuteSuccess(String onceAutomaticExcuteSuccess) {
		this.onceAutomaticExcuteSuccess = onceAutomaticExcuteSuccess;
	}
	public String getOnceAutomaticExcuteCase() {
		return onceAutomaticExcuteCase;
	}
	public void setOnceAutomaticExcuteCase(String onceAutomaticExcuteCase) {
		this.onceAutomaticExcuteCase = onceAutomaticExcuteCase;
	}
	public String getHwpdu() {
		return hwpdu;
	}
	public void setHwpdu(String hwpdu) {
		this.hwpdu = hwpdu;
	}
	
}
