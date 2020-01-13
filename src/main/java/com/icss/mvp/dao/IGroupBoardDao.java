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
}
