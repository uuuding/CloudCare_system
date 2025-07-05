<template>
  <div class="data-screen">
    <!-- 背景动画 -->
    <div class="bg-animation">
      <div class="particle" v-for="i in 80" :key="i" :style="getParticleStyle(i)"></div>
      <div class="grid-lines">
        <div class="grid-line horizontal" v-for="i in 20" :key="'h'+i" :style="getGridLineStyle('horizontal', i)"></div>
        <div class="grid-line vertical" v-for="i in 30" :key="'v'+i" :style="getGridLineStyle('vertical', i)"></div>
      </div>
      <div class="energy-waves">
        <div class="wave" v-for="i in 3" :key="'wave'+i" :style="getWaveStyle(i)"></div>
      </div>
      
      <!-- 流动装饰条 -->
      <div class="flowing-bars">
        <div class="flow-bar" v-for="i in 8" :key="'bar'+i" :style="getFlowBarStyle(i)"></div>
      </div>
      
      <!-- 光效轨迹 -->
      <div class="light-trails">
        <div class="trail" v-for="i in 6" :key="'trail'+i" :style="getTrailStyle(i)"></div>
      </div>
      
      <!-- 动态光圈 -->
      <div class="dynamic-rings">
        <div class="ring" v-for="i in 4" :key="'ring'+i" :style="getRingStyle(i)"></div>
      </div>
      
      <!-- 数据流线 -->
      <div class="data-streams">
        <div class="stream" v-for="i in 12" :key="'stream'+i" :style="getStreamStyle(i)"></div>
      </div>
      
      <!-- 边框流光 -->
      <div class="border-lights">
        <div class="border-light top"></div>
        <div class="border-light right"></div>
        <div class="border-light bottom"></div>
        <div class="border-light left"></div>
      </div>
      
      <!-- 悬浮光点 -->
      <div class="floating-dots">
        <div class="floating-dot" v-for="i in 15" :key="'dot'+i" :style="getFloatingDotStyle(i)"></div>
      </div>
      
      <!-- 数据脉冲 -->
      <div class="data-pulses">
        <div class="pulse-ring" v-for="i in 5" :key="'pulse'+i" :style="getPulseStyle(i)"></div>
      </div>
      
      <!-- 扫描线 -->
      <div class="scan-lines">
        <div class="scan-line horizontal-scan"></div>
        <div class="scan-line vertical-scan"></div>
      </div>
    </div>
    
    <!-- 顶部标题栏 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="logo">
          <div class="logo-icon"></div>
          <span class="logo-text">CloudCare 智慧医养数据中心</span>
        </div>
      </div>
      <div class="header-center">
        <div class="time-display">
          <div class="current-time">{{ currentTime }}</div>
          <div class="current-date">{{ currentDate }}</div>
        </div>

      </div>
      <div class="header-right">
        <el-button type="primary" @click="goBack" class="back-btn">
          <el-icon><ArrowLeft /></el-icon>
          返回系统
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="screen-content">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-overlay">
        <div class="loading-spinner">
          <i class="fas fa-spinner fa-spin"></i>
          <span>正在加载数据...</span>
        </div>
      </div>
      
      <!-- 错误提示 -->
      <div v-if="error" class="error-overlay">
        <div class="error-message">
          <i class="fas fa-exclamation-triangle"></i>
          <span>{{ error }}</span>
          <button @click="fetchAllData" class="retry-btn">
            <i class="fas fa-redo"></i>
            重试
          </button>
        </div>
      </div>
      
      <!-- 第一行：核心指标 -->
      <div class="metrics-row" v-show="!loading">
        <div class="metric-card" v-for="metric in coreMetrics" :key="metric.key">
          <div class="metric-icon" :class="metric.iconClass">
            <i :class="metric.icon"></i>
          </div>
          <div class="metric-content">
            <div class="metric-value" :data-target="metric.value">
              <span class="animated-number">{{ animatedMetrics[metric.key] || 0 }}</span>
            </div>
            <div class="metric-label">{{ metric.label }}</div>
            <div class="metric-trend" :class="metric.trendClass">
              <i :class="metric.trendIcon"></i>
              {{ metric.trend }}
            </div>
          </div>
          <div class="metric-bg-effect"></div>
        </div>
      </div>

      <!-- 第二行：图表区域 -->
      <div class="charts-row">
        <!-- 健康状态分布 -->
        <div class="chart-container">
          <div class="chart-header">
            <h3>健康状态实时监控</h3>
            <div class="chart-controls">
              <span class="status-indicator online"></span>
              <span>实时更新</span>
              <div class="data-flow">
                <div class="flow-dot" v-for="i in 5" :key="i" :style="getFlowDotStyle(i)"></div>
              </div>
            </div>
          </div>
          <div class="chart-content">
            <div id="healthStatusChart" class="chart"></div>
            <div class="chart-legend">
              <div class="legend-item" v-for="item in healthLegend" :key="item.name">
                <span class="legend-color" :style="{backgroundColor: item.color}"></span>
                <span class="legend-text">{{ item.name }}</span>
                <span class="legend-value">{{ item.value }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 地理分布 -->
        <div class="chart-container">
          <div class="chart-header">
            <h3>地理分布热力图</h3>
          </div>
          <div class="chart-content">
            <div id="geoChart" class="chart"></div>
          </div>
        </div>

        <!-- 趋势分析 -->
        <div class="chart-container">
          <div class="chart-header">
            <h3>健康趋势分析</h3>
            <div class="chart-controls">
              <div class="time-tabs">
                <span class="time-tab" :class="{active: activeTab === '7d'}" @click="handleTimeRangeChange('7d')">7天</span>
                <span class="time-tab" :class="{active: activeTab === '30d'}" @click="handleTimeRangeChange('30d')">30天</span>
                <span class="time-tab" :class="{active: activeTab === '90d'}" @click="handleTimeRangeChange('90d')">90天</span>
              </div>
            </div>
          </div>
          <div class="chart-content">
            <div id="trendChart" class="chart"></div>
          </div>
        </div>
      </div>

      <!-- 第三行：详细数据 -->
      <div class="details-row">
        <!-- 设备状态 -->
        <div class="detail-panel">
          <div class="panel-header">
            <h3>设备监控状态</h3>
            <div class="panel-status">
              <span class="status-dot online"></span>
              <span>{{ onlineDevices }}/{{ totalDevices }} 在线</span>
            </div>
          </div>
          <div class="panel-content">
            <div class="device-list">
              <div class="device-item" v-for="device in deviceStatus" :key="device.id" :class="device.status">
                <div class="device-icon" :class="device.id <= 7 ? 'online' : device.id === 8 ? 'warning' : 'offline'">
                  <div class="device-number">{{ device.deviceNumber }}</div>
                  <i :class="device.deviceIcon"></i>
                </div>
                <div class="device-content">
                  <div class="device-title">{{ device.name }}</div>
                  <div class="device-desc">{{ device.description }}</div>
                  <div class="device-detail">{{ device.deviceDesc }}</div>
                  <div class="device-meta">
                    <span class="device-time">{{ device.time }}</span>
                    <span class="device-gps">状态: {{ device.gps }}</span>
                  </div>
                </div>
                <div class="device-info">
                  <div class="device-status" :class="device.status">
                    {{ device.status === 'online' ? '在线' : device.status === 'offline' ? '离线' : '警告' }}
                  </div>
                  <div class="device-signal">
                    <span class="signal-label">信号:</span>
                    <div class="signal-bars">
                      <span v-for="i in 5" :key="i" 
                            class="signal-bar active" 
                            :class="{ 
                              'signal-online': device.status === 'online', 
                              'signal-warning': device.status === 'warning' 
                            }">
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 告警信息 -->
        <div class="detail-panel">
          <div class="panel-header">
            <h3>实时告警中心</h3>
            <div class="panel-status">
              <span class="alert-count">{{ alertCount }}</span>
              <span>条待处理</span>
            </div>
          </div>
          <div class="panel-content">
            <div class="alert-list">
              <div class="alert-item" v-for="alert in recentAlerts" :key="alert.id" :class="alert.level">
                <div class="alert-icon">
                  <i class="fas fa-exclamation-triangle"></i>
                </div>
                <div class="alert-content">
                  <div class="alert-title">{{ alert.title }}</div>
                  <div class="alert-desc">{{ alert.description }}</div>
                  <div class="alert-time">{{ alert.time }}</div>
                </div>
                <div class="alert-status" :style="{color: alert.levelColor}">
                  {{ alert.level }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 数据统计 -->
        <div class="detail-panel">
          <div class="panel-header">
            <h3>数据统计概览</h3>
            <div class="panel-status">
              <span class="refresh-time">{{ lastRefreshTime }}</span>
            </div>
          </div>
          <div class="panel-content">
            <div class="stats-grid">
              <div class="stat-item" v-for="stat in dataStats" :key="stat.key">
                <div class="stat-icon" :style="{color: stat.color}">
                  <i :class="stat.icon"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-value">{{ stat.value }}</div>
                  <div class="stat-label">{{ stat.label }}</div>
                  <div class="stat-change" :class="stat.changeType">
                    {{ stat.change }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部状态栏 -->
    <div class="screen-footer">
      <div class="footer-left">
        <span class="system-status">
          <span class="status-dot online"></span>
          系统运行正常
        </span>
        <span class="data-source">数据来源：CloudCare智慧医养平台</span>
      </div>
      <div class="footer-right">
        <span class="update-time">最后更新：{{ lastUpdateTime }}</span>
        <span class="version">v2.1.0</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getAllObservations } from '@/api/elderlyObservations'
import { getAllElderlyProfiles } from '@/api/elderlyProfile'
import { getAllAlerts, getAlertsByTimeRange } from '@/api/healthAlert'
import { getDeviceList } from '@/api/device'
import gpsApi from '@/api/gps'

const router = useRouter()

// 时间相关
const currentTime = ref('')
const currentDate = ref('')
const lastUpdateTime = ref('')
const lastRefreshTime = ref('')

// 核心指标
const coreMetrics = ref([
  {
    key: 'totalElderly',
    label: '在管老人总数',
    value: '1,248',
    icon: 'fas fa-users',
    iconClass: 'metric-icon-blue',
    trend: '+12.5%',
    trendClass: 'trend-up',
    trendIcon: 'fas fa-arrow-up'
  },
  {
    key: 'healthyRate',
    label: '健康率',
    value: '87.3%',
    icon: 'fas fa-heartbeat',
    iconClass: 'metric-icon-green',
    trend: '+2.1%',
    trendClass: 'trend-up',
    trendIcon: 'fas fa-arrow-up'
  },
  {
    key: 'alertCount',
    label: '今日告警',
    value: '23',
    icon: 'fas fa-exclamation-triangle',
    iconClass: 'metric-icon-orange',
    trend: '-15.2%',
    trendClass: 'trend-down',
    trendIcon: 'fas fa-arrow-down'
  },
  {
    key: 'deviceOnline',
    label: '设备在线率',
    value: '94.7%',
    icon: 'fas fa-wifi',
    iconClass: 'metric-icon-purple',
    trend: '+0.8%',
    trendClass: 'trend-up',
    trendIcon: 'fas fa-arrow-up'
  }
])

// 图表相关
const activeTab = ref('7d')

// 时间范围切换处理
const handleTimeRangeChange = async (timeRange) => {
  activeTab.value = timeRange
  console.log('🔄 切换时间范围:', timeRange)
  await renderHealthTrend(timeRange)
}
const healthLegend = ref([
  { name: '健康', color: '#00ff88', value: '1,089' },
  { name: '注意', color: '#ffaa00', value: '127' },
  { name: '异常', color: '#ff4757', value: '32' }
])

// 设备状态
const totalDevices = ref(1248)
const onlineDevices = ref(1182)
const deviceStatus = ref([
  {
    id: 1,
    name: 'GPS定位手环',
    description: '张老太太 - 北京朝阳区',
    deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
    status: 'online',
    time: '2分钟前',
    gps: '正常',
    deviceIcon: 'fas fa-map-marker-alt',
    deviceNumber: '001'
  },
  {
    id: 2,
    name: 'GPS定位手环',
    description: '李大爷 - 上海浦东新区',
    deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
    status: 'online',
    time: '5分钟前',
    gps: '正常',
    deviceIcon: 'fas fa-map-marker-alt',
    deviceNumber: '002'
  },
  {
    id: 3,
    name: 'GPS定位手环',
    description: '王奶奶 - 广州天河区',
    deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
    status: 'online',
    time: '1小时前',
    gps: '正常',
    deviceIcon: 'fas fa-map-marker-alt',
    deviceNumber: '003'
  },
  {
    id: 4,
    name: 'GPS定位手环',
    description: '陈爷爷 - 深圳南山区',
    deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
    status: 'online',
    time: '刚刚',
    gps: '正常',
    deviceIcon: 'fas fa-map-marker-alt',
    deviceNumber: '004'
  },
  {
    id: 5,
    name: 'GPS定位手环',
    description: '刘奶奶 - 杭州西湖区',
    deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
    status: 'online',
    time: '3分钟前',
    gps: '正常',
    deviceIcon: 'fas fa-map-marker-alt',
    deviceNumber: '005'
  },
  {
    id: 6,
    name: 'GPS定位手环',
    description: '孙奶奶 - 武汉江汉区',
    deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
    status: 'online',
    time: '1分钟前',
    gps: '正常',
    deviceIcon: 'fas fa-map-marker-alt',
    deviceNumber: '006'
  },
  {
    id: 7,
    name: 'GPS定位手环',
    description: '赵大爷 - 成都锦江区',
    deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
    status: 'online',
    time: '10分钟前',
    gps: '正常',
    deviceIcon: 'fas fa-map-marker-alt',
    deviceNumber: '007'
  },
  {
    id: 8,
    name: 'GPS定位手环',
    description: '马奶奶 - 西安雁塔区',
    deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
    status: 'warning',
    time: '5分钟前',
    gps: '正常',
    deviceIcon: 'fas fa-map-marker-alt',
    deviceNumber: '008'
  },
  {
    id: 9,
    name: 'GPS定位手环',
    description: '周爷爷 - 南京鼓楼区',
    deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
    status: 'offline',
    time: '15分钟前',
    gps: '正常',
    deviceIcon: 'fas fa-map-marker-alt',
    deviceNumber: '009'
  },
  {
    id: 10,
    name: 'GPS定位手环',
    description: '吴奶奶 - 重庆渝中区',
    deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
    status: 'offline',
    time: '8分钟前',
    gps: '正常',
    deviceIcon: 'fas fa-map-marker-alt',
    deviceNumber: '010'
  }
])

// 告警信息
const alertCount = ref(23)
const recentAlerts = ref([
  {
    id: 1,
    title: '围栏告警',
    description: '张老太太离开安全区域',
    time: '2分钟前',
    level: 'high'
  },
  {
    id: 2,
    title: '健康异常',
    description: '李大爷心率异常',
    time: '5分钟前',
    level: 'high'
  },
  {
    id: 3,
    title: '设备离线',
    description: '王奶奶GPS设备连接中断',
    time: '10分钟前',
    level: 'medium'
  },
  {
    id: 4,
    title: '紧急求助',
    description: '孙奶奶按下紧急呼叫按钮',
    time: '1分钟前',
    level: 'critical'
  },
  {
    id: 5,
    title: '体温异常',
    description: '赵大爷体温超出正常范围',
    time: '8分钟前',
    level: 'high'
  },
  {
    id: 6,
    title: '电量不足',
    description: '马爷爷智能手环需要充电',
    time: '15分钟前',
    level: 'low'
  }
])



// 数据统计
const dataStats = ref([
  {
    key: 'totalObservations',
    label: '总健康记录数',
    value: '0',
    icon: 'fas fa-stethoscope',
    color: '#00d4ff',
    change: '今日0条',
    changeType: 'neutral'
  },
  {
    key: 'gpsPoints',
    label: 'GPS定位点',
    value: '0',
    icon: 'fas fa-map-pin',
    color: '#ff6b6b',
    change: '暂无设备',
    changeType: 'neutral'
  },
  {
    key: 'dataVolume',
    label: '数据量(GB)',
    value: '0.0',
    icon: 'fas fa-database',
    color: '#4ecdc4',
    change: '暂无数据',
    changeType: 'neutral'
  },
  {
    key: 'totalRecords',
    label: '总记录数',
    value: '0',
    icon: 'fas fa-chart-bar',
    color: '#ffe66d',
    change: '今日0次',
    changeType: 'neutral'
  }
])

// 定时器
let timeInterval = null
let dataInterval = null

// 动画数据
const animatedMetrics = ref({})

// 加载状态
const loading = ref(true)
const error = ref(null)

// 获取核心指标数据
const fetchCoreMetrics = async () => {
  try {
    // 获取老人档案总数
    const elderlyResponse = await getAllElderlyProfiles()
    let elderlyCount = 0
    if (elderlyResponse && (elderlyResponse.code === 200 || elderlyResponse.success)) {
      const elderlyData = elderlyResponse.data || elderlyResponse.result || elderlyResponse
      if (Array.isArray(elderlyData)) {
        elderlyCount = elderlyData.length
      } else if (elderlyData && typeof elderlyData === 'object') {
        elderlyCount = elderlyData.total || elderlyData.count || 0
        if (elderlyCount === 0 && elderlyData.records && Array.isArray(elderlyData.records)) {
          elderlyCount = elderlyData.records.length
        }
      }
    }
    
    // 获取健康预警数据
    const alertResponse = await getAllAlerts()
    let alertCount = 0
    if (alertResponse && (alertResponse.code === 200 || alertResponse.status === 200)) {
      const alertData = alertResponse.data || alertResponse.result || alertResponse
      const alerts = Array.isArray(alertData) ? alertData : []
      const activeAlerts = alerts.filter(alert => 
        alert.status === '未处理' || alert.status === 'PENDING' || alert.status === 'ACTIVE'
      )
      alertCount = activeAlerts.length
    }
    
    // 获取健康记录数据
    const observationResponse = await getAllObservations()
    let healthRecordsCount = 0
    if (observationResponse && observationResponse.success) {
      const observationData = observationResponse.data
      if (Array.isArray(observationData)) {
        healthRecordsCount = observationData.length
      } else if (observationData && typeof observationData === 'object') {
        healthRecordsCount = observationData.total || observationData.count || 0
        if (healthRecordsCount === 0 && observationData.records) {
          healthRecordsCount = observationData.records.length
        }
      }
    }
    
    // 获取设备数据
    const deviceResponse = await getDeviceList({})
    let onlineDevicesCount = 0
    let totalDevicesCount = 0
    if (deviceResponse) {
      const isSuccess = deviceResponse.code === 200 || deviceResponse.status === 200 || deviceResponse.success === true
      if (isSuccess || !deviceResponse.code) {
        const deviceData = deviceResponse.data?.records || 
                         deviceResponse.data?.list ||
                         deviceResponse.data ||
                         deviceResponse.result ||
                         deviceResponse.records ||
                         deviceResponse.list ||
                         deviceResponse
        
        if (Array.isArray(deviceData)) {
          totalDevicesCount = deviceData.length
          const onlineDevices = deviceData.filter(device => 
            device.deviceStatus === 1 || device.deviceStatus === '1'
          )
          onlineDevicesCount = onlineDevices.length
        } else if (deviceData && typeof deviceData === 'object') {
          totalDevicesCount = deviceData.total || deviceData.count || 0
          onlineDevicesCount = deviceData.online || deviceData.onlineCount || 0
        }
      }
    }
    
    // 更新核心指标数据
    coreMetrics.value[0].value = elderlyCount
    coreMetrics.value[0].trend = elderlyCount > 0 ? `共${elderlyCount}人` : '暂无数据'
    
    coreMetrics.value[1].value = alertCount
    coreMetrics.value[1].trend = alertCount > 0 ? `${alertCount}条待处理` : '无预警'
    
    coreMetrics.value[2].value = healthRecordsCount
    coreMetrics.value[2].trend = `共${healthRecordsCount}条记录`
    
    coreMetrics.value[3].value = onlineDevicesCount
    const percentage = totalDevicesCount > 0 ? Math.round((onlineDevicesCount / totalDevicesCount) * 100) : 0
    coreMetrics.value[3].trend = totalDevicesCount > 0 ? `${percentage}%在线` : '暂无设备'
    
  } catch (err) {
    console.error('获取核心指标数据失败:', err)
    // 保持默认数据
  }
}

// 获取健康分布数据 - 使用真实数据
const fetchHealthDistribution = async () => {
  try {
    console.log('🏥 开始获取健康分布数据...')
    const observationResponse = await getAllObservations()
    console.log('📡 健康观察API原始响应:', observationResponse)
    
    if (observationResponse && observationResponse.success) {
      const observationData = observationResponse.data || []
      console.log('📊 健康观察数据:', observationData)
      
      if (Array.isArray(observationData)) {
        // 统计健康状态分布
        const healthStats = { healthy: 0, attention: 0, abnormal: 0 }
        
        observationData.forEach(record => {
          const healthStatus = getOverallHealthStatus(record)
          if (healthStatus.type === 'success') {
            healthStats.healthy++
          } else if (healthStatus.type === 'warning') {
            healthStats.attention++
          } else if (healthStatus.type === 'danger') {
            healthStats.abnormal++
          }
        })
        
        console.log('📈 健康状态统计:', healthStats)
        
        // 更新健康分布图表数据
        healthLegend.value = [
          { name: '健康', value: healthStats.healthy, color: '#67c23a' },
          { name: '注意', value: healthStats.attention, color: '#e6a23c' },
          { name: '异常', value: healthStats.abnormal, color: '#f56c6c' }
        ]
        
        // 渲染健康状态分布图表
        renderHealthStatusChart(healthStats)
        
        console.log('🎯 健康分布数据更新完成:', healthLegend.value)
      } else {
        console.warn('⚠️ 健康观察数据格式异常')
      }
    } else {
      console.warn('⚠️ 健康观察API调用失败或返回异常')
    }
  } catch (err) {
    console.error('❌ 获取健康分布数据失败:', err)
  }
}

// 健康状态计算函数（参考健康评估界面逻辑）
const getTemperatureStatus = (temperature) => {
  if (temperature < 36.0) {
    return { type: 'info', text: '偏低' }
  } else if (temperature >= 36.0 && temperature <= 37.2) {
    return { type: 'success', text: '正常' }
  } else if (temperature > 37.2 && temperature <= 38.0) {
    return { type: 'warning', text: '偏高' }
  } else {
    return { type: 'danger', text: '发热' }
  }
}

const getBloodPressureStatus = (systolicBp) => {
  if (systolicBp < 90) {
    return { type: 'info', text: '偏低' }
  } else if (systolicBp >= 90 && systolicBp <= 139) {
    return { type: 'success', text: '正常' }
  } else if (systolicBp >= 140 && systolicBp <= 159) {
    return { type: 'warning', text: '偏高' }
  } else {
    return { type: 'danger', text: '高压' }
  }
}

const getHeartRateStatus = (heartRate) => {
  if (heartRate < 60) {
    return { type: 'info', text: '偏低' }
  } else if (heartRate >= 60 && heartRate <= 100) {
    return { type: 'success', text: '正常' }
  } else if (heartRate > 100 && heartRate <= 120) {
    return { type: 'warning', text: '偏高' }
  } else {
    return { type: 'danger', text: '过快' }
  }
}

const getBMIStatus = (bmi) => {
  const bmiValue = parseFloat(bmi)
  if (bmiValue < 18.5) {
    return { type: 'info', text: '偏瘦' }
  } else if (bmiValue >= 18.5 && bmiValue <= 23.9) {
    return { type: 'success', text: '正常' }
  } else if (bmiValue >= 24.0 && bmiValue <= 27.9) {
    return { type: 'warning', text: '偏胖' }
  } else {
    return { type: 'danger', text: '肥胖' }
  }
}

const calculateBMI = (height, weight) => {
  if (!height || !weight) return 0
  const heightInMeters = height / 100
  const bmi = weight / (heightInMeters * heightInMeters)
  return bmi.toFixed(1)
}

const getOverallHealthStatus = (row) => {
  let abnormalCount = 0
  
  // 检查体温
  if (row.bodyTemperature) {
    const tempStatus = getTemperatureStatus(row.bodyTemperature)
    if (tempStatus.type !== 'success') abnormalCount++
  }
  
  // 检查血压
  if (row.systolicBp) {
    const bpStatus = getBloodPressureStatus(row.systolicBp)
    if (bpStatus.type !== 'success') abnormalCount++
  }
  
  // 检查心率
  if (row.heartRate) {
    const hrStatus = getHeartRateStatus(row.heartRate)
    if (hrStatus.type !== 'success') abnormalCount++
  }
  
  // 检查BMI
  if (row.height && row.weight) {
    const bmi = calculateBMI(row.height, row.weight)
    const bmiStatus = getBMIStatus(bmi)
    if (bmiStatus.type !== 'success') abnormalCount++
  }
  
  if (abnormalCount === 0) {
    return { type: 'success', text: '健康' }
  } else if (abnormalCount === 1) {
    return { type: 'warning', text: '注意' }
  } else {
    return { type: 'danger', text: '异常' }
  }
}

// 渲染健康状态分布图表
const renderHealthStatusChart = (healthStats) => {
  const chartDom = document.getElementById('healthStatusChart')
  if (!chartDom) return
  
  const myChart = echarts.getInstanceByDom(chartDom) || echarts.init(chartDom)
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },

    series: [
      {
        name: '健康状态',
        type: 'pie',
        radius: ['40%', '65%'],
         center: ['50%', '50%'],
        data: [
          { 
            value: healthStats.healthy, 
            name: '健康', 
            itemStyle: { color: '#67c23a' },
            label: { color: '#67c23a' },
            labelLine: { lineStyle: { color: '#67c23a' } }
          },
          { 
            value: healthStats.attention, 
            name: '注意', 
            itemStyle: { color: '#e6a23c' },
            label: { color: '#e6a23c' },
            labelLine: { lineStyle: { color: '#e6a23c' } }
          },
          { 
            value: healthStats.abnormal, 
            name: '异常', 
            itemStyle: { color: '#f56c6c' },
            label: { color: '#f56c6c' },
            labelLine: { lineStyle: { color: '#f56c6c' } }
          }
        ].filter(item => item.value > 0),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        label: {
          show: true,
          position: 'outside',
          distanceToLabelLine: 5,
          fontSize: 12,
          fontWeight: 'bold',
          formatter: function(params) {
            return params.name + ' ' + params.value
          }
        },
        labelLine: {
          show: true,
          length: 8,
          length2: 5,
          smooth: true,
          lineStyle: {
            width: 1,
            type: 'solid'
          }
        }
      }
    ]
  }
  
  // 如果没有数据，显示提示
  if (healthStats.healthy === 0 && healthStats.attention === 0 && healthStats.abnormal === 0) {
    option.series[0].data = [{ value: 1, name: '暂无数据', itemStyle: { color: '#ddd' } }]
  }
  
  myChart.setOption(option)
}

