package com.icss.mvp.dao;


import org.apache.ibatis.annotations.Param;


public interface ActualValCalculateDao {
	
	/**
	* @Description: 测试用例设计效率
	* @author Administrator  
	* @date 2018年5月15日  
	 */
	String testCaseDesignEffec(@Param("proNo") String proNo);
	
	/**
	* @Description: 月代码产量
	* @author Administrator  
	* @date 2018年5月15日  
	 */
	String getCodeNum(@Param("proNo") String proNo,@Param("authors") String authors);
	String getCodeNumWx(@Param("proNo") String proNo,@Param("authors") String authors);
	
	/**
	* @Description: 统计工作投入占比
	* @author Administrator  
	* @date 2018年5月17日  
	* @version V1.0
	 */
	String getWorkPercent(@Param("proNo") String proNo,@Param("name") String name);
	
	/**
	* @Description: 统计月总工时
	* @author Administrator  
	* @date 2018年5月18日  
	* @version V1.0
	 */
	String queryHoursAll(@Param("zrAccount")String zrAccount);
	/**
	 * 问题单数量
	 */
	Integer dtsCount(String proNo);
	
	
	
}
