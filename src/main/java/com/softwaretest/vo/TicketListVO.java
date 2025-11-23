package com.softwaretest.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工单列表视图对象
 */
@Data
public class TicketListVO {
    private Integer ticketId;
    private String title;
    private String status;
    private Integer priority;
    private String category;
    private String creatorName;
    private String assignedCsName;
    private LocalDateTime createdAt;
}
