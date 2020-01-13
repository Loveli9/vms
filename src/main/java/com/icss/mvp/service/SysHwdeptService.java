package com.icss.mvp.service;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.jdbc.Null;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.SysHwdeptDao;
import com.icss.mvp.entity.SysHwdept;

@Service("sysHwdeptService")
@PropertySource(value = "classpath:/application.properties")
@Transactional
public class SysHwdeptService {

    @Resource
    private SysHwdeptDao dao;

    @Value(value = "${business_line_id_huawei}")
    private String display_line_id_huawei;

    private static Logger logger = Logger.getLogger(SysHwdeptService.class);

    /**
     * <p>Title: getSysHwdeptByPId</p>
     * <p>Description: 通过父id查找下面的子节点信息</p>
     *
     * @param pId
     * @return
     * @author gaoyao
     */
    public List<SysHwdept> getSysHwdeptByPId(String pId) {
        List<SysHwdept> result = new ArrayList<>();
        if (StringUtils.isBlank(pId)) {
            return result;
        }

        if ("0".equals(pId) && StringUtils.isNotBlank(display_line_id_huawei)) {
            SysHwdept dept = dao.getSysHwdeptByDeptId(display_line_id_huawei);
            result.add(dept);
        } else {
            result = dao.getSysHwdeptByPId(pId);
        }

        return result;
    }

    /**
     * 在数据库中新插入一条指定的华为部门记录
     *
     * @author gaoyao
     * @time 2018-4-24 19:08:41
     */
    public Integer insertSysHwdept(SysHwdept sysHwdept) {
        return dao.insertSysHwdept(sysHwdept);
    }

    /**
     * 在数据库中修改此条华为部门记录
     *
     * @author gaoyao
     * @time 2018-4-24 19:08:41
     */
    public Integer updateSysHwdept(SysHwdept sysHwdept) {
        return dao.updateSysHwdept(sysHwdept);
    }

    /**
     * 在数据库中通过主键deptId查出指定的华为部门记录
     *
     * @author gaoyao
     * @time 2018-4-24 19:08:41
     */
    public SysHwdept getSysHwdeptByDeptId(String deptId) {
        return dao.getSysHwdeptByDeptId(deptId);
    }


    /**
     * 在数据库中通过主键deptId删除指定华为部门记录
     *
     * @author gaoyao
     * @time 2018-4-24 19:08:41
     */
    public Integer deleteSysHwdeptByDeptId(String deptId) {
        return dao.deleteSysHwdeptByDeptId(deptId);
    }

    /**
     * 在数据库中通过主键deptId批量删除指定华为部门记录
     *
     * @author gaoyao
     * @time 2018-4-24 19:08:41
     */
    public Integer deleteSysHwdeptByDeptIdList(List<String> list) {
        return dao.deleteSysHwdeptByDeptIdList(list);
    }

    /**
     * 在数据库中通过Map中的分页参数（startNo,pageSize）
     * 和其他条件参数得到指定条数的华为部门记录
     *
     * @author gaoyao
     * @time 2018-4-24 19:08:41
     */
    public List<SysHwdept> getSysHwdeptForPage(Map<String, Object> map) {
        return dao.getSysHwdeptForPage(map);
    }

    /**
     * 在数据库中通过Map中条件参数得到指定华为部门记录的总条数
     *
     * @author gaoyao
     * @time 2018-4-24 19:08:41
     */
    public Integer getSysHwdeptCount(Map<String, Object> map) {
        return dao.getSysHwdeptCount(map);
    }

    /**
     * 在数据库中查出所有华为部门记录
     *
     * @author gaoyao
     * @time 2018-4-24 19:08:41
     */
    public List<SysHwdept> getAllSysHwdept() {
        return dao.getAllSysHwdept();
    }

    public List<SysHwdept> getSysHwdeptByPIds(Set<String> deptIds) {
        return dao.getSysHwdeptByPIds(deptIds);
    }

    /**
     *
     * @param level
     * @param deptName
     * @return
     */
    public String getHwOrganiza(Integer level,String deptName,String pid){
        String id = "";
        try {
            SysHwdept sw = dao.getHwOrganiza(level,deptName);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(sw == null){
                SysHwdept sysHwdept = new SysHwdept();
                sysHwdept.setDeptLevel(level);
                sysHwdept.setDeptName(deptName);
                sysHwdept.setIgnoe(0);
                sysHwdept.setOperateTime(format.format(new Date()));
                sysHwdept.setParentId(StringUtils.isEmpty(pid)?0:Long.valueOf(pid));
                sysHwdept.setOperateUser("admin");
                dao.insertSysHwdept(sysHwdept);
                SysHwdept sh = dao.getHwOrganiza(level,deptName);
                return sh.getDeptId().toString();
            }
            id = sw.getDeptId().toString();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return id;
    }

    public Map<String,Object> getHwOrganizaMap(String pdName, String pduName, String pdtName) {
        Map<String,Object> map = new HashMap();
        try {
            map = dao.getHwOrganizaMap(pdName,pduName,pdtName);
            /*if(map == null){
                map.put("lv1","");
                map.put("lv1","");
                map.put("lv1","");
            }*/
        }catch (Exception e){
            logger.error(e);
        }
        return map;
    }

    public String getHwDeptCode(String pdName) {
        String deptCode = "";
        try {
            deptCode = dao.getHwDeptCode(pdName);
        }catch (Exception e){
            logger.error("获取华为部门编号失败"+e);
        }
        return deptCode;
    }
}
