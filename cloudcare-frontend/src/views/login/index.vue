<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <img src="@/assets/logo.png" alt="Logo" class="logo" />
        <h2 class="title">智慧医养大数据公共服务平台云端后台管理系统</h2>
      </div>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        autocomplete="on"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            type="text"
            autocomplete="on"
            prefix-icon="User"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            placeholder="密码"
            :type="passwordVisible ? 'text' : 'password'"
            autocomplete="on"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item class="remember-me">
          <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
          <el-link type="primary" :underline="false" @click="forgotPassword">忘记密码?</el-link>
        </el-form-item>
        
        <el-form-item>
          <el-button
            :loading="loading"
            type="primary"
            class="login-button"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
        
        <div class="register-link">
          <span>还没有账号?</span>
          <el-link type="primary" :underline="false" @click="goToRegister">立即注册</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

// 路由实例
const router = useRouter()
const route = useRoute()

// 用户状态
const userStore = useUserStore()

// 表单引用
const loginFormRef = ref(null)

// 密码可见性
const passwordVisible = ref(false)

// 加载状态
const loading = ref(false)

// 登录表单
const loginForm = reactive({
  username: route.query.type === 'elderly' ? route.query.elderlyName || '老人' : '',
  password: '',
  rememberMe: false
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '用户名长度在4到20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20个字符之间', trigger: 'blur' }
  ]
}

// 处理登录
const handleLogin = () => {
  loginFormRef.value.validate(valid => {
    if (valid) {
      loading.value = true
      userStore.login(loginForm)
        .then(() => {
          // 登录成功后获取用户信息
          return userStore.getInfo()
        })
        .then(() => {
          ElMessage.success('登录成功')
          // 根据用户类型跳转到对应的首页
          let redirect = route.query.redirect
          if (!redirect) {
            // 根据用户类型设置默认首页
            if (userStore.userType === 2) {
              redirect = '/doctor-dashboard'
            } else if (userStore.userType === 3) {
              redirect = '/elderly-dashboard'
            } else {
              redirect = '/dashboard'
            }
          }
          router.push(redirect)
        })
        .catch(error => {
          console.error('登录失败:', error)
          ElMessage.error(error.message || '登录失败，请检查用户名和密码')
        })
        .finally(() => {
          loading.value = false
        })
    } else {
      return false
    }
  })
}

// 忘记密码
const forgotPassword = () => {
  ElMessage.info('请联系管理员重置密码')
}

// 跳转到注册页面
const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  opacity: 0.9;
}

.login-card {
  width: 420px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(59, 130, 246, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.2);
  position: relative;
  z-index: 1;
}

.login-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 32px;
}

.logo {
  width: 72px;
  height: 72px;
  margin-bottom: 16px;
  filter: drop-shadow(0 4px 8px rgba(59, 130, 246, 0.3));
}

.title {
  font-size: 20px;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
  text-align: center;
  line-height: 1.4;
}

.login-form {
  margin-top: 24px;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.1);
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.login-form :deep(.el-input__wrapper:hover) {
  border-color: #3b82f6;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.15);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.remember-me {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.remember-me :deep(.el-checkbox__label) {
  color: #64748b;
  font-size: 14px;
}

.remember-me :deep(.el-link) {
  font-size: 14px;
}

.login-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(59, 130, 246, 0.4);
}

.register-link {
  margin-top: 20px;
  text-align: center;
  color: #64748b;
  font-size: 14px;
}

.register-link :deep(.el-link) {
  font-weight: 600;
}
</style>