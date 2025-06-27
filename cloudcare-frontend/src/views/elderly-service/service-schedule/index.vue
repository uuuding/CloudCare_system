<template>
  <div class="service-schedule-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>服务调度管理</h2>
      <p>管理养老服务的调度和安排</p>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="服务名称">
          <el-input
            v-model="searchForm.serviceName"
            placeholder="请输入服务名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="服务类型">
          <el-select
            v-model="searchForm.serviceType"
            placeholder="请选择服务类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in serviceTypeOptions"
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
      
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增调度
        </el-button>
        <el-button type="success" @click="getTodayPending">
          <el-icon><Clock /></el-icon>
          今日待执行
        </el-button>
        <el-button type="danger" @click="handleGetUrgentSchedules">
          <el-icon><Warning /></el-icon>
          紧急调度
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table
        :data="tableData"
        v-loading="loading"
        stripe
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="serviceName" label="服务名称" width="150" show-overflow-tooltip />
        <el-table-column prop="serviceType" label="服务类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ getServiceTypeLabel(row.serviceType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="elderId" label="老人" width="120">
          <template #default="{ row }">
            <span>{{ getElderName(row.elderId) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="staffId" label="服务人员" width="120">
          <template #default="{ row }">
            <span>{{ getStaffName(row.staffId) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="scheduledStartTime" label="计划开始时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.scheduledStartTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="scheduledEndTime" label="计划结束时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.scheduledEndTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="{ row }">
            <el-tag :type="getPriorityType(row.priority)">{{ getPriorityLabel(row.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="服务描述" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 1"
              type="primary"
              size="small"
              @click="handleStart(row)"
            >
              开始
            </el-button>
            <el-button
              v-if="row.status === 2"
              type="success"
              size="small"
              @click="handleComplete(row)"
            >
              完成
            </el-button>
            <el-button
              v-if="row.status === 1 || row.status === 2"
              type="warning"
              size="small"
              @click="handleCancel(row)"
            >
              取消
            </el-button>
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(row)"
              :disabled="row.status === 3 || row.status === 4"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="服务名称" prop="serviceName">
              <el-input v-model="formData.serviceName" placeholder="请输入服务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务类型" prop="serviceType">
              <el-select v-model="formData.serviceType" placeholder="请选择服务类型" style="width: 100%">
                <el-option
                  v-for="item in serviceTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="老人" prop="elderId">
              <el-select v-model="formData.elderId" placeholder="请选择老人" style="width: 100%" filterable>
                <el-option
                  v-for="item in elderList"
                  :key="item.userId"
                  :label="item.realName"
                  :value="item.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务人员" prop="staffId">
              <el-select v-model="formData.staffId" placeholder="请选择服务人员" style="width: 100%" filterable>
                <el-option
                  v-for="item in staffList"
                  :key="item.userId"
                  :label="item.realName"
                  :value="item.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划开始时间" prop="scheduledStartTime">
              <el-date-picker
                v-model="formData.scheduledStartTime"
                type="datetime"
                placeholder="选择开始时间"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划结束时间" prop="scheduledEndTime">
              <el-date-picker
                v-model="formData.scheduledEndTime"
                type="datetime"
                placeholder="选择结束时间"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="formData.priority" placeholder="请选择优先级" style="width: 100%">
                <el-option
                  v-for="item in priorityOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%">
                <el-option
                  v-for="item in statusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="服务描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入服务描述"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Clock, Warning } from '@element-plus/icons-vue'
import {
  getServiceSchedulePage,
  getServiceScheduleById,
  createServiceSchedule,
  updateServiceSchedule,
  deleteServiceSchedule,
  startService,
  completeService,
  cancelService,
  getTodayPendingSchedules,
  getUrgentSchedules,
  getServiceTypeOptions,
  getServiceStatusOptions,
  getPriorityOptions
} from '@/api/elderly/serviceSchedule'
import { getElderList } from '@/api/user'
import { getStaffList } from '@/api/user'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref()
const tableData = ref([])
const selectedRows = ref([])
const elderList = ref([])
const staffList = ref([])
const dateRange = ref([])

// 搜索表单
const searchForm = reactive({
  serviceName: '',
  serviceType: '',
  status: ''
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表单数据
const formData = reactive({
  scheduleId: null,
  serviceName: '',
  serviceType: '',
  elderId: null,
  staffId: null,
  scheduledStartTime: '',
  scheduledEndTime: '',
  priority: 2,
  status: 1,
  description: '',
  remark: ''
})

// 表单验证规则
const formRules = {
  serviceName: [
    { required: true, message: '请输入服务名称', trigger: 'blur' }
  ],
  serviceType: [
    { required: true, message: '请选择服务类型', trigger: 'change' }
  ],
  elderId: [
    { required: true, message: '请选择老人', trigger: 'change' }
  ],
  staffId: [
    { required: true, message: '请选择服务人员', trigger: 'change' }
  ],
  scheduledStartTime: [
    { required: true, message: '请选择计划开始时间', trigger: 'change' }
  ],
  scheduledEndTime: [
    { required: true, message: '请选择计划结束时间', trigger: 'change' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入服务描述', trigger: 'blur' }
  ]
}

// 选项数据
const serviceTypeOptions = ref([])
const statusOptions = ref([])
const priorityOptions = ref([])

// 计算属性
const dialogTitle = computed(() => {
  return formData.scheduleId ? '编辑服务调度' : '新增服务调度'
})

// 初始化选项数据
const initOptions = async () => {
  try {
    serviceTypeOptions.value = getServiceTypeOptions()
    statusOptions.value = getServiceStatusOptions()
    priorityOptions.value = getPriorityOptions()
  } catch (error) {
    console.error('初始化选项数据失败:', error)
  }
}

// 获取老人列表
const getElders = async () => {
  try {
    const response = await getElderList()
    if (response.code === 200) {
      elderList.value = response.data || []
    }
  } catch (error) {
    console.error('获取老人列表失败:', error)
  }
}

// 获取服务人员列表
const getStaff = async () => {
  try {
    const response = await getStaffList()
    if (response.code === 200) {
      staffList.value = response.data || []
    }
  } catch (error) {
    console.error('获取服务人员列表失败:', error)
  }
}

// 获取服务调度列表
const getScheduleList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      serviceName: searchForm.serviceName || undefined,
      serviceType: searchForm.serviceType || undefined,
      status: searchForm.status || undefined,
      startTime: dateRange.value?.[0] || undefined,
      endTime: dateRange.value?.[1] || undefined
    }
    
    const response = await getServiceSchedulePage(params)
    if (response.code === 200) {
      tableData.value = response.data.records || []
      pagination.total = response.data.total || 0
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取服务调度列表失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getScheduleList()
}

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    serviceName: '',
    serviceType: '',
    status: ''
  })
  dateRange.value = []
  pagination.current = 1
  getScheduleList()
}

// 分页大小改变
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  getScheduleList()
}

// 当前页改变
const handleCurrentChange = (current) => {
  pagination.current = current
  getScheduleList()
}

// 选择行改变
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 新增
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = async (row) => {
  try {
    const response = await getServiceScheduleById(row.scheduleId)
    if (response.code === 200) {
      Object.assign(formData, response.data)
      dialogVisible.value = true
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取服务调度详情失败:', error)
    ElMessage.error('获取数据失败')
  }
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除服务调度"${row.serviceName}"吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await deleteServiceSchedule(row.scheduleId)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      getScheduleList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除服务调度失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 开始服务
const handleStart = async (row) => {
  try {
    const response = await startService(row.scheduleId)
    if (response.code === 200) {
      ElMessage.success('服务已开始')
      getScheduleList()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('开始服务失败:', error)
    ElMessage.error('操作失败')
  }
}

// 完成服务
const handleComplete = async (row) => {
  try {
    const response = await completeService(row.scheduleId)
    if (response.code === 200) {
      ElMessage.success('服务已完成')
      getScheduleList()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('完成服务失败:', error)
    ElMessage.error('操作失败')
  }
}

// 取消服务
const handleCancel = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要取消服务"${row.serviceName}"吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await cancelService(row.scheduleId)
    if (response.code === 200) {
      ElMessage.success('服务已取消')
      getScheduleList()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消服务失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 获取今日待执行
const getTodayPending = async () => {
  loading.value = true
  try {
    const response = await getTodayPendingSchedules()
    if (response.code === 200) {
      tableData.value = response.data || []
      pagination.total = response.data?.length || 0
      ElMessage.success(`找到 ${pagination.total} 条今日待执行的服务调度`)
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取今日待执行调度失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 获取紧急调度
const handleGetUrgentSchedules = async () => {
  loading.value = true
  try {
    const response = await getUrgentSchedules()
    if (response.code === 200) {
      tableData.value = response.data || []
      pagination.total = response.data?.length || 0
      ElMessage.success(`找到 ${pagination.total} 条紧急服务调度`)
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取紧急调度失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    // 验证时间
    if (formData.scheduledStartTime >= formData.scheduledEndTime) {
      ElMessage.error('计划结束时间必须大于开始时间')
      return
    }
    
    submitLoading.value = true
    
    const response = formData.scheduleId
      ? await updateServiceSchedule(formData.scheduleId, formData)
      : await createServiceSchedule(formData)
    
    if (response.code === 200) {
      ElMessage.success(formData.scheduleId ? '更新成功' : '创建成功')
      dialogVisible.value = false
      getScheduleList()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    if (error !== false) { // 表单验证失败时error为false
      console.error('提交表单失败:', error)
      ElMessage.error('操作失败')
    }
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    scheduleId: null,
    serviceName: '',
    serviceType: '',
    elderId: null,
    staffId: null,
    scheduledStartTime: '',
    scheduledEndTime: '',
    priority: 2,
    status: 1,
    description: '',
    remark: ''
  })
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 获取服务类型标签
const getServiceTypeLabel = (value) => {
  const option = serviceTypeOptions.value.find(item => item.value === value)
  return option ? option.label : value
}

// 获取状态标签
const getStatusLabel = (value) => {
  const option = statusOptions.value.find(item => item.value === value)
  return option ? option.label : value
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    1: '', // 待执行
    2: 'warning', // 执行中
    3: 'success', // 已完成
    4: 'danger' // 已取消
  }
  return typeMap[status] || ''
}

// 获取优先级标签
const getPriorityLabel = (value) => {
  const option = priorityOptions.value.find(item => item.value === value)
  return option ? option.label : value
}

// 获取优先级类型
const getPriorityType = (priority) => {
  const typeMap = {
    1: 'danger', // 紧急
    2: 'warning', // 高
    3: '', // 中
    4: 'info' // 低
  }
  return typeMap[priority] || ''
}

// 获取老人姓名
const getElderName = (elderId) => {
  const elder = elderList.value.find(item => item.userId === elderId)
  return elder ? elder.realName : '-'
}

// 获取服务人员姓名
const getStaffName = (staffId) => {
  const staff = staffList.value.find(item => item.userId === staffId)
  return staff ? staff.realName : '-'
}

// 组件挂载时初始化
onMounted(() => {
  initOptions()
  getElders()
  getStaff()
  getScheduleList()
})
</script>

<style scoped>
.service-schedule-container {
  padding: 24px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.page-header h2 {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.search-section {
  background: white;
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.search-form {
  margin-bottom: 16px;
}

.action-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.action-buttons .el-button {
  border-radius: 8px;
  font-weight: 500;
}

.table-section {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.el-table {
  border-radius: 8px;
  overflow: hidden;
}

.el-table .el-table__header-wrapper {
  background: #f8f9fa;
}

.el-table .el-table__header th {
  background: #f8f9fa;
  color: #495057;
  font-weight: 600;
  border-bottom: 2px solid #e9ecef;
}

.el-table .el-table__row:hover {
  background-color: #f8f9fa;
}

.el-table .el-button {
  margin: 0 2px;
  border-radius: 6px;
  font-size: 12px;
  padding: 4px 8px;
}

.pagination-section {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

.el-pagination {
  --el-pagination-button-color: #606266;
  --el-pagination-hover-color: #409eff;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.el-dialog {
  border-radius: 12px;
}

.el-dialog__header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px 24px;
  border-radius: 12px 12px 0 0;
}

.el-dialog__title {
  color: white;
  font-weight: 600;
}

.el-dialog__body {
  padding: 24px;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-form-item__label {
  font-weight: 500;
  color: #495057;
}

.el-input, .el-select, .el-date-editor {
  border-radius: 8px;
}

.el-input__wrapper {
  border-radius: 8px;
}

.el-textarea__inner {
  border-radius: 8px;
}

.el-tag {
  border-radius: 6px;
  font-weight: 500;
}

.el-tag--primary {
  background-color: #e3f2fd;
  color: #1976d2;
  border-color: #bbdefb;
}

.el-tag--success {
  background-color: #e8f5e8;
  color: #2e7d32;
  border-color: #c8e6c9;
}

.el-tag--warning {
  background-color: #fff3e0;
  color: #f57c00;
  border-color: #ffcc02;
}

.el-tag--danger {
  background-color: #ffebee;
  color: #d32f2f;
  border-color: #ffcdd2;
}

.el-tag--info {
  background-color: #f5f5f5;
  color: #616161;
  border-color: #e0e0e0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .service-schedule-container {
    padding: 16px;
  }
  
  .page-header {
    padding: 16px;
  }
  
  .page-header h2 {
    font-size: 24px;
  }
  
  .search-section {
    padding: 16px;
  }
  
  .table-section {
    padding: 16px;
  }
  
  .search-form .el-form-item {
    margin-bottom: 16px;
  }
  
  .action-buttons {
    justify-content: center;
  }
  
  .el-table .el-table__cell {
    padding: 8px 4px;
  }
  
  .el-dialog {
    width: 95% !important;
    margin: 0 auto;
  }
}

/* 动画效果 */
.el-table .el-table__row {
  transition: background-color 0.3s ease;
}

.el-button {
  transition: all 0.3s ease;
}

.el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.el-card {
  transition: box-shadow 0.3s ease;
}

.el-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>