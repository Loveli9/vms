package com.icss.mvp.service.job;

import com.icss.mvp.constant.FileTypeEnum.FileType;
import com.icss.mvp.dao.*;
import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author up
 * 
 */
@Service("jobPcbService")
@EnableScheduling
@PropertySource("classpath:task.properties")
@SuppressWarnings("all")
@Transactional
public class JobPcbService {

	private static Logger logger = Logger.getLogger(JobPcbService.class);
	private static final String[] MONTHS = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
	private static final SimpleDateFormat YEAR = new SimpleDateFormat("yyyy");
	private static final SimpleDateFormat YEARMOUTHDAY = new SimpleDateFormat("yyyy-MM-dd");

	@Resource
	private ProjectInfoVoDao projectInfoVoDao;
	@Resource
	private ILowLocDao lowLocDao;
	@Resource
	private IJobPcbDao jobPcbDao;
	@Resource
	private IProjectParameterValueDao parameterValueDao;
	@Autowired
	private IPersonalBulidTimeDao personalBulidTimeDao;
	@Autowired
	private VersionStrucDao versionStrucDao;
	@Autowired
	private VerificationCycleDao verificationCycleDao;
	
	@Resource
	private IProjectManagersDao managersDao;
	
	@Autowired
	private MonthMeasureDao measureDao;
	@Autowired
	private IProjectInfoDao projectInfoDao;
	/**
	 * PCB指标定时计算入库 每天凌晨一点执行
	 */
//	@Scheduled(cron = "${pcb_Task_scheduled}")
	public void saveEfficiency() {
		List<ProjectInfo> projectInfos = projectInfoDao.queryEffectiveProjects();

		// 自动化用例占比 24
		try {
			insertTestMeasures(projectInfos);
		} catch (Exception e) {
			logger.error("自动化用例占比计算错误",e);
		}
		
		// 代码检视缺陷密度 17
		try {
			codeviewDefectDensity(projectInfos);
		} catch (Exception e) {
			logger.error("代码检视缺陷密度计算错误",e);
		}
		// 个人构建平均时长 9
		try {
			personalBulidTime(projectInfos);
		} catch (Exception e) {
			logger.error("个人构建平均时长计算错误",e);
		}
		
		// 版本级编译构建时间 10
		try {
			VersionStrucInfo(projectInfos);
		} catch (Exception e) {
			logger.error("版本级编译构建时间计算错误",e);
		}
		
		// 全量自动化回归验证周期 11
		try {
			returnValidateCycle(projectInfos);
		} catch (Exception e) {
			logger.error("全量自动化回归验证周期计算错误",e);
		}
		// 全量功能验证周期  12
		try {
			totalValidateCycle(projectInfos,12);
		} catch (Exception e) {
			logger.error("全量功能验证周期计算错误",e);
		}
		// 解决方案验证周期 13
		try {
			totalValidateCycle(projectInfos,13);
		} catch (Exception e) {
			logger.error("解决方案验证周期计算错误",e);
		}
		// 研发效率指标入库 15 16
		/*try {
			insertDevelopTarget(projectInfos);
		} catch (Exception e) {
			logger.error("研发效率指标计算错误",e);
		}*/
		
	}

