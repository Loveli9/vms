package com.icss.mvp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.CodeCheck;

public interface ICodeCheckDao {

    /**
     * 保存代码检视结果
     * 
     * @param list
     */
    void insertCodeCheck(@Param("list") List<CodeCheck> list);

    /**
     * 获得上次采集的最后一天
     * 
     * @param no
     * @return
     */
    String getLastday(@Param("no") String no);

//    /**
//     * 获得项目启动时间
//     *
//     * @param no
//     * @return
//     */
//    String getStartdate(@Param("no") String no);

    // /**
    // * 获得项目信息
    // *
    // * @return
    // */
    // public List<Map<String, Object>> getProjAndPDU(Map<String, Object> map);

}
