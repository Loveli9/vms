package com.icss.mvp.service;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.icss.mvp.dao.IJenkinsBuildDetailedDao;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.JenkinsDetailedEntity;
import com.icss.mvp.entity.JenkinsEntity;
import com.icss.mvp.entity.MeasureLoadEveryInfo;
import com.icss.mvp.entity.request.RepositoryRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.HttpExecuteUtils;

@Service
public class JenkinsCollectService {

    private final static Logger logger = Logger.getLogger(JenkinsCollectService.class);

    @Autowired
    private IJenkinsBuildDetailedDao jenkinsBuildDetailedDao;

    @Resource
    HttpServletRequest request;

    @Resource
    HttpServletResponse response;

    @Value("${micro_service_jenkins_url}")
    private String msJenkinsUrl;

    /**
     * jenkins 数据采集
     *
     * @param no
     * @param id
     * @param token
     * @return
     */
    public BaseResponse collect(String no, String id, String token, String url, String jobName, String otherAccount) {
        BaseResponse response = new BaseResponse();
        Set<String> keywords = new HashSet<>();
        RepositoryRequest repoRequest = new RepositoryRequest();
        repoRequest.setUrl(url);
        repoRequest.setRepositoryId(Integer.valueOf(id));
        repoRequest.setProjectId(no);
        repoRequest.setRequestId(token);
        repoRequest.setUserId(jobName);// 暂存项目名称
        if ("0".equals(otherAccount)) {
            keywords.add("jenkinsuser" + id);
            keywords.add("jenkinspass" + id);
            Map<String, String> cookies = CookieUtils.getCookies(request, keywords);
            String username = cookies.get(cookies.containsKey("jenkinsuser" + id) ? "jenkinsuser" + id : "");
            String password = cookies.get(cookies.containsKey("jenkinspass" + id) ? "jenkinspass" + id : "");
            repoRequest.setAccount(username);
            repoRequest.setSecret(password);
        } else {
            repoRequest.setAccount(null);
            repoRequest.setSecret(null);
        }
        try {
            BaseResponse responsecmetrics = jenkins("cmetrics", repoRequest, no);
            BaseResponse responsefindbugs = jenkins("findbugs", repoRequest, no);
            if ("200".equals(responsecmetrics.getCode()) && "200".equals(responsefindbugs.getCode())) {
                response.setMessage("jenkins success");
            } else if ("401".equals(responsecmetrics.getCode())) {
                response.setMessage(responsecmetrics.getMessage());
                response.setCode("401");
            } else if ("402".equals(responsecmetrics.getCode())) {
                response.setMessage(responsecmetrics.getMessage());
                response.setCode("402");
            } else if ("403".equals(responsecmetrics.getCode())) {
                response.setMessage(responsecmetrics.getMessage());
                response.setCode("403");
            } else if ("404".equals(responsecmetrics.getCode())) {
                response.setMessage(responsecmetrics.getMessage());
                response.setCode("404");
            }else if("405".equals(responsecmetrics.getCode())){
                response.setMessage(responsecmetrics.getMessage());
                response.setCode("405");
            }else if("406".equals(responsecmetrics.getCode())){
                response.setMessage(responsecmetrics.getMessage());
                response.setCode("406");
            }
        } catch (Exception e) {
            response.setMessage("fail");
            logger.error("jenkins采集失败" + e.getMessage());
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    private BaseResponse jenkins(String type, RepositoryRequest repoRequest, String no) {
        BaseResponse response = new BaseResponse();
        Map<String, Object> parameters = JSON.parseObject(JSON.toJSONString(repoRequest), Map.class);
        String result = HttpExecuteUtils.httpPost(msJenkinsUrl + "plugin/" + type, parameters);
        if (StringUtils.isNotEmpty(result)) {
            if ("200".equals(JSON.parseObject(result).get("code"))) {
                String data = JSON.parseObject(result).getString("data");
                if (!"".equals(StringUtils.strip(data, "{}"))) {
                    JenkinsEntity jenkins = JSON.parseObject(data, JenkinsEntity.class);
                    savaJenkinsDetailed(jenkins, no, type);
                }
                response.setMessage(JSON.parseObject(result).get("message").toString());
                response.setCode("200");
            } else if ("401".equals(JSON.parseObject(result).get("code"))) {
                response.setMessage(JSON.parseObject(result).get("message").toString());
                response.setCode("401");
            } else if ("402".equals(JSON.parseObject(result).get("code"))) {
                response.setMessage(JSON.parseObject(result).get("message").toString());
                response.setCode("402");
            } else if ("403".equals(JSON.parseObject(result).get("code"))) {
                response.setMessage(JSON.parseObject(result).get("message").toString());
                response.setCode("403");
            } else if ("404".equals(JSON.parseObject(result).get("code"))) {
                response.setMessage(JSON.parseObject(result).get("message").toString());
                response.setCode("404");
            }else if ("405".equals(JSON.parseObject(result).get("code"))) {
                response.setMessage(JSON.parseObject(result).get("message").toString());
                response.setCode("405");
            }else if ("406".equals(JSON.parseObject(result).get("code"))) {
                response.setMessage(JSON.parseObject(result).get("message").toString());
                response.setCode("406");
            }
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    private void savaJenkinsDetailed(JenkinsEntity jenkins, String no, String type) {
        try {
            Map<String, Object> map = null;
            switch (type) {
                case "cmetrics":
                    map = jenkins.getCmetrics();
                    break;
                case "findbugs":
                    map = jenkins.getFindbugs();
                    break;
            }
            map = (Map<String, Object>) map.get("Summary");
            Date createTime = new Date();
            for (String key : map.keySet()) {
                JenkinsDetailedEntity jenkinsDetaile = new JenkinsDetailedEntity();
                jenkinsDetaile.setType(type);
                jenkinsDetaile.setPlatform("jenkins");
                jenkinsDetaile.setTimeStamp(jenkins.getTimeStamp());
                jenkinsDetaile.setNo(no);
                jenkinsDetaile.setIdentity(String.valueOf(jenkins.getNumber()));
                jenkinsDetaile.setCreateTime(createTime);
                jenkinsDetaile.setMeasure(key);
                jenkinsDetaile.setValue(String.valueOf(map.get(key)));
                JenkinsDetailedEntity detailed = jenkinsBuildDetailedDao.queryJenkinsDetail(jenkinsDetaile);
                if (detailed != null) {
                    detailed.setCreateTime(createTime);
                    jenkinsDetaile = detailed;
                }
                jenkinsBuildDetailedDao.insertJenkinsDetailed(jenkinsDetaile);
            }
        } catch (Exception e) {
            logger.error("构建详情获取失败" + e.getMessage());
        }
    }

    public void calculateJenkins(String no, IterationCycle iterationCycle, List<MeasureLoadEveryInfo> measureDetail) {
        Date iteEndDate = iterationCycle.getEndDate();
        Map<String, String> cmetrics = new HashMap<>();
        cmetrics.put("maximumCyclomaticComplexity", "659");
        cmetrics.put("codeSize", "662");
        cmetrics.put("linesPerFile", "661");
        cmetrics.put("fileDuplicationRatio", "664");
        cmetrics.put("codeDuplicationRatio", "670");
        cmetrics.put("dangerousFunctionsTotal", "663");
        cmetrics.put("redundantCodeTotal", "665");
        cmetrics.put("methodLines", "660");
        Set<String> keys = cmetrics.keySet();
        JenkinsDetailedEntity jenkinsDetailes = null;
        for (String key : keys) {
            jenkinsDetailes = jenkinsBuildDetailedDao.calculateJenkins(no, "cmetrics", key);
            if (jenkinsDetailes != null) {
                measureDetail
                        .add(new MeasureLoadEveryInfo(no, iteEndDate, cmetrics.get(key), jenkinsDetailes.getValue()));
            }
        }
        String[] findbugs = {"highPriority", "normalPriority"};
        Integer priority = 0;
        for (String findbug : findbugs) {
            jenkinsDetailes = jenkinsBuildDetailedDao.calculateJenkins(no, "findbugs", findbug);
            priority += jenkinsDetailes == null ? 0 : Integer.valueOf(jenkinsDetailes.getValue());
        }
        if (jenkinsDetailes != null) {
            measureDetail.add(new MeasureLoadEveryInfo(no, iteEndDate, "669", String.valueOf(priority)));
        }
    }

}
