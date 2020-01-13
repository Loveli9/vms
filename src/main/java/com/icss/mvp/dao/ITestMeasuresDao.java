package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import com.icss.mvp.entity.TestCaseInput;

public interface ITestMeasuresDao {
	public List<Map<String, Object>> queryWorkHoursDayByAuthorLatest(@Param("author") String author, @Param("month") String month);
	public List<Map<String, Object>> queryWorkHoursDayByAuthorCurdate(@Param("author") String author, @Param("month") String month);
	public List<Map<String, Object>> queryTestCasesByAuthor(@Param("projNo") String projNo,
			@Param("author") String author,
			@Param("startDate") String startDate,
			@Param("endDate") String endDate);
	
	public List<Map<String, Object>> queryTestCasesStartByAuthor(@Param("projNo") String projNo,
			@Param("author") String author,
			@Param("startDate") String startDate,
			@Param("endDate") String endDate);

	public List<Map<String, Object>> queryInputTestCasesByAuthor(@Param("proNo") String proNo,
			@Param("author") String author,
			@Param("type") String type,
			@Param("startDate") String startDate,
			@Param("endDate") String endDate);

	public void deleteTestCaseByNo(@Param("proNo") String proNo,@Param("type") Integer type);

	public void insertTestCases(@Param("list") List<TestCaseInput> caseInputs);
	
	public int insert2TestCases(@Param("input") TestCaseInput testCaseInput);
	
	public void insertTestStatisticsCases(@Param("list") List<TestCaseInput> caseInputs);
	
	public List<Map<String, Object>> queryInputTestCasesList(@Param("proNo") String proNo,@Param("author") String author,@Param("type") String type);
	public List<Map<String, Object>> queryTestCasesList(@Param("proNo") String proNo,@Param("author") String author);
	public List<Map<String, Object>> queryTestCasesStartList(@Param("proNo") String proNo,@Param("author") String author);

}
