package com.icss.mvp.dao.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.system.UserEntity;

/**
 * @author Ray on 2018/11/30.
 */
public interface IUserDao {
    /**
     * 获取当前系统登录人信息的方法
     * @param user 存储有关当前登录用户信息的map集合
     * @return 返回包含当前登录人信息实体类的list集合
     */
    List<UserEntity> getUserInfo(@Param("user") Map user);
    /**
     * 修改（完善）当前系统登录人的信息
     * @param user 封装当前登录人信息的实体类
     */
    void replaceUserInfo(@Param("user") UserEntity user);
    /**
     * 修改登录人信息
     * @param user 封装当前登录人信息的实体类
     */
    void alertUserInfo(@Param("user") Map user);
}
