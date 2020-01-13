package com.icss.mvp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.Dept;
import com.icss.mvp.entity.LowLoc;
import com.icss.mvp.entity.PageInfo;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.TableSplitResult;

public interface ILowLocDao {

	/**
	 * 查出所有低产出原因
	 * 
	 * @return
	 */
	public List<String> queryLowLocReasons();

	/**
	 * 查出所有无产出原因
	 * 
	 * @return
	 */
	public List<String> queryNoLocReasons();

	/**
	 * 查出项目所属PDU
	 * 
	 * @return
	 */
	public String queryPDU(@Param("proNo") String proNo);

	/**
	 * 查出项目名称
	 * 
	 * @return
	 */
	public String queryProName(@Param("proNo") String proNo);

	/**
	 * 查出所有成员
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryMember(@Param("proNo") String proNo);

	/**
	 * 查出该项目代码统计类型
	 * 
	 * @return
	 */
	public Integer queryTongjiType(@Param("proNo") String proNo);

	/**
	 * 查出每个人某月之前的代码量（commit方式）
	 * 
	 * @return
	 */
	public Integer queryLoc(@Param("account") String account, @Param("month") String month, @Param("no") String no);

	/**
	 * 查出每个人某月之前的代码量（commit方式）
	 * 
	 * @return
	 */
	public Integer queryLocWx(@Param("account") String account, @Param("month") String month, @Param("no") String no);

	/**
	 * 查出每个人某月之前的代码量（message方式）
	 * 
	 * @return
	 */
	public Integer queryLocByMessage(@Param("author") String author, @Param("month") String month,
			@Param("no") String no);

	/**
	 * 保存所有低产出人员
	 * 
	 * @return
	 */
	public void saveLowLoc(@Param("lowloc") LowLoc lowloc);

	/**
	 * 删除已有低产出人员
	 * 
	 * @return
	 */
	public void deleteHavedLowLoc(@Param("proNo") String proNo, @Param("month") String month);

	/**
	 * 查询已有低产出人员
	 * 
	 * @return
	 */
	public LowLoc queryFromLowloc(@Param("proNo") String proNo, @Param("account") String account,
			@Param("month") String month, @Param("standard") Integer standard);

	/**
	 * 根据PDU分类统计无产出和低产出总数
	 * @param month 
	 * @param no 
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryCountnoAndCountlow();

	/**
	 * 根据PDU分类统计无产出和低产出总数
	 * @param month 
	 * @param no 
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryCountnoAndCountlow1(@Param("no") String no, @Param("month") String month);
	/**
	 * 根据PDU分组
	 * 
	 * @return
	 */
	public List<String> queryPDUs();

	/**
	 * 根据PDU分组统计总人数
	 * 
	 * @return
	 */
	public Integer queryPDUNum(@Param("pdu") String pdu);

	/**
	 * 查询低产出的PDU
	 * @param month 
	 * @param no 
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryPDUfromLowloc(@Param("no") String no,@Param("month") String month);

	/**
	 * 根据PDU分原因统计低产出和无产出的人数
	 * 
	 * @return
	 */
	/*public List<Map<String, Object>> queryLowlocNum(@Param("pdu") String pdu);*/

	/**
	 * 根据PDU统计低产出和无产出的总人数
	 * 
	 * @return
	 */
	/*public Integer queryLowlocCount(@Param("pdu") String pdu, @Param("type") String type);*/

	/**
	 * 直接统计低产出和无产出各原因总数
	 * 
	 * @return
	 */
	/*public Integer queryReasonCount(@Param("sureLowloc") String sureLowloc, @Param("lowLocReason") String lowLocReason);*/

	/**
	 * 查询低产出项目明细
	 * @param no 
	 * @param month 
	 * 
	 * @return
	 */
	public List<LowLoc> queryLowlocDetail(@Param("no") String no,@Param("month") String month);

	/**
	 * 查询所有在行项目
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryDoingPro1();

	/**
	 * 查询每个项目每个月的分析数据条数
	 * @param data 
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryCountEverymonth(Map<String, Object> parameter);
	/**
	 * 查询每个项目每个月的分析总数据条数
	 * @param data 
	 * 
	 * @return
	 */
	public Integer queryCountTotalmonth(String month, Set<String> nos);
	/**
	 * 查询无产出和低产出总人数
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryCountFromLowloc(Map<String, Object> parameter);

	/**
	 * 查询每个月无产出和低产出人数
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryCountByMonth(Map<String, Object> parameter);

	/**
	 * 查询无产出各原因人数
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryEveryReasonForNo();

	/**
	 * 查询低产出各原因人数
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryEveryReasonForLow();

	/**
	 * 首页加载低产出折线图
	 * 
	 * @return
	 */
	// public List<Map<String, Object>> loadZXT(Map<String, Object> map);
	public List<Map<String, Object>> loadZXTfromViewOP(Map<String, Object> map);

