package com.icss.mvp.service.rank;

import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.rank.RankSalaryDao;
import com.icss.mvp.entity.rank.ProjectMonthBudget;
import com.icss.mvp.entity.rank.RankTotalHours;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.util.BigDecUtils;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chengchenhui
 * @title: RankSalaryService
 * @projectName mvp
 * @description: 项目成本计算
 * @date 2019/7/1716:08
 */
@Service("RankSalaryService")
public class RankSalaryService {



    private static Logger logger = Logger.getLogger(RankSalaryService.class);

    public final static Map<String, Integer> month_days = new HashMap<String, Integer>() {
        {
            put("01", 22);
            put("02", 17);
            put("03", 22);
            put("04", 20);
            put("05", 22);
            put("06", 20);

            put("07", 22);
            put("08", 23);
            put("09", 21);
            put("10", 18);
            put("11", 22);
            put("12", 21);
        }
    };

    @Autowired
    private IProjectListDao projectListDao;

//    @Autowired
//    private SysDictItemService sysDictItemService;

    @Autowired
    private RankSalaryDao dao;

    public void calculate(String proNo, String selectDate,
                          BaseResponse response) {
        try {
            Map<String,Object> map = parseDates(selectDate);
            getAccountHours(proNo,response,map);
            response.setCode("success");
        } catch (Exception e) {
            response.setCode("failed");
            logger.error("salary calculate failed："+e.getMessage());
        }
    }

    //参数封装
    public Map<String,Object> parseDates(String calDate) {
        Map<String,Object> paraMap = new HashMap<>();
        Date date = !StringUtilsLocal.isBlank(calDate) ? DateUtils.parseDate(calDate, "yyyy-MM") : new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String ym_date = DateUtils.formatDate(calendar.getTime(), "yyyy-MM");
        if (ym_date.equals(DateUtils.formatDate(new Date(), "yyyy-MM"))) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        paraMap.put("monthDays", month_days.get(DateUtils.formatDate(calendar.getTime(), "MM")));
        paraMap.put("ymDate", DateUtils.formatDate(calendar.getTime(), "yyyy-MM"));
        paraMap.put("ymdDate", DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd"));
        return paraMap;
    }



    /**
     * 　　* @description: 获取每个员工的累计工时
     * 　　* @param map
     * 　　* @return List
     * 　　* @throws
     * 　　* @author chengchenhui
     * 　　* @date 2019/7/28 9:58
     *
     */
    public void getAccountHours(String no, BaseResponse response,Map<String,Object> map){
        Set<String> setNos = new HashSet<>();
        map.put("no",no);
        List<RankTotalHours> totalHours = dao.getAccountHours(map);
        Integer days = Integer.valueOf(map.get("monthDays").toString());
        String  ymd_date = map.get("ymdDate").toString();
        if (null != totalHours && totalHours.size() > 0) {
            for (RankTotalHours hours : totalHours) {
                //出勤时长大于月理论
                hours.setAttendence(hours.getAttendence() > days * 8 ? days * 8 : hours.getAttendence());
                setNos.add(hours.getNo());
            }
            calCulateCost(totalHours, setNos,days,ymd_date);
        }else{
            response.setMessage(!StringUtilsLocal.isBlank(no) ? "This project members is empty :" + no :
                "all project members are empty");
        }

    }


    public void calCulateCost(List<RankTotalHours> totalHours, Set<String> nos,Integer days,String ymd_date) {
        Double normal;
        Double attendence;
        Double overtime;
        Double salary;
        List<ProjectMonthBudget> pbList = new ArrayList<>();
        for (String no : nos) {
            normal = 0.0;
            attendence = 0.0;
            overtime = 0.0;
            ProjectMonthBudget pb = new ProjectMonthBudget();
            pb.setNo(no);
            List<RankTotalHours> list = totalHours.stream().filter(m -> no.equals(m.getNo())).collect(Collectors.toList());
            for (RankTotalHours rh : list) {
                salary = rh.getSalary();
                normal += BigDecUtils.add(BigDecUtils.mul(salary, days) * 1.06,
                    BigDecUtils.mul(salary, 2) * 2 * 1.06);//理论
                attendence += BigDecUtils.mul(BigDecUtils.div(rh.getAttendence(), 8, 5), salary) * 1.06;//出勤
                overtime += BigDecUtils.mul(BigDecUtils.div(rh.getOverTime(), 8, 5), salary) * 2 * 1.06;//加班
            }
            pb.setNormalOut(BigDecUtils.round(normal, 2));
            pb.setAttendMoney(BigDecUtils.round(attendence, 2));
            pb.setOverMoney(BigDecUtils.round(overtime, 2));
            pbList.add(pb);
        }
        dao.SaveProjectCost(pbList,ymd_date);
    }
}
