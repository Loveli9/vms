/**
 * 
 */
package com.icss.mvp.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.Measure;

/**
 * @author 陈丽佳
 *
 */
public interface MeasureTopDao {
	
	/**
	 * 查询当月历史指标信息
	 * @author chenlijia
	 */
	public List<Measure> getCurrentMonthTopMeasure(@Param("list") Set<String> proNos);
	
	
	
	/**
	 * 根据ID查询本年度所有的指标信息
	 * @author chenlijia
	 */
	public List<Measure> statisticsByID(@Param("list") Set<String> proNos, @Param("measureID") Long measureID);
	
	
	

}
