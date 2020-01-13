package com.icss.mvp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.ProjectCost;
import com.icss.mvp.entity.ProjectKeyroles;
import com.icss.mvp.entity.ProjectPost;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.system.DictEntryItemEntity;
import com.icss.mvp.service.GeneralSituationService;
import com.icss.mvp.service.system.DictionaryService;

/**
 * 概况
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/GeneralSituation")
@SuppressWarnings("all")
public class GeneralSituationController {

    @Autowired private GeneralSituationService service;

    //	@Autowired
    //	private  UserManagerService userService;

    private static Logger logger = Logger.getLogger(GeneralSituationController.class);

    /**
     * 分页查询关键角色
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryByPage", method = RequestMethod.POST, consumes = "application/json") @ResponseBody
	public TableSplitResult queryKeyroles(HttpServletRequest request) {
        String roleName = StringUtils.isEmpty(request.getParameter("rolename")) ? "" : request.getParameter("rolename");
        //		String prior = TextUtils.isEmpty(request.getParameter("prior"))?"":request.getParameter("prior");
        String userid = StringUtils.isEmpty(request.getParameter("userid")) ? "" : request.getParameter("userid");
        //		String status = TextUtils.isEmpty(request.getParameter("status"))?"":request.getParameter("status");
        String zrAccount = StringUtils.isEmpty(request.getParameter("zrAccount")) ? "" : request.getParameter(
                "zrAccount");
        String position = StringUtils.isEmpty(request.getParameter("position")) ? "" : request.getParameter("position");
        try {
            roleName = URLDecoder.decode(roleName, "UTF-8");
            position = URLDecoder.decode(position, "UTF-8");

        } catch (UnsupportedEncodingException e) {
			logger.error("URLDecoder.decode exception, error: "+e.getMessage());
        }
        int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));//每页显示条数
    	int offset = request.getParameter("offset") == null?0:Integer.parseInt(request.getParameter("offset"));//第几页
    	String sort = StringUtils.isEmpty(request.getParameter("sort"))?"":request.getParameter("sort");//排序字段
    	sort = transIntoSqlChar(sort);
    	String sortOrder = StringUtils.isEmpty(request.getParameter("sortOrder"))?"":request.getParameter("sortOrder");//排序方式
    	TableSplitResult page = new TableSplitResult();
    	page.setPage(offset);
        page.setRows(limit);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", roleName);
//        map.put("prior", prior);
        map.put("zrAccount", zrAccount);
        map.put("userid", userid);
//        map.put("status", status);
        map.put("position", position);
        page.setQueryMap(map);
        page = service.queryProjectKeyroles(page,userid,sort,sortOrder);
		return page;
	}

    /**
     * 项目中分页查询关键角色
     * @param request
     * @return
     */
    @RequestMapping(value = "/getProjectKeyroles", method = RequestMethod.POST, consumes = "application/json") @ResponseBody
	public TableSplitResult getProjectKeyroles(HttpServletRequest request) {
        String roleName = StringUtils.isEmpty(request.getParameter("rolename")) ? "" : request.getParameter("rolename");
        String proNo = StringUtils.isEmpty(request.getParameter("proNo")) ? "" : request.getParameter("proNo");
        String zrAccount = StringUtils.isEmpty(request.getParameter("zrAccount")) ? "" : request.getParameter(
                "zrAccount");
        String position = StringUtils.isEmpty(request.getParameter("position")) ? "" : request.getParameter("position");
		int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));//每页显示条数
		int offset = request.getParameter("offset") == null?0:Integer.parseInt(request.getParameter("offset"));//第几页
		try {
			roleName = URLDecoder.decode(roleName, "UTF-8");
			position = URLDecoder.decode(position, "UTF-8");

		} catch (UnsupportedEncodingException e) {
			logger.error("URLDecoder.decode exception, error: "+e.getMessage());
		}
    	String sort = StringUtils.isEmpty(request.getParameter("sort"))?"":request.getParameter("sort");//排序字段
    	sort = transIntoSqlChar(sort);
    	String sortOrder = StringUtils.isEmpty(request.getParameter("sortOrder"))?"":request.getParameter("sortOrder");//排序方式
    	TableSplitResult page = new TableSplitResult();
    	page.setPage(offset);
        page.setRows(limit);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", roleName);
        map.put("zrAccount", zrAccount);
        map.put("proNo", proNo);
        map.put("position", position);
        page.setQueryMap(map);
        page = service.getProjectKeyroles(page,sort,sortOrder);
		return page;
	}

    @Autowired
    DictionaryService dictionaryService;

    @RequestMapping(value = "/getSelectVal1", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ListResponse getItems(HttpServletRequest request) {
        ListResponse result = new ListResponse();

        String entryCode = request.getParameter("type");
        entryCode = StringUtils.isBlank(entryCode) ? "" : entryCode;

        DictEntryItemEntity entity = new DictEntryItemEntity();
        entity.setEntryCode(entryCode);
        List<DictEntryItemEntity> entryItems = dictionaryService.listAllEntryItems(entity);

        List<Map> data = new ArrayList<>(0);
        if (CollectionUtils.isNotEmpty(entryItems)) {
            for (DictEntryItemEntity item : entryItems) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("text", item.getKey());
                map.put("value", "position".equals(entryCode) ? item.getKey() : item.getValue());

                data.add(map);
            }
        }

        result.setData(data);

        return result;
    }

