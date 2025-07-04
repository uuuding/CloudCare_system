<template>
  <div class="sms-send-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>短信发送</span>
        </div>
      </template>

      <el-tabs v-model="activeTab" class="sms-tabs">
        <!-- 单条短信发送 -->
        <el-tab-pane label="单条发送" name="single">
          <el-form
            ref="singleFormRef"
            :model="singleForm"
            :rules="singleRules"
            label-width="100px"
            class="sms-form"
          >
            <el-form-item label="手机号码" prop="phone">
              <el-input
                v-model="singleForm.phone"
                placeholder="请输入手机号码"
                maxlength="11"
                show-word-limit
              />
            </el-form-item>
            <el-form-item label="短信内容" prop="content">
              <el-input
                v-model="singleForm.content"
                type="textarea"
                :rows="4"
                placeholder="请输入短信内容"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                :loading="singleLoading"
                @click="sendSingleSms"
              >
                发送短信
              </el-button>
              <el-button @click="resetSingleForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 批量短信发送 -->
        <el-tab-pane label="批量发送" name="batch">
          <el-form
            ref="batchFormRef"
            :model="batchForm"
            :rules="batchRules"
            label-width="100px"
            class="sms-form"
          >
            <el-form-item label="手机号码" prop="phones">
              <el-input
                v-model="batchForm.phones"
                type="textarea"
                :rows="3"
                placeholder="请输入手机号码，多个号码用逗号分隔，如：13800138000,13800138001"
              />
              <div class="form-tip">
                支持批量输入，用逗号分隔，最多支持100个号码
              </div>
            </el-form-item>
            <el-form-item label="短信内容" prop="content">
              <el-input
                v-model="batchForm.content"
                type="textarea"
                :rows="4"
                placeholder="请输入短信内容"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                :loading="batchLoading"
                @click="sendBatchSms"
              >
                批量发送
              </el-button>
              <el-button @click="resetBatchForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 模板短信发送 -->
        <el-tab-pane label="模板发送" name="template">
          <el-form
            ref="templateFormRef"
            :model="templateForm"
            :rules="templateRules"
            label-width="100px"
            class="sms-form"
          >
            <el-form-item label="短信模板" prop="template">
              <el-select
                v-model="templateForm.template"
                placeholder="请选择短信模板"
                style="width: 100%"
                @change="onTemplateChange"
              >
                <el-option
                  v-for="template in templates"
                  :key="template.code"
                  :label="template.name"
                  :value="template.code"
                >
                  <div>
                    <div>{{ template.name }}</div>
                    <div class="template-content">{{ template.content }}</div>
                  </div>
                </el-option>
              </el-select>
            </el-form-item>
            
            <el-form-item label="手机号码" prop="phone">
              <el-input
                v-model="templateForm.phone"
                placeholder="请输入手机号码，批量发送用逗号分隔"
              />
            </el-form-item>

            <!-- 动态参数输入 -->
            <div v-if="currentTemplate && currentTemplate.params">
              <el-form-item
                v-for="param in currentTemplate.params"
                :key="param.key"
                :label="param.label"
                :prop="`params.${param.key}`"
              >
                <el-input
                  v-model="templateForm.params[param.key]"
                  :placeholder="param.placeholder"
                />
              </el-form-item>
            </div>

            <!-- 预览内容 -->
            <el-form-item label="预览内容" v-if="previewContent">
              <el-input
                :model-value="previewContent"
                type="textarea"
                :rows="3"
                readonly
                class="preview-content"
              />
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                :loading="templateLoading"
                @click="sendTemplateSms"
              >
                发送模板短信
              </el-button>
              <el-button @click="resetTemplateForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { smsApi } from '@/api/sms'

// 响应式数据
const activeTab = ref('single')
const singleLoading = ref(false)
const batchLoading = ref(false)
const templateLoading = ref(false)

// 表单引用
const singleFormRef = ref()
const batchFormRef = ref()
const templateFormRef = ref()

// 单条发送表单
const singleForm = reactive({
  phone: '',
  content: ''
})

// 批量发送表单
const batchForm = reactive({
  phones: '',
  content: ''
})

// 模板发送表单
const templateForm = reactive({
  template: '',
  phone: '',
  params: {}
})

