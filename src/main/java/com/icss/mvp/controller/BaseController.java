package com.icss.mvp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.icss.mvp.constant.Constants;
import com.icss.mvp.dao.IBuOverviewDao;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.entity.system.UserEntity;
import com.icss.mvp.util.CollectionUtilsLocal;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.StringUtilsLocal;

/**
 * @author Ray
 * @date 2018/9/12
 */
@Controller
public class BaseController {

    private static Logger        logger         = Logger.getLogger(BaseController.class);

    /**
     * <pre>
     *     匹配数字使用 \\d+ 会报错，必须指定长度，
     *     地区Code和部门ID都不超过10位，使用 \\d{1,9}
     * </pre>
     */
    private static final String  NUMBER_PATTERN = "\\d{1,9}";

    /**
     * <pre>
     *     /AR/AR0/2604,/AR/AR0/2605
     *     (?<=/AR/AR0/)\d{1,9}(?=(\D*?|$))
     *     (?<=/AR/AR0/)\(2604|2601)}(?=(\D*?|$))
     * </pre>
     */
    private static final String  AREA_PATTERN   = "(?<=/AR/AR0/)(%s)(?=(\\D*?|$))";

    /**
     * <pre>
     *     /HW/HW0/32/2828/2804081
     *     (?<=/HW/HW0/\d{1,9}/\d{1,9}/)\d{1,9}(?=(\D*?|$)) all of authorized
     *     (?<=/HW/HW0/\(30028|30270)/\d{1,9}/)\d{1,9}(?=(\D*?|$)) get by trunk
     *     (?<=/HW/HW0/\\d{1,9}/(30029|30030|30214)/)\d{1,9}(?=(\D*?|$)) get by branch
     *     (?<=/HW/HW0/\\d{1,9}/\d{1,9}/)(30215)(?=(\D*?|$)) get by dept
     * 
     *     /CS/2556/30028/30029/30047
     *     (?<=/CS/2556/\d{1,9}/\d{1,9}/)\d{1,9}(?=(\D*?|$))
     * </pre>
     */
    private static final String  DEPT_PATTERN   = "(?<=%s/(%s)/(%s)/)(%s)(?=(\\D*?|$))";

    /**
     * <pre>
     *     (?<=/HW/HW0/)\d{1,9}(?=\D*?)
     * </pre>
     */
    private static final String  TRUNK_PATTERN  = "(?<=%s/)%s(?=\\D*?)";

    @Resource
    protected HttpServletRequest request;
    @Resource
    HttpServletResponse          response;

    @Autowired
    private IBuOverviewDao       buOverviewDao;
    @Resource
    IUserManagerDao              userManagerDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome(HttpServletRequest request) {
        // TODO 判断有无session，有直接到首页
        if (request.getSession().getAttribute("userToken") != null) {
            return "/index";
        }

        return "login";
    }

    public void authentication0(ProjectInfo projectInfo, String clientType) throws AuthenticationException {
        String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
        UserInfo userInfo = userManagerDao.getUserInfoByName(username);

        if ("0".equals(clientType)) {// 华为
            if (StringUtilsLocal.isBlank(projectInfo.getHwpdu())) {
                projectInfo.setHwpdu(userInfo.getHwpdu() == null ? "0" : userInfo.getHwpdu());
            }
            if (StringUtilsLocal.isBlank(projectInfo.getHwzpdu())) {
                projectInfo.setHwzpdu(userInfo.getHwzpdu() == null ? "" : userInfo.getHwzpdu());
            }
            if (StringUtilsLocal.isBlank(projectInfo.getPduSpdt())) {
                projectInfo.setPduSpdt(userInfo.getPduspdt() == null ? "" : userInfo.getPduspdt());
            }
        } else if ("1".equals(clientType)) {// 中软
            if (StringUtilsLocal.isBlank(projectInfo.getBu())) {
                projectInfo.setBu(userInfo.getBu() == null ? "0" : userInfo.getBu());
            }
            if (StringUtilsLocal.isBlank(projectInfo.getPdu())) {
                projectInfo.setPdu(userInfo.getDu() == null ? "" : userInfo.getDu());
            }
            if (StringUtilsLocal.isBlank(projectInfo.getDu())) {
                projectInfo.setDu(userInfo.getDept() == null ? "" : userInfo.getDept());
            }
        }
    }

