package com.icss.mvp.dao;

import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.IterationMeasureIndex;
import com.icss.mvp.entity.Measure;
import com.icss.mvp.entity.TableSplitResult;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IterationMeasureIndexDao {
    int insert(IterationMeasureIndex record);

    int insertSelective(IterationMeasureIndex record);

   /* List<IterationMeasureIndex> measureIndexValues(@Param("iterationId") String iterationId,
            @Param("list") List measureIds);*/
    
    List<IterationMeasureIndex> measureIndexValues(@Param("iterationId") String iterationId,
                                                   @Param("list") List measureIds,
                                                   @Param("startDate") String startDate,
                                                   @Param("endDate") String endDate,
                                                   @Param("no") String no);
    int updateByPrimaryKeySelective(IterationMeasureIndex record);

    int insertByBatch(@Param("list") List<IterationMeasureIndex> list);

    /**
     * 删除非手工录入的旧指标
     * @param iterationId
     * @return
     */
    int delOldValue(@Param("iterationId") String iterationId,
                    @Param("title") String title);

	void delOldValueByName(@Param("iterationId") String iterationId,
                    @Param("title") String title);
	
	/**
	 * 
	 */
	void delOldTime(@Param("measure") String measure);
	
	int insertNewTime(@Param("list") List<IterationMeasureIndex> list);
	
	List<IterationMeasureIndex> queryIterationInfo(@Param("measureId") String measure_id,
										   @Param("list")String[] list);
	/**
	 * 查询手工录入指标列表
	 * @param page
	 * @param proNo
	 * @return
	 */
	List<Measure> queryManualEntryMeasure(@Param("page")TableSplitResult page,@Param("proNo") String proNo);
	/**
	 * 查询手工录入指标数量
	 * @param proNo
	 * @return
	 */
	Integer countManualEntryMeasure(@Param("proNo") String proNo);
	Integer checkIterationInfo(@Param("iterationId") String iterationId,
							   @Param("measureId") String measureId);
	/**
	 * 查询指标值
	 * @param iterationCycle
	 * @return
	 */
	List<IterationMeasureIndex> getMeasureIndexValue(IterationCycle iterationCycle);
	
	
}