	/**
	 * 自动化用例占比
	 * 
	 * @param projectInfos
	 */
	private void insertTestMeasures(List<ProjectInfo> projectInfos) {
		List<String> projNo = new ArrayList<>();
		List<ParameterValueNew> inserts= new ArrayList<>();
		//自动化用例占比 24
		int parameterId = 24;
		
		for (ProjectInfo projectInfo : projectInfos) {
			projNo.add(projectInfo.getNo());
		}
		Date todat = new Date();
		String year = YEAR.format(todat);
		String todatMonth = YEARMOUTHDAY.format(todat);
		todatMonth = todatMonth.substring(5, 7);
		for (String month : MONTHS) {
			String date =  year + "-" + month;
			if(Integer.parseInt(todatMonth) < Integer.parseInt(month)) {
				continue;
			}
			List<Map<String, Object>> autoTestCases = jobPcbDao.autoTestCaseNum("(" + StringUtilsLocal.listToSqlIn(
                    projNo) + ")", date);
			List<Map<String, Object>> testCases = jobPcbDao.testCaseCount("(" + StringUtilsLocal.listToSqlIn(projNo) + ")", date);
			for (Map<String, Object> testCase : testCases) {
				for (Map<String, Object> autoTestCase : autoTestCases) {
					if(autoTestCase.get("NO").equals(testCase.get("NO"))) {
						Double nums = StringUtilsLocal.parseDouble(testCase.get("nums"));
						Double autoNums = StringUtilsLocal.parseDouble(autoTestCase.get("nums"));
						nums = autoNums/nums;
						ParameterValueNew parameterValueNew = new ParameterValueNew();
						parameterValueNew.setNo(StringUtilsLocal.valueOf(testCase.get("NO")));
						try {
							parameterValueNew.setMonth(YEARMOUTHDAY.parse(date+"-01"));
						} catch (ParseException e) {
							logger.error("时间序列化异常：",e);
						}
						parameterValueNew.setParameterId(parameterId);
						parameterValueNew.setValue(StringUtilsLocal.keepTwoDecimals(nums));
						inserts.add(parameterValueNew);
					}
				}
			}
		}
		
		if(inserts.size()>0) {
//			parameterValueDao.deleteParameterValueByIds("(" + StringUtilsLocal.listToSqlIn(projNo) + ")", parameterId+"");
			parameterValueDao.insertParams(inserts);
		}
	}

	/**
	 * 代码检视缺陷密度
	 */
	private void codeviewDefectDensity(List<ProjectInfo> projectInfos) {
		List<String> projNo = new ArrayList<>();
		//代码检视缺陷密度 17
		int parameterId = 17;
		for (ProjectInfo projectInfo : projectInfos) {
			projNo.add(projectInfo.getNo());
		}
		Date todat = new Date();
		String year = YEAR.format(todat);
		String todatMonth = YEARMOUTHDAY.format(todat);
		todatMonth = todatMonth.substring(5, 7);
		List<ParameterValueNew> inserts=new ArrayList<>();
		for (String no : projNo) {
			for (String month : MONTHS) {
				Integer countLoc = 0;
				if(Integer.parseInt(todatMonth)<Integer.parseInt(month)) {
					continue;
				}
				// 获得项目组成员信息
				List<Map<String, Object>> members = lowLocDao.queryMember(no);
				// 获得代码统计方式
				Integer type = lowLocDao.queryTongjiType(no);
				if(type==null) {
					type=0;
				}
				String date = year + month;
				for (Map<String, Object> member : members) {
					Integer loc = 0;
					if (type == 0) {// 0为commit统计
						if (member.get("svngit") == null || "".equals(String.valueOf(member.get("svngit")))) {
							loc = lowLocDao.queryLocWx(String.valueOf(member.get("account")), date, no);
						} else {
							loc = lowLocDao.queryLoc(String.valueOf(member.get("svngit")), date, no);
						}
					} else if (type == 1) {// 1为message统计
						if (member.get("svngit") == null || "".equals(String.valueOf(member.get("svngit")))) {
							loc = lowLocDao.queryLocByMessage(String.valueOf(member.get("account")), date, no);
						} else {
							loc = lowLocDao.queryLocByMessage(String.valueOf(member.get("svngit")), date, no);
						}
					}
					if(loc==null) {
						loc=0;
					}
					countLoc = countLoc + loc;
				}
				//检视代码规模Kloc
				Double kloc= StringUtilsLocal.keepTwoDecimals(Double.valueOf(countLoc) / 1000);
				//代码检视发现缺陷个数（一般+严重）
				Double problemNum= StringUtilsLocal.keepTwoDecimals(
                        Double.valueOf(jobPcbDao.codecheckDefectNum(date, no)));
				Double rate=0.0;
				if(kloc!=0) {
					rate= StringUtilsLocal.keepTwoDecimals(problemNum / kloc);
				}
				ParameterValueNew para=new ParameterValueNew();
				para.setNo(no);
				try {
					para.setMonth(YEARMOUTHDAY.parse(year +"-"+ month+"-01"));
				} catch (ParseException e) {
					logger.error("时间序列化异常：",e);
				}
				para.setParameterId(parameterId);
				para.setValue(rate);
				inserts.add(para);
			}
		}
		if(inserts.size()>0) {
			parameterValueDao.insertParams(inserts);
		}
	}
	
