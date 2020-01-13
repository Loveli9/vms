package com.icss.mvp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icss.mvp.dao.GeneralSituationDao;
import com.icss.mvp.dao.IProjectInfoDao;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.IProjectManagersDao;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.dao.MeasureCommentDao;
import com.icss.mvp.dao.MeasureRangeDao;
import com.icss.mvp.dao.OpDepartmentDao;
import com.icss.mvp.dao.ProjectLableDao;
import com.icss.mvp.dao.SysRoleDao;
import com.icss.mvp.dao.index.InTmplDao;
import com.icss.mvp.entity.Label;
import com.icss.mvp.entity.OpDepartment;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.Permission;
import com.icss.mvp.entity.PersonnelInfo;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.ProjectLabelConfig;
import com.icss.mvp.entity.ProjectMembersLocal;
import com.icss.mvp.entity.SysHwdept;
import com.icss.mvp.entity.SysRole;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.TeamLabel;
import com.icss.mvp.entity.TeamMembers;
import com.icss.mvp.entity.UserDetailInfo;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.util.AESOperator;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringMD5Util;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("userManagerService")
@Transactional
public class UserManagerService {
	@Resource
	IUserManagerDao dao;
	@Resource
	IProjectManagersDao projectManagersDao;
	@Resource
	ProjectLableDao projectLableDao;
	@Resource
	private IProjectListDao prolistdao;
	@Resource
	private OpDepartmentDao opDepartmentDao;
	@Resource
	private SysRoleService sysRoleService;
	@Resource
	private SysRoleDao sysRoleDao;

	@Autowired
	private MeasureRangeDao measureRangeDao;

//	@Autowired
//	private InTmplIndexService indexService;

	@Autowired
	private InTmplDao inTmplDao;

	@Autowired
	private MeasureCommentDao measureCommentDao;

	@Autowired
	private CodeGainTypeService codeGainTypeService;

	@Autowired
	private GeneralSituationService generalSituationService;

	@Autowired
	private GeneralSituationDao generalSituationDao;

	@Autowired
	private  IUserManagerDao  UserManagerDao;

	@Autowired
	private IProjectInfoDao projectInfoDao;

//	@Autowired
//	private ICodeMasterInfoDao codeMasterInfoDao;




	private static Logger logger = Logger.getLogger(UserManagerService.class);

	public Map<String, Object> queryUserInfo(UserInfo userInfo, Page page) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<UserInfo> userList = dao.queryUserInfos(userInfo, page.getSort(), page.getOrder());

