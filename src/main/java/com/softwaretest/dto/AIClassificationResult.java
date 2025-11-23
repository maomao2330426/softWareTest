package com.softwaretest.dto;

import lombok.Data;

/**
 * AI分类结果DTO
 */
@Data
public class AIClassificationResult {
    private String category; // 分类结果
    private Double confidence; // 置信度 (0.0 - 1.0)
    private String reasoning; // AI推理过程
    private Integer matchedKbEntryId; // 匹配的知识库条目ID

    public AIClassificationResult(String category, Double confidence, String reasoning, Integer matchedKbEntryId) {
        this.category = category;
        this.confidence = confidence;
        this.reasoning = reasoning;
        this.matchedKbEntryId = matchedKbEntryId;
    }
}
