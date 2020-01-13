package com.icss.mvp.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.IProjectManagersDao;
import com.icss.mvp.dao.LabelMeasureConfigDao;
import com.icss.mvp.dao.MonthMeasureDao;
import com.icss.mvp.entity.MonthMeasure;
import com.icss.mvp.constant.FileTypeEnum.FileType;

@Service
@SuppressWarnings("all")
public class MonthMeasureService {
	private static Logger logger = Logger.getLogger(MonthMeasureService.class);

	@Autowired
	private ProjectLableService projectLableService;

	@Autowired
	private MonthMeasureDao measureDao;

	@Resource
	private IProjectManagersDao managersDao;

	@Autowired
	private BuOverviewService buOverviewService;


	@Resource
	private CodeGainTypeService codeGainTypeService;

	@Autowired
	private LabelMeasureConfigDao labelMeasureConfigDao;

	/**
	 * @Description: 获取所有月份指标实际值
	 * @author Administrator
	 * @date 2018年5月25日
	 */
	public List<MonthMeasure> geMonthtMeasureValue(String proNo) {
		List<MonthMeasure> measures = null;
		try {
			List<Map<String, Object>> list = measureDao.getMeasuresByProject(proNo);
			List<String> idList = new ArrayList<String>();
			if (list.size() > 0) {
				for (Map<String, Object> map : list) {
					idList.add(map.get("ID").toString());
				}
				measures = measureDao.geMonthtMeasureValue("(" + StringUtilsLocal.listToSqlIn(idList) + ")");
			}
		} catch (Exception e) {
			logger.info("measureDao.geMonthtMeasureValue exception, error: "+e.getMessage());
		}
		return measures;
	}

	/**
	 * @Description: 计算每个月指标实际值 ()
	 * @author Administrator
	 * @date 2018年5月25日
	 */
	public List<MonthMeasure> calculateMeasure(String proNo) {
		String startDate = "";// year+month
		String endDate = "";// year+month

		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM");
		// 获取项目配置的指标
		List<Map<String, Object>> list = measureDao.getMeasuresByProject(proNo);
		// 获取项目启动时间
		Map<String, Object> map = buOverviewService.getProjOverveiwData(proNo);
		if (map != null) {
			startDate = map.get("startDate") == null ? "" : sFormat.format(map.get("startDate"));
			endDate = map.get("endDate") == null ? "" : sFormat.format(map.get("endDate"));
		}
		// 从项目启动时间计算项目指标实际值
		if (list.size() > 0) {
			// 获取项目启动至今的所有日期
			List<String> ym = getYearMonthFromStart(startDate, endDate);
			System.out.println(ym);
			// 开始计算指标值
			try {
				startMeasureCal(list, ym, proNo);
			} catch (Exception e) {
				logger.info("startMeasureCal exception, error: "+e.getMessage());
			}
		}
		return null;
	}

