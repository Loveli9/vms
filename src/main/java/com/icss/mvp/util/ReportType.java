package com.icss.mvp.util;

/**
 * 报表类型
 */
public enum ReportType {
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    YEAR("year");
    private String type;

    ReportType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ReportType fromType(String type) {
        for (ReportType reportType : ReportType.values()) {
            if (reportType.getType().equals(type)) {
                return reportType;
            }
        }
        return null;
    }
}
