package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import com.icss.mvp.entity.PersonnelInfo;
import com.icss.mvp.entity.ProjectMembersLocal;
import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.UserDetailInfo;
import com.icss.mvp.entity.UserInfo;

public interface IUserManagerDao {
	Integer queyMembersTotal(@Param("renyuan") PersonnelInfo renyuan);
	Integer queyMembersTotalByNo(@Param("proNo") String proNo);
	Integer queyMembersByPmid(@Param("renyuan") PersonnelInfo renyuan);
	Integer queyMembersByPmidofproject(@Param("renyuan") PersonnelInfo renyuan);
	Integer queyMembersByNo(@Param("proNo") String proNo);
	Integer queryGuanjianByPmid (@Param("renyuan") PersonnelInfo renyuan);
	Integer queryGuanjianByNo (@Param("proNo") String proNo);
	Integer queryGuanjianByPmidofproject (@Param("renyuan") PersonnelInfo renyuan);
	List<UserInfo> queryUserInfos(@Param("user") UserInfo userInfo, @Param("sort") String sort,
			@Param("order") String order);

	int deleteUser(UserInfo user);

	int updateUser(UserInfo user);
	int updateUsers(Map map);

	int addUserInfo(UserDetailInfo userDetailInfo);

	List<UserDetailInfo> isExistsByAccount(String userName);

	UserInfo isExistis(String userName);

	UserInfo getUserInfoByName(@Param("userName") String username);

	void editRole(@Param("userId") String userId, @Param("roleId") String roleId);

	Integer queryId(@Param("roleName") String roleName);

	Integer queryperId(@Param("permissionName") String permissionName);

	String querypNameById(@Param("permissionid") Integer permissionid);

	void updateper(@Param("userId") String userId, @Param("perids") String perids);

	List<UserInfo> queryIteInfoByPage(@Param("page") TableSplitResult page, @Param("proNo") String proNo,
			@Param("sort") String sort, @Param("sortOrder") String sortOrder);

	Integer queryIterationTotals(@Param("proNo") String proNo, @Param("page") TableSplitResult page);

	UserInfo selecById(@Param("id") String id);

	int updateNew(UserInfo userInfo);

	// 查询被继承项目的成员
	List<Map<String, Object>> queryOldProjectMembers(@Param("proNo") String proNo, @Param("oldNo") String oldNo);

	int queryRepeatProjectStaff(@Param("zrAccount") String zrAccount, @Param("no") String no);

	// 继承团队下所属项目配置成员&继承团队配置成员
	int addMemberInfo(@Param("member") Map<String, Object> member);

	int updateMemberInfo(@Param("member") Map<String, Object> member);

	String queryTeamIdByProNo(@Param("proNo") String proNo);

	// 查询被继承团队的成员
	List<Map<String, Object>> queryTeamMembers(@Param("proNo") String proNo, @Param("teamId") String teamId);

	// 查询被继承项目的角色需求
	List<Map<String, Object>> queryOldProjectPost(@Param("pmid") String pmid );

	// 继承团队下所属项目配置的角色需求
	void updateProjectPostDemand(@Param("map") Map<String, Object> map);

	List<String> queryProjectNos(@Param("pmid") String pmid);
	List<Map<String, Object>> getRelateProjects(@Param("pmid")String pmid);
	ProjectMembersLocal queryMemberinfoDisplay(@Param("no") String no,@Param("pmid") String pmid, @Param("zrAccount") String zrAccount);
	ProjectMembersLocal editProjectPages(@Param("no") String no,@Param("zrAccount") String zrAccount);
	void updatememberlocalToMemberBase(@Param("membersLocal") ProjectMembersLocal membersLocal);
	void updatmemberlocalToProjectStaff(@Param("membersLocal") ProjectMembersLocal membersLocal);
	void deleteProjectStaff(@Param("projNo") String projNo, @Param("zrAccount") String zrAccount);
	Map<String, Object> getMemberinfoByZr(@Param("zr") String zrAccount);
	Integer getMemberinfoByPm(@Param("pmid")String pmid);
	Integer getkeyRoleBypm(@Param("pmid")String pmid);
}
