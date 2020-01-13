package com.icss.mvp.service;

import com.icss.mvp.dao.IGroupBoardDao;
import com.icss.mvp.entity.GroupAcceptanceEntity;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectStateNumber;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * 组织看板 &项目看板
 *
 * @author limingming
 */
@Service
@SuppressWarnings("all")
public class GroupBoardService {
    private final static Logger logger = Logger.getLogger(GroupBoardService.class);

    @Autowired
    private IGroupBoardDao groupBoardDao;

    @Resource
    private ProjectInfoService projectInfoService;

    /**
     * 组织看板总览表
     *
     * @param projectInfo
     * @return
     */
    public TableSplitResult<List<GroupAcceptanceEntity>> queryGroupOverview(ProjectInfo projectInfo) {
        TableSplitResult<List<GroupAcceptanceEntity>> result = new TableSplitResult<>();
        List<GroupAcceptanceEntity> groupAcceptance = new ArrayList<>();
        Map<String, Object> organizationMap = new HashMap<>();

        projectInfoService.setParamNew(projectInfo, null, organizationMap);

        try {
            queryGroupBoardOrganization(projectInfo, organizationMap, groupAcceptance, "overview");
        } catch (Exception e) {
            logger.error("queryGroupOverview method of GroupBoardService failed: " + e.getMessage());
        }

        if (null != groupAcceptance) {
            result.setRows(groupAcceptance);
        }
        return result;
    }

    /**
     * 组织看板总览饼图
     *
     * @param projectInfo
     * @return
     */
    public Map<String, Object> groupOverviewPieChart(ProjectInfo projectInfo) {
        Map<String, Object> pieCharMap = new HashMap<>();
        Map<String, Object> organizationMap = new HashMap<>();

        projectInfoService.setParamNew(projectInfo, null, organizationMap);

        try {
            Map<String, Object> map = queryGroupChartOrganization(projectInfo, organizationMap, "");

            if (null != map) {
                Map<String, Object> pieChart = (Map) map.get("chart");
                pieCharMap.put("sources", pieChart.get("sources"));
                pieCharMap.put("values", pieChart.get("values"));
            }
        } catch (Exception e) {
            logger.error("groupOverviewPieChart method of GroupBoardService failed: " + e.getMessage());
        }
        return pieCharMap;
    }

    /**
     * 组织看板总览柱图
     *
     * @param projectInfo
     * @return
     */
    public Map<String, Object> groupOverviewHistogram(ProjectInfo projectInfo, String riskCategory) {
        Map<String, Object> histogramMap = new HashMap<>();
        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();
        Map<String, Object> organizationMap = new HashMap<>();

        projectInfoService.setParamNew(projectInfo, null, organizationMap);

        try {
            Map<String, Object> map = queryGroupChartOrganization(projectInfo, organizationMap, riskCategory);

            if (null != map) {
                List<Map<String, Object>> histograms = (ArrayList) map.get("histogram");

                for (Map<String, Object> histogram : histograms) {
                    histogram.forEach((key, value) -> {
                        if ("name".equals(key)) {
                            names.add(StringUtilsLocal.valueOf(value));
                        } else {
                            values.add(StringUtilsLocal.valueOf(value));
                        }
                    });
                }
            }
            histogramMap.put("names", names);
            histogramMap.put("values", values);
        } catch (Exception e) {
            logger.error("groupOverviewHistogram method of GroupBoardService failed: " + e.getMessage());
        }
        return histogramMap;
    }

    /**
     * 组织看板验收表
     *
     * @param projectInfo
     * @return
     */
    public TableSplitResult<List<GroupAcceptanceEntity>> queryGroupAcceptance(ProjectInfo projectInfo) {
        TableSplitResult<List<GroupAcceptanceEntity>> result = new TableSplitResult<>();
        List<GroupAcceptanceEntity> groupAcceptance = new ArrayList<>();
        Map<String, Object> organizationMap = new HashMap<>();
        projectInfoService.setParamNew(projectInfo, null, organizationMap);

        try {
            queryGroupBoardOrganization(projectInfo, organizationMap, groupAcceptance, "acceptance");
        } catch (Exception e) {
            logger.error("queryGroupAcceptance method of GroupBoardService failed: " + e.getMessage());
        }

        if (null != groupAcceptance) {
            result.setRows(groupAcceptance);
        }
        return result;
    }

