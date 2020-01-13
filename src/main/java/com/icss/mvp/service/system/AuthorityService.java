package com.icss.mvp.service.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.dao.OpDepartmentDao;
import com.icss.mvp.dao.SysHwdeptDao;
import com.icss.mvp.dao.system.IAuthorityDao;
import com.icss.mvp.entity.DepartmentTree;
import com.icss.mvp.entity.OpDepartment;
import com.icss.mvp.entity.PermissionDetail;
import com.icss.mvp.entity.SysHwdept;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.system.AuthorityEntity;
import com.icss.mvp.service.LoginService;
import com.icss.mvp.util.MybatisUtils;

/**
 * Created by Ray on 2019/8/5.
 *
 * @author Ray
 * @date 2019/8/5 15:11
 */
@Transactional(rollbackFor = Exception.class)
@Service("authorityService") public class AuthorityService {

    private static Logger logger = Logger.getLogger(AuthorityService.class);

    @Autowired IAuthorityDao authorityDao;
    @Autowired LoginService  loginService;

    @Autowired OpDepartmentDao opDepartmentService;
    @Autowired SysHwdeptDao    sysHwdeptService;

    public PageResponse<AuthorityEntity> describeAuthorities(AuthorityEntity entry, PageRequest pager) {
        Map<String, Object> parameter = new HashMap<>(0);
        parameter.put("order", "p.`update_time`");
        parameter.put("sort", "DESC");

        getConditions(parameter, entry);

        if (pager == null) {
            pager = new PageRequest();
        }

        PageResponse<AuthorityEntity> result = new PageResponse<>();

        List<AuthorityEntity> data = null;
        try {
            Page page = PageHelper.startPage((pager.getPageNumber()) + 1, pager.getPageSize());
            data = authorityDao.getAuthorities(parameter);

            result.setTotalCount((int) page.getTotal());
        } catch (Exception e) {
            logger.error("authorityDao.getAuthorities exception, error: " + e.getMessage() + ", params: "
                         + JSON.toJSONString(parameter));
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }
        result.setData(data);

        result.setPageSize(pager.getPageSize());
        result.setPageNumber(pager.getPageNumber() < 0 ? 0 : pager.getPageNumber());

        return result;
    }

    private void getConditions(Map<String, Object> parameter, AuthorityEntity entity) {
        if (entity != null) {
            MybatisUtils.buildParam(parameter, entity.getId(), "id");
            MybatisUtils.buildParam(parameter, entity.getName(), "name");
            MybatisUtils.buildParam(parameter, entity.getDescription(), "description");
            MybatisUtils.buildParam(parameter, entity.getAuthorized(), "authorized");
            MybatisUtils.buildParam(parameter, entity.getOperator(), "operator");
        }
    }

