package com.softwaretest.enums;

/**
 * 事件类型枚举
 */
public enum EventType {
    CREATED("工单创建"),
    CLASSIFIED("工单分类"),
    ASSIGNED("工单分配"),
    REPLIED("回复消息"),
    ESCALATED("工单升级"),
    CLOSED("工单关闭");

    private final String description;

    EventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
