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
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.log4j.Logger;

import com.icss.mvp.entity.TblArea;
import com.icss.mvp.service.TblAreaService;

@Controller
@RequestMapping("/tblArea")
public class TblAreaController {
	private static Logger logger = Logger.getLogger(TblAreaController.class);

	@Resource
	private TblAreaService tblAreaService;
	@Resource
    private HttpServletRequest request;
	@Resource
    private UserManagerService userManagerService;
	@Resource
    private SysRoleService sysRoleService;
	
	/**
	 * <p>Title: getAllTblArea</p>  
	 * <p>Description: 获取所有的地域</p>  
	 * @author gaoyao
	 * @return
	 */ 
	@RequestMapping("/getAllTblArea")
	@ResponseBody
	public Map<String, Object> getAllTblArea() {
		Map<String, Object> map = new HashMap<>();
		try {
//            Cookie[] cookies = request.getCookies();
//            String username = "";
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("username")) {
//                    username = cookie.getValue();
//                    break;
//                }
//            }
//            UserInfo userInfo = userManagerService.getUserInfoByName(username);
//            String userRole = userInfo.getRoleId();
//            if (null == userRole || "0".equals(userRole)){
//                map.put("data", null);
//                map.put("msg", "返回成功");
//                map.put("status", "0");
//                return map;
//            }
//
//            SysRole sysRole = sysRoleService.getSysRoleByRoleId(userRole);
//            String roleAuth = sysRole.getRoleAuth();
//            String[] deptArr = roleAuth.split(",");
//            Set<String> codes = new HashSet<>();
//            for (String dept : deptArr) {
//                if (StringUtils.isNotBlank(dept) && dept.startsWith(DeptTreeUtil.AR_DEPT)){
//                    dept = dept.substring(DeptTreeUtil.AR_DEPT.length() + 2);
//                    String[] split = dept.split("/");
//                    codes.addAll(Arrays.asList(split));
//                }
//            }
//            if (CollectionUtilsLocal.isEmpty(codes)){
//                map.put("data", null);
//            }else {
//			    List<TblArea> tblAreas = tblAreaService.getTblAreaByCodes(codes);
//			    map.put("data", tblAreas);
//            }
            List<TblArea> tblAreas = tblAreaService.getTblAreaByCodes(null);
            map.put("data", tblAreas);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("tblAreaService.getTblAreaByCodes exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}
	
	
}
