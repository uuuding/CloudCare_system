<template>
  <div class="family-interaction-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>我的家属互动</h2>
      <p>查看与家属的沟通和互动记录</p>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="家属姓名">
          <el-input
            v-model="searchForm.familyName"
            placeholder="请输入家属姓名"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="互动类型">
          <el-select
            v-model="searchForm.interactionType"
            placeholder="请选择互动类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in interactionTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 350px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :loading="loading">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalInteractions }}</div>
              <div class="stat-label">总互动次数</div>
            </div>
            <el-icon class="stat-icon"><ChatDotRound /></el-icon>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.todayInteractions }}</div>
              <div class="stat-label">今日互动</div>
            </div>
            <el-icon class="stat-icon"><Calendar /></el-icon>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.activeFamilies }}</div>
              <div class="stat-label">活跃家属</div>
            </div>
            <el-icon class="stat-icon"><User /></el-icon>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.pendingReplies }}</div>
              <div class="stat-label">待回复</div>
            </div>
            <el-icon class="stat-icon"><Bell /></el-icon>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table
        :data="tableData"
        v-loading="loading"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="familyName" label="家属姓名" width="120" />
        <el-table-column prop="familyRelation" label="关系" width="100" />
        <el-table-column prop="interactionType" label="互动类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getInteractionTypeTag(row.interactionType)">
              {{ getInteractionTypeLabel(row.interactionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="互动内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="interactionTime" label="互动时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="replyContent" label="回复内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="replyTime" label="回复时间" width="180" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>



    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="互动记录详情"
      width="700px"
    >
      <div class="detail-content" v-if="currentRecord">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="老人姓名">{{ currentRecord.elderName }}</el-descriptions-item>
          <el-descriptions-item label="家属姓名">{{ currentRecord.familyName }}</el-descriptions-item>
          <el-descriptions-item label="家属关系">{{ currentRecord.familyRelation }}</el-descriptions-item>
          <el-descriptions-item label="互动类型">
            <el-tag :type="getInteractionTypeTag(currentRecord.interactionType)">
              {{ getInteractionTypeLabel(currentRecord.interactionType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="互动时间" :span="2">{{ currentRecord.interactionTime }}</el-descriptions-item>
          <el-descriptions-item label="互动内容" :span="2">
            <div class="content-text">{{ currentRecord.content }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTag(currentRecord.status)">
              {{ getStatusLabel(currentRecord.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="回复时间">{{ currentRecord.replyTime || '暂无' }}</el-descriptions-item>
          <el-descriptions-item label="回复内容" :span="2">
            <div class="content-text">{{ currentRecord.replyContent || '暂无回复' }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">
            <div class="content-text">{{ currentRecord.remarks || '无' }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>


  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search,
  Refresh,
  ChatDotRound,
  Calendar,
  User,
  Bell
} from '@element-plus/icons-vue'
import {
  getFamilyInteractionPage,
  getInteractionStatistics
} from '@/api/elderly/familyInteraction'
import { useUserStore } from '@/stores/user'

// 用户状态管理
const userStore = useUserStore()

// 响应式数据
const loading = ref(false)
const viewDialogVisible = ref(false)
const tableData = ref([])
const currentRecord = ref(null)

// 搜索表单
const searchForm = reactive({
  familyName: '',
  interactionType: '',
  status: ''
})

const dateRange = ref([])

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 统计数据
const statistics = reactive({
  totalInteractions: 0,
  todayInteractions: 0,
  activeFamilies: 0,
  pendingReplies: 0
})

// 选项数据
const interactionTypeOptions = [
  { label: '电话沟通', value: 'phone' },
  { label: '视频通话', value: 'video' },
  { label: '短信交流', value: 'sms' },
  { label: '微信聊天', value: 'wechat' },
  { label: '邮件往来', value: 'email' },
  { label: '面对面交流', value: 'face_to_face' },
  { label: '其他', value: 'other' }
]

const statusOptions = [
  { label: '待回复', value: 'pending' },
  { label: '已回复', value: 'replied' },
  { label: '已关闭', value: 'closed' }
]

// 获取互动类型标签
const getInteractionTypeLabel = (type) => {
  const option = interactionTypeOptions.find(item => item.value === type)
  return option ? option.label : type
}

const getInteractionTypeTag = (type) => {
  const tagMap = {
    phone: 'primary',
    video: 'success',
    sms: 'info',
    wechat: 'success',
    email: 'warning',
    face_to_face: 'danger',
    other: 'info'
  }
  return tagMap[type] || 'info'
}

// 获取状态标签
const getStatusLabel = (status) => {
  const option = statusOptions.find(item => item.value === status)
  return option ? option.label : status
}

const getStatusTag = (status) => {
  const tagMap = {
    pending: 'warning',
    replied: 'success',
    closed: 'info'
  }
  return tagMap[status] || 'info'
}

// 获取互动记录列表
const getInteractionList = async () => {
  try {
    loading.value = true
    const params = {
      ...searchForm,
      currentPage: pagination.currentPage,
      pageSize: pagination.pageSize,
      elderId: userStore.userId // 只获取当前老人的互动记录
    }
    
    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }
    
    const response = await getFamilyInteractionPage(params)
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取互动记录列表失败:', error)
    ElMessage.error('获取互动记录列表失败')
  } finally {
    loading.value = false
  }
}

// 获取统计数据
const getStatistics = async () => {
  try {
    const params = {
      elderId: userStore.userId // 只获取当前老人的统计数据
    }
    const response = await getInteractionStatistics(params)
    Object.assign(statistics, response.data)
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
  getInteractionList()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    familyName: '',
    interactionType: '',
    status: ''
  })
  dateRange.value = []
  pagination.currentPage = 1
  getInteractionList()
}

// 查看详情
const handleView = (row) => {
  currentRecord.value = row
  viewDialogVisible.value = true
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  getInteractionList()
}

// 当前页变化
const handleCurrentChange = (page) => {
  pagination.currentPage = page
  getInteractionList()
}

// 页面初始化
onMounted(() => {
  getInteractionList()
  getStatistics()
})
</script>

<style scoped>
.family-interaction-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.search-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 16px;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.statistics-section {
  margin-bottom: 20px;
}

.stat-card {
  position: relative;
  overflow: hidden;
}

.stat-content {
  padding: 20px;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

.stat-icon {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 48px;
  color: #409eff;
  opacity: 0.1;
}

.table-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.detail-content {
  padding: 20px 0;
}

.content-text {
  line-height: 1.6;
  word-break: break-all;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-table .el-table__cell) {
  padding: 12px 0;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
}
</style>