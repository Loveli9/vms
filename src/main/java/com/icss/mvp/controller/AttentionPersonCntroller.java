package com.icss.mvp.controller;/**
 * Created by chengchenhui on 2019/12/25.
 */

import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.member.AttentionPersonServices;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 关注人员
 * @author: chengchenhui
 * @create: 2019-12-25 17:14
 **/
@Controller
@RequestMapping("/attention")
public class AttentionPersonCntroller {
    private Logger logger = Logger.getLogger(AttentionPersonCntroller.class);

    @Autowired
    private AttentionPersonServices service;

    /**
     * 分页查询关注人员列表
     * @param: [request]
     * @return: com.icss.mvp.entity.TableSplitResult
     * @Author: chengchenhui
     * @Date: 2019/12/25
     */
    @RequestMapping("/personList")
    @ResponseBody
    public TableSplitResult getAttentionInfoByPage(HttpServletRequest request){
        TableSplitResult result = new TableSplitResult();
        try {
            result = service.getAttentionInfoByPage(request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("query attention person error:"+e.getMessage());
        }
        return result;
    }


    /**
     * 取消关注
     * @param: [person]
     * @return: com.icss.mvp.entity.common.response.BaseResponse
     * @Author: chengchenhui
     * @Date: 2019/12/27
     */
    @RequestMapping("/removeConcern")
    @ResponseBody
    public BaseResponse removeConcerns(String id){
        BaseResponse response = new BaseResponse();
        try {
            service.removeConcerns(id);
            response.setCode("success");
        } catch (Exception e) {
            response.setCode("error");
            logger.error("remove concern failed:"+e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}
