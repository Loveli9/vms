package com.icss.mvp.service;

import com.icss.mvp.dao.GeneralSituationDao;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.dao.ManpowerBudgetDao;
import com.icss.mvp.entity.ManpowerBudget;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.util.StringUtilsLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.icss.mvp.util.StringUtil;

@Service public class ManpowerBudgetService {

    @Autowired private ManpowerBudgetDao dao;

    @Autowired private GeneralSituationDao generalSituationDao;

    @Autowired private IUserManagerDao UserManagerDao;

    public ManpowerBudget getManpowerBudgetByProNo(String userid) {
        UserInfo user = UserManagerDao.getUserInfoByName(userid);
        String pmid = "";
        if ("2".equals(user.getUsertype())) {
            pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
        } else if ("1".equals(user.getUsertype())) {
            pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
        }
        ManpowerBudget man = dao.getManpowerBudgetByPmid(pmid);
        if (man != null) {
            if (man.getHeadcount() == null) {
                man.setHeadcount(0);
            }
            if (man.getKeyRoleCount() == null) {
                man.setKeyRoleCount(0);
            }
        }
        return man;
    }

    public ManpowerBudget getManpowerBudgetByNo(String proNo) {
        ManpowerBudget man = dao.getManpowerBudgetByProNo(proNo);
        if (man != null) {
            if (man.getHeadcount() == null) {
                man.setHeadcount(0);
            }
            if (man.getKeyRoleCount() == null) {
                man.setKeyRoleCount(0);
            }
        }
        return man;
    }

    public ManpowerBudget getManpowerBudgetByPmid(String userid) {
        UserInfo user = UserManagerDao.getUserInfoByName(userid);
        String pmid = "";
        if ("2".equals(user.getUsertype())) {
            pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
        } else if ("1".equals(user.getUsertype())) {
            pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
        }
        ManpowerBudget man = dao.getManpowerBudgetByPmid(pmid);
        if (man != null) {
            if (man.getHeadcount() == null) {
                man.setHeadcount(0);
            }
            if (man.getKeyRoleCount() == null) {
                man.setKeyRoleCount(0);
            }
        }
        return man;
    }

    public ManpowerBudget getManpowerBudgetByPmids(String pmid) {

        ManpowerBudget man = dao.getManpowerBudgetByPmid(pmid);
        if (man != null) {
            if (man.getHeadcount() == null) {
                man.setHeadcount(0);
            }
            if (man.getKeyRoleCount() == null) {
                man.setKeyRoleCount(0);
            }
        }
        return man;
    }

    public int insert(ManpowerBudget manpowerBudget) {
        if (manpowerBudget.getHeadcount() == null) {
            manpowerBudget.setHeadcount(0);
        }
        if (manpowerBudget.getKeyRoleCount() == null) {
            manpowerBudget.setKeyRoleCount(0);
        }
        String userid = manpowerBudget.getUserid();
        UserInfo user = UserManagerDao.getUserInfoByName(userid);
        String pmid = "";
        //	String proNos ="";
        if ("2".equals(user.getUsertype())) {
            pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
            manpowerBudget.setPmid(pmid);
        } else if ("1".equals(user.getUsertype())) {
            pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
            manpowerBudget.setPmid(pmid);
        }
        ManpowerBudget man = getManpowerBudgetByPmids(manpowerBudget.getPmid());
        if (man != null) {
            deleteByPrimaryKey(man.getId());
        }
        return dao.insert(manpowerBudget);
    }

    public int deleteByPrimaryKey(Integer id) {
        return dao.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(ManpowerBudget record) {
        return dao.updateByPrimaryKeySelective(record);
    }
}
