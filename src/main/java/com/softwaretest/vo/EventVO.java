package com.softwaretest.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 事件视图对象
 */
@Data
public class EventVO {
    private Integer eventId;
    private Integer ticketId;
    private String eventType;
    private String eventTypeDesc; // 事件类型描述
    private String payload;
    private Integer operatorId;
    private String operatorType;
    private String operatorName; // 操作者姓名
    private LocalDateTime timestamp;
}
