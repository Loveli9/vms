package com.icss.mvp.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface VerificationCycleDao {
	
	/**
	 * 获取tmss信息
	 * @param projNo
	 * @return
	 */
	List<Map<String, Object>> getTmssInfo(@Param("no")String projNo,@Param("year")String year);
	/**
	 * 获取tmss信息
	 * @param projNo
	 * @return
	 */
	List<Map<String, Object>> getTmssInfoNos(@Param("no")String projNo,@Param("year")String year);
	/**
	 * 获取所有执行成功的自动化用例个数，用B版本分组，获取总数
	 * @param projNo
	 * @return
	 */
	List<Map<String, Object>> getAllBVversion(@Param("no")String projNo, @Param("month")String month,@Param("year")String year);
	
	/**
	 * 获取所有执行成功的自动化用例个数，用B版本分组，获取总数
	 * @param projNo ('******','******')in输入语句
	 * @param month
	 * @param year
	 * @return
	 */
	List<Map<String, Object>> getAllBVversionNos(@Param("no")String projNo, @Param("month")String month,@Param("year")String year);
	
	/**
	 * 获取所有执行成功的自动化用例执行时间总和，以分为单位
	 * @param projNo
	 * @param bVersionName
	 * @param month
	 * @param year
	 * @return
	 */
	Map<String, Object> getMinutesGroupMouth(@Param("no")String projNo,@Param("bVersionName")String bVersionName, @Param("month")String month,@Param("year")String year);
	/**
	 * 获取所有执行成功的自动化用例执行时间总和，以分为单位
	 * @param bVersionName ('******','******')in输入语句
	 * @param month
	 * @param year
	 * @return
	 */
	Map<String, Object> getMinutesGroupMouthVers(@Param("bVersionName")String bVersionName, @Param("month")String month,@Param("year")String year);
	
	/**
	 * 获取所有pass，fail，investigate的测试用例参数，用B版本分组，获取版本信息
	 * @param projNo
	 * @return
	 */
	List<Map<String, Object>> getTotalAllBVversion(@Param("no")String projNo, @Param("month")String month,@Param("year")String year);
	/**
	 * 获取所有pass，fail，investigate的测试用例参数，用B版本分组，获取版本信息
	 * @param projNo ('******','******')in输入语句
	 * @return
	 */
	List<Map<String, Object>> getTotalAllBVversionNos(@Param("no")String projNo, @Param("month")String month,@Param("year")String year);
}
