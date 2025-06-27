<template>
  <div class="elderly-profile-container">
    <div class="header">
      <h1>老人档案管理</h1>
      <el-button type="primary" @click="handleAdd" :icon="Plus">新增老人</el-button>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.realName" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
          <el-button @click="handleReset" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格区域 -->
    <div class="table-area">
      <el-table 
        :data="tableData" 
        v-loading="loading" 
        border 
        stripe
        style="width: 100%"
      >
        <el-table-column prop="userId" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <span v-if="row.gender === 1">男</span>
            <span v-else-if="row.gender === 2">女</span>
            <span v-else>未知</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)" :icon="Edit">编辑</el-button>
            <el-button 
              :type="row.status === 1 ? 'warning' : 'success'" 
              size="small" 
              @click="handleStatusChange(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)" :icon="Delete">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页区域 -->
    <div class="pagination-area">
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      v-model="dialogVisible" 
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form 
        ref="formRef" 
        :model="formData" 
        :rules="formRules" 
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="请输入用户名" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="formData.realName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
            <el-radio :label="0">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input 
            v-model="formData.remark" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, Delete } from '@element-plus/icons-vue'
import { getUserPage, addUser, updateUser, deleteUser, updateUserStatus } from '@/api/user'
import { isPhone, isEmail, isValidUsername } from '@/utils/validate'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

// 搜索表单
const searchForm = reactive({
  realName: '',
  phone: '',
  status: ''
})

// 分页信息
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 表单数据
const formData = reactive({
  userId: null,
  username: '',
  realName: '',
  phone: '',
  email: '',
  gender: 0,
  status: 1,
  userType: 3, // 固定为老人类型
  remark: ''
})

// 表单验证规则
const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { validator: validateUsername, trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { validator: validatePhone, trigger: 'blur' }
  ],
  email: [
    { validator: validateEmail, trigger: 'blur' }
  ]
}

// 计算属性
const dialogTitle = computed(() => isEdit.value ? '编辑老人信息' : '新增老人信息')

// 验证函数
function validateUsername(rule, value, callback) {
  if (value && !isValidUsername(value)) {
    callback(new Error('用户名只能包含字母、数字和下划线，长度3-20位'))
  } else {
    callback()
  }
}

function validatePhone(rule, value, callback) {
  if (value && !isPhone(value)) {
    callback(new Error('请输入正确的手机号码'))
  } else {
    callback()
  }
}

function validateEmail(rule, value, callback) {
  if (value && !isEmail(value)) {
    callback(new Error('请输入正确的邮箱地址'))
  } else {
    callback()
  }
}

// 格式化日期
function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// 获取列表数据
async function getList() {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      userType: 3, // 只查询老人类型
      ...searchForm
    }
    
    const response = await getUserPage(params)
    if (response && response.data) {
      tableData.value = response.data.records || []
      pagination.total = response.data.total || 0
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  pagination.pageNum = 1
  getList()
}

// 重置搜索
function handleReset() {
  Object.assign(searchForm, {
    realName: '',
    phone: '',
    status: ''
  })
  pagination.pageNum = 1
  getList()
}

// 分页大小改变
function handleSizeChange(size) {
  pagination.pageSize = size
  pagination.pageNum = 1
  getList()
}

// 当前页改变
function handleCurrentChange(page) {
  pagination.pageNum = page
  getList()
}

// 新增
function handleAdd() {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑
function handleEdit(row) {
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除
function handleDelete(row) {
  ElMessageBox.confirm(
    `确定要删除老人 "${row.realName}" 吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteUser(row.userId)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 状态切换
function handleStatusChange(row) {
  const newStatus = row.status === 1 ? 0 : 1
  const action = newStatus === 1 ? '启用' : '禁用'
  
  ElMessageBox.confirm(
    `确定要${action}老人 "${row.realName}" 吗？`,
    `${action}确认`,
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await updateUserStatus(row.userId, newStatus)
      ElMessage.success(`${action}成功`)
      getList()
    } catch (error) {
      ElMessage.error(`${action}失败`)
    }
  })
}

// 提交表单
function handleSubmit() {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitLoading.value = true
    try {
      if (isEdit.value) {
        await updateUser(formData)
        ElMessage.success('更新成功')
      } else {
        await addUser(formData)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      getList()
    } catch (error) {
      ElMessage.error(isEdit.value ? '更新失败' : '新增失败')
    } finally {
      submitLoading.value = false
    }
  })
}

// 关闭对话框
function handleDialogClose() {
  dialogVisible.value = false
  resetForm()
}

// 重置表单
function resetForm() {
  Object.assign(formData, {
    userId: null,
    username: '',
    realName: '',
    phone: '',
    email: '',
    gender: 0,
    status: 1,
    userType: 3,
    remark: ''
  })
  formRef.value?.clearValidate()
}

// 组件挂载时获取数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
.elderly-profile-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h1 {
  margin: 0;
  color: #303133;
}

.search-area {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.search-form {
  margin: 0;
}

.table-area {
  margin-bottom: 20px;
}

.pagination-area {
  display: flex;
  justify-content: center;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table th) {
  background-color: #fafafa;
}
</style>