		for (UserInfo userInfos : userList) {
			String perids = userInfos.getPermissionids();
			String[] perid = perids.split(",");
			StringBuffer sbf = new StringBuffer();
			for (String perstr : perid) {
				String pername = StringUtilsLocal.valueOf(dao.querypNameById(Integer.parseInt(perstr)));
				sbf.append(pername).append(",");
			}
			String permissionNames = null;
			if (sbf.length() > 0) {
				permissionNames = (sbf.substring(0, sbf.length() - 1)).toString();
			}
			userInfos.setPermissionNames(permissionNames);
		}
		result.put("rows", userList);
		result.put("total", userList.size());
		return result;
	}

	/*
	 * public int deleteUser(UserInfo user) { return dao.deleteUser(user); }
	 */

	public void deleteUser(UserInfo user) throws Exception {
		user.setUPDATERTIME(new Date());
		dao.deleteUser(user);
	}

	/**
	 * 导入历史项目的的团队成员
	 * 
	 * @param newNo
	 * @param oldNo
	 */
	public void addOldProjectTeamMembers(String newNo, String oldNo) {
		projectManagersDao.addOldProjectTeamMembers(newNo, oldNo);
	}

	public Map<String, Object> updateUser(UserInfo user) {
		Map<String, Object> result = new HashMap<String, Object>();
		String message;
		int counts = dao.updateUser(user);
		if (counts > 0) {
			message = "更新成功";
		} else {
			message = "更新失败";
		}
		result.put("message", message);
		return result;
	}

	public Map<String, Object> insertInfo(UserDetailInfo userDetailInfo) {
		Map<String, Object> result = new HashMap<String, Object>();
		String message;
		List<UserDetailInfo> users = dao.isExistsByAccount(userDetailInfo.getUserId());
		if (users.size() > 0) {
			message = "用户名已存在";
		} else {
			int userInfo = dao.addUserInfo(userDetailInfo);
			if (userInfo > 0) {
				message = "保存成功";
			} else {
				message = "保存失败";
			}
		}
		result.put("message", message);
		return result;
	}

	public UserInfo isExistsUserInfo(UserInfo userInfo, Page page) {
		return dao.isExistis(userInfo.getUSERNAME());
	}

	public String MD5Password(String password) {
		logger.info("##### MD5Password begin password #####");
		String mdpassword = StringMD5Util.MD5String(password);
		logger.info("##### MD5Password end  password #####=" + 123456 + "mdpassword" + 654321);
		return mdpassword;
	}

	public List<Map<String, Object>> getOMPUser(String projNo) {
		ProjectDetailInfo projd = prolistdao.isExit(projNo);
		List<Map<String, Object>> maps = projectManagersDao.queryOMPUser(projd.getPo());
		List<Map<String, Object>> selectedMaps = projectManagersDao.queryOMPUserSelected(projNo);
		List<Map<String, Object>> ret = new ArrayList<>();
	for (Map<String, Object> omp : maps) {
			for (Map<String, Object> selectedMap : selectedMaps) {
				if (omp.get("NAME") == null || omp.get("AUTHOR") == null || omp.get("ROLE") == null) {
					ret.add(omp);
					break;
				}
				if (omp.get("NAME").equals(selectedMap.get("NAME"))
						&& omp.get("AUTHOR").equals(selectedMap.get("HW_ACCOUNT"))
						&& omp.get("ROLE").equals(selectedMap.get("ROLE"))) {
					ret.add(omp);
					break;
				}
			}
		}
		maps.removeAll(ret);
		return maps;
	}

	public List<Map<String, Object>> getSelectedUser(String projNo) {
		List<Map<String, Object>> selectedMaps = new ArrayList<>();
		try {
			selectedMaps = projectManagersDao.queryOMPUserSelected(projNo);
		} catch (Exception e) {
			logger.error("UserManagerService getSelectedUser failed:" + e.getMessage());
		}
		/*
		 * ProjectInfo projInfo = projectManagersDao.queryProjectDate(projNo);
		 * Date startDate = null; Date endDate = null; if(projInfo!=null){
		 * startDate = projInfo.getStartDate(); endDate = projInfo.getEndDate();
		 * } if(selectedMaps!=null && !selectedMaps.isEmpty()){ for(Map<String,
		 * Object> member: selectedMaps){ member.put("START_DATE", startDate);
		 * member.put("END_DATE", endDate); } }
		 */
		return selectedMaps;
	}

	public List<ProjectMembersLocal> getSelectedUsers(TableSplitResult page,String pmid) {
		List<ProjectMembersLocal> datalist = new ArrayList<>();
		try {
			datalist= projectManagersDao.queryOMPUserSelecteds(page ,pmid);
		} catch (Exception e) {
			logger.error("UserManagerService getSelectedUser failed:" + e.getMessage());
		}
		/*
		 * ProjectInfo projInfo = projectManagersDao.queryProjectDate(projNo);
		 * Date startDate = null; Date endDate = null; if(projInfo!=null){
		 * startDate = projInfo.getStartDate(); endDate = projInfo.getEndDate();
		 * } if(selectedMaps!=null && !selectedMaps.isEmpty()){ for(Map<String,
		 * Object> member: selectedMaps){ member.put("START_DATE", startDate);
		 * member.put("END_DATE", endDate); } }
		 */
		return datalist;
	}

	public Map<String, Object> selectedUserAdd(List<ProjectMembersLocal> membersLocals) {
		Map<String, Object> ret = new HashMap<>();

		if (membersLocals == null || membersLocals.size() <= 0) {
			ret.put("ret", "fail");
			return ret;
		}

		int a = 0;
		int b = 0;
		
		try {
			String pmids = "";
			String usernames = membersLocals.get(0).getUserid();
			UserInfo userinfos = UserManagerDao.getUserInfoByName(usernames);
			if (userinfos.getUsertype().equals("2")){
				pmids = generalSituationDao.getPMZRAccountByHW(usernames);

			}else if (userinfos.getUsertype().equals("1")){
				pmids = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(userinfos.getUSERID()), "0", 10);

			}

			projectManagersDao.deleteProjectStaff(pmids);
            String pmid = "";
			for (ProjectMembersLocal projectMembersLocal : membersLocals) {
				String zrAcc = projectMembersLocal.getZrAccount();
				if (StringUtilsLocal.isBlank(zrAcc)) {
					continue;
				}
				String username = projectMembersLocal.getUserid();
                    UserInfo userinfo = UserManagerDao.getUserInfoByName(username);
                    if (userinfo.getUsertype().equals("2")){
                       pmid = generalSituationDao.getPMZRAccountByHW(username);
                        projectMembersLocal.setPmid((pmid ==null || "".equals(pmid))? null : pmid) ;
                    }else if (userinfo.getUsertype().equals("1")){
                    pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(userinfo.getUSERID()), "0", 10);
                    projectMembersLocal.setPmid(pmid);
                    }
				zrAcc = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(zrAcc), "0", 10);
				projectMembersLocal.setZrAccount(zrAcc);
				projectMembersLocal.setName(StringUtils.isNotBlank(projectMembersLocal.getName())
						? StringUtilsLocal.clearSpaceAndLine(projectMembersLocal.getName()) : null);
				projectMembersLocal.setHwAccount(StringUtils.isNotBlank(projectMembersLocal.getHwAccount())
						? StringUtilsLocal.clearSpaceAndLine(projectMembersLocal.getHwAccount()) : null);
				projectMembersLocal.setSvnGitNo(StringUtils.isNotBlank(projectMembersLocal.getSvnGitNo())
						? projectMembersLocal.getSvnGitNo().replaceAll(" ", "") : null);

				int memberBaseCount = projectManagersDao.selectMemberBase(zrAcc);
				int projectStaffCount = projectManagersDao.selectProjectStaff(pmid, zrAcc);

				if (memberBaseCount == 0) {
					a += projectManagersDao.addProjectStaffToMemberBase(projectMembersLocal);
				} else {
					a += projectManagersDao.updateProjectStaffToMemberBase(projectMembersLocal);
				}

				if (projectStaffCount == 0) {
					b += projectManagersDao.addProjectStaffToProjectStaff(projectMembersLocal);
				} else {
					b += projectManagersDao.updateProjectStaffToProjectStaff(projectMembersLocal);
				}

				generalSituationDao.alterRankOfProjectStaff(projectMembersLocal.getStatus(), projectMembersLocal.getRank(),
						projectMembersLocal.getZrAccount(), "two");
				generalSituationDao.alterRankOfTeamStaff(projectMembersLocal.getStatus(), projectMembersLocal.getRank(),
						projectMembersLocal.getZrAccount(), "two");
			}
			
			if (a == b && a == membersLocals.size()) {
				ret.put("ret", "success");
			} else {
				ret.put("ret", "fail");
			}
		} catch (Exception e) {
			logger.error("UserManagerService selectedUserAdd failed:" + e.getMessage());
		}
		// projectManagersDao.deleteSelectedUserByNo(projNo);

		// int i = projectManagersDao.insertSelectedUser(membersLocals);
		// if (i > 0) {
		// ret.put("ret", "success");
		// } else {
		// ret.put("ret", "fail");
		//
		// }
		// sprojectManagersDao.updateProjectKeyroles(membersLocals);
		return ret;
	}
	public Map<String, Object> membersAdd(ProjectMembersLocal membersLocals) {
		Map<String, Object> ret = new HashMap<>();
		if (membersLocals.getReprname()!=null && !("".equals(membersLocals.getReprname()))){
			membersLocals.setNo(projectInfoDao.getNoByName(membersLocals.getReprname()));
		}else {
			ret.put("code","norelateprojects");
			return ret;
		}
		membersLocals.setRole(membersLocals.getRole());
		membersLocals.setName(StringUtils.isNotBlank(membersLocals.getName())
				? StringUtilsLocal.clearSpaceAndLine(membersLocals.getName()) : null);
		membersLocals.setZrAccount(StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(membersLocals.getZrAccount()), "0", 10));

		membersLocals.setHwAccount(StringUtils.isNotBlank(membersLocals.getHwAccount())
				? StringUtilsLocal.clearSpaceAndLine(membersLocals.getHwAccount()) : null);

		membersLocals.setSvnGitNo(StringUtils.isNotBlank(membersLocals.getSvnGitNo())
				? membersLocals.getSvnGitNo().replaceAll(" ", "") : null);
       try {
		   int memberBaseCount = projectManagersDao.selectMemberBase(membersLocals.getZrAccount());
		   int projectStaffCount = projectManagersDao.selectProjectStaff(membersLocals.getNo(), membersLocals.getZrAccount());
		   int a = 0;
		   int b = 0;
		   if (memberBaseCount == 0) {
			   a = projectManagersDao.addProjectStaffToMemberBase(membersLocals);
		   } else {
			   a = projectManagersDao.updateProjectStaffToMemberBase(membersLocals);
		   }

		   if (projectStaffCount == 0) {
			   b = projectManagersDao.addProjectStaffToProjectStaffs(membersLocals);
		   } else {
			   b = projectManagersDao.updateProjectStaffToProjectStaffs(membersLocals);
		   }

//		   generalSituationDao.alterRankOfProjectStaff(membersLocals.getStatus(), membersLocals.getRank(), membersLocals.getZrAccount(), "two");
		   if (a > 0 && b > 0) {
			   ret.put("code", "success");
		   } else {
			   ret.put("code", "fail");
		   }
	   }catch (Exception e){
		   logger.error("UserManagerService selectedUserAdd failed:" + e.getMessage());
	   }
       return ret;

	}

	/*
	* 项目中团队成员添加
	*/
	public Map<String, Object> projectMembersAdd(ProjectMembersLocal membersLocals) {
		Map<String, Object> ret = new HashMap<>();
		membersLocals.setName(StringUtils.isNotBlank(membersLocals.getName())
				? StringUtilsLocal.clearSpaceAndLine(membersLocals.getName()) : null);
		membersLocals.setZrAccount(StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(membersLocals.getZrAccount()), "0", 10));

		membersLocals.setHwAccount(StringUtils.isNotBlank(membersLocals.getHwAccount())
				? StringUtilsLocal.clearSpaceAndLine(membersLocals.getHwAccount()) : null);

		membersLocals.setSvnGitNo(StringUtils.isNotBlank(membersLocals.getSvnGitNo())
				? membersLocals.getSvnGitNo().replaceAll(" ", "") : null);
       try {
		   int memberBaseCount = projectManagersDao.selectMemberBase(membersLocals.getZrAccount());
		   int projectStaffCount = projectManagersDao.selectProjectStaff(membersLocals.getNo(), membersLocals.getZrAccount());
		   int a = 0;
		   int b = 0;
		   if (memberBaseCount == 0) {
			   a = projectManagersDao.addProjectStaffToMemberBase(membersLocals);
		   } else {
			   a = projectManagersDao.updateProjectStaffToMemberBase(membersLocals);
		   }

		   if (projectStaffCount == 0) {
			   b = projectManagersDao.addProjectStaffToProjectStaffs(membersLocals);
		   } else {
			   b = projectManagersDao.updateProjectStaffToProjectStaffs(membersLocals);
		   }

		   if (a > 0 && b > 0) {
			   ret.put("code", "success");
		   } else {
			   ret.put("code", "fail");
		   }
	   }catch (Exception e){
		   logger.error("UserManagerService selectedUserAdd failed:" + e.getMessage());
	   }
       return ret;

	}

	public ProjectMembersLocal  backforuserDisplay(String no, String userid, String zrAccount) {
		ProjectMembersLocal membersLocal  = new ProjectMembersLocal();

		String zr = "";
		if (StringUtils.isNotBlank(zrAccount) && zrAccount.length() <= 10) {
			zr = StringUtilsLocal.zeroFill(zrAccount, 10);
			UserInfo user = UserManagerDao.getUserInfoByName(userid);
			String pmid ="";
			//	String proNos ="";
			if ("2".equals(user.getUsertype())){
				pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
				if (pmid==null || "".equals(pmid)){
					return membersLocal;
				}
			}else if ("1".equals(user.getUsertype())){
				pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
				if (pmid==null || "".equals(pmid)){
					return membersLocal;
				}
			}
			ProjectMembersLocal ml = UserManagerDao.queryMemberinfoDisplay(no ,pmid, StringUtils.isNotBlank(zr) ? zr : zrAccount);
			//Map<String, Object> rankMap = generalSituationService.projectMemberEcho(pmid, StringUtils.isNotBlank(zr) ? zr : zrAccount);

			if (null != ml) {
				membersLocal.setNo(ml.getNo()==null? "" : ml.getNo());
				membersLocal.setReprname(projectInfoDao.getNameByNo(ml.getNo()));
				membersLocal.setName(ml.getName()==null? "" : ml.getName());
				membersLocal.setZrAccount(ml.getZrAccount()==null? "" : ml.getZrAccount());
				membersLocal.setHwAccount(ml.getHwAccount()==null? "" : ml.getHwAccount());
				membersLocal.setSvnGitNo(ml.getSvnGitNo()==null? "" : ml.getSvnGitNo());
				membersLocal.setRole(ml.getRole()==null? "" : ml.getRole());
				membersLocal.setRank(ml.getRank()==null? "" : ml.getRank());
				membersLocal.setStartDate(ml.getStartDate());
				membersLocal.setEndDate(ml.getEndDate());
				membersLocal.setStartDatestr(ml.getStartDate()==null? "": DateUtils.SHORT_FORMAT_GENERAL .format(ml.getStartDate()));
				membersLocal.setEndDatestr(ml.getEndDate()==null? "":DateUtils.SHORT_FORMAT_GENERAL .format(ml.getEndDate()));
				membersLocal.setStatus(ml.getStatus()==null? "" : ml.getStatus());
			}
			/**if (null != rankMap){

			}*/

		}
		return membersLocal ;
	}

	/*
	* 项目中团队成员修改回显
	*/
	public ProjectMembersLocal  editProjectPages(String no,String zrAccount) {
		ProjectMembersLocal membersLocal  = new ProjectMembersLocal();

		String zr = "";
		if (StringUtils.isNotBlank(zrAccount) && zrAccount.length() <= 10) {
			zr = StringUtilsLocal.zeroFill(zrAccount, 10);
			ProjectMembersLocal ml = UserManagerDao.editProjectPages(no , StringUtils.isNotBlank(zr) ? zr : zrAccount);

			if (null != ml) {
				membersLocal.setNo(ml.getNo()==null? "" : ml.getNo());
				membersLocal.setName(ml.getName()==null? "" : ml.getName());
				membersLocal.setZrAccount(ml.getZrAccount()==null? "" : ml.getZrAccount());
				membersLocal.setHwAccount(ml.getHwAccount()==null? "" : ml.getHwAccount());
				membersLocal.setSvnGitNo(ml.getSvnGitNo()==null? "" : ml.getSvnGitNo());
				membersLocal.setRole(ml.getRole()==null? "" : ml.getRole());
				membersLocal.setRank(ml.getRank()==null? "" : ml.getRank());
				membersLocal.setStartDate(ml.getStartDate());
				membersLocal.setEndDate(ml.getEndDate());
				membersLocal.setStartDatestr(ml.getStartDate()==null? "": DateUtils.SHORT_FORMAT_GENERAL .format(ml.getStartDate()));
				membersLocal.setEndDatestr(ml.getEndDate()==null? "":DateUtils.SHORT_FORMAT_GENERAL .format(ml.getEndDate()));
				membersLocal.setStatus(ml.getStatus()==null? "" : ml.getStatus());
			}
		}
		return membersLocal ;
	}
	public void updatemembersLocal(ProjectMembersLocal membersLocal) {
		UserInfo user = UserManagerDao.getUserInfoByName(membersLocal.getUserid());
		String pmid ="";
		if ("2".equals(user.getUsertype())){
			pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
		}else if ("1".equals(user.getUsertype())){
			pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
		}
        membersLocal.setNo(membersLocal.getReprname()==null? membersLocal.getNo(): projectInfoDao.getNoByName(membersLocal.getReprname()));
		membersLocal.setName(StringUtils.isNotBlank(membersLocal.getName())
				? StringUtilsLocal.clearSpaceAndLine(membersLocal.getName()) : null);
		membersLocal.setZrAccount(StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(membersLocal.getZrAccount()), "0", 10));
		membersLocal.setHwAccount(StringUtils.isNotBlank(membersLocal.getHwAccount())
				? StringUtilsLocal.clearSpaceAndLine(membersLocal.getHwAccount()) : null);
		membersLocal.setSvnGitNo(StringUtils.isNotBlank(membersLocal.getSvnGitNo())
				? membersLocal.getSvnGitNo().replaceAll(" ", "") : null);
		membersLocal.setStartDate(membersLocal.getStartDatestr()== null? membersLocal.getStartDate(): DateUtils.parseDate(membersLocal.getStartDatestr(),"yyyy-MM-dd"));
		membersLocal.setEndDate(membersLocal.getEndDatestr() == null ? membersLocal.getEndDate(): DateUtils.parseDate(membersLocal.getEndDatestr(),"yyyy-MM-dd"));
