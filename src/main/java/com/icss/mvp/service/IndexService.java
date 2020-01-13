package com.icss.mvp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.IJobPcbDao;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.util.DateUtils;

@Service
@SuppressWarnings("all")
public class IndexService {
	private static Logger logger = Logger.getLogger(IndexService.class);
	@Resource
	private IJobPcbDao jobPcbDao;
	@Resource
	private IProjectListDao projectListDao;


	/**
	 * 根据parameter_info_new的id和项目编号，获取project_parameter_value_new对应的每个月的数据，返回
	 * @param proNo
	 * @param ids
	 * @return
	 */
	public Map<String,Object> getParameterValuesByMouth(String proNo, String[] ids) {
		Map<String,Object> retMap = new HashMap<>();
		List<String> listMouth = new ArrayList<>();
		List<String> idsList = new ArrayList<>();
		Map<String, List<Map<String, Object>>> parameterValues = new HashMap<>();
		
		ProjectInfo projectInfo= projectListDao.getProjInfoByNo(proNo);
		String startDate = DateUtils.format2.format(projectInfo.getStartDate());
		String endDate = DateUtils.format2.format(projectInfo.getEndDate());
		String mon = DateUtils.getSystemMonth();
		if(DateUtils.comparisonDateSize_yyyyMM(endDate, mon)) {
			mon = endDate;
		}
		if(!DateUtils.comparisonDateSize_yyyyMM(startDate, mon)) {
			logger.error("开始日期大于结束日期，错误---startDate="+startDate+"--endDate="+mon);
			return retMap;
		}
		for (String id : ids) {
			idsList.add(id);
			List<Map<String, Object>> list1 = jobPcbDao.getParameterValueByNoId(proNo,id,startDate,mon);
			if(list1!=null && list1.size()>0) {
				parameterValues.put(id, list1);
			}
		}
		List<Map<String, Object>> parameters = jobPcbDao.getParameterInfoIds("(" + StringUtilsLocal.listToSqlIn(idsList) + ")");
		retMap.put("parameters", parameters);
		
		
		Boolean falg = true;
		mon = DateUtils.getDateFewMonth(mon, 1);
		while (!startDate.equals(mon)) {
			String year = startDate.substring(0,4);
			String month = startDate.substring(startDate.length() - 2);
			startDate = DateUtils.getDateFewMonth(startDate, 1);
			Map<String, Object> retValue  = new HashMap<>();
			retValue.put("month", year+"-"+month);
			for (String id : ids) {
				falg = true;
				List<Map<String, Object>> parameters1 = parameterValues.get(id);
				if(parameters1==null||parameters1.size()<=0) {
					retValue.put("num"+id, 0.00);
					continue;
				}
				for (Map<String, Object> parameter : parameters1) {
					String monthMap = StringUtilsLocal.valueOf(parameter.get("date"));
					if (monthMap.endsWith(month)) {
						Double sum = StringUtilsLocal.parseDouble(parameter.get("VALUE"));
						retValue.put("num"+id, StringUtilsLocal.keepTwoDecimals(sum));
						falg=false;
						break;
					}
				}
				if(falg) {
					retValue.put("num"+id, 0.00);
				}
			}
			retMap.put("month"+year+month, retValue);
			listMouth.add(year+month);
		}
		retMap.put("month", listMouth);
		return retMap;
	}
	
	/**
	 * 根据parameter_info_new的id和项目编号，获取project_parameter_value_new对应的每个月的数据，返回
	 * 用于图表，返回数据为每个指标为一个map，里面有1-12月的值
	 * @param proNo
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> getParameterValuesByMouthImage(String proNo, String[] ids) {
		List<Map<String, Object>> ret = new ArrayList<>();
		List<Map<String, Object>> listSelect = new ArrayList<>();
		
		ProjectInfo projectInfo= projectListDao.getProjInfoByNo(proNo);
		String startDate = DateUtils.format2.format(projectInfo.getStartDate());
		String endDate = DateUtils.format2.format(projectInfo.getEndDate());
		String mon = DateUtils.getSystemMonth();
		if(DateUtils.comparisonDateSize_yyyyMM(endDate, mon)) {
			mon = endDate;
		}
		if(!DateUtils.comparisonDateSize_yyyyMM(startDate, mon)) {
			logger.error("开始日期大于结束日期，错误---startDate="+startDate+"--endDate="+mon);
			return ret;
		}
		String mon1 = DateUtils.getDateFewMonth(mon, 1);
		for (String id : ids) {
			List<Map<String, Object>> list = jobPcbDao.getParameterValueByNoId(proNo,id,startDate,mon);
			Map<String, Object> parameterInfo = jobPcbDao.getParameterInfo(id);
			List<String> idList = new ArrayList<String>();
			String name = StringUtilsLocal.valueOf(parameterInfo.get("name"));
			if (list!=null && list.size() > 0) {
				for (Map<String, Object> map : list) {
					Map<String, Object> retMap  = new HashMap<>();
					Double sum = StringUtilsLocal.parseDouble(map.get("VALUE"));
					retMap.put("num", StringUtilsLocal.keepTwoDecimals(sum));
					retMap.put("name", name);
					retMap.put("month", map.get("date"));
					listSelect.add(retMap);
				}
			}
			boolean falg;
			
			while (!startDate.equals(mon1)) {
				String year = startDate.substring(0,4);
				String month = startDate.substring(startDate.length() - 2);
				startDate = DateUtils.getDateFewMonth(startDate, 1);
				Map<String, Object> retValue  = new HashMap<>();
				falg = true;
				for (Map<String, Object> map : listSelect) {
					String monthMap = StringUtilsLocal.valueOf(map.get("month"));
					if (monthMap.endsWith(month)) {
						ret.add(map);
						falg = false;
						break;
					}
				}
				if (falg) {
					Map<String, Object> map = new HashMap<>();
					map.put("num", 0.0);
					map.put("month", year + "-" + month);
					map.put("name", name);
					ret.add(map);
				}
			}
		}
		return ret;
	}
	
	
}
