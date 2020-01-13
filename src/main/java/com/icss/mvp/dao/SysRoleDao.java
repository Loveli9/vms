package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;
import com.icss.mvp.entity.Permission;
import com.icss.mvp.entity.SysRole;
import com.icss.mvp.entity.TableSplitResult;
import org.apache.ibatis.annotations.Param;

/**
 * 系统角色表表对应的MyBatis SQL XML配置文件的映射接口
 * 
 * @author gaoyao
 * @time 2018-5-29 9:48:07
 */
public interface SysRoleDao {
	/**
	 * 在数据库中新插入一条指定的系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public Integer insertSysRole(SysRole sysRole);

	/**
	 * 在数据库中修改此条系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public Integer updateSysRole(SysRole sysRole);

	/**
	 * 在数据库中通过主键roleId查出指定的系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public SysRole getSysRoleByRoleId(String roleId);

	/**
	 * 在数据库中通过主键roleId删除指定系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public Integer deleteSysRoleByRoleId(String roleId);

	/**
	 * 在数据库中通过主键roleId批量删除指定系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public Integer deleteSysRoleByRoleIdList(List<String> list);

	/**
	 * 在数据库中通过Map中的分页参数（startNo,pageSize） 和其他条件参数得到指定条数的系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public List<SysRole> getSysRoleForPage(Map<String, Object> map);

	/**
	 * 在数据库中通过Map中条件参数得到指定系统角色表记录的总条数
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public Integer getSysRoleCount(Map<String, Object> map);

	/**
	 * 在数据库中查出所有系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public List<SysRole> getAllSysRole(@Param("roleName") String roleName);

	int roleCountByAuth(@Param("authId") int authId);

	int selectCountByRoleName(@Param("roleName") String roleName, @Param("managerRole") String managerRole);

	public int selectCountByPerName(@Param("perName") String perName, @Param("managePer") String managePer);

	public Integer insertPer(Permission per);

	public List<Permission> getAllPermission(@Param("perName") String perName);

	public Permission getPermissionByPerId(String permissionid);

	public Integer updatePermission(Permission permission);

	public Integer delperById(Integer perId);

	/**
	 * 新改 角色列表显示 及按照角色名称查询列表显示
	 * 
	 * @param perName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Permission> queryPermission(@Param("page") TableSplitResult page, @Param("sort") String sort,
			@Param("sortOrder") String sortOrder);

	int deleteSelections(@Param("id") int id);

	@SuppressWarnings("rawtypes")
	Integer queryTotalCount(@Param("page") TableSplitResult page);

}