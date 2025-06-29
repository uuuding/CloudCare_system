import request from '@/utils/request'

// 分页查询干预执行记录
export function getInterventionExecutionPage(params) {
  return request({
    url: '/api/intervention-execution/page',
    method: 'get',
    params
  })
}

// 根据ID查询干预执行记录详情
export function getInterventionExecutionById(executionId) {
  return request({
    url: `/api/intervention-execution/${executionId}`,
    method: 'get'
  })
}

// 创建干预执行记录
export function createInterventionExecution(data) {
  return request({
    url: '/api/intervention-execution',
    method: 'post',
    data
  })
}

// 更新干预执行记录
export function updateInterventionExecution(executionId, data) {
  return request({
    url: `/api/intervention-execution/${executionId}`,
    method: 'put',
    data
  })
}

// 删除干预执行记录
export function deleteInterventionExecution(executionId) {
  return request({
    url: `/api/intervention-execution/${executionId}`,
    method: 'delete'
  })
}

// 根据方案ID查询执行记录
export function getInterventionExecutionsByPlan(planId) {
  return request({
    url: `/api/intervention-execution/plan/${planId}`,
    method: 'get'
  })
}

// 查询最近的执行记录
export function getRecentInterventionExecutions(planId) {
  return request({
    url: `/api/intervention-execution/plan/${planId}/recent`,
    method: 'get'
  })
}

// 完成干预执行
export function completeInterventionExecution(executionId, params) {
  return request({
    url: `/api/intervention-execution/${executionId}/complete`,
    method: 'post',
    params
  })
}

// 标记干预执行为未完成
export function skipInterventionExecution(executionId, reason) {
  return request({
    url: `/api/intervention-execution/${executionId}/skip`,
    method: 'post',
    params: { reason }
  })
}

// 统计执行记录（按状态分组）
export function getInterventionExecutionStatsByStatus(planId) {
  return request({
    url: '/api/intervention-execution/statistics/status',
    method: 'get',
    params: { planId }
  })
}

// 计算平均完成率
export function getAverageCompletionRate(planId) {
  return request({
    url: '/api/intervention-execution/average-completion-rate',
    method: 'get',
    params: { planId }
  })
}

// 计算平均效果评分
export function getAverageEffectivenessRating(planId) {
  return request({
    url: '/api/intervention-execution/average-effectiveness-rating',
    method: 'get',
    params: { planId }
  })
}

// 查询指定日期范围内的执行统计
export function getExecutionStatsByDateRange(startDate, endDate, planId) {
  return request({
    url: '/api/intervention-execution/statistics/date-range',
    method: 'get',
    params: {
      startDate,
      endDate,
      planId
    }
  })
}

// 查询今日待执行的干预记录
export function getTodayPendingExecutions() {
  return request({
    url: '/api/intervention-execution/today-pending',
    method: 'get'
  })
}

// 查询逾期未执行的干预记录
export function getOverdueExecutions() {
  return request({
    url: '/api/intervention-execution/overdue',
    method: 'get'
  })
}

// 批量创建执行计划
export function batchCreateExecutionPlan(planId, startDate, endDate, frequency) {
  return request({
    url: '/api/intervention-execution/batch-create',
    method: 'post',
    params: {
      planId,
      startDate,
      endDate,
      frequency
    }
  })
}

// 更新生命体征
export function updateVitalSigns(executionId, preVitalSigns, postVitalSigns) {
  return request({
    url: `/api/intervention-execution/${executionId}/vitals`,
    method: 'post',
    params: {
      preVitalSigns,
      postVitalSigns
    }
  })
}

// 获取执行趋势数据
export function getExecutionTrends(planId, days = 30) {
  return request({
    url: `/api/intervention-execution/plan/${planId}/trends`,
    method: 'get',
    params: { days }
  })
}

// 导出执行记录
export function exportExecutionRecords(params) {
  return request({
    url: '/api/intervention-execution/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}