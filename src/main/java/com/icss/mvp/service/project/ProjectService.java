package com.icss.mvp.service.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.util.CollectionUtilsLocal;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.constant.ERatingStar;
import com.icss.mvp.dao.*;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.system.UserEntity;
import com.icss.mvp.service.ProjectInfoService;
import com.icss.mvp.util.BigDecUtils;
import com.icss.mvp.util.DateUtils;

/**
 * Created by Ray on 2018/6/6.
 */
@Service("projectService")
public class ProjectService {

    private static Logger           logger = Logger.getLogger(ProjectService.class);

    // @Autowired
    // private IBuOverviewDao buOverviewDao;

    @Autowired
    private IProjectListDao         projectListDao;

    @Autowired
    private IProjectKeyrolesDao     keyRolesDao;

    @Autowired
    private IProjectSourceConfigDao configDao;

    @Autowired
    private IProjectInfoDao         projectInfoDao;

    @Autowired
    private IPcbListDao             pcbListDao;
    
    @Autowired
    private IProjectSourceConfigDao sourceDao;
    
    @Resource
    private ProjectInfoService projectInfoService;

    public ListResponse<ProjectInfo> describeProject(ProjectInfo project, String clientType, String userName) {
        return describeProject(project, clientType, null, userName);
    }
    
