<template>
  <div class="gps-alarm-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <h2>GPS报警监控</h2>
      <div class="header-stats">
        <el-statistic title="未处理报警" :value="unhandledCount" class="stat-item" />
        <el-statistic title="今日报警" :value="todayCount" class="stat-item" />
      </div>
    </div>

    <!-- 搜索筛选 -->
    <div class="search-bar">
      <el-form :model="searchForm" inline>
        <el-form-item label="老人">
          <el-select v-model="searchForm.elderlyId" placeholder="请选择老人" clearable>
            <el-option
              v-for="elderly in elderlyList"
              :key="elderly.id"
              :label="elderly.name"
              :value="elderly.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="报警类型">
          <el-select v-model="searchForm.alarmType" placeholder="请选择报警类型" clearable>
            <el-option label="离开围栏" :value="1" />
            <el-option label="进入围栏" :value="2" />
            <el-option label="低电量" :value="3" />
            <el-option label="SOS求救" :value="4" />
            <el-option label="设备离线" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="searchForm.handleStatus" placeholder="请选择处理状态" clearable>
            <el-option label="未处理" :value="0" />
            <el-option label="已处理" :value="1" />
          </el-select>
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
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadAlarmList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="autoRefresh = !autoRefresh">
            {{ autoRefresh ? '停止自动刷新' : '开启自动刷新' }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 报警列表 -->
    <div class="alarm-list">
      <el-table :data="alarmList" v-loading="loading" @row-click="viewAlarmDetail">
        <el-table-column prop="alarmTime" label="报警时间" width="180">
          <template #default="{ row }">
            <el-text :type="getAlarmTimeType(row.alarmTime)">{{ formatTime(row.alarmTime) }}</el-text>
          </template>
        </el-table-column>
        <el-table-column prop="elderlyName" label="老人" width="100" />
        <el-table-column prop="deviceName" label="设备" width="120" />
        <el-table-column prop="alarmType" label="报警类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getAlarmTypeColor(row.alarmType)">
              {{ getAlarmTypeName(row.alarmType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="报警描述" show-overflow-tooltip />
        <el-table-column prop="location" label="位置" width="200">
          <template #default="{ row }">
            <div class="location-info">
              <div>纬度: {{ row.lat }}</div>
              <div>经度: {{ row.lon }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="handleStatus" label="处理状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.handleStatus === 1 ? 'success' : 'danger'">
              {{ row.handleStatus === 1 ? '已处理' : '未处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click.stop="viewAlarmDetail(row)">详情</el-button>
            <el-button 
              size="small" 
              type="primary" 
              @click.stop="handleAlarm(row)"
              v-if="row.handleStatus === 0"
            >
              处理
            </el-button>
            <el-button size="small" type="success" @click.stop="viewLocation(row)">定位</el-button>
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
          @size-change="loadAlarmList"
          @current-change="loadAlarmList"
        />
      </div>
    </div>

    <!-- 报警详情对话框 -->
    <el-dialog v-model="showDetailDialog" title="报警详情" width="60%">
      <div v-if="currentAlarm">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="报警时间">{{ formatTime(currentAlarm.alarmTime) }}</el-descriptions-item>
          <el-descriptions-item label="老人姓名">{{ currentAlarm.elderlyName }}</el-descriptions-item>
          <el-descriptions-item label="设备名称">{{ currentAlarm.deviceName }}</el-descriptions-item>
          <el-descriptions-item label="设备IMEI">{{ currentAlarm.imei }}</el-descriptions-item>
          <el-descriptions-item label="报警类型">
            <el-tag :type="getAlarmTypeColor(currentAlarm.alarmType)">
              {{ getAlarmTypeName(currentAlarm.alarmType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处理状态">
            <el-tag :type="currentAlarm.handleStatus === 1 ? 'success' : 'danger'">
              {{ currentAlarm.handleStatus === 1 ? '已处理' : '未处理' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="经度">{{ currentAlarm.lon }}</el-descriptions-item>
          <el-descriptions-item label="纬度">{{ currentAlarm.lat }}</el-descriptions-item>
          <el-descriptions-item label="速度">{{ currentAlarm.speed }} km/h</el-descriptions-item>
          <el-descriptions-item label="方向">{{ currentAlarm.direction }}°</el-descriptions-item>
          <el-descriptions-item label="报警描述" :span="2">{{ currentAlarm.description }}</el-descriptions-item>
          <el-descriptions-item label="处理人" v-if="currentAlarm.handleStatus === 1">
            {{ currentAlarm.handlePerson }}
          </el-descriptions-item>
          <el-descriptions-item label="处理时间" v-if="currentAlarm.handleStatus === 1">
            {{ formatTime(currentAlarm.handleTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ currentAlarm.remark || '无' }}</el-descriptions-item>
        </el-descriptions>
        
        <!-- 位置地图 -->
        <div class="detail-map-container" style="margin-top: 20px;">
          <div id="detail-map" style="height: 300px; width: 100%;"></div>
        </div>
      </div>
    </el-dialog>

    <!-- 处理报警对话框 -->
    <el-dialog v-model="showHandleDialog" title="处理报警" width="40%">
      <el-form :model="handleForm" :rules="handleRules" ref="handleFormRef" label-width="100px">
        <el-form-item label="处理结果" prop="handleResult">
          <el-radio-group v-model="handleForm.handleResult">
            <el-radio label="已联系家属">已联系家属</el-radio>
            <el-radio label="已现场处理">已现场处理</el-radio>
            <el-radio label="误报">误报</el-radio>
            <el-radio label="其他">其他</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理说明" prop="handleRemark">
          <el-input
            v-model="handleForm.handleRemark"
            type="textarea"
            :rows="4"
            placeholder="请输入处理说明"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showHandleDialog = false">取消</el-button>
        <el-button type="primary" @click="submitHandle" :loading="handling">确认处理</el-button>
      </template>
    </el-dialog>

    <!-- 位置查看对话框 -->
    <el-dialog v-model="showLocationDialog" title="位置信息" width="70%">
      <div v-if="currentAlarm">
        <div class="location-info-header">
          <el-descriptions :column="3" border>
            <el-descriptions-item label="报警时间">{{ formatTime(currentAlarm.alarmTime) }}</el-descriptions-item>
            <el-descriptions-item label="经度">{{ currentAlarm.lon }}</el-descriptions-item>
            <el-descriptions-item label="纬度">{{ currentAlarm.lat }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <!-- 位置地图 -->
        <div class="location-map-container" style="margin-top: 20px;">
          <div id="location-map" style="height: 400px; width: 100%;"></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

// 响应式数据
const loading = ref(false)
const handling = ref(false)
const autoRefresh = ref(false)
const showDetailDialog = ref(false)
const showHandleDialog = ref(false)
const showLocationDialog = ref(false)
const currentAlarm = ref(null)
const handleFormRef = ref()

// 统计数据
const unhandledCount = ref(0)
const todayCount = ref(0)

// 地图相关
let detailMap = null
let locationMap = null
let refreshTimer = null

// 搜索表单
const searchForm = reactive({
  elderlyId: null,
  alarmType: null,
  handleStatus: null,
  dateRange: null
})

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 报警列表
const alarmList = ref([])
const elderlyList = ref([])

// 处理表单
const handleForm = reactive({
  handleResult: '',
  handleRemark: ''
})

// 处理表单验证规则
const handleRules = {
  handleResult: [{ required: true, message: '请选择处理结果', trigger: 'change' }],
  handleRemark: [{ required: true, message: '请输入处理说明', trigger: 'blur' }]
}

// 初始化
onMounted(() => {
  loadElderlyList()
  loadAlarmList()
  loadStatistics()
  loadBaiduMapScript()
  startAutoRefresh()
})

// 组件卸载时清理定时器
onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
  }
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
  const infoWindow = new window.BMap.InfoWindow('报警位置')
  marker.addEventListener('click', () => {
    mapInstance.openInfoWindow(infoWindow, point)
  })
  
  return mapInstance
}

// 开始自动刷新
const startAutoRefresh = () => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
  }
  
  refreshTimer = setInterval(() => {
    if (autoRefresh.value) {
      loadAlarmList(false) // 静默刷新
      loadStatistics()
    }
  }, 30000) // 30秒刷新一次
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

// 加载报警列表
const loadAlarmList = async (showLoading = true) => {
  if (showLoading) {
    loading.value = true
  }
  
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    
    // 处理时间范围
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startTime = searchForm.dateRange[0]
      params.endTime = searchForm.dateRange[1]
    }
    
    const response = await api.get('/api/gps-alarm/list', { params })
    if (response.data.success) {
      const data = response.data.data
      alarmList.value = data.records.map(alarm => ({
        ...alarm,
        elderlyName: elderlyList.value.find(e => e.id === alarm.elderlyId)?.name || '未知'
      }))
      pagination.total = data.total
    }
  } catch (error) {
    if (showLoading) {
      ElMessage.error('加载报警列表失败')
    }
    console.error(error)
  } finally {
    if (showLoading) {
      loading.value = false
    }
  }
}

// 加载统计数据
const loadStatistics = async () => {
  try {
    const response = await api.get('/api/gps-alarm/statistics')
    if (response.data.success) {
      const data = response.data.data
      unhandledCount.value = data.unhandledCount || 0
      todayCount.value = data.todayCount || 0
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.elderlyId = null
  searchForm.alarmType = null
  searchForm.handleStatus = null
  searchForm.dateRange = null
  pagination.pageNum = 1
  loadAlarmList()
}

// 查看报警详情
const viewAlarmDetail = (alarm) => {
  currentAlarm.value = alarm
  showDetailDialog.value = true
  
  nextTick(() => {
    if (alarm.lat && alarm.lon) {
      detailMap = initMap('detail-map', alarm.lat, alarm.lon)
    }
  })
}

// 处理报警
const handleAlarm = (alarm) => {
  currentAlarm.value = alarm
  handleForm.handleResult = ''
  handleForm.handleRemark = ''
  showHandleDialog.value = true
}

// 提交处理
const submitHandle = async () => {
  if (!handleFormRef.value) return
  
  try {
    await handleFormRef.value.validate()
    
    handling.value = true
    
    const response = await api.put(`/api/gps-alarm/${currentAlarm.value.id}/handle`, {
      handleResult: handleForm.handleResult,
      handleRemark: handleForm.handleRemark
    })
    
    if (response.data.success) {
      ElMessage.success('处理成功')
      showHandleDialog.value = false
      loadAlarmList()
      loadStatistics()
    } else {
      ElMessage.error('处理失败')
    }
  } catch (error) {
    ElMessage.error('处理失败')
    console.error(error)
  } finally {
    handling.value = false
  }
}

// 查看位置
const viewLocation = (alarm) => {
  currentAlarm.value = alarm
  showLocationDialog.value = true
  
  nextTick(() => {
    if (alarm.lat && alarm.lon) {
      locationMap = initMap('location-map', alarm.lat, alarm.lon)
    }
  })
}

// 获取报警类型名称
const getAlarmTypeName = (type) => {
  const typeMap = {
    1: '离开围栏',
    2: '进入围栏',
    3: '低电量',
    4: 'SOS求救',
    5: '设备离线'
  }
  return typeMap[type] || '未知'
}

// 获取报警类型颜色
const getAlarmTypeColor = (type) => {
  const colorMap = {
    1: 'warning',
    2: 'danger',
    3: 'info',
    4: 'danger',
    5: 'warning'
  }
  return colorMap[type] || 'default'
}

// 获取报警时间类型
const getAlarmTimeType = (alarmTime) => {
  const now = new Date()
  const alarm = new Date(alarmTime)
  const diff = now - alarm
  
  if (diff < 5 * 60 * 1000) { // 5分钟内
    return 'danger'
  } else if (diff < 30 * 60 * 1000) { // 30分钟内
    return 'warning'
  } else {
    return 'info'
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}
</script>

<style scoped>
.gps-alarm-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-stats {
  display: flex;
  gap: 40px;
}

.stat-item {
  text-align: center;
}

.search-bar {
  background: #f5f5f5;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.alarm-list {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.location-info {
  font-size: 12px;
  color: #666;
}

.detail-map-container,
.location-map-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.location-info-header {
  margin-bottom: 10px;
}

/* 报警行样式 */
.el-table__row {
  cursor: pointer;
}

.el-table__row:hover {
  background-color: #f5f7fa;
}
</style>