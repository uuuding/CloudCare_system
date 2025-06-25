<template>
  <div class="error-container">
    <div class="error-content">
      <div class="error-image">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200" width="200" height="200">
          <path d="M20,100 C20,55.82 55.82,20 100,20 C144.18,20 180,55.82 180,100 C180,144.18 144.18,180 100,180 C55.82,180 20,144.18 20,100 Z" fill="#f5f5f5" />
          <path d="M100,60 C93.4,60 88,65.4 88,72 L88,100 C88,106.6 93.4,112 100,112 C106.6,112 112,106.6 112,100 L112,72 C112,65.4 106.6,60 100,60 Z M100,120 C93.4,120 88,124.5 88,130 C88,135.5 93.4,140 100,140 C106.6,140 112,135.5 112,130 C112,124.5 106.6,120 100,120 Z" fill="#909399" />
          <path d="M60,80 L140,80 L140,90 L60,90 Z" fill="#909399" />
        </svg>
      </div>
      <h1 class="error-title">500</h1>
      <p class="error-desc">抱歉，服务器内部错误</p>
      <div class="error-actions">
        <el-button type="primary" @click="goHome">返回首页</el-button>
        <el-button @click="refresh">刷新页面</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

// 返回首页
const goHome = () => {
  router.push('/')
}

// 刷新页面
const refresh = () => {
  window.location.reload()
}
</script>

<style scoped>
.error-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.error-content {
  text-align: center;
  padding: 40px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.error-image {
  margin-bottom: 20px;
}

.error-title {
  font-size: 72px;
  color: #909399;
  margin: 0 0 20px 0;
  font-weight: bold;
}

.error-desc {
  font-size: 20px;
  color: #606266;
  margin: 0 0 30px 0;
}

.error-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}
</style>