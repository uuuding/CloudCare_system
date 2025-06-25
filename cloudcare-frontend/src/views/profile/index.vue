<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <!-- 个人信息卡片 -->
      <el-col :span="8">
        <el-card class="profile-card">
          <div class="profile-header">
            <div class="avatar-wrapper">
              <el-avatar :size="100" :src="userInfo.avatar || defaultAvatar" />
              <el-upload
                class="avatar-uploader"
                action="/api/system/user/avatar"
                :headers="headers"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <el-button size="small" type="primary" class="upload-btn">更换头像</el-button>
              </el-upload>
            </div>
            <div class="user-info">
              <h2>{{ userInfo.realName || userInfo.username }}</h2>
              <p>{{ userInfo.roleName || '普通用户' }}</p>
            </div>
          </div>
          <div class="profile-content">
            <div class="info-item">
              <span class="label">用户名：</span>
              <span class="value">{{ userInfo.username }}</span>
            </div>
            <div class="info-item">
              <span class="label">手机号码：</span>
              <span class="value">{{ userInfo.phoneNumber || '未设置' }}</span>
            </div>
            <div class="info-item">
              <span class="label">邮箱：</span>
              <span class="value">{{ userInfo.email || '未设置' }}</span>
            </div>
            <div class="info-item">
              <span class="label">创建时间：</span>
              <span class="value">{{ formatDateTime(userInfo.createTime) }}</span>
            </div>
            <div class="info-item">
              <span class="label">最后登录：</span>
              <span class="value">{{ formatDateTime(userInfo.loginDate) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 个人设置选项卡 -->
      <el-col :span="16">
        <el-card>
          <el-tabs v-model="activeTab">
            <!-- 基本资料 -->
            <el-tab-pane label="基本资料" name="basic">
              <el-form
                ref="basicFormRef"
                :model="basicForm"
                :rules="basicRules"
                label-width="100px"
              >
                <el-form-item label="用户昵称" prop="nickName">
                  <el-input v-model="basicForm.nickName" placeholder="请输入用户昵称" />
                </el-form-item>
                <el-form-item label="真实姓名" prop="realName">
                  <el-input v-model="basicForm.realName" placeholder="请输入真实姓名" />
                </el-form-item>
                <el-form-item label="手机号码" prop="phoneNumber">
                  <el-input v-model="basicForm.phoneNumber" placeholder="请输入手机号码" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="basicForm.email" placeholder="请输入邮箱" />
                </el-form-item>
                <el-form-item label="性别">
                  <el-radio-group v-model="basicForm.gender">
                    <el-radio label="0">男</el-radio>
                    <el-radio label="1">女</el-radio>
                    <el-radio label="2">保密</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="updateBasicInfo">保存</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 修改密码 -->
            <el-tab-pane label="修改密码" name="password">
              <el-form
                ref="passwordFormRef"
                :model="passwordForm"
                :rules="passwordRules"
                label-width="100px"
              >
                <el-form-item label="旧密码" prop="oldPassword">
                  <el-input
                    v-model="passwordForm.oldPassword"
                    type="password"
                    placeholder="请输入旧密码"
                    show-password
                  />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input
                    v-model="passwordForm.newPassword"
                    type="password"
                    placeholder="请输入新密码"
                    show-password
                  />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input
                    v-model="passwordForm.confirmPassword"
                    type="password"
                    placeholder="请确认新密码"
                    show-password
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="updatePassword">保存</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { updateUserInfo, updateUserPassword } from '@/api/auth'
import { isEmail, isMobile, isPassword } from '@/utils/validate'

// 默认头像
const defaultAvatar = new URL('@/assets/default-avatar.png', import.meta.url).href

// 用户状态
const userStore = useUserStore()

// 用户信息
const userInfo = computed(() => {
  return {
    userId: userStore.userId,
    username: userStore.username,
    realName: userStore.realName,
    nickName: userStore.nickName,
    phoneNumber: userStore.phoneNumber,
    email: userStore.email,
    gender: userStore.gender,
    avatar: userStore.avatar,
    roleName: userStore.roleName,
    createTime: userStore.createTime,
    loginDate: userStore.loginDate
  }
})

// 上传头像的请求头
const headers = computed(() => {
  return {
    Authorization: 'Bearer ' + userStore.token
  }
})

// 当前激活的选项卡
const activeTab = ref('basic')

// 基本资料表单
const basicFormRef = ref(null)
const basicForm = reactive({
  nickName: '',
  realName: '',
  phoneNumber: '',
  email: '',
  gender: '0'
})

// 基本资料表单校验规则
const basicRules = {
  nickName: [
    { required: true, message: '请输入用户昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '用户昵称长度必须在2到20个字符之间', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '真实姓名长度必须在2到20个字符之间', trigger: 'blur' }
  ],
  phoneNumber: [
    { required: false, message: '请输入手机号码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value && !isMobile(value)) {
        callback(new Error('请输入正确的手机号码'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ],
  email: [
    { required: false, message: '请输入邮箱', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value && !isEmail(value)) {
        callback(new Error('请输入正确的邮箱地址'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ]
}

// 修改密码表单
const passwordFormRef = ref(null)
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 修改密码表单校验规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (!isPassword(value)) {
        callback(new Error('密码必须包含字母、数字，长度在6-20之间'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value !== passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ]
}

// 初始化表单数据
const initFormData = () => {
  basicForm.nickName = userInfo.value.nickName || ''
  basicForm.realName = userInfo.value.realName || ''
  basicForm.phoneNumber = userInfo.value.phoneNumber || ''
  basicForm.email = userInfo.value.email || ''
  basicForm.gender = userInfo.value.gender || '0'
}

// 更新基本信息
const updateBasicInfo = () => {
  basicFormRef.value.validate(valid => {
    if (valid) {
      updateUserInfo(basicForm).then(res => {
        if (res.code === 200) {
          ElMessage.success('个人信息修改成功')
          // 更新用户信息
          userStore.updateUserInfo(basicForm)
        } else {
          ElMessage.error(res.msg || '修改失败')
        }
      }).catch(() => {
        ElMessage.error('修改失败，请稍后重试')
      })
    }
  })
}

// 更新密码
const updatePassword = () => {
  passwordFormRef.value.validate(valid => {
    if (valid) {
      updateUserPassword(passwordForm).then(res => {
        if (res.code === 200) {
          ElMessage.success('密码修改成功，请重新登录')
          // 清空表单
          passwordForm.oldPassword = ''
          passwordForm.newPassword = ''
          passwordForm.confirmPassword = ''
          // 退出登录
          setTimeout(() => {
            userStore.logout()
          }, 1500)
        } else {
          ElMessage.error(res.msg || '修改失败')
        }
      }).catch(() => {
        ElMessage.error('修改失败，请稍后重试')
      })
    }
  })
}

// 头像上传前的校验
const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg'
  const isPNG = file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG && !isPNG) {
    ElMessage.error('上传头像图片只能是 JPG 或 PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 头像上传成功的回调
const handleAvatarSuccess = (res) => {
  if (res.code === 200) {
    userStore.updateUserInfo({ avatar: res.data })
    ElMessage.success('头像修改成功')
  } else {
    ElMessage.error(res.msg || '上传失败')
  }
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString()
}

// 页面加载时初始化
onMounted(() => {
  initFormData()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
}

.profile-card {
  margin-bottom: 20px;
}

.profile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.avatar-wrapper {
  position: relative;
  margin-bottom: 20px;
  text-align: center;
}

.upload-btn {
  margin-top: 10px;
}

.user-info {
  text-align: center;
}

.user-info h2 {
  margin: 0 0 5px 0;
  font-size: 20px;
}

.user-info p {
  margin: 0;
  color: #909399;
}

.profile-content {
  margin-top: 20px;
}

.info-item {
  display: flex;
  margin-bottom: 10px;
  line-height: 24px;
}

.info-item .label {
  width: 100px;
  color: #606266;
}

.info-item .value {
  flex: 1;
  color: #303133;
}
</style>