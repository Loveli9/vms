package com.icss.mvp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.entity.PageInfo;
import com.icss.mvp.entity.SysAnswer;
import com.icss.mvp.entity.SysQuestion;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.service.QuestionFeedbackService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 问题反馈
 */
@RequestMapping("/questionFeedback")
@RestController
public class QuestionFeedbackController {

	private static Logger logger = Logger.getLogger(QuestionFeedbackController.class);
	@Autowired
	private QuestionFeedbackService service;

	/**
	 * 问题新增
	 */
	@RequestMapping(value = "/questionAdd", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse addQuestion(SysQuestion sysQuestion) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			service.addQuestion(sysQuestion);
			baseResponse.setMessage("反馈成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			baseResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return baseResponse;
	}

	/**
	 * 问题编辑
	 */
	@RequestMapping(value = "/questionEdit", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse editQuestion(@RequestParam(name = "questionId") String id,
			@RequestParam(name = "content", required = false) String content,
			@RequestParam(name = "stopState", required = false) String stopState,
			@RequestParam(name = "userId") String userId) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			service.editQuestion(id, content, stopState, userId);
			baseResponse.setMessage("反馈成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			baseResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return baseResponse;
	}

	/**
	 * 回复新增
	 */
	@RequestMapping(value = "/answerAdd", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse addAnswer(String solveState,SysAnswer sysAnswer) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			service.updateSolveState(solveState,sysAnswer.getQuestionId());
			service.addAnswer(sysAnswer);
			baseResponse.setMessage("回复成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			baseResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return baseResponse;
	}

	/**
	 * 回复修改
	 */
	@RequestMapping(value = "/answerEdit", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse editAnswer(@RequestParam(name = "answerId") String answerId,
			@RequestParam(name = "content", required = false) String content,
			@RequestParam(name = "acceptState", required = false) String acceptState) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			service.editAnswer(answerId, content, acceptState);
			baseResponse.setMessage("回复成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			baseResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return baseResponse;
	}

	/**
	 * 问题列表
	 */
	@RequestMapping(value = "/questions")
	@ResponseBody
	public Map<String, Object> questionList(@ModelAttribute SysQuestion sysQuestion,
			@ModelAttribute PageInfo pageInfo) {
		// PageResponse<Map<String, Object>> pageResponse = new PageResponse<>();
		// try {
		// List<Map<String, Object>> sysQuestions = service.questionList(sysQuestion,
		// pageInfo);
		// pageResponse.setPageCount(pageInfo.getTotalPage());
		// pageResponse.setTotalCount(pageInfo.getTotalRecord());
		// pageResponse.setPageSize(pageInfo.getPageSize());
		// pageResponse.setPageNumber(pageInfo.getCurrentPage());
		// pageResponse.setData(sysQuestions);
		// } catch (Exception e) {
		// logger.error(e.getMessage(), e);
		// pageResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
		// }
		// return pageResponse;

		Map<String, Object> map = new HashMap<>();
		try {
			List<Map<String, Object>> sysQuestions = service.questionList(sysQuestion, pageInfo);
			String title = "[{\"field\":\"反馈人\",\"title\":\"反馈人\"},{\"field\":\"问题类型\",\"title\":\"问题类型\"},{\"field\":\"问题描述\",\"title\":\"问题描述\"},{\"field\":\"反馈时间\",\"title\":\"反馈时间\"},{\"field\":\"问题回复\",\"title\":\"问题回复\"},{\"field\":\"回复时间\",\"title\":\"回复时间\"},{\"field\":\"问题状态\",\"title\":\"问题状态\"},{\"field\":\"操作\",\"title\":\"操作\"}]";
			Object gridTitles = JSON.parse(title);
			map.put("gridTitles", gridTitles);
			JSONObject json = new JSONObject();
			json.put("total", sysQuestions.size());
			JSONArray jsonArray = new JSONArray();
			for (Map<String, Object> temp : sysQuestions) {
				JSONObject projectInfoObject = new JSONObject();
				projectInfoObject.put("反馈人", temp.get("name"));
				projectInfoObject.put("问题类型", temp.get("type"));
				projectInfoObject.put("问题描述", temp.get("content"));
				projectInfoObject.put("反馈时间", temp.get("create_date"));
				projectInfoObject.put("问题回复", temp.get("acontent"));
				projectInfoObject.put("回复时间", temp.get("adate"));
				projectInfoObject.put("问题状态", temp.get("solve_state"));
				projectInfoObject.put("操作", temp.get("id"));
				jsonArray.add(projectInfoObject);
			}
			json.put("rows", jsonArray);
			map.put("gridDatas", json);
		} catch (Exception e) {
			logger.error("service.questionList exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	/**
	 * 回复列表
	 */
	@RequestMapping(value = "/answers", method = RequestMethod.POST)
	@ResponseBody
	public PageResponse<SysAnswer> answerList(@RequestParam(name = "questionId") String questionId,
			@ModelAttribute PageInfo pageInfo) {
		PageResponse<SysAnswer> pageResponse = new PageResponse<>();
		try {
			List<SysAnswer> sysAnswers = service.answersList(questionId, pageInfo);
			pageResponse.setPageCount(pageInfo.getTotalPage());
			pageResponse.setTotalCount(pageInfo.getTotalRecord());
			pageResponse.setPageSize(pageInfo.getPageSize());
			pageResponse.setPageNumber(pageInfo.getCurrentPage());
			pageResponse.setData(sysAnswers);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			pageResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return pageResponse;
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/details")
	@ResponseBody
	public PageResponse<Map<String, Object>> details(@RequestParam(name = "questionId") String questionId) {
		PageResponse<Map<String, Object>> pageResponse = new PageResponse<>();
		try {
			List<Map<String, Object>> question = service.details(questionId);
			pageResponse.setData(question);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
			pageResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return pageResponse;
	}
}
