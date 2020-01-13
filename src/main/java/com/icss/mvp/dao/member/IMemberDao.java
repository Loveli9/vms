package com.icss.mvp.dao.member;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.member.MemberEntity;

/**
 * Created by Ray on 2019/2/15.
 * 
 * @author Ray
 * @date 2019/2/15
 */
public interface IMemberDao {

    /**
     * @return
     */
    List<MemberEntity> getProjectLocalMembers(@Param("projectId") String projectId, @Param("duty") Set<String> duties);

}
