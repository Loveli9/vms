package com.icss.mvp.dao.project;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectTeam;

public interface IProjectTeamDao {

	List<ProjectTeam> getProjectTeams(@Param("teamName") String teamName);
	
	void insertProjectTeam(ProjectTeam projectTeam);
}
