package com.icss.mvp.dao.project;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.project.MaturityAssessment;

import java.util.List;
import java.util.Map;

public interface MaturityAssessmentDao {
	
	public List<MaturityAssessment> getAssessmentCategory(@Param("createTime") String createTime,@Param("no") String projNo,@Param("template") String template);

	public List<Map<String, Object>> getIdByTemplate(@Param("template") String template);

	public int insertAssessmentCategory(@Param("assessmentList") List<Map<String, Object>> assessmentList);

	public List<Map<String, Object>> getTemplateIs361();

	public List<MaturityAssessment> getProjectManualValue(@Param("proNo") String proNo,@Param("flag") boolean flag,@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("value") String value,@Param("template") String template);

	public String getTemplateValue(@Param("proNo") String proNo);

	public void updateTemplateValue(@Param("proNo") String proNo, @Param("template") String template);

}
