package com.icss.mvp.controller.io;

import com.icss.mvp.controller.BaseController;
import com.icss.mvp.entity.request.HandsontableRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.io.HandsontableService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by up on 2019/2/22.
 */
@Controller
@RequestMapping("/handsontableController")
public class HandsontableController extends BaseController {

    private static Logger  logger = Logger.getLogger(HandsontableController.class);

    @Autowired
    private HandsontableService handsontableService;

	/**
	 * 保存Handsontable数据
	 * @param handsontableRequest
	 * @return
	 */
	@RequestMapping("/saveHandsontable")
	@ResponseBody
	public BaseResponse saveHandsontable(@RequestBody HandsontableRequest handsontableRequest){
		BaseResponse result = new BaseResponse();
		try {
			int ret = handsontableService.saveHandsontable(handsontableRequest);
			result.setCode("0");
			result.setMessage("保存表格数据成功");
		} catch (Exception e) {
			logger.error("saveHandsontable exception, error:" ,e);
			result.setCode("1");
			result.setMessage("保存表格数据失败");
		}
		return result;
	}

	/**
	 * 获取Handsontable数据
	 * @param handsontableRequest
	 * @return
	 */
	@RequestMapping("/queryHandsontable")
	@ResponseBody
	public PlainResponse<HandsontableRequest> queryHandsontable(@RequestBody HandsontableRequest handsontableRequest){
		PlainResponse<HandsontableRequest> result = new PlainResponse<>();
		try {
			HandsontableRequest data = handsontableService.queryHandsontable(handsontableRequest);
			result.setCode("0");
			result.setMessage("查询表格数据成功");
			result.setData(data);
		} catch (Exception e) {
			logger.error("queryHandsontable exception, error:" ,e);
			result.setCode("1");
			result.setMessage("查询表格数据失败");
		}
		return result;
	}

}
