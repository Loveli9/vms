package com.icss.mvp.constant;

/**
 * 项目类型 Created by Ray on 2018/8/9.
 */
public enum EProjectType {

    /**
     * 开发类项目
     */
    DEVELOPMENT(0, "开发"),
    /**
     * 测试类项目
     */
    TESTING(1, "测试"),
    /**
     * 维护类项目
     */
    MAINTENANCE(2, "维护"),
    /**
     * 解决方案
     */
    SOLUTION(3, "解决方案"),

    ;

    public final int    code;
    public final String title;

    EProjectType(int code, String title){
        this.code = code;
        this.title = title;
    }

    public static EProjectType getByTitle(String title) {
        for (EProjectType type : EProjectType.values()) {
            if (type.title.equals(title)) {
                return type;
            }
        }
        return null;
    }

}
