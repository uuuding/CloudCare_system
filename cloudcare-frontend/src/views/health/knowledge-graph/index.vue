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
                <li>点击节点查看详情</li>
                <li>双击节点高亮相关节点</li>
              </ul>
            </template>
          </el-alert>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import * as echarts from 'echarts';
import request from '@/utils/request';
import { ElMessage } from 'element-plus';

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

// 获取相关节点
const getRelatedNodes = (nodeId) => {
  const related = [];
  filteredData.value.links.forEach(link => {
    if (link.source === nodeId) {
      const targetNode = filteredData.value.nodes.find(node => node.id === link.target);
      if (targetNode) related.push(targetNode);
    } else if (link.target === nodeId) {
      const sourceNode = filteredData.value.nodes.find(node => node.id === link.source);
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
    animationDuration: layoutType.value === 'grid' ? 0 : 1500,
    animationEasingUpdate: layoutType.value === 'grid' ? 'linear' : 'quinticInOut',
    series: [
      {
        name: '知识图谱',
        type: 'graph',
        layout: layoutType.value === 'grid' ? 'none' : layoutType.value,
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
          
          // 网格布局时手动设置位置
           if (layoutType.value === 'grid') {
             const cols = Math.ceil(Math.sqrt(filteredData.value.nodes.length));
             const row = Math.floor(index / cols);
             const col = index % cols;
             nodeData.x = (col + 1) * (100 / (cols + 1));
             nodeData.y = (row + 1) * (100 / (Math.ceil(filteredData.value.nodes.length / cols) + 1));
             // 不完全固定位置，允许轻微移动但保持网格结构
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
          repulsion: layoutType.value === 'force' ? 200 : (layoutType.value === 'grid' ? 50 : 100),
          edgeLength: layoutType.value === 'force' ? 80 : (layoutType.value === 'grid' ? 30 : 50),
          gravity: layoutType.value === 'grid' ? 0.3 : 0.1
        },
        circular: {
          rotateLabel: true
        },
        // 网格布局时的特殊配置
        ...(layoutType.value === 'grid' ? {
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

onMounted(async () => {
  initChart();
  await loadData();
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
  padding: 20px 32px;
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
  font-size: 24px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  display: flex;
  align-items: center;
  gap: 12px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-title i {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 28px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-input {
  width: 320px;
}

/* 主要内容区域 */
.main-content {
  flex: 1;
  display: flex;
  gap: 24px;
  padding: 24px;
  overflow: visible;
  position: relative;
  z-index: 1;
  min-height: calc(100vh - 120px);
}

/* 左侧控制面板 */
.control-panel {
  width: 320px;
  display: flex;
  flex-direction: column;
  gap: 20px;
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
  padding: 20px 24px;
  border-bottom: 1px solid rgba(240, 242, 245, 0.6);
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
  position: relative;
}

.panel-card :deep(.el-card__body) {
  padding: 24px;
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
  margin-bottom: 28px;
}

.control-section:last-child {
  margin-bottom: 0;
}

.control-section h4 {
  margin: 0 0 16px 0;
  font-size: 14px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-transform: uppercase;
  letter-spacing: 1px;
  position: relative;
  padding-bottom: 8px;
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
  gap: 12px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
  border-radius: 12px;
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