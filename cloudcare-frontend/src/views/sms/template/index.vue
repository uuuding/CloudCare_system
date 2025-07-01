<template>
  <div class="sms-template-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>短信模板管理</span>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="模板名称">
            <el-input
              v-model="searchForm.name"
              placeholder="请输入模板名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="模板类型">
            <el-select
              v-model="searchForm.type"
              placeholder="请选择模板类型"
              clearable
              style="width: 150px"
            >
              <el-option label="全部" value="" />
              <el-option label="通知类" value="notification" />
              <el-option label="提醒类" value="reminder" />
              <el-option label="安全类" value="security" />
              <el-option label="紧急类" value="emergency" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="resetSearch">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 模板列表 -->
      <div class="template-list">
        <el-row :gutter="20">
          <el-col
            v-for="template in filteredTemplates"
            :key="template.code"
            :xs="24"
            :sm="12"
            :md="8"
            :lg="6"
            class="template-col"
          >
            <el-card class="template-card" shadow="hover">
              <template #header>
                <div class="template-header">
                  <span class="template-name">{{ template.name }}</span>
                  <el-tag :type="getTemplateTypeColor(template.type)" size="small">
                    {{ getTemplateTypeName(template.type) }}
                  </el-tag>
                </div>
              </template>
              
              <div class="template-content">
                <div class="template-code">
                  <span class="label">模板编码：</span>
                  <span class="value">{{ template.code }}</span>
                </div>
                
                <div class="template-text">
                  <span class="label">模板内容：</span>
                  <div class="content-text">{{ template.content }}</div>
                </div>
                
                <div v-if="template.params && template.params.length > 0" class="template-params">
                  <span class="label">参数列表：</span>
                  <div class="params-list">
                    <el-tag
                      v-for="param in template.params"
                      :key="param.key"
                      size="small"
                      class="param-tag"
                    >
                      {{ param.label }}
                    </el-tag>
                  </div>
                </div>
                
                <div class="template-usage">
                  <span class="label">使用场景：</span>
                  <span class="value">{{ template.description }}</span>
                </div>
              </div>
              
              <template #footer>
                <div class="template-actions">
                  <el-button
                    type="primary"
                    size="small"
                    @click="previewTemplate(template)"
                  >
                    <el-icon><View /></el-icon>
                    预览
                  </el-button>
                  <el-button
                    type="success"
                    size="small"
                    @click="useTemplate(template)"
                  >
                    <el-icon><Promotion /></el-icon>
                    使用
                  </el-button>
                </div>
              </template>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 模板预览对话框 -->
    <el-dialog
      v-model="previewDialogVisible"
      title="模板预览"
      width="600px"
      :before-close="handlePreviewClose"
    >
      <div v-if="currentPreviewTemplate" class="preview-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="模板名称">
            {{ currentPreviewTemplate.name }}
          </el-descriptions-item>
          <el-descriptions-item label="模板编码">
            {{ currentPreviewTemplate.code }}
          </el-descriptions-item>
          <el-descriptions-item label="模板类型">
            <el-tag :type="getTemplateTypeColor(currentPreviewTemplate.type)">
              {{ getTemplateTypeName(currentPreviewTemplate.type) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="使用场景">
            {{ currentPreviewTemplate.description }}
          </el-descriptions-item>
          <el-descriptions-item label="模板内容">
            <div class="template-content-preview">{{ currentPreviewTemplate.content }}</div>
          </el-descriptions-item>
          <el-descriptions-item
            v-if="currentPreviewTemplate.params && currentPreviewTemplate.params.length > 0"
            label="参数说明"
          >
            <el-table :data="currentPreviewTemplate.params" size="small">
              <el-table-column prop="key" label="参数名" width="120" />
              <el-table-column prop="label" label="参数说明" width="120" />
              <el-table-column prop="placeholder" label="示例" />
            </el-table>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="previewDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="useCurrentTemplate">
            使用此模板
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, Refresh, View, Promotion } from '@element-plus/icons-vue'

const router = useRouter()

// 响应式数据
const searchForm = reactive({
  name: '',
  type: ''
})

const previewDialogVisible = ref(false)
const currentPreviewTemplate = ref(null)

// 短信模板数据
const templates = ref([
  {
    code: 'REGISTER_WELCOME',
    name: '注册欢迎',
    type: 'notification',
    description: '用户注册成功后发送的欢迎短信',
    content: '【云护CloudCare平台】感谢您的注册，欢迎来到云护CloudCare智慧系统。',
    params: []
  },
  {
    code: 'LOGIN_SECURITY',
    name: '登录安全提醒',
    type: 'security',
    description: '用户登录时发送的安全提醒短信',
    content: '【云护CloudCare平台】安全提醒：您的账户{username}在{loginTime}有新的登录记录，登录地点：{location}。如非本人操作请及时修改密码。',
    params: [
      { key: 'username', label: '用户名', placeholder: '张三' },
      { key: 'loginTime', label: '登录时间', placeholder: '2024-01-15 10:30' },
      { key: 'location', label: '登录地点', placeholder: '北京市朝阳区' }
    ]
  },
  {
    code: 'MESSAGE_NOTIFICATION',
    name: '消息提醒',
    type: 'notification',
    description: '收到新消息时发送的提醒短信',
    content: '【云护CloudCare平台】消息提醒：您有来自{senderName}的新消息，内容：{messageContent}。请及时回复。',
    params: [
      { key: 'senderName', label: '发送者姓名', placeholder: '李医生' },
      { key: 'messageContent', label: '消息内容', placeholder: '请按时服药' }
    ]
  },
  {
    code: 'SERVICE_NOTIFICATION',
    name: '服务通知',
    type: 'notification',
    description: '服务安排时发送的通知短信',
    content: '【云护CloudCare平台】服务通知：{serviceName}已安排在{scheduledTime}为{elderlyName}提供服务，服务人员：{serviceProvider}，联系电话：{contactPhone}。',
    params: [
      { key: 'serviceName', label: '服务名称', placeholder: '健康检查' },
      { key: 'scheduledTime', label: '安排时间', placeholder: '2024-01-16 09:00' },
      { key: 'elderlyName', label: '老人姓名', placeholder: '王奶奶' },
      { key: 'serviceProvider', label: '服务人员', placeholder: '张护士' },
      { key: 'contactPhone', label: '联系电话', placeholder: '13800138000' }
    ]
  },
  {
    code: 'APPOINTMENT_REMINDER',
    name: '就诊提醒',
    type: 'reminder',
    description: '医疗预约前发送的提醒短信',
    content: '【云护CloudCare平台】就诊提醒：{elderlyName}明天{appointmentTime}在{hospitalName}{department}有预约，请提前30分钟到达。如需改期请及时联系。',
    params: [
      { key: 'elderlyName', label: '老人姓名', placeholder: '王奶奶' },
      { key: 'appointmentTime', label: '预约时间', placeholder: '09:00' },
      { key: 'hospitalName', label: '医院名称', placeholder: '市人民医院' },
      { key: 'department', label: '科室', placeholder: '心内科' }
    ]
  },
  {
    code: 'DEVICE_ALERT',
    name: '设备提醒',
    type: 'reminder',
    description: '设备异常时发送的提醒短信',
    content: '【云护CloudCare平台】设备提醒：老人{elderlyName}的{deviceName}({deviceCode})出现故障，请及时检查或联系维修。设备状态：{deviceStatus}。',
    params: [
      { key: 'elderlyName', label: '老人姓名', placeholder: '王奶奶' },
      { key: 'deviceName', label: '设备名称', placeholder: '血压计' },
      { key: 'deviceCode', label: '设备编码', placeholder: 'BP001' },
      { key: 'deviceStatus', label: '设备状态', placeholder: '离线' }
    ]
  },
  {
    code: 'EMERGENCY_ALERT',
    name: '紧急提醒',
    type: 'emergency',
    description: '紧急情况时发送的警报短信',
    content: '【云护CloudCare平台】紧急提醒：您关注的老人{elderlyName}出现{alertType}异常，{alertDescription}。请及时关注并联系医护人员。详情请登录系统查看。',
    params: [
      { key: 'elderlyName', label: '老人姓名', placeholder: '王奶奶' },
      { key: 'alertType', label: '预警类型', placeholder: '心率异常' },
      { key: 'alertDescription', label: '预警描述', placeholder: '心率超过正常范围' }
    ]
  }
])

// 计算属性
const filteredTemplates = computed(() => {
  return templates.value.filter(template => {
    const nameMatch = !searchForm.name || template.name.includes(searchForm.name)
    const typeMatch = !searchForm.type || template.type === searchForm.type
    return nameMatch && typeMatch
  })
})

// 方法
const getTemplateTypeColor = (type) => {
  const colorMap = {
    notification: 'primary',
    reminder: 'warning',
    security: 'danger',
    emergency: 'danger'
  }
  return colorMap[type] || 'info'
}

const getTemplateTypeName = (type) => {
  const nameMap = {
    notification: '通知类',
    reminder: '提醒类',
    security: '安全类',
    emergency: '紧急类'
  }
  return nameMap[type] || '其他'
}

const handleSearch = () => {
  // 搜索逻辑已通过计算属性实现
  ElMessage.success('搜索完成')
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.type = ''
  ElMessage.success('搜索条件已重置')
}

const previewTemplate = (template) => {
  currentPreviewTemplate.value = template
  previewDialogVisible.value = true
}

const useTemplate = (template) => {
  // 跳转到短信发送页面，并传递模板信息
  router.push({
    path: '/sms/send',
    query: {
      template: template.code,
      tab: 'template'
    }
  })
}

const useCurrentTemplate = () => {
  if (currentPreviewTemplate.value) {
    useTemplate(currentPreviewTemplate.value)
  }
  previewDialogVisible.value = false
}

const handlePreviewClose = () => {
  previewDialogVisible.value = false
  currentPreviewTemplate.value = null
}

onMounted(() => {
  // 组件挂载时可以加载模板数据
  // loadTemplates()
})
</script>

<style scoped>
.sms-template-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  font-size: 16px;
}

