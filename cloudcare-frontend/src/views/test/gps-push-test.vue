<template>
  <div class="gps-push-test">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>GPS数据推送测试</span>
        </div>
      </template>
      
      <!-- 测试配置 -->
      <el-form :model="testConfig" label-width="120px" class="test-config">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="推送地址:">
              <el-input v-model="testConfig.pushUrl" placeholder="请输入推送地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备序列号:">
              <el-input v-model="testConfig.serialNumber" placeholder="请输入设备序列号" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 推送类型选择 -->
      <el-tabs v-model="activeTab" class="push-tabs">
        <!-- 报警推送测试 -->
        <el-tab-pane label="报警推送" name="alarm">
          <el-form :model="alarmData" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="设备IMEI:">
                  <el-input v-model="alarmData.Macid" placeholder="设备IMEI" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="设备名称:">
                  <el-input v-model="alarmData.FullName" placeholder="设备名称" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="报警类型:">
                  <el-select v-model="alarmData.Classify" placeholder="选择报警类型">
                    <el-option label="SOS报警" :value="1" />
                    <el-option label="低电量报警" :value="2" />
                    <el-option label="围栏报警" :value="3" />
                    <el-option label="震动报警" :value="4" />
                    <el-option label="移动报警" :value="5" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="纬度:">
                  <el-input-number v-model="alarmData.Lat" :precision="6" :step="0.000001" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="经度:">
                  <el-input-number v-model="alarmData.Lon" :precision="6" :step="0.000001" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="速度:">
                  <el-input-number v-model="alarmData.Speed" :min="0" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="报警描述:">
                  <el-input v-model="alarmData.Describe" placeholder="报警描述" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="备注:">
                  <el-input v-model="alarmData.Notea" placeholder="备注信息" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item>
              <el-button type="primary" @click="sendAlarmData">发送报警数据</el-button>
              <el-button @click="generateRandomAlarm">生成随机数据</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 位置状态推送测试 -->
        <el-tab-pane label="位置状态推送" name="status">
          <el-form :model="statusData" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="设备IMEI:">
                  <el-input v-model="statusData.Macid" placeholder="设备IMEI" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="纬度:">
                  <el-input-number v-model="statusData.Lat" :precision="6" :step="0.000001" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="经度:">
                  <el-input-number v-model="statusData.Lon" :precision="6" :step="0.000001" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="速度:">
                  <el-input-number v-model="statusData.Speed" :min="0" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="方向:">
                  <el-input-number v-model="statusData.Dir" :min="0" :max="360" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="状态信息:">
                  <el-input v-model="statusData.Stats" placeholder="状态信息" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item>
              <el-button type="primary" @click="sendStatusData">发送位置数据</el-button>
              <el-button @click="generateRandomStatus">生成随机数据</el-button>
              <el-button @click="startAutoSend" :disabled="autoSending">开始自动发送</el-button>
              <el-button @click="stopAutoSend" :disabled="!autoSending">停止自动发送</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 推送测试 -->
        <el-tab-pane label="推送测试" name="pushtest">
          <div class="push-test">
            <p>测试推送连接是否正常</p>
            <el-button type="primary" @click="sendPushTest">发送推送测试</el-button>
          </div>
        </el-tab-pane>
      </el-tabs>

      <!-- 响应日志 -->
      <el-divider>响应日志</el-divider>
      <div class="response-log">
        <el-button @click="clearLog" size="small" style="margin-bottom: 10px">清空日志</el-button>
        <el-scrollbar height="300px">
          <div class="log-content">
            <div v-for="(log, index) in responseLogs" :key="index" class="log-item" :class="log.type">
              <span class="log-time">{{ log.time }}</span>
              <span class="log-message">{{ log.message }}</span>
            </div>
          </div>
        </el-scrollbar>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 测试配置
const testConfig = reactive({
  pushUrl: 'http://localhost:8080/api/gps/receive',
  serialNumber: 'TEST123456789'
})

// 当前选中的标签页
const activeTab = ref('alarm')

// 报警数据
const alarmData = reactive({
  Id: 'ALARM001',
  Macid: '860123456789012',
  FullName: '测试设备001',
  Lat: 39.908823,
  Lon: 116.397470,
  MapLat: 39.908823,
  MapLon: 116.397470,
  Speed: 0,
  Dir: 0,
  Classify: 1,
  Describe: 'SOS紧急求助',
  Notea: '测试报警数据'
})

