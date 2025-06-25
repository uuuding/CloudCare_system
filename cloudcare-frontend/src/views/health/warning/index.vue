<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="老人ID" prop="elderId">
          <el-input v-model="queryParams.elderId" placeholder="请输入老人ID" clearable />
        </el-form-item>
        <el-form-item label="预警类型" prop="warningType">
          <el-select v-model="queryParams.warningType" placeholder="请选择预警类型" clearable>
            <el-option v-for="dict in warningTypeOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警级别" prop="warningLevel">
          <el-select v-model="queryParams.warningLevel" placeholder="请选择预警级别" clearable>
            <el-option v-for="dict in warningLevelOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态" prop="processStatus">
          <el-select v-model="queryParams.processStatus" placeholder="请选择处理状态" clearable>
            <el-option v-for="dict in processStatusOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <el-icon><Search /></el-icon> 搜索
          </el-button>
          <el-button @click="resetQuery">
            <el-icon><Refresh /></el-icon> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">健康预警列表</span>
          <div class="right-btns">
            <el-button type="primary" @click="handleAdd" v-if="hasRole(['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE'])">
              <el-icon><Plus /></el-icon> 新增
            </el-button>
            <el-button type="danger" @click="handleBatchDelete" :disabled="selectedRows.length === 0" v-if="hasRole(['ROLE_ADMIN'])">
              <el-icon><Delete /></el-icon> 批量删除
            </el-button>
            <el-button @click="handleRefresh">
              <el-icon><Refresh /></el-icon> 刷新
            </el-button>
          </div>
        </div>
      </template>

      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="warningList"
        @selection-change="handleSelectionChange"
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="预警ID" prop="warningId" width="80" />
        <el-table-column label="老人ID" prop="elderId" width="80" />
        <el-table-column label="预警类型" prop="warningType" width="120">
          <template #default="scope">
            <el-tag :type="getWarningTypeTag(scope.row.warningType)">
              {{ getWarningTypeLabel(scope.row.warningType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警级别" prop="warningLevel" width="100">
          <template #default="scope">
            <el-tag :type="getWarningLevelTag(scope.row.warningLevel)">
              {{ getWarningLevelLabel(scope.row.warningLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警内容" prop="warningContent" min-width="200" show-overflow-tooltip />
        <el-table-column label="预警时间" prop="warningTime" width="160">
          <template #default="scope">
            <span>{{ formatDateTime(scope.row.warningTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="处理状态" prop="processStatus" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.processStatus === 0 ? 'danger' : 'success'">
              {{ scope.row.processStatus === 0 ? '未处理' : '已处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="scope">
            <el-button
              type="primary"
              link
              @click="handleView(scope.row)"
            >
              <el-icon><View /></el-icon> 查看
            </el-button>
            <el-button
              type="success"
              link
              @click="handleProcess(scope.row)"
              v-if="scope.row.processStatus === 0 && hasRole(['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE'])"
            >
              <el-icon><Check /></el-icon> 处理
            </el-button>
            <el-button
              type="danger"
              link
              @click="handleDelete(scope.row)"
              v-if="hasRole(['ROLE_ADMIN'])"
            >
              <el-icon><Delete /></el-icon> 删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 预警处理对话框 -->
    <el-dialog
      v-model="processDialogVisible"
      title="处理健康预警"
      width="500px"
      append-to-body
    >
      <el-form :model="processForm" ref="processFormRef" :rules="processRules" label-width="100px">
        <el-form-item label="预警ID" prop="warningId">
          <el-input v-model="processForm.warningId" disabled />
        </el-form-item>
        <el-form-item label="预警内容" prop="warningContent">
          <el-input v-model="processForm.warningContent" type="textarea" rows="3" disabled />
        </el-form-item>
        <el-form-item label="处理结果" prop="processResult">
          <el-input v-model="processForm.processResult" type="textarea" rows="3" placeholder="请输入处理结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="processDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitProcess">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 新增预警对话框 -->
    <el-dialog
      v-model="addDialogVisible"
      title="新增健康预警"
      width="500px"
      append-to-body
    >
      <el-form :model="addForm" ref="addFormRef" :rules="addRules" label-width="100px">
        <el-form-item label="老人ID" prop="elderId">
          <el-input v-model="addForm.elderId" placeholder="请输入老人ID" />
        </el-form-item>
        <el-form-item label="预警类型" prop="warningType">
          <el-select v-model="addForm.warningType" placeholder="请选择预警类型">
            <el-option v-for="dict in warningTypeOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警级别" prop="warningLevel">
          <el-select v-model="addForm.warningLevel" placeholder="请选择预警级别">
            <el-option v-for="dict in warningLevelOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警内容" prop="warningContent">
          <el-input v-model="addForm.warningContent" type="textarea" rows="3" placeholder="请输入预警内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="addDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitAdd">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import {
  listHealthWarnings,
  getHealthWarning,
  addHealthWarning,
  updateHealthWarning,
  deleteHealthWarning,
  processHealthWarning
} from '@/api/health/warning'

// 路由实例
const router = useRouter()

// 用户状态
const userStore = useUserStore()

// 判断是否有角色权限
const hasRole = (roles) => {
  return roles.some(role => userStore.roles.includes(role))
}

// 数据列表
const warningList = ref([])
const loading = ref(false)
const total = ref(0)
const selectedRows = ref([])

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  elderId: undefined,
  warningType: undefined,
  warningLevel: undefined,
  processStatus: undefined
})

// 预警类型选项
const warningTypeOptions = [
  { value: 1, label: '体温异常' },
  { value: 2, label: '血压异常' },
  { value: 3, label: '血糖异常' },
  { value: 4, label: '心率异常' },
  { value: 5, label: '血氧异常' },
  { value: 6, label: '体重异常' },
  { value: 7, label: '其他' }
]

// 预警级别选项
const warningLevelOptions = [
  { value: 1, label: '轻度' },
  { value: 2, label: '中度' },
  { value: 3, label: '重度' }
]

// 处理状态选项
const processStatusOptions = [
  { value: 0, label: '未处理' },
  { value: 1, label: '已处理' }
]

// 获取预警类型标签类型
const getWarningTypeTag = (type) => {
  switch (type) {
    case 1: return 'danger'
    case 2: return 'warning'
    case 3: return 'warning'
    case 4: return 'danger'
    case 5: return 'danger'
    case 6: return 'warning'
    default: return 'info'
  }
}

// 获取预警类型标签文本
const getWarningTypeLabel = (type) => {
  const option = warningTypeOptions.find(item => item.value === type)
  return option ? option.label : '未知'
}

// 获取预警级别标签类型
const getWarningLevelTag = (level) => {
  switch (level) {
    case 1: return 'info'
    case 2: return 'warning'
    case 3: return 'danger'
    default: return 'info'
  }
}

// 获取预警级别标签文本
const getWarningLevelLabel = (level) => {
  const option = warningLevelOptions.find(item => item.value === level)
  return option ? option.label : '未知'
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString()
}

// 处理查询
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置查询
const resetQuery = () => {
  queryParams.elderId = undefined
  queryParams.warningType = undefined
  queryParams.warningLevel = undefined
  queryParams.processStatus = undefined
  handleQuery()
}

// 获取列表数据
const getList = () => {
  loading.value = true
  listHealthWarnings(queryParams).then(res => {
    warningList.value = res.data.records
    total.value = res.data.total
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

// 处理表格选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 处理刷新
const handleRefresh = () => {
  getList()
}

// 处理分页大小变化
const handleSizeChange = (size) => {
  queryParams.pageSize = size
  getList()
}

// 处理页码变化
const handleCurrentChange = (page) => {
  queryParams.pageNum = page
  getList()
}

// 处理查看
const handleView = (row) => {
  router.push(`/health/warning/detail/${row.warningId}`)
}

// 处理删除
const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除预警ID为${row.warningId}的数据吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    deleteHealthWarning([row.warningId]).then(res => {
      ElMessage.success('删除成功')
      getList()
    })
  }).catch(() => {})
}

// 处理批量删除
const handleBatchDelete = () => {
  const ids = selectedRows.value.map(item => item.warningId)
  ElMessageBox.confirm(`确认删除选中的${ids.length}条数据吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    deleteHealthWarning(ids).then(res => {
      ElMessage.success('批量删除成功')
      getList()
    })
  }).catch(() => {})
}

// 处理预警处理
const processDialogVisible = ref(false)
const processForm = reactive({
  warningId: undefined,
  warningContent: '',
  processResult: ''
})
const processFormRef = ref(null)
const processRules = {
  processResult: [{ required: true, message: '请输入处理结果', trigger: 'blur' }]
}

const handleProcess = (row) => {
  processForm.warningId = row.warningId
  processForm.warningContent = row.warningContent
  processForm.processResult = ''
  processDialogVisible.value = true
}

const submitProcess = () => {
  processFormRef.value.validate(valid => {
    if (valid) {
      const data = {
        warningId: processForm.warningId,
        processResult: processForm.processResult,
        processStatus: 1
      }
      processHealthWarning(data).then(res => {
        ElMessage.success('处理成功')
        processDialogVisible.value = false
        getList()
      })
    }
  })
}

// 处理新增
const addDialogVisible = ref(false)
const addForm = reactive({
  elderId: undefined,
  warningType: undefined,
  warningLevel: undefined,
  warningContent: ''
})
const addFormRef = ref(null)
const addRules = {
  elderId: [{ required: true, message: '请输入老人ID', trigger: 'blur' }],
  warningType: [{ required: true, message: '请选择预警类型', trigger: 'change' }],
  warningLevel: [{ required: true, message: '请选择预警级别', trigger: 'change' }],
  warningContent: [{ required: true, message: '请输入预警内容', trigger: 'blur' }]
}

const handleAdd = () => {
  addForm.elderId = undefined
  addForm.warningType = undefined
  addForm.warningLevel = undefined
  addForm.warningContent = ''
  addDialogVisible.value = true
}

const submitAdd = () => {
  addFormRef.value.validate(valid => {
    if (valid) {
      addHealthWarning(addForm).then(res => {
        ElMessage.success('新增成功')
        addDialogVisible.value = false
        getList()
      })
    }
  })
}

// 页面加载时获取数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
}

.right-btns {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>