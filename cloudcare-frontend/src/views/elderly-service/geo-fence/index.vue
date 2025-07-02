<template>
  <div class="geo-fence-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>电子围栏管理</h2>
      <p>为老人设置安全活动范围，实时监控位置状态</p>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        创建围栏
      </el-button>
      <el-button @click="refreshData">
        <el-icon><Refresh /></el-icon>
        刷新
      </el-button>
    </div>

    <!-- 围栏列表 -->
    <el-card class="fence-list-card">
      <template #header>
        <span>围栏列表</span>
      </template>
      
      <el-table :data="fenceList" v-loading="loading" stripe>
        <el-table-column prop="fenceName" label="围栏名称" width="150" />
        <el-table-column prop="elderlyId" label="关联老人ID" width="120" />
        <el-table-column prop="fenceType" label="围栏类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.fenceType === 'circle' ? 'primary' : 'success'">
              {{ row.fenceType === 'circle' ? '圆形' : '多边形' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertType" label="告警方式" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.alertType === 'sms'" type="warning">短信</el-tag>
            <el-tag v-else-if="row.alertType === 'app'" type="info">应用推送</el-tag>
            <el-tag v-else type="danger">短信+推送</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-switch 
              v-model="row.status" 
              @change="toggleFenceStatus(row)"
              :active-value="1"
              :inactive-value="0"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="viewFenceEvents(row)">
              事件记录
            </el-button>
            <el-button size="small" type="primary" @click="editFence(row)">
              编辑
            </el-button>
            <el-button size="small" type="danger" @click="deleteFence(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 创建/编辑围栏对话框 -->
    <el-dialog 
      v-model="showCreateDialog" 
      :title="editingFence ? '编辑围栏' : '创建围栏'"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="fenceForm" :rules="fenceRules" ref="fenceFormRef" label-width="100px">
        <el-form-item label="围栏名称" prop="fenceName">
          <el-input v-model="fenceForm.fenceName" placeholder="请输入围栏名称" />
        </el-form-item>
        
        <el-form-item label="关联老人" prop="elderlyId">
          <el-select v-model="fenceForm.elderlyId" placeholder="请选择老人" style="width: 100%">
            <el-option 
              v-for="elderly in elderlyList" 
              :key="elderly.id" 
              :label="elderly.name" 
              :value="elderly.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="围栏类型" prop="fenceType">
          <el-radio-group v-model="fenceForm.fenceType">
            <el-radio label="circle">圆形围栏</el-radio>
            <el-radio label="polygon">多边形围栏</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="中心坐标" prop="centerCoordinates" v-if="fenceForm.fenceType === 'circle'">
          <el-input v-model="fenceForm.centerCoordinates" placeholder="格式：经度,纬度 如：116.404,39.915" />
        </el-form-item>
        
        <el-form-item label="半径(米)" prop="radius" v-if="fenceForm.fenceType === 'circle'">
          <el-input-number v-model="fenceForm.radius" :min="10" :max="10000" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="多边形坐标" prop="polygonCoordinates" v-if="fenceForm.fenceType === 'polygon'">
          <el-input 
            v-model="fenceForm.polygonCoordinates" 
            type="textarea" 
            :rows="4"
            placeholder="格式：经度1,纬度1;经度2,纬度2;经度3,纬度3..."
          />
        </el-form-item>
        
        <el-form-item label="告警方式" prop="alertType">
          <el-radio-group v-model="fenceForm.alertType">
            <el-radio label="sms">短信</el-radio>
            <el-radio label="app">应用推送</el-radio>
            <el-radio label="both">短信+推送</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="紧急联系人" prop="emergencyContacts">
          <el-input 
            v-model="fenceForm.emergencyContacts" 
            placeholder="多个手机号用逗号分隔，如：13800138000,13900139000"
          />
        </el-form-item>
        
        <el-form-item label="围栏描述">
          <el-input 
            v-model="fenceForm.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入围栏描述"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="saveFence" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <!-- 围栏事件记录对话框 -->
    <el-dialog v-model="showEventsDialog" title="围栏事件记录" width="800px">
      <el-table :data="eventList" v-loading="eventsLoading" stripe>
        <el-table-column prop="eventType" label="事件类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.eventType === 'enter' ? 'success' : 'warning'">
              {{ row.eventType === 'enter' ? '进入' : '离开' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="eventTime" label="事件时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.eventTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="location" label="位置坐标" width="150" />
        <el-table-column prop="alertSent" label="告警状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.alertSent ? 'success' : 'danger'">
              {{ row.alertSent ? '已发送' : '未发送' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertContent" label="告警内容" show-overflow-tooltip />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils/date'
import * as geoFenceApi from '@/api/geoFence'
import * as elderlyApi from '@/api/elderlyProfile'

// 响应式数据
const loading = ref(false)
const saving = ref(false)
const eventsLoading = ref(false)
const showCreateDialog = ref(false)
const showEventsDialog = ref(false)
const editingFence = ref(null)

// 分页数据
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 列表数据
const fenceList = ref([])
const elderlyList = ref([])
const eventList = ref([])

// 表单数据
const fenceForm = reactive({
  fenceName: '',
  elderlyId: null,
  fenceType: 'circle',
  centerCoordinates: '',
  radius: 500,
  polygonCoordinates: '',
  alertType: 'sms',
  emergencyContacts: '',
  description: ''
})

// 表单验证规则
const fenceRules = {
  fenceName: [{ required: true, message: '请输入围栏名称', trigger: 'blur' }],
  elderlyId: [{ required: true, message: '请选择关联老人', trigger: 'change' }],
  fenceType: [{ required: true, message: '请选择围栏类型', trigger: 'change' }],
  centerCoordinates: [{ required: true, message: '请输入中心坐标', trigger: 'blur' }],
  radius: [{ required: true, message: '请输入半径', trigger: 'blur' }],
  polygonCoordinates: [{ required: true, message: '请输入多边形坐标', trigger: 'blur' }],
  alertType: [{ required: true, message: '请选择告警方式', trigger: 'change' }],
  emergencyContacts: [{ required: true, message: '请输入紧急联系人', trigger: 'blur' }]
}

const fenceFormRef = ref()

// 生命周期
onMounted(() => {
  loadFenceList()
  loadElderlyList()
})

// 方法
const loadFenceList = async () => {
  loading.value = true
  try {
    const response = await geoFenceApi.getFenceList({
      page: currentPage.value,
      size: pageSize.value
    })
    if (response.success) {
      // 后端返回的数据结构是 { list: [], total: 0 }
      fenceList.value = response.data.list || []
      total.value = response.data.total || 0
      console.log('围栏列表数据:', response.data)
    }
  } catch (error) {
    console.error('加载围栏列表失败:', error)
    ElMessage.error('加载围栏列表失败')
  } finally {
    loading.value = false
  }
}

const loadElderlyList = async () => {
  try {
    const response = await elderlyApi.getElderlyList()
    if (response.success) {
      elderlyList.value = response.data || []
    }
  } catch (error) {
    console.error('加载老人列表失败:', error)
  }
}

const refreshData = () => {
  loadFenceList()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  loadFenceList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadFenceList()
}

const saveFence = async () => {
  if (!fenceFormRef.value) return
  
  try {
    await fenceFormRef.value.validate()
    saving.value = true
    
    const response = editingFence.value 
      ? await geoFenceApi.updateFence(editingFence.value.id, fenceForm)
      : await geoFenceApi.createFence(fenceForm)
    
    if (response.success) {
      ElMessage.success(editingFence.value ? '更新成功' : '创建成功')
      showCreateDialog.value = false
      loadFenceList()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('保存围栏失败:', error)
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

const editFence = (fence) => {
  editingFence.value = fence
  Object.assign(fenceForm, {
    fenceName: fence.fenceName,
    elderlyId: fence.elderlyId,
    fenceType: fence.fenceType,
    centerCoordinates: fence.centerCoordinates,
    radius: fence.radius,
    polygonCoordinates: fence.polygonCoordinates,
    alertType: fence.alertType,
    emergencyContacts: fence.emergencyContacts,
    description: fence.description
  })
  showCreateDialog.value = true
}

const deleteFence = async (fence) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除围栏"${fence.fenceName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await geoFenceApi.deleteFence(fence.id)
    if (response.success) {
      ElMessage.success('删除成功')
      loadFenceList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const toggleFenceStatus = async (fence) => {
  try {
    const response = await geoFenceApi.updateFence(fence.id, {
      ...fence,
      status: fence.status
    })
    
    if (response.success) {
      ElMessage.success(fence.status ? '围栏已启用' : '围栏已禁用')
    } else {
      // 回滚状态
      fence.status = fence.status ? 0 : 1
      ElMessage.error(response.message || '状态更新失败')
    }
  } catch (error) {
    // 回滚状态
    fence.status = fence.status ? 0 : 1
    ElMessage.error('状态更新失败')
  }
}

const viewFenceEvents = async (fence) => {
  eventsLoading.value = true
  showEventsDialog.value = true
  
  try {
    const response = await geoFenceApi.getFenceEvents(fence.id)
    if (response.success) {
      eventList.value = response.data || []
    }
  } catch (error) {
    ElMessage.error('加载事件记录失败')
  } finally {
    eventsLoading.value = false
  }
}

const resetForm = () => {
  editingFence.value = null
  Object.assign(fenceForm, {
    fenceName: '',
    elderlyId: null,
    fenceType: 'circle',
    centerCoordinates: '',
    radius: 500,
    polygonCoordinates: '',
    alertType: 'sms',
    emergencyContacts: '',
    description: ''
  })
  if (fenceFormRef.value) {
    fenceFormRef.value.clearValidate()
  }
}
</script>

<style scoped>
.geo-fence-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.toolbar {
  margin-bottom: 20px;
}

.fence-list-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-dialog__body) {
  padding: 20px;
}
</style>