// 获取设备状态数据
const fetchDeviceStats = async () => {
  try {
    console.log('🔍 开始获取设备绑定数据...')
    const bindingResponse = await gpsApi.getBindings()
    console.log('📡 设备绑定API原始响应:', bindingResponse)
    console.log('📡 响应数据类型:', typeof bindingResponse)
    console.log('📡 响应是否为null/undefined:', bindingResponse === null || bindingResponse === undefined)
    
    if (bindingResponse) {
      const isSuccess = bindingResponse.code === 200 || bindingResponse.status === 200 || bindingResponse.success === true
      console.log('✅ API调用成功状态:', isSuccess)
      
      if (isSuccess || !bindingResponse.code) {
        const bindingData = bindingResponse.data || bindingResponse.result || bindingResponse
        
        console.log('📊 提取的设备绑定数据:', bindingData)
        console.log('📊 设备绑定数据类型:', typeof bindingData, '是否为数组:', Array.isArray(bindingData))
        
        if (Array.isArray(bindingData)) {
          console.log('📈 绑定设备数量:', bindingData.length)
          console.log('📈 绑定数据详情:', bindingData)
          totalDevices.value = bindingData.length
          
          // 数据库查询已经过滤了有效绑定（status=1），所以直接使用返回的数据
          const activeBindings = bindingData // 所有返回的数据都是有效绑定
          onlineDevices.value = activeBindings.length
          console.log('🟢 在线设备数量:', onlineDevices.value)
          console.log('🟢 活跃绑定详情:', activeBindings)
          
          // 使用真实设备绑定数据替换静态数据
          if (activeBindings.length > 0) {
            deviceStatus.value = activeBindings.map((binding, index) => ({
              id: binding.id || index + 1,
              name: 'GPS定位手环', // 统一设备名称
              description: `${binding.elderly_name || binding.elderlyName || '未知老人'} - 北京朝阳区`, // 使用老人姓名，地址保持不变
              deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时', // GPS定位设备描述
              status: 'online', // 已绑定的设备都显示为在线
              time: '2024-01-15 14:30',
              gps: '正常', // 统一状态信息
              deviceIcon: 'fas fa-map-marker-alt', // GPS定位图标
              deviceNumber: String(binding.macid || index + 1).slice(-3).padStart(3, '0') // 使用MAC地址后3位作为设备编号
            }))
            console.log('🎯 处理后的设备状态数据:', deviceStatus.value)
          } else {
            console.log('⚠️ 没有活跃的设备绑定数据，保持空设备列表')
            deviceStatus.value = [] // 清空设备列表
          }
        } else if (bindingData && typeof bindingData === 'object') {
          totalDevices.value = bindingData.total || bindingData.count || 0
          onlineDevices.value = bindingData.online || bindingData.onlineCount || 0
          console.log('📊 从对象获取设备统计 - 总数:', totalDevices.value, '在线:', onlineDevices.value)
        }
      } else {
        console.warn('⚠️ API调用失败，响应码:', bindingResponse.code || bindingResponse.status)
      }
    } else {
      console.warn('⚠️ 设备绑定API返回空响应')
    }
  } catch (err) {
    console.error('❌ 获取设备绑定数据失败:', err)
    console.error('❌ 错误详情:', err.message)
    console.error('❌ 错误堆栈:', err.stack)
    
    // 如果API调用失败，显示一些模拟数据用于测试
    console.log('🔧 API调用失败，使用模拟数据进行测试')
    totalDevices.value = 5
    onlineDevices.value = 3
    deviceStatus.value = [
      {
        id: 1,
        name: 'GPS定位手环',
        description: '张三 - 北京朝阳区',
        deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
        status: 'online',
        time: '2024-01-15 14:30',
        gps: '正常',
        deviceIcon: 'fas fa-map-marker-alt',
        deviceNumber: '001'
      },
      {
        id: 2,
        name: 'GPS定位手环',
        description: '李四 - 北京朝阳区',
        deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
        status: 'online',
        time: '2024-01-15 14:28',
        gps: '正常',
        deviceIcon: 'fas fa-map-marker-alt',
        deviceNumber: '002'
      },
      {
        id: 3,
        name: 'GPS定位手环',
        description: '王五 - 北京朝阳区',
        deviceDesc: '高精度GPS定位，支持室内外无缝切换，电池续航72小时',
        status: 'online',
        time: '2024-01-15 14:25',
        gps: '正常',
        deviceIcon: 'fas fa-map-marker-alt',
        deviceNumber: '003'
      }
    ]
    console.log('🎯 使用模拟设备数据:', deviceStatus.value)
  }
}

