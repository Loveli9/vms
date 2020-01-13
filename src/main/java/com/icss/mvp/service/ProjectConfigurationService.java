package com.icss.mvp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.IProjectSourceConfigDao;
import com.icss.mvp.dao.ISvnTaskDao;
import com.icss.mvp.entity.CollectionJobInfo;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ProjectConfigInfo;
import com.icss.mvp.entity.ProjectDataSourceInfo;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.RepositoryInfo;
import com.icss.mvp.util.StringMD5Util;
import com.icss.mvp.util.StringUtilsLocal;

/*
 * @author liuwenyan
 * 项目试题信息配置service
 */
@Service
public class ProjectConfigurationService {
	@Autowired
	private IProjectListDao projectListDao;
	@Autowired
	private IProjectSourceConfigDao projectDSConfDao;
//	@Autowired
//	private ICodeMasterDao codeMasterDao;
	@Autowired
	private IProjectSourceConfigDao sourceDao;
	@Autowired
	private ISvnTaskDao svnTaskDao;

	private final static Logger LOG = Logger.getLogger(ProjectListService.class);

	private final int SUCCESS_CODE = 1;

	private final int FAIL_CODE = 0;

	private final int ALREADY_EXIST = 2;

	private final String DATASOURCE_KEY = "Source";

	public Map<String, Object> queryProjInfo(String projNo) {
		LOG.info("queryProjInfo begin.");
		Map<String, Object> result = new HashMap<String, Object>();
		List<ProjectDetailInfo> projList = projectListDao.queryProjInfo(projNo);
		if (null == projList || projList.isEmpty()) {
			LOG.warn("This Project does not exist.");
			result.put("resultCode", FAIL_CODE);
			result.put("projInfo", null);
			return result;
		}
		LOG.info("queryProjInfo success.");
		result.put("resultCode", SUCCESS_CODE);
		result.put("projInfo", projList.get(0));
		return result;
	}

	public Map<String, Object> queryProjDataSrcInfo(String projNo, Page page) {
		LOG.info("queryProjDataSrcInfo begin.");
		Map<String, Object> result = new HashMap<String, Object>();
		List<ProjectDataSourceInfo> projDSList = projectDSConfDao.queryProjDSInfo(projNo, page.getSort(),
				page.getOrder());
		result.put("rows", projDSList);
		result.put("total", projDSList.size());
		LOG.info("queryProjDataSrcInfo success.");
		return result;
	}

	public Map<String, Object> insertDataSourceForProj(ProjectDataSourceInfo projDSInfo) {
		LOG.info("insertDataSourceForProj begin.");
		Map<String, Object> result = new HashMap<String, Object>();
		ProjectDataSourceInfo projDS = projectDSConfDao.queryProjDSByKey(projDSInfo.getNo(),
				projDSInfo.getSource_value());
		if (null != projDS) {
			result.put("resultCode", ALREADY_EXIST);
			LOG.info("insertDataSourceForProj  proj and datasource already exist.");
			return result;
		}
		if (null != projDSInfo.getCreateDate()) {
			Date date = convertDate(projDSInfo.getCreateDate());
			projDSInfo.setCreateDate(date);
		}
		String passWord = StringMD5Util.MD5String(projDSInfo.getPassword());
		projDSInfo.setPassword(passWord);
		int insertRslt = projectDSConfDao.insertProjDS(projDSInfo);
		result.put("resultCode", insertRslt);
		LOG.info("insertDataSourceForProj success.");
		return result;
	}

