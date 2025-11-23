<template>
  <div class="create-ticket">
    <el-card style="max-width: 800px; margin: 0 auto;">
      <template #header>
        <span>创建新工单</span>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入工单标题"></el-input>
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="6"
            placeholder="请详细描述您遇到的问题"
          ></el-input>
        </el-form-item>

        <el-form-item label="优先级" prop="priority">
          <el-radio-group v-model="form.priority">
            <el-radio :label="1">低</el-radio>
            <el-radio :label="2">中</el-radio>
            <el-radio :label="3">高</el-radio>
            <el-radio :label="4">紧急</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="用户ID" prop="creatorId">
          <el-input-number v-model="form.creatorId" :min="1" placeholder="请输入用户ID"></el-input-number>
          <el-text type="info" size="small" style="margin-left: 10px;">
            （测试用，实际应从登录状态获取）
          </el-text>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="submitting">
            提交工单
          </el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button @click="$router.push('/')">返回</el-button>
        </el-form-item>
      </el-form>

      <el-alert
        title="提示"
        type="info"
        :closable="false"
        style="margin-top: 20px;"
      >
        <p>系统将自动使用 AI 分析您的问题并分配合适的客服。</p>
        <p>如果包含关键词（如"登录"、"bug"、"退款"等），分类准确率更高。</p>
      </el-alert>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ticketApi } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  title: '',
  description: '',
  priority: 2,
  creatorId: 1
})

const rules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { min: 5, max: 200, message: '标题长度在 5 到 200 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入描述', trigger: 'blur' },
    { min: 10, message: '描述至少 10 个字符', trigger: 'blur' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ],
  creatorId: [
    { required: true, message: '请输入用户ID', trigger: 'blur' }
  ]
}

const submitForm = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const res = await ticketApi.createTicket(form)
        ElMessage.success('工单创建成功！系统已自动分类并分配客服')
        router.push(`/ticket/${res.data.ticketId}`)
      } catch (error) {
        ElMessage.error('创建工单失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

const resetForm = () => {
  formRef.value?.resetFields()
}
</script>

<style scoped>
.create-ticket {
  padding: 20px;
}
</style>
