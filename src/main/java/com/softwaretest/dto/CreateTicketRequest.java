package com.softwaretest.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 创建工单请求DTO
 */
@Data
public class CreateTicketRequest {
    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "描述不能为空")
    private String description;

    @NotNull(message = "优先级不能为空")
    private Integer priority;

    @NotNull(message = "创建者ID不能为空")
    private Integer creatorId;
}
