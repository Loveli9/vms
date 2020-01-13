package com.icss.mvp.service.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.dao.SysHwdeptDao;
import com.icss.mvp.entity.SysHwdept;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.dept.HwOrganization;
import com.icss.mvp.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @description: 华为组织机构同步
 * @author: chengchenhui
 * @create: 2019-12-12 15:19
 **/
@Service("JobHwDeptSyncronizeService")
@EnableScheduling
@PropertySource("classpath:task.properties")
@SuppressWarnings("all")
@Transactional
public class JobHwDeptSyncronizeService {
    private static Logger logger = Logger.getLogger(JobHwDeptSyncronizeService.class);

    @Autowired
    private Environment env;


    @Autowired
    private SysHwdeptDao hwdeptDao;


    @Value("${project.secretkey}")
    private String secretKeys;


    @Scheduled(cron = "${hwdept_Task_scheduled}")
    public BaseResponse synchronizeHwDepts() {
        BaseResponse result = new BaseResponse();
        Map<Long, Boolean> deptIds = new HashMap<>(16);

        for (String secretKey : CollectionUtilsLocal.splitToSet(secretKeys)) {
            int totalCount = 0;
            int step = 1;
            PageResponse<HwOrganization> stepResponse = obtainHwOrganization(step, 300, secretKey);
            if (!stepResponse.getSucceed()) {
                logger.error("华为组织机构同步失败，业务线：" + secretKey + " " + stepResponse.getMessage());
                break;
            }

            saveHwOrgnizetion(stepResponse.getData(), deptIds);

            long modified = deptIds.entrySet().stream().filter(Map.Entry::getValue).count();
            logger.info("华为组织机构信息同步成功，业务线：" + secretKey + " 共：" + totalCount + " 条，更新：" + modified + " 条");
        }

        return result;
    }

    final static Type TYPE_COLLECTION_HWDEPTS = new TypeReference<List<HwOrganization>>() {

    }.getType();



    /**
     * 分页获取华为组织机构信息
     * @param pageNumber
     * @param pageSize
     * @param key
     * @return
     */
    private PageResponse<HwOrganization> obtainHwOrganization(int pageNumber, int pageSize, String key) {
        PageResponse<HwOrganization> result = new PageResponse<>();

        String hwOrgnizetionUrl = env.getProperty("hwdept.url");

        Map<String, Object> paraMap = new HashMap<>(16);
        paraMap.put("fromIndex", (pageNumber - 1) * pageSize);
        paraMap.put("pageSize", pageSize);
        paraMap.put("secretKey", key);

        try {
            String response = HttpExecuteUtils.httpPost(hwOrgnizetionUrl, paraMap, null, getProxySetting());
            /**
             *    <pre>
             *   {
             *    "message": "",
             *     "data": [
             *      {
             *        "id": 1,
             *        "deptId": "100001",
             *        "deptName": "TPO",
             *       }
             * </pre>
             */


            JSONObject re = JSONUtils.parseInstance(response);
            if (re.getBoolean("success")) {
                result.setData(JSON.parseObject(JSON.toJSONString(re.getJSONArray("data")), TYPE_COLLECTION_HWDEPTS));
            } else {
                result.setError(CommonResultCode.INTERNAL, re.getString("message"));
            }
        } catch (Exception e) {
            logger.error("obtain hwOrganization exception, url: " + hwOrgnizetionUrl + ", para: " + JSON.toJSONString(paraMap));
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }

        result.setPageSize(result.getData().size());
        result.setPageNumber(1);

        if (result.getData() == null || result.getData().isEmpty()) {
            result.setData(new ArrayList<>());
        }

        return result;
    }

    /**
     * 保存华为组织机构信息
     * @param hwDeptList
     * @param deptIds
     */
    private void saveHwOrgnizetion(List<HwOrganization> hwDeptList, Map<Long, Boolean> deptIds) {
        if (hwDeptList == null || hwDeptList.isEmpty()) {
            return;
        }
        for (HwOrganization dept : hwDeptList) {
            Long deptId = Long.valueOf(dept.getDeptId());
            if (deptIds.containsKey(deptId)) {
                logger.error("duplicate dept, project: " + JSON.toJSONString(dept));
                continue;
            }
            SysHwdept hw = new SysHwdept();
            hw.setDeptId(deptId);
            hw.setDeptName(dept.getDeptName());
            hw.setDeptLevel(dept.getDeptLevel());
            hw.setOperateTime(DateUtils.formatDate(new Date(dept.getOperateTime()), "yyyy-MM-dd"));
            hw.setOperateUser(dept.getOperateUser());
            hw.setParentId(Long.valueOf(dept.getParentId()));
            hw.setIgnoe(0);
            try {
                hwdeptDao.replaceHwDepts(hw);
                deptIds.put(deptId, true);
            } catch (Exception e) {
                logger.error("update hwdept error:" + "【" + hw.getDeptId() + "】" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String[] getProxySetting() {
        if ("1".equals(env.getProperty("is_proxy"))) {
            try {
                String host = env.getProperty("proxy.host");
                String port = env.getProperty("proxy.port");
                String account = env.getProperty("w3.account");
                String secret = AESOperator.getInstance().decrypt(env.getProperty("w3.password"));

                return new String[]{host, port, account, secret};
            } catch (Exception e) {
                logger.error("getProxySetting exception, error:" + e.getMessage());
            }
        }

        return new String[0];
    }


}
