package com.icss.mvp.dao.statistics;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.capacity.AbilityEntity;
import com.icss.mvp.entity.capacity.LanguageEntity;
import com.icss.mvp.entity.capacity.WorkloadEntity;

/**
 * Created by Ray on 2018/7/27.
 */
public interface IProgramingDao {

    /**
     * 每月代码量统计
     *
     * @param projectId 项目编号
     * @return
     */
    List<Map<String, Object>> summarizeWorkloadMonthly(@Param("projectId") String projectId);

    /**
     * 每月提交次数统计
     *
     * @param projectId 项目编号
     * @return
     */
    List<Map<String, Object>> summarizeCommitAnnual(@Param("projectId") String projectId);

    /**
     * 每月提交次数统计
     *
     * @param projectId 项目编号
     * @return
     */
    List<WorkloadEntity> summarizeCommitMonthly(@Param("projectId") String projectId, @Param("begin") String begin,
                                                @Param("end") String end);

    /**
     * 个人代码量统计列表，团队所有成员每月代码量
     *
     * @param projectId 项目编号
     * @param type 统计方式，author或者messageAuthor
     * @return
     */
    List<Map<String, Object>> displayQuantityPersonal(@Param("projectId") String projectId, @Param("type") String type);

    /**
     * 个人提交次数统计列表，团队所有成员每月提交次数
     *
     * @param projectId 项目编号
     * @param type 统计方式，author或者messageAuthor
     * @return
     */
    List<Map<String, Object>> displayFrequencyPersonal(@Param("projectId") String projectId, @Param("type") String type);

    /**
     * 团队提交代码行数统计列表，团队各成员每月提交代码行数和代码类型，代码行数已按系数折算
     * 
     * @param projectId 项目编号
     * @param type 统计方式，Author或者message author
     * @param codeType 代码类型
     * @param begin 起始时间
     * @param end 结束时间
     * @return
     */
    List<WorkloadEntity> summarizeAmountMonthly(@Param("projectId") String projectId, @Param("type") String type,
                                                @Param("codeType") Set<String> codeType, @Param("begin") String begin,
                                                @Param("end") String end);

    /**
     * 个人提交代码行数统计列表，团队各成员每月提交代码行数和代码类型，已按系数折算
     *
     * @param projectId 项目编号
     * @param role 岗位指责列表
     * @return
     */
    List<AbilityEntity> getTeamMembers(@Param("no") String projectId, @Param("role") Set<String> role);

    /**
     * 获取全部文件类型
     * 
     * @return
     */
    List<LanguageEntity> getLanguage();

    /**
     * 团队各成员每月代码提交详情，提交代码行数和代码类型以及权重系数，代码行数未按系数折算
     * 
     * @param projectId 项目编号
     * @param type 统计方式，Author或者message author
     * @param codeType 文件类型
     * @param begin 起始时间
     * @param end 结束时间
     * @return
     */
    List<WorkloadEntity> metricRespectiveMonthly(@Param("projectId") String projectId, @Param("type") String type,
                                                 @Param("codeType") Set<String> codeType, @Param("begin") String begin,
                                                 @Param("end") String end);

    /**
     * 团队各成员每月代码提交详情，提交代码行数和代码类型以及权重系数，代码行数未按系数折算
     * 
     * @param projectId 项目编号
     * @param pattern 时间格式化
     * @param dictId 数据字典编号
     * @param begin 起始时间
     * @param end 结束时间
     * @return
     */
    List<AbilityEntity> metricRespective(@Param("projectId") String projectId, @Param("pattern") String pattern,
                                         @Param("dictId") String dictId, @Param("begin") String begin,
                                         @Param("end") String end);

    /**
     * 团队各成员每月代码提交详情，每月提交次数和最后提交时间
     *
     * @param projectId 项目编号
     * @param codeType 文件类型
     * @param pattern 时间格式化
     * @param begin 起始时间
     * @param end 结束时间
     * @return
     */
    List<AbilityEntity> metricCommit(@Param("projectId") String projectId, @Param("codeType") Set<String> codeType,
                                     @Param("pattern") String pattern, @Param("begin") String begin,
                                     @Param("end") String end);

}
