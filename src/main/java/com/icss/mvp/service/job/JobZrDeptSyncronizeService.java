package com.icss.mvp.service.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.dao.OpDepartmentDao;
import com.icss.mvp.entity.OpDepartment;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.dept.ZrOrganization;
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
 * @description: 中软组织机构同步
 * @author: chengchenhui
 * @create: 2019-12-12 15:20
 **/
@Service("JobZrDeptSyncronizeService")
@EnableScheduling
@PropertySource("classpath:task.properties")
@SuppressWarnings("all")
@Transactional
public class JobZrDeptSyncronizeService {
    private static Logger logger = Logger.getLogger(JobZrDeptSyncronizeService.class);

    @Autowired
    private Environment env;


    @Autowired
    private OpDepartmentDao opDepartmentDao;


    @Value("${project.secretkey}")
    private String secretKeys;


    @Scheduled(cron = "${zrdept_Task_scheduled}")
    public BaseResponse synchronizeDepts() {
        BaseResponse result = new BaseResponse();
        Map<String, Boolean> deptIds = new HashMap<>(16);

        for (String secretKey : CollectionUtilsLocal.splitToSet(secretKeys)) {
            int totalCount = 0;
            int step = 1;
            PageResponse<ZrOrganization> stepResponse = obtainZrOrganization(step, 300, secretKey);
            if (!stepResponse.getSucceed()) {
                logger.error("中软组织机构同步失败，业务线：" + secretKey + " " + stepResponse.getMessage());
                break;
            }

            saveZrOrgnizetion(stepResponse.getData(), deptIds);

            long modified = deptIds.entrySet().stream().filter(Map.Entry::getValue).count();
            logger.info("中软组织机构信息同步成功，业务线：" + secretKey + " 共：" + totalCount + " 条，更新：" + modified + " 条");
        }

        return result;
    }

    final static Type TYPE_COLLECTION_ZRDEPTS = new TypeReference<List<ZrOrganization>>() {

    }.getType();


    /**
     * 分页获取项目列表
     *
     * @param pageNumber
     * @param pageSize
     * @param key
     * @return
     */
    private PageResponse<ZrOrganization> obtainZrOrganization(int pageNumber, int pageSize, String key) {
        PageResponse<ZrOrganization> result = new PageResponse<>();

        String zrOrgnizetionUrl = env.getProperty("zrdept.url");

        Map<String, Object> paraMap = new HashMap<>(16);
        paraMap.put("fromIndex", (pageNumber - 1) * pageSize);
        paraMap.put("pageSize", pageSize);
        paraMap.put("secretKey", key);

        try {
            String response = HttpExecuteUtils.httpPost(zrOrgnizetionUrl, paraMap, null, getProxySetting());
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
                result.setData(JSON.parseObject(JSON.toJSONString(re.getJSONArray("data")), TYPE_COLLECTION_ZRDEPTS));
            } else {
                result.setError(CommonResultCode.INTERNAL, re.getString("message"));
            }
        } catch (Exception e) {
            logger.error("obtain ZrOrganization exception, url: " + zrOrgnizetionUrl + ", para: " + JSON.toJSONString(paraMap));
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }

        result.setPageSize(result.getData().size());
        result.setPageNumber(1);

        if (result.getData() == null || result.getData().isEmpty()) {
            result.setData(new ArrayList<>());
        }

        return result;
    }

    private void saveZrOrgnizetion(List<ZrOrganization> zrDeptList, Map<String, Boolean> deptIds) {
        if (zrDeptList == null || zrDeptList.isEmpty()) {
            return;
        }
        for (ZrOrganization dept : zrDeptList) {
            String deptId = dept.getDeptId();
            if (deptIds.containsKey(deptId)) {
                logger.error("duplicate dept, project: " + JSON.toJSONString(dept));
                continue;
            }
            OpDepartment op = new OpDepartment();
            op.setDeptId(deptId);
            op.setDeptName(dept.getDeptName());
            op.setDeptLevel(dept.getDeptLevel());
            op.setRemark(dept.getRemark());
            op.setLastUpdate(DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            op.setLastUpdateBy(dept.getLastUpdateBy());
            op.setEnable("0");
            op.setParentDeptId(dept.getParentId());
            op.setSeq(dept.getSeq());
            op.setCreateBy(dept.getCreateBy());
            op.setSign(dept.getSign());

            try {
                opDepartmentDao.replaceOpdepartMent(op);
                deptIds.put(deptId, true);
            } catch (Exception e) {
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