    public void authentication(ProjectInfo projectInfo, String clientType) throws AuthenticationException {
        String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
        String authorities = buOverviewDao.getAuthOpDepartment(username);

        Set<String> authorizedAreas = getAuthorizedAreas(projectInfo.getArea(), username, authorities);
        projectInfo.setArea(StringUtils.join(authorizedAreas, ","));

        // series 传入值为 1:按中软组织结构查询，其他都按华为组织机构处理
        boolean isDefault = !"1".equals(clientType);
        String selected;
        String deptPattern = String.format(DEPT_PATTERN, isDefault ? "/HW/HW0" : "/CS/2556", NUMBER_PATTERN,
                                           NUMBER_PATTERN, NUMBER_PATTERN);

        // 部门id是通过层级关系选择的，因此在 trunk，branch，dept 都有值的情况下，只取层级最低的部门作为筛选条件
        Set<String> selectedDepts = CollectionUtilsLocal.splitToSet(
                isDefault ? projectInfo.getPduSpdt() : projectInfo.getDu());
        if (!selectedDepts.isEmpty()) {
            selected = StringUtils.join(selectedDepts.toArray(), "|");
            deptPattern = String.format(DEPT_PATTERN, isDefault ? "/HW/HW0" : "/CS/2556", NUMBER_PATTERN,
                                        NUMBER_PATTERN, selected);
        } else {
            Set<String> selectedBranches = CollectionUtilsLocal.splitToSet(
                    isDefault ? projectInfo.getHwzpdu() : projectInfo.getPdu());
            if (!selectedBranches.isEmpty()) {
                selected = StringUtils.join(selectedBranches.toArray(), "|");
                deptPattern = String.format(DEPT_PATTERN, isDefault ? "/HW/HW0" : "/CS/2556", NUMBER_PATTERN, selected,
                                            NUMBER_PATTERN);
            } else {
                Set<String> selectedTrunks = CollectionUtilsLocal.splitToSet(
                        isDefault ? projectInfo.getHwpdu() : projectInfo.getBu());
                if (!selectedTrunks.isEmpty()) {
                    selected = StringUtils.join(selectedTrunks.toArray(), "|");
                    deptPattern = String.format(DEPT_PATTERN, isDefault ? "/HW/HW0" : "/CS/2556", selected,
                                                NUMBER_PATTERN, NUMBER_PATTERN);
                }
            }
        }

        Set<String> authorizedDepts = getAuthorizedDepts(deptPattern, username, authorities);
        if (isDefault) {
            projectInfo.setPduSpdt(StringUtils.join(authorizedDepts, ","));
        } else {
            projectInfo.setDu(StringUtils.join(authorizedDepts, ","));
        }
    }

    private Set<String> getAuthorizedAreas(String selectAreas, String username, String authorities)
                                                                                                   throws AuthenticationException {
        String selected = StringUtils.join(CollectionUtilsLocal.splitToSet(selectAreas).toArray(), "|");

        /**
         * <pre>
         *     /CS/2556/30028/30029/30047,/HW/HW0/32/2831/2804401,
         *     /AR/AR0/2604,/AR/AR0/2605
         * </pre>
         */
        String areaPattern = String.format(AREA_PATTERN, StringUtils.isBlank(selected) ? NUMBER_PATTERN : selected);
        Set<String> authorized = CollectionUtilsLocal.getMatched(authorities, Pattern.compile(areaPattern));

        if (authorized.isEmpty()) {
            logger.error("authentication failed, no authorized area found for user: " + username);
            throw new AuthenticationException("no authorized area found for user");
        }

        return authorized;
    }

