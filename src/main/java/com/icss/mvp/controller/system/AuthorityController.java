package com.icss.mvp.controller.system;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.icss.mvp.entity.DepartmentTree;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.entity.PermissionDetail;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.system.AuthorityEntity;
import com.icss.mvp.service.LoginService;
import com.icss.mvp.service.system.AuthorityService;
import com.icss.mvp.tree.DeptTreeUtil;
import com.icss.mvp.tree.Node;
import com.icss.mvp.tree.NodeTuple;
import com.icss.mvp.tree.PermissionTreeUtil;

/**
 * Created by Ray on 2019/8/5.
 *
 * @author Ray
 * @date 2019/8/5 14:38
 */
@Controller
@RequestMapping("/auth")
public class AuthorityController {

    private static Logger logger = Logger.getLogger(AuthorityController.class);

    @Autowired
    private AuthorityService authorityService;
    
    @Autowired
    LoginService loginService;

    /**
     * Description: 分页查询角色列表
     * @param pager
     * @param entity
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public PageResponse<AuthorityEntity> describeAuthorities(AuthorityEntity entity, PageRequest pager) {
        PageResponse<AuthorityEntity> result = new PageResponse<>();

        try {
            if (entity == null) {
                entity = new AuthorityEntity();
            }
            // 处理中文乱码
            entity.setName(URLDecoder.decode(entity.getName(), "UTF-8"));
            
            result = authorityService.describeAuthorities(entity, pager);
        } catch (Exception e) {
            logger.error("authorityService.describeAuthorities, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

	@RequestMapping("/viewPer")
	@ResponseBody
	public ListResponse<Node> perview(String id) {
		ListResponse<Node> result = new ListResponse<>();
		List<Node> nodeList = new ArrayList<>();
		
		try {
			// 中软部门
			List<PermissionDetail> permissionDetails = authorityService.getPermissionTree();
			NodeTuple nodeTuple = PermissionTreeUtil.builperTree(permissionDetails);
			Node rootNode = DeptTreeUtil.createRootNode();
      		List<Node> childrens = rootNode.getChildren()==null? new ArrayList<>() : rootNode.getChildren();

//			if (null == childrens) {
//				childrens = new ArrayList<>();
//			}
			childrens.add(nodeTuple.getNode());
			
			String[] authorized = authorityService.roleAuthorized(id);
			
			Set<String> csSet = new HashSet<>();
			for (String dept : authorized) {
				if (dept.startsWith(PermissionTreeUtil.CS_DEPT)) {
					dept = dept.substring(PermissionTreeUtil.CS_DEPT.length());
					csSet.addAll(Arrays.asList(dept.split("/")));
				}
			}
			this.setChecked(nodeTuple, csSet);
			nodeList.add(rootNode);
			result.setData(nodeList);
		} catch (Exception e) {
			result.setErrorMessage("ParamterError", "返回失败！");
		}
		return result;
	}
	
	private void setChecked(NodeTuple nodeTuple, Set<String> set) {
		Map<String, Node> nodeMap = nodeTuple.getMap();
		for (String id : set) {
			nodeMap.get(id).setChecked(true);
		}
	}

	/**
	 * @Description: 增加角色
	 * @param entity
	 * @return
	 */
	@RequestMapping("/addPer")
	@ResponseBody
	public BaseResponse createAuthority(AuthorityEntity entity) {
		BaseResponse result = new BaseResponse();
		
		try {
			if (StringUtils.isBlank(entity.getAuthorized())) {
				result.setErrorMessage("ParamterError", "权限不能为空!");
				return result;
			}

			int count = authorityService.selectCountByName(entity.getName());
			if (count > 0) {
				result.setErrorMessage("ParamterError", "该角色名称已存在！");
				return result;
			} else if (count == -1) {
				result.setError(CommonResultCode.INTERNAL, "查询出错，请稍后重试。");
				return result;
			}
			authorityService.increaseAuthority(entity);
		} catch (Exception e) {
			result.setError(CommonResultCode.INTERNAL, e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * @Description: 修改角色
	 * @param entity
	 * @return
	 */
	@RequestMapping("/editPer")
	@ResponseBody
	public BaseResponse modifyAuthority(AuthorityEntity entity) {
		BaseResponse result = new BaseResponse();
		
		try {
			authorityService.modifyAuthority(entity);
		} catch (Exception e) {
			result.setError(CommonResultCode.INTERNAL, e.getMessage());
		}
		return result;
	}
	
	/**
	 * @Description: 删除角色
	 * @param id
	 * @return
	 */
	@RequestMapping("/delPer")
	@ResponseBody
	public BaseResponse deleteAuthority(@RequestBody String id) {
		BaseResponse result = new BaseResponse();
		if (StringUtils.isBlank(id)) {
			result.setError(CommonResultCode.ILLEGAL_PARAM, "ID cannot be empty");
			return result;
		}

		try {
			// 查询数据库中用户使用的权限id
			Set<String> permissions = new HashSet<>(Arrays.asList(loginService.getUserOfPer().split(",")));
			if (permissions.contains(id)) {
				result.setError(CommonResultCode.ILLEGAL_PARAM, "当前角色已分配用户使用，请修改相关用户角色后再删除。");
				return result;
			}

			result = authorityService.deleteAuthority(id);
		} catch (Exception e) {
			result.setErrorMessage("ParamterError", "删除失败！");
		}
		return result;
	}
	
	@RequestMapping("/perTree")
	@ResponseBody
	public ListResponse<Node> getpermissionTree(){
		ListResponse<Node> result = new ListResponse<>();
		List<Node> nodeList = new ArrayList<>();
		
		try {
			List<PermissionDetail> permissionDetails = authorityService.getPermissionTree();
			NodeTuple nodeTuple = PermissionTreeUtil.builperTree(permissionDetails);
			Node rootNode = DeptTreeUtil.createRootNode();
			List<Node> nodes = rootNode.getChildren();
			if (null == nodes){
				nodes = new ArrayList<>();
			}
			nodes.add(nodeTuple.getNode());
			nodeList.add(rootNode);
			result.setData(nodeList);
		}catch (Exception e){
			result.setErrorMessage("ParamterError", "返回失败！");
		}
		return result;
	}

	@RequestMapping("/getTree")
	@ResponseBody
	public List<DepartmentTree> getTree() {
		return authorityService.getTree();
	}

}
