package com.icss.mvp.dao.rank;

import com.icss.mvp.entity.rank.ProjectMonthBudget;
import com.icss.mvp.entity.rank.RankTotalHours;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by chengchenhui on 2019/7/18.
 */
public interface RankSalaryDao {

    void calculateMoney(Map<String,Object> params);

    List<RankTotalHours> getAccountHours(Map<String, Object> params);

    void SaveProjectCost(@Param("pbList") List<ProjectMonthBudget> pbList,@Param("ymdDate")String ymd_date);
}
