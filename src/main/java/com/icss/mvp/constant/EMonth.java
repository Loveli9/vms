package com.icss.mvp.constant;

/**
 * @author 133918
 */
@Deprecated
public enum EMonth {
    /**
     *
     */
    JAN("01"),
    /**
     *
     */
    FEB("02"),
    /**
     *
     */
    MAR("03"),
    /**
     *
     */
    APR("04"),
    /**
     *
     */
    MAY("05"),
    /**
     *
     */
    JUN("06"),
    /**
     *
     */
    JUL("07"),
    /**
     *
     */
    AUG("08"),
    /**
     *
     */
    SEP("09"),
    /**
     *
     */
    OCT("10"),
    /**
     *
     */
    NOV("11"),
    /**
     *
     */
    DEC("12");

    private String month;

    EMonth(String month){
        this.month = month;
    }

    public static EMonth fromMonth(String month) {
        for (EMonth mon : EMonth.values()) {
            if (mon.getMonth().equals(month)) {
                return mon;
            }
        }
        return null;
    }

    public String getMonth() {
        return month;
    }
}
