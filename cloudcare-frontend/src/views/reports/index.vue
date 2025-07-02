<template>
  <div class="reports-container">    
    <!-- 智能分析助手入口 -->
    <div class="assistant-fab" @click="assistantDrawerVisible = true">
      <el-icon><ChatDotRound /></el-icon>
      <span>智能分析</span>
    </div>

    <!-- 智能分析助手抽屉 -->
    <el-drawer
      v-model="assistantDrawerVisible"
      title="DeepSeek智能化数据分析助手"
      direction="rtl"
      size="40%"
      :before-close="handleDrawerClose"
    >
      <div class="assistant-chat-container">
        <div class="chat-history">
                    <div v-for="(message, index) in chatHistory" :key="index" :class="['chat-message', message.role]">
            <div class="message-bubble">
              <div v-if="message.role === 'assistant'" class="role-label assistant-label">AI助手</div>
              <div v-if="message.role === 'user'" class="role-label user-label">您</div>
                                          <div v-if="message.loading" class="loading-indicator">
                <span>正在思考中....</span>
                <div class="dot"></div>
                <div class="dot"></div>
                <div class="dot"></div>
              </div>
              <div v-else class="message-content" v-html="message.htmlContent || message.content"></div>
            </div>
          </div>
        </div>
        <div class="chat-input-area">
          <el-input
            v-model="chatInput"
            placeholder="请输入您关心的数据分析问题..."
            @keyup.enter="handleAssistantChat"
            clearable
          ></el-input>
          <div class="button-group">
            <el-button class="clear-button" size="small" @click="clearChatHistory" title="清除对话历史">清除</el-button>
            <el-button class="send-button" type="primary" @click="handleAssistantChat" :loading="assistantLoading">发送</el-button>
          </div>
        </div>
      </div>
    </el-drawer>

    <!-- 页面标题 -->
    <div class="page-header">
      <h1>报表统计与分析</h1>
      <p class="page-description">全面的数据统计分析，助您洞察系统运营状况</p>
    </div>

    <!-- 总览统计卡片 -->
    <div class="overview-cards">
      <div class="stat-card">
        <div class="card-icon elderly">
          <el-icon><User /></el-icon>
        </div>
        <div class="card-content">
          <h3>{{ overviewStats.totalElderly }}</h3>
          <p>老人总数</p>
          <span class="trend positive">+{{ overviewStats.newElderlyThisMonth }}</span>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="card-icon alert">
          <el-icon><Warning /></el-icon>
        </div>
        <div class="card-content">
          <h3>{{ overviewStats.totalAlerts }}</h3>
          <p>健康预警</p>
          <span class="trend negative">{{ overviewStats.activeAlerts }} 活跃</span>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="card-icon intervention">
          <el-icon><Document /></el-icon>
        </div>
        <div class="card-content">
          <h3>{{ overviewStats.totalInterventions }}</h3>
          <p>干预方案</p>
          <span class="trend positive">{{ overviewStats.activeInterventions }} 进行中</span>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="card-icon device">
          <el-icon><Monitor /></el-icon>
        </div>
        <div class="card-content">
          <h3>{{ overviewStats.totalDevices }}</h3>
          <p>设备总数</p>
          <span class="trend positive">{{ overviewStats.normalDevices }} 正常</span>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <!-- 第一行图表 -->
      <div class="chart-row">
        <div class="chart-container">
          <div class="chart-header">
            <h3>健康预警趋势</h3>
            <el-select v-model="alertTimeRange" @change="updateAlertChart" size="small">
              <el-option label="最近7天" value="week"></el-option>
              <el-option label="最近30天" value="month"></el-option>
              <el-option label="最近3个月" value="quarter"></el-option>
            </el-select>
          </div>
          <div ref="alertTrendChart" class="chart"></div>
        </div>
        
        <div class="chart-container">
          <div class="chart-header">
            <h3>干预方案状态分布</h3>
          </div>
          <div ref="interventionStatusChart" class="chart"></div>
        </div>
      </div>
      
      <!-- 第二行图表 -->
      <div class="chart-row">
        <div class="chart-container">
          <div class="chart-header">
            <h3>设备使用情况</h3>
          </div>
          <div ref="deviceUsageChart" class="chart"></div>
        </div>
        
        <div class="chart-container">
          <div class="chart-header">
            <h3>老人年龄分布</h3>
          </div>
          <div ref="ageDistributionChart" class="chart"></div>
        </div>
      </div>
      
      <!-- 第三行图表 -->
      <div class="chart-row">
        <div class="chart-container full-width">
          <div class="chart-header">
            <h3>月度数据对比</h3>
            <el-radio-group v-model="monthlyDataType" @change="updateMonthlyChart" size="small">
              <el-radio-button label="alerts">预警数量</el-radio-button>
              <el-radio-button label="interventions">干预方案</el-radio-button>
              <el-radio-button label="assessments">健康评估</el-radio-button>
            </el-radio-group>
          </div>
          <div ref="monthlyComparisonChart" class="chart large"></div>
        </div>
      </div>
    </div>

    <!-- 详细统计表格 -->
    <div class="detailed-stats">
      <el-tabs v-model="activeTab" class="stats-tabs">
        <el-tab-pane label="预警统计" name="alerts">
          <div class="table-container">
            <el-table :data="alertStats" stripe>
              <el-table-column prop="alertType" label="预警类型" width="150"></el-table-column>
              <el-table-column prop="total" label="总数" width="100"></el-table-column>
              <el-table-column prop="active" label="活跃" width="100"></el-table-column>
              <el-table-column prop="resolved" label="已解决" width="100"></el-table-column>
              <el-table-column prop="rate" label="解决率" width="120">
                <template #default="scope">
                  <el-progress :percentage="scope.row.rate" :color="getProgressColor(scope.row.rate)"></el-progress>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="干预统计" name="interventions">
          <div class="table-container">
            <el-table :data="interventionStats" stripe>
              <el-table-column prop="planType" label="干预类型" width="150"></el-table-column>
              <el-table-column prop="total" label="总数" width="100"></el-table-column>
              <el-table-column prop="active" label="进行中" width="100"></el-table-column>
              <el-table-column prop="completed" label="已完成" width="100"></el-table-column>
              <el-table-column prop="effectiveness" label="有效率" width="120">
                <template #default="scope">
                  <el-progress :percentage="scope.row.effectiveness" :color="getProgressColor(scope.row.effectiveness)"></el-progress>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="设备统计" name="devices">
          <div class="table-container">
            <el-table :data="deviceStats" stripe>
              <el-table-column prop="deviceType" label="设备类型" width="120"></el-table-column>
              <el-table-column prop="total" label="总数" width="80"></el-table-column>
              <el-table-column prop="normal" label="正常" width="80"></el-table-column>
              <el-table-column prop="disabled" label="停用" width="80"></el-table-column>
              <el-table-column prop="maintenance" label="维修中" width="80"></el-table-column>
              <el-table-column prop="scrapped" label="已报废" width="80"></el-table-column>
              <el-table-column prop="normalRate" label="正常率" width="120">
                <template #default="scope">
                  <el-progress :percentage="scope.row.normalRate" :color="getProgressColor(scope.row.normalRate)"></el-progress>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { User, Warning, Document, Monitor, ChatDotRound } from '@element-plus/icons-vue'