    /**
     * 组织看板总览下饼图和柱图的组织机构归纳
     *
     * @param projectInfo
     * @param organizationMap
     * @param riskCategory
     * @return
     */
    private Map<String, Object> queryGroupChartOrganization(ProjectInfo projectInfo, Map<String, Object> organizationMap, String riskCategory) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> chartMap = new HashMap<>();
        List<String> chartSources = new ArrayList<>(Arrays.asList("风险", "预警", "正常", "未评估"));
        List<Map<String, Object>> chartList = new ArrayList<>();
        List<Map<String, Object>> firstOrganizations = new ArrayList<>();
        String clientType = projectInfo.getClientType();

        if ("0".equals(clientType)) {
            organizationMap.put("flag", "hwpdu");
            firstOrganizations = groupBoardDao.getFirstOrganizations(organizationMap);
        } else if ("1".equals(clientType)) {
            organizationMap.put("flag", "bu");
            firstOrganizations = groupBoardDao.getFirstOrganizations(organizationMap);
        }

        int riskCount = 0;
        int warningCount = 0;
        int normalCount = 0;
        int notAssessedCount = 0;
        List<Map<String, Object>> histogramList = new ArrayList<>();

        for (Map<String, Object> firstOrganization : firstOrganizations) {
            boolean firstFlag = false;
            int firstRisk = 0;
            int firstWarning = 0;
            int firstNormal = 0;
            int firstNotAssessed = 0;

            if ("0".equals(clientType)) {
                if (null == organizationMap.get("hwzpduId") || 0 == ((ArrayList) organizationMap.get("hwzpduId")).size()) {
                    organizationMap = new HashMap<>();
                    organizationMap.put("hwpduId", new ArrayList<>(Arrays.asList(firstOrganization.get("firstOrgId"))));
                    firstFlag = true;
                }
                organizationMap.put("flag", "hwpdu");
            } else if ("1".equals(clientType)) {
                if (null == organizationMap.get("pduId") || 0 == ((ArrayList) organizationMap.get("pduId")).size()) {
                    organizationMap = new HashMap<>();
                    organizationMap.put("buId", new ArrayList<>(Arrays.asList(firstOrganization.get("firstOrgId"))));
                    firstFlag = true;
                }
                organizationMap.put("flag", "bu");
            }

            boolean firstIsTrue = "0".equals(clientType) ? StringUtils.isBlank(projectInfo.getHwpdu()) : StringUtils.isBlank(projectInfo.getBu());
            if (firstFlag && firstIsTrue) {
                List<String> projectStatus = groupBoardDao.queryRiskProjectCount(StringUtilsLocal.valueOf(firstOrganization.get("firstOrgId")),
                        projectInfo.getMonth(), "0".equals(clientType) ? "firstOrgHw" : "firstOrgZr");

                for (String s : projectStatus) {
                    if (4 == transformProjectStatus(s)) {
                        notAssessedCount++;
                        firstNotAssessed++;
                    } else if (3 == transformProjectStatus(s)) {
                        normalCount++;
                        firstNormal++;
                    } else if (2 == transformProjectStatus(s)) {
                        warningCount++;
                        firstWarning++;
                    } else {
                        riskCount++;
                        firstRisk++;
                    }
                }

                Map<String, Object> map = new HashMap<>();
                map.put("name", firstOrganization.get("firstOrg"));
                if ("风险".equals(riskCategory)) {
                    map.put("value", firstRisk);
                } else if ("预警".equals(riskCategory)) {
                    map.put("value", firstWarning);
                } else if ("正常".equals(riskCategory)) {
                    map.put("value", firstNormal);
                } else {
                    map.put("value", firstNotAssessed);
                }
                histogramList.add(map);
                continue;
            }

            List<Map<String, Object>> secondOrganizations = groupBoardDao.getSecondOrganizations(organizationMap);

            for (Map<String, Object> secondOrganization : secondOrganizations) {
                boolean secondFlag = false;
                int secondRisk = 0;
                int secondWarning = 0;
                int secondNormal = 0;
                int secondNotAssessed = 0;

                if ("0".equals(clientType)) {
                    if (null == organizationMap.get("pduSpdtId") || 0 == ((ArrayList) organizationMap.get("pduSpdtId")).size()) {
                        organizationMap = new HashMap<>();
                        organizationMap.put("hwzpduId", new ArrayList<>(Arrays.asList(secondOrganization.get("secondOrgId"))));
                        secondFlag = true;
                    }
                    organizationMap.put("flag", "hwzpdu");
                } else if ("1".equals(clientType)) {
                    if (null == organizationMap.get("duId") || 0 == ((ArrayList) organizationMap.get("duId")).size()) {
                        organizationMap = new HashMap<>();
                        organizationMap.put("pduId", new ArrayList<>(Arrays.asList(secondOrganization.get("secondOrgId"))));
                        secondFlag = true;
                    }
                    organizationMap.put("flag", "pdu");
                }
                boolean secondIsTrue = "0".equals(clientType) ? StringUtils.isBlank(projectInfo.getHwzpdu()) : StringUtils.isBlank(projectInfo.getPdu());
                if (secondFlag && secondIsTrue) {
                    List<String> projectStatus = groupBoardDao.queryRiskProjectCount(StringUtilsLocal.valueOf(secondOrganization.get("secondOrgId")),
                            projectInfo.getMonth(), "0".equals(clientType) ? "secondOrgHw" : "secondOrgZr");

                    for (String s : projectStatus) {
                        if (4 == transformProjectStatus(s)) {
                            notAssessedCount++;
                            secondNotAssessed++;
                        } else if (3 == transformProjectStatus(s)) {
                            normalCount++;
                            secondNormal++;
                        } else if (2 == transformProjectStatus(s)) {
                            warningCount++;
                            secondWarning++;
                        } else {
                            riskCount++;
                            secondRisk++;
                        }
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", secondOrganization.get("secondOrg"));
                    if ("风险".equals(riskCategory)) {
                        map.put("value", secondRisk);
                    } else if ("预警".equals(riskCategory)) {
                        map.put("value", secondWarning);
                    } else if ("正常".equals(riskCategory)) {
                        map.put("value", secondNormal);
                    } else {
                        map.put("value", secondNotAssessed);
                    }
                    histogramList.add(map);
                    continue;
                }

                List<Map<String, Object>> thirdOrganizations = groupBoardDao.getThirdOrganizations(organizationMap);

                for (Map<String, Object> thirdOrganization : thirdOrganizations) {
                    int thirdRisk = 0;
                    int thirdWarning = 0;
                    int thirdNormal = 0;
                    int thirdNotAssessed = 0;

                    boolean isTrue = "0".equals(clientType)
                            ? (StringUtils.isNotBlank(projectInfo.getHwzpdu()) || StringUtils.isNotBlank(projectInfo.getPduSpdt()))
                            : (StringUtils.isNotBlank(projectInfo.getPdu()) || StringUtils.isNotBlank(projectInfo.getDu()));

                    if (isTrue) {
                        List<String> projectStatus = groupBoardDao.queryRiskProjectCount(StringUtilsLocal.valueOf(thirdOrganization.get("thirdOrgId")),
                                projectInfo.getMonth(), "0".equals(clientType) ? "thirdOrgHw" : "thirdOrgZr");

                        for (String s : projectStatus) {
                            if (4 == transformProjectStatus(s)) {
                                notAssessedCount++;
                                thirdNotAssessed++;
                            } else if (3 == transformProjectStatus(s)) {
                                normalCount++;
                                thirdNormal++;
                            } else if (2 == transformProjectStatus(s)) {
                                warningCount++;
                                thirdWarning++;
                            } else {
                                riskCount++;
                                thirdRisk++;
                            }
                        }

                        Map<String, Object> map = new HashMap<>();
                        map.put("name", thirdOrganization.get("thirdOrg"));
                        if ("风险".equals(riskCategory)) {
                            map.put("value", thirdRisk);
                        } else if ("预警".equals(riskCategory)) {
                            map.put("value", thirdWarning);
                        } else if ("正常".equals(riskCategory)) {
                            map.put("value", thirdNormal);
                        } else {
                            map.put("value", thirdNotAssessed);
                        }
                        histogramList.add(map);
                    }
                }
            }
            if ("0".equals(clientType)) {
                organizationMap.put("hwzpduId", new ArrayList<>());
            } else if ("1".equals(clientType)) {
                organizationMap.put("pduId", new ArrayList<>());
            }
        }

        Map<String, Object> riskMap = new HashMap<>();
        riskMap.put("name", chartSources.get(0));
        riskMap.put("value", riskCount);

        Map<String, Object> warningMap = new HashMap<>();
        warningMap.put("name", chartSources.get(1));
        warningMap.put("value", warningCount);

        Map<String, Object> normalMap = new HashMap<>();
        normalMap.put("name", chartSources.get(2));
        normalMap.put("value", normalCount);

        Map<String, Object> notAssessedMap = new HashMap<>();
        notAssessedMap.put("name", chartSources.get(3));
        notAssessedMap.put("value", notAssessedCount);

        chartList.add(riskMap);
        chartList.add(warningMap);
        chartList.add(normalMap);
        chartList.add(notAssessedMap);

        chartMap.put("sources", chartSources);
        chartMap.put("values", chartList);

        result.put("chart", chartMap);
        result.put("histogram", histogramList);
        return result;
    }

