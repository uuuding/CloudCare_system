<template>
  <div class="elderly-profile-analysis">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>老人画像分析</h1>
      <el-button @click="goBack" type="primary" plain>
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
    </div>

    <!-- 上部分：老人信息和六维图 -->
    <div class="upper-section">
      <!-- 左侧：老人健康观察信息 -->
      <div class="health-info-card">
        <div class="card-header">
          <h3>健康观察信息</h3>
          <span class="observation-time">{{ observationData?.observationTime }}</span>
        </div>
        <div class="health-info-content">
          <div class="patient-info">
            <div class="info-row">
              <span class="label">姓名：</span>
              <span class="value">{{ elderlyProfile?.name || '-' }}</span>
            </div>
            <div class="info-row">
              <span class="label">年龄：</span>
              <span class="value">{{ elderlyProfile?.age || '-' }}岁</span>
            </div>
            <div class="info-row">
              <span class="label">性别：</span>
              <span class="value">{{ elderlyProfile?.gender || '-' }}</span>
            </div>
            <div class="info-row">
              <span class="label">体检地点：</span>
              <span class="value">{{ observationData?.observationLocation || '-' }}</span>
            </div>
          </div>

          <div class="vital-signs">
            <h4>身体指标</h4>
            <div class="signs-grid">
              <div class="sign-item">
                <span class="sign-label">体温</span>
                <span class="sign-value" :class="getTemperatureClass(observationData?.bodyTemperature)">
                  {{ observationData?.bodyTemperature || '-' }}°C
                </span>
              </div>
              <div class="sign-item">
                <span class="sign-label">血压</span>
                <span class="sign-value" :class="getBloodPressureClass(observationData?.systolicBp)">
                  {{ observationData?.systolicBp || '-' }}mmHg
                </span>
              </div>
              <div class="sign-item">
                <span class="sign-label">心率</span>
                <span class="sign-value" :class="getHeartRateClass(observationData?.heartRate)">
                  {{ observationData?.heartRate || '-' }}次/分
                </span>
              </div>
              <div class="sign-item">
                <span class="sign-label">BMI</span>
                <span class="sign-value" :class="getBMIClass(calculateBMI())">
                  {{ calculateBMI() || '-' }}
                </span>
              </div>
              <div class="sign-item">
                <span class="sign-label">睡眠时长</span>
                <span class="sign-value">
                  {{ observationData?.sleepHours || '-' }}小时
                </span>
              </div>
              <div class="sign-item">
                <span class="sign-label">咳嗽</span>
                <span class="sign-value">
                  {{ observationData?.cough ? '是' : '否' }}
                </span>
              </div>
            </div>
          </div>

          <div class="disease-history" v-if="chronicDiseases?.length">
            <h4>既往病史</h4>
            <div class="disease-list">
              <el-tag
                  v-for="disease in chronicDiseases"
                  :key="disease.id"
                  :type="getDiseaseTagType(disease.diseaseCategory)"
                  class="disease-tag"
              >
                {{ disease.diseaseName }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：六维雷达图 -->
      <div class="radar-chart-card">
        <div class="card-header">
          <h3>健康指标六维分析</h3>
        </div>
        <div class="chart-container">
          <div id="radarChart" style="width: 100%; height: 400px;"></div>
        </div>
      </div>
    </div>

    <!-- 画像分析图部分 -->
    <div class="profile-chart-section">
      <div class="profile-chart-card">
        <div class="card-header">
          <h3>老人健康画像分析</h3>
        </div>
        <div class="chart-container">
          <div id="wordCloudChart" style="width: 100%; height: 300px;"></div>
        </div>
      </div>
    </div>

    <!-- 下部分：HMM预测结果 -->
    <div class="lower-section">
      <div class="prediction-card">
        <div class="card-header">
          <h3>健康状态预测分析</h3>
          <el-button @click="refreshPrediction" type="primary" size="small" :loading="predictionLoading">
            <el-icon><Refresh /></el-icon>
            刷新预测
          </el-button>
        </div>

        <div class="prediction-content" v-loading="predictionLoading">
          <div class="current-state">
            <div class="state-item">
              <span class="state-label">当前状态：</span>
              <el-tag :type="getStateTagType(currentState)" size="large" class="state-tag">
                {{ currentState }}
              </el-tag>
            </div>
            <div class="state-item">
              <span class="state-label">下一状态预测结果：</span>
              <el-tag :type="getStateTagType(predictedState)" size="large" class="state-tag">
                {{ predictedState }}
              </el-tag>
            </div>
          </div>

          <div class="scores-section">
            <h4>各状态评分</h4>
            <div class="scores-grid">
              <div
                  v-for="(score, state) in stateScores"
                  :key="state"
                  class="score-item"
              >
                <div class="score-header">
                  <span class="score-state">{{ state }}</span>
                  <span class="score-value">{{ (score * 100).toFixed(1) }}%</span>
                </div>
                <el-progress
                    :percentage="score * 100"
                    :color="getProgressColor(state)"
                    :stroke-width="8"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { predictNextState, getStateScores } from '@/api/hmm'
import { getAllElderlyProfiles, getChronicDiseasesByElderlyId } from '@/api/elderlyProfile'

const route = useRoute()
const router = useRouter()

// 响应式数据
const observationData = ref(null)
const elderlyProfile = ref(null)
const chronicDiseases = ref([])
const currentState = ref('')
const predictedState = ref('')
const stateScores = ref({})
const predictionLoading = ref(false)

// 生命周期
onMounted(async () => {
  await initializeData()
  initRadarChart()
  initProfileChart()
  await performPrediction()
})

// 初始化数据
const initializeData = async () => {
  try {
    // 从路由query参数获取数据
    const queryData = route.query

    if (queryData.observationData) {
      observationData.value = JSON.parse(decodeURIComponent(queryData.observationData))
    }
    if (queryData.elderlyProfile) {
      elderlyProfile.value = JSON.parse(decodeURIComponent(queryData.elderlyProfile))
    }
    if (queryData.chronicDiseases) {
      chronicDiseases.value = JSON.parse(decodeURIComponent(queryData.chronicDiseases))
    }

    // 如果没有从query获取到数据，显示警告
    if (!observationData.value) {
      ElMessage.warning('数据获取失败，请重新进入')
      return
    }

    // 计算当前健康状态
    currentState.value = calculateCurrentHealthState()
  } catch (error) {
    console.error('初始化数据失败:', error)
    ElMessage.error('数据初始化失败')
  }
}

// 计算当前健康状态
const calculateCurrentHealthState = () => {
  if (!observationData.value) return '未知'

  let abnormalCount = 0
  const obs = observationData.value

  // 检查各项指标
  if (obs.bodyTemperature && (obs.bodyTemperature < 36.0 || obs.bodyTemperature > 37.2)) abnormalCount++
  if (obs.systolicBp && (obs.systolicBp < 90 || obs.systolicBp > 139)) abnormalCount++
  if (obs.heartRate && (obs.heartRate < 60 || obs.heartRate > 100)) abnormalCount++

  const bmi = calculateBMI()
  if (bmi && (bmi < 18.5 || bmi > 23.9)) abnormalCount++

  if (abnormalCount === 0) return '健康'
  if (abnormalCount === 1) return '注意'
  return '异常'
}

// 计算BMI
const calculateBMI = () => {
  if (!observationData.value?.height || !observationData.value?.weight) return null
  const heightInMeters = observationData.value.height / 100
  const bmi = observationData.value.weight / (heightInMeters * heightInMeters)
  return bmi.toFixed(1)
}

// 执行HMM预测
const performPrediction = async () => {
  if (!observationData.value) return

  predictionLoading.value = true
  try {
    // 构造HMM请求数据
    const hmmData = {
      observation: convertToObservationArray(),
      diseaseCount: calculateDiseaseCount(),
      currentState: currentState.value
    }

    // 获取预测结果
    const [predictResponse, scoresResponse] = await Promise.all([
      predictNextState(hmmData),
      getStateScores(hmmData)
    ])

    if (predictResponse.code === 200) {
      predictedState.value = predictResponse.data.nextState
    }

    if (scoresResponse.code === 200) {
      stateScores.value = scoresResponse.data
    }
  } catch (error) {
    console.error('预测失败:', error)
    ElMessage.error('健康状态预测失败')
  } finally {
    predictionLoading.value = false
  }
}

// 将观察数据转换为HMM所需的数组格式
const convertToObservationArray = () => {
  if (!observationData.value) return [0, 0, 0, 0, 0]

  const obs = observationData.value
  const observation = []

  // 体温 (0: 正常, 1: 轻微异常, 2: 严重异常)
  if (obs.bodyTemperature) {
    if (obs.bodyTemperature >= 36.0 && obs.bodyTemperature <= 37.2) observation.push(0)
    else if (obs.bodyTemperature > 37.2 && obs.bodyTemperature <= 38.0) observation.push(1)
    else observation.push(2)
  } else observation.push(0)

  // 血压
  if (obs.systolicBp) {
    if (obs.systolicBp >= 90 && obs.systolicBp <= 139) observation.push(0)
    else if (obs.systolicBp >= 140 && obs.systolicBp <= 159) observation.push(1)
    else observation.push(2)
  } else observation.push(0)

  // BMI
  const bmi = calculateBMI()
  if (bmi) {
    if (bmi >= 18.5 && bmi <= 23.9) observation.push(0)
    else if (bmi >= 24.0 && bmi <= 27.9) observation.push(1)
    else observation.push(2)
  } else observation.push(0)

  // 心率
  if (obs.heartRate) {
    if (obs.heartRate >= 60 && obs.heartRate <= 100) observation.push(0)
    else if (obs.heartRate > 100 && obs.heartRate <= 120) observation.push(1)
    else observation.push(2)
  } else observation.push(0)

  // 睡眠时长
  if (obs.sleepHours) {
    if (obs.sleepHours >= 7 && obs.sleepHours <= 9) observation.push(0)
    else if (obs.sleepHours >= 6 && obs.sleepHours < 7) observation.push(1)
    else observation.push(2)
  } else observation.push(0)

  return observation
}

// 计算疾病数量
const calculateDiseaseCount = () => {
  if (!chronicDiseases.value?.length) return [0, 0, 0]

  const count = [0, 0, 0] // A类, B类, C类
  chronicDiseases.value.forEach(disease => {
    switch (disease.diseaseCategory) {
      case 'A':
      case '重病':
        count[0]++
        break
      case 'B':
      case '中度病':
        count[1]++
        break
      case 'C':
      case '轻病':
        count[2]++
        break
    }
  })

  return count
}

// 初始化雷达图
const initRadarChart = () => {
  const chartDom = document.getElementById('radarChart')
  if (!chartDom) return

  const myChart = echarts.init(chartDom)

  // 标准健康数据（绿色基准线）- 基于最佳健康值计算
  const generateStandardData = () => {
    // 使用各指标的最佳值来计算标准分数
    const standardScores = []

    // 体温指标：36.6°C（最佳体温）
    standardScores.push(100)

    // 血压指标：120mmHg（最佳收缩压）
    standardScores.push(100)

    // 心率指标：75bpm（最佳心率）
    standardScores.push(100)

    // BMI指标：21.2（最佳BMI）
    standardScores.push(100)

    // 睡眠质量：8小时（最佳睡眠时间）
    standardScores.push(100)

    // 整体状态：所有指标都是最佳值
    standardScores.push(100)

    return standardScores
  }

  const standardData = generateStandardData()

  // 阈值数据（红色警戒线）- 基于临界值计算
  const generateThresholdData = () => {
    const thresholdScores = []

    // 体温阈值：37.2°C（发热临界点）
    thresholdScores.push(75)

    // 血压阈值：140mmHg（高血压临界点）
    thresholdScores.push(75)

    // 心率阈值：100bpm（心动过速临界点）
    thresholdScores.push(75)

    // BMI阈值：24.0（超重临界点）
    thresholdScores.push(75)

    // 睡眠阈值：6小时（睡眠不足临界点）
    thresholdScores.push(75)

    // 整体阈值状态
    thresholdScores.push(75)

    return thresholdScores
  }

  const option = {
    title: {
      text: '健康指标六维对比分析',
      left: 'center',
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333'
      }
    },
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(0, 0, 0, 0.8)',
      borderColor: '#409EFF',
      borderWidth: 1,
      textStyle: {
        color: '#fff',
        fontSize: 12
      },
      triggerOn: 'mousemove',
      enterable: true,
      formatter: function(params) {
        const indicators = ['体温指标', '血压指标', '心率指标', 'BMI指标', '睡眠质量', '整体状态']
        let result = `<div style="padding: 8px;">`
        result += `<div style="font-weight: bold; margin-bottom: 6px; color: ${params.color};">${params.seriesName} - ${params.name}</div>`
        params.value.forEach((value, index) => {
          result += `<div style="margin: 2px 0;">${indicators[index]}: <span style="font-weight: bold;">${value}%</span></div>`
        })
        result += `</div>`
        return result
      }
    },
    legend: {
      data: ['标准指标', '阈值指标', '当前状态'],
      top: 30,
      itemGap: 20,
      textStyle: {
        fontSize: 12,
        color: '#666'
      }
    },
    radar: {
      indicator: [
        { name: '体温指标', max: 100, min: 0 },
        { name: '血压指标', max: 100, min: 0 },
        { name: '心率指标', max: 100, min: 0 },
        { name: 'BMI指标', max: 100, min: 0 },
        { name: '睡眠质量', max: 100, min: 0 },
        { name: '整体状态', max: 100, min: 0 }
      ],
      center: ['50%', '55%'],
      radius: '65%',
      splitNumber: 3,
      shape: 'polygon',
      axisName: {
        color: '#333',
        fontSize: 13,
        fontWeight: '500'
      },
      axisLine: {
        lineStyle: {
          color: '#ccc'
        }
      },
      splitLine: {
        lineStyle: {
          color: '#e0e0e0',
          width: 1
        }
      },
      splitArea: {
        show: true,
        areaStyle: {
          color: ['rgba(250, 250, 250, 0.1)', 'rgba(200, 200, 200, 0.1)']
        }
      },
      axisLabel: {
        show: true,
        fontSize: 11,
        color: '#666',
        fontWeight: '500',
        formatter: function(value) {
          if (value === 50) return '50%'
          if (value === 100) return '100%'
          return ''
        }
      }
    },
    series: [{
      name: '健康指标对比',
      type: 'radar',
      data: [
        {
          value: standardData,
          name: '标准指标',
          lineStyle: {
            color: '#52c41a',
            width: 2
          },
          itemStyle: {
            color: '#52c41a'
          },
          areaStyle: {
            color: 'rgba(82, 196, 26, 0.1)'
          }
        },
        {
          value: generateThresholdData(),
          name: '阈值指标',
          lineStyle: {
            color: '#ff4d4f',
            width: 2,
            type: 'dashed'
          },
          itemStyle: {
            color: '#ff4d4f'
          },
          areaStyle: {
            color: 'rgba(255, 77, 79, 0.1)'
          }
        },
        {
          value: generateRadarData(),
          name: '当前状态',
          lineStyle: {
            color: '#1890ff',
            width: 2
          },
          itemStyle: {
            color: '#1890ff'
          },
          areaStyle: {
            color: 'rgba(24, 144, 255, 0.1)'
          }
        }
      ]
    }]
  }

  myChart.setOption(option)

  // 响应式处理
  window.addEventListener('resize', () => {
    myChart.resize()
  })
}

