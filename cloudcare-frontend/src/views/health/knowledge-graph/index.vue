<template>
  <div class="knowledge-graph-page">
    <!-- 头部工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <h2 class="page-title">
          <i class="el-icon-share"></i>
          医疗知识图谱
        </h2>
      </div>
      <div class="toolbar-right">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索疾病、症状或用药"
          prefix-icon="el-icon-search"
          class="search-input"
          @input="handleSearch"
          clearable
        />
        <el-button type="primary" @click="resetGraph">
          <i class="el-icon-refresh"></i>
          重置视图
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧控制面板 -->
      <div class="control-panel">
        <el-card class="panel-card">
          <template #header>
            <span class="panel-title">图谱控制</span>
          </template>
          
          <!-- 节点类型筛选 -->
          <div class="control-section">
            <h4>节点类型</h4>
            <el-checkbox-group v-model="selectedCategories" @change="handleCategoryChange">
              <el-checkbox label="疾病">疾病</el-checkbox>
              <el-checkbox label="症状">症状</el-checkbox>
              <el-checkbox label="用药">用药</el-checkbox>
            </el-checkbox-group>
          </div>

          <!-- 布局控制 -->
          <div class="control-section">
            <h4>布局设置</h4>
            <el-select v-model="layoutType" @change="handleLayoutChange" placeholder="选择布局">
              <el-option label="力导向布局" value="force"></el-option>
              <el-option label="圆形布局" value="circular"></el-option>
              <el-option label="网格布局" value="grid"></el-option>
              <el-option label="层次布局" value="hierarchical"></el-option>
              <el-option label="径向布局" value="radial"></el-option>
              <el-option label="树形布局" value="tree"></el-option>
              <el-option label="同心圆布局" value="concentric"></el-option>
            </el-select>
          </div>

          <!-- 图谱统计 -->
          <div class="control-section">
            <h4>图谱统计</h4>
            <div class="stats">
              <div class="stat-item">
                <span class="stat-label">疾病节点：</span>
                <span class="stat-value">{{ diseaseCount }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">症状节点：</span>
                <span class="stat-value">{{ symptomCount }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">用药节点：</span>
                <span class="stat-value">{{ medicineCount }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">关系数量：</span>
                <span class="stat-value">{{ linkCount }}</span>
              </div>
            </div>
          </div>

          <!-- 节点管理 -->
          <div class="control-section">
            <h4>节点管理</h4>
            <div class="management-buttons">
              <el-button type="primary" size="small" @click="openAddNodeDialog">
                <i class="el-icon-plus"></i>
                添加节点
              </el-button>
              <el-tooltip content="请先点击图谱中的节点进行选择" placement="top" :disabled="!!selectedNode">
                <el-button type="warning" size="small" @click="openEditNodeDialog" :disabled="!selectedNode">
                  <i class="el-icon-edit"></i>
                  编辑节点
                </el-button>
              </el-tooltip>
              <el-tooltip content="请先点击图谱中的节点进行选择" placement="top" :disabled="!!selectedNode">
                <el-button type="danger" size="small" @click="deleteSelectedNode" :disabled="!selectedNode">
                  <i class="el-icon-delete"></i>
                  删除节点
                </el-button>
              </el-tooltip>
            </div>
          </div>

          <!-- 关系管理 -->
          <div class="control-section">
            <h4>关系管理</h4>
            <div class="management-buttons">
              <el-button type="primary" size="small" @click="openAddRelationDialog">
                <i class="el-icon-connection"></i>
                添加关系
              </el-button>
              <el-button type="danger" size="small" @click="openDeleteRelationDialog">
                <i class="el-icon-remove"></i>
                删除关系
              </el-button>
            </div>
          </div>

          <!-- 智能药物推荐 -->
          <div class="control-section">
            <h4>智能药物推荐</h4>
            <div class="recommendation-form">
              <el-form :model="recommendationForm" label-width="80px" size="small">
                <el-form-item label="选择症状">
                  <el-select v-model="recommendationForm.symptomId" placeholder="请选择症状" style="width: 100%">
                    <el-option 
                      v-for="symptom in allSymptoms" 
                      :key="symptom.id" 
                      :label="symptom.name" 
                      :value="symptom.id">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="推荐数量">
                  <el-input-number 
                    v-model="recommendationForm.topK" 
                    :min="1" 
                    :max="10" 
                    style="width: 100%">
                  </el-input-number>
                </el-form-item>
                <el-form-item label="传播参数">
                  <el-slider 
                    v-model="recommendationForm.alpha" 
                    :min="0.1" 
                    :max="1" 
                    :step="0.1">
                  </el-slider>
                </el-form-item>
                <el-form-item label="迭代次数">
                  <el-input-number 
                    v-model="recommendationForm.iterations" 
                    :min="1" 
                    :max="10" 
                    style="width: 100%">
                  </el-input-number>
                </el-form-item>
                <el-form-item>
                  <el-button 
                    type="success" 
                    @click="getRecommendations" 
                    :loading="recommendationLoading"
                    :disabled="recommendationForm.symptomId === null || recommendationForm.symptomId === undefined"
                    style="width: 100%">
                    <i class="el-icon-magic-stick"></i>
                    获取推荐
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </el-card>

        <!-- 节点详情 -->
        <el-card class="panel-card" v-if="selectedNode">
          <template #header>
            <span class="panel-title">节点详情</span>
          </template>
          <div class="node-detail">
            <h4>{{ selectedNode.name }}</h4>
            <p><strong>类型：</strong>{{ selectedNode.category === 0 ? '疾病' : (selectedNode.category === 1 ? '症状' : '用药') }}</p>
            <p v-if="selectedNode.dosage"><strong>剂量：</strong>{{ selectedNode.dosage }}</p>
            <p v-if="selectedNode.frequency"><strong>用药频率：</strong>{{ selectedNode.frequency }}</p>
            <p v-if="selectedNode.sideEffects"><strong>副作用：</strong>{{ selectedNode.sideEffects }}</p>
            <p v-if="selectedNode.description"><strong>描述：</strong>{{ selectedNode.description }}</p>
            <div v-if="relatedNodes.length > 0">
              <h5>相关节点：</h5>
              <el-tag
                v-for="node in relatedNodes"
                :key="node.id"
                class="related-tag"
                @click="highlightNode(node.id)"
              >
                {{ node.name }}
              </el-tag>
            </div>
          </div>
        </el-card>

        <!-- 推荐结果 -->
        <el-card class="panel-card" v-if="recommendations.length > 0">
          <template #header>
            <span class="panel-title">药物推荐结果</span>
            <el-button 
              type="text" 
              size="small" 
              @click="clearRecommendations"
              style="float: right; margin-top: -5px;">
              清除
            </el-button>
          </template>
          <div class="recommendation-results">
            <div 
              v-for="(rec, index) in recommendations" 
              :key="rec.medicineId"
              class="recommendation-item"
              @click="highlightMedicine(rec.medicineId)">
              <div class="recommendation-rank">{{ index + 1 }}</div>
              <div class="recommendation-content">
                <h5 class="medicine-name">{{ rec.medicineName }}</h5>
                <div class="similarity-score">
                  <span>相似度: </span>
                  <el-progress 
                    :percentage="Math.round(rec.similarity * 100)" 
                    :stroke-width="8"
                    :show-text="false"
                    status="success">
                  </el-progress>
                  <span class="score-text">{{ (rec.similarity * 100).toFixed(1) }}%</span>
                </div>
                <div class="medicine-details" v-if="rec.medicine">
                  <p v-if="rec.medicine.description"><small>{{ rec.medicine.description }}</small></p>
                  <div class="medicine-meta">
                    <el-tag size="mini" v-if="rec.medicine.dosage">{{ rec.medicine.dosage }}</el-tag>
                    <el-tag size="mini" type="info" v-if="rec.medicine.frequency">{{ rec.medicine.frequency }}</el-tag>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧图谱区域 -->
      <div class="graph-area">
        <div ref="graph" class="graph-container"></div>
        
        <!-- 图谱操作提示 -->
        <div class="graph-tips">
          <el-alert
            title="操作提示"
            type="info"
            :closable="false"
            show-icon
          >
            <template #default>
              <ul class="tips-list">
                <li>鼠标拖拽可移动节点</li>
                <li>滚轮缩放图谱</li>
                <li>点击节点查看详情并启用编辑/删除功能</li>
                <li>双击节点高亮相关节点</li>
              </ul>
            </template>
          </el-alert>
        </div>
      </div>
    </div>

    <!-- 添加节点对话框 -->
    <el-dialog v-model="showAddNodeDialog" title="添加节点" width="500px">
      <el-form :model="newNodeForm" label-width="80px">
        <el-form-item label="节点类型">
          <el-select v-model="newNodeForm.type" placeholder="请选择节点类型">
            <el-option label="疾病" value="disease"></el-option>
            <el-option label="症状" value="symptom"></el-option>
            <el-option label="用药" value="medicine"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="节点名称">
          <el-input v-model="newNodeForm.name" placeholder="请输入节点名称"></el-input>
        </el-form-item>
        <el-form-item label="描述" v-if="newNodeForm.type !== 'symptom'">
          <el-input v-model="newNodeForm.description" type="textarea" placeholder="请输入描述"></el-input>
        </el-form-item>
        <template v-if="newNodeForm.type === 'medicine'">
          <el-form-item label="剂量">
            <el-input v-model="newNodeForm.dosage" placeholder="请输入剂量"></el-input>
          </el-form-item>
          <el-form-item label="用药频率">
            <el-input v-model="newNodeForm.frequency" placeholder="请输入用药频率"></el-input>
          </el-form-item>
          <el-form-item label="副作用">
            <el-input v-model="newNodeForm.sideEffects" type="textarea" placeholder="请输入副作用"></el-input>
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddNodeDialog = false">取消</el-button>
          <el-button type="primary" @click="addNode">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑节点对话框 -->
    <el-dialog v-model="showEditNodeDialog" title="编辑节点" width="500px">
      <el-form :model="editNodeForm" label-width="80px">
        <el-form-item label="节点名称">
          <el-input v-model="editNodeForm.name" placeholder="请输入节点名称"></el-input>
        </el-form-item>
        <el-form-item label="描述" v-if="editNodeForm.category !== 1">
          <el-input v-model="editNodeForm.description" type="textarea" placeholder="请输入描述"></el-input>
        </el-form-item>
        <template v-if="editNodeForm.category === 2">
          <el-form-item label="剂量">
            <el-input v-model="editNodeForm.dosage" placeholder="请输入剂量"></el-input>
          </el-form-item>
          <el-form-item label="用药频率">
            <el-input v-model="editNodeForm.frequency" placeholder="请输入用药频率"></el-input>
          </el-form-item>
          <el-form-item label="副作用">
            <el-input v-model="editNodeForm.sideEffects" type="textarea" placeholder="请输入副作用"></el-input>
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditNodeDialog = false">取消</el-button>
          <el-button type="primary" @click="updateNode">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加关系对话框 -->
    <el-dialog v-model="showAddRelationDialog" title="添加关系" width="500px">
      <el-form :model="newRelationForm" label-width="80px">
        <el-form-item label="关系类型">
          <el-select v-model="newRelationForm.type" placeholder="请选择关系类型" @change="handleRelationTypeChange">
            <el-option label="疾病-症状" value="disease-symptom"></el-option>
            <el-option label="用药-疾病" value="medicine-disease"></el-option>
          </el-select>
        </el-form-item>
        <template v-if="newRelationForm.type === 'disease-symptom'">
          <el-form-item label="疾病">
            <el-select v-model="newRelationForm.disease" placeholder="请选择疾病">
              <el-option 
                v-for="disease in allDiseases" 
                :key="disease.id" 
                :label="disease.name" 
                :value="disease.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="症状">
            <el-select v-model="newRelationForm.symptom" placeholder="请选择症状">
              <el-option 
                v-for="symptom in allSymptoms" 
                :key="symptom.id" 
                :label="symptom.name" 
                :value="symptom.name">
              </el-option>
            </el-select>
          </el-form-item>
        </template>
        <template v-if="newRelationForm.type === 'medicine-disease'">
          <el-form-item label="用药">
            <el-select v-model="newRelationForm.medicine" placeholder="请选择用药">
              <el-option 
                v-for="medicine in allMedicines" 
                :key="medicine.id" 
                :label="medicine.name" 
                :value="medicine.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="疾病">
            <el-select v-model="newRelationForm.disease" placeholder="请选择疾病">
              <el-option 
                v-for="disease in allDiseases" 
                :key="disease.id" 
                :label="disease.name" 
                :value="disease.name">
              </el-option>
            </el-select>
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddRelationDialog = false">取消</el-button>
          <el-button type="primary" @click="addRelation">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 删除关系对话框 -->
    <el-dialog v-model="showDeleteRelationDialog" title="删除关系" width="500px">
      <el-form :model="deleteRelationForm" label-width="80px">
        <el-form-item label="关系类型">
          <el-select v-model="deleteRelationForm.type" placeholder="请选择关系类型" @change="handleDeleteRelationTypeChange">
            <el-option label="疾病-症状" value="disease-symptom"></el-option>
            <el-option label="用药-疾病" value="medicine-disease"></el-option>
          </el-select>
        </el-form-item>
        <template v-if="deleteRelationForm.type === 'disease-symptom'">
          <el-form-item label="疾病">
            <el-select v-model="deleteRelationForm.disease" placeholder="请选择疾病">
              <el-option 
                v-for="disease in allDiseases" 
                :key="disease.id" 
                :label="disease.name" 
                :value="disease.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="症状">
            <el-select v-model="deleteRelationForm.symptom" placeholder="请选择症状">
              <el-option 
                v-for="symptom in allSymptoms" 
                :key="symptom.id" 
                :label="symptom.name" 
                :value="symptom.name">
              </el-option>
            </el-select>
          </el-form-item>
        </template>
        <template v-if="deleteRelationForm.type === 'medicine-disease'">
          <el-form-item label="用药">
            <el-select v-model="deleteRelationForm.medicine" placeholder="请选择用药">
              <el-option 
                v-for="medicine in allMedicines" 
                :key="medicine.id" 
                :label="medicine.name" 
                :value="medicine.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="疾病">
            <el-select v-model="deleteRelationForm.disease" placeholder="请选择疾病">
              <el-option 
                v-for="disease in allDiseases" 
                :key="disease.id" 
                :label="disease.name" 
                :value="disease.name">
              </el-option>
            </el-select>
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDeleteRelationDialog = false">取消</el-button>
          <el-button type="danger" @click="deleteRelation">确定删除</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, watch } from 'vue';
import * as echarts from 'echarts';
import request from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';
import { 
  getFullGraph, 
  getAllDiseases, 
  getAllSymptoms, 
  getAllMedicines,
  addDisease,
  addSymptom,
  addMedicine,
  updateDisease,
  updateSymptom,
  updateMedicine,
  deleteDisease,
  deleteSymptom,
  deleteMedicine,
  addSymptomToDisease,
  addMedicineToDisease,
  removeSymptomFromDisease,
  removeMedicineFromDisease,
  recommendMedicinesBySymptom
} from '@/api/knowledgeGraph';

const graph = ref(null);
let myChart = null;

// 响应式数据
const originalData = ref({ nodes: [], links: [] });
const filteredData = ref({ nodes: [], links: [] });
const searchKeyword = ref('');
const selectedCategories = ref(['疾病', '症状', '用药']);
const layoutType = ref('force');
const selectedNode = ref(null);
const relatedNodes = ref([]);

// 对话框控制
const showAddNodeDialog = ref(false);
const showEditNodeDialog = ref(false);
const showAddRelationDialog = ref(false);
const showDeleteRelationDialog = ref(false);

// 表单数据
const newNodeForm = ref({
  type: '',
  name: '',
  description: '',
  dosage: '',
  frequency: '',
  sideEffects: ''
});

const editNodeForm = ref({
  name: '',
  category: 0,
  description: '',
  dosage: '',
  frequency: '',
  sideEffects: ''
});

const newRelationForm = ref({
  type: '',
  disease: '',
  symptom: '',
  medicine: ''
});

const deleteRelationForm = ref({
  type: '',
  disease: '',
  symptom: '',
  medicine: ''
});

// 所有节点数据
const allDiseases = ref([]);
const allSymptoms = ref([]);
const allMedicines = ref([]);

// 药物推荐相关数据
const recommendationForm = ref({
  symptomId: null,
  topK: 5,
  alpha: 0.6,
  iterations: 3
});
const recommendations = ref([]);
const recommendationLoading = ref(false);

// 计算属性
const diseaseCount = computed(() => {
  return filteredData.value.nodes.filter(node => node.category === 0).length;
});

const symptomCount = computed(() => {
  return filteredData.value.nodes.filter(node => node.category === 1).length;
});

const medicineCount = computed(() => {
  return filteredData.value.nodes.filter(node => node.category === 2).length;
});

const linkCount = computed(() => {
  return filteredData.value.links.length;
});

// 监听搜索关键词变化
watch(searchKeyword, (newKeyword) => {
  filterData();
});

// 监听分类选择变化
watch(selectedCategories, () => {
  filterData();
});

// 数据过滤函数
const filterData = () => {
  let nodes = originalData.value.nodes.filter(node => {
    // 分类过滤 - 将字符串转换为数字进行比较
    const categoryMap = { '疾病': 0, '症状': 1, '用药': 2 };
    const nodeCategoryNumbers = selectedCategories.value.map(cat => categoryMap[cat]);
    if (!nodeCategoryNumbers.includes(node.category)) {
      return false;
    }
    
    // 搜索关键词过滤
    if (searchKeyword.value && !node.name.toLowerCase().includes(searchKeyword.value.toLowerCase())) {
      return false;
    }
    
    return true;
  });
  
  // 过滤相关的连接
  const nodeIds = new Set(nodes.map(node => node.id));
  let links = originalData.value.links.filter(link => {
    return nodeIds.has(link.source) && nodeIds.has(link.target);
  });
  
  filteredData.value = { nodes, links };
  updateChart();
};

// 搜索处理
const handleSearch = () => {
  // 搜索逻辑已在watch中处理
};

// 分类变化处理
const handleCategoryChange = () => {
  // 分类变化逻辑已在watch中处理
};

// 布局变化处理
const handleLayoutChange = () => {
  updateChart();
};

// 重置图谱
const resetGraph = () => {
  searchKeyword.value = '';
  selectedCategories.value = ['疾病', '症状', '用药'];
  layoutType.value = 'force';
  selectedNode.value = null;
  relatedNodes.value = [];
  
  if (myChart) {
    myChart.dispatchAction({
      type: 'restore'
    });
  }
  
  ElMessage.success('图谱已重置');
};

// 高亮节点
const highlightNode = (nodeId) => {
  if (myChart) {
    myChart.dispatchAction({
      type: 'highlight',
      dataIndex: filteredData.value.nodes.findIndex(node => node.id === nodeId)
    });
  }
};

// 获取相关节点 - 基于原始完整数据
const getRelatedNodes = (nodeId) => {
  const related = [];
  // 使用原始数据中的所有连接来查找相关节点
  originalData.value.links.forEach(link => {
    if (link.source === nodeId) {
      const targetNode = originalData.value.nodes.find(node => node.id === link.target);
      if (targetNode) related.push(targetNode);
    } else if (link.target === nodeId) {
      const sourceNode = originalData.value.nodes.find(node => node.id === link.source);
      if (sourceNode) related.push(sourceNode);
    }
  });
  return related;
};

// 更新图表
const updateChart = () => {
  if (!myChart) return;
  
  const option = {
    title: {
      text: '医疗知识图谱',
      top: 20,
      left: 'center',
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: function(params) {
        if (params.dataType === 'node') {
          const typeText = params.data.category === 0 ? '疾病' : (params.data.category === 1 ? '症状' : '用药');
          let content = `<div style="padding: 10px;">
                          <strong>${params.data.name}</strong><br/>
                          类型: ${typeText}<br/>`;
          
          if (params.data.description) {
            content += `描述: ${params.data.description}<br/>`;
          }
          if (params.data.dosage) {
            content += `剂量: ${params.data.dosage}<br/>`;
          }
          if (params.data.frequency) {
            content += `用药频率: ${params.data.frequency}<br/>`;
          }
          if (params.data.sideEffects) {
            content += `副作用: ${params.data.sideEffects}<br/>`;
          }
          
          content += `</div>`;
          return content;
        } else if (params.dataType === 'edge') {
          return `关系: ${params.data.relationship || '相关'}`;
        }
      }
    },
    legend: {
      data: ['疾病', '症状', '用药'],
      top: 50,
      left: 'center'
    },
    animationDuration: ['grid', 'hierarchical', 'radial', 'tree', 'concentric'].includes(layoutType.value) ? 0 : 1500,
    animationEasingUpdate: ['grid', 'hierarchical', 'radial', 'tree', 'concentric'].includes(layoutType.value) ? 'linear' : 'quinticInOut',
    series: [
      {
        name: '知识图谱',
        type: 'graph',
        layout: ['grid', 'hierarchical', 'radial', 'tree', 'concentric'].includes(layoutType.value) ? 'none' : layoutType.value,
        data: filteredData.value.nodes.map((node, index) => {
          const nodeData = {
            id: String(node.id), // 确保ID是字符串
            name: node.name,
            symbolSize: node.category === 0 ? 60 : (node.category === 1 ? 40 : 50),
            category: node.category,
            description: node.description,
            dosage: node.dosage,
            frequency: node.frequency,
            sideEffects: node.sideEffects,
            itemStyle: {
              color: node.category === 0 ? '#ff6b6b' : (node.category === 1 ? '#4ecdc4' : '#ffa726')
            }
          };
          
          // 手动布局时设置位置
          if (layoutType.value === 'grid') {
            const cols = Math.ceil(Math.sqrt(filteredData.value.nodes.length));
            const row = Math.floor(index / cols);
            const col = index % cols;
            nodeData.x = (col + 1) * (100 / (cols + 1));
            nodeData.y = (row + 1) * (100 / (Math.ceil(filteredData.value.nodes.length / cols) + 1));
            nodeData.fixed = false;
          } else if (layoutType.value === 'hierarchical') {
            // 层次布局：按类别分层
            const layerHeight = 100 / 4; // 分为4层
            const categoryLayer = node.category + 1;
            const sameTypeNodes = filteredData.value.nodes.filter(n => n.category === node.category);
            const indexInType = sameTypeNodes.findIndex(n => n.id === node.id);
            nodeData.x = (indexInType + 1) * (100 / (sameTypeNodes.length + 1));
            nodeData.y = categoryLayer * layerHeight;
            nodeData.fixed = false;
          } else if (layoutType.value === 'radial') {
            // 径向布局：以第一个疾病节点为中心
            const centerNode = filteredData.value.nodes.find(n => n.category === 0);
            if (node.id === centerNode?.id) {
              nodeData.x = 50;
              nodeData.y = 50;
            } else {
              const angle = (index * 2 * Math.PI) / filteredData.value.nodes.length;
              const radius = node.category === 0 ? 20 : (node.category === 1 ? 35 : 30);
              nodeData.x = 50 + radius * Math.cos(angle);
              nodeData.y = 50 + radius * Math.sin(angle);
            }
            nodeData.fixed = false;
          } else if (layoutType.value === 'tree') {
            // 树形布局：疾病在顶层，症状和用药在下层
            if (node.category === 0) { // 疾病
              const diseaseNodes = filteredData.value.nodes.filter(n => n.category === 0);
              const diseaseIndex = diseaseNodes.findIndex(n => n.id === node.id);
              nodeData.x = (diseaseIndex + 1) * (100 / (diseaseNodes.length + 1));
              nodeData.y = 20;
            } else { // 症状和用药
              const nonDiseaseNodes = filteredData.value.nodes.filter(n => n.category !== 0);
              const nonDiseaseIndex = nonDiseaseNodes.findIndex(n => n.id === node.id);
              nodeData.x = (nonDiseaseIndex + 1) * (100 / (nonDiseaseNodes.length + 1));
              nodeData.y = node.category === 1 ? 60 : 80; // 症状在中间，用药在底部
            }
            nodeData.fixed = false;
          } else if (layoutType.value === 'concentric') {
            // 同心圆布局：按类别分圆圈
            const centerX = 50, centerY = 50;
            const radius = node.category === 0 ? 15 : (node.category === 1 ? 25 : 35);
            const sameTypeNodes = filteredData.value.nodes.filter(n => n.category === node.category);
            const indexInType = sameTypeNodes.findIndex(n => n.id === node.id);
            const angle = (indexInType * 2 * Math.PI) / sameTypeNodes.length;
            nodeData.x = centerX + radius * Math.cos(angle);
            nodeData.y = centerY + radius * Math.sin(angle);
            nodeData.fixed = false;
          }
          
          return nodeData;
        }),
        links: filteredData.value.links.map(link => ({
          source: String(link.source), // 确保source是字符串
          target: String(link.target), // 确保target是字符串
          lineStyle: {
            color: '#999',
            width: 2
          }
        })),
        categories: [
          {
            name: '疾病',
            itemStyle: {
              color: '#ff6b6b'
            }
          },
          {
            name: '症状',
            itemStyle: {
              color: '#4ecdc4'
            }
          },
          {
            name: '用药',
            itemStyle: {
              color: '#ffa726'
            }
          }
        ],
        roam: true,
        draggable: true, // 允许所有布局的节点拖拽
        label: {
          show: true,
          position: 'right',
          formatter: '{b}',
          fontSize: 12
        },
        lineStyle: {
          color: 'source',
          curveness: 0.2,
          opacity: 0.7
        },
        emphasis: {
          focus: 'adjacency',
          lineStyle: {
            width: 4,
            opacity: 1
          },
          itemStyle: {
            borderWidth: 3,
            borderColor: '#333'
          }
        },
        force: {
          repulsion: layoutType.value === 'force' ? 200 : (['grid', 'hierarchical', 'radial', 'tree', 'concentric'].includes(layoutType.value) ? 50 : 100),
          edgeLength: layoutType.value === 'force' ? 80 : (['grid', 'hierarchical', 'radial', 'tree', 'concentric'].includes(layoutType.value) ? 30 : 50),
          gravity: ['grid', 'hierarchical', 'radial', 'tree', 'concentric'].includes(layoutType.value) ? 0.3 : 0.1
        },
        circular: {
          rotateLabel: true
        },
        // 手动布局时的特殊配置
        ...(['grid', 'hierarchical', 'radial', 'tree', 'concentric'].includes(layoutType.value) ? {
          animation: false, // 禁用动画以提高性能
          edgeSymbol: ['none', 'none'], // 简化边的显示
          edgeSymbolSize: 0
        } : {})
      }
    ]
  };
  
  myChart.setOption(option, true);
};

// 初始化图表
const initChart = () => {
  myChart = echarts.init(graph.value);
  
  // 添加点击事件
  myChart.on('click', (params) => {
    if (params.dataType === 'node') {
      selectedNode.value = params.data;
      relatedNodes.value = getRelatedNodes(params.data.id);
    }
  });
  
  // 添加双击事件
  myChart.on('dblclick', (params) => {
    if (params.dataType === 'node') {
      highlightNode(params.data.id);
    }
  });
  
  // 响应式调整
  window.addEventListener('resize', () => {
    myChart.resize();
  });
};

// 加载数据
const loadData = async () => {
  try {
    const response = await request.get('/knowledge-graph/full-graph');
    
    // 确保所有ID都是字符串类型，避免类型不匹配
    const processedData = {
      nodes: response.data.nodes.map(node => ({
        ...node,
        id: String(node.id) // 确保ID是字符串
      })),
      links: response.data.links.map(link => ({
        ...link,
        source: String(link.source), // 确保source是字符串
        target: String(link.target)  // 确保target是字符串
      }))
    };
    
    // 调试信息：检查数据结构
    console.log('处理后的节点数据:', processedData.nodes);
    console.log('处理后的连接数据:', processedData.links);
    
    // 验证连接的有效性
    const nodeIds = new Set(processedData.nodes.map(node => node.id));
    const invalidLinks = processedData.links.filter(link => 
      !nodeIds.has(link.source) || !nodeIds.has(link.target)
    );
    
    if (invalidLinks.length > 0) {
      console.warn('发现无效连接:', invalidLinks);
    }
    
    originalData.value = processedData;
    filteredData.value = { ...processedData };
    updateChart();
    ElMessage.success('知识图谱加载成功');
  } catch (error) {
    console.error('加载知识图谱失败:', error);
    ElMessage.error('加载知识图谱失败，请稍后重试');
  }
};

// 节点管理方法
const openAddNodeDialog = () => {
  newNodeForm.value = {
    type: '',
    name: '',
    description: '',
    dosage: '',
    frequency: '',
    sideEffects: ''
  };
  showAddNodeDialog.value = true;
};

const openEditNodeDialog = () => {
  if (!selectedNode.value) {
    ElMessage.warning('请先选择一个节点');
    return;
  }
  editNodeForm.value = {
    name: selectedNode.value.name,
    category: selectedNode.value.category,
    description: selectedNode.value.description || '',
    dosage: selectedNode.value.dosage || '',
    frequency: selectedNode.value.frequency || '',
    sideEffects: selectedNode.value.sideEffects || ''
  };
  showEditNodeDialog.value = true;
};

const deleteSelectedNode = async () => {
  if (!selectedNode.value) {
    ElMessage.warning('请先选择一个节点');
    return;
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除节点 "${selectedNode.value.name}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );
    
    let endpoint = '';
    if (selectedNode.value.category === 0) {
      endpoint = `/knowledge-graph/disease/${selectedNode.value.name}`;
    } else if (selectedNode.value.category === 1) {
      endpoint = `/knowledge-graph/symptom/${selectedNode.value.name}`;
    } else if (selectedNode.value.category === 2) {
      endpoint = `/knowledge-graph/medicine/${selectedNode.value.name}`;
    }
    
    await request.delete(endpoint);
    ElMessage.success('节点删除成功');
    selectedNode.value = null;
    await loadData();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除节点失败:', error);
      ElMessage.error('删除节点失败，请稍后重试');
    }
  }
};

const addNode = async () => {
  try {
    let endpoint = '';
    let data = {
      name: newNodeForm.value.name,
      description: newNodeForm.value.description
    };
    
    if (newNodeForm.value.type === 'disease') {
      endpoint = '/knowledge-graph/disease';
    } else if (newNodeForm.value.type === 'symptom') {
      endpoint = '/knowledge-graph/symptom';
    } else if (newNodeForm.value.type === 'medicine') {
      endpoint = '/knowledge-graph/medicine';
      data = {
        ...data,
        dosage: newNodeForm.value.dosage,
        frequency: newNodeForm.value.frequency,
        sideEffects: newNodeForm.value.sideEffects
      };
    }
    
    const response = await request.post(endpoint, data);
    
    ElMessage.success('节点添加成功');
    showAddNodeDialog.value = false;
    await loadData();
  } catch (error) {
    console.error('添加节点失败:', error);
    ElMessage.error('添加节点失败，请稍后重试');
  }
};

const updateNode = async () => {
  try {
    let endpoint = '';
    let data = {
      name: editNodeForm.value.name, // 使用编辑后的节点名称
      description: editNodeForm.value.description
    };
    
    if (editNodeForm.value.category === 0) {
      endpoint = `/knowledge-graph/disease/${selectedNode.value.name}`; // 路径参数使用原始名称
    } else if (editNodeForm.value.category === 1) {
      endpoint = `/knowledge-graph/symptom/${selectedNode.value.name}`;
    } else if (editNodeForm.value.category === 2) {
      endpoint = `/knowledge-graph/medicine/${selectedNode.value.name}`;
      data = {
        ...data,
        dosage: editNodeForm.value.dosage,
        frequency: editNodeForm.value.frequency,
        sideEffects: editNodeForm.value.sideEffects
      };
    }
    
    await request.put(endpoint, data);
    ElMessage.success('节点更新成功');
    showEditNodeDialog.value = false;
    selectedNode.value = null;
    await loadData();
  } catch (error) {
    console.error('更新节点失败:', error);
    ElMessage.error('更新节点失败，请稍后重试');
  }
};

// 关系管理方法
const openAddRelationDialog = async () => {
  try {
    // 获取所有节点数据
    const [diseasesRes, symptomsRes, medicinesRes] = await Promise.all([
      request.get('/knowledge-graph/diseases'),
      request.get('/knowledge-graph/symptoms'),
      request.get('/knowledge-graph/medicines')
    ]);
    
    allDiseases.value = diseasesRes.data;
    allSymptoms.value = symptomsRes.data;
    allMedicines.value = medicinesRes.data;
    
    newRelationForm.value = {
      type: '',
      disease: '',
      symptom: '',
      medicine: ''
    };
    showAddRelationDialog.value = true;
  } catch (error) {
    console.error('获取节点数据失败:', error);
    ElMessage.error('获取节点数据失败，请稍后重试');
  }
};

const openDeleteRelationDialog = async () => {
  try {
    // 获取所有节点数据
    const [diseasesRes, symptomsRes, medicinesRes] = await Promise.all([
      request.get('/knowledge-graph/diseases'),
      request.get('/knowledge-graph/symptoms'),
      request.get('/knowledge-graph/medicines')
    ]);
    
    allDiseases.value = diseasesRes.data;
    allSymptoms.value = symptomsRes.data;
    allMedicines.value = medicinesRes.data;
    
    deleteRelationForm.value = {
      type: '',
      disease: '',
      symptom: '',
      medicine: ''
    };
    showDeleteRelationDialog.value = true;
  } catch (error) {
    console.error('获取节点数据失败:', error);
    ElMessage.error('获取节点数据失败，请稍后重试');
  }
};

const addRelation = async () => {
  try {
    let endpoint = '';
    
    if (newRelationForm.value.type === 'disease-symptom') {
      endpoint = `/knowledge-graph/symptom/${newRelationForm.value.symptom}/disease/${newRelationForm.value.disease}`;
    } else if (newRelationForm.value.type === 'medicine-disease') {
      endpoint = `/knowledge-graph/medicine/${newRelationForm.value.medicine}/disease/${newRelationForm.value.disease}`;
    }
    
    await request.post(endpoint);
    ElMessage.success('关系添加成功');
    showAddRelationDialog.value = false;
    await loadData();
  } catch (error) {
    console.error('添加关系失败:', error);
    ElMessage.error('添加关系失败，请稍后重试');
  }
};

const deleteRelation = async () => {
  try {
    let endpoint = '';
    
    if (deleteRelationForm.value.type === 'disease-symptom') {
      endpoint = `/knowledge-graph/symptom/${deleteRelationForm.value.symptom}/disease/${deleteRelationForm.value.disease}`;
    } else if (deleteRelationForm.value.type === 'medicine-disease') {
      endpoint = `/knowledge-graph/medicine/${deleteRelationForm.value.medicine}/disease/${deleteRelationForm.value.disease}`;
    }
    
    await request.delete(endpoint);
    ElMessage.success('关系删除成功');
    showDeleteRelationDialog.value = false;
    await loadData();
  } catch (error) {
    console.error('删除关系失败:', error);
    ElMessage.error('删除关系失败，请稍后重试');
  }
};

// 处理关系类型变化
const handleRelationTypeChange = (value) => {
  console.log('关系类型变化:', value);
  // 可以在这里添加额外的逻辑，比如根据关系类型更新其他字段
};

const handleDeleteRelationTypeChange = (value) => {
  console.log('删除关系类型变化:', value);
  // 可以在这里添加额外的逻辑，比如根据关系类型更新其他字段
};

// 药物推荐相关方法
const getRecommendations = async () => {
  if (recommendationForm.value.symptomId === null || recommendationForm.value.symptomId === undefined) {
    ElMessage.warning('请先选择症状');
    return;
  }
  
  recommendationLoading.value = true;
  try {
    const response = await recommendMedicinesBySymptom({
      symptomId: recommendationForm.value.symptomId,
      topK: recommendationForm.value.topK,
      alpha: recommendationForm.value.alpha,
      iterations: recommendationForm.value.iterations
    });
    
    recommendations.value = response.data;
    ElMessage.success(`成功获取 ${response.data.length} 个药物推荐`);
    
    // 高亮推荐的药物节点
    if (recommendations.value.length > 0) {
      highlightRecommendedMedicines();
    }
  } catch (error) {
    console.error('获取药物推荐失败:', error);
    ElMessage.error('获取药物推荐失败，请稍后重试');
  } finally {
    recommendationLoading.value = false;
  }
};

const clearRecommendations = () => {
  recommendations.value = [];
  // 重置图谱高亮
  updateChart();
};

const highlightMedicine = (medicineId) => {
  // 在图谱中高亮指定的药物节点
  const nodeId = `Medicine_${medicineId}`;
  highlightNode(nodeId);
};

const highlightRecommendedMedicines = () => {
  // 高亮所有推荐的药物节点
  const recommendedNodeIds = recommendations.value.map(rec => `Medicine_${rec.medicineId}`);
  
  // 更新图表选项以高亮推荐的节点
  const option = myChart.getOption();
  if (option && option.series && option.series[0]) {
    const series = option.series[0];
    series.data = series.data.map(node => {
      if (recommendedNodeIds.includes(node.id)) {
        return {
          ...node,
          itemStyle: {
            ...node.itemStyle,
            borderWidth: 4,
            borderColor: '#ff4757',
            shadowBlur: 10,
            shadowColor: '#ff4757'
          },
          label: {
            ...node.label,
            fontWeight: 'bold',
            color: '#ff4757'
          }
        };
      }
      return node;
    });
    
    myChart.setOption(option);
  }
};

// 加载所有节点数据用于推荐功能
const loadAllNodesData = async () => {
  try {
    const [diseasesRes, symptomsRes, medicinesRes] = await Promise.all([
      getAllDiseases(),
      getAllSymptoms(),
      getAllMedicines()
    ]);
    
    allDiseases.value = diseasesRes.data;
    allSymptoms.value = symptomsRes.data;
    allMedicines.value = medicinesRes.data;
  } catch (error) {
    console.error('加载节点数据失败:', error);
  }
};

onMounted(async () => {
  initChart();
  await loadData();
  await loadAllNodesData();
});
</script>

<style scoped>
.knowledge-graph-page {
  min-height: 100vh;
  height: auto;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: auto;
}

.knowledge-graph-page::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 80%, rgba(120, 119, 198, 0.3) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(255, 119, 198, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 40% 40%, rgba(120, 219, 255, 0.1) 0%, transparent 50%);
  pointer-events: none;
}

