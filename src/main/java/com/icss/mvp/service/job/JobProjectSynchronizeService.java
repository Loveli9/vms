package com.icss.mvp.service.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.prj.PrjProject;
import com.icss.mvp.entity.system.DepartmentEntity;
import com.icss.mvp.service.IterationCycleService;
import com.icss.mvp.service.TblAreaService;
import com.icss.mvp.service.project.ProjectReviewService;
import com.icss.mvp.service.system.DepartmentService;
import com.icss.mvp.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ray on 2019/9/11.
 *
 * @author Ray
 * @date 2019/9/11 10:18
 */
@Service("JobProjectSynchronizeService")
@EnableScheduling
@PropertySource("classpath:task.properties")
@SuppressWarnings("all")
@Transactional
public class JobProjectSynchronizeService {

    private static Logger logger = Logger.getLogger(JobProjectSynchronizeService.class);

    @Autowired
    private Environment env;


    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private IProjectListDao projectListDao;

    @Autowired
    private TblAreaService tblAreaService;

    @Autowired
    private ProjectReviewService projectReviewService;

    @Autowired
    private IterationCycleService cycleService;

    @Value("${zr.dept}")
    private String contractors;

    @Value("${hw.dept}")
    private String suppliers;

    @Value("${project.secretkey}")
    private String secretKeys;

    private Set<String> grantedContractorTrunks;

    protected Set<String> getContractorTrunks() {
        if (grantedContractorTrunks == null || grantedContractorTrunks.isEmpty()) {
            List<DepartmentEntity> trunks = getTrunks(DepartmentService.CONTRACTOR_TYPE, contractors, 2);
            grantedContractorTrunks = trunks.stream().map(DepartmentEntity::getName).collect(Collectors.toSet());
        }
        return grantedContractorTrunks;
    }

    private Set<String> grantedSupplierTrunks;

    protected Set<String> getSupplierTrunks() {
        if (grantedSupplierTrunks == null || grantedSupplierTrunks.isEmpty()) {
            List<DepartmentEntity> trunks = getTrunks(DepartmentService.SUPPLIER_TYPE_WAHWAY, suppliers, 1);
            grantedSupplierTrunks = trunks.stream().map(DepartmentEntity::getName).collect(Collectors.toSet());
        }
        return grantedContractorTrunks;
    }


    @Scheduled(cron = "${project_Task_scheduled}")
    public BaseResponse synChronizePims() {
        BaseResponse result = new BaseResponse();
        Map<String, Boolean> pmNos = new HashMap<>(16);

        for (String secretKey : CollectionUtilsLocal.splitToSet(secretKeys)) {
            Map<Integer, Boolean> lobProjects = new HashMap<>(16);
            int totalCount = 0;
            int totalPage = 1;
            int step = 1;

            while (true) {
                PageResponse<PrjProject> stepResponse = obtainPrjProjects(step, 300, secretKey);
                if (!stepResponse.getSucceed()) {
                    logger.error("项目信息同步失败，业务线：" + secretKey + " " + stepResponse.getMessage());
                    break;
                }

                if (step == 1) {
                    totalCount = stepResponse.getTotalCount();
                    totalPage = stepResponse.getPageCount();
                }

                if (stepResponse.getData().isEmpty()) {
                    break;
                }

                saveProjects(stepResponse.getData(), lobProjects);

                if (step >= totalPage) {
                    break;
                }

                step++;
            }

            long modified = lobProjects.entrySet().stream().filter(Map.Entry::getValue).count();
            logger.info("项目信息同步完成，业务线：" + secretKey + " 共：" + totalCount + " 条，更新：" + modified + " 条");
        }

        return result;
    }

    final static Type TYPE_COLLECTION_PROJECT = new TypeReference<List<PrjProject>>() {

    }.getType();

