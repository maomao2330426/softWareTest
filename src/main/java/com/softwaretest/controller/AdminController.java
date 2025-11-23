package com.softwaretest.controller;

import com.softwaretest.dto.ClassifyTicketRequest;
import com.softwaretest.dto.Response;
import com.softwaretest.mapper.TicketMapper;
import com.softwaretest.pojo.Ticket;
import com.softwaretest.service.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 管理员控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private TicketMapper ticketMapper;

    /**
     * 手动分类工单
     */
    @PutMapping("/tickets/{id}/classify")
    public Response<Ticket> classifyTicket(@PathVariable Integer id,
                                          @Valid @RequestBody ClassifyTicketRequest request) {
        try {
            log.info("管理员手动分类工单: ticketId={}, category={}",
                    id, request.getCategory());

            Ticket ticket = workflowService.classifyTicketManually(
                id,
                request.getCategory(),
                request.getAdminId()
            );

            return Response.success("工单分类成功", ticket);
        } catch (Exception e) {
            log.error("分类工单失败", e);
            return Response.error("分类工单失败: " + e.getMessage());
        }
    }

    /**
     * 获取未分类工单列表（需要管理员处理）
     */
    @GetMapping("/tickets/unclassified")
    public Response<List<Ticket>> getUnclassifiedTickets() {
        try {
            List<Ticket> tickets = ticketMapper.findByStatus("UNCLASSIFIED");
            return Response.success(tickets);
        } catch (Exception e) {
            log.error("获取未分类工单失败", e);
            return Response.error("获取未分类工单失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有工单（管理员视图）
     */
    @GetMapping("/tickets")
    public Response<List<Ticket>> getAllTickets() {
        try {
            List<Ticket> tickets = ticketMapper.findAll();
            return Response.success(tickets);
        } catch (Exception e) {
            log.error("获取工单列表失败", e);
            return Response.error("获取工单列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据分类获取工单
     */
    @GetMapping("/tickets/category/{category}")
    public Response<List<Ticket>> getTicketsByCategory(@PathVariable String category) {
        try {
            List<Ticket> tickets = ticketMapper.findByCategory(category);
            return Response.success(tickets);
        } catch (Exception e) {
            log.error("获取工单列表失败", e);
            return Response.error("获取工单列表失败: " + e.getMessage());
        }
    }
}
