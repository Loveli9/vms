package com.icss.mvp.dao;


import com.icss.mvp.entity.ManpowerBudget;

public interface ManpowerBudgetDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ManpowerBudget record);

    int insertSelective(ManpowerBudget record);

    ManpowerBudget selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManpowerBudget record);

    int updateByPrimaryKey(ManpowerBudget record);

    ManpowerBudget getManpowerBudgetByPmid(String pmid);

    ManpowerBudget getManpowerBudgetByProNo(String proNo);
}
