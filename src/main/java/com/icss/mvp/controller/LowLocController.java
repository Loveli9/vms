package com.icss.mvp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.LowDetails;
import com.icss.mvp.entity.LowLoc;
import com.icss.mvp.entity.LowOutputEntity;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.SaveLowLoc;
import com.icss.mvp.service.LowLocService;

//import org.apache.http.auth.AuthenticationException;

@Controller
@RequestMapping("/LowLoc")
public class LowLocController extends BaseController {

	private final static Logger LOG = Logger.getLogger(LowLocController.class);

	@Autowired
	private LowLocService lowLocService;

	/**
	 * 查询所有低产出人员
	 * 
	 * @param proNo
	 */
	@RequestMapping("/queryLowLocPerson")
	@ResponseBody
	public List<LowLoc> chooseMonthLoc(String proNo, String month, String standard) {
		return lowLocService.queryLowLocPerson(proNo, month, standard);
	}

	/**
	 * 查询所有低产出原因
	 * 
	 * @return
	 */
	@RequestMapping("/queryLowLocReasons")
	@ResponseBody
	public Map<String, List<String>> queryLowLocReasons() {
		return lowLocService.queryLowLocReasons();
	}

	/**
	 * 保存所有低产出人员
	 * 
	 * @return
	 */
	@RequestMapping("/saveLowLoc")
	@ResponseBody
	public String saveLowLoc(@RequestBody SaveLowLoc allLowLoc) {
		return lowLocService.saveLowLoc(allLowLoc);
	}

