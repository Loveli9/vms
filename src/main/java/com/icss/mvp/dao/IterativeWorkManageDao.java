package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import com.icss.mvp.entity.ProjectInfo;
import org.apache.ibatis.annotations.Param;
import com.icss.mvp.entity.IterativeWorkManage;
import com.icss.mvp.entity.ProjectMembersLocal;
import com.icss.mvp.entity.TableSplitResult;

/**
 * @author chengchenhui
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年8月3日
 */
@SuppressWarnings("all") public interface IterativeWorkManageDao {

    /**
     * @Description: 分页查询需求管理信息 @param @return 参数 @return List<IterativeWorkManage>
     * 返回类型 @throws
     */
    List<IterativeWorkManage> queryIteWorkManagePage(@Param("page") TableSplitResult page, @Param("proNo") String proNo,
                                                     @Param("hwAccount") String hwAccount, @Param("sort") String sort,
                                                     @Param("sortOrder") String sortOrder);

    /**
     * @Description: PM周报导出需求管理信息 @param @return 参数 @return
     * List<IterativeWorkManage> 返回类型 @throws
     */
    List<IterativeWorkManage> queryIteWorkManageInfo(@Param("page") TableSplitResult page, @Param("proNo") String proNo,
                                                     @Param("sort") String sort, @Param("sortOrder") String sortOrder);

    /**
     * @Description: 查询所有需求管理信息 @param @return 参数 @return List<IterativeWorkManage>
     * 返回类型 @throws
     */
    List<IterativeWorkManage> queryIteWorkManageAll(@Param("proNo") String proNo);

    List<IterativeWorkManage> queryIteWorkManageAllNew();

    /**
     * @Description: 新增需求内容 @param @return 参数 @return int 返回类型 @throws
     */
    int addIteWorkManageInfo(@Param("iteInfo") IterativeWorkManage iteInfo);

    /**
     * @param iterativeWorkManage
     * @Description: 编辑信息 @param @return 参数 @return int 返回类型 @throws
     */
    int editIteWorkManageInfo(@Param("itera") IterativeWorkManage iterativeWorkManage);

    /**
     * @Description: 删除信息 @param @return 参数 @return int 返回类型 @throws
     */
    int deleteIteWorkManageInfo(@Param("id") String id);

    /**
     * @Description:查询需求工作总记录 @param @param proNo @param @return 参数 @return Integer
     * 返回类型 @throws
     */
    Integer queryTotalCount(@Param("proNo") String proNo, @Param("hwAccount") String hwAccount,
                            @Param("page") TableSplitResult page);

    /**
     * @Description:根据项目编号和迭代名称查询出迭代id @param @param proNo @param @return 参数 @return
     * Integer 返回类型 @throws
     */
    String queryIteIdByProNoAndIteName(@Param("proNo") String proNo, @Param("iteName") String iteName);

    /**
     * @Description:查询关闭结束的总记录 @param @param iteId @param @return 参数 @return Integer
     * 返回类型 @throws
     */
    Integer queryTotalCountByIteId(@Param("iteId") String iteId);

    /**
     * @Description:查询总记录不包括拒绝的 @param @param iteId @param @return 参数 @return
     * Integer 返回类型 @throws
     */
    Integer queryTotalCountByIteIdNot5(@Param("iteId") String iteId);

    /**
     * @Description:根据结束时间统计数量 @param @param iteId @param @return 参数 @return Integer
     * 返回类型 @throws
     */
    List<Map<String, Object>> queryIteTotalByEndTime(@Param("iteId") String iteId);

    /**
     * @Description:根据结束时间统计使用天数 @param @param iteId @param @return 参数 @return
     * Integer 返回类型 @throws
     */
    Map<String, Object> queryIteDaysByEndTime(@Param("iteId") String iteId);

    /**
     * @Description:统计超期个数 @param @param iteId @param @return 参数 @return Integer
     * 返回类型 @throws
     */
    Map<String, Object> queryIteOverDue(@Param("iteId") String iteId);

    /**
     * 编辑页面回显
     *
     * @param id
     * @return
     */
    IterativeWorkManage openEditPage(@Param("id") String id);

    /**
     * 编辑页面回显
     *
     * @param id
     * @return
     */
    IterativeWorkManage openIteWorkManageInfo(@Param("proNo") String proNo, @Param("topic") String topic);

    /**
     * 获取项目成员下拉列表
     *
     * @param proNo
     * @return
     */
    List<Map<String, Object>> getProjectMebersSelect(@Param("proNo") String proNo);

    /**
     * 获取项目成员下拉列表
     *
     * @param proNo
     * @return
     */
    List<ProjectMembersLocal> getProjectMebers(@Param("proNo") String proNo);

    /**
     * 判断topic相同的值是否存在
     *
     * @param topic
     * @return
     */
    int getTopicCountIteWorkManage(@Param("proNo") String proNo, @Param("topic") String topic);

    /**
     * 导入过程中当topic相同时修改
     *
     * @param iterativeWorkManage
     * @return
     */
    int editIteWorkManageInfoByTopic(@Param("proNo") String proNo,
                                     @Param("itera") IterativeWorkManage iterativeWorkManage);

    /**
     * 根据id获得ite_name(迭代名)
     *
     * @return
     */
    String getIteNameById(@Param("proNo") String proNo, @Param("id") String id);

    /**
     * 根据proNo获得PM(项目经理)
     *
     * @return
     */
    String getPMByProNo(@Param("proNo") String proNo);

    /**
     * 根据proNo,name查询该项目的ZR_ACCOUNT
     */
    String[] getZrAccountByName(@Param("proNo") String proNo, @Param("personLiable") String personLiable);

    /**
     * 根据proNo,iteName查询该项目的iteId
     */
    String getIteIdByIteName(@Param("proNo") String proNo, @Param("iteName") String iteName);

    /**
     * 根据proNo查询是否已存在该项目
     *
     * @return
     */
    int getProjectCount(@Param("proNo") String proNo);

    /**
     * 根据id删除需求管理
     *
     * @return
     */
    int deleteIteWorkManage(@Param("id") String id);

    Integer queryAllCount(@Param("list") List<ProjectInfo> proNo);

    List<IterativeWorkManage> queryAllIteWorkManage(@Param("page") TableSplitResult page, @Param("list") List proNo,
                                                    @Param("sort") String sort, @Param("sortOrder") String sortOrder);

    List<ProjectInfo> queryProNO(@Param("zrAccount") String zrAccount);

    String queryIteName(@Param("id")String id);
}
