package com.icss.mvp.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.icss.mvp.entity.PersonnelInfo;
import com.icss.mvp.entity.ProjectKeyroles;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.member.MemberEntity;
import com.icss.mvp.entity.system.UserEntity;
import com.icss.mvp.service.GeneralSituationService;
import com.icss.mvp.util.AESOperator;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringUtilsLocal;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icss.mvp.constant.Constants;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ProjectMembersLocal;
import com.icss.mvp.entity.ProjectMembersLocalsList;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.TeamMembers;
import com.icss.mvp.entity.UserDetailInfo;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.ProjectLableService;
import com.icss.mvp.service.SysRoleService;
import com.icss.mvp.service.UserManagerService;

@RestController
@RequestMapping("/user")
public class UserManagerController {
	private static Logger logger = Logger.getLogger(UserManagerController.class);
	
	@Resource
	private HttpServletRequest request;

	@Resource
	private UserManagerService userManagerService;
	
	@Resource
	private SysRoleService sysRoleService;
	
	@Autowired
	private ProjectLableService service;




	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryUserListInfo(UserInfo userInfo, Page page) {
		String userName = request.getParameter("username");
		userInfo.setUSERNAME(userName);
		page.setSort("CREATETIME");
		page.setOrder("DESC");
		return userManagerService.queryUserInfo(userInfo, page);
	}
	@RequestMapping("/getZongrenliData")
	@ResponseBody
	public PlainResponse<PersonnelInfo> queryUser(String userid) {
		PlainResponse<PersonnelInfo> result = new PlainResponse<>();
		try{
			result.setData(userManagerService.getZongrenliData(userid ));
			result.setCode("success");
		}catch (Exception e){
			logger.error("select renyuaninfo fail:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}


		return result;
	}

	/*
	* 项目中人力查询
	*/
	@RequestMapping("/getPersonnel")
	@ResponseBody
	public PlainResponse<PersonnelInfo> getPersonnel(String proNo) {
		PlainResponse<PersonnelInfo> result = new PlainResponse<>();
		try{
			result.setData(userManagerService.getPersonnel(proNo ));
			result.setCode("success");
		}catch (Exception e){
			logger.error("select renyuaninfo fail:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}


		return result;
	}

	@RequestMapping("/insertInfo")
	@ResponseBody
	public Map<String, Object> insertInfo(HttpServletRequest request)
			throws ParseException {
		UserDetailInfo userDetailInfo = new UserDetailInfo();
		userDetailInfo.setUserName(request.getParameter("userName"));
	    String pwd = userManagerService.MD5Password(request.getParameter("password").trim());
		userDetailInfo.setPassword(pwd);
		String repwd = userManagerService.MD5Password(request.getParameter("repassword").trim());
		userDetailInfo.setRepassword(repwd);
		userDetailInfo.setUserId(request.getParameter("userName"));
		setCreater(userDetailInfo);
		// userDetailInfo.setIdentity(request.getParameter("identity"));
		userDetailInfo.setCreateData(getCreateDate());
		userDetailInfo.setParma(request.getParameter("parma"));
		return userManagerService.insertInfo(userDetailInfo);
	}
	
	@RequestMapping("/addUserInfo")
	@ResponseBody
	public Map<String, Object> addInfo(HttpServletRequest request)
			throws ParseException {
		Map<String, Object> message = new HashMap<String, Object>();
		Map<String, Object> status = new HashMap<String, Object>();
		String userType = request.getParameter("userType")== null?"":request.getParameter("userType");

        UserDetailInfo userDetailInfo = new UserDetailInfo();
        userDetailInfo.setUserId(request.getParameter("userId"));
        userDetailInfo.setUserName(request.getParameter("userName"));
		String pwd;
        try{
	      pwd = AESOperator.getInstance().encrypt(request.getParameter("password").trim());
	      userDetailInfo.setPassword(pwd);
        }catch (Exception e){
			logger.error("des加密失败：" + e.getMessage());
		}
		userDetailInfo.setUsertype(userType);

	/**	String buSearch = request.getParameter("buSearch");
		String duSearch = request.getParameter("duSearch");
		String deptSearch = request.getParameter("deptSearch");

		if(!StringUtilsLocal.isBlank(deptSearch)){
			userDetailInfo.setDept(deptSearch);
		}
		if(!StringUtilsLocal.isBlank(duSearch)){
			userDetailInfo.setDu(duSearch);
		}
		if(!StringUtilsLocal.isBlank(buSearch)){
			userDetailInfo.setBu(buSearch);
		}
		String hwpduSearch = request.getParameter("hwpduSearch");
		String hwzpduSearch = request.getParameter("hwzpduSearch");
		String pduspdtSearch = request.getParameter("pduspdtSearch");

		if(!StringUtilsLocal.isBlank(pduspdtSearch)){
			userDetailInfo.setPduspdt(pduspdtSearch);
		}
		if(!StringUtilsLocal.isBlank(hwzpduSearch)){
			userDetailInfo.setHwzpdu(hwzpduSearch);
		}
		if(!StringUtilsLocal.isBlank(hwpduSearch)){
			userDetailInfo.setHwpdu(hwpduSearch);
		}*/

		String permissonName = request.getParameter("permissionName").trim();
		String[] permissonNames = permissonName.split(",");
		StringBuffer sbf = new StringBuffer();
		for (String perstr : permissonNames) {
			String pidstr =String.valueOf(userManagerService.queryperId(perstr));
			sbf.append(pidstr).append(",");
		}
		String permissionids = null;
		if (sbf.length()>0) {
			permissionids = (sbf.substring(0, sbf.length()-1)).toString();
		}
//		Integer permissionid = userManagerService.queryperId(permissonName);
		
		userDetailInfo.setPermissionids(permissionids);
//		userDetailInfo.setRoleId(roleId);
//		userDetailInfo.setUserId(request.getParameter("userName"));
		setCreater(userDetailInfo);
		// userDetailInfo.setIdentity(request.getParameter("identity"));
		userDetailInfo.setCreateData(getCreateDate());
		userDetailInfo.setParma(request.getParameter("parma"));
		message = userManagerService.insertInfo(userDetailInfo);
		if ("用户名已存在".equals(message.get("message"))) {
			status.put("status", 1);
		}
		if ("保存成功".equals(message.get("message"))) {
			status.put("status", 0);
		}
		if ("保存失败".equals(message.get("message"))) {
			status.put("status", 2);
		}
		return status;
	}

	private UserEntity getLoginUserInfo() {
		return (UserEntity) request.getSession().getAttribute(Constants.USER_KEY);
	}

	private void setCreater(UserDetailInfo userDetailInfo) {
		if (null != getLoginUserInfo()) {
			userDetailInfo.setCreater(getLoginUserInfo().getId());
		} else {
			userDetailInfo.setCreater("");
		}
	}

	private Date getCreateDate() throws ParseException {
		Calendar currTime = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String cdate = df.format(currTime.getTime());
		Date now = null;
		if (cdate != "") {
			now = df.parse(cdate);
		}
		return now;
	}

	@RequestMapping("/add")
	public String addUserInfo(String parma) {
		request.setAttribute("parma", parma);
		return "/userInfo/userInfo";
	}

	@RequestMapping("/update")
	public String updateUserList(String username, Page page) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUSERNAME(username);
		UserInfo userInfos = userManagerService
				.isExistsUserInfo(userInfo, page);
		request.setAttribute("user", userInfos);
		return "/userInfo/userInfo";
	}

	@RequestMapping("/updateUser")
	@ResponseBody
	public Map<String, Object> updateUser(String username, String password) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUSERNAME(username);
		userInfo.setPASSWORD(userManagerService.MD5Password(password));
		Map<String, Object> result = new HashMap<String, Object>();
		if (null != getLoginUserInfo()) {
			return result = userManagerService.updateUser(userInfo);
		}
		return result;
	}

	@RequestMapping("/editRole")
	@ResponseBody
	public Map<String, Object> editRole(String userId, String roleId) {
		Map<String, Object> map = new HashMap<>();
		try{
			userManagerService.editRole(userId, roleId);
			map.put("msg", "返回成功");
			map.put("status", "0");
		}catch (Exception e ){
			logger.error("userManagerService.editRole exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}
	@RequestMapping("/updateper")
	@ResponseBody
	public Map<String, Object> updateper(String userId, String permissionames) {
		Map<String, Object> map = new HashMap<>();
		StringBuffer sbf = new StringBuffer();
		String perids = null;
		try{
			for (String pername :permissionames.split(",")) {
				sysRoleService.queryPernameById(pername);
				sbf.append((sysRoleService.queryPernameById(pername))).append(",");
			}
			if (sbf.length()>0) {
				perids = (sbf.substring(0, sbf.length()-1)).toString();
			}
			userManagerService.editper(userId, perids);
			map.put("msg", "返回成功");
			map.put("status", "0");
		}catch (Exception e ){
			logger.error("userManagerService.editper exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	/*@RequestMapping("/delete")
	@ResponseBody
	public int deleteUser(UserInfo user) {
		return userManagerService.deleteUser(user);
	}*/
	
	@RequestMapping("/delete")
	@ResponseBody
	public BaseResponse deleteUser(UserInfo user) {
		BaseResponse result = new BaseResponse();
		try {
			userManagerService.deleteUser(user);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}

	@RequestMapping("/users")
	public Object getUser() {
		return "/userList/userList";
	}
	
	/**
	 * 用户管理，查询OMP用户
	 * @param projNo
	 * @return
	 */
	@RequestMapping("/getOMPUser")
	@ResponseBody
	public List<Map<String, Object>> getOMPUser(String projNo){
		return userManagerService.getOMPUser(projNo);
	}
	/**
	 * 用户管理，查询OMP用户
	 * @param
	 * @return
	 */
	@RequestMapping("/getOMPUserByAuthor")
	@ResponseBody
	public Map<String, Object> getOMPUserByAuthor(String userid,String author){
		return userManagerService.getOMPUserByAuthor(userid,author);
	}
	/**
	 * 用户管理，查询选中用户
	 * @param projNo
	 * @return
	 */
	/*@RequestMapping("/getSelectedUser")
	@ResponseBody
	public List<Map<String, Object>> getSelectedUser(String projNo){
		return userManagerService.getSelectedUser(projNo);
	}*/
	/**
	 * 用户管理，查询选中用户
	 * @param membersLocals
	 * @return
	 */
//	@RequestMapping("/getSelectedUser")
//	@ResponseBody
//	public List<Map<String, Object>> getSelectedUser(String projNo, PageRequest pager){
//		return userManagerService.getSelectedUser(projNo);
//	}
//	/**
//	 * 用户管理，添加选中用户
//	 * @param membersLocals
//	 * @return
//	 */
	@RequestMapping("/selectedUserAdd")
	@ResponseBody
	public Map<String, Object> selectedUserAdd(@RequestBody ProjectMembersLocalsList membersLocals){

		return userManagerService.selectedUserAdd(membersLocals.getMembersLocals());
	}

	@RequestMapping(value = "/Membersadd",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public Map<String, Object> membersAdd( HttpServletRequest request){
        ProjectMembersLocal membersLocals = new ProjectMembersLocal();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		String userid = request.getParameter("userid")== null?"":request.getParameter("userid");
        String name = request.getParameter("name")== null?"":request.getParameter("name");
		String zrAccount = request.getParameter("zrAccount")== null?"":request.getParameter("zrAccount");
		String hwAccount = request.getParameter("hwAccount")== null?"":request.getParameter("hwAccount");
		String reprname = request.getParameter("reprname")== null?"":request.getParameter("reprname");
		String role =  request.getParameter("role")== null?"":request.getParameter("role");
		String rank =  request.getParameter("rank")== null?"":request.getParameter("rank");
		String startDate = request.getParameter("startDate")== null?"":request.getParameter("startDate");
		String endDate = request.getParameter("endDate")== null?"":request.getParameter("endDate");
		String svnGitNo = request.getParameter("svnGitNo")== null?"":request.getParameter("svnGitNo");
		String status = request.getParameter("status")== null?"":request.getParameter("status");
		membersLocals.setUserid(userid);
		membersLocals.setName(name);
		membersLocals.setZrAccount(zrAccount);
		membersLocals.setHwAccount(hwAccount);
		membersLocals.setReprname(reprname);
		membersLocals.setRole(role);
		membersLocals.setRank(rank);
		try{
			membersLocals.setStartDate(DateUtils.parseDate(startDate,"yyyy-MM-dd"));
			membersLocals.setEndDate(DateUtils.parseDate(endDate,"yyyy-MM-dd"));
		}catch (Exception e){
			logger.error("日期格式转化异常:"+ e.getMessage());
		}
		membersLocals.setSvnGitNo(svnGitNo);
		membersLocals.setStatus(status);
		return userManagerService.membersAdd(membersLocals);
	}

	/*
	* 项目中团队成员添加保存
	*/
	@RequestMapping(value = "/projectMembersAdd",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public Map<String, Object> projectMembersAdd( HttpServletRequest request){
        ProjectMembersLocal membersLocals = new ProjectMembersLocal();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        String name = request.getParameter("name")== null?"":request.getParameter("name");
		String zrAccount = request.getParameter("zrAccount")== null?"":request.getParameter("zrAccount");
		String hwAccount = request.getParameter("hwAccount")== null?"":request.getParameter("hwAccount");
		String proNo = request.getParameter("proNo")== null?"":request.getParameter("proNo");
		String role =  request.getParameter("role")== null?"":request.getParameter("role");
		String rank =  request.getParameter("rank")== null?"":request.getParameter("rank");
		String startDate = request.getParameter("startDate")== null?"":request.getParameter("startDate");
		String endDate = request.getParameter("endDate")== null?"":request.getParameter("endDate");
		String svnGitNo = request.getParameter("svnGitNo")== null?"":request.getParameter("svnGitNo");
		String status = request.getParameter("status")== null?"":request.getParameter("status");
		membersLocals.setName(name);
		membersLocals.setZrAccount(zrAccount);
		membersLocals.setHwAccount(hwAccount);
		membersLocals.setNo(proNo);
		membersLocals.setRole(role);
		membersLocals.setRank(rank);
		try{
			membersLocals.setStartDate(DateUtils.parseDate(startDate,"yyyy-MM-dd"));
			membersLocals.setEndDate(DateUtils.parseDate(endDate,"yyyy-MM-dd"));
		}catch (Exception e){
			logger.error("日期格式转化异常:"+ e.getMessage());
		}
		membersLocals.setSvnGitNo(svnGitNo);
		membersLocals.setStatus(status);
		return userManagerService.projectMembersAdd(membersLocals);
	}

	@RequestMapping(value = "/editPages")
	@ResponseBody
	public Map<String, Object> editPages(String no ,String userid, String zrAccount) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ProjectMembersLocal membersLocal = userManagerService.backforuserDisplay(no,userid, zrAccount);
			map.put("code", "success");
			map.put("rows", membersLocal);
		} catch (Exception e) {
			map.put("code", "failure");
			logger.error(e.getMessage(), e);
		}
		return map;
	}

	/*
	 * 项目中团队成员修改回显
	 */
	@RequestMapping(value = "/editProjectPages")
	@ResponseBody
	public Map<String, Object> editProjectPages(String no , String zrAccount) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ProjectMembersLocal membersLocal = userManagerService.editProjectPages(no, zrAccount);
			map.put("code", "success");
			map.put("rows", membersLocal);
		} catch (Exception e) {
			map.put("code", "failure");
			logger.error(e.getMessage(), e);
		}
		return map;
	}

	/*
	* 项目中团队成员修改保存
	*/
	@RequestMapping(value = "/updateProjectMembers", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse updateProjectMembers(ProjectMembersLocal membersLocal){
		BaseResponse result = new BaseResponse();
		try {
			userManagerService.updateProjectMembers(membersLocal);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}

	@RequestMapping(value = "/saveedit", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse updatemembersLocal(ProjectMembersLocal membersLocal){
		BaseResponse result = new BaseResponse();
		try {
			userManagerService.updatemembersLocal(membersLocal);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}

	@RequestMapping("/membersdelete")
	@ResponseBody
	public BaseResponse deletePromembers(String[] nos, String[] zrAccounts) {
		BaseResponse result = new BaseResponse();
		try {
			userManagerService.deletePromembers(nos, zrAccounts);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("删除失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}

	@RequestMapping("/membersdeleteone")
	@ResponseBody
	public BaseResponse deletePromember(String no, String zrAccount) {
		BaseResponse result = new BaseResponse();
		try {
			userManagerService.deletePromember(no, zrAccount);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("删除失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	/**
	 * 用户管理，删除选中用户
	 * @param projNo
	 * @param author
	 * @return
	 */
	@RequestMapping("/selectedUserDel")
	@ResponseBody
	public Map<String, Object> selectedUserDel(String projNo,String author){
		return userManagerService.selectedUserDel(projNo,author);
	}
	
	//分页查询
	 @RequestMapping(value = "/queryList",method = RequestMethod.POST,consumes = "application/json")
	 @ResponseBody
	 public TableSplitResult query(HttpServletRequest request){
		    String usertype = request.getParameter("userType")== null?"":request.getParameter("userType");
	    	String lobline = request.getParameter("lobline")== null?"":request.getParameter("lobline");
	    	String lobdept =request.getParameter("lobdept")== null?"":request.getParameter("lobdept");
	    	String lobdepment = request.getParameter("lobdepment")== null?"":request.getParameter("lobdepment");
	    	String hwpdu = request.getParameter("hwpdu")== null?"":request.getParameter("hwpdu");
	    	String hwzpdu =request.getParameter("hwzpdu")== null?"":request.getParameter("hwzpdu");
	    	String pduspdt = request.getParameter("pduspdt")== null?"":request.getParameter("pduspdt");
	    	String lobname = request.getParameter("lobname")== null?"":request.getParameter("lobname");
	    	String lobrole = request.getParameter("lobrole")== null?"":request.getParameter("lobrole");
			try {
				lobname = new String(lobname.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("转码异常",e);
			}
	    	int limit = request.getParameter("limit") == null?10:Integer.parseInt(request.getParameter("limit"));//每页显示条数
	    	int offset = request.getParameter("offset") == null?0:Integer.parseInt(request.getParameter("offset"));//第几页
	    	String proNo = request.getParameter("proNo");
	        TableSplitResult page = new TableSplitResult();
	        page.setPage(offset);
	        page.setRows(limit);
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("lobline", lobline);
	        map.put("lobdept", lobdept);
	        map.put("lobdepment", lobdepment);
	        map.put("hwpdu", hwpdu);
	        map.put("hwzpdu", hwzpdu);
	        map.put("pduspdt", pduspdt);
	        map.put("lobname", lobname);
	        map.put("lobrole", lobrole);
	        map.put("usertype", usertype);
	        page.setQueryMap(map);
	        page = userManagerService.queryIteInfoByPage(page,proNo);
	        return page;
	    }



	 private String transIntoSqlChar(String sort) {
	    	String sortName = "";
	    	if("code".equals(sort)) {
	    		sortName = "code";
	    	}else if("iteName".equals(sort)) {
	    		sortName = "ite_name";
	    	}else if("startDate".equals(sort)) {
	    		sortName = "start_date";
	    	}else if("endDate".equals(sort)) {
	    		sortName = "end_date";
	    	}else if("planStartDate".equals(sort)) {
	    		sortName = "plan_start_date";
	    	}else if("planEndDate".equals(sort)) {
	    		sortName = "plan_end_date";
	    	}
			return sortName;
		}
	
	 	@RequestMapping(value = "/selecById")
	    @ResponseBody
	    public Map<String, Object> selecById(String id){
	    	Map<String, Object> map = new  HashMap<String, Object>();
			try {
				UserInfo iterativeWorkManage = userManagerService.selecById(id);
				map.put("code", "success");
				map.put("rows", iterativeWorkManage);
			} catch (Exception e) {
				map.put("code", "failure");
				logger.error(e.getMessage(), e);
			}
			return map;
	    }

	@RequestMapping(value = "/updateNews", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse updateNews(HttpServletRequest request) throws ParseException {
		UserInfo userInfo = new UserInfo();
		String pwd= request.getParameter("password").trim();
		String password;
		try{
			if (null != request.getParameter("password").trim()&& !(request.getParameter("password").trim().equals("")) ){
				password = AESOperator.getInstance().encrypt(request.getParameter("password").trim());
				userInfo.setPASSWORD(password);
			}
		}catch (Exception e){
			logger.error("enc加密失败：" + e.getMessage());
		}
		String permissonName = request.getParameter("permissionName").trim();
		System.out.println(permissonName+"001");
		String[] permissonNames = permissonName.split(",");
		StringBuffer sbf = new StringBuffer();
		for (String perstr : permissonNames) {
			String pidstr =String.valueOf(userManagerService.queryperId(perstr));
			sbf.append(pidstr).append(",");
		}
		String permissionids = null;
		if (sbf.length()>0) {
			permissionids = (sbf.substring(0, sbf.length()-1)).toString();
		}
		System.out.println(permissionids+"002");
		userInfo.setUSERID(request.getParameter("userId"));
		userInfo.setPermissionNames(permissionids);
		BaseResponse result = new BaseResponse();
		System.out.println(userInfo);
		try {
			userManagerService.updateNew(userInfo);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;

	}
	 	@RequestMapping(value = "/updateNew", method = RequestMethod.POST)
		@ResponseBody
		public BaseResponse updateNew(HttpServletRequest request)
				throws ParseException {				
	 		UserInfo userInfo = new UserInfo();
	 		String permissonName = request.getParameter("permissionName").trim();
	 		System.out.println(permissonName+"001");
			String[] permissonNames = permissonName.split(",");
			StringBuffer sbf = new StringBuffer();
			for (String perstr : permissonNames) {
				String pidstr =String.valueOf(userManagerService.queryperId(perstr));
				sbf.append(pidstr).append(",");
			}
			String permissionids = null;
			if (sbf.length()>0) {
				permissionids = (sbf.substring(0, sbf.length()-1)).toString();
			}		
			System.out.println(permissionids+"002");
//			userInfo.setPermissionids(permissionids);
			String buSearch = request.getParameter("buSearch");
			String duSearch = request.getParameter("duSearch");
			String deptSearch = request.getParameter("deptSearch");
			
			String hwpduSearch = request.getParameter("hwpduSearch");
			String hwzpduSearch = request.getParameter("hwzpduSearch");
			String pduspdtSearch = request.getParameter("pduspdtSearch");

//            userInfo.setUSERNAME(request.getParameter("userName"));
            userInfo.setUSERID(request.getParameter("userId"));
			userInfo.setBuVal(buSearch);
			userInfo.setDuVal(duSearch);
			userInfo.setDeptVal(deptSearch);
			userInfo.setHwpduVal(hwpduSearch);
			userInfo.setHwzpduVal(hwzpduSearch);
			userInfo.setPduspdtVal(pduspdtSearch);
			userInfo.setPermissionNames(permissionids);
			BaseResponse result = new BaseResponse();
			System.out.println(userInfo);
			try {
				userManagerService.updateNew(userInfo);
				result.setCode("success");
			} catch (Exception e) {
				logger.error("编辑失败：", e);
				result.setCode("failure");
				result.setMessage("操作失败");
			}
			return result;
		
		}
	 	
	 	/**
	 	 * 获取项目成员
	 	 * @param
	 	 * @return
	 	 */
	 	@RequestMapping(value = "/getProjectMembers",method = RequestMethod.POST,consumes = "application/json")
		@ResponseBody
		public TableSplitResult getProjectMembers(HttpServletRequest request){
			String userid = request.getParameter("userid")== null?"":request.getParameter("userid");
			int limit = request.getParameter("limit") == null ? 5 : Integer.parseInt(request.getParameter("limit"));//每页显示条数
			int offset = request.getParameter("offset") == null?0:Integer.parseInt(request.getParameter("offset"));//第几页
			TableSplitResult page = new TableSplitResult();
			page.setPage(offset);
			page.setRows(limit);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userid",userid);
			page.setQueryMap(map);
			page = userManagerService.getProjectMembers(page,userid);
			return page;
		}
	 	/**
	 	 * 获取项目成员
	 	 * @param
	 	 * @return
	 	 */
	 	@RequestMapping(value = "/getProjectMembersByNo",method = RequestMethod.POST,consumes = "application/json")
		@ResponseBody
		public TableSplitResult getProjectMembersByNo(HttpServletRequest request){
			String proNo = request.getParameter("proNo")== null?"":request.getParameter("proNo");
			int limit = request.getParameter("limit") == null ? 5 : Integer.parseInt(request.getParameter("limit"));//每页显示条数
			int offset = request.getParameter("offset") == null?0:Integer.parseInt(request.getParameter("offset"));//第几页
			TableSplitResult page = new TableSplitResult();
			page.setPage(offset);
			page.setRows(limit);
			page = userManagerService.getProjectMembersByNo(page,proNo);
			return page;
		}

 	/**
 	 * 项目配置成员复用
 	 * @param proNo
 	 * @param oldNo
 	 * @return
 	 */
 	@RequestMapping("/inheritProjectMember")
	@ResponseBody
	public Map<String, Object> inheritProjectMember(String proNo, String oldNo) {
		Map<String, Object> map = new HashMap<>();
		int count = userManagerService.inheritProjectMember(proNo, oldNo);
		
		if (count == 1) {
			map.put("code", "success");
		} else {
			map.put("code", "failed");
		}
		return map;
	}
	 	
 	/**
 	 * 团队配置成员复用
 	 * @param proNo
 	 * @return
 	 */
	@RequestMapping("/inheritTeamMember")
	@ResponseBody
	public Map<String, Object> inheritTeamMember(String proNo) {
		Map<String, Object> map = new HashMap<>();
		int count = userManagerService.inheritTeamMember(proNo);

		if (count == 1) {
			map.put("code", "success");
		} else {
			map.put("code", "failed");
		}
		return map;
	}
	 	
	 	/*@RequestMapping("/getProjectTeamMember")
		@ResponseBody
		public List<Map<String, Object>> getProjectTeamMember(String projNo){
			return userManagerService.getProjectTeamMember(projNo);
		}*/
	 	
	 	/**
	 	 * 项目指标配置继承
	 	 * @param
	 	 * @param oldNo
	 	 * @return
	 	 */
	 	@RequestMapping(value = "/modifyProcessIndicator", method = RequestMethod.POST)
		@ResponseBody
		public BaseResponse modifyProcessIndicator(String proNo, String oldNo){
			BaseResponse result = new BaseResponse();
			try {
				userManagerService.modifyProcessIndicator(proNo,oldNo);
				result.setCode("success");
			} catch (Exception e) {
				logger.error("项目指标配置继承失败：", e);
				result.setCode("failure");
				result.setMessage("项目指标配置继承失败");
			}
			return result;
		}

		/**
	　　* @description: 团队所属项目指标配置
	　　* @param proNo,oldNo
	　　* @return BaseResponse
	　　* @throws
	　　* @author chengchenhui
	　　* @date 2019/5/14 11:43
	　　*/
		@RequestMapping(value = "/teamProjectCopy", method = RequestMethod.POST)
		@ResponseBody
		public BaseResponse teamProjectCopy(String proNo, String oldNo){
			BaseResponse result = new BaseResponse();
			try {
				userManagerService.teamProjectCopy(proNo,oldNo);
			} catch (Exception e) {
				logger.error("团队项目指标继承失败："+e.getMessage());
			}
			return result;
		}
	 	
	 	/**
	 	 * 团队指标配置继承
	 	 * @param
	 	 * @return
	 	 */
	 	@RequestMapping(value = "/modifyProcessIndicatorByTeam", method = RequestMethod.POST)
		@ResponseBody
		public BaseResponse modifyProcessIndicatorByTeam(String proNo){
	 		BaseResponse result = new BaseResponse();
	 		if(StringUtilsLocal.isBlank(proNo)){
	 			return result;
	 		}
			try {
				String teamId = userManagerService.getTeamId(proNo);
				List<Map<String, Object>> team = new ArrayList<Map<String, Object>>();
				if(StringUtils.isNotBlank(teamId)){
					team = service.getTeamLabs(teamId);
					if(team.size() != 0){
	        			userManagerService.modifyProcessIndicatorByTeam(proNo, teamId);
	        			result.setCode("success");
	        		}
			    }
			} catch (Exception e) {
				logger.error("团队指标配置继承失败：", e);
			}
			return result;
		}


	/**
　　* @description:团队指标继承
　　* @param proNo
　　* @return BaseResponse
　　* @throws
　　* @author chengchenhui
　　* @date 2019/5/15 10:39
　　*/
	@RequestMapping(value = "/measureCopyByTeam", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse measureCopyByTeam(String proNo){
		BaseResponse result = new BaseResponse();
		try{
			userManagerService.measureCopyByTeam(proNo,result);
		}catch (Exception e){
			result.setErrorMessage("msg",e.getMessage());
			logger.error("team measure copy failed:"+e.getMessage());
			return result;
		}
		return result;
	}
	 	
	 	/*
	 	 * 自动继承团队指标配置
	 	 */
	 	@RequestMapping(value = "/autoModifyProcessIndicator", method = RequestMethod.POST)
		@ResponseBody
		public BaseResponse autoModifyProcessIndicator(String proNo){
	 		BaseResponse result = new BaseResponse();
	 		if(StringUtilsLocal.isBlank(proNo)){
	 			return result;
	 		}
			try {
				List<Map<String, Object>> projectAllab = service.getProjectAllabs(proNo);
				if(projectAllab.size()==0) {
					String teamId = userManagerService.getTeamId(proNo);
					List<Map<String, Object>> teamList = new ArrayList<Map<String, Object>>();
					if(StringUtils.isNotBlank(teamId)){
		    			teamList = service.getTeamLabs(teamId);
		    			if(teamList.size() != 0){
		        			userManagerService.modifyProcessIndicatorByTeam(proNo, teamId);
		        			result.setCode("success");
		        		}
				    }
				}
			} catch (Exception e) {
				logger.error("团队指标配置继承失败：", e);
				result.setCode("failure");
				result.setMessage("团队指标配置继承失败");
			}
			return result;
		}
	 	
	 	@RequestMapping("/getProjectNo")
		@ResponseBody
		public String getProjectNo(String projNo){
			return userManagerService.getProjectNo(projNo);
		}

	/**
	 * 项目成员编辑页面回显
	 * @param projNo
	 * @param hwAccount
	 * @return
	 */
	@RequestMapping(value = "/editPage")
	    @ResponseBody
	    public Map<String, Object> editPage(String projNo, String hwAccount){
	    	Map<String, Object> map = new  HashMap<String, Object>();
			try {
				ProjectMembersLocal projectMembersLocal = userManagerService.openEditPage(projNo,hwAccount);
				map.put("code", "success");
				map.put("rows", projectMembersLocal);
			} catch (Exception e) {
				map.put("code", "failure");
				logger.error(e.getMessage(), e);
			}
			return map;
	    }
	 	
	 	/**
	 	 * 新增项目成员信息
	 	 * @param projectMembersLocal
	 	 * @param request
	 	 * @return
	 	 */
	 	@RequestMapping("/addProjectMember")
		@ResponseBody
		public BaseResponse addProjectMember(@RequestBody ProjectMembersLocal projectMembersLocal,
				HttpServletRequest request){
			BaseResponse result = new BaseResponse();
			try {
				Integer m = userManagerService.selectRepeat(projectMembersLocal);
				if(m==0) {
					userManagerService.addProjectMember(projectMembersLocal,request);
					result.setCode("success");			
				}else {
					result.setCode("failure");
					result.setMessage("项目成员已存在");
				}
			} catch (Exception e) {
				logger.error("新增失败：", e);
				result.setCode("failure");
				result.setMessage("操作失败");
			}
			return result;
		}
	 	/**
	 	 * 修改项目成员信息
	 	 * @param projectMembersLocal
	 	 * @return
	 	 */
	 	@RequestMapping(value = "/editProjectMember", method = RequestMethod.POST)
		@ResponseBody
		public BaseResponse editProjectMember(@RequestBody ProjectMembersLocal projectMembersLocal){
			BaseResponse result = new BaseResponse();
			try {
				userManagerService.editProjectMember(projectMembersLocal);
				result.setCode("success");
			} catch (Exception e) {
				logger.error("编辑失败：", e);
				result.setCode("failure");
				result.setMessage("操作失败");
			}
			return result;
		}
	 	/**
	 	 * 删除项目成员信息
	 	 * @param projNos
	 	 * @param hwAccounts
	 	 * @return
	 	 */
	 	@RequestMapping("/deleteProjectMems")
		@ResponseBody
		public BaseResponse deleteProjectMems(String[] projNos,String[] hwAccounts){
			BaseResponse result = new BaseResponse();
			try {
				userManagerService.deleteProjectMems(projNos,hwAccounts);
				result.setCode("success");
			} catch (Exception e) {
				logger.error("删除失败：", e);
				result.setCode("failure");
				result.setMessage("操作失败");
			}
			return result;
		}
	 	/**
	 	 * 单个修改保存
	 	 * @param projectMembersLocal
	 	 * @return
	 	 */
	 	@RequestMapping(value = "/editOne", method = RequestMethod.POST)
		@ResponseBody
		public BaseResponse updateOneProjectMember(@RequestBody ProjectMembersLocal projectMembersLocal){
			BaseResponse result = new BaseResponse();
			try {
				userManagerService.updatemembersLocal(projectMembersLocal);
				result.setCode("success");
			} catch (Exception e) {
				logger.error("编辑失败：", e);
				result.setCode("failure");
				result.setMessage("操作失败");
			}
			return result;
		}

	/**
	 * 新增团队成员
	 * @param teamMembers
	 * @return
	 */
	@RequestMapping("/addTeamMember")
	@ResponseBody
	public BaseResponse addTeamMember(@RequestBody TeamMembers teamMembers) {
		BaseResponse result = new BaseResponse();
		String role = teamMembers.getRole();
		teamMembers.setRole(getRoleText(role));

		try {
			int count = userManagerService.addTeamMember(teamMembers);

			if (count > 0) {
				result.setCode("success");
			} else {
				result.setCode("failure");
				result.setMessage("团队成员已存在");
			}
		} catch (Exception e) {
			logger.error("新增失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}

	/**
	 * 团队成员信息回显
	 * @param teamId
	 * @param zrAccount
	 * @return
	 */
	@RequestMapping(value = "/getTeamMemberInfo")
	@ResponseBody
	public Map<String, Object> getTeamMemberInfo(String teamId, String zrAccount) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			TeamMembers teamMembers = userManagerService.queryMemberEchoDisplay(teamId, zrAccount);
			if ("开发工程师".equals(teamMembers.getRole())) {
				teamMembers.setRole("1");
			} else if ("测试工程师".equals(teamMembers.getRole())) {
				teamMembers.setRole("2");
			} else if ("PM".equals(teamMembers.getRole())) {
				teamMembers.setRole("3");
			} else if ("产品经理".equals(teamMembers.getRole())) {
				teamMembers.setRole("4");
			} else if ("SE".equals(teamMembers.getRole())) {
				teamMembers.setRole("5");
			} else if ("MDE".equals(teamMembers.getRole())) {
				teamMembers.setRole("6");
			} else if ("BA".equals(teamMembers.getRole())) {
				teamMembers.setRole("7");
			} else if ("IA".equals(teamMembers.getRole())) {
				teamMembers.setRole("8");
			} else if ("TC".equals(teamMembers.getRole())) {
				teamMembers.setRole("9");
			} else if ("TSE".equals(teamMembers.getRole())) {
				teamMembers.setRole("10");
			} else if ("QA".equals(teamMembers.getRole())) {
				teamMembers.setRole("11");
			} else if ("TL".equals(teamMembers.getRole())) {
				teamMembers.setRole("12");
			} else if ("UI".equals(teamMembers.getRole())) {
				teamMembers.setRole("13");
			} else if ("运维工程师".equals(teamMembers.getRole())) {
				teamMembers.setRole("14");
			} else if ("数据工程师".equals(teamMembers.getRole())) {
				teamMembers.setRole("15");
			} else if ("资料工程师".equals(teamMembers.getRole())) {
				teamMembers.setRole("16");
			} else if ("认证工程师".equals(teamMembers.getRole())) {
				teamMembers.setRole("17");
			} else if ("PL".equals(teamMembers.getRole())) {
				teamMembers.setRole("18");
			}
			map.put("code", "success");
			map.put("rows", teamMembers);
		} catch (Exception e) {
			map.put("code", "failure");
			logger.error(e.getMessage(), e);
		}
		return map;
	}
	 	
	 	/**
	 	 * 团队成员信息修改
	 	 * @param teamMembers
	 	 * @return
	 	 */
	@RequestMapping(value = "/editTeamMemberInfo", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse editTeamMemberInfo(@RequestBody TeamMembers teamMembers) {
		BaseResponse result = new BaseResponse();
		try {
			String role = teamMembers.getRole();
			teamMembers.setRole(getRoleText(role));
			userManagerService.editTeamMemberInfo(teamMembers);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}

	/**
	 * 删除团队成员
	 * @param teamIds
	 * @param zrAccounts
	 * @return
	 */
	@RequestMapping("/deleteTeamMembers")
	@ResponseBody
	public BaseResponse deleteTeamMembers(String[] teamIds,String[] zrAccounts){
		BaseResponse result = new BaseResponse();
		try {
			userManagerService.deleteTeamMembers(teamIds,zrAccounts);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("删除失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}

	private String getRoleText(String role){
		String roleText = "";
		if("1".equals(role)){
			roleText = "开发工程师";
		}else if("2".equals(role)){
			roleText = "测试工程师";
		}else if("3".equals(role)){
			roleText = "PM";
		}else if("4".equals(role)){
			roleText = "产品经理";
		}else if("5".equals(role)){
			roleText = "SE";
		}else if("6".equals(role)){
			roleText = "MDE";
		}else if("7".equals(role)){
			roleText = "BA";
		}else if("8".equals(role)){
			roleText = "IA";
		}else if("9".equals(role)){
			roleText = "TC";
		}else if("10".equals(role)){
			roleText = "TSE";
		}else if("11".equals(role)){
			roleText = "QA";
		}else if("12".equals(role)){
			roleText = "TL";
		}else if("13".equals(role)){
			roleText = "UI";
		}else if("14".equals(role)){
			roleText = "运维工程师";
		}else if("15".equals(role)){
			roleText = "数据工程师";
		}else if("16".equals(role)){
			roleText = "资料工程师";
		}else if("17".equals(role)){
			roleText = "认证工程师";
		}else if("18".equals(role)){
			roleText = "PL";
		}
		return roleText;
	}

	@RequestMapping("/getProjectPmId")
	@ResponseBody
	public String getProjectPmId(String projNo){
		return userManagerService.getProjectPmId(projNo);
	}

	// 查询关联项目
	@RequestMapping("/getRelateProjects")
	@ResponseBody
	public Map<String, Object> getRelateProjects(String userid){
		return userManagerService.getRelateProjects(userid);

	}

	@RequestMapping("/getMemberinfo")
	@ResponseBody
	public Map<String, Object> getMemberinfo(String zrAccount) {
		String zr =  StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(zrAccount), "0", 10);
		Map<String, Object> map = new HashMap<>();
		map = userManagerService.getMemberinfoByZr(zr);
		if (map != null && map.size()>0){
			map.put("code","success");
		}else {
			map.put("code","fail");
		}
		return  map;
	}
	@RequestMapping("/verifyHwAccount")
	@ResponseBody
	public Map<String, Object> verifyHwAccount(String zrAccount,String hwAccount) {
		String zr =  StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(zrAccount), "0", 10);
		Map<String, Object> map  = new HashMap<>(0);
		map = userManagerService.verifyHwAccount(zr,hwAccount);
		return  map;
	}
}