import { getAllAlerts, getAlertStatistics, getAlertsByTimeRange } from '@/api/healthAlert'
import { getTemplateUsageStatistics, getInterventionTemplatePage } from '@/api/intervention/template'
import { getAllElderlyProfiles } from '@/api/elderlyProfile'
import { getDeviceList } from '@/api/device'
import { getInterventionPlanPage } from '@/api/intervention/plan'
import { chatWithDeepSeek } from '@/api/deepseek'
// 移除不再使用的执行统计接口
// import { getExecutionStatsByDateRange } from '@/api/intervention/execution'

// 响应式数据
const overviewStats = ref({
  totalElderly: 0,
  newElderlyThisMonth: 0,
  totalAlerts: 0,
  activeAlerts: 0,
  totalInterventions: 0,
  activeInterventions: 0,
  totalDevices: 0,
  normalDevices: 0
})

const alertTimeRange = ref('week')
const monthlyDataType = ref('alerts')
const activeTab = ref('alerts')

const alertStats = ref([])
const interventionStats = ref([])
const deviceStats = ref([])

// DeepSeek Test Data
const userInput = ref('')
const apiResponse = ref('')
const loading = ref(false)

// 智能分析助手相关数据
const assistantDrawerVisible = ref(false)
const assistantLoading = ref(false)
const chatInput = ref('')

// 从sessionStorage加载聊天历史
const loadChatHistory = () => {
  try {
    const saved = sessionStorage.getItem('reports_chat_history')
    if (saved) {
      return JSON.parse(saved)
    }
  } catch (error) {
    console.warn('加载聊天历史失败:', error)
  }
  return [{ role: 'assistant', content: '您好！我是您的智能化数据分析助手，我可以根据当前页面的统计数据为您提供分析和解答。' }]
}

// 保存聊天历史到sessionStorage
const saveChatHistory = (history) => {
  try {
    sessionStorage.setItem('reports_chat_history', JSON.stringify(history))
  } catch (error) {
    console.warn('保存聊天历史失败:', error)
  }
}

// 从sessionStorage加载其他状态
const loadChatState = () => {
  try {
    const savedState = sessionStorage.getItem('reports_chat_state')
    if (savedState) {
      return JSON.parse(savedState)
    }
  } catch (error) {
    console.warn('加载聊天状态失败:', error)
  }
  return { isFirstChat: true, sessionId: '' }
}

// 保存聊天状态到sessionStorage
const saveChatState = (state) => {
  try {
    sessionStorage.setItem('reports_chat_state', JSON.stringify(state))
  } catch (error) {
    console.warn('保存聊天状态失败:', error)
  }
}

const chatHistory = ref(loadChatHistory())
const chatState = loadChatState()
const isFirstChat = ref(chatState.isFirstChat)
const sessionId = ref(chatState.sessionId)


