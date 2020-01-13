package com.icss.mvp.entity;

import java.io.Serializable;
/**
 * 【系统权限表表】的对应的Java映射类
 * @author gaoyao
 * @time 2018-5-29 10:00:52
*/
public class SysAuth implements Serializable{
    private static final long serialVersionUID = 1L;
    //
    private Integer id;
    //名称
    private String name;
    //是否启用 true启用 false停用
    private Boolean open;
    //类别 1:url 2:菜单 3:button
    private String genre;
    //访问地址
    private String url;
    //父节点id
    private Integer parentId;
    //节点访问路径
    private String path;
    //节点等级
    private Integer level;
   
    /** 获取 的属性 */
    public Integer getId() {
        return id;
    }
    /** 设置的属性 */
    public void setId(Integer id) {
        this.id = id;
    }
    /** 获取 名称的属性 */
    public String getName() {
        return name;
    }
    /** 设置名称的属性 */
    public void setName(String name) {
        this.name = name;
    }
    /** 获取 是否启用 true启用 false停用的属性 */
    public Boolean getOpen() {
        return open;
    }
    /** 设置是否启用 true启用 false停用的属性 */
    public void setOpen(Boolean open) {
        this.open = open;
    }
    /** 获取 类别 1:url 2:菜单 3:button的属性 */
    public String getGenre() {
        return genre;
    }
    /** 设置类别 1:url 2:菜单 3:button的属性 */
    public void setGenre(String genre) {
        this.genre = genre;
    }
    /** 获取 访问地址的属性 */
    public String getUrl() {
        return url;
    }
    /** 设置访问地址的属性 */
    public void setUrl(String url) {
        this.url = url;
    }
    /** 获取 父节点id的属性 */
    public Integer getParentId() {
        return parentId;
    }
    /** 设置父节点id的属性 */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    /** 获取 节点访问路径的属性 */
    public String getPath() {
        return path;
    }
    /** 设置节点访问路径的属性 */
    public void setPath(String path) {
        this.path = path;
    }
    /** 获取 节点等级的属性 */
    public Integer getLevel() {
        return level;
    }
    /** 设置节点等级的属性 */
    public void setLevel(Integer level) {
        this.level = level;
    }
}
