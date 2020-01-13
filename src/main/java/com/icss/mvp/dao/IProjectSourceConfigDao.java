package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.CollectionJobInfo;
import com.icss.mvp.entity.ProjectConfigInfo;
import com.icss.mvp.entity.ProjectDataSourceInfo;
import com.icss.mvp.entity.RepositoryInfo;

public interface IProjectSourceConfigDao {
	public List<ProjectDataSourceInfo> queryProjDSInfo(@Param("projNo") String projNo, @Param("sort") String sort,
			@Param("order") String order);

	public int insertProjDS(ProjectDataSourceInfo projDSInfo);

	public int updateProjDS(ProjectDataSourceInfo projDSInfo);

	public int delProjDS(ProjectDataSourceInfo projDSInfo);

	public ProjectDataSourceInfo queryProjDSByKey(@Param("projNo") String projNo,
			@Param("source_value") String sourceValue);

	public List<ProjectDataSourceInfo> queryAllDS();

	public List<ProjectDataSourceInfo> queryDSBySource(String source);

	public ProjectDataSourceInfo queryDSByNo(@Param("no") String no);

	public void inserturl(@Param("projDSInfo") ProjectDataSourceInfo projDSInfo);

	public void updateurl(@Param("projDSInfo") ProjectDataSourceInfo projDSInfo);

	public String searchUrlById(@Param("Id") Integer Id);

	public Integer insertRepository(@Param("repInfo") RepositoryInfo repositoryInfo);

	public int replaceRepository(@Param("repInfo") RepositoryInfo repositoryInfo);
	
	public int insertToRepository(@Param("repInfo") RepositoryInfo repositoryInfo);

	public List<RepositoryInfo> searchRepositoryByNo(@Param("projNo") String projNo);
	
	public List<ProjectConfigInfo> searchProjectConfigByIds(@Param("ids") String ids);

	public List<RepositoryInfo> searchRepositoryByNos(@Param("projNo") String projNo, @Param("type") String type,
			@Param("id") String id);

	public int insertProjectConfig(@Param("projConfInfo") ProjectConfigInfo projectConfigInfo);

	public int delProjectConfig(@Param("ids") String ids);
	public int delProjectConfigByReId(@Param("ids") String ids, @Param("projNo") String no);

	public int delRepository(@Param("ids") String ids);
	
	public int delCollectionJob(@Param("ids") String ids);
	
	public int delSvnLogTimeById(@Param("ids") String ids);

	public List<RepositoryInfo> searchRepositoryByNoHasOtherAcc(@Param("projNo") String projNo);

	public void delSvnLogTime(@Param("projNo") String projNo, @Param("ids") String del);

	public void insertCollectionJob(@Param("repInfo") CollectionJobInfo collectionJobInfo);
	public void replaceCollectionJob(@Param("repInfo") CollectionJobInfo collectionJobInfo);
	
	public List<CollectionJobInfo> searchCollectionJob(@Param("projectId")String projectId);
	
	public String getProjNameByNo(@Param("projectId")String projectId);
	
	public List<CollectionJobInfo> searchCollectionJobByTeam(@Param("projectId")String projectId);
	
	public List<RepositoryInfo> getRepository(@Param("id")String id);
	public void saveRepository(@Param("repository") RepositoryInfo repository);
	public void saveCollectionJob(@Param("collectionJobInfo") CollectionJobInfo collectionJobInfo);
	public void saveProjectConfig(@Param("projectId") String projectId, @Param("repositoryId") String repositoryId);
	public List<Integer> getRepositoryId(String no);
	
	public List<RepositoryInfo> searchRepositoryByIds(@Param("ids") String ids);
	public List<CollectionJobInfo> searchCollectionJobById(@Param("id") String id);

	public void updateCollectionJob(@Param("repInfo") CollectionJobInfo collectionJobInfo);

	public void searchConfigReNum(@Param("id") String id, @Param("num") String num,@Param("progressLast") String progressLast);

	public List<Integer> getRepositoryIdByServiceId(String serviceId);
}
