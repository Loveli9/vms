package com.icss.mvp.service;

import com.icss.mvp.dao.IPersonnelTrackDao;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.member.MemberEntity;
import com.icss.mvp.service.member.MemberService;
import com.icss.mvp.util.CollectionUtilsLocal;
import com.icss.mvp.util.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Administrator
 */
@Service("PersonnelTrackService")
public class PersonnelTrackService {

    private static Logger logger = Logger.getLogger(PersonnelTrackService.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private IUserManagerDao userManagerDao;

    @Autowired
    private IPersonnelTrackDao personnelTrackDao;

    /**
     * 整体评估
     *
     * @param projectId
     * @param duty
     * @return
     */
    public TableSplitResult<List<Map<String, String>>> metricRespective(HttpServletRequest request, String projectId, String duty) {

        UserInfo userInfo = userManagerDao.getUserInfoByName(CookieUtils.value(request, CookieUtils.USER_NAME));
        String userType = null != userInfo ? userInfo.getUsertype() : null;
        Set<String> duties = CollectionUtilsLocal.splitToSet(duty);
        List<MemberEntity> stuff = memberService.getProjectMembers(projectId, duties).getData();
        List<Map<String, String>> data = new ArrayList<>();
        for (MemberEntity entity : stuff) {
            if (entity == null || StringUtils.isBlank(entity.getAccount())) {
                continue;
            }

            Map<String, String> ability = new HashMap<>(0);

            ability.put("name", entity.getName());
            ability.put("account", "2".equals(userType) ? entity.getClientAccount() : entity.getAccount());
            ability.put("role", entity.getDuty());
            ability.put("demandCompletion", "");
            ability.put("codeQuantity", "");
            ability.put("testCase", "");
            ability.put("file", "");
            ability.put("personalDefects", "");
            ability.put("riskPersonnel", "");
            ability.put("riskReason", "");
            data.add(ability);
        }

        TableSplitResult<List<Map<String, String>>> result = new TableSplitResult<>();
        result.setRows(data);

        return result;
    }

    /**
     * 质量问题
     *
     * @param pageRequest
     * @return
     */
    public TableSplitResult<List<Map<String, String>>> queryQualityProblem(PageRequest pageRequest) {

        TableSplitResult<List<Map<String, String>>> result = new TableSplitResult<>();
        try {
            List<Map<String, String>> list = new ArrayList<>();
            String[] arr = {"CodeReview问题：命名规则", "代码重复率超标", "构建失败", "findbugs扫描问题"};
            String[] arr1 = {"1", "2", "3", "4"};
            for (int i = 0; i < arr.length; i++) {
                Map<String, String> map = new HashMap<>(0);
                map.put("problemNumber", "" + arr1[i]);
                map.put("description", "" + arr[i]);
                list.add(map);
            }
            if (list.size() <= 0) {
                result.setErr(new ArrayList<>(), 1);
            }
            result.setRows(list);
            result.setTotal(list.size());
        } catch (Exception e) {
            logger.error("queryQualityProblem exception, error:", e);
        }
        return result;
    }

    /**
     * 项目群人员跟踪
     *
     * @param pageRequest
     * @return
     */
    public TableSplitResult<List<Map<String, String>>> queryGroupPersonTrack(PageRequest pageRequest) {
        TableSplitResult<List<Map<String, String>>> result = new TableSplitResult<>();

        try {
            Integer count = personnelTrackDao.queryGroupPersonTrackCount();

            if (null == count) {
                result.setErr(new ArrayList<>(), 1);
            } else {
                List<Map<String, String>> list = personnelTrackDao.queryGroupPersonTrack(pageRequest);

                result.setRows(list);
                result.setTotal(count);
            }
        } catch (Exception e) {
            logger.error("queryGroupPersonTrack method of PersonnelTrackService failed: " + e.getMessage());
        }
        return result;
    }
}