	/**
	 * 导出所有低产出人员
	 * 
	 * @return
	 */
	@RequestMapping("/exportLowloc")
	@ResponseBody
	public void exportLowloc(HttpServletResponse response,String nos,String month) throws Exception {
		try {
			nos = nos.substring(0,nos.length()-1);
			List<String> no = new ArrayList<>();
			String[] str = nos.split(",");
			no = Arrays.asList(str);
			byte[] fileContents = lowLocService.exportLowloc(no,month);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = "低产出人员分析报表" + sf.format(new Date()).toString() + ".xlsx";
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));
			response.getOutputStream().write(fileContents);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e);
		}
	}

	/**
	 * 页面加载产出分析汇总报表点击加载详情页面
	 * 
	 * @return
	 */
	@RequestMapping("/AnalysisSummary")
	@ResponseBody
	public List<LowDetails> AnalysisSummary(@ModelAttribute ProjectInfo projectInfo,
			String client,String month,String line,String column) {
		List<LowDetails> result = new ArrayList<>();
		try {
            authentication0(projectInfo, client);
            result = (List<LowDetails>) lowLocService.AnalysisSummary(projectInfo,client,month,line,column);
        }catch (AuthenticationException e) {
            LOG.error("AnalysisSummary occurs AuthenticationException");
            result = new ArrayList<>();
        } catch (Exception e) {
        	LOG.error("AnalysisSummary exception, error:" + e.getMessage());
        	result = new ArrayList<>();
        }
		return result;
	}
	/**
	 * 无产出页面详情加载
	 * 
	 * @return
	 */
	@RequestMapping("/zeroMember")
	@ResponseBody
	public List<LowDetails> zeroMember(@ModelAttribute ProjectInfo projectInfo,
			String client,String month,String line,String column) {
		List<LowDetails> result = new ArrayList<>();
		try {
			authentication0(projectInfo, client);
            result = (List<LowDetails>) lowLocService.zeroMember(projectInfo,client,month,line,column);
        }catch (AuthenticationException e) {
            LOG.error("zeroMember occurs AuthenticationException");
            result = new ArrayList<>();
        } catch (Exception e) {
        	LOG.error("zeroMember exception, error:" + e.getMessage());
        	result = new ArrayList<>();
        }
		return result;
	}
	/**
	 * 低产出页面详情加载
	 * 
	 * @return
	 */
	@RequestMapping("/lowMember")
	@ResponseBody
	public List<LowDetails> lowMember(@ModelAttribute ProjectInfo projectInfo,
			String client,String month,String line,String column) {
		List<LowDetails> result = new ArrayList<>();
		try {
			authentication0(projectInfo, client);
            result = (List<LowDetails>) lowLocService.lowMember(projectInfo,client,month,line,column);
        }catch (AuthenticationException e) {
            LOG.error("lowMember occurs AuthenticationException");
            result = new ArrayList<>();
        } catch (Exception e) {
        	LOG.error("lowMember exception, error:" + e.getMessage());
        	result = new ArrayList<>();
        }
		return result;
	}
	/**
	 * 页面加载产出分析汇总报表
	 * 
	 * @return
	 */
	@RequestMapping("/loadreportFormTable1")
	@ResponseBody
	public List<LowOutputEntity> loadreportFormTable1(@ModelAttribute ProjectInfo projectInfo,String client,String month) {
		List<LowOutputEntity> result = new ArrayList<>();
		try {
			authentication0(projectInfo, client);
            result = (List<LowOutputEntity>) lowLocService.loadreportFormTable1(projectInfo,client,month);
        } catch (AuthenticationException e) {
            LOG.error("loadreportFormTable1 occurs AuthenticationException");
            result = new ArrayList<>();
        } catch (Exception e) {
        	LOG.error("loadreportFormTable1 exception, error:" + e.getMessage());
        	result = new ArrayList<>();
        }
		return result;
	}
	/**
	 * 页面加载产出分析汇总饼图
	 * 
	 * @return
	 */
	@RequestMapping("/loadreportFormcharts1")
	@ResponseBody
	public List<List<String>> loadreportFormcharts1(@ModelAttribute ProjectInfo projectInfo,String client,String month) {
		List<List<String>> result = new ArrayList<>();
		try {
			authentication0(projectInfo, client);
            result = lowLocService.loadreportFormcharts1(projectInfo,client,month);
        } catch (AuthenticationException e) {
            LOG.error("loadreportFormcharts1 occurs AuthenticationException");
            result = new ArrayList<>();
        } catch (Exception e) {
        	LOG.error("loadreportFormcharts1 exception, error:" + e.getMessage());
        	result = new ArrayList<>();
        }
		return result;
	}

	/**
	 * 页面加载无产出汇总报表
	 * 
	 * @return
	 */
	@RequestMapping("/loadreportFormTable2")
	@ResponseBody
	public List<Map<String, Object>> loadreportFormTable2(@ModelAttribute ProjectInfo projectInfo,String client,String month) {
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			authentication0(projectInfo, client);
            result = lowLocService.loadreportFormTable2(projectInfo,client,month);
        } catch (AuthenticationException e) {
            LOG.error("loadreportFormTable2 occurs AuthenticationException");
            result = new ArrayList<>();
        } catch (Exception e) {
        	LOG.error("loadreportFormTable2 exception, error:" + e.getMessage());
        	result = new ArrayList<>();
        }
		return result;
	}

	/**
	 * 页面加载无产出汇总饼图
	 * 
	 * @return
	 */
	@RequestMapping("/loadreportFormcharts2")
	@ResponseBody
	public List<List<String>> loadreportFormcharts2() {
		return lowLocService.loadreportFormcharts2();
	}

	/**
	 * 页面加载低产出汇总报表
	 * 
	 * @return
	 */
	@RequestMapping("/loadreportFormTable3")
	@ResponseBody
	public List<Map<String, Object>> loadreportFormTable3(@ModelAttribute ProjectInfo projectInfo,String client,String month) {
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			authentication0(projectInfo, client);
            result = lowLocService.loadreportFormTable3(projectInfo,client,month);
        } catch (AuthenticationException e) {
            LOG.error("loadreportFormTable3 occurs AuthenticationException");
            result = new ArrayList<>();
        } catch (Exception e) {
        	LOG.error("loadreportFormTable3 exception, error:" + e.getMessage());
        	result = new ArrayList<>();
        }
		return result;
	}

	/**
	 * 未进行低产出分析
	 * 
	 * @return
	 */
	@RequestMapping("/loadreportFormTable4")
	@ResponseBody
	public List<Map<String, Object>> loadreportFormTable4(@ModelAttribute ProjectInfo projectInfo,String client,String month) {
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			authentication0(projectInfo, client);
            result = lowLocService.loadreportFormTable4(projectInfo,client,month);
        } catch (AuthenticationException e) {
            LOG.error("loadreportFormTable4 occurs AuthenticationException");
            result = new ArrayList<>();
        } catch (Exception e) {
        	LOG.error("loadreportFormTable4 exception, error:" + e.getMessage());
        	result = new ArrayList<>();
        }
		return result;
	}
	/**
	 * 首页加载低产出折线图
	 * 
	 * @return
	 */
	@RequestMapping("/loadZXT")
	@ResponseBody
	public List<List<Map<String, Object>>> loadZXT(ProjectInfo proj) {
		return lowLocService.loadZXT(proj);
	}

	/**
	 * 首页加载低产出无产出各原因饼图
	 */
	@RequestMapping("/lowlocBT")
	@ResponseBody
	public List<List<Map<String, Object>>> lowlocBT(ProjectInfo proj) {
		List<List<Map<String, Object>>> resultList = lowLocService.lowlocBT(proj);
		return resultList;
	}

	/**
	 * 首页加载低产出各PDU柱状图
	 * 
	 * @return
	 */
	@RequestMapping("/lowlocPDU")
	@ResponseBody
	public List<List<Map<String, Object>>> lowlocPDU(ProjectInfo proj) {
		return lowLocService.lowlocPDU(proj);
	}

	/**
	 * 加载选中项目组的开发总人数
	 * 
	 * @return
	 */
	@RequestMapping("/kaifaNum")
	@ResponseBody
	public Integer kaifaNum(ProjectInfo proj) {
		return lowLocService.kaifaNum(proj);
	}

}
