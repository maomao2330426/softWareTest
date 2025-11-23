package com.softwaretest.enums;

/**
 * 工单状态枚举
 */
public enum TicketStatus {
    OPEN("待处理"),
    PENDING("处理中"),
    UNCLASSIFIED("未分类"),
    CLOSED("已关闭");

    private final String description;

    TicketStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
