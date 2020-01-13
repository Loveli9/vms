package com.icss.mvp.controller;

import com.icss.mvp.constant.Constants;
import com.icss.mvp.dao.OpDepartmentDao;
import com.icss.mvp.entity.Permission;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.entity.VisitRecord;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.system.UserEntity;
import com.icss.mvp.service.LoginService;
import com.icss.mvp.service.SysRoleService;
import com.icss.mvp.util.DesUtil;
import com.icss.mvp.util.PropertiesUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <pre>
 * <b>描述：登录模块controller</b>
 * <b>任务编号：</b>
 * <b>作者：张鹏飞</b> 
 * <b>创建日期： 2017年5月12日 下午2:55:48</b>
 * </pre>
 */
@RestController
@RequestMapping("/login")
public class LoginController extends BaseController {

	private static Logger logger = Logger.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;
	@Autowired
	SysRoleService sysRoleService;

	private static final String login_key = "DFHSJDFADAS0987";// 用户登陆加密密钥

	@Resource
	private OpDepartmentDao opDepartmentDao;

	@RequestMapping("/dologin")
	@ResponseBody
	public Object login(UserInfo user, HttpServletRequest request) {
		UserEntity entity = new UserEntity();
		entity.setId(user.getUSERNAME());
		try {
			entity.setPassword(DesUtil.decryption(user.getPASSWORD(), login_key));
		} catch (Exception e) {
			logger.error("des解密失败：" + e.getMessage());
		}
		// getISONLINE 登录框，1=华为登录，2=中软登录
		// 用户类型 1:中软 2:华为 0:系统用户
		entity.setType(user.getISONLINE() == 1 ? 2 : 1);
		HttpSession session = request.getSession();
		// 将数据存储到session中
		session.setAttribute("loginer", user.getISONLINE() == 1 ? 2 : 1);
		PlainResponse<UserEntity> loginResponse = loginService.login(entity);
		if (!loginResponse.getSucceed()) {
			return loginResponse.getCode();
		}
		UserEntity currentUser = loginResponse.getData();
		try {
			loginService.saveLoginRecord(currentUser, request);
		} catch (Exception e) {
			logger.error("保存访问记录失败" + e.getMessage());
		}
		// 检查session是否已经存在此用户信息
		Object userIn = request.getSession().getAttribute(Constants.USER_KEY);
		if (null == userIn) {
			request.getSession().setAttribute(Constants.USER_KEY, loginResponse.getData());
		}
		String menus = getShowMenuString(currentUser);
		addCookie("username", user.getUSERNAME().toLowerCase());//工号
		addCookie("realname", currentUser.getName());//姓名
		addCookie("password", user.getPASSWORD());
		addCookie("Menulist", menus);
		addCookie("Currentpername", currentUser.getCurrentPermissionName());//角色
		// 用户类型 1:中软 2:华为 0:系统用户，华为和系统用户都显示为华为
		addCookie("zrOrhwselect", currentUser.getType() == 1 ? "1" : "0");
		return Constants.SUCCESS;
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
			if (StringUtils.isNotBlank(menu)) {
				sbf.append("-").append(menu).append("-").append(",");
			}
		}
		String result = "";
		if (sbf.length() > 0) {
			result = (sbf.substring(0, sbf.length() - 1));
		}

		return result;
	}

	@RequestMapping("/quit")
	@ResponseBody
	@Deprecated
	public String quit() {
		// 清除session
		request.getSession().removeAttribute(Constants.USER_KEY);
		return "quit";
	}

	@RequestMapping("/logout")
	@ResponseBody
	public BaseResponse logout() {
		BaseResponse result = new BaseResponse();
		try {
			String currentUser = getCurrentUser().getId();
			// 清除session
			request.getSession().removeAttribute(Constants.USER_KEY);
			logger.info("logout succeed, userId: " + currentUser);
		} catch (UnsupportedOperationException e) {
			logger.info(e.getMessage());
		}
		return result;
	}

	@RequestMapping("/visit")
	@ResponseBody
	public PlainResponse<VisitRecord> getVisitNum() {
		PlainResponse<VisitRecord> result = new PlainResponse<>();
		try {
			loginService.getVisitNum(result);
		} catch (Exception e) {
			logger.error("loginService.getVisitNum exception, error: "+e.getMessage());
		}
		return result;
	}

	@RequestMapping("/loginEnvironment")
	@ResponseBody
	public PlainResponse<String> loginEnvironment() {
		PlainResponse<String> result = new PlainResponse<>();
		try {
			String type = PropertiesUtil.getApplicationValue("login_environment");
			result.setData(type);
		} catch (Exception e) {
			logger.info(e);
		}
		return result;
	}

}
