package com.icss.mvp.service;

import com.icss.mvp.dao.*;
import com.icss.mvp.dao.index.InTmplDao;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.IterationMeasureIndex;
import com.icss.mvp.entity.MeasureLoadEveryInfo;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 指标每日计算（定时任务）
 *
 * @author Administrator
 */
@Service
@EnableScheduling
@PropertySource("classpath:task.properties")
@SuppressWarnings("all")
@Transactional
public class MeasureLoadEveryDay {
    private static Logger logger = Logger.getLogger(MeasureLoadEveryDay.class);

    private final BigDecimal zero = new BigDecimal(0.00);

    @Autowired
    private MeasureLoadEveryDayDao dao;

    @Autowired
    private IterationCycleDao iterationCycleDao;

    @Autowired
    private IterationMeasureIndexDao iterationMeasureIndexDao;

    @Autowired
    private IProjectInfoDao projectInfoDao;

    @Autowired
    private IJobPcbDao jobPcbDao;

    @Autowired
    private MeasureCommentDao measureCommentDao;

    @Autowired
    private InTmplDao inTmplDao;
    
    @Autowired
    private JenkinsCollectService jenkinsCollectService;

    /**
     * 计算指标值(业务线通用)
     */
//    @Scheduled(cron = "${job-measure-datas}")
    public void countMeasureValues() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        logger.info("获取在行项目列表");
        List<ProjectInfo> projectInfos = projectInfoDao.queryEffectiveProjects();
        logger.info("当前时间与项目起止时间对比");
        for (ProjectInfo projectInfo : projectInfos) {
            String no = projectInfo.getNo();
//			String no = "HWHZP5FF1606F03X11";
            try {
                logger.info("项目：" + projectInfo.getName());
                logger.info("获取项目迭代列表信息");
                logger.info("计算非迭代相关指标，项目编号NO：" + projectInfo.getNo());
                calculateNotIteration(no);
                List<IterationCycle> list = iterationCycleDao.iterationListasc(no);
//				int totals = dao.isFirstCalculate(projectInfo.getNo());
                //获取项目实际配置指标
                List<String> ids = inTmplDao.getConfigMeasureIds(projectInfo.getNo(),false);
                if( null == ids || ids.size() == 0){
                    continue;
                }
                //获取手动指标ids
                Map<String,Object> param = new HashMap<>();
                param.put("proNo",no);
                param.put("ids",ids);
                param.put("flag",false);
                List<Map<String, Object>> mapList = inTmplDao.getConfigRecords(param);
                for (IterationCycle iterationCycle : list) {
                    if (iterationCycle.getStartDate().after(new Date()) || iterationCycle.getEndDate().before(new Date())) {
                        continue;
                    }
                    logger.info("计算迭代相关指标，项目编号NO：" + projectInfo.getNo());
                    startCalculate(projectInfo.getNo(), iterationCycle, ids, mapList);
                }
            } catch (Exception e) {
                logger.error("指标计算异常:" + projectInfo.getNo() + "-----" + e.getMessage());
            }
        }
    }

    /**
     * 计算非迭代相关指标
     *
     * @param no
     */
    private void calculateNotIteration(String no) {
        try {
            dao.insertNotIterationMeasure(no);
        } catch (Exception e) {
            logger.error("插入非配置化指标失败：" + no + "-----" + e.getMessage());
        }
    }

    /**
     * 开始计算指标
     *
     * @param no
     * @param iteId
     */
    private void startCalculate(String no, IterationCycle iterationCycle, List<String> idList,
                                List<Map<String, Object>> mapList) {

        String startDate = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getStartDate());
        String endDate = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getEndDate());

        List<IterationMeasureIndex> measureValues = dao.ManualMeasureValues(no, startDate, endDate);
        logger.info("自动采集指标开始计算");
        List<MeasureLoadEveryInfo> measureDetail = new ArrayList<MeasureLoadEveryInfo>();
        calculateCurrency(no, iterationCycle, measureValues, measureDetail);
        calculateConsumer(no, iterationCycle, measureValues, measureDetail);
        calculateGaussian(no, iterationCycle, measureDetail);
        //可信指标，jenkins采集结果存到各个表中，定时任务跑时将相应可信指标从jenkins表中添加到质量表中
