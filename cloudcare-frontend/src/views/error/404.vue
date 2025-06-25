<template>
  <div class="error-container">
    <div class="error-content">
      <div class="error-image">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200" width="200" height="200">
          <path d="M20,100 C20,55.82 55.82,20 100,20 C144.18,20 180,55.82 180,100 C180,144.18 144.18,180 100,180 C55.82,180 20,144.18 20,100 Z" fill="#f5f5f5" />
          <path d="M86.6,62.8 C84.2,60.4 80.2,60.4 77.8,62.8 C75.4,65.2 75.4,69.2 77.8,71.6 L106.2,100 L77.8,128.4 C75.4,130.8 75.4,134.8 77.8,137.2 C80.2,139.6 84.2,139.6 86.6,137.2 L115,108.8 L143.4,137.2 C145.8,139.6 149.8,139.6 152.2,137.2 C154.6,134.8 154.6,130.8 152.2,128.4 L123.8,100 L152.2,71.6 C154.6,69.2 154.6,65.2 152.2,62.8 C149.8,60.4 145.8,60.4 143.4,62.8 L115,91.2 L86.6,62.8 Z" fill="#f56c6c" />
        </svg>
      </div>
      <h1 class="error-title">404</h1>
      <p class="error-desc">抱歉，您访问的页面不存在</p>
      <div class="error-actions">
        <el-button type="primary" @click="goHome">返回首页</el-button>
        <el-button @click="goBack">返回上一页</el-button>
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

// 返回上一页
const goBack = () => {
  router.go(-1)
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
  color: #f56c6c;
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