<template>
  <div class="appointment-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>医疗预约管理</h1>
      <p>管理老人的医疗预约服务</p>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchForm.elderName"
            placeholder="请输入老人姓名"
            clearable
            @clear="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-select
            v-model="searchForm.appointmentType"
            placeholder="请选择预约类型"
            clearable
            @clear="handleSearch"
          >
            <el-option label="门诊" :value="1" />
            <el-option label="上门" :value="2" />
            <el-option label="检查" :value="3" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            @clear="handleSearch"
          >
            <el-option label="待就诊" :value="0" />
            <el-option label="已完成" :value="1" />
            <el-option label="已取消" :value="2" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
          <el-button type="success" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增预约
          </el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 预约列表 -->
    <div class="table-section">
      <el-table
        :data="appointmentList"
        v-loading="loading"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column prop="appointmentId" label="预约ID" width="100" />
        <el-table-column prop="elderName" label="老人姓名" width="120" />
        <el-table-column prop="doctorName" label="医生姓名" width="120" />
        <el-table-column prop="appointmentTime" label="预约时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.appointmentTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="appointmentType" label="预约类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.appointmentType)">
              {{ getTypeText(row.appointmentType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(row)"
              :disabled="row.status === 2"
            >
              编辑
            </el-button>
            <el-button
              v-if="row.status === 0"
              type="success"
              size="small"
              @click="handleComplete(row)"
            >
              完成
            </el-button>
            <el-button
              v-if="row.status === 0"
              type="danger"
              size="small"
              @click="handleCancel(row)"
            >
              取消
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

    <!-- 新增/编辑预约对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="老人" prop="elderId">
          <el-select
            v-model="form.elderId"
            placeholder="请选择老人"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="elder in elderList"
              :key="elder.elderId"
              :label="elder.name"
              :value="elder.elderId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="医生" prop="doctorId">
          <el-select
            v-model="form.doctorId"
            placeholder="请选择医生"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="doctor in doctorList"
              :key="doctor.doctorId"
              :label="doctor.name"
              :value="doctor.doctorId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预约时间" prop="appointmentTime">
          <el-date-picker
            v-model="form.appointmentTime"
            type="datetime"
            placeholder="请选择预约时间"
            style="width: 100%"
            :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="预约类型" prop="appointmentType">
          <el-radio-group v-model="form.appointmentType">
            <el-radio :label="1">门诊</el-radio>
            <el-radio :label="2">上门</el-radio>
            <el-radio :label="3">检查</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import {
  getAppointmentPage,
  createAppointment,
  updateAppointment,
  deleteAppointment,
  updateAppointmentStatus
} from '@/api/medical/appointment'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  elderName: '',
  appointmentType: '',
  status: ''
})

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 预约列表
const appointmentList = ref([])

// 老人列表
const elderList = ref([])

// 医生列表
const doctorList = ref([])

// 表单数据
const form = reactive({
  appointmentId: null,
  elderId: '',
  doctorId: '',
  appointmentTime: '',
  appointmentType: 1,
  remark: ''
})

// 表单验证规则
const rules = {
  elderId: [
    { required: true, message: '请选择老人', trigger: 'change' }
  ],
  doctorId: [
    { required: true, message: '请选择医生', trigger: 'change' }
  ],
  appointmentTime: [
    { required: true, message: '请选择预约时间', trigger: 'change' }
  ],
  appointmentType: [
    { required: true, message: '请选择预约类型', trigger: 'change' }
  ]
}

// 获取预约列表
const getAppointmentList = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      current: pagination.currentPage,
      size: pagination.pageSize
    }
    
    const response = await getAppointmentPage(params)
    if (response.code === 200) {
      appointmentList.value = response.data.records
      pagination.total = response.data.total
    } else {
      ElMessage.error(response.message || '获取预约列表失败')
    }
  } catch (error) {
    console.error('获取预约列表失败:', error)
    ElMessage.error('获取预约列表失败')
  } finally {
    loading.value = false
  }
}

// 获取老人列表
const getElderList = async () => {
  try {
    // 模拟API调用
    elderList.value = [
      { elderId: 1, name: '张三' },
      { elderId: 2, name: '李四' },
      { elderId: 3, name: '王五' }
    ]
  } catch (error) {
    ElMessage.error('获取老人列表失败')
  }
}

// 获取医生列表
const getDoctorList = async () => {
  try {
    // 模拟API调用
    doctorList.value = [
      { doctorId: 1, name: '李医生' },
      { doctorId: 2, name: '王医生' },
      { doctorId: 3, name: '赵医生' }
    ]
  } catch (error) {
    ElMessage.error('获取医生列表失败')
  }
}

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
  getAppointmentList()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    elderName: '',
    appointmentType: '',
    status: ''
  })
  handleSearch()
}