    private Set<String> getAuthorizedDepts(String deptPattern, String username, String authorities)
                                                                                                   throws AuthenticationException {

        /**
         * <pre>
         *     /CS/2556/30028/30029/30047,/HW/HW0/32/2831/2804401,
         *     /AR/AR0/2604,/AR/AR0/2605
         * </pre>
         */
        Set<String> authorized = CollectionUtilsLocal.getMatched(authorities, Pattern.compile(deptPattern));

        if (authorized.isEmpty()) {
            logger.error("authentication failed, no authorized dept found for user: " + username);
            throw new AuthenticationException("no authorized dept found for user");
        }

        return authorized;
    }

    // public ListResponse getOrganisations(String client, String branchName, String branchId) {
    // String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
    // String authorities = buOverviewDao.getAuthOpDepartment(username);
    //
    // boolean isDefault = !"1".equals(client);
    // String selected;
    // String deptPattern = String.format(DEPT_PATTERN, isDefault ? "/HW/HW0" : "/CS/2556", NUMBER_PATTERN,
    // NUMBER_PATTERN, NUMBER_PATTERN);
    //
    // List<Map<String, Object>> records = buOverviewDao.getHWCPX();
    // if(records!=null && !records.isEmpty()){
    //
    // }
    //
    // }

    public Set<String> getAuthorizedTrunk(String... client) throws AuthenticationException {
        String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
        String authorities = buOverviewDao.getAuthOpDepartment(username);

        // series 传入值为 1 是按中软组织结构查询，其他都按华为组织机构处理
        boolean isDefault = client.length == 0 || !"1".equals(client[0]);
        String deptPattern = String.format(TRUNK_PATTERN, isDefault ? "/HW/HW0" : "/CS/2556", NUMBER_PATTERN);

        return getAuthorizedDepts(deptPattern, username, authorities);
    }

    /**
     * 本地cookie添加新键值
     *
     * @param key
     * @param value
     */
    public void addCookie(String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            try {
                Cookie cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
                cookie.setPath("/");

                /**
                 * <pre>
                 *     Cookie的maxAge决定着Cookie的有效期，单位为秒（Second）
                 *     cookie.setMaxAge(7 * 24 * 60 * 60);
                 * </pre>
                 */
                cookie.setMaxAge(7 * 24 * 60 * 60);

                response.addCookie(cookie);
            } catch (UnsupportedEncodingException e) {
                logger.warn("addCookie exception, EncodingException: " + e.getMessage());
            }
        }
    }

    public UserEntity getCurrentUser() throws UnsupportedOperationException {
        UserEntity result = null;
        try {
            result = (UserEntity) request.getSession().getAttribute(Constants.USER_KEY);
        } catch (Exception e) {
            logger.error("addCookie exception, error: " + e.getMessage());
        }

        if (result == null || StringUtils.isBlank(result.getId())) {
            throw new UnsupportedOperationException("user not logged in");
        }

        return result;
    }

    public List<String> getUserMenus(String userAuth) {
        // 获取全部的权限范围
        List<String> result = new ArrayList<>();

        if (StringUtils.isNotBlank(userAuth)) {
            /**
             * /CS/1/2/6,/CS/1/2/7,/CS/1/2/8,/CS/1/3/9/10,
             */
            Set<String> ids = new HashSet<>();
            for (String auth : userAuth.split(",")) {
                if (auth.contains("/")) {
                    Collections.addAll(ids, auth.split("/"));
                }
            }
            result.addAll(ids);
        }

        return result;
    }

    public String urlDecode(String encoded) {
        String result = null;

        try {
            result = StringUtils.isNotBlank(encoded) ? URLDecoder.decode(encoded, "UTF-8") : "";
        } catch (UnsupportedEncodingException e) {
            logger.warn("urlDecode exception, EncodingException: " + e.getMessage());
        }

        return result;
    }

    public String urlDecodeISO(String encoded) {
        String result = null;

        try {
            if (StringUtils.isNotBlank(encoded)) {
                result = new String(encoded.getBytes("ISO-8859-1"), "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            logger.warn("urlDecodeISO exception, EncodingException: " + e.getMessage());
        }

        return result;

    }
}
