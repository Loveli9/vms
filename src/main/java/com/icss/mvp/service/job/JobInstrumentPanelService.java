package com.icss.mvp.service.job;

import com.icss.mvp.dao.IProjectInfoDao;
import com.icss.mvp.dao.InstrumentPanelDAO;
import com.icss.mvp.dao.ProjectInfoVoDao;
import com.icss.mvp.entity.*;
import com.icss.mvp.service.TestMeasuresService;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("jobInstrumentPanelService")
@EnableScheduling
@PropertySource("classpath:task.properties")
public class JobInstrumentPanelService {
	private final static Logger LOG = Logger.getLogger(JobInstrumentPanelService.class);

	@Autowired
	private InstrumentPanelDAO instrumentPanelDAO;
	@Autowired
	private TestMeasuresService testMeasuresService;
	@Autowired
	private ProjectInfoVoDao projectInfoVoDao;
	@Autowired
	private IProjectInfoDao projectInfoDao;

	/**
	 * 计算指标并存库
	 */
//	@Scheduled(cron = "${instrument_Task_scheduled}")
	public void calcu() {
		List<ProjectInfo> projectInfos = projectInfoDao.queryEffectiveProjects();
		for (ProjectInfo projectInfo : projectInfos) {
			String no =projectInfo.getNo(); 
			Double month = accumulativePersonMonth(no);// 累计人力投入
			Double loc = instrumentPanelDAO.codeValue(no, "167");// 判断pcb定时任务是否已经获取最新总代码量值
			productRate(no, loc, month);// E2E生产率
			demandDeliveryRate(no);// 累计需求交付率
		}
	}

	/**
	 * 读取指标值
	 */
	public Map<String, Object> board(String no) {		
		Map<String, Object> result = new HashMap<>();
		Double loc = instrumentPanelDAO.codeValue(no, "167");
		loc = loc == null ? 0.0 : StringUtilsLocal.keepTwoDecimals(loc / 1000);// 累计总代码量
		Double locStand = instrumentPanelDAO.value(no, "170");// 累计总代码量标准值
		locStand = locStand == null ? 0.0 : StringUtilsLocal.keepTwoDecimals(locStand);
		Double month = instrumentPanelDAO.value(no, "168");// 累计人力投入
		month = month == null ? 0.0 : StringUtilsLocal.keepTwoDecimals(month);
		Double monthStand = instrumentPanelDAO.value(no, "171");// 累计投入人月标准值
		monthStand = monthStand == null ? 0.0 : StringUtilsLocal.keepTwoDecimals(monthStand);
		Double eeRate = instrumentPanelDAO.value(no, "169");// E2E生产率
		eeRate = eeRate == null ? 0.0 : StringUtilsLocal.keepTwoDecimals(eeRate);
		Double eeRateStand = instrumentPanelDAO.value(no, "172");// E2E生产率标准值
		eeRateStand = eeRateStand == null ? 0.0 : StringUtilsLocal.keepTwoDecimals(eeRateStand);
		Double payRate = instrumentPanelDAO.value(no, "176");// 累计需求交付率
		payRate = payRate == null ? 0.0 : StringUtilsLocal.keepTwoDecimals(payRate);
		Double payRateStand = instrumentPanelDAO.value(no, "173");// 需求交付率标准值
		payRateStand = payRateStand == null ? 0.0 : StringUtilsLocal.keepTwoDecimals(payRateStand);
		result.put("loc", loc);// 累计总代码量
		result.put("month", month);// 累计投入人月
		result.put("eeRate", eeRate);// E2E生产率
		result.put("payRate", payRate);// 需求交付率
		result.put("locStand", locStand);// 累计总代码量标准值
		result.put("monthStand", monthStand);// 累计投入人月标准值
		result.put("eeRateStand", eeRateStand);// E2E生产率标准值
		result.put("payRateStand", payRateStand);// 需求交付率标准值
		return result;
	}

