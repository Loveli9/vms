package com.icss.mvp.controller.project;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.controller.BaseController;
import com.icss.mvp.dao.project.ISchedulePlanDao;
import com.icss.mvp.entity.Biweekly;
import com.icss.mvp.entity.PreassessmentScore;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectQuality;
import com.icss.mvp.entity.ProjectStatus;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.project.ProjectLampMode;
import com.icss.mvp.entity.project.ProjectReviewEntity;
import com.icss.mvp.entity.project.ProjectWeekLamp;
import com.icss.mvp.entity.project.SchedulePlanEntity;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.ProjectOperationService;
import com.icss.mvp.service.ProjectOverviewService;
import com.icss.mvp.service.project.ProjectReviewService;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.DateUtils;

/**
 * Created by up on 2019/2/14.
 */
@Controller
@RequestMapping("/projectReview")
public class ProjectReviewController extends BaseController {

	private static Logger logger = Logger.getLogger(ProjectReviewController.class);

	@Autowired
	private ProjectReviewService projectReviewService;

	@Resource
	private ProjectOverviewService projectOverviewService;

	@Autowired
	private ProjectOperationService projectOperationService;

	@Autowired
	private ISchedulePlanDao iSchedulePlanDao;

	/**
	 * 查询项目最后5个项目点评信息
	 * 
	 * @param no
	 * @return
	 */
	@RequestMapping("/queryProjectReviewTop5")
	@ResponseBody
	public ListResponse<ProjectReviewEntity> queryProjectReviewTop5(String no) {
		ListResponse<ProjectReviewEntity> result = new ListResponse<>();
		try {
			List<ProjectReviewEntity> data = projectReviewService.queryProjectReviewTop5(no);
			result.setCode("0");
			result.setMessage("查询项目最后5个项目点评信息成功");
			result.setData(data);
			result.setTotalCount(data.size());
		} catch (Exception e) {
			logger.error("queryProjectReviewTop5 exception, error:", e);
			result.setCode("1");
			result.setMessage("查询项目最后5个项目点评信息失败");
			result.setData(new ArrayList<>());
			result.setTotalCount(0);
		}
		return result;
	}

