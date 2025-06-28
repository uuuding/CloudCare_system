import request from '@/utils/request'

// 获取所有预警记录
export function getAllAlerts() {
  return request({
    url: '/health-alert/all',
    method: 'get'
  })
}

// 根据老人ID获取预警记录
export function getAlertsByElderlyId(elderlyId) {
  return request({
    url: `/health-alert/elderly/${elderlyId}`,
    method: 'get'
  })
}

// 根据预警状态获取预警记录
export function getAlertsByStatus(status) {
  return request({
    url: `/health-alert/status/${status}`,
    method: 'get'
  })
}

// 根据预警级别获取预警记录
export function getAlertsByLevel(alertLevel) {
  return request({
    url: `/health-alert/level/${alertLevel}`,
    method: 'get'
  })
}

// 根据时间范围获取预警记录
export function getAlertsByTimeRange(startTime, endTime) {
  return request({
    url: '/health-alert/time-range',
    method: 'get',
    params: {
      startTime,
      endTime
    }
  })
}

// 处理预警（标记为已解决）
export function resolveAlert(alertId, resolvedBy, resolvedNote) {
  return request({
    url: `/health-alert/resolve/${alertId}`,
    method: 'put',
    params: {
      resolvedBy,
      resolvedNote
    }
  })
}

// 忽略预警
export function ignoreAlert(alertId, resolvedBy, resolvedNote) {
  return request({
    url: `/health-alert/ignore/${alertId}`,
    method: 'put',
    params: {
      resolvedBy,
      resolvedNote
    }
  })
}

// 获取活跃预警数量
export function getActiveAlertCount() {
  return request({
    url: '/health-alert/active-count',
    method: 'get'
  })
}

// 获取预警统计信息
export function getAlertStatistics() {
  return request({
    url: '/health-alert/statistics',
    method: 'get'
  })
}

// 获取所有预警规则
export function getAllRules() {
  return request({
    url: '/health-alert/rules',
    method: 'get'
  })
}

// 根据类型获取启用的预警规则
export function getEnabledRulesByType(alertType) {
  return request({
    url: `/health-alert/rules/type/${alertType}`,
    method: 'get'
  })
}

// 添加预警规则
export function addRule(rule) {
  return request({
    url: '/health-alert/rules',
    method: 'post',
    data: rule
  })
}

// 更新预警规则
export function updateRule(rule) {
  return request({
    url: '/health-alert/rules',
    method: 'put',
    data: rule
  })
}

// 删除预警规则
export function deleteRule(ruleId) {
  return request({
    url: `/health-alert/rules/${ruleId}`,
    method: 'delete'
  })
}

// 启用/禁用预警规则
export function toggleRuleStatus(ruleId, enabled) {
  return request({
    url: `/health-alert/rules/${ruleId}/toggle`,
    method: 'put',
    params: {
      enabled
    }
  })
}

// 初始化默认预警规则
export function initializeDefaultRules() {
  return request({
    url: '/health-alert/rules/init',
    method: 'post'
  })
}

// 检查并生成预警（用于体检记录提交后触发）
export function checkAndGenerateAlerts(elderlyId, observationId) {
  return request({
    url: '/health-alert/check',
    method: 'post',
    params: {
      elderlyId,
      observationId
    }
  })
}