// 获取告警数据
const fetchAlertStats = async () => {
  try {
    console.log('🚨 开始获取告警数据...')
    const alertResponse = await getAllAlerts()
    console.log('📡 告警API原始响应:', alertResponse)
    
    if (alertResponse && (alertResponse.code === 200 || alertResponse.status === 200)) {
      const alertData = alertResponse.data || alertResponse.result || alertResponse
      const alerts = Array.isArray(alertData) ? alertData : []
      console.log('📊 提取的告警数据:', alerts)
      console.log('📊 告警数据数量:', alerts.length)
      
      // 过滤活跃告警
      const activeAlerts = alerts.filter(alert => 
        alert.status === '未处理' || alert.status === 'PENDING' || alert.status === 'ACTIVE'
      )
      console.log('🔴 活跃告警数量:', activeAlerts.length)
      
      // 更新告警统计数据
      alertCount.value = activeAlerts.length
      recentAlerts.value = alerts.slice(0, 6).map(alert => ({
        id: alert.alertId,
        title: alert.alertTitle || '健康预警',
        description: `${alert.elderlyName || '老人'}${alert.alertDescription || '预警描述'}存在非正常状况，请及时处理`,
        time: formatTime(alert.createdAt),
        level: getAlertLevelText(alert.alertLevel),
        levelColor: getAlertLevelColor(alert.alertLevel),
        status: alert.status || 'ACTIVE',
        statusText: alert.status === 'ACTIVE' ? '待处理' : alert.status === 'RESOLVED' ? '已处理' : '未处理',
        icon: 'fas fa-exclamation-triangle'
      }))
      console.log('🎯 处理后的告警数据:', recentAlerts.value)
    } else {
      console.warn('⚠️ 告警API调用失败或返回异常:', alertResponse)
    }
  } catch (err) {
    console.error('❌ 获取告警数据失败:', err)
  }
}

