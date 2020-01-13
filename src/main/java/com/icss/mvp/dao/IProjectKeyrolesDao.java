package com.icss.mvp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectKeyroles;

public interface IProjectKeyrolesDao {

    List<ProjectKeyroles> queryProjectKeyrolesNo(@Param("pmid") String pmid);

    int insertInfos(@Param("proj") List<ProjectKeyroles> proj);

    int batchDeleteByNo(@Param("no") String no);

    void batchDelete();

    List<ProjectKeyroles> queryProjectKeyrolesZrAccount(@Param("zrAccount") String zrAccount);

    int updateMemberBase(@Param("proj") ProjectKeyroles projectKey);

    int updateProjectStaff(@Param("proj") ProjectKeyroles projectKey);

    int updateScore(@Param("account") String account, @Param("score") String score);

    List<ProjectKeyroles> queryProjectKeyrolesByNo(@Param("no") String proNo);
}
