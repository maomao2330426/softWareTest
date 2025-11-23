package com.softwaretest.controller;

import com.softwaretest.dto.Response;
import com.softwaretest.mapper.EventMapper;
import com.softwaretest.pojo.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件控制器（工单溯源）
 */
@Slf4j
@RestController
@RequestMapping("/api/events")
@CrossOrigin
public class EventController {

    @Autowired
    private EventMapper eventMapper;

    /**
     * 获取工单的事件历史（溯源）
     */
    @GetMapping("/ticket/{ticketId}")
    public Response<List<Event>> getTicketEvents(@PathVariable Integer ticketId) {
        try {
            List<Event> events = eventMapper.findByTicketId(ticketId);
            return Response.success(events);
        } catch (Exception e) {
            log.error("获取工单事件失败", e);
            return Response.error("获取工单事件失败: " + e.getMessage());
        }
    }

    /**
     * 获取最近的事件
     */
    @GetMapping("/recent")
    public Response<List<Event>> getRecentEvents(@RequestParam(defaultValue = "50") Integer limit) {
        try {
            List<Event> events = eventMapper.findRecent(limit);
            return Response.success(events);
        } catch (Exception e) {
            log.error("获取最近事件失败", e);
            return Response.error("获取最近事件失败: " + e.getMessage());
        }
    }

    /**
     * 根据事件类型获取事件
     */
    @GetMapping("/type/{eventType}")
    public Response<List<Event>> getEventsByType(@PathVariable String eventType) {
        try {
            List<Event> events = eventMapper.findByEventType(eventType);
            return Response.success(events);
        } catch (Exception e) {
            log.error("获取事件失败", e);
            return Response.error("获取事件失败: " + e.getMessage());
        }
    }
}