// 获取数据统计概览
const fetchDataOverview = async () => {
  try {
    console.log('📈 开始获取数据统计概览...')
    
    // 获取健康观察记录数据
    const observationResponse = await getAllObservations()
    console.log('📡 健康观察API原始响应:', observationResponse)
    
    let totalObservations = 0
    let todayObservations = 0
    
    if (observationResponse && observationResponse.success) {
      const observationData = observationResponse.data || []
      console.log('📊 健康观察数据:', observationData)
      
      if (Array.isArray(observationData)) {
        totalObservations = observationData.length
        const today = new Date().toISOString().split('T')[0]
        todayObservations = observationData.filter(obs => 
          obs.createTime && obs.createTime.startsWith(today)
        ).length
        console.log('📊 总观察记录:', totalObservations, '今日记录:', todayObservations)
      }
    } else {
      console.warn('⚠️ 健康观察API调用失败或返回异常')
    }
    
    // 获取binding表数据用于GPS定位点统计
    const bindingResponse = await gpsApi.getBindings()
    console.log('📡 设备绑定API响应(用于GPS统计):', bindingResponse)
    
    let gpsPoints = 0
    if (bindingResponse && (bindingResponse.code === 200 || bindingResponse.success)) {
      const bindingData = bindingResponse.data || []
      if (Array.isArray(bindingData)) {
        gpsPoints = bindingData.length // 使用binding表的真实数据数量
        console.log('📍 GPS设备绑定数量:', gpsPoints)
      }
    } else {
      console.warn('⚠️ 获取设备绑定数据失败，使用默认值')
      gpsPoints = 2 // 用户提到的默认数量
    }
    
    // 获取告警数据用于API调用统计
    const alertResponse = await getAllAlerts()
    let totalAlerts = 0
    if (alertResponse && (alertResponse.code === 200 || alertResponse.status === 200)) {
      const alertData = alertResponse.data || alertResponse.result || alertResponse
      totalAlerts = Array.isArray(alertData) ? alertData.length : 0
    }
    
    // 获取老人档案数据
    const elderlyResponse = await getAllElderlyProfiles()
    let totalElderly = 0
    if (elderlyResponse && (elderlyResponse.code === 200 || elderlyResponse.success)) {
      const elderlyData = elderlyResponse.data || elderlyResponse.result || elderlyResponse
      if (Array.isArray(elderlyData)) {
        totalElderly = elderlyData.length
      } else if (elderlyData && elderlyData.records) {
        totalElderly = elderlyData.records.length
      }
    }
    
    // 计算数据量（基于实际记录数）
    const totalRecords = totalObservations + totalAlerts + totalElderly
    const dataVolumeGB = (totalRecords * 0.001).toFixed(1) // 假设每条记录约1KB
    
    // 更新数据统计概览为真实数据
    dataStats.value[0].value = totalObservations.toString()
    dataStats.value[0].change = todayObservations > 0 ? `今日${todayObservations}条` : '今日0条'
    
    dataStats.value[1].value = gpsPoints.toString()
    dataStats.value[1].change = gpsPoints > 0 ? `绑定设备${gpsPoints}台` : '暂无设备'
    
    dataStats.value[2].value = dataVolumeGB
    dataStats.value[2].change = totalRecords > 0 ? `${totalRecords}条记录` : '暂无数据'
    
    dataStats.value[3].value = `${(totalObservations + totalAlerts + totalElderly).toFixed(0)}`
    dataStats.value[3].change = `今日${todayObservations + totalAlerts}次`
    
    console.log('🎯 更新后的数据统计:', dataStats.value)
  } catch (err) {
    console.error('获取数据统计概览失败:', err)
  }
}

// 渲染地理分布热力图
const renderGeoDistribution = async () => {
  try {
    console.log('🗺️ 开始渲染地理分布热力图...')
    
    // 获取健康观察记录数据
    const observationResponse = await getAllObservations()
    console.log('📡 观察记录API响应:', observationResponse)
    
    let locationData = []
    
    if (observationResponse && observationResponse.success) {
      const observations = observationResponse.data || []
      console.log('📊 观察记录数据:', observations)
      
      // 统计各省份的数据
      const provinceCount = {}
      
      observations.forEach(observation => {
        const location = observation.observationLocation
        if (location) {
          if (!provinceCount[location]) {
            provinceCount[location] = 0
          }
          provinceCount[location] += 1
        }
      })
      
      console.log('📍 省份统计数据:', provinceCount)
      
      // 转换为地图数据格式
      locationData = Object.entries(provinceCount).map(([name, value]) => ({ name, value }))
      
      // 如果没有真实数据，使用默认数据
      if (locationData.length === 0) {
        locationData = [
          { name: '北京', value: 15 },
          { name: '上海', value: 12 },
          { name: '广东', value: 18 },
          { name: '浙江', value: 8 }
        ]
        console.log('⚠️ 使用默认地理数据')
      }
    } else {
      console.warn('⚠️ 观察记录API调用失败，使用默认数据')
      locationData = [
        { name: '北京', value: 15 },
        { name: '上海', value: 12 },
        { name: '广东', value: 18 },
        { name: '浙江', value: 8 }
      ]
    }
    
    console.log('🎯 最终地理数据:', locationData)
    
    // 渲染地图
    const geoChart = echarts.init(document.getElementById('geoChart'))
    
    fetch('https://geo.datav.aliyun.com/areas/bound/100000_full.json')
      .then(res => res.json())
      .then(geoJson => {
        echarts.registerMap('china', geoJson)
        
        const maxValue = Math.max(...locationData.map(item => item.value))
        
        const geoOption = {
          backgroundColor: 'transparent',
          title: {
            text: '健康数据地理分布',
            left: 'center',
            textStyle: {
              color: '#ffffff',
              fontSize: 16
            }
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c}条记录'
          },
          visualMap: {
            min: 0,
            max: maxValue,
            left: 'left',
            top: 'bottom',
            text: ['多', '少'],
            calculable: true,
            inRange: {
              color: ['#1e3a8a', '#2563eb', '#3b82f6', '#60a5fa', '#93c5fd']
            },
            textStyle: {
              color: '#ffffff'
            }
          },
          geo: {
            map: 'china',
            roam: true,
            itemStyle: {
              areaColor: '#1e3a8a',
              borderColor: '#00d4ff'
            },
            emphasis: {
              itemStyle: {
                areaColor: '#2563eb'
              }
            }
          },
          series: [{
            name: '健康记录数量',
            type: 'map',
            map: 'china',
            roam: true,
            data: locationData,
            itemStyle: {
              borderColor: '#00d4ff',
              borderWidth: 1
            },
            emphasis: {
              itemStyle: {
                areaColor: '#00ff88'
              }
            }
          }]
        }
        
        geoChart.setOption(geoOption)
        console.log('✅ 地理分布热力图渲染完成')
        
        // 添加到resize监听中
        window.addEventListener('resize', () => {
          geoChart.resize()
        })
      })
      .catch(err => {
        console.error('❌ 获取地图数据失败:', err)
      })
      
  } catch (err) {
    console.error('❌ 渲染地理分布热力图失败:', err)
  }
}

