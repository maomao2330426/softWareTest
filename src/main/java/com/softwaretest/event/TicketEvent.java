package com.softwaretest.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 工单事件基类
 */
@Getter
public class TicketEvent extends ApplicationEvent {
    private Integer ticketId;
    private String eventType;
    private String payload;
    private Integer operatorId;
    private String operatorType;

    public TicketEvent(Object source, Integer ticketId, String eventType,
                      String payload, Integer operatorId, String operatorType) {
        super(source);
        this.ticketId = ticketId;
        this.eventType = eventType;
        this.payload = payload;
        this.operatorId = operatorId;
        this.operatorType = operatorType;
    }
}
