package com.icss.mvp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.SysHwdeptDao;
import com.icss.mvp.dao.TblAreaDao;
import com.icss.mvp.entity.ProjectTeam;
import com.icss.mvp.entity.TblArea;

/**
 * 地域表对应的MyBatis SQL XML配置文件的映射接口
 * @author gaoyao
 * @time 2018-4-26 9:50:09
*/
@Service("tblAreaService")
@Transactional
public class TblAreaService {
	
	@Resource
	private TblAreaDao dao;
	
	private static Logger logger = Logger.getLogger(TblAreaService.class);
	
	
    /**
	 * 在数据库中新插入一条指定的地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public Integer insertTblArea(TblArea tblArea){
		return dao.insertTblArea(tblArea);
	}
	
	/**
	 * 在数据库中修改此条地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public Integer updateTblArea(TblArea tblArea){
		return dao.updateTblArea(tblArea);
	}
	
	/**
	 * 在数据库中通过主键id查出指定的地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public TblArea getTblAreaById(String id){
		return dao.getTblAreaById(id);
	}
	
	
	/**
	 * 在数据库中通过主键id删除指定地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public Integer deleteTblAreaById(String id){
		return dao.deleteTblAreaById(id);
	}
	
	/**
	 * 在数据库中通过主键id批量删除指定地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public Integer deleteTblAreaByIdList(List<String> list){
		return dao.deleteTblAreaByIdList(list);
	}
	
	/**
	 * 在数据库中通过Map中的分页参数（startNo,pageSize）
	 * 和其他条件参数得到指定条数的地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public List<TblArea> getTblAreaForPage(Map<String,Object> map){
		return dao.getTblAreaForPage(map);
	}
	
    /**
     * 在数据库中通过Map中条件参数得到指定地域记录的总条数
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
     */
	public Integer getTblAreaCount(Map<String,Object> map){
		return dao.getTblAreaCount(map);
	}
	
	/**
     * 在数据库中查出所有地域记录
     * @author gaoyao
     * @time 2018-4-26 9:50:09
     */
	public List<TblArea> getAllTblArea(){
		return dao.getAllTblArea();
	}
	
	
	/**
	 * <p>Title: getProjectArea</p>
	 * <p>Description: 查处现有项目所在的所有地域</p>
	 * @author gaoyao
	 * @return
	 */
	public List<String> getProjectArea(){
		return dao.getProjectArea();
	}

	public List<TblArea> getTblAreaByCodes(Set<String> codes) {
		return dao.getTblAreaByCodes(codes);
	}

	public String getArea(String areaName){
		String areaCode = "";
		try {
			TblArea tr = dao.getArea(areaName);
			if(null != tr){
				areaCode = tr.getAreaCode().toString();
			}
		}catch (Exception e){
			logger.error("地域信息查询失败"+e.getMessage());
		}
		return areaCode;
	}
	
	public int getTeam(String teamName){
		int teamCode = 0;
		try {
			ProjectTeam pt = dao.getTeam(teamName);
			if(null != pt){
				teamCode = pt.getId();
			}
		}catch (Exception e){
			logger.error("团队信息查询失败"+e.getMessage());
		}
		return teamCode;
	}
}