/* 工具栏样式 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 24px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 10;
}

.toolbar-left {
  display: flex;
  align-items: center;
}

.page-title {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  display: flex;
  align-items: center;
  gap: 8px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-title i {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 22px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-input {
  width: 280px;
}

/* 主要内容区域 */
.main-content {
  flex: 1;
  display: flex;
  gap: 16px;
  padding: 16px;
  overflow: visible;
  position: relative;
  z-index: 1;
  min-height: calc(100vh - 80px);
}

/* 左侧控制面板 */
.control-panel {
  width: 300px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: none;
  overflow-y: auto;
}

.panel-card {
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}

.panel-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}

.panel-card :deep(.el-card__header) {
  padding: 12px 16px;
  border-bottom: 1px solid rgba(240, 242, 245, 0.6);
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
  position: relative;
}

.panel-card :deep(.el-card__body) {
  padding: 16px;
}

.panel-title {
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 16px;
  letter-spacing: 0.5px;
}

.control-section {
  margin-bottom: 16px;
}

.control-section:last-child {
  margin-bottom: 0;
}

.control-section h4 {
  margin: 0 0 10px 0;
  font-size: 13px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-transform: uppercase;
  letter-spacing: 1px;
  position: relative;
  padding-bottom: 6px;
}

.control-section h4::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 30px;
  height: 2px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 1px;
}

