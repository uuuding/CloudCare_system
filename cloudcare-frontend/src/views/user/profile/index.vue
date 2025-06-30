<template>
  <div class="profile-container">
    <el-card class="profile-card" shadow="hover">
      <div class="header">
        <h2>个人中心</h2>
      </div>
      
      <!-- 头像上传区域 -->
      <div class="avatar-section">
        <AvatarUpload 
          v-model="userInfo.avatar" 
          :size="120"
          @success="handleAvatarSuccess"
        />
        <div class="avatar-info">
          <h3>{{ userInfo.realName || userInfo.username }}</h3>
          <p class="user-type">{{ userInfo.userType }}</p>
        </div>
      </div>
      
      <el-divider />
      
      <el-form :model="userInfo" label-width="100px" class="profile-form" ref="formRef">
        <el-form-item label="用户名">
          <el-input v-model="userInfo.username" disabled />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="userInfo.realName" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userInfo.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" />
        </el-form-item>
        <el-form-item label="用户类型">
          <el-input v-model="userInfo.userType" disabled />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveUserInfo" :loading="saving">
            保存
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserInfo, updateUserInfo } from '@/api/auth'
import * as userApi from '@/api/user'
import AvatarUpload from '@/components/AvatarUpload.vue'
import { useUserStore } from '@/stores/user'

const formRef = ref(null)
const saving = ref(false)
const userStore = useUserStore()

const userInfo = reactive({
  username: '',
  realName: '',
  phone: '',
  email: '',
  userType: '',
  avatar: ''
})

const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    if (res && res.data) {
      Object.assign(userInfo, res.data)
    }
  } catch (error) {
    ElMessage.error('加载用户信息失败')
  }
}

const saveUserInfo = async () => {
  try {
    saving.value = true
    await updateUserInfo(userInfo)
    
    // 更新用户状态
    await userStore.getInfo()
    
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

const handleAvatarSuccess = async (avatarUrl) => {
  console.log('头像上传成功:', avatarUrl)
  userInfo.avatar = avatarUrl
  
  // 立即更新用户头像到数据库
  try {
    await userApi.updateAvatar(avatarUrl)
    // 更新store中的头像
    await userStore.getInfo()
    ElMessage.success('头像更新成功')
  } catch (error) {
    console.error('更新头像失败:', error)
    ElMessage.error('头像更新失败')
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}

.avatar-section {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px 0;
}

.avatar-info h3 {
  margin: 0 0 8px 0;
  font-size: 20px;
  color: #303133;
}

.user-type {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.profile-form {
  margin-top: 20px;
}
</style>