// 短信模板列表
const templates = ref([
  {
    code: 'REGISTER_WELCOME',
    name: '注册欢迎',
    content: '【云护CloudCare平台】感谢您的注册，欢迎来到云护CloudCare智慧系统。',
    params: []
  },
  {
    code: 'LOGIN_SECURITY',
    name: '登录安全提醒',
    content: '【云护CloudCare平台】安全提醒：您的账户{username}在{loginTime}有新的登录记录，登录地点：{location}。如非本人操作请及时修改密码。',
    params: [
      { key: 'username', label: '用户名', placeholder: '请输入用户名' },
      { key: 'loginTime', label: '登录时间', placeholder: '请输入登录时间' },
      { key: 'location', label: '登录地点', placeholder: '请输入登录地点' }
    ]
  },
  {
    code: 'MESSAGE_NOTIFICATION',
    name: '消息提醒',
    content: '【云护CloudCare平台】消息提醒：您有来自{senderName}的新消息，内容：{messageContent}。请及时回复。',
    params: [
      { key: 'senderName', label: '发送者姓名', placeholder: '请输入发送者姓名' },
      { key: 'messageContent', label: '消息内容', placeholder: '请输入消息内容' }
    ]
  },
  {
    code: 'SERVICE_NOTIFICATION',
    name: '服务通知',
    content: '【云护CloudCare平台】服务通知：{serviceName}已安排在{scheduledTime}为{elderlyName}提供服务，服务人员：{serviceProvider}，联系电话：{contactPhone}。',
    params: [
      { key: 'serviceName', label: '服务名称', placeholder: '请输入服务名称' },
      { key: 'scheduledTime', label: '安排时间', placeholder: '请输入安排时间' },
      { key: 'elderlyName', label: '老人姓名', placeholder: '请输入老人姓名' },
      { key: 'serviceProvider', label: '服务人员', placeholder: '请输入服务人员' },
      { key: 'contactPhone', label: '联系电话', placeholder: '请输入联系电话' }
    ]
  },
  {
    code: 'APPOINTMENT_REMINDER',
    name: '就诊提醒',
    content: '【云护CloudCare平台】就诊提醒：{elderlyName}明天{appointmentTime}在{hospitalName}{department}有预约，请提前30分钟到达。如需改期请及时联系。',
    params: [
      { key: 'elderlyName', label: '老人姓名', placeholder: '请输入老人姓名' },
      { key: 'appointmentTime', label: '预约时间', placeholder: '请输入预约时间' },
      { key: 'hospitalName', label: '医院名称', placeholder: '请输入医院名称' },
      { key: 'department', label: '科室', placeholder: '请输入科室' }
    ]
  },
  {
    code: 'DEVICE_ALERT',
    name: '设备提醒',
    content: '【云护CloudCare平台】设备提醒：老人{elderlyName}的{deviceName}({deviceCode})出现故障，请及时检查或联系维修。设备状态：{deviceStatus}。',
    params: [
      { key: 'elderlyName', label: '老人姓名', placeholder: '请输入老人姓名' },
      { key: 'deviceName', label: '设备名称', placeholder: '请输入设备名称' },
      { key: 'deviceCode', label: '设备编码', placeholder: '请输入设备编码' },
      { key: 'deviceStatus', label: '设备状态', placeholder: '请输入设备状态' }
    ]
  },
  {
    code: 'EMERGENCY_ALERT',
    name: '紧急提醒',
    content: '【云护CloudCare平台】紧急提醒：您关注的老人{elderlyName}出现{alertType}异常，{alertDescription}。请及时关注并联系医护人员。详情请登录系统查看。',
    params: [
      { key: 'elderlyName', label: '老人姓名', placeholder: '请输入老人姓名' },
      { key: 'alertType', label: '预警类型', placeholder: '请输入预警类型' },
      { key: 'alertDescription', label: '预警描述', placeholder: '请输入预警描述' }
    ]
  },
   {
     code: 'FENCE_LEAVE_ALERT',
     name: '围栏离开提醒',
     content: '【云护CloudCare】老人{elder_name}已离开电子围栏"{fence_name}"，当前位置：纬度{latitude}，经度{longitude}，事件时间：{event_time}，请及时关注。',
     params: [
       { key: 'elder_name', label: '老人姓名', placeholder: '请输入老人姓名' },
       { key: 'fence_name', label: '围栏名称', placeholder: '请输入围栏名称' },
       { key: 'latitude', label: '纬度', placeholder: '请输入纬度' },
       { key: 'longitude', label: '经度', placeholder: '请输入经度' },
       { key: 'event_time', label: '事件时间', placeholder: '请输入事件时间' }
     ]
   }
])

// 当前选中的模板
const currentTemplate = computed(() => {
  return templates.value.find(t => t.code === templateForm.template)
})

// 预览内容
const previewContent = computed(() => {
  if (!currentTemplate.value) return ''
  
  let content = currentTemplate.value.content
  if (currentTemplate.value.params) {
    currentTemplate.value.params.forEach(param => {
      const value = templateForm.params[param.key] || `{${param.key}}`
      content = content.replace(new RegExp(`\\{${param.key}\\}`, 'g'), value)
    })
  }
  return content
})

