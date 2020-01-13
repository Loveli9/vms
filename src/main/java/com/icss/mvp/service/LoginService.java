package com.icss.mvp.service;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.constant.Constants;
import com.icss.mvp.dao.ILoginDao;
import com.icss.mvp.dao.OpDepartmentDao;
import com.icss.mvp.entity.VisitRecord;
import com.icss.mvp.entity.prj.EhrUser;
import com.icss.mvp.entity.request.UserRequest;
import com.icss.mvp.entity.response.ExecuteResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.system.UserEntity;
import com.icss.mvp.service.system.UserService;
import com.icss.mvp.util.*;

/**
 * <pre>
 * <b>描述：登录模块service层</b>
 * <b>任务编号：</b>
 * <b>作者：张鹏飞</b> 
 * <b>创建日期： 2017年5月12日 下午2:56:40</b>
 * </pre>
 */
@Service
public class LoginService {

	private static Logger logger = Logger.getLogger(LoginService.class);

	@Autowired
	ILoginDao dao;

	@Autowired
	UserService userService;

	@Autowired
	OpDepartmentDao opDepartmentDao;

	@Autowired
	private Environment env;

	public int getUserCountByRoleId(int roleId) {
		return dao.getUserCountByRoleId(roleId);
	}

	public String getUserOfPer() {
		return dao.getUserOfPer();
	}

	public ExecuteResponse W3Login(String userId, String password) {
		Map<String, Object> params = new HashMap<>();
		params.put("actionFlag", "loginAuthenticate");
		params.put("uid", userId);
		params.put("loginPageType", "mix");
		params.put("password", password);
		params.put("loginMethod", "login");
		params.put("verifyCode", "2345");
		String loginUrl = "https://login.huawei.com/login/login.do";
		return HttpExecuteUtils.encodedHttpPost(loginUrl, params, null);
	}

