<template>
  <div class="elderly-dashboard-container">
    <!-- 欢迎信息 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="welcome-card">
          <div class="welcome-info">
            <div class="welcome-avatar">
              <el-avatar :size="80" :src="userInfo.avatar || defaultAvatar" />
            </div>
            <div class="welcome-text">
              <h2>您好，{{ userInfo.realName || userInfo.username }}</h2>
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

    <!-- 健康状态卡片 -->
    <el-row :gutter="20" class="health-status-row">
      <el-col :span="8">
        <el-card class="health-card heart-rate">
          <div class="health-content">
            <div class="health-icon">
              <el-icon :size="40"><Monitor /></el-icon>
            </div>
            <div class="health-info">
              <h3>{{ healthData.heartRate || '--' }}</h3>
              <p>心率 (次/分)</p>
              <span :class="['status', getHealthStatus(healthData.heartRate, 'heartRate')]">{{ getHealthStatusText(healthData.heartRate, 'heartRate') }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="health-card blood-pressure">
          <div class="health-content">
            <div class="health-icon">
              <el-icon :size="40"><TrendCharts /></el-icon>
            </div>
            <div class="health-info">
              <h3>{{ healthData.bloodPressure || '--' }}</h3>
              <p>血压 (mmHg)</p>
              <span :class="['status', getHealthStatus(healthData.bloodPressure, 'bloodPressure')]">{{ getHealthStatusText(healthData.bloodPressure, 'bloodPressure') }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="health-card blood-sugar">
          <div class="health-content">
            <div class="health-icon">
              <el-icon :size="40"><DataAnalysis /></el-icon>
            </div>
            <div class="health-info">
              <h3>{{ healthData.bloodSugar || '--' }}</h3>
              <p>血糖 (mmol/L)</p>
              <span :class="['status', getHealthStatus(healthData.bloodSugar, 'bloodSugar')]">{{ getHealthStatusText(healthData.bloodSugar, 'bloodSugar') }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷功能 -->
    <el-row :gutter="20" class="quick-functions-row">
      <el-col :span="24">
        <el-card class="quick-functions-card">
          <template #header>
            <div class="card-header">
              <span>常用功能</span>
              <el-icon><Operation /></el-icon>
            </div>
          </template>
          <div class="quick-functions">
            <div class="function-item" @click="navigateTo('/health/assessment')">
              <div class="function-icon health">
                <el-icon :size="32"><DataAnalysis /></el-icon>
              </div>
              <span>健康评估</span>
            </div>
            <div class="function-item" @click="navigateTo('/medical/appointment')">
              <div class="function-icon medical">
                <el-icon :size="32"><Calendar /></el-icon>
              </div>
              <span>预约挂号</span>
            </div>
            <div class="function-item" @click="navigateTo('/medical/medical-record')">
              <div class="function-icon record">
                <el-icon :size="32"><Document /></el-icon>
              </div>
              <span>病历查看</span>
            </div>
            <div class="function-item" @click="navigateTo('/elderly-service/family-interaction')">
              <div class="function-icon family">
                <el-icon :size="32"><ChatDotRound /></el-icon>
              </div>
              <span>家属互动</span>
            </div>
            <div class="function-item" @click="navigateTo('/user/profile')">
              <div class="function-icon profile">
                <el-icon :size="32"><User /></el-icon>
              </div>
              <span>个人信息</span>
            </div>
            <div class="function-item" @click="navigateTo('/help')">
              <div class="function-icon help">
                <el-icon :size="32"><QuestionFilled /></el-icon>
              </div>
              <span>使用帮助</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 今日提醒和健康建议 -->
    <el-row :gutter="20" class="content-row">
      <el-col :span="12">
        <el-card class="reminder-card">
          <template #header>
            <div class="card-header">
              <span>今日提醒</span>
              <el-icon><Bell /></el-icon>
            </div>
          </template>
          <div class="reminder-list">
            <div v-for="(item, index) in todayReminders" :key="index" class="reminder-item">
              <div class="reminder-time">{{ item.time }}</div>
              <div class="reminder-content">
                <div class="reminder-title">
                  <el-icon :class="item.type"><Clock /></el-icon>
                  <span>{{ item.title }}</span>
                </div>
                <p>{{ item.content }}</p>
              </div>
            </div>
            <div v-if="todayReminders.length === 0" class="empty-reminder">
              <el-icon><DocumentRemove /></el-icon>
              <span>今日暂无提醒事项</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="advice-card">
          <template #header>
            <div class="card-header">
              <span>健康建议</span>
              <el-icon><MagicStick /></el-icon>
            </div>
          </template>
          <div class="advice-list">
            <div v-for="(item, index) in healthAdvices" :key="index" class="advice-item">
              <div class="advice-icon" :class="item.type">
                <el-icon><InfoFilled /></el-icon>
              </div>
              <div class="advice-content">
                <h4>{{ item.title }}</h4>
                <p>{{ item.content }}</p>
              </div>
            </div>
            <div v-if="healthAdvices.length === 0" class="empty-advice">
              <el-icon><DocumentRemove /></el-icon>
              <span>暂无健康建议</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import {
  Monitor,
  TrendCharts,
  DataAnalysis,
  Operation,
  Calendar,
  Document,
  ChatDotRound,
  User,
  QuestionFilled,
  Bell,
  Clock,
  DocumentRemove,
  InfoFilled,
  MagicStick,
  Sunny
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 默认头像
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

// 用户信息
const userInfo = computed(() => ({
  username: userStore.username,
  realName: userStore.realName,
  avatar: userStore.avatar
}))

// 欢迎信息
const welcomeMessage = ref('祝您身体健康，生活愉快！')

// 当前时间
const currentDate = ref('')
const currentTime = ref('')

// 天气信息
const weather = reactive({
  temp: 22,
  text: '晴'
})

// 健康数据
const healthData = reactive({
  heartRate: 72,
  bloodPressure: '120/80',
  bloodSugar: 5.6
})

// 今日提醒
const todayReminders = ref([
  {
    time: '08:00',
    title: '服药提醒',
    content: '请按时服用降压药',
    type: 'medicine'
  },
  {
    time: '14:00',
    title: '体检提醒',
    content: '下午2点血压测量',
    type: 'check'
  },
  {
    time: '18:00',
    title: '运动提醒',
    content: '晚饭后散步30分钟',
    type: 'exercise'
  }
])

// 健康建议
const healthAdvices = ref([
  {
    title: '饮食建议',
    content: '建议多食用富含纤维的蔬菜水果，控制盐分摄入',
    type: 'diet'
  },
  {
    title: '运动建议',
    content: '每天进行30分钟的轻度有氧运动，如散步、太极拳',
    type: 'exercise'
  },
  {
    title: '作息建议',
    content: '保持规律作息，每晚11点前入睡，保证7-8小时睡眠',
    type: 'sleep'
  }
])

// 更新时间
const updateTime = () => {
  const now = new Date()
  currentDate.value = now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
  currentTime.value = now.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 获取健康状态
const getHealthStatus = (value, type) => {
  if (!value || value === '--') return 'unknown'
  
  switch (type) {
    case 'heartRate':
      if (value >= 60 && value <= 100) return 'normal'
      return 'abnormal'
    case 'bloodPressure':
      // 简单判断，实际应该解析血压值
      return 'normal'
    case 'bloodSugar':
      if (value >= 3.9 && value <= 6.1) return 'normal'
      return 'abnormal'
    default:
      return 'unknown'
  }
}

// 获取健康状态文本
const getHealthStatusText = (value, type) => {
  const status = getHealthStatus(value, type)
  switch (status) {
    case 'normal': return '正常'
    case 'abnormal': return '异常'
    default: return '未知'
  }
}

// 导航到指定页面
const navigateTo = (path) => {
  router.push(path)
}

// 初始化
onMounted(() => {
  updateTime()
  // 每秒更新时间
  setInterval(updateTime, 1000)
  
  // 加载健康数据
  loadHealthData()
})

// 加载健康数据
const loadHealthData = async () => {
  try {
    // 这里应该调用API获取用户的健康数据
    // const res = await getElderlyHealthData(userStore.userId)
    // healthData.heartRate = res.data.heartRate
    // healthData.bloodPressure = res.data.bloodPressure
    // healthData.bloodSugar = res.data.bloodSugar
  } catch (error) {
    console.error('加载健康数据失败:', error)
  }
}
</script>

<style scoped>
.elderly-dashboard-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

/* 欢迎卡片 */
.welcome-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
}

.welcome-card :deep(.el-card__body) {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30px;
}

.welcome-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.welcome-text h2 {
  margin: 0 0 10px 0;
  font-size: 28px;
  font-weight: 600;
}

.welcome-text p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.welcome-date {
  text-align: right;
}

.date-info .date {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 5px 0;
}

.date-info .time {
  font-size: 24px;
  font-weight: 700;
  margin: 0 0 10px 0;
}

.weather-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
}

/* 健康状态卡片 */
.health-status-row {
  margin-bottom: 20px;
}

.health-card {
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.health-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.health-content {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
}

.health-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.heart-rate .health-icon {
  background: linear-gradient(135deg, #ff6b6b, #ee5a52);
}

.blood-pressure .health-icon {
  background: linear-gradient(135deg, #4ecdc4, #44a08d);
}

.blood-sugar .health-icon {
  background: linear-gradient(135deg, #45b7d1, #96c93d);
}

.health-info h3 {
  margin: 0 0 5px 0;
  font-size: 32px;
  font-weight: 700;
  color: #303133;
}

.health-info p {
  margin: 0 0 8px 0;
  color: #606266;
  font-size: 14px;
}

.status {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.status.normal {
  background-color: #f0f9ff;
  color: #10b981;
}

.status.abnormal {
  background-color: #fef2f2;
  color: #ef4444;
}

.status.unknown {
  background-color: #f9fafb;
  color: #6b7280;
}

/* 快捷功能 */
.quick-functions-row {
  margin-bottom: 20px;
}

.quick-functions-card {
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.quick-functions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
  padding: 20px 0;
}

.function-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px;
  border-radius: 12px;
  background: #f8fafc;
  cursor: pointer;
  transition: all 0.3s ease;
}

.function-item:hover {
  background: #e2e8f0;
  transform: translateY(-2px);
}

.function-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.function-icon.health {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.function-icon.medical {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.function-icon.record {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.function-icon.family {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.function-icon.profile {
  background: linear-gradient(135deg, #fa709a, #fee140);
}

.function-icon.help {
  background: linear-gradient(135deg, #a8edea, #fed6e3);
  color: #666;
}

.function-item span {
  font-size: 14px;
  font-weight: 500;
  color: #374151;
}

/* 内容区域 */
.content-row {
  margin-bottom: 20px;
}

.reminder-card,
.advice-card {
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  height: 400px;
}

.reminder-list,
.advice-list {
  max-height: 320px;
  overflow-y: auto;
}

.reminder-item {
  display: flex;
  gap: 15px;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.reminder-item:last-child {
  border-bottom: none;
}

.reminder-time {
  font-size: 14px;
  font-weight: 600;
  color: #667eea;
  min-width: 50px;
}

.reminder-content {
  flex: 1;
}

.reminder-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 5px;
}

.reminder-title span {
  font-weight: 600;
  color: #303133;
}

.reminder-content p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.advice-item {
  display: flex;
  gap: 15px;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.advice-item:last-child {
  border-bottom: none;
}

.advice-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.advice-icon.diet {
  background: linear-gradient(135deg, #ff9a9e, #fecfef);
}

.advice-icon.exercise {
  background: linear-gradient(135deg, #a8edea, #fed6e3);
}

.advice-icon.sleep {
  background: linear-gradient(135deg, #d299c2, #fef9d7);
}

.advice-content h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.advice-content p {
  margin: 0;
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
}

.empty-reminder,
.empty-advice {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #909399;
}

.empty-reminder span,
.empty-advice span {
  margin-top: 10px;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .elderly-dashboard-container {
    padding: 10px;
  }
  
  .welcome-card :deep(.el-card__body) {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }
  
  .quick-functions {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .health-status-row .el-col {
    margin-bottom: 15px;
  }
}
</style>