// 表单验证规则
const phoneValidator = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入手机号码'))
  } else if (!/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('请输入正确的手机号码'))
  } else {
    callback()
  }
}

const phonesValidator = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入手机号码'))
  } else {
    const phones = value.split(',')
    if (phones.length > 100) {
      callback(new Error('最多支持100个手机号码'))
    }
    for (let phone of phones) {
      phone = phone.trim()
      if (!/^1[3-9]\d{9}$/.test(phone)) {
        callback(new Error(`手机号码 ${phone} 格式不正确`))
        return
      }
    }
    callback()
  }
}

const singleRules = {
  phone: [{ validator: phoneValidator, trigger: 'blur' }],
  content: [
    { required: true, message: '请输入短信内容', trigger: 'blur' },
    { min: 1, max: 500, message: '短信内容长度在 1 到 500 个字符', trigger: 'blur' }
  ]
}

const batchRules = {
  phones: [{ validator: phonesValidator, trigger: 'blur' }],
  content: [
    { required: true, message: '请输入短信内容', trigger: 'blur' },
    { min: 1, max: 500, message: '短信内容长度在 1 到 500 个字符', trigger: 'blur' }
  ]
}

const templateRules = {
  template: [{ required: true, message: '请选择短信模板', trigger: 'change' }],
  phone: [{ validator: phonesValidator, trigger: 'blur' }]
}

// 方法
const sendSingleSms = async () => {
  try {
    await singleFormRef.value.validate()
    singleLoading.value = true
    
    const response = await smsApi.sendSms({
      phone: singleForm.phone,
      content: singleForm.content
    })
    
    if (response.success) {
      ElMessage.success('短信发送成功')
      resetSingleForm()
    } else {
      ElMessage.error(response.message || '短信发送失败')
    }
  } catch (error) {
    console.error('发送短信失败:', error)
    ElMessage.error('短信发送失败')
  } finally {
    singleLoading.value = false
  }
}

const sendBatchSms = async () => {
  try {
    await batchFormRef.value.validate()
    batchLoading.value = true
    
    const response = await smsApi.sendBatchSms({
      phones: batchForm.phones,
      content: batchForm.content
    })
    
    if (response.success) {
      ElMessage.success('批量短信发送成功')
      resetBatchForm()
    } else {
      ElMessage.error(response.message || '批量短信发送失败')
    }
  } catch (error) {
    console.error('发送批量短信失败:', error)
    ElMessage.error('批量短信发送失败')
  } finally {
    batchLoading.value = false
  }
}

const sendTemplateSms = async () => {
  try {
    await templateFormRef.value.validate()
    templateLoading.value = true
    
    const isMultiple = templateForm.phone.includes(',')
    const apiMethod = isMultiple ? smsApi.sendBatchTemplateSms : smsApi.sendTemplateSms
    
    const response = await apiMethod({
      phone: templateForm.phone,
      phones: templateForm.phone,
      templateCode: templateForm.template,
      params: templateForm.params
    })
    
    if (response.success) {
      ElMessage.success('模板短信发送成功')
      resetTemplateForm()
    } else {
      ElMessage.error(response.message || '模板短信发送失败')
    }
  } catch (error) {
    console.error('发送模板短信失败:', error)
    ElMessage.error('模板短信发送失败')
  } finally {
    templateLoading.value = false
  }
}

const onTemplateChange = () => {
  // 重置参数
  templateForm.params = {}
  if (currentTemplate.value && currentTemplate.value.params) {
    currentTemplate.value.params.forEach(param => {
      templateForm.params[param.key] = ''
    })
  }
}

const resetSingleForm = () => {
  singleFormRef.value?.resetFields()
  Object.assign(singleForm, {
    phone: '',
    content: ''
  })
}

const resetBatchForm = () => {
  batchFormRef.value?.resetFields()
  Object.assign(batchForm, {
    phones: '',
    content: ''
  })
}

const resetTemplateForm = () => {
  templateFormRef.value?.resetFields()
  Object.assign(templateForm, {
    template: '',
    phone: '',
    params: {}
  })
}

onMounted(() => {
  // 组件挂载时可以加载模板列表
  // loadTemplates()
})
</script>

<style scoped>
.sms-send-container {
  padding: 20px;
}

.box-card {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  font-size: 16px;
}

.sms-form {
  max-width: 600px;
  margin: 20px 0;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.template-content {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 300px;
}

.preview-content {
  background-color: #f5f7fa;
}

.sms-tabs {
  margin-top: 20px;
}

:deep(.el-tabs__item) {
  font-size: 14px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}
</style>