    public String[] roleAuthorized(String id) {
        AuthorityEntity entity = new AuthorityEntity();
        entity.setId(id);

        try {
            PageResponse<AuthorityEntity> response = describeAuthorities(entity, null);
            if (response.getSucceed()) {
                if (response.getData() != null && response.getData().size() > 0) {
                    entity = response.getData().get(0);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return (entity != null ? entity.getAuthorized() : "").split(",");
    }

    public int selectCountByName(String name) {
        AuthorityEntity entity = new AuthorityEntity();
        entity.setName(name);

        PageResponse<AuthorityEntity> response = new PageResponse<>();
        try {
            response = describeAuthorities(entity, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return response != null && response.getSucceed() ? response.getTotalCount() : -1;
    }

    /**
     * @param entity
     * @Description: 增加角色
     */
    public void increaseAuthority(AuthorityEntity entity) {
        authorityDao.insertAuthority(entity);
    }

    /**
     * @param entity
     * @Description: 修改角色
     */
    public void modifyAuthority(AuthorityEntity entity) {
        Map<String, Object> params = new HashMap<>(0);
        getConditions(params, entity);

        params.put("description", entity.getDescription());
        params.put("modifyTime", new Date());

        authorityDao.updateAuthority(params);
    }

    /**
     * @param id
     * @return
     * @Description: 删除角色
     */
    public BaseResponse deleteAuthority(String id) {
        Map<String, Object> params = new HashMap<>(0);
        params.put("id", id);
        params.put("modifyTime", new Date());
        params.put("isDeleted", 1);

        BaseResponse result = new BaseResponse();
        try {
            authorityDao.updateAuthority(params);
        } catch (Exception e) {
            result.setError(CommonResultCode.INTERNAL, "删除异常！" + e.getMessage());
        }
        return result;
    }

    public List<PermissionDetail> getPermissionTree() {
        return authorityDao.getPermissionTree();
    }

    public List<DepartmentTree> getTree() {
        List<DepartmentTree> list = new ArrayList<DepartmentTree>();

        DepartmentTree departmentTree1 = new DepartmentTree();
        departmentTree1.setId((long) 2);
        departmentTree1.setDepartmentId("23");
        departmentTree1.setDepartmentParentId("0");
        departmentTree1.setDepartmentName("华为组织");
        departmentTree1.setLevel(0);
        departmentTree1.setSeq(1);
        list.add(departmentTree1);

        DepartmentTree departmentTree = new DepartmentTree();
        departmentTree.setId((long) 1);
        departmentTree.setDepartmentId("100000");
        departmentTree.setDepartmentParentId("0");
        departmentTree.setDepartmentName("中软组织");
        departmentTree.setLevel(0);
        departmentTree.setSeq(0);
        list.add(departmentTree);

        List<OpDepartment> opDepartments = opDepartmentService.getEnableDepartment();
        list = arrangementZrList(opDepartments,list);
        List<SysHwdept> sysHwdepts = sysHwdeptService.getAllSysHwdept();
		list = arrangementHwList(sysHwdepts,list);
        return list;
    }

	private List<DepartmentTree> arrangementZrList(List<OpDepartment> opDepartments,List<DepartmentTree> list) {
    	List<String> listId = new ArrayList<>();
    	listId.add("100000");
		for (int i = 0; i < opDepartments.size(); i++) {
			if(listId.contains(opDepartments.get(i).getParentDeptId())){
				listId.add(opDepartments.get(i).getDeptId());
			}
		}
		for (int i = 0; i < opDepartments.size(); i++) {
			DepartmentTree departmentTree = new DepartmentTree();
			if (opDepartments.get(i).getDeptLevel() != 0 && listId.contains(opDepartments.get(i).getParentDeptId())) {
				departmentTree.setId(opDepartments.get(i).getId());
				departmentTree.setDepartmentId(opDepartments.get(i).getDeptId());
				departmentTree.setDepartmentParentId(opDepartments.get(i).getParentDeptId());
				departmentTree.setDepartmentName(opDepartments.get(i).getDeptName());
				departmentTree.setLevel(opDepartments.get(i).getDeptLevel());
				departmentTree.setSeq(0);
				list.add(departmentTree);
			}
		}
		return list;

	}
	private List<DepartmentTree> arrangementHwList(List<SysHwdept> sysHwdepts,List<DepartmentTree> list) {
    	List<Long> listId = new ArrayList<>();
		listId.add((long) 23);
		for (int i = 0; i < sysHwdepts.size(); i++) {
			if(listId.contains(sysHwdepts.get(i).getParentId())){
				listId.add(sysHwdepts.get(i).getDeptId());
			}
		}
		for (int i = 0; i < sysHwdepts.size(); i++) {
			DepartmentTree departmentTree = new DepartmentTree();
			if (sysHwdepts.get(i).getDeptLevel() != 0 && listId.contains(sysHwdepts.get(i).getParentId())) {
				departmentTree.setId(sysHwdepts.get(i).getId());
				departmentTree.setDepartmentId("" + sysHwdepts.get(i).getDeptId());
				departmentTree.setDepartmentParentId("" + sysHwdepts.get(i).getParentId());
				departmentTree.setDepartmentName(sysHwdepts.get(i).getDeptName());
				departmentTree.setLevel(sysHwdepts.get(i).getDeptLevel());
				departmentTree.setSeq(1);
				list.add(departmentTree);
			}
		}
		return list;

	}

}
