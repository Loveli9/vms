package com.icss.mvp.dao;

import org.apache.ibatis.annotations.Param;
import com.icss.mvp.entity.JenkinsDetailedEntity;

public interface IJenkinsBuildDetailedDao {

	void insertJenkinsDetailed(JenkinsDetailedEntity jenkinsDetaile);

	JenkinsDetailedEntity queryJenkinsDetail(JenkinsDetailedEntity jenkinsDetaile);

	JenkinsDetailedEntity calculateJenkins(@Param("no") String no, @Param("type") String type,
			@Param("measure") String measure);

}