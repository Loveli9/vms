package com.icss.mvp.dao;

import com.icss.mvp.entity.*;

import com.icss.mvp.entity.common.request.PageRequest;
import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectInfoVo;

/**
 * @author 安瑜东
 */
public interface ProjectInfoVoDao {
    /**
     * 查询项目总数，用于分页查询的页码总数计算
     * @param map
     * @return
     */
    int projectInfoCount(Map<String, Object> map);

    int projectCountByProjectId(String projectId);

    /**
     * 查询项目列表
     * @param map
     * @return
     */
    List<ProjectInfoVo> projectInfos(Map<String, Object> map);
    List<ProjectInfo> getProjectInfos(Map<String, Object> map);

    /**
     * 查询当前用户可以查看的项目编号
     * @param map
     * @return
     */
    List<String> projectNos(Map<String, Object> map);

    /**
     * 根据项目编号查询项目进度列表
     * @param nos
     * @return
     */
    List<ProjectInfoVo> projectScheduleList(@Param("list") List<String> nos, @Param("name") String name);

    /**
     * 根据proNo查询项目详情
     * @param proNo
     * @return
     */
    ProjectInfoVo fetchProjectInfoByKey(@Param("proNo") String proNo);

    /**
     * 消费者业务线--项目信息查询分页
     * @param map
     * @return
     */
    List<ProjectInfoVo> queryProjectInfos(Map<String, Object> map);

    /**
     * 消费者业务线--统计项目总数
     * @param map
     * @return
     */
    Integer queryProjectInfosCount(Map<String, Object> map);


    /**
     * 消费者业务线--统计部门总数
     * @param map
     * @return
     */
    Integer queryPDUInfosCount(Map<String, Object> map);

    /**
     * 消费者业务线--PDU部门查询
     * @param map
     * @return
     */
    List<String> queryProjectPDUInfos(Map<String, Object> map);

    /**
     * 每个PDU部门每个指标总和
     * @param pduSpdt
     * @param idsList
     * @return
     */
    List<CousumerQuality> getCousumerQualityPDUIndex(@Param("pduSpdt") String pduSpdt,@Param("idsList")List<Integer> idsList);

    /**
     * 每个PDU部门中项目总和
     * @param pduSpdt
     * @return
     */
    Double getCousumerQualityPDUProjectNum(@Param("pduSpdt") String pduSpdt);

    Set<String> projectStatesProblem(Map<String, Object> map);

    List<ProjectInfo> followProjectNos(Map<String, Object> map);

    List<String> collectedProjectNos(Map<String, Object> map);

    Map<String, String> getUserAccounts(@Param("username") String username);

    List<String> getOnsiteNums(@Param("zr") String zr,@Param("hw") String hw,@Param("type") String type);

    List<OnsiteNews> getOnsiteNews(@Param("proNos") String proNos,@Param("zr") String zr,@Param("hw") String hw);

    List<String> getNewsIds(@Param("zh") String zr,@Param("proNo") String proNo,@Param("type") String type);

    void saveReadedNews(@Param("data") List<Map<String, Object>> data);

    List<OnsiteNews> getReadedNews(@Param("zr") String zr);

    /**
     * 查询站内信息
     * @param account
     * @return
     */
    List<StationInformation> getReadedNewsClone(@Param("account") String account);

    /**
     * 保存发送的信息
     * @param data
     * @return
     */
    void saveInformation(@Param("data") StationInformation data);

    /**
     * 更新发送的信息的接收时间
     * @param ids,time
     * @return
     */
    void updateInformation(@Param("ids") String ids,@Param("time") Date time);
}
