package com.icss.mvp.dao;

import com.icss.mvp.entity.ManpowerReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ManpowerReportDao {
    List<ManpowerReport> keyRole(Map<String, Object> paraMap);

    List<Map<String,String>> positionRate(Map<String, Object> paraMap);
}
