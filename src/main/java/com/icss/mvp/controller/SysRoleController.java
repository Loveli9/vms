package com.icss.mvp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.icss.mvp.constant.Constants;
import com.icss.mvp.entity.OpDepartment;
import com.icss.mvp.entity.PerChangleCondition;
import com.icss.mvp.entity.Permission;
import com.icss.mvp.entity.PermissionDetail;
import com.icss.mvp.entity.SysHwdept;
import com.icss.mvp.entity.TblArea;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.system.UserEntity;
import com.icss.mvp.service.*;
import com.icss.mvp.tree.DeptTreeUtil;
import com.icss.mvp.tree.Node;
import com.icss.mvp.tree.NodeTuple;
import com.icss.mvp.tree.PermissionTreeUtil;
import com.icss.mvp.util.MathUtils;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.icss.mvp.entity.SysRole;
import com.icss.mvp.entity.TableSplitResult;

@RestController
@RequestMapping("/sysRole")
public class SysRoleController extends BaseController {
	private static Logger logger = Logger.getLogger(SysRoleController.class);

	@Resource
	HttpServletResponse response;
	@Resource
	private SysRoleService sysRoleService;
	@Resource
	private LoginService loginService;
	@Resource
	private OpDepartmentService opDepartmentService;
	@Resource
	private SysHwdeptService sysHwdeptService;
	@Resource
	private TblAreaService tblAreaService;

