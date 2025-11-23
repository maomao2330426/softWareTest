package com.softwaretest.constant;

/**
 * 工单相关常量
 */
public class TicketConstants {

    // 优先级
    public static final Integer PRIORITY_LOW = 1;
    public static final Integer PRIORITY_MEDIUM = 2;
    public static final Integer PRIORITY_HIGH = 3;
    public static final Integer PRIORITY_URGENT = 4;

    // AI 置信度阈值
    public static final Double AI_CONFIDENCE_THRESHOLD = 0.7;

    // 分类类型
    public static final String CATEGORY_TECHNICAL = "技术支持";
    public static final String CATEGORY_ACCOUNT = "账户问题";
    public static final String CATEGORY_PRODUCT = "产品咨询";
    public static final String CATEGORY_BILLING = "账单问题";
    public static final String CATEGORY_OTHER = "其他";
}
