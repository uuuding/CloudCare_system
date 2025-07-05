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
      <el-button type="success" @click="showBindDialog = true">
        <el-icon><Link /></el-icon>
        设备绑定
      </el-button>
      <el-button @click="refreshData">
        <el-icon><Refresh /></el-icon>
        刷新
      </el-button>
    </div>

    <!-- 围栏列表 -->
    <el-card class="fence-list-card">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>围栏列表</span>
          <el-button type="info" @click="showAllEventsDialog = true">
            <el-icon><List /></el-icon>
            查看所有事件
          </el-button>
        </div>
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
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button size="small" @click="viewFenceEvents(row)">
              事件记录
            </el-button>
            <el-button size="small" type="success" @click="viewElderlyTrack(row)">
              轨迹查看
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
          <div class="coordinate-input-group">
            <el-input 
              v-model="fenceForm.centerCoordinates" 
              placeholder="格式：经度,纬度 如：114.125,22.699"
              style="flex: 1; margin-right: 10px;"
            />
            <MapPicker 
              v-model="fenceForm.centerCoordinates"
              button-text="地图选点"
              @change="handleCoordinateChange"
            />
            <el-button 
              @click="getCurrentLocation" 
              :loading="gettingLocation"
              style="margin-left: 10px;"
            >
              <el-icon><Location /></el-icon>
              当前位置
            </el-button>
          </div>
          <div class="form-tip">
            提示：可通过地图选点、获取当前位置或手动输入坐标（格式：经度,纬度）
          </div>
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
    <el-dialog
      v-model="showEventsDialog"
      title="围栏事件记录"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-table
        v-loading="eventsLoading"
        :data="eventList"
        style="width: 100%"
      >
        <el-table-column prop="eventTime" label="事件时间" width="180" />
        <el-table-column prop="eventType" label="事件类型" width="120">
          <template #default="{ row }">
            <el-tag :type="row.eventType === 'enter' ? 'success' : 'danger'">
              {{ row.eventType === 'enter' ? '进入' : '离开' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="elderlyName" label="老人姓名" width="120" />
        <el-table-column prop="coordinates" label="位置坐标" />
        <el-table-column prop="description" label="描述" />
      </el-table>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEventsDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 设备绑定对话框 -->
    <el-dialog
      v-model="showBindDialog"
      title="设备绑定管理"
      width="900px"
      :close-on-click-modal="false"
      @close="resetBindForm"
    >
      <!-- 绑定表单 -->
      <el-card class="bind-form-card" shadow="never">
        <template #header>
          <span>新增设备绑定</span>
        </template>
        <el-form
          ref="bindFormRef"
          :model="bindForm"
          :rules="bindRules"
          label-width="120px"
          inline
        >
          <el-form-item label="设备MAC地址" prop="macid">
            <el-input
              v-model="bindForm.macid"
              placeholder="请输入设备MAC地址"
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="关联老人" prop="elderlyId">
            <el-select
              v-model="bindForm.elderlyId"
              placeholder="请选择老人"
              style="width: 200px"
            >
              <el-option
                v-for="elderly in elderlyList"
                :key="elderly.id"
                :label="elderly.name"
                :value="elderly.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              :loading="binding"
              @click="bindDevice"
            >
              绑定设备
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 已绑定设备列表 -->
      <el-card class="bind-list-card" shadow="never">
        <template #header>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span>已绑定设备列表</span>
            <el-button size="small" @click="loadDeviceBindings">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </template>
        <el-table
          v-loading="bindingsLoading"
          :data="deviceBindings"
          style="width: 100%"
        >
          <el-table-column prop="macid" label="设备MAC地址" width="180" />
          <el-table-column prop="elderlyName" label="关联老人" width="120" />
          <el-table-column prop="elderlyId" label="老人ID" width="100" />
          <el-table-column prop="bindTime" label="绑定时间" width="180" />
          <el-table-column prop="lastActiveTime" label="最后活跃时间" width="180" />
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button
                type="danger"
                size="small"
                @click="unbindDevice(row)"
              >
                解绑
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showBindDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 所有围栏事件对话框 -->
    <el-dialog
      v-model="showAllEventsDialog"
      title="所有围栏事件记录"
      width="1200px"
      :close-on-click-modal="false"
    >
      <!-- 筛选条件 -->
      <div class="event-filters">
        <el-form :model="eventFilters" inline>
          <el-form-item label="老人选择">
            <el-select
              v-model="eventFilters.elderlyId"
              placeholder="选择老人"
              clearable
              style="width: 150px"
              @change="loadAllEvents"
            >
              <el-option label="全部" :value="null" />
              <el-option
                v-for="elderly in elderlyList"
                :key="elderly.id"
                :label="elderly.name"
                :value="elderly.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="事件类型">
            <el-select
              v-model="eventFilters.eventType"
              placeholder="选择事件类型"
              clearable
              style="width: 120px"
              @change="loadAllEvents"
            >
              <el-option label="全部" :value="null" />
              <el-option label="进入" value="enter" />
              <el-option label="离开" value="exit" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="eventFilters.dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              @change="loadAllEvents"
              style="width: 350px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadAllEvents">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="resetEventFilters">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 事件列表 -->
      <el-table
        v-loading="allEventsLoading"
        :data="allEventList"
        style="width: 100%"
        height="400"
      >
        <el-table-column prop="eventTime" label="事件时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.eventTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="elderlyName" label="老人姓名" width="120" />
        <el-table-column prop="fenceName" label="围栏名称" width="150" />
        <el-table-column prop="eventType" label="事件类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.eventType === 'enter' ? 'success' : 'danger'">
              {{ row.eventType === 'enter' ? '进入' : '离开' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="coordinates" label="位置坐标" width="200">
          <template #default="{ row }">
            {{ row.lat }}, {{ row.lon }}
          </template>
        </el-table-column>
        <el-table-column prop="macid" label="设备MAC" width="150" />
        <el-table-column prop="alertSent" label="告警状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.alertSent === 1 ? 'success' : 'warning'">
              {{ row.alertSent === 1 ? '已发送' : '未发送' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isRead" label="读取状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isRead === 1 ? 'info' : 'danger'">
              {{ row.isRead === 1 ? '已读' : '未读' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button
              v-if="row.isRead === 0"
              size="small"
              type="primary"
              @click="markEventAsRead(row)"
            >
              标记已读
            </el-button>
            <span v-else class="text-gray-400">已读</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container" style="margin-top: 20px;">
        <el-pagination
          v-model:current-page="eventCurrentPage"
          v-model:page-size="eventPageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="eventTotal"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleEventSizeChange"
          @current-change="handleEventCurrentChange"
        />
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAllEventsDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 轨迹查看对话框 -->
    <el-dialog
      v-model="showTrackDialog"
      title="老人轨迹查看"
      width="90%"
      :close-on-click-modal="false"
      top="5vh"
    >
      <TrackViewer 
        v-if="showTrackDialog && selectedElderlyForTrack"
        :elderly-id="selectedElderlyForTrack.elderlyId"
        :elderly-name="selectedElderlyForTrack.elderlyName"
      />
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showTrackDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Link, Location, List, Search } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils/date'
import * as geoFenceApi from '@/api/geoFence'
import * as elderlyApi from '@/api/elderlyProfile'
import MapPicker from '@/components/MapPicker.vue'
import TrackViewer from '@/components/TrackViewer.vue'

// 响应式数据
const loading = ref(false)
const saving = ref(false)
const eventsLoading = ref(false)
const allEventsLoading = ref(false)
const showCreateDialog = ref(false)
const showEventsDialog = ref(false)
const showAllEventsDialog = ref(false)
const showBindDialog = ref(false)
const showTrackDialog = ref(false)
const binding = ref(false)
const bindingsLoading = ref(false)
const editingFence = ref(null)
const gettingLocation = ref(false)
const selectedElderlyForTrack = ref(null)

// 分页数据
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 事件分页数据
const eventCurrentPage = ref(1)
const eventPageSize = ref(10)
const eventTotal = ref(0)

// 列表数据
const fenceList = ref([])
const elderlyList = ref([])
const eventList = ref([])
const allEventList = ref([])
const deviceBindings = ref([])

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

const bindForm = reactive({
  macid: '',
  elderlyId: null
})

// 事件筛选条件
const eventFilters = reactive({
  elderlyId: null,
  eventType: null,
  dateRange: null
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
const bindFormRef = ref()

// 设备绑定表单验证规则
const bindRules = {
  macid: [{ required: true, message: '请输入设备MAC地址', trigger: 'blur' }],
  elderlyId: [{ required: true, message: '请选择关联老人', trigger: 'change' }]
}

// 生命周期
onMounted(() => {
  loadFenceList()
  loadElderlyList()
  loadDeviceBindings()
  loadAllEvents()
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
    
    // 准备提交数据
    const submitData = { ...fenceForm }
    
    // 处理圆形围栏的坐标数据：将centerCoordinates拆分为centerLat和centerLon
    if (submitData.fenceType === 'circle' && submitData.centerCoordinates) {
      const coords = submitData.centerCoordinates.split(',')
      if (coords.length === 2) {
        submitData.centerLon = parseFloat(coords[0].trim())
        submitData.centerLat = parseFloat(coords[1].trim())
      }
      // 删除centerCoordinates字段，因为后端不需要这个字段
      delete submitData.centerCoordinates
    }
    
    // 处理多边形围栏的坐标数据
    if (submitData.fenceType === 'polygon' && submitData.polygonCoordinates) {
      submitData.coordinates = submitData.polygonCoordinates
      delete submitData.polygonCoordinates
    }
    
    const response = editingFence.value 
      ? await geoFenceApi.updateFence(editingFence.value.id, submitData)
      : await geoFenceApi.createFence(submitData)
    
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
  
  // 处理中心坐标：将centerLat和centerLon组合成centerCoordinates格式
  let centerCoordinates = ''
  if (fence.fenceType === 'circle' && fence.centerLat && fence.centerLon) {
    centerCoordinates = `${fence.centerLon},${fence.centerLat}`
  }
  
  Object.assign(fenceForm, {
    fenceName: fence.fenceName || '',
    elderlyId: fence.elderlyId || null,
    fenceType: fence.fenceType || 'circle',
    centerCoordinates: centerCoordinates,
    radius: fence.radius || 500,
    polygonCoordinates: fence.polygonCoordinates || fence.coordinates || '',
    alertType: fence.alertType || 'sms',
    emergencyContacts: fence.emergencyContacts || '',
    description: fence.description || ''
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

const viewElderlyTrack = (fence) => {
  // 获取老人姓名
  const elderly = elderlyList.value.find(e => e.id === fence.elderlyId)
  const elderlyName = elderly ? elderly.name : '未知老人'
  
  selectedElderlyForTrack.value = {
    elderlyId: fence.elderlyId,
    elderlyName: elderlyName
  }
  
  showTrackDialog.value = true
}

const resetForm = () => {
  // 重置编辑状态
  editingFence.value = null
  
  // 获取当前位置作为默认中心坐标
  let defaultCenterCoordinates = ''
  
  // 如果浏览器支持地理位置API，可以获取当前位置
  // 这里暂时使用默认值，实际项目中可以根据需求调整
  defaultCenterCoordinates = '114.12503,22.69937' // 默认经度,纬度
  
  // 重置表单数据
  Object.assign(fenceForm, {
    fenceName: '',
    elderlyId: null,
    fenceType: 'circle',
    centerCoordinates: defaultCenterCoordinates, // 使用默认中心坐标
    radius: 500, // 默认半径500米
    polygonCoordinates: '',
    alertType: 'sms',
    emergencyContacts: '',
    description: ''
  })
  
  // 清除表单验证
  if (fenceFormRef.value) {
    fenceFormRef.value.clearValidate()
  }
}

// 事件相关方法
const loadAllEvents = async () => {
  allEventsLoading.value = true
  try {
    const params = {
      page: eventCurrentPage.value,
      size: eventPageSize.value
    }
    
    // 添加筛选条件
    if (eventFilters.elderlyId) {
      params.elderlyId = eventFilters.elderlyId
    }
    if (eventFilters.eventType) {
      params.eventType = eventFilters.eventType
    }
    if (eventFilters.dateRange && eventFilters.dateRange.length === 2) {
      params.startTime = eventFilters.dateRange[0]
      params.endTime = eventFilters.dateRange[1]
    }
    
    const response = await geoFenceApi.getAllEvents(params)
    if (response.success) {
      allEventList.value = response.data.list || []
      eventTotal.value = response.data.total || 0
    }
  } catch (error) {
    console.error('加载事件列表失败:', error)
    ElMessage.error('加载事件列表失败')
  } finally {
    allEventsLoading.value = false
  }
}

const handleEventSizeChange = (val) => {
  eventPageSize.value = val
  eventCurrentPage.value = 1
  loadAllEvents()
}

const handleEventCurrentChange = (val) => {
  eventCurrentPage.value = val
  loadAllEvents()
}

const resetEventFilters = () => {
  Object.assign(eventFilters, {
    elderlyId: null,
    eventType: null,
    dateRange: null
  })
  eventCurrentPage.value = 1
  loadAllEvents()
}

const markEventAsRead = async (event) => {
  try {
    const response = await geoFenceApi.markEventAsRead(event.id)
    if (response.success) {
      event.isRead = 1
      event.readTime = new Date().toISOString()
      ElMessage.success('标记已读成功')
    } else {
      ElMessage.error(response.message || '标记已读失败')
    }
  } catch (error) {
    console.error('标记已读失败:', error)
    ElMessage.error('标记已读失败')
  }
}

// 设备绑定相关方法
const loadDeviceBindings = async () => {
  bindingsLoading.value = true
  try {
    const response = await geoFenceApi.getDeviceBindings()
    if (response.success) {
      deviceBindings.value = response.data || []
      // 处理每个绑定记录的数据
      deviceBindings.value.forEach(binding => {
        // 映射字段名
        binding.elderlyName = binding.elderly_name || '未知'
        binding.elderlyId = binding.elderly_id
        binding.bindTime = binding.bind_time ? formatDateTime(binding.bind_time) : '未知'
        binding.lastActiveTime = binding.last_active_time === '暂无数据' ? '暂无数据' : 
          (binding.last_active_time ? formatDateTime(binding.last_active_time) : '暂无数据')
      })
    }
  } catch (error) {
    console.error('加载设备绑定列表失败:', error)
    ElMessage.error('加载设备绑定列表失败')
  } finally {
    bindingsLoading.value = false
  }
}

const bindDevice = async () => {
  if (!bindFormRef.value) return
  
  try {
    await bindFormRef.value.validate()
    binding.value = true
    
    const response = await geoFenceApi.bindDevice({
      macid: bindForm.macid,
      elderlyId: bindForm.elderlyId
    })
    
    if (response.success) {
      ElMessage.success('设备绑定成功')
      resetBindForm()
      loadDeviceBindings()
    } else {
      ElMessage.error(response.message || '设备绑定失败')
    }
  } catch (error) {
    console.error('设备绑定失败:', error)
    ElMessage.error('设备绑定失败')
  } finally {
    binding.value = false
  }
}

const unbindDevice = async (binding) => {
  try {
    await ElMessageBox.confirm(
      `确定要解绑设备"${binding.macid}"吗？`,
      '确认解绑',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await geoFenceApi.unbindDevice(binding.macid)
    if (response.success) {
      ElMessage.success('设备解绑成功')
      loadDeviceBindings()
    } else {
      ElMessage.error(response.message || '设备解绑失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('设备解绑失败')
    }
  }
}

const resetBindForm = () => {
  Object.assign(bindForm, {
    macid: '',
    elderlyId: null
  })
  if (bindFormRef.value) {
    bindFormRef.value.clearValidate()
  }
}

// 处理坐标变化
const handleCoordinateChange = (coordinates) => {
  console.log('坐标已更新:', coordinates)
  // 坐标已通过v-model自动更新到fenceForm.centerCoordinates
  // 这里可以添加额外的处理逻辑，比如验证坐标格式
  if (coordinates) {
    ElMessage.success('坐标选择成功')
  }
}

// 获取当前位置
const getCurrentLocation = () => {
  if (!navigator.geolocation) {
    ElMessage.error('您的浏览器不支持地理位置获取')
    return
  }
  
  gettingLocation.value = true
  
  navigator.geolocation.getCurrentPosition(
    (position) => {
      const { longitude, latitude } = position.coords
      fenceForm.centerCoordinates = `${longitude.toFixed(6)},${latitude.toFixed(6)}`
      ElMessage.success('位置获取成功')
      gettingLocation.value = false
    },
    (error) => {
      console.error('获取位置失败:', error)
      let errorMessage = '获取位置失败'
      
      switch (error.code) {
        case error.PERMISSION_DENIED:
          errorMessage = '用户拒绝了位置请求'
          break
        case error.POSITION_UNAVAILABLE:
          errorMessage = '位置信息不可用'
          break
        case error.TIMEOUT:
          errorMessage = '获取位置超时'
          break
      }
      
      ElMessage.error(errorMessage)
      gettingLocation.value = false
    },
    {
      enableHighAccuracy: true,
      timeout: 10000,
      maximumAge: 60000
    }
  )
}
</script>

<script>
export default {
  components: {
    TrackViewer
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

.bind-form-card {
  margin-bottom: 20px;
}

.bind-list-card {
  margin-top: 20px;
}

:deep(.bind-form-card .el-card__header) {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

:deep(.bind-list-card .el-card__header) {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  line-height: 1.4;
}

.event-filters {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 6px;
  margin-bottom: 20px;
}

.text-gray-400 {
  color: #9ca3af;
}

.coordinate-input-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.coordinate-input-group .el-input {
  flex: 1;
}

@media (max-width: 768px) {
  .coordinate-input-group {
    flex-direction: column;
    align-items: stretch;
  }
  
  .coordinate-input-group .el-input {
    margin-right: 0 !important;
    margin-bottom: 10px;
  }
  
  .coordinate-input-group .el-button {
    margin-left: 0 !important;
    margin-bottom: 10px;
  }
}
</style>