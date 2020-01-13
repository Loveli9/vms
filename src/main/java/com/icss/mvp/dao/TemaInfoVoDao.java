package com.icss.mvp.dao;

import com.icss.mvp.entity.IterativeWorkManage;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectTeamVo;
import com.icss.mvp.entity.TeamInfoVo;
import com.icss.mvp.entity.TeamMembers;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TemaInfoVoDao {
    /**
     * 查询团队总数，用于分页查询的页码总数计算
     * @param map
     * @return
     */
    int teamInfoCount(Map<String, Object> map);
    int teamCount(@Param("teamName") String teamName,@Param("tm") String tm);

    /**
     * 查询团队列表
     * @param map
     * @return
     */
    List<ProjectTeamVo> teamInfos(Map<String, Object> map);
    List<ProjectTeamVo> getTeamInfos(Map<String, Object> map);
    /**
     * 查询当前用户可以查看的团队编号
     * @param map
     * @return
     */
    List<String> teamNos(Map<String, Object> map);
    
    public int replaceInfo(@Param("team") TeamInfoVo teamInfoVo);
    public TeamInfoVo getTeamInfoVo(@Param("buName") String name, @Param("no") String no);
    
    List<ProjectInfo> getTeamInfo(@Param("teamId") String teamId);
    
    int getTeamSize(@Param("teamId") String teamId);
    
    int getMemberBaseCount(@Param("chinasoftAccount")String chinasoftAccount);

	int getTeamStaffCount(@Param("teamId") String teamId, @Param("chinasoftAccount") String chinasoftAccount);
    
//    int editTeamMemberInfoByImport(@Param("teamMembers")TeamMembers teamMembers);
	
	List<String> getRankList();
}