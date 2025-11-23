package com.softwaretest.enums;

/**
 * 操作者类型枚举
 */
public enum OperatorType {
    USER("用户"),
    CS("客服"),
    ADMIN("管理员"),
    SYSTEM("系统");

    private final String description;

    OperatorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
