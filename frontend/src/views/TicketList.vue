<template>
  <div class="ticket-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>工单列表</span>
          <el-button type="primary" @click="$router.push('/create')">
            <el-icon><Plus /></el-icon>
            创建工单
          </el-button>
        </div>
      </template>

      <!-- 筛选器 -->
      <el-form :inline="true" class="filter-form">
        <el-form-item label="状态">
          <el-select v-model="filterStatus" placeholder="全部" @change="loadTickets" clearable>
            <el-option label="待处理" value="OPEN"></el-option>
            <el-option label="处理中" value="PENDING"></el-option>
            <el-option label="未分类" value="UNCLASSIFIED"></el-option>
            <el-option label="已关闭" value="CLOSED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="loadTickets">刷新</el-button>
        </el-form-item>
      </el-form>

      <!-- 工单表格 -->
      <el-table :data="tickets" v-loading="loading" stripe>
        <el-table-column prop="ticketId" label="工单ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题" min-width="200"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="{ row }">
            <el-tag :type="getPriorityType(row.priority)">{{ getPriorityText(row.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120"></el-table-column>
        <el-table-column prop="creatorName" label="创建者" width="100"></el-table-column>
        <el-table-column prop="assignedCsName" label="客服" width="100"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="viewDetail(row.ticketId)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ticketApi } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const tickets = ref([])
const loading = ref(false)
const filterStatus = ref('')

const loadTickets = async () => {
  loading.value = true
  try {
    let res
    if (filterStatus.value) {
      res = await ticketApi.getTicketsByStatus(filterStatus.value)
    } else {
      res = await ticketApi.getAllTickets()
    }
    tickets.value = res.data
  } catch (error) {
    ElMessage.error('加载工单列表失败')
  } finally {
    loading.value = false
  }
}

const viewDetail = (id) => {
  router.push(`/ticket/${id}`)
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
  const map = {
    1: '低',
    2: '中',
    3: '高',
    4: '紧急'
  }
  return map[priority] || priority
}

onMounted(() => {
  loadTickets()
})
</script>

<style scoped>
.ticket-list {
  max-width: 1400px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-form {
  margin-bottom: 20px;
}
</style>