    /**
     * 获取项目信息列表，支持Name，PM姓名模糊查询，部门，地域，项目状态精确查询
     *
     * @param project 查询条件
     * @param clientType 组织结构类型，默认：华为，1：中软
     * @param pager 分页信息
     * @param userName 登陆用户信息
     * @return
     */
    public PageResponse<ProjectInfo> describeProject(ProjectInfo project, String clientType, PageRequest pager,
                                                     String userName) {
        Map<String, Object> parameter = new HashMap<>();
        // parameter.put("client", clientType);
        // parameter.put("depts", depts);

        // series 传入值为 1 是按中软组织结构查询，其他都按华为组织机构处理
        if (!"1".equals(clientType)) {// 华为
            parameter.put("hwpduId", CollectionUtilsLocal.splitToList(project.getHwpdu()));
            parameter.put("hwzpduId", CollectionUtilsLocal.splitToList(project.getHwzpdu()));
            parameter.put("pduSpdtId", CollectionUtilsLocal.splitToList(project.getPduSpdt()));
        } else {// 中软
            parameter.put("buId", CollectionUtilsLocal.splitToList(project.getBu()));
            parameter.put("pduId", CollectionUtilsLocal.splitToList(project.getPdu()));
            parameter.put("duId", CollectionUtilsLocal.splitToList(project.getDu()));
        }
        parameter.put("areas", CollectionUtilsLocal.splitToList(project.getArea()));
        parameter.put("status", CollectionUtilsLocal.splitToList(project.getProjectState()));
        parameter.put("name", project.getName());
        parameter.put("manager", project.getPm());
        // parameter.put("startDate", project.getStartStr());
        // parameter.put("endDate", project.getEndStr());
        if (!ERatingStar.ALL.title.equals(project.getStarType())
            && !ERatingStar.ZERO.title.equals(project.getStarType())) {
            parameter.put("star", project.getStarType());
        }
        if (StringUtils.isNotEmpty(userName)) {
            parameter.put("userName", userName);
        }
        // List<ProjectInfo> data = new ArrayList<>();
        // 需要全量查询而不是分页查询结果时，可以传入空值pager
        // 第一步获取符合条件的记录总数，全量查询跳过这一步
        // 跟符合条件的记录总数为0的情况做区分，全量查询设定total值为 -1
        // int total = pager == null ? -1 : countTotal(parameter);
        // if (pager == null) {
        // pager = new PageRequest();
        // }
        //
        // // 符合查询条件的结果为0，跳过第二步实体列表查询
        // if (total != 0) {
        // // 总数大于零，设定offset和limit，获取指定页记录
        // if (total > 0) {
        // parameter.put("offset", pager.getOffset(total));
        // parameter.put("limit", pager.getPageSize());
        // }
        //
        // parameter.put("order", "START_DATE, END_DATE, NAME");
        // parameter.put("sort", "ASC");
        //
        // data = projectInfoDao.queryProject(parameter);
        // }

        parameter.put("order", "START_DATE, END_DATE, NAME");
        parameter.put("sort", "ASC");

        PageResponse<ProjectInfo> result = new PageResponse<>();

        List<ProjectInfo> data = null;
        try {
            Page page = PageHelper.startPage((pager.getPageNumber()) + 1, pager.getPageSize());
            if (StringUtils.isNotBlank(project.getTeamId())) {
                parameter.put("teamId", project.getTeamId());
                data = projectInfoDao.queryProjectInfo(parameter);
            } else {
                data = projectInfoDao.queryProject(parameter);
            }

            result.setTotalCount((int) page.getTotal());
        } catch (Exception e) {
            logger.error("projectInfoDao.queryProject exception, error: " + e.getMessage() + ", params: "
                         + JSON.toJSONString(parameter));
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }
        result.setData(data);

        result.setPageSize(pager.getPageSize());
        result.setPageNumber(pager.getPageNumber() == null ? 0 : pager.getPageNumber());

        // //处理日期的显示
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // for (int i = 0; i < data.size(); i++) {
        // data.get(i).setStartStr(sdf.format(data.get(i).getStartDate()));
        // data.get(i).setEndStr(sdf.format(data.get(i).getEndDate()));
        // }

        // result.setPageSize(pager.getPageSize());
        // result.setPageNumber(pager.getPageNumber() == null ? 0 : pager.getPageNumber());
        // // result.setTotalCount(total == -1 ? data.size() : total);
        // result.setTotalCount((int) page.getTotal());

        return result;
    }

//    private int countTotal(Map<String, Object> parameter) {
//        int total;
//        try {
//            total = projectInfoDao.countProject(parameter);
//        } catch (Exception e) {
//            logger.error("projectInfoDao.countProject exception, error: "+e.getMessage());
//            total = 0;
//        }
//        return total;
//    }

//    public List<MeasureInfo> getProjectDetail(ProjectInfo projectInfo) {
//        List<MeasureInfo> result;
//
//        try {
//            Map<String, Object> map = generateFilter(projectInfo);
//            result = buOverviewDao.getProjDetail(map);
//        } catch (Exception e) {
//            result = new ArrayList<>();
//            logger.error("buOverviewDao.getProjDetail exception, error: "+e.getMessage());
//        }
//
//        return result;
//    }

//    private Map<String, Object> generateFilter(ProjectInfo projectInfo) {
//        Map<String, Object> result = new HashMap<>();
//        if (StringUtils.isNotBlank(projectInfo.getName())) {
//            result.put("name", projectInfo.getName());
//        }
//        if (StringUtils.isNotBlank(projectInfo.getPm())) {
//            result.put("pm", projectInfo.getPm());
//        }
//
//        result.put("area", splitToSet(projectInfo.getArea(), ","));
//
//        result.put("hwpdu", splitToSet(projectInfo.getHwpdu(), ","));
//        result.put("hwzpdu", splitToSet(projectInfo.getHwzpdu(), ","));
//        result.put("pduSpdt", splitToSet(projectInfo.getPduSpdt(), ","));
//
//        result.put("bu", splitToSet(projectInfo.getBu(), ","));
//        result.put("pdu", splitToSet(projectInfo.getPdu(), ","));
//        result.put("du", splitToSet(projectInfo.getDu(), ","));
//
//        result.put("projectState", splitToSet(projectInfo.getProjectState(), ","));
//
//        return result;
//    }

//    private Set<String> splitToSet(String union, String separator) {
//        Set<String> result = new HashSet<>();
//        if (StringUtils.isBlank(union)) {
//            return result;
//        }
//
//        String[] splits = union.split(separator);
//        for (String element : splits) {
//            if (StringUtils.isNotBlank(element)) {
//                result.add(element);
//            }
//        }
//        return result;
//    }

    /**
     * 新增或更新项目信息
     * 
     * @param project project detail info
     * @return if error occurs return -1
     */
    public int createProject(ProjectDetailInfo project) {
        int result;

        try {
            result = projectListDao.replaceInfo(project);
        } catch (Exception e) {
            logger.error("projectListDao.replaceInfo exception, error: " + e.getMessage());
            result = -1;
        }

        return result;
    }
    /**
     * 更新团队信息
     * @param team
     * @return
     */
    public Object createTeam(ProjectTeam team) {
        int result;
        List<String> list = projectListDao.getTeamNames();
        boolean bool = list.contains(team.getTeamName());
        if(bool){
        	return null;
        }else{
        	try {
                result = projectListDao.replaceTeam(team);
            } catch (Exception e) {
                logger.error("projectListDao.replaceTeam exception, error: " + e.getMessage());
                result = -1;
            }
        }
        return result;
    }
    
