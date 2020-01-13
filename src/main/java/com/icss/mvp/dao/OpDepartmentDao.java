package com.icss.mvp.dao;

import com.icss.mvp.entity.OpDepartment;
import com.icss.mvp.entity.PermissionDetail;
import com.icss.mvp.entity.SysHwdept;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 中软组织结构表对应的MyBatis SQL XML配置文件的映射接口
 * @author gaoyao
 * @time 2018-4-25 11:06:49
*/
public interface OpDepartmentDao {
	
	/**
	 * <p>Title: getSysHwdeptByPId</p>
	 * <p>Description: 通过父id查找下面的子节点信息</p>
	 * @author gaoyao
	 * @param pId
	 * @return
	 */
	public List<OpDepartment> getOpDepartmentByPId(String pId);
	
    /**
	 * 在数据库中新插入一条指定的中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer insertOpDepartment(OpDepartment opDepartment);
	
	/**
	 * 在数据库中修改此条中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer updateOpDepartment(OpDepartment opDepartment);
	
	/**
	 * 在数据库中通过主键id查出指定的中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public OpDepartment getOpDepartmentById(String id);
	
	
	/**
	 * 在数据库中通过主键id删除指定中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer deleteOpDepartmentById(String id);
	
	/**
	 * 在数据库中通过主键id批量删除指定中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer deleteOpDepartmentByIdList(List<String> list);
	
	/**
	 * 在数据库中通过Map中的分页参数（startNo,pageSize）
	 * 和其他条件参数得到指定条数的中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public List<OpDepartment> getOpDepartmentForPage(Map<String,Object> map);
	
    /**
     * 在数据库中通过Map中条件参数得到指定中软组织结构记录的总条数
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
     */
	public Integer getOpDepartmentCount(Map<String,Object> map);
	
	/**
     * 在数据库中查出所有中软组织结构记录
     * @author gaoyao
     * @time 2018-4-25 11:06:49
     */
	public List<OpDepartment> getAllOpDepartment();

    /**
     * query by dept_id
     * 
     * @param deptId
     * @return
     */
    OpDepartment getOpDepartmentByDeptId(String deptId);

    List<OpDepartment> getEnableDepartment();
    List<SysHwdept> getHwEnableDepartment();

    List<OpDepartment> getOpDepartmentByPIds(@Param("list") Set<String> deptIds);

	List<PermissionDetail> getpermissionTree();


	List<Map<String,Object>> getOpDepartmentBylevel(@Param("level") String level,@Param("list") Set<String> deptIds);
	List<Map<String,Object>> getOpDepaBylevel(@Param("level") String level,@Param("list") Set<String> deptIds);

	List<Map<String,Object>> getHwDepartmentBylevel(@Param("level") String level,@Param("list") Set<String> deptIds);

	/**
	 *
	 * @param level
	 * @param deptName
	 * @return
	 */
	OpDepartment getZrOrganiza(@Param("level") Integer level,@Param("deptName")String deptName);

    Map<String,Object> getZrOrganizaMap(@Param("buName") String buName, @Param("duName") String duName, @Param("pdtName") String pdtName);

    void replaceOpdepartMent(@Param("op") OpDepartment opDepartment);
}
