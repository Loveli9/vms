package com.icss.mvp.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import com.icss.mvp.entity.ParameterInfo;
import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.entity.ProjectParameter;

public interface InstrumentPanelDAO {

	/**
	 * 已关闭需求数
	 */
	Integer closedNeeds(@Param("no") String no);

	/**
	 * 总需求数
	 */
	Integer needs(@Param("no") String no);

	/**
	 * 删除指标值
	 */
	void delete(@Param("no") String no, @Param("id") String id);

	/**
	 * 增加指标值
	 */
	void insert(ParameterValueNew parameterValueNew);

	/**
	 * 查询指标值
	 */
	Double value(@Param("no") String no, @Param("id") String id);

	/**
	 * 查询仪表盘标准排序
	 */
	ParameterInfo instrumentPanel(@Param("id") String id);

	/**
	 * 查询仪表盘排序
	 */
	String instrumentPanelValue(@Param("no") String no, @Param("id") String id);

	/**
	 * 删除仪表盘排序
	 */
	void deleteInstrumentPanelValue(@Param("no") String no, @Param("id") String id);

	/**
	 * 增加仪表盘排序
	 */
	void insertInstrumentPanelValue(ProjectParameter projectParameter);
	/**
	 * 查询指标值(PCB取值)
	 */
	Double codeValue(@Param("no")String no, @Param("id")String string);
	/**
	 * 查询项目起始时间
	 */
	Date queryPrStartDate(String no);
}
