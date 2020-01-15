package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectCost;
import com.icss.mvp.entity.ProjectKeyroles;
import com.icss.mvp.entity.ProjectMembersLocal;
import com.icss.mvp.entity.ProjectPost;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.TeamMembers;
import com.icss.mvp.entity.common.request.PageRequest;

/**
 * 
 * @author 关键角色
 *
 */
@SuppressWarnings("all")
public interface GeneralSituationDao {
	/**
	 * 分页查询关键角色
	 * 
	 * @return
	 */
	List<ProjectKeyroles> queryProjectKeyroles(@Param("page") TableSplitResult page, @Param("pmid") String pmid,
			@Param("sort") String sort, @Param("sortOrder") String sortOrder);
	/**
	 * 项目中分页查询关键角色
	 * @return
	 */
	List<ProjectKeyroles> getProjectKeyroles(@Param("page") TableSplitResult page,@Param("sort") String sort, @Param("sortOrder") String sortOrder);

	// List<ProjectKeyroles> queryIteWorkManageInfo1(@Param("proNo") String
	// proNo);

	// List<SysDictItem> querySysDictItemBycode(@Param("code") String code);

	/**
	 * 导入历史项目的关键角色
	 * 
	 * @param newNo
	 * @param oldNo
	 * @return
	 */
	int addOldProjectKeyroles(@Param("newNo") String newNo, @Param("oldNo") String oldNo);

	// int updateProjectKeyroles(@Param("proj") ProjectKeyroles projectKey);

	/**
	 * 新增关键角色
	 * 
	 * @param iteInfo
	 * @return
	 */
	// int addImportantRole(@Param("proj")ProjectKeyroles projectKeyroles);

	// int selectRepeat(@Param("proj")ProjectKeyroles projectKeyroles);

	Integer queryTotalCount(@Param("pmid") String pmid, @Param("page") TableSplitResult page);

	/**
	 * 项目中分页查询关键角色总数
	 * @return
	 */
	Integer getTotalCount(@Param("page") TableSplitResult page);

	// int deleteProjectKeyroles(@Param("no") String no,@Param("zrAccount")
	// String zrAccount);

	ProjectKeyroles openEditPage(@Param("no") String no, @Param("zrAccount") String zrAccount);

	/**
	 * 分页查询团队成员
	 * 
	 * @param page
	 * @param proNo
	 * @param sort
	 * @param sortOrder
	 * @return
	 */
	List<TeamMembers> queryTeamMembers(@Param("page") TableSplitResult page, @Param("teamId") String teamId,
			@Param("sort") String sort, @Param("sortOrder") String sortOrder);

	Integer queryMembersCount(@Param("teamId") String teamId);

	List<Map<String, Object>> organizationalStructure(@Param("proNo") String projNo);

	void projectPostHierarchyInsert(@Param("data") Map<String, Object> data);

	List<ProjectPost> projectPostHierarchySelect(@Param("pmid") String pmid);

	List<ProjectPost> organizationalHierarchical(@Param("proNo") String projNo);

	void demandUpdata(@Param("id") String Id, @Param("value") String value);

	List<Map<String, Object>> sysDictItemSelect(@Param("id") int id, @Param("position") String position);

	void delDemandPopulation(@Param("id") String ID);

	List<String> getSelectNames(@Param("pmid") String pmid);

	List<Map<String, Object>> getSelectPost(@Param("pmid") String pmid, @Param("type") String type);

	// List<Map<String, Object>> getSelectDict(@Param("projNo") String projNo);

	Map<String, Object> queryProjectDate(@Param("projNo") String projNo);

	int addKeyRoleToMemberBase(@Param("proj") ProjectKeyroles projectKeyroles);

	int addKeyRoleToProjectStaff(@Param("proj") ProjectKeyroles projectKeyroles);

	void deleteProjectStaff(@Param("proNo") String no, @Param("zrAccount") String zrAccount);

	ProjectKeyroles queryMemberEchoDisplay(@Param("no")String no,@Param("pmid") String pmid, @Param("zrAccount") String zrAccount);

	ProjectKeyroles getProjectKeyrole(@Param("no")String no, @Param("zrAccount") String zrAccount);

	int updateKeyRoleToMemberBase(@Param("proj") ProjectKeyroles projectKeyroles);

	int updateKeyRoleToProjectStaff(@Param("proj") ProjectKeyroles projectKeyroles);

	int selectRepeatMemberBase(@Param("proj") ProjectKeyroles projectKeyroles);

	int selectRepeatProjectStaff(@Param("proj") ProjectKeyroles projectKeyroles);

	void updateProjectKeyRoleCount(@Param("pmid") String pmid, @Param("count") String count);

	List<ProjectCost> getProjectCost(@Param("projNo") String projNo, @Param("statisticalTime") String statisticalTime,
			@Param("nextTime") String nextTime, @Param("flag") String flag);

	List<ProjectCost> getMemberList(@Param("projNo") String projNo, @Param("page") PageRequest pageRequest,
			@Param("statisticalTime") String statisticalTime, @Param("nextTime") String nextTime, 
			@Param("flag") String flag);

	int getMemberSize(@Param("projNo") String projNo, @Param("statisticalTime") String statisticalTime,
			@Param("nextTime") String nextTime, @Param("flag") String flag);

	void updateMemberCost(@Param("projCost") ProjectCost projectCost);

	void updateProjectBudget(@Param("projectBudget") String projectBudget, @Param("projNo") String projNo);

	List<ProjectCost> getMemberByDay(@Param("day") String day, @Param("projNo") String projNo, @Param("flag") String flag);

	void updateHour(@Param("memberHour") List<ProjectCost> list);

	int getPayroll(@Param("zrAccount") String zrAccount, @Param("projNo") String projNo, @Param("week") String week);

	List<ProjectMembersLocal> getMemberAccount(@Param("projNo") String projNo);

	int getHWAccount(@Param("username") String username);

	int getzrAccount(@Param("zrAccount") String zrAccount);
	int addZRAccount(@Param("zrAccount") String zrAccount, @Param("hwAccount") String hwAccount);

	String getPMZRAccountByNo(@Param("projNo") String projNo);

	String getPMZRAccountByHW(@Param("hwAccount") String hwAccount);

	Map<String, Object> selfRank(@Param("zrAccount") String zrAccount);
	
	Map<String, Object> importRank(@Param("zrAccount") String zrAccount);

	Map<String, Object> memberInfo(@Param("id") String id, @Param("zrAccount") String zrAccount, @Param("mark") String mark);

	Map<String, Object> newmemberInfo(@Param("no") String no ,@Param("id") String id, @Param("zrAccount") String zrAccount, @Param("mark") String mark);
	
	Map<String, Object> memberBasic(@Param("zrAccount") String zrAccount);

	void alterRankOfProjectStaff(@Param("status") String status, @Param("rank") String rank,
			@Param("zrAccount") String zrAccount, @Param("mark") String mark);

	void alterRankOfTeamStaff(@Param("status") String status, @Param("rank") String rank,
			@Param("zrAccount") String zrAccount, @Param("mark") String mark);
	   String getvalueBykey(@Param("key") String key);
	String queryzrbyhw(@Param("hwAccount") String hwAccount);

}
