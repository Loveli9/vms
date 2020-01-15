package com.icss.mvp.service;

import com.github.pagehelper.PageHelper;
import com.icss.mvp.dao.*;
import com.icss.mvp.dao.project.IProjectReviewDao;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.project.ProjectReviewEntity;
import com.icss.mvp.entity.project.ProjectReviewVo;
import com.icss.mvp.entity.rank.ProjectMonthBudget;
import com.icss.mvp.service.io.ExportService;
import com.icss.mvp.service.member.AttentionPersonServices;
import com.icss.mvp.service.project.ProjectReviewService;
import com.icss.mvp.service.rank.RankSalaryService;
import com.icss.mvp.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("projectOverviewService")
public class ProjectOverviewService {

	private static Logger logger = Logger.getLogger(ProjectOverviewService.class);

	@Resource
	private HttpServletRequest request;

	@Autowired
	private ProjectReportDao projectReportDao;

	@Autowired
	private IProjectReviewDao projectReviewDao;

	@Resource
	private IProjectOverviewDao projectOverviewDao;

	@Autowired
	private MeasureCommentDao measureCommentDao;

	@Resource
	private ProjectInfoService projectInfoService;

	@Resource
	private ProjectReviewService projectReviewService;

	@Resource
	private MeasureCommentService measureCommentService;

	@Autowired
	private ExportService exportService;

	@Autowired
	private MeasureAchievementStatusDao measureAchievementStatusDao;

	@Autowired
	private ProjectInfoVoDao projectInfoVoDao;

	@Autowired
	private RankSalaryService rankSalaryService;

	@Resource
	private ProjectListService projectListService;

	@Resource
	private BuOverviewService buOverviewService;

	@Resource
	private IProjectInfoDao projectInfoDao;

	@Autowired
	private AttentionPersonServices attentionPersonServices;

	/**
	 * 报表总览
	 *
	 * @param projectInfo
	 * @param pageRequest
	 * @param statisticalTime
	 * @param status
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "null" })
	public TableSplitResult getProjectOverview(ProjectInfo projectInfo, PageRequest pageRequest, String statisticalTime,
											   String model,String status,String sort,String sortOrder) throws ParseException {
		TableSplitResult ret = new TableSplitResult();

		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		List<String> proNos = new ArrayList<>();

		if(!StringUtils.isEmpty(model)){
			List<ProjectInfo> projectInfoList = projectInfoDao.queryFavoriteProject(username);
			proNos = projectInfoList.stream().map(ProjectInfo::getNo).collect(Collectors.toList());
		}else{
			Set<String> nos = projectInfoService.projectNos(projectInfo, null);
			proNos = new ArrayList<>(nos);
		}

		if (proNos.size() <= 0) {
			ret.setErr(new ArrayList<>(), pageRequest.getPageNumber());
			return ret;
		}
		com.github.pagehelper.Page page = PageHelper.startPage(
				(pageRequest.getPageNumber() == null ? 1 : pageRequest.getPageNumber()), pageRequest.getPageSize());
		List<QualityMonthlyReport> qualityMonthlyReports = new ArrayList<>();
//				projectReportDao.getProjectOverviews(
//				"(" + StringUtilsLocal.listToSqlIn(proNos) + ")", statisticalTime, status, username,
//				projectInfo.getCoopType(),sort,sortOrder);
		ret.setTotal((int) page.getTotal());
		for (QualityMonthlyReport qualityMonthlyReport : qualityMonthlyReports) {
			String pm = qualityMonthlyReport.getPm();
			if (StringUtils.isNotBlank(pm)) {
				qualityMonthlyReport.setPm(pm.replaceAll("[^\u4e00-\u9fa5]", ""));
			}
			if (StringUtils.isNotBlank(qualityMonthlyReport.getHwzpdu())) {
				qualityMonthlyReport
						.setDepartment(qualityMonthlyReport.getHwzpdu() + "/" + qualityMonthlyReport.getPduSpdt());
			} else {
				qualityMonthlyReport.setDepartment(qualityMonthlyReport.getPdu() + "/" + qualityMonthlyReport.getDu());
			}
//			Map<String, Object> startStopTime = projectOverviewDao.getStartStopTime(qualityMonthlyReport.getNo());
			SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
			List<String> firstFiveWeekDate = DateUtils.getLatestWeek(5, true, statisticalTime);
			Date startTime = null;
			Date stopTime = null;
			try {
				if (null != qualityMonthlyReport.getStartDate() && null != qualityMonthlyReport.getEndDate()) {
					startTime = qualityMonthlyReport.getStartDate();
					stopTime = qualityMonthlyReport.getEndDate();
					Date nowTime = new Date();
					Date currentPeriod = sdt.parse(statisticalTime);
					long totalDate = (stopTime.getTime() - startTime.getTime()) / (60 * 60 * 24 * 1000);
					long scheduleDate = (nowTime.getTime() - startTime.getTime()) / (60 * 60 * 24 * 1000);
					long overdueDate1 = (nowTime.getTime() - stopTime.getTime()) / (60 * 60 * 24 * 1000);
					long overdueDate = (currentPeriod.getTime() - stopTime.getTime()) / (60 * 60 * 24 * 1000);
					long overdueDate2 = (currentPeriod.getTime() - startTime.getTime()) / (60 * 60 * 24 * 1000);
					NumberFormat num = NumberFormat.getPercentInstance();
					double progressProportion = (double) scheduleDate / totalDate;
					String mileagePoint = num.format(progressProportion);
					double oldProgress = (double) overdueDate2 / totalDate;
					String oldMileagePoint = num.format(oldProgress);

					if (StringUtils.isNotBlank(qualityMonthlyReport.getProjectState())) {
						List<String> date1 = DateUtils.getLatestCycles(1, true);
						if (("在行").equals(qualityMonthlyReport.getProjectState())) {
							if (statisticalTime.equals(date1.get(0))) {
								if (overdueDate1 > 0) {
									qualityMonthlyReport.setMileagePoint("100");
									qualityMonthlyReport.setOverdueDate(overdueDate1);
								} else {
									qualityMonthlyReport.setMileagePoint(mileagePoint);
									qualityMonthlyReport.setOverdueDate(-1L);
								}
							} else {
								if (overdueDate > 0) {
									qualityMonthlyReport.setMileagePoint("100");
									qualityMonthlyReport.setOverdueDate(overdueDate);
								} else {
									qualityMonthlyReport.setMileagePoint(oldMileagePoint);
									qualityMonthlyReport.setOverdueDate(-1L);
								}
							}
							List<String> totalCycle = null;
							if (startTime.after(sdt.parse(firstFiveWeekDate.get(1)))
									&& !startTime.after(sdt.parse(statisticalTime))) {
								qualityMonthlyReport.setStatisticalCycle(1);
							} else {
								String startDate = sdt.format(startTime);
								if (DateUtils.isLastDayOfMonth(startTime)
										|| Integer.valueOf(startDate.split("-")[2]) == 15) {
									startDate = DateUtils.getAroundDay(startTime, -1);
								}
								totalCycle = DateUtils.getInHoursCycles(startDate, statisticalTime);
							}

							if (null != totalCycle && totalCycle.size() > 0) {
								qualityMonthlyReport.setStatisticalCycle(totalCycle.size() + 1);
							}
						} else {
							qualityMonthlyReport.setMileagePoint("-1");
							qualityMonthlyReport.setOverdueDate(-1L);
						}
					}
				}
			} catch (Exception e) {
				logger.error("acquire mileagePoint failed: " + e.getMessage());
			}

			String knotProjectDate = projectOverviewDao.getKnotProjectDate(qualityMonthlyReport.getNo(), 1);

			List<ProjectReviewEntity> projectReview = projectReviewService
					.queryProjectReview(qualityMonthlyReport.getNo(), statisticalTime, status);
			if (projectReview != null && projectReview.size() > 0) {
				qualityMonthlyReport.setQualityLamp(projectReview.get(0).getQualityLamp());
				qualityMonthlyReport.setKeyRoles(projectReview.get(0).getResourcesLamp());
				qualityMonthlyReport.setPlanLamp(projectReview.get(0).getProgressLamp());
				qualityMonthlyReport.setScopeLamp(projectReview.get(0).getChangesLamp());
				qualityMonthlyReport.setComment(projectReview.get(0).getProjectReview());
			}
			if (StringUtils.isNotBlank(statisticalTime) && startTime != null) {
				if (StringUtils.isNotBlank(knotProjectDate)) {
					if (sdt.parse(knotProjectDate).before(sdt.parse(statisticalTime))
							|| sdt.parse(statisticalTime).before(startTime)) {
						qualityMonthlyReport.setQualityLamp("-1");
						qualityMonthlyReport.setKeyRoles("-1");
						qualityMonthlyReport.setPlanLamp("-1");
						qualityMonthlyReport.setScopeLamp("-1");
						qualityMonthlyReport.setStatusAssessment("-1");
						qualityMonthlyReport.setComment("");
					}
				} else {
					if (sdt.parse(statisticalTime).before(startTime)) {
						qualityMonthlyReport.setQualityLamp("-1");
						qualityMonthlyReport.setKeyRoles("-1");
						qualityMonthlyReport.setPlanLamp("-1");
						qualityMonthlyReport.setScopeLamp("-1");
						qualityMonthlyReport.setStatusAssessment("-1");
						qualityMonthlyReport.setComment("");
					}
				}
			}

			List<String> fiveQualityLamp = new ArrayList<>();
			List<String> fiveKeyRoles = new ArrayList<>();
			List<String> fivePlanLamp = new ArrayList<>();
			List<String> fiveScopeLamp = new ArrayList<>();
			List<String> fiveStatusAssessment = new ArrayList<>();
			List<String> fiveComment = new ArrayList<>();

			for (int i = 0; i < 5; i++) {
				List<ProjectReviewVo> projectReviewData = oldWeekReview(firstFiveWeekDate.get(i), qualityMonthlyReport,
						sdt, knotProjectDate, startTime);
				if (null != projectReviewData && projectReviewData.size() > 0) {
					fiveQualityLamp.add(projectReviewData.get(0).getQualityLamp());
					fiveKeyRoles.add(projectReviewData.get(0).getResourcesLamp());
					fivePlanLamp.add(projectReviewData.get(0).getProgressLamp());
					fiveScopeLamp.add(projectReviewData.get(0).getChangesLamp());
					fiveStatusAssessment.add(projectReviewData.get(0).getProjectStatus());
					fiveComment.add(projectReviewData.get(0).getProjectReview());
				} else {
					fiveQualityLamp.add(null);
					fiveKeyRoles.add(null);
					fivePlanLamp.add(null);
					fiveScopeLamp.add(null);
					fiveStatusAssessment.add(null);
					fiveComment.add(null);
				}
			}

			qualityMonthlyReport.setFiveQualityLamp(fiveQualityLamp);
			qualityMonthlyReport.setFiveKeyRoles(fiveKeyRoles);
			qualityMonthlyReport.setFivePlanLamp(fivePlanLamp);
			qualityMonthlyReport.setFiveScopeLamp(fiveScopeLamp);
			qualityMonthlyReport.setFiveStatusAssessment(fiveStatusAssessment);
			qualityMonthlyReport.setFiveComment(fiveComment);
		}
		ret.setRows(qualityMonthlyReports);
		ret.setPage(pageRequest.getPageNumber());
		return ret;
	}


	/**
	 * 近五次点评
	 *
	 * @param projectInfo
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "null" })
	public TableSplitResult getProjectAccess(ProjectInfo projectInfo, TableSplitResult page) {
		TableSplitResult ret = new TableSplitResult();

		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);

		List<ProjectInfo> favoritePros = projectInfoDao.queryFavoriteProject(username);
		if(null == favoritePros || favoritePros.size() == 0){
			ret.setTotal(0);
			return ret;
		}
		List<String> proNos = favoritePros.stream().map(ProjectInfo::getNo).collect(Collectors.toList());

		ret.setTotal(proNos.size());
		List<QualityMonthlyReport> qualityMonthlyReports = projectReportDao.getProjectAccess(
				"(" + StringUtilsLocal.listToSqlIn(proNos) + ")",page);

		List<String> queryedNos =  qualityMonthlyReports.stream().map(QualityMonthlyReport::getNo).collect(Collectors.toList());

		//查询近五次结果
		List<QualityMonthlyReport> currentResult = projectReportDao.getProjectAccessCurrent(
				"(" + StringUtilsLocal.listToSqlIn(queryedNos) + ")");

		Map<String, List<QualityMonthlyReport>> resultMaps = currentResult.stream()
				.collect(Collectors.groupingBy(QualityMonthlyReport::getNo));

		for(QualityMonthlyReport report : qualityMonthlyReports){
			List<QualityMonthlyReport> list = resultMaps.get(report.getNo());
			report.setFiveQualityLamp(completeListStr(list.stream().map(QualityMonthlyReport::getQualityLamp).collect(Collectors.toList())));
			report.setFiveKeyRoles(completeListStr(list.stream().map(QualityMonthlyReport::getKeyRoles).collect(Collectors.toList())));
			report.setFivePlanLamp(completeListStr(list.stream().map(QualityMonthlyReport::getPlanLamp).collect(Collectors.toList())));
			report.setFiveScopeLamp(completeListStr(list.stream().map(QualityMonthlyReport::getScopeLamp).collect(Collectors.toList())));
			report.setFiveStatusAssessment(completeListStr(list.stream().map(QualityMonthlyReport::getStatusAssessment).collect(Collectors.toList())));
		}

//		ret.setRows(qualityMonthlyReports);
//		ret.setPage(pageRequest.getPageNumber());
//		ret.setTotal(total);
		ret.setRows(qualityMonthlyReports);
		return ret;
	}

	/**
	 * 近五次点评
	 *
	 * @param projectInfo
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "null" })
	public TableSplitResult getProjectAccessByNo(ProjectInfo projectInfo) {
		TableSplitResult ret = new TableSplitResult();

		List<String> proNos = new ArrayList<>();
		proNos.add(projectInfo.getNo());

		ret.setTotal(proNos.size());
		List<QualityMonthlyReport> qualityMonthlyReports = projectReportDao.getProjectAccess(
				"(" + StringUtilsLocal.listToSqlIn(proNos) + ")",null);

		List<String> queryedNos =  qualityMonthlyReports.stream().map(QualityMonthlyReport::getNo).collect(Collectors.toList());

		//查询近五次结果
		List<QualityMonthlyReport> currentResult = projectReportDao.getProjectAccessCurrent(
				"(" + StringUtilsLocal.listToSqlIn(queryedNos) + ")");

		Map<String, List<QualityMonthlyReport>> resultMaps = currentResult.stream()
				.collect(Collectors.groupingBy(QualityMonthlyReport::getNo));

		for(QualityMonthlyReport report : qualityMonthlyReports){
			List<QualityMonthlyReport> list = resultMaps.get(report.getNo());
			report.setFiveQualityLamp(completeListStr(list.stream().map(QualityMonthlyReport::getQualityLamp).collect(Collectors.toList())));
			report.setFiveKeyRoles(completeListStr(list.stream().map(QualityMonthlyReport::getKeyRoles).collect(Collectors.toList())));
			report.setFivePlanLamp(completeListStr(list.stream().map(QualityMonthlyReport::getPlanLamp).collect(Collectors.toList())));
			report.setFiveScopeLamp(completeListStr(list.stream().map(QualityMonthlyReport::getScopeLamp).collect(Collectors.toList())));
			report.setFiveStatusAssessment(completeListStr(list.stream().map(QualityMonthlyReport::getStatusAssessment).collect(Collectors.toList())));
		}

		ret.setRows(qualityMonthlyReports);
		return ret;
	}

	private List<String> completeListStr(List<String> list){
		for(int i = null == list? 0:list.size();i < 5 ;i++){
			list.add(null);
		}
		return list;
	}

	private List<ProjectReviewVo> oldWeekReview(String time, QualityMonthlyReport qualityMonthlyReport,
												SimpleDateFormat sdt, String knotProjectDate, Date startTime) throws ParseException {
		List<String> dateList = DateUtils.getLatestCycles(1, false);
		List<ProjectReviewVo> projectReview = null;
		if (StringUtils.isNotBlank(time) && StringUtils.isNotBlank(dateList.get(0)) && startTime != null) {
			projectReview = projectReviewService.queryOldWeekReview(qualityMonthlyReport.getNo(), time);

			if (StringUtils.isNotBlank(knotProjectDate)) {
				if (sdt.parse(knotProjectDate).before(sdt.parse(time)) || sdt.parse(time).before(startTime)) {
					filterAssessmentCycle(projectReview);
				}
			} else {
				if (sdt.parse(time).before(startTime)) {
					filterAssessmentCycle(projectReview);
				}
			}

		}
		return projectReview;
	}

	private void filterAssessmentCycle(List<ProjectReviewVo> projectReview) {
		if (null != projectReview && projectReview.size() > 0) {
			for (ProjectReviewVo projectReviewVo : projectReview) {
				projectReviewVo.setQualityLamp("-1");
				projectReviewVo.setResourcesLamp("-1");
				projectReviewVo.setProgressLamp("-1");
				projectReviewVo.setChangesLamp("-1");
				projectReviewVo.setProjectStatus("-1");
				projectReviewVo.setProjectReview("-1");
			}
		} else {
			ProjectReviewVo projectReviewVo = new ProjectReviewVo();
			projectReviewVo.setQualityLamp("-1");
			projectReviewVo.setResourcesLamp("-1");
			projectReviewVo.setProgressLamp("-1");
			projectReviewVo.setChangesLamp("-1");
			projectReviewVo.setProjectStatus("-1");
			projectReviewVo.setProjectReview("-1");
			projectReview.add(projectReviewVo);
		}
	}

	/**
	 * 总览Top项目
	 *
	 * @param projectInfo
	 * @param pageRequest
	 * @param statisticalTime
	 * @param projectStatus
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TableSplitResult getZongLanTopProject(ProjectInfo projectInfo, PageRequest pageRequest,
												 String statisticalTime, String projectStatus) throws ParseException {
		TableSplitResult ret = new TableSplitResult();
		String lastWeek = statisticalTime;
		/* 2-连续2周风险，3-连续2周预警 */
		if ("2".equals(projectStatus) || "3".equals(projectStatus)) {
			List<String> dateList = DateUtils.getLatestWeek(1, false, statisticalTime);
			lastWeek = dateList.get(0);
		}

