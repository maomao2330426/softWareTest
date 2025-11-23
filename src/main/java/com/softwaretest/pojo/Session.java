package com.softwaretest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会话实体类
 * 用于记录工单相关的对话会话
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private Integer sessionId;
    private Integer ticketId; // 关联的工单ID
    private String messageList; // JSON格式的消息列表
    private Boolean archived; // 是否已归档
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