    /**
     * 组织看板总览和验收的组织机构归纳
     *
     * @param projectInfo
     * @param organizationMap
     * @param groupAcceptance
     * @param groupBoardSort
     */
    private void queryGroupBoardOrganization(ProjectInfo projectInfo, Map<String, Object> organizationMap, List<GroupAcceptanceEntity> groupAcceptance, String groupBoardSort) {
        List<Map<String, Object>> firstOrganizations = new ArrayList<>();
        String clientType = projectInfo.getClientType();
        if ("0".equals(clientType)) {
            organizationMap.put("flag", "hwpdu");
            firstOrganizations = groupBoardDao.getFirstOrganizations(organizationMap);
        } else if ("1".equals(clientType)) {
            organizationMap.put("flag", "bu");
            firstOrganizations = groupBoardDao.getFirstOrganizations(organizationMap);
        }

        for (Map<String, Object> firstOrganization : firstOrganizations) {
            if ("0".equals(clientType)) {
                if (null == organizationMap.get("hwzpduId") || 0 == ((ArrayList) organizationMap.get("hwzpduId")).size()) {
                    organizationMap = new HashMap<>();
                    organizationMap.put("hwpduId", new ArrayList<>(Arrays.asList(firstOrganization.get("firstOrgId"))));
                }
                organizationMap.put("flag", "hwpdu");
            } else if ("1".equals(clientType)) {
                if (null == organizationMap.get("pduId") || 0 == ((ArrayList) organizationMap.get("pduId")).size()) {
                    organizationMap = new HashMap<>();
                    organizationMap.put("buId", new ArrayList<>(Arrays.asList(firstOrganization.get("firstOrgId"))));
                }
                organizationMap.put("flag", "bu");
            }

            List<Map<String, Object>> secondOrganizations = groupBoardDao.getSecondOrganizations(organizationMap);

            for (Map<String, Object> secondOrganization : secondOrganizations) {
                if ("0".equals(clientType)) {
                    if (null == organizationMap.get("pduSpdtId") || 0 == ((ArrayList) organizationMap.get("pduSpdtId")).size()) {
                        organizationMap = new HashMap<>();
                        organizationMap.put("hwzpduId", new ArrayList<>(Arrays.asList(secondOrganization.get("secondOrgId"))));
                    }
                    organizationMap.put("flag", "hwzpdu");
                } else if ("1".equals(clientType)) {
                    if (null == organizationMap.get("duId") || 0 == ((ArrayList) organizationMap.get("duId")).size()) {
                        organizationMap = new HashMap<>();
                        organizationMap.put("pduId", new ArrayList<>(Arrays.asList(secondOrganization.get("secondOrgId"))));
                    }
                    organizationMap.put("flag", "pdu");
                }

                GroupAcceptanceEntity groupAcceptanceSecondOrgEntity = new GroupAcceptanceEntity();
                groupAcceptanceSecondOrgEntity.setHwzpdu(StringUtilsLocal.valueOf(secondOrganization.get("secondOrg")));
                groupAcceptance.add(groupAcceptanceSecondOrgEntity);

                List<Map<String, Object>> thirdOrganizations = groupBoardDao.getThirdOrganizations(organizationMap);

                if ("overview".equals(groupBoardSort)) {
                    queryOverviewInfo(projectInfo, groupAcceptance, secondOrganization, thirdOrganizations);
                } else {
                    queryAcceptanceInfo(projectInfo, groupAcceptance, secondOrganization, thirdOrganizations);
                }
            }
            if ("0".equals(clientType)) {
                organizationMap.put("hwzpduId", new ArrayList<>());
            } else if ("1".equals(clientType)) {
                organizationMap.put("pduId", new ArrayList<>());
            }
        }
    }

