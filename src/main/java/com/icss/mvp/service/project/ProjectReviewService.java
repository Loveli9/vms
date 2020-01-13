package com.icss.mvp.service.project;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.icss.mvp.dao.IProjectInfoDao;
import com.icss.mvp.dao.IProjectKeyrolesDao;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.IProjectManagersDao;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.dao.project.MaturityAssessmentDao;
import com.icss.mvp.dao.project.IProjectReviewDao;
import com.icss.mvp.entity.Biweekly;
import com.icss.mvp.entity.ManpowerBudget;
import com.icss.mvp.entity.MeasureComment;
import com.icss.mvp.entity.MonthlyReportStatistics;
import com.icss.mvp.entity.PreassessmentScore;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectKeyroles;
import com.icss.mvp.entity.ProjectQuality;
import com.icss.mvp.entity.ProjectStatus;
import com.icss.mvp.entity.QualifyTrend;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.project.MaturityAssessment;
import com.icss.mvp.entity.project.ProjectLampMode;
import com.icss.mvp.entity.project.ProjectReviewEntity;
import com.icss.mvp.entity.project.ProjectReviewVo;
import com.icss.mvp.entity.project.ProjectWeekLamp;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.service.ManpowerBudgetService;
import com.icss.mvp.service.MeasureCommentService;
import com.icss.mvp.service.ProjectInfoService;
import com.icss.mvp.service.ProjectOverviewService;
import com.icss.mvp.util.BigDecUtils;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.MeasureUtils;
import com.icss.mvp.util.NumberUtil;

/**
 * Created by up on 2019/2/14.
 */
@Service("projectReviewService") public class ProjectReviewService {

    private static Logger logger = Logger.getLogger(ProjectReviewService.class);

    @Resource private HttpServletRequest request;

    @Resource IUserManagerDao userManagerDao;

    @Autowired private IProjectReviewDao projectReviewDao;

    @Autowired private MeasureCommentService measureCommentService;

    @Autowired private ManpowerBudgetService manpowerBudgetService;

    @Autowired private IProjectKeyrolesDao projectKeyrolesDao;

    @Autowired IProjectManagersDao projectManagersDao;

    @Autowired private IProjectListDao projectListDao;

    @Autowired private IProjectInfoDao projectInfoDao;

    @Resource ProjectInfoService projectInfoService;

    @Resource private ProjectOverviewService projectOverviewService;

    @Resource private MaturityAssessmentDao maturityAssessmentDao;

    public List<ProjectReviewEntity> queryProjectReviewTop5(String no) {
        return projectReviewDao.getProjectReviewListTop5(no);
    }

    public List<String> queryProjectCycle() {
        return projectReviewDao.queryProjectCycle();
    }

    public List<String> queryProjectCycleQ(String proNo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ProjectInfo projectInfo = projectListDao.getProjInfoByNoQ(proNo);
        String startDate = sdf.format(projectInfo.getStartDate());
        String endDate = sdf.format(projectInfo.getEndDate());
        return projectReviewDao.queryProjectCycleQ(startDate, endDate);
    }

