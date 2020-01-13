package com.icss.mvp.service;

import com.icss.mvp.dao.MeasureDao;
import com.icss.mvp.entity.Measure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("measureService")
@Transactional
public class MeasureService {

    @Autowired
    private MeasureDao dao;


    /**
     * 在数据库中通过主键id查出指定的度量指标表记录
     *
     * @author gaoyao
     * @time 2018-5-7 14:40:18
     */
    public Measure getMeasureById(String id) {
        return dao.getMeasureById(id);
    }

    /**
     * 在数据库中通过主键id删除指定度量指标表记录
     *
     * @author gaoyao
     * @time 2018-5-7 14:40:18
     */
    public Integer deleteMeasureById(String id) {
        return dao.deleteMeasureById(id);
    }

    /**
     * 在数据库中查出所有度量指标表记录
     *
     * @author gaoyao
     * @time 2018-5-7 14:40:18
     */
    public List<Measure> getAllMeasure() {
        return dao.getAllMeasure();
    }

    /**
     * 通过wd查询度量指标表记录
     *
     * @author gaoyao
     * @time 2018-5-7 14:40:18
     */
    public List<Measure> getMeasureByWD(String wd) {
        return dao.getMeasureByWD(wd);
    }

}
