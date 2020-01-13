package com.icss.mvp.service.member;

import com.icss.mvp.dao.member.AttentionPersonDao;
import com.icss.mvp.entity.StationInformation;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.entity.member.AttentionPerson;
import com.icss.mvp.util.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: chengchenhui
 * @create: 2019-12-25 17:20
 **/
@Service
public class AttentionPersonServices {

    private static Logger logger = Logger.getLogger(AttentionPersonServices.class);
    @Autowired
    private AttentionPersonDao dao;

    @Autowired
    private HttpServletRequest request;
    /**
     * 分页查询关注人员列表
     * @param: [request]
     * @return: com.icss.mvp.entity.TableSplitResult
     * @Author: chengchenhui
     * @Date: 2019/12/25
     */
    public TableSplitResult getAttentionInfoByPage(HttpServletRequest request)throws Exception {
        TableSplitResult page = new TableSplitResult();
        String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
        if(StringUtils.isEmpty(username)){
            throw new Exception("to query parameter exception");
        }
        int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));// 每页显示条数
        int offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));// 第几页
        page.setPage(offset);
        page.setRows(limit);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("account", username);
        page.setQueryMap(map);
        List<UserInfo> persons = dao.getAttentionInfoByPage(page);
        if(null != persons && persons.size() > 0){
            for (UserInfo u : persons){
                List<String> userInfos = dao.getRoles("("+u.getPermissionids()+")");
                u.setPermissionNames(String.join(",", userInfos));
            }
        }
        page.setTotal(persons.size());
        page.setRows(persons);
        return page;
    }

    /**
     * 保存关注人员列表信息
     * @param: [person]
     * @return: void
     * @Author: chengchenhui
     * @Date: 2019/12/26
     */
    public void saveAttentionPersonnel(StationInformation station){
        AttentionPerson attentionPerson = new AttentionPerson();
        attentionPerson.setConcernPerson(station.getReceiver());
        attentionPerson.setConcernedPerson(station.getSender());
        attentionPerson.setConcernedTime(station.getSendTime());
        try {
            dao.saveAttentionPersonnel(attentionPerson);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("save concern person failed:"+e.getMessage());
        }
    }

    /**
     * 取消关注
     * @param: [person]
     * @return: void
     * @Author: chengchenhui
     * @Date: 2019/12/27
     */
    public void removeConcerns(String id)throws Exception {
        if(StringUtils.isBlank(id)){
            throw new Exception("To update parameter exception");
        }
        String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
        dao.removeConcerns(username,id);
    }
}
