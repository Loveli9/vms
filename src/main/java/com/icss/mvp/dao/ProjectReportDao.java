package com.icss.mvp.dao;

import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.rank.ProjectMonthBudget;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProjectReportDao {

    /**
     * 根据项目id查询项目报表
     *
     * @param proNo
     * @return
     */
    List<MeasureValue> getMeasureValues(@Param("proNo") String proNo);

    List<ProjectAssessInfo> getProjectAssessByNo(@Param("proNo") String proNo);

    List<QualityMonthlyReport> getQualityonthlyReportList(@Param("proNo") String proNo, @Param("month") String month);

    Integer insertQualityMonthlyReport(@Param("data") QualityMonthlyReport qualityMonthlyReport);

    List<QualityMonthlyReport> getProjectAssessByNosQ(@Param("proNos") String proNos,
                                                      @Param("page") PageRequest pageRequest, @Param("numb") int numb);

    List<QualityMonthlyReport> getProjectAssessByNos(@Param("proNos") String proNos,
                                                     @Param("page") PageRequest pageRequest, @Param("numb") int numb);

    Integer getProjectAssessCount(@Param("proNos") String proNos, @Param("numb") int numb);

    Integer getProjectAssessCountQ(@Param("proNos") String proNos, @Param("numb") int numb);

    List<ProjectCommentsInfo> getProjectDscByNos(@Param("proNos") String proNos,
                                                 @Param("page") PageRequest pageRequest);

    Integer getProjectDscCount(@Param("proNos") String proNos);

    /**
     * @Description:查询符合条件的问题风险记录 @throws
     */
    List<ProjectAssessInfo> getProjectDscByNo(@Param("parameter") Map<String, Object> parameter);

    /**
     * @Description:查询符合条件的问题风险记录数 @throws
     */
    Integer getProjectDscByNoCount(Map<String, Object> parameter);

    int deleteProjectDsc(@Param("id") String id);

    int editProjectDsc(@Param("date") ProjectCommentsListInfo projectCommentsListInfo);

    /**
     * 保存问题列表仪表盘定制
     *
     * @param id
     * @param flag
     * @return
     */
    int editProjectDscConfig(@Param("id") Integer id, @Param("flag") Integer flag);

    /**
     * 需求总数查询
     */
    Integer queryTotalCount(@Param("proNo") String proNo);

    /**
     * 需求总数查询
     */
    Integer queryTotalCountByStatus(@Param("proNo") String proNo, @Param("status") String status);

    /**
     * 修改需求变更率
     */
    int editQualityMonthlyReport(@Param("qualityMonthlyReport") QualityMonthlyReport qualityMonthlyReport);

    /**
     * 查询需求完成率
     */
    String getDemandProgress(@Param("proNo") String proNo);

    /**
     * 修改需求完成率
     */
    void editDemandProgress(@Param("qualityMonthlyReport") QualityMonthlyReport qualityMonthlyReport);

    Nunber getAssessmentNunber(@Param("proNos") String proNos, @Param("num") int num);

    Nunber getAARNunber(@Param("proNos") String proNos, @Param("num") int num);

    Nunber getAuditNunber(@Param("proNos") String proNos, @Param("num") int num);

    Nunber getQualityNunber(@Param("proNos") String proNos, @Param("num") int num);

    String projectStartTime(String proNo);

    /**
     * 消费者业务线指标查询getProjectInfoByNos
     *
     * @param proNo
     * @param idsList
     * @return
     */
    List<CousumerQuality> getCousumerQualityIndex(@Param("proNo") String proNo,
                                                  @Param("idsList") List<Integer> idsList);

    List<QualityMonthlyReport> getProjectAssessPageAll(@Param("proNos") String proNos,
                                                       @Param("page") PageRequest pageRequest);

    Integer getProjectAssessCountAll(@Param("proNos") String proNos);

    List<QualityMonthlyReport> getProjectOverview(@Param("proNos") String proNos,
                                                  @Param("page") PageRequest pageRequest);

    List<QualityMonthlyReport> getProjectOverviews(@Param("proNos") String proNos,
                                                   @Param("statisticalTime") String statisticalTime, @Param("status") String status,
                                                   @Param("username") String username, @Param("coopType") String coopType,
                                                   @Param("sort") String sort, @Param("sortOrder") String sortOrder);

    List<QualityMonthlyReport> getProjectAccess(@Param("proNos") String proNos,
                                                @Param("page") TableSplitResult page);

    List<QualityMonthlyReport> getProjectAccessCurrent(@Param("proNos") String proNos);

    List<QualityMonthlyReport> getZongLanTopProject(Map<String, Object> maps);

    List<MeasureVo> getZhiLiangTopProject(Map<String, Object> maps);

    Integer getProjectOverviewCount(@Param("proNos") String proNos);

    Integer getProjectOverviewCounts(@Param("proNos") String proNos, @Param("statisticalTime") String statisticalTime,
                                     @Param("status") String status);

    List<QualityMonthlyReport> queryQualityProjects(Map<String, Object> parameterMap);

    int queryQualityProjectsCount(Map<String, Object> parameterMap);

    List<Map<String, Object>> getProjectMeasureIndex(@Param("proNo") String proNo,
                                                     @Param("measureIds") String measureIds, @Param("startDate") String startDate,
                                                     @Param("endDate") String endDate);

    List<Map<String, Object>> getCurrentMeasure(@Param("proNo") String proNo, @Param("measureIds") String measureIds,
                                                @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ReportProblemAnalysis> queryProblemAnalysis(Map<String, Object> maps);

    ReportProblemAnalysis reportProblemAnalysis(@Param("no") String no, @Param("startDate") String startDate,
                                                @Param("endDate") String endDate);

    ReportProblemAnalysis kanbanProblemAnalysis(Map<String, Object> organizationMap);

    int queryQualityTrendCount(Map<String, Object> parameterMap);

    List<ReportQualityEntity> queryQualityTrendList(Map<String, Object> parameterMap);

    List<String> getCredibleMeasureIds(@Param("credibleLableIds") String credibleLableIds);

    List<ReportProblemAnalysis> statesProblem(@Param("du") String du, @Param("start") String start,
                                              @Param("end") String end, @Param("type") String type);

    List<Map<String, Object>> getHistoryMeasure(@Param("proNo") String proNo, @Param("measureIds") String measureIds,
                                                @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<String> queryProNoList(Map<String, Object> parameterMap);

    int queryResourceReportCount(@Param("proNos") String proNos, @Param("id") Integer id, @Param("date") String date,
                                 @Param("username") String username, @Param("coopType") String coopType);

    List<MemberResourceReport> queryCurrentResourceReport(@Param("proNos") String proNos, @Param("id") Integer id,
                                                          @Param("date") String date, @Param("lastWeek") String lastWeek, @Param("page") PageRequest pageRequest,
                                                          @Param("username") String username, @Param("coopType") String coopType);

    List<MemberResourceReport> queryHistoryResourceReport(@Param("proNos") String proNos, @Param("id") Integer id,
                                                          @Param("date") String date, @Param("lastWeek") String lastWeek, @Param("page") PageRequest pageRequest,
                                                          @Param("username") String username, @Param("coopType") String coopType);

    /**
     * 查询符合权限且在周期内配置的项目数量
     *
     * @param proNos
     * @param ids
     * @param date
     * @return
     */
    int queryEfficiencyProjectsCount(@Param("proNos") List<String> proNos, @Param("measureIds") String[] measureIds,
                                     @Param("date") String date, @Param("username") String username, @Param("coopType") String coopType);

    /**
     * 查询符合权限且在周期内配置的项目信息集合
     *
     * @param proNos
     * @param ids
     * @param date
     * @param pageRequest
     * @return
     */

    List<ProjectInfo> queryEfficiencyProjectList(@Param("proNos") List<String> proNos,
                                                 @Param("measureIds") String[] measureIds, @Param("date") String date,
                                                 @Param("page") PageRequest pageRequest);

    List<ProjectInfo> queryEfficiencyProjectList(@Param("proNos") List<String> proNos,
                                                 @Param("measureIds") String[] measureIds, @Param("date") String date,
                                                 @Param("page") PageRequest pageRequest, @Param("username") String username,
                                                 @Param("coopType") String coopType);

    /**
     * 查询项目效率指标值
     *
     * @param proNo
     * @param ids
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String, Object>> getEfficiencyMeasureIndex(@Param("proNo") String proNo,
                                                        @Param("measureIds") String[] measureIds, @Param("startDate") String startDate,
                                                        @Param("endDate") String endDate);

    String queryProjectStabilityScore(@Param("proNo") String proNo, @Param("date") String date,
                                      @Param("field") String field, @Param("lampMode") String lampMode);

    List<Map<String, Object>> getMeasureIds(@Param("measureIdList") Set<String> measureIdList,
                                            @Param("category") String category);

    List<Map<String, Object>> getMeasureVale(Map<String, Object> parameter);

    List<ProjectMonthBudget> queryMonthCost(Map<String, Object> parameter);

    int queryCostReportCount(Map<String, Object> parameter);

    ProjectMonthBudget getProjectCost(@Param("proNo") String proNo, @Param("beginDate") String beginDate,
                                      @Param("endDate") String endDate);

    void addConcernItems(@Param("proNo") String proNo, @Param("username") String username);

    void deleteConcernItems(@Param("proNo") String proNo, @Param("username") String username);

    void addConcernItems(List<Map<String, Object>> list);


    ProjectAssessInfo getProjectEdit(@Param("id") String id);

}
