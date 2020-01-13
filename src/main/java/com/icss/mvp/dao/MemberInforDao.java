package com.icss.mvp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectInformation;
import com.icss.mvp.entity.ProjectMembersInformation;
import com.icss.mvp.entity.ProjectPostInfor;

@SuppressWarnings("all")
public interface MemberInforDao {

	public ProjectInformation projectInformation(@Param("projectId") String projectId);

	public List<ProjectPostInfor> memberinformation(@Param("projectId") String projectId);

	public void saveMembersInformation(@Param("project") ProjectMembersInformation project);

	public ProjectMembersInformation editDataPosts(@Param("id") String id);
	
	List<ProjectMembersInformation> queryMembersInformation(@Param("proNo") String projectId);

	public void saveProjectInformation(@Param("project") ProjectMembersInformation project);
	
	/**
	 * 根据proNo,zrAccount查询是否已存在该中软工号
	 * @return
	 */
	int getZRAccountCount(@Param("proNo")String proNo, @Param("zrAccount")String zrAccount);
	
	/**
	 * 导入岗位信息
	 * @return
	 */
	int addPostInformation(@Param("proNo")String proNo, @Param("projectPostInfo")ProjectPostInfor projectPostInfor);
	
	/**
	 * 导入成员
	 * @return
	 */
	int addMemberInfo(@Param("projectMembersInfo")ProjectMembersInformation projectMembersInfo);
	
	/**
	 * 查询所有成员信息
	 * @param proNo
	 * @return
	 */
	List<ProjectMembersInformation> queryProjectMembersInfo(@Param("proNo") String proNo);
	
	/**
	 * 查询所有预算岗位
	 * @param proNo
	 * @return
	 */
	List<ProjectPostInfor> queryProjectPostInfo(@Param("proNo") String proNo);
	
	List<ProjectMembersInformation> queryActualNumberOfPost(String proNo);

	List<ProjectPostInfor> getPostCount(@Param("proNo")String proNo, @Param("projectPostInfo")ProjectPostInfor projectPostInfor);

	public void updatePostInformation(@Param("proNo")String proNo, @Param("projectPostInfo")ProjectPostInfor projectPostInfor);

}