.control-section h5 {
  margin: 16px 0 12px 0;
  font-size: 14px;
  font-weight: 600;
  color: #606266;
}

/* 统计信息样式 */
.stats {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 12px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
  border-radius: 8px;
  border: 1px solid rgba(102, 126, 234, 0.15);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.stat-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.15);
  border-color: rgba(102, 126, 234, 0.3);
}

.stat-label {
  font-size: 14px;
  color: #606266;
  font-weight: 600;
}

.stat-value {
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 节点详情样式 */
.node-detail h4 {
  margin: 0 0 16px 0;
  font-size: 18px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  position: relative;
  padding-bottom: 8px;
}

.node-detail h4::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 40px;
  height: 2px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 1px;
}

.node-detail p {
  margin: 12px 0;
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  padding: 8px 0;
}

.related-tag {
  margin: 6px 6px 6px 0;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  border: 1px solid rgba(102, 126, 234, 0.2);
  color: #667eea;
  font-weight: 600;
}

.related-tag:hover {
  transform: translateY(-2px) scale(1.05);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.25);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-color: transparent;
}

/* 右侧图谱区域 */
.graph-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
  position: relative;
}

.graph-area::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}

.graph-container {
  flex: 1;
  width: 100%;
  min-height: 600px;
  position: relative;
}