	/**
	 * <p>
	 * Title: getAllSysRole
	 * </p>
	 * <p>
	 * Description: 查询所有（测试）
	 * </p>
	 * 
	 * @author gaoyao
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> getAllSysRole(@RequestParam(required = false, name = "roleName") String roleName) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<SysRole> list = sysRoleService.getAllSysRole(roleName);
			map.put("data", list);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("sysRoleService.getAllSysRole exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/perlist")
	@ResponseBody
	public Map<String, Object> getAllpermission(@RequestParam(required = false, name = "perName") String perName) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<Permission> list = sysRoleService.getAllPermission(perName);
			map.put("data", list);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("sysRoleService.getAllPermission exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/queryperlist")
	@ResponseBody
	public Map<String, Object> getUserOfpermission(@RequestParam(required = false, name = "name") String name) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<Permission> list = sysRoleService.getUserOfpermission(name);
			map.put("data", list);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("sysRoleService.getUserOfpermission exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/view")
	@ResponseBody
	public Map<String, Object> view(String roleId) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 中软部门
			List<OpDepartment> opDepartments = opDepartmentService.getEnableDepartment();
			NodeTuple nodeTuple = DeptTreeUtil.builderTree(opDepartments);
			// 华为部门
			List<SysHwdept> sysHwdepts = sysHwdeptService.getAllSysHwdept();
			NodeTuple hwNodeTuple = DeptTreeUtil.builderTree(sysHwdepts);
			// 地域
			List<TblArea> tblAreas = tblAreaService.getAllTblArea();
			NodeTuple tblNodeTuple = DeptTreeUtil.builderTree(tblAreas);
			Node rootNode = DeptTreeUtil.createRootNode();
			List<Node> childrens = rootNode.getChildren();
			if (null == childrens) {
				childrens = new ArrayList<>();
			}
			childrens.add(nodeTuple.getNode());
			childrens.add(hwNodeTuple.getNode());
			childrens.add(tblNodeTuple.getNode());
			SysRole sysRole = sysRoleService.getSysRoleByRoleId(roleId);
			if (StringUtils.isNotBlank(sysRole.getRoleAuth())) {
				rootNode.setChecked(true);
			}
			String[] strings = sysRole.getRoleAuth().split(",");
			Set<String> csSet = new HashSet<>();
			Set<String> hwSet = new HashSet<>();
			Set<String> arSet = new HashSet<>();
			for (String dept : strings) {
				if (dept.startsWith(DeptTreeUtil.HW_DEPT)) {
					dept = dept.substring(DeptTreeUtil.HW_DEPT.length());
					hwSet.addAll(Arrays.asList(dept.split("/")));

				} else if (dept.startsWith(DeptTreeUtil.AR_DEPT)) {
					dept = dept.substring(DeptTreeUtil.AR_DEPT.length());
					arSet.addAll(Arrays.asList(dept.split("/")));
				} else if (dept.startsWith(DeptTreeUtil.CS_DEPT)) {
					dept = dept.substring(DeptTreeUtil.CS_DEPT.length());
					csSet.addAll(Arrays.asList(dept.split("/")));
				}
			}
			this.setChecked(nodeTuple, csSet);
			this.setChecked(hwNodeTuple, hwSet);
			this.setChecked(tblNodeTuple, arSet);
			map.put("data", rootNode);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("sysRoleService.getSysRoleByRoleId exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/perview")
	@ResponseBody
	public Map<String, Object> perview(String permissionid) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 中软部门
			List<PermissionDetail> permissionDetails = opDepartmentService.getpermissionTree();
			NodeTuple nodeTuple = PermissionTreeUtil.builperTree(permissionDetails);
			Node rootNode = DeptTreeUtil.createRootNode();
			List<Node> childrens = rootNode.getChildren();
			if (null == childrens) {
				childrens = new ArrayList<>();
			}
			childrens.add(nodeTuple.getNode());
			Permission permission = sysRoleService.getPermissionByPerId(permissionid);
			if (StringUtils.isNotBlank(permission.getPerAuth())) {
				rootNode.setChecked(true);
			}
			String[] strings = permission.getPerAuth().split(",");
			Set<String> csSet = new HashSet<>();
			for (String dept : strings) {
				if (dept.startsWith(PermissionTreeUtil.CS_DEPT)) {
					dept = dept.substring(PermissionTreeUtil.CS_DEPT.length());
					csSet.addAll(Arrays.asList(dept.split("/")));
				}
			}
			this.setChecked(nodeTuple, csSet);
			map.put("data", rootNode);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("sysRoleService.getPermissionByPerId exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/add")
	@ResponseBody
	public Map<String, Object> add(SysRole sysRole) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (StringUtils.isBlank(sysRole.getRoleAuth())) {
				throw new Exception("角色内容不能为空!");
			}
			int count = sysRoleService.selectCountByRoleName(sysRole.getRoleName(), sysRole.getManageRole());
			if (count > 0) {
				map.put("msg", "该角色名称和描述已存在！");
				map.put("status", "1");
				return map;
			}
			sysRole.setOperateTime(new Date());
			sysRole.setIgnoe(0);
			sysRoleService.insertSysRole(sysRole);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("sysRoleService.insertSysRole exception, error: "+e.getMessage());
			String msg = StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "返回失败";
			map.put("msg", msg);
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/addPer")
	@ResponseBody
	public Map<String, Object> addPermission(Permission per) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (StringUtils.isBlank(per.getPerAuth())) {
				throw new Exception("角色内容不能为空!");
			}
			int count = sysRoleService.selectCountByPerName(per.getPerName(), per.getManagePer());
			if (count > 0) {
				map.put("msg", "该角色名称和描述已存在！");
				map.put("status", "1");
				return map;
			}
			per.setCreattime(new Date());
			per.setUpdator(per.getCreator());
			per.setUpDattime(new Date());
			per.setIgnoe(0);
			sysRoleService.insertPer(per);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("sysRoleService.insertPer exception, error；"+e.getMessage());
			String msg = StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "返回失败";
			map.put("msg", msg);
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Map<String, Object> edit(SysRole sysRole) {
		Map<String, Object> map = new HashMap<>();
		try {
			sysRoleService.updateSysRole(sysRole);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("sysRoleService.updateSysRole exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/perchangle")
	@ResponseBody
	public Map<String, Object> changlePer(PerChangleCondition condition, HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		condition.setNewperid(sysRoleService.queryPernameById(condition.getNewpername()));
		UserEntity user = (UserEntity) session.getAttribute(Constants.USER_KEY);
		try {
			int newId = MathUtils.parseIntSmooth(condition.getNewperid(), -1);
			if (newId != user.getCurrentPermissionId()) {
				user.setCurrentPermissionId(newId);
				String permissionId = String.valueOf(user.getCurrentPermissionId());
				Permission permission = sysRoleService.getPermissionByPerId(permissionId);
				user.setAuth(permission.getPerAuth());
				user.setMenus(getUserMenus(user.getAuth()));
				user.setCurrentPermissionName(permission.getPerName());
				addCookie("Menulist", getShowMenuString(user));
				addCookie("Currentpername", user.getCurrentPermissionName());
				map.put("msg", "权限切换成功！");
				map.put("status", "0");
			} else {
				map.put("msg", "已是当前权限！");
				map.put("status", "2");
			}
		} catch (Exception e) {
			logger.error("sysRoleService.getPermissionByPerId exception, error: "+e.getMessage());
//			map.put("msg", "权限切换异常！");
			map.put("msg", "1");
		}
		return map;
	}

	public String getShowMenuString(UserEntity userInfo) {
		// 多权限用户
		int current = userInfo.getCurrentPermissionId();
		if (current >= 0) {
			// 查询权限范围
			Permission permission = sysRoleService.getPermissionByPerId(String.valueOf(current));
			if (permission != null) {
				userInfo.setCurrentPermissionId(permission.getPermissionid().intValue());
				userInfo.setCurrentPermissionName(permission.getPerName());
				userInfo.setAuth(permission.getPerAuth());
			}
		}
		// 查询菜单
		List<String> menus = getUserMenus(userInfo.getAuth());
		userInfo.setMenus(menus);
		StringBuilder sbf = new StringBuilder();
		for (String menu : menus) {
			sbf.append("-").append(menu).append("-").append(",");
		}
		String result = "";
		if (sbf.length() > 0) {
			result = (sbf.substring(0, sbf.length() - 1));
		}
		return result;
	}

	@RequestMapping("/peredit")
	@ResponseBody
	public Map<String, Object> editPer(Permission permission) {
		Map<String, Object> map = new HashMap<>();
		permission.setUpDattime(new Date());
		try {
			sysRoleService.updatePermission(permission);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("sysRoleService.updatePermission exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/del")
	@ResponseBody
	public Map<String, Object> del(int roleId) {
		Map<String, Object> map = new HashMap<>();
		try {
			int count = loginService.getUserCountByRoleId(roleId);
			if (count > 0) {
				map.put("msg", "该角色下用户不为空，无法进行删除操作！");
				map.put("status", "1");
				return map;
			}
			sysRoleService.deleteSysRoleByRoleId(roleId + "");
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("sysRoleService.deleteSysRoleByRoleId exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/delper")
	@ResponseBody
	public Map<String, Object> delper(Integer perId) {
		Map<String, Object> map = new HashMap<>();
		Set<String> pSet = new HashSet<>();
		try {
			// 查询数据库中用户使用的权限id
			for (String pid : loginService.getUserOfPer().split(",")) {
				pSet.add(pid);
			}
			if (pSet.contains(String.valueOf(perId))) {
				map.put("msg", "该角色下用户不为空，无法进行删除操作！");
				map.put("status", "1");
				return map;
			}
			sysRoleService.delperById(perId);
			map.put("msg", "删除成功！");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("loginService.getUserOfPer exception, error: "+e.getMessage());
			map.put("msg", "删除异常！");
			map.put("status", "1");
		}
		return map;
	}

	private void setChecked(NodeTuple nodeTuple, Set<String> set) {
		Map<String, Node> nodeMap = nodeTuple.getMap();
		for (String id : set) {
			nodeMap.get(id).setChecked(true);
		}
	}

	/**
	 * 新改 角色列表显示 及按照角色名称查询列表显示
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/perlistNew", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public TableSplitResult queryPermission(HttpServletRequest request) {
		String importRoleName = StringUtils.isEmpty(request.getParameter("importRoleName")) ? ""
				: request.getParameter("importRoleName");
		try {
			importRoleName = URLDecoder.decode(importRoleName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("URLDecoder.decode exception, error: "+e.getMessage());
		}
		int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));// 每页显示条数
		int offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));// 第几页
		String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");// 排序字段
		sort = transIntoSqlChar(sort);
		String sortOrder = StringUtils.isEmpty(request.getParameter("sortOrder")) ? ""
				: request.getParameter("sortOrder");// 排序方式
		TableSplitResult page = new TableSplitResult();
		page.setPage(offset);
		page.setRows(limit);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("perName", importRoleName);
		page.setQueryMap(map);
		page = sysRoleService.queryPermission(page, sort, sortOrder);
		return page;
	}

	/**
	 * @Description:将表头字段映射为数据库字段 @param @param sort @param @return 参数 @return
	 *                            String 返回类型 @throws
	 */
	private String transIntoSqlChar(String sort) {
		String sortName = "";
		if ("prior".equals(sort)) {
			sortName = "prior";
		}
		return sortName;
	}

	/**
	 * @Description: 删除角色 @param @return 参数 @return BaseResponse 返回类型 @throws
	 */
	@RequestMapping("/deleteSelections")
	@ResponseBody
	public BaseResponse deleteSelections(@RequestBody String[] ids) {
		BaseResponse result = new BaseResponse();
		try {
			String msg = sysRoleService.deleteSelections(ids);
			result.setCode(msg);
		} catch (Exception e) {
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}

}