	/**
	 * 个人构建时长
	 * @param projectInfos
	 */
	private void personalBulidTime(List<ProjectInfo> projectInfos) {
		//版本级编译构建时间 9
		int parameterId = 9;
		Date todat = new Date();
		String year = YEAR.format(todat);
		String years = "";
		String todatMonth = YEARMOUTHDAY.format(todat);
		todatMonth = todatMonth.substring(5, 7);
		List<ParameterValueNew> inserts=new ArrayList<>();
		for (ProjectInfo projectInfo : projectInfos) {
			String no = projectInfo.getNo();
			for (int i = 1; i <= 12; i++) {
				if(Integer.parseInt(todatMonth)<i) {
					continue;
				}
				if (i < 10) {
					years = year + "-0" + String.valueOf(i);
				} else {
					years = year + "-" + String.valueOf(i);
				}
				List<Map<String, Object>> buildList = personalBulidTimeDao.buildPerMonth(years, no);
				Double time = 0.0;
				ParameterValueNew para=new ParameterValueNew();
				para.setNo(no);
				try {
					para.setMonth(YEARMOUTHDAY.parse(years + "-01"));
				} catch (ParseException e) {
					logger.error("时间序列化异常：",e);
				}
				para.setParameterId(parameterId);
				
				if (buildList != null && buildList.size() != 0) {
					for (Map<String, Object> map : buildList) {
						Double minutes = DateUtils.comparisonDateSizeYMdHmsTime(String.valueOf(map.get("start_time")),
								String.valueOf(map.get("end_time")));
						time = time + minutes;
					}
					time = StringUtilsLocal.keepTwoDecimals(time / buildList.size());
					para.setValue(time);
				} else {
					para.setValue(0);
				}
				inserts.add(para);
			}
		}
		if(inserts.size()>0) {
			parameterValueDao.insertParams(inserts);
		}
	}
	/**
	 * 项目构建时长
	 * @param projectInfos
	 */
	private void VersionStrucInfo(List<ProjectInfo> projectInfos) {
		//版本级编译构建时间 10
		int parameterId = 10;
		
		Date todat = new Date();
		String year = YEAR.format(todat);
		String todatMonth = YEARMOUTHDAY.format(todat);
		todatMonth = todatMonth.substring(5, 7);
		List<ParameterValueNew> inserts=new ArrayList<>();
		for (ProjectInfo projectInfo : projectInfos) {
			String no = projectInfo.getNo();
			for (String month : MONTHS) {
				if(Integer.parseInt(todatMonth)<Integer.parseInt(month)) {
					continue;
				}
				double value = 0;
				//计算截止当前日期的构建时间
				Double buildTime = versionStrucDao.queryBuildTimes(year+month,no);
				Double buildCounts = versionStrucDao.queryBuildCounts(year+month, no);
				if(buildTime*buildCounts > 0) {
					value = buildTime/buildCounts;
				}
				
				ParameterValueNew para=new ParameterValueNew();
				para.setNo(no);
				try {
					para.setMonth(YEARMOUTHDAY.parse(year +"-"+ month+"-01"));
				} catch (ParseException e) {
					logger.error("时间序列化异常：",e);
				}
				para.setParameterId(parameterId);
				
				para.setValue(value);
				inserts.add(para);
			}
		}
		if(inserts.size()>0) {
			parameterValueDao.insertParams(inserts);
		}
	}
	
