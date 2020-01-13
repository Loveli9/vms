package com.icss.mvp.dao;

import com.icss.mvp.entity.common.request.PageRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author limingming
 * @date 2019/12/30
 */
public interface IPersonnelTrackDao {
    /**
     * 项目群人员跟踪
     *
     * @param pageRequest
     * @return
     */
    List<Map<String, String>> queryGroupPersonTrack(@Param("page") PageRequest pageRequest);

    /**
     * 项目群人员跟踪count
     *
     * @return
     */
    Integer queryGroupPersonTrackCount();
}
