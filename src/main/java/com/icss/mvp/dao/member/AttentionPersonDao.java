package com.icss.mvp.dao.member;

import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.entity.member.AttentionPerson;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: chengchenhui
 * @create: 2019-12-25 17:32
 **/
public interface AttentionPersonDao {
    List<UserInfo> getAttentionInfoByPage(@Param("page") TableSplitResult page);

    void saveAttentionPersonnel(@Param("person") AttentionPerson attentionPerson);

    void removeConcerns(@Param("concern") String username,@Param("concerned") String userId);

    List<String> getRoles(@Param("ids") String ids);
}
