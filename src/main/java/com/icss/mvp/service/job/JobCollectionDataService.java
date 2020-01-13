package com.icss.mvp.service.job;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.*;
import com.icss.mvp.entity.*;

/**
 * 
 * @author up
 * 
 */
@Service("jobCollectionDataService")
@EnableScheduling
@PropertySource("classpath:task.properties")
@SuppressWarnings("all")
@Transactional
public class JobCollectionDataService {

	private static Logger logger = Logger.getLogger(JobCollectionDataService.class);
	@Resource
	private ProjectInfoVoDao projectInfoVoDao;
	@Resource
	private IProjectListDao projectListDao;
	@Autowired
	private IProjectSourceConfigDao sourceDao;	
	@Resource
	private OpDepartmentDao opDepartmentDao;
	@Resource
	private SysHwdeptDao SysHwdeptDao;
	@Resource
	private TblAreaDao TblAreaDao;

//	@Scheduled(cron = "${job-collection-data}")
	public void dingshi() {
		List<Map<String,Object>> opDepartmentsList = opDepartmentDao.getOpDepartmentBylevel("2",new HashSet<>());
		Map<String,Object> opDepartmentsBu = new HashMap<>();
		for (Map<String,Object> opDepartment: opDepartmentsList ) {
			opDepartmentsBu.put(StringUtilsLocal.valueOf(opDepartment.get("dept_name")),opDepartment.get("dept_id"));
		}
		opDepartmentsList = opDepartmentDao.getOpDepartmentBylevel("3",new HashSet<>());
		Map<String,Object> opDepartmentsPdu = new HashMap<>();
		for (Map<String,Object> opDepartment: opDepartmentsList ) {
			opDepartmentsPdu.put(StringUtilsLocal.valueOf(opDepartment.get("dept_name")),opDepartment.get("dept_id"));
		}
		opDepartmentsList = opDepartmentDao.getOpDepartmentBylevel("4",new HashSet<>());
		Map<String,Object> opDepartmentsDu = new HashMap<>();
		for (Map<String,Object> opDepartment: opDepartmentsList ) {
			opDepartmentsDu.put(StringUtilsLocal.valueOf(opDepartment.get("dept_name")),opDepartment.get("dept_id"));
		}
		List<Map<String,Object>> SysHwdeptList= SysHwdeptDao.getSysHwdeptBylevel("1",new HashSet<>());
		Map<String,Object> SysHwdeptsPdu  = new HashMap<>();
		for (Map<String,Object> SysHwdept: SysHwdeptList ) {
			SysHwdeptsPdu.put(StringUtilsLocal.valueOf(SysHwdept.get("dept_name")),SysHwdept.get("dept_id").toString());
		}
		SysHwdeptList = SysHwdeptDao.getSysHwdeptBylevel("2",new HashSet<>());
		Map<String,Object> SysHwdeptsZpdu = new HashMap<>();
		for (Map<String,Object> SysHwdept: SysHwdeptList ) {
			SysHwdeptsZpdu.put(StringUtilsLocal.valueOf(SysHwdept.get("dept_name")),SysHwdept.get("dept_id").toString());
		}
		SysHwdeptList = SysHwdeptDao.getSysHwdeptBylevel("3",new HashSet<>());
		Map<String,Object> SysHwdeptsPduSpdt = new HashMap<>();
		for (Map<String,Object> SysHwdept: SysHwdeptList ) {
			SysHwdeptsPduSpdt.put(StringUtilsLocal.valueOf(SysHwdept.get("dept_name")),SysHwdept.get("dept_id").toString());
		}
		List<TblArea> TblAreaList = TblAreaDao.getAllTblArea();
		Map<String,Object> TblArea = new HashMap<>();
		for (TblArea TblAreas: TblAreaList ) {
			TblArea.put(TblAreas.getAreaName(),TblAreas.getAreaCode());
		}
		ProjectInfo proj = new ProjectInfo();
		String sort = null;
		String order = null;
		List<ProjectInfo> projs=projectListDao.getList(proj, sort, order);
		for(ProjectInfo proj1:projs) {
			proj1.setBuId((String) opDepartmentsBu.get(proj1.getBu()));
			proj1.setPduId((String) opDepartmentsPdu.get(proj1.getPdu()));
			proj1.setDuId((String) opDepartmentsDu.get(proj1.getDu()));
			proj1.setHwpduId((String) SysHwdeptsPdu.get(proj1.getHwpdu()));
			proj1.setHwzpduId((String) SysHwdeptsZpdu.get(proj1.getHwzpdu()));
			proj1.setPduSpdtId((String) SysHwdeptsPduSpdt.get(proj1.getPduSpdt()));
			proj1.setAreaId((String) TblArea.get(proj1.getArea()));
			projectListDao.update(proj1);
		}


	}
	
	
	//@Scheduled(cron = "${job-collection-data}")
	public void saveEfficiency() {
		Map<String, Object> progectInfoSelect = new HashMap<>();
		List<ProjectInfoVo> projectInfos = projectInfoVoDao.projectInfos(progectInfoSelect);
		
		for (ProjectInfoVo projectInfoVo : projectInfos) {
			try {
				String no = projectInfoVo.getNo();
				List<RepositoryInfo> infos = sourceDao.searchRepositoryByNo(no);
				if(infos==null||infos.size()<=0) {
					continue;
				}
				String ids = "";
				String type = "";
				Map<String, Object> types = new HashMap<>();
				for (RepositoryInfo repositoryInfo : infos) {
					ids += repositoryInfo.getId()+";";
					//对接平台类型1:DTS，2:SVN，3:Git，4:TMSS，5:ci，6:iso
					if(repositoryInfo.getType()==1) {
						types.put("dts", 1);
					}else if (repositoryInfo.getType()==2) {
						types.put("svn", 1);
					}else if (repositoryInfo.getType()==3) {
						types.put("git", 1);
					}else if (repositoryInfo.getType()==4) {
						types.put("tmss", 1);
					}else if (repositoryInfo.getType()==5) {
						types.put("icp-ci", 1);
					}else if (repositoryInfo.getType()==8) {
						types.put("smartIDE", 1);
					}
				}
				
				for (String repositoryInfo : types.keySet()) {
					type+=repositoryInfo+";";
				}
				ids = ids.substring(0, ids.length()-1);
				type = type.substring(0, type.length()-1);
				CollectionJobInfo collectionJobInfo = new CollectionJobInfo();
				collectionJobInfo.setIds(ids);
				collectionJobInfo.setSelectedIds(ids);
				collectionJobInfo.setNo(no);
				collectionJobInfo.setName("数据采集");
				collectionJobInfo.setTypes(type);
				collectionJobInfo.setProgressLast("0");
				collectionJobInfo.setNum("0");
				sourceDao.insertCollectionJob(collectionJobInfo);
			} catch (Exception e) {
				logger.error("异常：",e);
			}
		}
		
	}
}
