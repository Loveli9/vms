package com.icss.mvp.service.project;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.project.IProjectTeamDao;
import com.icss.mvp.entity.ProjectTeam;

@Service("projectTeamService")
public class ProjectTeamService {

    private static Logger   logger = Logger.getLogger(ProjectTeamService.class);

    @Autowired
    private IProjectTeamDao teamDao;

    public int getTeamId(String teamName) {
        int result = 0;

        try {
            List<ProjectTeam> teams = teamDao.getProjectTeams(teamName);
            if (teams != null && !teams.isEmpty()) {
                result = teams.get(0).getId();
            }

            if (result <= 0) {
                // 新增团队信息
                ProjectTeam projectTeam = new ProjectTeam();
                projectTeam.setTeamName(teamName);
                projectTeam.setCreateTime(new Date());
                projectTeam.setModifyTime(projectTeam.getCreateTime());
                projectTeam.setIsDeleted(0);
                teamDao.insertProjectTeam(projectTeam);

                result = projectTeam.getId();
            }
        } catch (Exception e) {
            result = -1;
            logger.error("getTeamId exception, error: " + e.getMessage());
        }
        return result;
    }
}
