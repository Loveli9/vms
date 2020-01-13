package com.icss.mvp.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.icss.mvp.controller.ProjectLableController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.ScripInfoDao;
import com.icss.mvp.entity.ScripInfo;
/**
 * 记录采集错误消息
 * @author chengchenhui
 * 	
 */
@Service
@SuppressWarnings("all")
public class ScripInfoService {
	private static Logger logger = Logger.getLogger(ScripInfoService.class);

	@Autowired
	private ScripInfoDao dao;
	
	public void insertErrorMessage(ScripInfo info) {
		try {
			Date date = new Date();
			info.setCreateTime(date);
			info.setEffectiveTime(date);
			info.setModifyTime(date);
			info.setIsDeleted(0);
			dao.insertErrorMessage(info);
		} catch (Exception e) {
			logger.error("dao.insertErrorMessage exception, error: "+e.getMessage());
			System.out.println(e.getMessage());
		}
	}
	
	/**
	* @Description: 更新错误消息  
	* @author Administrator  
	* @date 2018年5月21日  
	 */
	public void updateErrorMessage(String id) {
		try {
			Integer a = dao.updateErrorMessage(id);
		} catch (Exception e) {
			logger.error("dao.updateErrorMessage exception, error: "+e.getMessage());
		}
	}
	
	/**
	* @Description:获取消息日志  
	* @author Administrator  
	* @date 2018年5月20日  
	* @version V1.0
	 */
	public List<Map<String, Object>> getMessage(String proNo,String token) {
		return dao.getMessage(proNo,token);
	}
	
	/**
	 * 获取本次采集已完成消息记录
	 * @param proNo
	 * @param token
	 * @return
	 */
	public Map<String, Object> getCompleteCount(String proNo, String token) {
		return dao.getCompleteCount(proNo,token);
	}
}
