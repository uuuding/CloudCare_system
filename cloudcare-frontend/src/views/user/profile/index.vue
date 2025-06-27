<template>
  <div class="profile-container">
    <h1>个人中心</h1>
    <el-form :model="userInfo" ref="formRef" label-width="100px" class="profile-form">
      <el-form-item label="用户名">
        <el-input v-model="userInfo.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="真实姓名">
        <el-input v-model="userInfo.realName"></el-input>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="userInfo.phone"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="userInfo.email"></el-input>
      </el-form-item>
      <el-form-item label="用户类型">
        <el-input v-model="userInfo.userType" disabled></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {getUserInfo, updateUserInfo} from "@/api/auth";

const formRef = ref(null)
const userInfo = reactive({
  username: '',
  realName: '',
  phone: '',
  email: '',
  userType: ''
})

const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    console.log(res)
    if (res && res.data) {
      Object.assign(userInfo, res.data)
    }
  } catch (error) {
    ElMessage.error('加载用户信息失败')
  }
}

const handleSave = async () => {
  try {
    await updateUserInfo(userInfo)
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
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
.profile-form {
  margin-top: 20px;
}
</style>