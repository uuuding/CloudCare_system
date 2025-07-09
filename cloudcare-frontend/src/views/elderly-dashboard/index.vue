<template>
  <div class="dashboard-container">
    <!-- 欢迎信息组件 -->
    
    <!-- 功能快捷入口 -->
    <div class="quick-access-grid">
      <el-card 
        v-for="(item, index) in quickActions" 
        :key="index"
        class="quick-access-item"
        @click="navigateTo(item.path)"
      >
        <el-icon :size="32">
          <component :is="item.icon" />
        </el-icon>
        <h3>{{ item.title }}</h3>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import WelcomeMessage from '@/components/WelcomeMessage.vue'

const router = useRouter()

const quickActions = [
  {
    title: '健康档案',
    icon: 'Document',
    path: '/health/alert'
  },
  {
    title: '预约挂号',
    icon: 'Calendar',
    path: '/medical/appointment'
  },
  {
    title: '报表统计',
    icon: 'PieChart',
    path: '/reports'
  },
  {
    title: '个人中心',
    icon: 'User',
    path: '/profile'
  },
  {
    title: '使用帮助',
    icon: 'QuestionFilled',
    path: '/help'
  }
]

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.quick-access-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 30px;
}

.quick-access-item {
  cursor: pointer;
  transition: transform 0.3s;
  text-align: center;
  padding: 20px;
}

.quick-access-item:hover {
  transform: translateY(-5px);
}
</style>