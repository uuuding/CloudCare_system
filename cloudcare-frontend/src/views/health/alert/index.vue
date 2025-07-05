<template>
  <div class="health-alert">
    <!-- 页面标题和统计卡片 -->
    <div class="header-section">
      <h2>健康预警管理</h2>
      <div class="statistics-cards">
        <el-card class="stat-card critical">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.criticalCount || 0 }}</div>
            <div class="stat-label">危急预警</div>
          </div>
        </el-card>
        <el-card class="stat-card warning">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.highCount || 0 }}</div>
            <div class="stat-label">重度预警</div>
          </div>
        </el-card>
        <el-card class="stat-card info">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.mediumCount || 0 }}</div>
            <div class="stat-label">中度预警</div>
          </div>
        </el-card>
        <el-card class="stat-card success">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.lowCount || 0 }}</div>
            <div class="stat-label">轻度预警</div>
          </div>
        </el-card>
        <el-card class="stat-card active">
          <div class="stat-content">
            <div class="stat-number">{{ activeAlertCount }}</div>
            <div class="stat-label">活跃预警</div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <div class="filter-section">
        <el-form :model="filterForm" inline>
          <el-form-item label="预警状态">
            <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
              <el-option label="全部" value=""></el-option>
              <el-option label="活跃" value="ACTIVE"></el-option>
              <el-option label="已解决" value="RESOLVED"></el-option>
              <el-option label="已忽略" value="IGNORED"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="预警级别">
            <el-select v-model="filterForm.level" placeholder="请选择级别" clearable>
              <el-option label="全部" value=""></el-option>
              <el-option label="危急" value="CRITICAL"></el-option>
              <el-option label="重度" value="HIGH"></el-option>
              <el-option label="中度" value="MEDIUM"></el-option>
              <el-option label="轻度" value="LOW"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="预警类型">
            <el-select v-model="filterForm.type" placeholder="请选择类型" clearable>
              <el-option label="全部" value=""></el-option>
              <el-option label="体温异常" value="TEMPERATURE"></el-option>
              <el-option label="血压异常" value="BLOOD_PRESSURE"></el-option>
              <el-option label="心率异常" value="HEART_RATE"></el-option>
              <el-option label="BMI异常" value="BMI"></el-option>
              <el-option label="睡眠异常" value="SLEEP"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="filterForm.timeRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchAlerts">查询</el-button>
            <el-button @click="resetFilter">重置</el-button>
            <el-button type="success" @click="showRuleDialog = true">预警规则管理</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 预警列表 -->
    <el-card class="table-card">
      <el-table :data="alertList" v-loading="loading" stripe>
        <el-table-column prop="elderlyName" label="老人姓名" width="120"></el-table-column>
        <el-table-column prop="alertType" label="预警类型" width="120">
          <template #default="scope">
            <el-tag :type="getAlertTypeTagType(scope.row.alertType)">{{ getAlertTypeText(scope.row.alertType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertLevel" label="预警级别" width="100">
          <template #default="scope">
            <el-tag :type="getAlertLevelTagType(scope.row.alertLevel)">{{ getAlertLevelText(scope.row.alertLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertTitle" label="预警标题" width="200"></el-table-column>
        <el-table-column prop="alertDescription" label="预警描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="triggerValue" label="触发值" width="100"></el-table-column>
        <el-table-column prop="normalRange" label="正常范围" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              v-if="scope.row.status === 'ACTIVE'" 
              type="success" 
              size="small" 
              @click="handleResolve(scope.row)">
              处理
            </el-button>
            <el-button 
              v-if="scope.row.status === 'ACTIVE'" 
              type="warning" 
              size="small" 
              @click="handleIgnore(scope.row)">
              忽略
            </el-button>
            <el-button 
              type="primary" 
              size="small" 
              @click="viewDetail(scope.row)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
        </el-pagination>
      </div>
    </el-card>

    <!-- 处理预警对话框 -->
    <el-dialog v-model="resolveDialogVisible" title="处理预警" width="500px">
      <el-form :model="resolveForm" label-width="100px">
        <el-form-item label="处理人员" required>
          <el-input v-model="resolveForm.resolvedBy" placeholder="请输入处理人员姓名"></el-input>
        </el-form-item>
        <el-form-item label="处理备注">
          <el-input 
            v-model="resolveForm.resolvedNote" 
            type="textarea" 
            :rows="4" 
            placeholder="请输入处理备注">
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="resolveDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmResolve">确认处理</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 忽略预警对话框 -->
    <el-dialog v-model="ignoreDialogVisible" title="忽略预警" width="500px">
      <el-form :model="ignoreForm" label-width="100px">
        <el-form-item label="操作人员" required>
          <el-input v-model="ignoreForm.resolvedBy" placeholder="请输入操作人员姓名"></el-input>
        </el-form-item>
        <el-form-item label="忽略原因">
          <el-input 
            v-model="ignoreForm.resolvedNote" 
            type="textarea" 
            :rows="4" 
            placeholder="请输入忽略原因">
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="ignoreDialogVisible = false">取消</el-button>
          <el-button type="warning" @click="confirmIgnore">确认忽略</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 预警详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="预警详情" width="600px">
      <div v-if="selectedAlert" class="alert-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="老人姓名">{{ selectedAlert.elderlyName }}</el-descriptions-item>
          <el-descriptions-item label="预警类型">
            <el-tag :type="getAlertTypeTagType(selectedAlert.alertType)">{{ getAlertTypeText(selectedAlert.alertType) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警级别">
            <el-tag :type="getAlertLevelTagType(selectedAlert.alertLevel)">{{ getAlertLevelText(selectedAlert.alertLevel) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警状态">
            <el-tag :type="getStatusTagType(selectedAlert.status)">{{ getStatusText(selectedAlert.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警标题" :span="2">{{ selectedAlert.alertTitle }}</el-descriptions-item>
          <el-descriptions-item label="预警描述" :span="2">{{ selectedAlert.alertDescription }}</el-descriptions-item>
          <el-descriptions-item label="触发值">{{ selectedAlert.triggerValue }}</el-descriptions-item>
          <el-descriptions-item label="正常范围">{{ selectedAlert.normalRange }}</el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ formatDateTime(selectedAlert.createdAt) }}</el-descriptions-item>
          <el-descriptions-item v-if="selectedAlert.resolvedAt" label="解决时间" :span="2">{{ formatDateTime(selectedAlert.resolvedAt) }}</el-descriptions-item>
          <el-descriptions-item v-if="selectedAlert.resolvedBy" label="处理人员">{{ selectedAlert.resolvedBy }}</el-descriptions-item>
          <el-descriptions-item v-if="selectedAlert.resolvedNote" label="处理备注" :span="2">{{ selectedAlert.resolvedNote }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 预警规则管理对话框 -->
    <el-dialog v-model="showRuleDialog" title="预警规则管理" width="80%" top="5vh">
      <div class="rule-management">
        <div class="rule-actions">
          <el-button type="primary" @click="showAddRuleDialog = true">添加规则</el-button>
          <el-button type="success" @click="initDefaultRules">初始化默认规则</el-button>
        </div>
        
        <el-table :data="ruleList" v-loading="ruleLoading" stripe>
          <el-table-column prop="ruleName" label="规则名称" width="200"></el-table-column>
          <el-table-column prop="alertType" label="预警类型" width="120">
            <template #default="scope">
              <el-tag :type="getAlertTypeTagType(scope.row.alertType)">{{ getAlertTypeText(scope.row.alertType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="alertLevel" label="预警级别" width="100">
            <template #default="scope">
              <el-tag :type="getAlertLevelTagType(scope.row.alertLevel)">{{ getAlertLevelText(scope.row.alertLevel) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="minThreshold" label="最小阈值" width="100"></el-table-column>
          <el-table-column prop="maxThreshold" label="最大阈值" width="100"></el-table-column>
          <el-table-column prop="messageTemplate" label="消息模板" show-overflow-tooltip></el-table-column>
          <el-table-column prop="enabled" label="状态" width="80">
            <template #default="scope">
              <el-tag :type="scope.row.enabled ? 'success' : 'danger'">{{ scope.row.enabled ? '启用' : '禁用' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" @click="editRule(scope.row)">编辑</el-button>
              <el-button 
                :type="scope.row.enabled ? 'warning' : 'success'" 
                size="small" 
                @click="toggleRule(scope.row)">
                {{ scope.row.enabled ? '禁用' : '启用' }}
              </el-button>
              <el-button type="danger" size="small" @click="deleteRuleConfirm(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

    <!-- 添加/编辑规则对话框 -->
    <el-dialog v-model="showAddRuleDialog" :title="editingRule ? '编辑规则' : '添加规则'" width="600px">
      <el-form :model="ruleForm" :rules="ruleRules" ref="ruleFormRef" label-width="120px">
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="ruleForm.ruleName" placeholder="请输入规则名称"></el-input>
        </el-form-item>
        <el-form-item label="预警类型" prop="alertType">
          <el-select v-model="ruleForm.alertType" placeholder="请选择预警类型">
            <el-option label="体温异常" value="TEMPERATURE"></el-option>
            <el-option label="血压异常" value="BLOOD_PRESSURE"></el-option>
            <el-option label="心率异常" value="HEART_RATE"></el-option>
            <el-option label="BMI异常" value="BMI"></el-option>
            <el-option label="睡眠异常" value="SLEEP"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预警级别" prop="alertLevel">
          <el-select v-model="ruleForm.alertLevel" placeholder="请选择预警级别">
            <el-option label="危急" value="CRITICAL"></el-option>
            <el-option label="重度" value="HIGH"></el-option>
            <el-option label="中度" value="MEDIUM"></el-option>
            <el-option label="轻度" value="LOW"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="最小阈值">
          <el-input-number v-model="ruleForm.minThreshold" :precision="2" placeholder="最小阈值"></el-input-number>
        </el-form-item>
        <el-form-item label="最大阈值">
          <el-input-number v-model="ruleForm.maxThreshold" :precision="2" placeholder="最大阈值"></el-input-number>
        </el-form-item>
        <el-form-item label="消息模板" prop="messageTemplate">
          <el-input v-model="ruleForm.messageTemplate" type="textarea" :rows="3" placeholder="请输入消息模板"></el-input>
        </el-form-item>
        <el-form-item label="适用年龄">
          <el-col :span="11">
            <el-input-number v-model="ruleForm.minAge" placeholder="最小年龄"></el-input-number>
          </el-col>
          <el-col :span="2" class="text-center">-</el-col>
          <el-col :span="11">
            <el-input-number v-model="ruleForm.maxAge" placeholder="最大年龄"></el-input-number>
          </el-col>
        </el-form-item>
        <el-form-item label="适用性别">
          <el-select v-model="ruleForm.gender" placeholder="请选择适用性别" clearable>
            <el-option label="男" value="MALE"></el-option>
            <el-option label="女" value="FEMALE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="启用状态">
          <el-switch v-model="ruleForm.enabled"></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelRuleEdit">取消</el-button>
          <el-button type="primary" @click="saveRule">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getAllAlerts,
  getAlertsByStatus,
  getAlertsByLevel,
  getAlertsByType,
  getAlertsByTimeRange,
  resolveAlert,
  ignoreAlert,
  getActiveAlertCount,
  getAlertStatistics,
  getAllRules,
  addRule,
  updateRule,
  deleteRule,
  toggleRuleStatus,
  initializeDefaultRules
} from '@/api/healthAlert'

// 响应式数据
const loading = ref(false)
const ruleLoading = ref(false)
const alertList = ref([])
const ruleList = ref([])
const activeAlertCount = ref(0)
const statistics = ref({})
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

// 对话框显示状态
const resolveDialogVisible = ref(false)
const ignoreDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const showRuleDialog = ref(false)
const showAddRuleDialog = ref(false)

// 选中的预警记录
const selectedAlert = ref(null)
const currentAlert = ref(null)
const editingRule = ref(false)

// 筛选表单
const filterForm = reactive({
  status: '',
  level: '',
  type: '',
  timeRange: []
})

// 处理表单
const resolveForm = reactive({
  resolvedBy: '',
  resolvedNote: ''
})

// 忽略表单
const ignoreForm = reactive({
  resolvedBy: '',
  resolvedNote: ''
})

// 规则表单
const ruleForm = reactive({
  ruleId: null,
  ruleName: '',
  alertType: '',
  alertLevel: '',
  minThreshold: null,
  maxThreshold: null,
  messageTemplate: '',
  minAge: null,
  maxAge: null,
  gender: '',
  enabled: true
})

// 表单验证规则
const ruleRules = {
  ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
  alertType: [{ required: true, message: '请选择预警类型', trigger: 'change' }],
  alertLevel: [{ required: true, message: '请选择预警级别', trigger: 'change' }],
  messageTemplate: [{ required: true, message: '请输入消息模板', trigger: 'blur' }]
}

const ruleFormRef = ref(null)

// 计算属性
const paginatedAlerts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return alertList.value.slice(start, end)
})

// 生命周期
onMounted(() => {
  loadAlerts()
  loadStatistics()
  loadActiveAlertCount()
})

// 方法
const loadAlerts = async () => {
  loading.value = true
  try {
    const response = await getAllAlerts()
    if (response.code === 200) {
      alertList.value = response.data || []
      total.value = alertList.value.length
    }
  } catch (error) {
    console.error('加载预警记录失败:', error)
    ElMessage.error('加载预警记录失败')
  } finally {
    loading.value = false
  }
}

const loadStatistics = async () => {
  try {
    const response = await getAlertStatistics()
    if (response.code === 200) {
      statistics.value = response.data || {}
    }
  } catch (error) {
    console.error('加载统计信息失败:', error)
  }
}

const loadActiveAlertCount = async () => {
  try {
    const response = await getActiveAlertCount()
    if (response.code === 200) {
      activeAlertCount.value = response.data || 0
    }
  } catch (error) {
    console.error('加载活跃预警数量失败:', error)
  }
}

const loadRules = async () => {
  ruleLoading.value = true
  try {
    const response = await getAllRules()
    if (response.code === 200) {
      ruleList.value = response.data || []
    }
  } catch (error) {
    console.error('加载预警规则失败:', error)
    ElMessage.error('加载预警规则失败')
  } finally {
    ruleLoading.value = false
  }
}

const searchAlerts = async () => {
  loading.value = true
  try {
    let response
    if (filterForm.status) {
      response = await getAlertsByStatus(filterForm.status)
    } else if (filterForm.level) {
      response = await getAlertsByLevel(filterForm.level)
    } else if (filterForm.type) {
      response = await getAlertsByType(filterForm.type)
    } else if (filterForm.timeRange && filterForm.timeRange.length === 2) {
      response = await getAlertsByTimeRange(filterForm.timeRange[0], filterForm.timeRange[1])
    } else {
      response = await getAllAlerts()
    }
    
    if (response.code === 200) {
      alertList.value = response.data || []
      total.value = alertList.value.length
      currentPage.value = 1
    }
  } catch (error) {
    console.error('搜索预警记录失败:', error)
    ElMessage.error('搜索预警记录失败')
  } finally {
    loading.value = false
  }
}

const resetFilter = () => {
  filterForm.status = ''
  filterForm.level = ''
  filterForm.type = ''
  filterForm.timeRange = []
  loadAlerts()
}

const handleResolve = (alert) => {
  currentAlert.value = alert
  resolveForm.resolvedBy = ''
  resolveForm.resolvedNote = ''
  resolveDialogVisible.value = true
}

const handleIgnore = (alert) => {
  currentAlert.value = alert
  ignoreForm.resolvedBy = ''
  ignoreForm.resolvedNote = ''
  ignoreDialogVisible.value = true
}

const confirmResolve = async () => {
  if (!resolveForm.resolvedBy) {
    ElMessage.warning('请输入处理人员')
    return
  }
  
  try {
    const response = await resolveAlert(
      currentAlert.value.alertId,
      resolveForm.resolvedBy,
      resolveForm.resolvedNote
    )
    if (response.code === 200) {
      ElMessage.success('预警处理成功')
      resolveDialogVisible.value = false
      loadAlerts()
      loadStatistics()
      loadActiveAlertCount()
    }
  } catch (error) {
    console.error('处理预警失败:', error)
    ElMessage.error('处理预警失败')
  }
}

const confirmIgnore = async () => {
  if (!ignoreForm.resolvedBy) {
    ElMessage.warning('请输入操作人员')
    return
  }
  
  try {
    const response = await ignoreAlert(
      currentAlert.value.alertId,
      ignoreForm.resolvedBy,
      ignoreForm.resolvedNote
    )
    if (response.code === 200) {
      ElMessage.success('预警忽略成功')
      ignoreDialogVisible.value = false
      loadAlerts()
      loadStatistics()
      loadActiveAlertCount()
    }
  } catch (error) {
    console.error('忽略预警失败:', error)
    ElMessage.error('忽略预警失败')
  }
}

const viewDetail = (alert) => {
  selectedAlert.value = alert
  detailDialogVisible.value = true
}

const editRule = (rule) => {
  editingRule.value = true
  Object.assign(ruleForm, rule)
  showAddRuleDialog.value = true
}

const toggleRule = async (rule) => {
  try {
    const response = await toggleRuleStatus(rule.ruleId, !rule.enabled)
    if (response.code === 200) {
      ElMessage.success('规则状态更新成功')
      loadRules()
    }
  } catch (error) {
    console.error('更新规则状态失败:', error)
    ElMessage.error('更新规则状态失败')
  }
}

const deleteRuleConfirm = (rule) => {
  ElMessageBox.confirm(
    `确定要删除规则 "${rule.ruleName}" 吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await deleteRule(rule.ruleId)
      if (response.code === 200) {
        ElMessage.success('规则删除成功')
        loadRules()
      }
    } catch (error) {
      console.error('删除规则失败:', error)
      ElMessage.error('删除规则失败')
    }
  })
}

const saveRule = async () => {
  if (!ruleFormRef.value) return
  
  try {
    await ruleFormRef.value.validate()
    
    const response = editingRule.value 
      ? await updateRule(ruleForm)
      : await addRule(ruleForm)
      
    if (response.code === 200) {
      ElMessage.success(editingRule.value ? '规则更新成功' : '规则添加成功')
      showAddRuleDialog.value = false
      loadRules()
      cancelRuleEdit()
    }
  } catch (error) {
    console.error('保存规则失败:', error)
    ElMessage.error('保存规则失败')
  }
}

const cancelRuleEdit = () => {
  editingRule.value = false
  Object.assign(ruleForm, {
    ruleId: null,
    ruleName: '',
    alertType: '',
    alertLevel: '',
    minThreshold: null,
    maxThreshold: null,
    messageTemplate: '',
    minAge: null,
    maxAge: null,
    gender: '',
    enabled: true
  })
  if (ruleFormRef.value) {
    ruleFormRef.value.clearValidate()
  }
}

const initDefaultRules = async () => {
  try {
    const response = await initializeDefaultRules()
    if (response.code === 200) {
      ElMessage.success('默认规则初始化成功')
      loadRules()
    }
  } catch (error) {
    console.error('初始化默认规则失败:', error)
    ElMessage.error('初始化默认规则失败')
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

// 监听规则对话框打开
const watchRuleDialog = () => {
  if (showRuleDialog.value) {
    loadRules()
  }
}

// 工具方法
const getAlertTypeText = (type) => {
  const typeMap = {
    'TEMPERATURE': '体温异常',
    'BLOOD_PRESSURE': '血压异常',
    'HEART_RATE': '心率异常',
    'BMI': 'BMI异常',
    'SLEEP': '睡眠异常'
  }
  return typeMap[type] || type
}

const getAlertTypeTagType = (type) => {
  const typeMap = {
    'TEMPERATURE': 'danger',
    'BLOOD_PRESSURE': 'warning',
    'HEART_RATE': 'danger',
    'BMI': 'info',
    'SLEEP': 'success'
  }
  return typeMap[type] || ''
}

const getAlertLevelText = (level) => {
  const levelMap = {
    'CRITICAL': '危急',
    'HIGH': '重度',
    'MEDIUM': '中度',
    'LOW': '轻度'
  }
  return levelMap[level] || level
}

const getAlertLevelTagType = (level) => {
  const levelMap = {
    'CRITICAL': 'danger',
    'HIGH': 'warning',
    'MEDIUM': 'info',
    'LOW': 'success'
  }
  return levelMap[level] || ''
}

const getStatusText = (status) => {
  const statusMap = {
    'ACTIVE': '活跃',
    'RESOLVED': '已解决',
    'IGNORED': '已忽略'
  }
  return statusMap[status] || status
}

const getStatusTagType = (status) => {
  const statusMap = {
    'ACTIVE': 'danger',
    'RESOLVED': 'success',
    'IGNORED': 'info'
  }
  return statusMap[status] || ''
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  })
}

// 监听规则对话框
watch(() => showRuleDialog.value, watchRuleDialog)
</script>

<style scoped>
.health-alert {
  padding: 20px;
}

.header-section {
  margin-bottom: 20px;
}

.header-section h2 {
  margin-bottom: 20px;
  color: #303133;
}

.statistics-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-card.critical {
  border-left: 4px solid #f56c6c;
}

.stat-card.warning {
  border-left: 4px solid #e6a23c;
}

.stat-card.info {
  border-left: 4px solid #409eff;
}

.stat-card.success {
  border-left: 4px solid #67c23a;
}

.stat-card.active {
  border-left: 4px solid #909399;
}

.stat-content {
  padding: 20px;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.filter-card {
  margin-bottom: 20px;
}

.filter-section {
  padding: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.alert-detail {
  padding: 20px;
}

.rule-management {
  padding: 20px;
}

.rule-actions {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.text-center {
  text-align: center;
  line-height: 32px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .statistics-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .filter-section .el-form {
    display: block;
  }
  
  .filter-section .el-form-item {
    margin-bottom: 15px;
  }
}

@media (max-width: 480px) {
  .statistics-cards {
    grid-template-columns: 1fr;
  }
}
</style>