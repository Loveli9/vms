package com.icss.mvp.dao.system;

import java.util.List;
import java.util.Map;

import com.icss.mvp.entity.PermissionDetail;
import com.icss.mvp.entity.system.AuthorityEntity;

/**
 * Created by Ray on 2019/8/5.
 *
 * @author Ray
 * @date 2019/8/5 15:22
 */
public interface IAuthorityDao {

    /**
     * 获取指标数据
     *
     * <pre>
     *     name：名称
     * 
     *     order：ASC/DESC，排序规则，正序或反序
     *     sort：排序规则，字段或字段组合
     *     offset：分页相关，跳过符合条件的前若干条
     *     limit：分页相关，只获取指定数目的记录
     * 
     *     order by ${order} ${sort}
     *     limit #{offset}, #{limit}
     * </pre>
     *
     * @param parameter 查询参数
     * @return 符合条件的记录
     */
    List<AuthorityEntity> getAuthorities(Map parameter);

    /**
     * 此方法的作用在于新建系统角色
     *
     * @param authority 封装角色信息的实体类
     * @return 返回值无要求
     */
    void insertAuthority(AuthorityEntity authority);
    /**
     * 用于修改已建角色的信息（如：名称、权限范围等）
     * @param authority 传参map集合包含需要修改角色的信息
     */
    void updateAuthority(Map authority);
    /**
     * 工具类用于生成特定角色权限树
     * @return 返回值为包含特定的实体类的集合
     */
    List<PermissionDetail> getPermissionTree();

}
