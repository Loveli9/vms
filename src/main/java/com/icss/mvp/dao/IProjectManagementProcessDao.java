package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.MeasureComment;
import com.icss.mvp.entity.QualityPlanModule;

public interface IProjectManagementProcessDao {

	List<Map<String, Object>> measures();

	List<Map<String, Object>> categorys();

	List<Map<String, Object>> measureRanges(@Param("proNo") String proNo);

	void saveModule(QualityPlanModule qualityPlanModule);

	QualityPlanModule queryModule(@Param("no") String no, @Param("module") String module);

	List<QualityPlanModule> queryModules(@Param("no") String no);

	void deleteExceltable(@Param("no") String no, @Param("module") String module);

	void deleteQualityplanmodule(@Param("no") String no, @Param("moduleName") String moduleName);

	List<MeasureComment> queryMeasureList(@Param("proNo") String proNo);
}