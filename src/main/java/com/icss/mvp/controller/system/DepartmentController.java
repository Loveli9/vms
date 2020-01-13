package com.icss.mvp.controller.system;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.controller.BaseController;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.system.DepartmentEntity;
import com.icss.mvp.service.system.DepartmentService;

/**
 * Created by Ray on 2018/7/27.
 *
 * @author Ray
 * @date 2018/7/27 15:36
 */
@Controller
@RequestMapping("/department")
public class DepartmentController extends BaseController {

    private static Logger logger = Logger.getLogger(DepartmentController.class);

    @Autowired
    DepartmentService     departmentService;

    @RequestMapping("/list")
    @ResponseBody
    public ListResponse<DepartmentEntity> describeDepartments(DepartmentEntity dept) {
        ListResponse<DepartmentEntity> result = new ListResponse<>();

        try {
            departmentService.describeDepartments(dept);
        } catch (Exception e) {
            logger.error("departmentService.describeDepartments exception, error:" + e.getMessage());
            result.setData(new ArrayList<>());
        }

        return result;
    }

    @RequestMapping("/index")
    @ResponseBody
    public PlainResponse generateFullIndexMap(DepartmentEntity dept) {
        PlainResponse result = new PlainResponse();

        try {
            if (dept == null) {
                dept = new DepartmentEntity();
            }

            result.setData(departmentService.getChainMap(dept.getType(), dept.getLevel(), dept.getParentCode(), false));
        } catch (Exception e) {
            logger.error("departmentService.getChildren exception, error:" + e.getMessage());
            result.setData(new HashMap<>(0));
        }

        return result;
    }

    @RequestMapping("/diff")
    @ResponseBody
    public PlainResponse alertDepartment(DepartmentEntity dept) {
        PlainResponse result = new PlainResponse();

        try {
            String type = dept != null ? dept.getType() : "";
            result = departmentService.renewOrganization(type, false);
        } catch (Exception e) {
            logger.error("departmentService.getChildren exception, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }
}