    public void publishAllProjectReview(String date) {
        List<ProjectInfo> projectInfos = projectInfoDao.queryEffectiveProjects();
        logger.info("获取在行上线项目列表，项目数：" + projectInfos.size());
        for (ProjectInfo project : projectInfos) {
            try {
                logger.info("统计项目当期的周报数据,项目编号：" + project.getNo());
                ProjectReviewEntity entity = getProjectReviewCurrent(project.getNo(), date);
                logger.info("发布项目当期的周报");
                projectReviewDao.editProjectReview(entity);
            } catch (Exception e) {
                logger.error("publishProjectReview exception, NO:" + project.getNo() + " error:", e);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public ProjectReviewEntity getProjectReviewCurrent(String proNo, String date) {
        ProjectReviewEntity entity = projectReviewDao.getProjectReviewDetail(proNo, date);
        if (null == entity) {
            entity = new ProjectReviewEntity();
            entity.setProNo(proNo);
            entity.setStatisticalTime(date);
            entity.setType(projectReviewDao.getProjectTmNoindex(proNo, date));
        }
        changesLamp(entity);
        progressLamp(entity);
        qualityLamp(proNo, date, entity);
        resourcesLamp(proNo, entity, date);
        // 判断是否是无配置指标且是TM项目
        if (!"0".equals(entity.getType()) && !StringUtilsLocal.isBlank(entity.getType())) {
            String resource = entity.getResources();
            resource = StringUtilsLocal.isBlank(resource) ? "0" : resource;
            resource = NumberUtil.stod(resource) > 100 ? "100" : resource;
            entity.setProjectStatus(formatValue(resource));
            entity.setProgressLamp("0");
            entity.setQualityLamp("0");
            entity.setChangesLamp("0");
        } else {
            entity.setProjectStatus(formatValue(projectReviewDao.getProjectStatus(proNo, date)));
        }
        editProjectReviewTemp(entity);
        return entity;
    }

    public int publishProjectReview(String proNo, String date, String review) {
        ProjectReviewEntity entity = getProjectReviewCurrent(proNo, date);
        entity.setProjectReview(review);
        int num = projectReviewDao.editProjectReview(entity);
        return num;
    }

    public void publishCloseProjectReview(String proNo, String date, String review) throws Exception {
        ProjectReviewEntity entity = getProjectReviewCurrent(proNo, date);
        entity.setProjectReview(review);
        entity.setIsClose(1);
        projectReviewDao.editProjectReview(entity);
    }

    public List<String> getDateTimeByNo(String proNo) {
        // 转化时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ProjectInfo projectInfo = projectReviewDao.getDateTimeByNo(proNo);
        String startDate = sdf.format(projectInfo.getStartDate());
        String endDate = sdf.format(projectInfo.getEndDate());
        // 存入list集合
        List<String> result = new ArrayList<String>();
        result.add(startDate);
        result.add(endDate);
        return result;
    }

    public ProjectReviewEntity closeProjectReview(String proNo, String date) {
        ProjectReviewEntity result = null;
        try {
            result = projectReviewDao.getProjectReviewQ(proNo, date);
        } catch (Exception e) {
            logger.error("projectReviewDao.getProjectReviewQ exception, error: " + e.getMessage());
        }
        return result;
    }

    public String getCloseDateByNo(String proNo) {
        String result = null;
        try {
            result = projectReviewDao.getCloseDateByNo(proNo);
        } catch (Exception e) {
            logger.error("projectReviewDao.getCloseDateByNo exception, error: " + e.getMessage());
        }
        return result;
    }

    /**
     * 项目状态关闭
     */
    public void closeProjects(String proNo) {
        try {
            projectReviewDao.closeProjects(proNo);
        } catch (Exception e) {
            logger.error("projectReviewDao.closeProjects exception, error: " + e.getMessage());
        }
    }

    public void changeIsClose(String proNo, String date) {
        try {
            projectReviewDao.changeIsClose(proNo, date);
        } catch (Exception e) {
            logger.error("projectReviewDao.changeIsClose exception, error: " + e.getMessage());
        }
    }

    private int editProjectReviewTemp(ProjectReviewEntity entity) {
        int num = projectReviewDao.editProjectReviewTemp(entity);
        return num;
    }

    private void changesLamp(ProjectReviewEntity entity) {
        List<String> latestCycles = DateUtils.getLatestWeek(2, true, entity.getStatisticalTime());
        // 需求总工作量
        Double totalHours = projectReviewDao.totalHours(entity.getProNo(), latestCycles.get(1), latestCycles.get(0));
        // 需求变更工作量(需求修改)
        Double modifyHours = projectReviewDao.modifyHours(entity.getProNo(), "2", latestCycles.get(1),
                                                          latestCycles.get(0));
        // 需求变更工作量(需求取消)
        Double cancelHours = projectReviewDao.newOrCancelHours(entity.getProNo(), "4", latestCycles.get(1),
                                                               latestCycles.get(0));
        // 需求变更工作量(需求新增)
        Double newHours = projectReviewDao.newOrCancelHours(entity.getProNo(), "3", latestCycles.get(1),
                                                            latestCycles.get(0));
        // 需求总工作量
        String demandNumber = String.valueOf(totalHours == null ? 0 : totalHours);
        // 需求变更工作量
        String changeNumber = String.valueOf(Math.abs(
                (modifyHours == null ? 0 : modifyHours) + (newHours == null ? 0 : newHours) - (
                        cancelHours == null ? 0 : cancelHours)));
        String changes = null;
        if ("0".equals(demandNumber)) {
            changes = "0";
        } else {
            changes = String.valueOf(StringUtilsLocal.keepTwoDecimals(
                    Double.valueOf(changeNumber) / Double.valueOf(demandNumber) * 100));
        }
        entity.setDemandNumber(String.valueOf(StringUtilsLocal.keepTwoDecimals(Double.valueOf(demandNumber))));
        entity.setChangeNumber(String.valueOf(StringUtilsLocal.keepTwoDecimals(Double.valueOf(changeNumber))));
        entity.setChanges(changes);
        int autoValue = 0;
        if (!"".equals(changes) && null != changes) {
            double change = Double.valueOf(changes);
            double change1 = 5;
            double change2 = 10;
            autoValue = getDeng(change < change1, change >= change2);
        }
        entity.setChangesLamp(
                formatAutoValue(entity.getProNo(), entity.getStatisticalTime(), "changes", autoValue + ""));
    }

    @SuppressWarnings("deprecation")
    private void progressLamp(ProjectReviewEntity entity) {
        List<String> latestCycles = DateUtils.getLatestWeek(2, true, entity.getStatisticalTime());
        // 需求完成数
        int demandCompleteCount = projectReviewDao.demandCompletion(entity.getProNo(), "closed", latestCycles.get(1),
                                                                    latestCycles.get(0));
        // 需求总数
        int demandCount = projectReviewDao.demandCompletion(entity.getProNo(), null, latestCycles.get(1),
                                                            latestCycles.get(0));
        // 需求完成率
        String demandCompletion = null;
        if (demandCount == 0) {
            demandCompletion = "0";
        } else {
            demandCompletion = String.valueOf(StringUtilsLocal.keepTwoDecimals(
                    Double.valueOf(demandCompleteCount) / Double.valueOf(demandCount) * 100));
        }
        if (StringUtilsLocal.isBlank(demandCompletion)) {
            demandCompletion = null;
        }
        entity.setDemandCompletion(demandCompletion);
        // 进度完成率
        ProjectInfo projectInfo = projectListDao.getProjInfoByNo(entity.getProNo());
        Date startDate = projectInfo.getStartDate();
        Date endDate = projectInfo.getEndDate();
        Date nowDate = new Date();
        double differ = DateUtils.differenceTime(entity.getStatisticalTime(),
                                                 DateUtils.SHORT_FORMAT_GENERAL.format(nowDate));
        String now = entity.getStatisticalTime();
        if (differ <= 0) {
            now = DateUtils.SHORT_FORMAT_GENERAL.format(nowDate);
        }
        double usedTime = DateUtils.differenceTime(DateUtils.SHORT_FORMAT_GENERAL.format(startDate), now);
        double totalTime = DateUtils.differenceTime(DateUtils.SHORT_FORMAT_GENERAL.format(startDate),
                                                    DateUtils.SHORT_FORMAT_GENERAL.format(endDate));
        double progressCompletion = usedTime / totalTime * 100;
        if (progressCompletion <= 0) {
            progressCompletion = 0;
        } else if (progressCompletion > 100) {
            progressCompletion = 100;
        }
        progressCompletion = StringUtilsLocal.keepTwoDecimals(progressCompletion);
        entity.setProgressCompletion(String.valueOf(progressCompletion));
        double deviation = StringUtilsLocal.parseDouble(demandCompletion) - progressCompletion;
        // 进度偏差率
        double progressDeviation = 0;
        if (deviation != 0 && progressCompletion != 0) {
            progressDeviation = deviation / (progressCompletion) * 100;
            if (progressDeviation > 100) {
                progressDeviation = 100;
            } else if (progressDeviation < -100) {
                progressDeviation = -100;
            }
        }
        entity.setProgressDeviation(StringUtilsLocal.keepTwoDecimals(progressDeviation).toString());
        double value = 10;
        if (progressDeviation < 0) {
            value = (100 + progressDeviation) / 100 * 10;
        }
        String autoValue = StringUtilsLocal.keepTwoDecimals(value).toString();
        entity.setProgressLamp(
                (formatAutoValue(entity.getProNo(), entity.getStatisticalTime(), "progress", autoValue)));
    }

    private double formatPercent(double val) {
        if (val < 0) {
            val = 0;
        } else if (val > 100) {
            val = 100;
        }
        return val;
    }

    private int getDeng(boolean f1, boolean f2) {
        int lamp = 0;
        if (f1) {
            lamp = 85;
        } else if (f2) {
            lamp = 65;
        } else {
            lamp = 75;
        }
        return lamp;
    }

    @SuppressWarnings("unused")
    private void resourcesLamp(String proNo, ProjectReviewEntity entity, String date) {
        Double autoValue = 0.00;
        ManpowerBudget manpowerBudget = manpowerBudgetService.getManpowerBudgetByNo(entity.getProNo());
        if (manpowerBudget != null) {
            if (manpowerBudget.getKeyRoleCount() == null) {
                manpowerBudget.setKeyRoleCount(0);
            }
            if (manpowerBudget.getHeadcount() == null) {
                manpowerBudget.setHeadcount(0);
            }
            List<ProjectKeyroles> keyroles = projectKeyrolesDao.queryProjectKeyrolesByNo(proNo);
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
            String lastWeek = DateUtils.getLatestWeek(1, false, date).get(0);
            DecimalFormat df = new DecimalFormat("##0.00");
            if (keyroles != null && keyroles.size() > 0) {
                Integer all = keyroles.size();
                int num = 0;
                int onDuty = 0;
                int reserve = 0;
                for (ProjectKeyroles keyrole : keyroles) {
                    if ("通过".equals(keyrole.getReplyResults())) {
                        num++;
                    }
                    try {
                        if (StringUtils.isNotBlank(keyrole.getEndDate())) {
                            if ("在岗".equals(keyrole.getStatus()) && !sdt.parse(date).before(
                                    sdt.parse(keyrole.getStartDate()))) {
                                onDuty++;
                            } else if ("后备".equals(keyrole.getStatus()) && !sdt.parse(date).before(
                                    sdt.parse(keyrole.getStartDate()))) {
                                reserve++;
                            }
                        }
                    } catch (ParseException e) {
                        logger.error("onDuty,reserve members fail to obtain:" + e.getMessage());
                    }
                }
                if (num == 0 || all == 0) {
                    entity.setKeyRolesPass("0");
                } else {
                    entity.setKeyRolesPass(
                            StringUtilsLocal.keepTwoDecimals(num / Double.valueOf(all) * 100).toString());
                }
                // 关键角色待答辩数
                int notPass = all - num;
                entity.setKeyRolesfail(notPass);
                // 关键角色匹配缺口
                int rolesDifferNumber = manpowerBudget.getKeyRoleCount() - (onDuty + reserve);
                // int rolesDifferNumber = manpowerBudget.getKeyRoleCount() - (all);
                entity.setKeyRolesDiffer(rolesDifferNumber);
                // 关键角色匹配度
                if (manpowerBudget.getKeyRoleCount() <= 0) {
                    entity.setKeyRolesArrival("--");
                } else {
                    entity.setKeyRolesArrival(df.format(
                            (onDuty + reserve) / Double.valueOf(manpowerBudget.getKeyRoleCount()) * 100).toString());
                }
            } else {
                if (manpowerBudget.getKeyRoleCount() <= 0) {
                    entity.setKeyRolesArrival("--");
                    entity.setKeyRolesDiffer(0);
                } else {
                    entity.setKeyRolesArrival("0.00");
                    // 关键角色匹配缺口
                    entity.setKeyRolesDiffer(manpowerBudget.getKeyRoleCount());
                }
            }
            Map<String, Object> selectMap = projectManagersDao.queryOMPUserSelectedCount(proNo, date);
            int onDutyCount = 0;
            int reserveCount = 0;
            if (null != selectMap) {
                onDutyCount = StringUtils.isNotBlank(
                        StringUtilsLocal.valueOf(selectMap.get("on_duty"))) ? Integer.parseInt(
                        StringUtilsLocal.valueOf(selectMap.get("on_duty"))) : 0;
                reserveCount = StringUtils.isNotBlank(
                        StringUtilsLocal.valueOf(selectMap.get("reserve"))) ? Integer.parseInt(
                        StringUtilsLocal.valueOf(selectMap.get("reserve"))) : 0;
            }
            if (manpowerBudget.getHeadcount() <= 0) {
                entity.setMemberDiffer(0);
                entity.setMemberArrival("--");
            } else {
                entity.setMemberArrival(df.format(
                        (onDutyCount + reserveCount) / Double.valueOf(manpowerBudget.getHeadcount()) * 100).toString());
                // 人力缺口数
                entity.setMemberDiffer(manpowerBudget.getHeadcount() - (onDutyCount + reserveCount));
            }
            String keyRolesArrival = entity.getKeyRolesArrival();
            String memberArrival = entity.getMemberArrival();
            entity.setResources(keyRolesArrival);
            if (!"--".equals(keyRolesArrival)) {
                autoValue += formatPercent(Double.valueOf(keyRolesArrival)) / 100 * 15;
            }
            if (!"--".equals(memberArrival)) {
                autoValue += formatPercent(Double.valueOf(memberArrival)) / 100 * 10;
            }
        }
        entity.setResourcesLamp(formatAutoValue(proNo, entity.getStatisticalTime(), "resources",
                                                StringUtilsLocal.keepTwoDecimals(autoValue).toString()));
    }

    private void qualityLamp(String proNo, String date, ProjectReviewEntity entity) {
        int red = 0;
        int green = 0;
        int yellow = 0;
        String startDate = "";
        if ("15".equals(date.substring(8))) {
            startDate = date.substring(0, 8) + "01";
        } else {
            startDate = date.substring(0, 8) + "16";
        }
        List<MeasureComment> measureComments = measureCommentService.queryMeasureList(proNo, startDate, date);
        List<Map<String, Object>> measureList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> redList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> yellowList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> greenList = new ArrayList<Map<String, Object>>();
        for (MeasureComment measureComment : measureComments) {
            String measureValue = measureComment.getMeasureValue();
            if (null == measureValue || "".equals(measureValue)) {
                continue;
            }
            Integer changeValue = measureComment.getChangeValue();
            String light = "";
            if (null != changeValue && changeValue != 4) {
                switch (changeValue) {
                    case 1:
                        light = "green";
                        break;
                    case 2:
                        light = "yellow";
                        break;
                    case 3:
                        light = "red";
                        break;
                    default:
                        break;
                }
            } else {
                light = MeasureUtils.light(measureComment);
            }

            if (light == null || "".equals(light)) {
                continue;
            }
            String measureName = measureComment.getMeasureName();
            if ("green".equals(light)) {
                green++;
                Map<String, Object> greenMap = new HashMap<>();
                greenMap.put("measureName", measureName);
                greenMap.put("measureValue", measureValue);
                greenList.add(greenMap);
            } else if ("red".equals(light)) {
                red++;
                Map<String, Object> redMap = new HashMap<>();
                redMap.put("measureName", measureName);
                redMap.put("measureValue", measureValue);
                redList.add(redMap);
            } else if ("yellow".equals(light)) {
                yellow++;
                Map<String, Object> yellowMap = new HashMap<>();
                yellowMap.put("measureName", measureName);
                yellowMap.put("measureValue", measureValue);
                yellowList.add(yellowMap);
            }
        }
        measureList.addAll(redList);
        measureList.addAll(yellowList);
        measureList.addAll(greenList);
        if (measureList.size() > 5) {
            measureList = measureList.subList(0, 5);
        } else {
            measureList = measureList.subList(0, measureList.size());
        }
        ObjectMapper om = new ObjectMapper();
        try {
            entity.setMeasureList(om.writeValueAsString(measureList));
        } catch (JsonProcessingException e) {

        }
        double sum = yellow * 2 + green * 5;
        double num = (red + yellow + green) * 5;
        double value = (num == 0 ? 0 : sum / num) * 15;
        Double autoValue = StringUtilsLocal.keepTwoDecimals(value);
        entity.setRedLight(red);
        entity.setGreenLight(green);
        entity.setYellowLight(yellow);
        entity.setQualityLamp(formatAutoValue(proNo, date, "quality", autoValue.toString()));
    }

    private String formatAutoValue(String proNo, String date, String field, String autoValue) {
        ProjectLampMode lampMode = projectReviewDao.getProjectLampMode(proNo, date, field);
        if (null != lampMode) {
            lampMode.setAutoValue(autoValue);
            projectReviewDao.updateProjectLampMode(lampMode);
        } else {
            lampMode = new ProjectLampMode();
            lampMode.setAutoValue(autoValue);
            lampMode.setField(field);
            lampMode.setProNo(proNo);
            lampMode.setStatisticalTime(date);
            lampMode.setLampMode(0);
            projectReviewDao.editProjectLampMode(lampMode);
        }
        return lampMode.getValue();
    }

    public ProjectWeekLamp changeEdit(ProjectReviewEntity entity) {
        ProjectWeekLamp map = new ProjectWeekLamp();
        changesLamp(entity);
        int num = projectReviewDao.changeEdit(entity);
        if (num > 0) {
            map.setLamp(entity.getChangesLamp());
            map.setStatus(
                    formatValue(projectReviewDao.getProjectStatus(entity.getProNo(), entity.getStatisticalTime())));
        }
        return map;
    }

    public ProjectWeekLamp progressEdit(ProjectReviewEntity entity) {
        ProjectWeekLamp map = new ProjectWeekLamp();
        progressLamp(entity);
        int num = projectReviewDao.progressEdit(entity);
        if (num > 0) {
            map.setLamp(entity.getProgressLamp());
            map.setStatus(
                    formatValue(projectReviewDao.getProjectStatus(entity.getProNo(), entity.getStatisticalTime())));
        }
        return map;
    }

    public ProjectWeekLamp lampModeEdit(ProjectLampMode lampMode) {
        ProjectWeekLamp map = new ProjectWeekLamp();
        int num = projectReviewDao.editProjectLampMode(lampMode);
        if (num > 0) {
            projectReviewDao.updateProjectLamp(lampMode.getProNo(), lampMode.getStatisticalTime(),
                                               lampMode.getField() + "_lamp", lampMode.getValue());
            map.setLamp(lampMode.getValue());
            map.setStatus(
                    formatValue(projectReviewDao.getProjectStatus(lampMode.getProNo(), lampMode.getStatisticalTime())));
        }
        return map;
    }

    private String formatValue(String value) {
        int num = value.indexOf(".");
        if (num > 0 && num < value.length() - 3) {
            value = value.substring(0, num + 3);
        }
        return value;
    }

    public List<ProjectReviewEntity> queryProjectReview(String proNo, String statisticalTime, String status) {
        return projectReviewDao.getProjectReviewInfo(proNo, statisticalTime, status);
    }

    public List<ProjectReviewVo> queryOldWeekReview(String proNo, String oldWeekDate) {
        return projectReviewDao.getOldWeekReview(proNo, oldWeekDate);
    }

    public ProjectLampMode getProjectLampMode(String proNo, String date, String field) {
        return projectReviewDao.getProjectLampMode(proNo, date, field);
    }

    public ProjectWeekLamp getProjectReviewTop6(ProjectInfo projectInfo) {
        ProjectWeekLamp map = new ProjectWeekLamp();
        List<String> listMonth = cycleTime(projectInfo.getMonth());// 获取时间一组周期
        List<String> listMonth1 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String str = listMonth.get(i);
            String strmonth = str.substring(5, 7);
            String strday = str.substring(8, 10);
            listMonth1.add(strmonth + "." + strday);
        }
        List<Object> listRed = new ArrayList<>();
        List<Object> listYellow = new ArrayList<>();
        List<Object> listGreen = new ArrayList<>();
        for (int i = 0; i < listMonth.size(); i++) {
            String month = (String) listMonth.get(i);
            int redCount = getProjectCount(projectInfo, month, "0");
            int yellowCount = getProjectCount(projectInfo, month, "1");
            int greenCount = getProjectCount(projectInfo, month, "4");
            listRed.add(redCount);
            listYellow.add(yellowCount);
            listGreen.add(greenCount);
        }
        map.setRed(listRed);
        map.setYellow(listYellow);
        map.setGreen(listGreen);
        map.setMonthList(listMonth1);
        return map;
    }

    public Integer queryProjectReviewTop6(String month, ProjectInfo projectInfo, int num) {
        Map<String, Object> map = new HashMap<>();
        projectInfoService.setParamNew(projectInfo, null, map);
        int yellow = 79;
        int green = 80;
        int red = 70;
        map.put("red", red);
        map.put("month", month);
        map.put("num", num);
        map.put("yellow", yellow);
        map.put("green", green);
        return projectReviewDao.queryProjectReviewTop6(map);
    }

    public ProjectWeekLamp querytwoWeekState(ProjectInfo projectInfo) {
        ProjectWeekLamp map = new ProjectWeekLamp();
        List<String> listMonth = cycleTime(projectInfo.getMonth());// 获取时间一组周期
        List<String> listMonth1 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String str = listMonth.get(i);
            String strmonth = str.substring(5, 7);
            String strday = str.substring(8, 10);
            listMonth1.add(strmonth + "." + strday);
        }
        List<Object> redList = new ArrayList<>();
        List<Object> yellowList = new ArrayList<>();
        for (int i = 0; i < listMonth.size(); i++) {
            String month = (String) listMonth.get(i);
            Integer red1 = 0;
            red1 = getProjectCount(projectInfo, month, "2");
            Integer yellow1 = 0;
            yellow1 = getProjectCount(projectInfo, month, "3");
            redList.add(red1);
            yellowList.add(yellow1);
        }
        map.setRed(redList);
        map.setYellow(yellowList);
        map.setMonthList(listMonth1);
        return map;
    }

    public Integer getState(String month, String username, ProjectInfo projectInfo, int num, String month2) {
        Map<String, Object> map = new HashMap<>();
        projectInfoService.setParamNew(projectInfo, null, map);
        map.put("month", month);
        map.put("month2", month2);
        map.put("num", num);
        int yellow = 79;
        int green = 80;
        int red = 70;
        map.put("red", red);
        map.put("yellow", yellow);
        map.put("green", green);
        return projectReviewDao.getState(map);
    }

    public Integer queryLastPeriod(String month, ProjectInfo projectInfo, int num) {
        String username = CookieUtils.value(request, CookieUtils.USER_NAME);
        Map<String, Object> map = new HashMap<>();
        projectInfoService.setParamNew(projectInfo, username, map);
        map.put("month", month);
        map.put("num", num);
        int red = 70;
        int yellow = 79;
        int green = 80;
        map.put("yellow", yellow);
        map.put("green", green);
        map.put("red", red);
        return projectReviewDao.queryLastPeriod(map);
    }

    public Integer twoWeekPeriod(String month, ProjectInfo projectInfo, int num) {
        String username = CookieUtils.value(request, CookieUtils.USER_NAME);
        Map<String, Object> map = new HashMap<>();
        projectInfoService.setParamNew(projectInfo, username, map);
        map.put("month", month);
        map.put("num", num);
        int yellow = 79;
        int green = 80;
        int red = 70;
        map.put("red", red);
        map.put("yellow", yellow);
        map.put("green", green);
        return projectReviewDao.twoWeekPeriod(map);
    }

    public List<MeasureComment> getMeasureCommentList(String id, String month) {
        return projectReviewDao.getMeasureCommentList(id, month);
    }

    public PreassessmentScore queryProjectExpect(ProjectInfo projectInfo) {
        PreassessmentScore data = new PreassessmentScore();
        try {
            Map<String, Object> maps = new HashMap<>();
            projectInfoService.setParamNew(projectInfo, null, maps);
            String current = DateUtils.getCurrentWeek();// 本周日期
            String lastMonth = DateUtils.lastCycleMonthEnd();// 上月月底
            String lastWeek = DateUtils.getLastWeekEnd();// 上周日期
            maps.put("month", current);
            MonthlyReportStatistics map = projectReviewDao.queryProjectExpect(maps);
            maps.put("month", lastMonth);
            MonthlyReportStatistics mapMonth = projectReviewDao.queryProjectExpect(maps);
            MonthlyReportStatistics mapWeek = new MonthlyReportStatistics();
            if (lastMonth.equals(lastWeek)) {
                mapWeek = mapMonth;
            } else {
                maps.put("month", lastWeek);
                mapWeek = projectReviewDao.queryProjectExpect(maps);
            }
            data.setAverage(Division(map.getStatus(), map.getNum()));
            data.setProcess(Division(map.getQuality(), map.getNum()));
            data.setDeliver(Division(map.getProgress(), map.getNum()));
            data.setPersonnel(Division(map.getResources(), map.getNum()));
            data.setLastMonthAverage(
                    (double) Math.round((data.getAverage() - (Division(mapMonth.getStatus(), mapMonth.getNum()))) * 100)
                    / 100);
            data.setLastMonthProcess((double) Math.round(
                    (data.getProcess() - (Division(mapMonth.getQuality(), mapMonth.getNum()))) * 100) / 100);
            data.setLastMonthDeliver((double) Math.round(
                    (data.getDeliver() - (Division(mapMonth.getProgress(), mapMonth.getNum()))) * 100) / 100);
            data.setLastMonthPersonnel((double) Math.round(
                    (data.getPersonnel() - (Division(mapMonth.getResources(), mapMonth.getNum()))) * 100) / 100);
            data.setLastAverage(
                    (double) Math.round((data.getAverage() - (Division(mapWeek.getStatus(), mapWeek.getNum()))) * 100)
                    / 100);
            data.setLastProcess(
                    (double) Math.round((data.getProcess() - (Division(mapWeek.getQuality(), mapWeek.getNum()))) * 100)
                    / 100);
            data.setLastDeliver(
                    (double) Math.round((data.getDeliver() - (Division(mapWeek.getProgress(), mapWeek.getNum()))) * 100)
                    / 100);
            data.setLastPersonnel((double) Math.round(
                    (data.getPersonnel() - (Division(mapWeek.getResources(), mapWeek.getNum()))) * 100) / 100);
        } catch (Exception e) {
            logger.error(e);
        }
        return data;
    }

    public Biweekly averageBrokenline(ProjectInfo projectInfo) {
        Biweekly data = new Biweekly();
        try {
            Map<String, Object> maps = new HashMap<>();
            projectInfoService.setParamNew(projectInfo, null, maps);
            List<String> dates = new ArrayList<>();// 获取时间集合
            Calendar cale = Calendar.getInstance();
            Integer day = cale.get(Calendar.DATE);
            if (day > 15) {
                dates.add(DateUtils.getMidMonth());
            }
            for (int i = 1; i < 5; i++) {
                dates.add(DateUtils.getWindMonthEnd(i));
                dates.add(DateUtils.getWindMonthMid(i));
            }
            List<MonthlyReportStatistics> reports = new ArrayList<>();
            List<String> date = new ArrayList<>();// 时间集合
            List<String> thisMonth = new ArrayList<>();// 本月数据集合
            List<String> lastMonth = new ArrayList<>();// 同比上月数据集合
            for (String time : dates) {
                maps.put("month", time);
                MonthlyReportStatistics map = projectReviewDao.queryProjectExpect(maps);
                reports.add(map);
            }
            for (int i = 0; i < 6; i++) {
                MonthlyReportStatistics map = reports.get(i);
                String time = dates.get(i).substring(5, 7) + "." + dates.get(i).substring(8, 10);
                date.add(time);
                Integer last = Integer.parseInt(dates.get(i).substring(8, 10));
                int m = i + 1;
                if (last > 15) {
                    m = i + 2;
                }
                MonthlyReportStatistics map2 = reports.get(m);
                double div = 0;
                if (projectInfo.getNumber() == 0) {
                    div = Division(map.getStatus(), map.getNum());
                    thisMonth.add(String.valueOf(div));
                    lastMonth.add(String.valueOf(
                            (double) Math.round((div - Division(map2.getStatus(), map2.getNum())) * 100) / 100));
                } else if (projectInfo.getNumber() == 1) {
                    div = Division(map.getQuality(), map.getNum());
                    thisMonth.add(String.valueOf(div));
                    lastMonth.add(String.valueOf(
                            (double) Math.round((div - Division(map2.getQuality(), map2.getNum())) * 100) / 100));
                } else if (projectInfo.getNumber() == 2) {
                    div = Division(map.getProgress(), map.getNum());
                    thisMonth.add(String.valueOf(div));
                    lastMonth.add(String.valueOf(
                            (double) Math.round((div - Division(map2.getProgress(), map2.getNum())) * 100) / 100));
                } else if (projectInfo.getNumber() == 3) {
                    div = Division(map.getResources(), map.getNum());
                    thisMonth.add(String.valueOf(div));
                    lastMonth.add(String.valueOf(
                            (double) Math.round((div - Division(map2.getResources(), map2.getNum())) * 100) / 100));
                }
            }
            data.setDate(Reversal(date));
            data.setThisMonth(Reversal(thisMonth));
            data.setLastMonth(Reversal(lastMonth));
        } catch (Exception e) {
            logger.error(e);
        }
        return data;
    }

    public List<String> Reversal(List<String> data) {
        for (int j = 0; j < data.size() / 2; j++) {
            String temp = data.get(j);
            data.set(j, data.get(data.size() - 1 - j));
            data.set((data.size() - 1 - j), temp);
        }
        return data;
    }

    public double Division(double m, double d) {
        if (d > 0) {
            return (double) Math.round((m / d) * 100) / 100;
        } else {
            return 0;
        }
    }

    @SuppressWarnings("rawtypes")
    public TableSplitResult<List<ProjectStatus>> actualTableSave(ProjectInfo projectInfo,
                                                                                               PageRequest pageRequest) {
        TableSplitResult<List<ProjectStatus>> ret = new TableSplitResult<List<ProjectStatus>>();
        try {
            Map<String, Object> maps = new HashMap<>();
            projectInfoService.setParamNew(projectInfo, null, maps);
            String current = DateUtils.getCurrentWeek();
            maps.put("month", current);
            maps.put("num", projectInfo.getNumber());
            com.github.pagehelper.Page page = PageHelper.startPage(
                    (pageRequest.getPageNumber() == null ? 0 : (pageRequest.getPageNumber() - 1)) + 1,
                    pageRequest.getPageSize());
            List<Map<String, Object>> list = projectReviewDao.actualTableSave(maps);
            ret.setTotal((int) page.getTotal());
            List<ProjectStatus> data = new ArrayList<>();
            for (Map<String, Object> map : list) {
                ProjectStatus status = new ProjectStatus();
                Object obj = map.get("NO");
                String no = obj == null ? "" : String.valueOf(obj);
                status.setNo(no);
                obj = map.get("NAME");
                String name = obj == null ? "" : String.valueOf(obj);
                status.setName(name);
                if ("0".equals(projectInfo.getClientType())) {
                    obj = map.get("HWZPDU");
                    String hwzpdu = obj == null ? "" : String.valueOf(obj);
                    status.setHwzpdu(hwzpdu);
                    obj = map.get("PDU_SPDT");
                    String pduSpdt = obj == null ? "" : String.valueOf(obj);
                    status.setPduSpdt(pduSpdt);
                } else if ("1".equals(projectInfo.getClientType())) {
                    obj = map.get("PDU");
                    String pdu = obj == null ? "" : String.valueOf(obj);
                    status.setHwzpdu(pdu);
                    obj = map.get("DU");
                    String du = obj == null ? "" : String.valueOf(obj);
                    status.setPduSpdt(du);
                }
                obj = map.get("project_status");
                double statu = obj == null ? 0 : BigDecUtils.round(Double.parseDouble(obj.toString()), 2);
                obj = map.get("quality_lamp");
                double quality = obj == null ? 0 : BigDecUtils.round(Double.parseDouble(obj.toString()), 2);
                obj = map.get("progress_lamp");
                double progress = obj == null ? 0 : BigDecUtils.round(Double.parseDouble(obj.toString()), 2);
                obj = map.get("resources_lamp");
                double resources = obj == null ? 0 : BigDecUtils.round(Double.parseDouble(obj.toString()), 2);
                switch (projectInfo.getNumber()) {
                    case 0:
                        status.setStatusAssessment(statu);
                        break;
                    case 1:
                        status.setStatusAssessment(quality);
                        break;
                    case 2:
                        status.setStatusAssessment(progress);
                        break;
                    case 3:
                        status.setStatusAssessment(resources);
                        break;
                }
                data.add(status);
            }
            ret.setRows(data);
            ret.setPage(pageRequest.getPageNumber());
        } catch (Exception e) {
            logger.error(e);
        }
        return ret;
    }

    public List<ProjectQuality> queryQualityState(ProjectInfo projectInfo, int[] Measure_List, String measureMark,
                                                  List<Integer> deleteList) throws ParseException {
        List<String> list = DateUtils.getLatestCycles(7, true);
        List<String> list1 = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            list1.add(list.get(i));
        }
        List<Integer> no = new ArrayList<>();
        if ("163".equals(measureMark)) {
            for (int measureId : Measure_List) {
                no.add(measureId);
            }
        } else {
            for (int measureId : Measure_List) {
                if (null != deleteList && deleteList.size() > 0) {
                    if (!deleteList.contains(measureId)) {
                        no.add(measureId);
                    }
                }
            }
        }
        List<ProjectQuality> listxx = new ArrayList<>();
        Map<Integer, int[]> measureList2 = new HashMap<>();
        List<Map<String, Object>> measureNameList = projectReviewDao.getMeasuresName(no, measureMark);
        if ("163".equals(measureMark)) {
            for (int measureId : Measure_List) {
                measureList2.put(measureId, new int[] { 0, 0, 0 });
            }
        } else {
            for (Map<String, Object> version : measureNameList) {
                if (StringUtils.isNotBlank(StringUtilsLocal.valueOf(version.get("VERSION")))) {
                    measureList2.put(Integer.parseInt(StringUtilsLocal.valueOf(version.get("VERSION"))),
                                     new int[] { 0, 0, 0 });
                }
            }
        }
        Map<String, Object> maps = new HashMap<>();
        projectInfoService.setParamNew(projectInfo, null, maps);
		/*if ("164".equals(measureMark)
				&& DateUtils.isLastDayOfMonth(DateUtils.SHORT_FORMAT_GENERAL.parse(list.get(0)))) {
			maps.put("nowDate", list1.get(1));
		} else {
			maps.put("nowDate", list1.get(0));
		}*/
        if ("164".equals(measureMark)) {
            if (DateUtils.isLastDayOfMonth(DateUtils.SHORT_FORMAT_GENERAL.parse(list.get(0)))) {
                maps.put("nowDate", list1.get(1));
            } else {
                maps.put("nowDate", list1.get(0));
            }
        } else if ("163".equals(measureMark)) {
            maps.put("nowDate", list.get(1));
        }
        maps.put("no", no);
        maps.put("measureMark", measureMark);
        List<QualifyTrend> qualifyTrend = projectReviewDao.queryQualifyTrend(maps);
        //		List<QualifyTrend> qualifyTrend = new ArrayList<>();
		/*List<String> nos = projectReviewDao.isKxProject();
		if ("164".equals(measureMark)) {
			if (nos != null && nos.size() > 0) {
				for (QualifyTrend qt : qualifyTrendTemp) {
					if (nos.contains(qt.getNo())) {
						qualifyTrend.add(qt);
					}
				}
			}
		} else {
			qualifyTrend = qualifyTrendTemp;
		}*/
        for (QualifyTrend qualify : qualifyTrend) {
            Integer id = null;
            if ("163".equals(measureMark)) {
                id = qualify.getMeasureId();
            } else {
                id = qualify.getVersion();
            }
            if (!measureList2.containsKey(id)) {
                continue;
            }
            int[] values = measureList2.get(id);
            if (qualify.getQualify() == 0) {
                values[0]++;
            } else if (qualify.getQualify() == 1) {
                values[1]++;
            }
            measureList2.put(id, values);
        }
        Map<String, Object> maps1 = new HashMap<>();
        projectInfoService.setParamNew(projectInfo, null, maps1);
        String[] date = list1.get(0).split("-");
        String lastDate = "";
        if ("164".equals(measureMark)) {
            if ("15".equals(date[2])) {
                lastDate = list1.get(3);
            } else {
                lastDate = list1.get(2);
            }
        } else if ("163".equals(measureMark)) {
            lastDate = list1.get(2);
        }
        maps1.put("lastDate", lastDate);
        maps1.put("no", no);
        maps1.put("measureMark", measureMark);
        List<QualifyTrend> qualifyTrendLastMonth = projectReviewDao.queryQualifyTrendLastMonth(maps1);
        //		List<QualifyTrend> qualifyTrendLastMonth = new ArrayList<>();
		/*if ("164".equals(measureMark)) {
			if (nos != null && nos.size() > 0) {
				for (QualifyTrend qt : qualifyTrendLastMonthTemp) {
					if (nos.contains(qt.getNo())) {
						qualifyTrendLastMonth.add(qt);
					}
				}
			}
		} else {
			qualifyTrendLastMonth = qualifyTrendLastMonthTemp;
		}*/
        for (QualifyTrend lastQualify : qualifyTrendLastMonth) {
            Integer id = null;
            if ("163".equals(measureMark)) {
                id = lastQualify.getMeasureId();
            } else {
                id = lastQualify.getVersion();
            }
            if (!measureList2.containsKey(id)) {
                continue;
            }
            int[] values = measureList2.get(id);
            if (lastQualify.getQualify() == 1) {
                values[2]++;
            }
            measureList2.put(id, values);
        }
        for (Map<String, Object> map : measureNameList) {
            for (Entry<Integer, int[]> entry : measureList2.entrySet()) {
                ProjectQuality projectQuality = new ProjectQuality();
                Integer id = entry.getKey();
                if (null == id) {
                    continue;
                }
                int[] a = entry.getValue();
                projectQuality.setId(id);
                if ("163".equals(measureMark)) {
                    String measureId = StringUtilsLocal.valueOf(map.get("ID"));
                    if (measureId.equals(String.valueOf(id))) {
                        projectQuality.setName(StringUtils.isNotBlank(
                                StringUtilsLocal.valueOf(map.get("NAME"))) ? StringUtilsLocal.valueOf(
                                map.get("NAME")) : "");
                    } else {
                        continue;
                    }
                } else {
                    String version = StringUtilsLocal.valueOf(map.get("VERSION"));
                    if (version.equals(String.valueOf(id))) {
                        projectQuality.setName(StringUtils.isNotBlank(
                                StringUtilsLocal.valueOf(map.get("NAME"))) ? StringUtilsLocal.valueOf(
                                map.get("NAME")) : "");
                    } else {
                        continue;
                    }
                }
                if (a == null) {
                    projectQuality.setNowmonth(0);
                    projectQuality.setGreen(0);
                    projectQuality.setLastmonth(0);
                } else {
                    projectQuality.setNowmonth(a[1]);
                    projectQuality.setGreen(a[0]);
                    projectQuality.setLastmonth(a[1] - a[2]);
                }
                listxx.add(projectQuality);
            }
        }
        return listxx;
    }