	public List<Map<String, Object>> loadZXTfromViewHW(Map<String, Object> map);

	public List<Map<String, Object>> loadZXTfromViewArea(Map<String, Object> map);

	/**
	 * 首页加载低产出各原因饼图
	 * 
	 * @return
	 */
	// public List<Map<String, Object>> lowlocBT(Map<String, Object> map);
	public List<Map<String, Object>> lowlocBTfromViewOP(Map<String, Object> map);

	public List<Map<String, Object>> lowlocBTfromViewHW(Map<String, Object> map);

	public List<Map<String, Object>> lowlocBTfromViewArea(Map<String, Object> map);

	/**
	 * 首页加载无产出各原因饼图
	 * 
	 * @return
	 */
	// public List<Map<String, Object>> nolocBT(Map<String, Object> map);
	public List<Map<String, Object>> nolocBTfromViewOP(Map<String, Object> map);

	public List<Map<String, Object>> nolocBTfromViewHW(Map<String, Object> map);

	public List<Map<String, Object>> nolocBTfromViewArea(Map<String, Object> map);

	/**
	 * 首页加载低产出各PDU柱状图
	 * 
	 * @return
	 */
	public Map<String, Object> lowlocPDU(@Param("proNo") String proNo, @Param("month") String month);

	/**
	 * 加载选中项目组的开发总人数
	 * 
	 * @return
	 */
	public Integer kaifaNum(Map<String, Object> map);

	/**
	 * 加载子部门
	 * 
	 * @return
	 */
	public List<Map<String, Object>> loadZBM(Map<String, Object> map);

	/**
	 * 项目PDU
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getPDU(Map<String, Object> map);

	/**
	 * 所有PDU
	 * 
	 * @return
	 */
	public List<Dept> getAllPDU(Map<String, Object> map);

	/**
	 * 项目DU
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getDU(Map<String, Object> map);

	/**
	 * 所有DU
	 * 
	 * @return
	 */
	public List<Dept> getAllDU(Map<String, Object> map);

	/**
	 * 项目HWZPDU
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getHWZPDU(Map<String, Object> map);

	/**
	 * 所有HWZPDU
	 * 
	 * @return
	 */
	public List<Dept> getAllHWZPDU(Map<String, Object> map);

	/**
	 * 所有地域
	 * 
	 * @return
	 */
	public List<Dept> getAllAreas(Map<String, Object> map);
	
	/**
	 * 项目PDU_SPDT
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getPDUSPDT(Map<String, Object> map);

	/**
	 * 所有PDU_SPDT
	 * 
	 * @return
	 */
	public List<Dept> getAllPDUSPDT(Map<String, Object> map);

	/**
	 * 所有成员人数
	 * 
	 * @return
	 */
	public Integer getAllMembers(@Param("no") String no);

	/**
	 * 所有低产出成员人数
	 * 
	 * @return
	 */
	public Map<String, Object> getLowlocMembers(@Param("month") String month, @Param("no") String no);

	public List<String> lowlocAllProjNofromOPbyPDU(@Param("id") String id);

	public List<String> lowlocAllProjNofromOPbyDU(@Param("id") String id);

	public List<String> lowlocAllProjNofromHWbyHWZPDU(@Param("id") String id);

	public List<String> lowlocAllProjNofromHWbyPDUSPDT(@Param("id") String id);

	public List<String> lowlocAllProjNofromArea(@Param("id") String id);

	Set<String> projectNos(Map<String, Object> param);

	//低产出分析总表
	List<Map<String, Object>> queryPDUNum1(Map<String, Object> parameter);

	public List<String> queryPDUs1(@Param("no")String no);

	//无产出pdu查询
	public List<Map<String, Object>> queryPDUfromLowloc3(Map<String, Object> parameter);

	//按照项目编号和时间查询计数
	List<Map<String, Object>> queryCountMonthByNo(@Param("mouth")String mouth,@Param("no") String no);
	public List<Map<String, Object>> queryPDUfromLowloc2(Map<String, Object> parameter);

	public List<Map<String, Object>> AnalysisSummary(Map<String, Object> parameter);
	public List<Map<String, Object>> zeroMember(Map<String, Object> parameter);
	public List<Map<String, Object>> lowMember(Map<String, Object> parameter);

	public List<Map<String, Object>> queryCountnoAndCount(@Param("no") String no,@Param("month") String month);

	public List<Map<String, Object>> queryPDUfromLowloczero(@Param("no") String no,@Param("month") String month);
	
}