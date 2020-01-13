package com.icss.mvp.entity;

import java.util.Objects;

public class SysResource {
    public static final String MENU = "1";
    public static final String REF = "2";
    public static final String BUTTON = "3";
    private String resourceId;
    private String name;
    private String type;

    private String url;

    private Integer level;

    private String menuId;

    private Integer orderNum;

    public SysResource() {
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
        if (MENU.equals(type)){
            this.level = 1;
        }else {
            this.level = 2;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getLevel() {
        return level;
    }


    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuId() {
        return menuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysResource that = (SysResource) o;
        return Objects.equals(resourceId, that.resourceId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(resourceId);
    }
}