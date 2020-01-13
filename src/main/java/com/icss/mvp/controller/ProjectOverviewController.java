package com.icss.mvp.controller;

import com.icss.mvp.dao.ProjectReportDao;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.project.ProjectReviewEntity;
import com.icss.mvp.entity.rank.ProjectMonthBudget;
import com.icss.mvp.service.ProjectOverviewService;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/projectOverview")
public class ProjectOverviewController {

	private static Logger logger = Logger.getLogger(ProjectOverviewController.class);

	@Resource
	private HttpServletRequest request;

	@Resource
	private ProjectOverviewService projectOverviewService;

	@Autowired
	private ProjectReportDao projectReportDao;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getProjectOverview")
	@ResponseBody
	public TableSplitResult getProjectOverview(ProjectInfo projectInfo, PageRequest pageRequest, String statisticalTime,String model,
											   String status) {
		TableSplitResult ret = null;
		String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");
		sort = transIntoSqlChar(sort);
		String sortOrder = StringUtils.isEmpty(request.getParameter("order")) ? "" : request.getParameter("order");
		if("p.START_DATE".equals(sort)) {
			if("asc".equals(sortOrder)) {
				sortOrder = "desc";
			}else {
				sortOrder = "asc";
			}
		}
		try {
			ret = projectOverviewService.getProjectOverview(projectInfo, pageRequest, statisticalTime, model,status,sort,sortOrder);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
		}
		return ret;
	}

