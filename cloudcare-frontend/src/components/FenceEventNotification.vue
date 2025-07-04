<template>
  <!-- 围栏事件实时弹窗通知组件 -->
  <div class="fence-event-notification">
    <!-- 弹窗列表 -->
    <transition-group name="notification" tag="div" class="notification-container">
      <div 
        v-for="notification in notifications" 
        :key="notification.id"
        :class="['notification-item', `notification-${notification.type}`]"
      >
        <div class="notification-header">
          <el-icon class="notification-icon">
            <LocationInformation v-if="notification.type === 'enter'" />
            <Warning v-else />
          </el-icon>
          <span class="notification-title">围栏{{ notification.type === 'enter' ? '进入' : '离开' }}提醒</span>
          <el-button 
            type="text" 
            size="small" 
            @click="closeNotification(notification.id)"
            class="close-btn"
          >
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <div class="notification-content">
          <div class="elderly-info">
            <strong>{{ notification.elderlyName || '未知老人' }}</strong>
            <span class="event-type">{{ notification.type === 'enter' ? '进入了' : '离开了' }}</span>
            <strong>{{ notification.fenceName }}</strong>
          </div>
          
          <div class="event-details">
            <div class="detail-item">
              <span class="label">时间：</span>
              <span class="value">{{ formatTime(notification.eventTime) }}</span>
            </div>
            <div class="detail-item">
              <span class="label">位置：</span>
              <span class="value">{{ notification.coordinates }}</span>
            </div>
          </div>
          
          <div class="notification-actions">
            <el-button size="small" type="primary" @click="viewDetails(notification)">
              查看详情
            </el-button>
            <el-button size="small" @click="markAsRead(notification.id)">
              标记已读
            </el-button>
          </div>
        </div>
      </div>
    </transition-group>
    

  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElNotification } from 'element-plus'
import { LocationInformation, Warning, Close } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils/date'
import request from '@/utils/request'

// 响应式数据
const notifications = ref([])
const maxNotifications = 5 // 最大显示通知数量
const autoCloseTime = 10000 // 自动关闭时间（毫秒）

// 定义事件
const emit = defineEmits(['view-details', 'notification-read'])

// 生命周期
onMounted(() => {
  // 启动轮询检查新的围栏事件
  startPolling()
})

onUnmounted(() => {
  // 清理轮询
  stopPolling()
})

// 轮询相关
let pollingTimer = null
const pollingInterval = 5000 // 5秒轮询一次

const startPolling = () => {
  // 立即检查一次
  checkNewFenceEvents()
  
  // 设置定时轮询
  pollingTimer = setInterval(() => {
    checkNewFenceEvents()
  }, pollingInterval)
}

const stopPolling = () => {
  if (pollingTimer) {
    clearInterval(pollingTimer)
    pollingTimer = null
  }
}

// 检查新的围栏事件
const checkNewFenceEvents = async () => {
  try {
    // 获取最近的围栏事件（未读的）
    const response = await request({
      url: '/geo-fence/events/recent',
      method: 'get',
      params: {
        unread: true,
        limit: 10
      }
    })
    
    if (response.success && response.data && response.data.length > 0) {
      // 处理新事件
      response.data.forEach(event => {
        addNotification(event)
      })
    }
  } catch (error) {
    console.error('检查围栏事件失败:', error)
  }
}

// 添加通知
const addNotification = (event) => {
  // 检查是否已存在相同的通知
  const exists = notifications.value.some(n => n.eventId === event.id)
  if (exists) return
  
  const notification = {
    id: Date.now() + Math.random(), // 生成唯一ID
    eventId: event.id,
    type: event.eventType, // 'enter' 或 'exit'
    elderlyName: event.elderlyName,
    fenceName: event.fenceName,
    eventTime: event.eventTime,
    coordinates: event.coordinates,
    alertContent: event.alertContent,
    createTime: new Date()
  }
  
  // 添加到通知列表
  notifications.value.unshift(notification)
  
  // 限制通知数量
  if (notifications.value.length > maxNotifications) {
    notifications.value = notifications.value.slice(0, maxNotifications)
  }
  
  // 播放提示音
  playAlertSound()
  
  // 显示系统通知
  showSystemNotification(notification)
  
  // 设置自动关闭
  setTimeout(() => {
    closeNotification(notification.id)
  }, autoCloseTime)
}

