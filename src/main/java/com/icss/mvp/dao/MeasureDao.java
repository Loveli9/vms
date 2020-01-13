package com.icss.mvp.dao;

import com.icss.mvp.entity.Measure;

import java.util.List;

public interface MeasureDao {


    /**
     * 在数据库中通过主键id查出指定的度量指标表记录
     *
     * @author gaoyao
     * @time 2018-5-7 14:40:18
     */
    public Measure getMeasureById(String id);

    /**
     * 在数据库中通过主键id删除指定度量指标表记录
     *
     * @author gaoyao
     * @time 2018-5-7 14:40:18
     */
    public Integer deleteMeasureById(String id);

    /**
     * 在数据库中查出所有度量指标表记录
     *
     * @author Administrator
     * @time 2018-5-7 14:40:18
     */
    public List<Measure> getAllMeasure();

    /**
     * 通过wd查询度量指标表记录
     *
     * @author gaoyao
     * @time 2018-5-7 14:40:18
     */
    public List<Measure> getMeasureByWD(String wd);

}
