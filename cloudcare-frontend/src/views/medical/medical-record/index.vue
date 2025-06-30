<template>
  <div class="medical-record-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>电子病历共享</h1>
      <p>查看和管理老人的电子病历信息</p>
    </div>

    <!-- 搜索区域 -->
    <div class="search-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入老人姓名"
            clearable
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-select
            v-model="searchForm.ageRange"
            placeholder="请选择年龄段"
            clearable
            @change="handleSearch"
          >
            <el-option label="60-70岁" value="60-70" />
            <el-option label="70-80岁" value="70-80" />
            <el-option label="80-90岁" value="80-90" />
            <el-option label="90岁以上" value="90+" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 老人列表 -->
    <div class="elderly-list">
      <el-row :gutter="20">
        <el-col :span="8" v-for="elderly in filteredElderlyList" :key="elderly.id">
          <el-card class="elderly-card" @click="selectElderly(elderly)" :class="{ active: selectedElderly?.id === elderly.id }">
            <div class="elderly-info">
              <div class="elderly-avatar">
                <el-avatar :size="60" :src="elderly.avatar">
                  <el-icon><User /></el-icon>
                </el-avatar>
              </div>
              <div class="elderly-details">
                <h3>{{ elderly.name }}</h3>
                <p><span class="label">年龄：</span>{{ elderly.age }}岁</p>
                <p><span class="label">性别：</span>{{ elderly.gender === 'M' ? '男' : '女' }}</p>
                <p><span class="label">最近体检：</span>{{ getLastObservationDate(elderly.id) }}</p>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 病历详情 -->
    <div class="medical-details" v-if="selectedElderly">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>{{ selectedElderly.name }} 的电子病历</span>
            <el-button type="primary" @click="refreshMedicalData">
              <el-icon><Refresh /></el-icon>
              刷新数据
            </el-button>
          </div>
        </template>

        <el-tabs v-model="activeTab" type="card">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <div class="basic-info">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="姓名">{{ selectedElderly.name }}</el-descriptions-item>
                <el-descriptions-item label="年龄">{{ selectedElderly.age }}岁</el-descriptions-item>
                <el-descriptions-item label="性别">{{ selectedElderly.gender === 'M' ? '男' : '女' }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ formatDate(selectedElderly.createTime) }}</el-descriptions-item>
                <el-descriptions-item label="更新时间">{{ formatDate(selectedElderly.updateTime) }}</el-descriptions-item>
              </el-descriptions>
            </div>
          </el-tab-pane>

          <!-- 体检记录 -->
          <el-tab-pane label="体检记录" name="observations">
            <div class="observations-section">
              <div class="section-header">
                <h3>体检记录历史</h3>
                <el-date-picker
                  v-model="dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  @change="filterObservationsByDate"
                  clearable
                />
              </div>
              
              <el-table :data="currentObservations" stripe style="width: 100%" v-loading="observationsLoading">
                <el-table-column prop="observationTime" label="体检时间" width="180">
                  <template #default="{ row }">
                    {{ formatDate(row.observationTime) }}
                  </template>
                </el-table-column>
                <el-table-column prop="bodyTemperature" label="体温(°C)" width="100" />
                <el-table-column prop="systolicBp" label="收缩压(mmHg)" width="120" />
                <el-table-column prop="heartRate" label="心率(次/分)" width="120" />
                <el-table-column prop="sleepHours" label="睡眠时长(小时)" width="130" />
                <el-table-column prop="cough" label="咳嗽" width="80">
                  <template #default="{ row }">
                    <el-tag :type="row.cough ? 'warning' : 'success'">
                      {{ row.cough ? '是' : '否' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="observationLocation" label="观察地点" />
                <el-table-column label="操作" width="120">
                  <template #default="{ row }">
                    <el-button type="primary" size="small" @click="viewObservationDetail(row)">
                      详情
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination" v-if="currentObservations.length > 0">
                <el-pagination
                  v-model:current-page="currentPage"
                  v-model:page-size="pageSize"
                  :page-sizes="[10, 20, 50, 100]"
                  :total="totalObservations"
                  layout="total, sizes, prev, pager, next, jumper"
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                />
              </div>
            </div>
          </el-tab-pane>

          <!-- 既往病史 -->
          <el-tab-pane label="既往病史" name="chronic">
            <div class="chronic-diseases">
              <el-empty v-if="chronicDiseases.length === 0" description="暂无既往病史记录" />
              <div v-else>
                <el-tag
                  v-for="disease in chronicDiseases"
                  :key="disease.id"
                  type="warning"
                  size="large"
                  class="disease-tag"
                >
                  {{ disease.diseaseName }}
                </el-tag>
              </div>
            </div>
          </el-tab-pane>

          <!-- 健康预警 -->
          <el-tab-pane label="健康预警" name="alerts">
            <div class="health-alerts">
              <el-table :data="healthAlerts" stripe style="width: 100%" v-loading="alertsLoading">
                <el-table-column prop="alertTitle" label="预警标题" />
                <el-table-column prop="alertLevel" label="预警级别" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getAlertLevelType(row.alertLevel)">
                      {{ getAlertLevelText(row.alertLevel) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="alertDescription" label="预警描述" />
                <el-table-column prop="triggerValue" label="触发值" width="100" />
                <el-table-column prop="normalRange" label="正常范围" width="120" />
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="{ row }">
                    <el-tag :type="row.status === 'RESOLVED' ? 'success' : 'danger'">
                      {{ row.status === 'RESOLVED' ? '已解决' : '待处理' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="createdAt" label="创建时间" width="180">
                  <template #default="{ row }">
                    {{ formatDate(row.createdAt) }}
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>

    <!-- 空状态 -->
    <div class="empty-state" v-else>
      <el-empty description="请选择一位老人查看其电子病历信息" />
    </div>

    <!-- 体检详情对话框 -->
    <el-dialog v-model="observationDetailVisible" title="体检详情" width="600px">
      <div v-if="selectedObservation">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="体检时间">{{ formatDate(selectedObservation.observationTime) }}</el-descriptions-item>
          <el-descriptions-item label="观察地点">{{ selectedObservation.observationLocation }}</el-descriptions-item>
          <el-descriptions-item label="体温">{{ selectedObservation.bodyTemperature }}°C</el-descriptions-item>
          <el-descriptions-item label="收缩压">{{ selectedObservation.systolicBp }} mmHg</el-descriptions-item>
          <el-descriptions-item label="心率">{{ selectedObservation.heartRate }} 次/分</el-descriptions-item>
          <el-descriptions-item label="睡眠时长">{{ selectedObservation.sleepHours }} 小时</el-descriptions-item>
          <el-descriptions-item label="咳嗽症状">
            <el-tag :type="selectedObservation.cough ? 'warning' : 'success'">
              {{ selectedObservation.cough ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, User } from '@element-plus/icons-vue'
import { getAllElderlyProfiles, getChronicDiseasesByElderlyId } from '@/api/elderlyProfile'
import { getObservationsByElderlyId, getObservationsByTimeRange } from '@/api/elderlyObservations'
import { getAlertsByElderlyId } from '@/api/healthAlert'

// 响应式数据
const elderlyList = ref([])
const selectedElderly = ref(null)
const activeTab = ref('basic')
const searchForm = reactive({
  name: '',
  ageRange: ''
})

// 体检记录相关
const allObservations = ref([])
const currentObservations = ref([])
const observationsLoading = ref(false)
const dateRange = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalObservations = computed(() => currentObservations.value.length)

// 既往病史
const chronicDiseases = ref([])

// 健康预警
const healthAlerts = ref([])
const alertsLoading = ref(false)

// 对话框
const observationDetailVisible = ref(false)
const selectedObservation = ref(null)

// 计算属性 - 过滤后的老人列表
const filteredElderlyList = computed(() => {
  let filtered = elderlyList.value
  
  if (searchForm.name) {
    filtered = filtered.filter(elderly => 
      elderly.name.toLowerCase().includes(searchForm.name.toLowerCase())
    )
  }
  
  if (searchForm.ageRange) {
    filtered = filtered.filter(elderly => {
      const age = elderly.age
      switch (searchForm.ageRange) {
        case '60-70':
          return age >= 60 && age < 70
        case '70-80':
          return age >= 70 && age < 80
        case '80-90':
          return age >= 80 && age < 90
        case '90+':
          return age >= 90
        default:
          return true
      }
    })
  }
  
  return filtered
})

// 生命周期
onMounted(() => {
  loadElderlyList()
})

// 方法
const loadElderlyList = async () => {
  try {
    const response = await getAllElderlyProfiles()
    elderlyList.value = response.data || []
  } catch (error) {
    console.error('加载老人列表失败:', error)
    ElMessage.error('加载老人列表失败')
  }
}

const selectElderly = async (elderly) => {
  selectedElderly.value = elderly
  activeTab.value = 'basic'
  await loadMedicalData(elderly.id)
}

const loadMedicalData = async (elderlyId) => {
  await Promise.all([
    loadObservations(elderlyId),
    loadChronicDiseases(elderlyId),
    loadHealthAlerts(elderlyId)
  ])
}

const loadObservations = async (elderlyId) => {
  observationsLoading.value = true
  try {
    const response = await getObservationsByElderlyId(elderlyId)
    allObservations.value = response.data || []
    currentObservations.value = allObservations.value
  } catch (error) {
    console.error('加载体检记录失败:', error)
    ElMessage.error('加载体检记录失败')
  } finally {
    observationsLoading.value = false
  }
}

const loadChronicDiseases = async (elderlyId) => {
  try {
    const response = await getChronicDiseasesByElderlyId(elderlyId)
    chronicDiseases.value = response.data || []
  } catch (error) {
    console.error('加载既往病史失败:', error)
    ElMessage.error('加载既往病史失败')
  }
}

const loadHealthAlerts = async (elderlyId) => {
  alertsLoading.value = true
  try {
    const response = await getAlertsByElderlyId(elderlyId)
    healthAlerts.value = response.data || []
  } catch (error) {
    console.error('加载健康预警失败:', error)
    ElMessage.error('加载健康预警失败')
  } finally {
    alertsLoading.value = false
  }
}

const handleSearch = () => {
  // 搜索逻辑已在计算属性中实现
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.ageRange = ''
}

const refreshMedicalData = () => {
  if (selectedElderly.value) {
    loadMedicalData(selectedElderly.value.id)
    ElMessage.success('数据已刷新')
  }
}

const filterObservationsByDate = async () => {
  if (!selectedElderly.value) return
  
  if (dateRange.value && dateRange.value.length === 2) {
    const startTime = formatDateForAPI(dateRange.value[0])
    const endTime = formatDateForAPI(dateRange.value[1])
    
    try {
      observationsLoading.value = true
      const response = await getObservationsByTimeRange(selectedElderly.value.id, startTime, endTime)
      currentObservations.value = response.data || []
    } catch (error) {
      console.error('按日期筛选体检记录失败:', error)
      ElMessage.error('按日期筛选失败')
    } finally {
      observationsLoading.value = false
    }
  } else {
    currentObservations.value = allObservations.value
  }
}

const viewObservationDetail = (observation) => {
  selectedObservation.value = observation
  observationDetailVisible.value = true
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

const getLastObservationDate = (elderlyId) => {
  // 这里可以根据需要实现获取最近体检日期的逻辑
  return '暂无记录'
}

const formatDate = (dateString) => {
  if (!dateString) return '-'
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

const formatDateForAPI = (date) => {
  return date.toISOString().split('T')[0]
}

const getAlertLevelType = (level) => {
  const types = {
    'LOW': 'info',
    'MEDIUM': 'warning',
    'HIGH': 'danger',
    'CRITICAL': 'danger'
  }
  return types[level] || 'info'
}

const getAlertLevelText = (level) => {
  const texts = {
    'LOW': '低',
    'MEDIUM': '中',
    'HIGH': '高',
    'CRITICAL': '严重'
  }
  return texts[level] || level
}
</script>

<style scoped>
.medical-record-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h1 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 28px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.search-section {
  margin-bottom: 30px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.elderly-list {
  margin-bottom: 30px;
}

.elderly-card {
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 20px;
}

.elderly-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.elderly-card.active {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.elderly-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.elderly-details h3 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.elderly-details p {
  margin: 4px 0;
  color: #606266;
  font-size: 14px;
}

.elderly-details .label {
  color: #909399;
  font-weight: 500;
}

.medical-details {
  margin-top: 30px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.basic-info {
  padding: 20px 0;
}

.observations-section {
  padding: 20px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.chronic-diseases {
  padding: 20px 0;
}

.disease-tag {
  margin: 5px 10px 5px 0;
}

.health-alerts {
  padding: 20px 0;
}

.empty-state {
  margin-top: 50px;
  text-align: center;
}
</style>