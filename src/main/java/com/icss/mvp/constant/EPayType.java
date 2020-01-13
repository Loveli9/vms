package com.icss.mvp.constant;

/**
 * 运营商务模式 Created by Ray on 2018/8/9.
 */
public enum EPayType {
    /**
     * FP
     */
    DEVELOPMENT(0, "FP"),
    /**
     * TM
     */
    TESTING(1, "TM"),
    /**
     * capped TM
     */
    MAINTENANCE(2, "capped TM"),

    ;

    public final int    code;
    public final String title;

    EPayType(int code, String title){
        this.code = code;
        this.title = title;
    }

    public static EPayType getByTitle(String title) {
        for (EPayType type : EPayType.values()) {
            if (type.title.equals(title)) {
                return type;
            }
        }
        return null;
    }
}
