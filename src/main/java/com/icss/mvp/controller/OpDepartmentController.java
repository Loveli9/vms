package com.icss.mvp.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.icss.mvp.entity.*;
import com.icss.mvp.service.*;
import com.icss.mvp.tree.DeptTreeUtil;
import com.icss.mvp.tree.Node;
import com.icss.mvp.tree.NodeTuple;
import com.icss.mvp.tree.PermissionTreeUtil;

import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/opDepartment")
@PropertySource(value = "classpath:/application.properties")
public class OpDepartmentController {
	private static Logger logger = Logger.getLogger(OpDepartmentController.class);

    @Resource
    private HttpServletRequest  request;

    @Resource
    private OpDepartmentService opDepartmentService;
    @Resource
    private SysRoleService      sysRoleService;
    @Resource
    private UserManagerService  userManagerService;
    @Resource
    private SysHwdeptService sysHwdeptService;
    @Resource
    private TblAreaService tblAreaService;

    @Value(value = "${default_role_id}")
    private String              default_role_id;

	/**
	 * <p>Title: getSysHwdeptByPId</p>  
	 * <p>Description: 通过父id查找下面的子节点信息</p>  
	 * @param pId
	 * @author gaoyao
	 * @return
	 */ 
	@RequestMapping("/getOpDepartmentByPId")
	@ResponseBody
	public Map<String, Object> getOpDepartmentByPId(String pId) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<OpDepartment> list = opDepartmentService.getOpDepartmentByPId(pId);
			map.put("data", list);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("opDepartmentService.getOpDepartmentByPId exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}
	@RequestMapping("/getOpDepartmentByCurrentUser")
	@ResponseBody
	public Map<String, Object> getOpDepartmentByCurrentUser(){
		Map<String, Object> map = new HashMap<>();
		try {

//            String userRole = default_role_id;
			String userRole = "";
            if (StringUtils.isBlank(userRole)) {
                Cookie[] cookies = request.getCookies();
                String username = "";
                for (Cookie cookie : cookies) {
                    if ("username".equals(cookie.getName())) {
                        username = cookie.getValue();
                        break;
                    }
                }
                UserInfo userInfo = userManagerService.getUserInfoByName(username);
                if (null == userInfo.getRoleId() || "0".equals(userInfo.getRoleId())) {
                    map.put("data", null);
                    map.put("msg", "返回成功");
                    map.put("status", "0");
                    return map;
                }

                userRole = userInfo.getRoleId();
            }

			SysRole sysRole = sysRoleService.getSysRoleByRoleId(userRole);
			String roleAuth = sysRole.getRoleAuth();
			String[] auths = roleAuth.split(",");
			Set<String> deptIds = new HashSet<>();
			for (String auth : auths) {
			    if (StringUtils.isNotBlank(auth) &&  auth.startsWith(DeptTreeUtil.CS_DEPT)){
                    auth = auth.substring(DeptTreeUtil.CS_DEPT.length());
                    String[] split = auth.split("/");
                    deptIds.addAll(Arrays.asList(split));
                }

			}
			if (CollectionUtils.isEmpty(deptIds)){
                map.put("data", null);
            }else {
			    List<OpDepartment> opDeparts= opDepartmentService.getOpDepartmentByPIds(deptIds);
			    NodeTuple nodeTuple = DeptTreeUtil.builderTree(opDeparts);
			    map.put("data", nodeTuple.getNode());
            }
			map.put("msg", "返回成功");
			map.put("status", "0");
		}catch (Exception e){
			logger.error("opDepartmentService.getOpDepartmentByPIds exception, error；"+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}
	@RequestMapping("/getOpDepartment")
	@ResponseBody
	public Map<String, Object> getOpDepartment(String level,String ids){
		Map<String, Object> map = new HashMap<>();
		try {
			String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
			List<Map<String,Object>> opDeparts= opDepartmentService.getOpDepartment(level,ids,username);
			map.put("data", opDeparts);
			map.put("msg", "返回成功");
			map.put("status", "0");
		}catch (Exception e){
			logger.error("opDepartmentService.getOpDepartment exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}
	@RequestMapping("/getOpDepartmentBylevel")
	@ResponseBody
	public Map<String, Object> getOpDepartmentBylevel(String level,String ids){
		Map<String, Object> map = new HashMap<>();
		try {
			Set<String> deptIds = new HashSet<>();
			if(!StringUtilsLocal.isBlank(ids)){
				String[] deIds = ids.split(",");
				for (String id : deIds) {
					deptIds.add(id);
				}
			}
			List<Map<String,Object>> opDeparts= opDepartmentService.getOpDepartmentBylevel(level,deptIds);
			map.put("data", opDeparts);
			map.put("msg", "返回成功");
			map.put("status", "0");
		}catch (Exception e){
			logger.error("opDepartmentService.getOpDepartmentBylevel exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/optTree")
	@ResponseBody
	public Map<String, Object> getTree(){
		Map<String, Object> map = new HashMap<>();
		try {
			List<OpDepartment> opDepartments = opDepartmentService.getEnableDepartment();
			NodeTuple nodeTuple = DeptTreeUtil.builderTree(opDepartments);
            List<SysHwdept> sysHwdepts = sysHwdeptService.getAllSysHwdept();
            NodeTuple hwNodeTuple = DeptTreeUtil.builderTree(sysHwdepts);
            List<TblArea> tblAreas = tblAreaService.getAllTblArea();
            NodeTuple tblAreaTuple = DeptTreeUtil.builderTree(tblAreas);
            Node rootNode = DeptTreeUtil.createRootNode();
            List<Node> nodes = rootNode.getChildren();
            if (null == nodes){
                nodes = new ArrayList<>();
            }
            nodes.add(nodeTuple.getNode());
            nodes.add(hwNodeTuple.getNode());
            nodes.add(tblAreaTuple.getNode());

            map.put("data", rootNode);
			map.put("msg", "返回成功");
			map.put("status", "0");
		}catch (Exception e){
			logger.error("DeptTreeUtil.createRootNode exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}

		return map;
	}
	@RequestMapping("/permissionTree")
	@ResponseBody
	public Map<String, Object> getpermissionTree(){
		Map<String, Object> map = new HashMap<>();
		try {
			List<PermissionDetail> permissionDetails = opDepartmentService.getpermissionTree();
			NodeTuple nodeTuple = PermissionTreeUtil.builperTree(permissionDetails);
//			List<SysHwdept> sysHwdepts = sysHwdeptService.getAllSysHwdept();
//			NodeTuple hwNodeTuple = DeptTreeUtil.builderTree(sysHwdepts);
//			List<TblArea> tblAreas = tblAreaService.getAllTblArea();
//			NodeTuple tblAreaTuple = DeptTreeUtil.builderTree(tblAreas);
			Node rootNode = DeptTreeUtil.createRootNode();
			List<Node> nodes = rootNode.getChildren();
			if (null == nodes){
				nodes = new ArrayList<>();
			}
			nodes.add(nodeTuple.getNode());
//			nodes.add(hwNodeTuple.getNode());
//			nodes.add(tblAreaTuple.getNode());
			
			map.put("data", rootNode);
			map.put("msg", "返回成功");
			map.put("status", "0");
		}catch (Exception e){
			logger.error("DeptTreeUtil.createRootNode exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		
		return map;
	}
	/**
	 * 华为线
	 * @param level
	 * @param ids
	 * @return
	 */
	@RequestMapping("/getHwDepartmentBylevel")
	@ResponseBody
	public Map<String, Object> getHwDepartmentBylevel(String level,String ids){
		Map<String, Object> map = new HashMap<>();
		try {
			Set<String> deptIds = new HashSet<>();
			if(!StringUtilsLocal.isBlank(ids)){
				String[] deIds = ids.split(",");
				for (String id : deIds) {
					deptIds.add(id);
				}
			}
			List<Map<String,Object>> hwDeparts= opDepartmentService.getHwDepartmentBylevel(level,deptIds);
			map.put("data", hwDeparts);
			map.put("msg", "返回成功");
			map.put("status", "0");
		}catch (Exception e){
			logger.error("opDepartmentService.getHwDepartmentBylevel exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

}