	/**
	 * 近五次点评信息
	 * @param: [projectInfo, pageRequest]
	 * @return: com.icss.mvp.entity.TableSplitResult
	 * @Author: chengchenhui
	 * @Date: 2019/12/18
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getProjectAccess")
	@ResponseBody
	public TableSplitResult getProjectAccess(ProjectInfo projectInfo, HttpServletRequest request) {
		TableSplitResult ret = null;

		String sortOrder = StringUtils.isEmpty(request.getParameter("order")) ? "" : request.getParameter("order");
		int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));// 每页显示条数
		int offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));// 第几页
		try {
			TableSplitResult page = new TableSplitResult();
			page.setPage(offset);
			page.setRows(limit);
			ret = projectOverviewService.getProjectAccess(projectInfo,page);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
		}
		return ret;
	}

	/**
	 * 项目中近五次点评信息
	 * @param: [projectInfo, pageRequest]
	 * @return: com.icss.mvp.entity.TableSplitResult
	 * @Author: zhanghucheng
	 * @Date: 2020/01/06
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getProjectAccessByNo")
	@ResponseBody
	public TableSplitResult getProjectAccessByNo(ProjectInfo projectInfo, HttpServletRequest request) {
		TableSplitResult ret = null;

		String sortOrder = StringUtils.isEmpty(request.getParameter("order")) ? "" : request.getParameter("order");
		try {
			ret = projectOverviewService.getProjectAccessByNo(projectInfo);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
		}
		return ret;
	}

	/**
	 * 获取总览Top项目
	 *
	 * @param projectInfo
	 * @param pageRequest
	 * @param statisticalTime
	 * @param projectStatus
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getZongLanTopProject")
	@ResponseBody
	public TableSplitResult getZongLanTopProject(ProjectInfo projectInfo, PageRequest pageRequest,
												 String statisticalTime, String projectStatus) {
		TableSplitResult ret = null;
		try {
			ret = projectOverviewService.getZongLanTopProject(projectInfo, pageRequest, statisticalTime, projectStatus);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
		}
		return ret;
	}

	/**
	 * 获取质量Top项目
	 *
	 * @param projectInfo
	 * @param pageRequest
	 * @param measureMark
	 * @param measure
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getZhiLiangTopProject")
	@ResponseBody
	public TableSplitResult getZhiLiangTopProject(ProjectInfo projectInfo, PageRequest pageRequest, String measure,
												  String measureMark) {
		TableSplitResult ret = null;
		try {
			ret = projectOverviewService.getZhiLiangTopProject(projectInfo, pageRequest, measure, measureMark);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
		}
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/getTime")
	@ResponseBody
	public PlainResponse getTime() {
		PlainResponse result = new PlainResponse();
		try {
			result.setData(projectOverviewService.getTime());
			result.setCode("success");
		} catch (Exception e) {
			logger.error("getTime exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}

	@RequestMapping("/queryProjectQualityReport")
	@ResponseBody
	public TableSplitResult<List<Map<String, Object>>> queryProjectQualityReport(ProjectInfo projectInfo,
																				 PageRequest pageRequest, String date) {
		TableSplitResult<List<Map<String, Object>>> result = new TableSplitResult<List<Map<String, Object>>>();

		String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");
		boolean flag = true ;
		Map<String, Object> tsq = transIntoSqlCharIsQuery(sort,flag);
		sort = StringUtilsLocal.valueOf(tsq.get("sortName"));
		flag = (boolean) tsq.get("flag");
		String sortOrder = StringUtils.isEmpty(request.getParameter("order")) ? ""
				: request.getParameter("order");
		if("p.START_DATE".equals(sort)) {
			if("asc".equals(sortOrder)) {
				sortOrder = "desc";
			}else {
				sortOrder = "asc";
			}
		}

		try {
			result = projectOverviewService.queryProjectQualityReport(projectInfo, pageRequest, date, "163",sortOrder,sort,flag);
		} catch (Exception e) {
			logger.error("queryProjectQualityReport exception, error:", e);

		}
		return result;
	}

	@RequestMapping("/queryProblemAnalysis")
	@ResponseBody
	public TableSplitResult<List<ReportProblemAnalysis>> queryProblemAnalysis(ProjectInfo projectInfo,
																			  PageRequest pageRequest, String date) {
		TableSplitResult<List<ReportProblemAnalysis>> result = new TableSplitResult<List<ReportProblemAnalysis>>();
		try {
			result = projectOverviewService.queryProblemAnalysis(projectInfo, pageRequest, date);
		} catch (Exception e) {
			logger.error("projectOverviewService.queryProblemAnalysis exception, error: "+e.getMessage());

		}
		return result;
	}

	@RequestMapping("/queryProjectReliableQuality")
	@ResponseBody
	public TableSplitResult<List<Map<String, Object>>> queryProjectReliableQuality(ProjectInfo projectInfo,
																				   PageRequest pageRequest, String date) {
		TableSplitResult<List<Map<String, Object>>> result = new TableSplitResult<List<Map<String, Object>>>();

		String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");
		boolean flag = true ;
		Map<String, Object> tsq = transIntoSqlCharIsQuery(sort,flag);
		sort = StringUtilsLocal.valueOf(tsq.get("sortName"));
		flag = (boolean) tsq.get("flag");
		String sortOrder = StringUtils.isEmpty(request.getParameter("order")) ? ""
				: request.getParameter("order");
		if("p.START_DATE".equals(sort)) {
			if("asc".equals(sortOrder)) {
				sortOrder = "desc";
			}else {
				sortOrder = "asc";
			}
		}

		try {
			result = projectOverviewService.queryProjectQualityReport(projectInfo, pageRequest, date, "164",sortOrder,sort,flag);
		} catch (Exception e) {
			logger.error("queryProjectQualityReport exception, error:", e);

		}
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
	@RequestMapping("/queryResourceReport")
	@ResponseBody
	public TableSplitResult<List<MemberResourceReport>> queryResourceReport(ProjectInfo projectInfo, PageRequest pageRequest, String date) {
		TableSplitResult<List<MemberResourceReport>> result = new TableSplitResult<List<MemberResourceReport>>();
		try {
			result = projectOverviewService.queryResourceReport(projectInfo, pageRequest, date);
		} catch (Exception e) {
			logger.error("queryResourceReport exception, error:", e);
		}
		return result;
	}


	@RequestMapping("/queryProjectDevtestReport")
	@ResponseBody
	public TableSplitResult<List<Map<String, Object>>> queryProjectDevtestReport(ProjectInfo projectInfo, String date, PageRequest pageRequest) {
		TableSplitResult<List<Map<String, Object>>> result = new TableSplitResult<List<Map<String, Object>>>();
		try {
			result = projectOverviewService.queryProjectDevtestReport(projectInfo, date, pageRequest);
		} catch (Exception e) {
			logger.error("queryProjectDevtestReport exception, error:", e);

		}
		return result;
	}

	/**
	 * 报表质量部分弹出窗口
	 * @param projectId
	 * @param id
	 * @return
	 */
	@RequestMapping("/measureValueLine")
	@ResponseBody
	public PlainResponse<ProjectReviewEntity> measureValueLine(String projectId, String id, String measureMark, String nowDate) {
		PlainResponse<ProjectReviewEntity> result = new PlainResponse();
		try {
			ProjectReviewEntity data = projectOverviewService.measureValueLine(projectId, id, measureMark, nowDate);
			result.setMessage("查询得分成功");
			result.setData(data);
		} catch (Exception e) {
			logger.error("queryResourceReport exception, error:", e);
			result.setCode("500");
			result.setMessage("查询得分失败");
			result.setData(new ProjectReviewEntity());
		}
		return result;
	}

