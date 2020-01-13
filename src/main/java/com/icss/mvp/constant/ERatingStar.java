package com.icss.mvp.constant;

/**
 * Created by Ray on 2018/9/20.
 */
public enum ERatingStar {

    /**
     * 未评级
     */
    ZERO("未评级"),
    /**
     * 一星
     */
    SINGLE("一星级"),
    /**
     * 二星
     */
    COUPLE("二星级"),
    /**
     * 三星
     */
    TRIPLE("三星级"),
    /**
     * 四星
     */
    QUADRUPLE("四星级"),
    /**
     * 五星
     */
    QUINTUPLE("五星级"),
    /**
     * 全部
     */
    ALL("全部"), ;

    // public final int code;
    public final String title;

    ERatingStar(String title){
        // this.code = code;
        this.title = title;
    }

    public static ERatingStar getByTitle(String title) {
        for (ERatingStar type : ERatingStar.values()) {
            if (type.title.equals(title)) {
                return type;
            }
        }
        return null;
    }
}