// 生成雷达图数据
// 动态健康指标计算算法
const generateRadarData = () => {
  if (!observationData.value) return [60, 60, 60, 60, 60, 60]

  const obs = observationData.value
  const data = []

  // 体温指标动态计算 (36.0-37.2°C为最佳)
  if (obs.bodyTemperature) {
    const temp = obs.bodyTemperature
    const optimal = 36.6 // 最佳体温
    const normalRange = 0.6 // 正常范围±0.6°C

    if (temp >= 36.0 && temp <= 37.2) {
      // 在正常范围内，根据与最佳值的距离计算分数
      const deviation = Math.abs(temp - optimal)
      const score = Math.max(75, 100 - (deviation / normalRange) * 25)
      data.push(Math.round(score))
    } else {
      // 超出正常范围，根据偏离程度计算
      const deviation = temp < 36.0 ? (36.0 - temp) : (temp - 37.2)
      const score = Math.max(10, 75 - deviation * 20)
      data.push(Math.round(score))
    }
  } else data.push(50)

  // 血压指标动态计算 (90-139 mmHg为正常)
  if (obs.systolicBp) {
    const bp = obs.systolicBp
    const optimal = 120 // 最佳收缩压

    if (bp >= 90 && bp <= 139) {
      // 正常范围内，根据与最佳值的距离计算
      const deviation = Math.abs(bp - optimal)
      const score = Math.max(75, 100 - (deviation / 29) * 25)
      data.push(Math.round(score))
    } else if (bp >= 140 && bp <= 179) {
      // 轻度高血压
      const deviation = bp - 139
      const score = Math.max(30, 75 - (deviation / 40) * 45)
      data.push(Math.round(score))
    } else {
      // 严重异常
      const score = bp < 90 ? Math.max(10, 50 - (90 - bp) * 2) : Math.max(10, 30 - (bp - 179) * 0.5)
      data.push(Math.round(score))
    }
  } else data.push(50)

  // 心率指标动态计算 (60-100 bpm为正常)
  if (obs.heartRate) {
    const hr = obs.heartRate
    const optimal = 75 // 最佳心率

    if (hr >= 60 && hr <= 100) {
      // 正常范围内
      const deviation = Math.abs(hr - optimal)
      const score = Math.max(75, 100 - (deviation / 20) * 25)
      data.push(Math.round(score))
    } else if ((hr >= 50 && hr < 60) || (hr > 100 && hr <= 120)) {
      // 轻度异常
      const deviation = hr < 60 ? (60 - hr) : (hr - 100)
      const score = Math.max(40, 75 - (deviation / 20) * 35)
      data.push(Math.round(score))
    } else {
      // 严重异常
      const score = hr < 50 ? Math.max(10, 40 - (50 - hr) * 2) : Math.max(10, 40 - (hr - 120) * 1.5)
      data.push(Math.round(score))
    }
  } else data.push(50)

  // BMI指标动态计算 (18.5-23.9为正常)
  const bmi = calculateBMI()
  if (bmi) {
    const optimal = 21.2 // 最佳BMI

    if (bmi >= 18.5 && bmi <= 23.9) {
      // 正常范围内
      const deviation = Math.abs(bmi - optimal)
      const score = Math.max(75, 100 - (deviation / 2.7) * 25)
      data.push(Math.round(score))
    } else if ((bmi >= 17.0 && bmi < 18.5) || (bmi > 23.9 && bmi <= 27.9)) {
      // 轻度异常
      const deviation = bmi < 18.5 ? (18.5 - bmi) : (bmi - 23.9)
      const score = Math.max(35, 75 - (deviation / 4) * 40)
      data.push(Math.round(score))
    } else {
      // 严重异常
      const deviation = bmi < 17.0 ? (17.0 - bmi) : (bmi - 27.9)
      const score = Math.max(10, 35 - deviation * 5)
      data.push(Math.round(score))
    }
  } else data.push(50)

  // 睡眠质量动态计算 (7-9小时为最佳)
  if (obs.sleepHours) {
    const sleep = obs.sleepHours
    const optimal = 8 // 最佳睡眠时间

    if (sleep >= 7 && sleep <= 9) {
      // 最佳范围内
      const deviation = Math.abs(sleep - optimal)
      const score = Math.max(80, 100 - deviation * 20)
      data.push(Math.round(score))
    } else if ((sleep >= 6 && sleep < 7) || (sleep > 9 && sleep <= 10)) {
      // 可接受范围
      const deviation = sleep < 7 ? (7 - sleep) : (sleep - 9)
      const score = Math.max(50, 80 - deviation * 30)
      data.push(Math.round(score))
    } else {
      // 睡眠不足或过多
      const deviation = sleep < 6 ? (6 - sleep) : (sleep - 10)
      const score = Math.max(15, 50 - deviation * 15)
      data.push(Math.round(score))
    }
  } else data.push(50)

  // 整体状态 - 加权平均，考虑各指标重要性
  const weights = [0.2, 0.25, 0.2, 0.15, 0.2] // 体温、血压、心率、BMI、睡眠的权重
  let weightedSum = 0
  let totalWeight = 0

  data.forEach((score, index) => {
    if (score > 0) { // 只计算有效数据
      weightedSum += score * weights[index]
      totalWeight += weights[index]
    }
  })

  const overallScore = totalWeight > 0 ? weightedSum / totalWeight : 50
  data.push(Math.round(overallScore))

  return data
}

