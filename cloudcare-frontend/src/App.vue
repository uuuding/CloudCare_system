<template>
  <router-view />
  <!-- 围栏事件实时通知组件 -->
  <FenceEventNotification 
    @view-details="handleViewEventDetails"
    @notification-read="handleNotificationRead"
  />
</template>

<script setup>
import { ElMessage } from 'element-plus'
import FenceEventNotification from '@/components/FenceEventNotification.vue'
import router from '@/router'

// 处理查看事件详情
const handleViewEventDetails = (notification) => {
  // 跳转到围栏管理页面并显示事件详情
  router.push({
    path: '/elderly-service/geo-fence',
    query: {
      showEvent: notification.eventId,
      elderlyId: notification.elderlyId
    }
  })
}

// 处理通知已读
const handleNotificationRead = (eventId) => {
  console.log('事件已标记为已读:', eventId)
}
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

#app {
  height: 100%;
}
</style>