package com.icss.mvp.dao;

import com.icss.mvp.entity.MeasureConfigRecord;
import com.icss.mvp.entity.MeasureRange;
import com.icss.mvp.entity.TeamMeasureRange;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MeasureRangeDao {

    /**
     * 更新
     *
     * @param mr
     */
    void upDateRangeById(@Param("mr") MeasureRange mr);

    void updateByTeamId(@Param("mr") MeasureRange mr);

    /**
     * 新增
     *
     * @param mr
     */
    void insert(@Param("mr") MeasureRange mr);

    void insertByTeamId(@Param("mr") MeasureRange mr);

    /**
     * 查询
     *
     * @param proNo
     * @param measureId
     * @return
     */
    MeasureRange queryMeasureRange(@Param("proNo") String proNo, @Param("measureId") String measureId);

    TeamMeasureRange queryMeasureRangeByTeamId(@Param("teamId") String teamId, @Param("measureId") String measureId);

    void deleteMeasureRange(@Param("proNo") String proNo);

    /**
     * 团队内项目指标继承
     *
     * @param oldNo
     */
    void copyMeasureConfigByProject(@Param("proNo") String proNo, @Param("oldNo") String oldNo);


    /*临时*/
    List<MeasureConfigRecord> getRecord();

    void updateConfigRecord(@Param("measureIds") String measureIds, @Param("id") String id);

    List<Map<String, Object>> getCheckIds();
}