	/**
	 * 保存指标标准值
	 * 
	 * @param no
	 * @param id
	 * @param value
	 */
	public void saveStandardValue(String no, String id, String value) {
		instrumentPanelDAO.delete(no, id);
		ParameterValueNew parameterValueNew = new ParameterValueNew();
		parameterValueNew.setNo(no);
		parameterValueNew.setParameterId(Integer.valueOf(id));
		if (value == null || "".equals(value)) {
			value = "0.0";
		}
		parameterValueNew.setValue(Double.valueOf(value));
		instrumentPanelDAO.insert(parameterValueNew);
	}

	/**
	 * 累计人力投入
	 */
	private Double accumulativePersonMonth(String no) {
		ProjectInfoVo projectInfo = projectInfoVoDao.fetchProjectInfoByKey(no);
		int differ = DateUtils.getMonthDiff(new Date(), projectInfo.getStartDate());		
		String[] months = DateUtils.getLatestMonths(differ);
		List<MonthlyManpower> monthlyManpowers = null;
		Double sum = 0.0;
		Double result = 0.0;
		try {
			monthlyManpowers = testMeasuresService.getManpowerList(no, "0", months, true);
			for (MonthlyManpower monthlyManpower : monthlyManpowers) {
				sum += monthlyManpower.getSum();
			}
			result = sum;
		} catch (Exception e) {
			// TODO: handle exception
			result = sum;
		} finally {
			instrumentPanelDAO.delete(no, "168");
			ParameterValueNew parameterValueNew = new ParameterValueNew();
			parameterValueNew.setNo(no);
			parameterValueNew.setParameterId(168);
			parameterValueNew.setValue(StringUtilsLocal.keepTwoDecimals(result / 22.5));
			instrumentPanelDAO.insert(parameterValueNew);
		}
		return result;
	}

	/**
	 * E2E生产率
	 */
	private void productRate(String no, Double loc, Double month) {
		instrumentPanelDAO.delete(no, "169");
		Double rate = 0.0;
		if (month != 0.0 && null != loc) {
			rate = loc / month;
		}
		ParameterValueNew parameterValueNew = new ParameterValueNew();
		parameterValueNew.setNo(no);
		parameterValueNew.setParameterId(169);
		parameterValueNew.setValue(rate);
		instrumentPanelDAO.insert(parameterValueNew);
	}

	/**
	 * 累计需求交付率
	 */
	private void demandDeliveryRate(String no) {
		instrumentPanelDAO.delete(no, "176");
		Integer needs = instrumentPanelDAO.needs(no);
		Integer closedNeeds = instrumentPanelDAO.closedNeeds(no);
		Double rate = 0.0;
		if (needs != 0) {
			rate = Double.valueOf(closedNeeds) / Double.valueOf(needs) * 100;
		}
		ParameterValueNew parameterValueNew = new ParameterValueNew();
		parameterValueNew.setNo(no);
		parameterValueNew.setParameterId(176);
		parameterValueNew.setValue(rate);
		instrumentPanelDAO.insert(parameterValueNew);
	}

	/**
	 * 查询仪表盘排序
	 */
	public List<String> instrumentPanelValue(String no) {
		List<String> ids = new ArrayList<>();
		try {
			String instrumentPanelValue = instrumentPanelDAO.instrumentPanelValue(no, "174");
			if (instrumentPanelValue == null || "".equals(instrumentPanelValue)) {
				instrumentPanelValue = instrumentPanelDAO.instrumentPanel("174").getParameter();
			}
			String[] values = instrumentPanelValue.split(",");
			for (String value : values) {
				ids.add(value);
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e);
		}
		return ids;
	}

	/**
	 * 修改仪表盘排序
	 */
	public void changeInstrumentPanel(String no, String sort) {
		try {
			instrumentPanelDAO.deleteInstrumentPanelValue(no, "174");
			if (sort != null && !"".equals(sort)) {
				sort = sort.substring(0, sort.lastIndexOf(","));
			}
			ProjectParameter projectParameter = new ProjectParameter();
			projectParameter.setNo(no);
			projectParameter.setParameterId(174);
			projectParameter.setParameter(sort);
			projectParameter.setIsDisplay(1);
			instrumentPanelDAO.insertInstrumentPanelValue(projectParameter);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e);
		}
	}

}
