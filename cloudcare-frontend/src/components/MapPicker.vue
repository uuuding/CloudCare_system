<template>
  <div class="map-picker">
    <!-- 地图选点按钮 -->
    <el-button 
      type="primary" 
      :icon="MapLocation" 
      @click="showMapDialog = true"
      :disabled="disabled"
    >
      {{ buttonText }}
    </el-button>
    
    <!-- 地图选点对话框 -->
    <el-dialog
      v-model="showMapDialog"
      title="地图选点"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <div class="map-container">
        <!-- 地图容器 -->
        <div id="amap-container" class="amap-container" v-loading="mapLoading" element-loading-text="地图加载中..."></div>
        
        <!-- 操作提示 -->
        <div class="map-tips">
          <el-alert
            title="操作提示"
            type="info"
            :closable="false"
            show-icon
          >
            <template #default>
              <p>• 点击地图任意位置选择坐标点</p>
              <p>• 可以拖拽标记点调整位置</p>
              <p>• 支持搜索地点名称快速定位</p>
              <p>• 当前选中坐标：{{ selectedCoordinates || '未选择' }}</p>
            </template>
          </el-alert>
        </div>
        
        <!-- 搜索框 -->
        <div class="search-container">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索地点（如：北京天安门）"
            @keyup.enter="searchLocation"
          >
            <template #append>
              <el-button @click="searchLocation" :loading="searching">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </div>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button 
            type="primary" 
            @click="confirmSelection"
            :disabled="!selectedCoordinates"
          >
            确定选择
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { MapLocation, Search } from '@element-plus/icons-vue'
import { 
  MAP_CONFIG, 
  loadAMapAPI, 
  validateCoordinates, 
  parseCoordinates, 
  formatCoordinates 
} from '@/config/map'

// Props
const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  buttonText: {
    type: String,
    default: '地图选点'
  },
  disabled: {
    type: Boolean,
    default: false
  },
  // 默认中心点坐标
  defaultCenter: {
    type: Array,
    default: () => MAP_CONFIG.DEFAULT_CENTER
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'change'])

// 响应式数据
const showMapDialog = ref(false)
const selectedCoordinates = ref('')
const searchKeyword = ref('')
const searching = ref(false)
const mapLoading = ref(false)
let map = null
let marker = null
let geocoder = null

// 监听modelValue变化
watch(() => props.modelValue, (newVal) => {
  selectedCoordinates.value = newVal
}, { immediate: true })

// 初始化地图
const initMap = async () => {
  mapLoading.value = true
  
  try {
    // 加载高德地图API
    await loadAMapAPI()
    
    // 创建地图实例
    map = new window.AMap.Map('amap-container', {
      zoom: MAP_CONFIG.DEFAULT_ZOOM,
      center: getInitialCenter(),
      mapStyle: MAP_CONFIG.MAP_STYLE
    })
    
    // 创建地理编码实例
    geocoder = new window.AMap.Geocoder(MAP_CONFIG.GEOCODER_CONFIG)
    
    // 如果有初始坐标，添加标记
    if (selectedCoordinates.value && validateCoordinates(selectedCoordinates.value)) {
      addMarker(parseCoordinates(selectedCoordinates.value))
    }
    
    // 地图点击事件
    map.on('click', (e) => {
      const { lng, lat } = e.lnglat
      const coordinates = [lng, lat]
      addMarker(coordinates)
      updateSelectedCoordinates(coordinates)
    })
    
    mapLoading.value = false
    
  } catch (error) {
    console.error('地图初始化失败:', error)
    mapLoading.value = false
    
    if (error.message.includes('API密钥')) {
      ElMessage.error('地图API密钥未配置，请联系管理员')
    } else {
      ElMessage.error('地图加载失败，请检查网络连接')
    }
  }
}

// 获取初始中心点
const getInitialCenter = () => {
  if (selectedCoordinates.value && validateCoordinates(selectedCoordinates.value)) {
    const coords = parseCoordinates(selectedCoordinates.value)
    return coords
  }
  return props.defaultCenter
}

// 添加标记
const addMarker = (coordinates) => {
  if (!map || !coordinates || !Array.isArray(coordinates) || coordinates.length !== 2) {
    return
  }
  
  // 移除旧标记
  if (marker) {
    map.remove(marker)
  }
  
  // 创建新标记
  marker = new window.AMap.Marker({
    position: coordinates,
    draggable: true,
    cursor: 'move'
  })
  
  // 标记拖拽事件
  marker.on('dragend', (e) => {
    const { lng, lat } = e.lnglat
    updateSelectedCoordinates([lng, lat])
  })
  
  map.add(marker)
  map.setCenter(coordinates)
}

// 更新选中坐标
const updateSelectedCoordinates = (coordinates) => {
  if (!coordinates || !Array.isArray(coordinates) || coordinates.length !== 2) {
    return
  }
  selectedCoordinates.value = formatCoordinates(coordinates)
}

// 搜索位置
const searchLocation = () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  
  if (!geocoder) {
    ElMessage.error('地理编码服务未初始化')
    return
  }
  
  searching.value = true
  
  geocoder.getLocation(searchKeyword.value, (status, result) => {
    searching.value = false
    
    if (status === 'complete' && result.geocodes.length > 0) {
      const location = result.geocodes[0].location
      const coordinates = [location.lng, location.lat]
      addMarker(coordinates)
      updateSelectedCoordinates(coordinates)
      ElMessage.success('搜索成功')
    } else {
      ElMessage.error('未找到相关位置，请尝试其他关键词')
    }
  })
}

// 确认选择
const confirmSelection = () => {
  if (!selectedCoordinates.value) {
    ElMessage.warning('请先选择一个位置')
    return
  }
  
  emit('update:modelValue', selectedCoordinates.value)
  emit('change', selectedCoordinates.value)
  showMapDialog.value = false
  ElMessage.success('坐标选择成功')
}

// 处理对话框关闭
const handleDialogClose = () => {
  showMapDialog.value = false
  // 重置搜索关键词
  searchKeyword.value = ''
}

// 监听对话框显示状态
watch(showMapDialog, async (newVal) => {
  if (newVal) {
    await nextTick()
    initMap()
  } else {
    // 清理地图实例
    if (map) {
      map.destroy()
      map = null
      marker = null
    }
  }
})
</script>

<style scoped>
.map-picker {
  display: inline-block;
}

.map-container {
  position: relative;
}

.amap-container {
  width: 100%;
  height: 400px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.map-tips {
  margin: 15px 0;
}

.search-container {
  margin-bottom: 15px;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-alert__content) {
  padding-left: 8px;
}

:deep(.el-alert__content p) {
  margin: 2px 0;
  font-size: 13px;
}
</style>