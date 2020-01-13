package com.icss.mvp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.CodeParam;
import com.icss.mvp.entity.CodeQualityInfo;
import com.icss.mvp.entity.LogModifyNum;
import com.icss.mvp.entity.SvnLogs;




/**
 * 
 * <pre>
 * <b>描述：svn数据操作dao层</b>
 * <b>任务编号：</b>
 * <b>作者：张鹏飞</b> 
 * <b>创建日期： 2017年5月19日 下午5:39:17</b>
 * </pre>
 */
public interface ISvnTaskDao
{
    /**
     * 
     * <pre>
     * <b>描述：保存svn日志</b>
     * <b>任务编号：</b>
     * <b>作者：张鹏飞</b> 
     * <b>创建日期： 2017年5月19日 下午5:39:57</b>
     *  @param logList
     *  @return
     * </pre>
     */
    int saveLogList(List<SvnLogs> logList);
    
    /**
     * 
     * <pre>
     * <b>描述：获取svn代码新增行数月度汇总</b>
     * <b>任务编号：</b>
     * <b>作者：张鹏飞</b> 
     * <b>创建日期： 2017年5月19日 下午5:40:27</b>
     *  @param month
     *  @return
     * </pre>
     */
    List<CodeQualityInfo> getMonthCollect(@Param("month") String month);
    
    /**
     * 
     * <pre>
     * <b>描述：更新svn代码新增行数月度汇总</b>
     * <b>任务编号：</b>
     * <b>作者：张鹏飞</b> 
     * <b>创建日期： 2017年5月19日 下午5:41:11</b>
     *  @param list
     *  @param month
     *  @return
     * </pre>
     */
    int updateSvnMonthCollect(@Param("list")CodeQualityInfo list, @Param("month") String month);

	void insertSvnMonthCollect(@Param("list")CodeQualityInfo info);
	
	
	public List<SvnLogs> serchSvn(@Param("no")String no);
	
	public int addCode(@Param("month")int month, @Param("no")String no, @Param("author")String author);
	public int addCodeWx(@Param("month")int month, @Param("no")String no, @Param("author")String author);
	
	public List<Integer> searchByAuthor(@Param("author")String author);
	public List<Integer> searchByAuthorWx(@Param("author")String author);
	
	public List<Integer> searchByAuthors(@Param("author")String author);
	
	void updatelasttime(@Param("map")Map<String, Object> map );
	void insertlasttime(@Param("map")Map<String, Object> map );
	Date searchByNoMax(@Param("no")String no);
	Date searchByReidMax(@Param("reId")String reId);
	Date searchByNo(@Param("no")String no,@Param("id")String id);
	Date queryTimeByNo(@Param("no")String no);
	Date searchStartDate(@Param("no")String no);
	List<String> searchNameByno(@Param("no")String no);

	List<Integer> searchById(@Param("id")String id);
	void savesvnurl(@Param("map")Map<String, Object> map);
	Integer seachIdByUrl(@Param("url")String url);
	
	/**
	 * 统计当月个人message总代码量
	 * @param month
	 * @param no
	 * @param author
	 * @return
	 */
	public Integer addCodeByMessage(@Param("month")int month, @Param("no")String no, @Param("author")String author);
	
	
	
	/**
	 * 统计message代码量(方式二)
	 * @param month
	 * @param no
	 * @param author
	 * @return
	 */
	public Integer addCodeByMessages(@Param("month")int month, @Param("no")String no,@Param("ids")String ids);
	
	
	
	/**
	 * 根据commit进行数据筛选-java/c
	 * @param month
	 * @param no
	 * @param author
	 * @param codeType
	 * @return
	 */
	public Integer getCodeNumByCommit(@Param("month")int month, @Param("no")String no, @Param("author")String author,@Param("codeType")String codeType);
	public Integer getCodeNumByCommitWx(@Param("month")int month, @Param("no")String no, @Param("author")String author,@Param("codeType")String codeType);
	/**
	 * 根据message进行数据筛选-JAVA/C++/JS
	 * @param j
	 * @param no
	 * @param author
	 * @param codeType
	 * @return
	 */
	Integer getCodeNumByMessage(@Param("month")int month, @Param("no")String no, @Param("author")String author,@Param("codeType")String codeType);
	
	/**
	 * 根据commit提交人获取代码量
	 * @param svnGitNo
	 * @param author
	 * @param codeType
	 * @return
	 */
	List<Map<String, String>> getCodeNumByCommitAndType(@Param("no")String no,
			@Param("svnGitNo")String svnGitNo,
			@Param("author")String author, 
			@Param("codeType")String codeType,
			@Param("startDate")String startDate,
			@Param("endDate")String endDate);
	
	/**
	 * 根据commit提交人获取代码量(不进行折算)
	 * @param svnGitNo
	 * @param author
	 * @param codeType
	 * @return
	 */
	List<Map<String, String>> getCodeNumByCommitAndTypeY(@Param("no")String no,
			@Param("svnGitNo")String svnGitNo,
			@Param("author")String author, 
			@Param("codeType")String codeType,
			@Param("startDate")String startDate,
			@Param("endDate")String endDate);
	
	/**
	 * 根据commit提交人获取代码量
	 * @param svnGitNo
	 * @param author
	 * @param codeType
	 * @return
	 */
	List<Map<String, String>> getCodeNumByParam(@Param("no")String no,
			@Param("svnGitNo")String svnGitNo,
			@Param("author")String author, 
			@Param("codeType")String codeType);
	
	
	/**
	 * 根据commit提交人获取代码量(不进行折算)
	 * @param svnGitNo
	 * @param author
	 * @param codeType
	 * @return
	 */
	List<Map<String, String>> getCodeNumByParamY(@Param("no")String no,
			@Param("svnGitNo")String svnGitNo,
			@Param("author")String author, 
			@Param("codeType")String codeType);
	
	/**
	 * 根据message 以及代码类型统计代码量 
	 * @param svnGitNo
	 * @param author
	 * @param codeType
	 * @return
	 */
	List<Map<String, String>> getCodeNumByMessageAndType(@Param("no")String no, 
		@Param("svnGitNo")String svnGitNo, 
		@Param("author")String author, 
		@Param("codeType")String codeType,
		@Param("startDate")String startDate,
		@Param("endDate")String endDate);
	
	
	/**
	 * 根据message 以及代码类型统计代码量 
	 * @param svnGitNo
	 * @param author
	 * @param codeType
	 * @return
	 */
	List<Map<String, String>> getCodeNumByMessageAndTypeY(@Param("no")String no, 
		@Param("svnGitNo")String svnGitNo, 
		@Param("author")String author, 
		@Param("codeType")String codeType,
		@Param("startDate")String startDate,
		@Param("endDate")String endDate);

	void insertcommitrecord(List<LogModifyNum> logmodelList);

	/**
	 * 获取最后提交时间、当月提交次数
	 * @param no
	 * @param svnGitNo
	 * @param author
	 * @return
	 */
	List<Map<String, Object>> searchSubMessage(@Param("no")String no, 
			@Param("svnGitNo")String svnGitNo, 
			@Param("author")String author,
			@Param("startDate")String startDate,
			@Param("endDate")String endDate);
	List<Map<String, Object>> searchNewSubMessage(@Param("no")String no, 
			@Param("svnGitNo")String svnGitNo, 
			@Param("author")String author);


	List<Map<String, Object>> getLastTimeGroupType(@Param("no")String no);
}
