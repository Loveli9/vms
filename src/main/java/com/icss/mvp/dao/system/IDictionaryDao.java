package com.icss.mvp.dao.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.system.DictEntryItemEntity;
import com.icss.mvp.entity.system.EntryEntity;
import com.icss.mvp.entity.system.EntryItemEntity;

/**
 * Created by Ray on 2019/3/19.
 *
 * @author Ray
 * @date 2019/3/19 16:08
 */
public interface IDictionaryDao {

    /**
     * 获取指标数据
     *
     * <pre>
     *     entryId：词条编号
     *     entryName：词条名称
     *     entryCode：词条编码
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
    List<EntryEntity> getEntries(Map parameter);

    /**
     * 获取指标数据
     *
     * <pre>
     *     entryId：词条编号
     *     entryName：词条名称
     *     entryCode：词条编码
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
    List<DictEntryItemEntity> getEntryItems(Map parameter);

    /**
     * 新增类目
     * 
     * @param entries
     * @param userId
     */
    void insertEntry(@Param("entries") List<EntryEntity> entries, @Param("userId") String userId);

    /**
     * 更新类目
     * 
     * @param entity
     */
    void updateEntry(Map entity);

    /**
     * 新增条目
     * 
     * @param items
     * @param userId
     */
    void insertEntryItem(@Param("items") List<EntryItemEntity> items, @Param("userId") String userId);

    /**
     * 更新条目
     * 
     * @param entity
     */
    void updateEntryItem(Map entity);

    /**
     * 调整条目顺序
     * 
     * @param items
     */
    void adjustItemOrder(List<EntryItemEntity> items);
}