// 初始化画像分析图
const initProfileChart = () => {
  const chartDom = document.getElementById('wordCloudChart')
  if (!chartDom) return

  const myChart = echarts.init(chartDom)

  // 生成画像分析数据
  const generateProfileData = () => {
    const words = []

    // 基于老人基本信息生成词汇
    if (elderlyProfile.value) {
      const profile = elderlyProfile.value
      if (profile.age) {
        if (profile.age >= 80) words.push({ name: '高龄老人', value: 90 })
        else if (profile.age >= 70) words.push({ name: '老年人', value: 80 })
        else words.push({ name: '中老年', value: 70 })
      }

      if (profile.gender === '男') words.push({ name: '男性', value: 60 })
      else if (profile.gender === '女') words.push({ name: '女性', value: 60 })
    }

    // 基于健康状态生成词汇
    if (currentState.value) {
      switch (currentState.value) {
        case '健康':
          words.push({ name: '身体健康', value: 100 })
          words.push({ name: '状态良好', value: 85 })
          break
        case '注意':
          words.push({ name: '需要关注', value: 95 })
          words.push({ name: '轻微异常', value: 80 })
          break
        case '异常':
          words.push({ name: '健康异常', value: 100 })
          words.push({ name: '需要治疗', value: 90 })
          break
      }
    }

    // 基于慢性疾病生成词汇
    if (chronicDiseases.value?.length) {
      chronicDiseases.value.forEach(disease => {
        words.push({ name: disease.diseaseName, value: 75 })

        switch (disease.diseaseCategory) {
          case 'A':
          case '重病':
            words.push({ name: '重症患者', value: 85 })
            break
          case 'B':
          case '中度病':
            words.push({ name: '慢性病', value: 70 })
            break
          case 'C':
          case '轻病':
            words.push({ name: '轻症', value: 55 })
            break
        }
      })
    }

    // 基于生理指标生成词汇
    if (observationData.value) {
      const obs = observationData.value

      // 体温相关
      if (obs.bodyTemperature) {
        if (obs.bodyTemperature > 37.2) words.push({ name: '发热', value: 80 })
        else words.push({ name: '体温正常', value: 65 })
      }

      // 血压相关
      if (obs.systolicBp) {
        if (obs.systolicBp > 140) words.push({ name: '高血压', value: 85 })
        else if (obs.systolicBp < 90) words.push({ name: '低血压', value: 75 })
        else words.push({ name: '血压正常', value: 60 })
      }

      // 心率相关
      if (obs.heartRate) {
        if (obs.heartRate > 100) words.push({ name: '心动过速', value: 80 })
        else if (obs.heartRate < 60) words.push({ name: '心动过缓', value: 75 })
        else words.push({ name: '心率正常', value: 55 })
      }

      // BMI相关
      const bmi = calculateBMI()
      if (bmi) {
        const bmiValue = parseFloat(bmi)
        if (bmiValue > 24) words.push({ name: '超重', value: 70 })
        else if (bmiValue < 18.5) words.push({ name: '偏瘦', value: 65 })
        else words.push({ name: '体重正常', value: 50 })
      }

      // 睡眠相关
      if (obs.sleepHours) {
        if (obs.sleepHours < 6) words.push({ name: '睡眠不足', value: 75 })
        else if (obs.sleepHours > 9) words.push({ name: '睡眠过多', value: 60 })
        else words.push({ name: '睡眠良好', value: 50 })
      }

      // 咳嗽相关
      if (obs.cough) words.push({ name: '咳嗽症状', value: 70 })
    }

    // 添加一些通用的老人画像词汇
    words.push(
        { name: '居家养老', value: 45 },
        { name: '定期体检', value: 40 },
        { name: '健康监测', value: 50 },
        { name: '医疗保健', value: 55 },
        { name: '生活照料', value: 35 },
        { name: '康复护理', value: 40 }
    )

    return words
  }

  const option = {
    title: {
      text: '老人健康画像分析',
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#333'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: function(params) {
        return `${params.name}: ${params.value} (${params.percent}%)`
      }
    },
    series: [{
      type: 'pie',
      radius: ['20%', '70%'],
      center: ['50%', '50%'],
      roseType: 'area',
      itemStyle: {
        borderRadius: 8
      },
      label: {
        show: true,
        position: 'outside',
        formatter: '{b}',
        fontSize: 12,
        fontWeight: 'bold'
      },
      labelLine: {
        show: true,
        length: 10,
        length2: 20
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      },
      data: generateProfileData().map(item => ({
        name: item.name,
        value: item.value
      }))
    }]
  }

  myChart.setOption(option)

  // 响应式处理
  window.addEventListener('resize', () => {
    myChart.resize()
  })
}

