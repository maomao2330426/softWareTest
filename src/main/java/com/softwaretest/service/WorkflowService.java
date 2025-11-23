package com.softwaretest.service;

import com.softwaretest.constant.TicketConstants;
import com.softwaretest.dto.AIClassificationResult;
import com.softwaretest.dto.CreateTicketRequest;
import com.softwaretest.enums.TicketStatus;
import com.softwaretest.event.EventPublisher;
import com.softwaretest.mapper.CustomerServiceMapper;
import com.softwaretest.mapper.TicketMapper;
import com.softwaretest.pojo.CustomerService;
import com.softwaretest.pojo.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 工单工作流服务
 * 实现智能工单创建、分类、分配的完整流程
 */
@Slf4j
@Service
public class WorkflowService {

    @Autowired
    private com.softwaretest.service.AIService aiService;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private CustomerServiceMapper customerServiceMapper;

    @Autowired
    private EventPublisher eventPublisher;

    /**
     * 智能创建工单（完整流程）
     * 1. 创建工单
     * 2. AI意图识别
     * 3. 根据置信度自动分类或标记为未分类
     * 4. 自动分配客服
     */
    @Transactional
    public Ticket createTicketWithAI(CreateTicketRequest request) {
        log.info("开始创建工单: title={}", request.getTitle());

        // 1. 创建工单（初始状态为OPEN）
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(TicketStatus.OPEN.name());
        ticket.setPriority(request.getPriority());
        ticket.setCreatorId(request.getCreatorId());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        ticketMapper.insert(ticket);
        log.info("工单创建成功: ticketId={}", ticket.getTicketId());

        // 发布工单创建事件
        eventPublisher.publishTicketCreatedEvent(
            ticket.getTicketId(),
            request.getCreatorId(),
            request.getTitle()
        );

        // 2. AI意图识别
        AIClassificationResult aiResult = aiService.classifyTicket(
            request.getTitle(),
            request.getDescription()
        );

        // 3. 检查AI置信度
        if (aiResult.getConfidence() >= TicketConstants.AI_CONFIDENCE_THRESHOLD) {
            // 高置信度：自动分类
            log.info("AI置信度高，自动分类: category={}, confidence={}",
                    aiResult.getCategory(), aiResult.getConfidence());

            ticket.setCategory(aiResult.getCategory());
            ticket.setStatus(TicketStatus.PENDING.name());

            // 发布分类事件
            eventPublisher.publishTicketClassifiedEvent(
                ticket.getTicketId(),
                aiResult.getCategory(),
                null,
                "SYSTEM"
            );

            // 4. 自动分配客服
            CustomerService cs = customerServiceMapper.findAvailableByCategory(aiResult.getCategory());
            if (cs != null) {
                ticket.setAssignedCsId(cs.getCsId());
                log.info("自动分配客服: csId={}, csName={}", cs.getCsId(), cs.getName());

                // 发布分配事件
                eventPublisher.publishTicketAssignedEvent(
                    ticket.getTicketId(),
                    cs.getCsId()
                );
            } else {
                log.warn("未找到可用的客服: category={}", aiResult.getCategory());
            }

        } else {
            // 低置信度：标记为未分类，等待管理员处理
            log.info("AI置信度低，标记为未分类: confidence={}", aiResult.getConfidence());
            ticket.setStatus(TicketStatus.UNCLASSIFIED.name());
            ticket.setCategory(TicketConstants.CATEGORY_OTHER);

            // TODO: 可以在这里发送通知给管理员
        }

        // 更新工单
        ticketMapper.update(ticket);

        return ticket;
    }

    /**
     * 管理员手动分类工单
     */
    @Transactional
    public Ticket classifyTicketManually(Integer ticketId, String category, Integer adminId) {
        log.info("管理员手动分类: ticketId={}, category={}, adminId={}",
                ticketId, category, adminId);

        Ticket ticket = ticketMapper.findById(ticketId);
        if (ticket == null) {
            throw new RuntimeException("工单不存在: " + ticketId);
        }

        // 更新分类
        ticket.setCategory(category);
        ticket.setStatus(TicketStatus.PENDING.name());
        ticket.setUpdatedAt(LocalDateTime.now());

        // 发布分类事件
        eventPublisher.publishTicketClassifiedEvent(
            ticketId,
            category,
            adminId,
            "ADMIN"
        );

        // 自动分配客服
        CustomerService cs = customerServiceMapper.findAvailableByCategory(category);
        if (cs != null) {
            ticket.setAssignedCsId(cs.getCsId());
            log.info("自动分配客服: csId={}", cs.getCsId());

            eventPublisher.publishTicketAssignedEvent(ticketId, cs.getCsId());
        }

        ticketMapper.update(ticket);
        return ticket;
    }

    /**
     * 关闭工单
     */
    @Transactional
    public void closeTicket(Integer ticketId, Integer operatorId, String operatorType) {
        log.info("关闭工单: ticketId={}, operatorId={}, operatorType={}",
                ticketId, operatorId, operatorType);

        Ticket ticket = ticketMapper.findById(ticketId);
        if (ticket == null) {
            throw new RuntimeException("工单不存在: " + ticketId);
        }

        ticket.setStatus(TicketStatus.CLOSED.name());
        ticket.setUpdatedAt(LocalDateTime.now());

        ticketMapper.update(ticket);

        // 发布关闭事件
        eventPublisher.publishTicketClosedEvent(ticketId, operatorId, operatorType);
    }

    /**
     * 升级工单（提高优先级）
     */
    @Transactional
    public void escalateTicket(Integer ticketId, Integer csId) {
        log.info("升级工单: ticketId={}, csId={}", ticketId, csId);

        Ticket ticket = ticketMapper.findById(ticketId);
        if (ticket == null) {
            throw new RuntimeException("工单不存在: " + ticketId);
        }

        // 提高优先级
        if (ticket.getPriority() < TicketConstants.PRIORITY_URGENT) {
            ticket.setPriority(ticket.getPriority() + 1);
        }

        ticket.setUpdatedAt(LocalDateTime.now());
        ticketMapper.update(ticket);

        // 发布升级事件
        eventPublisher.publishTicketEvent(
            ticketId,
            "ESCALATED",
            java.util.Map.of("newPriority", ticket.getPriority()),
            csId,
            "CS"
        );
    }
}
