<template>
  <div class="elderly-dashboard">
    <!-- 顶部统计信息 -->
    <div class="statistics">
      <div class="stat-card" v-if="!isElderly">
        <div id="locationMap" style="width: 100%; height: 300px;"></div>
      </div>
      <div class="stat-card" :class="{ 'full-width': isElderly }">
        <div id="observationStats" style="width: 100%; height: 300px;"></div>
      </div>
    </div>

    <!-- 体检记录展示和检索功能 -->
    <div class="observations">
      <div class="table-container">
        <div class="search-section">
          <div class="search-row">
            <template v-if="!isElderly">
              <el-input v-model="searchTerm" placeholder="搜索老人ID、姓名或地点" style="width: 250px;" clearable @keyup.enter="filterObservations" />
              <el-button type="primary" @click="filterObservations">搜索</el-button>
              <el-button @click="resetSearch">重置</el-button>
              <el-button type="success" @click="openImportDialog">添加新记录</el-button>
              <el-button type="warning" @click="showBindDialog">
                <el-icon><Link /></el-icon>
                设备绑定
              </el-button>
            </template>
          </div>
        </div>

        <el-table :data="filteredObservations" style="width: 100%" stripe border>
        <el-table-column label="老人ID" prop="elderlyId" width="80" align="center">
          <template #default="scope">
            {{ scope.row.elderlyId || scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column label="体检时间" prop="observationTime" width="120" align="center">
          <template #default="scope">
            <div style="font-size: 12px; line-height: 1.2;">
              {{ scope.row.observationTime }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="体温(°C)" prop="bodyTemperature" width="100" align="center">
          <template #default="{ row }">
            <div v-if="row.bodyTemperature" class="health-indicator">
              <span class="indicator-value">{{ row.bodyTemperature }}°C</span>
              <span :class="['health-status-tag', 'status-' + getTemperatureStatus(row.bodyTemperature).type]">
                {{ getTemperatureStatus(row.bodyTemperature).text }}
              </span>
            </div>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="血压(mmHg)" prop="systolicBp" width="110" align="center">
          <template #default="{ row }">
            <div v-if="row.systolicBp" class="health-indicator">
              <span class="indicator-value">{{ row.systolicBp }}</span>
              <span :class="['health-status-tag', 'status-' + getBloodPressureStatus(row.systolicBp).type]">
                {{ getBloodPressureStatus(row.systolicBp).text }}
              </span>
            </div>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="心率(次/分)" prop="heartRate" width="110" align="center">
          <template #default="{ row }">
            <div v-if="row.heartRate" class="health-indicator">
              <span class="indicator-value">{{ row.heartRate }}</span>
              <span :class="['health-status-tag', 'status-' + getHeartRateStatus(row.heartRate).type]">
                {{ getHeartRateStatus(row.heartRate).text }}
              </span>
            </div>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="身高(cm)" prop="height" width="90" align="center">
          <template #default="scope">
            <span v-if="scope.row.height">{{ scope.row.height }}</span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="体重(kg)" prop="weight" width="90" align="center">
          <template #default="scope">
            <span v-if="scope.row.weight">{{ scope.row.weight }}</span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="BMI指数" width="100" align="center">
          <template #default="scope">
            <div v-if="scope.row.height && scope.row.weight" class="health-indicator">
              <span class="indicator-value">{{ calculateBMI(scope.row.height, scope.row.weight) }}</span>
              <span :class="['health-status-tag', 'status-' + getBMIStatus(calculateBMI(scope.row.height, scope.row.weight)).type]">
                {{ getBMIStatus(calculateBMI(scope.row.height, scope.row.weight)).text }}
              </span>
            </div>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="睡眠时长(h)" prop="sleepHours" width="110" align="center">
          <template #default="scope">
            <span v-if="scope.row.sleepHours">{{ scope.row.sleepHours }}h</span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="观察地点" prop="observationLocation" width="100" align="center"></el-table-column>
        <el-table-column label="健康状态" width="120" align="center">
          <template #default="scope">
            <span :class="['health-status-tag', 'status-' + getOverallHealthStatus(scope.row).type]">
              {{ getOverallHealthStatus(scope.row).text }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="scope">
            <el-button @click="viewDetails(scope.row)" type="primary" size="small" style="margin-right: 8px;">查看详情</el-button>
            <el-button v-if="!isElderly" @click="deleteRecord(scope.row.id)" type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      </div>
    </div>

    <!-- 添加健康记录对话框 -->
    <el-dialog v-model="importDialogVisible" title="添加健康记录" width="600px">
      <el-form :model="importForm" :rules="importRules" ref="importFormRef" label-width="120px">
        <el-form-item label="选择老人" prop="elderlyId">
          <el-select v-model="importForm.elderlyId" placeholder="请选择老人" filterable>
            <el-option 
              v-for="elderly in elderlyList" 
              :key="elderly.id" 
              :label="`${elderly.name} (ID: ${elderly.id})`" 
              :value="elderly.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="老人姓名" prop="elderlyName">
          <el-input v-model="importForm.elderlyName" placeholder="请输入老人姓名" />
        </el-form-item>

        <el-form-item label="是否咳嗽" prop="cough">
          <el-switch
              v-model="importForm.cough"
              active-text="是"
              inactive-text="否"
              :active-value="true"
              :inactive-value="false"
          />
        </el-form-item>
        
        <el-form-item label="体检时间" prop="observationDate">
          <el-date-picker
            v-model="importForm.observationDate"
            type="datetime"
            placeholder="请选择体检时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="体检地点" prop="observationLocation">
          <el-select v-model="importForm.observationLocation" placeholder="请选择体检地点" style="width: 100%">
            <el-option label="北京市" value="北京市" />
            <el-option label="上海市" value="上海市" />
            <el-option label="广东省" value="广东省" />
            <el-option label="浙江省" value="浙江省" />
            <el-option label="江苏省" value="江苏省" />
            <el-option label="山东省" value="山东省" />
            <el-option label="河南省" value="河南省" />
            <el-option label="四川省" value="四川省" />
            <el-option label="湖北省" value="湖北省" />
            <el-option label="湖南省" value="湖南省" />
            <el-option label="河北省" value="河北省" />
            <el-option label="福建省" value="福建省" />
            <el-option label="安徽省" value="安徽省" />
            <el-option label="辽宁省" value="辽宁省" />
            <el-option label="陕西省" value="陕西省" />
            <el-option label="江西省" value="江西省" />
            <el-option label="重庆市" value="重庆市" />
            <el-option label="天津市" value="天津市" />
            <el-option label="云南省" value="云南省" />
            <el-option label="山西省" value="山西省" />
            <el-option label="广西壮族自治区" value="广西壮族自治区" />
            <el-option label="黑龙江省" value="黑龙江省" />
            <el-option label="吉林省" value="吉林省" />
            <el-option label="贵州省" value="贵州省" />
            <el-option label="甘肃省" value="甘肃省" />
            <el-option label="内蒙古自治区" value="内蒙古自治区" />
            <el-option label="新疆维吾尔自治区" value="新疆维吾尔自治区" />
            <el-option label="海南省" value="海南省" />
            <el-option label="宁夏回族自治区" value="宁夏回族自治区" />
            <el-option label="青海省" value="青海省" />
            <el-option label="西藏自治区" value="西藏自治区" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="血压" prop="bloodPressure">
          <el-input v-model="importForm.bloodPressure" placeholder="请输入血压值，如：120/80" />
        </el-form-item>
        
        <el-form-item label="心率" prop="heartRate">
          <el-input-number v-model="importForm.heartRate" :min="0" :max="200" placeholder="请输入心率" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="体重" prop="weight">
          <el-input-number v-model="importForm.weight" :min="0" :precision="1" placeholder="请输入体重(kg)" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="身高" prop="height">
          <el-input-number v-model="importForm.height" :min="0" :precision="1" placeholder="请输入身高(cm)" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="睡眠时长" prop="sleepHours">
          <el-input-number v-model="importForm.sleepHours" :min="0" :precision="1" placeholder="请输入睡眠时长(h)" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="体温" prop="temperature">
          <el-input-number v-model="importForm.temperature" :min="35" :max="42" :precision="1" placeholder="请输入体温(°C)" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="备注" prop="notes">
          <el-input v-model="importForm.notes" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeImportDialog">取消</el-button>
          <el-button type="primary" @click="handleImport" :loading="importLoading">
            确认添加
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 设备绑定对话框 -->
    <el-dialog v-model="bindDialogVisible" title="设备绑定" width="500px">
      <el-form :model="bindForm" :rules="bindRules" ref="bindFormRef" label-width="120px">
        <el-form-item label="设备MAC地址" prop="macid">
          <el-input v-model="bindForm.macid" placeholder="请输入设备MAC地址" />
        </el-form-item>
        
        <el-form-item label="选择老人" prop="elderlyId">
          <el-select v-model="bindForm.elderlyId" placeholder="请选择老人" filterable style="width: 100%">
            <el-option 
              v-for="elderly in elderlyList" 
              :key="elderly.id" 
              :label="`${elderly.name} (ID: ${elderly.id})`" 
              :value="elderly.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="resetBindForm">取消</el-button>
          <el-button type="primary" @click="bindDevice" :loading="bindLoading">
            确认绑定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import * as echarts from 'echarts';
import {
  deleteObservation,
  getAllObservations,
  getObservationsByElderlyId,
  addObservation
} from '@/api/elderlyObservations';
import { getAllElderlyProfiles, getChronicDiseasesByElderlyId } from '@/api/elderlyProfile';
import { ElMessage, ElMessageBox } from 'element-plus';

const router = useRouter();
const userStore = useUserStore();
const isElderly = userStore.isElderly;
const currentUserId = userStore.userId;

const searchTerm = ref('');
const observations = ref([]);
const filteredObservations = ref([]);
const locationStats = ref([]);
const elderlyList = ref([]);

// 添加记录功能相关变量
const importDialogVisible = ref(false);
const importLoading = ref(false);
const importFormRef = ref(null);

// 设备绑定功能相关变量
const bindDialogVisible = ref(false);
const bindLoading = ref(false);
const bindFormRef = ref(null);
const deviceBindings = ref([]);

// 表单数据
const importForm = ref({
  elderlyId: '',
  elderlyName: '',
  observationDate: '',
  observationLocation: '',
  bloodPressure: '',
  heartRate: null,
  weight: null,
  height: null,
  sleepHours: 0,
  temperature: null,
  cough:false,
  notes: ''
});

// 设备绑定表单数据
const bindForm = ref({
  macid: '',
  elderlyId: ''
});

// 日期时间格式化函数，避免时区转换问题
const formatDateToLocal = (dateTime) => {
  if (!dateTime) return '';
  // 如果已经是字符串格式，直接返回
  if (typeof dateTime === 'string') {
    return dateTime;
  }
  // 如果是Date对象，格式化为本地时间字符串
  const d = new Date(dateTime);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  const hours = String(d.getHours()).padStart(2, '0');
  const minutes = String(d.getMinutes()).padStart(2, '0');
  const seconds = String(d.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

// 表单验证规则
const importRules = ref({
  elderlyId: [
    { required: true, message: '请输入老人ID', trigger: 'blur' }
  ],
  elderlyName: [
    { required: true, message: '请输入老人姓名', trigger: 'blur' }
  ],
  observationDate: [
    { required: true, message: '请选择体检日期', trigger: 'change' }
  ],
  observationLocation: [
    { required: true, message: '请选择体检地点', trigger: 'change' }
  ],
  sleepHours: [
    { required: true, message: '请输入睡眠时长', trigger: 'blur' }
  ]
});

// 设备绑定验证规则
const bindRules = ref({
  macid: [
    { required: true, message: '请输入设备MAC地址', trigger: 'blur' }
  ],
  elderlyId: [
    { required: true, message: '请选择老人', trigger: 'change' }
  ]
});

const fetchObservations = async () => {
  try {
    let response;
    if (isElderly) {
      // 老人用户只能查看自己的记录
      response = await getObservationsByElderlyId(currentUserId);
    } else {
      // 管理员和医生可以查看所有记录
      response = await getAllObservations();
    }
    
    if (response.code === 200) {
      observations.value = response.data;
      filteredObservations.value = observations.value;
      console.log(filteredObservations);
      // 重新渲染健康状态统计图表
      renderObservationStats();
    } else {
      ElMessage.error('获取体检记录失败：' + response.msg);
    }
  } catch (error) {
    console.error('获取体检记录失败：', error);
    ElMessage.error('获取体检记录失败：' + error.message);
  }
};

const filterObservations = () => {
  const term = searchTerm.value.trim().toLowerCase();
  if (!term) {
    filteredObservations.value = observations.value;
  } else {
    filteredObservations.value = observations.value.filter(item => {
      return (item.elderlyId && item.elderlyId.toString().toLowerCase().includes(term)) ||
             (item.elderlyName && item.elderlyName.toLowerCase().includes(term)) ||
             (item.observationLocation && item.observationLocation.toLowerCase().includes(term));
    });
  }
  // 重新渲染健康状态统计图表
  renderObservationStats();
};

watch(searchTerm, filterObservations);

// 加载老人列表
const loadElderlyList = async () => {
  try {
    const response = await getAllElderlyProfiles();
    elderlyList.value = response.data || [];
  } catch (error) {
    console.error('加载老人列表失败:', error);
    ElMessage.error('加载老人列表失败');
  }
};

onMounted(() => {
  fetchObservations();
  if (!isElderly) {
    loadElderlyList();
    renderLocationMap();
  }
  renderObservationStats();
});

const renderLocationMap = async () => {
  if (isElderly) return;
  
  const response = await getAllObservations();
  const observations = response.data;

  const provinceCount = {};
  observations.forEach(observation => {
    const location = observation.observationLocation;
    if (location) {
      if (!provinceCount[location]) {
        provinceCount[location] = 0;
      }
      provinceCount[location] += 1;
    }
  });

  const mapData = Object.entries(provinceCount).map(([name, value]) => ({ name, value }));

  fetch('https://geo.datav.aliyun.com/areas/bound/100000_full.json')
      .then(res => res.json())
      .then(geoJson => {
        echarts.registerMap('china', geoJson);

        const mapChart = echarts.init(document.getElementById('locationMap'));
        const mapOption = {
          title: { text: '省份数据分布', left: 'center' },
          tooltip: { trigger: 'item' },
          visualMap: {
            min: 0,
            max: Math.max(...Object.values(provinceCount)),
            left: 'left',
            top: 'bottom',
            text: ['多', '少'],
            calculable: true,
          },
          series: [{
            name: '数量',
            type: 'map',
            map: 'china',
            roam: true,
            data: mapData
          }]
        };
        mapChart.setOption(mapOption);
      });
};

const renderObservationStats = () => {
  const chartContainer = document.getElementById('observationStats');
  if (!chartContainer) return;
  
  const myChart = echarts.init(chartContainer);
  
  // 设置图表标题
  const title = isElderly ? '我的健康状态统计' : '全部老人健康状态统计';
  
  // 统计健康状态分布
  const healthStats = {
    healthy: 0,    // 健康
    attention: 0,  // 注意
    abnormal: 0    // 异常
  };
  
  // 遍历所有体检记录，统计健康状态
  filteredObservations.value.forEach(record => {
    const healthStatus = getOverallHealthStatus(record);
    if (healthStatus.type === 'success') {
      healthStats.healthy++;
    } else if (healthStatus.type === 'warning') {
      healthStats.attention++;
    } else if (healthStatus.type === 'danger') {
      healthStats.abnormal++;
    }
  });
  
  const option = {
    title: {
      text: title,
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#333'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'horizontal',
      bottom: 'bottom',
      data: ['健康', '注意', '异常']
    },
    series: [
      {
        name: '健康状态',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '50%'],
        roseType: 'radius',
        data: [
          { value: healthStats.healthy, name: '健康', itemStyle: { color: '#67c23a' } },
          { value: healthStats.attention, name: '注意', itemStyle: { color: '#e6a23c' } },
          { value: healthStats.abnormal, name: '异常', itemStyle: { color: '#f56c6c' } }
        ].filter(item => item.value > 0),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  
  // 如果没有数据，显示提示
  if (healthStats.healthy === 0 && healthStats.attention === 0 && healthStats.abnormal === 0) {
    option.series[0].data = [{ value: 1, name: '暂无数据', itemStyle: { color: '#ddd' } }];
  }
  
  myChart.setOption(option);
};

const viewDetails = async (row) => {
  try {
    // 检查权限：老人用户只能查看自己的记录
    if (isElderly && row.elderlyId !== currentUserId) {
      ElMessage.error('您只能查看自己的健康记录详情')
      return
    }

    // 获取老人档案信息
    const elderlyProfileResponse = await getAllElderlyProfiles()
    const elderlyProfile = elderlyProfileResponse.data?.find(profile => profile.id === row.elderlyId)
    
    if (!elderlyProfile) {
      ElMessage.error('未找到老人档案信息')
      return
    }
    
    // 获取老人疾病信息
    const chronicDiseasesResponse = await getChronicDiseasesByElderlyId(row.elderlyId)
    const chronicDiseases = chronicDiseasesResponse.data || []
    
    // 跳转到老人画像分析页面，传递数据
    router.push({
      name: 'ElderlyProfileAnalysis',
      params: {
        id: row.elderlyId
      },
      query: {
        observationData: encodeURIComponent(JSON.stringify(row)),
        elderlyProfile: encodeURIComponent(JSON.stringify(elderlyProfile)),
        chronicDiseases: encodeURIComponent(JSON.stringify(chronicDiseases))
      }
    })
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败，请重试')
  }
}

const editRecord = (record) => {
};

const resetSearch = () => {
  searchTerm.value = '';
  filteredObservations.value = observations.value;
  // 重新渲染健康状态统计图表
  renderObservationStats();
};

const openImportDialog = () => {
  importDialogVisible.value = true;
};

const closeImportDialog = () => {
  importDialogVisible.value = false;
  // 重置表单
  if (importFormRef.value) {
    importFormRef.value.resetFields();
  }
  importForm.value = {
    elderlyId: '',
    elderlyName: '',
    observationDate: '',
    observationLocation: '',
    bloodPressure: '',
    heartRate: null,
    weight: null,
    height: null,
    sleepHours: 0,
    temperature: null,
    cough: false,
    notes: ''
  };
};

// 执行添加
const handleImport = async () => {
  if (!importFormRef.value) return;
  
  try {
    // 验证表单
    await importFormRef.value.validate();
    
    importLoading.value = true;
    
    // 格式化数据以匹配后端实体类字段
    const formData = {
      elderlyId: importForm.value.elderlyId,
      observationTime: importForm.value.observationDate ? 
        formatDateToLocal(importForm.value.observationDate) : '',
      observationLocation: importForm.value.observationLocation,
      systolicBp: importForm.value.bloodPressure,
      heartRate: importForm.value.heartRate,
      bodyTemperature: importForm.value.temperature,
      height: importForm.value.height,
      weight: importForm.value.weight,
      sleepHours: importForm.value.sleepHours,
      cough: importForm.value.cough,
      notes: importForm.value.notes
    };
    console.log('原始日期:', importForm.value.observationDate);
    console.log('格式化后日期:', formData.observationTime);
    console.log(formData.sleepHours)
    
    const response = await addObservation(formData);
    
    if (response.code === 200) {
      ElMessage.success('健康记录添加成功！');
      closeImportDialog();
      await fetchObservations(); // 重新加载数据
    } else {
      ElMessage.error(response.message || '添加失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('添加失败:', error);
      ElMessage.error('添加失败，请检查输入信息');
    }
  } finally {
    importLoading.value = false;
  }
};

const deleteRecord = async (id) => {
  const response = await deleteObservation(id);
  if (response.data) {
    await fetchObservations(); // 重新加载体检记录
  }
};

// 计算BMI指数
const calculateBMI = (height, weight) => {
  if (!height || !weight) return 0;
  const heightInMeters = height / 100;
  const bmi = weight / (heightInMeters * heightInMeters);
  return bmi.toFixed(1);
};

// 获取体温状态
const getTemperatureStatus = (temperature) => {
  if (temperature < 36.0) {
    return { type: 'info', text: '偏低', class: 'temp-low' };
  } else if (temperature >= 36.0 && temperature <= 37.2) {
    return { type: 'success', text: '正常', class: 'temp-normal' };
  } else if (temperature > 37.2 && temperature <= 38.0) {
    return { type: 'warning', text: '偏高', class: 'temp-high' };
  } else {
    return { type: 'danger', text: '发热', class: 'temp-fever' };
  }
};

// 获取血压状态
const getBloodPressureStatus = (systolicBp) => {
  if (systolicBp < 90) {
    return { type: 'info', text: '偏低', class: 'bp-low' };
  } else if (systolicBp >= 90 && systolicBp <= 139) {
    return { type: 'success', text: '正常', class: 'bp-normal' };
  } else if (systolicBp >= 140 && systolicBp <= 159) {
    return { type: 'warning', text: '偏高', class: 'bp-high' };
  } else {
    return { type: 'danger', text: '高压', class: 'bp-danger' };
  }
};

// 获取心率状态
const getHeartRateStatus = (heartRate) => {
  if (heartRate < 60) {
    return { type: 'info', text: '偏低', class: 'hr-low' };
  } else if (heartRate >= 60 && heartRate <= 100) {
    return { type: 'success', text: '正常', class: 'hr-normal' };
  } else if (heartRate > 100 && heartRate <= 120) {
    return { type: 'warning', text: '偏高', class: 'hr-high' };
  } else {
    return { type: 'danger', text: '过快', class: 'hr-danger' };
  }
};

// 获取BMI状态
const getBMIStatus = (bmi) => {
  const bmiValue = parseFloat(bmi);
  if (bmiValue < 18.5) {
    return { type: 'info', text: '偏瘦', class: 'bmi-low' };
  } else if (bmiValue >= 18.5 && bmiValue <= 23.9) {
    return { type: 'success', text: '正常', class: 'bmi-normal' };
  } else if (bmiValue >= 24.0 && bmiValue <= 27.9) {
    return { type: 'warning', text: '偏胖', class: 'bmi-high' };
  } else {
    return { type: 'danger', text: '肥胖', class: 'bmi-danger' };
  }
};

// 获取整体健康状态
const getOverallHealthStatus = (row) => {
  let abnormalCount = 0;
  
  // 检查体温
  if (row.bodyTemperature) {
    const tempStatus = getTemperatureStatus(row.bodyTemperature);
    if (tempStatus.type !== 'success') abnormalCount++;
  }
  
  // 检查血压
  if (row.systolicBp) {
    const bpStatus = getBloodPressureStatus(row.systolicBp);
    if (bpStatus.type !== 'success') abnormalCount++;
  }
  
  // 检查心率
  if (row.heartRate) {
    const hrStatus = getHeartRateStatus(row.heartRate);
    if (hrStatus.type !== 'success') abnormalCount++;
  }
  
  // 检查BMI
  if (row.height && row.weight) {
    const bmi = calculateBMI(row.height, row.weight);
    const bmiStatus = getBMIStatus(bmi);
    if (bmiStatus.type !== 'success') abnormalCount++;
  }
  
  if (abnormalCount === 0) {
    return { type: 'success', text: '健康' };
  } else if (abnormalCount === 1) {
    return { type: 'warning', text: '注意' };
  } else {
    return { type: 'danger', text: '异常' };
  }
};

// 设备绑定相关函数
const showBindDialog = () => {
  bindDialogVisible.value = true;
};

const resetBindForm = () => {
  bindDialogVisible.value = false;
  bindForm.value = {
    macid: '',
    elderlyId: ''
  };
  if (bindFormRef.value) {
    bindFormRef.value.resetFields();
  }
};

const bindDevice = async () => {
  try {
    const valid = await bindFormRef.value.validate();
    if (!valid) return;
    
    bindLoading.value = true;
    
    // 调用绑定设备API
    const response = await fetch('/api/gps/bind', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        macid: bindForm.value.macid,
        elderlyId: bindForm.value.elderlyId
      })
    });
    
    const result = await response.json();
    
    if (result.success === true) {
      ElMessage.success(result.message || '设备绑定成功！');
      resetBindForm();
      // 可以在这里刷新设备绑定列表
      // await loadDeviceBindings();
    } else {
      ElMessage.error(result.message || '绑定失败');
    }
  } catch (error) {
    console.error('设备绑定失败:', error);
    ElMessage.error('绑定失败，请检查网络连接');
  } finally {
    bindLoading.value = false;
  }
};
</script>

<style scoped>
.elderly-dashboard {
  padding: 24px;
  background-color: #f8f9fa;
  min-height: 100vh;
}

.statistics {
  max-width: 1400px;
  margin: 0 auto 40px auto;
  padding: 0 24px;
  display: flex;
  justify-content: space-between;
  gap: 24px;
}

.stat-card {
  flex: 1;
  padding: 24px;
  border: none;
  border-radius: 16px;
  background: #fff;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

.stat-card.full-width {
  flex: 1;
  max-width: 1000px;
  margin: 0 auto;
}

/* 健康状态标签样式 */
.health-status-tag {
  font-size: 16px;
  padding: 6px 16px;
  border-radius: 20px;
  font-weight: 600;
  display: inline-block;
  min-width: 80px;
  text-align: center;
  margin-top: 6px;
}

.status-success {
  background-color: #e3f3e6;
  color: #2c7a34;
  border: none;
}

.status-warning {
  background-color: #fff6e5;
  color: #b45309;
  border: none;
}

.status-danger {
  background-color: #fee2e2;
  color: #b91c1c;
  border: none;
}

.status-info {
  background-color: #e0f2fe;
  color: #0369a1;
  border: none;
}

/* 健康指标列样式 */
.health-indicator {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 8px 0;
}

.indicator-value {
  font-weight: 600;
  font-size: 18px;
  color: #1f2937;
}

.text-muted {
  color: #6b7280;
  font-size: 16px;
}

/* 表格样式优化 */
.table-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
  border-radius: 16px;
  background: #fff;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.el-table {
  --el-table-border-color: #e5e7eb;
  --el-table-header-bg-color: #f3f4f6;
  --el-table-row-hover-bg-color: #f9fafb;
  border-radius: 12px;
  overflow: hidden;
}

.el-table th {
  background-color: var(--el-table-header-bg-color) !important;
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  padding: 20px 16px;
  height: 60px;
}

.el-table td {
  font-size: 16px;
  color: #4b5563;
  padding: 20px 16px;
  height: 60px;
}

/* 搜索区域样式 */
.search-section {
  padding: 24px 0;
  border-bottom: 1px solid #e5e7eb;
  margin-bottom: 24px;
}

.search-row {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

/* 按钮样式优化 */
.el-button {
  height: 44px;
  padding: 0 24px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.el-button--primary {
  background-color: #2563eb;
  border-color: #2563eb;
}

.el-button--primary:hover {
  background-color: #1d4ed8;
  border-color: #1d4ed8;
  transform: translateY(-2px);
}

.el-button--success {
  background-color: #059669;
  border-color: #059669;
}

.el-button--success:hover {
  background-color: #047857;
  border-color: #047857;
  transform: translateY(-2px);
}

.el-button--danger {
  background-color: #dc2626;
  border-color: #dc2626;
}

.el-button--danger:hover {
  background-color: #b91c1c;
  border-color: #b91c1c;
  transform: translateY(-2px);
}

/* 表单控件样式 */
.el-input, .el-select, .el-date-picker {
  --el-input-height: 44px;
  font-size: 16px;
}

.el-input__inner {
  height: 44px !important;
  line-height: 44px !important;
  padding: 0 16px !important;
  border-radius: 8px !important;
  font-size: 16px !important;
}

.el-input__wrapper {
  border-radius: 8px !important;
}

/* 对话框样式 */
.el-dialog {
  border-radius: 16px;
  overflow: hidden;
}

.el-dialog__header {
  padding: 24px;
  background-color: #f8f9fa;
  margin-right: 0;
}

.el-dialog__title {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
}

.el-dialog__body {
  padding: 24px;
}

.el-form-item__label {
  font-size: 16px;
  font-weight: 500;
  color: #374151;
  padding-bottom: 8px;
}

.dialog-footer {
  padding: 20px 24px;
  background-color: #f8f9fa;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 适配移动设备 */
@media screen and (max-width: 768px) {
  .statistics {
    flex-direction: column;
  }

  .search-row {
    flex-direction: column;
    align-items: stretch;
  }

  .el-button {
    width: 100%;
  }

  .el-input {
    width: 100% !important;
  }
}
</style>



