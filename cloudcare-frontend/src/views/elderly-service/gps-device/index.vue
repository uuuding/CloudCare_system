<template>
  <div class="gps-device-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <h2>GPS设备管理</h2>
      <el-button type="primary" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        添加设备
      </el-button>
    </div>

    <!-- 搜索筛选 -->
    <div class="search-bar">
      <el-form :model="searchForm" inline>
        <el-form-item label="设备名称">
          <el-input v-model="searchForm.deviceName" placeholder="请输入设备名称" clearable />
        </el-form-item>
        <el-form-item label="IMEI号">
          <el-input v-model="searchForm.imei" placeholder="请输入IMEI号" clearable />
        </el-form-item>
        <el-form-item label="绑定老人">
          <el-select v-model="searchForm.elderlyId" placeholder="请选择老人" clearable>
            <el-option
              v-for="elderly in elderlyList"
              :key="elderly.id"
              :label="elderly.name"
              :value="elderly.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="设备状态">
          <el-select v-model="searchForm.deviceStatus" placeholder="请选择状态" clearable>
            <el-option label="在线" :value="1" />
            <el-option label="离线" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadDeviceList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 设备列表 -->
    <div class="device-list">
      <el-table :data="deviceList" v-loading="loading">
        <el-table-column prop="deviceName" label="设备名称" />
        <el-table-column prop="imei" label="IMEI号" />
        <el-table-column prop="elderlyName" label="绑定老人">
          <template #default="{ row }">
            <span v-if="row.elderlyName">{{ row.elderlyName }}</span>
            <el-text type="info" v-else>未绑定</el-text>
          </template>
        </el-table-column>
        <el-table-column prop="deviceStatus" label="设备状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getDeviceStatusColor(row.deviceStatus)">
              {{ getDeviceStatusText(row.deviceStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastGpsTime" label="最后GPS时间" width="180">
          <template #default="{ row }">
            <span v-if="row.lastGpsTime">{{ formatTime(row.lastGpsTime) }}</span>
            <el-text type="info" v-else>无数据</el-text>
          </template>
        </el-table-column>
        <el-table-column prop="lastHeartTime" label="最后心跳时间" width="180">
          <template #default="{ row }">
            <span v-if="row.lastHeartTime">{{ formatTime(row.lastHeartTime) }}</span>
            <el-text type="info" v-else>无数据</el-text>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="300">
          <template #default="{ row }">
            <el-button size="small" @click="viewDevice(row)">详情</el-button>
            <el-button size="small" type="primary" @click="editDevice(row)">编辑</el-button>
            <el-button 
              size="small" 
              type="success" 
              @click="bindDevice(row)"
              v-if="!row.elderlyId"
            >
              绑定
            </el-button>
            <el-button 
              size="small" 
              type="warning" 
              @click="unbindDevice(row)"
              v-else
            >
              解绑
            </el-button>
            <el-button size="small" type="info" @click="viewLocation(row)">定位</el-button>
            <el-button size="small" type="danger" @click="deleteDevice(row)">删除</el-button>
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
          @size-change="loadDeviceList"
          @current-change="loadDeviceList"
        />
      </div>
    </div>

    <!-- 添加/编辑设备对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="editingDevice ? '编辑设备' : '添加设备'"
      width="50%"
      :before-close="handleDialogClose"
    >
      <el-form :model="deviceForm" :rules="deviceRules" ref="deviceFormRef" label-width="120px">
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="deviceForm.deviceName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="IMEI号" prop="imei">
          <el-input 
            v-model="deviceForm.imei" 
            placeholder="请输入IMEI号" 
            :disabled="!!editingDevice"
          />
        </el-form-item>
        <el-form-item label="绑定老人">
          <el-select v-model="deviceForm.elderlyId" placeholder="请选择老人" clearable>
            <el-option
              v-for="elderly in elderlyList"
              :key="elderly.id"
              :label="elderly.name"
              :value="elderly.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="设备状态">
          <el-radio-group v-model="deviceForm.deviceStatus">
            <el-radio :label="1">在线</el-radio>
            <el-radio :label="0">离线</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="deviceForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="handleDialogClose">取消</el-button>
        <el-button type="primary" @click="saveDevice" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <!-- 设备详情对话框 -->
    <el-dialog v-model="showDetailDialog" title="设备详情" width="60%">
      <div v-if="currentDevice">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="设备名称">{{ currentDevice.deviceName }}</el-descriptions-item>
          <el-descriptions-item label="IMEI号">{{ currentDevice.imei }}</el-descriptions-item>
          <el-descriptions-item label="绑定老人">
            <span v-if="currentDevice.elderlyName">{{ currentDevice.elderlyName }}</span>
            <el-text type="info" v-else>未绑定</el-text>
          </el-descriptions-item>
          <el-descriptions-item label="设备状态">
            <el-tag :type="getDeviceStatusColor(currentDevice.deviceStatus)">
              {{ getDeviceStatusText(currentDevice.deviceStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="最后GPS时间">
            <span v-if="currentDevice.lastGpsTime">{{ formatTime(currentDevice.lastGpsTime) }}</span>
            <el-text type="info" v-else>无数据</el-text>
          </el-descriptions-item>
          <el-descriptions-item label="最后心跳时间">
            <span v-if="currentDevice.lastHeartTime">{{ formatTime(currentDevice.lastHeartTime) }}</span>
            <el-text type="info" v-else>无数据</el-text>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatTime(currentDevice.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ formatTime(currentDevice.updateTime) }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ currentDevice.remark || '无' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 绑定设备对话框 -->
    <el-dialog v-model="showBindDialog" title="绑定设备" width="40%">
      <el-form :model="bindForm" :rules="bindRules" ref="bindFormRef" label-width="100px">
        <el-form-item label="选择老人" prop="elderlyId">
          <el-select v-model="bindForm.elderlyId" placeholder="请选择老人">
            <el-option
              v-for="elderly in elderlyList"
              :key="elderly.id"
              :label="elderly.name"
              :value="elderly.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showBindDialog = false">取消</el-button>
        <el-button type="primary" @click="submitBind" :loading="binding">确认绑定</el-button>
      </template>
    </el-dialog>

    <!-- 位置查看对话框 -->
    <el-dialog v-model="showLocationDialog" title="设备位置" width="70%">
      <div v-if="currentDevice && deviceLocation">
        <div class="location-info-header">
          <el-descriptions :column="3" border>
            <el-descriptions-item label="设备名称">{{ currentDevice.deviceName }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ formatTime(deviceLocation.gpsTime) }}</el-descriptions-item>
            <el-descriptions-item label="定位状态">
              <el-tag :type="deviceLocation.signalType === 0 ? 'success' : 'warning'">
                {{ deviceLocation.signalType === 0 ? 'GPS定位' : 'LBS定位' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="经度">{{ deviceLocation.lon }}</el-descriptions-item>
            <el-descriptions-item label="纬度">{{ deviceLocation.lat }}</el-descriptions-item>
            <el-descriptions-item label="速度">{{ deviceLocation.speed }} km/h</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <!-- 位置地图 -->
        <div class="location-map-container" style="margin-top: 20px;">
          <div id="device-location-map" style="height: 400px; width: 100%;"></div>
        </div>
      </div>
      <div v-else-if="currentDevice">
        <el-empty description="暂无位置信息" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import api from '@/api'

// 响应式数据
const loading = ref(false)
const saving = ref(false)
const binding = ref(false)
const showAddDialog = ref(false)
const showDetailDialog = ref(false)
const showBindDialog = ref(false)
const showLocationDialog = ref(false)
const editingDevice = ref(null)
const currentDevice = ref(null)
const deviceLocation = ref(null)
const deviceFormRef = ref()
const bindFormRef = ref()

// 地图相关
let locationMap = null

// 搜索表单
const searchForm = reactive({
  deviceName: '',
  imei: '',
  elderlyId: null,
  deviceStatus: null
})

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 设备列表
const deviceList = ref([])
const elderlyList = ref([])

// 设备表单
const deviceForm = reactive({
  deviceName: '',
  imei: '',
  elderlyId: null,
  deviceStatus: 1,
  remark: ''
})

// 绑定表单
const bindForm = reactive({
  elderlyId: null
})

// 表单验证规则
const deviceRules = {
  deviceName: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
  imei: [
    { required: true, message: '请输入IMEI号', trigger: 'blur' },
    { pattern: /^\d{15}$/, message: 'IMEI号必须是15位数字', trigger: 'blur' }
  ]
}

const bindRules = {
  elderlyId: [{ required: true, message: '请选择老人', trigger: 'change' }]
}

// 初始化
onMounted(() => {
  loadElderlyList()
  loadDeviceList()
  loadBaiduMapScript()
})

// 加载百度地图脚本
const loadBaiduMapScript = () => {
  if (window.BMap) {
    return
  }
  
  const script = document.createElement('script')
  script.src = 'https://api.map.baidu.com/api?v=3.0&ak=YOUR_BAIDU_MAP_AK&callback=initBaiduMap'
  script.async = true
  document.head.appendChild(script)
  
  window.initBaiduMap = () => {
    console.log('百度地图API加载完成')
  }
}

// 初始化地图
const initMap = (containerId, lat, lon) => {
  if (!window.BMap) {
    ElMessage.warning('地图API未加载完成，请稍后重试')
    return
  }
  
  const mapInstance = new window.BMap.Map(containerId)
  const point = new window.BMap.Point(lon, lat)
  mapInstance.centerAndZoom(point, 16)
  mapInstance.enableScrollWheelZoom(true)
  
  // 添加标记
  const marker = new window.BMap.Marker(point)
  mapInstance.addOverlay(marker)
  
  // 添加信息窗口
  const infoWindow = new window.BMap.InfoWindow('设备位置')
  marker.addEventListener('click', () => {
    mapInstance.openInfoWindow(infoWindow, point)
  })
  
  return mapInstance
}

// 加载老人列表
const loadElderlyList = async () => {
  try {
    const response = await api.get('/api/elderly-profile/list', {
      params: { pageNum: 1, pageSize: 1000 }
    })
    if (response.data.success) {
      elderlyList.value = response.data.data.records || []
    }
  } catch (error) {
    console.error('加载老人列表失败:', error)
  }
}

// 加载设备列表
const loadDeviceList = async () => {
  loading.value = true
  try {
    const response = await api.get('/api/gps-device/list', {
      params: {
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize,
        ...searchForm
      }
    })
    if (response.data.success) {
      const data = response.data.data
      deviceList.value = data.records.map(device => ({
        ...device,
        elderlyName: elderlyList.value.find(e => e.id === device.elderlyId)?.name || null
      }))
      pagination.total = data.total
    }
  } catch (error) {
    ElMessage.error('加载设备列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.deviceName = ''
  searchForm.imei = ''
  searchForm.elderlyId = null
  searchForm.deviceStatus = null
  pagination.pageNum = 1
  loadDeviceList()
}

// 重置设备表单
const resetDeviceForm = () => {
  Object.assign(deviceForm, {
    deviceName: '',
    imei: '',
    elderlyId: null,
    deviceStatus: 1,
    remark: ''
  })
}

// 处理对话框关闭
const handleDialogClose = () => {
  showAddDialog.value = false
  editingDevice.value = null
  resetDeviceForm()
  if (deviceFormRef.value) {
    deviceFormRef.value.resetFields()
  }
}

// 查看设备详情
const viewDevice = (device) => {
  currentDevice.value = device
  showDetailDialog.value = true
}

// 编辑设备
const editDevice = (device) => {
  editingDevice.value = device
  Object.assign(deviceForm, device)
  showAddDialog.value = true
}

// 保存设备
const saveDevice = async () => {
  if (!deviceFormRef.value) return
  
  try {
    await deviceFormRef.value.validate()
    
    saving.value = true
    
    const url = editingDevice.value 
      ? `/api/gps-device/${editingDevice.value.id}`
      : '/api/gps-device'
    const method = editingDevice.value ? 'put' : 'post'
    
    const response = await api[method](url, deviceForm)
    
    if (response.data.success) {
      ElMessage.success(editingDevice.value ? '更新设备成功' : '添加设备成功')
      handleDialogClose()
      loadDeviceList()
    } else {
      ElMessage.error(response.data.message || '操作失败')
    }
  } catch (error) {
    console.error('保存设备失败:', error)
    ElMessage.error('保存设备失败')
  } finally {
    saving.value = false
  }
}

// 绑定设备
const bindDevice = (device) => {
  currentDevice.value = device
  bindForm.elderlyId = null
  showBindDialog.value = true
}

// 提交绑定
const submitBind = async () => {
  if (!bindFormRef.value) return
  
  try {
    await bindFormRef.value.validate()
    
    binding.value = true
    
    const response = await api.put(`/api/gps-device/${currentDevice.value.id}/bind`, null, {
      params: { elderlyId: bindForm.elderlyId }
    })
    
    if (response.data.success) {
      ElMessage.success('绑定成功')
      showBindDialog.value = false
      loadDeviceList()
    } else {
      ElMessage.error('绑定失败')
    }
  } catch (error) {
    ElMessage.error('绑定失败')
    console.error(error)
  } finally {
    binding.value = false
  }
}

// 解绑设备
const unbindDevice = async (device) => {
  try {
    await ElMessageBox.confirm('确定要解绑这个设备吗？', '确认解绑', {
      type: 'warning'
    })
    
    const response = await api.put(`/api/gps-device/${device.id}/unbind`)
    if (response.data.success) {
      ElMessage.success('解绑成功')
      loadDeviceList()
    } else {
      ElMessage.error('解绑失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('解绑失败')
    }
  }
}

// 查看位置
const viewLocation = async (device) => {
  currentDevice.value = device
  deviceLocation.value = null
  
  try {
    const response = await api.get(`/api/gps-device/${device.id}/location`)
    if (response.data.success && response.data.data) {
      deviceLocation.value = response.data.data
      showLocationDialog.value = true
      
      nextTick(() => {
        if (deviceLocation.value.lat && deviceLocation.value.lon) {
          locationMap = initMap('device-location-map', deviceLocation.value.lat, deviceLocation.value.lon)
        }
      })
    } else {
      showLocationDialog.value = true
    }
  } catch (error) {
    ElMessage.error('获取位置信息失败')
    console.error(error)
  }
}

// 删除设备
const deleteDevice = async (device) => {
  try {
    await ElMessageBox.confirm('确定要删除这个设备吗？', '确认删除', {
      type: 'warning'
    })
    
    const response = await api.delete(`/api/gps-device/${device.id}`)
    if (response.data.success) {
      ElMessage.success('删除成功')
      loadDeviceList()
    } else {
      ElMessage.error('删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 获取设备状态颜色
const getDeviceStatusColor = (status) => {
  return status === 1 ? 'success' : 'danger'
}

// 获取设备状态文本
const getDeviceStatusText = (status) => {
  return status === 1 ? '在线' : '离线'
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}
</script>

<style scoped>
.gps-device-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-bar {
  background: #f5f5f5;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.device-list {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.location-info-header {
  margin-bottom: 10px;
}

.location-map-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}
</style>