    /**
     * 新增或更新项目经理
     * 
     * @param manager project manager info
     * @return if error occurs return -1
     */
    public int createManger(ProjectManager manager) {
        int result;

        try {
            result = projectListDao.replaceMember(manager);
        } catch (Exception e) {
            logger.error("projectListDao.replaceMember exception, error: " + e.getMessage());
            result = -1;
        }

        return result;
    }

    /**
     * 新增或更新项目关键角色列表
     *
     * @param roles project key roles
     * @return if error occurs return -1
     */
    public int createKeyRoles(List<ProjectKeyroles> roles) {
        int result;

        try {
            result = keyRolesDao.insertInfos(roles);
        } catch (Exception e) {
            logger.error("keyRolesDao.insertInfos exception, error: " + e.getMessage());
            result = -1;
        }

        return result;
    }

    /**
     * 更新关键角色的考试成就
     * 
     * @param account 中软工号
     * @param passed 是否通过 PMP/RDPM 考试
     * @return if error occurs return -1
     */
    public int updateScore(String account, boolean passed) {
        int result;

        try {
            result = keyRolesDao.updateScore(account, passed ? "通过" : "未通过");
        } catch (Exception e) {
            logger.error("keyRolesDao.updateScore exception, error: " + e.getMessage());
            result = -1;
        }

        return result;
    }

    /**
     * 新增或更新项目经理
     * 
     * @param member project member info
     * @return if error occurs return -1
     */
    public int createOMP(ProjectMember member) {
        int result;

        try {
            result = projectListDao.insertmemberInfo(member);
        } catch (Exception e) {
            logger.error("projectListDao.insertmemberInfo exception, error: " + e.getMessage());
            result = -1;
        }

        return result;
    }

    public ProjectMember getMemberByIdentity(String identityNumber) {
        ProjectMember result = null;

        try {
        	String identityNumber1 = identityNumber.substring(0, 3);
        	String identityNumber2 = identityNumber.substring(14);
        	identityNumber = identityNumber1+"%"+identityNumber2;
            List<ProjectMember> matched = projectListDao.queryMembersByIdCard(identityNumber);
            if (matched != null && matched.size() >= 1) {
                result = matched.get(0);
            }
        } catch (Exception e) {
            result = null;
        }

        return result;
    }

    /**
     * 工时记录添加
     * 
     * @param clock clock record
     * @return if error occurs return -1
     */
    public int createClock(TblTimeInformation clock) {
        int result;

        try {
            projectListDao.inserTimeInformation(clock);
            result = 1;
        } catch (Exception e) {
            logger.error("projectListDao.inserTimeInformation exception, error: " + e.getMessage());
            result = -1;
        }

        return result;
    }

    public RepositoryInfo getRepository(String id) {
        RepositoryInfo result;

        try {
            List<RepositoryInfo> list = configDao.searchRepositoryByIds("(" + id + ")");
            result = list != null && !list.isEmpty() ? list.get(0) : null;
        } catch (Exception e) {
            logger.error("configDao.searchRepositoryByNos exception, error: " + e.getMessage());
            result = null;
        }

        return result;
    }
    /**
     * 查询pcb样本项目配置
     * @param project
     * @param clientType
     * @param pager
     * @return
     */
    public PageResponse<Map<String,Object>> describePcbProject(ProjectInfo project, String clientType, PageRequest pager) {
    	PageResponse<ProjectInfo> pageResponse = describeProject(project, clientType, pager, null);
        List<ProjectInfo> projectInfos = pageResponse.getData();
        List<String> proNos = new ArrayList<>();
        for (ProjectInfo projectInfo : projectInfos) {
            proNos.add(projectInfo.getNo());
        }

        Map<String,Object> parameter =  new HashMap<>();
        parameter.put("pcbCategory", project.getPcbCategory());
        parameter.put("proNos", proNos);
        String[] measureIds = project.getMeasureId().split(","); 
        parameter.put("measureIds", measureIds);
        
        List<Map<String,Object>> projectConfigs= pcbListDao.queryPcbProjectConfigListByNo(parameter);
        List<Map<String,Object>> ret = new ArrayList<>();
        for (ProjectInfo projectInfo : projectInfos) {
            String no =  projectInfo.getNo();
            Map<String,Object> retMap =  new HashMap<>();
            retMap.put("no", no);
            retMap.put("name", projectInfo.getName());
            retMap.put("pm", projectInfo.getPm());
            for (String measureId : measureIds) {
            	retMap.put("id"+measureId, "1");
			}
            for (Map<String,Object> projectConfig: projectConfigs) {
                if(no.equals(projectConfig.get("pro_no")) && null != projectConfig.get("measure_id")){
                	retMap.put("id"+projectConfig.get("measure_id"), projectConfig.get("status"));
                }
            }
            ret.add(retMap);
        }

        PageResponse<Map<String,Object>> retResponse = new PageResponse<>();
        retResponse.setPageNumber(pageResponse.getPageNumber());
        retResponse.setPageCount(pageResponse.getPageCount());
        retResponse.setPageSize(pageResponse.getPageSize());
        retResponse.setData(ret);
        retResponse.setTotalCount(pageResponse.getTotalCount());;
        return retResponse;
    }
    