//	/**
//	 * 根据名称查询状态
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/getSelectVal1",method = RequestMethod.POST,consumes = "application/json")
//	@ResponseBody
//	public ListResponse<Map<String, Object>> getSelectVal1(HttpServletRequest request){
//		ListResponse<Map<String, Object>> response = new ListResponse<Map<String, Object>>();
//		List<Map<String, Object> > dicts =new ArrayList<Map<String, Object> >();
//
//		String type = StringUtils.isEmpty(request.getParameter("type"))?"":request.getParameter("type");
//		try {
//			 List<SysDictItem>  model =service.querySysDictItemBycode(type);
//			if(model!=null&&model.size()>0){
//				for (SysDictItem sysDictItem : model) {
//					Map<String, Object> map = new HashMap<String, Object>();
//					if("position".equals(type)) {
//						map.put("value", sysDictItem.getName());
//					}else {
//						map.put("value", sysDictItem.getVal());
//					}
//					map.put("text", sysDictItem.getName());
//					dicts.add(map);
//				}
//			}
//			response.setCode("success");
//			response.setData(dicts);
//		} catch (Exception e) {
//			response.setCode("failure");
//			response.setMessage("操作失败");
//		}
//		return response;
//	}

	@RequestMapping("/getSelectNames")
	@ResponseBody
	public ListResponse<Map<String, Object>> getSelectNames(String userid){
		ListResponse<Map<String, Object>> response = new ListResponse<Map<String, Object>>();
		List<Map<String, Object>> data = new ArrayList<>(); 
		try {
			data = service.getSelectNames(userid);
			response.setCode("success");
			response.setData(data);
		} catch (Exception e) {
			response.setCode("failure");
			response.setMessage("操作失败");
		}
		return response;
    }
	@RequestMapping("/getSelectRoles")
	@ResponseBody
	public ListResponse<Map<String, Object>> getSelectRoles(String userid,String type){
		ListResponse<Map<String, Object>> response = new ListResponse<Map<String, Object>>();
		List<Map<String, Object>> data = new ArrayList<>(); 
		try {
			data = service.getSelectRoles(userid,type);
			response.setCode("success");
			response.setData(data);
		} catch (Exception e) {
			response.setCode("failure");
			response.setMessage("操作失败");
		}
		return response;
    }
	/**
	 * 导入历史项目关键角色、团队成员和流程指标
	 * @param newNo,@param oldNo,
	 * @return
	 */
	@RequestMapping(value = "/addProRolesAndTeamMembersAndProcessIndex", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse addProRolesAndTeamMembersAndProcessIndex(String newNo, String oldNo){
		BaseResponse result = new BaseResponse();
		try {
			service.addProRolesAndTeamMembersAndProcessIndex(newNo,oldNo);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("导入失败：", e);
			result.setCode("failure");
			result.setMessage("导入失败");
		}
		return result;
	}
	
	/**
	 * 保存修改
	 * @param projectKeyroles
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse updateProjectKeyroles(ProjectKeyroles projectKeyroles){
		BaseResponse result = new BaseResponse();
		try {
			service.updateProjectKeyroles(projectKeyroles);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	/**
	 * 项目中关键角色修改保存
	 * @param projectKeyroles
	 * @return
	 */
	@RequestMapping(value = "/updateProjectKeyrole", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse updateProjectKeyrole(ProjectKeyroles projectKeyroles){
		BaseResponse result = new BaseResponse();
		try {
			service.updateProjectKeyrole(projectKeyroles);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}

	/**
	 * 单个修改保存
	 * @param projectKeyroles
	 * @return
	 */
	@RequestMapping(value = "/editOne", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse updateProjectKeyrolesOne( @RequestBody ProjectKeyroles projectKeyroles){
		BaseResponse result = new BaseResponse();
		try {
			service.updateProjectKeyroles(projectKeyroles);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	/**
	    * @Description:将表头字段映射为数据库字段
	    * @param @param sort
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	     */
	    private String transIntoSqlChar(String sort) {
	    	String sortName = "";
	    	if("prior".equals(sort)) {
	    		sortName = "prior";
	    	}else if("status".equals(sort)) {
	    		sortName = "status";
	    	}else if("iteType".equals(sort)) {
	    		sortName = "ite_type";
	    	}
	    	else if("importance".equals(sort)) {
	    		sortName = "importance";
	    	}
	    	else if("expectHours".equals(sort)) {
	    		sortName = "expect_hours";
	    	}
	    	else if("actualHours".equals(sort)) {
	    		sortName = "actual_hours";
	    	}
	    	else if("wrField".equals(sort)) {
	    		sortName = "wr_field";
	    	}
	    	else if("createTime".equals(sort)) {
	    		sortName = "create_time";
	    	}
	    	else if("updateTime".equals(sort)) {
	    		sortName = "update_time";
	    	}
	    	else if("iteId".equals(sort)) {
	    		sortName = "ite_id";
	    	}
	    	else if("role".equals(sort)) {
	    		sortName = "ROLE";
	    	}
	    	else if("name".equals(sort)) {
	    		sortName = "NAME";
	    	}
	    	else if("zrAccount".equals(sort)) {
	    		sortName = "ZR_ACCOUNT";
	    	}
	    	else if("hwAccount".equals(sort)) {
	    		sortName = "HW_ACCOUNT";
	    	}
	    	else if("rdpmExam".equals(sort)) {
	    		sortName = "RDPM_EXAM";
	    	}
	    	else if("superior".equals(sort)) {
	    		sortName = "SUPERIOR";
	    	}
			return sortName;
		}
	    
	/**
	 * 新增重要角色信息
	 * @param projectKeyroles
	 * @param request
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public BaseResponse addProjectKeyroles(ProjectKeyroles projectKeyroles, HttpServletRequest request) {
		BaseResponse result = new BaseResponse();

		try {
			int count = service.addKeyRole(projectKeyroles);

			if (count == 1) {
				result.setCode("success");
			}else if (count ==2){
				result.setCode("nopmid");
				result.setMessage("用户暂无匹配中软工号信息，关键角色添加失败");
			} else if (count ==3){
				result.setCode("noprojects");
				result.setMessage("新增角色无指定待入项目，添加失败");
			} else {
				result.setCode("failure");
				result.setMessage("插入角色已存在");
			}

			/*
			 * if (m == 0) { service.addKeyRole(projectKeyroles);
			 * result.setCode("success"); } else { result.setCode("failure");
			 * result.setMessage("插入角色已存在"); }
			 */
		} catch (Exception e) {
			logger.error("新增失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	/**
	 * 项目中新增关键角色信息
	 * @param projectKeyroles
	 * @param request
	 * @return
	 */
	@RequestMapping("/addProjectKeyRole")
	@ResponseBody
	public BaseResponse addProjectKeyRole(ProjectKeyroles projectKeyroles, HttpServletRequest request) {
		BaseResponse result = new BaseResponse();

		try {
			int count = service.addProjectKeyRole(projectKeyroles);

			if (count == 1) {
				result.setCode("success");
			} else if (count ==3){
				result.setCode("noprojects");
				result.setMessage("新增角色无指定待入项目，添加失败");
			} else {
				result.setCode("failure");
				result.setMessage("插入角色已存在");
			}
		} catch (Exception e) {
			logger.error("新增失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}

	/**
	 * 删除重要角色(批量)
	 * @param nos
	 * @param zrAccounts
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BaseResponse deleteProjectKeyroles(String[] nos, String[] zrAccounts) {
		BaseResponse result = new BaseResponse();
		try {
			service.deleteProjectKeyroles(nos, zrAccounts);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("删除失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	/**
	 * 删除重要角色(单个)
	 * @param no
	 * @param zrAccount
	 * @return
	 */
	@RequestMapping("/deleteone")
	@ResponseBody
	public BaseResponse deleteProjectKeyrole(String no, String zrAccount) {
		BaseResponse result = new BaseResponse();
		try {
			service.deleteProjectKeyrole(no, zrAccount);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("删除失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	    
	/**
	 * 重要角色编辑页面回显
	 * 
	 * @param no
	 * @param zrAccount
	 * @return
	 */
	@RequestMapping(value = "/editPage")
	@ResponseBody
	public Map<String, Object> editPage(String no ,String userid, String zrAccount) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ProjectKeyroles projectKeyroles = service.memberEchoDisplay(no,userid, zrAccount);
			map.put("code", "success");
			map.put("rows", projectKeyroles);
		} catch (Exception e) {
			map.put("code", "failure");
			logger.error(e.getMessage(), e);
		}
		return map;
	}
	/**
	 * 项目中关键角色编辑页面回显
	 *
	 * @param no
	 * @param zrAccount
	 * @return
	 */
	@RequestMapping(value = "/editProjectPage")
	@ResponseBody
	public Map<String, Object> editProjectPage(String no , String zrAccount) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ProjectKeyroles projectKeyroles = service.editProjectPage(no, zrAccount);
			map.put("code", "success");
			map.put("rows", projectKeyroles);
		} catch (Exception e) {
			map.put("code", "failure");
			logger.error(e.getMessage(), e);
		}
		return map;
	}

	    /**
	     * 分页查询团队成员
	     * @param request
	     * @return
	     */
	    @RequestMapping(value = "/getTeamMembers",method = RequestMethod.POST,consumes = "application/json")
		@ResponseBody
		public TableSplitResult getTeamMembers(HttpServletRequest request){
			String teamId = StringUtils.isEmpty(request.getParameter("teamId"))?"":request.getParameter("teamId");
			int limit = request.getParameter("limit") == null?10:Integer.parseInt(request.getParameter("limit"));//每页显示条数
	    	int offset = request.getParameter("offset") == null?0:Integer.parseInt(request.getParameter("offset"));//第几页
	    	String sort = StringUtils.isEmpty(request.getParameter("sort"))?"":request.getParameter("sort");//排序字段
	    	sort = transIntoSqlChar(sort);
	    	String sortOrder = StringUtils.isEmpty(request.getParameter("sortOrder"))?"":request.getParameter("sortOrder");//排序方式
	    	TableSplitResult page = new TableSplitResult();
	    	page.setPage(offset);
	        page.setRows(limit);
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("teamId", teamId);
	        page.setQueryMap(map);
	        try {
				page = service.queryTeamMembers(page,teamId,sort,sortOrder);
			} catch (Exception e) {
				logger.error("团队成员查询失败", e);
			}
			return page;
		}
	    
	    @RequestMapping("/organizationalStructure")
		@ResponseBody
		public List<ProjectPost> organizationalStructure(String userid) {
	    	List<ProjectPost> list = new ArrayList<>();
	    	try {
	    		list = service.organizationalStructure(userid);
			} catch (Exception e) {
				logger.error("组织架构查询失败", e);
			}
			return list;
		}
	    
	    @RequestMapping("/organizationalHierarchical")
		@ResponseBody
		public List<ProjectPost> organizationalHierarchical(String projNo) {
	    	List<ProjectPost> list = new ArrayList<>();
	    	try {
	    		list = service.organizationalHierarchical(projNo);
			} catch (Exception e) {
				logger.error("组织层级关系查询失败", e);
			}
			return list;
		}
	    
	@RequestMapping("/demandUpdata")
	@ResponseBody
	public BaseResponse demandUpdata(String Id, String value, String userid) {
		BaseResponse result = new BaseResponse();
		try {
			service.demandUpdata(Id, value, userid);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	    
	    @RequestMapping("/delDemandPopulation")
		@ResponseBody
		public BaseResponse delDemandPopulation(String ID){
			BaseResponse result = new BaseResponse();
			try {
				service.delDemandPopulation(ID);
				result.setCode("success");
			} catch (Exception e) {
				logger.error("编辑失败：", e);
				result.setCode("failure");
				result.setMessage("操作失败");
			}
			return result;
		}
	    @RequestMapping("/addDemandName")
		@ResponseBody
		public BaseResponse addDemandName(String userid,String position,String demand){
			BaseResponse result = new BaseResponse();
			try {
				service.projectPostHierarchy(userid,position,demand);
				result.setCode("success");
			} catch (Exception e) {
				logger.error("添加失败：", e);
				result.setCode("failure");
				result.setMessage("添加失败");
			}
			return result;
		}
	    
	/**
	 * 项目成本    
	 * @param projNo
	 * @return
	 */
	@RequestMapping("/projectCost")
	@ResponseBody
	public TableSplitResult<List<ProjectCost>> projectCost(PageRequest pageRequest, String projNo, String statisticalTime, String nextTime, String flag) {
		TableSplitResult<List<ProjectCost>> list = new TableSplitResult<>();
		try {
			list = service.projectCost(pageRequest, projNo, statisticalTime, nextTime, flag);
		} catch (Exception e) {
			logger.error("projectCost failed:", e);
		}
		return list;
	}
	
	/**
	 * 项目成本编辑
	 * @param projectCost
	 * @return
	 */
	@RequestMapping(value = "/updateMemberCost", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse updateMemberCost(@RequestBody ProjectCost projectCost) {
		BaseResponse result = new BaseResponse();

		try {
			service.updateMemberCost(projectCost);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	
	/**
	 * 编辑项目总预算
	 * @param projectBudget
	 * @param projNo
	 * @return
	 */
	@RequestMapping(value = "/updateProjectBudget", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse updateProjectBudget(String projectBudget, String projNo) {
		BaseResponse result = new BaseResponse();

		try {
			service.updateProjectBudget(projectBudget, projNo);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	
	/**
	 * 按具体天更新项目在职成员工时
	 * @param day
	 * @param week
	 * @param projNo
	 * @return
	 */
	@RequestMapping(value = "/updateHourByDay", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse updateHourByDay(String day, String week, String projNo, String mark, String flag) {
		BaseResponse result = new BaseResponse();

		try {
			service.updateHourByDay(day, week, projNo, mark, flag);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	
	/**
	 * 按中软工号更新项目在职成员工时
	 * @param zrAccount
	 * @param day
	 * @param projNo
	 * @return
	 */
	@RequestMapping(value = "/updateHourByZrAccount", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse updateHourByZrAccount(String zrAccount, String[] dayArr, String projNo, String mark) {
		BaseResponse result = new BaseResponse();

		try {
			service.updateHourByZrAccount(zrAccount, dayArr, projNo, mark);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/oneClickInput", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse oneClickInput(String[] dayArr, String projNo, String mark, String flag) {
		BaseResponse result = new BaseResponse();

		try {
			service.oneClickInput(dayArr, projNo, mark, flag);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("编辑失败：", e);
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/getMemberAccount", method = RequestMethod.POST)
	@ResponseBody
	public List<String> getMemberAccount(String projNo) {
		return service.getMemberAccount(projNo);
	}
	
	@RequestMapping(value = "/verifyHWAccount", method = RequestMethod.POST)
	@ResponseBody
	public int verifyHWAccount(String username) {
		return service.getHWAccount(username);
	}
	
	@RequestMapping(value = "/addZRAccount", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse addZRAccount(String zrAccount, String hwAccount) {
		BaseResponse baseResponse = new BaseResponse();
		if(service.addZRAccount(zrAccount, hwAccount)>0){
			baseResponse.setCode("success");
		}else{
			baseResponse.setCode("fail");
		}
		
		return baseResponse;
	}
	
	@RequestMapping(value = "/getPMZRAccountByNo", method = RequestMethod.POST)
	@ResponseBody
	public String getPMZRAccountByNo(String projNo) {
		return service.getPMZRAccountByNo(projNo);
	}
	
	@RequestMapping(value = "/getPMZRAccountByHW", method = RequestMethod.POST)
	@ResponseBody
	public String getPMZRAccountByHW(String hwAccount) {
		return service.getPMZRAccountByHW(hwAccount);
	}
	
	@RequestMapping(value = "/projectMemberEcho", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> projectMemberEcho(String projNo, String zrAccount) {
		return service.projectMemberEcho(projNo, zrAccount);
	}
	
	@RequestMapping(value = "/teamMemberEcho", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> teamMemberEcho(String teamId, String zrAccount) {
		return service.teamMemberEcho(teamId, zrAccount);
	}
	
	@RequestMapping(value = "/resetRank", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse resetRank(String zrAccount, String rank) {
		return service.resetRank(zrAccount, rank);
	}
	
}
