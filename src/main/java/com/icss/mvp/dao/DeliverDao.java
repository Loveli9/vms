package com.icss.mvp.dao;

import com.icss.mvp.entity.Deliver;
import com.icss.mvp.entity.DeliverResult;
import com.icss.mvp.entity.TableSplitResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangjianbin
 * @date 2019/12/06
 */
public interface DeliverDao {

    /**
     * @param "获取deliver数据"
     * @return List
     */
    List<DeliverResult> queryList(@Param("page") TableSplitResult page, @Param("sort") String sort,
                                  @Param("sortOrder") String sortOrder);

    List<DeliverResult> queryAll(@Param("page") TableSplitResult page, @Param("sort") String sort,
                                 @Param("sortOrder") String sortOrder);

    Integer queryListTotals(@Param("page") TableSplitResult page);

    Integer queryAllTotals(@Param("page") TableSplitResult page);

    /**
     * @param "添加deliver数据"
     * @return List
     */
    void add(Deliver deliver);

    /**
     * @param "添加deliver数据"
     * @return Deliver
     */
    Deliver queryEditPageInfo(@Param("id") String id);

    /**
     * @param "更改deliver数据"
     * @return null
     */
    void update(@Param("deliver") Deliver deliver);

    /**
     * @param "刪除deliver数据"
     * @return null
     */
    void delete(@Param("id") String id);

    String queryAccount(@Param("hwAccount") String hwAccount);

}
