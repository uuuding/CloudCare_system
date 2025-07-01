<template>
  <div class="system-log-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">系统日志</h1>
      <p class="page-description">查看和管理系统操作日志</p>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-card class="search-card" shadow="never">
        <el-form :model="searchForm" :inline="true" class="search-form">
          <el-form-item label="日志级别">
            <el-select v-model="searchForm.level" placeholder="请选择日志级别" clearable style="width: 150px">
              <el-option label="全部" value="" />
              <el-option label="信息" value="INFO" />
              <el-option label="警告" value="WARN" />
              <el-option label="错误" value="ERROR" />
              <el-option label="调试" value="DEBUG" />
            </el-select>
          </el-form-item>
          <el-form-item label="操作模块">
            <el-select v-model="searchForm.module" placeholder="请选择模块" clearable style="width: 150px">
              <el-option label="全部" value="" />
              <el-option label="用户管理" value="USER" />
              <el-option label="老人档案" value="ELDERLY" />
              <el-option label="健康管理" value="HEALTH" />
              <el-option label="医疗服务" value="MEDICAL" />
              <el-option label="系统设置" value="SYSTEM" />
            </el-select>
          </el-form-item>
          <el-form-item label="操作用户">
            <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable style="width: 150px" />
          </el-form-item>
          <el-form-item label="时间范围">
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
          <el-form-item label="关键词">
            <el-input v-model="searchForm.keyword" placeholder="请输入关键词" clearable style="width: 200px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchLogs" :loading="loading">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="resetSearch">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
            <el-button type="success" @click="exportLogs">
              <el-icon><Download /></el-icon>
              导出
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 统计信息 -->
    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card info-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><InfoFilled /></el-icon>
              </div>
              <div class="stat-info">
                <h3>{{ stats.info }}</h3>
                <p>信息日志</p>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card warn-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><WarningFilled /></el-icon>
              </div>
              <div class="stat-info">
                <h3>{{ stats.warn }}</h3>
                <p>警告日志</p>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card error-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><CircleCloseFilled /></el-icon>
              </div>
              <div class="stat-info">
                <h3>{{ stats.error }}</h3>
                <p>错误日志</p>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card total-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><Document /></el-icon>
              </div>
              <div class="stat-info">
                <h3>{{ stats.total }}</h3>
                <p>总日志数</p>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 日志列表 -->
    <div class="table-section">
      <el-card class="table-card" shadow="never">
        <template #header>
          <div class="card-header">
            <span>日志列表</span>
            <div class="header-actions">
              <el-button size="small" @click="refreshLogs">
                <el-icon><Refresh /></el-icon>
                刷新
              </el-button>
              <el-button size="small" type="danger" @click="clearLogs">
                <el-icon><Delete /></el-icon>
                清空日志
              </el-button>
            </div>
          </div>
        </template>
        
        <el-table 
          :data="logList" 
          v-loading="loading" 
          stripe 
          border
          style="width: 100%"
          :default-sort="{prop: 'createTime', order: 'descending'}"
        >
          <el-table-column prop="id" label="ID" width="80" sortable />
          <el-table-column prop="level" label="级别" width="80" sortable>
            <template #default="scope">
              <el-tag 
                :type="getLevelTagType(scope.row.level)" 
                size="small"
              >
                {{ scope.row.level }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="module" label="模块" width="100" sortable>
            <template #default="scope">
              <el-tag type="info" size="small">{{ getModuleName(scope.row.module) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="username" label="操作用户" width="120" sortable />
          <el-table-column prop="operation" label="操作" width="150" show-overflow-tooltip />
          <el-table-column prop="content" label="日志内容" min-width="200" show-overflow-tooltip />
          <el-table-column prop="ip" label="IP地址" width="130" />
          <el-table-column prop="userAgent" label="用户代理" width="150" show-overflow-tooltip />
          <el-table-column prop="createTime" label="创建时间" width="180" sortable>
            <template #default="scope">
              {{ formatTime(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" link @click="viewDetail(scope.row)">
                <el-icon><View /></el-icon>
                详情
              </el-button>
              <el-button size="small" type="danger" link @click="deleteLog(scope.row)">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页 -->
        <div class="pagination-container">
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
    </div>

    <!-- 日志详情对话框 -->
    <el-dialog v-model="detailVisible" title="日志详情" width="60%" :before-close="closeDetail">
      <div v-if="currentLog" class="log-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="日志ID">{{ currentLog.id }}</el-descriptions-item>
          <el-descriptions-item label="日志级别">
            <el-tag :type="getLevelTagType(currentLog.level)">{{ currentLog.level }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="操作模块">
            <el-tag type="info">{{ getModuleName(currentLog.module) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="操作用户">{{ currentLog.username }}</el-descriptions-item>
          <el-descriptions-item label="操作类型">{{ currentLog.operation }}</el-descriptions-item>
          <el-descriptions-item label="IP地址">{{ currentLog.ip }}</el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ formatTime(currentLog.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="用户代理" :span="2">{{ currentLog.userAgent }}</el-descriptions-item>
          <el-descriptions-item label="日志内容" :span="2">
            <div class="log-content">{{ currentLog.content }}</div>
          </el-descriptions-item>
          <el-descriptions-item v-if="currentLog.stackTrace" label="异常堆栈" :span="2">
            <div class="stack-trace">{{ currentLog.stackTrace }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDetail">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search,
  Refresh,
  Download,
  Delete,
  View,
  InfoFilled,
  WarningFilled,
  CircleCloseFilled,
  Document
} from '@element-plus/icons-vue'
import {
  getSystemLogList,
  getSystemLogStats,
  getSystemLogDetail,
  deleteSystemLog,
  clearSystemLog,
  exportSystemLog
} from '@/api/systemLog'

// 搜索表单
const searchForm = reactive({
  level: '',
  module: '',
  username: '',
  dateRange: [],
  keyword: ''
})

// 统计数据
const stats = reactive({
  info: 0,
  warn: 0,
  error: 0,
  total: 0
})

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 状态
const loading = ref(false)
const detailVisible = ref(false)
const currentLog = ref(null)

// 日志列表
const logList = ref([])

// 获取日志级别标签类型
const getLevelTagType = (level) => {
  const typeMap = {
    'INFO': 'success',
    'WARN': 'warning',
    'ERROR': 'danger',
    'DEBUG': 'info'
  }
  return typeMap[level] || 'info'
}

// 获取模块名称
const getModuleName = (module) => {
  const nameMap = {
    'USER': '用户管理',
    'ELDERLY': '老人档案',
    'HEALTH': '健康管理',
    'MEDICAL': '医疗服务',
    'SYSTEM': '系统设置'
  }
  return nameMap[module] || module
}

// 格式化时间
const formatTime = (time) => {
  return time
}

// 搜索日志
const searchLogs = async () => {
  try {
    loading.value = true
    const params = {
      page: pagination.currentPage,
      size: pagination.pageSize,
      level: searchForm.level,
      module: searchForm.module,
      username: searchForm.username,
      keyword: searchForm.keyword
    }
    
    // 处理时间范围
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startTime = searchForm.dateRange[0]
      params.endTime = searchForm.dateRange[1]
    }
    
    const response = await getSystemLogList(params)
    if (response.success) {
      logList.value = response.data.records || []
      pagination.total = response.data.total || 0
      ElMessage.success('查询成功')
    } else {
      ElMessage.error(response.message || '查询失败')
    }
  } catch (error) {
    console.error('查询日志失败:', error)
    ElMessage.error('查询失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    level: '',
    module: '',
    username: '',
    dateRange: [],
    keyword: ''
  })
  searchLogs()
}

// 导出日志
const exportLogs = async () => {
  try {
    const params = {
      level: searchForm.level,
      module: searchForm.module,
      username: searchForm.username,
      keyword: searchForm.keyword
    }
    
    // 处理时间范围
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startTime = searchForm.dateRange[0]
      params.endTime = searchForm.dateRange[1]
    }
    
    const response = await exportSystemLog(params)
    
    // 创建下载链接
    const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `系统日志_${new Date().toISOString().slice(0, 10)}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出日志失败:', error)
    ElMessage.error('导出失败，请稍后重试')
  }
}

// 刷新日志
const refreshLogs = () => {
  searchLogs()
}

// 清空日志
const clearLogs = () => {
  ElMessageBox.confirm('确定要清空所有日志吗？此操作不可恢复！', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await clearSystemLog()
      if (response.success) {
        ElMessage.success('日志清空成功')
        await loadData() // 重新加载数据
      } else {
        ElMessage.error(response.message || '清空失败')
      }
    } catch (error) {
      console.error('清空日志失败:', error)
      ElMessage.error('清空失败，请稍后重试')
    }
  }).catch(() => {
    ElMessage.info('已取消清空操作')
  })
}

// 查看详情
const viewDetail = async (row) => {
  try {
    const response = await getSystemLogDetail(row.id)
    if (response.success) {
      currentLog.value = response.data
      detailVisible.value = true
    } else {
      ElMessage.error(response.message || '获取详情失败')
    }
  } catch (error) {
    console.error('获取日志详情失败:', error)
    ElMessage.error('获取详情失败，请稍后重试')
  }
}

// 关闭详情
const closeDetail = () => {
  detailVisible.value = false
  currentLog.value = null
}

// 删除日志
const deleteLog = (row) => {
  ElMessageBox.confirm('确定要删除这条日志吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await deleteSystemLog(row.id)
      if (response.success) {
        ElMessage.success('删除成功')
        await searchLogs() // 重新查询数据
      } else {
        ElMessage.error(response.message || '删除失败')
      }
    } catch (error) {
      console.error('删除日志失败:', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

// 分页处理
const handleSizeChange = (size) => {
  pagination.pageSize = size
  searchLogs()
}

const handleCurrentChange = (page) => {
  pagination.currentPage = page
  searchLogs()
}

// 加载统计数据
const loadStats = async () => {
  try {
    const response = await getSystemLogStats()
    if (response.success) {
      Object.assign(stats, response.data)
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

// 加载数据
const loadData = async () => {
  await Promise.all([
    loadStats(),
    searchLogs()
  ])
}

// 初始化
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.system-log-container {
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: calc(100vh - 120px);
}

.page-header {
  text-align: center;
  margin-bottom: 20px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  backdrop-filter: blur(10px);
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: #ffffff;
  margin: 0 0 10px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-description {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

.search-section {
  margin-bottom: 20px;
}

.search-card {
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.search-form {
  padding: 10px 0;
}

.stats-section {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 10px;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
}

.info-card .stat-icon {
  background: linear-gradient(135deg, #36d1dc 0%, #5b86e5 100%);
  color: white;
}

.warn-card .stat-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.error-card .stat-icon {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
  color: white;
}

.total-card .stat-icon {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  color: white;
}

.stat-info h3 {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 5px 0;
}

.stat-info p {
  font-size: 14px;
  color: #7f8c8d;
  margin: 0;
}

.table-section {
  margin-bottom: 20px;
}

.table-card {
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.log-detail {
  max-height: 60vh;
  overflow-y: auto;
}

.log-content {
  background: #f8f9fa;
  padding: 10px;
  border-radius: 6px;
  border-left: 3px solid #3498db;
  font-family: 'Courier New', monospace;
  white-space: pre-wrap;
  word-break: break-all;
}

.stack-trace {
  background: #f8f9fa;
  padding: 10px;
  border-radius: 6px;
  border-left: 3px solid #e74c3c;
  font-family: 'Courier New', monospace;
  white-space: pre-wrap;
  word-break: break-all;
  max-height: 200px;
  overflow-y: auto;
}

/* 表格样式优化 */
:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background: #f8f9fa;
  color: #2c3e50;
  font-weight: 600;
}

:deep(.el-table td) {
  padding: 12px 0;
}

:deep(.el-table .el-table__row:hover > td) {
  background-color: #f0f9ff;
}

/* 对话框样式 */
:deep(.el-dialog) {
  border-radius: 12px;
}

:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px 12px 0 0;
  padding: 20px;
}

:deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}
</style>