/* 图谱操作提示 */
.graph-tips {
  padding: 20px 24px;
  border-top: 1px solid rgba(240, 242, 245, 0.6);
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.03) 0%, rgba(118, 75, 162, 0.03) 100%);
}

.tips-list {
  margin: 0;
  padding-left: 24px;
  font-size: 14px;
  color: #606266;
}

.tips-list li {
  margin: 8px 0;
  line-height: 1.6;
  position: relative;
}

.tips-list li::marker {
  color: #667eea;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .control-panel {
    width: 300px;
  }
  
  .search-input {
    width: 280px;
  }
}

@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
    padding: 16px;
    gap: 16px;
  }
  
  .control-panel {
    width: 100%;
    flex-direction: row;
    overflow-x: auto;
    gap: 16px;
  }
  
  .panel-card {
    min-width: 300px;
  }
  
  .toolbar {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
    padding: 16px 20px;
  }
  
  .toolbar-right {
    justify-content: center;
  }
  
  .search-input {
    width: 100%;
    max-width: 320px;
  }
  
  .page-title {
    font-size: 20px;
  }
}

/* 动画效果 */
.panel-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(102, 126, 234, 0.15);
}

.graph-area:hover {
  box-shadow: 0 12px 40px rgba(102, 126, 234, 0.15);
}

