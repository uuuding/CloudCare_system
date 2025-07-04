<template>
  <div class="track-viewer">
    <!-- 控制面板 -->
    <div class="control-panel">
      <el-row :gutter="16">
        <el-col :span="6">
          <el-select 
            v-if="!props.elderlyId"
            v-model="selectedElderlyId" 
            placeholder="选择老人" 
            style="width: 100%" 
            @change="onElderlyChange"
          >
            <el-option
              v-for="elderly in elderlyList"
              :key="elderly.id"
              :label="elderly.name"
              :value="elderly.id"
            />
          </el-select>
          <div v-else class="elderly-name" style="line-height: 32px; font-weight: 500;">
            老人: {{ props.elderlyName || '未知老人' }}
          </div>
        </el-col>
        <el-col :span="5">
          <el-date-picker
            v-model="startTime"
            type="datetime"
            placeholder="开始时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-col>
        <el-col :span="5">
          <el-date-picker
            v-model="endTime"
            type="datetime"
            placeholder="结束时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="loadTrack" :loading="loading">
            <el-icon><Search /></el-icon>
            查询轨迹
          </el-button>
        </el-col>
        <el-col :span="3">
          <el-button @click="clearTrack">
            <el-icon><Delete /></el-icon>
            清除轨迹
          </el-button>
        </el-col>
        <el-col :span="3">
          <el-switch
            v-model="showFences"
            active-text="显示围栏"
            inactive-text="隐藏围栏"
            @change="onFenceToggle"
          />
        </el-col>
      </el-row>
      
      <!-- 轨迹信息 -->
      <div v-if="trackData.length > 0" class="track-info">
        <el-row :gutter="16">
          <el-col :span="6">
            <el-statistic title="轨迹点数" :value="trackData.length" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="时间跨度" :value="timeSpan" suffix="小时" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="最新位置" :value="latestLocationTime" />
          </el-col>
          <el-col :span="6">
            <el-button type="success" @click="playTrack" :disabled="isPlaying">
              <el-icon><VideoPlay /></el-icon>
              轨迹回放
            </el-button>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 地图容器 -->
    <div id="track-map" class="map-container"></div>

    <!-- 回放控制 -->
    <div v-if="isPlaying" class="playback-control">
      <el-row :gutter="16" align="middle">
        <el-col :span="2">
          <el-button @click="pausePlay" v-if="!isPaused">
            <el-icon><VideoPause /></el-icon>
          </el-button>
          <el-button @click="resumePlay" v-else>
            <el-icon><VideoPlay /></el-icon>
          </el-button>
        </el-col>
        <el-col :span="2">
          <el-button @click="stopPlay">
            <el-icon><Close /></el-icon>
          </el-button>
        </el-col>
        <el-col :span="16">
          <el-slider
            v-model="playProgress"
            :max="trackData.length - 1"
            @change="onProgressChange"
            :show-tooltip="false"
          />
        </el-col>
        <el-col :span="4">
          <span class="progress-text">
            {{ playProgress + 1 }} / {{ trackData.length }}
          </span>
        </el-col>
      </el-row>
      <div class="current-point-info" v-if="currentPoint">
        时间: {{ formatDateTime(currentPoint.gpsTime) }} | 
        位置: {{ currentPoint.mapLat || currentPoint.lat }}, {{ currentPoint.mapLon || currentPoint.lon }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Delete, VideoPlay, VideoPause, Close } from '@element-plus/icons-vue'
import { gpsApi } from '@/api/gps'
import * as elderlyApi from '@/api/elderlyProfile'
import { getFencesByElderlyId } from '@/api/geoFence'
import { loadAMapAPI } from '@/config/map'

// Props
const props = defineProps({
  elderlyId: {
    type: Number,
    required: false
  },
  elderlyName: {
    type: String,
    required: false
  }
})

// 响应式数据
const loading = ref(false)
const elderlyList = ref([])
const selectedElderlyId = ref(null)
const startTime = ref('')
const endTime = ref('')
const trackData = ref([])
const isPlaying = ref(false)
const isPaused = ref(false)
const playProgress = ref(0)
const currentPoint = ref(null)
const fenceData = ref([])
const showFences = ref(true)

// 地图相关
let map = null
let trackPolyline = null
let currentMarker = null
let playTimer = null
let fenceOverlays = []

// 计算属性
const timeSpan = computed(() => {
  if (trackData.value.length < 2) return 0
  const start = new Date(trackData.value[trackData.value.length - 1].gpsTime)
  const end = new Date(trackData.value[0].gpsTime)
  return ((end - start) / (1000 * 60 * 60)).toFixed(1)
})

const latestLocationTime = computed(() => {
  if (trackData.value.length === 0) return '无数据'
  return formatDateTime(trackData.value[0].gpsTime)
})

