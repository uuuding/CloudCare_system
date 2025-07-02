<template>
  <div class="geo-fence-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <h2>电子围栏管理</h2>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        创建围栏
      </el-button>
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
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadFenceList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 围栏列表 -->
    <div class="fence-list">
      <el-table :data="fenceList" v-loading="loading">
        <el-table-column prop="fenceName" label="围栏名称" />
        <el-table-column prop="elderlyName" label="关联老人" />
        <el-table-column prop="fenceType" label="围栏类型">
          <template #default="{ row }">
            <el-tag :type="row.fenceType === 1 ? 'primary' : 'success'">
              {{ row.fenceType === 1 ? '圆形' : '多边形' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alarmType" label="报警类型">
          <template #default="{ row }">
            <el-tag :type="row.alarmType === 1 ? 'warning' : 'danger'">
              {{ row.alarmType === 1 ? '离开报警' : '进入报警' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="updateFenceStatus(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="viewFence(row)">查看</el-button>
            <el-button size="small" type="primary" @click="editFence(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteFence(row)">删除</el-button>
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
          @size-change="loadFenceList"
          @current-change="loadFenceList"
        />
      </div>
    </div>

    <!-- 创建/编辑围栏对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingFence ? '编辑围栏' : '创建围栏'"
      width="80%"
      :before-close="handleDialogClose"
    >
      <el-form :model="fenceForm" :rules="fenceRules" ref="fenceFormRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="围栏名称" prop="fenceName">
              <el-input v-model="fenceForm.fenceName" placeholder="请输入围栏名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联老人" prop="elderlyId">
              <el-select v-model="fenceForm.elderlyId" placeholder="请选择老人">
                <el-option
                  v-for="elderly in elderlyList"
                  :key="elderly.id"
                  :label="elderly.name"
                  :value="elderly.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="围栏类型" prop="fenceType">
              <el-radio-group v-model="fenceForm.fenceType">
                <el-radio :label="1">圆形围栏</el-radio>
                <el-radio :label="2">多边形围栏</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报警类型" prop="alarmType">
              <el-radio-group v-model="fenceForm.alarmType">
                <el-radio :label="1">离开报警</el-radio>
                <el-radio :label="2">进入报警</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效时间">
              <el-time-picker
                v-model="fenceForm.startTime"
                placeholder="开始时间"
                format="HH:mm:ss"
                value-format="HH:mm:ss"
              />
              <span style="margin: 0 10px;">至</span>
              <el-time-picker
                v-model="fenceForm.endTime"
                placeholder="结束时间"
                format="HH:mm:ss"
                value-format="HH:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="围栏状态">
              <el-switch v-model="fenceForm.status" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input
            v-model="fenceForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <!-- 地图区域 -->
      <div class="map-container">
        <div id="fence-map" style="height: 400px; width: 100%;"></div>
        <div class="map-tools">
          <el-button-group>
            <el-button :type="drawMode === 'circle' ? 'primary' : 'default'" @click="setDrawMode('circle')">
              绘制圆形
            </el-button>
            <el-button :type="drawMode === 'polygon' ? 'primary' : 'default'" @click="setDrawMode('polygon')">
              绘制多边形
            </el-button>
            <el-button @click="clearMap">清除</el-button>
          </el-button-group>
          <div v-if="fenceForm.fenceType === 1 && fenceForm.radius" class="radius-info">
            半径: {{ fenceForm.radius }}米
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="handleDialogClose">取消</el-button>
        <el-button type="primary" @click="saveFence" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <!-- 查看围栏对话框 -->
    <el-dialog v-model="showViewDialog" title="围栏详情" width="60%">
      <div v-if="viewingFence">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="围栏名称">{{ viewingFence.fenceName }}</el-descriptions-item>
          <el-descriptions-item label="关联老人">{{ viewingFence.elderlyName }}</el-descriptions-item>
          <el-descriptions-item label="围栏类型">
            <el-tag :type="viewingFence.fenceType === 1 ? 'primary' : 'success'">
              {{ viewingFence.fenceType === 1 ? '圆形' : '多边形' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="报警类型">
            <el-tag :type="viewingFence.alarmType === 1 ? 'warning' : 'danger'">
              {{ viewingFence.alarmType === 1 ? '离开报警' : '进入报警' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="viewingFence.status === 1 ? 'success' : 'danger'">
              {{ viewingFence.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="生效时间">
            {{ viewingFence.startTime || '全天' }} - {{ viewingFence.endTime || '全天' }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ viewingFence.createTime }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ viewingFence.updateTime }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ viewingFence.remark || '无' }}</el-descriptions-item>
        </el-descriptions>
        
        <!-- 查看地图 -->
        <div class="view-map-container" style="margin-top: 20px;">
          <div id="view-map" style="height: 300px; width: 100%;"></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import api from '@/api'

// 响应式数据
const loading = ref(false)
const saving = ref(false)
const showCreateDialog = ref(false)
const showViewDialog = ref(false)
const editingFence = ref(null)
const viewingFence = ref(null)
const fenceFormRef = ref()
const drawMode = ref('circle')

// 地图相关
let map = null
let viewMap = null
let drawingManager = null
let currentOverlay = null

// 搜索表单
const searchForm = reactive({
  elderlyId: null,
  status: null
})

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 围栏列表
const fenceList = ref([])
const elderlyList = ref([])

// 围栏表单
const fenceForm = reactive({
  fenceName: '',
  elderlyId: null,
  fenceType: 1,
  centerLat: null,
  centerLon: null,
  radius: null,
  polygonPoints: null,
  alarmType: 1,
  startTime: null,
  endTime: null,
  status: 1,
  remark: ''
})

// 表单验证规则
const fenceRules = {
  fenceName: [{ required: true, message: '请输入围栏名称', trigger: 'blur' }],
  elderlyId: [{ required: true, message: '请选择关联老人', trigger: 'change' }],
  fenceType: [{ required: true, message: '请选择围栏类型', trigger: 'change' }],
  alarmType: [{ required: true, message: '请选择报警类型', trigger: 'change' }]
}

// 初始化
onMounted(() => {
  loadElderlyList()
  loadFenceList()
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
const initMap = (containerId, isViewMode = false) => {
  if (!window.BMap) {
    ElMessage.warning('地图API未加载完成，请稍后重试')
    return
  }
  
  const mapInstance = new window.BMap.Map(containerId)
  const point = new window.BMap.Point(116.404, 39.915) // 默认北京坐标
  mapInstance.centerAndZoom(point, 15)
  mapInstance.enableScrollWheelZoom(true)
  
  if (!isViewMode) {
    // 添加绘制工具
    const drawingManagerLib = new window.BMapLib.DrawingManager(mapInstance, {
      isOpen: false,
      enableDrawingTool: true,
      drawingToolOptions: {
        anchor: window.BMAP_ANCHOR_TOP_RIGHT,
        offset: new window.BMap.Size(5, 5)
      },
      circleOptions: {
        strokeColor: '#ff0000',
        fillColor: '#ff0000',
        strokeWeight: 2,
        strokeOpacity: 0.8,
        fillOpacity: 0.3
      },
      polygonOptions: {
        strokeColor: '#ff0000',
        fillColor: '#ff0000',
        strokeWeight: 2,
        strokeOpacity: 0.8,
        fillOpacity: 0.3
      }
    })
    
    drawingManagerLib.addEventListener('overlaycomplete', (e) => {
      if (currentOverlay) {
        mapInstance.removeOverlay(currentOverlay)
      }
      currentOverlay = e.overlay
      
      if (e.drawingMode === window.BMAP_DRAWING_CIRCLE) {
        const center = e.overlay.getCenter()
        const radius = e.overlay.getRadius()
        fenceForm.fenceType = 1
        fenceForm.centerLat = center.lat
        fenceForm.centerLon = center.lng
        fenceForm.radius = Math.round(radius)
        fenceForm.polygonPoints = null
      } else if (e.drawingMode === window.BMAP_DRAWING_POLYGON) {
        const path = e.overlay.getPath()
        const points = path.map(point => ({ lat: point.lat, lon: point.lng }))
        fenceForm.fenceType = 2
        fenceForm.polygonPoints = JSON.stringify(points)
        fenceForm.centerLat = null
        fenceForm.centerLon = null
        fenceForm.radius = null
      }
    })
    
    drawingManager = drawingManagerLib
  }
  
  return mapInstance
}

// 设置绘制模式
const setDrawMode = (mode) => {
  if (!drawingManager) return
  
  drawMode.value = mode
  if (mode === 'circle') {
    drawingManager.setDrawingMode(window.BMAP_DRAWING_CIRCLE)
  } else if (mode === 'polygon') {
    drawingManager.setDrawingMode(window.BMAP_DRAWING_POLYGON)
  }
  drawingManager.open()
}

// 清除地图
const clearMap = () => {
  if (currentOverlay && map) {
    map.removeOverlay(currentOverlay)
    currentOverlay = null
  }
  resetFenceForm()
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

// 加载围栏列表
const loadFenceList = async () => {
  loading.value = true
  try {
    const response = await api.get('/api/geo-fence/list', {
      params: {
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize,
        ...searchForm
      }
    })
    if (response.data.success) {
      const data = response.data.data
      fenceList.value = data.records.map(fence => ({
        ...fence,
        elderlyName: elderlyList.value.find(e => e.id === fence.elderlyId)?.name || '未知'
      }))
      pagination.total = data.total
    }
  } catch (error) {
    ElMessage.error('加载围栏列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.elderlyId = null
  searchForm.status = null
  pagination.pageNum = 1
  loadFenceList()
}

// 重置围栏表单
const resetFenceForm = () => {
  Object.assign(fenceForm, {
    fenceName: '',
    elderlyId: null,
    fenceType: 1,
    centerLat: null,
    centerLon: null,
    radius: null,
    polygonPoints: null,
    alarmType: 1,
    startTime: null,
    endTime: null,
    status: 1,
    remark: ''
  })
}

// 处理对话框关闭
const handleDialogClose = () => {
  showCreateDialog.value = false
  editingFence.value = null
  resetFenceForm()
  if (fenceFormRef.value) {
    fenceFormRef.value.resetFields()
  }
}

// 编辑围栏
const editFence = (fence) => {
  editingFence.value = fence
  Object.assign(fenceForm, fence)
  showCreateDialog.value = true
  
  nextTick(() => {
    map = initMap('fence-map')
    // 在地图上显示现有围栏
    if (fence.fenceType === 1 && fence.centerLat && fence.centerLon) {
      const center = new window.BMap.Point(fence.centerLon, fence.centerLat)
      const circle = new window.BMap.Circle(center, fence.radius, {
        strokeColor: '#ff0000',
        fillColor: '#ff0000',
        strokeWeight: 2,
        strokeOpacity: 0.8,
        fillOpacity: 0.3
      })
      map.addOverlay(circle)
      map.centerAndZoom(center, 15)
      currentOverlay = circle
    } else if (fence.fenceType === 2 && fence.polygonPoints) {
      const points = JSON.parse(fence.polygonPoints)
      const polygon = new window.BMap.Polygon(
        points.map(p => new window.BMap.Point(p.lon, p.lat)),
        {
          strokeColor: '#ff0000',
          fillColor: '#ff0000',
          strokeWeight: 2,
          strokeOpacity: 0.8,
          fillOpacity: 0.3
        }
      )
      map.addOverlay(polygon)
      map.setViewport(polygon.getPath())
      currentOverlay = polygon
    }
  })
}

// 查看围栏
const viewFence = (fence) => {
  viewingFence.value = fence
  showViewDialog.value = true
  
  nextTick(() => {
    viewMap = initMap('view-map', true)
    // 在地图上显示围栏
    if (fence.fenceType === 1 && fence.centerLat && fence.centerLon) {
      const center = new window.BMap.Point(fence.centerLon, fence.centerLat)
      const circle = new window.BMap.Circle(center, fence.radius, {
        strokeColor: '#ff0000',
        fillColor: '#ff0000',
        strokeWeight: 2,
        strokeOpacity: 0.8,
        fillOpacity: 0.3
      })
      viewMap.addOverlay(circle)
      viewMap.centerAndZoom(center, 15)
    } else if (fence.fenceType === 2 && fence.polygonPoints) {
      const points = JSON.parse(fence.polygonPoints)
      const polygon = new window.BMap.Polygon(
        points.map(p => new window.BMap.Point(p.lon, p.lat)),
        {
          strokeColor: '#ff0000',
          fillColor: '#ff0000',
          strokeWeight: 2,
          strokeOpacity: 0.8,
          fillOpacity: 0.3
        }
      )
      viewMap.addOverlay(polygon)
      viewMap.setViewport(polygon.getPath())
    }
  })
}

// 保存围栏
const saveFence = async () => {
  if (!fenceFormRef.value) return
  
  try {
    await fenceFormRef.value.validate()
    
    // 验证围栏数据
    if (fenceForm.fenceType === 1 && (!fenceForm.centerLat || !fenceForm.centerLon || !fenceForm.radius)) {
      ElMessage.warning('请在地图上绘制圆形围栏')
      return
    }
    if (fenceForm.fenceType === 2 && !fenceForm.polygonPoints) {
      ElMessage.warning('请在地图上绘制多边形围栏')
      return
    }
    
    saving.value = true
    
    const url = editingFence.value 
      ? `/api/geo-fence/${editingFence.value.id}`
      : '/api/geo-fence'
    const method = editingFence.value ? 'put' : 'post'
    
    const response = await api[method](url, fenceForm)
    
    if (response.data.success) {
      ElMessage.success(editingFence.value ? '更新围栏成功' : '创建围栏成功')
      handleDialogClose()
      loadFenceList()
    } else {
      ElMessage.error(response.data.message || '操作失败')
    }
  } catch (error) {
    console.error('保存围栏失败:', error)
    ElMessage.error('保存围栏失败')
  } finally {
    saving.value = false
  }
}

// 更新围栏状态
const updateFenceStatus = async (fence) => {
  try {
    const response = await api.put(`/api/geo-fence/${fence.id}/status`, null, {
      params: { status: fence.status }
    })
    if (response.data.success) {
      ElMessage.success('更新状态成功')
    } else {
      ElMessage.error('更新状态失败')
      fence.status = fence.status === 1 ? 0 : 1 // 回滚状态
    }
  } catch (error) {
    ElMessage.error('更新状态失败')
    fence.status = fence.status === 1 ? 0 : 1 // 回滚状态
  }
}

// 删除围栏
const deleteFence = async (fence) => {
  try {
    await ElMessageBox.confirm('确定要删除这个围栏吗？', '确认删除', {
      type: 'warning'
    })
    
    const response = await api.delete(`/api/geo-fence/${fence.id}`)
    if (response.data.success) {
      ElMessage.success('删除成功')
      loadFenceList()
    } else {
      ElMessage.error('删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 监听对话框打开，初始化地图
const watchDialogOpen = () => {
  if (showCreateDialog.value && !editingFence.value) {
    nextTick(() => {
      map = initMap('fence-map')
    })
  }
}

// 监听创建对话框
watch(() => showCreateDialog.value, (newVal) => {
  if (newVal && !editingFence.value) {
    nextTick(() => {
      map = initMap('fence-map')
    })
  }
})
</script>

<style scoped>
.geo-fence-container {
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

.fence-list {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.map-container {
  margin-top: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.map-tools {
  padding: 10px;
  background: #f5f5f5;
  border-top: 1px solid #dcdfe6;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.radius-info {
  font-size: 14px;
  color: #606266;
}

.view-map-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}
</style>