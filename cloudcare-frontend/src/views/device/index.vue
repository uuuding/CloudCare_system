<template>
  <div class="device-container">
    <!-- 页面标题和统计卡片 -->
    <div class="page-header">
      <h1>设备管理</h1>
      <div class="statistics-cards">
        <div class="stat-card">
          <div class="stat-number">{{ statistics.totalCount || 0 }}</div>
          <div class="stat-label">设备总数</div>
        </div>
        <div class="stat-card normal">
          <div class="stat-number">{{ statistics.normalCount || 0 }}</div>
          <div class="stat-label">正常设备</div>
        </div>
        <div class="stat-card maintenance">
          <div class="stat-number">{{ statistics.maintenanceCount || 0 }}</div>
          <div class="stat-label">维修中</div>
        </div>
        <div class="stat-card disabled">
          <div class="stat-number">{{ statistics.disabledCount || 0 }}</div>
          <div class="stat-label">停用设备</div>
        </div>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <div class="search-form">
        <el-form :model="searchForm" inline>
          <el-form-item label="设备编号">
            <el-input v-model="searchForm.deviceCode" placeholder="请输入设备编号" clearable />
          </el-form-item>
          <el-form-item label="设备名称">
            <el-input v-model="searchForm.deviceName" placeholder="请输入设备名称" clearable />
          </el-form-item>
          <el-form-item label="设备类型">
            <el-select v-model="searchForm.deviceType" placeholder="请选择设备类型" clearable class="device-select">
              <el-option
                v-for="(label, value) in deviceTypes"
                :key="value"
                :label="label"
                :value="parseInt(value)"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="设备状态">
            <el-select v-model="searchForm.deviceStatus" placeholder="请选择设备状态" clearable class="device-select">
              <el-option
                v-for="(label, value) in deviceStatusOptions"
                :key="value"
                :label="label"
                :value="parseInt(value)"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">新增设备</el-button>
        <el-button type="danger" :disabled="!selectedRows.length" @click="handleBatchDelete">批量删除</el-button>
        <el-button type="warning" @click="handleMaintenanceCheck">维护提醒</el-button>
      </div>
    </div>

    <!-- 设备列表表格 -->
    <div class="table-section">
      <el-table
        v-loading="loading"
        :data="deviceList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="deviceCode" label="设备编号" width="120" />
        <el-table-column prop="deviceName" label="设备名称" width="150" />
        <el-table-column prop="deviceType" label="设备类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ getDeviceTypeName(row.deviceType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deviceModel" label="设备型号" width="120" />
        <el-table-column prop="manufacturer" label="厂商" width="120" />
        <el-table-column prop="deviceStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.deviceStatus)">{{ getDeviceStatusName(row.deviceStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="使用位置" width="120" />
        <el-table-column prop="managerName" label="负责人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button type="warning" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 设备信息弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form
        ref="deviceFormRef"
        :model="deviceForm"
        :rules="deviceRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备名称" prop="deviceName">
              <el-input v-model="deviceForm.deviceName" placeholder="请输入设备名称" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备类型" prop="deviceType">
              <el-select v-model="deviceForm.deviceType" placeholder="请选择设备类型" :disabled="dialogMode === 'view'">
                <el-option
                  v-for="(label, value) in deviceTypes"
                  :key="value"
                  :label="label"
                  :value="parseInt(value)"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备型号">
              <el-input v-model="deviceForm.deviceModel" placeholder="请输入设备型号" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备厂商">
              <el-input v-model="deviceForm.manufacturer" placeholder="请输入设备厂商" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="购买日期">
              <el-date-picker
                v-model="deviceForm.purchaseDate"
                type="datetime"
                placeholder="请选择购买日期"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
                :disabled="dialogMode === 'view'"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保修期限(月)">
              <el-input-number v-model="deviceForm.warrantyPeriod" :min="0" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备状态">
              <el-select v-model="deviceForm.deviceStatus" placeholder="请选择设备状态" :disabled="dialogMode === 'view'">
                <el-option
                  v-for="(label, value) in deviceStatusOptions"
                  :key="value"
                  :label="label"
                  :value="parseInt(value)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="使用位置">
              <el-input v-model="deviceForm.location" placeholder="请输入使用位置" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人姓名">
              <el-input v-model="deviceForm.managerName" placeholder="请输入负责人姓名" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="IP地址">
              <el-input v-model="deviceForm.ipAddress" placeholder="请输入IP地址" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="MAC地址">
              <el-input v-model="deviceForm.macAddress" placeholder="请输入MAC地址" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次检修时间">
              <el-date-picker
                v-model="deviceForm.nextMaintenanceTime"
                type="datetime"
                placeholder="请选择下次检修时间"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
                :disabled="dialogMode === 'view'"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input
            v-model="deviceForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
            :disabled="dialogMode === 'view'"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button v-if="dialogMode !== 'view'" type="primary" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 维护提醒弹窗 -->
    <el-dialog v-model="maintenanceDialogVisible" title="维护提醒" width="600px">
      <div v-if="maintenanceDevices.length === 0" class="no-maintenance">
        <el-empty description="暂无需要维护的设备" />
      </div>
      <div v-else>
        <p class="maintenance-tip">以下设备需要进行维护检修：</p>
        <el-table :data="maintenanceDevices" stripe>
          <el-table-column prop="deviceName" label="设备名称" />
          <el-table-column prop="deviceCode" label="设备编号" />
          <el-table-column prop="location" label="使用位置" />
          <el-table-column prop="nextMaintenanceTime" label="应检修时间">
            <template #default="{ row }">
              {{ formatDateTime(row.nextMaintenanceTime) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <el-button @click="maintenanceDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getDeviceList,
  getDeviceById,
  addDevice,
  updateDevice,
  deleteDevice,
  getDeviceStatistics,
  getMaintenanceRequiredDevices,
  getDeviceTypes,
  getDeviceStatusOptions
} from '@/api/device'

// 响应式数据
const loading = ref(false)
const deviceList = ref([])
const selectedRows = ref([])
const statistics = ref({})
const deviceTypes = ref({})
const deviceStatusOptions = ref({})
const maintenanceDevices = ref([])

// 搜索表单
const searchForm = reactive({
  deviceCode: '',
  deviceName: '',
  deviceType: null,
  deviceStatus: null
})

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 弹窗相关
const dialogVisible = ref(false)
const dialogMode = ref('add') // add, edit, view
const deviceFormRef = ref()
const maintenanceDialogVisible = ref(false)

// 设备表单
const deviceForm = reactive({
  deviceId: null,
  deviceName: '',
  deviceType: null,
  deviceModel: '',
  manufacturer: '',
  purchaseDate: '',
  warrantyPeriod: null,
  deviceStatus: 1,
  location: '',
  managerName: '',
  ipAddress: '',
  macAddress: '',
  nextMaintenanceTime: '',
  remark: ''
})

// 表单验证规则
const deviceRules = {
  deviceName: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
  deviceType: [{ required: true, message: '请选择设备类型', trigger: 'change' }]
}

// 计算属性
const dialogTitle = computed(() => {
  const titles = {
    add: '新增设备',
    edit: '编辑设备',
    view: '查看设备'
  }
  return titles[dialogMode.value]
})

// 方法
const loadDeviceList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const response = await getDeviceList(params)
    if (response.code === 200) {
      deviceList.value = response.data.records
      pagination.total = response.data.total
    }
  } catch (error) {
    ElMessage.error('获取设备列表失败')
  } finally {
    loading.value = false
  }
}

const loadStatistics = async () => {
  try {
    const response = await getDeviceStatistics()
    if (response.code === 200) {
      statistics.value = response.data
    }
  } catch (error) {
    console.error('获取统计信息失败', error)
  }
}

const loadDeviceTypes = async () => {
  try {
    const response = await getDeviceTypes()
    if (response.code === 200) {
      deviceTypes.value = response.data
    }
  } catch (error) {
    console.error('获取设备类型失败', error)
  }
}

const loadDeviceStatusOptions = async () => {
  try {
    const response = await getDeviceStatusOptions()
    if (response.code === 200) {
      deviceStatusOptions.value = response.data
    }
  } catch (error) {
    console.error('获取设备状态选项失败', error)
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadDeviceList()
}

const handleReset = () => {
  Object.assign(searchForm, {
    deviceCode: '',
    deviceName: '',
    deviceType: null,
    deviceStatus: null
  })
  handleSearch()
}

const handleAdd = () => {
  dialogMode.value = 'add'
  resetDeviceForm()
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  dialogMode.value = 'edit'
  try {
    const response = await getDeviceById(row.deviceId)
    if (response.code === 200) {
      Object.assign(deviceForm, response.data)
      dialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取设备信息失败')
  }
}

const handleView = async (row) => {
  dialogMode.value = 'view'
  try {
    const response = await getDeviceById(row.deviceId)
    if (response.code === 200) {
      Object.assign(deviceForm, response.data)
      dialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取设备信息失败')
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这个设备吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await deleteDevice([row.deviceId])
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadDeviceList()
      loadStatistics()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个设备吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const deviceIds = selectedRows.value.map(row => row.deviceId)
    const response = await deleteDevice(deviceIds)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadDeviceList()
      loadStatistics()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleMaintenanceCheck = async () => {
  try {
    const response = await getMaintenanceRequiredDevices()
    if (response.code === 200) {
      maintenanceDevices.value = response.data
      maintenanceDialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取维护设备列表失败')
  }
}

const handleSubmit = async () => {
  try {
    await deviceFormRef.value.validate()
    
    let response
    if (dialogMode.value === 'add') {
      response = await addDevice(deviceForm)
    } else {
      response = await updateDevice(deviceForm)
    }
    
    if (response.code === 200) {
      ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功')
      dialogVisible.value = false
      loadDeviceList()
      loadStatistics()
    }
  } catch (error) {
    ElMessage.error(dialogMode.value === 'add' ? '新增失败' : '更新失败')
  }
}

const handleDialogClose = () => {
  deviceFormRef.value?.resetFields()
  resetDeviceForm()
}

const resetDeviceForm = () => {
  Object.assign(deviceForm, {
    deviceId: null,
    deviceName: '',
    deviceType: null,
    deviceModel: '',
    manufacturer: '',
    purchaseDate: '',
    warrantyPeriod: null,
    deviceStatus: 1,
    location: '',
    managerName: '',
    ipAddress: '',
    macAddress: '',
    nextMaintenanceTime: '',
    remark: ''
  })
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  loadDeviceList()
}

const handleCurrentChange = (page) => {
  pagination.pageNum = page
  loadDeviceList()
}

const getDeviceTypeName = (type) => {
  return deviceTypes.value[type] || '未知'
}

const getDeviceStatusName = (status) => {
  return deviceStatusOptions.value[status] || '未知'
}

const getStatusTagType = (status) => {
  const types = {
    0: 'info',    // 停用
    1: 'success', // 正常
    2: 'warning', // 维修中
    3: 'danger'   // 已报废
  }
  return types[status] || 'info'
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

// 生命周期
onMounted(() => {
  loadDeviceList()
  loadStatistics()
  loadDeviceTypes()
  loadDeviceStatusOptions()
})
</script>

<style scoped>
.device-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0 0 20px 0;
  color: #303133;
}

.statistics-cards {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-left: 4px solid #409eff;
}

.stat-card.normal {
  border-left-color: #67c23a;
}

.stat-card.maintenance {
  border-left-color: #e6a23c;
}

.stat-card.disabled {
  border-left-color: #f56c6c;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.search-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.search-form {
  margin-bottom: 20px;
}

.action-buttons {
  text-align: right;
}

.table-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

.maintenance-tip {
  color: #e6a23c;
  font-weight: bold;
  margin-bottom: 15px;
}

.no-maintenance {
  text-align: center;
  padding: 40px 0;
}

/* 设备类型和状态下拉框样式 */
.device-select {
  width: 180px !important;
  min-width: 180px;
}

.device-select .el-input__wrapper {
  width: 180px !important;
}

/* 下拉选项样式优化 */
.device-select .el-select-dropdown__item {
  padding: 8px 12px;
  white-space: nowrap;
  overflow: visible;
  text-overflow: clip;
}
</style>