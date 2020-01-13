package com.icss.mvp.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.IProjectManagersDao;
import com.icss.mvp.dao.ITestMeasuresDao;
import com.icss.mvp.entity.GerenCode;
import com.icss.mvp.entity.GerenCodeList;
import com.icss.mvp.entity.MonthlyManpower;
import com.icss.mvp.entity.TestCaseInput;
import com.icss.mvp.util.DateUtils;

@Service("testMeasuresService")
@Transactional
public class TestMeasuresService {
	private static Logger logger = Logger.getLogger(TestMeasuresService.class);

	public static final SimpleDateFormat format = new SimpleDateFormat("MMdd");
	@Resource
	private ITestMeasuresDao testMeasuresDao;
	@Resource
	IProjectManagersDao projectManagersDao;
	@Resource
	IProjectListDao iProjectListDao;

	public static String[] getLast12Months(){
		//Java获取最近12个月的月份
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String dateString = sdf.format(cal.getTime());
		List<String> rqList = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			dateString = sdf.format(cal.getTime());
			rqList.add(dateString.substring(0, 7));
			cal.add(Calendar.MONTH, -1);
		}
		//存入string数组
		String[] months = {(String)rqList.get(11),(String)rqList.get(10),(String)rqList.get(9),(String)rqList.get(8),(String)rqList.get(7),(String)rqList.get(6),
				(String)rqList.get(5),(String)rqList.get(4),(String)rqList.get(3),(String)rqList.get(2),(String)rqList.get(1),(String)rqList.get(0)};
		