    public List<ProjectQuality> queryKXqualityState(ProjectInfo projectInfo, PageRequest pageRequest,
                                                    String measureMark) throws ParseException {
        List<Integer> mids = projectReviewDao.getKXmeasure("630");// 高斯-欧拉可信指标
        int[] su = new int[mids.size()];
        for (int i = 0; i < mids.size(); i++) {
            su[i] = mids.get(i);
        }
        List<Integer> deleteList = Arrays.asList(653, 654, 655, 656, 657, 661, 662, 665);
        return queryQualityState(projectInfo, su, measureMark, deleteList);
    }

    public ProjectWeekLamp queryQualityEcahrs(ProjectInfo projectInfo, PageRequest pageRequest, String measureMark)
            throws ParseException {
        ProjectWeekLamp map = new ProjectWeekLamp();
        List<String> list = DateUtils.getLatestCycles(9, false);
        List<String> listMonth1 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String str = list.get(i);
            String strmonth = str.substring(5, 7);
            String strday = str.substring(8, 10);
            listMonth1.add(strmonth + "." + strday);
        }
        int no = projectInfo.getNumber();
        // 八个双周的非绿灯项目数
        List<Integer> listechars = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String date = "";
            if ("164".equals(measureMark) && Integer.valueOf(list.get(0).split("-")[2]) == 15) {
                date = list.get(i + 1);
            } else {
                date = list.get(i);
            }
            Integer qualifyTrend = getQualityEcahr(projectInfo, date, no, measureMark);
            listechars.add(null == qualifyTrend ? 0 : qualifyTrend);
        }
        List<Integer> lastMonth = new ArrayList<>();
        if ("163".equals(measureMark)) {
            for (int i = 0; i < 6; i++) {
                lastMonth.add(listechars.get(i) - listechars.get(i + 2));
            }
            map.setEchars(listechars);
            map.setLastMonth(lastMonth);
            map.setListMonth1(listMonth1);
        } else if ("164".equals(measureMark)) {
            List<Integer> echarList = new ArrayList<>();
            List<String> dateList = new ArrayList<>();
            List<String> dateRes = new ArrayList<>();
            if (Integer.valueOf(list.get(0).split("-")[2]) == 15) {
                dateList.add(list.get(1));
                dateList.add(list.get(3));
                dateList.add(list.get(5));
            } else {
                dateList.add(list.get(0));
                dateList.add(list.get(2));
                dateList.add(list.get(4));
            }
            for (String ele : dateList) {
                String[] dateArr = ele.split("-");
                dateRes.add(dateArr[0] + "-" + dateArr[1]);
            }
            echarList.add(listechars.get(0));
            echarList.add(listechars.get(2));
            echarList.add(listechars.get(4));
            echarList.add(listechars.get(6));
            for (int i = 0; i < 3; i++) {
                lastMonth.add(echarList.get(i) - echarList.get(i + 1));
            }
            map.setEchars(echarList);
            map.setLastMonth(lastMonth);
            map.setListMonth1(dateRes);
        }
        return map;
    }

    private Integer getQualityEcahr(ProjectInfo projectInfo, String nowDate, int measureId, String measureMark) {
        Map<String, Object> maps = new HashMap<>();
        projectInfoService.setParamNew(projectInfo, null, maps);
        maps.put("nowDate", nowDate);
        maps.put("measureId", measureId);
        maps.put("qualify", "1");
        maps.put("measureMark", measureMark);
        return projectReviewDao.queryQualifyEchar(maps);
    }

    public List<String> cycleTime(String time) {
        List<String> list = new ArrayList<>();
        String[] date = time.split("-");// 拆分时间
        int day = Integer.valueOf(date[2]);
        int n = 5;
        list.add(time);
        if (day > 15) {
            list.add(DateUtils.getWindMonthMid(0, time));
            n = 4;
        }
        for (int i = 1; i < n; i++) {
            list.add(DateUtils.getWindMonthEnd(i, time));
            list.add(DateUtils.getWindMonthMid(i, time));
        }
        return list;
    }

    @SuppressWarnings("deprecation")
    public ProjectWeekLamp queryZonglanEcahrs(ProjectInfo projectInfo) {
        ProjectWeekLamp map = new ProjectWeekLamp();
        List<Integer> listRed = new ArrayList<>();
        List<Integer> listYellow = new ArrayList<>();
        List<Integer> twoWeekRedList = new ArrayList<>();
        List<Integer> twoWeekYellowList = new ArrayList<>();
        Date date = new Date();
        int day = date.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        List<String> listMonth = DateUtils.getLatestCycles(8, false);
        List<String> listMonth1 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String str = listMonth.get(i);
            String strmonth = str.substring(5, 7);
            String strday = str.substring(8, 10);
            listMonth1.add(strmonth + "." + strday);
        }
        List<Integer> lastMonthRed = new ArrayList<>();
        // 同上月预警对比
        List<Integer> lastMonthYellow = new ArrayList<>();
        List<Integer> lastMonthTwoWeekRed = new ArrayList<>();
        List<Integer> lastMonthTwoWeekYellow = new ArrayList<>();
        for (int i = 0; i < listMonth.size(); i++) {
            String month = (String) listMonth.get(i);
            // 六周风险项目数量
            listRed.add(getProjectCount(projectInfo, month, "0"));
            // 六周预警项目数量
            listYellow.add(getProjectCount(projectInfo, month, "1"));
            // 连续两周风险
            twoWeekRedList.add(getProjectCount(projectInfo, month, "2"));
            // 连续两周预警
            twoWeekYellowList.add(getProjectCount(projectInfo, month, "3"));
        }
        for (int i = 0; i < 6; i++) {
            countLamp(i, (day < 16 ? getFormerStaticsType(i) : getLatterStaticsType(i)), listRed, listYellow,
                      twoWeekRedList, twoWeekYellowList, lastMonthRed, lastMonthYellow, lastMonthTwoWeekRed,
                      lastMonthTwoWeekYellow);
        }
        if (projectInfo.getNumber() == 0) {
            map.setEchars(listRed);
            map.setLastMonth(lastMonthRed);
        } else if (projectInfo.getNumber() == 1) {
            map.setEchars(listYellow);
            map.setLastMonth(lastMonthYellow);
        } else if (projectInfo.getNumber() == 2) {
            map.setEchars(twoWeekRedList);
            map.setLastMonth(lastMonthTwoWeekRed);
        } else if (projectInfo.getNumber() == 3) {
            map.setEchars(twoWeekYellowList);
            map.setLastMonth(lastMonthTwoWeekYellow);
        }
        map.setMonthList(listMonth1);
        return map;
    }

    private void countLamp(int current, int former, List<Integer> listRed, List<Integer> listYellow,
                           List<Integer> twoWeekRedList, List<Integer> twoWeekYellowList, List<Integer> lastMonthRed,
                           List<Integer> lastMonthYellow, List<Integer> lastMonthTwoWeekRed,
                           List<Integer> lastMonthTwoWeekYellow) {
        int monthRed = listRed.get(current) - listRed.get(former);
        int monthYellow = listYellow.get(current) - listYellow.get(former);
        int monthTwoWeekRed = twoWeekRedList.get(current) - twoWeekRedList.get(former);
        int monthTwoWeekYellow = twoWeekYellowList.get(current) - twoWeekYellowList.get(former);
        lastMonthTwoWeekYellow.add(monthTwoWeekYellow);
        lastMonthTwoWeekRed.add(monthTwoWeekRed);
        lastMonthRed.add(monthRed);
        lastMonthYellow.add(monthYellow);
    }

    @SuppressWarnings("deprecation")
    public List<ProjectQuality> queryProjectState(ProjectInfo projectInfo) {
        List<String> list = new ArrayList<>();
        Date date = new Date();
        int day = date.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        list = DateUtils.getLatestCycles(8, false);
        List<String> listMonth = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listMonth.add(list.get(i));
        }
        List<Integer> listRed = new ArrayList<>();
        List<Integer> listYellow = new ArrayList<>();
        List<Integer> twoWeekRedList = new ArrayList<>();
        List<Integer> twoWeekYellowList = new ArrayList<>();
        // 同上月风险对比
        List<Integer> lastMonthRed = new ArrayList<>();
        // 同上月预警对比
        List<Integer> lastMonthYellow = new ArrayList<>();
        List<Integer> lastMonthTwoWeekRed = new ArrayList<>();
        List<Integer> lastMonthTwoWeekYellow = new ArrayList<>();
        List<ProjectQuality> aList = new ArrayList<>();
        ProjectQuality p1 = new ProjectQuality();
        ProjectQuality p2 = new ProjectQuality();
        ProjectQuality p3 = new ProjectQuality();
        ProjectQuality p4 = new ProjectQuality();
        for (int i = 0; i < listMonth.size(); i++) {
            String month = (String) listMonth.get(i);
            // 六周风险项目数量
            listRed.add(getProjectCount(projectInfo, month, "0"));
            // 六周预警项目数量
            listYellow.add(getProjectCount(projectInfo, month, "1"));
            // 连续两周风险
            twoWeekRedList.add(getProjectCount(projectInfo, month, "2"));
            // 连续两周预警
            twoWeekYellowList.add(getProjectCount(projectInfo, month, "3"));
        }
        String month1 = listMonth.get(0);
        String month3 = listMonth.get(1);
        // 风险和预警项目同上个周期对比
        Integer lastRedCount = listRed.get(0) - listRed.get(1);
        Integer lastYellowCount = listYellow.get(0) - listYellow.get(1);
        // 连续两周风险和预警项目同上个周期对比
        Integer twoWeekRed = getProjectCount(projectInfo, month1, "2") - getProjectCount(projectInfo, month3, "2");
        Integer twoWeekYellow = getProjectCount(projectInfo, month1, "3") - getProjectCount(projectInfo, month3, "3");
        for (int i = 0; i < 6; i++) {
            countLamp(i, (day < 16 ? getFormerStaticsType(i) : getLatterStaticsType(i)), listRed, listYellow,
                      twoWeekRedList, twoWeekYellowList, lastMonthRed, lastMonthYellow, lastMonthTwoWeekRed,
                      lastMonthTwoWeekYellow);
        }
        p1.setNowmonth(listRed.get(0));
        p1.setGreen(lastRedCount);
        p1.setName("风险项目(个)");
        p2.setNowmonth(listYellow.get(0));
        p2.setGreen(lastYellowCount);
        p2.setName("预警项目(个)");
        p3.setNowmonth(twoWeekRedList.get(0));
        p3.setGreen(twoWeekRed);
        p3.setName("连续两周风险项目(个)");
        p4.setNowmonth(twoWeekYellowList.get(0));
        p4.setGreen(twoWeekYellow);
        p4.setName("连续两周预警项目(个)");
        p1.setLastmonth(lastMonthRed.get(0));
        p2.setLastmonth(lastMonthYellow.get(0));
        p3.setLastmonth(lastMonthTwoWeekRed.get(0));
        p4.setLastmonth(lastMonthTwoWeekYellow.get(0));
        aList.add(p1);
        aList.add(p2);
        aList.add(p3);
        aList.add(p4);
        return aList;
    }

    private int getLatterStaticsType(int i) {
        int result = i;
        if (i < 1) {
            result = 1;
        } else if (i >= 1 && i < 3) {
            result = 3;
        } else if (i >= 3 && i < 5) {
            result = 5;
        } else if (i == 5) {
            result = 7;
        }
        return result;
    }

    private int getFormerStaticsType(int i) {
        int result = i;
        if (i <= 1) {
            result = 2;
        } else if (i > 1 && i <= 3) {
            result = 4;
        } else if (i > 3 && i <= 5) {
            result = 6;
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private int getProjectCount(ProjectInfo projectInfo, String month, String type) {
        int result = 0;
        try {
            TableSplitResult response = projectOverviewService.getZongLanTopProject(projectInfo, new PageRequest(),
                                                                                    month, type);
            result = response.getTotal();
        } catch (ParseException e) {
            logger.error(" projectOverviewService.getZongLanTopProject exception, error: " + e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public Map<String, List> queryProjectStateScore(String no) {
        Map<String, List> ret = new HashMap<>();
        List<ProjectReviewEntity> projectReviewEntities = projectReviewDao.getProjectReviewList(no);
        List<String> statisticalTimes = new ArrayList<>();
        List<String> projectStatus = new ArrayList<>();
        for (int i = 0; i < projectReviewEntities.size(); i++) {
            String statisticalTime = projectReviewEntities.get(i).getStatisticalTime();
            statisticalTimes.add(statisticalTime);
            projectStatus.add(projectReviewEntities.get(i).getProjectStatus());
            if ((i + 1) < projectReviewEntities.size()) {
                String statisticalTimeNext = projectReviewEntities.get(i + 1).getStatisticalTime();
                double day = DateUtils.differenceTime(statisticalTime, statisticalTimeNext);
                if (day > 20) {
                    List<String> inHoursCycles = DateUtils.getInHoursCycles(statisticalTime, statisticalTimeNext);
                    for (String inHoursCycle : inHoursCycles) {
                        statisticalTimes.add(inHoursCycle);
                        projectStatus.add("-");
                    }
                }
            }
        }
        ret.put("monthList", statisticalTimes);
        ret.put("valueList", projectStatus);
        return ret;
    }

    @SuppressWarnings("unused")
    public ProjectReviewEntity getMembersStatusRate(String proNo, String date) {
        ProjectReviewEntity projectReview = projectReviewDao.getProjectReviewDetail(proNo, date);
        // ProjectReviewEntity projectReviewEntity = new ProjectReviewEntity();
        // Map<String, Object> map = projectReviewDao.getMembersStatusRate(proNo, date);
        // int manpowerDemand = null != map && map.size() > 0
        // && StringUtils.isNotBlank(StringUtilsLocal.valueOf(map.get("headcount")))
        // ? Integer.parseInt(StringUtilsLocal.valueOf(map.get("headcount")))
        // : 0;
        // int keyManpowerDemand = null != map && map.size() > 0
        // && StringUtils.isNotBlank(StringUtilsLocal.valueOf(map.get("keyRoleCount")))
        // ? Integer.parseInt(StringUtilsLocal.valueOf(map.get("keyRoleCount")))
        // : 0;
        // int keyOnduty = null != map && map.size() > 0
        // && StringUtils.isNotBlank(StringUtilsLocal.valueOf(map.get("key_onDuty")))
        // ? Integer.parseInt(StringUtilsLocal.valueOf(map.get("key_onDuty")))
        // : 0;
        // int keyReserve = null != map && map.size() > 0
        // && StringUtils.isNotBlank(StringUtilsLocal.valueOf(map.get("key_reserve")))
        // ? Integer.parseInt(StringUtilsLocal.valueOf(map.get("key_reserve")))
        // : 0;
        // int keyPerson = null != map && map.size() > 0
        // && StringUtils.isNotBlank(StringUtilsLocal.valueOf(map.get("key_person")))
        // ? Integer.parseInt(StringUtilsLocal.valueOf(map.get("key_person")))
        // : 0;
        // int onduty = null != map && map.size() > 0 &&
        // StringUtils.isNotBlank(StringUtilsLocal.valueOf(map.get("on_duty")))
        // ? Integer.parseInt(StringUtilsLocal.valueOf(map.get("on_duty")))
        // : 0;
        // int reserve = null != map && map.size() > 0 &&
        // StringUtils.isNotBlank(StringUtilsLocal.valueOf(map.get("reserve")))
        // ? Integer.parseInt(StringUtilsLocal.valueOf(map.get("reserve")))
        // : 0;
        // // 关键角色到位率
        // double keyArrival = keyManpowerDemand > 0 ? (double) (keyOnduty + keyReserve)
        // / keyManpowerDemand : -1.0;
        // // 全员到位率
        // double arrival = manpowerDemand > 0 ? (double) (onduty + reserve) /
        // manpowerDemand : -1.0;
        // if (null != projectReview) {
        // // 需求完成率
        // // progressLamp(projectReview);
        // // changesLamp(projectReview);
        // projectReview.setKeyRolesArrival(getResource(keyArrival));
        // projectReview.setMemberArrival(getResource(arrival));
        // projectReview.setKeyRolesDiffer(keyManpowerDemand > 0 && keyManpowerDemand -
        // (keyOnduty + keyReserve) > 0
        // ? keyManpowerDemand - (keyOnduty + keyReserve)
        // : 0);
        // projectReview.setMemberDiffer(
        // manpowerDemand > 0 && manpowerDemand - (onduty + reserve) > 0 ?
        // manpowerDemand - (onduty + reserve)
        // : 0);
        // return projectReview;
        // } else {
        // // projectReviewEntity.setProNo(proNo);
        // // projectReviewEntity.setStatisticalTime(date);
        // // progressLamp(projectReviewEntity);
        // // changesLamp(projectReviewEntity);
        // projectReviewEntity.setKeyRolesArrival(getResource(keyArrival));
        // projectReviewEntity.setMemberArrival(getResource(arrival));
        // projectReviewEntity
        // .setKeyRolesDiffer(keyManpowerDemand > 0 && keyManpowerDemand - (keyOnduty +
        // keyReserve) > 0
        // ? keyManpowerDemand - (keyOnduty + keyReserve)
        // : 0);
        // projectReviewEntity.setMemberDiffer(
        // manpowerDemand > 0 && manpowerDemand - (onduty + reserve) > 0 ?
        // manpowerDemand - (onduty + reserve)
        // : 0);
        // return projectReviewEntity;
        // }
        if(projectReview==null){
            projectReview = new ProjectReviewEntity();
        }
        return projectReview;
    }

    @SuppressWarnings("unused")
    private String getResource(double data) {
        String dataRate = "";
        DecimalFormat df = new DecimalFormat("##0.00");
        if (data >= 1) {
            dataRate = "100.00";
        } else {
            dataRate = df.format(data * 100).toString();
        }
        return dataRate;
    }

    public List<Map<String, Object>> getSelectProjectReview(String no) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            list = projectReviewDao.getSelectProjectReview(no);
        } catch (Exception e) {
            logger.error("projectReviewDao.getSelectProjectReview exception, error: " + e.getMessage());
        }
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
        }
        return list;
    }

    public String getProjectManualValue(String proNo, String date, String template) {
        String result = "";
        String value = "";
        boolean flag = false;
        try {
            if ("1".equals(template)) {
                value = "人力资源";
                flag = true;
            } else {
                value = "关键角色";
            }
            String startTime = measureCommentService.getStartDate(date);
            String endTime = date;
            List<MaturityAssessment> manualValueList = maturityAssessmentDao.getProjectManualValue(proNo, flag,
                                                                                                   startTime, endTime,
                                                                                                   value, template);
            double sum = 0;
            double other = 0;
            if ("2".equals(template)) {
                if (!manualValueList.isEmpty()) {
                    for (MaturityAssessment list : manualValueList) {
                        if (list.getName().equals("关键角色稳定") || list.getName().equals("PM")) {
                            if (list.getResult().equals("2.0")) {
                                sum += 5;
                            } else if (list.getResult().equals("3.0")) {
                                sum += 10;
                            }
                        } else {
                            if (list.getResult().equals("2.0")) {
                                other += 5;
                            } else if (list.getResult().equals("3.0")) {
                                other += 10;
                            }
                        }
                    }
                    sum += other / 4;
                }
            } else {
                if (!manualValueList.isEmpty()) {
                    for (MaturityAssessment list : manualValueList) {
                        if (list.getResult().equals("2.0")) {
                            sum += 5;
                        } else if (list.getResult().equals("3.0")) {
                            sum += 10;
                        }
                    }
                }
            }
            Double resultScore = StringUtilsLocal.keepTwoDecimals(sum);
            result = String.valueOf(resultScore);

        } catch (Exception e) {
            logger.error("getProjectManualValue exception, error:", e);
        }
        return result;
    }
}
