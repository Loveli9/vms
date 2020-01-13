package com.icss.mvp.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icss.mvp.entity.SysQuestion;
import com.icss.mvp.entity.SysResource;

import java.util.*;


public class SysResourceUtil {
    /**
     * 将资源按照菜单分类
     */
    public static JSONArray formatResource(List<SysResource> sysResources){
        sysResources.sort(new Comparator<SysResource>() {
            @Override
            public int compare(SysResource o1, SysResource o2) {
                return o1.getLevel() - o2.getLevel();
            }
        });
        Map<String, JSONArray> map = new HashMap<>();
        JSONArray resourceJsonArray = new JSONArray();
        for (SysResource sysResource : sysResources) {
            if (SysResource.MENU.equals(sysResource.getType())){
                JSONArray childs = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("resourceId", sysResource.getResourceId());
                jsonObject.put("name", sysResource.getName());
                jsonObject.put("type", sysResource.getType());
                jsonObject.put("url", sysResource.getUrl());
                jsonObject.put("level", sysResource.getLevel());
                jsonObject.put("childs", childs);
                map.put(sysResource.getResourceId(), childs);
                resourceJsonArray.add(jsonObject);
            }else {
                JSONArray jsonArray = map.get(sysResource.getMenuId());
                sysResource.setType(formatResourceType(sysResource.getType()));
                jsonArray.add(sysResource);
            }
        }
        return resourceJsonArray;
    }


//    public static JSONObject formatResourceById(List<SysResource> sysResources){
//        sysResources.sort(new Comparator<SysResource>() {
//            @Override
//            public int compare(SysResource o1, SysResource o2) {
//                return o1.getLevel() - o2.getLevel();
//            }
//        });
//        Map<String, JSONArray> map = new HashMap<>();
//        JSONObject jsonObject = new JSONObject();
//        for (SysResource sysResource : sysResources) {
//            if (SysResource.MENU.equals(sysResource.getType())){
//                JSONArray childrens = new JSONArray();
//                jsonObject.put("resourceId", sysResource.getResourceId());
//                jsonObject.put("name", sysResource.getName());
//                jsonObject.put("type", formatResourceType(sysResource.getType()));
//                jsonObject.put("url", sysResource.getUrl());
//                jsonObject.put("level", sysResource.getLevel());
//                jsonObject.put("childs", childrens);
//                map.put(sysResource.getResourceId(), childrens);
//            }else {
//                JSONArray jsonArray = map.get(sysResource.getMenuId());
//                sysResource.setType(formatResourceType(sysResource.getType()));
//                jsonArray.add(sysResource);
//            }
//        }
//        return jsonObject;
//    }

    private static String formatResourceType(String type){
        if (SysResource.MENU.equals(type)){
            return "菜单";
        }else if (SysResource.REF.equals(type)){
            return "链接";
        }else if (SysResource.BUTTON.equals(type)){
            return "按钮";
        }else {
            return "其它";
        }
    }
}