// 新增预约
const handleAdd = () => {
  dialogTitle.value = '新增预约'
  resetForm()
  dialogVisible.value = true
}

// 编辑预约
const handleEdit = (row) => {
  dialogTitle.value = '编辑预约'
  Object.assign(form, {
    appointmentId: row.appointmentId,
    elderId: row.elderId,
    doctorId: row.doctorId,
    appointmentTime: new Date(row.appointmentTime),
    appointmentType: row.appointmentType,
    remark: row.remark
  })
  dialogVisible.value = true
}

// 完成预约
const handleComplete = async (row) => {
  try {
    await ElMessageBox.confirm('确认完成此预约吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await updateAppointmentStatus(row.appointmentId, 1)
    if (response.code === 200) {
      ElMessage.success('预约已完成')
      getAppointmentList()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('更新预约状态失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 取消预约
const handleCancel = async (row) => {
  try {
    await ElMessageBox.confirm('确认取消此预约吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await updateAppointmentStatus(row.appointmentId, 2)
    if (response.code === 200) {
      ElMessage.success('预约已取消')
      getAppointmentList()
    } else {
      ElMessage.error(response.message || '取消失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消预约失败:', error)
      ElMessage.error('取消失败')
    }
  }
}

// 删除预约
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除此预约吗？删除后无法恢复！', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await deleteAppointment(row.appointmentId)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      getAppointmentList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除预约失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 分页大小改变
const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  getAppointmentList()
}

// 当前页改变
const handleCurrentChange = (page) => {
  pagination.currentPage = page
  getAppointmentList()
}

// 关闭对话框
const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const data = { ...form }
    // 格式化日期时间为ISO字符串
    if (data.appointmentTime) {
      data.appointmentTime = formatDateTimeForAPI(data.appointmentTime)
    }
    let response
    
    if (form.appointmentId) {
      response = await updateAppointment(data.appointmentId, data)
    } else {
      response = await createAppointment(data)
    }
    
    if (response.code === 200) {
      ElMessage.success(form.appointmentId ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      getAppointmentList()
    } else {
      ElMessage.error(response.message || (form.appointmentId ? '编辑失败' : '新增失败'))
    }
  } catch (error) {
    console.error('保存预约失败:', error)
    ElMessage.error(form.appointmentId ? '编辑失败' : '新增失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    appointmentId: null,
    elderId: '',
    doctorId: '',
    appointmentTime: '',
    appointmentType: 1,
    remark: ''
  })
  formRef.value?.clearValidate()
}

// 禁用过去的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7 // 禁用昨天之前的日期
}

// 格式化日期时间显示
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 格式化日期时间为ISO字符串
const formatDateTimeForAPI = (date) => {
  if (!date) return ''
  return new Date(date).toISOString()
}


// 获取预约类型文本
const getTypeText = (type) => {
  const typeMap = {
    1: '门诊',
    2: '上门',
    3: '检查'
  }
  return typeMap[type] || '未知'
}

// 获取预约类型标签类型
const getTypeTagType = (type) => {
  const typeMap = {
    1: 'primary',
    2: 'success',
    3: 'warning'
  }
  return typeMap[type] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待就诊',
    1: '已完成',
    2: '已取消'
  }
  return statusMap[status] || '未知'
}

// 获取状态标签类型
const getStatusTagType = (status) => {
  const statusMap = {
    0: 'warning',
    1: 'success',
    2: 'danger'
  }
  return statusMap[status] || 'info'
}

// 组件挂载时获取数据
onMounted(() => {
  getAppointmentList()
  getElderList()
  getDoctorList()
})
</script>

<style scoped>
.appointment-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-header h1 {
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
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.table-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table__header) {
  background-color: #f8f9fa;
}

:deep(.el-table th) {
  background-color: #f8f9fa !important;
  color: #303133;
  font-weight: 600;
}

:deep(.el-button + .el-button) {
  margin-left: 8px;
}

:deep(.el-form-item__label) {
  font-weight: 600;
  color: #303133;
}

:deep(.el-dialog__header) {
  background-color: #f8f9fa;
  padding: 20px 20px 10px 20px;
  margin: 0;
}

:deep(.el-dialog__title) {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-dialog__body) {
  padding: 20px;
}

:deep(.el-tag) {
  font-weight: 500;
}

.el-button {
  border-radius: 6px;
}

.el-input, .el-select {
  border-radius: 6px;
}

:deep(.el-input__inner) {
  border-radius: 6px;
}

:deep(.el-select .el-input__inner) {
  border-radius: 6px;
}
</style>