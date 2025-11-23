package com.softwaretest.controller;

import com.softwaretest.dto.Response;
import com.softwaretest.mapper.TicketMapper;
import com.softwaretest.pojo.Ticket;
import com.softwaretest.service.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客服控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/cs")
@CrossOrigin
public class CustomerServiceController {

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private WorkflowService workflowService;

    /**
     * 获取分配给客服的工单
     */
    @GetMapping("/{csId}/tickets")
    public Response<List<Ticket>> getAssignedTickets(@PathVariable Integer csId) {
        try {
            List<Ticket> tickets = ticketMapper.findByAssignedCsId(csId);
            return Response.success(tickets);
        } catch (Exception e) {
            log.error("获取客服工单失败", e);
            return Response.error("获取客服工单失败: " + e.getMessage());
        }
    }

    /**
     * 客服处理工单（添加备注、更新状态等）
     */
    @PutMapping("/tickets/{id}/handle")
    public Response<String> handleTicket(@PathVariable Integer id,
                                        @RequestParam Integer csId,
                                        @RequestParam(required = false) String note) {
        try {
            Ticket ticket = ticketMapper.findById(id);
            if (ticket == null) {
                return Response.error(404, "工单不存在");
            }

            // 这里可以添加更新备注的逻辑
            ticket.setStatus("PENDING");
            ticketMapper.update(ticket);

            log.info("客服处理工单: ticketId={}, csId={}", id, csId);
            return Response.success("工单处理成功");
        } catch (Exception e) {
            log.error("处理工单失败", e);
            return Response.error("处理工单失败: " + e.getMessage());
        }
    }

    /**
     * 客服关闭工单
     */
    @PutMapping("/tickets/{id}/close")
    public Response<String> closeTicket(@PathVariable Integer id, @RequestParam Integer csId) {
        try {
            workflowService.closeTicket(id, csId, "CS");
            return Response.success("工单已关闭");
        } catch (Exception e) {
            log.error("关闭工单失败", e);
            return Response.error("关闭工单失败: " + e.getMessage());
        }
    }

    /**
     * 客服升级工单
     */
    @PutMapping("/tickets/{id}/escalate")
    public Response<String> escalateTicket(@PathVariable Integer id, @RequestParam Integer csId) {
        try {
            workflowService.escalateTicket(id, csId);
            return Response.success("工单已升级");
        } catch (Exception e) {
            log.error("升级工单失败", e);
            return Response.error("升级工单失败: " + e.getMessage());
        }
    }
}