	/**
	 * 全量自动化回归验证周期
	 * @param projectInfos
	 */
	private void returnValidateCycle(List<ProjectInfo> projectInfos) {

		//全量自动化回归验证周期 11
		Date todat = new Date();
		String year = YEAR.format(todat);
		String todatMonth = YEARMOUTHDAY.format(todat);
		todatMonth = todatMonth.substring(5, 7);
		List<ParameterValueNew> inserts=new ArrayList<>();
		int parameterId = 11;
		for (ProjectInfo projectInfo : projectInfos) {
			String no = projectInfo.getNo();
	
			List<Map<String, Object>> tmss = verificationCycleDao.getTmssInfo(no,year);
			if(tmss==null || tmss.size()<=0) {
				continue;
			}
			String cVersionNames = StringUtilsLocal.valueOf(tmss.get(0).get("c_version_name"));
			for (String month : MONTHS) {
				if(Integer.parseInt(todatMonth)<Integer.parseInt(month)) {
					continue;
				}
				Double minute = 0.0;
				List<Map<String, Object>> tmssSums = verificationCycleDao.getAllBVversion(no,month,year);
				if(tmssSums==null || tmssSums.size()<=0) {
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
					Map<String, Object> minutesNum = verificationCycleDao.getMinutesGroupMouth(no,bVersionName,month,year);
					Double minutes = Double.parseDouble(StringUtilsLocal.valueOf(minutesNum.get("minutes")));
					Long num = Long.parseLong(StringUtilsLocal.valueOf(minutesNum.get("num")));
					if(num==0) {
						continue;
					}
					minute += minutes/num;
				}
				minute = minute/bVersionNames.size();
			
				
				ParameterValueNew para=new ParameterValueNew();
				para.setNo(no);
				try {
					para.setMonth(YEARMOUTHDAY.parse(year +"-"+ month+"-01"));
				} catch (ParseException e) {
					logger.error("时间序列化异常：",e);
				}
				para.setParameterId(parameterId);
				
				para.setValue(StringUtilsLocal.keepTwoDecimals(minute));
				inserts.add(para);
			}
		}
		if(inserts.size()>0) {
			parameterValueDao.insertParams(inserts);
		}
	}

	
	/**
	 * 全量功能验证周期 &&解决方案验证周期
	 * @param projectInfos
	 * @param parameterId
	 */
	private void totalValidateCycle(List<ProjectInfo> projectInfos,int parameterId) {
		List<String> projNo = new ArrayList<>();
		for (ProjectInfo projectInfo : projectInfos) {
			projNo.add(projectInfo.getNo());
		}
		Date todat = new Date();
		String year = YEAR.format(todat);
		String todatMonth = YEARMOUTHDAY.format(todat);
		todatMonth = todatMonth.substring(5, 7);
		List<ParameterValueNew> inserts=new ArrayList<>();
		for (String no : projNo) {
			List<Map<String, Object>> tmss = verificationCycleDao.getTmssInfo(no,year);
			if(tmss==null || tmss.size()<=0) {
				continue;
			}
			String cVersionNames = StringUtilsLocal.valueOf(tmss.get(0).get("c_version_name"));
			
			for (String month : MONTHS) {
				Double minute = 0.0;
				List<Map<String, Object>> tmssVersions = verificationCycleDao.getTotalAllBVversion(no,month,year);
				if(tmssVersions==null || tmssVersions.size()<=0) {
					continue;
				}
				for (Map<String, Object> tmssVersion : tmssVersions) {
					String bVersionName = StringUtilsLocal.valueOf(tmssVersion.get("b_version_name"));
					Map<String, Object> minutesNum = verificationCycleDao.getMinutesGroupMouth(no,bVersionName,month,year);
					Double minutes = Double.parseDouble(StringUtilsLocal.valueOf(minutesNum.get("minutes")));
					Long num = Long.parseLong(StringUtilsLocal.valueOf(minutesNum.get("num")));
					if(num==0) {
						continue;
					}
					minute += minutes/num;
				}
				minute = minute/tmssVersions.size();
			
				
				ParameterValueNew para=new ParameterValueNew();
				para.setNo(no);
				try {
					para.setMonth(YEARMOUTHDAY.parse(year +"-"+ month+"-01"));
				} catch (ParseException e) {
					logger.error("时间序列化异常：",e);
				}
				para.setParameterId(parameterId);
				
				para.setValue(StringUtilsLocal.keepTwoDecimals(minute));
				inserts.add(para);
			}
		}
		if(inserts.size()>0) {
			parameterValueDao.insertParams(inserts);
		}
	}