// 渲染健康趋势图 - 使用真实数据
const renderHealthTrend = async (timeRange = '7d') => {
  try {
    console.log('📈 开始获取健康趋势数据...', timeRange)
    
    // 根据时间范围计算日期
    const endDate = new Date()
    const startDate = new Date()
    let days = 7
    let titleText = '健康预警趋势（最近7天）'
    
    switch (timeRange) {
      case '30d':
        days = 30
        startDate.setDate(endDate.getDate() - 29)
        titleText = '健康预警趋势（最近30天）'
        break
      case '90d':
        days = 90
        startDate.setDate(endDate.getDate() - 89)
        titleText = '健康预警趋势（最近90天）'
        break
      default:
        days = 7
        startDate.setDate(endDate.getDate() - 6)
        titleText = '健康预警趋势（最近7天）'
    }
    
    const startDateStr = startDate.toISOString().split('T')[0] + ' 00:00:00'
    const endDateStr = endDate.toISOString().split('T')[0] + ' 23:59:59'
    
    console.log('📅 查询时间范围:', startDateStr, 'to', endDateStr)
    
    const alertResponse = await getAlertsByTimeRange(startDateStr, endDateStr)
    console.log('📡 预警趋势API响应:', alertResponse)
    
    let trendData = []
    let dateLabels = []
    
    if (alertResponse && alertResponse.code === 200) {
      const alertData = alertResponse.data || []
      console.log('📊 预警数据:', alertData)
      
      // 生成日期标签和数据
      const alertCountByDate = {}
      
      // 根据时间范围生成不同的标签格式
      if (timeRange === '7d') {
        // 7天：显示月/日(周几)
        for (let i = 0; i < days; i++) {
          const date = new Date()
          date.setDate(endDate.getDate() - (days - 1 - i))
          const dateStr = date.toISOString().split('T')[0]
          const dayNames = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
          dateLabels.push(`${date.getMonth() + 1}/${date.getDate()}(${dayNames[date.getDay()]})`)
          alertCountByDate[dateStr] = 0
        }
      } else if (timeRange === '30d') {
        // 30天：按周聚合，显示第几周
        const weekData = {}
        for (let i = 0; i < days; i++) {
          const date = new Date()
          date.setDate(endDate.getDate() - (days - 1 - i))
          const dateStr = date.toISOString().split('T')[0]
          const weekNum = Math.floor(i / 7) + 1
          const weekKey = `第${weekNum}周`
          if (!weekData[weekKey]) {
            weekData[weekKey] = 0
            dateLabels.push(weekKey)
          }
          alertCountByDate[dateStr] = 0
        }
      } else {
        // 90天：按月聚合，显示月份
        const monthData = {}
        for (let i = 0; i < days; i++) {
          const date = new Date()
          date.setDate(endDate.getDate() - (days - 1 - i))
          const dateStr = date.toISOString().split('T')[0]
          const monthKey = `${date.getMonth() + 1}月`
          if (!monthData[monthKey]) {
            monthData[monthKey] = 0
            if (!dateLabels.includes(monthKey)) {
              dateLabels.push(monthKey)
            }
          }
          alertCountByDate[dateStr] = 0
        }
      }
      
      // 统计每天的预警数量
      alertData.forEach(alert => {
        if (alert.createdAt) {
          const alertDate = new Date(alert.createdAt)
          const dateStr = alertDate.toISOString().split('T')[0]
          if (alertCountByDate.hasOwnProperty(dateStr)) {
            alertCountByDate[dateStr]++
          }
        }
      })
      
      // 根据时间范围聚合数据
      if (timeRange === '7d') {
        trendData = Object.values(alertCountByDate)
      } else if (timeRange === '30d') {
        // 按周聚合
        const weeklyData = []
        const dateKeys = Object.keys(alertCountByDate).sort()
        for (let i = 0; i < dateKeys.length; i += 7) {
          const weekSum = dateKeys.slice(i, i + 7).reduce((sum, key) => sum + alertCountByDate[key], 0)
          weeklyData.push(weekSum)
        }
        trendData = weeklyData
      } else {
        // 按月聚合
        const monthlyData = {}
        Object.keys(alertCountByDate).forEach(dateStr => {
          const date = new Date(dateStr)
          const monthKey = `${date.getMonth() + 1}月`
          if (!monthlyData[monthKey]) {
            monthlyData[monthKey] = 0
          }
          monthlyData[monthKey] += alertCountByDate[dateStr]
        })
        trendData = Object.values(monthlyData)
      }
      
      console.log('📈 趋势数据处理完成:', { dateLabels, trendData })
    } else {
      console.warn('⚠️ 预警趋势数据获取失败，使用默认数据')
      // 使用默认数据
      if (timeRange === '7d') {
        dateLabels = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        trendData = [12, 8, 15, 6, 9, 11, 7]
      } else if (timeRange === '30d') {
        dateLabels = ['第1周', '第2周', '第3周', '第4周']
        trendData = [45, 38, 52, 41]
      } else {
        dateLabels = ['1月', '2月', '3月']
        trendData = [156, 142, 178]
      }
    }
    
    // 渲染趋势图
    const trendChart = echarts.init(document.getElementById('trendChart'))
    const trendOption = {
      backgroundColor: 'transparent',
      title: {
        text: titleText,
        left: 'center',
        textStyle: {
          color: '#ffffff',
          fontSize: 16
        },
        top: 10
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          lineStyle: {
            color: '#00d4ff',
            type: 'dashed'
          }
        },
        backgroundColor: 'rgba(0, 0, 0, 0.8)',
        borderColor: '#00d4ff',
        borderWidth: 1,
        textStyle: {
          color: '#ffffff'
        },
        formatter: function(params) {
          const param = params[0]
          return `<div style="font-weight: bold; margin-bottom: 5px;">${param.axisValue}</div>
                  <div>${param.marker}<span style="margin-left: 5px;">预警数量: <strong>${param.value}</strong>条</span></div>`
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '8%',
        top: '20%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: dateLabels,
        axisLine: {
          lineStyle: {
            color: '#00d4ff'
          }
        },
        axisLabel: {
          color: '#ffffff',
          fontSize: 11,
          rotate: 0
        },
        axisTick: {
          show: false
        }
      },
      yAxis: {
        type: 'value',
        name: '预警数量',
        nameTextStyle: {
          color: '#ffffff',
          fontSize: 12
        },
        axisLine: {
          lineStyle: {
            color: '#00d4ff'
          }
        },
        axisLabel: {
          color: '#ffffff',
          fontSize: 11
        },
        splitLine: {
          lineStyle: {
            color: 'rgba(0, 212, 255, 0.2)',
            type: 'dashed'
          }
        },
        axisTick: {
          show: false
        }
      },
      series: [{
        name: '预警数量',
        data: trendData,
        type: 'line',
        smooth: true,
        lineStyle: {
          color: '#00ff88',
          width: 3
        },
        itemStyle: {
          color: '#00ff88',
          borderColor: '#ffffff',
          borderWidth: 2
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [{
              offset: 0, color: 'rgba(0, 255, 136, 0.3)'
            }, {
              offset: 1, color: 'rgba(0, 255, 136, 0)'
            }]
          }
        },
        symbol: 'circle',
        symbolSize: 6,
        showSymbol: true
      }]
    }
    
    trendChart.setOption(trendOption)
    
    // 保存图表实例以便响应式处理
    window.trendChart = trendChart
    
    console.log('🎯 健康趋势图渲染完成')
    
  } catch (error) {
    console.error('❌ 获取健康趋势数据失败:', error)
    
    // 显示错误状态的图表
    const trendChart = echarts.init(document.getElementById('trendChart'))
    const trendOption = {
      backgroundColor: 'transparent',
      title: {
        text: '健康预警趋势（数据获取失败）',
        left: 'center',
        textStyle: {
          color: '#ffffff',
          fontSize: 16
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        top: '20%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        axisLine: {
          lineStyle: {
            color: '#00d4ff'
          }
        },
        axisLabel: {
          color: '#ffffff'
        }
      },
      yAxis: {
        type: 'value',
        name: '预警数量',
        nameTextStyle: {
          color: '#ffffff'
        },
        axisLine: {
          lineStyle: {
            color: '#00d4ff'
          }
        },
        axisLabel: {
          color: '#ffffff'
        },
        splitLine: {
          lineStyle: {
            color: 'rgba(0, 212, 255, 0.2)'
          }
        }
      },
      series: [{
        name: '预警数量',
        data: [0, 0, 0, 0, 0, 0, 0],
        type: 'line',
        smooth: true,
        lineStyle: {
          color: '#666666',
          width: 2,
          type: 'dashed'
        },
        itemStyle: {
          color: '#666666'
        }
      }]
    }
    
    trendChart.setOption(trendOption)
    window.trendChart = trendChart
  }
}

// 获取所有数据
const fetchAllData = async () => {
  loading.value = true
  error.value = null
  
  try {
    await Promise.all([
      fetchCoreMetrics(),
      fetchHealthDistribution(),
      fetchDeviceStats(),
      fetchAlertStats(),
      fetchDataOverview()
    ])
  } catch (err) {
    error.value = '数据加载失败，请稍后重试'
    console.error('数据加载失败:', err)
  } finally {
    loading.value = false
  }
}

// 粒子动画样式
const getParticleStyle = (index) => {
  const size = Math.random() * 3 + 1
  return {
    left: Math.random() * 100 + '%',
    top: Math.random() * 100 + '%',
    width: size + 'px',
    height: size + 'px',
    animationDelay: Math.random() * 20 + 's',
    animationDuration: (Math.random() * 15 + 10) + 's',
    opacity: Math.random() * 0.8 + 0.2
  }
}

// 网格线样式
const getGridLineStyle = (type, index) => {
  if (type === 'horizontal') {
    return {
      top: (index * 5) + '%',
      animationDelay: (index * 0.1) + 's'
    }
  } else {
    return {
      left: (index * 3.33) + '%',
      animationDelay: (index * 0.05) + 's'
    }
  }
}

// 样式函数集合
const getWaveStyle = (index) => {
  return {
    animationDelay: (index * 2) + 's',
    animationDuration: (6 + index * 2) + 's'
  }
}

const getFlowBarStyle = (index) => {
  const colors = ['#00d4ff', '#00ff88', '#ff6b6b', '#ffd93d', '#6c5ce7', '#fd79a8']
  const angles = [15, 45, 75, 105, 135, 165, -15, -45]
  return {
    background: `linear-gradient(90deg, transparent, ${colors[index % colors.length]}, transparent)`,
    transform: `rotate(${angles[index % angles.length]}deg)`,
    animationDelay: `${index * 0.3}s`,
    animationDuration: `${4 + (index % 3)}s`
  }
}

const getTrailStyle = (index) => {
  const positions = [
    { top: '10%', left: '5%' },
    { top: '30%', right: '8%' },
    { bottom: '25%', left: '12%' },
    { top: '60%', right: '15%' },
    { bottom: '10%', right: '5%' },
    { top: '80%', left: '20%' }
  ]
  return {
    ...positions[index % positions.length],
    animationDelay: `${index * 0.5}s`,
    animationDuration: `${6 + (index % 2)}s`
  }
}

const getRingStyle = (index) => {
  const sizes = [150, 200, 250, 300]
  const colors = ['rgba(0, 212, 255, 0.1)', 'rgba(0, 255, 136, 0.1)', 'rgba(255, 107, 107, 0.1)', 'rgba(255, 217, 61, 0.1)']
  return {
    width: `${sizes[index]}px`,
    height: `${sizes[index]}px`,
    borderColor: colors[index],
    animationDelay: `${index * 1.2}s`,
    animationDuration: `${8 + index}s`
  }
}

const getStreamStyle = (index) => {
  const paths = [
    { top: '0%', left: `${10 + index * 7}%`, transform: 'rotate(45deg)' },
    { bottom: '0%', right: `${5 + index * 8}%`, transform: 'rotate(-45deg)' },
    { top: `${20 + index * 5}%`, left: '0%', transform: 'rotate(90deg)' },
    { top: `${15 + index * 6}%`, right: '0%', transform: 'rotate(-90deg)' }
  ]
  return {
    ...paths[index % paths.length],
    animationDelay: `${index * 0.2}s`,
    animationDuration: `${3 + (index % 4) * 0.5}s`
  }
}

const getFloatingDotStyle = (index) => {
  const colors = ['#00d4ff', '#00ff88', '#ff6b6b', '#ffd93d', '#6c5ce7']
  return {
    top: `${Math.random() * 80 + 10}%`,
    left: `${Math.random() * 80 + 10}%`,
    backgroundColor: colors[index % colors.length],
    animationDelay: `${index * 0.4}s`,
    animationDuration: `${4 + Math.random() * 3}s`
  }
}

const getPulseStyle = (index) => {
  const positions = [
    { top: '20%', left: '15%' },
    { top: '70%', right: '20%' },
    { bottom: '30%', left: '25%' },
    { top: '40%', right: '10%' },
    { top: '60%', left: '60%' }
  ]
  return {
    ...positions[index % positions.length],
    animationDelay: `${index * 0.8}s`,
    animationDuration: `${3 + index * 0.5}s`
  }
}

// 数据流动点样式
const getFlowDotStyle = (index) => {
  return {
    animationDelay: (index * 0.2) + 's'
  }
}

// 数字滚动动画
const animateNumber = (key, target) => {
  const start = animatedMetrics.value[key] || 0
  const targetNum = parseInt(target.toString().replace(/[^0-9]/g, '')) || 0
  const duration = 2000
  const startTime = Date.now()
  
  const animate = () => {
    const elapsed = Date.now() - startTime
    const progress = Math.min(elapsed / duration, 1)
    
    // 使用缓动函数
    const easeOutQuart = 1 - Math.pow(1 - progress, 4)
    const current = Math.floor(start + (targetNum - start) * easeOutQuart)
    
    animatedMetrics.value[key] = current
    
    if (progress < 1) {
      requestAnimationFrame(animate)
    } else {
      animatedMetrics.value[key] = targetNum
    }
  }
  
  animate()
}

// 更新时间
const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleTimeString('zh-CN', { hour12: false })
  currentDate.value = now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
  lastUpdateTime.value = now.toLocaleString('zh-CN')
  lastRefreshTime.value = now.toLocaleTimeString('zh-CN', { hour12: false })
}