	/**
	 * 报表总览页面项目质量点评数据导出功能
	 * @param response
	 * @param nos
	 */
	@RequestMapping("/exportProjectIndex")
	public void exportProjectIndex(HttpServletResponse response,String nos,String date) {
		try {
			byte[] fileContents = projectOverviewService.exportProjectIndex(nos,date);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = "项目质量点评导出" + sf.format(new Date()).toString() + ".xlsx";
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));

			response.getOutputStream().write(fileContents);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			logger.error("导出失败", e);
		}
	}

	/**
	 * 过程指标和结项指标项目信息获取
	 * @param projectInfo 包含有传入的选择时间
	 * @param pageRequest
	 * @param target 过程指标,结项指标
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/processNominalsTable")
	@ResponseBody
	public TableSplitResult<List<QualityMonthlyReport>> processNominalsTable(ProjectInfo projectInfo,PageRequest pageRequest,
																			 String statisticalTime, String target,String category) {
		TableSplitResult<List<QualityMonthlyReport>> result = new TableSplitResult<List<QualityMonthlyReport>>();

		String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");//排序字段
		sort = transIntoSqlChar(sort);
		String sortOrder = StringUtils.isEmpty(request.getParameter("sortOrder"))?"":request.getParameter("sortOrder");//排序方式
		if("p.START_DATE".equals(sort)) {
			if("asc".equals(sortOrder)) {
				sortOrder = "desc";
			}else {
				sortOrder = "asc";
			}
		}
		try {
			result = projectOverviewService.processNominalsTable(projectInfo, pageRequest, statisticalTime, target, URLDecoder.decode(category, "UTF-8"), sort, sortOrder);
		} catch (Exception e) {
			logger.error("queryProjectQualityReport exception, error:", e);
		}
		return result;
	}

	/**
	 * review质量
	 * @param projectInfo
	 * @param pageRequest
	 * @param date
	 * @return
	 */
	@RequestMapping("/queryProjectReviewQuality")
	@ResponseBody
	public TableSplitResult<List<Map<String, Object>>> queryProjectReviewQuality(ProjectInfo projectInfo,
																				 PageRequest pageRequest, String date) {
		TableSplitResult<List<Map<String, Object>>> result = new TableSplitResult<List<Map<String, Object>>>();

		String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");
		boolean flag = true ;
		Map<String, Object> tsq = transIntoSqlCharIsQuery(sort,flag);
		sort = StringUtilsLocal.valueOf(tsq.get("sortName"));
		flag = (boolean) tsq.get("flag");
		String sortOrder = StringUtils.isEmpty(request.getParameter("order")) ? ""
				: request.getParameter("order");
		if("p.START_DATE".equals(sort)) {
			if("asc".equals(sortOrder)) {
				sortOrder = "desc";
			}else {
				sortOrder = "asc";
			}
		}

		try {
			result = projectOverviewService.queryProjectQualityReport(projectInfo, pageRequest, date, "162",sortOrder,sort,flag);
		} catch (Exception e) {
			logger.error("queryProjectReviewQuality exception, error:", e);

		}
		return result;
	}

	/**
	 * 过程指标和结项指标值获取
	 * @param proNo
	 * @param statisticalTime
	 * @param category
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMeasureValueList")
	@ResponseBody
	public TableSplitResult<List<Map<String, Object>>> queryMeasureValueList(String proNo, String statisticalTime, String category) {
		TableSplitResult<List<Map<String, Object>>> result = new TableSplitResult<List<Map<String, Object>>>();
		try {
			List<Map<String, Object>> rets = new ArrayList<>();
			rets = projectOverviewService.queryMeasureValueList(proNo, statisticalTime, URLDecoder.decode(category, "UTF-8") );
			result.setRows(rets);
		} catch (Exception e) {
			logger.error("queryProjectDevtestReport exception, error:", e);

		}
		return result;
	}

	/**
	 * 项目成本
	 * @param projectInfo
	 * @param pageRequest
	 * @param date
	 * @return
	 */
	@RequestMapping("/queryCostReport")
	@ResponseBody
	public TableSplitResult<List<ProjectMonthBudget>> queryCostReport(ProjectInfo projectInfo, PageRequest pageRequest, String date) {
		TableSplitResult<List<ProjectMonthBudget>> result = new TableSplitResult<List<ProjectMonthBudget>>();
		try {
			result = projectOverviewService.queryCostReport(projectInfo, pageRequest, date);
		} catch (Exception e) {
			logger.error("queryCostReport exception, error:", e);
		}
		return result;
	}

	/**
	 * 成本趋势
	 * @param projectId
	 * @param nowDate
	 * @return
	 */
	@RequestMapping("/projectCostLine")
	@ResponseBody
	public PlainResponse<Map> projectCostLine(String projectId, String nowDate) {
		PlainResponse<Map> result = new PlainResponse();
		try {
			Map<String, List> data = projectOverviewService.projectCostLine(projectId, nowDate);
			result.setMessage("查询得分成功");
			result.setData(data);
		} catch (Exception e) {
			logger.error("projectCostLine exception, error:", e);
			result.setCode("500");
			result.setMessage("查询得分失败");
			result.setData(new HashMap<>());
		}
		return result;
	}

	@RequestMapping("/addConcernItems")
	@ResponseBody
	public Map<String, Object> addConcernItems(String proNo) {
		Map<String, Object> data = new HashMap<String, Object>();
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("proNo", proNo);
		map.put("username", username);
		list.add(map);
		try {
			projectReportDao.addConcernItems(list);
			data.put("msg", "关注成功");
			data.put("status", "0");
		} catch (Exception e) {
			logger.error("addFavoriteProject exception, error:" + e.getMessage());
			data.put("msg", "关注失败");
			data.put("status", "1");
		}
		return data;
	}

	@RequestMapping("/deleteConcernItems")
	@ResponseBody
	public Map<String, Object> deleteConcernItems(String proNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		try {
			projectReportDao.deleteConcernItems(proNo, username);
			map.put("msg", "取消关注成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("addFavoriteProject exception, error:" + e.getMessage());
			map.put("msg", "取消关注失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/queryConcernItems")
	@ResponseBody
	public PageResponse<ProjectInfo>queryConcernItems(ProjectInfo projectInfo){
		PageResponse<ProjectInfo> result = new PageResponse<>();
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);

		try {
			List<ProjectInfo> data = projectOverviewService.queryConcernItems(username,projectInfo);
			result.setCode("0");
			result.setMessage("查询关注项目成功");
			result.setData(data);
			result.setTotalCount(data.size());
		} catch (Exception e) {
			logger.error("addFavoriteProject exception, error:" + e.getMessage());
			result.setCode("1");
			result.setMessage("查询关注项目失败");
			result.setData(new ArrayList<>());
			result.setTotalCount(0);
		}
		return result;
	}

	@RequestMapping("/saveConcernItems")
	@ResponseBody
	public BaseResponse saveConcernItems(String nos){
		BaseResponse result = new BaseResponse();
		try {
			projectOverviewService.saveConcernItems(nos);
			result.setCode("success");
		} catch (Exception e) {
			logger.info(e.getMessage());
			result.setCode("failure");
			result.setMessage("保存失败");
		}
		return result;
	}

	/**
	 * @Description:将表头字段映射为数据库字段 @param @param sort @param @return 参数 @return
	 *                            String 返回类型 @throws
	 */
	private String transIntoSqlChar(String sort) {
		String sortName = "";
		if ("name".equals(sort)) {
			sortName = "p.NAME";
		} else if ("pm".equals(sort)) {
			sortName = "p.PM";
		} else if ("qa".equals(sort)) {
			sortName = "QA";
		} else if ("department".equals(sort)) {
			sortName = "p.HWZPDU,p.PDU_SPDT";
		} else if ("projectType".equals(sort)) {
			sortName = "p.PROJECT_TYPE";
		} else if ("qualityLamp".equals(sort)) {
			sortName = "quality_lamp";
		} else if ("keyRoles".equals(sort)) {
			sortName = "resources_lamp";
		} else if ("planLamp".equals(sort)) {
			sortName = "progress_lamp";
		} else if ("scopeLamp".equals(sort)) {
			sortName = "changes_lamp";
		}else if ("statisticalCycle".equals(sort)) {
			sortName = "p.START_DATE";
		}
		return sortName;
	}

	@RequestMapping("/getOnsiteNews")
	@ResponseBody
	public PlainResponse<List<OnsiteNews>> getOnsiteNews() {
		PlainResponse<List<OnsiteNews>> result = new PlainResponse<List<OnsiteNews>>();
		try {
			result.setData(projectOverviewService.getOnsiteNews());
			result.setCode("success");
		} catch (Exception e) {
			logger.error("getTime exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}

	@RequestMapping("/getReadedNews")
	@ResponseBody
	public PlainResponse<List<OnsiteNews>> getReadedNews() {
		PlainResponse<List<OnsiteNews>> result = new PlainResponse<List<OnsiteNews>>();
		try {
			result.setData(projectOverviewService.getReadedNews());
			result.setCode("success");
		} catch (Exception e) {
			logger.error("getTime exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}

	@RequestMapping("/saveReadedNews")
	@ResponseBody
	public BaseResponse saveReadedNews(String proNo){
		BaseResponse result = new BaseResponse();
		try {
			projectOverviewService.saveReadedNews(proNo);
			result.setCode("success");
		} catch (Exception e) {
			logger.info(e.getMessage());
			result.setCode("failure");
			result.setMessage("保存失败");
		}
		return result;
	}

	/**
	 * @Description:将表头字段映射为数据库字段 @param @param sort @param @return 参数 @return
	 *                            String 返回类型 @throws
	 */
	private Map<String, Object> transIntoSqlCharIsQuery(String sort,boolean flag) {
		Map<String, Object> map = new HashMap<>();
		String sortName = "";
		if ("name".equals(sort)) {
			sortName = "NAME";
		} else if ("pm".equals(sort)) {
			sortName = "pm";
		} else if ("department".equals(sort)) {
			sortName = "hwzpdu,pduSpdt";
		} else if ("projectType".equals(sort)) {
			sortName = "projectType";
		} else if ("statisticalCycle".equals(sort)) {
			sortName = "p.START_DATE";
		} else {
			if(!"".equals(sort)) {
				sortName = sort.substring(2);
				flag = false ;
			}
		}
		map.put("sortName", sortName);
		map.put("flag", flag);
		return map;
	}

	/**
	 * 获取站内消息集合
	 * @param: request
	 * @param: page 分页参数
	 * @return: com.icss.mvp.entity.TableSplitResult<java.util.List<com.icss.mvp.entity.StationInformation>>
	 * @Author: anyudong
	 * @Date: 2020/1/9
	 */
	@RequestMapping("/getAllNews")
	@ResponseBody
	public TableSplitResult<List<StationInformation>> getAllNews(HttpServletRequest request, PageRequest page) {
		int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
		int offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));
		page.setPageSize(limit);
		page.setOffset(offset);
		TableSplitResult<List<StationInformation>> result = new TableSplitResult<List<StationInformation>>();
		try {
			result = projectOverviewService.getAllNews(page);
		} catch (Exception e) {
			logger.error("getAllNews exception, error:", e);
		}
		return result;
	}

	/**
	 * 获取未读消息列表
	 * @param: [request]
	 * @return: com.icss.mvp.entity.common.response.PlainResponse<com.icss.mvp.entity.StationInformation>
	 * @Author: chengchenhui
	 * @Date: 2020/1/9
	 */
    @RequestMapping("/getUnreadMessage")
    @ResponseBody
	public PlainResponse<StationInformation> getMessageList(HttpServletRequest request){
		PlainResponse result = new PlainResponse();
		try {
			result.setData(projectOverviewService.getMessageList(request));
			result.setCode("success");
		} catch (Exception e) {
            result.setCode("failed");
		    e.printStackTrace();
		    logger.error("get message failed:"+e.getMessage());
		}
		return result;
	}

	/**
	 * 站内信息发送
	 * @param: job:工号，content:消息内容
	 * @return: map
	 * @Author: anyudong
	 * @Date: 2019/12/25
	 */
	@RequestMapping("/saveInformation")
	@ResponseBody
	private BaseResponse saveInformation(String job,String content) {
		BaseResponse result = new BaseResponse();
		try {
			result = projectOverviewService.saveInformation(job,content);
		} catch (Exception e) {
			result.setCode("error");
			result.setMessage("发送失败!");
			logger.error("saveInformation exception, error:", e);
		}
		return result;
	}
}