		return months;
	}
	
	/**
	 * 获取人力投入统计 actualFlag=0 实际工时 1 标准工时
	 * 
	 * @param projNo
	 *            项目编号
	 * @param actualFlag
	 *            标准/实际工时
	 * @param months
	 *            月份数组，如果使用一个月的，传入数组为{"01"}，全年为{"01","02","03","04","05","06","07","08","09","10","11","12"}
	 * @param oneOrAllMonth
	 *            true-全年 false-单月
	 * @return
	 */
	public List<MonthlyManpower> getMonthlyManpower(String projNo, String actualFlag, String[] months,
			Boolean oneOrAllMonth) {
		List<MonthlyManpower> ret = new ArrayList<>();
		String todayMonth = DateUtils.getMonth().split("-")[1];
		List<Map<String, Object>> selectedMaps = projectManagersDao.queryOMPUserSelected(projNo);
		for (Map<String, Object> user : selectedMaps) {
			MonthlyManpower manpower = new MonthlyManpower();
			manpower.setName(StringUtilsLocal.valueOf(user.get("NAME")));
			manpower.setRole(StringUtilsLocal.valueOf(user.get("ROLE")));
			String zrAcc = StringUtilsLocal.valueOf(user.get("ZR_ACCOUNT"));
			if ("".equals(zrAcc)) {
				zrAcc = StringUtilsLocal.valueOf(user.get("account"));
			}
			manpower.setNumber(zrAcc);
			if ("".equals(manpower.getNumber())) {
				ret.add(manpower);
				continue;
			}
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = DateUtils.SHORT_FORMAT_GENERAL.parse(StringUtilsLocal.valueOf(user.get("START_DATE")));
				endDate = DateUtils.SHORT_FORMAT_GENERAL.parse(StringUtilsLocal.valueOf(user.get("END_DATE")));
			} catch (Exception e) {
				logger.error("开始结束时间为空，会导致统计值缺少计算");
			}
			double all = 0.0;
			List<Map<String, Object>> WorkHoursdays = null;
			if (oneOrAllMonth) {
				WorkHoursdays = testMeasuresDao.queryWorkHoursDayByAuthorCurdate(manpower.getNumber(), null);
			} else {
				WorkHoursdays = testMeasuresDao.queryWorkHoursDayByAuthorCurdate(manpower.getNumber(), months[0]);
			}
			Map<String, Map<String, Object>> WorkHoursdaysList = classifiedByMonth(WorkHoursdays);
			for (String month : months) {
				double timeDou = 0.0;
				if (WorkHoursdaysList.get(month) != null) {
					if ("0".equals(actualFlag)) {
						String timeStr = StringUtilsLocal.valueOf(WorkHoursdaysList.get(month).get("actual_labor_hour"));
						timeDou = Double.parseDouble(timeStr == "" ? "0.0" : timeStr);
					} else if ("1".equals(actualFlag)) {
						String timeStr = StringUtilsLocal
								.valueOf(WorkHoursdaysList.get(month).get("standard_participation"));
						timeDou = Double.parseDouble(timeStr == "" ? "0.0" : timeStr);
					} else {
						String timeStr = StringUtilsLocal.valueOf(WorkHoursdaysList.get(month).get("actual_labor_hour"));
						timeDou = Double.parseDouble(timeStr == "" ? "0.0" : timeStr);
					}
					if (!"1".equals(actualFlag)) {
						timeDou = timeDou / 8;
					}
					// 数据库没有值的，使用默认值
					if (timeDou == 0.0) {
						timeDou = 21.5;
					}
					Date statisticalDate = (Date) WorkHoursdaysList.get(month).get("statistical_time");
					if (startDate != null && endDate != null) {
						timeDou = getTimeDou(statisticalDate, startDate, endDate, timeDou);
					}
				} else {
					if (Integer.parseInt(todayMonth) >= Integer.parseInt(month)) {
						timeDou = 21.5;
					}
					if (startDate != null && endDate != null) {
						Date monthDate = DateUtils.getMonthLastDay(Integer.parseInt(month));
						timeDou = getTimeDou(monthDate, startDate, endDate, timeDou);
					}
				}
				timeDou = keepTwoDecimals(timeDou);
				all += timeDou;
				switch (month) {
				case "01":
					manpower.setJanuary(timeDou);
					break;
				case "02":
					manpower.setFebruary(timeDou);
					break;
				case "03":
					manpower.setMarch(timeDou);
					break;
				case "04":
					manpower.setApril(timeDou);
					break;
				case "05":
					manpower.setMay(timeDou);
					break;
				case "06":
					manpower.setJune(timeDou);
					break;
				case "07":
					manpower.setJuly(timeDou);
					break;
				case "08":
					manpower.setAugust(timeDou);
					break;
				case "09":
					manpower.setSeptember(timeDou);
					break;
				case "10":
					manpower.setOctober(timeDou);
					break;
				case "11":
					manpower.setNovember(timeDou);
					break;
				case "12":
					manpower.setDecember(timeDou);
					break;
				default:
					break;
				}
			}
			manpower.setSum(keepTwoDecimals(all));
			ret.add(manpower);
		}
		return ret;
	}

	public List<MonthlyManpower> getManpowerList(String projNo, String actualFlag, String[] months,
			Boolean oneOrAllMonth) {
		List<MonthlyManpower> result = new ArrayList<MonthlyManpower>();
		List<Map<String, Object>> selectedMaps = projectManagersDao.queryOMPUserSelected(projNo);
		for (Map<String, Object> user : selectedMaps) {
			MonthlyManpower manpower = new MonthlyManpower();
			manpower.setName(StringUtilsLocal.valueOf(user.get("NAME")));
			manpower.setRole(StringUtilsLocal.valueOf(user.get("ROLE")));
			String zrAcc = StringUtilsLocal.valueOf(user.get("ZR_ACCOUNT"));
			if ("".equals(zrAcc)) {
				zrAcc = StringUtilsLocal.valueOf(user.get("account"));
			}
			manpower.setNumber(zrAcc);
			if ("".equals(manpower.getNumber())) {
				result.add(manpower);
				continue;
			}
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = DateUtils.SHORT_FORMAT_GENERAL.parse(StringUtilsLocal.valueOf(user.get("START_DATE")));
				endDate = DateUtils.SHORT_FORMAT_GENERAL.parse(StringUtilsLocal.valueOf(user.get("END_DATE")));
			} catch (Exception e) {
				logger.error("开始结束时间为空，会导致统计值缺少计算");
			}
			Map<String, Object> manpowers = new HashMap<String, Object>();
			double all = 0.0;
			List<Map<String, Object>> WorkHoursdays = null;
			if (oneOrAllMonth) {
				WorkHoursdays = testMeasuresDao.queryWorkHoursDayByAuthorLatest(manpower.getNumber(), null);
			} else {
				WorkHoursdays = testMeasuresDao.queryWorkHoursDayByAuthorLatest(manpower.getNumber(), months[0]);
			}
			Map<String, Map<String, Object>> WorkHoursdaysList = classifiedByMonth(WorkHoursdays);
			for (String  month: months) {
				double timeDou = 0.0;
				if (WorkHoursdaysList.get(month) != null) {
					if ("0".equals(actualFlag)) {
						String timeStr = StringUtilsLocal.valueOf(WorkHoursdaysList.get(month).get("actual_labor_hour"));
						timeDou = Double.parseDouble(timeStr == "" ? "0.0" : timeStr);
					} else if ("1".equals(actualFlag)) {
						String timeStr = StringUtilsLocal
								.valueOf(WorkHoursdaysList.get(month).get("standard_participation"));
						timeDou = Double.parseDouble(timeStr == "" ? "0.0" : timeStr);
					} else {
						String timeStr = StringUtilsLocal.valueOf(WorkHoursdaysList.get(month).get("actual_labor_hour"));
						timeDou = Double.parseDouble(timeStr == "" ? "0.0" : timeStr);
					}
					if (!"1".equals(actualFlag)) {
						timeDou = timeDou / 8;
					}
					// 数据库没有值的，使用默认值
					if (timeDou == 0.0) {
						timeDou = 22.5;
					}
					Date statisticalDate = (Date) WorkHoursdaysList.get(month).get("statistical_time");
					if (startDate != null && endDate != null) {
						timeDou = getNewTimeDou(statisticalDate, startDate, endDate, timeDou);
					}
				} else {
					timeDou = 22.5;
					if (startDate != null && endDate != null) {
						Date monthDate = DateUtils.getMonthLastDay(month);
						timeDou = getNewTimeDou(monthDate, startDate, endDate, timeDou);
					}
				}

				timeDou = keepTwoDecimals(timeDou);
				all += timeDou;
				manpowers.put(month, timeDou);
			}
			manpower.setManpowers(manpowers);
			manpower.setSum(keepTwoDecimals(all));
			result.add(manpower);
		}	
		return result;
	}	
	
	public Map<String, Object> getMonthlyManpowerList(String projNo, String actualFlag, String[] months,
			Boolean oneOrAllMonth) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<MonthlyManpower> list =getManpowerList(projNo, actualFlag, months, oneOrAllMonth);
		
		for (MonthlyManpower manpower : list) {
			Map<String, Object> map = manpower.getManpowers();
			if(null != map) {
				for (int i = 0; i < months.length; i++) {
					double timeDou = (double) map.get(months[i]);
					switch (i + 1) {
					case 1:
						manpower.setFirst(timeDou);
						break;
					case 2:
						manpower.setSecond(timeDou);
						break;
					case 3:
						manpower.setThird(timeDou);
						break;
					case 4:
						manpower.setFourth(timeDou);
						break;
					case 5:
						manpower.setFifth(timeDou);
						break;
					case 6:
						manpower.setSixth(timeDou);
						break;
					case 7:
						manpower.setSeventh(timeDou);
						break;
					case 8:
						manpower.setEighth(timeDou);
						break;
					case 9:
						manpower.setNinth(timeDou);
						break;
					case 10:
						manpower.setTenth(timeDou);
						break;
					case 11:
						manpower.setEleventh(timeDou);
						break;
					case 12:
						manpower.setTwelfth(timeDou);
						break;
					default:
						break;
					}
				}
			}
		}
		
		result.put("head", months);
		result.put("body", list);
		return result;
	}

	private double getNewTimeDou(Date statisticalDate, Date startDate, Date endDate, double timeDou) {
		if (DateUtils.comparisonDateSizeYM(statisticalDate, startDate)) {
			return 0.0;
		}
		if (DateUtils.comparisonDateSizeYM(endDate, statisticalDate)) {
			return 0.0;
		}
		String sdate = DateUtils.SHORT_FORMAT_GENERAL.format(statisticalDate);
		String startDateStr = DateUtils.SHORT_FORMAT_GENERAL.format(startDate);
		String endDateStr = DateUtils.SHORT_FORMAT_GENERAL.format(endDate);
		Map<String, Double> months = new HashMap<>();
		months.put("01", 31.0);
		months.put("02", 28.0);
		int year = Integer.parseInt(sdate.substring(0,4));
		if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
			months.put("02", 29.0);
		}
		months.put("03", 31.0);
		months.put("04", 30.0);
		months.put("05", 31.0);
		months.put("06", 30.0);
		months.put("07", 31.0);
		months.put("08", 31.0);
		months.put("09", 30.0);
		months.put("10", 31.0);
		months.put("11", 30.0);
		months.put("12", 31.0);
		if (sdate.substring(0, 7).equals(startDateStr.substring(0, 7))) {
			String day = startDateStr.substring(8);
			Double a = Double.parseDouble(day);
			Double b = months.get(startDateStr.substring(5, 7));
			return (b - a + 1.0) / b * timeDou;
		}
		if (sdate.substring(0, 7).equals(endDateStr.substring(0, 7))) {
			String day = endDateStr.substring(8);
			Double a = Double.parseDouble(day);
			Double b = months.get(endDateStr.substring(5, 7));
			return a / b * timeDou;
		}
		return timeDou;
	}
	
	
	private double getTimeDou(Date statisticalDate, Date startDate, Date endDate, double timeDou) {
		if (DateUtils.comparisonDateSizeYM(statisticalDate, startDate)) {
			return 0.0;
		}
		if (DateUtils.comparisonDateSizeYM(endDate, statisticalDate)) {
			return 0.0;
		}
		String sdate = format.format(statisticalDate);
		String startDateStr = format.format(startDate);
		String endDateStr = format.format(endDate);
		Map<String, Double> months = new HashMap<>();
		months.put("01", 31.0);
		months.put("02", 28.0);
		int year = Integer.parseInt(DateUtils.getSystemYear());
		if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
			months.put("02", 29.0);
		}
		months.put("03", 31.0);
		months.put("04", 30.0);
		months.put("05", 31.0);
		months.put("06", 30.0);
		months.put("07", 31.0);
		months.put("08", 31.0);
		months.put("09", 30.0);
		months.put("10", 31.0);
		months.put("11", 30.0);
		months.put("12", 31.0);
		if (sdate.substring(0, 2).equals(startDateStr.substring(0, 2))) {
			String day = startDateStr.substring(2);
			Double a = Double.parseDouble(day);
			Double b = months.get(startDateStr.substring(0, 2));
			return (b - a + 1.0) / b * timeDou;
		}
		if (sdate.substring(0, 2).equals(endDateStr.substring(0, 2))) {
			String day = endDateStr.substring(2);
			Double a = Double.parseDouble(day);
			Double b = months.get(endDateStr.substring(0, 2));
			return a / b * timeDou;
		}
		return timeDou;
	}

	/**
	 * 把获取到的全年的list按月区分
	 * 
	 * @param workHoursdays
	 * @param months
	 * @return
	 */
	private Map<String, Map<String, Object>> classifiedByMonth(List<Map<String, Object>> workHoursdays) {
		Map<String, Map<String, Object>> mapLists = new HashMap<>();
		for (Map<String, Object> map : workHoursdays) {
			mapLists.put(StringUtilsLocal.valueOf(map.get("months")), map);
		}
		return mapLists;
	}

	/**
	 * 按照规则计算每天工时，入参为一月的参数，累计为当月总工时
	 * 
	 * @param workHoursdays
	 * @return
	 */
	public double calculateTime(List<Map<String, Object>> workHoursdays) {
		double ret = 0.0;
		Long time8 = 0L;
		Long time12 = 0L;
		Long time13_5 = 0L;
		Long time17_5 = 0L;
		Long time18 = 0L;
		Long time00 = 0L;
		Long time04 = 0L;
		try {
			time8 = DateUtils.time.parse("8:00:00").getTime();
			time12 = DateUtils.time.parse("12:00:00").getTime();
			time13_5 = DateUtils.time.parse("13:30:00").getTime();
			time17_5 = DateUtils.time.parse("17:30:00").getTime();
			time18 = DateUtils.time.parse("18:00:00").getTime();
			time00 = DateUtils.time.parse("00:00:00").getTime() + 1000 * 24 * 60 * 60;
			time04 = DateUtils.time.parse("04:00:00").getTime() + 1000 * 24 * 60 * 60;
		} catch (Exception e) {
			logger.error("时间转换异常+" + e.getMessage());
		}
		for (Map<String, Object> day : workHoursdays) {
			String time = StringUtilsLocal.valueOf(day.get("times"));
			if ("".equals(time)) {
				continue;
			}
			double timeDou = keepTwoDecimals(Double.parseDouble(time) / 60);
			Date worktimeDate = (Date) day.get("WORKTIME");
			Date offtimeDate = (Date) day.get("OFFTIME");
			try {
				Long worktime = DateUtils.time.parse(DateUtils.time.format(worktimeDate)).getTime();
				Long offtime = DateUtils.time.parse(DateUtils.time.format(offtimeDate)).getTime();
				if (!DateUtils.format.format(worktimeDate).equals(DateUtils.format.format(offtimeDate))) {
					offtime += 1000 * 24 * 60 * 60;
				}
				if (time8 > worktime) {
					timeDou = timeDou - (time8 - worktime) / 1000 / 60 / 60;
					if (offtime > time17_5 && offtime < time18) {
						timeDou = timeDou - (offtime - time17_5) / 1000 / 60 / 60 - 1.5;
					} else if (offtime > time18 && offtime < time00) {
						timeDou = timeDou - 2;
					} else if (offtime < time12) {
					} else if (offtime > time12 && offtime < time13_5) {
						timeDou = timeDou - (offtime - time12) / 1000 / 60 / 60;
					} else if (offtime > time00 && offtime < time04) {
						timeDou = timeDou - 2;
					}
				} else if (worktime > time8 && worktime < time12) {
					if (offtime > time17_5 && offtime < time18) {
						timeDou = timeDou - (offtime - time17_5) / 1000 / 60 / 60 - 1.5;
					} else if (offtime > time18 && offtime < time00) {
						timeDou = timeDou - 2;
					} else if (offtime < time12) {
					} else if (offtime > time12 && offtime < time13_5) {
						timeDou = timeDou - (offtime - time12) / 1000 / 60 / 60;
					} else if (offtime > time00 && offtime < time04) {
						timeDou = timeDou - 2;
					}
				} else if (worktime > time12 && worktime < time13_5) {
					timeDou = timeDou - (time13_5 - worktime) / 1000 / 60 / 60;
					if (offtime > time17_5 && offtime < time18) {
						timeDou = timeDou - (offtime - time17_5) / 1000 / 60 / 60;
					} else if (offtime > time18 && offtime < time00) {
						timeDou = timeDou - 0.5;
					} else if (offtime > time00 && offtime < time04) {
						timeDou = timeDou - 0.5;
					}
				} else if (worktime > time13_5) {
					if (offtime > time17_5 && offtime < time18) {
						timeDou = timeDou - (offtime - time17_5) / 1000 / 60 / 60;
					} else if (offtime > time18 && offtime < time00) {
						timeDou = timeDou - 0.5;
					} else if (offtime > time00 && offtime < time04) {
						timeDou = timeDou - 0.5;
					}
				}

				// if(offtime.getTime() > time17_5.getTime() &&
				// offtime.getTime() < time18.getTime()) {
				// timeDou = timeDou - (offtime.getTime()-time17_5.getTime())/1000/60/60;
				// }else if (offtime.getTime() > time18.getTime()&&
				// offtime.getTime() < time00.getTime()){
				// timeDou = timeDou - 0.5;
				// }else if (offtime.getTime() < time12.getTime()){
				// }else if (offtime.getTime() > time12.getTime()&&
				// offtime.getTime() < time13_5.getTime()){
				// timeDou = timeDou-(offtime.getTime()-time12.getTime())/1000/60/60;
				// }else if (offtime.getTime() > time00.getTime()&&
				// offtime.getTime() < time04.getTime()){
				// timeDou = timeDou - 2;
				// }
				if (timeDou <= 0) {
					logger.error("上班时间为0，或者时间计算有误");
					timeDou = 0.0;
				}
				ret += timeDou;
			} catch (Exception e) {
				logger.error("时间转换异常+" + e.getMessage());
			}
		}
		return ret;
	}

	public Double keepTwoDecimals(double d1) {
		if (d1 == 0.0) {
			return d1;
		}
		BigDecimal b = new BigDecimal(d1);
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	private boolean setValue(Map<String, Object> map, String key, String value) {
		List<String> valueList = new ArrayList<>();
		if (value != null && !"".equals(value)) {
			String[] values = value.split(",");
			for (String value1 : values) {
				valueList.add(value1);
			}
		}
		map.put(key, valueList);
		if (valueList.size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 获取测试用例数 type=0计划 1 执行
	 * 
	 * @param projNo
	 * @param type
	 * @return
	 */
	public List<GerenCode> getSubmitTestCases(String projNo, String type, String role, String[] months) {
		List<GerenCode> ret = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		if (!setValue(map, "role", role)) {
			logger.error("职位参数序列化异常");
			return ret;
		}
		map.put("no", projNo);
		List<Map<String, Object>> selectedMaps = projectManagersDao.queryOMPUserSelectedDevelop(map);

		for (Map<String, Object> user : selectedMaps) {
			GerenCode gerenCode = new GerenCode();
			gerenCode.setName(StringUtilsLocal.valueOf(user.get("NAME")));
			gerenCode.setRole(StringUtilsLocal.valueOf(user.get("ROLE")));
			gerenCode.setNumber(StringUtilsLocal.valueOf(user.get("HW_ACCOUNT")));
			if ("".equals(gerenCode.getNumber())) {
				ret.add(gerenCode);
				continue;
			}
			int all = 0;
			List<Map<String, Object>> testCases;
			if ("0".equals(type)) {
				testCases = testMeasuresDao.queryTestCasesList(projNo, gerenCode.getNumber());
			} else {
				testCases = testMeasuresDao.queryTestCasesStartList(projNo, gerenCode.getNumber());
			}
			for (Map<String, Object> testCase : testCases) {
				int timeDou = Integer.parseInt(StringUtilsLocal.valueOf(testCase.get("sums")));
				all += timeDou;
				for (int i = 0; i < months.length; i++) {
					String month = months[i];
					if (month.equals(StringUtilsLocal.valueOf(testCase.get("months")))) {
						switch (i + 1) {
						case 1:
							gerenCode.setFirst(timeDou);
							break;
						case 2:
							gerenCode.setSecond(timeDou);
							break;
						case 3:
							gerenCode.setThird(timeDou);
							break;
						case 4:
							gerenCode.setFourth(timeDou);
							break;
						case 5:
							gerenCode.setFifth(timeDou);
							break;
						case 6:
							gerenCode.setSixth(timeDou);
							break;
						case 7:
							gerenCode.setSeventh(timeDou);
							break;
						case 8:
							gerenCode.setEighth(timeDou);
							break;
						case 9:
							gerenCode.setNinth(timeDou);
							break;
						case 10:
							gerenCode.setTenth(timeDou);
							break;
						case 11:
							gerenCode.setEleventh(timeDou);
							break;
						case 12:
							gerenCode.setTwelfth(timeDou);
							break;
						default:
							break;
						}
						break;
					}
				}
			}
			gerenCode.setSum(all);
			ret.add(gerenCode);
		}
		return ret;
	}

	public List<GerenCode> getSubmitTestCases(String projNo, String type,String role) {
		List<GerenCode> ret = new ArrayList<>();
		Map<String, Object> map=new HashMap<>();
		
		try {
  		  	role = URLDecoder.decode(role, "UTF-8");
	  	} catch (UnsupportedEncodingException e) {
	  		logger.error("URLDecoder.decode exception, error: "+e.getMessage());
	  	}
		
		if(!setValue(map,"role",role)) {
			logger.error("职位参数序列化异常");
			return ret;
		}
		map.put("no", projNo);
		List<Map<String, Object>> selectedMaps = projectManagersDao.queryOMPUserSelectedDevelop(map);
		
		//最近12个月的月份
		String[] months = TestMeasuresService.getLast12Months();
		
		for (Map<String, Object> user : selectedMaps) {
			GerenCode gerenCode = new GerenCode();
			gerenCode.setName(StringUtilsLocal.valueOf(user.get("NAME")));
			gerenCode.setRole(StringUtilsLocal.valueOf(user.get("ROLE")));
			gerenCode.setNumber(StringUtilsLocal.valueOf(user.get("HW_ACCOUNT")));
			if("".equals(gerenCode.getNumber())) {
				ret.add(gerenCode);
				continue;
			}
			int all = 0;
			List<Map<String, Object>> testCases;
			if("0".equals(type)) {
				testCases = testMeasuresDao.queryTestCasesByAuthor(projNo,gerenCode.getNumber(),months[0],months[11]);
			}else {
				testCases = testMeasuresDao.queryTestCasesStartByAuthor(projNo,gerenCode.getNumber(),months[0],months[11]);
			}
			for (Map<String, Object> testCase : testCases) {
				int timeDou = Integer.parseInt(StringUtilsLocal.valueOf(testCase.get("sums")));
				all += timeDou;
				
				//用if else语句改写switch..case语句
				if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[0])) {
					gerenCode.setJanuary(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[1])) {
					gerenCode.setFebruary(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[2])) {
					gerenCode.setMarch(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[3])) {
					gerenCode.setApril(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[4])) {
					gerenCode.setMay(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[5])) {
					gerenCode.setJune(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[6])) {
					gerenCode.setJuly(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[7])) {
					gerenCode.setAugust(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[8])) {
					gerenCode.setSeptember(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[9])) {
					gerenCode.setOctober(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[10])) {
					gerenCode.setNovember(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[11])) {
					gerenCode.setDecember(timeDou);
				}
//				switch (StringUtilsLocal.valueOf(testCase.get("months"))) {
//					case "Jan":
//						gerenCode.setJanuary(timeDou);
//						break;
//					case "Feb":
//						gerenCode.setFebruary(timeDou);
//						break;
//					case "Mar":
//						gerenCode.setMarch(timeDou);
//						break;
//					case "Apr":
//						gerenCode.setApril(timeDou);
//						break;
//					case "May":
//						gerenCode.setMay(timeDou);
//						break;
//					case "Jun":
//						gerenCode.setJune(timeDou);
//						break;
//					case "Jul":
//						gerenCode.setJuly(timeDou);
//						break;
//					case "Aug":
//						gerenCode.setAugust(timeDou);
//						break;
//					case "Sep":
//						gerenCode.setSeptember(timeDou);
//						break;
//					case "Oct":
//						gerenCode.setOctober(timeDou);
//						break;
//					case "Nov":
//						gerenCode.setNovember(timeDou);
//						break;
//					case "Dec":
//						gerenCode.setDecember(timeDou);
//						break;
//					default:
//						break;
//				}
			}
			gerenCode.setSum(all);
			ret.add(gerenCode);
		}
		return ret;
	}

	public Map<String, Object> inputTestCases(GerenCodeList gerenCodeList, String[] months) {
		List<TestCaseInput> caseInputs = new ArrayList<>();
		Map<String, Object> ret = new HashMap<>();
		if (gerenCodeList.getGerenCodes() == null) {
			ret.put("msg", "传入表为空");
			return ret;
		}
		String proNo = gerenCodeList.getProNo();
		Integer type = gerenCodeList.getType();

		for (GerenCode gerenCode : gerenCodeList.getGerenCodes()) {
			if (StringUtilsLocal.isBlank(gerenCode.getNumber())) {
				continue;
			}
			for (int i = 0; i < months.length; i++) {
				String month = months[i];
				TestCaseInput testCaseInput = new TestCaseInput();
				testCaseInput.setProNO(proNo);
				testCaseInput.setHwAccount(gerenCode.getNumber());
				testCaseInput.setTestCaseType(type);

				int value = 0;
				switch (i + 1) {
				case 1:
					value = gerenCode.getFirst();
					break;
				case 2:
					value = gerenCode.getSecond();
					break;
				case 3:
					value = gerenCode.getThird();
					break;
				case 4:
					value = gerenCode.getFourth();
					break;
				case 5:
					value = gerenCode.getFifth();
					break;
				case 6:
					value = gerenCode.getSixth();
					break;
				case 7:
					value = gerenCode.getSeventh();
					break;
				case 8:
					value = gerenCode.getEighth();
					break;
				case 9:
					value = gerenCode.getNinth();
					break;
				case 10:
					value = gerenCode.getTenth();
					break;
				case 11:
					value = gerenCode.getEleventh();
					break;
				case 12:
					value = gerenCode.getTwelfth();
					break;
				default:
					break;
				}
				Date date = null;
				try {
					date = DateUtils.parseDate(month + "-01", "yyyy-MM-dd");
				} catch (Exception e) {
					logger.error("时间序列号异常：" + e.getMessage());
				}
				testCaseInput.setDate(date);
				testCaseInput.setTestCaseValue(value + "");
				caseInputs.add(testCaseInput);
			}
		}
		if (caseInputs.size() > 0) {
			// testMeasuresDao.deleteTestCaseByNo(proNo,type);
			testMeasuresDao.insertTestCases(caseInputs);
		}
		ret.put("msg", "导入成功");
		return ret;
	}

	public Map<String, Object> inputTestCases(GerenCodeList gerenCodeList) {
		List<TestCaseInput> caseInputs = new ArrayList<>();
		Map<String, Object> ret = new HashMap<>();
		if (gerenCodeList.getGerenCodes() == null) {
			ret.put("msg", "传入表为空");
			return ret;
		}
		String proNo = gerenCodeList.getProNo();
		Integer type = gerenCodeList.getType();
		
		//最近12个月的月份
		String[] months = TestMeasuresService.getLast12Months();
		
		for (GerenCode gerenCode : gerenCodeList.getGerenCodes()) {
			if (StringUtilsLocal.isBlank(gerenCode.getNumber())) {
				continue;
			}
			for (String month : months) {
				TestCaseInput testCaseInput = new TestCaseInput();
				testCaseInput.setProNO(proNo);
				testCaseInput.setHwAccount(gerenCode.getNumber());
				testCaseInput.setTestCaseType(type);
				int value = 0;
				//用if else语句改写switch..case语句(注意：if语句的条件)
				if (month.equals(months[0])) {
					value = gerenCode.getJanuary();
				}else if (month.equals(months[1])) {
					value = gerenCode.getFebruary();
				}else if (month.equals(months[2])) {
					value = gerenCode.getMarch();
				}else if (month.equals(months[3])) {
					value = gerenCode.getApril();
				}else if (month.equals(months[4])) {
					value = gerenCode.getMay();
				}else if (month.equals(months[5])) {
					value = gerenCode.getJune();
				}else if (month.equals(months[6])) {
					value = gerenCode.getJuly();
				}else if (month.equals(months[7])) {
					value = gerenCode.getAugust();
				}else if (month.equals(months[8])) {
					value = gerenCode.getSeptember();
				}else if (month.equals(months[9])) {
					value = gerenCode.getOctober();
				}else if (month.equals(months[10])) {
					value = gerenCode.getNovember();
				}else if (month.equals(months[11])) {
					value = gerenCode.getDecember();
				}
//				switch (month) {
//				case "01":
//					value = gerenCode.getJanuary();
//					break;
//				case "02":
//					value = gerenCode.getFebruary();
//					break;
//				case "03":
//					value = gerenCode.getMarch();
//					break;
//				case "04":
//					value = gerenCode.getApril();
//					break;
//				case "05":
//					value = gerenCode.getMay();
//					break;
//				case "06":
//					value = gerenCode.getJune();
//					break;
//				case "07":
//					value = gerenCode.getJuly();
//					break;
//				case "08":
//					value = gerenCode.getAugust();
//					break;
//				case "09":
//					value = gerenCode.getSeptember();
//					break;
//				case "10":
//					value = gerenCode.getOctober();
//					break;
//				case "11":
//					value = gerenCode.getNovember();
//					break;
//				case "12":
//					value = gerenCode.getDecember();
//					break;
//				default:
//					break;
//				}
				Date date = null;
				try {
					date = DateUtils.parseDate(month + "-01", "yyyy-MM-dd");
				} catch (Exception e) {
					logger.error("时间序列号异常：" + e.getMessage());
				}
				testCaseInput.setDate(date);
				testCaseInput.setTestCaseValue(value + "");
				caseInputs.add(testCaseInput);
			}

		}
		if (caseInputs.size() > 0) {
			// testMeasuresDao.deleteTestCaseByNo(proNo,type);
			testMeasuresDao.insertTestCases(caseInputs);
		}
		ret.put("msg", "导入成功");
		return ret;
	}
	
	public void input2TestCases(TestCaseInput testCaseInput) {
		try {
			testMeasuresDao.insert2TestCases(testCaseInput);
		} catch (Exception e) {
			logger.error("testMeasuresDao.insert2TestCases exception, error: "+e.getMessage());
		}
	}

	public List<GerenCode> getInputTestCases(String projNo, String type, String role, String[] months) {
		List<GerenCode> ret = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		if (!setValue(map, "role", role)) {
			logger.error("职位参数序列化异常");
			return ret;
		}
		map.put("no", projNo);
		List<Map<String, Object>> selectedMaps = projectManagersDao.queryOMPUserSelectedDevelop(map);

		for (Map<String, Object> user : selectedMaps) {
			GerenCode gerenCode = new GerenCode();
			gerenCode.setName(StringUtilsLocal.valueOf(user.get("NAME")));
			gerenCode.setRole(StringUtilsLocal.valueOf(user.get("ROLE")));
			gerenCode.setNumber(StringUtilsLocal.valueOf(user.get("HW_ACCOUNT")));
			int all = 0;
			List<Map<String, Object>> testCases = testMeasuresDao.queryInputTestCasesList(projNo, gerenCode.getNumber(),
					type);
			for (Map<String, Object> testCase : testCases) {
				int timeDou = Integer.parseInt(StringUtilsLocal.valueOf(testCase.get("TESTCASE_VALUE")));
				
				for (int i = 0; i < months.length; i++) {
					String month = months[i];
					if (month.equals(StringUtilsLocal.valueOf(testCase.get("months")))) {
						all += timeDou;
						switch (i + 1) {
						case 1:
							gerenCode.setFirst(timeDou);
							break;
						case 2:
							gerenCode.setSecond(timeDou);
							break;
						case 3:
							gerenCode.setThird(timeDou);
							break;
						case 4:
							gerenCode.setFourth(timeDou);
							break;
						case 5:
							gerenCode.setFifth(timeDou);
							break;
						case 6:
							gerenCode.setSixth(timeDou);
							break;
						case 7:
							gerenCode.setSeventh(timeDou);
							break;
						case 8:
							gerenCode.setEighth(timeDou);
							break;
						case 9:
							gerenCode.setNinth(timeDou);
							break;
						case 10:
							gerenCode.setTenth(timeDou);
							break;
						case 11:
							gerenCode.setEleventh(timeDou);
							break;
						case 12:
							gerenCode.setTwelfth(timeDou);
							break;
						default:
							break;
						}
						break;
					}

				}
			}
			gerenCode.setSum(all);
			ret.add(gerenCode);
		}
		return ret;
	}

	public List<GerenCode> getInputTestCases(String projNo, String type, String role) {
		List<GerenCode> ret = new ArrayList<>();
		Map<String, Object> map=new HashMap<>();
		
		try {
  		  	role = URLDecoder.decode(role, "UTF-8");
	  	} catch (UnsupportedEncodingException e) {
	  		logger.error("URLDecoder.decode exception, error: "+e.getMessage());
	  	}
		
		if(!setValue(map,"role",role)) {
			logger.error("职位参数序列化异常");
			return ret;
		}
		map.put("no", projNo);
		List<Map<String, Object>> selectedMaps = projectManagersDao.queryOMPUserSelectedDevelop(map);
		
		//最近12个月的月份
		String[] months = TestMeasuresService.getLast12Months();
		
		for (Map<String, Object> user : selectedMaps) {
			GerenCode gerenCode = new GerenCode();
			gerenCode.setName(StringUtilsLocal.valueOf(user.get("NAME")));
			gerenCode.setRole(StringUtilsLocal.valueOf(user.get("ROLE")));
			gerenCode.setNumber(StringUtilsLocal.valueOf(user.get("HW_ACCOUNT")));
			int all = 0;
			List<Map<String, Object>> testCases = testMeasuresDao.queryInputTestCasesByAuthor(projNo,gerenCode.getNumber(),type,months[0],months[11]);
			for (Map<String, Object> testCase : testCases) {
				int timeDou = Integer.parseInt(StringUtilsLocal.valueOf(testCase.get("TESTCASE_VALUE")));
				all += timeDou;
				
				//用if else语句改写switch..case语句
				if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[0])) {
					gerenCode.setJanuary(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[1])) {
					gerenCode.setFebruary(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[2])) {
					gerenCode.setMarch(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[3])) {
					gerenCode.setApril(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[4])) {
					gerenCode.setMay(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[5])) {
					gerenCode.setJune(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[6])) {
					gerenCode.setJuly(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[7])) {
					gerenCode.setAugust(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[8])) {
					gerenCode.setSeptember(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[9])) {
					gerenCode.setOctober(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[10])) {
					gerenCode.setNovember(timeDou);
				}else if (StringUtilsLocal.valueOf(testCase.get("months")).equals(months[11])) {
					gerenCode.setDecember(timeDou);
				}
//				switch (StringUtilsLocal.valueOf(testCase.get("months"))) {
//				case "01":
//					gerenCode.setJanuary(timeDou);
//					break;
//				case "02":
//					gerenCode.setFebruary(timeDou);
//					break;
//				case "03":
//					gerenCode.setMarch(timeDou);
//					break;
//				case "04":
//					gerenCode.setApril(timeDou);
//					break;
//				case "05":
//					gerenCode.setMay(timeDou);
//					break;
//				case "06":
//					gerenCode.setJune(timeDou);
//					break;
//				case "07":
//					gerenCode.setJuly(timeDou);
//					break;
//				case "08":
//					gerenCode.setAugust(timeDou);
//					break;
//				case "09":
//					gerenCode.setSeptember(timeDou);
//					break;
//				case "10":
//					gerenCode.setOctober(timeDou);
//					break;
//				case "11":
//					gerenCode.setNovember(timeDou);
//					break;
//				case "12":
//					gerenCode.setDecember(timeDou);
//					break;
//				default:
//					break;
			}
			gerenCode.setSum(all);
			ret.add(gerenCode);
		}
		return ret;
	}

	public void saveTestCaseStatistics(String projNo, String type, List<GerenCode> gerenCodes, String[] months) {
		List<TestCaseInput> caseInputs = new ArrayList<>();
		Map<String, TestCaseInput> testCaseInputs = new HashMap<>();
		for (GerenCode gerenCode : gerenCodes) {
			for (int i = 0; i < months.length; i++) {
				String month = months[i];
				if (!testCaseInputs.containsKey(projNo + month + type)) {
					TestCaseInput testCaseInput = new TestCaseInput();
					testCaseInput.setProNO(projNo);
					testCaseInput.setTestCaseType(Integer.parseInt(type));
					testCaseInputs.put(projNo + month + type, testCaseInput);
				}
				int value = 0;
				switch (i + 1) {
				case 1:
					value = gerenCode.getFirst();
					break;
				case 2:
					value = gerenCode.getSecond();
					break;
				case 3:
					value = gerenCode.getThird();
					break;
				case 4:
					value = gerenCode.getFourth();
					break;
				case 5:
					value = gerenCode.getFifth();
					break;
				case 6:
					value = gerenCode.getSixth();
					break;
				case 7:
					value = gerenCode.getSeventh();
					break;
				case 8:
					value = gerenCode.getEighth();
					break;
				case 9:
					value = gerenCode.getNinth();
					break;
				case 10:
					value = gerenCode.getTenth();
					break;
				case 11:
					value = gerenCode.getEleventh();
					break;
				case 12:
					value = gerenCode.getTwelfth();
					break;
				default:
					break;
				}
				Date date = null;
				try {
					date = DateUtils.parseDate(month + "-01", "yyyy-MM-dd");
				} catch (Exception e) {
					logger.error("时间序列号异常：" + e.getMessage());
				}
				testCaseInputs.get(projNo + month + type).setDate(date);
				String old = testCaseInputs.get(projNo + month + type).getTestCaseValue();
				if (old != null && !"".equals(old)) {
					value += Integer.parseInt(old);
				}
				testCaseInputs.get(projNo + month + type).setTestCaseValue(value + "");
			}
		}
		for (TestCaseInput testCaseInput : testCaseInputs.values()) {
			caseInputs.add(testCaseInput);
		}
		if (caseInputs.size() > 0) {
			testMeasuresDao.insertTestStatisticsCases(caseInputs);
		}
	}

	public void saveTestCaseStatistics(String projNo,String type, List<GerenCode> gerenCodes) {
		List<TestCaseInput> caseInputs = new ArrayList<>();
		Map<String, TestCaseInput> testCaseInputs = new HashMap<>();
		
		//最近12个月的月份
		String[] months = TestMeasuresService.getLast12Months();
				
		for (GerenCode gerenCode: gerenCodes) {
			for (String month : months) {
				if(!testCaseInputs.containsKey(projNo+month+type)) {
					TestCaseInput testCaseInput = new TestCaseInput();
					testCaseInput.setProNO(projNo);
					testCaseInput.setTestCaseType(Integer.parseInt(type));
					testCaseInputs.put(projNo+month+type, testCaseInput);
				}
				int value = 0;
				
				//用if else语句改写switch..case语句(注意：if语句的条件)
				if (month.equals(months[0])) {
					value = gerenCode.getJanuary();
				}else if (month.equals(months[1])) {
					value = gerenCode.getFebruary();
				}else if (month.equals(months[2])) {
					value = gerenCode.getMarch();
				}else if (month.equals(months[3])) {
					value = gerenCode.getApril();
				}else if (month.equals(months[4])) {
					value = gerenCode.getMay();
				}else if (month.equals(months[5])) {
					value = gerenCode.getJune();
				}else if (month.equals(months[6])) {
					value = gerenCode.getJuly();
				}else if (month.equals(months[7])) {
					value = gerenCode.getAugust();
				}else if (month.equals(months[8])) {
					value = gerenCode.getSeptember();
				}else if (month.equals(months[9])) {
					value = gerenCode.getOctober();
				}else if (month.equals(months[10])) {
					value = gerenCode.getNovember();
				}else if (month.equals(months[11])) {
					value = gerenCode.getDecember();
				}
//				switch (month) {
//				case "01":
//					value = gerenCode.getJanuary();
//					break;
//				case "02":
//					value = gerenCode.getFebruary();
//					break;
//				case "03":
//					value = gerenCode.getMarch();
//					break;
//				case "04":
//					value = gerenCode.getApril();
//					break;
//				case "05":
//					value = gerenCode.getMay();
//					break;
//				case "06":
//					value = gerenCode.getJune();
//					break;
//				case "07":
//					value = gerenCode.getJuly();
//					break;
//				case "08":
//					value = gerenCode.getAugust();
//					break;
//				case "09":
//					value = gerenCode.getSeptember();
//					break;
//				case "10":
//					value = gerenCode.getOctober();
//					break;
//				case "11":
//					value = gerenCode.getNovember();
//					break;
//				case "12":
//					value = gerenCode.getDecember();
//					break;
//				default:
//					break;
//				}
				
				//date的异常处理与类型转换：sdf.parse(string) 与 sdf.format(date)
				Date date = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date = sdf.parse(month + "-01");//string转date类型
				} catch (Exception e) {
					logger.error("时间序列号异常："+e.getMessage());
				}
				testCaseInputs.get(projNo+month+type).setDate(date);
				String old = testCaseInputs.get(projNo+month+type).getTestCaseValue();
				if(old != null && !"".equals(old)) {
					value += Integer.parseInt(old);
				}
				testCaseInputs.get(projNo+month+type).setTestCaseValue(value+"");
			}
		}
		for (TestCaseInput testCaseInput : testCaseInputs.values()) {
			caseInputs.add(testCaseInput);
		}
		if(caseInputs.size()>0) {
			testMeasuresDao.insertTestStatisticsCases(caseInputs);
		}
	}

	public static double monthGetValue(String month, MonthlyManpower manpower) {
		double ret = 0;
		switch (month) {
		case "01":
			ret = manpower.getJanuary();
			break;
		case "02":
			ret = manpower.getFebruary();
			break;
		case "03":
			ret = manpower.getMarch();
			break;
		case "04":
			ret = manpower.getApril();
			break;
		case "05":
			ret = manpower.getMay();
			break;
		case "06":
			ret = manpower.getJune();
			break;
		case "07":
			ret = manpower.getJuly();
			break;
		case "08":
			ret = manpower.getAugust();
			break;
		case "09":
			ret = manpower.getSeptember();
			break;
		case "10":
			ret = manpower.getOctober();
			break;
		case "11":
			ret = manpower.getNovember();
			break;
		case "12":
			ret = manpower.getDecember();
			break;
		default:
			break;
		}
		return ret;
	}
	
	/**
	 * 计算项目在起止时间区间内总人天
	 * @param projNo
	 * @param start
	 * @param end
	 * @return
	 */
	public double getProjectAllManpower(String projNo, Date start, Date end) {
		String[] months = DateUtils.getInMonths(start, end);
		List<Map<String, Object>> selectedMaps = projectManagersDao.queryOMPUserSelected(projNo);
		double allManpower = 0.0;
		for (Map<String, Object> user : selectedMaps) {
			String zrAcc = StringUtilsLocal.valueOf(user.get("ZR_ACCOUNT"));
			if ("".equals(zrAcc)) {
				zrAcc = StringUtilsLocal.valueOf(user.get("account"));
			}
			if ("".equals(zrAcc)) {
				continue;
			}
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = DateUtils.SHORT_FORMAT_GENERAL.parse(StringUtilsLocal.valueOf(user.get("START_DATE")));
				endDate = DateUtils.SHORT_FORMAT_GENERAL.parse(StringUtilsLocal.valueOf(user.get("END_DATE")));
			} catch (Exception e) {
				logger.error("开始结束时间为空，会导致统计值缺少计算");
			}
			
			List<Map<String, Object>> WorkHoursdays = testMeasuresDao.queryWorkHoursDayByAuthorLatest(zrAcc, null);
			Map<String, Map<String, Object>> WorkHoursdaysList = classifiedByMonth(WorkHoursdays);
			for (String  month: months) {
				double timeDou = 22.5;
				Date date = DateUtils.getMonthLastDay(month);
				if (WorkHoursdaysList.get(month) != null) {
					String timeStr = StringUtilsLocal.valueOf(WorkHoursdaysList.get(month).get("actual_labor_hour"));
					timeDou = Double.parseDouble(timeStr == "" ? "0.0" : timeStr)/8;	
					date = (Date) WorkHoursdaysList.get(month).get("statistical_time");	
				}
				if (startDate != null && endDate != null) {
					timeDou = getNewTimeDou(date, startDate, endDate, timeDou);
				}
				timeDou = keepTwoDecimals(timeDou);
				allManpower += timeDou;
			}	
		}
		return allManpower;
	}
}
