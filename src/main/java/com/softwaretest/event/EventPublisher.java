package com.softwaretest.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 事件发布器
 * 封装Spring事件发布机制，简化事件发布操作
 */
@Slf4j
@Component
public class EventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 发布工单事件
     */
    public void publishTicketEvent(Integer ticketId, String eventType,
                                   Map<String, Object> payload,
                                   Integer operatorId, String operatorType) {
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);
            TicketEvent event = new TicketEvent(
                this,
                ticketId,
                eventType,
                payloadJson,
                operatorId,
                operatorType
            );

            applicationEventPublisher.publishEvent(event);
            log.info("事件已发布: ticketId={}, eventType={}", ticketId, eventType);

        } catch (Exception e) {
            log.error("发布事件失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 发布工单创建事件
     */
    public void publishTicketCreatedEvent(Integer ticketId, Integer creatorId, String title) {
        Map<String, Object> payload = Map.of(
            "title", title,
            "creatorId", creatorId
        );
        publishTicketEvent(ticketId, "CREATED", payload, creatorId, "USER");
    }

    /**
     * 发布工单分类事件
     */
    public void publishTicketClassifiedEvent(Integer ticketId, String category,
                                            Integer operatorId, String operatorType) {
        Map<String, Object> payload = Map.of(
            "category", category,
            "classifiedBy", operatorType
        );
        publishTicketEvent(ticketId, "CLASSIFIED", payload, operatorId, operatorType);
    }

    /**
     * 发布工单分配事件
     */
    public void publishTicketAssignedEvent(Integer ticketId, Integer csId) {
        Map<String, Object> payload = Map.of(
            "assignedCsId", csId
        );
        publishTicketEvent(ticketId, "ASSIGNED", payload, csId, "SYSTEM");
    }

    /**
     * 发布工单关闭事件
     */
    public void publishTicketClosedEvent(Integer ticketId, Integer operatorId, String operatorType) {
        Map<String, Object> payload = Map.of(
            "closedBy", operatorType
        );
        publishTicketEvent(ticketId, "CLOSED", payload, operatorId, operatorType);
    }
}