    public Integer addFavoriteProject(String proNo,String userID){
		return projectInfoDao.addFavoriteProject(proNo,userID);
	}
	
	public Integer deleteFavoriteProject(String proNo,String userID){
		return projectInfoDao.deleteFavoriteProject(proNo,userID);
	}
	
	public List<ProjectInfo> queryFavoriteProject(String userID){
		return projectInfoDao.queryFavoriteProject(userID);
	}

	public List<String> getNO(Map<String, Object> map) {
		return projectInfoDao.getNO(map);
	}

	public String getPdu(Map<String, Object> bm) {
		return projectInfoDao.getPdu(bm);
	}

	public Object getState(String dateend, String proNo) {
		return projectInfoDao.getState(dateend,proNo);
	}

	public List<String> getNo1(List<String> no) {
		return projectInfoDao.getNo1(no);
	}

	public List<Map<String, String>> getPduspt(Map<String, Object> map) {
		return projectInfoDao.getPduspt(map);
	}

	public List<String> getNOByPduspt(Map<String, Object> map) {
		return projectInfoDao.getNOByPduspt(map);
	}

	public List<String> getNoPD(String pduspdt) {
		return projectInfoDao.getNoPD(pduspdt);
	}

	public String getPduspt1(String pduspdt) {
		return projectInfoDao.getPduspt1(pduspdt);
	}

	public ProjectBoard getStateNumber(String lastday, List<String> nop) {
		return projectInfoDao.getStateNumber(lastday,nop);
	}

	public ProjectBoard getTwoWeekState(String lastday, List<String> no,String midday) {
		return projectInfoDao.getTwoWeekState(lastday,no,midday);
	}

	public int getYellow(String midDay, List<String> no,String midday) {
		return projectInfoDao.getYellow(midDay,no,midday);
	}
	/**
	 * 卡片2 (在行项目数、上个月立项项目数、当月结项xian)
	 * @param result
	 */
	public void getProjectsInline(PlainResponse<Map<String, Object>> result,UserEntity uEntity)throws Exception{
		Map<String, Object> parameter = new HashMap<>();
		strSql(uEntity.getTrunkHW());
		parameter.put("hw", strSql(uEntity.getTrunkHW()));
		parameter.put("op", strSql(uEntity.getTrunkOP()));
		parameter.put("type", uEntity.getType());
		Integer aInteger = projectInfoDao.getTotalProNums(parameter);
		Map<String, Object> map = projectInfoDao.getProjectsInline();
		map.put("inLinePro", aInteger);
		result.setData(map);
	}
	/**
	 * 发布项目数量
	 * @param result
	 */
	public void getReleasePro(PlainResponse<Map<String, Object>> result)throws Exception {
//		List<String> dates = projectReviewService.queryProjectCycle();//发布时间集合
		Map<String, Object> map = projectInfoDao.getReleasePro(getBiweekly(0));
		map.put("noexcutePro", BigDecUtils.sub(Double.valueOf(map.get("inline").toString()), Double.valueOf(map.get("excute").toString())));
		result.setData(map);
	}

