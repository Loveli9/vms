package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.CodeMasterInfo;
import com.icss.mvp.entity.OrganizeMgmer;
import com.icss.mvp.entity.ProjectClock;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectManager;
import com.icss.mvp.entity.ProjectMember;
import com.icss.mvp.entity.ProjectTeam;
import com.icss.mvp.entity.TblTimeInformation;

public interface IProjectListDao
{
	public List<ProjectInfo> getList(@Param("proj")ProjectInfo proj, @Param("sort") String sort, @Param("order")String order);
	public List<ProjectDetailInfo> queryProjInfo(@Param("projNo")String projNo);
	public ProjectInfo getProjInfo(@Param("buName") String name, @Param("no") String no);
	public ProjectInfo getProjInfoByNo(@Param("no") String no);
	
	public ProjectInfo getProjInfoByNoQ(@Param("proNo") String proNo);
	public List<OrganizeMgmer> getBusinessUnit();
	public List<OrganizeMgmer> getDeliveryUnit();
	public List<CodeMasterInfo> getCountArea();
	public List<CodeMasterInfo> getProjectType();
	public List<ProjectManager> getProjectManager();
	
	public int insertInfo(@Param("proj") ProjectDetailInfo proj);
	public  List<ProjectDetailInfo> isExits(String pmid);
	public  ProjectDetailInfo isExit(String no);
	public int updateInfo(ProjectDetailInfo proj);

	public int insertMember(ProjectManager projManger);
	public ProjectManager isExitMember(String no);
	public int updateMemberInfo(ProjectManager projManger);
	
	public int insertProjInfos(List<ProjectDetailInfo> projInfo);
	public int insertProjMembers(List<ProjectManager> projMgs);
	
	public List<ProjectInfo> isExistVersion(@Param("version")String version);
	
	public int insertmemberInfo(@Param("projm") ProjectMember projm);
	
	public ProjectMember queryMember(String author);
	
	public String searchNameByAuthor(@Param("author")String author,@Param("po")String po);
	
	public int inserClockInfo(@Param("projc") ProjectClock projc);
	public int replaceInfo(@Param("proj") ProjectDetailInfo projInfo);
	
	public int replaceTeam(ProjectTeam team);
	
	public List<String> getTeamNames();
	
	public int replaceMember(ProjectManager projMember);
	public List<ProjectMember> searchmembersByPo(@Param("po") String po);
	public String queryRoleByAuthor(@Param("author")String author,@Param("po")String po);
	public List<Map<String, Object>> queryMembersByName(@Param("name")String name);
	public void inserTimeInformation(@Param("tblti") TblTimeInformation tblTimeInformation);
	public List<ProjectMember> queryMembersByIdCard(@Param("idCard")String idCard);
	public void delTimeInformation(@Param("author") String author,@Param("date") String date);
	public void update(ProjectInfo proj);

    /**
     * 项目状态关闭
     * @param nowDateString
     */
	public void closeProjects(@Param("importTime") String nowDateString);

	/**
	 * 更新项目导入时间
	 * @param prjId
	 */
	void updateInportDate(@Param("prjId") String prjId);
	
	public String isExistProject(@Param("prjId") String prjId);
	
	public String queryProjectNumber(@Param("prjName") String prjName);

    List<ProjectInfo> projectInfos(@Param("proNos") Set<String> proNos);
}
