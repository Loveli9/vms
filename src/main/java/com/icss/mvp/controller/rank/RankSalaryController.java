package com.icss.mvp.controller.rank;
/**
 * Created by chengchenhui on 2019/7/22.
 */

import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.rank.RankSalaryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chengchenhui
 * @title: RankSalaryController
 * @projectName mvp
 * @description: TODO
 * @date 2019/7/2217:39
 */
@Controller
@RequestMapping("/salary")
public class RankSalaryController {

    private static Logger logger = Logger.getLogger(RankSalaryController.class);

    @Autowired
    private RankSalaryService service;

    @RequestMapping("/calculate")
    @ResponseBody
    public BaseResponse calculate(String proNo, String selectDate) {
        BaseResponse response = new BaseResponse();
        service.calculate(proNo, selectDate,response);
        return response;
    }
}
