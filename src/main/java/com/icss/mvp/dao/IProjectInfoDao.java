package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectBoard;
import com.icss.mvp.entity.ProjectInfo;

/**
 * Created by Ray on 2018/9/13.
 */
public interface IProjectInfoDao {

    /**
     * 根据条件查询项目
     * 
     * <pre>
     *     client：组织机构类型，1：中软，0：华为，默认为0
     *     name：项目名称，模糊匹配
     *     manager：项目经理名称，模糊匹配
     *     areas：地域列表，areaCode，用户可见地域，受用户权限影响
     *     depts：部门列表，deptId，用户可见部门，受用户权限影响
     *     status：项目状态，在行/关闭，支持多状态
     *     offset：分页相关，跳过符合条件的前若干条
     *     limit：分页相关，只获取指定数目的记录
     *     sort：排序规则，字段或字段组合
     *     order：ASC/DESC，排序规则，正序或反序
     * </pre>
     *
     * @param parameter 查询参数
     * @return 符合条件的记录
     */
    List<ProjectInfo> queryByClient(Map<String, Object> parameter);

    /**
     * 获取符合查询条件的记录总数
     * 
     * @param parameter 查询条件
     * @return 符合条件的记录总数
     */
    int countByClient(Map<String, Object> parameter);

    /**
     * 获取项目的星级评估
     * 
     * @param parameter 查询条件
     * @return
     */
    List<ProjectInfo> queryStarRating(Map<String, Object> parameter);
    
    /**
     * 查询用户关注的项目信息
     * @param userID
     * @return
     */
    List<ProjectInfo> queryFavoriteProject(String userID);
    
    /**
     * 添加关注项目
     * @param proNo
     * @return
     */
    Integer addFavoriteProject(@Param("proNo")String proNo, @Param("userID")String userID);
    
    /**
     * 取消关注项目
     * @param proNo
     * @return
     */
    Integer deleteFavoriteProject(@Param("proNo")String proNo, @Param("userID")String userID);
    
    List<Map<String,Object>> getSubNoByBu(String Bu);
    
    List<Map<String,Object>> getSubNoByPdu(String Pdu);
    
    List<Map<String,Object>> getSubNoByHwpdu(String Hwpdu);
    
    List<Map<String,Object>> getSubNoByHwzpdu(String Hwzpdu);
    
    List<Map<String,Object>> getNoByDu(String Du);
    List<Map<String,Object>> getNoBySpdt(String Pduspdt);

    String getProjectNameByNo(String noId);
    String getProjectNoByName(String name);
    /**
     * 根据条件查询项目
     *
     * <pre>
     *     name：项目名称，模糊匹配
     *     manager：项目经理名称，模糊匹配
     *     status：项目状态，在行/关闭，支持多状态
     *     下面字段为id列表
     *     areas：地域列表，用户可见地域，受用户权限影响
     *     hwpduId：华为产品线列表，用户可见部门，受用户权限影响
     *     hwzpduId：华为子产品线列表，用户可见部门，受用户权限影响
     *     pduSpdtId：PDU/SPDT列表，用户可见部门，受用户权限影响
     *     buId：业务线列表，用户可见部门，受用户权限影响
     *     pduId：事业部列表，用户可见部门，受用户权限影响
     *     duId：交付部列表，用户可见部门，受用户权限影响
     *
     *     offset：分页相关，跳过符合条件的前若干条
     *     limit：分页相关，只获取指定数目的记录
     *     sort：排序规则，字段或字段组合
     *     order：ASC/DESC，排序规则，正序或反序
     * </pre>
     *
     * @param parameter 查询参数
     * @return 符合条件的记录
     */
    List<ProjectInfo> queryProject(Map<String, Object> parameter);
    
    List<ProjectInfo> queryProjectInfo(Map<String, Object> parameter);
    /**
     * 获取符合查询条件的记录总数
     *
     * @param parameter 查询条件
     * @return 符合条件的记录总数
     */
    int countProject(Map<String, Object> parameter);

    /**
     *
     * @param
     * @param
     * @return
     */
    List<ProjectInfo> getProjectByPo(@Param("proName") String proName);

	List<String> getNO(Map<String, Object> map);

    String getPdu(Map<String, Object> bm);

	Object getState(@Param("date")String dateend,@Param("proNo")String proNo);

	List<String> getNo1(@Param("no")List<String> no);

	List<Map<String, String>> getPduspt(Map<String, Object> map);

	List<String> getNOByPduspt(Map<String, Object> map);

	List<String> getNoPD(@Param("pduspdt")String pduspdt);

	String getPduspt1(@Param("pduspdt")String pduspdt);

	ProjectBoard getStateNumber(@Param("date")String lastday, @Param("no")List<String> nop);

	ProjectBoard getTwoWeekState(@Param("month")String lastday, @Param("no")List<String> no,@Param("month1")String midday);

	int getYellow(@Param("month")String midDay,@Param("no") List<String> no,@Param("month1")String midday);

	List<ProjectInfo> queryEffectiveProjects();

	Map<String, Object> getProjectsInline();

	Map<String, Object> getReleasePro(@Param("date")String date);

	Integer queryDoneProjects(@Param("courrent")String courrent);

	Integer getTotalProNums(Map<String, Object> parameter);

	String getNoByName(@Param("name") String name);

	List<Map<String,Object>>  checkboxOnclickData(@Param("proNos") String proNos,@Param("month") String month);

	String getNameByNo(@Param("no") String no);
}
