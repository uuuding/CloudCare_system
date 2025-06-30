<template>
  <div class="test-container">
    <el-card class="test-card" shadow="hover">
      <div class="header">
        <h2>组件测试页面</h2>
      </div>
      
      <el-divider />
      
      <!-- 头像上传组件测试 -->
      <div class="test-section">
        <h3>头像上传组件测试</h3>
        <AvatarUpload 
          v-model="avatarUrl" 
          :size="120"
          @success="handleAvatarSuccess"
        />
        <p>当前头像URL: {{ avatarUrl }}</p>
      </div>
      
      <el-divider />
      
      <!-- 路由测试 -->
      <div class="test-section">
        <h3>路由测试</h3>
        <el-space wrap>
          <el-button @click="$router.push('/dashboard')">首页</el-button>
          <el-button @click="$router.push('/user/profile')">个人中心</el-button>
          <el-button @click="$router.push('/elderly-profile')">老人档案</el-button>
          <el-button @click="$router.push('/health/alert')">健康预警</el-button>
        </el-space>
      </div>
      
      <el-divider />
      
      <!-- API测试 -->
      <div class="test-section">
        <h3>API测试</h3>
        <el-space wrap>
          <el-button @click="testGetUserInfo" :loading="loading">获取用户信息</el-button>
          <el-button @click="testUpdateUserInfo" :loading="loading">更新用户信息</el-button>
        </el-space>
        <div v-if="userInfo" class="user-info">
          <pre>{{ JSON.stringify(userInfo, null, 2) }}</pre>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import AvatarUpload from '@/components/AvatarUpload.vue'
import { getUserInfo, updateUserInfo } from '@/api/auth'

const avatarUrl = ref('')
const loading = ref(false)
const userInfo = ref(null)

const handleAvatarSuccess = (url) => {
  ElMessage.success('头像更新成功')
  console.log('Avatar URL:', url)
}

const testGetUserInfo = async () => {
  try {
    loading.value = true
    const res = await getUserInfo()
    userInfo.value = res.data
    ElMessage.success('获取用户信息成功')
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败: ' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

const testUpdateUserInfo = async () => {
  try {
    loading.value = true
    const testData = {
      realName: '测试用户',
      phone: '13800138000',
      email: 'test@example.com'
    }
    await updateUserInfo(testData)
    ElMessage.success('更新用户信息成功')
  } catch (error) {
    console.error('更新用户信息失败:', error)
    ElMessage.error('更新用户信息失败: ' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.test-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.test-section {
  margin: 20px 0;
}

.test-section h3 {
  margin-bottom: 15px;
  color: #303133;
}

.user-info {
  margin-top: 15px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  font-size: 12px;
  max-height: 300px;
  overflow-y: auto;
}

.user-info pre {
  margin: 0;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>