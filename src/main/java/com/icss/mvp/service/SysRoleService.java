package com.icss.mvp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.dao.SysRoleDao;
import com.icss.mvp.entity.Permission;
import com.icss.mvp.entity.SysRole;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.UserInfo;

/**
 * 系统角色表表对应的MyBatis SQL XML配置文件的映射接口
 * 
 * @author gaoyao
 * @time 2018-5-29 9:48:07
 */
@Service("sysRoleService")
@Transactional
public class SysRoleService {

	@Resource
	private IUserManagerDao userdao;
	@Resource
	private SysRoleDao dao;
	@Resource
	private LoginService loginService;

	private static Logger logger = Logger.getLogger(SysRoleService.class);

	/**
	 * 在数据库中新插入一条指定的系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public Integer insertSysRole(SysRole sysRole) {
		return dao.insertSysRole(sysRole);
	}

	/**
	 * 在数据库中修改此条系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public Integer updateSysRole(SysRole sysRole) {
		return dao.updateSysRole(sysRole);
	}

	/**
	 * 在数据库中通过主键roleId查出指定的系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public SysRole getSysRoleByRoleId(String roleId) {
		return dao.getSysRoleByRoleId(roleId);
	}

	/**
	 * 在数据库中通过主键roleId删除指定系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public Integer deleteSysRoleByRoleId(String roleId) {
		return dao.deleteSysRoleByRoleId(roleId);
	}

	/**
	 * 在数据库中通过主键roleId批量删除指定系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public Integer deleteSysRoleByRoleIdList(List<String> list) {
		return dao.deleteSysRoleByRoleIdList(list);
	}

	/**
	 * 在数据库中通过Map中的分页参数（startNo,pageSize） 和其他条件参数得到指定条数的系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public List<SysRole> getSysRoleForPage(Map<String, Object> map) {
		return dao.getSysRoleForPage(map);
	}

	/**
	 * 在数据库中通过Map中条件参数得到指定系统角色表记录的总条数
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public Integer getSysRoleCount(Map<String, Object> map) {
		return dao.getSysRoleCount(map);
	}

	/**
	 * 在数据库中查出所有系统角色表记录
	 * 
	 * @author gaoyao
	 * @time 2018-5-29 9:48:07
	 */
	public List<SysRole> getAllSysRole(String roleName) {
		return dao.getAllSysRole(roleName);
	}

	public int roleCountByAuth(int authId) {
		return dao.roleCountByAuth(authId);
	}

	public int selectCountByRoleName(String roleName, String managerRole) {
		return dao.selectCountByRoleName(roleName, managerRole);
	}

	public int selectCountByPerName(String perName, String managePer) {
		return dao.selectCountByPerName(perName, managePer);
	}

	public Integer insertPer(Permission per) {
		return dao.insertPer(per);
	}

	public List<Permission> getAllPermission(String perName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Permission> allPermission = new ArrayList<Permission>();
		allPermission = dao.getAllPermission(perName);
		for (int i = 0; i < allPermission.size(); i++) {
			allPermission.get(i).setCreatstr(sdf.format(allPermission.get(i).getCreattime()));
		}
		return allPermission;
	}

	public Permission getPermissionByPerId(String permissionid) {
		return dao.getPermissionByPerId(permissionid);
	}

	public Integer updatePermission(Permission permission) {
		return dao.updatePermission(permission);
	}

	public List<Permission> getUserOfpermission(String name) {
		List<Permission> perlist = new ArrayList<Permission>();
		UserInfo user = userdao.getUserInfoByName(name);
		for (String perid : user.getPermissionids().split(",")) {
			Permission permission = dao.getPermissionByPerId(perid);
			perlist.add(permission);
		}
		return perlist;
	}

	public String queryPernameById(String newpername) {
		String perId = String.valueOf(userdao.queryperId(newpername));
		return perId;
	}

	public Integer delperById(Integer perId) {
		return dao.delperById(perId);
	}

	/**
	 * 新改 角色列表显示 及按照角色名称查询列表显示
	 * @param page
	 * @param sort
	 * @param sortOrder
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TableSplitResult queryPermission(TableSplitResult page, String sort, String sortOrder) {
		TableSplitResult data = new TableSplitResult();
		List<Permission> dataList = dao.queryPermission(page, sort, sortOrder);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.size(); i++) {
			dataList.get(i).setCreatstr(sdf.format(dataList.get(i).getCreattime()));
		}
		data.setRows(dataList);
		// 查询总记录条数
		Integer total = dao.queryTotalCount(page);
		data.setTotal(total);
		return data;
	}

	/**
	 * @Description: 删除角色 @param 参数 @return void 返回类型 @throws
	 */
	@SuppressWarnings("unused")
	public String deleteSelections(String[] ids) {
		Set<String> pSet = new HashSet<>();
		String msg = "";
		try {
			// 查询数据库中用户使用的权限id
			for (String pid : loginService.getUserOfPer().split(",")) {
				pSet.add(pid);
			}
			for (int i = 0; i < ids.length; i++) {
				if (pSet.contains(String.valueOf(ids[i]))) {
					msg = "error";
					return msg;
				}
			}
			for (int i = 0; i < ids.length; i++) {
				int id = Integer.parseInt(ids[i]);
				int a = dao.deleteSelections(id);
			}
			msg = "success";
		} catch (Exception e) {
			logger.error("dao.deleteSelections exception, error: "+e.getMessage());
			msg = "删除异常！";

		}
		return msg;
	}

}
