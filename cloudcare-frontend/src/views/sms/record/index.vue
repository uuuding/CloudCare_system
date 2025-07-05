<template>
  <div class="sms-record-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>短信发送记录</span>
          <el-button type="primary" @click="refreshData">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="手机号码">
            <el-input
              v-model="searchForm.phone"
              placeholder="请输入手机号码"
              clearable
              style="width: 150px"
            />
          </el-form-item>
          <el-form-item label="发送状态">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择发送状态"
              clearable
              style="width: 120px"
            >
              <el-option label="全部" value="" />
              <el-option label="成功" value="success" />
              <el-option label="失败" value="failed" />
              <el-option label="发送中" value="sending" />
            </el-select>
          </el-form-item>
          <el-form-item label="短信类型">
            <el-select
              v-model="searchForm.type"
              placeholder="请选择短信类型"
              clearable
              style="width: 120px"
            >
              <el-option label="全部" value="" />
              <el-option label="普通短信" value="normal" />
              <el-option label="模板短信" value="template" />
              <el-option label="批量短信" value="batch" />
            </el-select>
          </el-form-item>
          <el-form-item label="发送时间">
            <el-date-picker
              v-model="searchForm.dateRange"
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
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="resetSearch">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 统计信息 -->
      <div class="stats-area">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="今日发送" :value="stats.todaySent" suffix="条">
              <template #prefix>
                <el-icon style="vertical-align: -0.125em">
                  <Promotion />
                </el-icon>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="发送成功" :value="stats.todaySent" suffix="条">
              <template #prefix>
                <el-icon style="vertical-align: -0.125em; color: #67c23a">
                  <CircleCheck />
                </el-icon>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="发送失败" :value="stats.failedCount" suffix="条">
              <template #prefix>
                <el-icon style="vertical-align: -0.125em; color: #f56c6c">
                  <CircleClose />
                </el-icon>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="成功率" :value="stats.successRate" :precision="1">
              <template #prefix>
                <el-icon style="vertical-align: -0.125em; color: #409eff">
                  <TrendCharts />
                </el-icon>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
      </div>

      <!-- 数据表格 -->
      <div class="table-area">
        <el-table
          v-loading="loading"
          :data="tableData"
          stripe
          border
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="记录ID" width="80" />
          <el-table-column prop="phone" label="手机号码" width="120" />
          <el-table-column prop="content" label="短信内容" min-width="200" show-overflow-tooltip>
            <template #default="{ row }">
              <div class="content-cell">
                <span>{{ row.content }}</span>
                <el-button
                  v-if="row.content.length > 50"
                  type="text"
                  size="small"
                  @click="showContentDetail(row)"
                >
                  查看详情
                </el-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="短信类型" width="100">
            <template #default="{ row }">
              <el-tag :type="getTypeColor(row.type)" size="small">
                {{ getTypeName(row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="template" label="模板" width="120">
            <template #default="{ row }">
              <span v-if="row.template">{{ row.templateName || row.template }}</span>
              <span v-else class="text-muted">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="发送状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusColor(row.status)" size="small">
                <el-icon v-if="row.status === 'sending'" class="is-loading">
                  <Loading />
                </el-icon>
                {{ getStatusName(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="sendTime" label="发送时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.sendTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="responseCode" label="响应码" width="80">
            <template #default="{ row }">
              <span v-if="row.responseCode !== null">{{ row.responseCode }}</span>
              <span v-else class="text-muted">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="errorMessage" label="错误信息" width="150" show-overflow-tooltip>
            <template #default="{ row }">
              <span v-if="row.errorMessage" class="error-message">{{ row.errorMessage }}</span>
              <span v-else class="text-muted">-</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <el-button type="text" size="small" @click="viewDetail(row)">
                查看
              </el-button>
              <el-button
                v-if="row.status === 'failed'"
                type="text"
                size="small"
                @click="resendSms(row)"
              >
                重发
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div class="pagination-area">
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
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="短信详情"
      width="600px"
      :before-close="handleDetailClose"
    >
      <div v-if="currentRecord" class="detail-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="记录ID">
            {{ currentRecord.id }}
          </el-descriptions-item>
          <el-descriptions-item label="手机号码">
            {{ currentRecord.phone }}
          </el-descriptions-item>
          <el-descriptions-item label="短信类型">
            <el-tag :type="getTypeColor(currentRecord.type)">
              {{ getTypeName(currentRecord.type) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="模板信息" v-if="currentRecord.template">
            {{ currentRecord.templateName || currentRecord.template }}
          </el-descriptions-item>
          <el-descriptions-item label="发送状态">
            <el-tag :type="getStatusColor(currentRecord.status)">
              {{ getStatusName(currentRecord.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="短信内容">
            <div class="content-detail">{{ currentRecord.content }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="发送时间">
            {{ formatDateTime(currentRecord.sendTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="响应码" v-if="currentRecord.responseCode !== null">
            {{ currentRecord.responseCode }}
          </el-descriptions-item>
          <el-descriptions-item label="错误信息" v-if="currentRecord.errorMessage">
            <span class="error-message">{{ currentRecord.errorMessage }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="发送参数" v-if="currentRecord.params">
            <pre class="params-detail">{{ JSON.stringify(currentRecord.params, null, 2) }}</pre>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
          <el-button
            v-if="currentRecord && currentRecord.status === 'failed'"
            type="primary"
            @click="resendCurrentSms"
          >
            重新发送
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 内容详情对话框 -->
    <el-dialog
      v-model="contentDialogVisible"
      title="短信内容"
      width="500px"
    >
      <div class="content-detail-dialog">
        {{ currentContent }}
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="contentDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search,
  Refresh,
  Promotion,
  CircleCheck,
  CircleClose,
  TrendCharts,
  Loading
} from '@element-plus/icons-vue'
import { smsApi } from '@/api/sms'

// 响应式数据
const loading = ref(false)
const detailDialogVisible = ref(false)
const contentDialogVisible = ref(false)
const currentRecord = ref(null)
const currentContent = ref('')
const selectedRecords = ref([])

// 搜索表单
const searchForm = reactive({
  phone: '',
  status: '',
  type: '',
  dateRange: []
})

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 统计信息
const stats = reactive({
  todaySent: 0,
  successCount: 0,
  failedCount: 0,
  successRate: 0
})

// 表格数据
const tableData = ref([])

// 计算属性已移除，数据直接从API获取

// 方法
const getTypeColor = (type) => {
  const colorMap = {
    normal: 'info',
    template: 'primary',
    batch: 'warning'
  }
  return colorMap[type] || 'info'
}

const getTypeName = (type) => {
  const nameMap = {
    normal: '普通短信',
    template: '模板短信',
    batch: '批量短信'
  }
  return nameMap[type] || '未知'
}

const getStatusColor = (status) => {
  const colorMap = {
    success: 'success',
    failed: 'danger',
    sending: 'warning'
  }
  return colorMap[status] || 'info'
}

const getStatusName = (status) => {
  const nameMap = {
    success: '成功',
    failed: '失败',
    sending: '发送中'
  }
  return nameMap[status] || '未知'
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  })
}

const handleSearch = () => {
  pagination.currentPage = 1
  loadData()
}

const resetSearch = () => {
  Object.assign(searchForm, {
    phone: '',
    status: '',
    type: '',
    dateRange: []
  })
  pagination.currentPage = 1
  loadData()
}

const handleSelectionChange = (selection) => {
  selectedRecords.value = selection
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  loadData()
}

const handleCurrentChange = (page) => {
  pagination.currentPage = page
  loadData()
}

const viewDetail = (record) => {
  currentRecord.value = record
  detailDialogVisible.value = true
}

const showContentDetail = (record) => {
  currentContent.value = record.content
  contentDialogVisible.value = true
}

const resendSms = async (record) => {
  try {
    await ElMessageBox.confirm(
      `确定要重新发送短信到 ${record.phone} 吗？`,
      '确认重发',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 调用重发API
    const response = await smsApi.sendSms({
      phone: record.phone,
      content: record.content
    })
    
    if (response.success) {
      ElMessage.success('短信重发成功')
      refreshData()
    } else {
      ElMessage.error(response.message || '短信重发失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重发短信失败:', error)
      ElMessage.error('短信重发失败')
    }
  }
}

const resendCurrentSms = () => {
  if (currentRecord.value) {
    resendSms(currentRecord.value)
    detailDialogVisible.value = false
  }
}

const handleDetailClose = () => {
  detailDialogVisible.value = false
  currentRecord.value = null
}

const refreshData = () => {
  loadData()
  loadStats()
}

const loadData = async () => {
  try {
    loading.value = true
    
    // 构建查询参数
    const params = {
      page: pagination.currentPage,
      size: pagination.pageSize
    }
    
    // 添加搜索条件
    if (searchForm.phone) {
      params.phone = searchForm.phone
    }
    if (searchForm.status) {
      params.status = searchForm.status
    }
    if (searchForm.type) {
      params.type = searchForm.type
    }
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startTime = searchForm.dateRange[0]
      params.endTime = searchForm.dateRange[1]
    }
    
    // 调用真实API
    const response = await smsApi.getSendRecords(params)
    
    if (response.success) {
      tableData.value = response.data.records
      pagination.total = response.data.total
      ElMessage.success('数据加载完成')
    } else {
      ElMessage.error(response.message || '加载数据失败')
    }
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadStats = async () => {
  try {
    const response = await smsApi.getSendStats()
    
    if (response.success) {
      stats.todaySent = response.data.todaySent
      stats.successCount = response.data.successCount
      stats.failedCount = response.data.failedCount
      stats.successRate = response.data.successRate
    } else {
      console.error('加载统计数据失败:', response.message)
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

onMounted(() => {
  loadData()
  loadStats()
})
</script>

<style scoped>
.sms-record-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  font-size: 16px;
}

.search-area {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.search-form {
  margin: 0;
}

.stats-area {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fafafa;
  border-radius: 4px;
}

.table-area {
  margin-bottom: 20px;
}

.pagination-area {
  display: flex;
  justify-content: center;
}

.content-cell {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.text-muted {
  color: #909399;
}

.error-message {
  color: #f56c6c;
}

.detail-content {
  max-height: 500px;
  overflow-y: auto;
}

.content-detail {
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  line-height: 1.6;
  white-space: pre-wrap;
  max-height: 200px;
  overflow-y: auto;
}

.content-detail-dialog {
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  line-height: 1.6;
  white-space: pre-wrap;
  max-height: 300px;
  overflow-y: auto;
}

.params-detail {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  font-size: 12px;
  max-height: 200px;
  overflow-y: auto;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-statistic__content) {
  font-size: 24px;
}

:deep(.el-statistic__title) {
  font-size: 14px;
  color: #606266;
}

:deep(.el-table .cell) {
  padding: 8px 10px;
}

:deep(.is-loading) {
  animation: rotating 2s linear infinite;
}

@keyframes rotating {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>