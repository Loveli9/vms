package com.icss.mvp.service;

import com.alibaba.fastjson.JSONArray;
import com.icss.mvp.dao.SysResourceDao;
import com.icss.mvp.entity.SysResource;
import com.icss.mvp.util.SysResourceUtil;
import com.icss.mvp.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysResourceService {
    @Autowired
    private SysResourceDao sysResourceDao;
    public int insertSysResource(SysResource sysResource) {
        sysResource.setResourceId(UUIDUtil.getNew());
        return sysResourceDao.insert(sysResource);
    }

    public int editSysResource(SysResource sysResource) {
        return sysResourceDao.updateByPrimaryKeySelective(sysResource);
    }

    public int deleteSysResource(String id) {
        return sysResourceDao.deleteByPrimaryKey(id);
    }

    public JSONArray selectSysResources() {
        //资源在数据库查询时已经按照等级排序
        List<SysResource> sysResources = sysResourceDao.selectSysResources();
        return SysResourceUtil.formatResource(sysResources);
    }

}