	/**
	 * @Description: 开始计算指标实际值 (统计所有项目包括在行、已上线)
	 * @author Administrator
	 * @throws ParseException
	 * @date 2018年5月25日
	 */
	private void startMeasureCal(List<Map<String, Object>> list, List<String> ym, String proNo) throws Exception {
		double result = 0;
		if (list.size() == 0) {
			return;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> maps = managersDao.queryOMPUserSelected(proNo);
		List<String> kfList = new ArrayList<String>();
		List<String> listAuthor = new ArrayList<String>();
		List<String> sgList = new ArrayList<String>();
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

		for (Map<String, Object> mm : list) {
			String lmcId = mm.get("ID").toString();
			String name = mm.get("name").toString();
			for (String yearMonth : ym) {
				String yl = measureDao.getylNumByYearMonth(yearMonth, proNo);
				double ylNum = StringUtils.isEmpty(yl) ? 0 : Double.valueOf(yl);// 该月测试用例总数
				
				Double num = measureDao.getCodeNumByMonthWX(yearMonth,proNo);
				Double num1 = measureDao.getCodeNumByMonth(yearMonth,proNo);
				
				Double count = num + num1;// 该月份开发人员代码量总和

				String hs = measureDao.getHoursByYearMonth(yearMonth,
						"(" + StringUtilsLocal.listToSqlIn(listAuthor) + ")");
				double hours = StringUtils.isEmpty(hs) ? 0 : Double.valueOf(hs);// 该月份总工时和
				if(hours==0) {
					hours = 21.5*8*listAuthor.size();
				}
				

				if ("测试用例设计效率".equals(name.trim())) {
					String ylPercent = measureDao.getPercentByYearMonth(yearMonth, proNo, "测试用例设计投入");
					double ylp = StringUtils.isEmpty(ylPercent) ? 0 : Double.valueOf(ylPercent);
					result = getResult(ylNum, ylp, hours);

				} else if ("代码开发效率".equals(name.trim())) {
					String kfPercent = measureDao.getPercentByYearMonth(yearMonth, proNo, "开发投入");
					double kfp = StringUtils.isEmpty(kfPercent) ? 0 : Double.valueOf(kfPercent);
					result = getResult(count, kfp, hours);

				} else if ("人均月代码量".equals(name.trim())) {
					String kfPercent = measureDao.getPercentByYearMonth(yearMonth, proNo, "开发投入");
					double kfp = StringUtils.isEmpty(kfPercent) ? 0 : Double.valueOf(kfPercent);
					double kfxl = getResult(count, kfp, hours);
					result = kfxl * 22.5;

				} else if ("E2E代码生产率".equals(name.trim())) {
					result = hours > 0 ? (count / (hours / 8)) : 0;

				} else if ("用例执行率".equals(name.trim())) {
					// 测试用例执行数
					Integer zxyls = measureDao.startTestCaseNum(proNo, yearMonth);
					if (zxyls == null) {
						zxyls = 0;
					}
					// 测试用例总数
					Integer zyls = measureDao.testCaseCount(proNo, yearMonth);
					if (zyls == null) {
						zyls = 0;
					}
					Double testCaseStartRate = Double.valueOf(zxyls) / Double.valueOf(zyls);
					if (zyls == 0) {
						testCaseStartRate = 0.0;
					}
					result = testCaseStartRate;

				} else if ("测试缺陷密度".equals(name.trim())) {
					Integer wtdzs = measureDao.dtsCount(proNo, yearMonth);
					if (wtdzs == null) {
						wtdzs = 0;
					}
					// 新增修改代码量
					Integer xzxgdml = measureDao.newLoc(proNo, yearMonth);
					if (xzxgdml == null) {
						xzxgdml = 0;
					}
					Integer xzxgdmlWx = measureDao.newLocWx(proNo, yearMonth);
					if (xzxgdmlWx != null) {
						xzxgdml += xzxgdmlWx;
					}
					Double defectDensity = Double.valueOf(wtdzs) / (Double.valueOf(xzxgdml) / 1000);
					if (xzxgdml == 0) {
						defectDensity = 0.0;
					}
					result = defectDensity;
				} else if ("测试用例密度".equals(name.trim())) {
					// 新增修改代码量
					Integer xzxgdml = measureDao.newLoc(proNo, yearMonth);
					if (xzxgdml == null) {
						xzxgdml = 0;
					}
					// 新增测试用例数
					Integer xzcsyls = measureDao.newTestCaseNum(proNo, yearMonth);
					if (xzcsyls == null) {
						xzcsyls = 0;
					}
					Double testCaseDensity = Double.valueOf(xzcsyls) / (Double.valueOf(xzxgdml) / 1000);
					if (xzxgdml == 0) {
						testCaseDensity = 0.0;
					}
					result = testCaseDensity;
				} else if ("问题解决效率".equals(name.trim())) {
					// 已解决问题总数
					Integer yjjwtzs = measureDao.solvedDtsCount(proNo, yearMonth);
					if (yjjwtzs == null) {
						yjjwtzs = 0;
					}
					String wt = measureDao.solveHours("问题单修改投入", proNo, yearMonth);
					Double wtjjbfb = 0.0;
					if (wt == null || "".equals(wt.trim())) {
						wtjjbfb = 0.0;
					} else {
						wtjjbfb = Double.valueOf(wt);
					}
					wtjjbfb = wtjjbfb / 100;
					Double wtjjgzl = hours / 8 * wtjjbfb;
					Double solvedRate = Double.valueOf(yjjwtzs) / Double.valueOf(wtjjgzl);
					if (wtjjgzl == 0) {
						solvedRate = 0.0;
					}
					result = solvedRate;
				} else if ("手工测试执行效率".equals(name.trim())) {
					// 手工用例执行次数
					Integer sgzxylzs = measureDao.manualStartTestCaseCount(proNo, yearMonth);
					if (sgzxylzs == null) {
						sgzxylzs = 0;
					}
					// 测试执行工作量
					String cs = measureDao.solveHours("测试执行投入", proNo, yearMonth);
					Double cszxbfb = 0.0;
					if (cs == null || "".equals(cs.trim())) {
						cszxbfb = 0.0;
					} else {
						cszxbfb = Double.valueOf(cs);
					}
					cszxbfb = cszxbfb / 100;
					Double cszxgzl = hours / 8 * cszxbfb;
					// 手工测试执行效率=手工用例执行次数/测试执行工作量
					Double manualTestStartRate = Double.valueOf(sgzxylzs) / Double.valueOf(cszxgzl);
					if (cszxgzl == 0) {
						manualTestStartRate = 0.0;
					}
					result = manualTestStartRate;
				} else if ("自动化用例写作效率".equals(name.trim())) {
					// 测试自动化代码量
					Integer cszdhdml = measureDao.testCaseAutoLoc(proNo, yearMonth);
					if (cszdhdml == null) {
						cszdhdml = 0;
					}
					Integer cszdhdmlWx = measureDao.testCaseAutoLocWx(proNo, yearMonth);
					if (cszdhdmlWx != null) {
						cszdhdml += cszdhdmlWx;
					}
					// 自动化用例写作工作量
					String zdh = measureDao.solveHours("自动化用例写作", proNo, yearMonth);
					Double zdhylxzbfb = 0.0;
					if (zdh == null || "".equals(zdh.trim())) {
						zdhylxzbfb = 0.0;
					} else {
						zdhylxzbfb = Double.valueOf(zdh);
					}
					zdhylxzbfb = zdhylxzbfb / 100;
					Double zdhylxzgzl = hours / 8 * zdhylxzbfb;
					Double autoTestCaseWriteRate = Double.valueOf(cszdhdml) / Double.valueOf(zdhylxzgzl);
					if (zdhylxzgzl == 0) {
						autoTestCaseWriteRate = 0.0;
					}
					result = autoTestCaseWriteRate;
				} else if ("代码行数".equals(name.trim())) {
					result = count;
				} else if ("合作方投入工作量（人天）".equals(name.trim())) {
					result = hours / (8 * 22.5);
				} else if ("执行测试用例数".equals(name.trim())) {
					// 测试用例执行数
					Integer zxyls = measureDao.startTestCaseNum(proNo, yearMonth);
					if (zxyls == null) {
						zxyls = 0;
					}
					result = zxyls;
				} else if ("设计测试用例数".equals(name.trim())) {
					result = ylNum;
				} else if ("自动化脚本数".equals(name.trim())) {
					// 自动化脚本数=自动化用例数？
					Integer zdhyls = measureDao.autoTestCaseNum(proNo, yearMonth);
					if (zdhyls == null) {
						zdhyls = 0;
					}
					result = zdhyls;
				} else if ("问题数".equals(name.trim())) {
					Integer wtdzs = measureDao.dtsCount(proNo, yearMonth);
					if (wtdzs == null) {
						wtdzs = 0;
					}
					result = wtdzs;
				} else if ("版本数".equals(name.trim())) {
					Integer bbs = measureDao.getVersionNum(proNo, yearMonth);
					if (bbs == null) {
						bbs = 0;
					}
					result = bbs;
				}else if("C++语言代码量".equals(name.trim())) {
					List<Map<String, Object>> listMap = managersDao.queryOMPUserSelected(proNo);
					List<String> kfList1 = new ArrayList<String>();
					List<String> sgList1 = new ArrayList<String>();
					for (Map<String, Object> lmap : listMap) {
						if("开发工程师".equals(lmap.get("ROLE").toString())) {
							String svnGitNo = StringUtilsLocal.valueOf(lmap.get("svn_git_no"));
				    		String author = StringUtilsLocal.valueOf(lmap.get("HW_ACCOUNT"));
				    		
				    		if(StringUtilsLocal.isBlank(author)) {
				    			continue;
				    		}
				    		if(StringUtilsLocal.isBlank(svnGitNo)) {
				    			kfList1.add(author);//华为工号
							}else {
								sgList1.add(svnGitNo);//华为工号
							}
							
						}
					}
					String codeType = FileType.getFileTypes("C");
					Double numC =  measureDao.queryCodeNumByc(codeType,"("+ StringUtilsLocal.listToSqlIn(kfList1)+")",yearMonth);
					Double numWX =  measureDao.queryCodeNumBycWX(codeType,"("+ StringUtilsLocal.listToSqlIn(sgList1)+")",yearMonth);
					result = numC+numWX;
				} 
				else {
					break;
				}
				// 判断是否为第一次计算指标值
				MonthMeasure m = new MonthMeasure();
				List<MonthMeasure> isFirst = measureDao.isFirstCalculate(lmcId, yearMonth);
				if (isFirst.size() > 0) {// 更新
					m.setLabMeasureConfigId(lmcId);
					m.setCopyDate(yearMonth + "-01");
					m.setValue(result > 0 ? String.format("%.2f",result) : "");
					measureDao.updateMonthMeasures(m);
				} else {// 新增
					m.setLabMeasureConfigId(lmcId);
					m.setCopyDate(yearMonth + "-01");
					m.setValue(result > 0 ? String.format("%.2f",result) : "");
					measureDao.saveMonthMeasure(m);
				}
			}
		}

	}

	/**
	 * @Description: 获取从项目开始日期到现在的所有年月
	 * @author Administrator
	 * @date 2018年5月25日
	 */
	List<String> getYearMonthFromStart(String startDate, String endDate) {
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM");
		List<String> ls = new ArrayList<String>();
		Calendar dd = null;
		Date now = new Date();
		if ("".equals(startDate)) {// 如果项目开始时间为空，则默认为当前时间
			startDate = sFormat.format(now);
		}
		if ("".equals(endDate)) {// 项目结束时间为空，则默认为当前时间
			endDate = sFormat.format(now);
		} else {
			int a = compare_date(endDate, sFormat.format(now));
			if (a == 1 || a == 0) {
				endDate = sFormat.format(now);
			}
		}
		try {
			dd = Calendar.getInstance();// 定义日期实例
			Date d1 = (Date) sFormat.parse(startDate);
			Date d2 = (Date) sFormat.parse(endDate);

			dd.setTime(d1);// 设置日期起始时间

			while (dd.getTime().before(d2)) {// 判断是否到结束日期

				String str = sFormat.format(dd.getTime());
				ls.add(str);

				dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1
			}
			ls.add(endDate);
		} catch (ParseException e) {
			logger.error("dd.setTime exception, error: "+e.getMessage());
		}
		return ls;
	}

	/**
	 * @Description: 日期大小比较
	 * @author Administrator
	 */
	public static int compare_date(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * @Description: 计算公式
	 * @author Administrator
	 * @date 2018年5月28日
	 * @version V1.0
	 */
	public double getResult(double a, double b, double c) {
		double result = 0;
		if (a * b * c > 0) {
			result = a / ((b * 0.01) * (c / 8));
		}
		return result;
	}
}
