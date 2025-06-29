<template>
  <div class="intervention-template">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>干预模板管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="showCreateDialog">新增模板</el-button>

      </div>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-form :model="searchForm" inline>
        <el-form-item label="模板名称">
          <el-input v-model="searchForm.templateName" placeholder="请输入模板名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.category" placeholder="请选择分类" clearable>
            <el-option v-for="category in categories" :key="category" :label="category" :value="category"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预警类型">
          <el-select v-model="searchForm.alertType" placeholder="请选择预警类型" clearable>
            <el-option v-for="alertType in alertTypes" :key="alertType" :label="alertType" :value="alertType"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.isEnabled" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="true"></el-option>
            <el-option label="禁用" :value="false"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchTemplates">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.total || 0 }}</div>
              <div class="stat-label">总模板数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.enabled || 0 }}</div>
              <div class="stat-label">启用模板</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.categories || 0 }}</div>
              <div class="stat-label">模板分类</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalUsage || 0 }}</div>
              <div class="stat-label">总使用次数</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 模板列表 -->
    <div class="table-section">
      <el-table :data="templateList" v-loading="loading" stripe style="width: 100%;">
        <el-table-column prop="templateId" label="模板ID" width="80"></el-table-column>
        <el-table-column prop="templateName" label="模板名称" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="templateCategory" label="分类" width="120">
          <template #default="scope">
            <el-tag>{{ scope.row.templateCategory }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertType" label="适用预警类型" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="priorityLevel" label="优先级" width="100">
          <template #default="scope">
            <el-tag :type="getPriorityTagType(scope.row.priorityLevel)">{{ getPriorityLabel(scope.row.priorityLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="templateContent" label="干预内容" width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="usageCount" label="使用次数" width="100" align="center">
          <template #default="scope">
            <span class="usage-count">
              {{ scope.row.usageCount || 0 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="isActive" label="状态" width="100">
          <template #default="scope">
            <el-switch v-model="scope.row.isActive" @change="toggleTemplateStatus(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="150"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="viewTemplate(scope.row)">查看</el-button>
            <el-button size="small" type="primary" @click="editTemplate(scope.row)">编辑</el-button>
            <el-dropdown @command="handleCommand" trigger="click">
               <el-button size="small">更多<i class="el-icon-arrow-down el-icon--right"></i></el-button>
               <template #dropdown>
                 <el-dropdown-menu>
                   <el-dropdown-item :command="{action: 'copy', row: scope.row}">复制</el-dropdown-item>
                   <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>删除</el-dropdown-item>
                 </el-dropdown-menu>
               </template>
             </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
      </div>
    </div>

    <!-- 新增/编辑模板对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="900px" @close="resetForm">
      <el-form :model="templateForm" :rules="templateRules" ref="templateForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="templateForm.templateName" placeholder="请输入模板名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="category">
              <el-select v-model="templateForm.category" placeholder="请选择分类" filterable allow-create>
                <el-option v-for="category in categories" :key="category" :label="category" :value="category"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="适用预警类型" prop="applicableAlertTypes">
              <el-select v-model="templateForm.applicableAlertTypes" placeholder="请选择预警类型" multiple>
                <el-option v-for="alertType in alertTypes" :key="alertType" :label="alertType" :value="alertType"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priorityLevel">
              <el-select v-model="templateForm.priorityLevel" placeholder="请选择优先级">
                <el-option label="低" value="LOW"></el-option>
                <el-option label="中" value="MEDIUM"></el-option>
                <el-option label="高" value="HIGH"></el-option>
                <el-option label="紧急" value="URGENT"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="干预内容" prop="interventionContent">
          <el-input type="textarea" v-model="templateForm.interventionContent" :rows="4" placeholder="请输入干预内容"></el-input>
        </el-form-item>
        <el-form-item label="实施指南" prop="implementationGuidelines">
          <el-input type="textarea" v-model="templateForm.implementationGuidelines" :rows="4" placeholder="请输入实施指南"></el-input>
        </el-form-item>
        <el-form-item label="注意事项">
          <el-input type="textarea" v-model="templateForm.precautions" :rows="3" placeholder="请输入注意事项"></el-input>
        </el-form-item>
        <el-form-item label="预期效果">
          <el-input type="textarea" v-model="templateForm.expectedOutcome" :rows="3" placeholder="请输入预期效果"></el-input>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="templateForm.isEnabled"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveTemplate">保存</el-button>
      </div>
    </el-dialog>

    <!-- 模板详情对话框 -->
    <el-dialog title="模板详情" v-model="detailDialogVisible" width="900px">
      <div v-if="currentTemplate">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="模板ID">{{ currentTemplate.templateId }}</el-descriptions-item>
          <el-descriptions-item label="模板名称">{{ currentTemplate.templateName }}</el-descriptions-item>
          <el-descriptions-item label="分类">
            <el-tag>{{ currentTemplate.templateCategory || currentTemplate.category }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="适用预警类型">{{ currentTemplate.alertType || currentTemplate.applicableAlertTypes }}</el-descriptions-item>
          <el-descriptions-item label="优先级">
            <el-tag :type="getPriorityTagType(currentTemplate.priorityLevel)">{{ getPriorityLabel(currentTemplate.priorityLevel) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="使用次数">
            <span class="usage-count">{{ currentTemplate.usageCount || 0 }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentTemplate.isActive ? 'success' : 'danger'">{{ currentTemplate.isActive ? '启用' : '禁用' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(currentTemplate.createTime || currentTemplate.createdTime || currentTemplate.createdAt || currentTemplate.createDate) || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDateTime(currentTemplate.updateTime || currentTemplate.updatedTime || currentTemplate.updatedAt || currentTemplate.updateDate) || '暂无' }}
          </el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>干预内容</h4>
          <p>{{ currentTemplate.interventionContent || currentTemplate.templateContent || '暂无内容' }}</p>
        </div>
        <div style="margin-top: 20px;">
          <h4>实施指南</h4>
          <p>{{ currentTemplate.implementationGuidelines || currentTemplate.implementationGuide || currentTemplate.guidelines || '暂无指南' }}</p>
        </div>
        <div style="margin-top: 20px;" v-if="currentTemplate.precautions">
          <h4>注意事项</h4>
          <p>{{ currentTemplate.precautions }}</p>
        </div>
        <div style="margin-top: 20px;" v-if="currentTemplate.expectedOutcome">
          <h4>预期效果</h4>
          <p>{{ currentTemplate.expectedOutcome }}</p>
        </div>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import {
  getInterventionTemplatePage,
  getInterventionTemplateById,
  createInterventionTemplate,
  updateInterventionTemplate,
  deleteInterventionTemplate,
  getAllTemplateCategories,
  getAllAlertTypes,
  getTemplateUsageStatistics,
  enableTemplate,
  disableTemplate,
  copyInterventionTemplate,
  searchTemplatesByKeyword,

} from '@/api/intervention/template'

export default {
  name: 'InterventionTemplate',
  data() {
    return {
      loading: false,
      templateList: [],
      categories: [],
      alertTypes: [],
      searchForm: {
        templateName: '',
        category: '',
        alertType: '',
        isEnabled: null
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      statistics: {
        total: 0,
        enabled: 0,
        categories: 0,
        totalUsage: 0
      },
      dialogVisible: false,
      detailDialogVisible: false,
      isEdit: false,
      currentTemplate: null,
      templateForm: {
        templateName: '',
        category: '',
        applicableAlertTypes: [],
        priorityLevel: '',
        interventionContent: '',
        implementationGuidelines: '',
        precautions: '',
        expectedOutcome: '',
        isEnabled: true
      },
      templateRules: {
        templateName: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
        category: [{ required: true, message: '请选择分类', trigger: 'change' }],
        applicableAlertTypes: [{ required: true, message: '请选择适用预警类型', trigger: 'change' }],
        priorityLevel: [{ required: true, message: '请选择优先级', trigger: 'change' }],
        interventionContent: [{ required: true, message: '请输入干预内容', trigger: 'blur' }],
        implementationGuidelines: [{ required: true, message: '请输入实施指南', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑模板' : '新增模板'
    }
  },
  mounted() {
    this.loadTemplates()
    this.loadCategories()
    this.loadAlertTypes()
    this.loadStatistics()
  },
  methods: {
    // 加载模板列表
    async loadTemplates() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        
        // 处理数组参数
        if (params.alertType && Array.isArray(params.alertType)) {
          params.alertType = params.alertType.join(',')
        }
        
        console.log('发送的搜索参数:', params)
        console.log('模板名称参数:', params.templateName)
        
        const response = await getInterventionTemplatePage(params)
        console.log('API响应:', response)
        console.log('数据记录:', response.data?.records)
        
        if (response.code === 200 && response.data) { 
           this.templateList = (response.data.records || []).sort((a, b) => a.templateId - b.templateId)
           this.pagination.total = response.data.total || 0 
         } else { 
           this.templateList = [] 
           this.pagination.total = 0 
           this.$message.error('获取模板列表失败') 
         }
      } catch (error) {
        console.error('获取模板列表失败:', error)
        this.templateList = []
        this.pagination.total = 0
        this.$message.error('获取模板列表失败')
      } finally {
        this.loading = false
      }
    },

    // 加载分类列表
    async loadCategories() {
      try {
        const response = await getAllTemplateCategories()
        if (response.code === 200) {
          this.categories = response.data
        }
      } catch (error) {
        console.error('加载分类失败', error)
      }
    },

    // 加载预警类型列表
    async loadAlertTypes() {
      try {
        const response = await getAllAlertTypes()
        if (response.code === 200) {
          this.alertTypes = response.data
        }
      } catch (error) {
        console.error('加载预警类型失败', error)
      }
    },

    // 加载统计数据
    async loadStatistics() {
      try {
        const response = await getTemplateUsageStatistics()
        console.log('统计数据响应:', response)
        if (response.code === 200) {
          const templates = response.data
          // 手动计算统计数据
          this.statistics.total = templates.length || 0
          this.statistics.enabled = templates.filter(t => t.isActive).length || 0
          
          // 计算不同分类的数量
          const categories = new Set(templates.map(t => t.templateCategory))
          this.statistics.categories = categories.size || 0
          
          // 计算总使用次数
          this.statistics.totalUsage = templates.reduce((sum, t) => sum + (t.usageCount || 0), 0)
          
          console.log('计算后的统计数据:', this.statistics)
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
      }
    },

    // 搜索模板
    searchTemplates() {
      this.pagination.current = 1
      this.loadTemplates()
    },

    // 重置搜索
    resetSearch() {
      this.searchForm = {
        templateName: '',
        category: '',
        alertType: '',
        isEnabled: null
      }
      this.searchTemplates()
    },

    // 分页处理
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadTemplates()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadTemplates()
    },

    // 显示新增对话框
    showCreateDialog() {
      this.isEdit = false
      this.dialogVisible = true
    },

    // 编辑模板
    editTemplate(row) {
      this.isEdit = true
      // 将后端字段名映射到前端表单字段名
      this.templateForm = {
        templateId: row.templateId,
        templateName: row.templateName,
        category: row.templateCategory,
        applicableAlertTypes: row.alertType ? row.alertType.split(',') : [],
        priorityLevel: row.priorityLevel,
        interventionContent: row.templateContent,
        implementationGuidelines: row.implementationGuide,
        precautions: row.precautions,
        expectedOutcome: row.expectedOutcomes,
        isEnabled: row.isActive
      }
      this.dialogVisible = true
    },

    // 查看模板详情
    async viewTemplate(row) {
      try {
        const response = await getInterventionTemplateById(row.templateId)
        if (response.code === 200) {
          this.currentTemplate = response.data
          this.detailDialogVisible = true
        }
      } catch (error) {
        this.$message.error('获取模板详情失败')
      }
    },

    // 保存模板
    saveTemplate() {
      this.$refs.templateForm.validate(async (valid) => {
        if (valid) {
          try {
            // 将前端字段名映射到后端实体类字段名
            const formData = {
              templateName: this.templateForm.templateName,
              templateCategory: this.templateForm.category,
              alertType: Array.isArray(this.templateForm.applicableAlertTypes) 
                ? this.templateForm.applicableAlertTypes.join(',') 
                : this.templateForm.applicableAlertTypes,
              priorityLevel: this.templateForm.priorityLevel,
              templateContent: this.templateForm.interventionContent,
              implementationGuide: this.templateForm.implementationGuidelines,
              precautions: this.templateForm.precautions,
              expectedOutcomes: this.templateForm.expectedOutcome,
              isActive: this.templateForm.isEnabled
            }
            
            // 编辑时需要包含templateId
            if (this.isEdit) {
              formData.templateId = this.templateForm.templateId
            }
            
            let response
            if (this.isEdit) {
              response = await updateInterventionTemplate(this.templateForm.templateId, formData)
            } else {
              response = await createInterventionTemplate(formData)
            }
            
            if (response.code === 200) {
              this.$message.success(this.isEdit ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.loadTemplates()
              this.loadStatistics()
            }
          } catch (error) {
            console.error('保存模板失败:', error)
            this.$message.error(this.isEdit ? '更新失败' : '创建失败')
          }
        }
      })
    },

    // 重置表单
    resetForm() {
      if (this.$refs.templateForm) {
        this.$refs.templateForm.resetFields()
      }
      this.templateForm = {
        templateName: '',
        category: '',
        applicableAlertTypes: [],
        priorityLevel: '',
        interventionContent: '',
        implementationGuidelines: '',
        precautions: '',
        expectedOutcome: '',
        isEnabled: true
      }
    },

    // 切换模板状态
    async toggleTemplateStatus(row) {
      try {
        let response
        if (row.isActive) {
          response = await enableTemplate(row.templateId)
        } else {
          response = await disableTemplate(row.templateId)
        }
        if (response.code === 200) {
          this.$message.success(`${row.isActive ? '启用' : '禁用'}成功`)
          this.loadStatistics()
        } else {
          // 如果请求失败，恢复开关状态
          row.isActive = !row.isActive
        }
      } catch (error) {
        this.$message.error(`${row.isActive ? '启用' : '禁用'}失败`)
        // 如果请求失败，恢复开关状态
        row.isActive = !row.isActive
      }
    },

    // 处理下拉菜单命令
    async handleCommand(command) {
      const { action, row } = command
      
      switch (action) {
        case 'copy':
          await this.copyTemplate(row)
          break
        case 'delete':
          await this.deleteTemplate(row)
          break
      }
    },

    // 复制模板
    async copyTemplate(row) {
      this.$prompt('请输入新模板名称', '复制模板', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: `${row.templateName}_副本`
      }).then(async ({ value }) => {
        try {
          const response = await copyInterventionTemplate(row.templateId, value)
          if (response.code === 200) {
            this.$message.success('复制成功')
            this.loadTemplates()
          }
        } catch (error) {
          this.$message.error('复制失败')
        }
      })
    },

    // 查看使用统计
    async viewUsageStatistics(row) {
      try {
        const response = await getTemplateUsageStatistics(row.templateId)
        if (response.code === 200) {
          this.usageStatistics = {
            totalUsage: response.data?.totalUsage || 0,
            monthlyUsage: response.data?.monthlyUsage || 0,
            weeklyUsage: response.data?.weeklyUsage || 0,
            recentUsage: response.data?.recentUsage || []
          }
          this.usageDialogVisible = true
        } else {
          this.$message.error(response.message || '获取使用统计失败')
        }
      } catch (error) {
        console.error('获取使用统计错误:', error)
        this.$message.error('获取使用统计失败')
        // 设置默认值避免显示错误
        this.usageStatistics = {
          totalUsage: 0,
          monthlyUsage: 0,
          weeklyUsage: 0,
          recentUsage: []
        }
      }
    },

    // 删除模板
    async deleteTemplate(row) {
      this.$confirm('确定要删除这个模板吗？删除后不可恢复。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteInterventionTemplate(row.templateId)
          if (response.code === 200) {
            this.$message.success('删除成功')
            this.loadTemplates()
            this.loadStatistics()
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },



    // 获取优先级标签类型
    getPriorityTagType(priority) {
      const typeMap = {
        'LOW': 'info',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'URGENT': 'danger'
      }
      return typeMap[priority] || 'info'
    },

    // 获取优先级标签文本
    getPriorityLabel(priority) {
      const labelMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高',
        'URGENT': '紧急'
      }
      return labelMap[priority] || priority
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '暂无'
      
      // 处理时间戳格式（毫秒或秒）
      let date
      if (typeof dateTime === 'number') {
        // 如果是时间戳，判断是秒还是毫秒
        date = dateTime > 9999999999 ? new Date(dateTime) : new Date(dateTime * 1000)
      } else if (typeof dateTime === 'string') {
        // 处理字符串格式的时间
        date = new Date(dateTime.replace(/-/g, '/'))
      } else {
        date = new Date(dateTime)
      }
      
      if (isNaN(date.getTime())) {
        console.warn('无效的时间格式:', dateTime)
        return '格式错误'
      }
      
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  }
}
</script>

<style scoped>
.intervention-template {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  color: #303133;
}

.search-section {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.statistics-section {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
}

.stat-content {
  padding: 20px;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.table-section {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-section {
  margin-top: 20px;
  text-align: right;
}

.usage-badge {
  display: inline-block;
}

.usage-stat {
  text-align: center;
  padding: 20px;
}

.usage-number {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.usage-label {
  font-size: 14px;
  color: #909399;
}

.dialog-footer {
  text-align: right;
}
</style>