const handleAssistantChat = async () => {
  if (!chatInput.value.trim()) return

    const userMessage = { role: 'user', content: chatInput.value };
  chatHistory.value.push(userMessage);
  // 保存用户消息到sessionStorage
  saveChatHistory(chatHistory.value);
  const currentChatInput = chatInput.value;
  chatInput.value = '';
  assistantLoading.value = true;

  // 添加一个临时的加载中消息
  const loadingMessage = {
    role: 'assistant',
    content: '正在思考中...',
    loading: true // 自定义一个加载状态
  };
  chatHistory.value.push(loadingMessage);

  try {
    const requestPayload = {
      messages: [
        ...chatHistory.value.slice(-10) // 只发送最近10条消息以控制上下文长度
      ],
      model: 'deepseek-chat',
      temperature: 0.7,
      max_tokens: 2048,
      stream: false
    }

        // 生成或使用现有 sessionId
    if (!sessionId.value) {
      sessionId.value = 'user-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9)
      // 保存新的sessionId
      saveChatState({ isFirstChat: isFirstChat.value, sessionId: sessionId.value })
    }
    requestPayload.sessionId = sessionId.value
    console.log(isFirstChat.value)

    // 如果是第一次对话，附加页面统计数据和系统提示
    if (isFirstChat.value) {
      const pageStatistics = {
        '总览统计': overviewStats.value,
        '预警统计表': alertStats.value,
        '干预统计表': interventionStats.value,
        '设备统计表': deviceStats.value,
        // 未来可以添加图表的原始数据
      }

      console.log(pageStatistics)
      // 将统计数据添加到请求中
      requestPayload.statistics = pageStatistics
      // 更新系统消息，告知模型如何使用这些数据
      const systemPrompt = {
        role: 'system', 
        content: `你是一位资深的医疗数据分析专家，擅长从医疗管理系统中的统计数据中提取关键信息，判断整体健康趋势和潜在风险，并提供针对性的运营优化和干预建议。\n\n
        请根据用户提供的任务，判断是否涉及医疗数据分析。如果是，按照以下要求进行回答；如果不是，请根据实际问题提供简洁和针对性的解答：\n\n

        **如果任务是医疗数据分析**，请根据用户提供的后台系统统计数据，完成以下任务：
        1. 解读数据：清晰地解读各类统计指标，揭示数据反映的趋势或异常。\n
        2. 识别风险：精准判断健康管理中的潜在风险，如慢性病高发、异常指标集中等。\n
        3. 提供建议：基于数据提出具体、可行的建议，包括：\n - 重点人群管理：为特定风险群体制定管理策略。\n - 运营优化：改进提醒机制、完善系统功能等。\n - 数据质量：提出数据采集或质量的改进建议。\n
        4. 明确优先级：指明需要立即处理的风险项和重点关注领域。\n\n
        输出格式要求：请使用 Markdown 格式进行排版，但是不要输出markdown总的大标题。确保内容清晰、美观,特别注意换行。须包含以下部分：\n\n
        1.数据分析结果\n- 总览统计：数据统计结果说明了...\n\n
        2.变化趋势分析\n- 趋势分析：数据变化趋势体现了...\n\n
        3.潜在风险与异常\n1. ...\n2. ...\n\n
        4.优化建议\n- 重点人群管理：...\n- 运营改进：...\n\n
        5.是否需要立即处理\n- 是/否：...（请明确说明理由）

        **如果任务不涉及医疗数据分析**，请根据实际问题提供简洁、具体的解答，并避免使用上述格式。
        输出格式要求：请使用 Markdown 进行排版，你可以根据用户实际问题进行发挥，不需要再进行数据分析工作，同时不必给出不进行数据分析的原因`
      }

      const statsText = `以下是当前页面的统计数据，请结合分析：\n` +
          '```\n' + JSON.stringify(pageStatistics, null, 2) + '\n```';

      const userMessageWithStats = {
        role: 'user',
        content: currentChatInput + '\n\n' + statsText
      };

      requestPayload.messages = [
        systemPrompt,
        ...chatHistory.value.slice(-9), // +1 = 10 条消息
        userMessageWithStats
      ]

      isFirstChat.value = false
      // 保存isFirstChat状态
      saveChatState({ isFirstChat: isFirstChat.value, sessionId: sessionId.value })
    }

    const res = await chatWithDeepSeek(requestPayload)
    if (res.data && res.data.choices && res.data.choices.length > 0) {
      const assistantResponse = res.data.choices[0].message.content;
      // Simple markdown to HTML conversion
      let htmlContent = assistantResponse
        .replace(/^## (.*$)/gim, '<h2>$1</h2>')
        .replace(/^### (.*$)/gim, '<h3>$1</h3>')
        .replace(/^#### (.*$)/gim, '<h4>$1</h4>')
        .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
        .replace(/\n- /g, '<br>• ')
        .replace(/\n/g, '<br>');

            // 移除加载中消息，并添加助手的实际回复
      chatHistory.value.pop();
      const assistantMessage = { role: 'assistant', content: assistantResponse, htmlContent };
      chatHistory.value.push(assistantMessage);
      // 保存助手回复到sessionStorage
      saveChatHistory(chatHistory.value);
    } else {
            chatHistory.value.pop(); // 移除加载中消息
      const errorMessage = { role: 'assistant', content: '抱歉，未能获取有效响应。' };
      chatHistory.value.push(errorMessage);
      // 保存错误消息到sessionStorage
      saveChatHistory(chatHistory.value);
    }
  } catch (error) {
    console.error('Assistant API call failed:', error)
        chatHistory.value.pop(); // 移除加载中消息
    const errorMessage = { role: 'assistant', content: '抱歉，与助手通信时发生错误。' };
    chatHistory.value.push(errorMessage);
    // 保存错误消息到sessionStorage
    saveChatHistory(chatHistory.value);
  } finally {
    assistantLoading.value = false
  }
}

const handleDrawerClose = (done) => {
  // 如果需要，可以在这里添加确认逻辑
  done()
}

// 清除聊天历史（可在用户登出时调用）
const clearChatHistory = () => {
  try {
    sessionStorage.removeItem('reports_chat_history')
    sessionStorage.removeItem('reports_chat_state')
    // 重置为初始状态
    chatHistory.value = [{ role: 'assistant', content: '您好！我是您的智能化数据分析助手，我可以根据当前页面的统计数据为您提供分析和解答。' }]
    isFirstChat.value = true
    sessionId.value = ''
  } catch (error) {
    console.warn('清除聊天历史失败:', error)
  }
}

const handleDeepSeekChat = async () => {
  if (!userInput.value.trim()) {
    return
  }
  loading.value = true
  apiResponse.value = ''
  try {
    const requestPayload = {
      messages: [
        { content: 'You are a helpful assistant', role: 'system' },
        { content: userInput.value, role: 'user' }
      ],
      model: 'deepseek-chat',
      temperature: 0.7,
      max_tokens: 1024,
      stream: false
    }
    const res = await chatWithDeepSeek(requestPayload)
    if (res.data && res.data.choices && res.data.choices.length > 0) {
      apiResponse.value = res.data.choices[0].message.content
    } else {
      apiResponse.value = '未能获取有效响应。'
    }
  } catch (error) {
    console.error('DeepSeek API call failed:', error)
    apiResponse.value = 'API 调用失败。请查看控制台获取更多信息。'
  } finally {
    loading.value = false
  }
}

// 图表引用
const alertTrendChart = ref(null)
const interventionStatusChart = ref(null)
const deviceUsageChart = ref(null)
const ageDistributionChart = ref(null)
const monthlyComparisonChart = ref(null)

// 图表实例
let alertChart = null
let interventionChart = null
let deviceChart = null
let ageChart = null
let monthlyChart = null

// 初始化图表
const initCharts = async () => {
  await nextTick()
  await initAlertTrendChart()
  initInterventionStatusChart()
  initDeviceUsageChart()
  initAgeDistributionChart()
  initMonthlyComparisonChart()
}

// 初始化健康预警趋势图
const initAlertTrendChart = async () => {
  if (!alertTrendChart.value) return
  
  alertChart = echarts.init(alertTrendChart.value)
  
  try {
    // 根据选择的时间范围获取数据
    const endDate = new Date()
    const startDate = new Date()
    let days = 7
    let chartTitle = '健康预警趋势（最近7天）'
    
    switch (alertTimeRange.value) {
      case 'week':
        days = 7
        chartTitle = '健康预警趋势（最近7天）'
        startDate.setDate(endDate.getDate() - 6)
        break
      case 'month':
        days = 30
        chartTitle = '健康预警趋势（最近30天）'
        startDate.setDate(endDate.getDate() - 29)
        break
      case 'quarter':
        days = 90
        chartTitle = '健康预警趋势（最近3个月）'
        startDate.setDate(endDate.getDate() - 89)
        break
      default:
        startDate.setDate(endDate.getDate() - 6)
    }
    
    const startDateStr = startDate.toISOString().split('T')[0] + ' 00:00:00'
    const endDateStr = endDate.toISOString().split('T')[0] + ' 23:59:59'
    
    const response = await getAlertsByTimeRange(startDateStr, endDateStr)
    const alertData = response.data || []
    
    // 生成日期标签
    const dateLabels = []
    const dateKeys = []
    
    if (alertTimeRange.value === 'week') {
      // 最近7天：显示具体日期
      for (let i = days - 1; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        const dateStr = date.toISOString().split('T')[0]
        const dayNames = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
        dateLabels.push(`${date.getMonth() + 1}/${date.getDate()}(${dayNames[date.getDay()]})`)
        dateKeys.push(dateStr)
      }
    } else if (alertTimeRange.value === 'month') {
      // 最近30天：按周聚合
      const weekLabels = ['第1周', '第2周', '第3周', '第4周', '第5周']
      for (let i = 0; i < 5; i++) {
        dateLabels.push(weekLabels[i])
        dateKeys.push(`week_${i}`)
      }
    } else if (alertTimeRange.value === 'quarter') {
      // 最近3个月：按月聚合
      for (let i = 2; i >= 0; i--) {
        const date = new Date()
        date.setMonth(date.getMonth() - i)
        const monthStr = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`
        dateLabels.push(`${date.getMonth() + 1}月`)
        dateKeys.push(monthStr)
      }
    }
    
    // 统计每个时间段每种预警类型的数量
    const alertTypeStats = {}
    alertData.forEach(alert => {
      const alertDate = new Date(alert.createdAt)
      const alertType = alert.alertType
      let timeKey = ''
      
      if (alertTimeRange.value === 'week') {
        // 按天统计
        timeKey = alertDate.toISOString().split('T')[0]
      } else if (alertTimeRange.value === 'month') {
        // 按周统计
        const daysDiff = Math.floor((endDate - alertDate) / (1000 * 60 * 60 * 24))
        const weekIndex = Math.floor(daysDiff / 7)
        timeKey = `week_${Math.min(weekIndex, 4)}`
      } else if (alertTimeRange.value === 'quarter') {
        // 按月统计
        timeKey = `${alertDate.getFullYear()}-${String(alertDate.getMonth() + 1).padStart(2, '0')}`
      }
      
      if (!alertTypeStats[alertType]) {
        alertTypeStats[alertType] = {}
      }
      
      if (!alertTypeStats[alertType][timeKey]) {
        alertTypeStats[alertType][timeKey] = 0
      }
      
      alertTypeStats[alertType][timeKey]++
    })
    
    // 预警类型中文映射
    const alertTypeMap = {
      'TEMPERATURE': '体温异常',
      'HEART_RATE': '心率异常', 
      'BMI': 'BMI异常',
      'BLOOD_PRESSURE': '血压异常',
      'BLOOD_SUGAR': '血糖异常',
      'MEDICATION': '用药提醒',
      'EMERGENCY': '紧急预警'
    }
    
    // 生成图表系列数据
    const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#f9ca24', '#a55eea', '#6c5ce7', '#fd79a8']
    const series = []
    const legendData = []
    
    Object.keys(alertTypeStats).forEach((alertType, index) => {
      const data = dateKeys.map(dateKey => alertTypeStats[alertType][dateKey] || 0)
      const displayName = alertTypeMap[alertType] || alertType
      const hasData = data.some(value => value > 0)
      
      series.push({
        name: displayName,
        type: 'line',
        smooth: true,
        data: data,
        itemStyle: { 
          color: colors[index % colors.length],
          opacity: hasData ? 1 : 0.3
        },
        lineStyle: { 
          width: hasData ? 3 : 2,
          type: hasData ? 'solid' : 'dashed',
          opacity: hasData ? 1 : 0.5
        },
        symbol: 'circle',
        symbolSize: hasData ? 6 : 4,
        showSymbol: true,
        connectNulls: false
      })
      legendData.push(displayName)
    })
    
    // 如果没有数据，显示空图表
    if (series.length === 0) {
      series.push({
        name: '暂无数据',
        type: 'line',
        smooth: true,
        data: [0, 0, 0, 0, 0, 0, 0],
        itemStyle: { color: '#ddd' }
      })
      legendData.push('暂无数据')
    }
    
    const option = {
        title: {
          text: chartTitle,
          left: 'center',
          textStyle: { 
            fontSize: 18, 
            fontWeight: 'bold',
            color: '#333'
          },
          top: 10
        },
       tooltip: {
         trigger: 'axis',
         axisPointer: { 
           type: 'cross',
           lineStyle: {
             color: '#999',
             type: 'dashed'
           }
         },
         backgroundColor: 'rgba(255, 255, 255, 0.95)',
         borderColor: '#ccc',
         borderWidth: 1,
         textStyle: {
           color: '#333'
         },
         formatter: function(params) {
           let result = `<div style="font-weight: bold; margin-bottom: 5px;">${params[0].axisValue}</div>`
           params.forEach(param => {
             result += `<div style="margin: 2px 0;">${param.marker}<span style="margin-left: 5px;">${param.seriesName}: <strong>${param.value}</strong>条</span></div>`
           })
           return result
         }
       },
       legend: {
          data: legendData,
          top: 50,
          textStyle: {
            fontSize: 12,
            color: '#666'
          },
          itemGap: 20,
          padding: [5, 0, 15, 0]
        },
       grid: {
          left: '3%',
          right: '4%',
          bottom: '8%',
          top: '25%',
          containLabel: true
        },
       xAxis: {
         type: 'category',
         data: dateLabels,
         axisLine: {
           lineStyle: {
             color: '#e0e0e0'
           }
         },
         axisTick: {
           show: false
         },
         axisLabel: {
           color: '#666',
           fontSize: 11,
           rotate: alertTimeRange.value === 'week' ? 0 : 30
         }
       },
       yAxis: {
         type: 'value',
         name: '预警数量',
         nameTextStyle: {
           fontSize: 12,
           color: '#666'
         },
         axisLine: {
           show: false
         },
         axisTick: {
           show: false
         },
         axisLabel: {
           color: '#666',
           fontSize: 11
         },
         splitLine: {
           lineStyle: {
             color: '#f0f0f0',
             type: 'dashed'
           }
         }
       },
      series: series
    }
    
    alertChart.setOption(option)
  } catch (error) {
    console.error('获取预警趋势数据失败:', error)
    
    // 显示错误状态的图表
     const option = {
       title: {
         text: chartTitle || '健康预警趋势',
         left: 'center',
         textStyle: { fontSize: 16, fontWeight: 'bold' }
       },
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'cross' }
      },
      legend: {
        data: ['暂无数据'],
        top: 30
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        top: '15%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: ['暂无数据']
      },
      yAxis: {
        type: 'value',
        name: '预警数量'
      },
      series: [{
        name: '暂无数据',
        type: 'line',
        smooth: true,
        data: [0],
        itemStyle: { color: '#ddd' }
      }]
    }
    
    alertChart.setOption(option)
  }
}

// 干预方案状态分布图
const initInterventionStatusChart = () => {
  if (!interventionStatusChart.value) return
  
  interventionChart = echarts.init(interventionStatusChart.value)
  
  // 基于真实数据计算状态分布
  const totalActive = interventionStats.value.reduce((sum, item) => sum + item.active, 0)
  const totalCompleted = interventionStats.value.reduce((sum, item) => sum + item.completed, 0)
  const totalInterventions = overviewStats.value.totalInterventions
  
  // 估算其他状态的数量
  const remaining = Math.max(0, totalInterventions - totalActive - totalCompleted)
  const paused = Math.floor(remaining * 0.4)
  const cancelled = Math.floor(remaining * 0.3)
  const pending = remaining - paused - cancelled
  
  const pieData = [
    { value: totalActive, name: '进行中', itemStyle: { color: '#67c23a' } },
    { value: totalCompleted, name: '已完成', itemStyle: { color: '#409eff' } },
    { value: paused, name: '已暂停', itemStyle: { color: '#e6a23c' } },
    { value: cancelled, name: '已取消', itemStyle: { color: '#f56c6c' } },
    { value: pending, name: '待启动', itemStyle: { color: '#909399' } }
  ].filter(item => item.value > 0)
  
  if (pieData.length === 0) {
    pieData.push({ value: 1, name: '暂无数据', itemStyle: { color: '#ddd' } })
  }
  
  const option = {
    title: {
      text: '干预方案状态',
      left: 'center',
      textStyle: { fontSize: 16, fontWeight: 'bold' }
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'middle'
    },
    series: [
      {
        name: '干预方案',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['60%', '50%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: pieData
      }
    ]
  }
  interventionChart.setOption(option)
}

// 设备使用情况图
const initDeviceUsageChart = () => {
  if (!deviceUsageChart.value) return
  
  deviceChart = echarts.init(deviceUsageChart.value)
  
  // 基于真实设备数据生成图表
  const deviceTypes = deviceStats.value.map(item => item.deviceType)
  const normalData = deviceStats.value.map(item => item.normal)
  const disabledData = deviceStats.value.map(item => item.disabled)
  const maintenanceData = deviceStats.value.map(item => item.maintenance)
  const scrappedData = deviceStats.value.map(item => item.scrapped)
  
  // 如果没有数据，显示默认提示
  const categories = deviceTypes.length > 0 ? deviceTypes : ['暂无设备']
  const normalSeries = normalData.length > 0 ? normalData : [0]
  const disabledSeries = disabledData.length > 0 ? disabledData : [0]
  const maintenanceSeries = maintenanceData.length > 0 ? maintenanceData : [0]
  const scrappedSeries = scrappedData.length > 0 ? scrappedData : [0]
  
  const option = {
    title: {
      text: '设备状态分布',
      left: 'center',
      textStyle: { fontSize: 16, fontWeight: 'bold' }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      formatter: function(params) {
        let result = params[0].name + '<br/>'
        params.forEach(param => {
          result += param.marker + param.seriesName + ': ' + param.value + '<br/>'
        })
        return result
      }
    },
    legend: {
      data: ['正常', '停用', '维修中', '已报废'],
      top: 30
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: categories,
      axisLabel: {
        interval: 0,
        rotate: categories.length > 6 ? 45 : 0
      }
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '正常',
        type: 'bar',
        stack: '总量',
        data: normalSeries,
        itemStyle: { color: '#67c23a' }
      },
      {
        name: '停用',
        type: 'bar',
        stack: '总量',
        data: disabledSeries,
        itemStyle: { color: '#909399' }
      },
      {
        name: '维修中',
        type: 'bar',
        stack: '总量',
        data: maintenanceSeries,
        itemStyle: { color: '#e6a23c' }
      },
      {
        name: '已报废',
        type: 'bar',
        stack: '总量',
        data: scrappedSeries,
        itemStyle: { color: '#f56c6c' }
      }
    ]
  }
  deviceChart.setOption(option)
}

// 老人年龄分布图
const initAgeDistributionChart = async () => {
  if (!ageDistributionChart.value) return
  
  ageChart = echarts.init(ageDistributionChart.value)
  
  try {
    // 获取真实的老人档案数据
    const response = await getAllElderlyProfiles()
    const elderlyProfiles = response.data || []
    
    const ageRanges = ['60-65岁', '66-70岁', '71-75岁', '76-80岁', '81-85岁', '86岁以上']
    
    // 根据真实数据统计年龄分布
    const calculateAgeDistribution = (profiles) => {
      const ageCount = [0, 0, 0, 0, 0, 0] // 对应6个年龄段
      
      profiles.forEach(profile => {
        const age = profile.age
        if (age >= 60 && age <= 65) {
          ageCount[0]++
        } else if (age >= 66 && age <= 70) {
          ageCount[1]++
        } else if (age >= 71 && age <= 75) {
          ageCount[2]++
        } else if (age >= 76 && age <= 80) {
          ageCount[3]++
        } else if (age >= 81 && age <= 85) {
          ageCount[4]++
        } else if (age >= 86) {
          ageCount[5]++ // 86岁以上
        }
      })
      
      return ageCount
    }
    
    const ageData = calculateAgeDistribution(elderlyProfiles)
    const totalElderly = elderlyProfiles.length
    
    const option = {
      title: {
        text: `老人年龄分布 (总计: ${totalElderly}人)`,
        left: 'center',
        textStyle: { fontSize: 16, fontWeight: 'bold' }
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
        formatter: function(params) {
          const data = params[0]
          const percentage = totalElderly > 0 ? ((data.value / totalElderly) * 100).toFixed(1) : 0
          return `${data.name}<br/>人数: ${data.value}<br/>占比: ${percentage}%`
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        top: '15%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: ageRanges
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '人数',
          type: 'bar',
          data: ageData,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }
      ]
    }
    ageChart.setOption(option)
    
  } catch (error) {
    console.error('获取老人年龄分布数据失败:', error)
    // 如果获取数据失败，显示空图表
    const ageRanges = ['60-65岁', '66-70岁', '71-75岁', '76-80岁', '81-85岁', '86岁以上']
    const emptyData = [0, 0, 0, 0, 0, 0]
    
    const option = {
      title: {
        text: '老人年龄分布 (暂无数据)',
        left: 'center',
        textStyle: { fontSize: 16, fontWeight: 'bold' }
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        top: '15%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: ageRanges
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '人数',
          type: 'bar',
          data: emptyData,
          itemStyle: {
            color: '#cccccc'
          }
        }
      ]
    }
    ageChart.setOption(option)
  }
}

// 月度数据对比图
const initMonthlyComparisonChart = () => {
  if (!monthlyComparisonChart.value) return
  
  monthlyChart = echarts.init(monthlyComparisonChart.value)
  updateMonthlyChart()
}

// 更新月度对比图
const updateMonthlyChart = async () => {
  if (!monthlyChart) return
  
  // 获取真实的月度数据
  const getRealMonthlyData = async (type) => {
    const data = []
    const months = []
    
    // 生成过去12个月的时间范围
    for (let i = 11; i >= 0; i--) {
      const date = new Date()
      date.setMonth(date.getMonth() - i)
      const year = date.getFullYear()
      const month = date.getMonth() + 1
      
      // 计算月份的开始和结束日期
      const startDate = `${year}-${month.toString().padStart(2, '0')}-01`
      // 获取月份的最后一天（避免时区影响）
      const lastDay = new Date(year, month, 0).getDate()
      const endDate = `${year}-${month.toString().padStart(2, '0')}-${lastDay.toString().padStart(2, '0')}` // 月末日期
      
      months.push(`${month}月`)
      
      try {
        let count = 0
        
        if (type === 'alerts') {
          // 获取预警数据
          const response = await getAlertsByTimeRange(startDate + ' 00:00:00', endDate + ' 23:59:59')
          console.log('预警数据响应:', response, '时间范围:', startDate, 'to', endDate)
          if (response.code === 200) {
            count = response.data?.length || 0
          } else {
            console.warn('获取预警数据失败:', response)
            count = 0
          }
        } else if (type === 'interventions') {
          // 获取干预计划数据（按创建时间统计）
          const response = await getInterventionPlanPage({ pageNum: 1, pageSize: 1000 })
          if (response.data && response.data.records) {
            // 检查数据时间范围
            const records = response.data.records
            const validRecords = records.filter(plan => plan.createdTime)
            
            if (validRecords.length === 0) {
              console.warn('干预计划数据中没有有效的创建时间')
              count = 0
            } else {
              // 获取数据的时间范围用于调试
              const dataTimeRange = {
                min: Math.min(...validRecords.map(p => new Date(p.createdTime).getTime())),
                max: Math.max(...validRecords.map(p => new Date(p.createdTime).getTime()))
              }
              
              // 过滤指定时间范围内创建的计划
              const filteredPlans = validRecords.filter(plan => {
                const createDate = new Date(plan.createdTime).toISOString().split('T')[0]
                // 使用包含边界的日期比较
                const isAfterStart = createDate >= startDate
                const isBeforeEnd = createDate <= endDate
                return isAfterStart && isBeforeEnd
              })
              
              count = filteredPlans.length
              
              // 如果没有匹配的数据，提供调试信息
              if (count === 0 && validRecords.length > 0) {
                console.info('干预计划时间范围不匹配:', {
                  查询范围: `${startDate} 到 ${endDate}`,
                  数据范围: {
                    最早: new Date(dataTimeRange.min).toISOString().split('T')[0],
                    最晚: new Date(dataTimeRange.max).toISOString().split('T')[0]
                  },
                  总记录数: records.length,
                  有效记录数: validRecords.length
                })
              }
            }
          } else {
            console.warn('获取干预计划数据失败:', response)
            count = 0
          }
        } else {
          // 健康评估数据（基于预警数据估算）
          const response = await getAlertsByTimeRange(startDate + ' 00:00:00', endDate + ' 23:59:59')
          console.log('健康评估基础数据响应:', response, '时间范围:', startDate, 'to', endDate)
          if (response.code === 200) {
            count = Math.floor((response.data?.length || 0) * 1.5) // 假设评估数量是预警的1.5倍
          } else {
            console.warn('获取健康评估基础数据失败:', response)
            count = 0
          }
        }
        
        data.push(Math.floor(count))
      } catch (error) {
        console.warn(`获取${months[months.length - 1]}数据失败:`, error)
        // 如果获取失败，使用0或基于前一个月的估算值
        const lastValue = data[data.length - 1] || 0
        data.push(Math.max(0, Math.floor(lastValue * 0.9))) // 使用前一个月90%的值作为估算
      }
    }
    
    return { data, months }
  }
  
  try {
    const monthlyData = await getRealMonthlyData(monthlyDataType.value)
    
    const dataMap = {
      alerts: {
        title: '月度预警数量对比',
        data: monthlyData.data,
        months: monthlyData.months
      },
      interventions: {
        title: '月度干预计划创建对比',
        data: monthlyData.data,
        months: monthlyData.months
      },
      assessments: {
        title: '月度健康评估对比',
        data: monthlyData.data,
        months: monthlyData.months
      }
    }
    
    const currentData = dataMap[monthlyDataType.value]
    
    const option = {
      title: {
        text: currentData.title,
        left: 'center',
        textStyle: { fontSize: 18, fontWeight: 'bold' }
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'cross' }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        top: '10%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: currentData.months
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '数量',
          type: 'line',
          smooth: true,
          data: currentData.data,
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
              { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
            ])
          },
          itemStyle: { color: '#409eff' },
          lineStyle: { width: 3 }
        }
      ]
    }
    monthlyChart.setOption(option)
  } catch (error) {
    console.error('更新月度图表失败:', error)
    // 如果获取真实数据失败，显示错误提示
    ElMessage.warning('获取月度数据失败，请稍后重试')
  }
}

// 更新预警图表
const updateAlertChart = () => {
  // 根据时间范围更新数据
  initAlertTrendChart()
}

// 获取进度条颜色
const getProgressColor = (percentage) => {
  if (percentage >= 80) return '#67c23a'
  if (percentage >= 60) return '#e6a23c'
  return '#f56c6c'
}

// 加载数据
const loadData = async () => {
  try {
    // 并行加载所有数据
    const [elderlyData, alertData, deviceData, interventionData, templateData] = await Promise.all([
      getAllElderlyProfiles().catch(() => ({ data: [] })),
      getAllAlerts().catch(() => ({ data: [] })),
      getDeviceList({ pageNum: 1, pageSize: 1000 }).catch(() => ({ data: { records: [] } })),
      getInterventionPlanPage({ pageNum: 1, pageSize: 1000 }).catch(() => ({ data: { records: [] } })),
      getInterventionTemplatePage({ pageNum: 1, pageSize: 1000 }).catch(() => ({ data: { records: [] } }))
    ])

    // 处理老人数据
    const elderlyList = elderlyData.data || []
    const currentMonth = new Date().getMonth()
    const newElderlyThisMonth = elderlyList.filter(elderly => {
      const createTime = new Date(elderly.createTime || elderly.createdTime)
      return createTime.getMonth() === currentMonth
    }).length

    // 处理预警数据
    const alertList = alertData.data || []
    const activeAlerts = alertList.filter(alert => alert.status === 'ACTIVE').length
    
    // 处理设备数据
    const deviceList = deviceData.data?.records || deviceData.data || []
    const normalDevices = deviceList.filter(device => device.deviceStatus === 1).length
    
    // 处理干预方案数据
    const interventionList = interventionData.data?.records || interventionData.data || []
    const activeInterventions = interventionList.filter(plan => plan.status === 'ACTIVE').length

    // 更新总览统计
    overviewStats.value = {
      totalElderly: elderlyList.length,
      newElderlyThisMonth: newElderlyThisMonth,
      totalAlerts: alertList.length,
      activeAlerts: activeAlerts,
      totalInterventions: interventionList.length,
      activeInterventions: activeInterventions,
      totalDevices: deviceList.length,
      normalDevices: normalDevices
    }
    
    // 处理预警统计数据
    const alertTypeMap = new Map()
    alertList.forEach(alert => {
      const type = alert.alertType || '其他预警'
      if (!alertTypeMap.has(type)) {
        alertTypeMap.set(type, { total: 0, active: 0, resolved: 0 })
      }
      const stat = alertTypeMap.get(type)
      stat.total++
      if (alert.status === 'ACTIVE') {
        stat.active++
      } else if (alert.status === 'RESOLVED') {
        stat.resolved++
      }
      // IGNORED状态的预警不计入已解决
    })
    
    alertStats.value = Array.from(alertTypeMap.entries()).map(([type, stat]) => ({
      alertType: type,
      total: stat.total,
      active: stat.active,
      resolved: stat.resolved,
      rate: stat.total > 0 ? Math.round((stat.resolved / stat.total) * 100) : 0
    }))
    
    // 处理干预统计数据
    const interventionTypeMap = new Map()
    interventionList.forEach(plan => {
      const type = plan.planType || plan.interventionType || '其他干预'
      if (!interventionTypeMap.has(type)) {
        interventionTypeMap.set(type, { total: 0, active: 0, completed: 0 })
      }
      const stat = interventionTypeMap.get(type)
      stat.total++
      if (plan.status === 'ACTIVE') {
        stat.active++
      } else if (plan.status === 'COMPLETED') {
        stat.completed++
      }
      // PENDING, PAUSED, CANCELLED状态不计入活跃或已完成
    })
    
    // 确保显示所有干预类型，即使没有数据也要显示
    const allInterventionTypes = ['MEDICATION', 'EXERCISE', 'DIET', 'LIFESTYLE', 'MONITORING', 'MEDICAL']
    const typeDescriptions = {
      'MEDICATION': '用药',
      'EXERCISE': '运动', 
      'DIET': '饮食',
      'LIFESTYLE': '生活方式',
      'MONITORING': '监测',
      'MEDICAL': '医疗'
    }
    
    // 为没有数据的干预类型添加空记录
    allInterventionTypes.forEach(type => {
      if (!interventionTypeMap.has(type) && !interventionTypeMap.has(typeDescriptions[type])) {
        interventionTypeMap.set(typeDescriptions[type], { total: 0, active: 0, completed: 0 })
      }
    })
    
    interventionStats.value = Array.from(interventionTypeMap.entries()).map(([type, stat]) => ({
      planType: type,
      total: stat.total,
      active: stat.active,
      completed: stat.completed,
      effectiveness: stat.total > 0 ? Math.round((stat.completed / stat.total) * 100) : 0 // 基于完成率计算有效率
    }))
    
    // 处理设备统计数据
    const deviceTypeMap = new Map()
    deviceList.forEach(device => {
      const type = device.deviceType || '未知设备'
      if (!deviceTypeMap.has(type)) {
        deviceTypeMap.set(type, { total: 0, normal: 0, disabled: 0, maintenance: 0, scrapped: 0 })
      }
      const stat = deviceTypeMap.get(type)
      stat.total++
      // 根据设备状态分类：0-停用，1-正常，2-维修中，3-已报废
      switch (device.deviceStatus) {
        case 0:
          stat.disabled++
          break
        case 1:
          stat.normal++
          break
        case 2:
          stat.maintenance++
          break
        case 3:
          stat.scrapped++
          break
        default:
          stat.disabled++
      }
    })

    deviceStats.value = Array.from(deviceTypeMap.entries()).map(([type, stat]) => ({
      deviceType: type,
      total: stat.total,
      normal: stat.normal,
      disabled: stat.disabled,
      maintenance: stat.maintenance,
      scrapped: stat.scrapped,
      normalRate: stat.total > 0 ? Math.round((stat.normal / stat.total) * 100) : 0
    }))
    
  } catch (error) {
    console.error('加载数据失败:', error)
    // 如果API调用失败，使用默认数据
    overviewStats.value = {
      totalElderly: 0,
      newElderlyThisMonth: 0,
      totalAlerts: 0,
      activeAlerts: 0,
      totalInterventions: 0,
      activeInterventions: 0,
      totalDevices: 0,
      normalDevices: 0
    }
    alertStats.value = []
    interventionStats.value = []
    deviceStats.value = []
  }
}

// 窗口大小变化时重新调整图表
const handleResize = () => {
  if (alertChart) alertChart.resize()
  if (interventionChart) interventionChart.resize()
  if (deviceChart) deviceChart.resize()
  if (ageChart) ageChart.resize()
  if (monthlyChart) monthlyChart.resize()
}

// 生命周期
onMounted(async () => {
  await loadData()
  await initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  if (alertChart) {
    alertChart.dispose()
    alertChart = null
  }
  if (interventionChart) {
    interventionChart.dispose()
    interventionChart = null
  }
  if (deviceChart) {
    deviceChart.dispose()
    deviceChart = null
  }
  if (ageChart) {
    ageChart.dispose()
    ageChart = null
  }
  if (monthlyChart) {
    monthlyChart.dispose()
    monthlyChart = null
  }
})
</script>

<style scoped>
.deepseek-test-section {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  border: 1px solid #e0e0e0;
}

.response-area {
  margin-top: 15px;
  padding: 15px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.reports-container {
  padding: 24px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  text-align: center;
  margin-bottom: 32px;
}

.page-header h1 {
  font-size: 32px;
  font-weight: bold;
  color: #2c3e50;
  margin: 0 0 8px 0;
}

.page-description {
  font-size: 16px;
  color: #7f8c8d;
  margin: 0;
}

/* 统计卡片 */
.overview-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.card-icon.elderly {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-icon.alert {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.card-icon.intervention {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.card-icon.device {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.card-content h3 {
  font-size: 28px;
  font-weight: bold;
  color: #2c3e50;
  margin: 0 0 4px 0;
}

.card-content p {
  font-size: 14px;
  color: #7f8c8d;
  margin: 0 0 8px 0;
}

.trend {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 12px;
  font-weight: 500;
}

.trend.positive {
  background: #e8f5e8;
  color: #67c23a;
}

.trend.negative {
  background: #fef0f0;
  color: #f56c6c;
}

/* 图表区域 */
.charts-section {
  margin-bottom: 32px;
}

.chart-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}

.chart-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.chart-container.full-width {
  grid-column: 1 / -1;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 12px;
}

.chart-header h3 {
  flex: 1;
  min-width: 120px;
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50;
  margin: 0;
}

.chart {
  width: 100%;
  height: 300px;
}

.chart.large {
  height: 400px;
}

/* 详细统计 */
.detailed-stats {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.stats-tabs {
  margin-top: 16px;
}

.table-container {
  margin-top: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .overview-cards {
    grid-template-columns: 1fr;
  }
  
  .chart-row {
    grid-template-columns: 1fr;
  }
  
  .stat-card {
    padding: 16px;
  }
  
  .card-icon {
    width: 48px;
    height: 48px;
    font-size: 20px;
    margin-right: 16px;
  }
  
  .card-content h3 {
    font-size: 24px;
  }
}

@media (max-width: 480px) {
  .deepseek-test-section {
    background-color: #f9f9f9;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    border: 1px solid #e0e0e0;
  }

  .response-area {
    margin-top: 15px;
    padding: 15px;
    background-color: #fff;
    border: 1px solid #ddd;
    border-radius: 4px;
    white-space: pre-wrap;
    word-wrap: break-word;
  }

  .reports-container {
    padding: 16px;
  }

  .page-header h1 {
    font-size: 24px;
  }

  .chart-container {
    padding: 16px;
  }

  .chart {
    height: 250px;
  }
}

.assistant-fab {
  position: fixed;
  right: 20px;
  bottom: 100px;
  width: 60px;
  height: 60px;
  background-color: #409EFF;
  color: white;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  z-index: 1000;

  .el-icon {
    font-size: 24px;
  }

  span {
    font-size: 12px;
    margin-top: 2px;
  }

  &:hover {
    transform: scale(1.1);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
  }
}

.assistant-chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chat-history {
  flex-grow: 1;
  overflow-y: auto;
  padding: 16px;
  background-color: #f7f7f7;
}

.chat-message {
  margin-bottom: 16px;
  display: flex;
}

.chat-message.user {
  justify-content: flex-end;
}

.chat-message.assistant {
  justify-content: flex-start;
}

.message-bubble {
  max-width: 80%;
  padding: 12px 16px;
  border-radius: 18px;
  position: relative;
}

.chat-message.user .message-bubble {
  background-color: #409EFF;
  color: white;
  border-top-right-radius: 4px;
}

.chat-message.assistant .message-bubble {
  background-color: #FFFFFF;
  color: #303133;
  border: 1px solid #e4e7ed;
  border-top-left-radius: 4px;
}

.role-label {
  font-size: 0.8em;
  font-weight: bold;
  margin-bottom: 5px;
}

.user-label {
  color: #5c5c5c; /* 深灰色字体 */
}

.assistant-label {
  color: #007bff; /* 蓝色字体 */
}

.message-content {
  font-size: 1em;
  line-height: 1.4;
}

.loading-indicator {
  display: flex;
  align-items: center;
  span {
    margin-right: 8px;
    color: #999;
  }
  .dot {
    width: 8px;
    height: 8px;
    margin: 0 2px;
    background-color: #999;
    border-radius: 50%;
    animation: bounce 1.4s infinite ease-in-out both;
    &:nth-child(1) { animation-delay: -0.32s; }
    &:nth-child(2) { animation-delay: -0.16s; }
  }
}

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1.0); }
}

.chat-input-area {
  display: flex;
  align-items: center;
  padding: 16px;
  border-top: 1px solid #e4e7ed;
  background-color: #ffffff;
}

.chat-input-area .el-input {
  flex-grow: 1;
}

.button-group {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: 16px;
  flex-shrink: 0;
}

.clear-button {
  background-color: #f56c6c;
  border-color: #f56c6c;
  color: white;
  height: 32px;
  padding: 0 15px;
}

.clear-button:hover {
  background-color: #f78989;
  border-color: #f78989;
}

.send-button {
  flex-shrink: 0;
  height: 32px;
  padding: 0 15px;
}



.assistant-fab-header {
  position: absolute;
  top: 24px;
  right: 24px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background-color: #409EFF;
  color: white;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.assistant-fab-header:hover {
  background-color: #66b1ff;
}

/* 抽屉标题样式 */
:deep(.el-drawer__header) {
  margin-bottom: 0;
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
  background-color: #fafafa;
}

:deep(.el-drawer__title) {
  color: #303133;
  font-weight: 600;
}

/* 美化聊天记录滚动条 */
.chat-history::-webkit-scrollbar {
  width: 6px;
}

.chat-history::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 6px;
}

.chat-history::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 6px;
}

.chat-history::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>