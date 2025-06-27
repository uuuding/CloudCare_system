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

  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

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


.data-compare .el-icon {
  margin-right: 5px;
}

.announcement-title .el-icon {
  margin-right: 10px;
  color: #409EFF;
}

</style>