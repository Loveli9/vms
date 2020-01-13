package com.icss.mvp.controller.schedule;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.entity.RepositoryInfo;
import com.icss.mvp.entity.ScripInfo;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.request.RepositoryRequest;
import com.icss.mvp.service.ScripInfoService;
import com.icss.mvp.service.project.ProjectService;
import com.icss.mvp.service.schedule.IsourceTaskService;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.DesUtil;
import com.icss.mvp.util.HttpExecuteUtils;

/**
 * @author Ray
 * @date 2018/8/25
 */
@Controller
@RequestMapping("/acquire")
public class AcquisitionController {

    private static Logger       logger    = Logger.getLogger(AcquisitionController.class);

    private static final String login_key = "DFHSJDFADAS0987";                            // 用户登陆加密密钥

    @Value("${micro_service_svn_url}")
    private String              msSvnUrl;

    @Value("${micro_service_icp_url}")
    private String              icpUrl;

    @Value("${micro_service_dts_url}")
    private String              dtsUrl;

    @Value("${micro_service_clouddragon_url}")
    private String              clouddragon;

    @Resource
    HttpServletRequest          request;

    @Autowired
    private ProjectService      projectService;

    @Autowired
    private ScripInfoService    InfoService;

    // @Autowired
    // private SvnTaskService svnTaskService;

    @Autowired
    private IsourceTaskService  isourceTaskService;

    /**
     * SVN日志采集
     * 
     * @param id
     * @param no
     * @param token
     * @return
     */
    @RequestMapping("/svn")
    @ResponseBody
    public PlainResponse svnRevisionHistory(String id, String no, String token) {
        PlainResponse result = new PlainResponse();
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        RepositoryInfo repository = projectService.getRepository(id);
        if (repository == null || StringUtils.isBlank(repository.getUrl())) {
            result.setError(CommonResultCode.INVALID_PARAMETER, "repositoryId", " Repository is invalid");
            return result;
        }

        Set<String> keywords = new HashSet<>(Arrays.asList("username", "password"));
        if (repository.getOtherAccount() == 0) {
            keywords.add("svnuser" + id);
            keywords.add("svnpass" + id);
        }
        Map<String, String> cookies = CookieUtils.getCookies(request, keywords);
        String username = cookies.get(cookies.containsKey("svnuser" + id) ? "svnuser" + id : "username");
        String password = cookies.get(cookies.containsKey("svnpass" + id) ? "svnpass" + id : "password");
        // cookie Des解密
        try {
            password = cookies.containsKey("svnpass" + id) ? password : DesUtil.decryption(password, login_key);
        } catch (Exception e) {
            logger.error("des解密失败：" + e.getMessage());
        }
        RepositoryRequest repoRequest = new RepositoryRequest();
        // repoRequest.setUrl(repository.getUrl());
        repoRequest.setRepositoryId(Integer.valueOf(id));
        repoRequest.setProjectId(no);
        repoRequest.setRequestId(token);
        repoRequest.setAccount(username);
        repoRequest.setSecret(password);
        repoRequest.setUserId(cookies.get("username"));

        if (StringUtils.isNotBlank(msSvnUrl)) {
            Map<String, Object> parameters = JSON.parseObject(JSON.toJSONString(repoRequest), Map.class);
            String response = HttpExecuteUtils.httpGet(msSvnUrl + "/acquire/quantity", parameters);
            Map responseMap = JSON.parseObject(response, Map.class);
            if (responseMap != null) {
                result.setCode(String.valueOf(responseMap.get("code")));
                result.setMessage(result.getSucceed() ? "开始采集SVN数据。" : String.valueOf(responseMap.get("message")));
            } else {// 插入记录，为svn采集异常增加标识
                ScripInfo info = new ScripInfo();
                info.setMessage("更新SVN数据失败：服务连接超时，请检查配置，或查看服务是否已正常启动:" + repository.getUrl());
                info.setNo(no);
                info.setResult("complete");
                info.setMesType("error");
                info.setToken(token);
                InfoService.insertErrorMessage(info);// 0-svn采集
            }
            // } else {
            // // 开始采集时间
            // int a = svnTaskService.getSvnlog0(no, token, id);
            // // 采集结束时间
            // if (a == 1) {
            // result.setErrorMessage(CommonResultCode.INTERNAL.code, "更新SVN数据失败。");
            // } else {
            // result.setMessage("更新SVN数据成功。");
            // }
        }
        endTime = System.currentTimeMillis();
        result.setData(StringUtilsLocal.formatTime(endTime - startTime));
        return result;
    }

    /**
     * isource_issue采集
     *
     * @param id
     * @param no
     * @param token
     * @return
     */
    @RequestMapping("/isourceIssue")
    @ResponseBody
    public PlainResponse isourceIssueRevisionHistory(String id, String no, String token) {
        PlainResponse result = new PlainResponse();
        long startTime = System.currentTimeMillis();

        // 开始采集时间
        int a = 0;
        try {
            a = isourceTaskService.getIssues(no, token, id);
            // 采集结束时间
            if (a == 1) {
                result.setErrorMessage(CommonResultCode.INTERNAL.code, "更新isourceIssue数据失败。");
            } else {
                result.setCode("success");
                result.setMessage("更新isourceIssue数据成功。");
            }
        } catch (Exception e) {
            logger.error("isourceIssue采集异常：", e);
            result.setErrorMessage(CommonResultCode.INTERNAL.code, "更新isourceIssue数据失败。");
        }

        long endTime = System.currentTimeMillis();
        result.setData(StringUtilsLocal.formatTime(endTime - startTime));
        return result;
    }

