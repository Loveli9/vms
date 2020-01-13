package com.icss.mvp.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.LabelMeasureConfigDao;
import com.icss.mvp.entity.LabelMeasureConfig;

/**
 * 指标配置表表对应的MyBatis SQL XML配置文件的映射接口
 * @author Administrator
 * @time 2018-5-10 15:32:49
*/

@Service("labelMeasureConfigService")
@Transactional
public class LabelMeasureConfigService {
	
	@Resource
	private LabelMeasureConfigDao dao;
	
	private static Logger logger = Logger.getLogger(LabelMeasureConfigService.class);
	
	
    /**
	 * 在数据库中新插入一条指定的指标配置表记录
	 * @author Administrator
     * @time 2018-5-10 15:32:49
	 */
	public Integer insertLabelMeasureConfig(LabelMeasureConfig labelMeasureConfig){
		return dao.insertLabelMeasureConfig(labelMeasureConfig);
	}
	
	/**
	 * 在数据库中修改此条指标配置表记录
	 * @author Administrator
     * @time 2018-5-10 15:32:49
	 */
	public Integer updateLabelMeasureConfig(LabelMeasureConfig labelMeasureConfig){
		return dao.updateLabelMeasureConfig(labelMeasureConfig);
	}
	
	/**
	 * 在数据库中通过主键id查出指定的指标配置表记录
	 * @author Administrator
     * @time 2018-5-10 15:32:49
	 */
	public LabelMeasureConfig getLabelMeasureConfigById(String id){
		return dao.getLabelMeasureConfigById(id);
	}
	
	
	/**
	 * 在数据库中通过主键id删除指定指标配置表记录
	 * @author Administrator
     * @time 2018-5-10 15:32:49
	 */
	public Integer deleteLabelMeasureConfigById(String id){
		return dao.deleteLabelMeasureConfigById(id);
	}
	
	/**
	 * 在数据库中通过主键id批量删除指定指标配置表记录
	 * @author Administrator
     * @time 2018-5-10 15:32:49
	 */
	public Integer deleteLabelMeasureConfigByIdList(List<String> list){
		return dao.deleteLabelMeasureConfigByIdList(list);
	}
	
	/**
	 * 在数据库中通过Map中的分页参数（startNo,pageSize）
	 * 和其他条件参数得到指定条数的指标配置表记录
	 * @author Administrator
     * @time 2018-5-10 15:32:49
	 */
	public List<LabelMeasureConfig> getLabelMeasureConfigForPage(Map<String,Object> map){
		return dao.getLabelMeasureConfigForPage(map);
	}
	
    /**
     * 在数据库中通过Map中条件参数得到指定指标配置表记录的总条数
	 * @author Administrator
     * @time 2018-5-10 15:32:49
     */
	public Integer getLabelMeasureConfigCount(Map<String,Object> map){
		return dao.getLabelMeasureConfigCount(map);
	}
	
	/**
     * 在数据库中查出所有指标配置表记录
     * @author Administrator
     * @time 2018-5-10 15:32:49
     */
	public List<LabelMeasureConfig> getAllLabelMeasureConfig(){
		return dao.getAllLabelMeasureConfig();
	}
}
