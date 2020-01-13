package com.icss.mvp.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.VerificationCycleDao;
import com.icss.mvp.dao.VersionStrucDao;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.util.DateUtils;

@Service
public class VerificationCycleService {
	private final static Logger LOG = Logger.getLogger(VerificationCycleService.class);
	
	@Autowired
	private VerificationCycleDao cycleDao;
	@Autowired
	private VersionStrucDao dao;

	/**
	 * 计算回归验证周期 自动化用例执行时长  C版本
	 * 该c版本下的b版本规模最大任务集时长，多个规模最大任务集b版本则取平均
	 * 各个b版本用例数最大的为规模最大
	 * @param projNo
	 * @return
	 */
	public Map<String, Object> returnValidateCycle(String projNo,String[] months,String yearNow) {
		Map<String, Object> reMap = new HashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		if(StringUtils.isEmpty(yearNow)) {
			yearNow = format.format(new Date());
		}
		
		List<Map<String, Object>> tmss = cycleDao.getTmssInfo(projNo,yearNow);
		if(tmss==null || tmss.size()<=0) {
			LOG.error("该项目下没有tmss信息");
			return reMap;
		}
		String cVersionNames = StringUtilsLocal.valueOf(tmss.get(0).get("c_version_name"));
		for (String month : months) {
			Double minute = 0.0;
			List<Map<String, Object>> tmssSums = cycleDao.getAllBVversion(projNo,month,yearNow);
			if(tmssSums==null || tmssSums.size()<=0) {
				reMap.put("mon"+month, minute);
				reMap.put("cVersionNames", cVersionNames);
				continue;
			}
			String numMax =  StringUtilsLocal.valueOf(tmssSums.get(0).get("numAll"));
			List<String> bVersionNames = new ArrayList<>();
			for (Map<String, Object> map : tmssSums) {
				String numAll  =  StringUtilsLocal.valueOf(map.get("numAll"));
				if(numMax.equals(numAll)) {
					bVersionNames.add(StringUtilsLocal.valueOf(map.get("b_version_name")));
				}
			}
			
			for (String bVersionName : bVersionNames) {
				Map<String, Object> minutesNum = cycleDao.getMinutesGroupMouth(projNo,bVersionName,month,yearNow);
				Double minutes = Double.parseDouble(StringUtilsLocal.valueOf(minutesNum.get("minutes")));
				Long num = Long.parseLong(StringUtilsLocal.valueOf(minutesNum.get("num")));
				if(num==0) {
					continue;
				}
				minute += minutes/num;
			}
			minute = minute/bVersionNames.size();
			reMap.put("mon"+month, keepTwoDecimals(minute));
			reMap.put("cVersionNames", cVersionNames);
		}
		
		return reMap;
	}
	
	/**
	 * 计算回归验证周期 自动化用例执行时长  C版本
	 * 取一个月的，当前月的前一月
	 * @param projNo
	 * @return
	 */
	public Map<String, Object> returnValidateCycle(String projNo,String month,String yearNow) {
		Map<String, Object> reMap = new HashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		if(StringUtils.isEmpty(yearNow)) {
			yearNow = format.format(new Date());
		}
		
		List<Map<String, Object>> tmss = cycleDao.getTmssInfo(projNo,yearNow);
		if(tmss==null || tmss.size()<=0) {
			LOG.error("该项目下没有tmss信息");
			reMap.put("result", 0);
			return reMap;
		}
		String cVersionNames = StringUtilsLocal.valueOf(tmss.get(0).get("c_version_name"));
		
		Double minute = 0.0;
		List<Map<String, Object>> tmssSums = cycleDao.getAllBVversion(projNo,month,yearNow);
		if(tmssSums==null || tmssSums.size()<=0) {
			LOG.error("该项目下没有tmss信息");
			reMap.put("result", 0);
			return reMap;
		}
		String numMax =  StringUtilsLocal.valueOf(tmssSums.get(0).get("numAll"));
		List<String> bVersionNames = new ArrayList<>();
		for (Map<String, Object> map : tmssSums) {
			String numAll  =  StringUtilsLocal.valueOf(map.get("numAll"));
			if(numMax.equals(numAll)) {
				bVersionNames.add(StringUtilsLocal.valueOf(map.get("b_version_name")));
			}
		}
		
		for (String bVersionName : bVersionNames) {
			Map<String, Object> minutesNum = cycleDao.getMinutesGroupMouth(projNo,bVersionName,month,yearNow);
			Double minutes = Double.parseDouble(StringUtilsLocal.valueOf(minutesNum.get("minutes")));
			Long num = Long.parseLong(StringUtilsLocal.valueOf(minutesNum.get("num")));
			if(num==0) {
				continue;
			}
			minute += minutes/num;
		}
		minute = minute/bVersionNames.size();
		reMap.put("result", keepTwoDecimals(minute));
		reMap.put("cVersionNames", cVersionNames);
		
		return reMap;
	}
	