//		Map<String, Object> rankMap = new HashMap<>();
//		if (pmid != null && !("".equals(pmid))){
//			membersLocal.setPmid(pmid);
//			rankMap = generalSituationService .projectMemberEcho(membersLocal.getPmid(), membersLocal.getZrAccount());
//		}


//		if(null != rankMap){
//			membersLocal.setRank(StringUtilsLocal.valueOf(rankMap.get("RANK")));
//		}
		UserManagerDao.updatememberlocalToMemberBase(membersLocal);
		UserManagerDao.updatmemberlocalToProjectStaff(membersLocal);

		generalSituationDao.alterRankOfProjectStaff(membersLocal.getStatus(), membersLocal.getRank(),
				membersLocal.getZrAccount(), "two");

	}
	public void updateProjectMembers(ProjectMembersLocal membersLocal) {
		membersLocal.setName(StringUtils.isNotBlank(membersLocal.getName())
				? StringUtilsLocal.clearSpaceAndLine(membersLocal.getName()) : null);
		membersLocal.setZrAccount(StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(membersLocal.getZrAccount()), "0", 10));
		membersLocal.setHwAccount(StringUtils.isNotBlank(membersLocal.getHwAccount())
				? StringUtilsLocal.clearSpaceAndLine(membersLocal.getHwAccount()) : null);
		membersLocal.setSvnGitNo(StringUtils.isNotBlank(membersLocal.getSvnGitNo())
				? membersLocal.getSvnGitNo().replaceAll(" ", "") : null);
		membersLocal.setStartDate(membersLocal.getStartDatestr()== null? membersLocal.getStartDate(): DateUtils.parseDate(membersLocal.getStartDatestr(),"yyyy-MM-dd"));
		membersLocal.setEndDate(membersLocal.getEndDatestr() == null ? membersLocal.getEndDate(): DateUtils.parseDate(membersLocal.getEndDatestr(),"yyyy-MM-dd"));
		UserManagerDao.updatememberlocalToMemberBase(membersLocal);
		UserManagerDao.updatmemberlocalToProjectStaff(membersLocal);

		generalSituationDao.alterRankOfProjectStaff(membersLocal.getStatus(), membersLocal.getRank(),
				membersLocal.getZrAccount(), "two");

	}

	public void deletePromembers(String[] nos, String[] zrAccounts) {
		String noss = nos[0];
		String zrAccountss = zrAccounts[0];
		ObjectMapper obj = new ObjectMapper();
		ArrayList<String> nosArray;
		ArrayList<String> zrAccountsArray;
		try {
			nosArray = obj.readValue(noss, ArrayList.class);
			zrAccountsArray = obj.readValue(zrAccountss, ArrayList.class);
			for (int i = 0; i < nosArray.size(); i++) {
				String no = nosArray.get(i);
				String zrAccount = zrAccountsArray.get(i);

				UserManagerDao.deleteProjectStaff(no, zrAccount);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void deletePromember(String no, String zrAccount) {

		try {
			UserManagerDao.deleteProjectStaff(no, zrAccount);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Map<String, Object> selectedUserDel(String projNo, String author) {
		Map<String, Object> ret = new HashMap<>();
		int i = projectManagersDao.deleteSelectedUser(projNo, author);
		if (i > 0) {
			ret.put("ret", "success");
		} else {
			ret.put("ret", "fail");

		}
		return ret;
	}

	public Map<String, Object> getOMPUserByAuthor(String userid, String author) {
		UserInfo userinfo = UserManagerDao.getUserInfoByName(userid);
		String pmid = "";
		if (userinfo.getUsertype().equals("2")){
			pmid = generalSituationDao.getPMZRAccountByHW(userinfo.getUSERID());

		}else if (userinfo.getUsertype().equals("1")){
			pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(userinfo.getUSERID()), "0", 10);
		}
//		List<String> nos = UserManagerDao.queryProjectNos(pmid);

			List<ProjectDetailInfo> projds = prolistdao.isExits(pmid);
		for (ProjectDetailInfo projd: projds) {
			List<Map<String, Object>> maps = projectManagersDao.queryOMPUserByAuthor(projd.getPo(), author);
			if (maps != null && maps.size() > 0) {
				return maps.get(0);
			}
		}
		return new HashMap<>();
	}

	public void editRole(String userId, String roleId) {
		dao.editRole(userId, roleId);
	}

	public UserInfo getUserInfoByName(String username) {
		return dao.getUserInfoByName(username);
	}

	public Integer queryId(String roleName) {
		return dao.queryId(roleName);
	}

	public Integer queryperId(String permissionName) {

		return dao.queryperId(permissionName);
	}

	public void editper(String userId, String perids) {
		// Map<String, Object> pmap = new HashMap<String, Object>();
		// pmap.put("userId", userId);
		// pmap.put("perids", perids);
		dao.updateper(userId, perids);

	}

	public Long insertRole(String level, String search, String userName) {
		Set<String> deptIds = new HashSet<>();
		if (!StringUtilsLocal.isBlank(search)) {
			String[] deIds = search.split(",");
			for (String id : deIds) {
				deptIds.add(id);
			}
		}
		StringBuffer roleAuth = new StringBuffer();
		List<Map<String, Object>> ret = opDepartmentDao.getOpDepaBylevel(level, deptIds);
		for (Map<String, Object> map : ret) {
			String path = "/CS";
			String id = StringUtilsLocal.valueOf(map.get("id"));
			if (!StringUtilsLocal.isBlank(id)) {
				path += "/" + id;
			}
			String trunkId = StringUtilsLocal.valueOf(map.get("trunk_id"));
			if (!StringUtilsLocal.isBlank(trunkId)) {
				path += "/" + trunkId;
			}
			String branchId = StringUtilsLocal.valueOf(map.get("branch_id"));
			if (!StringUtilsLocal.isBlank(branchId)) {
				path += "/" + branchId;
			}
			String deptId = StringUtilsLocal.valueOf(map.get("dept_id"));
			if (!StringUtilsLocal.isBlank(deptId)) {
				path += "/" + deptId;
			}

			roleAuth.append(path).append(",");
		}

		SysRole init = new SysRole();
		init.setOperateUser("admin");
		init.setIgnoe(0);
		init.setOperateTime(new Date());
		init.setRoleName(userName);
		init.setRoleAuth(roleAuth.substring(0, roleAuth.length() - 1));
		sysRoleService.insertSysRole(init);
		return init.getRoleId();
	}

	@SuppressWarnings("all")
	public TableSplitResult queryIteInfoByPage(TableSplitResult page, String proNo) {
		TableSplitResult data = new TableSplitResult();
		List<OpDepartment> opDepartmentsList = opDepartmentDao.getEnableDepartment();
		Map<String, Object> opDepartments = new HashMap<>();
		for (OpDepartment opDepartment : opDepartmentsList) {
			opDepartments.put(opDepartment.getDeptId(), opDepartment.getDeptName());
		}
		// 华为线
		List<SysHwdept> hwDepartmentsList = opDepartmentDao.getHwEnableDepartment();
		Map<String, Object> hwDepartments = new HashMap<>();
		for (SysHwdept hwDepartment : hwDepartmentsList) {
			hwDepartments.put(hwDepartment.getDeptId().toString(), hwDepartment.getDeptName());
		}
		List<Permission> permissionList = sysRoleDao.getAllPermission(null);
		Map<String, Object> permissions = new HashMap<>();
		for (Permission permission : permissionList) {
			permissions.put(permission.getPermissionid().toString(), permission.getPerName());
		}
		List<UserInfo> resultList = dao.queryIteInfoByPage(page, proNo, null, null);

		for (UserInfo userInfo : resultList) {
			userInfo.setPermissionNames(setValue(permissions, userInfo.getPermissionids()));
			userInfo.setDuVal(setValue(opDepartments, userInfo.getDu()));
			userInfo.setBuVal(setValue(opDepartments, userInfo.getBu()));
			userInfo.setDeptVal(setValue(opDepartments, userInfo.getDept()));
			// hwpduVal; hwzpduVal; pduspdtVal;
			userInfo.setHwpduVal(setValue(hwDepartments, userInfo.getHwpdu()));
			userInfo.setHwzpduVal(setValue(hwDepartments, userInfo.getHwzpdu()));
			userInfo.setPduspdtVal(setValue(hwDepartments, userInfo.getPduspdt()));
			try{
			userInfo.setPASSWORD(userInfo.getUsertype().equals("0")? (null == userInfo.getPASSWORD()? "": AESOperator.getInstance().decrypt(userInfo.getPASSWORD())):"");
			}catch (Exception e){
				logger.error("des解密失败：" + e.getMessage());
			}
		}
		data.setRows(resultList);
		Integer total = dao.queryIterationTotals(proNo, page);
		data.setTotal(total);
		return data;
	}

	private String setValue(Map<String, Object> val, String allids) {
		StringBuffer name = new StringBuffer();
		if (!StringUtilsLocal.isBlank(allids)) {
			String[] ids = allids.split(",");
			for (String id : ids) {
				name.append(val.get(id)).append(",");
			}
			return name.substring(0, name.length() - 1);
		}
		return "";
	}

	public UserInfo selecById(String id) throws Exception {
		return dao.selecById(id);
	}

	public void updateNew(UserInfo userInfo) throws Exception {
		userInfo.setUPDATERTIME(new Date());
		dao.updateNew(userInfo);

	}

	public TableSplitResult getProjectMembers(TableSplitResult page,String userid) {
		PersonnelInfo personnelInfo = new PersonnelInfo();
		TableSplitResult data = new TableSplitResult();
		List<ProjectMembersLocal> dataList = new ArrayList<>();
		if (StringUtils.isBlank(userid)) {
			logger.error("parameter illegal");
			return new TableSplitResult();
		}
		try {
			UserInfo user = UserManagerDao.getUserInfoByName(userid);
			String pmid ="";
			//	String proNos ="";
			if ("2".equals(user.getUsertype())){
				pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
				if (pmid==null || "".equals(pmid)){
					return data;
				}
			}else if ("1".equals(user.getUsertype())){
				pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
				if (pmid==null || "".equals(pmid)){
					return data;
				}
			}else {
				return data;
			}
			personnelInfo.setPmid(pmid);
			dataList = getSelectedUsers(page,pmid);
			// }
		} catch (Exception e) {
			logger.error("getSelectedUser exception, error: "+e.getMessage());
		}
		data.setRows(dataList);
		//查询总数
		Integer total= UserManagerDao.queyMembersTotal(personnelInfo);
		data.setTotal(total);
		return data;
	}
	public TableSplitResult getProjectMembersByNo(TableSplitResult page,String proNo) {
		TableSplitResult data = new TableSplitResult();
		List<ProjectMembersLocal> dataList = new ArrayList<>();
		try {
			dataList = projectManagersDao.getProjectMembersByNo(page ,proNo);
			data.setRows(dataList);
			//查询总数
			Integer total= UserManagerDao.queyMembersTotalByNo(proNo);
			data.setTotal(total);
		} catch (Exception e) {
			logger.error("getSelectedUser exception, error: "+e.getMessage());
		}
		return data;
	}

	public int inheritProjectMember(String proNo, String oldNo) {
		if (StringUtils.isBlank(proNo)) {
			logger.error("parameter illegal");
		}

		if (StringUtils.isBlank(oldNo)) {
			logger.error("parameter illegal");
		}

		int res = 0;
		String teamId = "teamId";

		List<Map<String, Object>> list = dao.queryOldProjectPost(oldNo);
		if (null != list && list.size() > 0) {
			for (Map<String, Object> map : list) {
				map.put("NO", proNo);
				dao.updateProjectPostDemand(map);
			}
		}

		res = inheritMemberInfo(proNo, oldNo, res, teamId);

		return res;

	}

	public int inheritTeamMember(String proNo) {
		if (StringUtils.isBlank(proNo)) {
			logger.error("parameter illegal");
		}

		int res = 0;
		String teamId = dao.queryTeamIdByProNo(proNo);
		String oldNo = "";

		res = inheritMemberInfo(proNo, oldNo, res, teamId);

		return res;
	}

	private int inheritMemberInfo(String proNo, String oldNo, int res, String teamId) {
		int count = 0;
		List<Map<String, Object>> memberList = null;

		try {
			if (StringUtils.isNotBlank(teamId)) {
				if (!teamId.equals("teamId")) {
					memberList = dao.queryTeamMembers(proNo, teamId);
				} else {
					memberList = dao.queryOldProjectMembers(proNo, oldNo);
				}
			}

			if (null != memberList && memberList.size() > 0) {
				for (Map<String, Object> member : memberList) {
					member.put("NO", proNo);
					int projectStaffCount = dao.queryRepeatProjectStaff(
                            StringUtilsLocal.valueOf(member.get("ZR_ACCOUNT")),
							StringUtilsLocal.valueOf(member.get("NO")));

					if (projectStaffCount == 0) {
						count += dao.addMemberInfo(member);
					} else {
						count += dao.updateMemberInfo(member);
					}

					Map<String, Object> rankMap = StringUtils.isNotBlank(teamId) && "teamId".equals(teamId)
							? generalSituationService.projectMemberEcho(oldNo,
									StringUtilsLocal.valueOf(member.get("ZR_ACCOUNT")))
							: generalSituationService.teamMemberEcho(teamId, StringUtilsLocal.valueOf(
                                    member.get("ZR_ACCOUNT")));

					if (null != rankMap) {
						generalSituationDao.alterRankOfProjectStaff(StringUtilsLocal.valueOf(rankMap.get("STATUS")),
								StringUtilsLocal.valueOf(rankMap.get("RANK")), StringUtilsLocal.valueOf(
                                        member.get("ZR_ACCOUNT")), "two");
						generalSituationDao.alterRankOfTeamStaff(StringUtilsLocal.valueOf(rankMap.get("STATUS")),
								StringUtilsLocal.valueOf(rankMap.get("RANK")), StringUtilsLocal.valueOf(
                                        member.get("ZR_ACCOUNT")), "two");
					}
					
				}
			}

			if (memberList.size() > 0 && count == memberList.size()) {
				res = 1;
			}
		} catch (Exception e) {
			logger.error("UserManagerService inheritMemberInfo failed:" + e.getMessage());
		}

		return res;
	}

	public List<Map<String, Object>> getProjectTeamMember(String projNo) {
		List<Map<String, Object>> selectedMaps = projectManagersDao.getProjectTeamMember(projNo);
		return selectedMaps;
	}

	public List<ProjectLabelConfig> getLabelConfig(String proNo) {
		List<ProjectLabelConfig> labelConfig = projectLableDao.getLabelConfig(proNo);
		return labelConfig;
	}

	public void modifyProcessIndicator(String proNo, String oldNo) {
		List<Label> list = projectLableDao.queryProjectAlllabs(oldNo);
		projectLableDao.deleteProjectAlllabs(proNo);
		for (Label oldLabel : list) {
			Integer oldId = oldLabel.getId();
			Label newLabel = new Label();
			newLabel.setIsDeleted(oldLabel.getIsDeleted());
			newLabel.setProNo(proNo);
			newLabel.setPlId(oldLabel.getPlId());
			projectLableDao.saveProjectAlllabs(newLabel);
			Integer newId = newLabel.getId();
			projectLableDao.saveLabelMesure(newId, oldId);
		}
	}

	/**
	 * @description: 团队项目指标继承 @param proNo,oldNo @author chengchenhiu @date
	 *               2019/5/14 11:39
	 */
	public void teamProjectCopy(String proNo, String oldNo) {
		// 团队内项目指标配置继承
		measureRangeDao.copyMeasureConfigByProject(proNo, oldNo);
		// 删除measure_range记录
		measureRangeDao.deleteMeasureRange(proNo);
		// 继承团队项目measure_range信息
		inTmplDao.CopyMeasureRangeByProject(proNo, oldNo);
		// 判断项目质量可信标识
		codeGainTypeService.saveProjectQualityFlag(proNo);
	}

	public void modifyProcessIndicatorByTeam(String proNo, String teamId) {
		List<TeamLabel> list = projectLableDao.queryProjectAlllabsByTeam(teamId);
		projectLableDao.deleteProjectAlllabs(proNo);
		for (TeamLabel oldLabel : list) {
			Integer oldId = oldLabel.getId();
			Label newLabel = new Label();
			newLabel.setIsDeleted(oldLabel.getIsDeleted());
			newLabel.setProNo(proNo);
			newLabel.setPlId(oldLabel.getPlId());
			projectLableDao.saveProjectAlllabs(newLabel);
			Integer newId = newLabel.getId();
			projectLableDao.saveLabelMesureByTeam(newId, oldId);
		}
	}

	/**
	 * @description: 团队指标继承 @param proNo teamId @return @throws @author
	 *               chengchenhiu @date 2019/5/15 10:41
	 */
	public BaseResponse measureCopyByTeam(String proNo, BaseResponse response) throws Exception {
		if (StringUtilsLocal.isBlank(proNo)) {
			throw new Exception("参数异常");
		}
		String teamId = projectManagersDao.getTeamId(proNo);
		if (StringUtilsLocal.isBlank(teamId)) {
			throw new Exception("该项目团队对信息不存在...");
		}
		Map<String, String> measureConfigMap = new HashMap<>();
		String measureIds = "";
		try {
			measureIds = measureCommentDao.measureConfigurationTeamRecord(teamId, null);
			measureConfigMap.put("no", proNo);
			measureConfigMap.put("measureIds", measureIds);
			projectLableDao.replaceMeasureConfig(measureConfigMap);
			// 判断项目质量可信标识
			codeGainTypeService.saveProjectQualityFlag(proNo);
		} catch (Exception e) {
			logger.error("团队指标信息获取失败" + e.getMessage());
			return response;
		}
		return saveMeasureRangeInfo(teamId, proNo);
	}

	/**
	 * @description: copy measure_info @param teamId no @return
	 *               BaseResponse @throws @author chengchenhiu @date 2019/5/15
	 *               11:22
	 */
	public BaseResponse saveMeasureRangeInfo(String teamId, String no) {
		BaseResponse response = new BaseResponse();
		try {
			// 删除记录
			measureRangeDao.deleteMeasureRange(no);
			inTmplDao.saveMeasureRangeInfo(teamId, no);
		} catch (Exception e) {
			logger.error("copy measure_info failed:" + e.getMessage());
			return response;
		}
		return response;
	}

	public void inheritProcessIndicator(String projNo, String oldNo) {
		List<Label> list = projectLableDao.queryProjectAlllabs(oldNo);
		projectLableDao.deleteProjectAlllabs(projNo);
		for (Label oldLabel : list) {
			Integer oldId = oldLabel.getId();
			Label newLabel = new Label();
			newLabel.setIsDeleted(oldLabel.getIsDeleted());
			newLabel.setProNo(projNo);
			newLabel.setPlId(oldLabel.getPlId());
			projectLableDao.saveProjectAlllabs(newLabel);
			Integer newId = newLabel.getId();
			projectLableDao.saveLabelMesure(newId, oldId);
		}
	}

	public String getTeamId(String proNo) {
		String result = projectManagersDao.getTeamId(proNo);
		return result;
	}

	public String getProjectNo(String projNo) {
		String result = projectManagersDao.getProjectNo(projNo);
		return result;
	}

	/**
	 * 项目成员编辑页面回显
	 * 
	 * @param
	 * @param hwAccount
	 * @return
	 * @throws Exception
	 */
	public ProjectMembersLocal openEditPage(String projNo, String hwAccount) throws Exception {
		return projectManagersDao.openEditPage(projNo, hwAccount);
	}

	/**
	 * 项目成员查重
	 * 
	 * @param projectMembersLocal
	 * @return
	 * @throws Exception
	 */
	public Integer selectRepeat(ProjectMembersLocal projectMembersLocal) throws Exception {
		// 查询是否存在
		Integer m = projectManagersDao.selectRepeat(projectMembersLocal);
		return m;
	}

	/**
	 * 新增项目成员
	 * 
	 * @param projectMembersLocal
	 * @param request
	 * @throws Exception
	 */
	public void addProjectMember(ProjectMembersLocal projectMembersLocal, HttpServletRequest request) throws Exception {
		int a = projectManagersDao.addProjectMember(projectMembersLocal);
	}

	/**
	 * 修改项目成员
	 *
	 * @param projectMembersLocal
	 */
	public void editProjectMember(ProjectMembersLocal projectMembersLocal) {
		projectManagersDao.editProjectMember(projectMembersLocal);
	}

	/**
	 * 删除项目成员
	 * 
	 * @param projNos
	 * @param hwAccounts
	 */
	public void deleteProjectMems(String[] projNos, String[] hwAccounts) {
		String projNoss = projNos[0];
		String hwAccountss = hwAccounts[0];
		ObjectMapper obj = new ObjectMapper();
		ArrayList<String> projNosArray;
		ArrayList<String> hwAccountsArray;
		try {
			projNosArray = obj.readValue(projNoss, ArrayList.class);
			hwAccountsArray = obj.readValue(hwAccountss, ArrayList.class);
			for (int i = 0; i < projNosArray.size(); i++) {
				String projNo = projNosArray.get(i);
				String hwAccount = hwAccountsArray.get(i);
				int a = projectManagersDao.deleteProjectMems(projNo, hwAccount);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("projectManagersDao.deleteProjectMems exception, error: "+e.getMessage());
		}
	}

	public int addTeamMember(TeamMembers teamMembers) throws Exception {
		teamMembers.setZrAccount(StringUtils.isNotBlank(teamMembers.getZrAccount())
				? StringUtilsLocal.zeroFill(StringUtilsLocal.clearSpaceAndLine(teamMembers.getZrAccount()), 10) : null);
		teamMembers.setName(StringUtils.isNotBlank(teamMembers.getName())
				? StringUtilsLocal.clearSpaceAndLine(teamMembers.getName()) : null);
		teamMembers.setHwAccount(StringUtils.isNotBlank(teamMembers.getHwAccount())
				? StringUtilsLocal.clearSpaceAndLine(teamMembers.getHwAccount()) : null);
		teamMembers.setSvnGitNo(StringUtils.isNotBlank(teamMembers.getSvnGitNo())
				? teamMembers.getSvnGitNo().replaceAll(" ", "") : null);

		int memberBaseCount = projectManagersDao.selectRepeatMemberBase(teamMembers);
		int teamStaffCount = projectManagersDao.selectRepeatTeamStaff(teamMembers);
		int count = 0;
		int a = 0;
		int b = 0;

		if (memberBaseCount == 0) {
			a = projectManagersDao.addTeamMemberToMemberBase(teamMembers);
		} else {
			a = projectManagersDao.updateTeamMemberToMemberBase(teamMembers);
		}

		if (teamStaffCount == 0) {
			b = projectManagersDao.addTeamMemberToTeamStaff(teamMembers);
		} else {
			b = projectManagersDao.updateTeamMemberToTeamStaff(teamMembers);
		}

		generalSituationDao.alterRankOfProjectStaff(teamMembers.getStatus(), teamMembers.getRank(),
				teamMembers.getZrAccount(), "two");
		generalSituationDao.alterRankOfTeamStaff(teamMembers.getStatus(), teamMembers.getRank(),
				teamMembers.getZrAccount(), "two");

		if (a + b > 1) {
			count = 1;
		} else {
			count = 0;
		}

		return count;
	}

	public void editTeamMemberInfo(TeamMembers teamMembers) {
		teamMembers.setName(StringUtils.isNotBlank(teamMembers.getName())
				? StringUtilsLocal.clearSpaceAndLine(teamMembers.getName()) : null);
		teamMembers.setHwAccount(StringUtils.isNotBlank(teamMembers.getHwAccount())
				? StringUtilsLocal.clearSpaceAndLine(teamMembers.getHwAccount()) : null);
		teamMembers.setSvnGitNo(StringUtils.isNotBlank(teamMembers.getSvnGitNo())
				? teamMembers.getSvnGitNo().replaceAll(" ", "") : null);

		projectManagersDao.updateTeamMemberToMemberBase(teamMembers);
		projectManagersDao.updateTeamMemberToTeamStaff(teamMembers);

		generalSituationDao.alterRankOfProjectStaff(teamMembers.getStatus(), teamMembers.getRank(),
				teamMembers.getZrAccount(), "two");
		generalSituationDao.alterRankOfTeamStaff(teamMembers.getStatus(), teamMembers.getRank(),
				teamMembers.getZrAccount(), "two");
	}

	public void deleteTeamMembers(String[] teamIds, String[] zrAccounts) {
		String teamIdss = teamIds[0];
		String zrAccountss = zrAccounts[0];
		ObjectMapper obj = new ObjectMapper();
		ArrayList<String> teamIdsArray = new ArrayList<>();
		ArrayList<String> zrAccountsArray = new ArrayList<>();
		try {
			teamIdsArray = obj.readValue(teamIdss, ArrayList.class);
			zrAccountsArray = obj.readValue(zrAccountss, ArrayList.class);
			for (int i = 0; i < teamIdsArray.size(); i++) {
				String teamId = teamIdsArray.get(i);
				String zrAccount = zrAccountsArray.get(i);
				int a = projectManagersDao.deleteTeamMembers(teamId, zrAccount);
			}
		} catch (IOException e) {
			logger.error("删除团队成员失败", e);
		}
	}

	public TeamMembers queryMemberEchoDisplay(String teamId, String zrAccount) {
		return projectManagersDao.queryMemberEchoDisplay(teamId, zrAccount);
	}

	public String getProjectPmId(String projNo) {
		return StringUtils.isNotBlank(projNo) ? projectManagersDao.getProjectPmId(projNo).replaceAll("^(0+)", "") : "";
	}

	public PersonnelInfo getZongrenliData(String userid) {
		PersonnelInfo personnelInfo = new PersonnelInfo();
		UserInfo user = UserManagerDao.getUserInfoByName(userid);
		String pmid = "";
		if (user.getUsertype().equals("2")){
			pmid = generalSituationDao.getPMZRAccountByHW(userid);
			if (null==pmid || "".equals(pmid)){
				return personnelInfo;
			}
			personnelInfo.setPmid(pmid);
		}else if (user.getUsertype().equals("1")){
			pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
           personnelInfo.setPmid(pmid);
		}
         Integer renlinum = UserManagerDao.queyMembersByPmid(personnelInfo);
		if (null == renlinum){
			personnelInfo.setRenlinum(0);
		}else {
			personnelInfo.setRenlinum(renlinum);
		}

		Integer renlinums = UserManagerDao.queyMembersByPmidofproject(personnelInfo);
		if (null == renlinums){
			personnelInfo.setRenlinums(0);
		}else {
			personnelInfo.setRenlinums(renlinums);
		}

		Integer guanjiannum = UserManagerDao.queryGuanjianByPmid(personnelInfo);
		if (null == guanjiannum){
			personnelInfo.setGuanjiannum(0);
		}else {
			personnelInfo.setGuanjiannum(guanjiannum);
		}
		Integer guanjiannums = UserManagerDao.queryGuanjianByPmidofproject(personnelInfo);
		if (null == guanjiannums){
			personnelInfo.setGuanjiannums(0);
		}else {
			personnelInfo.setGuanjiannums(guanjiannums);
		}
		return personnelInfo;
	}
	public PersonnelInfo getPersonnel(String proNo) {
		PersonnelInfo personnelInfo = new PersonnelInfo();
		Integer renlinum = UserManagerDao.queyMembersByNo(proNo);
		if (null == renlinum){
			personnelInfo.setRenlinum(0);
		}else {
			personnelInfo.setRenlinum(renlinum);
		}
		Integer guanjiannum = UserManagerDao.queryGuanjianByNo(proNo);
		if (null == guanjiannum){
			personnelInfo.setGuanjiannum(0);
		}else {
			personnelInfo.setGuanjiannum(guanjiannum);
		}
		return personnelInfo;
	}

	public Map<String, Object> getRelateProjects(String userid) {
		Map<String, Object> result = new HashMap<String, Object>();
		UserInfo user = UserManagerDao.getUserInfoByName(userid);
		String pmid ="";
		//	String proNos ="";
		if ("2".equals(user.getUsertype())){
			pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
			if (pmid==null || "".equals(pmid)){
				return result;
			}
		}else if ("1".equals(user.getUsertype())){
			pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
			if (pmid==null || "".equals(pmid)){
				return result;
			}
		}else {
			return result;
		}
		List<Map<String, Object>> list = UserManagerDao.getRelateProjects(pmid);
		result.put("rows", list);
		result.put("total", list.size());
		return result;
	}
	public Map<String, Object> getMemberinfoByZr(String zrAccount){
	    return UserManagerDao.getMemberinfoByZr(zrAccount);
	}

	public Map<String, Object> verifyHwAccount(String zrAccount,String hwAccount){
		Map<String, Object> map  = new HashMap<>();
		int memberbasecount = generalSituationDao.getHWAccount(hwAccount);
		if (memberbasecount == 0){
			map.put("code","success");
		}else{
			if (generalSituationDao.queryzrbyhw(hwAccount).equals(zrAccount)){
				map.put("code","success");
			}else {
				map.put("code","fail");
			}
		}
		return map;
	}
}