// 工具函数
const getTemperatureClass = (temp) => {
  if (!temp) return ''
  if (temp >= 36.0 && temp <= 37.2) return 'normal'
  if (temp > 37.2 && temp <= 38.0) return 'warning'
  return 'danger'
}

const getBloodPressureClass = (bp) => {
  if (!bp) return ''
  if (bp >= 90 && bp <= 139) return 'normal'
  if (bp >= 140 && bp <= 159) return 'warning'
  return 'danger'
}

const getHeartRateClass = (hr) => {
  if (!hr) return ''
  if (hr >= 60 && hr <= 100) return 'normal'
  if (hr > 100 && hr <= 120) return 'warning'
  return 'danger'
}

const getBMIClass = (bmi) => {
  if (!bmi) return ''
  const bmiValue = parseFloat(bmi)
  if (bmiValue >= 18.5 && bmiValue <= 23.9) return 'normal'
  if (bmiValue >= 24.0 && bmiValue <= 27.9) return 'warning'
  return 'danger'
}

const getDiseaseTagType = (type) => {
  switch (type) {
    case 'A':
    case '重病':
      return 'danger'
    case 'B':
    case '中度病':
      return 'warning'
    case 'C':
    case '轻病':
      return 'info'
    default:
      return ''
  }
}

const getStateTagType = (state) => {
  switch (state) {
    case '健康':
      return 'success'
    case '注意':
      return 'warning'
    case '异常':
      return 'danger'
    default:
      return 'info'
  }
}

