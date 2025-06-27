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

    <!-- 编辑弹窗（浮动提示框）-->
    <div v-if="dialogVisible" class="edit-dialog">
      <div class="edit-dialog-content">
        <h3>编辑老人档案</h3>
        <form @submit.prevent="handleEditSave">
          <div>
            <label>姓名</label>
            <input v-model="editProfile.name" placeholder="请输入姓名" required />
          </div>
          <div>
            <label>年龄</label>
            <input v-model="editProfile.age" type="number" placeholder="请输入年龄" required />
          </div>
          <div>
            <label>性别</label>
            <select v-model="editProfile.gender" required>
              <option value="男">男</option>
              <option value="女">女</option>
              <option value="其他">其他</option>
            </select>
          </div>
          <div class="dialog-footer">
            <button type="button" @click="closeEditDialog">取消</button>
            <button type="submit">确定</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAllElderlyProfiles, searchElderlyProfiles, updateElderlyProfile, deleteElderlyProfile } from '@/api/elderlyProfile'

const elderlyProfiles = ref([])
const searchQuery = reactive({ name: '', age: '' })
const editProfile = reactive({ id: null, name: '', age: '', gender: '' })
const dialogVisible = ref(false) // 控制弹窗的显示

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

const openEditDialog = (profile) => {
  console.log('Opening edit dialog for profile:', profile)  // 确保数据传递正确
  editProfile.id = profile.id
  editProfile.name = profile.name
  editProfile.age = profile.age
  editProfile.gender = profile.gender
  dialogVisible.value = true // 打开弹窗
}

const closeEditDialog = () => {
  dialogVisible.value = false
}

const handleEditSave = async () => {
  try {
    const response = await updateElderlyProfile(editProfile)
    if (response.data) {
      ElMessage.success('更新成功')
      closeEditDialog()
      await loadElderlyProfiles()
    }
  } catch (error) {
    ElMessage.error('更新失败')
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
/* 新增编辑弹窗样式 */
.edit-dialog {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5); /* 遮罩层 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.edit-dialog-content {
  background: white;
  padding: 20px;
  width: 400px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.dialog-footer {
  text-align: right;
  margin-top: 20px;
}

.dialog-footer button {
  margin-left: 10px;
}
</style>
