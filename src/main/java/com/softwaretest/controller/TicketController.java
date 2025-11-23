package com.softwaretest.controller;

import com.softwaretest.dto.CreateTicketRequest;
import com.softwaretest.dto.Response;
import com.softwaretest.mapper.EventMapper;
import com.softwaretest.mapper.TicketMapper;
import com.softwaretest.mapper.UserMapper;
import com.softwaretest.mapper.CustomerServiceMapper;
import com.softwaretest.pojo.Event;
import com.softwaretest.pojo.Ticket;
import com.softwaretest.pojo.User;
import com.softwaretest.pojo.CustomerService;
import com.softwaretest.service.WorkflowService;
import com.softwaretest.vo.EventVO;
import com.softwaretest.vo.TicketDetailVO;
import com.softwaretest.vo.TicketListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工单控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin
public class TicketController {

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CustomerServiceMapper customerServiceMapper;

    @Autowired
    private EventMapper eventMapper;

    /**
     * 创建工单（智能分类）
     */
    @PostMapping
    public Response<Ticket> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        try {
            log.info("收到创建工单请求: title={}", request.getTitle());
            Ticket ticket = workflowService.createTicketWithAI(request);
            return Response.success("工单创建成功", ticket);
        } catch (Exception e) {
            log.error("创建工单失败", e);
            return Response.error("创建工单失败: " + e.getMessage());
        }
    }

    /**
     * 获取工单详情（包含事件历史）
     */
    @GetMapping("/{id}")
    public Response<TicketDetailVO> getTicketDetail(@PathVariable Integer id) {
        try {
            Ticket ticket = ticketMapper.findById(id);
            if (ticket == null) {
                return Response.error(404, "工单不存在");
            }

            TicketDetailVO vo = new TicketDetailVO();
            BeanUtils.copyProperties(ticket, vo);

            // 填充创建者信息
            if (ticket.getCreatorId() != null) {
                User creator = userMapper.findById(ticket.getCreatorId());
                if (creator != null) {
                    vo.setCreatorName(creator.getUsername());
                }
            }

            // 填充客服信息
            if (ticket.getAssignedCsId() != null) {
                CustomerService cs = customerServiceMapper.findById(ticket.getAssignedCsId());
                if (cs != null) {
                    vo.setAssignedCsName(cs.getName());
                }
            }

            // 获取事件历史
            List<Event> events = eventMapper.findByTicketId(id);
            List<EventVO> eventVOs = events.stream().map(event -> {
                EventVO eventVO = new EventVO();
                BeanUtils.copyProperties(event, eventVO);
                return eventVO;
            }).collect(Collectors.toList());
            vo.setEvents(eventVOs);

            return Response.success(vo);
        } catch (Exception e) {
            log.error("获取工单详情失败", e);
            return Response.error("获取工单详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有工单列表
     */
    @GetMapping
    public Response<List<TicketListVO>> getAllTickets() {
        try {
            List<Ticket> tickets = ticketMapper.findAll();
            List<TicketListVO> vos = tickets.stream().map(ticket -> {
                TicketListVO vo = new TicketListVO();
                BeanUtils.copyProperties(ticket, vo);

                // 填充创建者和客服名称
                if (ticket.getCreatorId() != null) {
                    User creator = userMapper.findById(ticket.getCreatorId());
                    if (creator != null) {
                        vo.setCreatorName(creator.getUsername());
                    }
                }

                if (ticket.getAssignedCsId() != null) {
                    CustomerService cs = customerServiceMapper.findById(ticket.getAssignedCsId());
                    if (cs != null) {
                        vo.setAssignedCsName(cs.getName());
                    }
                }

                return vo;
            }).collect(Collectors.toList());

            return Response.success(vos);
        } catch (Exception e) {
            log.error("获取工单列表失败", e);
            return Response.error("获取工单列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据状态获取工单
     */
    @GetMapping("/status/{status}")
    public Response<List<TicketListVO>> getTicketsByStatus(@PathVariable String status) {
        try {
            List<Ticket> tickets = ticketMapper.findByStatus(status);
            List<TicketListVO> vos = tickets.stream().map(ticket -> {
                TicketListVO vo = new TicketListVO();
                BeanUtils.copyProperties(ticket, vo);
                return vo;
            }).collect(Collectors.toList());

            return Response.success(vos);
        } catch (Exception e) {
            log.error("获取工单列表失败", e);
            return Response.error("获取工单列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的工单
     */
    @GetMapping("/user/{userId}")
    public Response<List<TicketListVO>> getUserTickets(@PathVariable Integer userId) {
        try {
            List<Ticket> tickets = ticketMapper.findByCreatorId(userId);
            List<TicketListVO> vos = tickets.stream().map(ticket -> {
                TicketListVO vo = new TicketListVO();
                BeanUtils.copyProperties(ticket, vo);
                return vo;
            }).collect(Collectors.toList());

            return Response.success(vos);
        } catch (Exception e) {
            log.error("获取用户工单失败", e);
            return Response.error("获取用户工单失败: " + e.getMessage());
        }
    }

    /**
     * 关闭工单
     */
    @PutMapping("/{id}/close")
    public Response<String> closeTicket(@PathVariable Integer id,
                                       @RequestParam Integer operatorId,
                                       @RequestParam String operatorType) {
        try {
            workflowService.closeTicket(id, operatorId, operatorType);
            return Response.success("工单已关闭");
        } catch (Exception e) {
            log.error("关闭工单失败", e);
            return Response.error("关闭工单失败: " + e.getMessage());
        }
    }

    /**
     * 升级工单
     */
    @PutMapping("/{id}/escalate")
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
