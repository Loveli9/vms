package com.icss.mvp.util;

import com.icss.mvp.tree.DeptTreeUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthUtil {
    /**
     * 华为部门
     */
    public static final String HW_DEPT = "HW_DEPT";
    /**
     * 中软部门
     */
    public static final String OPT_DEPT = "OPT_DEPT";
    /**
     * 地域
     */
    public static final String AREA = "AREA";

    /**
     * 用户权限列表
     * @param roleAuths
     * @return
     * @throws Exception
     */
    public static Map<String, Object> authMap(String roleAuths) throws IllegalArgumentException{
        Map<String, Object> returnMap = new HashMap<>();
        if (StringUtils.isBlank(roleAuths)){
            return returnMap;
        }
        for (String roleAuth : roleAuths.split(",")) {
            if (roleAuth.startsWith(DeptTreeUtil.CS_DEPT)){
                roleAuth = roleAuth.substring(DeptTreeUtil.CS_DEPT.length());
                String[] split = roleAuth.split("/");
                initAuthMap(split, returnMap, OPT_DEPT);
            }else if (roleAuth.startsWith(DeptTreeUtil.HW_DEPT)){
                roleAuth = roleAuth.substring(DeptTreeUtil.HW_DEPT.length() + 2);
                String[] split = roleAuth.split("/");
                initAuthMap(split, returnMap, HW_DEPT);
            }else{
                roleAuth = roleAuth.substring(DeptTreeUtil.AR_DEPT.length() + 2);
                String[] split = roleAuth.split("/");
                List<String> areas = (List<String>)returnMap.get(AREA);
                if (null == areas){
                    areas = new ArrayList<>();
                    returnMap.put(AREA, areas);
                }
                areas.add(split[split.length - 1]);
            }
        }

        return returnMap;
    }

    public static void initAuthMap(String[] auths, Map<String, Object> map, String prefix) throws IllegalArgumentException{
        int len = auths.length;
        if (len <= 2) throw new IllegalArgumentException("权限信息有误!");

        Map<String, Object> buMap = (Map<String, Object>)map.get(prefix);
        if (null == buMap){
            buMap = new HashMap<>();
            map.put(prefix, buMap);
        }

        Map<String, Object> pduMap = (Map<String, Object>)buMap.get(auths[1]);
        if (null == pduMap){
            pduMap = new HashMap<>();
            buMap.put(auths[1], pduMap);
        }
        List<String> duList = null;
        if (len >= 3){
            duList = (List<String>) pduMap.get(auths[2]);
            if (null == duList){
                duList = new ArrayList<>();
                pduMap.put(auths[2], duList);
            }
        }
        if (len >= 4){
            duList.add(auths[3]);
        }
    }
}
