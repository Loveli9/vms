package com.icss.mvp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.icss.mvp.entity.IterationCycleResult;
import org.apache.ibatis.annotations.Param;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.Measure;
import com.icss.mvp.entity.TableSplitResult;

@SuppressWarnings("all") public interface IterationCycleDao {

    int deleteByPrimaryKey(String id);

    int insert(IterationCycle record);

    int insertSelective(IterationCycle record);

    IterationCycle selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(IterationCycle record);

    /**
     * 查询迭代周期
     *
     * @return
     */
    List<IterationCycle> iterationInfo();

    List<Map<String, Object>> iterationSchedule(@Param("proNo") String proNo, @Param("fromDate") String fromDate,
                                                @Param("toDate") String toDate);

    List<Map<String, Object>> workingHours(@Param("proNo") String proNo, @Param("startDate") String startDate,
                                           @Param("endDate") String endDate);

    Map<String, Object> queryBugInfo(@Param("proNo") String proNo, @Param("startDate") String startDate,
                                     @Param("endDate") String endDate);

    int demandCodeNum(@Param("proNo") String proNo, @Param("startDate") String startDate,
                      @Param("endDate") String endDate);

    int closedBugCount(@Param("proNo") String proNo, @Param("startDate") String startDate,
                       @Param("endDate") String endDate);

    List<Measure> selectmeasure(@Param("proNo") String proNo, @Param("title") String title,
                                @Param("category") String category, @Param("labId") String labId);

    List<String> getSelectmeasure(Map<String, Object> map);

    List<Measure> getSelectMeasureByProjectId(String proId);

    List<Measure> getManInsertByProjectId(String proId);

    /**
     * 查询项目的迭代列表
     *
     * @param proNo
     * @return
     */
    List<IterationCycle> iterationList(String proNo);

    /**
     * 查询项目的迭代列表
     *
     * @param proNo
     * @return
     */
    List<IterationCycle> iterationListasc(String proNo);

    /*
     * 查询自动化新增指标对应值
     */
    int queryAddCaseINfo(@Param("proNo") String proNo, @Param("startDate") String startDate,
                         @Param("endDate") String endDate);

    /*
     * 查询自动化执行指标对应值
     */
    int queryImpCaseInfo(@Param("proNo") String proNo, @Param("startDate") String startDate,
                         @Param("endDate") String endDate);

    int queryDtsCount(@Param("proNo") String proNo, @Param("startDate") String startDate,
                      @Param("endDate") String endDate);

    int searchParameter(@Param("proNo") String proNo, @Param("startDate") String startDate,
                        @Param("endDate") String endDate, @Param("parameterId") String parameterId);

    /*
     * 测试-测试缺陷密度-迭代内发现问题单总数
     */
    Double queryWtdNum(@Param("proNo") String proNo, @Param("startDate") String startDate,
                       @Param("endDate") String endDate);

    /***
     * @Description:分页查询迭代管理信息 @param @param request 参数 @return void 返回类型 @throws
     */
    List<IterationCycle> queryIteInfoByPage(@Param("page") TableSplitResult page, @Param("sort") String sort,
                                            @Param("sortOrder") String sortOrder);

    List<IterationCycleResult> queryAll(@Param("page") TableSplitResult page, @Param("sort") String sort,
                                        @Param("sortOrder") String sortOrder);

    /**
     * @Description:删除当前选中的迭代管理信息 @param @param ids @param @return 参数 @return
     * BaseResponse 返回类型 @throws
     */
    void deleteIterationCycle(@Param("id") String id);

    /**
     * @Description:查询总记录数 @param @param proNo @param @return 参数 @return int
     * 返回类型 @throws
     */
    Integer queryIterationTotals(@Param("page") TableSplitResult page);

    Integer queryAllTotals(@Param("page") TableSplitResult page);

    /**
     * @Description:迭代编辑页面回显 @param @param iterationCycle @param @return 参数 @return
     * BaseResponse 返回类型 @throws
     */
    IterationCycle queryEditPageInfo(@Param("id") String id);

    /**
     * @Description: 加载迭代名称下拉列表值 @param @param iterationCycle @param @return
     * 参数 @return BaseResponse 返回类型 @author chengchenhui @throws
     */
    List<Map<String, Object>> getIteNameSelect(@Param("proNo") String proNo);

    /**
     * @Description: 加载迭代名称下拉列表值 @param @param iterationCycle @param @return
     * 参数 @return BaseResponse 返回类型 @author chengchenhui @throws
     */
    List<IterationCycle> getIteNameSelectByProNo(@Param("proNo") String proNo);

    /**
     * @Description: 校验迭代名称唯一性 @param @param iterationCycle @param @return
     * 参数 @return BaseResponse 返回类型 @author chengchenhui @throws
     */
    List<IterationCycle> checkIteName(@Param("proNo") String proNo, @Param("iteName") String iteName);

    /**
     * @Description: 校验迭代时间顺序 @param @param iterationCycle @param @return 参数 @return
     * BaseResponse 返回类型 @author chengchenhui @throws
     */
    IterationCycle checkStartTime(@Param("proNo") String proNo);

    /**
     * 查询项目名称
     *
     * @param projNo
     * @return
     */
    String getProjNoName(@Param("proNo") String proNo);

    /**
     * 代码监视发现问题数
     *
     * @param proNo
     * @param formatDate
     * @param formatDate2
     * @return
     */
    Integer queryCodeProblemCount(@Param("proNo") String proNo, @Param("startDate") String startDate,
                                  @Param("endDate") String endDate);

    /**
     * 代码监视发现问题处理数量
     *
     * @param proNo
     * @param formatDate
     * @param formatDate2
     * @return
     */
    Integer queryCodeProblemDealCount(@Param("proNo") String proNo, @Param("startDate") String startDate,
                                      @Param("endDate") String endDate);

    /**
     * 判断当前项目是否含有迭代信息
     *
     * @param proNo
     * @return
     */
    List<IterationCycle> isHaveIteration(@Param("proNo") String proNo);

    List<Map<String, Object>> queryCompletionDegree(@Param("iteId") String iteId);

    List<Map<String, Object>> completion(@Param("iteId") String iteId);

    int editCompletion(@Param("id") String id, @Param("display") String display);

    Integer bugSubmissionPeople(@Param("no") String no);

    List<Map<String, Object>> bugSubmission(@Param("no") String no);

    Integer bugSubmissionZR(@Param("no") String no);

    /**
     * 消费者业务线-问题单总数
     */
    Integer numberOfIssues(@Param("proNo") String no, @Param("startDate") String startDate,
                           @Param("endDate") String endDate);

    /**
     * 消费者业务线-无效问题数
     */
    Integer invalidProblem(@Param("proNo") String no, @Param("startDate") String startDate,
                           @Param("endDate") String endDate);

    /**
     * 消费者业务线-解决问题数
     */
    Integer numberOfProblemSolvers(@Param("proNo") String no, @Param("startDate") String startDate,
                                   @Param("endDate") String endDate);

    /**
     * 消费者业务线-回归不通过问题单数
     */
    Integer numberReturnOrNot(@Param("proNo") String no, @Param("startDate") String startDate,
                              @Param("endDate") String endDate);

    /**
     * 根据日期获取迭代名称
     *
     * @param proNo
     * @param date
     * @return
     */
    IterationCycle queryIteInfo(@Param("proNo") String proNo, @Param("date") Date date);

    /**
     * 根据id获取迭代信息
     *
     * @param id
     * @return
     */
    IterationCycle queryIteInfoById(@Param("id") String id);
}
