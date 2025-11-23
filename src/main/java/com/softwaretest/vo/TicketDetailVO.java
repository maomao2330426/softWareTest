package com.softwaretest.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单详情视图对象
 */
@Data
public class TicketDetailVO {
    private Integer ticketId;
    private String title;
    private String description;
    private String status;
    private Integer priority;
    private String category;
    private Integer creatorId;
    private String creatorName;
    private Integer assignedCsId;
    private String assignedCsName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<EventVO> events; // 工单事件历史
}
