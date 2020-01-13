package com.icss.mvp.dao.project;

import com.icss.mvp.entity.MeasureComment;
import com.icss.mvp.entity.MonthlyReportStatistics;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.QualifyTrend;
import com.icss.mvp.entity.project.ProjectLampMode;
import com.icss.mvp.entity.project.ProjectReviewEntity;
import com.icss.mvp.entity.project.ProjectReviewVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * Created by up on 2019/2/14.
 */
public interface IProjectReviewDao {

	/**
	 * 获取项目对应的项目点评信息
	 *
	 * @param no
	 *            项目编号
	 * @return
	 */
	List<ProjectReviewEntity> getProjectReviewList(@Param("proNo") String proNo);

	/**
	 * 获取项目对应的最后5个项目点评信息
	 *
	 * @param no
	 *            项目编号
	 * @return
	 */
	List<ProjectReviewEntity> getProjectReviewListTop5(@Param("proNo") String proNo);

	int editProjectQualityDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

	List<String> queryProjectCycle();

	List<String> queryProjectCycleQ(@Param("startDate") String startDate, @Param("endDate") String endDate);

	ProjectReviewEntity getProjectReview(@Param("proNo") String proNo, @Param("date") String date);

	ProjectInfo getDateTimeByNo(@Param("proNo") String proNo);

	ProjectReviewEntity getProjectReviewQ(@Param("proNo") String proNo, @Param("date") String date);

	String getCloseDateByNo(@Param("proNo") String proNo);

	/**
	 * 项目状态关闭
	 * 
	 * @param importTime
	 */
	void closeProjects(@Param("proNo") String proNo);

	void changeIsClose(@Param("proNo") String proNo, @Param("date") String date);

	ProjectReviewEntity getProjectReviewHistory(@Param("proNo") String proNo, @Param("date") String date);

	ProjectReviewEntity getProjectReviewDetail(@Param("proNo") String proNo, @Param("date") String date);

	int editProjectReviewTemp(ProjectReviewEntity entity);

	int editProjectReview(ProjectReviewEntity projectReviewEntity);

	int changeEdit(ProjectReviewEntity projectReviewEntity);

	String getProjectStatus(@Param("proNo") String proNo, @Param("date") String date);

	int progressEdit(ProjectReviewEntity projectReviewEntity);

	int manualLampEdit(@Param("field") String field, @Param("manualValue") int manualValue,
			@Param("proNo") String proNo, @Param("statisticalTime") String statisticalTime);

	int updateProjectLamp(@Param("proNo") String proNo, @Param("date") String date, @Param("field") String field,
			@Param("lamp") String lamp);

	List<ProjectReviewEntity> getProjectReviewInfo(@Param("proNo") String proNo,
			@Param("statisticalTime") String statisticalTime, @Param("status") String status);

	List<ProjectReviewEntity> getTopProjectReview(@Param("proNo") String proNo,
			@Param("statisticalTime") String statisticalTime);

	List<ProjectReviewVo> getOldWeekReview(@Param("proNo") String proNo, @Param("oldWeekDate") String oldWeekDate);

	int updateProjectLampMode(ProjectLampMode lampMode);

	int editProjectLampMode(ProjectLampMode lampMode);

	ProjectLampMode getProjectLampMode(@Param("proNo") String proNo, @Param("date") String date,
			@Param("field") String field);

	Integer queryProjectReviewTop6(Map<String, Object> map);

	Integer getState(Map<String, Object> map);

	Integer queryLastPeriod(Map<String, Object> map);

	Integer twoWeekPeriod(Map<String, Object> map);

	List<MeasureComment> getMeasureCommentList(@Param("id") String id, @Param("month") String month);

	MonthlyReportStatistics queryProjectExpect(Map<String, Object> map);

	List<Map<String, Object>> actualTableSave(Map<String, Object> maps);

	List<MeasureComment> queryQualityState(@Param("month1") String month1, @Param("month2") String month2,
			@Param("no") List<Integer> no);

	List<MeasureComment> queryQualityEchars(@Param("month1") String month1, @Param("month2") String month2,
			@Param("no") int no);

	String getMeasureName(@Param("id") Integer id);

	List<QualifyTrend> queryQualifyTrend(Map<String, Object> maps);

	List<QualifyTrend> queryQualifyTrendLastMonth(Map<String, Object> maps);

	List<String> isKxProject();

	Integer queryQualifyEchar(Map<String, Object> maps);

	List<Map<String, Object>> getMeasuresName(@Param("Measure_List") List<Integer> Measure_List,
			@Param("measureMark") String measureMark);

	List<Integer> getKXmeasure(@Param("lid") String lid);

	List<MeasureComment> measureValueLine(@Param("projectId") String projectId, @Param("id") String id);

	String measureValueHistory(@Param("start") String start, @Param("end") String end,
			@Param("projectId") String projectId, @Param("id") String id);

	List<MeasureComment> measureValueLineCode(@Param("projectId") String projectId, @Param("id") String id);

	String measureValueHistoryCode(@Param("start") String start, @Param("end") String end,
			@Param("projectId") String projectId, @Param("id") String id);

	String getProjectTmNoindex(@Param("proNo") String proNo, @Param("date") String date);

	Map<String, Object> getMembersStatusRate(@Param("proNo") String proNo, @Param("date") String date);

	List<Map<String, Object>> getSelectProjectReview(@Param("no") String no);

	ProjectInfo getProjectMeasureids(@Param("no") String no, @Param("month") String month);

	Integer demandCompletion(@Param("no") String no, @Param("status") String status, @Param("start") String start,
			@Param("end") String end);

	Double totalHours(@Param("no") String no, @Param("start") String start, @Param("end") String end);

	Double modifyHours(@Param("no") String no, @Param("change") String change, @Param("start") String start,
			@Param("end") String end);

	Double newOrCancelHours(@Param("no") String no, @Param("change") String change, @Param("start") String start,
			@Param("end") String end);

}