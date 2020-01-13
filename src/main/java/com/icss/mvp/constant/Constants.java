package com.icss.mvp.constant;


/**
 * 
 * <pre>
 * <b>描述：常量类</b>
 * <b>任务编号：</b>
 * <b>作者：张鹏飞</b> 
 * <b>创建日期： 2017年5月17日 下午2:37:43</b>
 * </pre>
 */
public class Constants {
	/**
	 * session存储用户信息KEY
	 */
	public final static String USER_KEY = "USERINFO";
	public final static String STATUS = "STATUS";

	public final static String FAILED = "FAILED";

	public final static String ABNORMITY = "ABNORMITY";

	public final static String MESSAGE = "MESSAGE";

	public final static String SUCCESS = "SUCCESS";

	public final static String TR5DI = "114";

	public final static String TR6DI = "115";

	public final static String TOTAL_DTS_NUM = "98";

	public final static String CODE_ADD_NUM = "129";

	public final static String ITERATION_DEFECT_DENSITY = "106";

	public final static String DTS_NO_PASS_NUM = "110";

	public final static String LEAVE_DI = "107";

	public final static String RESOLUTION_RATE = "111";
	public final static String STRUCT_TIME = "struct_time";

	public final static int CI_UADP_GARDING = 99;
	public final static int CI_SOURCE_MONITOR = 103;
	public final static int CI_LLT_COVERAGE = 135;
	public final static int CI_AVG_STRCUT = 120;
	public final static int CI_STRCUT_RATE = 118;
	public final static int CI_CODE_CC = 100;

	public final static String SVI_DEFECT_DENSITY = "112";

	public final static String CODE_DUPLICATION = "102";

	public final static String STATIC_CHECK_WARING = "101";

	public final static String MONTH_PRODUCT_RATE = "83";

	public final static String PROJECT_E2E_PRODUCT_RATE = "84";

	public final static String ITERATION_PRODUCT_RATE = "85";

	
	
	// DTS 相关
	public final static String CANCEL = "Cancel";
	public final static String CLOSE = "Close";
	public final static String CLOSURE_AFTER_CORRECTION = "Closure After Correction";
	public final static String CRITIAL = "Critical";
	public final static String MAJOR = "Major";
	public final static String MINOR = "Minor";
	public final static String SUGGESTION = "Suggestion";
	public final static String SYSTEM_VERIFICATION_TEST_SVT = "System Verification Test(SVT)";
	public final static String BETA_TEST = "Beta Test";
	public final static String GA = "GA";
	public final static String LIFT_CYCLE = "Lift Cycle(Including ESP Maintenace)";
	public final static String NETWORK_ENTRY_TEST = "network entry test";
	public final static String TR5_GA = "TR5-GA";
	public final static String SYSTEM_INTEGRATION_TEST_SIT = "System Integration Test(SIT)";

	
  /*****  消费者业务线 度量指标*****/
	//无效版本数
	public final static Integer INVALID_VERSIONS = 528;
	//回归不通过问题单数
	public final static Integer SINGULAR_REGRESSION_FAILURES = 591;
	//质量事故个数
	public final static Integer QUALITY_ACCIDENTS = 577;
	//质量预警个数
	public final static Integer QUALITY_EARLY_WARNING = 544;
	//严重网上问题个数
	public final static Integer SERIOUS_ONLINE_PROBLEMS= 546;
	//生产批量问题
	public final static Integer PRODUCTION_BATCH_PROBLEM= 545;
	//交付后漏测问题
	public final static Integer MISSING_TESTING_AFTER_DELIVERY= 558;
	//低质量、进度偏差严重，导致华为方的投诉
	public final static Integer LOW_UALITY_AND_SERIOUS_PROGRESS_DEVIATION= 550;
	
	//代码开发效率
	public final static Integer CODE_DEVELOPMENT_EFFICIENCY= 576;
	//E2E代码开发效率
	public final static Integer E2E_CODE_DEVELOPMENT_EFFICIENCY = 575;
	//问题解决效率
	public final static Integer PROBLEM_SOLVING_EFFICIENCY = 523;
	//版本交付效率
	public final static Integer VERSION_DELIVERY_EFFICIENCY = 541;
	//版本需求开发效率
	public final static Integer VERSION_REQUIREMENTS_DEVELOPMENT_EFFICIENCY = 579;
	//手工测试执行效率
	public final static Integer MANUAL_TEST_EXECUTION_EFFICIENCY = 574;
	//测试用例设计效率
	public final static Integer TEST_CASE_DESIGN_EFFICIENCY = 564;
	//测试自动化脚本开发效率
	public final static Integer TEST_AUTOMATION_SCRIPT_DEVELOPMENT_EFFICIENCY = 572;

}
