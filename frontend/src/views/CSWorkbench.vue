<template>
  <div class="cs-workbench">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客服工作台</span>
          <div>
            <el-input-number
              v-model="csId"
              :min="1"
              placeholder="客服ID"
              style="width: 150px; margin-right: 10px;"
            ></el-input-number>
            <el-button @click="loadMyTickets">加载我的工单</el-button>
          </div>
        </div>
      </template>

      <el-alert
        title="提示"
        type="info"
        :closable="false"
        style="margin-bottom: 20px;"
      >
        请输入客服ID（测试用：1-技术支持, 2-账户问题, 3-产品咨询, 4-账单问题）
      </el-alert>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="待处理" name="OPEN"></el-tab-pane>
        <el-tab-pane label="处理中" name="PENDING"></el-tab-pane>
        <el-tab-pane label="已完成" name="CLOSED"></el-tab-pane>
      </el-tabs>

      <el-table :data="myTickets" v-loading="loading" stripe>
        <el-table-column prop="ticketId" label="ID" width="80"></el-table-column>
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
        <el-table-column prop="creatorName" label="用户" width="100"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              size="small"
              @click="$router.push(`/ticket/${row.ticketId}`)"
            >
              详情
            </el-button>
            <el-button
              v-if="row.status !== 'CLOSED'"
              size="small"
              type="success"
              @click="handleTicket(row.ticketId)"
            >
              处理
            </el-button>
            <el-button
              v-if="row.status !== 'CLOSED'"
              size="small"
              type="danger"
              @click="closeTicket(row.ticketId)"
            >
              关闭
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { csApi } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'

const csId = ref(1)
const myTickets = ref([])
const loading = ref(false)
const activeTab = ref('OPEN')

const loadMyTickets = async () => {
  loading.value = true
  try {
    const res = await csApi.getAssignedTickets(csId.value)
    myTickets.value = res.data.filter(t => t.status === activeTab.value)
  } catch (error) {
    ElMessage.error('加载工单失败')
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  loadMyTickets()
}

const handleTicket = async (ticketId) => {
  try {
    await ElMessageBox.prompt('请输入处理备注', '处理工单', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '输入处理说明'
    }).then(async ({ value }) => {
      await csApi.handleTicket(ticketId, csId.value, value)
      ElMessage.success('处理成功')
      loadMyTickets()
    })
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('处理失败')
    }
  }
}

const closeTicket = async (ticketId) => {
  try {
    await ElMessageBox.confirm('确定要关闭这个工单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await csApi.closeTicket(ticketId, csId.value)
    ElMessage.success('工单已关闭')
    loadMyTickets()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('关闭失败')
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

onMounted(() => {
  loadMyTickets()
})
</script>

<style scoped>
.cs-workbench {
  max-width: 1400px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