// 位置状态数据
const statusData = reactive({
  Macid: '860123456789012',
  Lat: 39.908823,
  Lon: 116.397470,
  Speed: 5,
  Dir: 90,
  Stats: '1,1,1,1,1,1'
})

// 响应日志
const responseLogs = ref([])

// 自动发送状态
const autoSending = ref(false)
let autoSendTimer = null

// 添加日志
const addLog = (message, type = 'info') => {
  const now = new Date()
  const time = now.toLocaleTimeString()
  responseLogs.value.unshift({
    time,
    message,
    type
  })
  
  // 限制日志数量
  if (responseLogs.value.length > 100) {
    responseLogs.value = responseLogs.value.slice(0, 100)
  }
}

// 清空日志
const clearLog = () => {
  responseLogs.value = []
}

// 发送HTTP请求
const sendRequest = async (method, data) => {
  try {
    const params = new URLSearchParams()
    params.append('method', method)
    params.append('serialNumber', testConfig.serialNumber)
    if (data) {
      params.append('data', JSON.stringify([data]))
    }
    
    addLog(`发送${method}请求: ${JSON.stringify(data || {})}`, 'info')
    
    const response = await axios.post(testConfig.pushUrl, params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      timeout: 10000
    })
    
    addLog(`响应成功: ${response.data}`, 'success')
    ElMessage.success('发送成功')
    
  } catch (error) {
    const errorMsg = error.response ? error.response.data : error.message
    addLog(`请求失败: ${errorMsg}`, 'error')
    ElMessage.error('发送失败: ' + errorMsg)
  }
}

// 发送报警数据
const sendAlarmData = () => {
  sendRequest('alarm', alarmData)
}

// 发送位置状态数据
const sendStatusData = () => {
  sendRequest('status', statusData)
}

// 发送推送测试
const sendPushTest = () => {
  sendRequest('PushTest')
}

// 生成随机报警数据
const generateRandomAlarm = () => {
  alarmData.Id = 'ALARM' + Date.now()
  alarmData.Lat = 39.9 + Math.random() * 0.1
  alarmData.Lon = 116.4 + Math.random() * 0.1
  alarmData.MapLat = alarmData.Lat
  alarmData.MapLon = alarmData.Lon
  alarmData.Speed = Math.floor(Math.random() * 60)
  alarmData.Dir = Math.floor(Math.random() * 360)
  alarmData.Classify = Math.floor(Math.random() * 5) + 1
}

// 生成随机位置数据
const generateRandomStatus = () => {
  statusData.Lat = 39.9 + Math.random() * 0.1
  statusData.Lon = 116.4 + Math.random() * 0.1
  statusData.Speed = Math.floor(Math.random() * 60)
  statusData.Dir = Math.floor(Math.random() * 360)
}

// 开始自动发送
const startAutoSend = () => {
  autoSending.value = true
  autoSendTimer = setInterval(() => {
    generateRandomStatus()
    sendStatusData()
  }, 5000) // 每5秒发送一次
  
  ElMessage.success('开始自动发送位置数据')
}

// 停止自动发送
const stopAutoSend = () => {
  autoSending.value = false
  if (autoSendTimer) {
    clearInterval(autoSendTimer)
    autoSendTimer = null
  }
  
  ElMessage.info('停止自动发送')
}

// 组件卸载时清理定时器
import { onUnmounted } from 'vue'
onUnmounted(() => {
  if (autoSendTimer) {
    clearInterval(autoSendTimer)
  }
})
</script>

<style scoped>
.gps-push-test {
  padding: 20px;
}

.box-card {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

.test-config {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.push-tabs {
  margin-bottom: 20px;
}

.push-test {
  text-align: center;
  padding: 40px;
}

.response-log {
  margin-top: 20px;
}

.log-content {
  font-family: 'Courier New', monospace;
  font-size: 12px;
}

.log-item {
  padding: 5px 10px;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
}

.log-item.success {
  background-color: #f0f9ff;
  color: #059669;
}

.log-item.error {
  background-color: #fef2f2;
  color: #dc2626;
}

.log-item.info {
  background-color: #f8fafc;
  color: #374151;
}

.log-time {
  margin-right: 10px;
  color: #6b7280;
  min-width: 80px;
}

.log-message {
  flex: 1;
  word-break: break-all;
}
</style>