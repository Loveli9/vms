package com.icss.mvp.dao.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.system.DepartmentEntity;

/**
 * @author  Ray on 2018/7/27.
 */
public interface IDepartmentDao {

    /**
     * 获取部门列表，数据库中仅回溯向上两级的三层结构
     * 
     * @param parameter
     * @return
     */
    List<DepartmentEntity> queryDepartments(Map<String, Object> parameter);

    /**
     * 获取指定部门的下级部门列表
     * 
     * @param departmentEntity 封装指定部门信息的实体类
     * @return 存储包含指定部门信息实体类的list集合
     */
    @Deprecated
    List<DepartmentEntity> getSublevels(DepartmentEntity departmentEntity);
    /**
     * 有条件查询华为组织结构方法
     * @param parameter 为封装华为组织结构信息的map集合
     * @return 返回值为便于系统解析的包含华为组织结构实体类的list集合
     */
    List<DepartmentEntity> queryHwDepts(Map<String, Object> parameter);
    /**
     * 有条件查询中软组织结构方法
     * @param parameter 为封装中软组织结构信息的map集合
     * @return 返回值为便于系统解析的包含华为中软结构实体类的list集合
     */
    List<DepartmentEntity> queryOpDepts(Map<String, Object> parameter);
    @Deprecated
    /**
     * 查询部门信息
     * @param  departmentEntity 封装指定部门信息的实体类
     * @return  包含封装指定部门信息实体类的list集合
     */
    List<DepartmentEntity> readDepartment(DepartmentEntity departmentEntity);
    @Deprecated
    /**
     * 查询部门信息
     * @param  departmentEntity 封装指定部门信息的实体类
     * @return  包含封装指定部门信息实体类的list集合
     */
    List<DepartmentEntity> readDepartmentCopy(DepartmentEntity departmentEntity);
    /**
     * 查询处于启用状态中软组织结构的方法
     * @return 返回值为便于系统解析的包含中软结构实体类的list集合
     */
    List<DepartmentEntity> readOpCopy();
    @Deprecated
    /**
     * 查询中软组织结构信息
     * @param departmentEntity 封装中软结构组织信息的实体类
     * @return  返回包含封装包含中软组织结构信息实体类的list集合
     */
    List<DepartmentEntity> queryOpDepartments(DepartmentEntity departmentEntity);

    /**
     * 批量新增中软组织结构信息方法
     *
     * @param departmentEntities  为封装新增中软组织结构信息的list集合
     */
    void increaseOpDepartments(List<DepartmentEntity> departmentEntities);

    /**
     * 批量展示/隐藏中软结构组织信息
     *
     * @param modifyTime 中软组织结构修改时间
     * @param deptCodes 包含组织结构id信息的list 集合 （集合中包含的组织结构信息的启用状态将不会被修改）
     * @param  level 中软组织结构的等级信息
     */
    void switchOpDepartments(@Param("level") int level, @Param("deptIds") List<String> deptCodes,
                             @Param("modifyTime") Date modifyTime);
    /**
     *查询处于启用状态华为组织结构的方法
     * @return 返回值为便于系统解析的包含中软结构实体类的list集合
     */
    List<DepartmentEntity> readHwCopy();
    @Deprecated
    /**
     * 查询华为组织结构信息
     * @param  departmentEntity 封装华为组织结构信息的实体类
     * @return  包含封装华为组织结构信息实体类的list集合
     */
    List<DepartmentEntity> queryHwDepartments(DepartmentEntity departmentEntity);

    /**
     * 批量新增华为组织结构信息方法
     *
     * @param departmentEntities 为封装新增华为组织结构信息的list集合
     */
    void increaseHwDepartments(List<DepartmentEntity> departmentEntities);

    /**
     * 批量展示/隐藏华为结构组织信息
     *
     * @param modifyTime  华为组织结构修改时间
     * @param deptCodes 包含组织结构id信息的list 集合 （集合中包含的组织结构信息的启用状态将不会被修改）
     * @param  level  华为组织结构的等级信息
     */
    void switchHwDepartments(@Param("level") int level, @Param("deptIds") List<String> deptCodes,
                             @Param("modifyTime") Date modifyTime);

}
