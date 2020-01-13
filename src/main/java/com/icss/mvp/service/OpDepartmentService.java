package com.icss.mvp.service;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.OpDepartmentDao;
import com.icss.mvp.entity.OpDepartment;
import com.icss.mvp.entity.PermissionDetail;

@Service("opDepartmentService")
@PropertySource(value = "classpath:/application.properties")
@Transactional
public class OpDepartmentService {

	@Resource
	private OpDepartmentDao dao;
	@Resource
	IUserManagerDao userManagerDao;

    @Value(value = "${business_line_parent_id_chinasoft}")
    private String          display_parent_id;

    @Value(value = "${business_line_id_chinasoft}")
    private String          display_line_id;

	private static Logger logger = Logger.getLogger(OpDepartmentService.class);

	/**
	 * <p>Title: getSysHwdeptByPId</p>
	 * <p>Description: 通过父id查找下面的子节点信息</p>
	 * @author gaoyao
	 * @param pId
	 * @return
	 */
    public List<OpDepartment> getOpDepartmentByPId(String pId) {
        List<OpDepartment> result = new ArrayList<>();
        if (StringUtils.isBlank(pId)) {
            return result;
        }

        if (pId.equals(display_parent_id) && StringUtils.isNotBlank(display_line_id)) {
            OpDepartment dept = dao.getOpDepartmentByDeptId(display_line_id);
            result.add(dept);
        } else {
            result = dao.getOpDepartmentByPId(pId);
        }
        return result;
    }

    /**
	 * 在数据库中新插入一条指定的中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer insertOpDepartment(OpDepartment opDepartment){
		return dao.insertOpDepartment(opDepartment);
	}

	/**
	 * 在数据库中修改此条中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer updateOpDepartment(OpDepartment opDepartment){
		return dao.updateOpDepartment(opDepartment);
	}

	/**
	 * 在数据库中通过主键id查出指定的中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public OpDepartment getOpDepartmentById(String id){
		return dao.getOpDepartmentById(id);
	}
	
	public OpDepartment getOpDepartmentByDeptId(String id){
		return dao.getOpDepartmentByDeptId(id);
	}

	/**
	 * 在数据库中通过主键id删除指定中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer deleteOpDepartmentById(String id){
		return dao.deleteOpDepartmentById(id);
	}

	/**
	 * 在数据库中通过主键id批量删除指定中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer deleteOpDepartmentByIdList(List<String> list){
		return dao.deleteOpDepartmentByIdList(list);
	}

	/**
	 * 在数据库中通过Map中的分页参数（startNo,pageSize）
	 * 和其他条件参数得到指定条数的中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public List<OpDepartment> getOpDepartmentForPage(Map<String,Object> map){
		return dao.getOpDepartmentForPage(map);
	}

    /**
     * 在数据库中通过Map中条件参数得到指定中软组织结构记录的总条数
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
     */
	public Integer getOpDepartmentCount(Map<String,Object> map){
		return dao.getOpDepartmentCount(map);
	}

	/**
     * 在数据库中查出所有中软组织结构记录
     * @author gaoyao
     * @time 2018-4-25 11:06:49
     */
	public List<OpDepartment> getAllOpDepartment(){
		return dao.getAllOpDepartment();
	}

	public List<OpDepartment> getEnableDepartment() {
		return dao.getEnableDepartment();
	}

	public List<OpDepartment> getOpDepartmentByPIds(Set<String> deptIds) {
		return dao.getOpDepartmentByPIds(deptIds);
	}

	public List<PermissionDetail> getpermissionTree() {
		
		return dao.getpermissionTree();
	}

	public List<Map<String,Object>> getOpDepartmentBylevel(String level,Set<String> deptIds) {
		if(!"2".equals(level) && deptIds.isEmpty()){
			return new ArrayList<>();
		}
		List<Map<String,Object>> ret = dao.getOpDepartmentBylevel(level,deptIds);
		return ret;
	}

    public List<Map<String, Object>> getOpDepartment(String level, String ids, String username) {
		if(!"2".equals(level) && StringUtilsLocal.isBlank(ids)){
			return new ArrayList<>();
		}
		UserInfo userInfo = userManagerDao.getUserInfoByName(username);
		if(StringUtilsLocal.isBlank(ids) && StringUtilsLocal.isBlank(userInfo.getBu())){
			return new ArrayList<>();
		}
		String userIds = "";
		if("2".equals(level)){
			userIds = userInfo.getBu();
		}else if ("3".equals(level)){
			userIds = userInfo.getDu();
		}else if ("4".equals(level)){
			userIds = userInfo.getDept();
		}

		Set<String> deptIds = new HashSet<>();
		if(!StringUtilsLocal.isBlank(ids)){
			String[] deIds = ids.split(",");
			for (String id : deIds) {
				deptIds.add(id);
			}
		}
		List<Map<String,Object>> opDepartments = dao.getOpDepartmentBylevel(level,deptIds);
		if(StringUtilsLocal.isBlank(userIds)){
			return opDepartments;
		}
		userIds = ","+userIds+",";
		List<Map<String,Object>> ret = new ArrayList<>();
		for (Map<String,Object> opDepartment : opDepartments) {
			if(userIds.contains(","+ StringUtilsLocal.valueOf(opDepartment.get("dept_id"))+",")){
				ret.add(opDepartment);
			}
		}

		return ret;
    }
    
    /**
     * 华为线
     * @param level
     * @param deptIds
     * @return
     */
	public List<Map<String,Object>> getHwDepartmentBylevel(String level,Set<String> deptIds) {
		if(!"1".equals(level) && deptIds.isEmpty()){
			return new ArrayList<>();
		}
		List<Map<String,Object>> ret = dao.getHwDepartmentBylevel(level,deptIds);
		return ret;
	}

	/**
	 *
	 * @param level
	 * @param deptName
	 * @param pid
	 * @return
	 */
	public String getZrOrganiza(Integer level,String deptName, String pid,String deptId){
		String id = "";
		try {
			OpDepartment ot = dao.getZrOrganiza(level,deptName);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(ot == null){
				OpDepartment om = new OpDepartment();
				om.setParentDeptId(null==pid?"1":pid);
				om.setDeptLevel(level);
				om.setDeptId(deptId);
				om.setCreateBy("system");
				om.setCreationDate(format.format(new Date()));
				om.setDeptName(deptName);
				om.setEnable("0");//开启状态
				om.setLastUpdateBy("system");
				om.setLastUpdate(format.format(new Date()));
				om.setRemark("");
				dao.insertOpDepartment(om);
				OpDepartment o = dao.getZrOrganiza(level,deptName);
				return o.getDeptId();
			}
				id = ot.getDeptId();
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		return id;
	}


	public Map<String,Object> getZrOrganizaMap(String buName, String duName, String pdtName) {
		Map<String,Object> map = new HashMap<>();
		try {
			map = dao.getZrOrganizaMap(buName,duName,pdtName);
		}catch (Exception e){
			logger.error(e);
		}
		return map;
	}
}
