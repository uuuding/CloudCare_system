<template>
  <div class="intervention-plan">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>干预方案管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="showCreateDialog">新增干预方案</el-button>
        <el-button @click="showTemplateDialog">模板管理</el-button>
      </div>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-form :model="searchForm" inline>
        <el-form-item label="老人姓名">
          <el-input v-model="searchForm.elderlyName" placeholder="请输入老人姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="方案状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待启动" value="PENDING"></el-option>
            <el-option label="进行中" value="ACTIVE"></el-option>
            <el-option label="已暂停" value="PAUSED"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已取消" value="CANCELLED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="干预类型">
          <el-select v-model="searchForm.planType" placeholder="请选择类型" clearable>
            <el-option label="药物干预" value="MEDICATION"></el-option>
            <el-option label="运动干预" value="EXERCISE"></el-option>
            <el-option label="饮食干预" value="DIET"></el-option>
            <el-option label="心理干预" value="PSYCHOLOGICAL"></el-option>
            <el-option label="康复训练" value="REHABILITATION"></el-option>
            <el-option label="其他" value="OTHER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="searchForm.priorityLevel" placeholder="请选择优先级" clearable>
            <el-option label="低" value="LOW"></el-option>
            <el-option label="中" value="MEDIUM"></el-option>
            <el-option label="高" value="HIGH"></el-option>
            <el-option label="紧急" value="URGENT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchPlans">搜索</el-button>
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
              <div class="stat-label">总方案数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.active || 0 }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.completed || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.expiring || 0 }}</div>
              <div class="stat-label">即将到期</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 干预方案列表 -->
    <div class="table-section">
      <el-table :data="planList" v-loading="loading" stripe>
        <el-table-column prop="planId" label="方案ID" width="80"></el-table-column>
        <el-table-column prop="elderlyName" label="老人姓名" width="100"></el-table-column>
        <el-table-column prop="planTitle" label="干预标题" width="250" show-overflow-tooltip></el-table-column>
        <el-table-column prop="planType" label="干预类型" width="120">
          <template #default="scope">
            <el-tag :type="getTypeTagType(scope.row.planType)">{{ getTypeLabel(scope.row.planType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priorityLevel" label="优先级" width="100">
          <template #default="scope">
            <el-tag :type="getPriorityTagType(scope.row.priorityLevel)">{{ getPriorityLabel(scope.row.priorityLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="150">
          <template #default="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="110"></el-table-column>
        <el-table-column prop="endDate" label="结束日期" width="110"></el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="viewPlan(scope.row)">查看</el-button>
            <el-button size="small" type="primary" @click="editPlan(scope.row)" v-if="scope.row.status === 'PENDING'">编辑</el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="small" type="text">更多<el-icon class="el-icon--right"><arrow-down /></el-icon></el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="{action: 'start', row: scope.row}" v-if="scope.row.status === 'PENDING'">启动</el-dropdown-item>
                  <el-dropdown-item :command="{action: 'pause', row: scope.row}" v-if="scope.row.status === 'ACTIVE'">暂停</el-dropdown-item>
                  <el-dropdown-item :command="{action: 'resume', row: scope.row}" v-if="scope.row.status === 'PAUSED'">恢复</el-dropdown-item>
                  <el-dropdown-item :command="{action: 'complete', row: scope.row}" v-if="['ACTIVE', 'PAUSED'].includes(scope.row.status)">完成</el-dropdown-item>
                  <el-dropdown-item :command="{action: 'cancel', row: scope.row}" v-if="['PENDING', 'ACTIVE', 'PAUSED'].includes(scope.row.status)">取消</el-dropdown-item>
                  <el-dropdown-item :command="{action: 'copy', row: scope.row}">复制</el-dropdown-item>
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

    <!-- 查看干预方案详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="查看干预方案详情" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="方案ID">{{ viewPlanData.planId }}</el-descriptions-item>
        <el-descriptions-item label="老人姓名">{{ viewPlanData.elderlyName }}</el-descriptions-item>
        <el-descriptions-item label="干预标题" :span="2">{{ viewPlanData.planTitle }}</el-descriptions-item>
        <el-descriptions-item label="干预类型">
          <el-tag :type="getTypeTagType(viewPlanData.planType)">{{ getTypeLabel(viewPlanData.planType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="优先级">
          <el-tag :type="getPriorityTagType(viewPlanData.priorityLevel)">{{ getPriorityLabel(viewPlanData.priorityLevel) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="执行频率">{{ getFrequencyLabel(viewPlanData.executionFrequency) }}</el-descriptions-item>
        <el-descriptions-item label="持续天数">{{ viewPlanData.durationDays }}天</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ viewPlanData.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ viewPlanData.endDate }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="getStatusTagType(viewPlanData.status)">{{ getStatusLabel(viewPlanData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDateTime(viewPlanData.createdTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="干预内容" :span="2">
          <div class="content-text">{{ viewPlanData.interventionContent }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="实施方法" :span="2">
          <div class="content-text">{{ viewPlanData.implementationMethod }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="注意事项" :span="2">
          <div class="content-text">{{ viewPlanData.notes }}</div>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="editFromView" v-if="viewPlanData.status === 'PENDING'">编辑</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 新增/编辑干预方案对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px" @close="resetForm">
      <el-form :model="planForm" :rules="planRules" ref="planFormRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="选择老人" prop="elderlyId">
              <el-select v-model="planForm.elderlyId" placeholder="请选择老人" filterable @change="handleElderlyChange">
                <el-option
                  v-for="elderly in elderlyList"
                  :key="elderly.id"
                  :label="elderly.name"
                  :value="elderly.id">
                </el-option>
              </el-select>
              <!-- 调试信息 -->
              <div v-if="elderlyList.length === 0" style="color: #999; font-size: 12px; margin-top: 5px;">
                暂无老人数据，请检查API接口
              </div>
              <div v-else style="color: #999; font-size: 12px; margin-top: 5px;">
                已加载 {{ elderlyList.length }} 位老人
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="干预标题" prop="planTitle">
              <el-input v-model="planForm.planTitle" placeholder="请输入干预标题"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="干预类型" prop="planType">
              <el-select v-model="planForm.planType" placeholder="请选择干预类型">
                <el-option label="药物干预" value="MEDICATION"></el-option>
                <el-option label="运动干预" value="EXERCISE"></el-option>
                <el-option label="饮食干预" value="DIET"></el-option>
                <el-option label="心理干预" value="PSYCHOLOGICAL"></el-option>
                <el-option label="康复训练" value="REHABILITATION"></el-option>
                <el-option label="其他" value="OTHER"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="优先级" prop="priorityLevel">
              <el-select v-model="planForm.priorityLevel" placeholder="请选择优先级">
                <el-option label="低" value="LOW"></el-option>
                <el-option label="中" value="MEDIUM"></el-option>
                <el-option label="高" value="HIGH"></el-option>
                <el-option label="紧急" value="URGENT"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行频率" prop="executionFrequency">
              <el-select v-model="planForm.executionFrequency" placeholder="请选择执行频率">
                <el-option label="每日一次" value="DAILY"></el-option>
                <el-option label="每周一次" value="WEEKLY"></el-option>
                <el-option label="每月一次" value="MONTHLY"></el-option>
                <el-option label="按需执行" value="AS_NEEDED"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="planForm.startDate" type="date" placeholder="请选择开始日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker v-model="planForm.endDate" type="date" placeholder="请选择结束日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="目标指标" prop="targetMetrics">
          <el-input v-model="planForm.targetMetrics" placeholder="请输入目标指标"></el-input>
        </el-form-item>
        <el-form-item label="干预内容" prop="interventionContent">
          <el-input v-model="planForm.interventionContent" type="textarea" :rows="3" placeholder="请输入干预内容"></el-input>
        </el-form-item>
        <el-form-item label="实施方法" prop="implementationMethod">
          <el-input v-model="planForm.implementationMethod" type="textarea" :rows="3" placeholder="请输入实施方法"></el-input>
        </el-form-item>
        <el-form-item label="注意事项" prop="notes">
          <el-input v-model="planForm.notes" type="textarea" :rows="2" placeholder="请输入注意事项"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'
import {
  getInterventionPlanPage,
  createInterventionPlan,
  updateInterventionPlan,
  deleteInterventionPlan,
  startInterventionPlan,
  pauseInterventionPlan,
  completeInterventionPlan,
  cancelInterventionPlan,
  copyInterventionPlan,
  getInterventionPlanStatsByStatus
} from '@/api/intervention/plan'
import { getAllElderlyProfiles } from '@/api/elderlyProfile'

export default {
  name: 'InterventionPlan',
  components: {
    ArrowDown
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      dialogTitle: '新增干预方案',
      viewDialogVisible: false,
      viewPlanData: {},
      
      // 搜索表单
      searchForm: {
        elderlyName: '',
        status: '',
        planType: '',
        priorityLevel: ''
      },
      
      // 分页数据
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      
      // 统计数据
      statistics: {
        total: 0,
        active: 0,
        completed: 0,
        expiring: 0
      },
      
      // 方案列表
      planList: [],
      
      // 表单数据
      planForm: {
        planId: null,
        elderlyId: null,
        elderlyName: '',
        planTitle: '',
        planType: '',
        priorityLevel: '',
        targetMetrics: '',
        interventionContent: '',
        implementationMethod: '',
        executionFrequency: '',
        startDate: '',
        endDate: '',
        notes: ''
      },
      
      // 老人列表
      elderlyList: [],
      
      // 表单验证规则
      planRules: {
        elderlyId: [{ required: true, message: '请选择老人', trigger: 'change' }],
        planTitle: [{ required: true, message: '请输入干预标题', trigger: 'blur' }],
        planType: [{ required: true, message: '请选择干预类型', trigger: 'change' }],
        priorityLevel: [{ required: true, message: '请选择优先级', trigger: 'change' }],
        targetMetrics: [{ required: true, message: '请输入目标指标', trigger: 'blur' }],
        interventionContent: [{ required: true, message: '请输入干预内容', trigger: 'blur' }],
        executionFrequency: [{ required: true, message: '请选择执行频率', trigger: 'change' }],
        startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
        endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
      }
    }
  },
  
  mounted() {
    this.fetchPlans()
    this.fetchStatistics()
    this.fetchElderlyList()
  },
  
  methods: {

    // 获取方案列表
    async fetchPlans() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        const response = await getInterventionPlanPage(params)
        if (response.code === 200) {
          this.planList = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        ElMessage.error('获取方案列表失败')
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async fetchStatistics() {
      try {
        const response = await getInterventionPlanStatsByStatus()
        if (response.code === 200) {
          const stats = response.data || []
          this.statistics.total = stats.reduce((sum, item) => sum + (item.count || 0), 0)
          this.statistics.active = stats.find(item => item.status === 'ACTIVE')?.count || 0
          this.statistics.completed = stats.find(item => item.status === 'COMPLETED')?.count || 0
          this.statistics.expiring = 0 // 需要单独接口获取
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },

    // 搜索方案
    searchPlans() {
      this.pagination.current = 1
      this.fetchPlans()
    },

    // 重置搜索
    resetSearch() {
      Object.assign(this.searchForm, {
        elderlyName: '',
        status: '',
        planType: '',
        priorityLevel: ''
      })
      this.searchPlans()
    },

    // 分页处理
    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchPlans()
    },

    handleCurrentChange(current) {
      this.pagination.current = current
      this.fetchPlans()
    },

    // 显示创建对话框
    showCreateDialog() {
      this.dialogTitle = '新增干预方案'
      this.resetForm()
      this.dialogVisible = true
    },

    // 显示模板对话框
    showTemplateDialog() {
      this.$router.push('/intervention/template')
    },

    // 重置表单
    resetForm() {
      Object.assign(this.planForm, {
        planId: null,
        elderlyId: null,
        elderlyName: '',
        planTitle: '',
        planType: '',
        priorityLevel: '',
        targetMetrics: '',
        interventionContent: '',
        implementationMethod: '',
        executionFrequency: '',
        startDate: '',
        endDate: '',
        notes: ''
      })
      if (this.$refs.planFormRef) {
        this.$refs.planFormRef.clearValidate()
      }
    },

    // 获取老人列表
    async fetchElderlyList() {
      try {
        const response = await getAllElderlyProfiles()
        console.log('API响应:', response)
        if (response && response.code === 200) {
          // 处理不同的数据结构
          let elderlyData = response.data || []
          if (Array.isArray(elderlyData)) {
            this.elderlyList = elderlyData
          } else if (elderlyData.records && Array.isArray(elderlyData.records)) {
            this.elderlyList = elderlyData.records
          } else {
            this.elderlyList = []
          }
          console.log('老人列表:', this.elderlyList)
        } else {
          console.warn('API返回异常:', response)
          this.elderlyList = []
        }
      } catch (error) {
        console.error('获取老人列表失败:', error)
        this.elderlyList = []
      }
    },

    // 处理老人选择
    handleElderlyChange(elderlyId) {
      const selectedElderly = this.elderlyList.find(item => item.id === elderlyId)
      if (selectedElderly) {
        this.planForm.elderlyName = selectedElderly.name
        // 确保elderlyId是数字类型
        this.planForm.elderlyId = parseInt(elderlyId)
        console.log('选中的老人:', selectedElderly)
        console.log('设置的elderlyId:', this.planForm.elderlyId)
      }
    },

    // 提交表单
    async submitForm() {
      if (!this.$refs.planFormRef) return
      
      try {
        await this.$refs.planFormRef.validate()
        this.submitLoading = true
        
        const isEdit = !!this.planForm.planId
        const apiCall = isEdit ? updateInterventionPlan : createInterventionPlan
        const params = isEdit ? [this.planForm.planId, this.planForm] : [this.planForm]
        
        const response = await apiCall(...params)
        if (response.code === 200) {
          ElMessage.success(isEdit ? '更新成功' : '创建成功')
          this.dialogVisible = false
          this.fetchPlans()
          this.fetchStatistics()
        } else {
          ElMessage.error(response.message || '操作失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('操作失败')
        }
      } finally {
        this.submitLoading = false
      }
    },

    // 查看方案
    viewPlan(row) {
      this.viewPlanData = { ...row }
      this.viewDialogVisible = true
    },

    // 从查看页面进入编辑
    editFromView() {
      this.viewDialogVisible = false
      this.editPlan(this.viewPlanData)
    },

    // 编辑方案
    editPlan(row) {
      this.dialogTitle = '编辑干预方案'
      Object.assign(this.planForm, row)
      this.dialogVisible = true
    },

    // 处理下拉菜单命令
    async handleCommand(command) {
      const { action, row } = command
      
      switch (action) {
        case 'start':
          await this.startPlan(row)
          break
        case 'pause':
          await this.pausePlan(row)
          break
        case 'resume':
          await this.startPlan(row)
          break
        case 'complete':
          await this.completePlan(row)
          break
        case 'cancel':
          await this.cancelPlan(row)
          break
        case 'copy':
          await this.copyPlan(row)
          break
        case 'execution':
          ElMessage.info('执行记录功能开发中...')
          break
      }
    },

    // 启动方案
    async startPlan(row) {
      try {
        const response = await startInterventionPlan(row.planId)
        if (response.code === 200) {
          ElMessage.success('启动成功')
          this.fetchPlans()
          this.fetchStatistics()
        } else {
          ElMessage.error(response.message || '启动失败')
        }
      } catch (error) {
        ElMessage.error('启动失败')
      }
    },

    // 暂停方案
    async pausePlan(row) {
      try {
        const response = await pauseInterventionPlan(row.planId)
        if (response.code === 200) {
          ElMessage.success('暂停成功')
          this.fetchPlans()
          this.fetchStatistics()
        } else {
          ElMessage.error(response.message || '暂停失败')
        }
      } catch (error) {
        ElMessage.error('暂停失败')
      }
    },

    // 完成方案
    async completePlan(row) {
      try {
        const { value: score } = await ElMessageBox.prompt('请输入效果评分(1-10)', '完成方案', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /^([1-9]|10)$/,
          inputErrorMessage: '请输入1-10的数字'
        })
        
        const response = await completeInterventionPlan(row.planId, score)
        if (response.code === 200) {
          ElMessage.success('完成成功')
          this.fetchPlans()
          this.fetchStatistics()
        } else {
          ElMessage.error(response.message || '完成失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('完成失败')
        }
      }
    },

    // 取消方案
    async cancelPlan(row) {
      try {
        const { value: reason } = await ElMessageBox.prompt('请输入取消原因', '取消方案', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
        
        const response = await cancelInterventionPlan(row.planId, reason)
        if (response.code === 200) {
          ElMessage.success('取消成功')
          this.fetchPlans()
          this.fetchStatistics()
        } else {
          ElMessage.error(response.message || '取消失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('取消失败')
        }
      }
    },

    // 复制方案
    async copyPlan(row) {
      try {
        const { value: elderlyName } = await ElMessageBox.prompt('请输入目标老人姓名', '复制方案', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
        
        const response = await copyInterventionPlan(row.planId, row.elderlyId, elderlyName)
        if (response.code === 200) {
          ElMessage.success('复制成功')
          this.fetchPlans()
          this.fetchStatistics()
        } else {
          ElMessage.error(response.message || '复制失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('复制失败')
        }
      }
    },

    // 获取类型标签类型
    getTypeTagType(type) {
      const typeMap = {
        'MEDICATION': 'danger',
        'EXERCISE': 'success',
        'DIET': 'warning',
        'PSYCHOLOGICAL': 'info',
        'REHABILITATION': 'primary',
        'OTHER': ''
      }
      return typeMap[type] || ''
    },

    // 获取类型标签文本
    getTypeLabel(type) {
      const typeMap = {
        'MEDICATION': '药物干预',
        'EXERCISE': '运动干预',
        'DIET': '饮食干预',
        'PSYCHOLOGICAL': '心理干预',
        'REHABILITATION': '康复训练',
        'OTHER': '其他'
      }
      return typeMap[type] || type
    },

    // 获取优先级标签类型
    getPriorityTagType(priority) {
      const priorityMap = {
        'LOW': 'info',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'URGENT': 'danger'
      }
      return priorityMap[priority] || ''
    },

    // 获取优先级标签文本
    getPriorityLabel(priority) {
      const priorityMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高',
        'URGENT': '紧急'
      }
      return priorityMap[priority] || priority
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'ACTIVE': 'primary',
        'PAUSED': 'warning',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || ''
    },

    // 获取状态标签文本
    getStatusLabel(status) {
      const statusMap = {
        'PENDING': '待启动',
        'ACTIVE': '进行中',
        'PAUSED': '已暂停',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },

    // 获取执行频率标签
    getFrequencyLabel(frequency) {
      const frequencyMap = {
        'DAILY': '每日一次',
        'WEEKLY': '每周一次',
        'MONTHLY': '每月一次',
        'AS_NEEDED': '按需执行'
      }
      return frequencyMap[frequency] || frequency
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      const date = new Date(dateTime)
      if (isNaN(date.getTime())) return '-'
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
.intervention-plan {
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: calc(100vh - 60px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.3);
  color: white;
}

.page-header h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-actions {
  display: flex;
  gap: 12px;
}

.header-actions .el-button {
  border-radius: 20px;
  padding: 12px 24px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.header-actions .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

.search-section {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.statistics-section {
  margin-bottom: 24px;
}

.stat-card {
  text-align: center;
  border: none;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  overflow: hidden;
  position: relative;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea, #764ba2);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.stat-content {
  padding: 24px;
}

.stat-number {
  font-size: 36px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 8px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

.table-section {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.pagination-section {
  margin-top: 24px;
  text-align: right;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-table) {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

:deep(.el-table th) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  color: #374151;
  font-weight: 600;
  border: none;
}

:deep(.el-table td) {
  border: none;
  border-bottom: 1px solid #f1f5f9;
}

:deep(.el-table tbody tr:hover) {
  background-color: #f8fafc;
}

:deep(.el-progress-bar__outer) {
  background-color: #e5e7eb;
  border-radius: 10px;
}

:deep(.el-progress-bar__inner) {
  background: linear-gradient(90deg, #10b981, #059669);
  border-radius: 10px;
}

:deep(.el-tag) {
  border-radius: 20px;
  padding: 4px 12px;
  font-weight: 500;
  border: none;
}

:deep(.el-button) {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

:deep(.el-button:hover) {
  transform: translateY(-1px);
}

:deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px 24px;
}

:deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}

:deep(.el-textarea__inner) {
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.el-textarea__inner:hover) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.content-text {
  line-height: 1.6;
  color: #606266;
  white-space: pre-wrap;
  word-break: break-word;
  max-height: 120px;
  overflow-y: auto;
}

:deep(.el-date-editor) {
  border-radius: 8px;
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.search-section,
.statistics-section,
.table-section {
  animation: fadeInUp 0.6s ease-out;
}

.stat-card:nth-child(1) { animation-delay: 0.1s; }
.stat-card:nth-child(2) { animation-delay: 0.2s; }
.stat-card:nth-child(3) { animation-delay: 0.3s; }
.stat-card:nth-child(4) { animation-delay: 0.4s; }
</style>