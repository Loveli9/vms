package com.icss.mvp.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.entity.SysResource;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.SysResourceService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * 系统资源
 */
@RestController
@RequestMapping("/resource")
public class SysResourceController {
    private static Logger logger= Logger.getLogger(SysResourceController.class);
    @Autowired
    private SysResourceService sysResourceService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse add(@RequestBody SysResource sysResource){
        BaseResponse baseResponse = new BaseResponse();
        try {
            sysResourceService.insertSysResource(sysResource);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            baseResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }
        return baseResponse;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @ResponseBody
    public BaseResponse edit(@RequestBody SysResource sysResource){
        BaseResponse baseResponse = new BaseResponse();
        try {
            sysResourceService.editSysResource(sysResource);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            baseResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }
        return baseResponse;
    }

    @RequestMapping(value = "/del/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public BaseResponse delete(@PathVariable String id){
        BaseResponse baseResponse = new BaseResponse();
        try {
            sysResourceService.deleteSysResource(id);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            baseResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }
        return baseResponse;
    }

    @RequestMapping(value = "/sysResource", method = RequestMethod.GET)
    @ResponseBody
    public PlainResponse<JSONArray> sysResource(){
        PlainResponse<JSONArray> plainResponse = new PlainResponse();
        try {
            JSONArray jsonArray = sysResourceService.selectSysResources();
            plainResponse.setData(jsonArray);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            plainResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }
        return plainResponse;
    }
}