		Map<String, Object> maps = new HashMap<>();
		projectInfoService.setParamNew(projectInfo, null, maps);
		maps.put("statisticalTime", statisticalTime);
		maps.put("projectStatus", projectStatus);
		maps.put("lastWeek", lastWeek);
		com.github.pagehelper.Page page = PageHelper.startPage(
				(pageRequest.getPageNumber() == null ? 0 : (pageRequest.getPageNumber() - 1)) + 1,
				pageRequest.getPageSize());
		List<QualityMonthlyReport> topProjects = projectReportDao.getZongLanTopProject(maps);
		ret.setRows(topProjects);
		ret.setTotal((int) page.getTotal());
		ret.setPage(pageRequest.getPageNumber());
		return ret;
	}

	/**
	 * 质量Top项目
	 *
	 * @param projectInfo
	 * @param pageRequest
	 * @param measure
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TableSplitResult getZhiLiangTopProject(ProjectInfo projectInfo, PageRequest pageRequest, String measure,
												  String measureMark) {
		TableSplitResult ret = new TableSplitResult();

		List<String> dateList = DateUtils.getLatestCycles(2, false);
		String endDate = dateList.get(0);
		if ("164".equals(measureMark) && Integer.valueOf(endDate.split("-")[2]) == 15) {
			endDate = dateList.get(1);
		}
		Map<String, Object> maps = new HashMap<>();
		projectInfoService.setParamNew(projectInfo, null, maps);
		maps.put("endDate", endDate);
		maps.put("measure", measure);
		maps.put("redMeasure", "red");
		maps.put("yellowMeasure", "yellow");
		maps.put("measureMark", measureMark);
		com.github.pagehelper.Page page = PageHelper.startPage(
				(pageRequest.getPageNumber() == null ? 0 : (pageRequest.getPageNumber() - 1)) + 1,
				pageRequest.getPageSize());

		List<MeasureVo> topProjects = projectReportDao.getZhiLiangTopProject(maps);

		ret.setRows(topProjects);
		ret.setPage(pageRequest.getPageNumber());
		ret.setTotal((int) page.getTotal());
		return ret;
	}

	public String getTime() {
		return DateUtils.getLatestCycles(1, false).get(0);
	}

	public TableSplitResult<List<Map<String, Object>>> queryProjectQualityReport(ProjectInfo projectInfo,
																				 PageRequest pageRequest, String date, String mark,String sortOrder,String sort,boolean flag) {
		TableSplitResult<List<Map<String, Object>>> result = new TableSplitResult<List<Map<String, Object>>>();
		Set<String> nos = projectInfoService.projectNos(projectInfo, null);
		List<String> proNos = new ArrayList<>(nos);
		String statisticalCycle = date;
		/*List<String> proNos = new ArrayList<>();
		if ("164".equals(mark)) {
			List<String> pnos = projectReviewDao.isKxProject();
			if (pnos != null && pnos.size() > 0) {
				for (String t : temp) {
					if (pnos.contains(t)) {
						proNos.add(t);
					}
				}
			}
		} else {
			proNos = temp;
		}*/
		if (proNos.size() <= 0) {
			result.setErr(new ArrayList<>(), 1);
		}
		List<String> measureIds = null;
		if ("163".equals(mark)) {
			measureIds = new ArrayList<>();
			measureIds.add("223");// 静态检查告警数
			measureIds.add("309");// 代码重复率（JAVA）
			measureIds.add("311");// 代码重复率（C/C++）
			measureIds.add("337");// 平均圈复杂度
			measureIds.add("385");// 危险函数
			measureIds.add("307");// CodeDEX（Coverity）
			measureIds.add("308");// CodeDEX（Fortify）
			measureIds.add("387");// SAI
		} else if ("164".equals(mark)) {
			measureIds = projectReportDao.getCredibleMeasureIds("630");
		} else if ("162".equals(mark)) {
			measureIds = Arrays.asList("116", "117", "643", "118", "120", "140", "141", "275", "313", "654", "655",
					"656", "657", "751");
		}
		List<String> dateList = DateUtils.getLatestCycles(2, true);
		boolean bool = false;
		try {
			bool = DateUtils.isLastDayOfMonth(DateUtils.SHORT_FORMAT_GENERAL.parse(dateList.get(0)));
		} catch (ParseException e) {
			logger.error("acquire isLastDayOfMonth failed:" + e.getMessage());
		}
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);

		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("mark", mark);
		parameterMap.put("coopType", projectInfo.getCoopType());
		parameterMap.put("username", username);
		parameterMap.put("statisticalCycle", statisticalCycle);
		parameterMap.put("sort", sort);
		parameterMap.put("sortOrder", sortOrder);
		parameterMap.put("flag", flag);

		if (date.equals(dateList.get(0)) || ("164".equals(mark) && bool && date.equals(dateList.get(1)))) {
			boolean kexinHistory = false;
			if ("164".equals(mark) && date.equals(dateList.get(1))) {
				date = DateUtils.getNextCycle(date);
				kexinHistory = true;
			}
			parameterMap.put("date", date);
			result = getCurrentReport(pageRequest,parameterMap,proNos,measureIds,kexinHistory);
		} else {
			if ("164".equals(mark)) {
				String[] dateArr = date.split("-");
				if (Integer.valueOf(dateArr[2]) == 15) {
					date = DateUtils.getNextCycle(date);
				}
			}
			parameterMap.put("date", date);
			result = getHistoryReport(pageRequest,parameterMap,proNos, measureIds);
		}

		return result;
	}

	private TableSplitResult<List<Map<String, Object>>> getHistoryReport(PageRequest pageRequest,Map<String, Object> parameterMap,
																		 List<String> proNos, List<String> measureIds) {
		TableSplitResult<List<Map<String, Object>>> result = new TableSplitResult<List<Map<String, Object>>>();
		parameterMap.put("proNos", proNos);
		parameterMap.put("measureIds", measureIds);
//		int total = projectReportDao.queryQualityTrendCount(parameterMap);
//		if (total <= 0) {
//			result.setErr(new ArrayList<>(), 1);
//		} else {
//			pageRequest.setOffset(0);

//		parameterMap.put("page", pageRequest);

		List<String> proNoList = projectReportDao.queryProNoList(parameterMap);

//			pageRequest.setPageNumber(pageRequest.getPageNumber()/pageRequest.getPageSize());
//			Page page = PageHelper.startPage((pageRequest.getPageNumber()) + 1, pageRequest.getPageSize());
		if(proNoList.isEmpty()||measureIds.isEmpty()) {
			return result;
		}
		parameterMap.put("proNoList", proNoList);
		List<ReportQualityEntity> qualityTrends = projectReportDao.queryQualityTrendList(parameterMap);

//			result.setTotal((int) page.getTotal());

		//			Map<String, Map<String, Object>> rets = new HashMap<String, Map<String, Object>>();
//			for (Map<String, Object> map : qualityTrends) {
//				String proNo = StringUtilsLocal.valueOf(map.get("proNo"));
//				if (rets.containsKey(proNo)) {
//					/*if ("163".equals(mark) || "162".equals(mark)) {
//						rets.get(proNo).put("id" + StringUtilsLocal.valueOf(map.get("measure_id")),
//								StringUtilsLocal.valueOf(map.get("measure_value")));
//					} else if ("164".equals(mark)) {
//						rets.get(proNo).put("id" + StringUtilsLocal.valueOf(map.get("VERSION")),
//								StringUtilsLocal.valueOf(map.get("measure_value")));
//					}*/
//					rets.get(proNo).put("id" + StringUtilsLocal.valueOf(map.get("measure_id")),
//							StringUtilsLocal.valueOf(map.get("measure_value")));
//				} else {
//					Map<String, Object> ret = new HashMap<>();
//					ret.put("proNo", proNo);
//					ret.put("name", StringUtilsLocal.valueOf(map.get("name")));
//					ret.put("pm", StringUtilsLocal.valueOf(map.get("pm")).replaceAll("[^\u4e00-\u9fa5]", ""));
//					ret.put("collection", StringUtilsLocal.valueOf(map.get("collection")));
//					String department = "";
//					if (StringUtils.isNotBlank(StringUtilsLocal.valueOf(map.get("hwzpdu")))) {
//						department = StringUtilsLocal.valueOf(map.get("hwzpdu")) + "/"
//								+ StringUtilsLocal.valueOf(map.get("pduSpdt"));
//					} else {
//						department = StringUtilsLocal.valueOf(map.get("pdu")) + "/" + StringUtilsLocal.valueOf(map.get("du"));
//					}
//					ret.put("department", department);
//					ret.put("projectType", StringUtilsLocal.valueOf(map.get("projectType")));
//
////					Map<String, Object> startStopTime = projectOverviewDao.getStartStopTime(proNo);
//
//					if (null != map.get("START_DATE")) {
//						String startTime = StringUtilsLocal.valueOf(map.get("START_DATE"));
//						List<String> totalCycle = getStatisticalCycle(statisticalCycle, ret, startTime);
//
//						if (null != totalCycle && totalCycle.size() > 0) {
//							ret.put("statisticalCycle", totalCycle.size() + 1);
//						}
//					}
//
//					/*if ("163".equals(mark) || "162".equals(mark)) {
//						ret.put("id" + StringUtilsLocal.valueOf(map.get("measure_id")),
//								StringUtilsLocal.valueOf(map.get("measure_value")));
//					} else if ("164".equals(mark)) {
//						ret.put("id" + StringUtilsLocal.valueOf(map.get("VERSION")),
//								StringUtilsLocal.valueOf(map.get("measure_value")));
//					}*/
//					ret.put("id" + StringUtilsLocal.valueOf(map.get("measure_id")),
//							StringUtilsLocal.valueOf(map.get("measure_value")));
//					rets.put(proNo, ret);
//				}
//			}

		List<Map<String, Object>> rets = new ArrayList<>();
		for (ReportQualityEntity map : qualityTrends) {

			Map<String, Object> ret = new HashMap<>();
			ret.put("proNo",map.getProNo());
			ret.put("name", map.getName());
			ret.put("pm", map.getPm().replaceAll("[^\u4e00-\u9fa5]", ""));
			ret.put("collection", map.getCollection());
			ret.put("projectType", map.getProjectType());
			List<MeasureVo> measureVoList = map.getMeasureVoList();

			if (null != map.getStartDate()) {
				String startTime = DateUtils.formatDate(DateUtils.STANDARD_FORMAT_GENERAL, map.getStartDate());
				List<String> totalCycle = getStatisticalCycle(
                        StringUtilsLocal.valueOf(parameterMap.get("statisticalCycle")), ret, startTime);

				if (null != totalCycle && totalCycle.size() > 0) {
					ret.put("statisticalCycle", totalCycle.size() + 1);
				}
			}
			String department = "";
			if (StringUtils.isNotBlank(map.getHwzpdu())) {
				department = map.getHwzpdu() + "/" + map.getPduSpdt();
			} else {
				department = map.getPdu() + "/" + map.getDu();
			}
			ret.put("department", department);

			for (MeasureVo measureVo : measureVoList) {
				Map<String,String> valueMap = new HashMap<>();
				valueMap.put("value", measureVo.getValue());
				valueMap.put("light", measureVo.getColor());
				ret.put("id" + StringUtilsLocal.valueOf(measureVo.getId()),valueMap);
			}

			rets.add(ret);
		}
		result.setRows(rets);
//		result.setTotal(total);
//		}
		return result;
	}

	private TableSplitResult<List<Map<String, Object>>> getCurrentReport(PageRequest pageRequest,Map<String, Object> parameterMap,
																		 List<String> proNos,List<String> measureIds,boolean kexinHistory) {
		TableSplitResult<List<Map<String, Object>>> result = new TableSplitResult<List<Map<String, Object>>>();
//		int total = 0;
		String mark = StringUtilsLocal.valueOf(parameterMap.get("mark"));
		String date = StringUtilsLocal.valueOf(parameterMap.get("date"));
		parameterMap.put("proNos", proNos);
		parameterMap.put("id", Integer.parseInt(mark));
//		parameterMap.put("page", pageRequest);
//		if (StringUtils.isNotBlank(mark)) {
//			total = projectReportDao.queryQualityProjectsCount(parameterMap);
//		}
//		if (total <= 0) {
//			result.setErr(new ArrayList<>(), 1);
//		} else {
		List<QualityMonthlyReport> qualityMonthlyReports = null;
//		pageRequest.setPageNumber(pageRequest.getOffset()/pageRequest.getPageSize());
//		Page page = PageHelper.startPage((pageRequest.getPageNumber()) + 1, pageRequest.getPageSize());
		if (StringUtils.isNotBlank(mark)) {
			qualityMonthlyReports = projectReportDao.queryQualityProjects(parameterMap);
		}
//		result.setTotal((int) page.getTotal());
		List<Map<String, Object>> rets = new ArrayList<>();

		for (QualityMonthlyReport project : qualityMonthlyReports) {
			String proNo = project.getNo();
			String measures = measureCommentService.measureConfigurationRecord(proNo, date);
			if (StringUtils.isNotBlank(measures)) {
				measures = measures.replaceAll(" ", "").replaceAll("303", "223").replaceAll("305", "223")
						.replaceAll("306", "223");
			}
			List<String> measureIdList = CollectionUtilsLocal.splitToList(measures);
			measureIdList.retainAll(measureIds);

			Map<String, Object> ret = new HashMap<>();
			ret.put("proNo", proNo);
			ret.put("name", project.getName());
			ret.put("collection", project.getPeopleLamp());
			ret.put("pm", project.getPm().replaceAll("[^\u4e00-\u9fa5]", ""));
			String department = "";
			if (StringUtils.isNotBlank(project.getHwzpdu())) {
				department = project.getHwzpdu() + "/" + project.getPduSpdt();
			} else {
				department = project.getPdu() + "/" + project.getDu();
			}
			ret.put("department", department);
			ret.put("projectType", project.getProjectType());

//				Map<String, Object> startStopTime = projectOverviewDao.getStartStopTime(proNo);

			if (null != project.getStartDate()) {
				List<String> totalCycle = getStatisticalCycle(date, ret,
						DateUtils.SHORT_FORMAT_GENERAL.format(project.getStartDate()));

				if (null != totalCycle && totalCycle.size() > 0) {
					if (kexinHistory) {
						ret.put("statisticalCycle", totalCycle.size());
					} else {
						ret.put("statisticalCycle", totalCycle.size() + 1);
					}
				}
			}

			List<Map<String, Object>> list = new ArrayList<>();
			if (null != measureIdList && measureIdList.size() > 0) {
				list = projectReportDao.getProjectMeasureIndex(proNo,
						"(" + StringUtilsLocal.listToSqlIn(measureIdList) + ")", DateUtils.getThisClcyeStart(date),
						date + " 23:59:59");
			}
			if (null != list && list.size() > 0) {
				for (Map<String, Object> map : list) {
					MeasureComment measureComment = new MeasureComment();
					measureComment.setMeasureId(Integer.parseInt(StringUtilsLocal.valueOf(map.get("measure_id"))));
					measureComment.setMeasureValue(StringUtilsLocal.valueOf(map.get("measure_value")));
					measureComment.setChallenge(StringUtilsLocal.valueOf(map.get("challenge")));
					measureComment.setUpper(StringUtilsLocal.valueOf(map.get("upper")));
					measureComment.setLower(StringUtilsLocal.valueOf(map.get("lower")));
					measureComment.setTarget(StringUtilsLocal.valueOf(map.get("target")));
					measureComment.setComputeRule(StringUtilsLocal.valueOf(map.get("computeRule")));
					String light = "";
					int changeValue = map.get("change_value")==null ? -1: (int)map.get("change_value");
					//1:正常 2：预警 3：超标 4：实际
					if(changeValue==1) {
						light = "green";
					}else if(changeValue==2) {
						light = "yellow";
					}else if(changeValue==3) {
						light = "red";
					}else {
						light = MeasureUtils.light(measureComment);
					}
//					if ("163".equals(mark) || "162".equals(mark)) {
//						ret.put("id" + StringUtilsLocal.valueOf(map.get("measure_id")),
//								StringUtilsLocal.valueOf(map.get("measure_value")));
//					} else if ("164".equals(mark)) {
//						ret.put("id" + StringUtilsLocal.valueOf(map.get("VERSION")),
//								StringUtilsLocal.valueOf(map.get("measure_value")));
//					}
					Map<String,String> valueMap = new HashMap<>();
					valueMap.put("value", StringUtilsLocal.valueOf(map.get("measure_value")));
					valueMap.put("light", light);
					ret.put("id" + StringUtilsLocal.valueOf(map.get("measure_id")),valueMap);
				}
			}
			rets.add(ret);
		}
		result.setRows(rets);
//		}
		return result;
	}

	private List<String> getStatisticalCycle(String date, Map<String, Object> ret, String startTime) {
		String lastCycle = DateUtils.getLatestCycles(1, false).get(0);
		List<String> totalCycle = null;
		try {
			Date startDate = DateUtils.SHORT_FORMAT_GENERAL.parse(startTime);

			if (startDate.after(DateUtils.SHORT_FORMAT_GENERAL.parse(lastCycle))
					&& !startDate.after(DateUtils.SHORT_FORMAT_GENERAL.parse(date))) {
				ret.put("statisticalCycle", 1);
			} else {
				if (DateUtils.isLastDayOfMonth(startDate)
						|| Integer.valueOf(DateUtils.SHORT_FORMAT_GENERAL.format(startDate).split("-")[2]) == 15) {
					startTime = DateUtils.getAroundDay(startDate, -1);
				}
				totalCycle = DateUtils.getInHoursCycles(startTime, date);
			}
		} catch (ParseException e) {
			logger.error("acquire statisticalCycle failed:" + e.getMessage());
		}
		return totalCycle;
	}

	@SuppressWarnings("rawtypes")
	public TableSplitResult<List<ReportProblemAnalysis>> queryProblemAnalysis(ProjectInfo projectInfo,
																			  PageRequest pageRequest, String date) throws ParseException {
		TableSplitResult<List<ReportProblemAnalysis>> result = new TableSplitResult<List<ReportProblemAnalysis>>();
		Map<String, Object> maps = new HashMap<>();
		projectInfoService.setParamNew(projectInfo, null, maps);

		if (null == maps.get("pm") || "" == maps.get("pm")) {
			return new TableSplitResult<>();
		}

		maps.put("endDate", date);
		String startDate = DateUtils.getThisClcyeStart(date);
		maps.put("startDate", startDate);
		com.github.pagehelper.Page page = PageHelper.startPage(
				(pageRequest.getPageNumber() == null ? 0 : (pageRequest.getPageNumber() - 1)) + 1,
				pageRequest.getPageSize());
		List<ReportProblemAnalysis> list = projectReportDao.queryProblemAnalysis(maps);
		result.setTotal((int) page.getTotal());
		for (int i = 0; i < list.size(); i++) {
			ReportProblemAnalysis report = list.get(i);
			if ("0".equals(projectInfo.getClientType())) {
				report.setDepartment(report.getHwzpdu() + "/" + report.getPduSpdt());
			} else {
				report.setDepartment(report.getPdu() + "/" + report.getDu());
			}
			ReportProblemAnalysis problem = projectReportDao.reportProblemAnalysis(report.getNo(), startDate, date);
			report.setAarDelay(problem.getAarDelay());
			report.setProblemDelay(problem.getProblemDelay());
			report.setAuditDelay(problem.getAuditDelay());
			report.setBackDelay(problem.getBackDelay());
			report.setDelay(problem.getAarDelay() + problem.getProblemDelay() + problem.getAuditDelay()
					+ problem.getBackDelay());
			int sum = problem.getAar() + problem.getProblem() + problem.getAudit() + problem.getBack();
			double closed = 100 * (problem.getAarClosed() + problem.getAuditClosed() + problem.getBackClosed()
					+ problem.getProblemClosed());
			report.setClosed(BigDecUtils.division(closed, sum, 2) + "%");
			report.setAarClosed(Math.round(BigDecUtils.division(problem.getAarClosed() * 100, problem.getAar(), 2)));
			report.setAuditClosed(
					Math.round(BigDecUtils.division(problem.getAuditClosed() * 100, problem.getAudit(), 2)));
			report.setBackClosed(Math.round(BigDecUtils.division(problem.getBackClosed() * 100, problem.getBack(), 2)));
			report.setProblemClosed(
					Math.round(BigDecUtils.division(problem.getProblemClosed() * 100, problem.getProblem(), 2)));
		}
		result.setRows(list);
		return result;
	}

	/**
	 * 资源报表
	 *
	 * @param projectInfo
	 * @param pageRequest
	 * @param date
	 * @return
	 */
	public TableSplitResult<List<MemberResourceReport>> queryResourceReport(ProjectInfo projectInfo,
																			PageRequest pageRequest, String date) {
		TableSplitResult<List<MemberResourceReport>> result = new TableSplitResult<List<MemberResourceReport>>();
		Set<String> nos = projectInfoService.projectNos(projectInfo, null);
		List<String> proNos = new ArrayList<>(nos);
		if (proNos.size() <= 0) {
			result.setErr(new ArrayList<>(), 1);
		}

		String lastWeek = DateUtils.getLatestWeek(1, false, date).get(0);
		String currentCycle = DateUtils.getLatestCycles(1, true).get(0);
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		int total = projectReportDao.queryResourceReportCount("(" + StringUtilsLocal.listToSqlIn(proNos) + ")", 162, date,
				username, projectInfo.getCoopType());
		if (total <= 0) {
			result.setErr(new ArrayList<>(), 1);
		} else {
			List<MemberResourceReport> memberResourceReport = null;

			if (date.equals(currentCycle)) {
				memberResourceReport = projectReportDao.queryCurrentResourceReport(
						"(" + StringUtilsLocal.listToSqlIn(proNos) + ")", 162, date, lastWeek, pageRequest, username,
						projectInfo.getCoopType());

				getResourceReport(memberResourceReport);

				for (MemberResourceReport resourceReport : memberResourceReport) {
					String stabilityScore = projectReportDao.queryProjectStabilityScore(resourceReport.getNo(), date,
							"resources", "1");
					if (StringUtils.isNotBlank(stabilityScore)) {
						resourceReport.setPersonnelStability(stabilityScore);
					} else {
						double arrivalVal = resourceReport.getArrival();
						double keyArrivalVal = resourceReport.getKeyArrival();

						if (keyArrivalVal == -1.0) {
							keyArrivalVal = 0.0;
						} else if (keyArrivalVal > 1.0) {
							keyArrivalVal = 1.0;
						}
						if (arrivalVal == -1.0) {
							arrivalVal = 0.0;
						} else if (arrivalVal > 1.0) {
							arrivalVal = 1.0;
						}
						DecimalFormat df = new DecimalFormat("##0.00");
						resourceReport
								.setPersonnelStability(df.format(keyArrivalVal * 15 + arrivalVal * 10).toString());
					}
				}
			} else {
				memberResourceReport = projectReportDao.queryHistoryResourceReport(
						"(" + StringUtilsLocal.listToSqlIn(proNos) + ")", 162, date, lastWeek, pageRequest, username,
						projectInfo.getCoopType());
				getResourceReport(memberResourceReport);
			}
			// 361人员稳定度
			for (MemberResourceReport resourceReport : memberResourceReport) {
				try {
					String proNo = resourceReport.getNo();
					String template = projectListService.getTemplateValue(proNo);
					if (StringUtils.isBlank(template)) {
						Map<String, Object> data = buOverviewService.getProjOverveiwData(proNo);
						if ("FP".equals(data.get("type"))) {
							template = "2";
						} else {
							template = "1";
						}
					}
					String manualValue = projectReviewService.getProjectManualValue(proNo, date, template);
					if (template == "1") {
						manualValue = StringUtilsLocal.keepTwoDecimals(Double.parseDouble(manualValue) / 40 * 25).toString();
					} else {
						manualValue = StringUtilsLocal.keepTwoDecimals(Double.parseDouble(manualValue) / 30 * 25).toString();
					}
					resourceReport.setPersonnelStability361(manualValue);
				} catch (Exception e) {
					logger.error("resourceReport.setPersonnelStability361 exception, error: "+e.getMessage());
				}
			}
			result.setRows(memberResourceReport);
			result.setTotal(total);
		}

		return result;
	}

	private void getResourceReport(List<MemberResourceReport> memberResourceReport) {
		for (MemberResourceReport resourceReport : memberResourceReport) {
			String department = "";
			if (StringUtils.isNotBlank(resourceReport.getHwzpdu())) {
				department = resourceReport.getHwzpdu() + "/" + resourceReport.getPduSpdt();
			} else {
				department = resourceReport.getPdu() + "/" + resourceReport.getDu();
			}
			resourceReport.setDepartment(department);
			resourceReport.setPm(StringUtils.isNotBlank(resourceReport.getPm())
					? resourceReport.getPm().replaceAll("[^\u4e00-\u9fa5]", "") : "");

			// 关键角色到位率
			double keyArrival = resourceReport.getKeyManpowerDemand() > 0
					? (double) (resourceReport.getKeyOnduty() + resourceReport.getKeyReserve())
					/ resourceReport.getKeyManpowerDemand()
					: -1.0;
			resourceReport.setKeyArrival(keyArrival);
			resourceReport.setKeyArrivalRate(getResource(keyArrival));

			// 关键角色人力缺口
			resourceReport.setKeyManpowerGap(
					resourceReport.getKeyManpowerDemand() > 0 && resourceReport.getKeyManpowerDemand()
							- (resourceReport.getKeyOnduty() + resourceReport.getKeyReserve()) >= 0
							? resourceReport.getKeyManpowerDemand()
							- (resourceReport.getKeyOnduty() + resourceReport.getKeyReserve())
							: -1);

			// 关键角色离职率
			double keyTurnover = resourceReport.getKeyManpowerDemand() > 0 && resourceReport.getKeyTurnoverPerson() > 0
					? (double) resourceReport.getKeyTurnoverPerson() / resourceReport.getKeyManpowerDemand() : -1.0;
			resourceReport.setKeyTurnoverRate(getResource(keyTurnover));

			// 全员到位率
			double arrival = resourceReport.getManpowerDemand() > 0
					? (double) (resourceReport.getOnduty() + resourceReport.getReserve())
					/ resourceReport.getManpowerDemand()
					: -1.0;
			resourceReport.setArrival(arrival);
			resourceReport.setArrivalRate(getResource(arrival));

			// 全员人力缺口
			resourceReport.setManpowerGap(resourceReport.getManpowerDemand() > 0 && resourceReport.getManpowerDemand()
					- (resourceReport.getOnduty() + resourceReport.getReserve()) >= 0
					? resourceReport.getManpowerDemand()
					- (resourceReport.getOnduty() + resourceReport.getReserve())
					: -1);

			// 全员离职率
			double turnover = resourceReport.getManpowerDemand() > 0 && resourceReport.getTurnoverPerson() > 0
					? (double) resourceReport.getTurnoverPerson() / resourceReport.getManpowerDemand() : -1.0;
			resourceReport.setTurnoverRate(getResource(turnover));
		}
	}

	private String getResource(double data) {
		NumberFormat format = NumberFormat.getPercentInstance();
		format.setMinimumFractionDigits(2);

		String dataRate = "";
		if (data >= 1) {
			dataRate = "100.00%";
		} else if (data == -1.0) {
			dataRate = "-";
		} else {
			dataRate = format.format(data);
		}
		return dataRate;

	}

	public TableSplitResult<List<Map<String, Object>>> queryProjectDevtestReport(ProjectInfo projectInfo, String date,
																				 PageRequest pageRequest) {
		TableSplitResult<List<Map<String, Object>>> result = new TableSplitResult<List<Map<String, Object>>>();
		Set<String> nos = projectInfoService.projectNos(projectInfo, null);
		List<String> proNos = new ArrayList<>(nos);
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		if (proNos.size() <= 0) {
			result.setErr(new ArrayList<>(), 1);
		} else {
			String[] ids = new String[] { "267", "266", "254", "255", "256", "257", "258", "259" };
			int total = projectReportDao.queryEfficiencyProjectsCount(proNos, ids, date, username,
					projectInfo.getCoopType());
			if (total <= 0) {
				result.setErr(new ArrayList<>(), 1);
			} else {
				List<ProjectInfo> proList = projectReportDao.queryEfficiencyProjectList(proNos, ids, date, pageRequest,
						username, projectInfo.getCoopType());
				List<Map<String, Object>> rets = new ArrayList<>();

				for (ProjectInfo project : proList) {
					Map<String, Object> ret = new HashMap<>();
					String proNo = project.getNo();
					ret.put("proNo", proNo);
					ret.put("name", project.getName());
					ret.put("collection", project.getCoopType());
					ret.put("pm", project.getPm().replaceAll("[^\u4e00-\u9fa5]", ""));
					String department = "";
					if (StringUtils.isNotBlank(project.getHwzpdu())) {
						department = project.getHwzpdu() + "/" + project.getPduSpdt();
					} else {
						department = project.getPdu() + "/" + project.getDu();
					}
					ret.put("department", department);
					ret.put("projectType", project.getProjectType());

					List<Map<String, Object>> list = projectReportDao.getEfficiencyMeasureIndex(proNo, ids,
							DateUtils.getThisClcyeStart(date), date);
					for (Map<String, Object> map : list) {
						ret.put("id" + StringUtilsLocal.valueOf(map.get("measure_id")),
								StringUtilsLocal.valueOf(map.get("measure_value")));
					}
					rets.add(ret);
				}
				result.setRows(rets);
				result.setTotal(total);
			}
		}
		return result;
	}

	@SuppressWarnings("null")
	public ProjectReviewEntity measureValueLine(String projectId, String id, String measureMark, String nowDate)
			throws ParseException {
		ProjectReviewEntity data = new ProjectReviewEntity();
		List<String> dateList = DateUtils.getLatestWeek(7, true, nowDate);
		List<String> latestDate = DateUtils.getLatestCycles(2, true);
		// 获取当前周期的数据
		String measureValue = "";
		// 获取过往周期已经打了快照的数据
		List<MeasureComment> list = new ArrayList<>();
		MeasureComment measure = new MeasureComment();

		if ("163".equals(measureMark) || "reviewQuality".equals(measureMark)) {
			list = projectReviewDao.measureValueLineCode(projectId, id);
			measureValue = projectReviewDao.measureValueHistoryCode(DateUtils.getThisClcyeStart(latestDate.get(0)),
					latestDate.get(0), projectId, id);
		} else if ("164".equals(measureMark)) {
			// 可信往期数据
			int loopSize = 3;

			if (Integer.valueOf(nowDate.split("-")[1]) == Integer.valueOf(latestDate.get(0).split("-")[1])) {
				loopSize = 2;
			}
			for (int i = 0; i < loopSize; i++) {
				String date = "";

				if (DateUtils.isLastDayOfMonth(DateUtils.SHORT_FORMAT_GENERAL.parse(dateList.get(0)))) {
					date = dateList.get(4 - 2 * i);
				} else {
					date = DateUtils.getNextCycle(dateList.get(4 - 2 * i));
				}

				MeasureComment kexinMeasure = measureAchievementStatusDao.queryKexinMeasure(projectId, id, date);
				if (null != kexinMeasure) {
					list.add(kexinMeasure);
				} else {
					MeasureComment measureComment = new MeasureComment();
					measureComment.setMonth(date);
					measureComment.setMeasureValue("-");
					list.add(measureComment);
				}
			}

			// 可信当期数据
			if (DateUtils.isLastDayOfMonth(DateUtils.SHORT_FORMAT_GENERAL.parse(latestDate.get(0)))) {
				if (Integer.valueOf(nowDate.split("-")[2]) == 15 && nowDate.equals(latestDate.get(1))) {
					nowDate = DateUtils.getNextCycle(nowDate);
				}
			}

			if (loopSize == 2) {
				measureValue = projectReviewDao.measureValueHistory(DateUtils.getThisClcyeStart(nowDate), nowDate,
						projectId, id);
				measure.setMonth(nowDate);
				measure.setMeasureValue(StringUtils.isNotBlank(measureValue) ? measureValue : "-");
				list.add(measure);
			}
		}

		if (("163".equals(measureMark) || "reviewQuality".equals(measureMark))
				&& StringUtils.isNotBlank(measureValue)) {
			measure.setMonth(latestDate.get(0));
			measure.setMeasureValue(measureValue);
			list.add(measure);
		}

		List<String> listMonth = new ArrayList<>();
		List<String> listValue = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			MeasureComment map = list.get(i);
			String date = map.getMonth();

			if ("164".equals(measureMark)) {
				String[] dateArr = date.split("-");
				date = dateArr[0] + "-" + dateArr[1];
			}

			listMonth.add(date);
			// listValue.add(StringUtils.isNotBlank(map.getMeasureValue()) ?
			// StringUtilsLocal.formatValue(map.getMeasureValue())
			// : "-");

			Double doubleValue = MathUtils.parseDoubleSmooth(map.getMeasureValue(), null);
			listValue.add(doubleValue != null ? String.format("%.2f", doubleValue) : "-");

			if ("163".equals(measureMark) || "reviewQuality".equals(measureMark)) {
				if ((i + 1) < list.size()) {
					String statisticalTimeNext = list.get(i + 1).getMonth();
					double day = DateUtils.differenceTime(date, statisticalTimeNext);
					if (day > 20) {
						List<String> inHoursCycles = DateUtils.getInHoursCycles(date, statisticalTimeNext);
						for (String inHoursCycle : inHoursCycles) {
							listMonth.add(inHoursCycle);
							listValue.add("-");
						}
					}
				}
			}
		}
		data.setMonthList(listMonth);
		data.setValueList(listValue);
		return data;
	}

	public byte[] exportProjectIndex(String nos, String date) {
		List<String> projectids = CollectionUtilsLocal.splitToList(nos);
		List<String> months = DateUtils.getLatestWeek(6, true, date);
		InputStream is = this.getClass().getResourceAsStream("/excel/export-qualityReviewry.xlsx");
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(is);
		} catch (Exception e) {
			logger.error("导出失败", e);
		}
		// 内容页
		Sheet sheet = wb.getSheetAt(1);
		Row row = sheet.createRow(1);
		// 目录页
		Sheet sheet1 = wb.getSheetAt(0);
		Row row1 = sheet1.createRow(0);
		CellStyle cellStyle = exportService.getCellStyle(wb);

		CellStyle headerStyle = exportService.getCellStyleGreenTitle(wb);
		exportService.setCellValue(0, "项目名称", row, headerStyle);
		exportService.setCellValue(1, "项目经理", row, headerStyle);
		exportService.setCellValue(2, "指标名称", row, headerStyle);
		exportService.setCellValue(3, "质量目标", row, headerStyle);
		/************************ 获取表格头 ***************************/
		int num = 2;
		/********************* 获取项目表格信息 ************************/
		XSSFDrawing draw = (XSSFDrawing) sheet.createDrawingPatriarch();
		XSSFFont commentFormatter = (XSSFFont) wb.createFont();
		List<CellRangeAddress> merge = new ArrayList<>();
		for (int j = 0; j < 5; j++) {
			exportService.setCellValue((4 + j * 1), months.get(j), row, headerStyle);
		}
		List<ProjectInfo> catalog = new ArrayList<>();
		for (String no : projectids) {
			HashSet<String> names = new HashSet<>();
			ProjectInfo info = new ProjectInfo();
			List<MeasureComment> list = new ArrayList<>();
			for (int j = 0; j < 5; j++) {
				String month = months.get(j);
				ProjectInfo project = projectReviewDao.getProjectMeasureids(no, month);
				if (j == 0) {
					info.setName(project.getName());
					info.setArea(project.getArea());
					info.setPduSpdt(project.getPduSpdt());
					info.setPm(project.getPm());
					info.setType(project.getType());
				}
				Map<String, Object> parameter = new HashMap<>();
				parameter.put("proNo", no);
				parameter.put("startDate1", measureCommentService.getStartDate(month));
				parameter.put("endDate1", month);
				parameter.put("startDate2", measureCommentService.getStartDate(months.get(j + 1)));
				parameter.put("endDate2", months.get(j + 1));
				parameter.put("measureIds", "(" + project.getMeasureId() + ")");
				if (!StringUtilsLocal.isBlank(project.getMeasureId())) {
					boolean flag = false;
					if (month.equals(DateUtils.getLatestCycles(1, true).get(0))) {
						flag = true;
					}
					List<MeasureComment> measureComments = null;
					if (flag) {
						measureComments = measureCommentDao.queryCurrentCommentList(parameter);
					} else {
						String[] measureIds = project.getMeasureId().split(",");
						Set<String> warnings = new HashSet<>();
						Set<String> others = new HashSet<>();
						for (String measureId : measureIds) {
							if ("303".equals(measureId) || "305".equals(measureId) || "306".equals(measureId)) {
								warnings.add(measureId);
							} else {
								others.add(measureId);
							}
						}
						parameter.put("warnings", warnings);
						parameter.put("others", others);
						measureComments = measureCommentDao.queryHistoryCommentList(parameter);
					}
					for (MeasureComment measureComment : measureComments) {
						// CollectType点灯情况
						String measureColor = "";
						if(flag) {
							if ( measureComment.getChangeValue() != null && measureComment.getChangeValue() != 4 && measureComment.getChangeValue() != 0) {
								if (measureComment.getChangeValue() == 2) {
									measureColor = "yellow";
								} else if (measureComment.getChangeValue() == 3) {
									measureColor = "red";
								} else {
									measureColor = "green";
								}
							} else {
								measureColor = MeasureUtils.light(measureComment);
							}
						}else {
							measureColor = measureComment.getLight();
						}

						measureComment.setCollectType(measureColor);
						measureComment.setUnit("上限：" + measureComment.getUpper() + ",下限：" + measureComment.getLower()
								+ ",目标：" + measureComment.getTarget());
						measureComment.setId(j);
						names.add(measureComment.getMeasureName());
						list.add(measureComment);
					}
				}
			}
			int sum = num;
			for (String name : names) {
				row = sheet.createRow(num);
				exportService.setCellValue(0, info.getName(), row, cellStyle);
				exportService.setCellValue(1, info.getPm(), row, cellStyle);
				exportService.setCellValue(2, name, row, cellStyle);
				int s = 0;
				Boolean newest = true;
				for (MeasureComment measureComment : list) {
					if (name.equals(measureComment.getMeasureName())) {
						int a = measureComment.getId();
						for (int n = s; n < a; n++) {
							exportService.setCellValue((4 + n * 1), "-", row, cellStyle);
						}
						if (newest) {
							exportService.setCellValue(3, measureComment.getUnit(), row, cellStyle);
							newest = false;
						}
						Cell cell = row.createCell((4 + a * 1));
						// 判断点评信息是否为空，不为空实际值加批注
						if (!StringUtilsLocal.isBlank(measureComment.getComment())) {
							XSSFComment comment = draw
									.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
							// 输入批注信息
							XSSFRichTextString rtf = new XSSFRichTextString(measureComment.getComment());
							rtf.applyFont(commentFormatter);
							comment.setString(rtf);
							cell.setCellComment(comment);
						}
						cell.setCellValue(
								measureComment.getMeasureValue() == null ? "" : measureComment.getMeasureValue());
						cell.setCellStyle(exportService.getCellStyleColor(wb,
								measureComment.getCollectType() == null ? "" : measureComment.getCollectType()));
						s = a + 1;
					}
				}
				num++;
			}
			info.setNumber(sum + 1);
			catalog.add(info);
			if (list.size() > 0) {
				CellRangeAddress callRangeAddress = new CellRangeAddress(sum, num - 1, 0, 0);
				CellRangeAddress callRangeAddress1 = new CellRangeAddress(sum, num - 1, 1, 1);
				merge.add(callRangeAddress);
				merge.add(callRangeAddress1);
			} else {
				row = sheet.createRow(num);
				exportService.setCellValue(0, info.getName(), row, cellStyle);
				exportService.setCellValue(1, info.getPm(), row, cellStyle);
				num++;
			}
		}
		for (CellRangeAddress cellRangeAddress : merge) {
			sheet.addMergedRegion(cellRangeAddress);
		}
		int Jump = 1;
		row1 = sheet1.createRow(0);
		exportService.setCellValue(0, "ID", row1, headerStyle);
		exportService.setCellValue(1, "项目名称", row1, headerStyle);
		exportService.setCellValue(2, "地域", row1, headerStyle);
		exportService.setCellValue(3, "交付部", row1, headerStyle);
		exportService.setCellValue(4, "项目经理", row1, headerStyle);
		exportService.setCellValue(5, "运营商务模式", row1, headerStyle);
		CreationHelper createHelper = wb.getCreationHelper();
		for (ProjectInfo project : catalog) {
			row1 = sheet1.createRow(Jump);
			exportService.setCellValue(0, String.valueOf(Jump), row1, cellStyle);
			// 超链接
			Cell cell = row1.createCell(1);
			XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
			link.setAddress("Sheet2!A" + project.getNumber());
			cell.setHyperlink(link);
			cell.setCellValue(project.getName());
			cell.setCellStyle(cellStyle);
			// exportService.setCellValue(1, project.getName(), row1,
			// cellStyle);
			exportService.setCellValue(2, project.getArea(), row1, cellStyle);
			exportService.setCellValue(3, project.getPduSpdt(), row1, cellStyle);
			exportService.setCellValue(4, project.getPm(), row1, cellStyle);
			exportService.setCellValue(5, project.getType(), row1, cellStyle);
			Jump++;
		}
		return exportService.returnWb2Byte(wb);
	}

	/**
	 * 报表/质量/过程、结项指标项目信息
	 *
	 * @param projectInfo
	 * @param pageRequest
	 * @param statisticalTime
	 * @param status
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "null" })
	public TableSplitResult processNominalsTable(ProjectInfo projectInfo, PageRequest pageRequest,
												 String statisticalTime, String status, String category, String sort, String sortOrder) throws ParseException {
		TableSplitResult ret = new TableSplitResult();
		// 项目信息
		Set<String> nos = projectInfoService.projectNos(projectInfo, null);
		List<String> proNos = new ArrayList<>(nos);
		if (proNos.size() <= 0) {
			ret.setErr(new ArrayList<>(), pageRequest.getPageNumber());
			return ret;
		}
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		com.github.pagehelper.Page page = PageHelper
				.startPage((pageRequest.getOffset() == null ? 1 : pageRequest.getOffset()), pageRequest.getPageSize());
		List<QualityMonthlyReport> qualityMonthlyReports = projectReportDao.getProjectOverviews(
				"(" + StringUtilsLocal.listToSqlIn(proNos) + ")", statisticalTime, status, username,
				projectInfo.getCoopType(),sort,sortOrder);
		ret.setTotal((int) page.getTotal());
		for (QualityMonthlyReport qualityMonthlyReport : qualityMonthlyReports) {
			String pm = qualityMonthlyReport.getPm();
			if (StringUtils.isNotBlank(pm)) {
				qualityMonthlyReport.setPm(pm.replaceAll("[^\u4e00-\u9fa5]", ""));
			}
			if (StringUtils.isNotBlank(qualityMonthlyReport.getHwzpdu())) {
				qualityMonthlyReport
						.setDepartment(qualityMonthlyReport.getHwzpdu() + "/" + qualityMonthlyReport.getPduSpdt());
			} else {
				qualityMonthlyReport.setDepartment(qualityMonthlyReport.getPdu() + "/" + qualityMonthlyReport.getDu());
			}
			Map<String, Object> startStopTime = projectOverviewDao.getStartStopTime(qualityMonthlyReport.getNo());
			String knotProjectDate = projectOverviewDao.getKnotProjectDate(qualityMonthlyReport.getNo(), 1);
			List<String> firstFiveWeekDate = DateUtils.getLatestWeek(5, true, statisticalTime);
			List<String> fiveStatusAssessment = new ArrayList<>();
			SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
			Date startTime = null;
			try {
				if (null != startStopTime && startStopTime.size() > 0) {
					startTime = sdt.parse(StringUtilsLocal.valueOf(startStopTime.get("START_DATE")));
				}
			} catch (ParseException e) {
				logger.error("acquire mileagePoint failed: " + e.getMessage());
			}
			List<String> totalCycle = null;
			if (startTime.after(sdt.parse(firstFiveWeekDate.get(1))) && !startTime.after(sdt.parse(statisticalTime))) {
				qualityMonthlyReport.setStatisticalCycle(1);
			} else {
				String startDate = sdt.format(startTime);
				if (DateUtils.isLastDayOfMonth(startTime) || Integer.valueOf(startDate.split("-")[2]) == 15) {
					startDate = DateUtils.getAroundDay(startTime, -1);
				}
				totalCycle = DateUtils.getInHoursCycles(startDate, statisticalTime);
			}
			if (null != totalCycle && totalCycle.size() > 0) {
				qualityMonthlyReport.setStatisticalCycle(totalCycle.size() + 1);
			}
			for (int i = 0; i < 5; i++) {
				List<ProjectReviewVo> projectReviewData = oldWeekReview(firstFiveWeekDate.get(i), qualityMonthlyReport,
						sdt, knotProjectDate, startTime);
				if (null != projectReviewData && projectReviewData.size() > 0) {
					fiveStatusAssessment.add(projectReviewData.get(0).getProjectStatus());
				} else {
					fiveStatusAssessment.add(null);
				}
			}
			qualityMonthlyReport.setFiveStatusAssessment(fiveStatusAssessment);

			// 过程/结项指标名称
			Map<String, Object> parameter = new HashMap<>();
			List<Map<String, Object>> targetList = new ArrayList<>();
			Set<String> measureIdList = new HashSet();
			if (null != firstFiveWeekDate && firstFiveWeekDate.size() > 0) {
				for (int i = 0; i < firstFiveWeekDate.size(); i++) {
					String WeekDate = firstFiveWeekDate.get(i);
					List<String> queryMeasureIdList = queryMeasureIdList(qualityMonthlyReport.getNo(), WeekDate,
							category, parameter);
					measureIdList.addAll(queryMeasureIdList);
				}
			}
			if (null != measureIdList && measureIdList.size() > 0) {
				targetList = projectReportDao.getMeasureIds(measureIdList, category);
			}
			qualityMonthlyReport.setTargetList(targetList);
		}
		// Integer count = projectReportDao.getProjectOverviewCounts("(" +
		// StringUtilsLocal.listToSqlIn(proNos) + ")",statisticalTime, status);
		ret.setRows(qualityMonthlyReports);
		ret.setPage(pageRequest.getPageNumber());
		return ret;
	}

	/**
	 * 获取过程、结项指标id
	 *
	 * @param proNo
	 * @param WeekDate
	 * @param category
	 * @param parameter
	 * @return
	 */
	public List<String> queryMeasureIdList(String proNo, String WeekDate, String category,
										   Map<String, Object> parameter) {
		List<String> result = new ArrayList<>();
		try {
			parameter.put("proNo", proNo);
			if (null == WeekDate) {
				return result;
			}
			String endDate = "";
			if (WeekDate.indexOf(",") > 0) {
				String date1 = WeekDate.substring(0, WeekDate.indexOf(","));
				String date2 = WeekDate.substring(WeekDate.indexOf(",") + 1);
				parameter.put("startDate1", measureCommentService.getStartDate(date1));
				parameter.put("endDate1", date1);
				endDate = date1;
				parameter.put("startDate2", measureCommentService.getStartDate(date2));
				parameter.put("endDate2", date2);
			} else {
				parameter.put("startDate1", measureCommentService.getStartDate(WeekDate));
				parameter.put("endDate1", WeekDate);
				endDate = WeekDate;
			}
			String measureId = measureCommentService.measureConfigurationRecord(proNo, endDate);
			if (StringUtils.isNotBlank(measureId)) {
				measureId = measureId.replaceAll(" ", "");
			}
			result = CollectionUtilsLocal.splitToList(measureId);
		} catch (Exception e) {
			logger.error("查询失败", e);
		}
		return result;
	}

	/**
	 * 获取过程、结项指标值
	 *
	 * @param proNo
	 * @param statisticalTime
	 * @param category
	 * @return
	 */
	public List<Map<String, Object>> queryMeasureValueList(String proNo, String statisticalTime, String category) {
		List<Map<String, Object>> rets = new ArrayList<>();
		try {
			Map<String, Object> parameter = new HashMap<>();
			parameter.put("proNo", proNo);
			if (null == statisticalTime) {
				return rets;
			}
			List<String> firstFiveWeekDate = DateUtils.getLatestWeek(5, true, statisticalTime);
			if (null != firstFiveWeekDate) {
				for (String weekDate : firstFiveWeekDate) {
					List<String> measureIds = queryMeasureIdList(proNo, weekDate, category, parameter);
					Set<String> measureIdList = new HashSet();
					measureIdList.addAll(measureIds);
					List<Map<String, Object>> measureIds2 = new ArrayList<>();
					if (null != measureIdList && measureIdList.size() > 0) {
						measureIds2 = projectReportDao.getMeasureIds(measureIdList, category);
					}
					List<String> measureList = new ArrayList<>();
					if (null != measureIds2 && measureIds2.size() > 0) {
						for (int i = 0; i < measureIds2.size(); i++) {
							measureList.add(measureIds2.get(i).get("ID") + "");
						}
					}
					parameter.put("measureList", measureList);
					List<Map<String, Object>> querymeasureList = new ArrayList<>();
					if (null != measureList && measureList.size() > 0) {
						querymeasureList = projectReportDao.getMeasureVale(parameter);
					}
					Map<String, Object> result = new HashMap<>();
					result.put("cycle", weekDate);
					if (null != querymeasureList && querymeasureList.size() > 0) {
						for (int i = 0; i < querymeasureList.size(); i++) {
							result.put(querymeasureList.get(i).get("measure_id") + "",
									querymeasureList.get(i).get("measure_value") + "");
						}
					}
					rets.add(result);
				}
			}
		} catch (Exception e) {
			logger.error("查询失败", e);
		}
		return rets;
	}

	@SuppressWarnings("unchecked")
	public TableSplitResult<List<ProjectMonthBudget>> queryCostReport(ProjectInfo projectInfo, PageRequest pageRequest,
																	  String date) {
		TableSplitResult<List<ProjectMonthBudget>> result = new TableSplitResult<List<ProjectMonthBudget>>();
		Set<String> nos = projectInfoService.projectNos(projectInfo, null);
		List<String> proNos = new ArrayList<>(nos);
		if (proNos.size() <= 0) {
			result.setErr(new ArrayList<>(), 1);
		}
		String progressDate = date;
		String[] dateArr = date.split("-");
		boolean middleMark = false;
		if (Integer.valueOf(dateArr[2]) == 15) {
			date = DateUtils.getNextCycle(date);
			middleMark = true;
		}

		List<ProjectMonthBudget> monthCostList = new ArrayList<>();
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		String month = dateArr[0] + "-" + dateArr[1];
		boolean flag = false;
		if (month.equals(DateUtils.YEAR_HYPHEN_MONTH.format(new Date()))) {
			flag = true;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("proNos", proNos);
		map.put("page", pageRequest);
		map.put("month", month);
		map.put("username", username);
		map.put("coopType", projectInfo.getCoopType());
		map.put("startDate", month + "-15");
		map.put("endDate", date);
		map.put("cycle", progressDate);
		map.put("middleMark", middleMark);
		int total = projectReportDao.queryCostReportCount(map);
		if (total <= 0) {
			result.setErr(new ArrayList<>(), 1);
		} else {
			monthCostList = projectReportDao.queryMonthCost(map);
			int inputDays = getInputDays(date, dateArr, flag);

			for (ProjectMonthBudget mb : monthCostList) {
				double timeProgress = 0.0;
				if (StringUtils.isNotBlank(mb.getEndDate())) {
					try {
						mb.setEndDate(DateUtils.SHORT_FORMAT_GENERAL
								.format(DateUtils.SHORT_FORMAT_GENERAL.parse(mb.getEndDate())));
						if (StringUtils.isNotBlank(mb.getStartDate())) {
							timeProgress = DateUtils.comparisonDateSizeYMdHmsTime(mb.getStartDate(),
									progressDate + " 00:00:00")
									/ DateUtils.comparisonDateSizeYMdHmsTime(mb.getStartDate(),
									mb.getEndDate() + " 00:00:00");
						}
					} catch (ParseException e1) {
						logger.error("ProjectOverviewService queryCostReport comparisonDateSizeYMdHmsTime failed"
								+ e1.getMessage());
					}
				}
				mb.setTimeProgress(timeProgress);
				mb.setPm(StringUtils.isNotBlank(mb.getPm()) ? mb.getPm().replaceAll("[^\u4e00-\u9fa5]", "") : "");
				mb.setDepartment(StringUtils.isNotBlank(mb.getHwzpdu()) ? mb.getHwzpdu() + "/" + mb.getPduSpdt()
						: mb.getPdu() + "/" + mb.getDu());
				mb.setSurplusBudget(mb.getProjectBudget() * 0.992 * 1.06 - (mb.getOldAttendMoney() + mb.getOldOverMoney()));
				mb.setOutputProgress(0.0 == mb.getProjectBudget() ? 0.0
						: (mb.getOldAttendMoney() + mb.getOldOverMoney()) / (mb.getProjectBudget() * 0.992 * 1.06));
				if (0.0 != (mb.getAttendMoney() + mb.getOverMoney()) && 0 != inputDays && mb.getSurplusBudget() > 0) {
					mb.setBudgetMaintenance(
							mb.getSurplusBudget() / ((mb.getAttendMoney() + mb.getOverMoney()) / inputDays));
				}

				getMaintenanceDate(date, flag, mb);
			}
		}
		result.setRows(monthCostList);
		result.setTotal(total);
		return result;
	}

	private void getMaintenanceDate(String date, boolean flag, ProjectMonthBudget mb) {
		if ((int) mb.getBudgetMaintenance() >= 1) {
			try {
				int count = 0;
				int affective = 0;
				int length = 0;

				if (!flag) {
					for (int j = 0; j <= length; j++) {
						if (DateUtils.isSaturdayAndSunday(
								DateUtils.getAroundDay(DateUtils.SHORT_FORMAT_GENERAL.parse(date), -j))) {
							affective++;
							length++;
						}
					}
				}
				for (int i = 0; i <= (int) mb.getBudgetMaintenance() + count; i++) {
					if (DateUtils.isSaturdayAndSunday(DateUtils.getAroundDay(DateUtils.SHORT_FORMAT_GENERAL
									.parse(flag ? DateUtils.SHORT_FORMAT_GENERAL.format(new Date())
											: DateUtils.getAroundDay(DateUtils.SHORT_FORMAT_GENERAL.parse(date), -affective)),
							i))) {
						count++;
					}
				}
				String maintenance = DateUtils.getAroundDay(
						DateUtils.SHORT_FORMAT_GENERAL.parse(flag ? DateUtils.getAroundDay(new Date(), -1)
								: DateUtils.getAroundDay(DateUtils.SHORT_FORMAT_GENERAL.parse(date), -affective)),
						(int) mb.getBudgetMaintenance() + count);
				mb.setMaintenanceDate(
						DateUtils.comparisonDateSizeYMdHms(maintenance + " 00:00:00", mb.getEndDate() + " 00:00:00")
								? maintenance : mb.getEndDate());
			} catch (ParseException e) {
				logger.error("ProjectOverviewService getMaintenanceDate failed:" + e.getMessage());
			}
		}
	}

	private int getInputDays(String date, String[] dateArr, boolean flag) {
		int inputDays = RankSalaryService.month_days.get(dateArr[1]);
		if (flag) {
			inputDays = 0;
			try {
				for (int i = 0; i < DateUtils.betweenDays(new Date(),
						DateUtils.SHORT_FORMAT_GENERAL.parse(DateUtils.getFirstDayOfMonth(date))); i++) {
					if (!DateUtils.isSaturdayAndSunday(DateUtils.getAroundDay(
							DateUtils.SHORT_FORMAT_GENERAL.parse(DateUtils.getFirstDayOfMonth(date)), i))) {
						inputDays++;
					}
				}
			} catch (ParseException e) {
				logger.error("ProjectOverviewService getInputDays failed:" + e.getMessage());
			}
		}
		return inputDays;
	}

	public Map<String, List> projectCostLine(String proNo, String nowDate) {
		Map<String, List> ret = new HashMap<>();
		List<String> dateList = DateUtils.getRecentMonths(7);
		List<String> date = new ArrayList<>();
		for (int i = 6; i >= 0; i--) {
			date.add(dateList.get(i));
		}
		List<ProjectMonthBudget> list = new ArrayList<ProjectMonthBudget>();
		String createTime = "";
		List<String> monthList = new ArrayList<>();
		List<String> normalValueList = new ArrayList<>();
		List<String> aoValueList = new ArrayList<>();
		List<String> attendValueList = new ArrayList<>();
		List<String> overValueList = new ArrayList<>();

		getProjectCost(proNo, dateList, list);

		if (null != list && list.size() > 0) {
			for (ProjectMonthBudget map : list) {
				if (null != map && null != map.getCostDate()) {
					String[] timeArr = DateUtils.SHORT_FORMAT_GENERAL.format(map.getCostDate()).split("-");
					createTime = timeArr[0] + '-' + timeArr[1];
					map.setTime(createTime);
				}
			}

			for (String s : date) {
				boolean flag = false;
				for (ProjectMonthBudget map : list) {
					if (s.equals(map.getTime())) {
						flag = true;
						monthList.add(s);
						normalValueList.add(String.valueOf(map.getNormalOut()));
						aoValueList.add(String.format("%.2f", map.getAttendMoney() + map.getOverMoney()));
						attendValueList.add(String.valueOf(map.getAttendMoney()));
						overValueList.add(String.valueOf(map.getOverMoney()));
						break;
					}
				}
				if (!flag) {
					monthList.add(s);
					normalValueList.add("-");
					aoValueList.add("-");
					attendValueList.add("-");
					overValueList.add("-");
				}
			}
		}
		ret.put("monthList", monthList);
		ret.put("normalValueList", normalValueList);
		ret.put("aoValueList", aoValueList);
		ret.put("attendValueList", attendValueList);
		ret.put("overValueList", overValueList);
		return ret;
	}

	private void getProjectCost(String proNo, List<String> dateList, List<ProjectMonthBudget> list) {
		for (int i = 6; i >= 0; i--) {
			ProjectMonthBudget measureValue = projectReportDao.getProjectCost(proNo, dateList.get(i) + "-01",
					DateUtils.getEndOfMonth(dateList.get(i) + "-01"));
			if (null != measureValue) {
				list.add(measureValue);
			}
		}
	}

	public List<ProjectInfo> queryConcernItems(String username, ProjectInfo projectInfo) {
		Map<String, Object> map = new HashMap<>();
		projectInfoService.setParamNew(projectInfo, null, map);
		map.put("username", username);
		List<ProjectInfo> list = projectInfoVoDao.followProjectNos(map);
		return list;
	}

	public void saveConcernItems(String nos) {
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		List<String> projectids = CollectionUtilsLocal.splitToList(nos);
		List<String> projectidsOld = CollectionUtilsLocal.splitToList(nos);
		List<String> collectedNos = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		List<ProjectInfo> data = projectInfoVoDao.followProjectNos(map);
		for (ProjectInfo projectInfo : data) {
			collectedNos.add(projectInfo.getNo());
		}
		projectidsOld.removeAll(collectedNos);
		collectedNos.removeAll(projectids);
		if (collectedNos.size() > 0) {
			for (String no : collectedNos) {
				projectReportDao.deleteConcernItems(no, username);
			}
		}
		if (projectidsOld.size() > 0) {
			List<Map<String, Object>> list = new ArrayList<>();
			for (String no : projectidsOld) {
				Map<String, Object> maps = new HashMap<>();
				maps.put("proNo", no);
				maps.put("username", username);
				list.add(maps);
			}
			projectReportDao.addConcernItems(list);
		}
	}

	public List<OnsiteNews> getOnsiteNews() {
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		Map<String, String> account = projectInfoVoDao.getUserAccounts(username);
		List<OnsiteNews> data = new ArrayList<>();
		if(account == null) {
			return data;
		}
		String zr = StringUtils.isEmpty(account.get("zrAccount")) ? "" : account.get("zrAccount");
		String hw = StringUtils.isEmpty(account.get("hwAccount")) ? "" : account.get("hwAccount");
		//获取所有相关项目的项目编号
		Set<String> nos = new HashSet<String>();
		nos.addAll(projectInfoVoDao.getOnsiteNums(zr,hw,"1"));
		nos.addAll(projectInfoVoDao.getOnsiteNums(zr,hw,"2"));
		nos.addAll(projectInfoVoDao.getOnsiteNums(zr,hw,"3"));
		List<String> proNos = new ArrayList<>(nos);
		data = projectInfoVoDao.getOnsiteNews("(" + StringUtilsLocal.listToSqlIn(proNos) + ")",zr,hw);
		return data;
	}

	public void saveReadedNews(String proNo) {
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		String date = DateUtils.STANDARD_FORMAT_GENERAL.format(new Date());
		Map<String, String> account = projectInfoVoDao.getUserAccounts(username);
		String zr = StringUtils.isEmpty(account.get("zrAccount")) ? "" : account.get("zrAccount");
		String hw = StringUtils.isEmpty(account.get("hwAccount")) ? "" : account.get("hwAccount");
		List<Map<String, Object>> data = new ArrayList<>();
		saveReadedData(projectInfoVoDao.getNewsIds(zr,proNo,"1"),proNo,"1",zr,date,data);
		saveReadedData(projectInfoVoDao.getNewsIds(zr,proNo,"2"),proNo,"2",zr,date,data);
		saveReadedData(projectInfoVoDao.getNewsIds(hw,proNo,"3"),proNo,"3",zr,date,data);
		projectInfoVoDao.saveReadedNews(data);
	}

	public void saveReadedData(List<String> ids,String no,String type,String zr,String date,List<Map<String, Object>> data){
		for (String string : ids) {
			Map<String, Object> map = new HashMap<>();
			map.put("taskId", string);
			map.put("no", no);
			map.put("type", type);
			map.put("zrAccount", zr);
			map.put("readTime", date);
			data.add(map);
		}
	}

	public List<OnsiteNews> getReadedNews() {
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		Map<String, String> account = projectInfoVoDao.getUserAccounts(username);
		List<OnsiteNews> data = new ArrayList<>();
		if(account == null) {
			return data;
		}
		String zr = StringUtils.isEmpty(account.get("zrAccount")) ? "" : account.get("zrAccount");
		data = projectInfoVoDao.getReadedNews(zr);
		return data;
	}

	public TableSplitResult<List<StationInformation>> getAllNews(PageRequest page) {
		TableSplitResult<List<StationInformation>> result = new TableSplitResult<List<StationInformation>>();
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		com.github.pagehelper.Page pageRequest = PageHelper.startPage((page.getOffset() == null ? 1 : (page.getOffset()/page.getPageSize())+1), page.getPageSize());
		List<StationInformation> data = projectInfoVoDao.getReadedNewsClone(username);
		result.setTotal((int) pageRequest.getTotal());
		Date time = new Date();
		List<String> ids = new ArrayList<>();
		for (StationInformation map : data) {
			if(map.getReceivingTime() == null){
				ids.add(map.getId().toString());
				map.setReceivingTime(time);
			}
		}
		if(ids.size() > 0 ){
			projectInfoVoDao.updateInformation("(" + String.join(",",ids) + ")",time);
		}
		result.setRows(data);
		return result;
	}

	public BaseResponse saveInformation(String job, String content) {
		BaseResponse ret = new BaseResponse();
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		if(username.equals(job)){
			ret.setCode("error");
			ret.setMessage("接收人不能是本人!");
			return ret;
		}
		/**
		 * 组装消息各字段信息并保存
		 */
		StationInformation data = new StationInformation();
		data.setSender(username);
		data.setReceiver(job);
		data.setSendTime(new Date());
		data.setInformation(content);
		projectInfoVoDao.saveInformation(data);

		//保存关注人员信息
		attentionPersonServices.saveAttentionPersonnel(data);
		ret.setCode("success");
		return ret;
	}

	public List<StationInformation> getMessageList(HttpServletRequest request) {
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		List<StationInformation> list = projectInfoVoDao.getReadedNewsClone(username);
		return list;
	}
}
