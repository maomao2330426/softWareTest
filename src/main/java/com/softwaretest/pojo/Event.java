package com.softwaretest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 事件实体类
 * 用于工单溯源和审计日志
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Integer eventId;
    private Integer ticketId; // 关联的工单ID
    private String eventType; // CREATED, CLASSIFIED, ASSIGNED, REPLIED, ESCALATED, CLOSED
    private String payload; // JSON格式的事件详细信息
    private Integer operatorId; // 操作者ID
    private String operatorType; // USER, CS, ADMIN, SYSTEM
    private LocalDateTime timestamp;
}
