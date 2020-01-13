package com.icss.mvp.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.CodeGainType;

public interface CodeGainTypeDao {
	
	/**
	* @Description:新增记录  
	* @author Administrator  
	* @date 2018年6月5日  
	 */
	void saveCodeGainType(@Param("cGainType")CodeGainType cGainType);
	
	/**
	* @Description:判断是否一存储记录
	* @author Administrator  
	 * @param string 
	* @date 2018年6月5日  
	 */
	Integer getCodeTypeNum(@Param("no") String no,@Param("parameterId") String parameterId);
	
	/**
	* @Description: 更新记录
	* @author Administrator  
	* @date 2018年6月5日  
	 */
	void updateCodeGainType(@Param("cGainType")CodeGainType cGainType);

	/**
	* @Description:获取配置方式  
	* @author Administrator  
	* @date 2018年6月5日  
	 */
	List<CodeGainType> getCodeTypeByNo(@Param("no") String no,@Param("parameterIds") String parameterIds);

	/**
	* @Description:保存邮件信息
	* @author Administrator  
	 * @param string 
	* @date 2018年6月5日  
	 */
	void saveEmailInfo(@Param("no") String no, @Param("emailUrl") String emailUrl, @Param("emailOnOff") String emailOnOff);

	List<Map<String,Object>> getEmailInfo(String no);

	void updateEmailInfo(@Param("no") String no, @Param("emailUrl") String emailUrl, @Param("emailOnOff") String emailOnOff);

	int saveProjectParamterRecord(CodeGainType cGainType);

	int getStaticMeasureCount(@Param("proNo") String proNo);

	int getReliableStaticMeasureCount(@Param("proNo") String proNo);

	Integer getClose(@Param("no") String no, @Param("closeMark") String closeMark);

	String queryProjectPO(@Param("proNo") String proNo);

	void updateProjectPO(@Param("proNo") String proNo, @Param("po") String po);

	void addProjectPO(@Param("proNo") String proNo, @Param("po") String po);
	
	String getProIsKx(@Param("no") String no);
	
	int queryProjectOffline(@Param("no") String no, @Param("measureMark") String measureMark,
			@Param("measureType") String measureType);
}
