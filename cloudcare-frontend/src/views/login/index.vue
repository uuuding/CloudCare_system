<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <img src="@/assets/logo.svg" alt="Logo" class="logo" />
        <h2 class="title">云护理系统</h2>
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
import { isValidUsername } from '@/utils/validate'

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
  username: '',
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
          // 跳转到首页或者重定向页面
          const redirect = route.query.redirect || '/'
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
  background-color: #f0f2f5;
  background-image: url('@/assets/login-bg.svg');
  background-size: cover;
  background-position: center;
}

.login-card {
  width: 400px;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.logo {
  width: 80px;
  height: 80px;
  margin-bottom: 15px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.login-form {
  margin-top: 20px;
}

.remember-me {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.login-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

.register-link {
  margin-top: 15px;
  text-align: center;
}
</style>