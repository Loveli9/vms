/**
 * 
 */
package com.icss.mvp.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.MeasureTopDao;
import com.icss.mvp.entity.Measure;

/**
 * @author user
 *
 */

@Service("measureTopService")
@Transactional
public class MeasureTopService {
	@Autowired
	private MeasureTopDao measureTopDao;

	public List<Measure> getCurrentMonthTopMeasure(Set<String> proNos) {
		return measureTopDao.getCurrentMonthTopMeasure(proNos);
	}

	public List<Measure> statisticsByID(Set<String> proNos, Long measureID) {
		return measureTopDao.statisticsByID(proNos, measureID);
	}

}
