package com.icss.mvp.dao.io;

import com.icss.mvp.entity.HandsontableInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HandsontableDao {

	/**
	 * 查询全部excelTable信息
	 * @return
	 */
	List<HandsontableInfo> getHandsontables();
	/**
	 * 查询单个excelTable信息，使用no和module确定
	 * @return
	 */
	HandsontableInfo getHandsontable(@Param("no")String no, @Param("module")String module);

	/**
	 * 添加excelTable信息
	 * @param handsontableInfo
	 * @return
	 */
	int insertHandsontable(HandsontableInfo handsontableInfo);
}
