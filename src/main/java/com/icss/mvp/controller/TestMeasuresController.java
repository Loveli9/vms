package com.icss.mvp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.CodeGainType;
import com.icss.mvp.entity.GerenCode;
import com.icss.mvp.entity.GerenCodeList;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.TestCaseInput;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.CodeGainTypeService;
import com.icss.mvp.service.TestMeasuresService;
import com.icss.mvp.util.DateUtils;

@Controller
@RequestMapping("/testMeasures")
public class TestMeasuresController {
	private static Logger logger = Logger.getLogger(TestMeasuresController.class);

	@Resource
	private TestMeasuresService testMeasuresService;
	@Autowired
	private CodeGainTypeService codeGainTypeService;

	/**
	 * 获取人力投入统计  actualFlag=0  实际工时     1 标准工时
	 * @param projNo
	 * @param actualFlag
	 * @return
	 */
	@RequestMapping("/getMonthlyManpowerList")
	@ResponseBody
	public Map<String, Object> getMonthlyManpowerList(String projNo,String actualFlag){
		String[] months = DateUtils.getLatestMonths(12);
		return testMeasuresService.getMonthlyManpowerList(projNo,actualFlag,months,true);
	}
	
	/**
	 * 获取测试用例数  type=0 计划     1 执行
	 * @param projNo
	 * @param type 类型
	 * @param role 职位
	 * @return
	 */
	@RequestMapping("/getTestCases")
	@ResponseBody
	public List<GerenCode> getSubmitTestCases(String projNo,String type,String role){
		String testcase = "0";
		List<CodeGainType> codeGainTypes = codeGainTypeService.getCodeTypeByNoParameterId(projNo,"161");
		if(codeGainTypes!=null && codeGainTypes.size()>0) {
			testcase = codeGainTypes.get(0).getType();
		}
		
		try {
			role = URLDecoder.decode(role, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("codeGainTypeService.getCodeTypeByNoParameterId exception, error: "+e.getMessage());
		}
		
		List<GerenCode> ret = null;
		//	 * @param testcase 获取数据类型 0-手动输入数据  1-tmss抓取数据
		if("0".equals(testcase)) {
			ret = testMeasuresService.getInputTestCases(projNo,type,role);
		}else if("1".equals(testcase)){
			ret = testMeasuresService.getSubmitTestCases(projNo,type,role);
		}else {
			return new ArrayList<>();
		}
		testMeasuresService.saveTestCaseStatistics(projNo, type, ret);
		return ret;
	}

	/**
	 * 获取测试用例数  type=0 计划     1 执行
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/get2TestCases",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public TableSplitResult<List<GerenCode>> getTestCases(HttpServletRequest request){
		String projNo = StringUtils.isEmpty(request.getParameter("projNo"))?"":request.getParameter("projNo");
		String type = StringUtils.isEmpty(request.getParameter("type"))?"":request.getParameter("type");
		String role = StringUtils.isEmpty(request.getParameter("role"))?"":request.getParameter("role");
		
		String testcase = "0";
		List<CodeGainType> codeGainTypes = codeGainTypeService.getCodeTypeByNoParameterId(projNo,"161");
		if(codeGainTypes!=null && codeGainTypes.size()>0) {
			testcase = codeGainTypes.get(0).getType();
		}
		
		try {
			role = URLDecoder.decode(role, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("URLDecoder.decode exception, error: "+e.getMessage());
		}
		
		TableSplitResult<List<GerenCode>> data = new TableSplitResult<List<GerenCode>>();
		List<GerenCode> ret = null;
		//	 * @param testcase 获取数据类型 0-手动输入数据  1-tmss抓取数据
		if("0".equals(testcase)) {
			ret = testMeasuresService.getInputTestCases(projNo,type,role);
		}else if("1".equals(testcase)){
			ret = testMeasuresService.getSubmitTestCases(projNo,type,role);
		}else {
			data.setRows(new ArrayList<>());
			return data;
		}
		testMeasuresService.saveTestCaseStatistics(projNo, type, ret);
		data.setRows(ret);
		return data;
	}

	/**
	 * 提交测试用例数  0 用例设计     1用例执行数
	 * @param gerenCodeList
	 * @return
	 */
	@RequestMapping("/inputTestCases")
	@ResponseBody
	public Map<String, Object> inputTestCases(@RequestBody GerenCodeList gerenCodeList){
		Map<String, Object> ret = testMeasuresService.inputTestCases(gerenCodeList);
		testMeasuresService.saveTestCaseStatistics(gerenCodeList.getProNo(), gerenCodeList.getType()+"", gerenCodeList.getGerenCodes());
		return ret;
	}

	/**
	 * 提交测试用例数  0 用例设计     1用例执行数
	 * @param testCaseInput
	 * @return
	 */
	@RequestMapping(value = "/input2TestCases", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse input2TestCases(@RequestBody TestCaseInput testCaseInput){
		BaseResponse result = new BaseResponse();
		try {
			testMeasuresService.input2TestCases(testCaseInput);
			result.setCode("success");
		} catch (Exception e) {
			result.setErrorMessage("failure", "编辑失败");
		}
		return result;
	}
}