    /**
     * 分页获取项目列表
     *
     * @param pageNumber
     * @param pageSize
     * @param key
     * @return
     */
    private PageResponse<PrjProject> obtainPrjProjects(int pageNumber, int pageSize, String key) {
        PageResponse<PrjProject> result = new PageResponse<>();

        String projectUrl = env.getProperty("project.url");

        Map<String, Object> paraMap = new HashMap<>(16);
        paraMap.put("fromIndex", (pageNumber - 1) * pageSize);
        paraMap.put("pageSize", pageSize);
        paraMap.put("secretKey", key);

        try {
            String response = HttpExecuteUtils.httpPost(projectUrl, paraMap, null, getProxySetting());

            /**
             * <pre>
             *  {
             *      "message": "",
             *      "data": {
             *          "total": 1254,
             *          "rows": []
             *      },
             *      "success": true
             *  }
             * </pre>
             */
            JSONObject re = JSONUtils.parseInstance(response);
            if (re.getBoolean("success")) {
                JSONObject data = re.getJSONObject("data");

                result.setData(JSON.parseObject(JSON.toJSONString(data.getJSONArray("rows")), TYPE_COLLECTION_PROJECT));
                result.setTotalCount(data.getInteger("total"));
            } else {
                result.setError(CommonResultCode.INTERNAL, re.getString("message"));
            }
        } catch (Exception e) {
            logger.error("obtainProjectList exception, url: " + projectUrl + ", para: " + JSON.toJSONString(paraMap));
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }

        result.setPageSize(pageSize);
        result.setPageNumber(pageNumber);

        if (result.getData() == null || result.getData().isEmpty()) {
            result.setData(new ArrayList<>());
        }

        return result;
    }

    private void saveProjects(List<PrjProject> projectList, Map<Integer, Boolean> modified) {
        if (projectList == null || projectList.isEmpty()) {
            return;
        }

        Set<String> hwTrunkNames = getContractorTrunks();
        Set<String> opTrunkNames = getSupplierTrunks();

        for (PrjProject project : projectList) {
            int projectId = project.getPrjId();
            if (modified.containsKey(projectId)) {
                logger.error("duplicate project, project: " + JSON.toJSONString(project));
                continue;
            }

            modified.put(projectId, false);

            if (!hwTrunkNames.isEmpty() && !hwTrunkNames.contains(project.getPdName()) && !opTrunkNames.isEmpty()
                    && !opTrunkNames.contains(project.getBuName())) {
                logger.error("project with irrelevant organization, project: " + JSON.toJSONString(project));
                continue;
            }

            // 项目是否包含当前周期
            if (!verifyCycle(project.getPlanStartTime(), project.getPlanEndTime())) {
                logger.error("project not activated, project: " + JSON.toJSONString(project));
                continue;
            }

            ProjectDetailInfo info = saveProjectInfo(project);
            if (info != null && StringUtils.isNotBlank(info.getNo())) {
                saveDeployIteration(info.getNo(), project);
                modified.put(projectId, true);
            }
        }
    }


    private ProjectDetailInfo saveProjectInfo(PrjProject info) {
        ProjectDetailInfo projectInfo = new ProjectDetailInfo();


        projectInfo.setName(info.getPrjName());

        // 华为组织结构
        projectInfo.setHwpdu(info.getPdName());
        projectInfo.setHwzpdu(info.getPduName());
        projectInfo.setPduSpdt(info.getPdtName());
        projectInfo.setHwpduId(StringUtils.isEmpty(info.getPd()) ? "" : info.getPd());
        projectInfo.setHwzpduId(StringUtils.isEmpty(info.getPdu()) ? "" : info.getPdu());
        projectInfo.setPduSpdtId(StringUtils.isEmpty(info.getPdt()) ? "" : info.getPdt());

        // 中软组织结构
        projectInfo.setBu(info.getBuName());
        projectInfo.setPdu(info.getDuName());
        projectInfo.setDu(info.getDeptName());
        projectInfo.setBuId(StringUtils.isEmpty(info.getBu()) ? "" : info.getBu());
        projectInfo.setPduId(StringUtils.isEmpty(info.getDu()) ? "" : info.getDu());
        projectInfo.setDuId(StringUtils.isEmpty(info.getDept()) ? "" : info.getDept());


        String oldProjectId = projectListDao.queryProjectNumber(info.getPrjName());
        projectInfo.setNo(StringUtils.isNotBlank(oldProjectId) ? oldProjectId : String.valueOf(info.getPrjId()));

        // TODO:读取是否结项，是：关闭，否：PrjStatus
        String isClose = projectReviewService.getCloseDateByNo(projectInfo.getNo());
        projectInfo.setProjectState(StringUtils.isNotBlank(isClose) ? "关闭" : info.getPrjStatusName());

        projectInfo.setProjectType(info.getPrjType());
        projectInfo.setPo(info.getOutPoNumber());
        /*根据接口是都返回areaCode待调整*/
        String areaCode = tblAreaService.getArea(info.getArea());
        projectInfo.setArea(info.getArea());
        projectInfo.setAreaId(areaCode);

        projectInfo.setPm(info.getPmName());
        projectInfo.setPmId(info.getPmNo());
        projectInfo.setCoopType(info.getCoopTypeName());
        projectInfo.setStartDate(info.getPlanStartTime());
        projectInfo.setEndDate(info.getPlanEndTime());
        projectInfo.setIsOffshore(1 == info.getIsOffshore() ? "是" : "否");
        projectInfo.setProjectSynopsis(info.getPrjDesc());

        projectInfo.setQaId(info.getQaNo());
        projectInfo.setQaName(info.getQaName());

        /* 计费类型 FP/TM，info.getCurTradmodeName() 调整为 getPricingModealName*/
        projectInfo.setType(info.getPricingModealName());


        projectInfo.setImportDate(new Date());
        projectInfo.setImportUser("admin");

        projectInfo.setTeamName(info.getTeamName());

        try {
            projectListDao.replaceInfo(projectInfo);
        } catch (Exception e) {
            logger.error("projectListDao.replaceInfo exception, error: " + e.getMessage());
            projectInfo = null;
        }

        return projectInfo;
    }