	public Map<String, Object> updateDataSourceForProj(ProjectDataSourceInfo projDSInfo) {
		LOG.info("updateDataSourceForProj begin.");
		Map<String, Object> result = new HashMap<String, Object>();
		// step1:check NO+source_value是否存在;step2:未存在，则插入；若已存在，则更新
		ProjectDataSourceInfo projDS = projectDSConfDao.queryProjDSByKey(projDSInfo.getNo(),
				projDSInfo.getSource_value());
		if (null != projDSInfo.getUpdateDate()) {
			Date date = convertDate(projDSInfo.getUpdateDate());
			projDSInfo.setUpdateDate(date);
		}
		int rslt = FAIL_CODE;
		if (null == projDS) {
			result.put("resultCode", rslt);
			LOG.info("updateDataSourceForProj fail: proj and datasource does not exist.");
			return result;
		}
		String passWord = StringMD5Util.MD5String(projDSInfo.getPassword());
		projDSInfo.setPassword(passWord);
		rslt = projectDSConfDao.updateProjDS(projDSInfo);
		result.put("resultCode", rslt);
		LOG.info("updateDataSourceForProj success.");
		return result;
	}

	private Date convertDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dd = format.format(date);
		Date ddd;
		try {
			ddd = format.parse(dd);

		} catch (ParseException e) {
			return null;
		}
		return ddd;
	}

	public Map<String, Object> delDataSourceForProj(ProjectDataSourceInfo projDSInfo) {
		LOG.info("delDataSourceForProj begin.");
		Map<String, Object> result = new HashMap<String, Object>();
		int rslt = projectDSConfDao.delProjDS(projDSInfo);
		result.put("resultCode", rslt);
		LOG.info("delDataSourceForProj success.");
		return result;
	}

