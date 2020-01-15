package com.icss.mvp.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icss.mvp.dao.GeneralSituationDao;
import com.icss.mvp.dao.IProjectInfoDao;
import com.icss.mvp.dao.IProjectManagersDao;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.dao.ProjectLableDao;
import com.icss.mvp.entity.Label;
import com.icss.mvp.entity.ProjectCost;
import com.icss.mvp.entity.ProjectKeyroles;
import com.icss.mvp.entity.ProjectMembersLocal;
import com.icss.mvp.entity.ProjectPost;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.TeamMembers;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.system.DictEntryItemEntity;
import com.icss.mvp.service.system.DictionaryService;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringUtilsLocal;

//import com.icss.mvp.util.StringUtil;

/**
 * @ClassName: GeneralSituationService
 * @Description: 关键角色处理层
 */
@Service
@SuppressWarnings("all")
public class GeneralSituationService {

	private final static Logger logger = Logger.getLogger(GeneralSituationService.class);

	@Autowired
	private IProjectManagersDao iProjectDao;

	@Autowired
	private GeneralSituationDao dao;

	@Autowired
	private ProjectLableDao projectLableDao;

	@Autowired
	private IUserManagerDao userManagerDao;

//	@Autowired
//	private SysDictItemDao sysDictItemDao;

	@Autowired
	private GeneralSituationDao generalSituationDao;

	@Autowired
	private IProjectInfoDao projectInfoDao;

	public TableSplitResult queryProjectKeyroles(TableSplitResult page, String userid, String sort, String sortOrder) {
		TableSplitResult data = new TableSplitResult();
		List<ProjectKeyroles> dataList = new ArrayList<ProjectKeyroles>();
		String pmid ="";
		try {
			UserInfo user = userManagerDao.getUserInfoByName(userid);

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
			}
			dataList = dao.queryProjectKeyroles(page, pmid, sort, sortOrder);
		} catch (Exception e) {
			logger.error("GeneralSituationService queryProjectKeyroles failed:" + e.getMessage());
		}
		// List<ProjectKeyroles> dataList = dao.queryIteWorkManageInfo1(proNo);
		data.setRows(dataList);
		// 查询总记录条数
		Integer total = dao.queryTotalCount(pmid, page);
		data.setTotal(total);
		return data;
	}
	public TableSplitResult getProjectKeyroles(TableSplitResult page,String sort, String sortOrder) {
		TableSplitResult data = new TableSplitResult();
		List<ProjectKeyroles> dataList = new ArrayList<ProjectKeyroles>();
		try {
			dataList = dao.getProjectKeyroles(page,sort, sortOrder);
		} catch (Exception e) {
			logger.error("GeneralSituationService queryProjectKeyroles failed:" + e.getMessage());
		}
		// List<ProjectKeyroles> dataList = dao.queryIteWorkManageInfo1(proNo);
		data.setRows(dataList);
		// 查询总记录条数
		Integer total = dao.getTotalCount(page);
		data.setTotal(total);
		return data;
	}

	/**
	 * 导入历史项目关键角色、团队成员和流程指标
	 * 
	 * @param newNo
	 * @param oldNo
	 */
	public void addProRolesAndTeamMembersAndProcessIndex(String newNo, String oldNo) {
		addOldProjectKeyroles(newNo, oldNo);
		addOldProjectTeamMembers(newNo, oldNo);
		addOldProjectProcessIndex(newNo, oldNo);
	}

	public void addOldProjectKeyroles(String newNo, String oldNo) {
		dao.addOldProjectKeyroles(newNo, oldNo);
	}

	public void addOldProjectTeamMembers(String newNo, String oldNo) {
		iProjectDao.addOldProjectTeamMembers(newNo, oldNo);
	}

	public void addOldProjectProcessIndex(String newNo, String oldNo) {
		List<Label> list = projectLableDao.queryProjectAlllabs(oldNo);
		projectLableDao.deleteProjectAlllabs(newNo);
		for (Label oldLabel : list) {
			Integer oldId = oldLabel.getId();
			Label newLabel = new Label();
			newLabel.setIsDeleted(oldLabel.getIsDeleted());
			newLabel.setProNo(newNo);
			newLabel.setPlId(oldLabel.getPlId());
			projectLableDao.saveProjectAlllabs(newLabel);
			Integer newId = newLabel.getId();
			projectLableDao.saveLabelMesure(newId, oldId);
		}
	}