    private void saveDeployIteration(String projectId, PrjProject info) {
        try {
            // 为当前导入项目创建迭代信息

            String projectNo = projectListDao.isExistProject(projectId);
            if (StringUtils.isBlank(projectNo)) {
                IterationCycle cle = new IterationCycle();
                cle.setId(UUIDUtil.getNew());
                cle.setIteName("迭代1");
                cle.setIsDeleted("0");
                cle.setProNo(projectNo);
                cle.setPlanStartDate(info.getPlanStartTime());
                cle.setPlanEndDate(info.getPlanEndTime());
                cle.setStartDate(info.getPlanStartTime());
                cle.setEndDate(info.getPlanEndTime());

                cycleService.deployIteration(cle);
            }
        } catch (Exception e) {
            logger.error("迭代信息配置失败" + e.getMessage());
        }
    }

    private List<DepartmentEntity> getTrunks(String type, String trunkIds, int level) {
        ListResponse<DepartmentEntity> response = new ListResponse<>();
        try {
            DepartmentEntity entity = new DepartmentEntity();
            entity.setType(type);
            entity.setLevel(level);
            entity.setCode(trunkIds);

            response = departmentService.describeDepartments(entity);
        } catch (Exception e) {
            logger.error("departmentService.describeDepartments exception, error: " + e.getMessage());
        }

        if (response.getData() == null || response.getData().isEmpty()) {
            response.setData(new ArrayList<>());
        }

        return response.getData();
    }

    private String[] getProxySetting() {
        if ("1".equals(env.getProperty("is_proxy"))) {
            try {
                String host = env.getProperty("proxy.host");
                String port = env.getProperty("proxy.port");
                String account = env.getProperty("w3.account");
                String secret = AESOperator.getInstance().decrypt(env.getProperty("w3.password"));

                return new String[]{host, port, account, secret};
            } catch (Exception e) {
                logger.error("getProxySetting exception, error:" + e.getMessage());
            }
        }

        return new String[0];
    }

    private boolean verifyCycle(Date startDate, Date endDate) {
        boolean result = false;
        try {
            List<String> cycleList = DateUtils.getLatestCycles(2, true);
            String beginCycle = DateUtils.getNextDay(cycleList.get(1));
            String endCycle = cycleList.get(0);

            if (DateUtils.differenceTime(endCycle, DateUtils.SHORT_FORMAT_GENERAL.format(startDate)) <= 0
                    && DateUtils.differenceTime(beginCycle, DateUtils.SHORT_FORMAT_GENERAL.format(endDate)) >= 0) {
                result = true;
            }
        } catch (Exception e) {
            logger.error("ProjectCollectService verifyCycle failed:" + e.getMessage());
        }
        return result;
    }
}