    /**
     * 获取组织看板总览表格所有列信息
     *
     * @param projectInfo
     * @param groupAcceptance
     * @param secondOrganization
     * @param thirdOrganizations
     */
    private void queryOverviewInfo(ProjectInfo projectInfo, List<GroupAcceptanceEntity> groupAcceptance, Map<String, Object> secondOrganization, List<Map<String, Object>> thirdOrganizations) {
        Integer riskProjectCount = 0;
        Integer executionProjectCount = 0;

        for (Map<String, Object> thirdOrganization : thirdOrganizations) {
            Map<String, Object> map = queryOverviewDetail(projectInfo, groupAcceptance, thirdOrganization);
            String risk = StringUtilsLocal.valueOf(map.get("riskCount"));
            String execution = StringUtilsLocal.valueOf(map.get("executionCount"));

            riskProjectCount += StringUtils.isNotBlank(risk) ? Integer.parseInt(risk) : 0;
            executionProjectCount += StringUtils.isNotBlank(execution) ? Integer.parseInt(execution) : 0;
        }

        for (GroupAcceptanceEntity group : groupAcceptance) {
            if (StringUtils.isNotBlank(group.getHwzpdu()) && group.getHwzpdu().equals(StringUtilsLocal.valueOf(secondOrganization.get("secondOrg")))) {
                group.setRiskProject(riskProjectCount);
                group.setExecutedProject(executionProjectCount);
                break;
            }
        }
    }

