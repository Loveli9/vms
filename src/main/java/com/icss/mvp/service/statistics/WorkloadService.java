package com.icss.mvp.service.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.constant.ELanguage;
import com.icss.mvp.dao.project.IProjectDao;
import com.icss.mvp.dao.statistics.IProgramingDao;
import com.icss.mvp.entity.capacity.AbilityEntity;
import com.icss.mvp.entity.capacity.WorkloadEntity;
import com.icss.mvp.entity.project.ProjectBaseEntity;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.LocalDateUtils;

/**
 * @author Ray
 * @date 2019/2/15
 */
@Service("workloadService")
public class WorkloadService {

    @Autowired
    IProgramingDao        programingDao;

    @Autowired
    IProjectDao           projectDao;

    private static Logger logger = Logger.getLogger(WorkloadService.class);

    public List<WorkloadEntity> summarizeAmountMonthly(String projectId, String codeType, Date beginTime, Date endTime) {
        ELanguage language = ELanguage.getByType(codeType);
        Set<String> types = language != null ? ELanguage.getSuffix(language) : new HashSet<>();

        // Set<String> types = new HashSet<>();
        // if (StringUtils.isNotBlank(codeType) && !"all".equalsIgnoreCase(codeType)) {
        // // 获取不同语言的文件后缀
        // types = FileTypeEnum.FileType.getFileTypeSet(codeType);
        // }

        List<WorkloadEntity> response = summarize(projectId, beginTime, endTime, types);
        if (response == null || response.isEmpty()) {
            response = new ArrayList<>();
        }

        return response;
    }

    public List<WorkloadEntity> summarizeCommitMonthly(String projectId, Date beginTime, Date endTime) {
        List<WorkloadEntity> result = null;
        try {
            Map<String, Date> scale = getTimescale(projectId, beginTime, endTime);
            if (!scale.isEmpty()) {
                String begin = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("StartTime"));
                String end = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("FinishTime"));

                result = programingDao.summarizeCommitMonthly(projectId, begin, end);
            }
        } catch (Exception e) {
            logger.error("programingDao.summarizeCommitMonthly exception, error: " + e.getMessage());
        }

        if (result == null || result.isEmpty()) {
            result = new ArrayList<>();
        }

        return result;
    }

    public List<AbilityEntity> commits(String projectId, String codeType, Date beginTime, Date endTime) {
        ELanguage language = ELanguage.getByType(codeType);
        Set<String> types = language != null ? ELanguage.getSuffix(language) : new HashSet<>();

        List<AbilityEntity> result = null;
        try {
            Map<String, Date> scale = getTimescale(projectId, beginTime, endTime);
            if (!scale.isEmpty()) {
                String begin = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("StartTime"));
                String end = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("FinishTime"));

                result = programingDao.metricCommit(projectId, types, "%Y.%m", begin, end);
            }
        } catch (Exception e) {
            logger.error("programingDao.metricCommit exception, error: " + e.getMessage());
        }

        if (result == null || result.isEmpty()) {
            result = new ArrayList<>();
        }

        return result;
    }

    public Map<String, AbilityEntity> metricCommit(String projectId, String codeType, Date beginTime, Date endTime) {
        List<AbilityEntity> abilities = commits(projectId, codeType, beginTime, endTime);

        return abilities.stream().collect(Collectors.toMap(AbilityEntity::getRelatedAccount, Function.identity()));
    }

    public Map<String, AbilityEntity> metricMonthly(String projectId, Date beginTime, Date endTime) {
        List<AbilityEntity> abilities = metric(projectId, beginTime, endTime);

        return abilities.stream().collect(Collectors.toMap(AbilityEntity::getRelatedAccount, Function.identity()));
    }

    /**
     * 根据项目起止时间获取时间区间
     *
     * @param projectId 项目编号
     * @return
     */
    public Map<String, Date> getTimescale(String projectId, Date beginTime, Date endTime) {
        Date first = beginTime == null ? new Date() : beginTime;
        Date firstNext = endTime == null ? first : endTime;

        ProjectBaseEntity project = null;
        try {
            List<ProjectBaseEntity> projectList = projectDao.getProjectList(Collections.singleton(projectId));
            if (projectList != null && !projectList.isEmpty()) {
                project = projectList.get(0);
            }
        } catch (Exception e) {
            logger.error("projectDao.getProjectList exception, error: " + e.getMessage());
        }

        if (project != null) {
            Date start = project.getBeginDate();
            // 使用 between {begin} and {end} 过滤时间区间，相当于 >= {begin} and < {end}，end 必须多加1天
            Date finish = LocalDateUtils.plusDays(project.getFinishDate(), 1);

            if (!firstNext.after(first)) {
                first = start;
                firstNext = finish;
            } else {
                first = (start != null && start.after(first)) ? start : first;
                firstNext = (finish != null && finish.before(firstNext)) ? finish : firstNext;
            }
        }

        Map<String, Date> result = new HashMap<>();
        result.put("StartTime", first);
        result.put("FinishTime", firstNext);

        return result;
    }

    private List<WorkloadEntity> summarize(String projectId, Date beginTime, Date endTime, Set<String> types) {
        List<WorkloadEntity> result = null;
        try {
            Map<String, Date> scale = getTimescale(projectId, beginTime, endTime);
            if (!scale.isEmpty()) {
                String begin = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("StartTime"));
                String end = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("FinishTime"));

                result = programingDao.summarizeAmountMonthly(projectId, null, types, begin, end);
            }
        } catch (Exception e) {
            logger.error("programingDao.summarizeAmountMonthly exception, error: " + e.getMessage());
        }

        if (result == null || result.isEmpty()) {
            result = new ArrayList<>();
        }

        return result;
    }

    private List<AbilityEntity> metric(String projectId, Date beginTime, Date endTime) {
        List<AbilityEntity> result = null;
        try {
            Map<String, Date> scale = getTimescale(projectId, beginTime, endTime);
            if (!scale.isEmpty()) {
                String begin = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("StartTime"));
                String end = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("FinishTime"));

                result = programingDao.metricRespective(projectId, "%Y.%m", "6", begin, end);
            }
        } catch (Exception e) {
            logger.error("programingDao.metricRespective exception, error: " + e.getMessage());
        }

        if (result == null || result.isEmpty()) {
            result = new ArrayList<>();
        }

        return result;
    }
}
