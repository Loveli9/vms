package com.icss.mvp.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.ProjectEntity;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.system.DepartmentEntity;
import com.icss.mvp.service.project.ProjectReviewService;
import com.icss.mvp.service.project.ProjectTeamService;
import com.icss.mvp.service.system.DepartmentService;
import com.icss.mvp.dao.CodeGainTypeDao;
import com.icss.mvp.util.*;

@Service
@PropertySource("classpath:application.properties")
@Deprecated
public class ProjectCollectService {

    private static Logger logger   = Logger.getLogger(ProjectCollectService.class);

    private int           pageSize = 50;

    @Autowired
    private Environment   env;

    @Value("${zr.dept}")
    private String        contractors;

    @Value("${hw.dept}")
    private String        suppliers;

    @Value("${project.secretkey}")
    private String        secretKeys;

    private Set<String>   grantedContractorTrunks;

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

    private List<DepartmentEntity> getTrunks(String type, String trunkIds, int level) {
        ListResponse<DepartmentEntity> response = new ListResponse<>();
        try {
            // List<Integer> intIds = CollectionUtilsLocal.splitToList(trunkIds, Integer.class);

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

    public Set<String> getSecretKeys() {
        return CollectionUtilsLocal.splitToSet(secretKeys);
    }

    @Resource
    private IterationCycleService cycleService;

    @Resource
    private IProjectListDao       projectListDao;

    @Resource
    private SysHwdeptService      sysHwdeptService;

    @Resource
    private OpDepartmentService   opDepartmentService;

    @Autowired
    private DepartmentService     departmentService;

    @Resource
    private TblAreaService        tblAreaService;

    @Autowired
    private ProjectTeamService    projectTeamService;

    @Autowired
    private ProjectReviewService  projectReviewService;
    
    @Autowired
    private MeasureAchievementStatusService measureAchievementStatusService;
    
    @Autowired
	private CodeGainTypeDao codeGainTypeDao;

    public BaseResponse collectProjectInfos() {

        // //获取配置资源
        // getApplicationPro();

        BaseResponse result = new BaseResponse();

        for (String secretKey : getSecretKeys()) {
            PageResponse step = synchroniseProject(1, pageSize, secretKey);

            if (!step.getSucceed()) {
                continue;
            }

            int totalCount = step.getTotalCount();
            int totalPage = step.getPageCount();
            if (totalPage > 1) {
                for (int i = 2; i <= totalPage; i++) {
                    synchroniseProject(i, pageSize, secretKey);
                }
            }

            logger.info("项目信息同步完成，共：" + totalCount + " 条");

            // if(totalCount > 0){
            // closeProjects(nowSateString);
            // }

        }
        return result;
    }

    private PageResponse<ProjectEntity> synchroniseProject(int pageNumber, int pageSize, String secretKey) {
        PageResponse<ProjectEntity> response = new PageResponse<>();

        try {
            response = obtainProjectList(pageNumber, pageSize, secretKey);
            if (response.getSucceed() && response.getData() != null) {
                saveProjectInfo(response.getData());
            }
        } catch (Exception e) {
            logger.error("同步项目信息失败，" + e.getMessage());
            response.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        response.setData(null);
        return response;
    }

    /**
     * 分页获取项目列表
     * 
     * @param pageNumber
     * @param pageSize
     * @param key
     * @return
     */
    private PageResponse<ProjectEntity> obtainProjectList(int pageNumber, int pageSize, String key) {
        PageResponse<ProjectEntity> result = new PageResponse<>();

        String projectUrl = env.getProperty("project.url");

        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("fromIndex", (pageNumber - 1) * pageSize);
        paraMap.put("pageSize", pageSize);
        paraMap.put("secretKey", key);
        List<ProjectEntity> projects = new ArrayList<>();
        int total = 0;
        try {
            String response = HttpExecuteUtils.httpPost(projectUrl, paraMap, null, getProxySetting());
            Map dataMap = JSONUtils.parseSelectedInstance(response, "data", Map.class, new HashMap<>());

            projects = JSONUtils.parseCollection(dataMap.get("rows"), ProjectEntity.class);
            total = MathUtils.parseIntSmooth(dataMap.get("total"));
        } catch (Exception e) {
            logger.error("obtainProjectList exception, url: " + projectUrl + ", para: " + JSON.toJSONString(paraMap));
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }

        result.setData(projects);
        result.setTotalCount(total);

        result.setPageSize(pageSize);
        result.setPageCount(pageNumber);

        return result;
    }

    private String[] getProxySetting() {
        if ("1".equals(env.getProperty("is_proxy"))) {
            try {
                String host = env.getProperty("proxy.host");
                String port = env.getProperty("proxy.port");
                String account = env.getProperty("w3.account");
                String secret = AESOperator.getInstance().decrypt(env.getProperty("w3.password"));

                return new String[] { host, port, account, secret };
            } catch (Exception e) {
                logger.error("new String[] exception, error: "+e.getMessage());
            }
        }

        return new String[0];
    }

    private void saveProjectInfo(List<ProjectEntity> projectList) {
        if (projectList == null || projectList.isEmpty()) {
            return;
        }
        for (ProjectEntity project : projectList) {
            ProjectDetailInfo info = saveProjectInfo(project);
            if (info == null) {
                continue;
            }
            saveDeployIteration(project);
        }
    }

    private ProjectDetailInfo saveProjectInfo(ProjectEntity info) {
        Set<String> hwTrunkNames = getContractorTrunks();
        Set<String> opTrunkNames = getSupplierTrunks();
        if (!hwTrunkNames.isEmpty() && !hwTrunkNames.contains(info.getPdName()) && !opTrunkNames.isEmpty()
            && !opTrunkNames.contains(info.getBuName())) {
            logger.error("project with irrelevant organization, project: " + JSON.toJSONString(info));
            return null;
        }

        // 校验项目是否满足获取条件
        Map<String, Object> mapHw = sysHwdeptService.getHwOrganizaMap(info.getPdName(), info.getPduName(),
                                                                      info.getPdtName());
        if (mapHw == null || mapHw.isEmpty()) {
            mapHw = new HashMap<>();
        }

        String deptName = info.getDeptName();
        Map<String, Object> mapZR = opDepartmentService.getZrOrganizaMap(info.getBuName(), info.getDuName(), deptName);
        if (mapZR == null || mapZR.isEmpty()) {
            mapZR = new HashMap<>();
        }

        // 都不满足中软华为三层结构
        if ((mapHw.size() == 0) && (mapZR.size() == 0)) {
            logger.error("project organization error, project: " + JSON.toJSONString(info));
            return null;
        }

        ProjectDetailInfo projectInfo = new ProjectDetailInfo();
        projectInfo.setName(info.getPrjName());

        String oldProjectId = projectListDao.queryProjectNumber(info.getPrjName());
        projectInfo.setNo(StringUtils.isNotBlank(oldProjectId) ? oldProjectId : info.getPrjId());

        // TODO:读取是否结项，是：关闭，否：PrjStatus
        String isClose = projectReviewService.getCloseDateByNo(projectInfo.getNo());
        String state = StringUtils.isNotBlank(isClose) ? "关闭" : info.getPrjStatusName();
        projectInfo.setProjectState(state);

        projectInfo.setProjectType(info.getPrjType());
        projectInfo.setPo(info.getOutPoNumber());

        String areaCode = tblAreaService.getArea(info.getArea());
        projectInfo.setArea(info.getArea());
        projectInfo.setAreaId(areaCode);

        // 华为组织结构
        projectInfo.setHwpdu(info.getPdName());
        projectInfo.setHwzpdu(info.getPduName());
        projectInfo.setPduSpdt(info.getPdtName());
        projectInfo.setHwpduId(mapHw.get("lv1") != null ? String.valueOf(mapHw.get("lv1")) : null);
        projectInfo.setHwzpduId(mapHw.get("lv2") != null ? String.valueOf(mapHw.get("lv2")) : null);
        projectInfo.setPduSpdtId(mapHw.get("lv3") != null ? String.valueOf(mapHw.get("lv3")) : null);

        // 中软组织结构
        projectInfo.setBu(info.getBuName());
        projectInfo.setPdu(info.getDuName());
        projectInfo.setDu(deptName);
        projectInfo.setBuId(mapZR.get("lv1") != null ? String.valueOf(mapZR.get("lv1")) : null);
        projectInfo.setPduId(mapZR.get("lv2") != null ? String.valueOf(mapZR.get("lv2")) : null);
        projectInfo.setDuId(mapZR.get("lv3") != null ? String.valueOf(mapZR.get("lv3")) : null);

        projectInfo.setPm(info.getPmName());
        projectInfo.setPmId(info.getPmNo());
        projectInfo.setCoopType(info.getCoopTypeName());
        projectInfo.setStartDate(info.getPlanStartTime());
        projectInfo.setEndDate(info.getPlanEndTime());
        projectInfo.setIsOffshore(("1").equals(info.getIsOffshore()) ? "是" : "否");
        projectInfo.setProjectSynopsis(info.getPrjDesc());
        
        // 项目是否包含当前周期
        boolean flag = false;
        if (!verifyCycle(projectInfo.getStartDate(), projectInfo.getEndDate(), flag)) {
			return null;
		}

        // 计费类型 FP/TM，info.getCurTradmodeName() 调整为 getPricingModealName
        projectInfo.setType(info.getPricingModealName());
        
        // FP项目自动上线
		int projectOffline = codeGainTypeDao.queryProjectOffline(projectInfo.getNo(), "162", "1");
		String offlineDate = DateUtils.getModifyDay(DateUtils.SHORT_FORMAT_GENERAL.format(new Date()));
		
		if ("FP".equalsIgnoreCase(info.getPricingModealName()) && StringUtils.isBlank(isClose) && 0 == projectOffline
				&& "2012实验室".equals(projectInfo.getHwpdu())) {
			measureAchievementStatusService.updateProjectOfflineStatus(projectInfo.getNo(), offlineDate, "0", "162");
		}
		
		if (!"2012实验室".equals(projectInfo.getHwpdu())) {
			measureAchievementStatusService.updateProjectOfflineStatus(projectInfo.getNo(), offlineDate, "1", "162");
		}

        projectInfo.setImportDate(new Date());
        projectInfo.setImportUser("admin");

        projectInfo.setTeamName(info.getTeamName());
        int teamId = projectTeamService.getTeamId(info.getTeamName());
        projectInfo.setTeamId(teamId);

        try {
            projectListDao.replaceInfo(projectInfo);
        } catch (Exception e) {
            logger.error("projectListDao.replaceInfo exception, error: " + e.getMessage());
            projectInfo = null;
        }

        return projectInfo;
    }

	private void saveDeployIteration(ProjectEntity info) {
        try {
            // 为当前导入项目创建迭代信息
            String id = UUIDUtil.getNew();

            String projectNumber = projectListDao.isExistProject(info.getPrjId());
            if (StringUtils.isBlank(projectNumber)) {
                IterationCycle cle = new IterationCycle();
                cle.setId(id);
                cle.setIteName("迭代1");
                cle.setIsDeleted("0");
                cle.setProNo(info.getPrjId());
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

    /**
     * 项目状态关闭
     */
    private void closeProjects(String nowSateString) {
        try {
            projectListDao.closeProjects(nowSateString);
        } catch (Exception e) {
            logger.error("projectListDao.closeProjects exception, error: " + e.getMessage());
        }
    }
    
	private boolean verifyCycle(Date startDate, Date endDate, boolean flag) {
		try {
			List<String> cycleList = DateUtils.getLatestCycles(2, true);
			String beginCycle = DateUtils.getNextDay(cycleList.get(1));
			String endCycle = cycleList.get(0);

			if (DateUtils.differenceTime(endCycle, DateUtils.SHORT_FORMAT_GENERAL.format(startDate)) <= 0
					&& DateUtils.differenceTime(beginCycle, DateUtils.SHORT_FORMAT_GENERAL.format(endDate)) >= 0) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error("ProjectCollectService verifyCycle failed:" + e.getMessage());
		}
		return flag;
	}
    
}