	/**
	 * 全量功能验证周期  C版本   
	 * 注：解决方案验证周期 计算方法和 全量功能验证周期 一致，两个只有一个有值，按照配置区分
	 * 包含自动化用例和手工用例
	 * 只统计pass，fail，investigate，执行率95%以上，取执行率到达95%时的时间
	 * b版本测试执行周期=从第一次执行到最后一次执行率达到95%为止，期间经历的时长
	 * c版本下所以b版本测试用例周期取平均值
	 * @param projNo
	 * @return
	 */
	public Map<String, Object> totalValidateCycle(String projNo,String[] months,String yearNow) {
		Map<String, Object> reMap = new HashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		if(StringUtils.isEmpty(yearNow)) {
			yearNow = format.format(new Date());
		}
		
		List<Map<String, Object>> tmss = cycleDao.getTmssInfo(projNo,yearNow);
		if(tmss==null || tmss.size()<=0) {
			LOG.error("该项目下没有tmss信息");
			return reMap;
		}
		String cVersionNames = StringUtilsLocal.valueOf(tmss.get(0).get("c_version_name"));
		
		for (String month : months) {
			Double minute = 0.0;
			List<Map<String, Object>> tmssVersions = cycleDao.getTotalAllBVversion(projNo,month,yearNow);
			if(tmssVersions==null || tmssVersions.size()<=0) {
				reMap.put("mon"+month, minute);
				reMap.put("cVersionNames", cVersionNames);
				continue;
			}
			for (Map<String, Object> tmssVersion : tmssVersions) {
				String bVersionName = StringUtilsLocal.valueOf(tmssVersion.get("b_version_name"));
				Map<String, Object> minutesNum = cycleDao.getMinutesGroupMouth(projNo,bVersionName,month,yearNow);
				Double minutes = Double.parseDouble(StringUtilsLocal.valueOf(minutesNum.get("minutes")));
				Long num = Long.parseLong(StringUtilsLocal.valueOf(minutesNum.get("num")));
				if(num==0) {
					continue;
				}
				minute += minutes/num;
			}
			minute = minute/tmssVersions.size();
			reMap.put("mon"+month, keepTwoDecimals(minute));
			reMap.put("cVersionNames", cVersionNames);
		}
		
		return reMap;
	}
	
	/**
	 * 全量功能验证周期  C版本   
	 * 注：解决方案验证周期 计算方法和 全量功能验证周期 一致，两个只有一个有值，按照配置区分
	 * 取一个月的，当前月的前一月
	 * @param projNo
	 * @return
	 */
	public Map<String, Object> totalValidateCycle(String projNo,String month,String yearNow) {
		Map<String, Object> reMap = new HashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		if(StringUtils.isEmpty(yearNow)) {
			yearNow = format.format(new Date());
		}
		
		List<Map<String, Object>> tmssVersions = cycleDao.getTotalAllBVversion(projNo,month,yearNow);
		if(tmssVersions==null || tmssVersions.size()<=0) {
			LOG.error("该项目下没有tmss信息");
			return reMap;
		}
		String cVersionNames = StringUtilsLocal.valueOf(tmssVersions.get(0).get("c_version_name"));
		Double minute = 0.0;
		for (Map<String, Object> tmssVersion : tmssVersions) {
			String bVersionName = StringUtilsLocal.valueOf(tmssVersion.get("b_version_name"));
			Map<String, Object> minutesNum = cycleDao.getMinutesGroupMouth(projNo,bVersionName,month,yearNow);
			Double minutes = Double.parseDouble(StringUtilsLocal.valueOf(minutesNum.get("minutes")));
			Long num = Long.parseLong(StringUtilsLocal.valueOf(minutesNum.get("num")));
			if(num==0) {
				continue;
			}
			minute += minutes/num;
		}
		minute = minute/tmssVersions.size();
		reMap.put("result", keepTwoDecimals(minute));
		reMap.put("cVersionNames", cVersionNames);
		
		return reMap;
	}
	