	@RequestMapping("/queryProjectCycle")
	@ResponseBody
	public PlainResponse<List<String>> queryProjectCycle(String proNo) {
		PlainResponse<List<String>> result = new PlainResponse<List<String>>();
		try {
			result.setData(projectReviewService.queryProjectCycleQ(proNo));
			result.setCode("success");
		} catch (Exception e) {
			logger.error("queryProjectCycle exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}

	@RequestMapping("/getProjectReviewDetail")
	@ResponseBody
	public PlainResponse<ProjectReviewEntity> getProjectReviewDetail(String proNo, String date, boolean flag) {
		PlainResponse<ProjectReviewEntity> result = new PlainResponse<ProjectReviewEntity>();
		try {
			if (flag) {
				result.setData(projectReviewService.getProjectReviewCurrent(proNo, date));
			} else {
				result.setData(projectReviewService.getMembersStatusRate(proNo, date));
			}
			result.setCode("success");
		} catch (Exception e) {
			logger.error("getProjectReviewDetail exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}

	@RequestMapping("/publishProjectReview")
	@ResponseBody
	public BaseResponse publishProjectReview(String proNo, String date, String review) {
		BaseResponse result = new BaseResponse();
		// 参数合法性校验，非空，必填
		if (StringUtils.isBlank(proNo) || StringUtils.isBlank(date) || StringUtils.isBlank(review)) {
			result.setError(CommonResultCode.ILLEGAL_PARAM, "proNo date review 为必填参数。");
			return result;
		}
		// TODO:参数校验 无周期、不是当期或已结项则不能发布
		String closeDate = getCloseDateByNo(proNo);
		if ("时间配置不在周期内".equals(date) || StringUtils.isNotBlank(closeDate)) {
			result.setError(CommonResultCode.INTERNAL, "时间配置不在周期内或者项目已结项");
			return result;
		}
		String currentWeek = DateUtils.getNowCurrentWeek();
		if (!currentWeek.equals(date)) {
			result.setErrorMessage("fail", "时间配置不是当前周期");
			return result;
		}
		try {
			projectReviewService.publishProjectReview(proNo, date, review);
			String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
			projectOperationService.saveProjectOperation(proNo, userName, "发布项目OSG双周报数据");
		} catch (Exception e) {
			logger.error("publishProjectReview exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}

	/**
	 * 结项：变更结项双周报发布状态和项目开启状态
	 * 
	 * @param proNo
	 * @param date
	 * @param review
	 * @return
	 */
	@RequestMapping("/closeProjectReview")
	@ResponseBody
	public BaseResponse closeProjectReview(String proNo, String date, String review) {
		BaseResponse result = new BaseResponse();
		// 参数合法性校验，非空，必填
		if (StringUtils.isBlank(proNo) || StringUtils.isBlank(date) || StringUtils.isBlank(review)) {
			result.setError(CommonResultCode.ILLEGAL_PARAM, "proNo date review 为必填参数。");
			return result;
		}
		// 参数校验: 无周期、不是当期或已结项则不能发布
		String closeDate = getCloseDateByNo(proNo);
		if ("时间配置不在周期内".equals(date) || StringUtils.isNotBlank(closeDate)) {
			result.setError(CommonResultCode.INTERNAL, "时间配置不在周期内或者项目已结项");
			return result;
		}
		String currentWeek = DateUtils.getNowCurrentWeek();
		if (!currentWeek.equals(date)) {
			result.setErrorMessage("fail", "时间配置不是当前周期");
			return result;
		}
		try {
			projectReviewService.publishCloseProjectReview(proNo, date, review);
			projectReviewService.closeProjects(proNo);
			// 查询概况中结项节点的信息
			List<SchedulePlanEntity> schedulePlan = iSchedulePlanDao.querySchedulePlan(proNo, "结项");
			if (null != schedulePlan && schedulePlan.size() > 0) {
				Date actualFinishDate = DateUtils.parseDate(DateUtils.SHORT_FORMAT_GENERAL, date);
				schedulePlan.get(0).setActualFinishDate(actualFinishDate);
				iSchedulePlanDao.editSchedulePlan(schedulePlan.get(0));
			}
			String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
			projectOperationService.saveProjectOperation(proNo, userName, "项目结项");
		} catch (Exception e) {
			logger.error("closeProjectReview exception, error:", e);
			result.setErrorMessage("error", e.getMessage());
		}

		return result;
	}

	@RequestMapping("/getCloseDateByNo")
	@ResponseBody
	public String getCloseDateByNo(String proNo) {
		String date = projectReviewService.getCloseDateByNo(proNo);
		return StringUtils.isNotBlank(date) ? date : "";
	}

	@RequestMapping("/changeEdit")
	@ResponseBody
	public PlainResponse<ProjectWeekLamp> changeEdit(ProjectReviewEntity projectReviewEntity) {
		PlainResponse<ProjectWeekLamp> result = new PlainResponse<ProjectWeekLamp>();
		try {
			result.setData(projectReviewService.changeEdit(projectReviewEntity));
			result.setCode("success");
			String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
			projectOperationService.saveProjectOperation(projectReviewEntity.getProNo(), userName, "录入变更率数据");
		} catch (Exception e) {
			logger.error("changeEdit exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}

	@RequestMapping("/progressEdit")
	@ResponseBody
	public PlainResponse<ProjectWeekLamp> progressEdit(ProjectReviewEntity projectReviewEntity) {
		PlainResponse<ProjectWeekLamp> result = new PlainResponse<ProjectWeekLamp>();
		try {
			result.setData(projectReviewService.progressEdit(projectReviewEntity));
			result.setCode("success");
			String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
			projectOperationService.saveProjectOperation(projectReviewEntity.getProNo(), userName, "录入交付及时性数据");
		} catch (Exception e) {
			logger.error("progressEdit exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}

	@RequestMapping("/getProjectLampMode")
	@ResponseBody
	public PlainResponse<ProjectLampMode> getProjectLampMode(String proNo, String date, String field) {
		PlainResponse<ProjectLampMode> result = new PlainResponse<ProjectLampMode>();
		try {
			result.setData(projectReviewService.getProjectLampMode(proNo, date, field));
			result.setCode("success");
		} catch (Exception e) {
			logger.error("getProjectLampMode exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}

	@RequestMapping("/lampModeEdit")
	@ResponseBody
	public PlainResponse<ProjectWeekLamp> manualLampEdit(ProjectLampMode lampMode) {
		PlainResponse<ProjectWeekLamp> result = new PlainResponse<ProjectWeekLamp>();
		try {
			result.setData(projectReviewService.lampModeEdit(lampMode));
			result.setCode("success");
			String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
			projectOperationService.saveProjectOperation(lampMode.getProNo(), userName, "修改OSG双周报点灯状态");
		} catch (Exception e) {
			logger.error("lampModeEdit exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}

	@RequestMapping("/queryProjectReviewTop6")
	@ResponseBody
	public ProjectWeekLamp queryProjectReviewTop6(ProjectInfo projectInfo) throws ParseException {
		ProjectWeekLamp map = new ProjectWeekLamp();
		map = projectReviewService.getProjectReviewTop6(projectInfo);
		return map;
	}

	@RequestMapping("/twoWeekState")
	@ResponseBody
	public ProjectWeekLamp twoWeekState(ProjectInfo projectInfo) throws ParseException {
		ProjectWeekLamp map = new ProjectWeekLamp();
		map = projectReviewService.querytwoWeekState(projectInfo);
		return map;
	}

	@RequestMapping("/queryZonglanEcahrs")
	@ResponseBody
	public ProjectWeekLamp queryZonglanEcahrs(ProjectInfo projectInfo, PageRequest pageRequest) throws ParseException {
		ProjectWeekLamp map = new ProjectWeekLamp();
		map = projectReviewService.queryZonglanEcahrs(projectInfo);
		return map;
	}

	@RequestMapping("/queryProjectState")
	@ResponseBody
	public List<ProjectQuality> queryProjectState(ProjectInfo projectInfo) throws ParseException {
		List<ProjectQuality> list = new ArrayList<>();
		list = projectReviewService.queryProjectState(projectInfo);
		return list;
	}

	@RequestMapping("/queryProjectExpect")
	@ResponseBody
	public PreassessmentScore queryProjectExpect(ProjectInfo projectInfo) {
		PreassessmentScore data = projectReviewService.queryProjectExpect(projectInfo);
		return data;
	}

	@RequestMapping("/averageBrokenline")
	@ResponseBody
	public Biweekly averageBrokenline(ProjectInfo projectInfo) {
		Biweekly data = projectReviewService.averageBrokenline(projectInfo);
		return data;
	}

	@RequestMapping("/actualTableSave")
	@ResponseBody
	public TableSplitResult<List<ProjectStatus>> actualTableSave(ProjectInfo projectInfo, PageRequest pageRequest) {
		return projectReviewService.actualTableSave(projectInfo, pageRequest);
	}

	@RequestMapping("/queryQualityState")
	@ResponseBody
	public List<ProjectQuality> queryQualityState(ProjectInfo projectInfo, String measureMark) throws ParseException {
		List<ProjectQuality> list = new ArrayList<>();
		int[] measureList = new int[] { 223, 311, 309, 337, 385, 307, 308, 387 };
		list = projectReviewService.queryQualityState(projectInfo, measureList, measureMark, null);
		return list;
	}

	@RequestMapping("/queryKXqualityState")
	@ResponseBody
	public List<ProjectQuality> queryKXqualityState(ProjectInfo projectInfo, PageRequest pageRequest,
			String measureMark) throws ParseException {
		List<ProjectQuality> list = new ArrayList<>();
		list = projectReviewService.queryKXqualityState(projectInfo, pageRequest, measureMark);
		return list;
	}

	@RequestMapping("/queryQualityEcahrs")
	@ResponseBody
	public ProjectWeekLamp queryQualityEcahrs(ProjectInfo projectInfo, PageRequest pageRequest, String measureMark)
			throws ParseException {
		ProjectWeekLamp map = new ProjectWeekLamp();
		map = projectReviewService.queryQualityEcahrs(projectInfo, pageRequest, measureMark);
		return map;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryProjectStateScore")
	@ResponseBody
	public PlainResponse<Map> queryProjectStateScore(String no) {
		PlainResponse<Map> result = new PlainResponse<>();
		try {
			Map<String, List> data = projectReviewService.queryProjectStateScore(no);
			result.setMessage("查询项目点评得分成功");
			result.setData(data);
		} catch (Exception e) {
			logger.error("queryProjectStateScore exception, error:", e);
			result.setCode("500");
			result.setMessage("查询项目点评得分失败");
			result.setData(new HashMap());
		}
		return result;
	}

	@RequestMapping("/getSelectProjectReview")
	@ResponseBody
	public List<Map<String, Object>> getSelectProjectReview(String no) {
		return projectReviewService.getSelectProjectReview(no);
	}
	
	@RequestMapping("/getProjectManualValue")
	@ResponseBody
	public PlainResponse<String> getProjectManualValue(String proNo, String date, String template){
		PlainResponse<String> result = new PlainResponse<String>();
		try {
			String manualValue = projectReviewService.getProjectManualValue(proNo, date,template);
			result.setData(manualValue);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("getProjectManualValue exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}
}
