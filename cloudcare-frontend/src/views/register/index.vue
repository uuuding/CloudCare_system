<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <img src="@/assets/logo.png" alt="Logo" class="logo" />
        <h2 class="title">用户注册</h2>
      </div>
      
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        class="register-form"
        label-position="top"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            type="text"
            autocomplete="off"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item label="真实姓名" prop="realName">
          <el-input
            v-model="registerForm.realName"
            placeholder="请输入真实姓名"
            type="text"
            autocomplete="off"
            prefix-icon="UserFilled"
          />
        </el-form-item>
        
        <el-form-item label="手机号码" prop="phone">
          <el-input
            v-model="registerForm.phone"
            placeholder="请输入手机号码"
            type="text"
            autocomplete="off"
            prefix-icon="Phone"
          />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="请输入邮箱"
            type="email"
            autocomplete="off"
            prefix-icon="Message"
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="registerForm.password"
            placeholder="请输入密码"
            type="password"
            autocomplete="off"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            placeholder="请再次输入密码"
            type="password"
            autocomplete="off"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="用户类型" prop="userType">
          <el-select v-model="registerForm.userType" placeholder="请选择用户类型" style="width: 100%">
            <el-option label="老人" value="3" />
            <el-option label="医生" value="2" />
            <el-option label="管理用户" value="1" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button
            :loading="loading"
            type="primary"
            class="register-button"
            @click="handleRegister"
          >
            注册
          </el-button>
        </el-form-item>
        
        <div class="login-link">
          <span>已有账号?</span>
          <el-link type="primary" :underline="false" @click="goToLogin">立即登录</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/auth'
import { isPhone, isEmail, isValidUsername } from '@/utils/validate'

// 路由实例
const router = useRouter()

// 表单引用
const registerFormRef = ref(null)

// 加载状态
const loading = ref(false)

// 注册表单
const registerForm = reactive({
  username: '',
  realName: '',
  phone: '',
  email: '',
  password: '',
  confirmPassword: '',
  userType: '1'
})

// 验证密码是否一致
const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

// 验证手机号
const validatePhone = (rule, value, callback) => {
  if (value && !isPhone(value)) {
    callback(new Error('请输入正确的手机号码'))
  } else {
    callback()
  }
}

// 验证邮箱
const validateEmail = (rule, value, callback) => {
  if (value && !isEmail(value)) {
    callback(new Error('请输入正确的邮箱地址'))
  } else {
    callback()
  }
}

// 表单验证规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '用户名长度在4到20个字符之间', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在2到20个字符之间', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { validator: validatePhone, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { validator: validateEmail, trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validatePass, trigger: 'blur' }
  ],
  userType: [
    { required: true, message: '请选择用户类型', trigger: 'change' }
  ]
}

// 处理注册
const handleRegister = () => {
  registerFormRef.value.validate(valid => {
    if (valid) {
      loading.value = true
      
      // 构建注册数据
      const registerData = {
        username: registerForm.username,
        realName: registerForm.realName,
        phone: registerForm.phone,
        email: registerForm.email,
        password: registerForm.password,
        userType: registerForm.userType
      }
      console.log(registerData)
      
      register(registerData)
        .then(res => {
          ElMessage.success('注册成功，请登录')
          router.push('/login')
        })
        .catch(error => {
          console.error('注册失败:', error)
          ElMessage.error(error.message || '注册失败，请稍后再试')
        })
        .finally(() => {
          loading.value = false
        })
    } else {
      return false
    }
  })
}

// 跳转到登录页面
const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
  padding: 20px 0;
}

.register-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  opacity: 0.9;
}

.register-card {
  width: 480px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(59, 130, 246, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.2);
  position: relative;
  z-index: 1;
}

.register-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}

.logo {
  width: 64px;
  height: 64px;
  margin-bottom: 12px;
  filter: drop-shadow(0 4px 8px rgba(59, 130, 246, 0.3));
}

.title {
  font-size: 22px;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.register-form {
  margin-top: 24px;
}

.register-form :deep(.el-form-item__label) {
  color: #374151;
  font-weight: 500;
  font-size: 14px;
}

.register-form :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.1);
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.register-form :deep(.el-input__wrapper:hover) {
  border-color: #3b82f6;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.15);
}

.register-form :deep(.el-input__wrapper.is-focus) {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.register-form :deep(.el-select .el-input__wrapper) {
  border-radius: 12px;
}

.register-button {
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

.register-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(59, 130, 246, 0.4);
}

.login-link {
  margin-top: 20px;
  text-align: center;
  color: #64748b;
  font-size: 14px;
}

.login-link :deep(.el-link) {
  font-weight: 600;
}
</style>