// 初始化图表
const initCharts = async () => {
  // 健康状态饼图 - 移除静态数据初始化，等待真实数据加载
  // renderHealthStatusChart 函数会在 fetchHealthDistribution 中被调用

  // 地理分布图 - 使用真实数据
  await renderGeoDistribution()

  // 趋势图 - 使用真实数据
  await renderHealthTrend()

  // 响应式处理 - 移除geoChart.resize()因为已在renderGeoDistribution中处理
  window.addEventListener('resize', () => {
    if (window.healthChart) {
      window.healthChart.resize()
    }
    if (window.trendChart) {
      window.trendChart.resize()
    }
  })
}

// 返回系统
const goBack = () => {
  router.push('/dashboard')
}

// 随机更新数据
const updateRandomData = () => {
  // 随机更新核心指标
  coreMetrics.value.forEach(metric => {
    const oldValue = parseInt(metric.value.toString().replace(/[^0-9]/g, '')) || 0
    const change = Math.floor(Math.random() * 20) - 10
    const newValue = Math.max(0, oldValue + change)
    
    if (metric.key === 'healthyRate' || metric.key === 'deviceOnline') {
      metric.value = Math.min(100, newValue) + '%'
    } else {
      metric.value = newValue.toLocaleString()
    }
    
    // 启动动画
    animateNumber(metric.key, metric.value)
  })
  
  // 更新设备状态
  deviceStatus.value.forEach(device => {
    if (Math.random() < 0.1) { // 10%概率更新
      device.lastUpdate = Math.random() < 0.5 ? '刚刚' : Math.floor(Math.random() * 10) + '分钟前'
    }
  })
}

// 生命周期
onMounted(async () => {
  updateTime()
  timeInterval = setInterval(updateTime, 1000)
  
  // 延迟确保DOM已渲染
  setTimeout(async () => {
    // 初始化数据
    await fetchAllData()
    
    // 数据加载完成后初始化图表
    await initCharts()
    
    // 启动数字动画
    coreMetrics.value.forEach(metric => {
      animateNumber(metric.key, metric.value)
    })
  }, 500)
  
  // 设置时间更新定时器（移除数据自动刷新）
  // dataInterval = setInterval(async () => {
  //   updateTime()
  //   // 刷新真实数据
  //   await fetchAllData()
  // }, 30000) // 30秒刷新一次
})

// 获取设备图标
const getDeviceIcon = (deviceType) => {
  const iconMap = {
    'GPS': 'fas fa-map-marker-alt',
    '健康监测': 'fas fa-heartbeat',
    '智能手环': 'fas fa-watch',
    '紧急呼叫': 'fas fa-phone',
    '血压计': 'fas fa-thermometer-half',
    '跌倒检测': 'fas fa-shield-alt',
    '智能药盒': 'fas fa-pills',
    '环境监测': 'fas fa-home',
    '体温计': 'fas fa-stethoscope'
  }
  
  // 根据设备类型返回对应图标，默认返回设备图标
  for (const [type, icon] of Object.entries(iconMap)) {
    if (deviceType && deviceType.includes(type)) {
      return icon
    }
  }
  
  return 'fas fa-microchip' // 默认设备图标
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return '刚刚'
  
  try {
    const time = new Date(timeStr)
    const now = new Date()
    const diff = now - time
    
    const minutes = Math.floor(diff / (1000 * 60))
    const hours = Math.floor(diff / (1000 * 60 * 60))
    const days = Math.floor(diff / (1000 * 60 * 60 * 24))
    
    if (minutes < 1) return '刚刚'
    if (minutes < 60) return `${minutes}分钟前`
    if (hours < 24) return `${hours}小时前`
    if (days < 7) return `${days}天前`
    
    return time.toLocaleDateString()
  } catch (error) {
    return '刚刚'
  }
}

// 获取告警级别文本
const getAlertLevelText = (level) => {
  const levelMap = {
    'CRITICAL': '危急',
    'HIGH': '高风险',
    'MEDIUM': '中风险',
    'LOW': '低风险'
  }
  return levelMap[level] || '中风险'
}

// 获取告警级别颜色
const getAlertLevelColor = (level) => {
  const colorMap = {
    'CRITICAL': '#ff4757',
    'HIGH': '#ff6b35',
    'MEDIUM': '#ffa502',
    'LOW': '#2ed573'
  }
  return colorMap[level] || '#ffa502'
}

// 获取告警级别（保留原函数以防其他地方使用）
const getSeverityLevel = (severity) => {
  if (!severity) return 'medium'
  
  const level = severity.toLowerCase()
  if (level.includes('critical') || level.includes('紧急') || level === 'high') return 'critical'
  if (level.includes('high') || level.includes('高') || level.includes('重要')) return 'high'
  if (level.includes('medium') || level.includes('中') || level.includes('一般')) return 'medium'
  if (level.includes('low') || level.includes('低') || level.includes('轻微')) return 'low'
  
  return 'medium'
}

onUnmounted(() => {
  if (timeInterval) clearInterval(timeInterval)
  if (dataInterval) clearInterval(dataInterval)
})
</script>

<style scoped>
.data-screen {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #0c1426 0%, #1a2332 50%, #0f1419 100%);
  color: #ffffff;
  font-family: 'Microsoft YaHei', sans-serif;
  overflow: hidden;
  position: relative;
}

/* 背景动画 */
.bg-animation {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
  overflow: hidden;
}

.particle {
  position: absolute;
  background: #00d4ff;
  border-radius: 50%;
  animation: float linear infinite;
  box-shadow: 0 0 6px #00d4ff;
}

@keyframes float {
  0% {
    transform: translateY(100vh) rotate(0deg) scale(0);
    opacity: 0;
  }
  10% {
    opacity: 1;
    transform: translateY(90vh) rotate(36deg) scale(1);
  }
  90% {
    opacity: 1;
    transform: translateY(10vh) rotate(324deg) scale(1);
  }
  100% {
    transform: translateY(-10vh) rotate(360deg) scale(0);
    opacity: 0;
  }
}

/* 网格线动画 */
.grid-lines {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.grid-line {
  position: absolute;
  background: linear-gradient(90deg, transparent, rgba(0, 212, 255, 0.1), transparent);
  animation: gridPulse 4s ease-in-out infinite;
}

.grid-line.horizontal {
  width: 100%;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(0, 212, 255, 0.1), transparent);
}

.grid-line.vertical {
  width: 1px;
  height: 100%;
  background: linear-gradient(0deg, transparent, rgba(0, 212, 255, 0.1), transparent);
}

@keyframes gridPulse {
  0%, 100% {
    opacity: 0.1;
  }
  50% {
    opacity: 0.3;
  }
}

/* 能量波动画 */
.energy-waves {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 200px;
  height: 200px;
}

.wave {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: 2px solid rgba(0, 255, 136, 0.3);
  border-radius: 50%;
  animation: waveExpand infinite ease-out;
}

@keyframes waveExpand {
  0% {
    transform: scale(0);
    opacity: 1;
  }
  100% {
    transform: scale(4);
    opacity: 0;
  }
}

/* 数据流动效果 */
.data-flow {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-left: 10px;
}

.flow-dot {
  width: 4px;
  height: 4px;
  background: #00ff88;
  border-radius: 50%;
  animation: flowPulse 1.5s ease-in-out infinite;
}

@keyframes flowPulse {
  0%, 100% {
    opacity: 0.3;
    transform: scale(0.8);
  }
  50% {
    opacity: 1;
    transform: scale(1.2);
  }
}

/* 顶部标题栏 */
.screen-header {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  background: rgba(0, 0, 0, 0.3);
  border-bottom: 2px solid #00d4ff;
  position: relative;
  z-index: 10;
}

.header-left .logo {
  display: flex;
  align-items: center;
  gap: 20px;
  min-width: 350px;
}

.logo-icon {
  width: 45px;
  height: 45px;
  background: linear-gradient(45deg, #00d4ff, #00ff88);
  border-radius: 8px;
  position: relative;
  flex-shrink: 0;
}

.logo-icon::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 22px;
  height: 22px;
  background: #ffffff;
  border-radius: 4px;
}

.logo-text {
  font-size: 22px;
  font-weight: bold;
  background: linear-gradient(45deg, #00d4ff, #00ff88);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  white-space: nowrap;
  letter-spacing: 1px;
  line-height: 1.2;
}

.header-center .time-display {
  text-align: center;
}

.current-time {
  font-size: 32px;
  font-weight: bold;
  color: #00ff88;
  text-shadow: 0 0 10px rgba(0, 255, 136, 0.5);
}

.current-date {
  font-size: 14px;
  color: #00d4ff;
  margin-top: 5px;
}

.data-refresh-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  background: rgba(0, 212, 255, 0.1);
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 20px;
  color: #00d4ff;
  font-size: 12px;
  transition: all 0.3s ease;
  margin-top: 10px;
}

.data-refresh-indicator.refreshing {
  background: rgba(255, 193, 7, 0.1);
  border-color: rgba(255, 193, 7, 0.3);
  color: #ffc107;
}

.data-refresh-indicator i {
  font-size: 14px;
}

.data-refresh-indicator .fa-spin {
  animation: spin 1s linear infinite;
}

.back-btn {
  background: linear-gradient(45deg, #00d4ff, #0099cc);
  border: none;
  color: white;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.back-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 212, 255, 0.3);
}

/* 主要内容区域 */
.screen-content {
  padding: 20px 40px;
  height: calc(100vh - 160px);
  display: flex;
  flex-direction: column;
  gap: 20px;
  position: relative;
  z-index: 10;
}

/* 核心指标行 */
.metrics-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  height: 120px;
}

.metric-card {
  background: rgba(0, 0, 0, 0.4);
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 20px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  animation: cardGlow 3s ease-in-out infinite;
}

.metric-card:hover {
  border-color: #00d4ff;
  box-shadow: 0 8px 25px rgba(0, 212, 255, 0.2);
  transform: translateY(-2px);
}

.metric-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 212, 255, 0.1), transparent);
  animation: scanLine 3s ease-in-out infinite;
}

@keyframes cardGlow {
  0%, 100% {
    box-shadow: 0 0 5px rgba(0, 212, 255, 0.1);
  }
  50% {
    box-shadow: 0 0 20px rgba(0, 212, 255, 0.2);
  }
}

@keyframes scanLine {
  0% {
    left: -100%;
  }
  100% {
    left: 100%;
  }
}

.metric-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  position: relative;
  z-index: 2;
}

