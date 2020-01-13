package com.icss.mvp.dao;

import com.icss.mvp.entity.SysResource;

import java.util.List;

public interface SysResourceDao {
    int deleteByPrimaryKey(String resourceId);

    int insert(SysResource record);


    SysResource selectByPrimaryKey(Integer resourceId);

    int updateByPrimaryKeySelective(SysResource record);

    /**
     * 查询资源列表
     * @return
     */
    List<SysResource> selectSysResources();
}