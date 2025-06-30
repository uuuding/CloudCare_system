<template>
  <div class="dashboard-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>云护理管理系统</h1>
      <p class="page-description">智慧养老，贴心守护每一位老人的健康与安全</p>
    </div>

    <!-- 欢迎信息 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="welcome-card">
          <div class="welcome-info">
            <div class="welcome-avatar">
              <el-avatar :size="80" :src="userInfo.avatar || defaultAvatar" />
            </div>

            <div class="welcome-text">
              <h2>欢迎回来，{{ userInfo.realName || userInfo.username }}</h2>
              <p>{{ welcomeMessage }}</p>
            </div>
          </div>
          <div class="welcome-date">
            <div class="date-info">
              <p class="date">{{ currentDate }}</p>
              <p class="time">{{ currentTime }}</p>
            </div>
            <div class="weather-info" v-if="weather.temp">
              <el-icon :size="24"><Sunny /></el-icon>
              <span>{{ weather.temp }}°C {{ weather.text }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card elderly-card" v-loading="stats.elderlyCount.loading">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon :size="40"><User /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.elderlyCount.value }}</h3>
              <p>老人总数</p>
              <span :class="['trend', stats.elderlyCount.trendType]">{{ stats.elderlyCount.trend }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card alert-card" v-loading="stats.alertCount.loading">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon :size="40"><Warning /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.alertCount.value }}</h3>
              <p>健康预警</p>
              <span :class="['trend', stats.alertCount.trendType]">{{ stats.alertCount.trend }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card health-card" v-loading="stats.healthRecords.loading">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon :size="40"><Monitor /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.healthRecords.value }}</h3>
              <p>健康记录</p>
              <span :class="['trend', stats.healthRecords.trendType]">{{ stats.healthRecords.trend }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card device-card" v-loading="stats.onlineDevices.loading">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon :size="40"><Connection /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.onlineDevices.value }}</h3>
              <p>在线设备</p>
              <span :class="['trend', stats.onlineDevices.trendType]">{{ stats.onlineDevices.trend }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷操作 -->
    <el-row :gutter="20" class="quick-actions-row">
      <el-col :span="24">
        <el-card class="quick-actions-card">
          <template #header>
            <div class="card-header">
              <span>快捷操作</span>
              <el-icon><Operation /></el-icon>
            </div>
          </template>
          <div class="quick-actions">
            <div class="action-item" @click="navigateTo('/health/assessment')">
              <div class="action-icon health">
                <el-icon :size="24"><DataAnalysis /></el-icon>
              </div>
              <span>健康评估</span>
            </div>
            <div class="action-item" @click="navigateTo('/user/elder-account')">
              <div class="action-icon user">
                <el-icon :size="24"><UserFilled /></el-icon>
              </div>
              <span>老人管理</span>
            </div>
            <div class="action-item" @click="navigateTo('/device')">
              <div class="action-icon device">
                <el-icon :size="24"><Monitor /></el-icon>
              </div>
              <span>设备管理</span>
            </div>
            <div class="action-item" @click="navigateTo('/intervention-plan')">
              <div class="action-icon intervention">
                <el-icon :size="24"><DocumentAdd /></el-icon>
              </div>
              <span>干预方案</span>
            </div>
            <div class="action-item" @click="navigateTo('/reports')">
              <div class="action-icon report">
                <el-icon :size="24"><TrendCharts /></el-icon>
              </div>
              <span>数据报表</span>
            </div>
            <div class="action-item" @click="navigateTo('/medical/appointment')">
              <div class="action-icon medical">
                <el-icon :size="24"><Calendar /></el-icon>
              </div>
              <span>医疗预约</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 系统公告和最新动态 -->
    <el-row :gutter="20" class="content-row">
      <el-col :span="12">
        <el-card class="announcement-card">
          <template #header>
            <div class="card-header">
              <span>系统公告</span>
              <el-icon><Bell /></el-icon>
            </div>
          </template>
          <div class="announcement-list">
            <div v-for="(item, index) in announcements" :key="index" class="announcement-item">
              <div class="announcement-title">
                <el-icon><InfoFilled /></el-icon>
                <span>{{ item.title }}</span>
              </div>
              <p class="announcement-content">{{ item.content }}</p>
              <div class="announcement-meta">
                <span>{{ item.createBy }}</span>
                <span>{{ formatDateTime(item.createTime) }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="activity-card">
          <template #header>
            <div class="card-header">
              <span>最新动态</span>
              <el-icon><Clock /></el-icon>
            </div>
          </template>
          <div class="activity-list">
            <div v-for="(item, index) in recentActivities" :key="index" class="activity-item">
              <div class="activity-icon" :class="item.type">
                <el-icon><component :is="item.icon" /></el-icon>
              </div>
              <div class="activity-content">
                <p class="activity-title">{{ item.title }}</p>
                <p class="activity-desc">{{ item.description }}</p>
                <span class="activity-time">{{ item.time }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getAllAlerts } from '@/api/healthAlert'
import { getDeviceList } from '@/api/device'
import { getAllObservations } from '@/api/elderlyObservations'
import { getAllElderlyProfiles } from '@/api/elderlyProfile'
import {
  User, Warning, Monitor, Connection, Operation, Bell, Clock,
  DataAnalysis, UserFilled, DocumentAdd, TrendCharts, Calendar,
  InfoFilled, Sunny
} from '@element-plus/icons-vue'

// 默认头像
const defaultAvatar = new URL('@/assets/default-avatar.png', import.meta.url).href

// 路由实例
const router = useRouter()

// 用户状态
const userStore = useUserStore()

// 用户信息
const userInfo = computed(() => {
  return {
    username: userStore.username,
    realName: userStore.realName,
    avatar: userStore.avatar
  }
})

// 欢迎消息
const welcomeMessage = computed(() => {
  const hour = new Date().getHours()
  if (hour < 6) {
    return '夜深了，注意休息！'
  } else if (hour < 9) {
    return '早上好，祝您有个愉快的一天！'
  } else if (hour < 12) {
    return '上午好，工作顺利！'
  } else if (hour < 14) {
    return '中午好，记得午休哦！'
  } else if (hour < 18) {
    return '下午好，继续加油！'
  } else if (hour < 22) {
    return '晚上好，辛苦了一天！'
  } else {
    return '夜深了，注意休息！'
  }
})

// 当前日期和时间
const currentDate = ref('')
const currentTime = ref('')
let timer = null

// 更新时间
const updateTime = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = now.getMonth() + 1
  const day = now.getDate()
  const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  const weekday = weekdays[now.getDay()]
  
  currentDate.value = `${year}年${month}月${day}日 ${weekday}`
  
  const hours = String(now.getHours()).padStart(2, '0')
  const minutes = String(now.getMinutes()).padStart(2, '0')
  const seconds = String(now.getSeconds()).padStart(2, '0')
  
  currentTime.value = `${hours}:${minutes}:${seconds}`
}

// 天气信息
const weather = reactive({
  temp: 25,
  text: '晴',
  city: '北京'
})

// 统计数据
const stats = reactive({
  elderlyCount: {
    value: 0,
    trend: '',
    trendType: 'info',
    loading: true
  },
  alertCount: {
    value: 0,
    trend: '',
    trendType: 'info',
    loading: true
  },
  healthRecords: {
    value: 0,
    trend: '',
    trendType: 'info',
    loading: true
  },
  onlineDevices: {
    value: 0,
    trend: '',
    trendType: 'info',
    loading: true
  }
})

// 最新动态数据
const recentActivities = ref([
  {
    id: 1,
    type: 'health',
    icon: 'Health',
    title: '健康数据异常提醒',
    description: '张老先生的血压数据超出正常范围，建议及时关注',
    time: '2分钟前'
  },
  {
    id: 2,
    type: 'user',
    icon: 'User',
    title: '新用户注册',
    description: '李阿姨已成功注册并完成基础信息录入',
    time: '15分钟前'
  },
  {
    id: 3,
    type: 'device',
    icon: 'Monitor',
    title: '设备连接状态',
    description: '智能手环设备离线，请检查网络连接',
    time: '30分钟前'
  },
  {
    id: 4,
    type: 'medical',
    icon: 'FirstAidKit',
    title: '医疗预约提醒',
    description: '王老太太明天上午10点的体检预约即将到期',
    time: '1小时前'
  },
  {
    id: 5,
    type: 'intervention',
    icon: 'Document',
    title: '干预方案更新',
    description: '为赵老先生制定了新的康复训练计划',
    time: '2小时前'
  },
  {
    id: 6,
    type: 'health',
    icon: 'TrendCharts',
    title: '健康报告生成',
    description: '本周健康数据分析报告已生成，请查看',
    time: '3小时前'
  }
])

// 导航方法
  const navigateTo = (path) => {
    router.push(path)
  }

  // 获取统计数据的方法
  const fetchStats = async () => {
    try {
      // 获取老人档案总数
      const elderlyResponse = await getAllElderlyProfiles()
      console.log('老人档案数据完整响应:', JSON.stringify(elderlyResponse, null, 2))
      
      let elderlyCount = 0
      
      // 检查多种可能的成功状态
      const isSuccess = elderlyResponse && (
        elderlyResponse.success === true ||
        elderlyResponse.code === 200 ||
        elderlyResponse.status === 200 ||
        !elderlyResponse.hasOwnProperty('success') // 如果没有success字段，假设成功
      )
      
      if (isSuccess) {
        // 尝试从多种可能的数据结构中获取数据
        const elderlyData = elderlyResponse.data?.records || // 分页数据结构
                           elderlyResponse.data?.list ||    // 列表数据结构
                           elderlyResponse.data ||          // 直接data字段
                           elderlyResponse.result ||       // result字段
                           elderlyResponse.records ||      // 直接records字段
                           elderlyResponse.list ||         // 直接list字段
                           elderlyResponse                 // 整个响应就是数组
        
        console.log('解析的老人档案数据:', elderlyData)
        
        if (Array.isArray(elderlyData)) {
          elderlyCount = elderlyData.length
          console.log('老人档案数组长度:', elderlyCount)
        } else if (elderlyData && typeof elderlyData === 'object') {
          // 检查是否有总数字段
          elderlyCount = elderlyData.total || elderlyData.count || elderlyData.totalElements || 0
          console.log('从对象获取的老人档案总数:', elderlyCount)
          
          // 如果total为0但有records，使用records长度
          if (elderlyCount === 0 && elderlyData.records && Array.isArray(elderlyData.records)) {
            elderlyCount = elderlyData.records.length
            console.log('从records获取的老人档案数量:', elderlyCount)
          }
        }
        
        console.log('最终老人档案数量:', elderlyCount)
      } else {
        console.warn('老人档案数据API调用失败:', elderlyResponse)
      }
      
      stats.elderlyCount.value = elderlyCount
      stats.elderlyCount.trend = elderlyCount > 0 ? `共${elderlyCount}人` : '暂无老人档案数据'
      stats.elderlyCount.trendType = elderlyCount > 0 ? 'positive' : 'info'
      stats.elderlyCount.loading = false
    } catch (error) {
      console.error('获取老人档案数据失败:', error)
      stats.elderlyCount.value = 0
      stats.elderlyCount.trend = '获取失败'
      stats.elderlyCount.trendType = 'negative'
      stats.elderlyCount.loading = false
    }

    try {
      // 获取健康预警数据
      const alertResponse = await getAllAlerts()
      console.log('预警数据响应:', alertResponse)
      
      if (alertResponse && (alertResponse.code === 200 || alertResponse.status === 200)) {
        const alertData = alertResponse.data || alertResponse.result || alertResponse
        const alerts = Array.isArray(alertData) ? alertData : []
        const activeAlerts = alerts.filter(alert => 
          alert.status === '未处理' || alert.status === 'PENDING' || alert.status === 'ACTIVE'
        )
        stats.alertCount.value = activeAlerts.length
        stats.alertCount.trend = `${activeAlerts.length}条待处理`
        stats.alertCount.trendType = activeAlerts.length > 0 ? 'negative' : 'positive'
      } else {
        stats.alertCount.value = 0
        stats.alertCount.trend = '暂无预警'
        stats.alertCount.trendType = 'positive'
      }
      stats.alertCount.loading = false
    } catch (error) {
      console.error('获取预警数据失败:', error)
      stats.alertCount.value = 0
      stats.alertCount.trend = '获取失败'
      stats.alertCount.trendType = 'negative'
      stats.alertCount.loading = false
    }

    try {
      // 获取健康记录数据
      const observationResponse = await getAllObservations()
      console.log('健康记录响应:', observationResponse)
      
      let healthRecordsCount = 0
      let todayRecordsCount = 0
      
      if (observationResponse && observationResponse.success) {
        const observationData = observationResponse.data
        
        if (Array.isArray(observationData)) {
          // 显示总记录数
          healthRecordsCount = observationData.length
          
          // 计算今日新增数量用于趋势显示
          const today = new Date().toISOString().split('T')[0]
          const todayRecords = observationData.filter(record => {
            const recordDate = record.observationTime || record.createTime || record.date || record.time
            return recordDate && recordDate.startsWith(today)
          })
          todayRecordsCount = todayRecords.length
        } else if (observationData && typeof observationData === 'object') {
          // 如果是对象，可能包含total字段
          healthRecordsCount = observationData.total || observationData.count || 0
          if (healthRecordsCount === 0 && observationData.records) {
            healthRecordsCount = observationData.records.length
          }
        }
      }
      
      stats.healthRecords.value = healthRecordsCount
      stats.healthRecords.trend = todayRecordsCount > 0 ? `今日新增${todayRecordsCount}条` : '今日暂无新增'
      stats.healthRecords.trendType = todayRecordsCount > 0 ? 'positive' : 'info'
      stats.healthRecords.loading = false
    } catch (error) {
      console.error('获取健康记录数据失败:', error)
      stats.healthRecords.value = 0
      stats.healthRecords.trend = '获取失败'
      stats.healthRecords.trendType = 'negative'
      stats.healthRecords.loading = false
    }

    try {
      // 获取设备数据
      const deviceResponse = await getDeviceList({})
      console.log('设备数据响应:', deviceResponse)
      
      let onlineDevicesCount = 0
      let totalDevicesCount = 0
      
      if (deviceResponse) {
        const isSuccess = deviceResponse.code === 200 || deviceResponse.status === 200 || deviceResponse.success === true
        
        if (isSuccess || !deviceResponse.code) {
          // 尝试多种数据结构
          const deviceData = deviceResponse.data?.records || // 分页数据
                           deviceResponse.data?.list ||    // 列表数据
                           deviceResponse.data ||          // 直接data
                           deviceResponse.result ||       // result字段
                           deviceResponse.records ||      // 直接records
                           deviceResponse.list ||         // 直接list
                           deviceResponse                 // 直接返回数组
          
          if (Array.isArray(deviceData)) {
            totalDevicesCount = deviceData.length
            // 根据deviceStatus判断设备状态：1表示正常（在线），0表示停用，2表示维修中，3表示已报废
            const onlineDevices = deviceData.filter(device => 
              device.deviceStatus === 1 || device.deviceStatus === '1'
            )
            onlineDevicesCount = onlineDevices.length
            console.log('设备总数:', totalDevicesCount, '在线设备数:', onlineDevicesCount)
          } else if (deviceData && typeof deviceData === 'object') {
            // 如果是对象，可能包含total和online字段
            totalDevicesCount = deviceData.total || deviceData.count || 0
            onlineDevicesCount = deviceData.online || deviceData.onlineCount || 0
          }
        }
      }
      
      stats.onlineDevices.value = onlineDevicesCount
      const percentage = totalDevicesCount > 0 ? Math.round((onlineDevicesCount / totalDevicesCount) * 100) : 0
      stats.onlineDevices.trend = totalDevicesCount > 0 ? `${percentage}%在线` : '暂无设备'
      stats.onlineDevices.trendType = percentage >= 90 ? 'positive' : percentage >= 70 ? 'info' : 'negative'
      stats.onlineDevices.loading = false
    } catch (error) {
      console.error('获取设备数据失败:', error)
      stats.onlineDevices.value = 0
      stats.onlineDevices.trend = '获取失败'
      stats.onlineDevices.trendType = 'negative'
      stats.onlineDevices.loading = false
    }
  }


// 图表相关
const warningChartRef = ref(null)
const recordChartRef = ref(null)
let warningChart = null
let recordChart = null
const warningChartTimeRange = ref('week')
const recordChartType = ref('type')


// 系统公告
const announcements = ref([
  {
    title: '系统升级通知',
    content: '尊敬的用户，系统将于2023年6月15日凌晨2:00-4:00进行升级维护，期间系统将暂停服务，请提前做好相关工作安排。',
    createTime: '2023-06-10 10:00:00',
    createBy: '系统管理员'
  },
  {
    title: '健康监测新功能上线',
    content: '云护理系统新增健康监测功能，支持实时监测老人的体温、血压、心率等健康指标，欢迎使用。',
    createTime: '2023-06-05 14:30:00',
    createBy: '系统管理员'
  }
])

// 预警类型选项
const warningTypeOptions = [
  { value: 1, label: '体温异常' },
  { value: 2, label: '血压异常' },
  { value: 3, label: '血糖异常' },
  { value: 4, label: '心率异常' },
  { value: 5, label: '血氧异常' },
  { value: 6, label: '体重异常' },
  { value: 7, label: '其他' }
]

// 获取预警类型标签文本
const getWarningTypeLabel = (type) => {
  const option = warningTypeOptions.find(item => item.value === type)
  return option ? option.label : '未知'
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString()
}

// 跳转到健康预警列表
const goToWarningList = () => {
  router.push('/health/warning')
}

// 监听窗口大小变化，重绘图表
const handleResize = () => {
  if (warningChart) {
    warningChart.resize()
  }
  if (recordChart) {
    recordChart.resize()
  }
}

// 页面加载时初始化
onMounted(() => {
  // 初始化时间并定时更新
  updateTime()
  timer = setInterval(updateTime, 1000)
  
  // 获取最近健康预警
  //getWarnings()
  
  // 初始化图表
  setTimeout(() => {
    //initWarningChart()
    //initRecordChart()
  }, 100)
  
  // 监听窗口大小变化
  window.addEventListener('resize', handleResize)
  
  // 获取统计数据
  fetchStats()
})

// 页面卸载前清理
onBeforeUnmount(() => {
  // 清除定时器
  if (timer) {
    clearInterval(timer)
    timer = null
  }
  
  // 销毁图表实例
  if (warningChart) {
    warningChart.dispose()
    warningChart = null
  }
  if (recordChart) {
    recordChart.dispose()
    recordChart = null
  }
  
  // 移除事件监听
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 页面标题样式 */
.page-header {
  text-align: center;
  margin-bottom: 32px;
  padding: 20px 0;
}

.page-header h1 {
  font-size: 36px;
  font-weight: bold;
  color: #2c3e50;
  margin: 0 0 8px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-description {
  font-size: 18px;
  color: #7f8c8d;
  margin: 0;
  line-height: 1.5;
}

.welcome-card {
  margin-bottom: 20px;
  background-image: linear-gradient(to right, #1890ff, #36cfc9);
  color: white;
}

.welcome-info {
  display: flex;
  align-items: center;
}

.welcome-avatar {
  margin-right: 20px;
}

.welcome-text h2 {
  margin: 0 0 10px 0;
  font-size: 24px;
}

.welcome-text p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.welcome-date {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.date-info .date {
  font-size: 16px;
  margin: 0;
}

.date-info .time {
  font-size: 24px;
  font-weight: bold;
  margin: 5px 0 0 0;
}

.weather-info {
  display: flex;
  align-items: center;
  font-size: 18px;
}

.weather-info .el-icon {
  margin-right: 10px;
}

/* 统计卡片样式 */
.stats-row {
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  color: white;
}

.elderly-card .stat-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.alert-card .stat-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.health-card .stat-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.device-card .stat-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info h3 {
  font-size: 28px;
  font-weight: bold;
  margin: 0 0 4px 0;
  color: #2c3e50;
}

.stat-info p {
  font-size: 14px;
  color: #7f8c8d;
  margin: 0 0 8px 0;
}

.trend {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 12px;
  font-weight: 500;
}

.trend.positive {
  background-color: #d4edda;
  color: #155724;
}

.trend.negative {
  background-color: #f8d7da;
  color: #721c24;
}

.trend.info {
  background-color: #d1ecf1;
  color: #0c5460;
}

/* 快捷操作样式 */
.quick-actions-row {
  margin-bottom: 24px;
}

.quick-actions-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 20px;
  padding: 20px 0;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border-radius: 12px;
  background: #fff;
  border: 2px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

.action-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
  color: white;
}

.action-icon.health {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.action-icon.user {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.action-icon.device {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.action-icon.intervention {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.action-icon.report {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.action-icon.medical {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
}

.action-item span {
  font-size: 14px;
  font-weight: 500;
  color: #2c3e50;
}

/* 内容区域样式 */
.content-row {
  margin-bottom: 24px;
}

.announcement-card,
.activity-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  height: 400px;
}

.announcement-list,
.activity-list {
  max-height: 320px;
  overflow-y: auto;
}

.announcement-item {
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-title {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  font-weight: 600;
  color: #2c3e50;
}

.announcement-title .el-icon {
  margin-right: 8px;
  color: #409eff;
}

.announcement-content {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin: 0 0 8px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.announcement-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: white;
  flex-shrink: 0;
}

.activity-icon.health {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.activity-icon.user {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.activity-icon.device {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.activity-icon.medical {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.activity-icon.intervention {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 4px 0;
}

.activity-desc {
  font-size: 13px;
  color: #666;
  margin: 0 0 8px 0;
  line-height: 1.4;
}

.activity-time {
  font-size: 12px;
  color: #999;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .quick-actions {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-row .el-col {
    margin-bottom: 16px;
  }
  
  .quick-actions {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .content-row .el-col {
    margin-bottom: 16px;
  }
}

@media (max-width: 480px) {
  .dashboard-container {
    padding: 16px;
  }
  
  .page-header h1 {
    font-size: 28px;
  }
  
  .quick-actions {
    grid-template-columns: 1fr;
  }
}

</style>