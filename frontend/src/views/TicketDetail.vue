<template>
  <div class="ticket-detail">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>工单详情 #{{ ticketId }}</span>
          <el-button @click="$router.push('/')">返回列表</el-button>
        </div>
      </template>

      <div v-if="ticket">
        <!-- 基本信息 -->
        <el-descriptions title="基本信息" :column="2" border>
          <el-descriptions-item label="工单ID">{{ ticket.ticketId }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(ticket.status)">{{ getStatusText(ticket.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="标题" :span="2">{{ ticket.title }}</el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">
            <div style="white-space: pre-wrap;">{{ ticket.description }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="优先级">
            <el-tag :type="getPriorityType(ticket.priority)">{{ getPriorityText(ticket.priority) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="分类">{{ ticket.category || '未分类' }}</el-descriptions-item>
          <el-descriptions-item label="创建者">{{ ticket.creatorName }}</el-descriptions-item>
          <el-descriptions-item label="分配客服">{{ ticket.assignedCsName || '未分配' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ ticket.createdAt }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ ticket.updatedAt }}</el-descriptions-item>
        </el-descriptions>

        <!-- 操作按钮 -->
        <div style="margin-top: 20px;">
          <el-button
            v-if="ticket.status !== 'CLOSED'"
            type="danger"
            @click="closeTicket"
          >
            关闭工单
          </el-button>
          <el-button
            v-if="ticket.status !== 'CLOSED'"
            type="warning"
            @click="escalateTicket"
          >
            升级工单
          </el-button>
        </div>

        <!-- 事件历史（溯源） -->
        <el-divider></el-divider>
        <h3>事件历史（全流程溯源）</h3>
        <el-timeline style="margin-top: 20px;">
          <el-timeline-item
            v-for="event in ticket.events"
            :key="event.eventId"
            :timestamp="event.timestamp"
            placement="top"
          >
            <el-card>
              <h4>{{ getEventTypeText(event.eventType) }}</h4>
              <p>操作者: {{ event.operatorType }}</p>
              <p v-if="event.payload">详情: {{ formatPayload(event.payload) }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ticketApi } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const ticketId = ref(route.params.id)
const ticket = ref(null)
const loading = ref(false)

const loadTicketDetail = async () => {
  loading.value = true
  try {
    const res = await ticketApi.getTicketDetail(ticketId.value)
    ticket.value = res.data
  } catch (error) {
    ElMessage.error('加载工单详情失败')
  } finally {
    loading.value = false
  }
}

const closeTicket = async () => {
  try {
    await ElMessageBox.confirm('确定要关闭这个工单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await ticketApi.closeTicket(ticketId.value, 1, 'USER')
    ElMessage.success('工单已关闭')
    loadTicketDetail()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('关闭工单失败')
    }
  }
}

const escalateTicket = async () => {
  try {
    await ElMessageBox.confirm('升级工单将提高优先级，确定要升级吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await ticketApi.escalateTicket(ticketId.value, ticket.value.assignedCsId || 1)
    ElMessage.success('工单已升级')
    loadTicketDetail()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('升级工单失败')
    }
  }
}

const getStatusType = (status) => {
  const map = {
    'OPEN': 'info',
    'PENDING': 'warning',
    'CLOSED': 'success',
    'UNCLASSIFIED': 'danger'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    'OPEN': '待处理',
    'PENDING': '处理中',
    'CLOSED': '已关闭',
    'UNCLASSIFIED': '未分类'
  }
  return map[status] || status
}

const getPriorityType = (priority) => {
  if (priority === 4) return 'danger'
  if (priority === 3) return 'warning'
  if (priority === 2) return 'info'
  return 'success'
}

const getPriorityText = (priority) => {
  const map = { 1: '低', 2: '中', 3: '高', 4: '紧急' }
  return map[priority] || priority
}

const getEventTypeText = (type) => {
  const map = {
    'CREATED': '工单创建',
    'CLASSIFIED': '工单分类',
    'ASSIGNED': '分配客服',
    'REPLIED': '回复消息',
    'ESCALATED': '工单升级',
    'CLOSED': '工单关闭'
  }
  return map[type] || type
}

const formatPayload = (payload) => {
  try {
    const obj = JSON.parse(payload)
    return JSON.stringify(obj, null, 2)
  } catch {
    return payload
  }
}

onMounted(() => {
  loadTicketDetail()
})
</script>

<style scoped>
.ticket-detail {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
