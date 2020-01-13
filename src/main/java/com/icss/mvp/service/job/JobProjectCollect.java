package com.icss.mvp.service.job;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.project.SynchronizeService;
import com.icss.mvp.service.rank.RankSalaryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chengchenhui 从项目集成管理系统获取项目列表
 */
@Service("JobProjectCollect")
@EnableScheduling
@PropertySource("classpath:task.properties")
// @SuppressWarnings("all")
@Transactional
public class JobProjectCollect {

    private static Logger     logger = Logger.getLogger(JobProjectCollect.class);

    // @Autowired
    // private ProjectCollectService service;

    @Autowired
    SynchronizeService        synchronizeService;

    @Autowired
    private RankSalaryService rankSalaryService;

    /**
     * @return
     */
    /*@Scheduled(cron = "${project_list_obtain}")*/
    public BaseResponse obtainProjectInfo() {
        BaseResponse result = new BaseResponse();
        try {
            result = synchronizeService.synchronizePIMS();
        } catch (Exception e) {
            logger.error("synchronizeService.synchronizePIMS exception, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

//    @Scheduled(cron = "${job-calculate-cost}")
    public BaseResponse calculateRank() {
        BaseResponse response = new BaseResponse();
        rankSalaryService.calculate(null, null, response);
        return response;
    }

}
