<template>
  <div class="intervention-execution">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>干预执行记录</h1>
      <div class="header-actions">
        <el-button type="primary" @click="showCreateDialog">新增执行记录</el-button>
        <el-button @click="batchCreateExecution">批量创建执行计划</el-button>
      </div>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-form :model="searchForm" inline>
        <el-form-item label="方案ID">
          <el-input v-model="searchForm.planId" placeholder="请输入方案ID" clearable></el-input>
        </el-form-item>
        <el-form-item label="执行状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待执行" value="PENDING"></el-option>
            <el-option label="执行中" value="IN_PROGRESS"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已跳过" value="SKIPPED"></el-option>
            <el-option label="逾期" value="OVERDUE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="执行日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchExecutions">搜索</el-button>
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
              <div class="stat-label">总执行记录</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.todayPending || 0 }}</div>
              <div class="stat-label">今日待执行</div>
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
              <div class="stat-number">{{ statistics.overdue || 0 }}</div>
              <div class="stat-label">逾期未执行</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 执行记录列表 -->
    <div class="table-section">
      <el-table :data="executionList" v-loading="loading" stripe>
        <el-table-column prop="executionId" label="执行ID" width="80"></el-table-column>
        <el-table-column prop="planId" label="方案ID" width="80"></el-table-column>
        <el-table-column prop="planType" label="干预类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeTagType(scope.row.planType)">{{ getTypeLabel(scope.row.planType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executionDate" label="执行日期" width="100"></el-table-column>
        <el-table-column prop="executionTime" label="执行时间" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="completionRate" label="完成率" width="100">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.completionRate || 0" :stroke-width="6"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="effectivenessRating" label="效果评分" width="100">
          <template slot-scope="scope">
            <el-rate v-model="scope.row.effectivenessRating" disabled show-score></el-rate>
          </template>
        </el-table-column>
        <el-table-column prop="executorName" label="执行人" width="100"></el-table-column>
        <el-table-column prop="executionLocation" label="执行地点" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="patientFeedback" label="患者反馈" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewExecution(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="editExecution(scope.row)" v-if="scope.row.status === 'PENDING'">编辑</el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini">更多<i class="el-icon-arrow-down el-icon--right"></i></el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'complete', row: scope.row}" v-if="['PENDING', 'IN_PROGRESS'].includes(scope.row.status)">完成执行</el-dropdown-item>
                <el-dropdown-item :command="{action: 'skip', row: scope.row}" v-if="scope.row.status === 'PENDING'">跳过执行</el-dropdown-item>
                <el-dropdown-item :command="{action: 'vitals', row: scope.row}">生命体征</el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" v-if="scope.row.status === 'PENDING'">删除</el-dropdown-item>
              </el-dropdown-menu>
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

    <!-- 新增/编辑执行记录对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" @close="resetForm">
      <el-form :model="executionForm" :rules="executionRules" ref="executionForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="方案ID" prop="planId">
              <el-input v-model="executionForm.planId" placeholder="请输入方案ID" :disabled="isEdit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行日期" prop="executionDate">
              <el-date-picker v-model="executionForm.executionDate" type="date" placeholder="选择执行日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="执行时间" prop="executionTime">
              <el-time-picker v-model="executionForm.executionTime" placeholder="选择执行时间" format="HH:mm" value-format="HH:mm"></el-time-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行状态" prop="status">
              <el-select v-model="executionForm.status" placeholder="请选择状态">
                <el-option label="待执行" value="PENDING"></el-option>
                <el-option label="执行中" value="IN_PROGRESS"></el-option>
                <el-option label="已完成" value="COMPLETED"></el-option>
                <el-option label="已跳过" value="SKIPPED"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="完成率" prop="completionRate">
              <el-slider v-model="executionForm.completionRate" :min="0" :max="100" show-input></el-slider>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="效果评分" prop="effectivenessRating">
              <el-rate v-model="executionForm.effectivenessRating" show-score></el-rate>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="执行人" prop="executorName">
              <el-input v-model="executionForm.executorName" placeholder="请输入执行人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行地点" prop="executionLocation">
              <el-input v-model="executionForm.executionLocation" placeholder="请输入执行地点"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="执行详情" prop="executionDetails">
          <el-input type="textarea" v-model="executionForm.executionDetails" :rows="3" placeholder="请输入执行详情"></el-input>
        </el-form-item>
        <el-form-item label="副作用">
          <el-input type="textarea" v-model="executionForm.sideEffects" :rows="2" placeholder="请输入副作用"></el-input>
        </el-form-item>
        <el-form-item label="患者反馈">
          <el-input type="textarea" v-model="executionForm.patientFeedback" :rows="2" placeholder="请输入患者反馈"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="executionForm.remarks" :rows="2" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveExecution">保存</el-button>
      </div>
    </el-dialog>

    <!-- 执行记录详情对话框 -->
    <el-dialog title="执行记录详情" :visible.sync="detailDialogVisible" width="900px">
      <div v-if="currentExecution">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="执行ID">{{ currentExecution.executionId }}</el-descriptions-item>
          <el-descriptions-item label="方案ID">{{ currentExecution.planId }}</el-descriptions-item>
          <el-descriptions-item label="执行日期">{{ currentExecution.executionDate }}</el-descriptions-item>
          <el-descriptions-item label="执行时间">{{ currentExecution.executionTime }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTagType(currentExecution.status)">{{ getStatusLabel(currentExecution.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="完成率">
            <el-progress :percentage="currentExecution.completionRate || 0"></el-progress>
          </el-descriptions-item>
          <el-descriptions-item label="效果评分">
            <el-rate v-model="currentExecution.effectivenessRating" disabled show-score></el-rate>
          </el-descriptions-item>
          <el-descriptions-item label="执行人">{{ currentExecution.executorName }}</el-descriptions-item>
          <el-descriptions-item label="执行地点">{{ currentExecution.executionLocation }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentExecution.createTime }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>执行详情</h4>
          <p>{{ currentExecution.executionDetails }}</p>
        </div>
        <div style="margin-top: 20px;" v-if="currentExecution.sideEffects">
          <h4>副作用</h4>
          <p>{{ currentExecution.sideEffects }}</p>
        </div>
        <div style="margin-top: 20px;" v-if="currentExecution.patientFeedback">
          <h4>患者反馈</h4>
          <p>{{ currentExecution.patientFeedback }}</p>
        </div>
        <div style="margin-top: 20px;" v-if="currentExecution.preVitalSigns || currentExecution.postVitalSigns">
          <h4>生命体征</h4>
          <el-row :gutter="20">
            <el-col :span="12" v-if="currentExecution.preVitalSigns">
              <h5>执行前</h5>
              <pre>{{ currentExecution.preVitalSigns }}</pre>
            </el-col>
            <el-col :span="12" v-if="currentExecution.postVitalSigns">
              <h5>执行后</h5>
              <pre>{{ currentExecution.postVitalSigns }}</pre>
            </el-col>
          </el-row>
        </div>
        <div style="margin-top: 20px;" v-if="currentExecution.remarks">
          <h4>备注</h4>
          <p>{{ currentExecution.remarks }}</p>
        </div>
      </div>
    </el-dialog>

    <!-- 生命体征对话框 -->
    <el-dialog title="生命体征记录" :visible.sync="vitalsDialogVisible" width="600px">
      <el-form :model="vitalsForm" label-width="120px">
        <h4>执行前生命体征</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="血压">
              <el-input v-model="vitalsForm.preBloodPressure" placeholder="如：120/80"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="心率">
              <el-input v-model="vitalsForm.preHeartRate" placeholder="次/分钟"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="体温">
              <el-input v-model="vitalsForm.preTemperature" placeholder="℃"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血氧">
              <el-input v-model="vitalsForm.preOxygenSaturation" placeholder="%"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <h4 style="margin-top: 30px;">执行后生命体征</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="血压">
              <el-input v-model="vitalsForm.postBloodPressure" placeholder="如：120/80"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="心率">
              <el-input v-model="vitalsForm.postHeartRate" placeholder="次/分钟"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="体温">
              <el-input v-model="vitalsForm.postTemperature" placeholder="℃"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血氧">
              <el-input v-model="vitalsForm.postOxygenSaturation" placeholder="%"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="vitalsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveVitals">保存</el-button>
      </div>
    </el-dialog>

    <!-- 批量创建执行计划对话框 -->
    <el-dialog title="批量创建执行计划" :visible.sync="batchDialogVisible" width="600px">
      <el-form :model="batchForm" label-width="120px">
        <el-form-item label="方案ID" required>
          <el-input v-model="batchForm.planId" placeholder="请输入方案ID"></el-input>
        </el-form-item>
        <el-form-item label="开始日期" required>
          <el-date-picker v-model="batchForm.startDate" type="date" placeholder="选择开始日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" required>
          <el-date-picker v-model="batchForm.endDate" type="date" placeholder="选择结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
        </el-form-item>
        <el-form-item label="执行频率" required>
          <el-select v-model="batchForm.frequency" placeholder="请选择执行频率">
            <el-option label="每日一次" value="DAILY"></el-option>
            <el-option label="每周一次" value="WEEKLY"></el-option>
            <el-option label="每月一次" value="MONTHLY"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="执行时间">
          <el-time-picker v-model="batchForm.executionTime" placeholder="选择执行时间" format="HH:mm" value-format="HH:mm"></el-time-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveBatchExecution">创建</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getInterventionExecutionPage,
  getInterventionExecutionById,
  createInterventionExecution,
  updateInterventionExecution,
  deleteInterventionExecution,
  completeInterventionExecution,
  skipInterventionExecution,
  getInterventionExecutionStatsByStatus,
  getTodayPendingExecutions,
  getOverdueExecutions,
  batchCreateExecutionPlan,
  updateVitalSigns
} from '@/api/intervention/execution'

export default {
  name: 'InterventionExecution',
  data() {
    return {
      loading: false,
      executionList: [],
      searchForm: {
        planId: '',
        status: '',
        dateRange: []
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      statistics: {
        total: 0,
        todayPending: 0,
        completed: 0,
        overdue: 0
      },
      dialogVisible: false,
      detailDialogVisible: false,
      vitalsDialogVisible: false,
      batchDialogVisible: false,
      isEdit: false,
      currentExecution: null,
      executionForm: {
        planId: '',
        executionDate: '',
        executionTime: '',
        status: 'PENDING',
        completionRate: 0,
        effectivenessRating: 0,
        executorName: '',
        executionLocation: '',
        executionDetails: '',
        sideEffects: '',
        patientFeedback: '',
        remarks: ''
      },
      vitalsForm: {
        preBloodPressure: '',
        preHeartRate: '',
        preTemperature: '',
        preOxygenSaturation: '',
        postBloodPressure: '',
        postHeartRate: '',
        postTemperature: '',
        postOxygenSaturation: ''
      },
      batchForm: {
        planId: '',
        startDate: '',
        endDate: '',
        frequency: '',
        executionTime: ''
      },
      executionRules: {
        planId: [{ required: true, message: '请输入方案ID', trigger: 'blur' }],
        executionDate: [{ required: true, message: '请选择执行日期', trigger: 'change' }],
        executionTime: [{ required: true, message: '请选择执行时间', trigger: 'change' }],
        status: [{ required: true, message: '请选择执行状态', trigger: 'change' }],
        executorName: [{ required: true, message: '请输入执行人', trigger: 'blur' }],
        executionDetails: [{ required: true, message: '请输入执行详情', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑执行记录' : '新增执行记录'
    }
  },
  mounted() {
    // 检查路由参数
    if (this.$route.query.planId) {
      this.searchForm.planId = this.$route.query.planId
    }
    this.loadExecutions()
    this.loadStatistics()
  },
  methods: {
    // 加载执行记录列表
    async loadExecutions() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        
        // 处理日期范围
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }
        delete params.dateRange
        
        const response = await getInterventionExecutionPage(params)
        if (response.success) {
          this.executionList = response.data.records
          this.pagination.total = response.data.total
        }
      } catch (error) {
        this.$message.error('加载执行记录失败')
      } finally {
        this.loading = false
      }
    },

    // 加载统计数据
    async loadStatistics() {
      try {
        const [statusStats, todayPending, overdue] = await Promise.all([
          getInterventionExecutionStatsByStatus(),
          getTodayPendingExecutions(),
          getOverdueExecutions()
        ])
        
        if (statusStats.success) {
          const stats = statusStats.data
          this.statistics.total = stats.reduce((sum, item) => sum + item.count, 0)
          this.statistics.completed = stats.find(item => item.status === 'COMPLETED')?.count || 0
        }
        
        if (todayPending.success) {
          this.statistics.todayPending = todayPending.data.length
        }
        
        if (overdue.success) {
          this.statistics.overdue = overdue.data.length
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
      }
    },

    // 搜索执行记录
    searchExecutions() {
      this.pagination.current = 1
      this.loadExecutions()
    },

    // 重置搜索
    resetSearch() {
      this.searchForm = {
        planId: '',
        status: '',
        dateRange: []
      }
      this.searchExecutions()
    },

    // 分页处理
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadExecutions()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadExecutions()
    },

    // 显示新增对话框
    showCreateDialog() {
      this.isEdit = false
      this.dialogVisible = true
    },

    // 编辑执行记录
    editExecution(row) {
      this.isEdit = true
      this.executionForm = { ...row }
      this.dialogVisible = true
    },

    // 查看执行记录详情
    async viewExecution(row) {
      try {
        const response = await getInterventionExecutionById(row.executionId)
        if (response.success) {
          this.currentExecution = response.data
          this.detailDialogVisible = true
        }
      } catch (error) {
        this.$message.error('获取执行记录详情失败')
      }
    },

    // 保存执行记录
    saveExecution() {
      this.$refs.executionForm.validate(async (valid) => {
        if (valid) {
          try {
            let response
            if (this.isEdit) {
              response = await updateInterventionExecution(this.executionForm.executionId, this.executionForm)
            } else {
              response = await createInterventionExecution(this.executionForm)
            }
            
            if (response.success) {
              this.$message.success(this.isEdit ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.loadExecutions()
              this.loadStatistics()
            }
          } catch (error) {
            this.$message.error(this.isEdit ? '更新失败' : '创建失败')
          }
        }
      })
    },

    // 重置表单
    resetForm() {
      if (this.$refs.executionForm) {
        this.$refs.executionForm.resetFields()
      }
      this.executionForm = {
        planId: '',
        executionDate: '',
        executionTime: '',
        status: 'PENDING',
        completionRate: 0,
        effectivenessRating: 0,
        executorName: '',
        executionLocation: '',
        executionDetails: '',
        sideEffects: '',
        patientFeedback: '',
        remarks: ''
      }
    },

    // 处理下拉菜单命令
    async handleCommand(command) {
      const { action, row } = command
      
      switch (action) {
        case 'complete':
          await this.completeExecution(row)
          break
        case 'skip':
          await this.skipExecution(row)
          break
        case 'vitals':
          this.showVitalsDialog(row)
          break
        case 'delete':
          await this.deleteExecution(row)
          break
      }
    },

    // 完成执行
    async completeExecution(row) {
      this.$prompt('请输入完成率(0-100)', '完成执行', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^(100|[1-9]?\d)$/,
        inputErrorMessage: '请输入0-100的数字'
      }).then(async ({ value }) => {
        try {
          const response = await completeInterventionExecution(row.executionId, { completionRate: value })
          if (response.success) {
            this.$message.success('完成成功')
            this.loadExecutions()
            this.loadStatistics()
          }
        } catch (error) {
          this.$message.error('完成失败')
        }
      })
    },

    // 跳过执行
    async skipExecution(row) {
      this.$prompt('请输入跳过原因', '跳过执行', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(async ({ value }) => {
        try {
          const response = await skipInterventionExecution(row.executionId, { reason: value })
          if (response.success) {
            this.$message.success('跳过成功')
            this.loadExecutions()
            this.loadStatistics()
          }
        } catch (error) {
          this.$message.error('跳过失败')
        }
      })
    },

    // 显示生命体征对话框
    showVitalsDialog(row) {
      this.currentExecution = row
      // 解析现有的生命体征数据
      if (row.preVitalSigns) {
        try {
          const preVitals = JSON.parse(row.preVitalSigns)
          this.vitalsForm.preBloodPressure = preVitals.bloodPressure || ''
          this.vitalsForm.preHeartRate = preVitals.heartRate || ''
          this.vitalsForm.preTemperature = preVitals.temperature || ''
          this.vitalsForm.preOxygenSaturation = preVitals.oxygenSaturation || ''
        } catch (e) {
          console.error('解析执行前生命体征失败', e)
        }
      }
      if (row.postVitalSigns) {
        try {
          const postVitals = JSON.parse(row.postVitalSigns)
          this.vitalsForm.postBloodPressure = postVitals.bloodPressure || ''
          this.vitalsForm.postHeartRate = postVitals.heartRate || ''
          this.vitalsForm.postTemperature = postVitals.temperature || ''
          this.vitalsForm.postOxygenSaturation = postVitals.oxygenSaturation || ''
        } catch (e) {
          console.error('解析执行后生命体征失败', e)
        }
      }
      this.vitalsDialogVisible = true
    },

    // 保存生命体征
    async saveVitals() {
      try {
        const preVitals = {
          bloodPressure: this.vitalsForm.preBloodPressure,
          heartRate: this.vitalsForm.preHeartRate,
          temperature: this.vitalsForm.preTemperature,
          oxygenSaturation: this.vitalsForm.preOxygenSaturation
        }
        
        const postVitals = {
          bloodPressure: this.vitalsForm.postBloodPressure,
          heartRate: this.vitalsForm.postHeartRate,
          temperature: this.vitalsForm.postTemperature,
          oxygenSaturation: this.vitalsForm.postOxygenSaturation
        }
        
        const response = await updateVitalSigns(this.currentExecution.executionId, {
          preVitalSigns: JSON.stringify(preVitals),
          postVitalSigns: JSON.stringify(postVitals)
        })
        
        if (response.success) {
          this.$message.success('保存成功')
          this.vitalsDialogVisible = false
          this.loadExecutions()
        }
      } catch (error) {
        this.$message.error('保存失败')
      }
    },

    // 删除执行记录
    async deleteExecution(row) {
      this.$confirm('确定要删除这条执行记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteInterventionExecution(row.executionId)
          if (response.success) {
            this.$message.success('删除成功')
            this.loadExecutions()
            this.loadStatistics()
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 批量创建执行计划
    batchCreateExecution() {
      this.batchDialogVisible = true
    },

    // 保存批量执行计划
    async saveBatchExecution() {
      if (!this.batchForm.planId || !this.batchForm.startDate || !this.batchForm.endDate || !this.batchForm.frequency) {
        this.$message.error('请填写完整信息')
        return
      }
      
      try {
        const response = await batchCreateExecutionPlan(this.batchForm)
        if (response.success) {
          this.$message.success('批量创建成功')
          this.batchDialogVisible = false
          this.loadExecutions()
          this.loadStatistics()
          // 重置表单
          this.batchForm = {
            planId: '',
            startDate: '',
            endDate: '',
            frequency: '',
            executionTime: ''
          }
        }
      } catch (error) {
        this.$message.error('批量创建失败')
      }
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'SKIPPED': 'info',
        'OVERDUE': 'danger'
      }
      return typeMap[status] || 'info'
    },

    // 获取状态标签文本
    getStatusLabel(status) {
      const labelMap = {
        'PENDING': '待执行',
        'IN_PROGRESS': '执行中',
        'COMPLETED': '已完成',
        'SKIPPED': '已跳过',
        'OVERDUE': '逾期'
      }
      return labelMap[status] || status
    },

    // 获取类型标签类型
    getTypeTagType(type) {
      const typeMap = {
        'MEDICATION': 'primary',
        'EXERCISE': 'success',
        'DIET': 'warning',
        'PSYCHOLOGICAL': 'info',
        'REHABILITATION': 'danger',
        'OTHER': ''
      }
      return typeMap[type] || ''
    },

    // 获取类型标签文本
    getTypeLabel(type) {
      const labelMap = {
        'MEDICATION': '药物干预',
        'EXERCISE': '运动干预',
        'DIET': '饮食干预',
        'PSYCHOLOGICAL': '心理干预',
        'REHABILITATION': '康复训练',
        'OTHER': '其他'
      }
      return labelMap[type] || type
    }
  }
}
</script>

<style scoped>
.intervention-execution {
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

.dialog-footer {
  text-align: right;
}

pre {
  background: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  font-size: 12px;
}
</style>