package com.icss.mvp.entity;

import java.io.Serializable;

/**
 * 【地域表】的对应的Java映射类
 * @author gaoyao
 * @time 2018-4-26 9:50:09
*/
public class TblArea implements Serializable{
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //地域编码
    private String areaCode;
    //地域名称
    private String areaName;
    //记录标识
    private Integer ignoe;
   
    /** 获取 主键的属性 */
    public Long getId() {
        return id;
    }
    /** 设置主键的属性 */
    public void setId(Long id) {
        this.id = id;
    }
    /** 获取 地域编码的属性 */
    public String getAreaCode() {
        return areaCode;
    }
    /** 设置地域编码的属性 */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    /** 获取 地域名称的属性 */
    public String getAreaName() {
        return areaName;
    }
    /** 设置地域名称的属性 */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    /** 获取 记录标识的属性 */
    public Integer getIgnoe() {
        return ignoe;
    }
    /** 设置记录标识的属性 */
    public void setIgnoe(Integer ignoe) {
        this.ignoe = ignoe;
    }
}