// 播放提示音
const playAlertSound = () => {
  try {
    // 使用Web Audio API创建简单的提示音
    const audioContext = new (window.AudioContext || window.webkitAudioContext)()
    const oscillator = audioContext.createOscillator()
    const gainNode = audioContext.createGain()
    
    oscillator.connect(gainNode)
    gainNode.connect(audioContext.destination)
    
    oscillator.frequency.setValueAtTime(800, audioContext.currentTime)
    oscillator.frequency.setValueAtTime(600, audioContext.currentTime + 0.1)
    oscillator.frequency.setValueAtTime(800, audioContext.currentTime + 0.2)
    
    gainNode.gain.setValueAtTime(0.3, audioContext.currentTime)
    gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.3)
    
    oscillator.start(audioContext.currentTime)
    oscillator.stop(audioContext.currentTime + 0.3)
  } catch (error) {
    console.warn('音频播放异常:', error)
  }
}

// 显示系统通知
const showSystemNotification = (notification) => {
  const title = `围栏${notification.type === 'enter' ? '进入' : '离开'}提醒`
  const message = `${notification.elderlyName}${notification.type === 'enter' ? '进入了' : '离开了'}${notification.fenceName}`
  
  ElNotification({
    title,
    message,
    type: notification.type === 'enter' ? 'success' : 'warning',
    duration: 5000,
    position: 'top-right'
  })
}

// 关闭通知
const closeNotification = (notificationId) => {
  const index = notifications.value.findIndex(n => n.id === notificationId)
  if (index > -1) {
    notifications.value.splice(index, 1)
  }
}

// 标记为已读
const markAsRead = async (notificationId) => {
  const notification = notifications.value.find(n => n.id === notificationId)
  if (notification) {
    try {
      // 调用API标记事件为已读
      await request({
        url: `/geo-fence/events/${notification.eventId}/read`,
        method: 'post'
      })
      
      emit('notification-read', notification.eventId)
      closeNotification(notificationId)
      ElMessage.success('已标记为已读')
    } catch (error) {
      console.error('标记已读失败:', error)
      ElMessage.error('标记已读失败')
    }
  }
}

// 查看详情
const viewDetails = (notification) => {
  emit('view-details', notification)
  closeNotification(notification.id)
}

// 格式化时间
const formatTime = (dateTime) => {
  if (!dateTime) return ''
  return formatDateTime(dateTime)
}

// 暴露方法供外部调用
defineExpose({
  addNotification,
  clearAllNotifications: () => {
    notifications.value = []
  },
  getNotificationCount: () => notifications.value.length
})
</script>

<style scoped>
.fence-event-notification {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
  pointer-events: none;
}

.notification-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.notification-item {
  width: 380px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-left: 4px solid #409EFF;
  pointer-events: auto;
  overflow: hidden;
}

.notification-enter {
  border-left-color: #67C23A;
}

.notification-exit {
  border-left-color: #F56C6C;
}

.notification-header {
  display: flex;
  align-items: center;
  padding: 12px 16px 8px;
  background: #f8f9fa;
  border-bottom: 1px solid #eee;
}

.notification-icon {
  font-size: 18px;
  margin-right: 8px;
}

.notification-enter .notification-icon {
  color: #67C23A;
}

.notification-exit .notification-icon {
  color: #F56C6C;
}

.notification-title {
  flex: 1;
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}

.close-btn {
  padding: 0;
  margin: 0;
  color: #909399;
}

.close-btn:hover {
  color: #F56C6C;
}

.notification-content {
  padding: 16px;
}

.elderly-info {
  margin-bottom: 12px;
  font-size: 15px;
  line-height: 1.4;
}

.elderly-info strong {
  color: #303133;
}

.event-type {
  margin: 0 4px;
  color: #606266;
}

.event-details {
  margin-bottom: 16px;
}

.detail-item {
  display: flex;
  margin-bottom: 6px;
  font-size: 13px;
}

.detail-item .label {
  color: #909399;
  width: 50px;
  flex-shrink: 0;
}

.detail-item .value {
  color: #606266;
  flex: 1;
}

.notification-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

/* 动画效果 */
.notification-enter-active {
  transition: all 0.3s ease-out;
}

.notification-leave-active {
  transition: all 0.3s ease-in;
}

.notification-enter-from {
  transform: translateX(100%);
  opacity: 0;
}

.notification-leave-to {
  transform: translateX(100%);
  opacity: 0;
}

.notification-move {
  transition: transform 0.3s ease;
}
</style>