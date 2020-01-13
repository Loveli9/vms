package com.icss.mvp.dao;

import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.entity.VisitRecord;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * <pre>
 * <b>描述：登录模块dao接口层</b>
 * <b>任务编号：</b>
 * <b>作者：张鹏飞</b> 
 * <b>创建日期： 2017年5月12日 下午2:57:14</b>
 * </pre>
 */
public interface ILoginDao {

	UserInfo getUserInfo(UserInfo user);

	int getUserCountByRoleId(@Param("roleId") int roleId);

	int addUserInfo(UserInfo user);

	String querypAuthById(@Param("permissionid") Integer permissionid);

	List<String> queryAllAuth();

	String getUserOfPer();

	void saveLoginRecord(@Param("record") VisitRecord record);

	VisitRecord getRecords();

}