	public Double getRebackTime(String projNo,String yearNow) {
		String month = DateUtils.getSystemFewMonth(0);
		int len = month.length();
		month = month.substring(len-2, len);
		Double minute = 0.0;
		
		List<Map<String, Object>> tmssSums = cycleDao.getAllBVversionNos(projNo,month,yearNow);
		if(tmssSums==null || tmssSums.size()<=0) {
			LOG.error("该项目下没有tmss信息");
			return 0.0;
		}
		String numMax =  StringUtilsLocal.valueOf(tmssSums.get(0).get("numAll"));
		List<String> bVersionNames = new ArrayList<>();
		for (Map<String, Object> map : tmssSums) {
			String numAll  =  StringUtilsLocal.valueOf(map.get("numAll"));
			if(numMax.equals(numAll)) {
				bVersionNames.add(StringUtilsLocal.valueOf(map.get("b_version_name")));
			}
		}
		String bVersionName = StringUtilsLocal.listToSqlIn(bVersionNames);
		Map<String, Object> minutesNum = cycleDao.getMinutesGroupMouthVers("("+bVersionName+")",month,yearNow);
		Double minutes = Double.parseDouble(StringUtilsLocal.valueOf(minutesNum.get("minutes")));
		Long num = Long.parseLong(StringUtilsLocal.valueOf(minutesNum.get("num")));
		if(num==0) {
			return minute;
		}
		minute = minutes/num;
		return keepTwoDecimals(minute);
	}
	
	public Double getQuanliangTime(String projNo,String yearNow) {
		String month = DateUtils.getSystemFewMonth(0);
		int len = month.length();
		month = month.substring(len-2, len);
		Double minute = 0.0;
		
		List<Map<String, Object>> tmssVersions = cycleDao.getTotalAllBVversionNos(projNo,month,yearNow);
		if(tmssVersions==null || tmssVersions.size()<=0) {
			LOG.error("该项目下没有tmss信息");
			return minute;
		}
		List<String> bVersionNames = new ArrayList<>();
		for (Map<String, Object> map : tmssVersions) {
			bVersionNames.add(StringUtilsLocal.valueOf(map.get("b_version_name")));
		}
		String bVersionName = StringUtilsLocal.listToSqlIn(bVersionNames);
		
		Map<String, Object> minutesNum = cycleDao.getMinutesGroupMouthVers("("+bVersionName+")",month,yearNow);
		Double minutes = Double.parseDouble(StringUtilsLocal.valueOf(minutesNum.get("minutes")));
		Long num = Long.parseLong(StringUtilsLocal.valueOf(minutesNum.get("num")));
		if(num==0) {
			return minute;
		}
		minute += minutes/num;
		return keepTwoDecimals(minute);
	}
	

	public Map<String, Object> returnValidateCycleByNos(ProjectInfo proj, String[] months) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		List<String> prons = new ArrayList<String>();
		//查询项目编号
		Map<String, Object> maps = setMap(proj);
		List<ProjectInfo> listNos = dao.querySelectedNos(maps);
		for (ProjectInfo pros : listNos) {
			prons.add(pros.getNo());
		}
		String sqlStr = StringUtilsLocal.listToSqlIn(prons);
		
		List<Map<String, Object>> tmss = cycleDao.getTmssInfoNos("("+sqlStr+")",proj.getYear());
		if(tmss==null || tmss.size()<=0) {
			LOG.error("该项目下没有tmss信息");
			return reMap;
		}
//		String cVersionNames = StringUtilsLocal.valueOf(tmss.get(0).get("c_version_name"));
		for (String month : months) {
			Double minute = 0.0;
			List<Map<String, Object>> tmssSums = cycleDao.getAllBVversionNos("("+sqlStr+")",month,proj.getYear());
			if(tmssSums==null || tmssSums.size()<=0) {
				reMap.put("mon"+month, minute);
//				reMap.put("cVersionNames", cVersionNames);
				continue;
			}
			String numMax =  StringUtilsLocal.valueOf(tmssSums.get(0).get("numAll"));
			List<String> bVersionNames = new ArrayList<>();
			for (Map<String, Object> map : tmssSums) {
				String numAll  =  StringUtilsLocal.valueOf(map.get("numAll"));
				if(numMax.equals(numAll)) {
					bVersionNames.add(StringUtilsLocal.valueOf(map.get("b_version_name")));
				}
			}
			String bVersionName = StringUtilsLocal.listToSqlIn(bVersionNames);
			Map<String, Object> minutesNum = cycleDao.getMinutesGroupMouthVers("("+bVersionName+")",month,proj.getYear());
			Double minutes = Double.parseDouble(StringUtilsLocal.valueOf(minutesNum.get("minutes")));
			Long num = Long.parseLong(StringUtilsLocal.valueOf(minutesNum.get("num")));
			if(num==0) {
				reMap.put("mon"+month, minute);
				continue;
			}
			minute = minutes/num;
			reMap.put("mon"+month, keepTwoDecimals(minute));
//			reMap.put("cVersionNames", cVersionNames);
		}
		return reMap;
	}
	
	public Map<String, Object> totalValidateCycleNos(ProjectInfo proj, String[] months) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		List<String> prons = new ArrayList<String>();
		//查询项目编号
		Map<String, Object> maps = setMap(proj);
		List<ProjectInfo> listNos = dao.querySelectedNos(maps);
		for (ProjectInfo pros : listNos) {
			prons.add(pros.getNo());
		}
		String sqlStr = StringUtilsLocal.listToSqlIn(prons);
		List<Map<String, Object>> tmss = cycleDao.getTmssInfoNos("("+sqlStr+")",proj.getYear());
		if(tmss==null || tmss.size()<=0) {
			LOG.error("该项目下没有tmss信息");
			return reMap;
		}
