package com.icss.mvp.service;

import com.icss.mvp.dao.IBuOverviewDao;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.dao.ProjectInfoVoDao;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.util.AuthUtil;
import com.icss.mvp.util.CollectionUtilsLocal;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ProjectInfoService {
    @Autowired
    private ProjectInfoVoDao projectInfoVoDao;
    @Autowired
    private IBuOverviewDao buOverviewDao;
    @Resource
    IUserManagerDao userManagerDao;

    private static final String NAME = "name";
    private static final String PM = "pm";
    public static final String AREA = "area";
    public static final String HW_DEPTS = "hwDepts";
    public static final String OPT_DEPTS = "optDepts";
    public static final String PROJECT_STATE = "projectState";


    public List<ProjectInfoVo> queryOldProjectInfo(ProjectInfo projectInfo,
                                                   String username,
                                                   String no,
                                                   String projectState) {
//		List<ProjectInfoVo> projectInfoVo = projectInfoVoDao.queryOldProjectInfo(no,projectState);
//		return projectInfoVo;
        return null;
    }

    public List<ProjectInfoVo> projectInfos(ProjectInfo projectInfo,
                                            String username,
                                            PageInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        setParamNew(projectInfo, username, map);
        Integer index = pageInfo.getCurrentPage();
        if (null != index) {
            int count = projectInfoVoDao.projectInfoCount(map);
            pageInfo.setTotalRecord(count);
            int fromIndex = (index - 1) * pageInfo.getPageSize();
            map.put("fromIndex", fromIndex);
            map.put("pageSize", pageInfo.getPageSize());
        }
        List<ProjectInfoVo> projectInfoVos = projectInfoVoDao.projectInfos(map);

        return projectInfoVos;
    }

    public List<ProjectInfo> getProjectInfos(String projectId,
                                             String username,
                                             PageInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        Integer index = pageInfo.getCurrentPage();
        if (null != index) {
            int count = projectInfoVoDao.projectCountByProjectId(projectId);
            pageInfo.setTotalRecord(count);
            int fromIndex = (index - 1) * pageInfo.getPageSize();
            map.put("fromIndex", fromIndex);
            map.put("pageSize", pageInfo.getPageSize());
            map.put("projectId", projectId);
        }
        List<ProjectInfo> projectInfos = projectInfoVoDao.getProjectInfos(map);

        return projectInfos;
    }

    public List<ProjectInfoVo> projectInfos(ProjectInfo projectInfo,
                                            String username) {
        Map<String, Object> map = new HashMap<>();
        setParamNew(projectInfo, username, map);
        List<ProjectInfoVo> projectInfoVos = projectInfoVoDao.projectInfos(map);

        return projectInfoVos;
    }

    /**
     * 查询用户具有权限的项目编号列表
     *
     * @param projectInfo
     * @param username
     * @return
     */
    public Set<String> projectNos(ProjectInfo projectInfo, String username) {
        Map<String, Object> map = new HashMap<>();
        setParamNew(projectInfo, username, map);
        return new HashSet<>(projectInfoVoDao.projectNos(map));
    }

    /**
     * 查询收藏项目信息
     *
     * @param projectInfo
     * @param username
     * @return
     */
    public Set<String> collectedProjectNos(ProjectInfo projectInfo, String username) {
        Map<String, Object> map = new HashMap<>();
        setParamNew(projectInfo, null, map);
        map.put("username", username);
        map.put("coopType", projectInfo.getCoopType());
        return new HashSet<>(projectInfoVoDao.collectedProjectNos(map));
    }

    public void setParamNew(ProjectInfo projectInfo, String username, Map<String, Object> parameter) {
        if (StringUtils.isNotBlank(username)) {
            UserInfo userInfo = userManagerDao.getUserInfoByName(username);
            if ("0".equals(projectInfo.getClientType())) {// 华为
                if (StringUtilsLocal.isBlank(projectInfo.getHwpdu())) {
                    projectInfo.setHwpdu(userInfo.getHwpdu() == null ? "0" : userInfo.getHwpdu());
                }
                if (StringUtilsLocal.isBlank(projectInfo.getHwzpdu())) {
                    projectInfo.setHwzpdu(userInfo.getHwzpdu() == null ? "" : userInfo.getHwzpdu());
                }
                if (StringUtilsLocal.isBlank(projectInfo.getPduSpdt())) {
                    projectInfo.setPduSpdt(userInfo.getPduspdt() == null ? "" : userInfo.getPduspdt());
                }
            } else if ("1".equals(projectInfo.getClientType())) {// 中软
                if (StringUtilsLocal.isBlank(projectInfo.getBu())) {
                    projectInfo.setBu(userInfo.getBu() == null ? "0" : userInfo.getBu());
                }
                if (StringUtilsLocal.isBlank(projectInfo.getPdu())) {
                    projectInfo.setPdu(userInfo.getDu() == null ? "" : userInfo.getDu());
                }
                if (StringUtilsLocal.isBlank(projectInfo.getBu())) {
                    projectInfo.setDu(userInfo.getDept() == null ? "" : userInfo.getDept());
                }
            }
        }
        parameter.put("client", projectInfo.getClientType());
        if ("0".equals(projectInfo.getClientType())) {//华为
            List<String> hwpduId = CollectionUtilsLocal.splitToList(projectInfo.getHwpdu());
            List<String> hwzpduId = CollectionUtilsLocal.splitToList(projectInfo.getHwzpdu());
            List<String> pduSpdtId = CollectionUtilsLocal.splitToList(projectInfo.getPduSpdt());
            parameter.put("hwpduId", hwpduId);
            parameter.put("hwzpduId", hwzpduId);
            parameter.put("pduSpdtId", pduSpdtId);
        } else if ("1".equals(projectInfo.getClientType())) {//中软
            List<String> buId = CollectionUtilsLocal.splitToList(projectInfo.getBu());
            List<String> pduId = CollectionUtilsLocal.splitToList(projectInfo.getPdu());
            List<String> duId = CollectionUtilsLocal.splitToList(projectInfo.getDu());
            parameter.put("buId", buId);
            parameter.put("pduId", pduId);
            parameter.put("duId", duId);
        }
        List<String> areas = CollectionUtilsLocal.splitToList(projectInfo.getArea());
        List<String> status = CollectionUtilsLocal.splitToList(projectInfo.getProjectState());
        parameter.put("areas", areas);
        parameter.put("status", status);
        parameter.put("name", projectInfo.getName());
        parameter.put("pm", projectInfo.getPm());
    }

    /**
     * 根据项目编号查询项目进度列表
     *
     * @param nos
     * @param name
     * @return
     */
    public List<ProjectInfoVo> projectScheduleList(String[] nos, String name) {
        return projectInfoVoDao.projectScheduleList(Arrays.asList(nos), name);
    }

    /**
     * 设置查询参数
     *
     * @param projectInfo
     * @param username
     * @param map         参数集合
     */
    public void setParam(ProjectInfo projectInfo, String username, Map<String, Object> map) {
        String auth = buOverviewDao.getAuthOpDepartment(username);
        Map<String, Object> authMap = AuthUtil.authMap(auth);
        if (StringUtils.isNotBlank(projectInfo.getName())) {
            map.put(NAME, projectInfo.getName());
        }

        if (StringUtils.isNotBlank(projectInfo.getPm())) {
            map.put(PM, projectInfo.getPm());
        }

        if (StringUtils.isBlank(projectInfo.getArea())) {
            map.put(AREA, authMap.get(AuthUtil.AREA));
        } else {
            map.put(AREA, Arrays.asList(projectInfo.getArea().split(",")));
        }
        //添加中软部门编号
        deptParam(map,
                (Map<String, Object>) authMap.get(AuthUtil.OPT_DEPT),
                projectInfo.getBu(),
                projectInfo.getPdu(),
                projectInfo.getDu(),
                OPT_DEPTS);
        //添加华为部门编号
        deptParam(map,
                (Map<String, Object>) authMap.get(AuthUtil.HW_DEPT),
                projectInfo.getHwpdu(),
                projectInfo.getHwzpdu(),
                projectInfo.getPduSpdt(),
                HW_DEPTS);
        if (StringUtils.isNotBlank(projectInfo.getProjectState())) {
            map.put(PROJECT_STATE, Arrays.asList(projectInfo.getProjectState().split(",")));
        }
    }

    /**
     * 根据参数(bu, pdu, du) 或 (hwpdu, hwzpdu, pduSpdt)初始化查询参数
     *
     * @param paraMap
     * @param authMap
     * @param bu
     * @param pdu
     * @param du
     * @param prefix  paraMap的key
     */
    private void deptParam(Map<String, Object> paraMap,
                           Map<String, Object> authMap,
                           String bu,
                           String pdu,
                           String du,
                           String prefix) {
        Set<String> set = new HashSet<>();
        if (StringUtils.isNotBlank(bu)
                && StringUtils.isNotBlank(pdu)
                && StringUtils.isNotBlank(du)) {
            set.addAll(Arrays.asList(du.split(",")));
            paraMap.put(prefix, set);
            return;
        }

        if (StringUtils.isNotBlank(bu)
                && StringUtils.isNotBlank(pdu)
                && StringUtils.isBlank(du)) {
            String[] bus = bu.split(",");
            for (String buNo : bus) {
                Map<String, List<String>> pduMap = (Map<String, List<String>>) authMap.get(buNo);
                if (pduMap == null) {
                    continue;
                }
                String[] pdus = pdu.split(",");
                for (String pduNo : pdus) {
                    set.addAll(pduMap.get(pduNo));
                }
            }
            paraMap.put(prefix, set);
            return;
        }

        if (StringUtils.isNotBlank(bu)
                && StringUtils.isBlank(pdu)
                && StringUtils.isBlank(du)) {
            String[] bus = bu.split(",");
            for (String buNo : bus) {
                Map<String, List<String>> pduMap = (Map<String, List<String>>) authMap.get(buNo);
                if (pduMap == null) {
                    continue;
                }
                for (List<String> list : pduMap.values()) {
                    set.addAll(list);
                }
            }
            paraMap.put(prefix, set);
            return;
        }

        if (StringUtils.isBlank(bu)
                && StringUtils.isBlank(pdu)
                && StringUtils.isBlank(du)) {
            for (String buNo : authMap.keySet()) {
                Map<String, List<String>> pduMap = (Map<String, List<String>>) authMap.get(buNo);
                if (pduMap == null) {
                    continue;
                }
                for (List<String> list : pduMap.values()) {
                    set.addAll(list);
                }

            }
            paraMap.put(prefix, set);
            return;
        }
    }

    /**
     * 消费者业务线--项目信息查询
     *
     * @param projectInfo
     * @param username
     * @param pageRequest
     * @return
     */
    public List<ProjectInfoVo> queryProjectInfos(ProjectInfo projectInfo, String username, PageRequest pageRequest) {
        Map<String, Object> map = new HashMap<>();
        setParamNew(projectInfo, username, map);
        map.put("offset", pageRequest.getOffset());
        map.put("pageSize", pageRequest.getPageSize());
        return projectInfoVoDao.queryProjectInfos(map);
    }

    /**
     * 消费者业务线--统计项目总数
     *
     * @param projectInfo
     * @param username
     * @return
     */
    public Integer queryProjectInfosCount(ProjectInfo projectInfo, String username) {
        Map<String, Object> map = new HashMap<>();
        setParamNew(projectInfo, username, map);
        return projectInfoVoDao.queryProjectInfosCount(map);
    }


    /**
     * 消费者业务线--统计部门总数
     *
     * @param projectInfo
     * @param username
     * @return
     */
    public Integer queryPDUInfosCount(ProjectInfo projectInfo, String username) {
        Map<String, Object> map = new HashMap<>();
        setParamNew(projectInfo, username, map);
        return projectInfoVoDao.queryPDUInfosCount(map);
    }

    /**
     * 消费者业务线--项目部门查询
     *
     * @param projectInfo
     * @param username
     * @param pageRequest
     * @return
     */
    public List<String> queryProjectPDUInfos(ProjectInfo projectInfo, String username, PageRequest pageRequest) {
        Map<String, Object> map = new HashMap<>();
        setParamNew(projectInfo, username, map);
        map.put("offset", pageRequest.getOffset());
        map.put("pageSize", pageRequest.getPageSize());
        return projectInfoVoDao.queryProjectPDUInfos(map);
    }

    public ProjectInfoVo getByProjectNo(String projectId) {
        return projectInfoVoDao.fetchProjectInfoByKey(projectId);
    }
}