    @RequestMapping("/icp-ci")
    @ResponseBody
    public PlainResponse icpCiCollect(String no, String token, String id) {
        PlainResponse result = new PlainResponse();
        RepositoryRequest repoRequest = new RepositoryRequest();
        repoRequest.setRepositoryId(Integer.valueOf(id));
        repoRequest.setProjectId(no);
        repoRequest.setRequestId(token);
        if (StringUtils.isNotBlank(icpUrl)) {
            Map<String, Object> parameters = JSON.parseObject(JSON.toJSONString(repoRequest), Map.class);
            String response = HttpExecuteUtils.httpGet(icpUrl + "/icpci/collectData", parameters);
            Map responseMap = JSON.parseObject(response, Map.class);
            if (responseMap != null) {
                result.setCode(String.valueOf(responseMap.get("code")));
            }
        } else {
            result.setMessage("更新icp异常。");
        }
        return result;
    }

    @RequestMapping("/dts")
    @ResponseBody
    public PlainResponse dtsCollect(String no, String token, String id) {
        PlainResponse result = new PlainResponse();
        RepositoryInfo repository = projectService.getRepository(id);
        if (repository == null || StringUtils.isBlank(repository.getUrl())) {
            result.setError(CommonResultCode.INVALID_PARAMETER, "repositoryId", " Repository is invalid");
            return result;
        }
        Set<String> keywords = new HashSet<>(Arrays.asList("username", "password"));
        if (repository.getOtherAccount() == 0) {
            keywords.add("dtsuser" + id);
            keywords.add("dtspass" + id);
        }
        Map<String, String> cookies = CookieUtils.getCookies(request, keywords);
        String username = cookies.get(cookies.containsKey("dtsuser" + id) ? "dtsuser" + id : "username");
        String password = cookies.get(cookies.containsKey("dtspass" + id) ? "dtspass" + id : "password");
        // cookie Des解密
        try {
            password = cookies.containsKey("dtspass" + id) ? password : DesUtil.decryption(password, login_key);
        } catch (Exception e) {
            logger.error("des解密失败：" + e.getMessage());
        }

        RepositoryRequest repoRequest = new RepositoryRequest();
        repoRequest.setRepositoryId(Integer.valueOf(id));
        repoRequest.setProjectId(no);
        repoRequest.setRequestId(token);
        repoRequest.setAccount(username);
        repoRequest.setSecret(password);
        repoRequest.setUserId(cookies.get("username"));
        if (StringUtils.isNotBlank(dtsUrl)) {
            Map<String, Object> parameters = JSON.parseObject(JSON.toJSONString(repoRequest), Map.class);
            String response = HttpExecuteUtils.httpGet(dtsUrl + "/dts/dtsTask", parameters);
            Map responseMap = JSON.parseObject(response, Map.class);
            if (responseMap != null) {
                result.setCode(String.valueOf(responseMap.get("code")));
            }
        } else {
            result.setMessage("更新dts异常。");
        }
        return result;
    }

    @RequestMapping("/clouddragonCollect")
    @ResponseBody
    public BaseResponse clouddragonCollect(String no, String token, String id) {
        BaseResponse result = new BaseResponse();
        if (StringUtils.isNotBlank(clouddragon)) {
            ScripInfo startInfo = new ScripInfo();
            startInfo.setMessage("开始采集clouddragon数据");
            startInfo.setNo(no);
            startInfo.setResult("");
            startInfo.setMesType("info");
            startInfo.setToken(token);
            InfoService.insertErrorMessage(startInfo);
            Map<String, Object> parameters = projectService.getClouddragonParameters(no, id, token);
            String response = HttpExecuteUtils.httpGet(clouddragon + "/pipeline/collect", parameters);
            result = JSON.parseObject(response, BaseResponse.class);
            ScripInfo endInfo = new ScripInfo();
            endInfo.setMessage(result.getMessage());
            endInfo.setNo(no);
            endInfo.setResult("complete");
            if ("200".equals(result.getCode())) {
                endInfo.setMesType("info");
            } else {
                endInfo.setMesType("error");
            }
            endInfo.setToken(token);
            InfoService.insertErrorMessage(endInfo);
        } else {
            ScripInfo info = new ScripInfo();
            info.setMessage("采集clouddragon数据失败：服务连接超时，请检查配置，或查看服务是否已正常启动:" + id);
            info.setNo(no);
            info.setResult("complete");
            info.setMesType("error");
            info.setToken(token);
            InfoService.insertErrorMessage(info);
            result.setMessage("采集clouddragon数据异常。");
        }

        return result;
    }
}