// 方法
const initMap = async () => {
  try {
    // 加载高德地图API
    await loadAMapAPI()
    
    // 初始化高德地图
    map = new AMap.Map('track-map', {
      zoom: 13,
      center: [114.125, 22.699], // 深圳坐标
      mapStyle: 'amap://styles/normal'
    })
    
    // 添加地图控件
    map.addControl(new AMap.Scale())
    map.addControl(new AMap.ToolBar())
  } catch (error) {
    console.error('地图初始化失败:', error)
    ElMessage.error('地图加载失败，请刷新页面重试')
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
    ElMessage.error('加载老人列表失败')
  }
}

const loadFences = async (elderlyId) => {
  try {
    const response = await getFencesByElderlyId(elderlyId)
    if (response.success) {
      fenceData.value = response.data || []
      drawFences()
    }
  } catch (error) {
    console.error('加载围栏数据失败:', error)
    ElMessage.error('加载围栏数据失败')
  }
}

const onElderlyChange = () => {
  clearTrack()
  // 清除围栏数据
  fenceData.value = []
  clearFences()
}

const loadTrack = async () => {
  if (!selectedElderlyId.value) {
    ElMessage.warning('请选择老人')
    return
  }
  
  if (!startTime.value || !endTime.value) {
    ElMessage.warning('请选择时间范围')
    return
  }
  
  loading.value = true
  try {
    const response = await gpsApi.getElderlyTrack(
      selectedElderlyId.value,
      startTime.value,
      endTime.value
    )
    
    if (response && response.data && Array.isArray(response.data)) {
      trackData.value = response.data
      console.log('轨迹数据:', trackData.value)
      
      if (trackData.value.length > 0) {
        // 检查数据中的坐标字段
        const samplePoint = trackData.value[0]
        console.log('示例数据点:', samplePoint)
        console.log('坐标字段检查:', {
          lat: samplePoint.lat,
          lon: samplePoint.lon,
          mapLat: samplePoint.mapLat,
          mapLon: samplePoint.mapLon
        })
        
        drawTrack()
        // 加载围栏数据
        await loadFences(selectedElderlyId.value)
        ElMessage.success(`加载了 ${trackData.value.length} 个轨迹点`)
      } else {
        ElMessage.info('该时间段内没有轨迹数据')
      }
    } else {
      console.log('响应数据格式:', response)
      ElMessage.error('轨迹数据格式错误')
    }
  } catch (error) {
    console.error('加载轨迹失败:', error)
    ElMessage.error('加载轨迹失败: ' + (error.message || '系统错误'))
  } finally {
    loading.value = false
  }
}

const drawTrack = () => {
  if (!map || trackData.value.length === 0) return
  
  // 清除之前的轨迹（但不清空trackData）
  clearMapOnly()
  
  // 构建轨迹点数组
  const path = trackData.value.map(point => {
    const lat = point.mapLat || point.lat
    const lon = point.mapLon || point.lon
    console.log('处理坐标点:', { lat, lon, original: point })
    return [lon, lat]
  }).filter(point => {
    const isValid = point[0] && point[1]
    if (!isValid) {
      console.log('过滤掉无效坐标:', point)
    }
    return isValid
  })
  
  console.log('有效轨迹点数量:', path.length, '总数据点:', trackData.value.length)
  
  if (path.length === 0) {
    ElMessage.warning('没有有效的坐标数据')
    return
  }
  
  // 绘制轨迹线
  trackPolyline = new AMap.Polyline({
    path: path,
    strokeColor: '#3366FF',
    strokeWeight: 4,
    strokeOpacity: 0.8
  })
  
  map.add(trackPolyline)
  
  // 添加起点和终点标记
  const startPoint = path[path.length - 1]
  const endPoint = path[0]
  
  const startMarker = new AMap.Marker({
    position: startPoint,
    icon: new AMap.Icon({
      size: new AMap.Size(25, 34),
      image: 'https://webapi.amap.com/theme/v1.3/markers/n/start.png'
    }),
    title: '起点'
  })
  
  const endMarker = new AMap.Marker({
    position: endPoint,
    icon: new AMap.Icon({
      size: new AMap.Size(25, 34),
      image: 'https://webapi.amap.com/theme/v1.3/markers/n/end.png'
    }),
    title: '终点'
  })
  
  map.add([startMarker, endMarker])
  
  // 调整地图视野
  map.setFitView([trackPolyline, startMarker, endMarker])
}

const clearTrack = () => {
  if (map) {
    map.clearMap()
  }
  trackData.value = []
  fenceData.value = []
  fenceOverlays = []
  stopPlay()
}

const clearMapOnly = () => {
  if (map) {
    map.clearMap()
  }
}

