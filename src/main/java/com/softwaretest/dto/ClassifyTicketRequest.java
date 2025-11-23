package com.softwaretest.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 分类工单请求DTO（管理员手动分类）
 */
@Data
public class ClassifyTicketRequest {
    @NotNull(message = "工单ID不能为空")
    private Integer ticketId;

    @NotNull(message = "分类不能为空")
    private String category;

    @NotNull(message = "管理员ID不能为空")
    private Integer adminId;
}