.metric-icon-blue {
  background: linear-gradient(45deg, #00d4ff, #0099cc);
  box-shadow: 0 0 20px rgba(0, 212, 255, 0.3);
}

.metric-icon-green {
  background: linear-gradient(45deg, #00ff88, #00cc66);
  box-shadow: 0 0 20px rgba(0, 255, 136, 0.3);
}

.metric-icon-orange {
  background: linear-gradient(45deg, #ffaa00, #ff8800);
  box-shadow: 0 0 20px rgba(255, 170, 0, 0.3);
}

.metric-icon-purple {
  background: linear-gradient(45deg, #aa00ff, #8800cc);
  box-shadow: 0 0 20px rgba(170, 0, 255, 0.3);
}

.metric-content {
  flex: 1;
}

.metric-value {
  font-size: 28px;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 5px;
  position: relative;
}

.animated-number {
  display: inline-block;
  animation: numberGlow 2s ease-in-out infinite;
}

@keyframes numberGlow {
  0%, 100% {
    text-shadow: 0 0 5px rgba(0, 255, 136, 0.5);
  }
  50% {
    text-shadow: 0 0 15px rgba(0, 255, 136, 0.8), 0 0 25px rgba(0, 255, 136, 0.4);
  }
}

.metric-label {
  font-size: 14px;
  color: #00d4ff;
  margin-bottom: 8px;
}

.metric-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.trend-up {
  color: #00ff88;
}

.trend-down {
  color: #ff4757;
}

.metric-bg-effect {
  position: absolute;
  top: 0;
  right: 0;
  width: 100px;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 212, 255, 0.1));
  pointer-events: none;
}

/* 图表行 */
.charts-row {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 20px;
  height: 300px;
}

.chart-container {
  background: rgba(0, 0, 0, 0.4);
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 12px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

.chart-container::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, #00d4ff, transparent);
  animation: topBorderFlow 2s ease-in-out infinite;
}

@keyframes topBorderFlow {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100%);
  }
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.chart-header h3 {
  margin: 0;
  font-size: 16px;
  color: #ffffff;
}

.chart-controls {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 12px;
  color: #00d4ff;
}

.status-indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #00ff88;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(0, 255, 136, 0.7);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(0, 255, 136, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(0, 255, 136, 0);
  }
}

/* 全局动画增强 */
.screen-header {
  animation: headerSlideIn 1s ease-out;
}

