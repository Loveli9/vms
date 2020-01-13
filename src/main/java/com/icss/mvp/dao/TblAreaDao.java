package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.icss.mvp.entity.ProjectTeam;
import com.icss.mvp.entity.TblArea;
import org.apache.ibatis.annotations.Param;

/**
 * 地域表对应的MyBatis SQL XML配置文件的映射接口
 * @author gaoyao
 * @time 2018-4-26 9:50:09
*/
public interface TblAreaDao {
    /**
	 * 在数据库中新插入一条指定的地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public Integer insertTblArea(TblArea tblArea);
	
	/**
	 * 在数据库中修改此条地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public Integer updateTblArea(TblArea tblArea);
	
	/**
	 * 在数据库中通过主键id查出指定的地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public TblArea getTblAreaById(String id);
	
	
	/**
	 * 在数据库中通过主键id删除指定地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public Integer deleteTblAreaById(String id);
	
	/**
	 * 在数据库中通过主键id批量删除指定地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public Integer deleteTblAreaByIdList(List<String> list);
	
	/**
	 * 在数据库中通过Map中的分页参数（startNo,pageSize）
	 * 和其他条件参数得到指定条数的地域记录
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
	 */
	public List<TblArea> getTblAreaForPage(Map<String,Object> map);
	
    /**
     * 在数据库中通过Map中条件参数得到指定地域记录的总条数
	 * @author gaoyao
     * @time 2018-4-26 9:50:09
     */
	public Integer getTblAreaCount(Map<String,Object> map);
	
	/**
     * 在数据库中查出所有地域记录
     * @author gaoyao
     * @time 2018-4-26 9:50:09
     */
	public List<TblArea> getAllTblArea();
	
	
	/**
	 * <p>Title: getProjectArea</p>
	 * <p>Description: 查处现有项目所在的所有地域</p>
	 * @author gaoyao
	 * @return
	 */
	public List<String> getProjectArea();

    List<TblArea> getTblAreaByCodes(@Param("set") Set<String> codes);

	/**
	 *
	 * @param areaName
	 * @return
	 */
	TblArea getArea(@Param("areaName") String areaName);
	
	ProjectTeam getTeam(@Param("teamName") String teamName);
}