//        jenkinsCollectService.calculateJenkins(no, iterationCycle, measureDetail);

        
//		if ("业务线通用".equals(category)) {
//			calculateCurrency(no, iterationCycle, manualMeasureList, measureDetail);
//		} else if ("消费者业务线".equals(category)) {
//			calculateConsumer(no, iterationCycle, manualMeasureList, measureDetail);
//		} else if ("高斯".equals(category)) {
//			calculateGaussian(no, iterationCycle, measureDetail);
//		}
        try {
            //只保留当前项目配置的指标运算结果
            measureDetail = measureDetail.stream().filter(ma -> idList.contains(ma.getMeasureId()))
                .collect(Collectors.toList());
            //自动->手动 过滤其自动计算结果
            if (measureDetail.size() > 0) {
                List<String> manuals = new ArrayList<>();
                if (null != mapList && mapList.size() > 0) {
                    for (Map<String, Object> map : mapList) {
                        if (null != map.get("collect_type") && "手工录入".equals(map.get("collect_type"))) {
                            manuals.add(map.get("measure_id").toString());
                        }
                    }
                }
                measureDetail = measureDetail.stream().filter(ma -> !manuals.contains(ma.getMeasureId()))
                    .collect(Collectors.toList());
            }
            if (measureDetail.size() > 0) {
                dao.insert(measureDetail);
            }
        } catch (Exception e) {
            logger.error("保存指标失败:" + no);
        }
    }

    private void calculateCurrency(String no, IterationCycle iterationCycle,
                                   List<IterationMeasureIndex> manualMeasureList, List<MeasureLoadEveryInfo> measureDetail) {
        // 迭代实际开始时间、结束时间
        String startDate = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getStartDate());
        Date iteEndDate = iterationCycle.getEndDate();
        String endDate = DateUtils.SHORT_FORMAT_GENERAL.format(iteEndDate);
        // 迭代计划开始时间、结束时间
        String planStartDate = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getPlanStartDate());
        String planEndDate = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getPlanEndDate());

        /******************************************* 需求指标计算 ***************************/

        logger.info("需求-需求稳定度指标计算");
        // '125','需求稳定度'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "125", returnDemandStability(
            manualIndex(126, manualMeasureList), manualIndex(127, manualMeasureList), iterationCycle)));
        logger.info("需求-生产率指标计算");
        // '132','需求生产率'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "132",
            returnValue(manualIndex(134, manualMeasureList), manualIndex(133, manualMeasureList))));

        logger.info("需求-需求文档质量指标计算");
        // '136','需求文档评审缺陷密度'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "136",
            returnValue(manualIndex(139, manualMeasureList), manualIndex(137, manualMeasureList))));
        // '138','需求对应新增代码量'
        Double codeNum = dao.queryCodeNum(no, startDate, endDate);
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "138", StringUtilsLocal.keepTwoDecimals(codeNum) + ""));
        // '142','检视发现问题处理率'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "142",
            returnParam(manualIndex(141, manualMeasureList), manualIndex(139, manualMeasureList))));

        logger.info("需求-需求交付率指标计算");
        // '129','需求交付率'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "129",
            returnParam(manualIndex(131, manualMeasureList), manualIndex(130, manualMeasureList))));
        /************************ 迭代指标计算 *********************/

        logger.info("迭代-遗留DI指标计算");
        Map<String, Object> bugInfo = iterationCycleDao.queryBugInfo(no, startDate, endDate);
        int critNum = 0;
        int majNum = 0;
        int minNum = 0;
        int sugNum = 0;
        BigDecimal dtsLeaveDINum = zero;
        if (null != bugInfo) {
            critNum = new Long(bugInfo.get("critNum").toString()).intValue();
            majNum = new Long(bugInfo.get("majNum").toString()).intValue();
            minNum = new Long(bugInfo.get("minNum").toString()).intValue();
            sugNum = new Long(bugInfo.get("sugNum").toString()).intValue();
            dtsLeaveDINum = (BigDecimal) bugInfo.get("dtsLeaveDINum");
        }
        int bugExportCount = critNum + majNum + minNum + sugNum;
        int bugSolve = iterationCycleDao.closedBugCount(no, startDate, endDate);
        int bugSumCount = bugExportCount + bugSolve;
        // '216','遗留致命问题个数'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "216", critNum + ""));
        // '219','遗留提示问题个数'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "219", sugNum + ""));
        // '217','遗留严重问题个数'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "217", majNum + ""));
        // '218','遗留一般问题个数'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "218", minNum + ""));
        // '215','遗留DI'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "215", dtsLeaveDINum + ""));
        // '220','迭代解决问题总数'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "220", bugSolve + ""));
        // '221','迭代问题总数'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "221", bugSumCount + ""));

        logger.info("迭代-迭代问题解决指标计算");
        // '204','迭代出口问题解决率'
        measureDetail
            .add(new MeasureLoadEveryInfo(no, iteEndDate, "204", returnParam(bugSolve + "", bugExportCount + "")));
        // '211','迭代出口问题总数'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "211", bugExportCount + ""));
        // '205','迭代内关闭问题个数'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "205", bugSolve + ""));
        // '206','迭代遗留DI'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "206", dtsLeaveDINum + ""));
        // '207','迭代遗留问题数(致命)'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "207", critNum + ""));
        // '210','迭代遗留问题数(提示)'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "210", sugNum + ""));
        // '208','迭代遗留问题数(严重)'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "208", majNum + ""));
        // '209','迭代遗留问题数(一般)'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "209", minNum + ""));
        logger.info("迭代-测试报告指标计算");
        // '212','报告评审缺陷密度'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "212",
            returnValue(manualIndex(213, manualMeasureList), manualIndex(214, manualMeasureList))));
        logger.info("迭代-迭代工作量偏差指标计算");
        List<Map<String, Object>> workHours = iterationCycleDao.workingHours(no, startDate, endDate);
        int day = monthDayNum(startDate, endDate);
        int cycle = betweenDays(endDate, startDate) + 1;// 计算这个迭代周期所跨月的总天数
        BigDecimal rate;
        try {
            rate = new BigDecimal(cycle).divide(new BigDecimal(day), 2, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            rate = zero;
        }
        BigDecimal workload, hours, deviationRate;
        if (CollectionUtils.isEmpty(workHours)) {
            workload = zero;
            hours = workload;
            deviationRate = workload;
        } else {
            Map<String, Object> map1 = workHours.get(0);
            workload = new BigDecimal((double) map1.get("dayNum")).multiply(rate).setScale(2, RoundingMode.HALF_UP);
            hours = new BigDecimal((double) map1.get("hourNum")).multiply(rate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal estimateWorkload = new BigDecimal(manualIndex(201, manualMeasureList));
            try {
                deviationRate = workload.subtract(estimateWorkload).multiply(new BigDecimal(100))
                    .divide(estimateWorkload, 2, RoundingMode.HALF_UP);
            } catch (ArithmeticException e) {
                deviationRate = zero;
            }
        }
        // '200','迭代工作量偏差'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "200", deviationRate + ""));
        // '202','迭代实际工作量'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "202", workload.toString()));
        // '203','迭代实际工时'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "203", hours.toString()));
        logger.info("迭代-迭代进度偏差指标计算");
        // 计划迭代周期天数(天)
        int planCycle = betweenDays(planEndDate, planStartDate) + 1;
        BigDecimal planCycleDecimal = new BigDecimal(planCycle);
        BigDecimal cycleDecimal = new BigDecimal(cycle);
        BigDecimal progressDeviation;
        try {
            progressDeviation = planCycleDecimal.subtract(cycleDecimal).multiply(new BigDecimal(100))
                .divide(planCycleDecimal, 2, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            progressDeviation = zero;
        }

        // '195','迭代进度偏差'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "195", progressDeviation + ""));
        // '196','迭代计划开始时间'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "196", planStartDate));
        // '197','迭代计划结束时间'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "197", planEndDate));
        // '198','迭代实际开始时间'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "198", startDate));
        // '199','迭代实际结束时间'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "199", endDate));
        /************************** 测试指标计算 *****************************/
        logger.info("测试-测试漏测指标计算");
        // '165','测试漏测率'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "165",
            returnParam(manualIndex(168, manualMeasureList), manualIndex(169, manualMeasureList))));

        logger.info("测试-测试缺陷密度指标计算");
        Double testCasePros = iterationCycleDao.queryWtdNum(no, startDate, endDate);
        // '170','转测试缺陷密度'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "170",
            returnValue(testCasePros.toString(), codeNum.toString())));
        // '171','迭代内测试发现缺陷数'
        measureDetail
            .add(new MeasureLoadEveryInfo(no, iteEndDate, "171", StringUtilsLocal.keepTwoDecimals(testCasePros) + ""));
        // '172','迭代新增代码规模'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "172", StringUtilsLocal.keepTwoDecimals(codeNum) + ""));
        logger.info("测试-用例发现缺陷比例指标计算");
        // '173','用例发现缺陷比例'
        BigDecimal autoBugs = new BigDecimal(manualIndex(174, manualMeasureList)); // 迭代自动化用例发现的缺陷数
        BigDecimal manuanBugs = new BigDecimal(manualIndex(175, manualMeasureList)); // 迭代手工用例发现的缺陷数
        BigDecimal othersBugs = new BigDecimal(manualIndex(176, manualMeasureList)); // 迭代其他方式发现的缺陷数
        BigDecimal allBugs = new BigDecimal(manualIndex(177, manualMeasureList)); // 迭代内测试发现总缺陷
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "173",
            returnParam((manuanBugs.add(autoBugs)) + "", allBugs + "")));
        /************************************ 开发指标计算 *********************************/
        logger.info("开发-LLT代码覆盖率指标计算");
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "121", 0 + ""));
        logger.info("开发-代码检视指标计算");
        // '115','迭代代码规模'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "115", StringUtilsLocal.keepTwoDecimals(codeNum) + ""));
        // '117','代码检视覆盖率'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "117",
            returnParam(manualIndex(116, manualMeasureList), codeNum + "")));
        // '118','代码检视发现问题总数'
        Integer wtCount = iterationCycleDao.queryCodeProblemCount(no, startDate, endDate);
        Integer wtDealCount = iterationCycleDao.queryCodeProblemDealCount(no, startDate, endDate);
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "118", wtCount + ""));
        // '120','代码检视发现问题处理率'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "120", returnParam(wtDealCount + "", wtCount + "")));

        logger.info("开发-开发自测试指标计算");
        int dtsCount = iterationCycleDao.queryDtsCount(no, startDate, endDate);
        // '122','迭代内开发发现缺陷密度'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "122", returnParam(dtsCount + "", codeNum + "")));
        // '123','迭代内开发自测试发现缺陷数'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "123", dtsCount + ""));
        // '124','迭代新增代码规模'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "124", StringUtilsLocal.keepTwoDecimals(codeNum) + ""));

        /********************************************
         * 验收指标计算
         **************************************/
        logger.info("验收-开发结项验收指标计算");
        // '153','结项验收发现缺陷密度'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "153",
            returnValue(manualIndex(154, manualMeasureList), codeNum + "")));
        // '155','结项新增代码规模'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "155", StringUtilsLocal.keepTwoDecimals(codeNum) + ""));

        logger.info("验收-开发迭代验收指标计算");
        // '143','迭代验收发现缺陷密度'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "143",
            returnValue(manualIndex(144, manualMeasureList), codeNum + "")));
        // '145','迭代新增代码规模'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "145", StringUtilsLocal.keepTwoDecimals(codeNum) + ""));

        logger.info("验收-开发遗留验收指标计算");
        BigDecimal yscritNum = new BigDecimal(manualIndex(160, manualMeasureList));
        BigDecimal ysmajNum = new BigDecimal(manualIndex(161, manualMeasureList));
        BigDecimal ysminNum = new BigDecimal(manualIndex(162, manualMeasureList));
        BigDecimal yssugNum = new BigDecimal(manualIndex(163, manualMeasureList));
        BigDecimal ysDI;
        try {
            yscritNum = yscritNum.multiply(new BigDecimal(10));
            ysmajNum = ysmajNum.multiply(new BigDecimal(3));
            yssugNum = yssugNum.multiply(new BigDecimal(0.1));
            ysDI = yscritNum.add(ysmajNum).add(ysminNum).add(yssugNum);
            ysDI = ysDI.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            ysDI = zero;
        }
        // '159','结项遗留DI值'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "159", ysDI + ""));

        logger.info("验收-测试结项验收指标计算");
        // '164','结项验收漏测数'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "164", 0 + "")); // 如何获取

        logger.info("验收-自动化迭代验收指标计算");
        // '150','自动化脚本验收缺陷密度'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "150",
            returnParam(manualIndex(151, manualMeasureList), manualIndex(152, manualMeasureList))));
        /****************************** 自动化指标计算 ******************************/
        logger.info("自动化-入厂前工厂验收通过率指标计算");
        // '186','入厂前工厂验收通过率'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "186",
            returnValue(manualIndex(187, manualMeasureList), manualIndex(188, manualMeasureList))));

        logger.info("自动化-全量自动化脚本连跑通过率指标计算");
        // '189','全量自动化脚本连跑通过率'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "189",
            returnValue(manualIndex(190, manualMeasureList), manualIndex(191, manualMeasureList))));

        logger.info("自动化-工厂脚本连跑失败分析完成率指标计算");
        // '192','工厂脚本连跑失败分析完成率'
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "192",
            returnValue(manualIndex(193, manualMeasureList), manualIndex(194, manualMeasureList))));
        logger.info("自动化-脚本检视缺陷密度指标计算");
        int addCaseINfo = iterationCycleDao.queryAddCaseINfo(no, startDate, endDate);
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "180",
            returnParam(manualIndex(181, manualMeasureList), addCaseINfo + "")));
        // 新增自动化用例脚本数
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "182", addCaseINfo + ""));

        logger.info("自动化-自动化用例执行率指标计算");
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "183",
            returnParam(manualIndex(184, manualMeasureList), manualIndex(185, manualMeasureList))));
        /*// JAVA迭代生产率
        String javaFiles = FileType.getFileTypes("JAVA");
		// c迭代生产率
		String cFiles = FileType.getFileTypes("C");
		// python迭代生产率
		String pythonFiles = FileType.getFileTypes("P");
		// 获取开发人员数量
		Integer developPerson = dao.getDevelopPerson(no);
		Double javaProduct = dao.getJavaOrcProduct(no, javaFiles, startDate, endDate);
		Double cProduct = dao.getJavaOrcProduct(no, cFiles, startDate, endDate);
		Double pythonProduct = dao.getJavaOrcProduct(no, pythonFiles, startDate, endDate);
		Double webProduct = dao.getJavaOrcProduct(no, null, startDate, endDate);
		if (developPerson * planCycle > 0) {
			javaProduct = javaProduct / (developPerson * planCycle);
			cProduct = cProduct / (developPerson * planCycle);
			pythonProduct = pythonProduct / (developPerson * planCycle);
			webProduct = webProduct / (developPerson * planCycle);
		}
		measureDetail
				.add(new MeasureLoadEveryInfo(no, iteEndDate, "254", StringUtilsLocal.keepTwoDecimals(javaProduct) + ""));
		// C/C++迭代生产率
		measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "255", StringUtilsLocal.keepTwoDecimals(cProduct) + ""));*/
        // 迭代测试用例设计效率
        Map<String, Long> caseMap = dao.queryCase(no, startDate, endDate);

        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "256", caseMap.get("design") + ""));

        // 迭代测试用例执行效率
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "257", caseMap.get("impl") + ""));

        // 自动化用例写作效率
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "258", caseMap.get("writing") + ""));

        // 自动化脚本连跑通过率
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "259",
            returnParam(caseMap.get("autoPassed").toString(), caseMap.get("autoTotal").toString())));

    }

    private void calculateConsumer(String no, IterationCycle iterationCycle,
                                   List<IterationMeasureIndex> manualMeasureList, List<MeasureLoadEveryInfo> measureDetail) {

        // 迭代实际开始时间、结束时间
        String startDate = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getStartDate());
        Date iteEndDate = iterationCycle.getEndDate();
        String endDate = DateUtils.SHORT_FORMAT_GENERAL.format(iteEndDate);
        // 迭代计划开始时间、结束时间
        String planStartDate = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getPlanStartDate());
        String planEndDate = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getPlanEndDate());

        // '138','需求对应新增代码量'
        Double codeNum = dao.queryCodeNum(no, startDate, endDate);

        Map<String, Object> bugInfo = iterationCycleDao.queryBugInfo(no, startDate, endDate);
        BigDecimal dtsLeaveDINum = zero;
        if (null != bugInfo) {
            dtsLeaveDINum = (BigDecimal) bugInfo.get("dtsLeaveDINum");
        }

        int cycle = betweenDays(endDate, startDate) + 1;// 计算这个迭代周期所跨月的总天数
        // 计划迭代周期天数(天)
        int planCycle = betweenDays(planEndDate, planStartDate) + 1;
        Double testCasePros = iterationCycleDao.queryWtdNum(no, startDate, endDate);
        int addCaseINfo = iterationCycleDao.queryAddCaseINfo(no, startDate, endDate);

        /****************************************
         * 消费者业务线指标计算
         **********************************************/
        // 问题总数
        Integer numberOfIssue = iterationCycleDao.numberOfIssues(no, startDate, endDate);
        // 获取代码行的值
        Integer lineOfCode = dao.getlineOfCode(no, startDate, endDate);
        if (null != lineOfCode) {
            lineOfCode = lineOfCode;
        } else {
            lineOfCode = 0;
        }
        logger.info("自动化-自动化指标计算");
        Integer autoScriptsNumber = dao.automationScriptsNumber(no, startDate, endDate);
        if (null != autoScriptsNumber) {
            autoScriptsNumber = autoScriptsNumber;
        } else {
            autoScriptsNumber = 0;
        }
        // 测试自动化脚本数
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "526", autoScriptsNumber + ""));
        // 测试自动化脚本开发效率
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "572",
            returnValue(autoScriptsNumber.toString(), manualIndex(539, manualMeasureList))));
        // 自动化用例比例571
        Integer automationUseCases = dao.getAutomationUseCases(no, startDate, endDate);
        Integer useCases = dao.getUseCases(no, startDate, endDate);
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "571",
            returnValue(automationUseCases.toString(), useCases.toString())));
        logger.info("需求—需求实现 ");
        // 需求实现率
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "561",
            returnParam(manualIndex(559, manualMeasureList), manualIndex(560, manualMeasureList))));

        // 版本需求开发效率
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "579",
            returnValue(manualIndex(559, manualMeasureList), manualIndex(535, manualMeasureList))));

        logger.info("开发—问题处理 ");
        // 解决问题数
        Integer solveProblem = iterationCycleDao.numberOfProblemSolvers(no, startDate, endDate);
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "524", solveProblem + ""));
        // 问题解决率
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "600",
            returnParam(solveProblem.toString(), numberOfIssue.toString())));
        // 回归不通过问题单数
        Integer returnOrNot = iterationCycleDao.numberReturnOrNot(no, startDate, endDate);
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "591", returnOrNot + ""));

        logger.info("开发—资料开发 ");
        // 资料开发效率565
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "565",
            returnValue(manualIndex(599, manualMeasureList), manualIndex(538, manualMeasureList))));

        logger.info("整版本—版本交付");
        // 版本交付效率
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "541",
            returnValue(manualIndex(525, manualMeasureList), manualIndex(534, manualMeasureList))));

        logger.info("测试指标—测试执行 ");
        Integer invalidProblem = iterationCycleDao.invalidProblem(no, startDate, endDate);
        Integer numberEffective = numberOfIssue - invalidProblem;
        // 问题数563
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "563", numberEffective + ""));
        // 执行测试用例数515
        Integer numberOfExecutionCases = dao.numberOfExecutionCases(no, startDate, endDate);
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "515", numberOfExecutionCases + ""));

        logger.info("测试指标—测试质量 ");
        // 无效问题数596
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "596", invalidProblem + ""));
        // 无效问题比例597 
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "597",
            returnParam(invalidProblem.toString(), numberOfIssue.toString())));

        // 手工执行测试用例总数
        Integer manualUseCases = dao.getManualUseCases(no, startDate, endDate);
        logger.info("测试效率—手工测试执行效率 ");
        // 手工测试执行效率574
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "574",
            returnValue(manualUseCases.toString(), manualIndex(537, manualMeasureList))));

        logger.info("测试设计—设计测试用例数 ");
        // 设计测试用例数598
        Integer NumberOfDesignCases = dao.getNumberOfDesignCases(no, startDate, endDate);
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "598", NumberOfDesignCases + ""));
        if (null != NumberOfDesignCases) {
            NumberOfDesignCases = NumberOfDesignCases;
        } else {
            NumberOfDesignCases = 0;
        }
        Integer scriptUseCases = autoScriptsNumber + NumberOfDesignCases;

        logger.info("测试—测试用例密度 ");
        // 测试用例密度555
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "555",
            returnValue(scriptUseCases.toString(), lineOfCode.toString())));

        logger.info("整版本—测试用例设计效率 ");
        // 测试用例设计效率564
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "564",
            returnValue(NumberOfDesignCases.toString(), manualIndex(536, manualMeasureList))));

        logger.info("整版本—研发效率 ");
        // 代码开发效率576
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "576",
            returnValue(lineOfCode.toString(), manualIndex(594, manualMeasureList))));
        // E2E代码开发效率575
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "575",
            returnValue(lineOfCode.toString(), manualIndex(593, manualMeasureList))));
        // 问题解决效率523
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "523",
            returnValue(numberOfIssue.toString(), manualIndex(540, manualMeasureList))));

        logger.info("测试—测试结果 ");
        // 软件缺陷密度软件缺陷密度（按DI）520
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "520",
            returnValue(dtsLeaveDINum.toString(), lineOfCode.toString())));
        // 软件缺陷密度（按个）516
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "516",
            returnValue(testCasePros.toString(), lineOfCode.toString())));

        logger.info("遗留DI—测试发现问题DI值 ");
        // 测试发现问题DI值592
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "592", dtsLeaveDINum + ""));
    }

    private void calculateGaussian(String no, IterationCycle iterationCycle, List<MeasureLoadEveryInfo> measureDetail) {
        /****************************************
         * 高斯指标计算
         **********************************************/
        // 迭代实际开始时间、结束时间
        String startDate = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getStartDate());
        Date iteEndDate = iterationCycle.getEndDate();
        String endDate = DateUtils.SHORT_FORMAT_GENERAL.format(iteEndDate);
        // 迭代计划开始时间、结束时间
        String planStartDate = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getPlanStartDate());
        String planEndDate = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getPlanEndDate());
        logger.info("E2E用例生产率指标计算");
        // 计划迭代周期天数(天)
        int planCycle = betweenDays(planEndDate, planStartDate) + 1;

        int addCaseINfo = iterationCycleDao.queryAddCaseINfo(no, startDate, endDate);
        // 获取开发人员数量
        Integer developPerson = dao.getDevelopPerson(no);
        Double zdhyl = 0.0;
        if (developPerson * planCycle > 0) {
            zdhyl = (double) addCaseINfo / (double) (developPerson * planCycle);
        }
        measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "274", StringUtilsLocal.keepTwoDecimals(zdhyl) + ""));
    }

    /**
     * 根据指标id获取手工录入的指标值
     *
     * @param a
     * @param manualMeasureList
     * @return
     */
    private String manualIndex(int a, List<IterationMeasureIndex> manualMeasureList) {
        for (IterationMeasureIndex iterationMeasureIndex : manualMeasureList) {
            if (String.valueOf(a).equals(iterationMeasureIndex.getMeasureId())) {
                if (StringUtils.isEmpty(iterationMeasureIndex.getValue())) {
                    return "0";
                }
                return iterationMeasureIndex.getValue();
            }
        }
        return "0";
    }

    /**
     * 时间比较
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static boolean compare_date(Date startDate, Date endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(df.format(new Date()));
            Date dt1 = df.parse(df.format(startDate));
            Date dt2 = df.parse(df.format(endDate));
            if (date.getTime() >= dt1.getTime() && date.getTime() <= dt2.getTime()) {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 比例计算*100%
     *
     * @param a
     * @param b
     * @return
     */
    public String returnParam(String a, String b) {
        BigDecimal param1 = new BigDecimal(StringUtils.isEmpty(a) ? "0" : a);
        BigDecimal param2 = new BigDecimal(StringUtils.isEmpty(b) ? "0" : b);
        BigDecimal param;
        try {
            param = param1.multiply(new BigDecimal(100)).divide(param2, 2, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            param = zero;
        }
        return param.toString();
    }

    /**
     * 比例计算
     *
     * @param a
     * @param b
     * @return
     */
    public String returnValue(String a, String b) {
        BigDecimal param1 = new BigDecimal(StringUtils.isEmpty(a) ? "0" : a);
        BigDecimal param2 = new BigDecimal(StringUtils.isEmpty(b) ? "0" : b);
        BigDecimal param;
        try {
            param = param1.divide(param2, 2, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            param = zero;
        }
        return param.toString();
    }

    /**
     * 需求稳定度计算
     *
     * @param a
     * @param b
     * @return
     */
    public String returnDemandStability(String a, String b, IterationCycle iterationCycle) {
        BigDecimal param1 = new BigDecimal(StringUtils.isEmpty(a) ? "0" : a);
        BigDecimal param2 = new BigDecimal(StringUtils.isEmpty(b) ? "0" : b);
        BigDecimal param;
        try {
            if (param2.compareTo(zero) == 0) {
                if (param1.compareTo(zero) == 0) {
                    param = new BigDecimal("100");
                } else {
                    param = zero;
                }
            } else {
                param = param2.subtract(param1).multiply(new BigDecimal(100)).divide(param2, 2, RoundingMode.HALF_UP);
            }
        } catch (ArithmeticException e) {
            param = zero;
            iterationCycle.setId("125");
            List<IterationMeasureIndex> values = iterationMeasureIndexDao.getMeasureIndexValue(iterationCycle);
            if (values.size() > 0) {
                param = new BigDecimal(values.get(0).getValue());
            }
        }
        return (param.compareTo(zero) < 0) ? "0.00" : param.setScale(2).toString();
    }

    private int monthDayNum(String startDate, String endDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startCalendar = Calendar.getInstance();
        try {
            startCalendar.setTime(format.parse(startDate));
            startCalendar.set(Calendar.DATE, 1);
            System.out.println(startCalendar);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(format.parse(endDate));
            endCalendar.set(Calendar.DATE, endCalendar.getActualMaximum(Calendar.DATE));
            System.out.println(endCalendar);
            return DateUtils.betweenDays(endCalendar.getTime(), startCalendar.getTime()) + 1;
        } catch (ParseException e) {
            logger.error("日期格式转换错误");
        }
        return 0;
    }

    /**
     * 计算两个日期相差的天数 date1 - date2
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int betweenDays(String endDate, String startDate) {
        int days;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = format.parse(endDate);
            Date date2 = format.parse(startDate);
            days = (int) Math.ceil((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));
            return days;
        } catch (ParseException e) {
            logger.error("日期格式转换错误");
        }
        return 0;
    }

}
