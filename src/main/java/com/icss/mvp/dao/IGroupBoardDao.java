package com.icss.mvp.dao;

import com.icss.mvp.entity.GroupAcceptanceEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 组织看板 &项目看板
 *
 * @author limingming
 */
public interface IGroupBoardDao {
    /**
     * 一级组织机构集合
     *
     * @param organizationMap
     * @return
     */
    List<Map<String, Object>> getFirstOrganizations(Map<String, Object> organizationMap);

    /**
     * 二级组织机构集合
     *
     * @param organizationMap
     * @return
     */
    List<Map<String, Object>> getSecondOrganizations(Map<String, Object> organizationMap);

    /**
     * 三级组织机构集合
     *
     * @param organizationMap
     * @return
     */
    List<Map<String, Object>> getThirdOrganizations(Map<String, Object> organizationMap);

    /**
     * 项目看板执行表
     *
     * @param organizationMap
     * @return
     */
    List<Map<String, Object>> queryProjectExecute(Map<String, Object> organizationMap);

    /**
     * 项目看板执行表count
     *
     * @param organizationMap
     * @return
     */
    Integer queryProjectExecuteCount(Map<String, Object> organizationMap);

    /**
     * 项目看板验收表
     *
     * @param organizationMap
     * @return
     */
    List<GroupAcceptanceEntity> queryProjectAcceptance(Map<String, Object> organizationMap);

    /**
     * 项目看板验收表count
     *
     * @param organizationMap
     * @return
     */
    Integer queryProjectAcceptanceCount(Map<String, Object> organizationMap);

    /**
     * 组织看板验收表结项项目个数
     *
     * @param id
     * @param month
     * @param flag
     * @return
     */
    Integer queryKnotProjectCount(@Param("id") String id, @Param("month") String month, @Param("flag") String flag);

    /**
     * 组织看板总览表风险项目个数
     *
     * @param id
     * @param month
     * @param flag
     * @return
     */
    List<String> queryRiskProjectCount(@Param("id") String id, @Param("month") String month, @Param("flag") String flag);

    /**
     * 组织看板总览表执行项目个数
     *
     * @param id
     * @param month
     * @param flag
     * @return
     */
    Integer queryExecutionProjectCount(@Param("id") String id, @Param("month") String month, @Param("flag") String flag);

    /**
     * 获取第二、第三组织部门
     *
     * @param flag
     * @param orgFlag
     * @return
     */
    List<Map<String, Object>> getSecondAndThirdOrg(@Param("flag") int flag, @Param("orgFlag") String orgFlag);

    /**
     * 获取第二组织部门的人员到位率和稳定度信息
     *
     * @param parameter
     * @return
     */
    Map<String, Object> querySecondOrgPersonnel(Map<String, Object> parameter);

    /**
     * 获取第三组织部门的人员到位率和稳定度信息
     *
     * @param parameter
     * @return
     */
    Map<String, Object> queryThirdOrgPersonnel(Map<String, Object> parameter);

    /**
     * 获取第二组织部门的问题闭环率信息
     *
     * @param parameter
     * @return
     */
    Map<String, Object> querySecondOrgIssue(Map<String, Object> parameter);

    /**
     * 获取第三组织部门的问题闭环率信息
     *
     * @param parameter
     * @return
     */
    Map<String, Object> queryThirdOrgIssue(Map<String, Object> parameter);

    /**
     * 更新看板总览表和验收表信息
     *
     * @param groupAcceptance
     */
    void updateGroupOverview(@Param("groupAcc") GroupAcceptanceEntity groupAcceptance);

    /**
     * 获取看板总览表和验收表信息
     *
     * @param statisticalTime
     * @param orgId
     * @return
     */
    GroupAcceptanceEntity queryBoardInfo(@Param("statisticalTime") String statisticalTime, @Param("orgId") String orgId);
}
