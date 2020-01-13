package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.Qmslist;
import com.icss.mvp.entity.Qmsproblem;
import com.icss.mvp.entity.Qmsresult;
import com.icss.mvp.entity.QualityMonthlyReport;

public interface IQmsMaturityDao {

	List<Qmsresult> queryQMSresults(@Param("no") String no, @Param("source") String source,@Param("better") String better,@Param("selects") List<String> selects);

	Qmsresult queryQMS(@Param("no") String no, @Param("id") String id);

	void replaceQMSresult(Qmsresult qmsresult);

	List<Integer> qmsRanges(@Param("qmsId") Integer qmsId);

	List<String> qmsTypes();

	List<Qmsproblem> qmsProblemType(@Param("source") String source);

	List<Qmsproblem> qmsReport(@Param("proNos") String proNos,@Param("type") String type,@Param("month") String month);
	
	Integer qmsReportNos(@Param("proNos") String proNos,@Param("pid") String pid,@Param("type") String type,@Param("month") String month);

	String sumConform(@Param("no") String no,@Param("source") String source);

	List<String> multipleMenuSource(@Param("source") String source);

	List<String> multipleMenuLabel(@Param("source") String source);

	List<Map<String, Object>> getSelectQuestion(@Param("source") String source);

	void queryQMSresultsSave(Qmsresult qmsresult);
	
	void queryQMSresultsSaves(List<Map<String, Object>> list);

	void saveSelectQuestion(@Param("no") String no,@Param("qmsId") String qmsId,@Param("pid") String pid);

	void qmsReportSave(Qmsproblem qmsproblem);

	Integer getQmsReports(@Param("proNo") String proNo,@Param("month") String month,@Param("type") String type);

	List<Qmsresult> downloadProblemQMS(@Param("no") String no);

	List<Qmsproblem> qmsSector(@Param("proNo") String proNo,@Param("month") String month,@Param("type") String type);

	String qmsSource(@Param("proNo") String proNo,@Param("month") String month,@Param("type") String type);

	List<Qmsproblem> qmsHistogram(@Param("proNo") String proNo,@Param("name") String name,@Param("month") String month,@Param("type") String type);

	void qmsReportSaves(List<Qmsproblem> list);

	List<Qmsresult> queryQMShistorical();

	void saveQMShistorical(List<Qmsresult> list);

	void importQMS(List<Qmsresult> list);

	List<Qmslist> getQmsidBy();

	List<Qmsproblem> getQmsListProblem();

	List<Qmsresult> exportQMS(@Param("type") String type,@Param("month") String month);

	List<QualityMonthlyReport> qmsDimension(@Param("proNo") String proNo,@Param("type") String type,@Param("month") String month
			,@Param("username") String username,@Param("coopType") String coopType);

	List<Qmsproblem> qmsWholeProject();

	List<QualityMonthlyReport> qmsSelfchecking(@Param("month") String month);

	List<Qmsresult> getPersonLiable(@Param("no") String no);

	void savePersonLiable(@Param("no") String no,@Param("qmsId") String qmsId,@Param("account") String account);
}