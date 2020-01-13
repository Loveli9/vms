package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import com.icss.mvp.entity.TableSplitResult;
import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectMembersLocal;
import com.icss.mvp.entity.TeamMembers;
import com.icss.mvp.entity.common.response.ListResponse;

public interface IProjectManagersDao {

	List<Map<String, Object>> queryOMPUser(String po);

	List<ProjectMembersLocal> queryOMPUserSelecteds(@Param("page") TableSplitResult page, @Param("pmid") String pmid);
	List<ProjectMembersLocal> getProjectMembersByNo(@Param("page") TableSplitResult page, @Param("proNo") String proNo);
	List<Map<String,Object>> queryOMPUserSelected(@Param("pmid") String pmid);

	// 继承团队下所属项目的配置成员
	// List<Map<String, Object>> queryMemberByProject(@Param("proNo") String
	// proNo, @Param("oldNo") String oldNo);

	// 继承团队的配置成员
	// List<Map<String, Object>> queryMemberByTeam(@Param("proNo") String
	// proNo);

	ProjectInfo queryProjectDate(@Param("projNo") String projNo);

	// 导入历史项目的团队成员
	int addOldProjectTeamMembers(@Param("newNo") String newNo, @Param("oldNo") String oldNo);

	int insertSelectedUser(@Param("sel") List<ProjectMembersLocal> membersLocals);

	int deleteSelectedUser(@Param("no") String no, @Param("hwAccount") String hwAccount);

	// int deleteSelectedUserByNo(String no);

	List<Map<String, Object>> queryOMPUserByAuthor(@Param("po") String po, @Param("author") String author);

	List<Map<String, Object>> queryOMPUserSelectedTest(String no);

	List<Map<String, Object>> queryOMPUserSelectedDevelop(Map<String, Object> map);

	/**
	 * 根据项目编号统计每月代码提交次数
	 * 
	 * @param projectId
	 *            项目编号
	 * @return
	 */
	List<Map<String, Object>> summarizeCommitMonthly(String projectId);

	Map<String, Object> queryOMPUserSelectedCount(@Param("no") String proNo, @Param("date") String date);

	List<Map<String, Object>> queryProjectMembers(@Param("projNo") String projNo);

	List<Map<String, Object>> getProjectTeamMember(@Param("projNo") String projNo);

	ListResponse<ProjectMembersLocal> getTeamMembers(@Param("projectId") String projectId);

	String getTeamId(@Param("proNo") String proNo);

	String getProjectNo(@Param("projNo") String projNo);

	ProjectMembersLocal openEditPage(@Param("projNo") String projNo, @Param("hwAccount") String hwAccount);

	Integer selectRepeat(@Param("proj") ProjectMembersLocal projectMembersLocal);

	/**
	 * 新增项目成员
	 * 
	 * @param projectMembersLocal
	 * @return
	 */
	int addProjectMember(@Param("proj") ProjectMembersLocal projectMembersLocal);

	/**
	 * 修改项目成员
	 * 
	 * @param projectMembersLocal
	 * @return
	 */
	int editProjectMember(@Param("proj") ProjectMembersLocal projectMembersLocal);

	/**
	 * 删除项目成员
	 * 
	 * @param projNo
	 * @param hwAccount
	 * @return
	 */
	int deleteProjectMems(@Param("projNo") String projNo, @Param("hwAccount") String hwAccount);

	Integer selectRepeatTeamMem(@Param("team") TeamMembers teamMembers);

	int addTeamMember(@Param("team") TeamMembers teamMembers);

	// TeamMembers getTeamMemberInfo(@Param("teamId")String
	// teamId,@Param("zrAccount")String zrAccount);
	// int editTeamMemberInfo(@Param("team")TeamMembers teamMembers);
	int deleteTeamMembers(@Param("teamId") String teamId, @Param("zrAccount") String zrAccount);

	// void
	// updateProjectKeyroles(@Param("membersLocals")List<ProjectMembersLocal>
	// membersLocals);

	int addTeamMemberToMemberBase(@Param("team") TeamMembers teamMembers);

	int addTeamMemberToTeamStaff(@Param("team") TeamMembers teamMembers);

	int updateTeamMemberToMemberBase(@Param("team") TeamMembers teamMembers);

	int updateTeamMemberToTeamStaff(@Param("team") TeamMembers teamMembers);

	TeamMembers queryMemberEchoDisplay(@Param("teamId") String teamId, @Param("zrAccount") String zrAccount);

	int selectRepeatMemberBase(@Param("teamMem") TeamMembers teamMembers);

	int selectRepeatTeamStaff(@Param("teamMem") TeamMembers teamMembers);

	// 项目配置成员查重
	int selectMemberBase(@Param("zrAcc") String zrAcc);

	int selectProjectStaff(@Param("no") String  no, @Param("zrAcc") String zrAcc);

	// 更新配置成员
	int addProjectStaffToMemberBase(@Param("projectML") ProjectMembersLocal projectMembersLocal);

	int updateProjectStaffToMemberBase(@Param("projectML") ProjectMembersLocal projectMembersLocal);

	int addProjectStaffToProjectStaff(@Param("projectML") ProjectMembersLocal projectMembersLocal);
	int addProjectStaffToProjectStaffs(@Param("projectML") ProjectMembersLocal projectMembersLocal);
	int updateProjectStaffToProjectStaff(@Param("projectML") ProjectMembersLocal projectMembersLocal);
	int updateProjectStaffToProjectStaffs(@Param("projectML") ProjectMembersLocal projectMembersLocal);

	// 删除配置成员
	void deleteProjectStaff(@Param("pmid") String pmid);

	String getProjectPmId(@Param("projNo") String projNo);

	int getMemberRankCount(String chinasoftAccount);

	int addRankToArchive(@Param("teamMembers") TeamMembers teamMembers);

	int updateRankToArchive(@Param("teamMembers") TeamMembers teamMembers);

}
