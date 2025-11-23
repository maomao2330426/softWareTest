package com.softwaretest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 知识库条目实体类
 * 用于AI意图识别和自动分类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KBEntry {
    private Integer entryId;
    private String question; // 问题关键词
    private String answer; // 答案或解决方案
    private String category; // 分类
    private String keywords; // 关键词列表（逗号分隔）
    private Integer useCount; // 使用次数
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
