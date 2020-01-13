package com.icss.mvp.controller.develop;

import java.time.LocalDate;
import java.util.*;

import com.icss.mvp.util.CollectionUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.constant.ELanguage;
import com.icss.mvp.controller.BaseController;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.entity.capacity.AbilityEntity;
import com.icss.mvp.entity.capacity.RecordEntity;
import com.icss.mvp.entity.capacity.WorkloadEntity;
import com.icss.mvp.entity.member.MemberEntity;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.service.member.MemberService;
import com.icss.mvp.service.statistics.WorkloadService;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.LocalDateUtils;

/**
 * @author Ray
 * @date 2018/9/28
 */
@Controller
@RequestMapping("/workload")
public class WorkloadController extends BaseController {

    private static Logger   logger = Logger.getLogger(WorkloadController.class);

    @Autowired
    private WorkloadService workloadService;

    @Autowired
    private MemberService   memberService;
    
    @Autowired
    private IUserManagerDao   userManagerDao;

    @RequestMapping("/abscissa")
    @ResponseBody
    public ListResponse<String> getAbscissas() {
        ListResponse<String> result = new ListResponse<>();
        result.setData(getStepPeriod());

        return result;
    }

    private List<String> getStepPeriod() {
        List<String> result = new ArrayList<>();

        LocalDate now = LocalDate.now().withDayOfMonth(1);
        for (int i = 0; i < 12; i++) {
            result.add(now.minusMonths(i).format(LocalDateUtils.YEAR_PERIOD_MONTH));
        }
        Collections.reverse(result);

        return result;
    }

    /**
     * @param projectId
     * @param type
     * @param codeType
     * @param role
     * @return
     */
    @RequestMapping("/summary")
    @ResponseBody
    public Map<String, Object> summaryMonthly(String projectId, String type, String codeType, String role, String month) {
        Map<String, Object> data = new HashMap<>();

        // Date displayMonth = DateUtils.parseDate(DateUtils.SHORT_FORMAT_GENERAL, month, new Date());
        //
        // Date nextMonth = DateUtils.getFirstDayOfAmountMonth(1, displayMonth);
        // Date oneYearBefore = DateUtils.getFirstDayOfAmountMonth(-11, displayMonth);

        Set<String> duties = CollectionUtilsLocal.splitToSet(urlDecode(role));

        // 统计自本月起一年内的数据
        LocalDate now = LocalDate.now().withDayOfMonth(1);
        Date begin = LocalDateUtils.parseDate(now.minusMonths(11));
        // 使用 between {begin} and {end} 过滤时间区间，相当于 >= {begin} and < {end}
        // end 必须多加1天，这里直接取下月第一天即可
        Date end = LocalDateUtils.parseDate(now.plusMonths(1));

        List<String> months = getStepPeriod();
        data.put("months", months);

        String[] result = new String[] { "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-" };

        String[] amount = result.clone();
        List<WorkloadEntity> ff = workloadService.summarizeAmountMonthly(projectId, codeType, begin, end);
        for (WorkloadEntity workload : ff) {
            int index = months.indexOf(workload.getPeriod());
            if (index < 0) {
                continue;
            }

            amount[index] = String.valueOf(workload.getAmount());
        }

        data.put("abilities", amount);

        String[] commit = result.clone();
        List<WorkloadEntity> cc = workloadService.summarizeCommitMonthly(projectId, begin, end);
        for (WorkloadEntity workload : cc) {
            int index = months.indexOf(workload.getPeriod());
            if (index < 0) {
                continue;
            }

            commit[index] = String.valueOf(workload.getAmount());
        }

        data.put("commits", commit);

        return data;
    }

    /**
     * 工作量产出列表
     * 
     * @param projectId
     * @param codeType
     * @param duty
     * @param month
     * @return
     */
    @RequestMapping(value = "/metric")
    @ResponseBody
    public ListResponse<AbilityEntity> metricRespective(String projectId, String codeType, String duty, String month) {
    	UserInfo userInfo = userManagerDao.getUserInfoByName(CookieUtils.value(request, CookieUtils.USER_NAME));
		String userType = null != userInfo ? userInfo.getUsertype() : null;
		
        // 统计自本月起一年内的数据
        LocalDate now = LocalDate.now().withDayOfMonth(1);
        Date begin = LocalDateUtils.parseDate(now.minusMonths(11));
        // 使用 between {begin} and {end} 过滤时间区间，相当于 >= {begin} and < {end}
        // end 必须多加1天，这里直接取下月第一天即可
        Date end = LocalDateUtils.parseDate(now.plusMonths(1));

        Set<String> duties = CollectionUtilsLocal.splitToSet(urlDecode(duty));
        List<MemberEntity> stuff = memberService.getProjectMembers(projectId, duties).getData();
        Map<String, AbilityEntity> abilities = workloadService.metricMonthly(projectId, begin, end);
        Map<String, AbilityEntity> commits = workloadService.metricCommit(projectId, codeType, begin, end);

        ELanguage language = ELanguage.getByType(codeType);
        language = language == null ? ELanguage.JAVA : language;
        Set<String> types = ELanguage.getSuffix(language);

        List<AbilityEntity> data = new ArrayList<>();
        for (MemberEntity entity : stuff) {
            if (entity == null || StringUtils.isBlank(entity.getAccount())) {
                continue;
            }
       
            Map<String, WorkloadEntity> commitRecord = new HashMap<>();
            
			String author = null;
			if (StringUtils.isNotBlank(author = entity.getRelatedAccount()) && commits.get(author) != null) {
				commitRecord = commits.get(author).getWorkloads();
			} else if (StringUtils.isNotBlank(author = entity.getClientAccount())
					&& commits.get(author.toLowerCase()) != null) {
				author = author.toLowerCase();
				commitRecord = commits.get(author).getWorkloads();
			} else if (StringUtils.isNotBlank(author = entity.getAccount())
					&& commits.get(String.valueOf(Integer.parseInt(author))) != null) {
				author = String.valueOf(Integer.parseInt(author));
				commitRecord = commits.get(author).getWorkloads();
			}
            
			AbilityEntity ability = abilities.get(author);
			if (ability == null) {
				ability = new AbilityEntity();
			}

            ability.setName(entity.getName());
			ability.setAccount("2".equals(userType) ? entity.getClientAccount() : entity.getAccount());
            ability.setRole(entity.getDuty());
            ability.setRelatedAccount(author);
            
            for (WorkloadEntity load : ability.getWorkload()) {
                Double amount = load.getRecord().stream().
                        filter(o -> types.isEmpty() || types.contains(o.getFileType())).mapToDouble(RecordEntity::getAmount).sum();

                load.setAmount((int) Math.ceil(amount));
                load.setType(language.getType());

                WorkloadEntity commit;
                if ((commit = commitRecord.get(load.getPeriod())) != null) {
                    load.setTimes(commit.getTimes());
                    load.setLastCommitTime(commit.getLastCommitTime());
                }
            }

            data.add(ability);
        }

        ListResponse<AbilityEntity> result = new ListResponse<>();
        result.setData(data);

        return result;
    }
}
