<template>
  <div class="avatar-upload">
    <el-upload
      class="avatar-uploader"
      :action="uploadUrl"
      :show-file-list="false"
      :on-success="handleSuccess"
      :on-error="handleError"
      :before-upload="beforeUpload"
      :headers="uploadHeaders"
      :auto-upload="false"
      :on-change="handleChange"
    >
      <img v-if="imageUrl" :src="imageUrl" class="avatar" :style="{ width: size + 'px', height: size + 'px' }" />
      <el-icon v-else class="avatar-uploader-icon" :style="{ fontSize: size / 3 + 'px' }">
        <Plus />
      </el-icon>
    </el-upload>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getToken } from '@/utils/auth'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  size: {
    type: Number,
    default: 100
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

const imageUrl = ref('')

// 上传地址
const uploadUrl = computed(() => {
  return import.meta.env.VITE_APP_BASE_API + '/upload/avatar'
})

// 上传请求头
const uploadHeaders = computed(() => {
  return {
    'Authorization': 'Bearer ' + getToken()
  }
})

// 监听modelValue变化
watch(() => props.modelValue, (newVal) => {
  imageUrl.value = newVal
}, { immediate: true })

// 上传前验证
const beforeUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传头像图片只能是 JPG/PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 文件选择变化
const handleChange = (file) => {
  if (file.raw) {
    // 验证文件
    if (!beforeUpload(file.raw)) {
      return
    }
    
    // 创建本地预览URL
    const reader = new FileReader()
    reader.onload = (e) => {
      imageUrl.value = e.target.result
    }
    reader.readAsDataURL(file.raw)
    
    // 自动上传文件
    uploadFile(file.raw)
  }
}

// 上传文件
const uploadFile = async (file) => {
  const formData = new FormData()
  formData.append('file', file)
  
  try {
    const response = await fetch(uploadUrl.value, {
      method: 'POST',
      headers: uploadHeaders.value,
      body: formData
    })
    
    const result = await response.json()
    handleSuccess(result)
  } catch (error) {
    handleError(error)
  }
}

// 上传成功
const handleSuccess = (response) => {
  console.log('上传成功:', response)
  if (response.code === 200 && response.data) {
    const avatarUrl = response.data.url || response.data
    imageUrl.value = avatarUrl
    emit('update:modelValue', avatarUrl)
    emit('success', avatarUrl)
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error(response.message || '头像上传失败')
  }
}

// 上传失败
const handleError = (error) => {
  console.error('Upload error:', error)
  ElMessage.error('上传失败，请重试')
}
</script>

<style scoped>
.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  color: #8c939d;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fafafa;
}

.avatar {
  display: block;
  object-fit: cover;
}

:deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
}

:deep(.el-upload:hover) {
  border-color: #409eff;
}
</style>