	/**
	 * 研发效率指标入库
	 * 
	 * @param projectInfos
	 */
	private void insertDevelopTarget(List<ProjectInfo> projectInfos) {
		// JAVA端到端生产率
		int parameterId1 = 15;
		// C++端到端生产率
		int parameterId2 = 16;
		Date todat = new Date();
		String year = YEAR.format(todat);
		String todatMonth = YEARMOUTHDAY.format(todat);
		todatMonth = todatMonth.substring(5, 7);
		for (ProjectInfo projectInfo : projectInfos) {
			
			List<String> kfList = new ArrayList<String>();
			List<String> listAuthor = new ArrayList<String>();
			List<String> sgList = new ArrayList<String>();
			List<Map<String, Object>> maps = managersDao.queryOMPUserSelected(projectInfo.getNo());
			// 获取当前项目人员配置
			for (Map<String, Object> map : maps) {
				String account = StringUtilsLocal.valueOf(map.get("ZR_ACCOUNT"));
				if (StringUtilsLocal.isBlank(account)) {
					account = StringUtilsLocal.valueOf(map.get("account"));
				}
				if (StringUtilsLocal.isBlank(account)) {
					continue;
				}
				listAuthor.add(account);
				if ("开发工程师".equals(map.get("ROLE").toString())) {
					String svnGitNo = StringUtilsLocal.valueOf(map.get("svn_git_no"));
					String author = StringUtilsLocal.valueOf(map.get("HW_ACCOUNT"));

					if (StringUtilsLocal.isBlank(author)) {
						continue;
					}
					if (StringUtilsLocal.isBlank(svnGitNo)) {
						kfList.add(author);// 华为工号
					} else {
						sgList.add(svnGitNo);// svn/git账号
					}

				}
			}
			for (String month : MONTHS) {
				String date = year + "-" + month;
				if (Integer.parseInt(todatMonth) < Integer.parseInt(month)) {
					continue;
				}
				
				// 获取当前项目投入总工时
				String hs = measureDao.getHoursByYearMonth(date, "(" + StringUtilsLocal.listToSqlIn(listAuthor) + ")");
				
				double hours = StringUtils.isEmpty(hs) ? 0 : Double.valueOf(hs);// 该月份总工时和
				if (hours == 0) {
					hours = 21.5 * 8 * listAuthor.size();
				}
				
				// JAVA代码量
				String javaCodeType = FileType.getFileTypes("JAVA");
				
				
				
				
				ParameterValueNew parameterJava = new ParameterValueNew();
				/*Double javaNum1 = 0.0;
				Double javaNum2 = 0.0;
				if(kfList.size()>0) {
					javaNum1 = jobPcbDao.queryCodeDTD("(" + StringUtilsLocal.listToSqlIn(kfList) + ")",date, javaCodeType,projectInfoVo.getNo().toString());
				}
				if(sgList.size()>0) {
					javaNum2 = jobPcbDao.queryCodeDTD("(" + StringUtilsLocal.listToSqlIn(sgList) + ")",date, javaCodeType,projectInfoVo.getNo().toString());
				}*/
				Double javaNum = jobPcbDao.queryCodeDTD(date,javaCodeType,projectInfo.getNo().toString());
				
				
				parameterJava.setNo(projectInfo.getNo().toString());
				parameterJava.setParameterId(parameterId1);
				parameterJava.setValue(StringUtilsLocal.keepTwoDecimals(javaNum / (hours / 8)));
				//parameterJava.setValue(StringUtilsLocal.keepTwoDecimals(javaNum1+javaNum2/(hours/8)));
				try {
					parameterJava.setMonth(YEARMOUTHDAY.parse(year +"-"+ month+"-01"));
				} catch (ParseException e) {
					logger.error("YEARMOUTHDAY.parse exception, error: "+e.getMessage());
				}
				parameterValueDao.insertParameterValueNew(parameterJava);
				// C++代码量
				ParameterValueNew parameterC = new ParameterValueNew();
				String cCodeType = FileType.getFileTypes("C");
				/*Double cNum1 = 0.0;
				Double cNum2 = 0.0;
				if(kfList.size()>0) {
					cNum1 = jobPcbDao.queryCodeDTD("(" + StringUtilsLocal.listToSqlIn(kfList) + ")",date,cCodeType,projectInfoVo.getNo().toString());
				}
				if(sgList.size()>0) {
					cNum2 = jobPcbDao.queryCodeDTD("(" + StringUtilsLocal.listToSqlIn(sgList) + ")",date,cCodeType,projectInfoVo.getNo().toString());
				}*/
				Double cNum = jobPcbDao.queryCodeDTD(date,cCodeType,projectInfo.getNo().toString());
				parameterC.setNo(projectInfo.getNo().toString());
				parameterC.setParameterId(parameterId2);
				//parameterC.setValue(StringUtilsLocal.keepTwoDecimals(cNum1+cNum2/(hours/8)));
				parameterC.setValue(StringUtilsLocal.keepTwoDecimals(cNum / (hours / 8)));
				try {
					parameterC.setMonth(YEARMOUTHDAY.parse(year +"-"+ month+"-01"));
				} catch (ParseException e) {
					logger.error("YEARMOUTHDAY.parse exception, error: "+e.getMessage());
				}
				parameterValueDao.insertParameterValueNew(parameterC);
			}
		}
	}
	
}
