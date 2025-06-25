<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span class="card-title">健康预警详情</span>
          <div class="right-btns">
            <el-button @click="goBack">
              <el-icon><Back /></el-icon> 返回
            </el-button>
            <el-button 
              type="success" 
              @click="handleProcess" 
              v-if="warningInfo.processStatus === 0 && hasRole(['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE'])"
            >
              <el-icon><Check /></el-icon> 处理预警
            </el-button>
          </div>
        </div>
      </template>

      <el-descriptions 
        title="预警基本信息" 
        :column="2" 
        border 
        v-loading="loading"
      >
        <el-descriptions-item label="预警ID">
          {{ warningInfo.warningId }}
        </el-descriptions-item>
        <el-descriptions-item label="老人ID">
          {{ warningInfo.elderId }}
        </el-descriptions-item>
        <el-descriptions-item label="预警类型">
          <el-tag :type="getWarningTypeTag(warningInfo.warningType)">
            {{ getWarningTypeLabel(warningInfo.warningType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警级别">
          <el-tag :type="getWarningLevelTag(warningInfo.warningLevel)">
            {{ getWarningLevelLabel(warningInfo.warningLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警时间" :span="2">
          {{ formatDateTime(warningInfo.warningTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="预警内容" :span="2">
          {{ warningInfo.warningContent }}
        </el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag :type="warningInfo.processStatus === 0 ? 'danger' : 'success'">
            {{ warningInfo.processStatus === 0 ? '未处理' : '已处理' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理时间" v-if="warningInfo.processStatus === 1">
          {{ formatDateTime(warningInfo.processTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="处理人ID" v-if="warningInfo.processStatus === 1">
          {{ warningInfo.processUserId }}
        </el-descriptions-item>
        <el-descriptions-item label="处理结果" :span="2" v-if="warningInfo.processStatus === 1">
          {{ warningInfo.processResult }}
        </el-descriptions-item>
        <el-descriptions-item label="创建者">
          {{ warningInfo.createBy }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDateTime(warningInfo.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="更新者" v-if="warningInfo.updateBy">
          {{ warningInfo.updateBy }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间" v-if="warningInfo.updateTime">
          {{ formatDateTime(warningInfo.updateTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2" v-if="warningInfo.remark">
          {{ warningInfo.remark }}
        </el-descriptions-item>
      </el-descriptions>

      <!-- 相关健康记录 -->
      <div class="related-record" v-if="warningInfo.recordId">
        <h3>相关健康记录</h3>
        <el-descriptions :column="2" border>
          <!-- 这里可以根据实际情况展示相关的健康记录信息 -->
          <el-descriptions-item label="记录ID">
            {{ warningInfo.recordId }}
          </el-descriptions-item>
          <!-- 其他健康记录信息 -->
        </el-descriptions>
      </div>
    </el-card>

    <!-- 预警处理对话框 -->
    <el-dialog
      v-model="processDialogVisible"
      title="处理健康预警"
      width="500px"
      append-to-body
    >
      <el-form :model="processForm" ref="processFormRef" :rules="processRules" label-width="100px">
        <el-form-item label="预警ID" prop="warningId">
          <el-input v-model="processForm.warningId" disabled />
        </el-form-item>
        <el-form-item label="预警内容" prop="warningContent">
          <el-input v-model="processForm.warningContent" type="textarea" rows="3" disabled />
        </el-form-item>
        <el-form-item label="处理结果" prop="processResult">
          <el-input v-model="processForm.processResult" type="textarea" rows="3" placeholder="请输入处理结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="processDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitProcess">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getHealthWarning, processHealthWarning } from '@/api/health/warning'

// 路由实例
const route = useRoute()
const router = useRouter()

// 用户状态
const userStore = useUserStore()

// 判断是否有角色权限
const hasRole = (roles) => {
  return roles.some(role => userStore.roles.includes(role))
}

// 预警信息
const warningInfo = ref({})
const loading = ref(false)

// 预警类型选项
const warningTypeOptions = [
  { value: 1, label: '体温异常' },
  { value: 2, label: '血压异常' },
  { value: 3, label: '血糖异常' },
  { value: 4, label: '心率异常' },
  { value: 5, label: '血氧异常' },
  { value: 6, label: '体重异常' },
  { value: 7, label: '其他' }
]

// 预警级别选项
const warningLevelOptions = [
  { value: 1, label: '轻度' },
  { value: 2, label: '中度' },
  { value: 3, label: '重度' }
]

// 获取预警类型标签类型
const getWarningTypeTag = (type) => {
  switch (type) {
    case 1: return 'danger'
    case 2: return 'warning'
    case 3: return 'warning'
    case 4: return 'danger'
    case 5: return 'danger'
    case 6: return 'warning'
    default: return 'info'
  }
}

// 获取预警类型标签文本
const getWarningTypeLabel = (type) => {
  const option = warningTypeOptions.find(item => item.value === type)
  return option ? option.label : '未知'
}

// 获取预警级别标签类型
const getWarningLevelTag = (level) => {
  switch (level) {
    case 1: return 'info'
    case 2: return 'warning'
    case 3: return 'danger'
    default: return 'info'
  }
}

// 获取预警级别标签文本
const getWarningLevelLabel = (level) => {
  const option = warningLevelOptions.find(item => item.value === level)
  return option ? option.label : '未知'
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString()
}

// 获取预警详情
const getDetail = () => {
  const warningId = route.params.id
  if (!warningId) {
    ElMessage.error('预警ID不能为空')
    return
  }
  
  loading.value = true
  getHealthWarning(warningId).then(res => {
    warningInfo.value = res.data
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

// 返回上一页
const goBack = () => {
  router.go(-1)
}

// 处理预警处理
const processDialogVisible = ref(false)
const processForm = reactive({
  warningId: undefined,
  warningContent: '',
  processResult: ''
})
const processFormRef = ref(null)
const processRules = {
  processResult: [{ required: true, message: '请输入处理结果', trigger: 'blur' }]
}

const handleProcess = () => {
  processForm.warningId = warningInfo.value.warningId
  processForm.warningContent = warningInfo.value.warningContent
  processForm.processResult = ''
  processDialogVisible.value = true
}

const submitProcess = () => {
  processFormRef.value.validate(valid => {
    if (valid) {
      const data = {
        warningId: processForm.warningId,
        processResult: processForm.processResult,
        processStatus: 1
      }
      processHealthWarning(data).then(res => {
        ElMessage.success('处理成功')
        processDialogVisible.value = false
        getDetail()
      })
    }
  })
}

// 页面加载时获取数据
onMounted(() => {
  getDetail()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
}

.right-btns {
  display: flex;
  gap: 10px;
}

.related-record {
  margin-top: 20px;
}

.related-record h3 {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: bold;
}
</style>