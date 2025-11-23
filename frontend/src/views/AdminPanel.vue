<template>
  <div class="admin-panel">
    <el-row :gutter="20">
      <!-- 未分类工单 -->
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>未分类工单（需要人工处理）</span>
              <el-button @click="loadUnclassified">刷新</el-button>
            </div>
          </template>

          <el-table :data="unclassifiedTickets" v-loading="loading" stripe>
            <el-table-column prop="ticketId" label="ID" width="80"></el-table-column>
            <el-table-column prop="title" label="标题" min-width="200"></el-table-column>
            <el-table-column prop="description" label="描述" min-width="250" show-overflow-tooltip></el-table-column>
            <el-table-column prop="priority" label="优先级" width="100">
              <template #default="{ row }">
                <el-tag :type="getPriorityType(row.priority)">{{ getPriorityText(row.priority) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="创建时间" width="180"></el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button size="small" type="primary" @click="openClassifyDialog(row)">
                  分类
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 按分类统计 -->
      <el-col :span="24" style="margin-top: 20px;">
        <el-card>
          <template #header>
            <span>工单分类统计</span>
          </template>
          <el-row :gutter="20">
            <el-col :span="6" v-for="category in categories" :key="category.name">
              <el-statistic :title="category.name" :value="category.count">
                <template #suffix>
                  <el-button
                    size="small"
                    text
                    @click="viewByCategory(category.name)"
                  >
                    查看
                  </el-button>
                </template>
              </el-statistic>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分类对话框 -->
    <el-dialog v-model="dialogVisible" title="手动分类工单" width="500px">
      <el-form :model="classifyForm" label-width="100px">
        <el-form-item label="工单标题">
          <el-text>{{ selectedTicket?.title }}</el-text>
        </el-form-item>
        <el-form-item label="选择分类">
          <el-select v-model="classifyForm.category" placeholder="请选择分类">
            <el-option label="技术支持" value="技术支持"></el-option>
            <el-option label="账户问题" value="账户问题"></el-option>
            <el-option label="产品咨询" value="产品咨询"></el-option>
            <el-option label="账单问题" value="账单问题"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="管理员ID">
          <el-input-number v-model="classifyForm.adminId" :min="1"></el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitClassify">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { adminApi } from '@/api'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const unclassifiedTickets = ref([])
const categories = ref([
  { name: '技术支持', count: 0 },
  { name: '账户问题', count: 0 },
  { name: '产品咨询', count: 0 },
  { name: '账单问题', count: 0 }
])

const dialogVisible = ref(false)
const selectedTicket = ref(null)
const classifyForm = reactive({
  category: '',
  adminId: 1
})

const loadUnclassified = async () => {
  loading.value = true
  try {
    const res = await adminApi.getUnclassifiedTickets()
    unclassifiedTickets.value = res.data
  } catch (error) {
    ElMessage.error('加载未分类工单失败')
  } finally {
    loading.value = false
  }
}

const loadCategoryStats = async () => {
  for (const category of categories.value) {
    try {
      const res = await adminApi.getTicketsByCategory(category.name)
      category.count = res.data.length
    } catch (error) {
      console.error(`加载${category.name}统计失败`)
    }
  }
}

const openClassifyDialog = (ticket) => {
  selectedTicket.value = ticket
  classifyForm.category = ''
  dialogVisible.value = true
}

const submitClassify = async () => {
  if (!classifyForm.category) {
    ElMessage.warning('请选择分类')
    return
  }

  try {
    await adminApi.classifyTicket(selectedTicket.value.ticketId, {
      ticketId: selectedTicket.value.ticketId,
      category: classifyForm.category,
      adminId: classifyForm.adminId
    })
    ElMessage.success('分类成功，系统已自动分配客服')
    dialogVisible.value = false
    loadUnclassified()
    loadCategoryStats()
  } catch (error) {
    ElMessage.error('分类失败')
  }
}

const viewByCategory = async (category) => {
  try {
    const res = await adminApi.getTicketsByCategory(category)
    ElMessage.info(`${category}共有 ${res.data.length} 个工单`)
  } catch (error) {
    ElMessage.error('查询失败')
  }
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
  loadUnclassified()
  loadCategoryStats()
})
</script>

<style scoped>
.admin-panel {
  max-width: 1400px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
