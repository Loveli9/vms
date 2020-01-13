package com.icss.mvp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.icss.mvp.entity.IterativeWorkManageEdit;
import com.icss.mvp.entity.ProjectMembersLocal;

public interface IterativeWorkManageEditDao {

    void readyToCheck(IterativeWorkManageEdit iterativeWorkManageEdit);

    IterativeWorkManageEdit queryIterativeWorkManageEdit(@Param("id") String id);

    String dataDictionary(@Param("code") String code, @Param("val") String val);

    void checkResult(@Param("result") String result, @Param("id") String id);

    void personLiable(@Param("person") String person, @Param("id") String id);

    void editIterativeWorkManage(IterativeWorkManageEdit iterativeWorkManageEdit);

    List<ProjectMembersLocal> members(@Param("no") String no, @Param("zrAccount") String zrAccount,
                                      @Param("hwAccount") String hwAccount);

    void deleteEdit(@Param("id") String id);

    List<ProjectMembersLocal> queryAllMembers(@Param("zrAccount") String zrAccount,
                                              @Param("hwAccount") String hwAccount);

}