@keyframes headerSlideIn {
  from {
    transform: translateY(-100%);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.metrics-row .metric-card {
  animation: cardFadeIn 0.8s ease-out forwards;
  opacity: 0;
}

.metrics-row .metric-card:nth-child(1) { animation-delay: 0.1s; }
.metrics-row .metric-card:nth-child(2) { animation-delay: 0.2s; }
.metrics-row .metric-card:nth-child(3) { animation-delay: 0.3s; }
.metrics-row .metric-card:nth-child(4) { animation-delay: 0.4s; }

@keyframes cardFadeIn {
  from {
    transform: translateY(30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.charts-row .chart-container {
  animation: chartSlideIn 1s ease-out forwards;
  opacity: 0;
}

.charts-row .chart-container:nth-child(1) { animation-delay: 0.5s; }
.charts-row .chart-container:nth-child(2) { animation-delay: 0.7s; }
.charts-row .chart-container:nth-child(3) { animation-delay: 0.9s; }

@keyframes chartSlideIn {
  from {
    transform: translateX(-50px);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* 鼠标悬停增强效果 */
.metric-card:hover .metric-icon {
  animation: iconSpin 0.6s ease-in-out;
}

@keyframes iconSpin {
  from {
    transform: rotateY(0deg);
  }
  to {
    transform: rotateY(360deg);
  }
}

.chart-container:hover {
  transform: scale(1.02);
  transition: transform 0.3s ease;
}

.detail-panel:hover {
  transform: translateY(-5px);
  transition: transform 0.3s ease;
}

.region-select {
  width: 80px;
}

.time-tabs {
  display: flex;
  gap: 5px;
}

.time-tab {
  padding: 4px 12px;
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 12px;
}

.time-tab.active,
.time-tab:hover {
  background: #00d4ff;
  color: #000000;
}

.chart-content {
  flex: 1;
  display: flex;
  gap: 15px;
}

.chart {
  flex: 1;
  min-height: 200px;
}

.chart-legend {
  width: 120px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-top: 20px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 2px;
}

.legend-text {
  flex: 1;
  color: #ffffff;
}

.legend-value {
  color: #00d4ff;
  font-weight: bold;
}

/* 详细数据行 */
.details-row {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 20px;
  flex: 1;
}

.detail-panel {
  background: rgba(0, 0, 0, 0.4);
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 12px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  position: relative;
  animation: panelBreath 4s ease-in-out infinite;
}

@keyframes panelBreath {
  0%, 100% {
    border-color: rgba(0, 212, 255, 0.3);
    box-shadow: 0 0 5px rgba(0, 212, 255, 0.1);
  }
  50% {
    border-color: rgba(0, 212, 255, 0.5);
    box-shadow: 0 0 15px rgba(0, 212, 255, 0.2);
  }
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(0, 212, 255, 0.2);
}

.panel-header h3 {
  margin: 0;
  font-size: 16px;
  color: #ffffff;
}

.panel-status {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #00d4ff;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.status-dot.online {
  background: #00ff88;
  animation: pulse 2s infinite;
}

.alert-count {
  background: #ff4757;
  color: #ffffff;
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: bold;
}

.refresh-time {
  color: #00ff88;
}

.panel-content {
  flex: 1;
  overflow-y: auto;
}

/* 设备网格 */
.device-grid {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.device-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  border: 1px solid rgba(0, 212, 255, 0.2);
}

.device-icon {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.device-icon.online {
  background: #00ff88;
  color: #000000;
  animation: deviceOnline 2s ease-in-out infinite;
}

@keyframes deviceOnline {
  0%, 100% {
    box-shadow: 0 0 5px rgba(0, 255, 136, 0.5);
  }
  50% {
    box-shadow: 0 0 15px rgba(0, 255, 136, 0.8);
  }
}

.device-icon.offline {
  background: #666666;
  color: #ffffff;
}

.device-info {
  flex: 1;
}

.device-name {
  font-size: 14px;
  font-weight: bold;
  color: #ffffff;
}

.device-location {
  font-size: 12px;
  color: #00d4ff;
}

.device-time {
  font-size: 11px;
  color: #999999;
}

.device-signal {
  display: flex;
  gap: 2px;
  align-items: end;
}

.signal-bar {
  width: 3px;
  background: #666666;
  border-radius: 1px;
}

.device-signal.strong .signal-bar:nth-child(1) {
  height: 8px;
  background: #00ff88;
}

.device-signal.strong .signal-bar:nth-child(2) {
  height: 12px;
  background: #00ff88;
}

.device-signal.strong .signal-bar:nth-child(3) {
  height: 16px;
  background: #00ff88;
}

.device-signal.strong .signal-bar:nth-child(4) {
  height: 20px;
  background: #00ff88;
}

.device-signal.medium .signal-bar:nth-child(1) {
  height: 8px;
  background: #ffaa00;
}

.device-signal.medium .signal-bar:nth-child(2) {
  height: 12px;
  background: #ffaa00;
}

.device-signal.medium .signal-bar:nth-child(3) {
  height: 16px;
  background: #ffaa00;
}

.device-signal.weak .signal-bar:nth-child(1) {
  height: 8px;
  background: #ff4757;
}

/* 告警列表 */
.alert-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.alert-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  border-left: 4px solid;
}

.alert-item.high {
  border-left-color: #ff4757;
}

.alert-item.medium {
  border-left-color: #ffaa00;
}

.alert-item.low {
  border-left-color: #00d4ff;
}

.alert-icon {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  background: rgba(255, 71, 87, 0.2);
  color: #ff4757;
}

.alert-content {
  flex: 1;
}

.alert-title {
  font-size: 14px;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 4px;
}

.alert-desc {
  font-size: 12px;
  color: #00d4ff;
  margin-bottom: 4px;
}

.alert-time {
  font-size: 11px;
  color: #999999;
}

.alert-status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: bold;
}

.alert-status.pending {
  background: rgba(255, 71, 87, 0.2);
  color: #ff4757;
}

.alert-status.processing {
  background: rgba(255, 170, 0, 0.2);
  color: #ffaa00;
}

.alert-status.resolved {
  background: rgba(0, 255, 136, 0.2);
  color: #00ff88;
}

/* 统计网格 */
.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 15px;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  border: 1px solid rgba(0, 212, 255, 0.2);
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  background: rgba(0, 0, 0, 0.3);
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #00d4ff;
  margin-bottom: 4px;
}

.stat-change {
  font-size: 11px;
  font-weight: bold;
}

.stat-change.positive {
  color: #00ff88;
}

.stat-change.negative {
  color: #ff4757;
}

/* 底部状态栏 */
.screen-footer {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  background: rgba(0, 0, 0, 0.3);
  border-top: 1px solid rgba(0, 212, 255, 0.3);
  position: relative;
  z-index: 10;
}

.footer-left,
.footer-right {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 12px;
}

.system-status {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #00ff88;
}

.data-source {
  color: #00d4ff;
}

.update-time {
  color: #ffffff;
}

.version {
  color: #999999;
}

/* 流动装饰条 */
.flowing-bars {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.flow-bar {
  position: absolute;
  width: 200px;
  height: 2px;
  opacity: 0.6;
  animation: flowMove linear infinite;
}

@keyframes flowMove {
  0% {
    transform: translateX(-100vw) rotate(var(--rotation, 0deg));
    opacity: 0;
  }
  10% {
    opacity: 0.6;
  }
  90% {
    opacity: 0.6;
  }
  100% {
    transform: translateX(100vw) rotate(var(--rotation, 0deg));
    opacity: 0;
  }
}

/* 光效轨迹 */
.light-trails {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.trail {
  position: absolute;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(0, 212, 255, 0.3) 0%, transparent 70%);
  animation: trailMove ease-in-out infinite;
}

@keyframes trailMove {
  0%, 100% {
    transform: scale(0.5);
    opacity: 0;
  }
  50% {
    transform: scale(1.5);
    opacity: 0.8;
  }
}

/* 动态光圈 */
.dynamic-rings {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.ring {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid;
  border-radius: 50%;
  animation: ringPulse ease-in-out infinite;
}

@keyframes ringPulse {
  0% {
    transform: translate(-50%, -50%) scale(0.8);
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
  100% {
    transform: translate(-50%, -50%) scale(2);
    opacity: 0;
  }
}

/* 数据流线 */
 .data-streams {
   position: absolute;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   overflow: hidden;
 }
 
 .stream {
   position: absolute;
   width: 1px;
   height: 50px;
   background: linear-gradient(0deg, transparent, #00d4ff, transparent);
   animation: streamFlow linear infinite;
 }
 
 @keyframes streamFlow {
   0% {
     opacity: 0;
     transform: translateY(-50px);
   }
   50% {
     opacity: 1;
   }
   100% {
     opacity: 0;
     transform: translateY(calc(100vh + 50px));
   }
 }
 
 /* 边框流光 */
 .border-lights {
   position: absolute;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   pointer-events: none;
 }
 
 .border-light {
   position: absolute;
   background: linear-gradient(90deg, transparent, #00d4ff, transparent);
   animation: borderFlow 4s linear infinite;
 }
 
 .border-light.top {
   top: 0;
   left: 0;
   width: 100%;
   height: 2px;
 }
 
 .border-light.bottom {
   bottom: 0;
   left: 0;
   width: 100%;
   height: 2px;
   animation-delay: 2s;
 }
 
 .border-light.left {
   top: 0;
   left: 0;
   width: 2px;
   height: 100%;
   background: linear-gradient(0deg, transparent, #00ff88, transparent);
   animation-delay: 1s;
 }
 
 .border-light.right {
   top: 0;
   right: 0;
   width: 2px;
   height: 100%;
   background: linear-gradient(0deg, transparent, #00ff88, transparent);
   animation-delay: 3s;
 }
 
 @keyframes borderFlow {
   0% {
     opacity: 0;
   }
   50% {
     opacity: 1;
   }
   100% {
     opacity: 0;
   }
 }
 
 /* 悬浮光点 */
 .floating-dots {
   position: absolute;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   pointer-events: none;
 }
 
 .floating-dot {
   position: absolute;
   width: 3px;
   height: 3px;
   border-radius: 50%;
   animation: floatDot ease-in-out infinite;
   box-shadow: 0 0 10px currentColor;
 }
 
 @keyframes floatDot {
   0%, 100% {
     transform: translateY(0px) scale(0.8);
     opacity: 0.4;
   }
   50% {
     transform: translateY(-20px) scale(1.2);
     opacity: 1;
   }
 }
 
 /* 数据脉冲 */
 .data-pulses {
   position: absolute;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   pointer-events: none;
 }
 
 .pulse-ring {
   position: absolute;
   width: 20px;
   height: 20px;
   border: 2px solid rgba(0, 255, 136, 0.6);
   border-radius: 50%;
   animation: pulseExpand ease-out infinite;
 }
 
 @keyframes pulseExpand {
   0% {
     transform: scale(0);
     opacity: 1;
   }
   100% {
     transform: scale(8);
     opacity: 0;
   }
 }
 
 /* 扫描线 */
 .scan-lines {
   position: absolute;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   pointer-events: none;
 }
 
 .scan-line {
   position: absolute;
   background: linear-gradient(90deg, transparent, rgba(0, 212, 255, 0.8), transparent);
   animation: scanMove linear infinite;
 }
 
 .horizontal-scan {
   width: 100%;
   height: 1px;
   top: 0;
   animation-duration: 8s;
 }
 
 .vertical-scan {
   width: 1px;
   height: 100%;
   left: 0;
   background: linear-gradient(0deg, transparent, rgba(0, 255, 136, 0.8), transparent);
   animation-duration: 6s;
   animation-delay: 3s;
 }
 
 @keyframes scanMove {
   0% {
     transform: translateY(0);
   }
   100% {
     transform: translateY(100vh);
   }
 }

/* 加载和错误状态样式 */
.loading-overlay,
.error-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 100;
  text-align: center;
}

.loading-spinner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  padding: 30px;
  background: rgba(0, 0, 0, 0.8);
  border: 1px solid #00d4ff;
  border-radius: 12px;
  color: #00d4ff;
}

.loading-spinner i {
  font-size: 32px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.error-message {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  padding: 30px;
  background: rgba(255, 71, 87, 0.1);
  border: 1px solid #ff4757;
  border-radius: 12px;
  color: #ff4757;
}

.error-message i {
  font-size: 32px;
}

.retry-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: #ff4757;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.retry-btn:hover {
  background: #ff3742;
  transform: translateY(-2px);
}

/* 响应式设计 */
@media (max-width: 1600px) {
  .screen-content {
    padding: 15px 30px;
  }
  
  .metrics-row {
    height: 100px;
  }
  
  .metric-card {
    padding: 15px;
  }
  
  .charts-row {
    height: 250px;
  }
}

@media (max-width: 1200px) {
  .charts-row,
  .details-row {
    grid-template-columns: 1fr 1fr;
  }
  
  .metrics-row {
    grid-template-columns: repeat(2, 1fr);
    height: auto;
    gap: 15px;
  }
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.2);
}

::-webkit-scrollbar-thumb {
  background: #00d4ff;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #00b8e6;
}

/* 设备列表样式 */
.device-list {
  max-height: 500px;
  overflow-y: auto;
  padding-right: 8px;
}

.device-item {
  display: flex;
  align-items: center;
  padding: 12px;
  margin-bottom: 8px;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.device-item:hover {
  background: rgba(0, 212, 255, 0.1);
  border-color: #00d4ff;
  transform: translateX(5px);
}

.device-item.online {
  border-left: 4px solid #00ff88;
}

.device-item.offline {
  border-left: 4px solid #666;
}

.device-item.warning {
  border-left: 4px solid #ffa500;
}

.device-item.error {
  border-left: 4px solid #ff4757;
}

.device-icon {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1a1a2e, #16213e);
  color: #00d4ff;
  margin-right: 15px;
  font-size: 20px;
  border: 2px solid rgba(0, 212, 255, 0.3);
  box-shadow: 0 0 15px rgba(0, 212, 255, 0.3);
  transition: all 0.3s ease;
}

.device-icon.online {
  background: linear-gradient(135deg, #0f3460, #16537e);
  color: #00ff88;
  border-color: rgba(0, 255, 136, 0.5);
  box-shadow: 0 0 20px rgba(0, 255, 136, 0.4);
  animation: pulseGreen 2s ease-in-out infinite;
}

@keyframes pulseGreen {
  0%, 100% {
    box-shadow: 0 0 20px rgba(0, 255, 136, 0.4);
  }
  50% {
    box-shadow: 0 0 30px rgba(0, 255, 136, 0.8);
  }
}

.device-icon.warning {
  background: linear-gradient(135deg, #4a2c2a, #6b3e07);
  color: #ffa500;
  border-color: rgba(255, 165, 0, 0.5);
  box-shadow: 0 0 20px rgba(255, 165, 0, 0.4);
  animation: pulseOrange 1.5s ease-in-out infinite;
}

@keyframes pulseOrange {
  0%, 100% {
    box-shadow: 0 0 20px rgba(255, 165, 0, 0.4);
  }
  50% {
    box-shadow: 0 0 35px rgba(255, 165, 0, 0.9);
  }
}

.device-icon.offline {
  background: linear-gradient(135deg, #2a2a2a, #3a3a3a);
  color: #666;
  border-color: rgba(102, 102, 102, 0.3);
  box-shadow: 0 0 10px rgba(102, 102, 102, 0.2);
}

.device-number {
  position: absolute;
  top: -8px;
  right: -8px;
  width: 20px;
  height: 20px;
  background: linear-gradient(135deg, #00d4ff, #0099cc);
  color: #fff;
  border-radius: 50%;
  font-size: 10px;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #001122;
  box-shadow: 0 0 10px rgba(0, 212, 255, 0.5);
}

.device-content {
  flex: 1;
}

.device-title {
  font-size: 14px;
  font-weight: bold;
  color: #00d4ff;
  margin-bottom: 4px;
}

.device-desc {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.device-detail {
  font-size: 11px;
  color: #888;
  margin-bottom: 6px;
  line-height: 1.4;
}

.device-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 4px;
  gap: 12px;
}

.device-time {
  font-size: 11px;
  color: #666;
}

.device-gps {
  font-size: 11px;
  color: #00ff88;
  font-weight: bold;
}

.device-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
  min-width: 120px;
  flex-shrink: 0;
}

.device-status {
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  text-align: center;
  min-width: 100px;
  width: auto;
  white-space: nowrap;
  display: inline-block;
}

.device-signal {
  display: flex;
  align-items: center;
  gap: 6px;
}

.signal-label {
  font-size: 10px;
  color: #666;
}

.signal-bars {
  display: flex;
  align-items: flex-end;
  gap: 2px;
}

.signal-bar {
  width: 4px;
  background: rgba(51, 51, 51, 0.5);
  border-radius: 2px;
  transition: all 0.3s ease;
  border: 1px solid rgba(0, 212, 255, 0.1);
}

.signal-bar:nth-child(1) { height: 8px; }
.signal-bar:nth-child(2) { height: 11px; }
.signal-bar:nth-child(3) { height: 14px; }
.signal-bar:nth-child(4) { height: 17px; }
.signal-bar:nth-child(5) { height: 20px; }

.signal-bar.active {
  background: linear-gradient(to top, #00ff88, #00d4ff);
  box-shadow: 0 0 8px rgba(0, 255, 136, 0.6);
  border-color: rgba(0, 255, 136, 0.8);
}

.signal-bar.active.signal-online {
  background: linear-gradient(to top, #00ff88, #00cc77);
  box-shadow: 0 0 10px rgba(0, 255, 136, 0.8);
  animation: signalPulse 2s ease-in-out infinite;
}

.signal-bar.active.signal-warning {
  background: linear-gradient(to top, #ffa500, #ff8800);
  box-shadow: 0 0 10px rgba(255, 165, 0, 0.8);
  border-color: rgba(255, 165, 0, 0.8);
  animation: signalPulseWarning 1.5s ease-in-out infinite;
}

@keyframes signalPulse {
  0%, 100% {
    opacity: 1;
    transform: scaleY(1);
  }
  50% {
    opacity: 0.7;
    transform: scaleY(1.1);
  }
}

@keyframes signalPulseWarning {
  0%, 100% {
    opacity: 1;
    transform: scaleY(1);
  }
  50% {
    opacity: 0.6;
    transform: scaleY(1.2);
  }
}

.device-status.online {
  background: rgba(0, 255, 136, 0.2);
  color: #00ff88;
  border: 1px solid #00ff88;
}

.device-status.offline {
  background: rgba(102, 102, 102, 0.2);
  color: #666;
  border: 1px solid #666;
}

.device-status.warning {
  background: rgba(255, 165, 0, 0.2);
  color: #ffa500;
  border: 1px solid #ffa500;
}

.device-status.error {
  background: rgba(255, 71, 87, 0.2);
  color: #ff4757;
  border: 1px solid #ff4757;
}

/* 告警列表样式 */
.alert-list {
  max-height: 400px;
  overflow-y: auto;
}

.alert-item {
  display: flex;
  align-items: center;
  padding: 12px;
  margin-bottom: 8px;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 71, 87, 0.2);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.alert-item:hover {
  background: rgba(255, 71, 87, 0.1);
  border-color: #ff4757;
  transform: translateX(5px);
}

.alert-item.critical {
  border-left: 4px solid #ff4757;
}

.alert-item.high {
  border-left: 4px solid #ffa500;
}

.alert-item.medium {
  border-left: 4px solid #ffeb3b;
}

.alert-item.low {
  border-left: 4px solid #4caf50;
}

.alert-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 71, 87, 0.2);
  color: #ff4757;
  margin-right: 15px;
  font-size: 18px;
}

.alert-content {
  flex: 1;
}

.alert-title {
  font-size: 14px;
  font-weight: bold;
  color: #00d4ff;
  margin-bottom: 4px;
}

.alert-desc {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.alert-time {
  font-size: 11px;
  color: #666;
}

.alert-status {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  text-align: center;
  min-width: 60px;
}

.alert-status.pending {
  background: rgba(255, 165, 0, 0.2);
  color: #ffa500;
  border: 1px solid #ffa500;
}

.alert-status.processing {
  background: rgba(0, 212, 255, 0.2);
  color: #00d4ff;
  border: 1px solid #00d4ff;
}

.alert-status.resolved {
  background: rgba(0, 255, 136, 0.2);
  color: #00ff88;
  border: 1px solid #00ff88;
}


</style>