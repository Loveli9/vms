package com.icss.mvp.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.icss.mvp.entity.SysRole;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.service.SysRoleService;
import com.icss.mvp.service.UserManagerService;
import com.icss.mvp.tree.DeptTreeUtil;
import com.icss.mvp.tree.NodeTuple;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icss.mvp.entity.SysHwdept;
import com.icss.mvp.service.SysHwdeptService;

@RestController
@RequestMapping("/sysHwdept")
public class SysHwdeptController {
	private static Logger logger = Logger.getLogger(SysHwdeptController.class);

	@Resource
	private HttpServletRequest request;
	@Resource
	private SysHwdeptService sysHwdeptService;
	@Resource
	private UserManagerService userManagerService;
	@Resource
	private SysRoleService sysRoleService;
	
	/**
	 * <p>Title: getSysHwdeptByPId</p>  
	 * <p>Description: 通过父id查找下面的子节点信息</p>  
	 * @param pId
	 * @author gaoyao
	 * @return
	 */ 
	@RequestMapping("/getSysHwdeptByPId")
	@ResponseBody
	public Map<String, Object> getSysHwdeptByPId(String pId) {
		Map<String, Object> map = new HashMap<>();
		try {
			Cookie[] cookies = request.getCookies();
			String username = "";
			for (Cookie cookie : cookies) {
				if ("username".equals(cookie.getName())) {
					username = cookie.getValue();
					break;
				}
			}
			UserInfo userInfo = userManagerService.getUserInfoByName(username);
			String userRole = userInfo.getRoleId();
			if (null == userRole || "0".equals(userRole)){
				map.put("data", null);
				map.put("msg", "返回成功");
				map.put("status", "0");
				return map;
			}

			SysRole sysRole = sysRoleService.getSysRoleByRoleId(userRole);
			String roleAuth = sysRole.getRoleAuth();
			String[] deptArr = roleAuth.split(",");
			Set<String> deptIds = new HashSet<>();
			for (String dept : deptArr) {
				if (StringUtils.isNotBlank(dept) && dept.startsWith(DeptTreeUtil.HW_DEPT)){
					dept = dept.substring(DeptTreeUtil.HW_DEPT.length() + 2);
					String[] split = dept.split("/");
					deptIds.addAll(Arrays.asList(split));
				}
			}
			if (CollectionUtils.isEmpty(deptIds)) {
				map.put("data", null);
			}else {
				List<SysHwdept> list = sysHwdeptService.getSysHwdeptByPIds(deptIds);
				NodeTuple nodeTuple = DeptTreeUtil.builderTree(list);
				map.put("data", nodeTuple.getNode());
			}

			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("sysHwdeptService.getSysHwdeptByPIds exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}
}