/* Element Plus 组件样式覆盖 */
:deep(.el-checkbox-group) {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

:deep(.el-checkbox) {
  margin-right: 0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

:deep(.el-checkbox:hover) {
  transform: translateX(4px);
}

:deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: #667eea;
}

:deep(.el-checkbox__input.is-checked + .el-checkbox__label) {
  color: #667eea;
  font-weight: 600;
}

:deep(.el-select) {
  width: 100%;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 12px;
  border: 1px solid rgba(102, 126, 234, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

:deep(.el-select .el-input__wrapper:hover) {
  border-color: #667eea;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

:deep(.el-select .el-input__wrapper.is-focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

:deep(.el-input__wrapper) {
  border-radius: 12px;
  border: 1px solid rgba(102, 126, 234, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
}

:deep(.el-input__wrapper:hover) {
  border-color: #667eea;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

:deep(.el-alert) {
  border-radius: 12px;
  border: 1px solid rgba(102, 126, 234, 0.15);
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
}

:deep(.el-alert__content) {
  padding: 0;
}

:deep(.el-alert__icon) {
  color: #667eea;
}

:deep(.el-button) {
  border-radius: 12px;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

:deep(.el-button--primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
}

:deep(.el-tag) {
  border-radius: 8px;
  font-weight: 500;
}

/* 加载状态 */
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
  flex-direction: column;
  gap: 20px;
  color: #909399;
}

.loading-container i {
  font-size: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: pulse 2s infinite;
}

/* 空状态 */
.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
  flex-direction: column;
  gap: 20px;
  color: #909399;
}

.empty-state i {
  font-size: 64px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.3) 0%, rgba(118, 75, 162, 0.3) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 动画关键帧 */
@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

/* 推荐功能样式 */
.recommendation-form {
  padding: 8px 0;
}

.recommendation-results {
  max-height: 400px;
  overflow-y: auto;
}

.recommendation-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px;
  margin-bottom: 8px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
  border-radius: 12px;
  border: 1px solid rgba(102, 126, 234, 0.15);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.recommendation-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
}

.recommendation-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.15);
  border-color: rgba(102, 126, 234, 0.3);
}

.recommendation-rank {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 50%;
  font-size: 12px;
  font-weight: bold;
  flex-shrink: 0;
}

.recommendation-content {
  flex: 1;
  min-width: 0;
}

.medicine-name {
  margin: 0 0 8px 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  line-height: 1.4;
}

.similarity-score {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-size: 12px;
  color: #606266;
}

.similarity-score .el-progress {
  flex: 1;
  max-width: 80px;
}

.score-text {
  font-weight: 600;
  color: #67C23A;
  min-width: 40px;
  text-align: right;
}

.medicine-details {
  font-size: 12px;
  color: #909399;
}

.medicine-details p {
  margin: 0 0 6px 0;
  line-height: 1.4;
}

.medicine-meta {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.medicine-meta .el-tag {
  font-size: 10px;
  padding: 2px 6px;
  height: auto;
  line-height: 1.2;
}

/* 推荐表单样式优化 */
.recommendation-form :deep(.el-form-item) {
  margin-bottom: 12px;
}

.recommendation-form :deep(.el-form-item__label) {
  font-size: 12px;
  font-weight: 600;
  color: #606266;
  line-height: 1.4;
  padding-bottom: 4px;
}

.recommendation-form :deep(.el-input-number) {
  width: 100%;
}

.recommendation-form :deep(.el-input-number .el-input__wrapper) {
  border-radius: 8px;
}

.recommendation-form :deep(.el-slider) {
  margin: 8px 0;
}

.recommendation-form :deep(.el-slider__runway) {
  background: rgba(102, 126, 234, 0.1);
}

.recommendation-form :deep(.el-slider__bar) {
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}

.recommendation-form :deep(.el-slider__button) {
  border: 2px solid #667eea;
  background: white;
}

.recommendation-form :deep(.el-slider__button:hover) {
  border-color: #5a6fd8;
}

/* 滚动条美化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
}
</style>