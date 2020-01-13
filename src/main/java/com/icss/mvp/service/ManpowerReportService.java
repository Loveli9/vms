package com.icss.mvp.service;

import com.icss.mvp.dao.ManpowerReportDao;
import com.icss.mvp.entity.ManpowerReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ManpowerReportService {
    @Autowired
    private ManpowerReportDao manpowerReportDao;
    public List<ManpowerReport> keyRole(Map<String, Object> paraMap) {
        return manpowerReportDao.keyRole(paraMap);
    }

    public List<Map<String, String>> positionRate(Map<String, Object> paraMap) {
        return manpowerReportDao.positionRate(paraMap);
    }
}
