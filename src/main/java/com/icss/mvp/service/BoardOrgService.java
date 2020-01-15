package com.icss.mvp.service;

import com.icss.mvp.dao.IGroupBoardDao;
import com.icss.mvp.entity.GroupAcceptanceEntity;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更新看板总览表&验收表信息
 *
 * @author limingming
 */
@Service("BoardOrgService")
@EnableScheduling
@SuppressWarnings("all")
@PropertySource("classpath:task.properties")
public class BoardOrgService {
    private static Logger logger = Logger.getLogger(BoardOrgService.class);

    @Resource
    GroupBoardService groupBoardService;

    @Resource
    IGroupBoardDao groupBoardDao;

    /**
     * 更新看板总览表&验收表信息
     */
    @Scheduled(cron = "${group_overview}")
    public void updateGroupOverview() {
        List<String> dateList = DateUtils.getLatestCycles(2, false);
        String statisticalTime = dateList.get(0);
        try {
            for (int i = 0; i < 2; i++) {
                List<Map<String, Object>> secondOrgList = groupBoardDao.getSecondAndThirdOrg(i, "secondOrg");
                List<Map<String, Object>> thirdOrgList = groupBoardDao.getSecondAndThirdOrg(i, "thirdOrg");
                for (Map<String, Object> secondOrg : secondOrgList) {
                    thirdOrgList.add(secondOrg);
                }

                for (Map<String, Object> thirdOrg : thirdOrgList) {
                    boolean bool = false;

                    for (Map.Entry<String, Object> org : thirdOrg.entrySet()) {
                        if ("secondOrg".equals(org.getKey())) {
                            bool = true;
                            break;
                        }
                    }

                    String orgId = StringUtilsLocal.valueOf(bool ? thirdOrg.get("secondOrgId") : thirdOrg.get("thirdOrgId"));
                    GroupAcceptanceEntity groupAcceptance = new GroupAcceptanceEntity();
                    groupAcceptance.setStatisticalTime(statisticalTime);
                    groupAcceptance.setOrg(StringUtilsLocal.valueOf(bool ? thirdOrg.get("secondOrg") : thirdOrg.get("thirdOrg")));
                    groupAcceptance.setOrgId(StringUtilsLocal.valueOf(bool ? thirdOrg.get("secondOrgId") : thirdOrg.get("thirdOrgId")));

                    String flag = "";
                    if (0 == i) {
                        flag = bool ? "secondOrgHw" : "thirdOrgHw";
                    } else if (1 == i) {
                        flag = bool ? "secondOrgZr" : "thirdOrgZr";
                    }

                    //组织看板总览
                    Integer riskProjectCount = 0;
                    Integer executionCount = groupBoardDao.queryExecutionProjectCount(orgId, statisticalTime, flag);
                    groupAcceptance.setExecutedProject(null != executionCount ? executionCount : 0);

                    List<String> riskRes = groupBoardDao.queryRiskProjectCount(orgId, statisticalTime, flag);
                    for (String s : riskRes) {
                        if (1 == groupBoardService.transformProjectStatus(s)) {
                            riskProjectCount++;
                        }
                    }
                    groupAcceptance.setRiskProject(riskProjectCount);

                    Map<String, Object> parameter = new HashMap<>();
                    parameter.put("orgId", orgId);
                    parameter.put("statisticalTime", statisticalTime);
                    parameter.put("lastTime", dateList.get(1));
                    parameter.put("orgFlag", flag);
                    parameter.put("personFlag", "arrival");

                    Map<String, Object> arrivalMap = bool ? groupBoardDao.querySecondOrgPersonnel(parameter) : groupBoardDao.queryThirdOrgPersonnel(parameter);
                    Integer personnel = null != arrivalMap.get("personnel") ? Integer.valueOf(StringUtilsLocal.valueOf(arrivalMap.get("personnel"))) : 0;
                    if (null != arrivalMap && null != arrivalMap.get("headCount")) {
                        Integer headCount = Integer.valueOf(StringUtilsLocal.valueOf(arrivalMap.get("headCount")));
                        if (null != personnel && (null != headCount && 0 != headCount)) {
                            double arrivalRate = (double) personnel / headCount;
                            groupAcceptance.setPersonnelArrival(String.format("%.2f", arrivalRate));
                        }
                    }

                    parameter.put("personFlag", "stable");
                    Map<String, Object> stableMap = bool ? groupBoardDao.querySecondOrgPersonnel(parameter) : groupBoardDao.queryThirdOrgPersonnel(parameter);
                    if (null != stableMap && null != stableMap.get("personnel") && null != personnel) {
                        Integer personnelCount = Integer.valueOf(StringUtilsLocal.valueOf(stableMap.get("personnel")));
                        if (null != personnelCount && 0 != personnelCount) {
                            double stableRate = (double) personnel / personnelCount;
                            groupAcceptance.setPersonnelStable(String.format("%.2f", stableRate));
                        }
                    }

                    parameter.put("statisticalTime", statisticalTime + " 23:59:59");
                    parameter.put("lastTime", dateList.get(1) + " 23:59:59");
                    parameter.put("issueFlag", "closed");
                    Map<String, Object> closedIssue = bool ? groupBoardDao.querySecondOrgIssue(parameter) : groupBoardDao.queryThirdOrgIssue(parameter);
                    Integer closeIss = 0;
                    if (null != closedIssue && null != closedIssue.get("issueCount")) {
                        Integer issue = Integer.valueOf(StringUtilsLocal.valueOf(closedIssue.get("issueCount")));
                        closeIss = null != issue ? issue : 0;
                    }

                    parameter.put("issueFlag", "all");
                    Map<String, Object> allIssue = bool ? groupBoardDao.querySecondOrgIssue(parameter) : groupBoardDao.queryThirdOrgIssue(parameter);
                    if (null != allIssue && null != allIssue.get("issueCount") & null != closeIss) {
                        Integer count = Integer.valueOf(StringUtilsLocal.valueOf(allIssue.get("issueCount")));
                        if (null != count && 0 != count) {
                            double issueLoop = (double) closeIss / count;
                            groupAcceptance.setIssueCloseLoop(String.format("%.2f", issueLoop));
                        }
                    }

                    //组织看板验收
                    Integer orgKnotProjectCount = groupBoardDao.queryKnotProjectCount(orgId, statisticalTime, flag);
                    groupAcceptance.setKnotProject(null != orgKnotProjectCount ? orgKnotProjectCount : 0);

                    if (null != groupAcceptance) {
                        groupBoardDao.updateGroupOverview(groupAcceptance);
                    }
                }
            }
            logger.info("--------------更新看板总览表&验收表信息结束!--------------");
        } catch (Exception e) {
            logger.error("updateGroupOverview method of BoardOrgService failed: " + e.getMessage());
        }
    }

}