//	public List<SysDictItem> querySysDictItemBycode(String code) {
//		List<SysDictItem> list = new ArrayList<SysDictItem>();
//		try {
//			list = sysDictItemDao.querySysDictItemBycode(code);
//		} catch (Exception e) {
//			logger.error("sysDictItemDao.querySysDictItemBycode exception, error: "+e.getMessage());
//		}
//		return list;
//	}

	public void updateProjectKeyroles(ProjectKeyroles projectKeyroles) {
		UserInfo user = userManagerDao.getUserInfoByName(projectKeyroles.getUserid());
		String pmid ="";
		if ("2".equals(user.getUsertype())){
			pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
		}else if ("1".equals(user.getUsertype())){
			pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
		}
		projectKeyroles.setHwAccount(StringUtils.isNotBlank(projectKeyroles.getHwAccount())
				? StringUtilsLocal.clearSpaceAndLine(projectKeyroles.getHwAccount()) : null);
		projectKeyroles.setName(StringUtils.isNotBlank(projectKeyroles.getName())
				? StringUtilsLocal.clearSpaceAndLine(projectKeyroles.getName()) : null);
		projectKeyroles.setNo(projectKeyroles.getNo()==null?
				projectInfoDao.getNoByName(projectKeyroles.getReproject()):projectKeyroles.getNo());
		Map<String, Object> rankMap = new HashMap<>();
		if (pmid != null && !("".equals(pmid))){
			projectKeyroles.setPmid(pmid);
			rankMap = projectMemberEcho(projectKeyroles.getPmid(), projectKeyroles.getZrAccount());
		}

		
		if(null != rankMap){
			projectKeyroles.setRank(StringUtilsLocal.valueOf(rankMap.get("RANK")));
		}
		dao.updateKeyRoleToMemberBase(projectKeyroles);
		dao.updateKeyRoleToProjectStaff(projectKeyroles);
		
		dao.alterRankOfProjectStaff(projectKeyroles.getStatus(), projectKeyroles.getRank(),
				projectKeyroles.getZrAccount(), "two");
		dao.alterRankOfTeamStaff(projectKeyroles.getStatus(), projectKeyroles.getRank(),
				projectKeyroles.getZrAccount(), "two");
	}

	/*
	* 项目中关键角色修改保存
	*/
	public void updateProjectKeyrole(ProjectKeyroles projectKeyroles) {
		projectKeyroles.setHwAccount(StringUtils.isNotBlank(projectKeyroles.getHwAccount())
				? StringUtilsLocal.clearSpaceAndLine(projectKeyroles.getHwAccount()) : null);
		projectKeyroles.setName(StringUtils.isNotBlank(projectKeyroles.getName())
				? StringUtilsLocal.clearSpaceAndLine(projectKeyroles.getName()) : null);
		projectKeyroles.setNo(projectKeyroles.getNo()==null?
				projectInfoDao.getNoByName(projectKeyroles.getReproject()):projectKeyroles.getNo());
		Map<String, Object> rankMap = new HashMap<>();
		rankMap = projectRoleKey(projectKeyroles.getNo(), projectKeyroles.getZrAccount());


		if(null != rankMap){
			projectKeyroles.setRank(StringUtilsLocal.valueOf(rankMap.get("RANK")));
		}
		dao.updateKeyRoleToMemberBase(projectKeyroles);
		dao.updateKeyRoleToProjectStaff(projectKeyroles);

		dao.alterRankOfProjectStaff(projectKeyroles.getStatus(), projectKeyroles.getRank(),
				projectKeyroles.getZrAccount(), "two");
		dao.alterRankOfTeamStaff(projectKeyroles.getStatus(), projectKeyroles.getRank(),
				projectKeyroles.getZrAccount(), "two");
	}

	/**
	 * 新增关键角色
	 * 
	 * @param projectKeyroles
	 * @return
	 * @throws Exception
	 */
	public int addKeyRole(ProjectKeyroles projectKeyroles) throws Exception {
		int count = 0;
		UserInfo user = userManagerDao.getUserInfoByName(projectKeyroles.getUserid());
		String pmid ="";
		if ("2".equals(user.getUsertype())){
			pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
			if (pmid==null || "".equals(pmid)){
				count = 2;
				return count;
			}
			projectKeyroles.setPmid(pmid);
		}else if ("1".equals(user.getUsertype())){
			pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
			projectKeyroles.setPmid(pmid);
		}

		if (projectKeyroles.getReproject()!=null && !("".equals(projectKeyroles.getReproject()))){
			projectKeyroles.setNo(projectInfoDao.getNoByName(projectKeyroles.getReproject()));
		}else {
			count = 3;
			return count;
		}

		projectKeyroles.setZrAccount(StringUtils.isNotBlank(projectKeyroles.getZrAccount())
				? StringUtilsLocal.zeroFill(StringUtilsLocal.clearSpaceAndLine(projectKeyroles.getZrAccount()), 10) : null);
		projectKeyroles.setHwAccount(StringUtils.isNotBlank(projectKeyroles.getHwAccount())
				? StringUtilsLocal.clearSpaceAndLine(projectKeyroles.getHwAccount()) : null);
		projectKeyroles.setName(StringUtils.isNotBlank(projectKeyroles.getName())
				? StringUtilsLocal.clearSpaceAndLine(projectKeyroles.getName()) : null);
		projectKeyroles.setStartofdate(	DateUtils.parseDate(projectKeyroles.getStartDate(),"yyyy-MM-dd"));
		projectKeyroles.setEndofdate(DateUtils.parseDate(projectKeyroles.getEndDate(),"yyyy-MM-dd"));
		//projectKeyroles.setRole(generalSituationDao.getvalueBykey(projectKeyroles.getRole()));
	//	Map<String, Object> map = dao.queryProjectDate(projectKeyroles.getPmid());
		Map<String, Object> rankMap = projectMemberEcho(projectKeyroles.getPmid(), projectKeyroles.getZrAccount());

		if(null != rankMap){
			projectKeyroles.setRank(StringUtilsLocal.valueOf(rankMap.get("RANK")));
		}
		int memberBaseCount = dao.selectRepeatMemberBase(projectKeyroles);
		int projectStaffCount = dao.selectRepeatProjectStaff(projectKeyroles);

		int a = 0;
		int b = 0;
		String status = projectKeyroles.getStatus();

		if (status.equals("1")) {
			projectKeyroles.setStatus("在岗");
		} else if (status.equals("2")) {
			projectKeyroles.setStatus("后备");
		} else if (status.equals("3")) {
			projectKeyroles.setStatus("离职");
		} else {
			projectKeyroles.setStatus(status);
		}

		/**if (null != map && map.size() > 0) {
			projectKeyroles.setStartDate(StringUtils.isNotBlank(StringUtil.Obj2Stri(map.get("START_DATE")))
					? StringUtil.Obj2Stri(map.get("START_DATE")) : "");
			projectKeyroles.setEndDate(StringUtils.isNotBlank(StringUtil.Obj2Stri(map.get("END_DATE")))
					? StringUtil.Obj2Stri(map.get("END_DATE")) : "");
		}*/

		if (memberBaseCount == 0) {
			a = dao.addKeyRoleToMemberBase(projectKeyroles);
		} else {
			a = dao.updateKeyRoleToMemberBase(projectKeyroles);
		}

		if (projectStaffCount == 0) {
			b = dao.addKeyRoleToProjectStaff(projectKeyroles);
		} else {
			b = dao.updateKeyRoleToProjectStaff(projectKeyroles);
		}
		
		dao.alterRankOfProjectStaff(status, projectKeyroles.getRank(), projectKeyroles.getZrAccount(), "two");
		dao.alterRankOfTeamStaff(status, projectKeyroles.getRank(), projectKeyroles.getZrAccount(), "two");

		if (a + b > 1) {
			count = 1;
		} else {
			count = 0;
		}

		return count;
		/*
		 * if (memberBaseCount == 0) {
		 * dao.addKeyRoleToMemberBase(projectKeyroles);
		 * dao.addKeyRoleToProjectStaff(projectKeyroles); count = 1; } else {
		 * dao.updateKeyRoleToMemberBase(projectKeyroles);
		 * dao.updateKeyRoleToProjectStaff(projectKeyroles); count = 1; }
		 */
	}
	/**
	 * 项目中新增关键角色
	 *
	 * @param projectKeyroles
	 * @return
	 * @throws Exception
	 */
	public int addProjectKeyRole(ProjectKeyroles projectKeyroles) throws Exception {
		int count = 0;
		if (projectKeyroles.getNo()==null || "".equals(projectKeyroles.getNo())){
			count = 3;
			return count;
		}

		projectKeyroles.setZrAccount(StringUtils.isNotBlank(projectKeyroles.getZrAccount())
				? StringUtilsLocal.zeroFill(StringUtilsLocal.clearSpaceAndLine(projectKeyroles.getZrAccount()), 10) : null);
		projectKeyroles.setHwAccount(StringUtils.isNotBlank(projectKeyroles.getHwAccount())
				? StringUtilsLocal.clearSpaceAndLine(projectKeyroles.getHwAccount()) : null);
		projectKeyroles.setName(StringUtils.isNotBlank(projectKeyroles.getName())
				? StringUtilsLocal.clearSpaceAndLine(projectKeyroles.getName()) : null);
		Map<String, Object> rankMap = projectRoleKey(projectKeyroles.getNo(), projectKeyroles.getZrAccount());

		if(null != rankMap){
			projectKeyroles.setRank(StringUtilsLocal.valueOf(rankMap.get("RANK")));
		}
		int memberBaseCount = dao.selectRepeatMemberBase(projectKeyroles);
		int projectStaffCount = dao.selectRepeatProjectStaff(projectKeyroles);

		int a = 0;
		int b = 0;
		String status = projectKeyroles.getStatus();

		if (status.equals("1")) {
			projectKeyroles.setStatus("在岗");
		} else if (status.equals("2")) {
			projectKeyroles.setStatus("后备");
		} else if (status.equals("3")) {
			projectKeyroles.setStatus("离职");
		} else {
			projectKeyroles.setStatus(status);
		}

		if (memberBaseCount == 0) {
			a = dao.addKeyRoleToMemberBase(projectKeyroles);
		} else {
			a = dao.updateKeyRoleToMemberBase(projectKeyroles);
		}

		if (projectStaffCount == 0) {
			b = dao.addKeyRoleToProjectStaff(projectKeyroles);
		} else {
			b = dao.updateKeyRoleToProjectStaff(projectKeyroles);
		}

		dao.alterRankOfProjectStaff(status, projectKeyroles.getRank(), projectKeyroles.getZrAccount(), "two");
		dao.alterRankOfTeamStaff(status, projectKeyroles.getRank(), projectKeyroles.getZrAccount(), "two");

		if (a + b > 1) {
			count = 1;
		} else {
			count = 0;
		}

		return count;
	}

	public void deleteProjectKeyroles(String[] nos, String[] zrAccounts) {
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
				dao.deleteProjectStaff(no, zrAccount);
			}
		} catch (IOException e) {
			logger.error("dao.deleteProjectStaff exception, error: "+e.getMessage());
		}

	}

	public void deleteProjectKeyrole(String no, String zrAccount) {
		try {
			dao.deleteProjectStaff(no, zrAccount);
		} catch (Exception e) {
			logger.error("dao.deleteProjectStaff exception, error: "+e.getMessage());
		}

	}

	/**
	 * 重要角色管理编辑页回显
	 * 
	 * @param id
	 * @return
	 */
	public ProjectKeyroles openEditPage(String no, String zrAccount) throws Exception {
		return dao.openEditPage(no, zrAccount);
	}

	/**
	 * 分页查询团队成员
	 * 
	 * @param page
	 * @param proNo
	 * @param sort
	 * @param sortOrder
	 * @return
	 */
	public TableSplitResult queryTeamMembers(TableSplitResult page, String teamId, String sort, String sortOrder) {
		TableSplitResult data = new TableSplitResult();
		try {
			List<TeamMembers> dataList = dao.queryTeamMembers(page, teamId, sort, sortOrder);
			
			if (null != dataList && dataList.size() > 0) {
				for (TeamMembers teamMembers : dataList) {
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
				} 
			}
			data.setRows(dataList);
			// 查询总记录条数
			Integer total = dao.queryMembersCount(teamId);
			data.setTotal(total);
		} catch (Exception e) {
			logger.error("团队成员查询失败", e);
		}
		return data;
	}

    @Autowired
    DictionaryService dictionaryService;

    public void projectPostHierarchy(String userid, String position, String demand) {
        DictEntryItemEntity entryItem = dictionaryService.getItem("position", position);

        // List<Map<String, Object>> item = dao.sysDictItemSelect(11, position);
        // Object obj = item.get(0).get("val");
        // String name = obj == null ? "" : String.valueOf(obj);
        // obj = item.get(0).get("id");
        // String dict_item_id = obj == null ? "" : String.valueOf(obj);

        Map<String, Object> data = new HashMap<String, Object>();
		UserInfo user = userManagerDao.getUserInfoByName(userid);
		String pmid ="";
		//	String proNos ="";
		if ("2".equals(user.getUsertype())){
			pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
		}else if ("1".equals(user.getUsertype())){
			pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
		}
        data.put("NAME", entryItem == null ? "" : entryItem.getValue());
        data.put("pmid", pmid==null? "" : pmid );
        data.put("POSITION", position);
        data.put("DEMAND", demand);
        data.put("dict_item_id", entryItem == null ? "" : entryItem.getId());

        dao.projectPostHierarchyInsert(data);
    }

	public List<ProjectPost> organizationalStructure(String userid) {
    	List<ProjectPost> lists = new ArrayList<>();
		UserInfo user = userManagerDao.getUserInfoByName(userid);
		String pmid ="";
		//	String proNos ="";
		if ("2".equals(user.getUsertype())){
			pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
			if (pmid==null || "".equals(pmid)){
				return lists;
			}
		}else if ("1".equals(user.getUsertype())){
			pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
			if (pmid==null || "".equals(pmid)){
				return lists;
			}
		}
		return dao.projectPostHierarchySelect(pmid);
	}

	public List<ProjectPost> organizationalHierarchical(String projNo) {
		List<ProjectPost> data = new ArrayList<>();
		List<ProjectPost> list = dao.organizationalHierarchical(projNo);
		List<String> names = new ArrayList<>();
		List<Integer> sequence = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ProjectPost map = list.get(i);
			String name = map.getName();
			if (names.contains(name)) {
				continue;
			}
			String PARENT_ORDER = map.getSuperiorSequence();
			if ("0".equals(PARENT_ORDER)) {
				map.setSuperior("PM");
			}
			data.add(map);
			names.add(name);
		}
		return data;
	}

	public void demandUpdata(String Id, String value, String userid) {
		dao.demandUpdata(Id, value);

		int count = 0;
		List<Map<String, Object>> list = new ArrayList<>();
		UserInfo user = userManagerDao.getUserInfoByName(userid);
		String pmid ="";
		//	String proNos ="";
		if ("2".equals(user.getUsertype())){
			pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
			if (pmid!=null && !("".equals(pmid))){
				list = userManagerDao.queryOldProjectPost(pmid);
			}
		}else if ("1".equals(user.getUsertype())){
			pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
			if (pmid!=null && !("".equals(pmid))){
				list = userManagerDao.queryOldProjectPost(pmid);
			}
		}
		if (null != list && list.size() > 0) {
			for (Map<String, Object> map : list) {
				count += Integer.valueOf(StringUtilsLocal.valueOf(map.get("DEMAND")));
			}

			dao.updateProjectKeyRoleCount(pmid, String.valueOf(count));
		}
	}

	public void delDemandPopulation(String ID) {
		dao.delDemandPopulation(ID);
	}

	public List<Map<String, Object>> getSelectNames(String userid) {
		List<Map<String, Object>> data = new ArrayList<>();
		UserInfo user = userManagerDao.getUserInfoByName(userid);
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
		}
		List<String> names = dao.getSelectNames(pmid);
		for (int i = 0; i < names.size(); i++) {
			Object obj = names.get(i);
			String name = obj == null ? "" : String.valueOf(obj);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", name);
			map.put("value", name);
			data.add(map);
		}
		return data;
	}

    public List<Map<String, Object>> getSelectRoles(String userid, String type) {
    	List<Map<String, Object>> data = new ArrayList<>();
    	UserInfo user = userManagerDao.getUserInfoByName(userid);
		String pmid ="";
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
		}
        // if ("1".equals(type)) {
        // data = dao.getSelectPost(projNo);
        // }
        // if ("2".equals(type)) {
        // data = dao.getSelectDict(projNo);
        // }
        // return data;

        return dao.getSelectPost(pmid, type);
    }

	public ProjectKeyroles memberEchoDisplay(String no,String userid, String zrAccount) {
		ProjectKeyroles projectKeyroles = new ProjectKeyroles();
		
		String zr = "";
		if (StringUtils.isNotBlank(zrAccount) && zrAccount.length() <= 10) {
			zr = StringUtilsLocal.zeroFill(zrAccount, 10);
			UserInfo user = userManagerDao.getUserInfoByName(userid);
			String pmid ="";
			//	String proNos ="";
			if ("2".equals(user.getUsertype())){
				pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
				if (pmid==null || "".equals(pmid)){
					return projectKeyroles;
				}
			}else if ("1".equals(user.getUsertype())){
				pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
				if (pmid==null || "".equals(pmid)){
					return projectKeyroles;
				}
			}
			ProjectKeyroles pk = dao.queryMemberEchoDisplay(no,pmid, StringUtils.isNotBlank(zr) ? zr : zrAccount);
			Map<String, Object> rankMap = newprojectMemberEcho(no,pmid, zr);
			
			if (null != pk) {
				projectKeyroles.setUserid(userid);
				projectKeyroles.setPmid(pmid);
				projectKeyroles.setZrAccount(pk.getZrAccount());
				projectKeyroles.setRole(pk.getRole());
				projectKeyroles.setReproject(pk.getNo()==null? "" : projectInfoDao.getNameByNo(pk.getNo()));
				projectKeyroles.setNo(pk.getNo()==null? "" : pk.getNo());
				projectKeyroles.setRdpmExam(pk.getRdpmExam());
				projectKeyroles.setReplyResults(pk.getReplyResults());
				projectKeyroles.setProCompetence(pk.getProCompetence());
			}
			
			if(null != rankMap){
				projectKeyroles.setName(StringUtilsLocal.valueOf(rankMap.get("NAME")));
				projectKeyroles.setHwAccount(StringUtilsLocal.valueOf(rankMap.get("HW_ACCOUNT")));
				projectKeyroles.setStatus(StringUtilsLocal.valueOf(rankMap.get("STATUS")));
			}
		}
		return projectKeyroles;
	}
	public ProjectKeyroles editProjectPage(String no, String zrAccount) {
		ProjectKeyroles projectKeyroles = new ProjectKeyroles();

		String zr = "";
		if (StringUtils.isNotBlank(zrAccount) && zrAccount.length() <= 10) {
			zr = StringUtilsLocal.zeroFill(zrAccount, 10);
			ProjectKeyroles pk = dao.getProjectKeyrole(no,StringUtils.isNotBlank(zr) ? zr : zrAccount);
			Map<String, Object> rankMap = projectRoleKey(no, zr);

			if (null != pk) {
				projectKeyroles.setZrAccount(pk.getZrAccount());
				projectKeyroles.setRole(pk.getRole());
				projectKeyroles.setReproject(pk.getNo()==null? "" : projectInfoDao.getNameByNo(pk.getNo()));
				projectKeyroles.setNo(pk.getNo()==null? "" : pk.getNo());
				projectKeyroles.setRdpmExam(pk.getRdpmExam());
				projectKeyroles.setReplyResults(pk.getReplyResults());
				projectKeyroles.setProCompetence(pk.getProCompetence());
			}

			if(null != rankMap){
				projectKeyroles.setName(StringUtilsLocal.valueOf(rankMap.get("NAME")));
				projectKeyroles.setHwAccount(StringUtilsLocal.valueOf(rankMap.get("HW_ACCOUNT")));
				projectKeyroles.setStatus(StringUtilsLocal.valueOf(rankMap.get("STATUS")));
			}
		}
		return projectKeyroles;
	}

	public TableSplitResult<List<ProjectCost>> projectCost(PageRequest pageRequest, String projNo,
			String statisticalTime, String nextTime, String flag) {
		TableSplitResult<List<ProjectCost>> result = new TableSplitResult<>();
		int total = dao.getMemberSize(projNo, statisticalTime, nextTime, StringUtilsLocal.zeroFill(flag, 10));

		if (total <= 0) {
			return new TableSplitResult<>();
		} else {
			List<ProjectCost> memberList = queryMemberCost(pageRequest, projNo, statisticalTime, nextTime, StringUtilsLocal.zeroFill(
                    flag, 10));
			result.setRows(memberList);
			result.setTotal(total);
			return result;
		}
	}

	private List<ProjectCost> queryMemberCost(PageRequest pageRequest, String projNo, String statisticalTime,
			String nextTime, String flag) {
		List<ProjectCost> memberList = dao.getMemberList(projNo, pageRequest, statisticalTime, nextTime, flag);
		List<ProjectCost> list = dao.getProjectCost(projNo, statisticalTime, nextTime, flag);
		DecimalFormat df = new DecimalFormat(".##");

		if (null != memberList && memberList.size() > 0) {
			for (ProjectCost cost : memberList) {
				String zrAccount = cost.getZrAccount();
				if (null != list && list.size() > 0) {
					for (ProjectCost costs : list) {
						if (zrAccount.equals(costs.getZrAccount())) {
							double attendence = StringUtils.isNotBlank(costs.getAttendence())
									? Double.parseDouble(costs.getAttendence()) : 0.0;
							double overtime = StringUtils.isNotBlank(costs.getOvertime())
									? Double.parseDouble(costs.getOvertime()) : 0.0;
							if ("0".equals(costs.getDay())) {
								cost.setAttendenceFirst(attendence);
								cost.setOvertimeFirst(overtime);
							} else if ("1".equals(costs.getDay())) {
								cost.setAttendenceSecond(attendence);
								cost.setOvertimeSecond(overtime);
							} else if ("2".equals(costs.getDay())) {
								cost.setAttendenceThird(attendence);
								cost.setOvertimeThird(overtime);
							} else if ("3".equals(costs.getDay())) {
								cost.setAttendenceFourth(attendence);
								cost.setOvertimeFourth(overtime);
							} else if ("4".equals(costs.getDay())) {
								cost.setAttendenceFifth(attendence);
								cost.setOvertimeFifth(overtime);
							} else if ("5".equals(costs.getDay())) {
								cost.setAttendenceSixth(attendence);
								cost.setOvertimeSixth(overtime);
							} else if ("6".equals(costs.getDay())) {
								cost.setAttendenceSeventh(attendence);
								cost.setOvertimeSeventh(overtime);
							}
						}
					}
				}
				cost.setTotalHours(Double.parseDouble(df.format(cost.getAttendenceFirst() + cost.getOvertimeFirst()
						+ cost.getAttendenceSecond() + cost.getOvertimeSecond() + cost.getAttendenceThird()
						+ cost.getOvertimeThird() + cost.getAttendenceFourth() + cost.getOvertimeFourth()
						+ cost.getAttendenceFifth() + cost.getOvertimeFifth() + cost.getAttendenceSixth()
						+ cost.getOvertimeSixth() + cost.getAttendenceSeventh() + cost.getOvertimeSeventh())));
			}
		}
		return memberList;
	}

	public void updateMemberCost(ProjectCost projectCost) {
		double att = 0.0;
		double ove = 0.0;
		String date = "";
		Date date1 = null;
		try {
			date1 = DateUtils.SHORT_FORMAT_GENERAL.parse(projectCost.getStatisticalTime());
		} catch (ParseException e) {
			logger.error("updateMemberCost failed:" + e.getMessage());
		}

		if ("0".equals(projectCost.getDay())) {
			att = projectCost.getAttendenceFirst();
			ove = projectCost.getOvertimeFirst();
			date = DateUtils.getAroundDay(date1, -6);
		} else if ("1".equals(projectCost.getDay())) {
			att = projectCost.getAttendenceSecond();
			ove = projectCost.getOvertimeSecond();
			date = DateUtils.getAroundDay(date1, -5);
		} else if ("2".equals(projectCost.getDay())) {
			att = projectCost.getAttendenceThird();
			ove = projectCost.getOvertimeThird();
			date = DateUtils.getAroundDay(date1, -4);
		} else if ("3".equals(projectCost.getDay())) {
			att = projectCost.getAttendenceFourth();
			ove = projectCost.getOvertimeFourth();
			date = DateUtils.getAroundDay(date1, -3);
		} else if ("4".equals(projectCost.getDay())) {
			att = projectCost.getAttendenceFifth();
			ove = projectCost.getOvertimeFifth();
			date = DateUtils.getAroundDay(date1, -2);
		} else if ("5".equals(projectCost.getDay())) {
			att = projectCost.getAttendenceSixth();
			ove = projectCost.getOvertimeSixth();
			date = DateUtils.getAroundDay(date1, -1);
		} else if ("6".equals(projectCost.getDay())) {
			att = projectCost.getAttendenceSeventh();
			ove = projectCost.getOvertimeSeventh();
			date = DateUtils.getAroundDay(date1, 0);
		}

		projectCost.setAttendence(String.format("%.1f", att));
		projectCost.setOvertime(String.format("%.1f", ove));
		projectCost.setTime(date);

		dao.updateMemberCost(projectCost);
	}

	public List<ProjectCost> exportXMCB(String proNo, String date, String nextDate) {
		return queryMemberCost(new PageRequest(), proNo, date, nextDate, "");
	}

	public void updateProjectBudget(String projectBudget, String projNo) {
		dao.updateProjectBudget(StringUtils.isNotBlank(projectBudget) ? projectBudget : "0.00", projNo);
	}

	public void updateHourByDay(String day, String week, String projNo, String mark, String flag) {
		if (StringUtils.isNotBlank(day) && StringUtils.isNotBlank(week) && StringUtils.isNotBlank(projNo)
				&& StringUtils.isNotBlank(mark)) {
			List<ProjectCost> list = dao.getMemberByDay(day, projNo, StringUtilsLocal.zeroFill(flag, 10));

			if (null != list && list.size() > 0) {
				for (ProjectCost ps : list) {
					ps.setDay(week);
					ps.setTime(day);
					ps.setAttendence("cz".equals(mark) ? "0.0" : "8.0");
				}

				dao.updateHour(list);
			}
		} else {
			logger.error("GeneralSituationService updateHourByDay parameter error!");
		}
	}

	public void updateHourByZrAccount(String zrAccount, String[] dayArr, String projNo, String mark) {
		if (StringUtils.isNotBlank(zrAccount) && StringUtils.isNotBlank(projNo) && StringUtils.isNotBlank(mark)
			&& (null != dayArr && dayArr.length > 0)) {
			List<ProjectCost> list = new ArrayList<>();
			int max = "cz".equals(mark) ? 7 : 5;
			
			for (int i = 0; i < max; i++) {
				String week = dayArr[i];
				countMember(zrAccount, projNo, list, i, week, mark);
			}
			if (null != list && list.size() > 0) {
				dao.updateHour(list);
			}
		} else {
			logger.error("GeneralSituationService updateHourByZrAccount parameter error!");
		}
	}

	public void oneClickInput(String[] dayArr, String projNo, String mark, String flag) {
		if (StringUtils.isNotBlank(projNo) && StringUtils.isNotBlank(mark) && (null != dayArr && dayArr.length > 0)) {
			List<ProjectCost> list = new ArrayList<>();
			List<ProjectCost> memberList = dao.getMemberByDay(null, projNo, StringUtilsLocal.zeroFill(flag, 10));

			if (null != memberList && memberList.size() > 0) {
				int max = "cz".equals(mark) ? 7 : 5;
				
				for (int i = 0; i < max; i++) {
					String week = dayArr[i];
					for (ProjectCost pc : memberList) {
						countMember(pc.getZrAccount(), projNo, list, i, week, mark);
					}
				}
				if (null != list && list.size() > 0) {
					dao.updateHour(list);
				}
			}
		} else {
			logger.error("GeneralSituationService oneClickInput parameter error!");
		}
	}
	
	private void countMember(String zrAccount, String projNo, List<ProjectCost> list, int i, String week, String mark) {
		int count = dao.getPayroll(zrAccount, projNo, week);

		if (count == 1) {
			ProjectCost projectCost = new ProjectCost();

			projectCost.setZrAccount(zrAccount);
			projectCost.setDay(String.valueOf(i));
			projectCost.setTime(week);
			projectCost.setNo(projNo);
			projectCost.setAttendence("cz".equals(mark) ? "0.0" : "8.0");

			list.add(projectCost);
		}
	}

	public List<String> getMemberAccount(String projNo) {
		List<String> list = new ArrayList<>();
		List<ProjectMembersLocal> accountList = StringUtils.isNotBlank(projNo) ? dao.getMemberAccount(projNo)
				: new ArrayList<>();

		for (ProjectMembersLocal projectMembersLocal : accountList) {
			if (StringUtils.isNotBlank(projectMembersLocal.getZrAccount())) {
				list.add(projectMembersLocal.getZrAccount().replaceAll("^(0+)", ""));
			}
			list.add(projectMembersLocal.getHwAccount());
		}

		return list;
	}

	public int getHWAccount(String username) {
		return StringUtils.isNotBlank(username) ? dao.getHWAccount(username) : 0;
	}

	public Integer addZRAccount(String zrAccount, String hwAccount) {
		int count = 0;
		if (StringUtils.isNotBlank(zrAccount)) {
			count =	dao.getzrAccount(StringUtilsLocal.zeroFill(zrAccount, 10)) > 0 ? count : dao.addZRAccount(StringUtilsLocal.zeroFill(zrAccount, 10), hwAccount);
		}

		return count ;
	}

	public String getPMZRAccountByNo(String projNo) {
		return StringUtils.isNotBlank(projNo) ? dao.getPMZRAccountByNo(projNo) : "";
	}

	public String getPMZRAccountByHW(String hwAccount) {
		return StringUtils.isNotBlank(hwAccount) ? dao.getPMZRAccountByHW(hwAccount) : "";
	}

	public Map<String, Object> projectMemberEcho(String pmid, String zrAccount) {
		Map<String, Object> map = new HashMap<>();

		return basicAndInfo(pmid, zrAccount, map, "project");
	}
    public Map<String, Object> newprojectMemberEcho(String no ,String pmid, String zrAccount) {
        Map<String, Object> map = new HashMap<>();

        return newbasicAndInfo(no ,pmid, zrAccount, map, "project");
    }
	public Map<String, Object> projectRoleKey(String proNo, String zrAccount) {
		Map<String, Object> map = new HashMap<>();

		return basicAndInfo(proNo, zrAccount, map, "projectRoleKey");
	}

	public Map<String, Object> teamMemberEcho(String teamId, String zrAccount) {
		Map<String, Object> map = new HashMap<>();
		
		return basicAndInfo(teamId, zrAccount, map, "team");
	}
	
	private Map<String, Object> basicAndInfo(String pmid, String zrAccount, Map<String, Object> map, String mark) {
        if (StringUtils.isNotBlank(pmid) && StringUtils.isNotBlank(zrAccount)) {
            try {
                Map<String, Object> selfRank = dao.selfRank(StringUtilsLocal.zeroFill(zrAccount, 10));
                Map<String, Object> importRank = dao.importRank(StringUtilsLocal.zeroFill(zrAccount, 10));

                map.put("STATUS",
                        null != selfRank && StringUtils.isNotBlank(StringUtilsLocal.valueOf(selfRank.get("STATUS")))
                                ? selfRank.get("STATUS") : null != importRank ? importRank.get("STATUS") : null);
                map.put("RANK", null != selfRank && StringUtils.isNotBlank(
                        StringUtilsLocal.valueOf(selfRank.get("RANK")))
                        ? selfRank.get("RANK") : null != importRank ? importRank.get("RANK") : null);
                Map<String, Object> mb = dao.memberBasic(StringUtilsLocal.zeroFill(zrAccount, 10));
                Map<String, Object> mi = dao.memberInfo(pmid, StringUtilsLocal.zeroFill(zrAccount, 10), mark);

                map.put("ROLE", null != mi ? mi.get("ROLE") : null);
                map.put("SVN_GIT_NO", null != mi ? mi.get("SVN_GIT_NO") : null);

                map.put("HW_ACCOUNT", null != mb ? mb.get("HW_ACCOUNT") : null);
                map.put("NAME", null != mb ? mb.get("NAME") : null);

            } catch (Exception e) {
                logger.error("GeneralSituationService basicAndInfo failed:" + e.getMessage());
            }
        }
        return map;
    }

    private Map<String, Object> newbasicAndInfo(String no,String pmid, String zrAccount, Map<String, Object> map, String mark) {
        if (StringUtils.isNotBlank(pmid) && StringUtils.isNotBlank(zrAccount)) {
            try {
                Map<String, Object> selfRank = dao.selfRank(StringUtilsLocal.zeroFill(zrAccount, 10));
                Map<String, Object> importRank = dao.importRank(StringUtilsLocal.zeroFill(zrAccount, 10));

                map.put("STATUS",
                        null != selfRank && StringUtils.isNotBlank(StringUtilsLocal.valueOf(selfRank.get("STATUS")))
                                ? selfRank.get("STATUS") : null != importRank ? importRank.get("STATUS") : null);
                map.put("RANK", null != selfRank && StringUtils.isNotBlank(
                        StringUtilsLocal.valueOf(selfRank.get("RANK")))
                        ? selfRank.get("RANK") : null != importRank ? importRank.get("RANK") : null);
                Map<String, Object> mb = dao.memberBasic(StringUtilsLocal.zeroFill(zrAccount, 10));
                Map<String, Object> mi = dao.newmemberInfo(no ,pmid, StringUtilsLocal.zeroFill(zrAccount, 10), mark);

                map.put("ROLE", null != mi ? mi.get("ROLE") : null);
                map.put("SVN_GIT_NO", null != mi ? mi.get("SVN_GIT_NO") : null);

                map.put("HW_ACCOUNT", null != mb ? mb.get("HW_ACCOUNT") : null);
                map.put("NAME", null != mb ? mb.get("NAME") : null);

            } catch (Exception e) {
                logger.error("GeneralSituationService basicAndInfo failed:" + e.getMessage());
            }
        }
        return map;
    }
	public BaseResponse resetRank(String zrAccount, String rank) {
		BaseResponse response = new BaseResponse();
		Map<String, Object> rankMap = null;

		if (StringUtils.isNotBlank(zrAccount) && StringUtils.isNotBlank(rank)) {
			rankMap = dao.importRank(zrAccount);

			if (null != rankMap) {
				rank = StringUtilsLocal.valueOf(rankMap.get("RANK"));
				response.setCode("success");
			} else {
				response.setCode("warning");
			}
			dao.alterRankOfProjectStaff("", "请选择".equals(rank) ? "" : rank, zrAccount, "one");
			dao.alterRankOfTeamStaff("", "请选择".equals(rank) ? "" : rank, zrAccount, "one");
		}

		return response;
	}
	
}
