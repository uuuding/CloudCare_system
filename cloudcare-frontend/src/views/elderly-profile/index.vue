<template>
  <div class="elderly-profile-container">
    <el-card class="profile-card" shadow="hover">
      <div class="header">
        <h2>老人档案管理</h2>
      </div>
      <div class="search-bar" style="margin-bottom: 20px; display: flex; gap: 10px; align-items: center;">
        <el-input v-model="searchQuery.name" placeholder="输入姓名" style="width: 200px;" clearable />
        <el-input v-model="searchQuery.age" placeholder="输入年龄" style="width: 200px;" clearable />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
      <el-table :data="elderlyProfiles" style="width: 100%" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="age" label="年龄" width="100" />
        <el-table-column prop="gender" label="性别" width="100" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="updateTime" label="更新时间" />
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button @click="openEditDialog(scope.row)" type="primary" size="small">编辑</el-button>
            <el-button @click="handleDelete(scope.row.id)" type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑弹窗 -->
    <el-dialog
        v-model="dialogVisible"
        title="编辑老人档案"
        width="600px"
        :before-close="closeEditDialog"
    >
      <el-form
          :model="editProfile"
          :rules="formRules"
          label-width="100px"
          ref="formRef"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editProfile.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="editProfile.age" type="number" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="editProfile.gender" placeholder="请选择性别">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeEditDialog">取消</el-button>
          <el-button type="primary" @click="handleEditSave">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElDialog, ElButton, ElInput, ElSelect, ElOption, ElForm, ElFormItem } from 'element-plus'
import { getAllElderlyProfiles, searchElderlyProfiles, updateElderlyProfile, deleteElderlyProfile } from '@/api/elderlyProfile'

const elderlyProfiles = ref([])
const searchQuery = reactive({ name: '', age: '' })
const editProfile = reactive({ id: null, name: '', age: '', gender: '' })
const dialogVisible = ref(false) // 控制弹窗的显示

// 表单验证规则
const formRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }]
}

const loadElderlyProfiles = async () => {
  try {
    const response = await getAllElderlyProfiles()
    elderlyProfiles.value = response.data || []
  } catch (error) {
    ElMessage.error('加载老人档案失败')
  }
}

const handleSearch = async () => {
  try {
    const response = await searchElderlyProfiles(searchQuery)
    elderlyProfiles.value = response.data || []
  } catch (error) {
    ElMessage.error('搜索失败')
  }
}

const resetSearch = () => {
  searchQuery.name = ''
  searchQuery.age = ''
  loadElderlyProfiles()
}

// 打开编辑弹窗并填充数据
const openEditDialog = (profile) => {
  editProfile.id = profile.id
  editProfile.name = profile.name
  editProfile.age = profile.age
  editProfile.gender = profile.gender
  dialogVisible.value = true // 打开弹窗
}

// 关闭编辑弹窗
const closeEditDialog = () => {
  dialogVisible.value = false
}

// 保存编辑的老人档案
const handleEditSave = async () => {
  try {
    const response = await updateElderlyProfile(editProfile)
    if (response) {
      ElMessage.success('更新成功')
      closeEditDialog()
      await loadElderlyProfiles()
    } else {
      ElMessage.error('更新失败')
    }
  } catch (error) {
    console.log(error)
    ElMessage.error('更新失败')
    if (error.response) {
      console.log("响应错误：", error.response.data)
    } else if (error.request) {
      console.log("请求没有收到响应：", error.request)
    } else {
      console.log("请求设置错误：", error.message)
    }
  }
}

const handleDelete = async (id) => {
  try {
    const response = await deleteElderlyProfile(id)
    if (response.data) {
      ElMessage.success('删除成功')
      await loadElderlyProfiles()
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

onMounted(() => {
  loadElderlyProfiles()
})
</script>

<style>
/* 样式：已包含Element Plus对话框的样式，默认居中并可适应大小 */
.dialog-footer {
  text-align: right;
  margin-top: 20px;
}

.dialog-footer button {
  margin-left: 10px;
}
</style>


