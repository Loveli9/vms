package com.icss.mvp.service;

import com.icss.mvp.dao.IProjectScheduleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProjectScheduleService {
    @Autowired
    private IProjectScheduleDao projectScheduleDao;

    /**
     * 项目进度列表查询
     * @param proNos
     * @param step
     * @return
     */
    public List<Map<String, Object>> projectScheduleList(Set<String> proNos, int step){
        return projectScheduleDao.projectScheduleList(proNos, step);
    }

}
