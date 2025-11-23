import request from './axios'

// 工单 API
export const ticketApi = {
  // 创建工单
  createTicket(data) {
    return request.post('/tickets', data)
  },
  // 获取所有工单
  getAllTickets() {
    return request.get('/tickets')
  },
  // 获取工单详情
  getTicketDetail(id) {
    return request.get(`/tickets/${id}`)
  },
  // 根据状态获取工单
  getTicketsByStatus(status) {
    return request.get(`/tickets/status/${status}`)
  },
  // 获取用户工单
  getUserTickets(userId) {
    return request.get(`/tickets/user/${userId}`)
  },
  // 关闭工单
  closeTicket(id, operatorId, operatorType) {
    return request.put(`/tickets/${id}/close`, null, {
      params: { operatorId, operatorType }
    })
  },
  // 升级工单
  escalateTicket(id, csId) {
    return request.put(`/tickets/${id}/escalate`, null, {
      params: { csId }
    })
  }
}

// 管理员 API
export const adminApi = {
  // 手动分类工单
  classifyTicket(id, data) {
    return request.put(`/admin/tickets/${id}/classify`, data)
  },
  // 获取未分类工单
  getUnclassifiedTickets() {
    return request.get('/admin/tickets/unclassified')
  },
  // 获取所有工单
  getAllTickets() {
    return request.get('/admin/tickets')
  },
  // 根据分类获取工单
  getTicketsByCategory(category) {
    return request.get(`/admin/tickets/category/${category}`)
  }
}

// 客服 API
export const csApi = {
  // 获取分配的工单
  getAssignedTickets(csId) {
    return request.get(`/cs/${csId}/tickets`)
  },
  // 处理工单
  handleTicket(id, csId, note) {
    return request.put(`/cs/tickets/${id}/handle`, null, {
      params: { csId, note }
    })
  },
  // 关闭工单
  closeTicket(id, csId) {
    return request.put(`/cs/tickets/${id}/close`, null, {
      params: { csId }
    })
  },
  // 升级工单
  escalateTicket(id, csId) {
    return request.put(`/cs/tickets/${id}/escalate`, null, {
      params: { csId }
    })
  }
}

// 事件 API
export const eventApi = {
  // 获取工单事件历史
  getTicketEvents(ticketId) {
    return request.get(`/events/ticket/${ticketId}`)
  },
  // 获取最近事件
  getRecentEvents(limit = 50) {
    return request.get('/events/recent', { params: { limit } })
  },
  // 根据类型获取事件
  getEventsByType(eventType) {
    return request.get(`/events/type/${eventType}`)
  }
}