//		String cVersionNames = StringUtilsLocal.valueOf(tmss.get(0).get("c_version_name"));
		
		for (String month : months) {
			Double minute = 0.0;
			List<Map<String, Object>> tmssVersions = cycleDao.getTotalAllBVversionNos("("+sqlStr+")",month,proj.getYear());
			if(tmssVersions==null || tmssVersions.size()<=0) {
				reMap.put("mon"+month, minute);
//				reMap.put("cVersionNames", cVersionNames);
				continue;
			}
			List<String> bVersionNames = new ArrayList<>();
			for (Map<String, Object> map : tmssVersions) {
				bVersionNames.add(StringUtilsLocal.valueOf(map.get("b_version_name")));
			}
			String bVersionName = StringUtilsLocal.listToSqlIn(bVersionNames);
			
			Map<String, Object> minutesNum = cycleDao.getMinutesGroupMouthVers("("+bVersionName+")",month,proj.getYear());
			Double minutes = Double.parseDouble(StringUtilsLocal.valueOf(minutesNum.get("minutes")));
			Long num = Long.parseLong(StringUtilsLocal.valueOf(minutesNum.get("num")));
			if(num==0) {
				reMap.put("mon"+month, minute);
				continue;
			}
			minute += minutes/num;
			reMap.put("mon"+month, keepTwoDecimals(minute));
//			reMap.put("cVersionNames", cVersionNames);
		}
		return reMap;
	}
	

	public Double keepTwoDecimals(double d1) {
		if(d1==0.0) {
			return d1;
		}
		BigDecimal b = new BigDecimal(d1);  
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private Map<String, Object> setMap(ProjectInfo proj) {
		Map<String, Object> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		if (proj.getArea() != null && !"".equals(proj.getArea())) {
			String[] arr = proj.getArea().split(",");
			map.put("area", Arrays.asList(arr));
		} else {
			map.put("area", list);
		}
		if (proj.getHwpdu() != null && !"".equals(proj.getHwpdu())) {
			String[] arr = proj.getHwpdu().split(",");
			map.put("hwpdu", Arrays.asList(arr));
		} else {
			map.put("hwpdu", list);
		}
		if (proj.getHwzpdu() != null && !"".equals(proj.getHwzpdu())) {
			String[] arr = proj.getHwzpdu().split(",");
			map.put("hwzpdu", Arrays.asList(arr));
		} else {
			map.put("hwzpdu", list);
		}
		if (proj.getPduSpdt() != null && !"".equals(proj.getPduSpdt())) {
			String[] arr = proj.getPduSpdt().split(",");
			map.put("pduSpdt", Arrays.asList(arr));
		} else {
			map.put("pduSpdt", list);
		}
		if (proj.getBu() != null && !"".equals(proj.getBu())) {
			String[] arr = proj.getBu().split(",");
			map.put("bu", Arrays.asList(arr));
		} else {
			map.put("bu", list);
		}
		if (proj.getPdu() != null && !"".equals(proj.getPdu())) {
			String[] arr = proj.getPdu().split(",");
			map.put("pdu", Arrays.asList(arr));
		} else {
			map.put("pdu", list);
		}
		if (proj.getDu() != null && !"".equals(proj.getDu())) {
			String[] arr = proj.getDu().split(",");
			map.put("du", Arrays.asList(arr));
		} else {
			map.put("du", list);
		}
		return map;
	}

	
}