    /**
     * 获取在当前双周之前的第n个双周结束时间
     *
     * @param amount 前n个双周
     * @return
     */
    public static String getBiweekly(int amount) {
        LocalDate current = LocalDate.now();

        Integer round = amount / 2;
        Integer mod = amount % 2;

        current = current.minus(round, ChronoUnit.MONTHS);

        if (mod == 1) {
            if (current.getDayOfMonth() > 15) {
                current = current.withDayOfMonth(15);
            } else {
                current = current.minus(1, ChronoUnit.MONTHS);
                current = current.with(TemporalAdjusters.lastDayOfMonth());
            }
        } else {
            if (current.getDayOfMonth() > 15) {
                current = current.with(TemporalAdjusters.lastDayOfMonth());
            } else {
                current = current.withDayOfMonth(15);
            }
        }

        return current.format(getFormatter("yyyy-MM-dd"));
    }

    private static final String ERROR_MESSAGE_PATTERN = "%s exception, error: %s%s";

    private static DateTimeFormatter getFormatter(String... pattern) {
        DateTimeFormatter result = DateTimeFormatter.BASIC_ISO_DATE;
        try {
            if (pattern.length > 0) {
                result = DateTimeFormatter.ofPattern(pattern[0]);
            }
        } catch (Exception e) {
            String detail = String.format(", pattern: %s", pattern);
            logger.error(String.format(ERROR_MESSAGE_PATTERN, "DateTimeFormatter.ofPattern", e.getMessage(), detail));
        }

        return result;
    }
	
	public static String strSql(String par){
		List<String> list = new ArrayList<>();
		if(StringUtils.isNotEmpty(par)){
			String str[] = par.split(",");
			for (String s : str) {
				list.add(s);
			}
			return "("+ StringUtilsLocal.listToSqlIn(list)+")";
		}
		return null;
	}

	public TableSplitResult<List<Map<String,Object>>> checkboxOnclickData(ProjectInfo projectInfo,PageRequest pageRequest,String username) {
		TableSplitResult<List<Map<String,Object>>> ret = new TableSplitResult<List<Map<String,Object>>>();
		Set<String> nos = projectInfoService.projectNos(projectInfo, null);
		List<String> proNos = new ArrayList<>(nos);
		//获取已选择的项目编号，因为用Arrays.asList不能进行removeAll的操作故用如下函数
		List<String> select = CollectionUtilsLocal.splitToList(projectInfo.getMeasureId());
		List<String> collectionNos = CollectionUtilsLocal.splitToList(projectInfo.getTeamId());
		if("1".equals(projectInfo.getCoopType())) {
			proNos.removeAll(collectionNos);
		}
		if("0".equals(projectInfo.getType()) && StringUtilsLocal.isBlank(projectInfo.getCoopType())) {
			proNos.retainAll(select);
		}else if(StringUtilsLocal.isBlank(projectInfo.getCoopType())){
			proNos.removeAll(select);
		}
		com.github.pagehelper.Page page = PageHelper.startPage((pageRequest.getPageNumber() == null ? 0 : (pageRequest.getPageNumber() - 1)) + 1,pageRequest.getPageSize());
		List<Map<String,Object>> map = projectInfoDao.checkboxOnclickData("(" + StringUtilsLocal.listToSqlIn(proNos) + ")",projectInfo.getMonth());
		ret.setTotal((int) page.getTotal());
		ret.setRows(map);
		return ret;
	}
	
	public Map<String,Object> getClouddragonParameters(String no, String repositoryId, String token){
	  ProjectInfo projInfo = projectListDao.getProjInfoByNo(no);
	  Map<String,Object> parameter = new HashMap<String,Object>();
	  if(null != projInfo && null != projInfo.getStartDate()) {
	    List<RepositoryInfo> repositoryInfos = sourceDao.searchRepositoryByNos(no, "7", repositoryId);
	    if(null!=repositoryInfos && repositoryInfos.size()>0) {
	      parameter.put("startTime",  DateUtils.STANDARD_FORMAT_GENERAL.format(projInfo.getStartDate()));
	      parameter.put("endTime",  DateUtils.STANDARD_FORMAT_GENERAL.format(new Date()));
	      parameter.put("proNo", no);
	      parameter.put("pipelineId", repositoryInfos.get(0).getUrl());
	      JSONObject jsonObject = JSON.parseObject(repositoryInfos.get(0).getScope());
	      parameter.put("stageName", jsonObject.getJSONArray("excludePath").get(0));
	      parameter.put("jobName", jsonObject.getJSONArray("excludeRevision").get(0)); 
	     }
	  }
	  return parameter;
	}
	
}
