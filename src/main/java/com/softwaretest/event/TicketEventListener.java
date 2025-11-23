package com.softwaretest.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwaretest.enums.EventType;
import com.softwaretest.enums.OperatorType;
import com.softwaretest.mapper.EventMapper;
import com.softwaretest.pojo.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 工单事件监听器
 * 负责监听工单事件并记录到数据库，实现溯源功能
 */
@Slf4j
@Component
public class TicketEventListener {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 异步处理工单事件
     */
    @Async
    @EventListener
    public void handleTicketEvent(TicketEvent ticketEvent) {
        try {
            log.info("收到工单事件: ticketId={}, eventType={}",
                    ticketEvent.getTicketId(), ticketEvent.getEventType());

            // 创建事件记录
            Event event = new Event();
            event.setTicketId(ticketEvent.getTicketId());
            event.setEventType(ticketEvent.getEventType());
            event.setPayload(ticketEvent.getPayload());
            event.setOperatorId(ticketEvent.getOperatorId());
            event.setOperatorType(ticketEvent.getOperatorType());
            event.setTimestamp(LocalDateTime.now());

            // 保存到数据库
            eventMapper.insert(event);
            log.info("事件已记录: eventId={}", event.getEventId());

        } catch (Exception e) {
            log.error("处理工单事件失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 创建事件payload的辅助方法
     */
    public String createPayload(Map<String, Object> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("序列化payload失败: {}", e.getMessage());
            return "{}";
        }
    }
}