//	public Map<String, Object> queryProjCMForDS() {
//		LOG.info("queryProjCMForDS begin.");
//		Map<String, Object> result = new HashMap<String, Object>();
//		List<CodeMasterInfo> cmforDSList = codeMasterDao.getCodeNameListbyKey(DATASOURCE_KEY);
//		result.put("rows", cmforDSList);
//		result.put("total", cmforDSList.size());
//		LOG.info("queryProjCMForDS success.");
//		return result;
//	}

	public ProjectDataSourceInfo searchProjDataSrcInfo(String no) {

		ProjectDataSourceInfo prodsinfo = sourceDao.queryDSByNo(no);
		StringBuffer sb = new StringBuffer();
		List<String> list = new ArrayList<String>();
		if (prodsinfo != null) {
			String svnurls = prodsinfo.getUrl();
			if (StringUtils.isNotBlank(svnurls)) {
				try {
					String[] ids = svnurls.split(",");
					for (String id : ids) {
						Integer Id = Integer.parseInt(id);
						String url = sourceDao.searchUrlById(Id);
						list.add(url);
					}
					for (int i = 0; i < list.size(); i++) {
						sb.append(list.get(i)).append(",");
					}
					if (sb.length() > 0) {
						String urls = sb.substring(0, sb.length() - 1);
						prodsinfo.setUrl(urls);
					}
				} catch (Exception e) {
					prodsinfo.setUrl(svnurls);
				}
			}
		}
		return prodsinfo;
	}

	@SuppressWarnings("unchecked")
	public List<RepositoryInfo> searchConfigInfo(String no) {
		List<RepositoryInfo> infos = sourceDao.searchRepositoryByNo(no);
		for (RepositoryInfo repositoryInfo : infos) {
			// svn scope为excludeRevision和excludePath拼接
			if (repositoryInfo.getType() == 2 || repositoryInfo.getType() == 3 || repositoryInfo.getType() == 5) {
				if (StringUtilsLocal.isBlank(repositoryInfo.getScope())) {
					continue;
				}
				Map<String, Object> map = JSON.parseObject(repositoryInfo.getScope(),Map.class);
				Object mapObj = map.get("excludeRevision");
				List<String> excludeRevisions = new ArrayList<>();
				if (mapObj instanceof List) {
					excludeRevisions = (List<String>) map.get("excludeRevision");
				}
				mapObj = map.get("excludePath");
				List<String> excludePaths = new ArrayList<>();
				if (mapObj instanceof List) {
					excludePaths = (List<String>) map.get("excludePath");
				}
//				StringBuilder excludeRevision = new StringBuilder();
//				StringBuilder excludePath = new StringBuilder();
//				for (String value : excludeRevisions) {
//					excludeRevision.append(value).append(";");
//				}
//				for (String value : excludePaths) {
//					excludePath.append(value).append(";");
//				}
//				repositoryInfo.setExcludeRevision(StringUtilsLocal.deleteCharAtLastOne(excludeRevision));
//				repositoryInfo.setExcludePath(StringUtilsLocal.deleteCharAtLastOne(excludePath));

                repositoryInfo.setExcludeRevision(StringUtils.join(excludeRevisions,";"));
                repositoryInfo.setExcludePath(StringUtils.join(excludePaths, ";"));
			}
		}
		return infos;
	}

	public List<RepositoryInfo> searchConfigInfoOtherAcc(String no) {
		return sourceDao.searchRepositoryByNoHasOtherAcc(no);
	}

	public Map<String, Object> saveConfigJob(String id,String no, String ids, String selectedIds, String types, String name) {
		Map<String, Object> ret = new HashMap<>();
		if(!StringUtilsLocal.isBlank(types)&&types.indexOf(";")>0) {
			String[] typeStr = types.split(";");
			Set<String> typeSet = new HashSet<>();
			for (String type:typeStr) {
				typeSet.add(type);
			}
			types="";
			for (String type : typeSet) {
				types+=type+";";
			}
			types = types.substring(0, types.length()-1);
		}
		
		if(StringUtilsLocal.isBlank(id)) {
			CollectionJobInfo collectionJobInfo = new CollectionJobInfo();
			collectionJobInfo.setIds(ids);
			collectionJobInfo.setSelectedIds(selectedIds);
			collectionJobInfo.setNo(no);
			collectionJobInfo.setName(name);
			collectionJobInfo.setTypes(types);
			collectionJobInfo.setProgressLast("0");
			collectionJobInfo.setNum("0");
			sourceDao.insertCollectionJob(collectionJobInfo);
		}else {
			CollectionJobInfo collectionJobInfo = new CollectionJobInfo();
			collectionJobInfo.setId(Integer.parseInt(id));
			collectionJobInfo.setIds(ids);
			collectionJobInfo.setSelectedIds(selectedIds);
			collectionJobInfo.setNo(no);
			collectionJobInfo.setName(name);
			collectionJobInfo.setTypes(types);
			collectionJobInfo.setProgressLast("0");
			sourceDao.updateCollectionJob(collectionJobInfo);
		}
		
//		Date date = new Date();
//		ProjectConfigInfo projectConfigInfo = new ProjectConfigInfo();
//		projectConfigInfo.setCreateTime(date);
//		projectConfigInfo.setModifyTime(date);
//		projectConfigInfo.setProjectId(no);
//		projectConfigInfo.setIsDeleted(0);
//		projectConfigInfo.setRepositoryId(collectionJobInfo.getId());
//		sourceDao.insertProjectConfig(projectConfigInfo);
		
		return ret;
	}
	
	public List<CollectionJobInfo> searchConfigJob(String projectId) {
		if(org.apache.commons.lang.StringUtils.isBlank(projectId)){
			LOG.error("parameter illegal");
			return new ArrayList<>();
		}
		List<CollectionJobInfo> collectionJobInfos= new ArrayList<>();
		try{
			collectionJobInfos = sourceDao.searchCollectionJob(projectId);
			if(collectionJobInfos.size() == 0 || collectionJobInfos == null){
				collectionJobInfos = searchConfigJobByTeam(projectId);
			}
		}catch(Exception e){
			LOG.error("sourceDao.searchCollectionJob exception, error: "+e.getMessage());
		}
		/*for (CollectionJobInfo collectionJobInfo : collectionJobInfos) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", collectionJobInfo.getId());
			map.put("ids", collectionJobInfo.getIds());
			map.put("selectedIds", collectionJobInfo.getSelectedIds());
			map.put("name", collectionJobInfo.getName());
			map.put("progressLast", collectionJobInfo.getProgressLast());
			map.put("types", collectionJobInfo.getTypes());
			String ids = collectionJobInfo.getSelectedIds();
			Date date= collectionJobInfo.getModifyTime();
			if(date==null) {
				date = svnTaskDao.searchStartDate(collectionJobInfo.getNo());
			}
			map.put("date", date);
			retList.add(map);
		}*/
		return collectionJobInfos;
	}
	
	public String getProjNameByNo(String projectId) {
		String projName = "";
		projName = sourceDao.getProjNameByNo(projectId);
		return projName;
	}
	
	public List<CollectionJobInfo> searchConfigJobByTeam(String projectId) {
		if(org.apache.commons.lang.StringUtils.isBlank(projectId)){
			LOG.error("parameter illegal");
			return new ArrayList<>();
		}
		List<CollectionJobInfo> collectionJobInfos = new ArrayList<>(); 
		try {
			collectionJobInfos = sourceDao.searchCollectionJobByTeam(projectId);
			for(CollectionJobInfo collectionJobInfo: collectionJobInfos){
				String[] originIds = collectionJobInfo.getIds().split(";");
				List<String> list = new ArrayList<>();
				for(int i = 0; i<originIds.length; i++){
					String id = originIds[i];
					List<RepositoryInfo> repositoryInfo = sourceDao.getRepository(id);
					for(RepositoryInfo repository : repositoryInfo){
						sourceDao.saveRepository(repository);
						String repositoryId = repository.getId().toString();
						list.add(repositoryId);
						sourceDao.saveProjectConfig(projectId,repositoryId);
					}
				}			
				String ids = org.apache.commons.lang.StringUtils.join(list, ';');
				collectionJobInfo.setNo(projectId);
				collectionJobInfo.setIds(ids);
				collectionJobInfo.setSelectedIds(ids);
				collectionJobInfo.setProgressLast("0");
				collectionJobInfo.setNum("0");
				sourceDao.insertCollectionJob(collectionJobInfo);
			}
		} catch (Exception e) {
			LOG.error("sourceDao.insertCollectionJob exception, error: "+e.getMessage());
		}
		return collectionJobInfos;
	}

	public List<RepositoryInfo> searchConfigInfoByJob(String ids) {
		if(StringUtilsLocal.isBlank(ids)) {
			LOG.error("没有需要保存的数据");
			return null;
		}
		String[] id = ids.split(";");
		List<String> idlist = Arrays.asList(id);
		List<RepositoryInfo> infos = sourceDao.searchRepositoryByIds("("+ StringUtilsLocal.listToSqlIn(idlist)+")");
		for (RepositoryInfo repositoryInfo : infos) {
			// svn scope为excludeRevision和excludePath拼接
			if (repositoryInfo.getType() == 2 || repositoryInfo.getType() == 3 || repositoryInfo.getType() == 5 || repositoryInfo.getType() == 7) {
				if (StringUtilsLocal.isBlank(repositoryInfo.getScope())) {
					continue;
				}
				Map<String, Object> map = JSON.parseObject(repositoryInfo.getScope(), Map.class);
				Object mapObj = map.get("excludeRevision");
				List<String> excludeRevisions = new ArrayList<>();
				if (mapObj instanceof List) {
					excludeRevisions = (List<String>) map.get("excludeRevision");
				}
				mapObj = map.get("excludePath");
				List<String> excludePaths = new ArrayList<>();
				if (mapObj instanceof List) {
					excludePaths = (List<String>) map.get("excludePath");
				}
				mapObj = map.get("baseLineVersion");
				String baseLineVersion = null;
				if (mapObj instanceof String) {
					baseLineVersion = (String) map.get("baseLineVersion");
				}
                repositoryInfo .setBaseLineVersion(baseLineVersion);

//				StringBuilder excludeRevision = new StringBuilder();
//				StringBuilder excludePath = new StringBuilder();
//				for (String value : excludeRevisions) {
//					excludeRevision.append(value).append(";");
//				}
//				for (String value : excludePaths) {
//					excludePath.append(value).append(";");
//				}

//				repositoryInfo.setExcludeRevision(StringUtilsLocal.deleteCharAtLastOne(excludeRevision));
//				repositoryInfo.setExcludePath(StringUtilsLocal.deleteCharAtLastOne(excludePath));

                repositoryInfo.setExcludeRevision(StringUtils.join(excludeRevisions,";"));
                repositoryInfo.setExcludePath(StringUtils.join(excludePaths, ";"));
			} 
		}
		return infos;
	}

	public Map<String, Object> copyJob(String id, String ids) {
		String[] ids1 = ids.split(";");
		List<String> idlist = Arrays.asList(ids1);
		List<RepositoryInfo> repositoryInfos = sourceDao.searchRepositoryByIds("("+ StringUtilsLocal.listToSqlIn(idlist)+")");
		List<ProjectConfigInfo> projectConfigInfos = sourceDao.searchProjectConfigByIds("("+ StringUtilsLocal.listToSqlIn(
                idlist)+")");
		List<CollectionJobInfo> collectionJobInfos = sourceDao.searchCollectionJobById(id);
		String newids = "";
		for (int i= 0;i<repositoryInfos.size();i++) {
			sourceDao.insertRepository(repositoryInfos.get(i));
			projectConfigInfos.get(i).setRepositoryId(repositoryInfos.get(i).getId());
			sourceDao.insertProjectConfig(projectConfigInfos.get(i));
			newids+=repositoryInfos.get(i).getId()+";";
		}
		if(newids.indexOf(";")>0) {
			newids = newids.substring(0, newids.length()-1);
		}
		for (CollectionJobInfo collectionJobInfo : collectionJobInfos) {
			collectionJobInfo.setIds(newids);
			collectionJobInfo.setName(collectionJobInfo.getName()+" - 副本");
			collectionJobInfo.setNum("0");
			collectionJobInfo.setProgressLast("0");
			sourceDao.insertCollectionJob(collectionJobInfo);
		}
		return null;
	}

	public Map<String, Object> delJob(String id, String ids, String no) {
		if(!StringUtilsLocal.isBlank(ids)){
			String[] ids1 = ids.split(";");
			List<String> idlist = Arrays.asList(ids1);
			sourceDao.delRepository(StringUtilsLocal.listToSqlIn(idlist));
			sourceDao.delProjectConfigByReId(StringUtilsLocal.listToSqlIn(idlist), no);
			sourceDao.delSvnLogTimeById(StringUtilsLocal.listToSqlIn(idlist));
		}
		sourceDao.delCollectionJob(id);
		return null;
	}

	public List<Map<String, Object>> searchConfigJobById(String id) {
		List<Map<String, Object>> retList = new ArrayList<>();
		List<CollectionJobInfo> collectionJobInfos = sourceDao.searchCollectionJobById(id);
		for (CollectionJobInfo collectionJobInfo : collectionJobInfos) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", collectionJobInfo.getId());
			map.put("ids", collectionJobInfo.getIds());
			map.put("selectedIds", collectionJobInfo.getSelectedIds());
			map.put("name", collectionJobInfo.getName());
			map.put("progressLast", collectionJobInfo.getProgressLast());
			map.put("types", collectionJobInfo.getTypes().replace(";", "/"));
			map.put("num", collectionJobInfo.getNum());
//			String ids = collectionJobInfo.getSelectedIds();
			Date date= collectionJobInfo.getModifyTime();
			if(date==null) {
				date = svnTaskDao.searchStartDate(collectionJobInfo.getNo());
			}
			map.put("date", date);
			retList.add(map);
		}
		return retList;
	}

	public void searchConfigReNum(String id, String num,String progressLast) {
		sourceDao.searchConfigReNum(id,num,progressLast);
	}

}