const getProgressColor = (state) => {
  switch (state) {
    case '健康':
      return '#67c23a'
    case '注意':
      return '#e6a23c'
    case '异常':
      return '#f56c6c'
    default:
      return '#909399'
  }
}

// 事件处理
const goBack = () => {
  router.go(-1)
}

const refreshPrediction = () => {
  performPrediction()
}
</script>

<style scoped>
.elderly-profile-analysis {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-header h1 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.upper-section {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.health-info-card {
  flex: 1;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.radar-chart-card {
  flex: 1;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.card-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.observation-time {
  color: #666;
  font-size: 14px;
}

.health-info-content {
  padding: 20px;
}

.patient-info {
  margin-bottom: 20px;
}

.info-row {
  display: flex;
  margin-bottom: 8px;
}

.label {
  width: 80px;
  color: #666;
  font-weight: 500;
}

.value {
  color: #333;
}

.vital-signs {
  margin-bottom: 20px;
}

.vital-signs h4 {
  margin: 0 0 15px 0;
  color: #333;
  font-size: 16px;
}

.signs-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
}

.sign-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 6px;
}

.sign-label {
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
}

.sign-value {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.sign-value.normal {
  color: #67c23a;
}

.sign-value.warning {
  color: #e6a23c;
}

.sign-value.danger {
  color: #f56c6c;
}

.disease-history h4 {
  margin: 0 0 15px 0;
  color: #333;
  font-size: 16px;
}

.disease-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.disease-tag {
  margin: 0;
}

.chart-container {
  padding: 20px;
}

.profile-chart-section {
  margin: 20px 0;
}

.profile-chart-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.lower-section {
  margin-top: 20px;
}

.prediction-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.prediction-content {
  padding: 20px;
}

.current-state {
  display: flex;
  gap: 30px;
  margin-bottom: 30px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.state-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.state-label {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.state-tag {
  font-size: 14px;
}

.scores-section h4 {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 16px;
}

.scores-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.score-item {
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
}

.score-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.score-state {
  font-weight: 500;
  color: #333;
}

.score-value {
  font-weight: 600;
  color: #409eff;
}
</style>