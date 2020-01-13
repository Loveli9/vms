package com.icss.mvp.service.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.icss.mvp.dao.IIsourceIssueTaskDao;
import com.icss.mvp.dao.IProjectSourceConfigDao;
import com.icss.mvp.dao.ISvnTaskDao;
import com.icss.mvp.entity.IsourceIssueInfo;
import com.icss.mvp.entity.RepositoryInfo;
import com.icss.mvp.entity.ScripInfo;
import com.icss.mvp.service.ScripInfoService;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.HttpExecuteUtils;
import com.icss.mvp.util.StringUtilsLocal;

/**
 * @author Ray
 */
@Service
@EnableScheduling
public class IsourceTaskService {

    private static Logger           logger = Logger.getLogger(IsourceTaskService.class);
    @Autowired
    ISvnTaskDao                     dao;
    @Autowired
    private IProjectSourceConfigDao sourceDao;
    @Autowired
    private ScripInfoService        scripInfoService;
    @Autowired
    private IIsourceIssueTaskDao    iSourceIssueTaskDao;

    @Value("${isource_issue_url}")
    public String                   iSourceIssueUrl;

    public int getIssues(String no, String token, String id) {
        int flag = 1;
        List<RepositoryInfo> repositories = sourceDao.searchRepositoryByNos(no, "8", id);

        if (CollectionUtils.isNotEmpty(repositories)) {
            Date projectModifyTime = getProjectModifyTime(no);
            for (RepositoryInfo repoRequest : repositories) {
                Date modifyTime = getLastModifyTime(no, repoRequest.getId(), projectModifyTime);

                String repoUrl = repoRequest.getUrl();
                String repoBranch = repoRequest.getBranch();

                if (StringUtils.isBlank(repoUrl) || StringUtils.isBlank(repoBranch)) {
                    String message = String.format("Url或Branch为空：%s / %s", repoUrl, repoBranch);
                    setScripInfo(no, "更新ISourceIssue数据失败，配置参数异常：" + message, "error", token);
                    logger.error(message);
                    continue;
                }

                Map<String, Object> parameters = new HashMap<>(0);
                String requestUrl = iSourceIssueUrl.replace("@[projects]", repoRequest.getUrl().replace("/", "%2F"));
                parameters.put("private_token", repoRequest.getBranch());
                parameters.put("since", DateUtils.format.format(modifyTime));
                parameters.put("order_by", "created_at");

                String response = HttpExecuteUtils.httpGet(requestUrl, parameters);
                List<Map> responseList = JSON.parseObject(response, List.class);
                if (responseList == null) {
                    String message = String.format("连接超时，采集服务未启动，或者配置链接无法访问：%s", repoUrl);
                    setScripInfo(no, message, "error", token);
                    logger.error(message);
                    continue;
                }

                List<IsourceIssueInfo> issues = new ArrayList<>();
                modifyTime = new Date();

                int i = 0;
                for (Map map : responseList) {
                    IsourceIssueInfo issue = dealIssue(no, modifyTime, map);
                    issues.add(issue);
                    i++;

                    if (i == 100) {
                        iSourceIssueTaskDao.saveLogList(issues);
                        issues.clear();
                        i = 0;
                    }
                }

                if (issues.size() > 0) {
                    iSourceIssueTaskDao.saveLogList(issues);
                }

                Map<String, Object> map = new HashMap<>(0);
                map.put("NO", no);
                map.put("lasttime", modifyTime);
                map.put("id", repoRequest.getId());
                dao.insertlasttime(map);
                flag = 0;
            }
        }
        return flag;
    }

    private IsourceIssueInfo dealIssue(String no, Date createTime, Map map) {
        IsourceIssueInfo issue = new IsourceIssueInfo();
        issue.setNo(no);
        issue.setCreateTime(createTime);
        issue.setIssueId(StringUtilsLocal.valueOf(map.get("id")));
        issue.setLocalId(StringUtilsLocal.valueOf(map.get("local_id")));
        issue.setProjectId(StringUtilsLocal.valueOf(map.get("project_id")));
        issue.setTitle(StringUtilsLocal.valueOf(map.get("title")));
        issue.setDescription(StringUtilsLocal.valueOf(map.get("description")));
        issue.setSeverity(StringUtilsLocal.valueOf(map.get("severity")));
        issue.setIssueType(StringUtilsLocal.valueOf(map.get("issue_type")));
        issue.setState(StringUtilsLocal.valueOf(map.get("state")));

        try {
            issue.setAuthor(StringUtilsLocal.valueOf(((Map) map.get("author")).get("username")));
        } catch (Exception e) {
            logger.error("获取提交人信息异常，请检查：", e);
        }

        try {
            issue.setCreatedAt(DateUtils.ISO_FORMAT_GENERAL.parse(StringUtilsLocal.valueOf(map.get("created_at"))));
            issue.setUpdatedAt(DateUtils.ISO_FORMAT_GENERAL.parse(StringUtilsLocal.valueOf(map.get("created_at"))));
        } catch (Exception e) {
            logger.error("获取时间参数或转换异常，请检查：", e);
        }

        return issue;
    }

    private Date getProjectModifyTime(String projectId) {
        return dao.queryTimeByNo(projectId);
    }

    private Date getLastModifyTime(String projectId, int repositoryId, Date projectTime) {
        Date result = null;
        try {
            result = dao.searchByNo(projectId, String.valueOf(repositoryId));
        } catch (Exception e) {
            logger.warn("dao.searchByNo exception, error: " + e.getMessage());
        }

        return result == null ? projectTime : result;
    }

    private void setScripInfo(String no, String message, String type, String token) {
        ScripInfo info = new ScripInfo();

        info.setNo(no);
        info.setMessage(message);
        info.setMesType(type);
        info.setToken(token);

        scripInfoService.insertErrorMessage(info);
    }
}