const drawFences = () => {
  if (!map || fenceData.value.length === 0 || !showFences.value) return
  
  // 清除之前的围栏
  clearFences()
  
  fenceData.value.forEach(fence => {
    if (fence.status !== 1) return // 只显示启用的围栏
    
    let overlay = null
    
    if (fence.fenceType === 'circle') {
      // 圆形围栏
      overlay = new AMap.Circle({
        center: [fence.centerLon, fence.centerLat],
        radius: fence.radius,
        strokeColor: '#FF6B6B',
        strokeWeight: 2,
        strokeOpacity: 0.8,
        fillColor: '#FF6B6B',
        fillOpacity: 0.2
      })
    } else if (fence.fenceType === 'polygon') {
      // 多边形围栏
      try {
        const coordinates = JSON.parse(fence.coordinates)
        const path = coordinates.map(coord => [coord.lng, coord.lat])
        overlay = new AMap.Polygon({
          path: path,
          strokeColor: '#4ECDC4',
          strokeWeight: 2,
          strokeOpacity: 0.8,
          fillColor: '#4ECDC4',
          fillOpacity: 0.2
        })
      } catch (error) {
        console.error('解析多边形坐标失败:', error)
      }
    }
    
    if (overlay) {
      map.add(overlay)
      fenceOverlays.push(overlay)
      
      // 添加围栏名称标签
      const marker = new AMap.Marker({
        position: fence.fenceType === 'circle' 
          ? [fence.centerLon, fence.centerLat]
          : [JSON.parse(fence.coordinates)[0].lng, JSON.parse(fence.coordinates)[0].lat],
        content: `<div style="background: rgba(0,0,0,0.7); color: white; padding: 4px 8px; border-radius: 4px; font-size: 12px;">${fence.fenceName}</div>`,
        offset: new AMap.Pixel(-20, -10)
      })
      map.add(marker)
      fenceOverlays.push(marker)
    }
  })
}

const clearFences = () => {
  if (fenceOverlays.length > 0) {
    map.remove(fenceOverlays)
    fenceOverlays = []
  }
}

const onFenceToggle = () => {
  if (showFences.value) {
    // 显示围栏
    if (selectedElderlyId.value) {
      loadFences(selectedElderlyId.value)
    }
  } else {
    // 隐藏围栏
    clearFences()
  }
}

const playTrack = () => {
  if (trackData.value.length === 0) return
  
  isPlaying.value = true
  isPaused.value = false
  playProgress.value = trackData.value.length - 1 // 从最早的点开始
  
  playTimer = setInterval(() => {
    if (!isPaused.value) {
      if (playProgress.value > 0) {
        playProgress.value--
        updateCurrentMarker()
      } else {
        stopPlay()
      }
    }
  }, 1000) // 每秒播放一个点
}

const pausePlay = () => {
  isPaused.value = true
}

const resumePlay = () => {
  isPaused.value = false
}

const stopPlay = () => {
  isPlaying.value = false
  isPaused.value = false
  if (playTimer) {
    clearInterval(playTimer)
    playTimer = null
  }
  if (currentMarker) {
    map.remove(currentMarker)
    currentMarker = null
  }
  currentPoint.value = null
}

const onProgressChange = (value) => {
  playProgress.value = value
  updateCurrentMarker()
}

const updateCurrentMarker = () => {
  const point = trackData.value[playProgress.value]
  if (!point) return
  
  currentPoint.value = point
  
  const lat = point.mapLat || point.lat
  const lon = point.mapLon || point.lon
  
  if (!lat || !lon) return
  
  // 移除之前的当前位置标记
  if (currentMarker) {
    map.remove(currentMarker)
  }
  
  // 添加新的当前位置标记
  currentMarker = new AMap.Marker({
    position: [lon, lat],
    icon: new AMap.Icon({
      size: new AMap.Size(20, 20),
      image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png'
    }),
    title: `当前位置: ${formatDateTime(point.gpsTime)}`
  })
  
  map.add(currentMarker)
  map.setCenter([lon, lat])
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 设置默认时间范围（最近24小时）
const setDefaultTimeRange = () => {
  const now = new Date()
  const yesterday = new Date(now.getTime() - 24 * 60 * 60 * 1000)
  
  endTime.value = now.toISOString().slice(0, 19).replace('T', ' ')
  startTime.value = yesterday.toISOString().slice(0, 19).replace('T', ' ')
}

// 生命周期
onMounted(async () => {
  await initMap()
  loadElderlyList()
  setDefaultTimeRange()
  
  // 如果传入了elderlyId，自动设置选中的老人并加载围栏
  if (props.elderlyId) {
    selectedElderlyId.value = props.elderlyId
    await loadFences(props.elderlyId)
  }
})

onUnmounted(() => {
  stopPlay()
  if (map) {
    map.destroy()
  }
})
</script>

<style scoped>
.track-viewer {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.control-panel {
  padding: 16px;
  background: #f5f5f5;
  border-radius: 4px;
  margin-bottom: 16px;
}

.track-info {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #e0e0e0;
}

.map-container {
  flex: 1;
  min-height: 400px;
  border-radius: 4px;
  overflow: hidden;
}

.playback-control {
  padding: 16px;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  margin-top: 16px;
}

.progress-text {
  font-size: 14px;
  color: #666;
}

.current-point-info {
  margin-top: 8px;
  font-size: 12px;
  color: #999;
}
</style>