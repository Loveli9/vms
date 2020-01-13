package com.icss.mvp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.icss.mvp.entity.IterationMeasureIndex;
import com.icss.mvp.entity.MeasureComment;
import com.icss.mvp.entity.MeasureLoadEveryInfo;
import com.icss.mvp.entity.MeasureProcess;
import com.icss.mvp.entity.ProjectInfo;

public interface MeasureCommentDao {

	/**
	 * 保存指标点评信息
	 * 
	 * @param measureComment
	 * @return
	 */
	int saveMeasureComment(@Param("measureComment") MeasureComment measureComment);

	/**
	 * 查询指标点评列表
	 * 
	 * @param parameter
	 * @return
	 */
	List<MeasureComment> queryCurrentCommentList(Map<String, Object> parameter);
	
	List<MeasureComment> queryHistoryCommentList(Map<String, Object> parameter);

	Date getCreateTime(Map<String, Object> parameter);

	/**
	 * 查询项目名称
	 * 
	 * @param proNo
	 * @return
	 */
	String queryName(@Param("proNo") String proNo);

	String queryUnit(@Param("id") Integer id);

	List<Map<String, Object>> queryIterativeWork(@Param("proNo") String proNo);

	/*List<Map<String, Object>> queryIterativeName(@Param("proNo") String proNo);*/

	List<Map<String, Object>> queryQroblemRisk(@Param("proNo") String proNo);

	/**
	 * 新增指标值
	 * 
	 * @param meaInfo
	 */
	void insert(MeasureLoadEveryInfo meaInfo);

	/**
	 * 查询时间段内指标最新值
	 */
	String queryNewestValue(@Param("no") String no, @Param("id") String id, @Param("startDate") String startDate,
			@Param("endDate") String endDate);

	/**
	 * 查询时间段内指标最新时间
	 */
	Map<String, Object> queryNewestTime(@Param("no") String no, @Param("id") String id,
			@Param("startDate") String startDate, @Param("endDate") String endDate);

	/**
	 * 查询过程指标所属指标ID
	 */
	String queryMeasureId(@Param("id") Integer id);

	/**
	 * 查询所属相同的其它过程指标
	 */
	List<String> querySameProcessMeasures(@Param("mid") String mid);

	/**
	 * 匹配当前迭代
	 */
	String currentIteration(@Param("no") String no, @Param("date") String date);

	/**
	 * 查询index表里有没有存过值
	 */
	IterationMeasureIndex queryFromIndex(@Param("mid") String mid, @Param("iter") String iter);

	/**
	 * 更新index表里指标值
	 */
	void updateIndexValue(IterationMeasureIndex iterationMeasureIndex);

	/**
	 * 使用no查询指标点评列表
	 *
	 * @param parameter
	 * @return
	 */
    List<MeasureComment> queryCommentListByNo(Map<String, Object> parameter);
    
	Integer getMeasureChangeKey(@Param("measureId")Integer measureId, @Param("proNo")String proNo, @Param("changeDate")String changeDate);
	
	void insertMeasureCommentChange(@Param("changeValue")Integer changeValue, @Param("measureId")Integer measureId, @Param("proNo")String proNo, @Param("changeDate")String changeDate);
	
	void updateMeasureCommentChange(@Param("changeValue")Integer changeValue, @Param("id")Integer id);
	
	List<MeasureComment> queryMeasureList(@Param("proNo") String proNo, @Param("measureIds")String[] measureIds, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	Integer getMeasureChange(@Param("measureId") Integer measureId, @Param("proNo") String proNo, @Param("date") String date);
	
	List<MeasureProcess> queryMeasureProcessData(Map<String, Object> parameter);
	
	String measureConfigurationRecord(@Param("proNo") String proNo,@Param("endDate") String endDate);
	/**
	 * 查询指标所属过程指标
	 */
	List<MeasureProcess> queryProcessMeasure(@Param("mid") String mid);
	
	ProjectInfo getDateTimeByNo(@Param("proNo") String proNo);
	
	String getCloseDateByNo(@Param("proNo") String proNo);

	String getHistoryValue(@Param("proNo")String proNo, @Param("measureId") String measureId);


	String measureConfigurationTeamRecord(@Param("teamId") String teamId,@Param("endDate") String endDate);

	
	Map<String, Object> getMeasureValue(@Param("proNo") String proNo, @Param("measureId") String measureId,
			@Param("endDate") String endDate, @Param("startDate") String startDate);

}