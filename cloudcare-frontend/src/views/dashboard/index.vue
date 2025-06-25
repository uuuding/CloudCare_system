<template>
  <div class="dashboard-container">
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
    <el-row :gutter="20" class="data-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="data-card">
          <div class="data-header">
            <span class="data-title">老人总数</span>
            <el-icon class="data-icon" :size="40" color="#409EFF"><User /></el-icon>
          </div>
          <div class="data-content">
            <div class="data-value">{{ statistics.elderlyCount }}</div>
            <div class="data-compare" :class="statistics.elderlyIncrease >= 0 ? 'up' : 'down'">
              <el-icon><component :is="statistics.elderlyIncrease >= 0 ? 'ArrowUp' : 'ArrowDown'" /></el-icon>
              <span>{{ Math.abs(statistics.elderlyIncrease) }}%</span>
              <span class="compare-text">较上月</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="data-card">
          <div class="data-header">
            <span class="data-title">健康记录</span>
            <el-icon class="data-icon" :size="40" color="#67C23A"><Notebook /></el-icon>
          </div>
          <div class="data-content">
            <div class="data-value">{{ statistics.recordCount }}</div>
            <div class="data-compare" :class="statistics.recordIncrease >= 0 ? 'up' : 'down'">
              <el-icon><component :is="statistics.recordIncrease >= 0 ? 'ArrowUp' : 'ArrowDown'" /></el-icon>
              <span>{{ Math.abs(statistics.recordIncrease) }}%</span>
              <span class="compare-text">较上月</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="data-card">
          <div class="data-header">
            <span class="data-title">健康预警</span>
            <el-icon class="data-icon" :size="40" color="#F56C6C"><Warning /></el-icon>
          </div>
          <div class="data-content">
            <div class="data-value">{{ statistics.warningCount }}</div>
            <div class="data-compare" :class="statistics.warningIncrease >= 0 ? 'up' : 'down'">
              <el-icon><component :is="statistics.warningIncrease >= 0 ? 'ArrowUp' : 'ArrowDown'" /></el-icon>
              <span>{{ Math.abs(statistics.warningIncrease) }}%</span>
              <span class="compare-text">较上月</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="data-card">
          <div class="data-header">
            <span class="data-title">系统用户</span>
            <el-icon class="data-icon" :size="40" color="#E6A23C"><UserFilled /></el-icon>
          </div>
          <div class="data-content">
            <div class="data-value">{{ statistics.userCount }}</div>
            <div class="data-compare" :class="statistics.userIncrease >= 0 ? 'up' : 'down'">
              <el-icon><component :is="statistics.userIncrease >= 0 ? 'ArrowUp' : 'ArrowDown'" /></el-icon>
              <span>{{ Math.abs(statistics.userIncrease) }}%</span>
              <span class="compare-text">较上月</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表和列表 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 健康预警趋势图 -->
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>健康预警趋势</span>
              <el-radio-group v-model="warningChartTimeRange" size="small">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">全年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-container" ref="warningChartRef"></div>
        </el-card>
      </el-col>

      <!-- 健康记录分布图 -->
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>健康记录分布</span>
              <el-radio-group v-model="recordChartType" size="small">
                <el-radio-button label="type">类型分布</el-radio-button>
                <el-radio-button label="level">等级分布</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-container" ref="recordChartRef"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="list-row">
      <!-- 最近健康预警 -->
      <el-col :xs="24" :lg="12">
        <el-card class="list-card">
          <template #header>
            <div class="card-header">
              <span>最近健康预警</span>
              <el-button type="primary" link @click="goToWarningList">查看更多</el-button>
            </div>
          </template>
          <el-table :data="recentWarnings" style="width: 100%" v-loading="warningsLoading">
            <el-table-column label="预警类型" width="100">
              <template #default="scope">
                <el-tag :type="getWarningTypeTag(scope.row.warningType)">
                  {{ getWarningTypeLabel(scope.row.warningType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="预警内容" prop="warningContent" show-overflow-tooltip />
            <el-table-column label="预警时间" width="160">
              <template #default="scope">
                <span>{{ formatDateTime(scope.row.warningTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.processStatus === 0 ? 'danger' : 'success'">
                  {{ scope.row.processStatus === 0 ? '未处理' : '已处理' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 系统公告 -->
      <el-col :xs="24" :lg="12">
        <el-card class="list-card">
          <template #header>
            <div class="card-header">
              <span>系统公告</span>
              <el-button type="primary" link>查看更多</el-button>
            </div>
          </template>
          <el-empty v-if="announcements.length === 0" description="暂无公告" />
          <div v-else class="announcement-list">
            <div v-for="(item, index) in announcements" :key="index" class="announcement-item">
              <div class="announcement-title">
                <el-icon><Bell /></el-icon>
                <span>{{ item.title }}</span>
              </div>
              <div class="announcement-content">{{ item.content }}</div>
              <div class="announcement-footer">
                <span class="announcement-time">{{ formatDateTime(item.createTime) }}</span>
                <span class="announcement-author">{{ item.createBy }}</span>
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
import { getRecentWarnings } from '@/api/health/warning'
import * as echarts from 'echarts'

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
const statistics = reactive({
  elderlyCount: 128,
  elderlyIncrease: 5.2,
  recordCount: 1024,
  recordIncrease: 12.8,
  warningCount: 36,
  warningIncrease: -8.5,
  userCount: 256,
  userIncrease: 3.6
})

// 图表相关
const warningChartRef = ref(null)
const recordChartRef = ref(null)
let warningChart = null
let recordChart = null
const warningChartTimeRange = ref('week')
const recordChartType = ref('type')

// 初始化健康预警趋势图
const initWarningChart = () => {
  if (!warningChartRef.value) return
  
  warningChart = echarts.init(warningChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['轻度预警', '中度预警', '重度预警']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '轻度预警',
        type: 'line',
        stack: 'Total',
        data: [3, 5, 2, 4, 6, 3, 2],
        itemStyle: {
          color: '#67C23A'
        }
      },
      {
        name: '中度预警',
        type: 'line',
        stack: 'Total',
        data: [2, 3, 4, 1, 2, 5, 3],
        itemStyle: {
          color: '#E6A23C'
        }
      },
      {
        name: '重度预警',
        type: 'line',
        stack: 'Total',
        data: [1, 2, 0, 3, 1, 2, 1],
        itemStyle: {
          color: '#F56C6C'
        }
      }
    ]
  }
  
  warningChart.setOption(option)
}

// 初始化健康记录分布图
const initRecordChart = () => {
  if (!recordChartRef.value) return
  
  recordChart = echarts.init(recordChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 10,
      data: ['体温', '血压', '血糖', '心率', '血氧', '体重']
    },
    series: [
      {
        name: '健康记录',
        type: 'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 335, name: '体温' },
          { value: 310, name: '血压' },
          { value: 234, name: '血糖' },
          { value: 135, name: '心率' },
          { value: 154, name: '血氧' },
          { value: 104, name: '体重' }
        ]
      }
    ]
  }
  
  recordChart.setOption(option)
}

// 最近健康预警
const recentWarnings = ref([])
const warningsLoading = ref(false)

// 获取最近健康预警
const getWarnings = () => {
  warningsLoading.value = true
  getRecentWarnings(5).then(res => {
    recentWarnings.value = res.data
    warningsLoading.value = false
  }).catch(() => {
    warningsLoading.value = false
  })
}

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

// 获取预警类型标签类型
const getWarningTypeTag = (type) => {
  switch (type) {
    case 1: return 'danger'
    case 2: return 'warning'
    case 3: return 'warning'
    case 4: return 'danger'
    case 5: return 'danger'
    case 6: return 'warning'
    default: return 'info'
  }
}

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
  getWarnings()
  
  // 初始化图表
  setTimeout(() => {
    initWarningChart()
    initRecordChart()
  }, 100)
  
  // 监听窗口大小变化
  window.addEventListener('resize', handleResize)
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

.data-row {
  margin-bottom: 20px;
}

.data-card {
  height: 120px;
  margin-bottom: 20px;
}

.data-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.data-title {
  font-size: 16px;
  color: #606266;
}

.data-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.data-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.data-compare {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.data-compare.up {
  color: #67C23A;
}

.data-compare.down {
  color: #F56C6C;
}

.data-compare .el-icon {
  margin-right: 5px;
}

.compare-text {
  margin-left: 5px;
  color: #909399;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-container {
  height: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.list-row {
  margin-bottom: 20px;
}

.list-card {
  margin-bottom: 20px;
}

.announcement-list {
  max-height: 400px;
  overflow-y: auto;
}

.announcement-item {
  padding: 15px 0;
  border-bottom: 1px solid #EBEEF5;
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-title {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}

.announcement-title .el-icon {
  margin-right: 10px;
  color: #409EFF;
}

.announcement-content {
  margin-bottom: 10px;
  color: #606266;
  line-height: 1.5;
}

.announcement-footer {
  display: flex;
  justify-content: space-between;
  color: #909399;
  font-size: 12px;
}
</style>