package com.softwaretest.service;

import com.softwaretest.constant.TicketConstants;
import com.softwaretest.dto.AIClassificationResult;
import com.softwaretest.mapper.KBEntryMapper;
import com.softwaretest.pojo.KBEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI意图识别服务（Mock实现）
 * 基于关键词匹配的简单分类逻辑
 */
@Slf4j
@Service
public class AIService {

    @Autowired
    private KBEntryMapper kbEntryMapper;

    /**
     * 识别工单意图并分类
     * @param title 工单标题
     * @param description 工单描述
     * @return AI分类结果
     */
    public AIClassificationResult classifyTicket(String title, String description) {
        log.info("开始AI意图识别: title={}", title);

        String content = (title + " " + description).toLowerCase();

        // 1. 尝试从知识库匹配
        KBEntry matchedEntry = findBestMatch(content);

        if (matchedEntry != null) {
            log.info("匹配到知识库条目: entryId={}, category={}",
                    matchedEntry.getEntryId(), matchedEntry.getCategory());

            // 更新知识库使用次数
            kbEntryMapper.incrementUseCount(matchedEntry.getEntryId());

            // 高置信度匹配
            return new AIClassificationResult(
                matchedEntry.getCategory(),
                0.85 + Math.random() * 0.10, // 0.85-0.95
                "基于知识库匹配: " + matchedEntry.getQuestion(),
                matchedEntry.getEntryId()
            );
        }

        // 2. 基于关键词规则的分类
        AIClassificationResult result = classifyByKeywords(content);
        log.info("AI分类结果: category={}, confidence={}",
                result.getCategory(), result.getConfidence());

        return result;
    }

    /**
     * 从知识库中查找最佳匹配
     */
    private KBEntry findBestMatch(String content) {
        List<KBEntry> allEntries = kbEntryMapper.findAll();

        for (KBEntry entry : allEntries) {
            String[] keywords = entry.getKeywords().split(",");
            int matchCount = 0;

            for (String keyword : keywords) {
                if (content.contains(keyword.trim().toLowerCase())) {
                    matchCount++;
                }
            }

            // 如果匹配到至少一半的关键词，认为是匹配的
            if (matchCount >= keywords.length / 2.0) {
                return entry;
            }
        }

        return null;
    }

    /**
     * 基于关键词规则的分类
     */
    private AIClassificationResult classifyByKeywords(String content) {
        // 技术支持关键词
        if (containsAny(content, "bug", "错误", "崩溃", "报错", "异常", "不能用", "无法", "故障")) {
            return new AIClassificationResult(
                TicketConstants.CATEGORY_TECHNICAL,
                0.75 + Math.random() * 0.10,
                "检测到技术问题相关关键词",
                null
            );
        }

        // 账户问题关键词
        if (containsAny(content, "登录", "注册", "密码", "账号", "找回", "验证码", "权限")) {
            return new AIClassificationResult(
                TicketConstants.CATEGORY_ACCOUNT,
                0.75 + Math.random() * 0.10,
                "检测到账户相关关键词",
                null
            );
        }

        // 产品咨询关键词
        if (containsAny(content, "如何", "怎么", "功能", "使用", "教程", "咨询", "了解", "介绍")) {
            return new AIClassificationResult(
                TicketConstants.CATEGORY_PRODUCT,
                0.70 + Math.random() * 0.10,
                "检测到产品咨询相关关键词",
                null
            );
        }

        // 账单问题关键词
        if (containsAny(content, "付款", "退款", "发票", "账单", "价格", "收费", "扣费", "充值")) {
            return new AIClassificationResult(
                TicketConstants.CATEGORY_BILLING,
                0.75 + Math.random() * 0.10,
                "检测到账单相关关键词",
                null
            );
        }

        // 未能分类
        return new AIClassificationResult(
            TicketConstants.CATEGORY_OTHER,
            0.30 + Math.random() * 0.20, // 低置信度
            "未能识别明确的分类",
            null
        );
    }

    /**
     * 检查内容是否包含任一关键词
     */
    private boolean containsAny(String content, String... keywords) {
        for (String keyword : keywords) {
            if (content.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
