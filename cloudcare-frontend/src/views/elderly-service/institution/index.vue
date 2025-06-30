<template>
  <div class="institution-container">
    <!-- 页面标题和统计卡片 -->
    <div class="page-header">
      <h1>机构管理</h1>
      <div class="statistics-cards">
        <div class="stat-card">
          <div class="stat-number">{{ statistics.totalCount || 0 }}</div>
          <div class="stat-label">机构总数</div>
        </div>
        <div class="stat-card active">
          <div class="stat-number">{{ statistics.activeCount || 0 }}</div>
          <div class="stat-label">运营中</div>
        </div>
        <div class="stat-card pending">
          <div class="stat-number">{{ statistics.pendingCount || 0 }}</div>
          <div class="stat-label">筹备中</div>
        </div>
        <div class="stat-card suspended">
          <div class="stat-number">{{ statistics.suspendedCount || 0 }}</div>
          <div class="stat-label">暂停服务</div>
        </div>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <div class="search-form">
        <el-form :model="searchForm" inline>
          <el-form-item label="机构名称">
            <el-input v-model="searchForm.institutionName" placeholder="请输入机构名称" clearable />
          </el-form-item>
          <el-form-item label="机构类型">
            <el-select v-model="searchForm.institutionType" placeholder="请选择机构类型" clearable style="width: 180px">
              <el-option label="全部" value="" />
              <el-option label="养老院" value="养老院" />
              <el-option label="护理院" value="护理院" />
              <el-option label="养老中心" value="养老中心" />
              <el-option label="老年公寓" value="老年公寓" />
              <el-option label="日间照料中心" value="日间照料中心" />
            </el-select>
          </el-form-item>
          <el-form-item label="机构状态">
            <el-select v-model="searchForm.institutionStatus" placeholder="请选择机构状态" clearable>
              <el-option label="全部" value="" />
              <el-option label="运营中" value="运营中" />
              <el-option label="筹备中" value="筹备中" />
              <el-option label="暂停服务" value="暂停服务" />
            </el-select>
          </el-form-item>
          <el-form-item label="联系人">
            <el-input v-model="searchForm.contactPerson" placeholder="请输入联系人" clearable />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="searchForm.contactPhone" placeholder="请输入联系电话" clearable />
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="searchForm.address" placeholder="请输入地址" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">新增机构</el-button>
        <el-button type="danger" :disabled="!selectedRows.length" @click="handleBatchDelete">批量删除</el-button>
        <el-button type="success" @click="handleExport">导出数据</el-button>
      </div>
    </div>

    <!-- 机构列表表格 -->
    <div class="table-section">
      <el-table
        v-loading="loading"
        :data="institutionList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="institutionId" label="机构ID" width="80" />
        <el-table-column prop="name" label="机构名称" width="200" show-overflow-tooltip />
        <el-table-column prop="type" label="机构类型" width="120" />
        <el-table-column prop="status" label="机构状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bedTotal" label="总床位数" width="100" />
        <el-table-column prop="bedAvailable" label="可用床位数" width="120" />
        <el-table-column label="入住率" width="100">
          <template #default="{ row }">
            <span>{{ calculateOccupancyRate(row.bedTotal - row.bedAvailable, row.bedTotal) }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="contactPerson" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column prop="address" label="地址" width="200" show-overflow-tooltip />
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
      <div class="pagination-section">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 新增/编辑机构对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="机构名称" prop="name">
              <el-input v-model="formData.name" placeholder="请输入机构名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构类型" prop="type">
              <el-select v-model="formData.type" placeholder="请选择机构类型" style="width: 100%">
                <el-option label="养老院" value="养老院" />
                <el-option label="护理院" value="护理院" />
                <el-option label="养老中心" value="养老中心" />
                <el-option label="老年公寓" value="老年公寓" />
                <el-option label="日间照料中心" value="日间照料中心" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="机构状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择机构状态" style="width: 100%">
                <el-option label="运营中" value="运营中" />
                <el-option label="筹备中" value="筹备中" />
                <el-option label="暂停服务" value="暂停服务" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- 预留位置 -->
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="机构地址" prop="address">
              <el-input v-model="formData.address" placeholder="请输入机构地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="总床位数" prop="bedTotal">
              <el-input-number v-model="formData.bedTotal" :min="1" :max="9999" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="可用床位数" prop="bedAvailable">
              <el-input-number v-model="formData.bedAvailable" :min="0" :max="formData.bedTotal || 9999" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="formData.contactPerson" placeholder="请输入联系人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="机构简介" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入机构简介"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看机构详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="机构详情"
      width="800px"
    >
      <div class="institution-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="机构名称">{{ viewData.name }}</el-descriptions-item>
          <el-descriptions-item label="机构类型">{{ viewData.type }}</el-descriptions-item>
          <el-descriptions-item label="机构状态">
            <el-tag :type="getStatusTagType(viewData.status)">{{ getStatusText(viewData.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="床位数">{{ viewData.bedTotal }}</el-descriptions-item>
          <el-descriptions-item label="可用床位">{{ viewData.bedAvailable }}</el-descriptions-item>
          <el-descriptions-item label="入住率">{{ calculateOccupancyRate(viewData.bedTotal - viewData.bedAvailable, viewData.bedTotal) }}%</el-descriptions-item>
          <el-descriptions-item label="联系人">{{ viewData.contactPerson }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ viewData.contactPhone }}</el-descriptions-item>
          <el-descriptions-item label="地址" :span="2">{{ viewData.address }}</el-descriptions-item>
          <el-descriptions-item label="机构简介" :span="2">{{ viewData.description }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDateTime(viewData.createTime) }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getInstitutionList,
  getInstitutionById,
  addInstitution,
  updateInstitution,
  deleteInstitution,
  deleteInstitutionById,
  getInstitutionStatistics
} from '@/api/institution'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('')
const selectedRows = ref([])
const formRef = ref(null)

// 统计数据
const statistics = reactive({
  totalCount: 0,
  activeCount: 0,
  pendingCount: 0,
  suspendedCount: 0
})

// 搜索表单
const searchForm = reactive({
  institutionName: '',
  institutionType: '',
  institutionStatus: '',
  contactPerson: '',
  contactPhone: '',
  address: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 机构列表
const institutionList = ref([])

// 表单数据
const formData = reactive({
  institutionId: null,
  name: '',
  type: '',
  status: null,
  address: '',
  contactPerson: '',
  contactPhone: '',
  bedTotal: null,
  bedAvailable: null,
  description: ''
})

// 查看详情数据
const viewData = reactive({
  institutionId: null,
  name: '',
  type: '',
  status: null,
  address: '',
  contactPerson: '',
  contactPhone: '',
  bedTotal: null,
  bedAvailable: null,
  description: '',
  createTime: ''
})





// 表单验证规则
const formRules = {
  name: [
    { required: true, message: '请输入机构名称', trigger: 'blur' },
    { min: 2, max: 50, message: '机构名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择机构类型', trigger: 'change' }
  ],
  address: [
    { required: true, message: '请输入机构地址', trigger: 'blur' }
  ],
  contactPerson: [
    { required: true, message: '请输入联系人姓名', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  bedTotal: [
    { required: true, message: '请输入总床位数', trigger: 'blur' },
    { type: 'number', min: 1, message: '总床位数必须大于0', trigger: 'blur' }
  ],
  bedAvailable: [
    { required: true, message: '请输入可用床位数', trigger: 'blur' },
    { type: 'number', min: 0, message: '可用床位数不能小于0', trigger: 'blur' }
  ]
}

// 方法定义
const calculateOccupancyRate = (occupied, total) => {
  if (!total || total === 0) return 0
  return Math.round((occupied / total) * 100)
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 搜索功能
const handleSearch = () => {
  pagination.current = 1
  loadInstitutionList()
}

const handleReset = () => {
  Object.assign(searchForm, {
    institutionName: '',
    institutionType: '',
    institutionStatus: '',
    contactPerson: '',
    contactPhone: '',
    address: ''
  })
  handleSearch()
}

// 表格操作
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadInstitutionList()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadInstitutionList()
}

// 新增机构
const handleAdd = () => {
  dialogTitle.value = '新增机构'
  resetFormData()
  dialogVisible.value = true
}

// 编辑机构
const handleEdit = async (row) => {
  try {
    const response = await getInstitutionById(row.institutionId)
    Object.assign(formData, response.data)
    dialogTitle.value = '编辑机构'
    dialogVisible.value = true
  } catch (error) {
    console.error('获取机构信息失败:', error)
    ElMessage.error('获取机构信息失败')
  }
}

// 查看机构详情
const handleView = async (row) => {
  try {
    const response = await getInstitutionById(row.institutionId)
    Object.assign(viewData, response.data)
    viewDialogVisible.value = true
  } catch (error) {
    console.error('获取机构详情失败:', error)
    ElMessage.error('获取机构详情失败')
  }
}

// 删除机构
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除机构 "${row.name}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteInstitutionById(row.institutionId)
    
    ElMessage.success('删除成功')
    loadInstitutionList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的机构')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个机构吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const institutionIds = selectedRows.value.map(row => row.institutionId)
    await deleteInstitution(institutionIds)
    
    ElMessage.success('批量删除成功')
    selectedRows.value = []
    loadInstitutionList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 导出数据
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 表单提交
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    const isEdit = !!formData.institutionId
    const apiCall = isEdit ? updateInstitution : addInstitution
    
    await apiCall(formData)
    
    const action = isEdit ? '更新' : '新增'
    ElMessage.success(`${action}成功`)
    
    dialogVisible.value = false
    loadInstitutionList()
  } catch (error) {
    console.error('提交表单失败:', error)
    if (error !== false) {
      ElMessage.error('操作失败')
    }
  }
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
  resetFormData()
}

// 重置表单数据
const resetFormData = () => {
  Object.assign(formData, {
    institutionId: null,
    name: '',
    type: '',
    status: null,
    address: '',
    contactPerson: '',
    contactPhone: '',
    bedTotal: null,
    bedAvailable: null,
    description: ''
  })
}

// 加载统计数据
const loadStatistics = async () => {
  try {
    const [institutionStats] = await Promise.all([
      getInstitutionStatistics()
    ])
    
    statistics.totalCount = institutionStats.data.totalCount || 0
    statistics.activeCount = institutionStats.data.activeCount || 0
    statistics.pendingCount = institutionStats.data.pendingCount || 0
    statistics.suspendedCount = institutionStats.data.suspendedCount || 0
  } catch (error) {
    console.error('加载统计数据失败:', error)
    ElMessage.error('加载统计数据失败')
  }
}

// 加载机构列表
const loadInstitutionList = async () => {
  loading.value = true
  try {
    const params = {
       pageNum: pagination.current,
       pageSize: pagination.size,
       name: searchForm.institutionName,
       type: searchForm.institutionType || undefined,
       status: searchForm.institutionStatus || undefined,
       contactPerson: searchForm.contactPerson,
       contactPhone: searchForm.contactPhone,
       address: searchForm.address
     }
     
     // 移除空值参数
     Object.keys(params).forEach(key => {
       if (params[key] === '' || params[key] === null || params[key] === undefined) {
         delete params[key]
       }
     })
    
    const response = await getInstitutionList(params)
    
    institutionList.value = response.data.records || []
    pagination.total = response.data.total || 0
    

    
  } catch (error) {
    console.error('加载机构列表失败:', error)
    ElMessage.error('加载机构列表失败')
  } finally {
    loading.value = false
  }
}

// 状态相关辅助方法
const getStatusText = (status) => {
  return status || '未设置'
}

const getStatusTagType = (status) => {
  const typeMap = {
    '运营中': 'success',
    '筹备中': 'warning',
    '暂停服务': 'danger'
  }
  return typeMap[status] || 'info'
}

// 组件挂载时加载数据
onMounted(() => {
  loadInstitutionList()
  loadStatistics()
})
</script>

<style scoped>
.institution-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 24px;
  font-weight: 600;
}

.statistics-cards {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  flex: 1;
  text-align: center;
  border-left: 4px solid #e0e0e0;
}

.stat-card.active {
  border-left-color: #67c23a;
}

.stat-card.pending {
  border-left-color: #e6a23c;
}

.stat-card.suspended {
  border-left-color: #909399;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.stat-label {
  color: #666;
  font-size: 14px;
}

.search-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}



.table-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  text-align: right;
}

.institution-detail {
  padding: 20px 0;
}

:deep(.el-table th) {
  background-color: #f8f9fa;
  color: #333;
  font-weight: 600;
}

:deep(.el-table td) {
  padding: 12px 0;
}

:deep(.el-button + .el-button) {
  margin-left: 8px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-descriptions-item__label) {
  font-weight: 600;
  color: #333;
}
</style>