	public ExecuteResponse ICSLogin(String userId, String password) {
		Map<String, Object> params = new HashMap<>();
		params.put("userid", userId);
		params.put("password", password);
		String loginUrl = "http://ics.chinasoftosg.com/login";
		ExecuteResponse response = HttpExecuteUtils.executeHttpGet(loginUrl, params, null, getProxySetting());
		if (response != null && StringUtils.isNotBlank(response.getHttpEntity())) {
			/**
			 * <pre>
			 *     登录系统失败:LIN106	密码输入错误
			 *     登录系统失败:LIN103	帐号【account】不存在
			 * </pre>
			 */
			Set<String> errorCode = CollectionUtilsLocal.getMatched(response.getHttpEntity(),
                                                                    "(?<=(登录系统失败:))\\S*(?=(\\s))");
			if (errorCode.contains("LIN106")) {
				response.setStatusCode(HttpStatus.SC_UNAUTHORIZED);
				response.setHttpEntity("登录系统失败: 密码输入错误");
			} else if (errorCode.size() > 0) {
				response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
		}
		return response;
	}

	private String[] getProxySetting() {
		if ("1".equals(env.getProperty("is_proxy"))) {
			try {
				String host = env.getProperty("proxy.host");
				String port = env.getProperty("proxy.port");
				String account = env.getProperty("w3.account");
				String secret = AESOperator.getInstance().decrypt(env.getProperty("w3.password"));
				return new String[] { host, port, account, secret };
			} catch (Exception e) {
				logger.error("new String[] exception, error: "+e.getMessage());
			}
		}
		return new String[0];
	}

	public PlainResponse<UserEntity> login(UserEntity user) {
		PlainResponse<UserEntity> result = new PlainResponse<>();
		if (user == null || StringUtils.isBlank(user.getId())) {
			result.setError(CommonResultCode.INVALID_PARAMETER, "userId");
			return result;
		}
		boolean confirmed = false;
		// 用户类型 1:中软 2:华为 0:系统用户
		if (user.getType() == 2) {
			ExecuteResponse response = W3Login(user.getId(), user.getPassword());
			if (response == null || !response.isSucceed()) {
				result.setError(CommonResultCode.UNAVAILABLE);
				logger.error("W3Login failed, code: "
						+ (response == null ? HttpStatus.SC_NOT_FOUND : response.getStatusCode()));
				return result;
			}
			int count = MathUtils.parseIntSmooth(response.getCookieStore().get("login_failLoginCount"));
			confirmed = count == 0;
		} else if (user.getType() == 1) {
			ExecuteResponse response = ICSLogin(user.getId(), user.getPassword());
			int status = response == null ? HttpStatus.SC_SERVICE_UNAVAILABLE : response.getStatusCode();
			if (status == HttpStatus.SC_UNAUTHORIZED) {
				result.setCode(Constants.FAILED);// .setError(CommonResultCode.AUTHENTICATION_EXCEPTION);
				logger.error("ICSLogin failed, account or secret invalid");
				return result;
			}
			if (confirmed = (response != null && response.getCookieStore().containsKey("ROLTPAToken"))) {
				getOrganization(user, response.getCookieStore());
			} else {
				user.setType(0);
			}
		}
		if (user.getType() == 0) {
			UserRequest params = new UserRequest();
			params.setId(user.getId());
			params.setType(user.getType());
			// params.setPassword(StringMD5Util.MD5String(user.getPassword()));
			try{
			params.setPassword(AESOperator.getInstance().encrypt(user.getPassword()));
			}catch (Exception e){
				logger.error("des加密失败：" + e.getMessage());
			}
			UserEntity matched = userService.verifyUser(user.getId(), params.getPassword());
			if (matched != null && StringUtils.isNotBlank(matched.getId())) {
				result.setData(matched);
			}
		} else if (confirmed) {
			user.setPassword(null);
			result = userService.alterUser(user);
		}
		if (result.getData() == null) {
			result.setErrorMessage(Constants.FAILED, Constants.FAILED);
		}
		return result;
	}

	public void getOrganization(UserEntity user, Map<String, String> cookies) {
		// 用户类型 1:中软 2:华为 0:系统用户
		if (user.getType() == 0) {
			return;
		}
		if (user.getType() == 2) {
			return;
		}
		if (user.getType() == 1) {
			if (cookies == null) {
				cookies = ICSLogin(user.getId(), user.getPassword()).getCookieStore();
			}
			String[] settings = getProxySetting();
			String ssoUrl = "http://ics.chinasoftosg.com:8001/sso/login";
			ExecuteResponse response = HttpExecuteUtils.executeHttpGet(ssoUrl, null, cookies, settings);
			/**
			 * <pre>
			 *     调用 /sso/login 失败时，接口仍然返回200，Cookie中也有JSESSIONID，与调用成功没有区别
			 *     除非读取HttpEntity，解析是否包含错误信息，这里暂不做处理，直接继续调用 /person
			 * </pre>
			 */
			String personUrl = "http://ics.chinasoftosg.com:8001/person";
			cookies.put("JSESSIONID", response.getCookieStore().get("JSESSIONID"));
			String person = HttpExecuteUtils.httpGet(personUrl, null, cookies, settings);
			/**
			 * <pre>
			 *     {
			 *          "ehrUser": {
			 *              "lobNumber": "",
			 *              "lastName": "",
			 *              "lob": "项目管理与质量部",
			 *              "budu": "系统工程部",
			 *              "dd": null,
			 *              "email": "@chinasoftinc.com",
			 * </pre>
			 */
			EhrUser ehrUser = JSONUtils.parseSelectedInstance(person, "ehrUser", EhrUser.class);
			if (ehrUser == null || StringUtils.isBlank(ehrUser.getLastName())) {
				return;
			}
			List<Map<String, Object>> opDepartmentsList = opDepartmentDao.getOpDepartmentBylevel("2", new HashSet<>());
			Map<String, Object> opDepartmentsBu = new HashMap<>();
			for (Map<String, Object> opDepartment : opDepartmentsList) {
				opDepartmentsBu.put(StringUtilsLocal.valueOf(opDepartment.get("dept_name")), opDepartment.get("dept_id"));
			}
			opDepartmentsList = opDepartmentDao.getOpDepartmentBylevel("3", new HashSet<>());
			Map<String, Object> opDepartmentsPdu = new HashMap<>();
			for (Map<String, Object> opDepartment : opDepartmentsList) {
				opDepartmentsPdu.put(StringUtilsLocal.valueOf(opDepartment.get("dept_name")), opDepartment.get("dept_id"));
			}
			opDepartmentsList = opDepartmentDao.getOpDepartmentBylevel("4", new HashSet<>());
			Map<String, Object> opDepartmentsDu = new HashMap<>();
			for (Map<String, Object> opDepartment : opDepartmentsList) {
				opDepartmentsDu.put(StringUtilsLocal.valueOf(opDepartment.get("dept_name")), opDepartment.get("dept_id"));
			}
			user.setName(ehrUser.getLastName());
			user.setTrunk(StringUtilsLocal.valueOf(opDepartmentsBu.get(ehrUser.getLob())));
		}
	}

	/**
	 * 保存登录人信息
	 */
	public void saveLoginRecord(UserEntity user, HttpServletRequest request) throws Exception {
		VisitRecord record = new VisitRecord();
		String ip = StringUtilsLocal.getIpAddr(request);
		record.setIp(ip);
		record.setuId(user.getId());
		record.setuName(user.getName());
		record.setuRole(user.getRoleId());
		record.setLoginTime(new Date());
		dao.saveLoginRecord(record);
	}

	/**
	 * 获取访问记录
	 * 
	 * @param result
	 */
	public void getVisitNum(PlainResponse<VisitRecord> result) throws Exception {
		VisitRecord records = dao.getRecords();
		if (null != records) {
			long totalNum = records.getTotalNum();
			long currentNum = records.getCurrentNum();
			records.setTotalNum(totalNum);
			records.setCurrentNum(currentNum);
			long lastTotalNum = records.getLastTotalNum();
			records.setGrowRate(lastTotalNum > 0 ? Math.ceil((currentNum * 1.0 / lastTotalNum) * 100) : 100);
		}
		result.setData(records);
	}
}
