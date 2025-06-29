import request from '@/utils/request'

// 分页查询干预方案
export function getInterventionPlanPage(params) {
  return request({
    url: '/api/intervention-plan/page',
    method: 'get',
    params
  })
}

// 根据ID查询干预方案详情
export function getInterventionPlanById(planId) {
  return request({
    url: `/api/intervention-plan/${planId}`,
    method: 'get'
  })
}

// 创建干预方案
export function createInterventionPlan(data) {
  return request({
    url: '/api/intervention-plan',
    method: 'post',
    data
  })
}

// 更新干预方案
export function updateInterventionPlan(planId, data) {
  return request({
    url: `/api/intervention-plan/${planId}`,
    method: 'put',
    data
  })
}

// 删除干预方案
export function deleteInterventionPlan(planId) {
  return request({
    url: `/api/intervention-plan/${planId}`,
    method: 'delete'
  })
}

// 根据老人ID查询活跃的干预方案
export function getActiveInterventionPlansByElderly(elderlyId) {
  return request({
    url: `/api/intervention-plan/elderly/${elderlyId}/active`,
    method: 'get'
  })
}

// 根据预警ID查询关联的干预方案
export function getInterventionPlansByAlert(alertId) {
  return request({
    url: `/api/intervention-plan/alert/${alertId}`,
    method: 'get'
  })
}

// 启动干预方案
export function startInterventionPlan(planId) {
  return request({
    url: `/api/intervention-plan/${planId}/start`,
    method: 'post'
  })
}

// 暂停干预方案
export function pauseInterventionPlan(planId) {
  return request({
    url: `/api/intervention-plan/${planId}/pause`,
    method: 'post'
  })
}

// 完成干预方案
export function completeInterventionPlan(planId, effectivenessScore) {
  return request({
    url: `/api/intervention-plan/${planId}/complete`,
    method: 'post',
    params: { effectivenessScore }
  })
}

// 取消干预方案
export function cancelInterventionPlan(planId, reason) {
  return request({
    url: `/api/intervention-plan/${planId}/cancel`,
    method: 'post',
    params: { reason }
  })
}

// 统计干预方案数量（按状态分组）
export function getInterventionPlanStatsByStatus(elderlyId) {
  return request({
    url: '/api/intervention-plan/statistics/status',
    method: 'get',
    params: { elderlyId }
  })
}

// 统计干预方案数量（按类型分组）
export function getInterventionPlanStatsByType(elderlyId) {
  return request({
    url: '/api/intervention-plan/statistics/type',
    method: 'get',
    params: { elderlyId }
  })
}

// 查询即将到期的干预方案
export function getExpiringInterventionPlans(days = 7) {
  return request({
    url: '/api/intervention-plan/expiring',
    method: 'get',
    params: { days }
  })
}

// 查询高优先级未完成的干预方案
export function getHighPriorityIncompleteInterventionPlans() {
  return request({
    url: '/api/intervention-plan/high-priority-incomplete',
    method: 'get'
  })
}

// 根据健康预警自动创建干预方案
export function createInterventionPlanFromAlert(alertId, elderlyId, elderlyName, alertType) {
  return request({
    url: '/api/intervention-plan/create-from-alert',
    method: 'post',
    params: {
      alertId,
      elderlyId,
      elderlyName,
      alertType
    }
  })
}

// 复制干预方案
export function copyInterventionPlan(planId, elderlyId, elderlyName) {
  return request({
    url: `/api/intervention-plan/${planId}/copy`,
    method: 'post',
    params: {
      elderlyId,
      elderlyName
    }
  })
}

// 批量更新干预方案状态
export function batchUpdateInterventionPlanStatus(planIds, status) {
  return request({
    url: '/api/intervention-plan/batch-update-status',
    method: 'post',
    data: {
      planIds,
      status
    }
  })
}