    /**
     * 获取组织看板验收表格所有列信息
     *
     * @param projectInfo
     * @param groupAcceptance
     * @param secondOrganization
     * @param thirdOrganizations
     */
    private void queryAcceptanceInfo(ProjectInfo projectInfo, List<GroupAcceptanceEntity> groupAcceptance, Map<String, Object> secondOrganization, List<Map<String, Object>> thirdOrganizations) {
        Integer orgKnotProjectCount = 0;

        for (Map<String, Object> thirdOrganization : thirdOrganizations) {
            orgKnotProjectCount += queryAcceptanceDetail(projectInfo, groupAcceptance, thirdOrganization);
        }

        for (GroupAcceptanceEntity group : groupAcceptance) {
            if (StringUtils.isNotBlank(group.getHwzpdu()) && group.getHwzpdu().equals(StringUtilsLocal.valueOf(secondOrganization.get("secondOrg")))) {
                group.setKnotProject(orgKnotProjectCount);
                break;
            }
        }
    }

    /**
     * 获取组织看板总览表格所有列详细信息
     *
     * @param projectInfo
     * @param groupAcceptance
     * @param organization
     * @return
     */
    private Map<String, Object> queryOverviewDetail(ProjectInfo projectInfo, List<GroupAcceptanceEntity> groupAcceptance, Map<String, Object> organization) {
        Map<String, Object> reslutMap = new HashMap<>();
        GroupAcceptanceEntity groupAcceptanceOrgEntity = new GroupAcceptanceEntity();
        Integer riskProjectCount = 0;

        groupAcceptanceOrgEntity.setPduSpdt(StringUtilsLocal.valueOf(organization.get("thirdOrg")));

        List<String> riskRes = groupBoardDao.queryRiskProjectCount(StringUtilsLocal.valueOf(organization.get("thirdOrgId")),
                projectInfo.getMonth(), "0".equals(projectInfo.getClientType()) ? "thirdOrgHw" : "thirdOrgZr");
        Integer executionProjectCount = groupBoardDao.queryExecutionProjectCount(StringUtilsLocal.valueOf(organization.get("thirdOrgId")),
                projectInfo.getMonth(), "0".equals(projectInfo.getClientType()) ? "thirdOrgHw" : "thirdOrgZr");

        for (String s : riskRes) {
            int isRisk = transformProjectStatus(s);

            if (1 == isRisk) {
                riskProjectCount++;
            }
        }
        groupAcceptanceOrgEntity.setRiskProject(riskProjectCount);
        groupAcceptanceOrgEntity.setExecutedProject(executionProjectCount);

        groupAcceptance.add(groupAcceptanceOrgEntity);

        reslutMap.put("riskCount", riskProjectCount);
        reslutMap.put("executionCount", executionProjectCount);
        return reslutMap;
    }

