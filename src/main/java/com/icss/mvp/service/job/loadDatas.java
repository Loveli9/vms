package com.icss.mvp.service.job;/**
 * Created by chengchenhui on 2019/5/23.
 */

import com.icss.mvp.dao.MeasureRangeDao;
import com.icss.mvp.entity.MeasureConfigRecord;
import com.icss.mvp.util.StringUtilsLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author chengchenhui
 * @title: 指标id更新
 * @projectName mvp
 * @description: TODO
 * @date 2019/5/2316:40
 */
@Service("loadDatas")
@EnableScheduling
@PropertySource("classpath:task.properties")
@SuppressWarnings("all")
@Transactional
public class loadDatas {
    @Autowired
    private MeasureRangeDao dao;

//    @Scheduled(cron = "${job-update-measureId}")
    public void updateRecords() {
        List<MeasureConfigRecord> list = dao.getRecord();
        List<Map<String, Object>> obj = dao.getCheckIds();
        if (null != list && list.size() > 0) {
            for (MeasureConfigRecord record : list) {
                if (StringUtilsLocal.isBlank(record.getMeasureIds())) {
                    continue;
                }
                List<String> measuresIds = Arrays.asList(record.getMeasureIds().split(","));
                for (String id : measuresIds) {
                    for (Map ob : obj) {
                        if (id.equals(ob.get("id"))) {
                            id = null == ob.get("copy_id") ? id : ob.get("copy_id").toString();
                        }
                    }
                }
                String updateMeasureIds = String.join(",", measuresIds);
                dao.updateConfigRecord(updateMeasureIds, record.getId());
            }
        }

    }

}
