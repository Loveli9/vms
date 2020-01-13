package com.icss.mvp.dao.index;

import com.icss.mvp.entity.index.InTmplIndexEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by up on 2019/4/26.
 */
public interface InTmplDao {

	/**
	 * 从measure和label表提取数据到in_tmpl_label表
	 *
	 * @return
	 */
	int insertLabel();

	/**
	 * 查询所以指标信息
	 * 
	 * @return
	 */
	List<InTmplIndexEntity> queryInTmplAll();

	void saveMeasureRangeInfo(@Param("teamId") String teamId, @Param("no") String no);

	void CopyMeasureRangeByProject(@Param("proNo") String proNo, @Param("oldNo") String oldNo);

//	List<Map<String, Object>> getConfigRecords(@Param("proNo") String proNo,@Param("ids")List<String> ids,@Param("flag")boolean flag);
    List<Map<String, Object>> getConfigRecords(Map<String, Object> map);

	List<String> getConfigMeasureIds(@Param("proNo") String proNo,@Param("flag") boolean flag);
}