    /**
     * 获取组织看板验收表格所有列详细信息
     *
     * @param projectInfo
     * @param groupAcceptance
     * @param organization
     * @return
     */
    private Integer queryAcceptanceDetail(ProjectInfo projectInfo, List<GroupAcceptanceEntity> groupAcceptance, Map<String, Object> organization) {
        GroupAcceptanceEntity groupAcceptanceOrgEntity = new GroupAcceptanceEntity();
        groupAcceptanceOrgEntity.setPduSpdt(StringUtilsLocal.valueOf(organization.get("thirdOrg")));

        Integer orgKnotProjectCount = groupBoardDao.queryKnotProjectCount(StringUtilsLocal.valueOf(organization.get("thirdOrgId")),
                projectInfo.getMonth(), "0".equals(projectInfo.getClientType()) ? "thirdOrgHw" : "thirdOrgZr");
        groupAcceptanceOrgEntity.setKnotProject(null != orgKnotProjectCount ? orgKnotProjectCount : 0);

        groupAcceptance.add(groupAcceptanceOrgEntity);
        return orgKnotProjectCount;
    }

    /**
     * 项目看板执行表
     *
     * @param projectInfo
     * @param pageRequest
     * @return
     */
    public TableSplitResult<List<ProjectStateNumber>> queryProjectExecute(ProjectInfo projectInfo, PageRequest pageRequest) {
        TableSplitResult<List<ProjectStateNumber>> result = new TableSplitResult<>();
        List<ProjectStateNumber> projectExecute = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> organizationMap = new HashMap<>();

        projectInfoService.setParamNew(projectInfo, null, organizationMap);

        try {
            String month = projectInfo.getMonth();
            List<String> monthList = DateUtils.getLatestWeek(2, true, month);

            if (StringUtils.isNotBlank(month)) {
                organizationMap.put("month", month);
                organizationMap.put("monthList", monthList);
            }

            Integer count = groupBoardDao.queryProjectExecuteCount(organizationMap);

            if (null == count) {
                result.setErr(new ArrayList<>(), 1);
            } else {
                organizationMap.put("pageSize", pageRequest.getPageSize());
                organizationMap.put("offset", pageRequest.getOffset());
                list = groupBoardDao.queryProjectExecute(organizationMap);

                for (Map<String, Object> map : list) {
                    ProjectStateNumber projectStateNumber = new ProjectStateNumber();

                    projectStateNumber.setProjectNo(StringUtilsLocal.valueOf(map.get("NO")));
                    projectStateNumber.setName(StringUtilsLocal.valueOf(map.get("NAME")));
                    projectStateNumber.setPm(StringUtilsLocal.valueOf(map.get("PM")));
                    projectStateNumber.setQa(StringUtilsLocal.valueOf(map.get("QA")));
                    projectStateNumber.setPdu(StringUtilsLocal.valueOf(map.get("pdu")));
                    projectStateNumber.setPduspt(StringUtilsLocal.valueOf(map.get("pduspt")));
                    String thisWeek = "";
                    String lastWeek = "";

                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        //本次项目状态
                        thisWeek = "".equals(thisWeek) ? getProjectSingleStatus(monthList.get(0), projectStateNumber, entry, thisWeek, "this") : thisWeek;

                        //上次项目状态
                        lastWeek = "".equals(lastWeek) ? getProjectSingleStatus(monthList.get(1), projectStateNumber, entry, lastWeek, "last") : lastWeek;
                    }
                    projectExecute.add(projectStateNumber);
                }
                if (null != projectExecute) {
                    result.setRows(projectExecute);
                    result.setTotal(count);
                }
            }
        } catch (Exception e) {
            logger.error("queryProjectExecute method of GroupBoardService failed: " + e.getMessage());
        }
        return result;
    }

    /**
     * 项目周期状态
     *
     * @param month
     * @param projectStateNumber
     * @param entry
     * @param week
     * @param flag
     * @return
     */
    private String getProjectSingleStatus(String month, ProjectStateNumber projectStateNumber, Map.Entry<String, Object> entry, String week, String flag) {
        if (month.equals(entry.getKey())) {
            int projectStatusFlag = transformProjectStatus(StringUtilsLocal.valueOf(entry.getValue()));

            if (1 == projectStatusFlag) {
                if ("this".equals(flag)) {
                    projectStateNumber.setRedState(1);
                } else {
                    projectStateNumber.setRedState1(1);
                }
                week = "".equals(week) ? "风险" : week;
            } else if (2 == projectStatusFlag) {
                if ("this".equals(flag)) {
                    projectStateNumber.setYellowState(1);
                } else {
                    projectStateNumber.setYellowState1(1);
                }
                week = "".equals(week) ? "预警" : week;
            } else if (3 == projectStatusFlag) {
                if ("this".equals(flag)) {
                    projectStateNumber.setGreenState(1);
                } else {
                    projectStateNumber.setGreenState1(1);
                }
            }
        }
        return week;
    }

    /**
     * 判断项目评估状态是否为风险，预警，正常, 未评估
     *
     * @param value
     * @return
     */
    private int transformProjectStatus(String value) {
        int res = 0;
        if (StringUtils.isBlank(value)) {
            return 4;
        }
        Double val = Double.parseDouble(value);

        if (val >= 0 && val < 70) {
            res = 1;
        } else if (val >= 70 && val < 80) {
            res = 2;
        } else if (val >= 80) {
            res = 3;
        }
        return res;
    }

    /**
     * 项目看板验收表
     *
     * @param projectInfo
     * @param pageRequest
     * @return
     */
    public TableSplitResult<List<GroupAcceptanceEntity>> queryProjectAcceptance(ProjectInfo projectInfo, PageRequest pageRequest) {
        TableSplitResult<List<GroupAcceptanceEntity>> result = new TableSplitResult<>();
        List<GroupAcceptanceEntity> list = new ArrayList<>();
        Map<String, Object> organizationMap = new HashMap<>();

        projectInfoService.setParamNew(projectInfo, null, organizationMap);

        try {
            if (StringUtils.isNotBlank(projectInfo.getMonth())) {
                organizationMap.put("month", projectInfo.getMonth());
            }

            Integer count = groupBoardDao.queryProjectAcceptanceCount(organizationMap);

            if (null == count) {
                result.setErr(new ArrayList<>(), 1);
            } else {
                organizationMap.put("pageSize", pageRequest.getPageSize());
                organizationMap.put("offset", pageRequest.getOffset());

                list = groupBoardDao.queryProjectAcceptance(organizationMap);

                if (null != list) {
                    result.setRows(list);
                    result.setTotal(count);
                }
            }
        } catch (Exception e) {
            logger.error("queryProjectAcceptance method of GroupBoardService failed: " + e.getMessage());
        }
        return result;
    }

}
