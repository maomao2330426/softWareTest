package com.softwaretest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 工单实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Integer ticketId;
    private String title;
    private String description;
    private String status; // OPEN, PENDING, CLOSED, UNCLASSIFIED
    private Integer priority; // 1-低, 2-中, 3-高, 4-紧急
    private String category; // 技术支持, 账户问题, 产品咨询等
    private Integer creatorId; // 创建者用户ID
    private Integer assignedCsId; // 分配的客服ID
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 更新工单状态
     */
    public void updateStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
