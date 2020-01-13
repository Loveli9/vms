package com.icss.mvp.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.icss.mvp.service.job.JobInstrumentPanelService;
import org.apache.log4j.Logger;

@Controller
@RequestMapping("/instrumentPanel")
public class InstrumentPanelController {

	private final static Logger LOG = Logger.getLogger(InstrumentPanelController.class);

	@Autowired
	private JobInstrumentPanelService instrumentPanelService;

	/**
	 * 计算指标
	 * 
	 * @param no
	 * @return
	 */
	@RequestMapping("/board")
	@ResponseBody
	public Map<String, Object> board(String no) {
		return instrumentPanelService.board(no);
	}

	/**
	 * 保存指标标准值
	 * 
	 * @param no
	 * @param id
	 * @param value
	 */
	@RequestMapping("/saveStandardValue")
	@ResponseBody
	public void saveStandardValue(String no, String id, String value) {
		try {
			instrumentPanelService.saveStandardValue(no, id, value);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e);
		}
	}

	/**
	 * 查询仪表盘排序
	 */
	@RequestMapping("/instrumentPanelValue")
	@ResponseBody
	public List<String> instrumentPanel(String no) {
		return instrumentPanelService.instrumentPanelValue(no);
	}

	/**
	 * 修改仪表盘排序
	 */
	@RequestMapping("/changeInstrumentPanel")
	@ResponseBody
	public void changeInstrumentPanel(String no, String sort) {
		instrumentPanelService.changeInstrumentPanel(no, sort);
	}

}