.search-area {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.search-form {
  margin: 0;
}

.template-list {
  min-height: 400px;
}

.template-col {
  margin-bottom: 20px;
}

.template-card {
  height: 100%;
  transition: transform 0.2s;
}

.template-card:hover {
  transform: translateY(-2px);
}

.template-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.template-name {
  font-weight: bold;
  font-size: 14px;
}

.template-content {
  padding: 10px 0;
}

.template-code,
.template-usage {
  margin-bottom: 10px;
  font-size: 12px;
}

.template-text {
  margin-bottom: 15px;
}

.template-params {
  margin-bottom: 10px;
}

.label {
  color: #606266;
  font-weight: 500;
}

.value {
  color: #303133;
}

.content-text {
  margin-top: 5px;
  padding: 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
  font-size: 12px;
  line-height: 1.5;
  color: #606266;
}

.params-list {
  margin-top: 5px;
}

.param-tag {
  margin-right: 5px;
  margin-bottom: 5px;
}

.template-actions {
  display: flex;
  justify-content: space-between;
}

.preview-content {
  max-height: 500px;
  overflow-y: auto;
}

.template-content-preview {
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  line-height: 1.6;
  white-space: pre-wrap;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-card__header) {
  padding: 15px 20px;
}

:deep(.el-card__body) {
  padding: 15px 20px;
}

:deep(.el-card__footer) {
  padding: 10px 20px;
  border-top: 1px solid #ebeef5;
}

:deep(.el-descriptions__label) {
  font-weight: 500;
}
</style>