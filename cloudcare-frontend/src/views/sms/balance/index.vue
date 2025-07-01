<template>
  <div class="sms-balance-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>短信余额查询</span>
          <el-button type="primary" @click="refreshBalance">
            <el-icon><Refresh /></el-icon>
            刷新余额
          </el-button>
        </div>
      </template>

      <!-- 余额概览 -->
      <div class="balance-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="balance-card" shadow="hover">
              <div class="balance-item">
                <div class="balance-icon">
                  <el-icon size="40" color="#409eff">
                    <Wallet />
                  </el-icon>
                </div>
                <div class="balance-info">
                  <div class="balance-title">当前余额</div>
                  <div class="balance-value">{{ balanceInfo.currentBalance }}</div>
                  <div class="balance-unit">条</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="balance-card" shadow="hover">
              <div class="balance-item">
                <div class="balance-icon">
                  <el-icon size="40" color="#67c23a">
                    <Promotion />
                  </el-icon>
                </div>
                <div class="balance-info">
                  <div class="balance-title">今日已用</div>
                  <div class="balance-value">{{ balanceInfo.todayUsed }}</div>
                  <div class="balance-unit">条</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="balance-card" shadow="hover">
              <div class="balance-item">
                <div class="balance-icon">
                  <el-icon size="40" color="#e6a23c">
                    <TrendCharts />
                  </el-icon>
                </div>
                <div class="balance-info">
                  <div class="balance-title">本月已用</div>
                  <div class="balance-value">{{ balanceInfo.monthUsed }}</div>
                  <div class="balance-unit">条</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="balance-card" shadow="hover">
              <div class="balance-item">
                <div class="balance-icon">
                  <el-icon size="40" color="#f56c6c">
                    <Warning />
                  </el-icon>
                </div>
                <div class="balance-info">
                  <div class="balance-title">预警阈值</div>
                  <div class="balance-value">{{ balanceInfo.warningThreshold }}</div>
                  <div class="balance-unit">条</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 余额状态 -->
      <div class="balance-status">
        <el-alert
          v-if="balanceInfo.currentBalance <= balanceInfo.warningThreshold"
          title="余额不足警告"
          :description="`当前余额仅剩 ${balanceInfo.currentBalance} 条，建议及时充值以免影响正常使用。`"
          type="warning"
          show-icon
          :closable="false"
        />
        <el-alert
          v-else-if="balanceInfo.currentBalance <= balanceInfo.warningThreshold * 2"
          title="余额提醒"
          :description="`当前余额为 ${balanceInfo.currentBalance} 条，请关注余额变化。`"
          type="info"
          show-icon
          :closable="false"
        />
        <el-alert
          v-else
          title="余额充足"
          :description="`当前余额为 ${balanceInfo.currentBalance} 条，余额充足。`"
          type="success"
          show-icon
          :closable="false"
        />
      </div>

      <!-- 使用统计图表 -->
      <div class="usage-charts">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="chart-card" shadow="hover">
              <template #header>
                <div class="chart-header">
                  <span>近7天使用趋势</span>
                  <el-button type="text" @click="refreshCharts">
                    <el-icon><Refresh /></el-icon>
                  </el-button>
                </div>
              </template>
              <div ref="weeklyChart" class="chart-container"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="chart-card" shadow="hover">
              <template #header>
                <div class="chart-header">
                  <span>短信类型分布</span>
                  <el-button type="text" @click="refreshCharts">
                    <el-icon><Refresh /></el-icon>
                  </el-button>
                </div>
              </template>
              <div ref="typeChart" class="chart-container"></div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 充值记录 -->
      <div class="recharge-section">
        <div class="section-header">
          <h3>充值记录</h3>
          <el-button type="primary" @click="showRechargeDialog">
            <el-icon><Plus /></el-icon>
            申请充值
          </el-button>
        </div>
        
        <el-table
          v-loading="rechargeLoading"
          :data="rechargeRecords"
          stripe
          border
          style="width: 100%"
        >
          <el-table-column prop="id" label="充值ID" width="100" />
          <el-table-column prop="amount" label="充值数量" width="120">
            <template #default="{ row }">
              <span class="amount-text">+{{ row.amount }} 条</span>
            </template>
          </el-table-column>
          <el-table-column prop="cost" label="充值金额" width="120">
            <template #default="{ row }">
              <span class="cost-text">¥{{ row.cost }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getRechargeStatusColor(row.status)" size="small">
                {{ getRechargeStatusName(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="rechargeTime" label="充值时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.rechargeTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="operator" label="操作员" width="120" />
          <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="{ row }">
              <el-button type="text" size="small" @click="viewRechargeDetail(row)">
                查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 充值记录分页 -->
        <div class="pagination-area">
          <el-pagination
            v-model:current-page="rechargePagination.currentPage"
            v-model:page-size="rechargePagination.pageSize"
            :page-sizes="[10, 20, 50]"
            :total="rechargePagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleRechargeSizeChange"
            @current-change="handleRechargeCurrentChange"
          />
        </div>
      </div>
    </el-card>

    <!-- 充值申请对话框 -->
    <el-dialog
      v-model="rechargeDialogVisible"
      title="申请充值"
      width="500px"
      :before-close="handleRechargeDialogClose"
    >
      <el-form
        ref="rechargeFormRef"
        :model="rechargeForm"
        :rules="rechargeRules"
        label-width="100px"
      >
        <el-form-item label="充值套餐" prop="package">
          <el-radio-group v-model="rechargeForm.package">
            <el-radio-button
              v-for="pkg in rechargePackages"
              :key="pkg.id"
              :label="pkg.id"
            >
              {{ pkg.name }}
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="套餐详情">
          <div v-if="selectedPackage" class="package-detail">
            <p><strong>数量：</strong>{{ selectedPackage.amount }} 条</p>
            <p><strong>价格：</strong>¥{{ selectedPackage.price }}</p>
            <p><strong>单价：</strong>¥{{ (selectedPackage.price / selectedPackage.amount).toFixed(4) }}/条</p>
            <p><strong>有效期：</strong>{{ selectedPackage.validity }}</p>
          </div>
        </el-form-item>
        
        <el-form-item label="申请说明" prop="remark">
          <el-input
            v-model="rechargeForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入充值申请说明"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rechargeDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitRechargeApplication">
            提交申请
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 充值详情对话框 -->
    <el-dialog
      v-model="rechargeDetailDialogVisible"
      title="充值详情"
      width="600px"
    >
      <div v-if="currentRechargeRecord" class="recharge-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="充值ID">
            {{ currentRechargeRecord.id }}
          </el-descriptions-item>
          <el-descriptions-item label="充值状态">
            <el-tag :type="getRechargeStatusColor(currentRechargeRecord.status)">
              {{ getRechargeStatusName(currentRechargeRecord.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="充值数量">
            <span class="amount-text">+{{ currentRechargeRecord.amount }} 条</span>
          </el-descriptions-item>
          <el-descriptions-item label="充值金额">
            <span class="cost-text">¥{{ currentRechargeRecord.cost }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="充值时间">
            {{ formatDateTime(currentRechargeRecord.rechargeTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="操作员">
            {{ currentRechargeRecord.operator }}
          </el-descriptions-item>
          <el-descriptions-item label="备注" span="2">
            {{ currentRechargeRecord.remark || '-' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rechargeDetailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Refresh,
  Wallet,
  Promotion,
  TrendCharts,
  Warning,
  Plus
} from '@element-plus/icons-vue'
import { smsApi } from '@/api/sms'

// 响应式数据
const rechargeLoading = ref(false)
const rechargeDialogVisible = ref(false)
const rechargeDetailDialogVisible = ref(false)
const currentRechargeRecord = ref(null)
const rechargeFormRef = ref(null)
const weeklyChart = ref(null)
const typeChart = ref(null)

// 余额信息
const balanceInfo = reactive({
  currentBalance: 8520,
  todayUsed: 45,
  monthUsed: 1280,
  warningThreshold: 1000
})

// 充值套餐
const rechargePackages = ref([
  {
    id: 1,
    name: '基础套餐',
    amount: 1000,
    price: 50,
    validity: '1年'
  },
  {
    id: 2,
    name: '标准套餐',
    amount: 5000,
    price: 200,
    validity: '1年'
  },
  {
    id: 3,
    name: '企业套餐',
    amount: 10000,
    price: 350,
    validity: '1年'
  },
  {
    id: 4,
    name: '旗舰套餐',
    amount: 50000,
    price: 1500,
    validity: '1年'
  }
])

// 充值表单
const rechargeForm = reactive({
  package: 1,
  remark: ''
})

// 充值表单验证规则
const rechargeRules = {
  package: [
    { required: true, message: '请选择充值套餐', trigger: 'change' }
  ],
  remark: [
    { required: true, message: '请输入申请说明', trigger: 'blur' },
    { min: 5, max: 200, message: '申请说明长度在 5 到 200 个字符', trigger: 'blur' }
  ]
}

// 充值分页
const rechargePagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 充值记录
const rechargeRecords = ref([
  {
    id: 'R202401001',
    amount: 10000,
    cost: 350,
    status: 'completed',
    rechargeTime: '2024-01-10 14:30:00',
    operator: '系统管理员',
    remark: '企业套餐充值'
  },
  {
    id: 'R202312002',
    amount: 5000,
    cost: 200,
    status: 'completed',
    rechargeTime: '2023-12-15 09:20:00',
    operator: '财务部',
    remark: '标准套餐充值'
  },
  {
    id: 'R202312001',
    amount: 1000,
    cost: 50,
    status: 'pending',
    rechargeTime: '2023-12-01 16:45:00',
    operator: '系统管理员',
    remark: '基础套餐充值申请'
  }
])

// 计算属性
const selectedPackage = computed(() => {
  return rechargePackages.value.find(pkg => pkg.id === rechargeForm.package)
})

// 方法
const getRechargeStatusColor = (status) => {
  const colorMap = {
    completed: 'success',
    pending: 'warning',
    failed: 'danger',
    cancelled: 'info'
  }
  return colorMap[status] || 'info'
}

const getRechargeStatusName = (status) => {
  const nameMap = {
    completed: '已完成',
    pending: '待处理',
    failed: '失败',
    cancelled: '已取消'
  }
  return nameMap[status] || '未知'
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

const refreshBalance = async () => {
  try {
    // 调用API获取余额信息
    const response = await smsApi.getBalance()
    if (response.success) {
      Object.assign(balanceInfo, response.data)
      ElMessage.success('余额信息已更新')
    } else {
      ElMessage.error(response.message || '获取余额信息失败')
    }
  } catch (error) {
    console.error('获取余额信息失败:', error)
    ElMessage.error('获取余额信息失败')
  }
}

const refreshCharts = () => {
  initWeeklyChart()
  initTypeChart()
  ElMessage.success('图表已刷新')
}

const initWeeklyChart = () => {
  // 这里应该使用 ECharts 或其他图表库
  // 模拟图表初始化
  if (weeklyChart.value) {
    weeklyChart.value.innerHTML = `
      <div style="height: 200px; display: flex; align-items: center; justify-content: center; background: #f5f7fa; border-radius: 4px;">
        <span style="color: #909399;">近7天使用趋势图表</span>
      </div>
    `
  }
}

const initTypeChart = () => {
  // 这里应该使用 ECharts 或其他图表库
  // 模拟图表初始化
  if (typeChart.value) {
    typeChart.value.innerHTML = `
      <div style="height: 200px; display: flex; align-items: center; justify-content: center; background: #f5f7fa; border-radius: 4px;">
        <span style="color: #909399;">短信类型分布图表</span>
      </div>
    `
  }
}

const showRechargeDialog = () => {
  rechargeForm.package = 1
  rechargeForm.remark = ''
  rechargeDialogVisible.value = true
}

const handleRechargeDialogClose = () => {
  rechargeDialogVisible.value = false
  if (rechargeFormRef.value) {
    rechargeFormRef.value.resetFields()
  }
}

const submitRechargeApplication = async () => {
  try {
    const valid = await rechargeFormRef.value.validate()
    if (!valid) return
    
    const selectedPkg = selectedPackage.value
    if (!selectedPkg) {
      ElMessage.error('请选择充值套餐')
      return
    }
    
    // 调用API提交充值申请
    // const response = await smsApi.submitRechargeApplication({
    //   packageId: rechargeForm.package,
    //   amount: selectedPkg.amount,
    //   cost: selectedPkg.price,
    //   remark: rechargeForm.remark
    // })
    
    // 模拟成功响应
    ElMessage.success('充值申请已提交，请等待审核')
    rechargeDialogVisible.value = false
    loadRechargeRecords()
  } catch (error) {
    console.error('提交充值申请失败:', error)
    ElMessage.error('提交充值申请失败')
  }
}

const viewRechargeDetail = (record) => {
  currentRechargeRecord.value = record
  rechargeDetailDialogVisible.value = true
}

const handleRechargeSizeChange = (size) => {
  rechargePagination.pageSize = size
  rechargePagination.currentPage = 1
  loadRechargeRecords()
}

const handleRechargeCurrentChange = (page) => {
  rechargePagination.currentPage = page
  loadRechargeRecords()
}

const loadRechargeRecords = async () => {
  try {
    rechargeLoading.value = true
    
    // 调用API获取充值记录
    // const response = await smsApi.getRechargeRecords({
    //   page: rechargePagination.currentPage,
    //   size: rechargePagination.pageSize
    // })
    
    // 模拟数据
    rechargePagination.total = rechargeRecords.value.length
  } catch (error) {
    console.error('加载充值记录失败:', error)
    ElMessage.error('加载充值记录失败')
  } finally {
    rechargeLoading.value = false
  }
}

onMounted(() => {
  refreshBalance()
  loadRechargeRecords()
  
  nextTick(() => {
    initWeeklyChart()
    initTypeChart()
  })
})
</script>

<style scoped>
.sms-balance-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  font-size: 16px;
}

.balance-overview {
  margin-bottom: 20px;
}

.balance-card {
  height: 120px;
}

.balance-item {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 10px;
}

.balance-icon {
  margin-right: 15px;
}

.balance-info {
  flex: 1;
}

.balance-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.balance-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.balance-unit {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

.balance-status {
  margin-bottom: 20px;
}

.usage-charts {
  margin-bottom: 30px;
}

.chart-card {
  height: 300px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.chart-container {
  height: 220px;
}

.recharge-section {
  margin-top: 30px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
}

.pagination-area {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.amount-text {
  color: #67c23a;
  font-weight: bold;
}

.cost-text {
  color: #e6a23c;
  font-weight: bold;
}

.package-detail {
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.package-detail p {
  margin: 5px 0;
  font-size: 14px;
}

.recharge-detail {
  max-height: 400px;
  overflow-y: auto;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.el-alert) {
  margin-bottom: 0;
}

:deep(.el-descriptions__label) {
  font-weight: bold;
}

:deep(.el-radio-button__inner) {
  padding: 8px 15px;
}
</style>