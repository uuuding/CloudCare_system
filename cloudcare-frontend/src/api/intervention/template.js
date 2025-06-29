import request from '@/utils/request'

// 分页查询干预模板
export function getInterventionTemplatePage(params) {
  return request({
    url: '/api/intervention-template/page',
    method: 'get',
    params
  })
}

// 根据ID查询干预模板详情
export function getInterventionTemplateById(templateId) {
  return request({
    url: `/api/intervention-template/${templateId}`,
    method: 'get'
  })
}

// 创建干预模板
export function createInterventionTemplate(data) {
  return request({
    url: '/api/intervention-template',
    method: 'post',
    data
  })
}

// 更新干预模板
export function updateInterventionTemplate(templateId, data) {
  return request({
    url: `/api/intervention-template/${templateId}`,
    method: 'put',
    data
  })
}

// 删除干预模板
export function deleteInterventionTemplate(templateId) {
  return request({
    url: `/api/intervention-template/${templateId}`,
    method: 'delete'
  })
}

// 根据预警类型查询推荐模板
export function getRecommendedTemplatesByAlertType(alertType, limit = 10) {
  return request({
    url: '/api/intervention-template/recommended',
    method: 'get',
    params: {
      alertType,
      limit
    }
  })
}

// 根据分类查询启用的模板
export function getEnabledTemplatesByCategory(category) {
  return request({
    url: '/api/intervention-template/enabled',
    method: 'get',
    params: { category }
  })
}

// 查询热门模板
export function getPopularTemplates(limit = 10) {
  return request({
    url: '/api/intervention-template/popular',
    method: 'get',
    params: { limit }
  })
}

// 增加模板使用次数
export function incrementTemplateUsage(templateId) {
  return request({
    url: `/api/intervention-template/${templateId}/increment-usage`,
    method: 'post'
  })
}

// 启用模板
export function enableTemplate(templateId) {
  return request({
    url: `/api/intervention-template/${templateId}/enable`,
    method: 'post'
  })
}

// 禁用模板
export function disableTemplate(templateId) {
  return request({
    url: `/api/intervention-template/${templateId}/disable`,
    method: 'post'
  })
}

// 查询所有模板分类
export function getAllTemplateCategories() {
  return request({
    url: '/api/intervention-template/categories',
    method: 'get'
  })
}

// 查询所有预警类型
export function getAllAlertTypes() {
  return request({
    url: '/api/intervention-template/alert-types',
    method: 'get'
  })
}

// 根据关键词搜索模板
export function searchTemplatesByKeyword(keyword) {
  return request({
    url: '/api/intervention-template/search',
    method: 'get',
    params: { keyword }
  })
}

// 复制模板
export function copyInterventionTemplate(templateId, newTemplateName) {
  return request({
    url: `/api/intervention-template/${templateId}/copy`,
    method: 'post',
    params: { newTemplateName }
  })
}

// 批量启用模板
export function batchEnableTemplates(templateIds) {
  return request({
    url: '/api/intervention-template/batch-enable',
    method: 'post',
    data: templateIds
  })
}

// 批量禁用模板
export function batchDisableTemplates(templateIds) {
  return request({
    url: '/api/intervention-template/batch-disable',
    method: 'post',
    data: templateIds
  })
}

// 获取模板使用统计
export function getTemplateUsageStatistics() {
  return request({
    url: '/api/intervention-template/statistics/usage',
    method: 'get'
  })
}

// 获取单个模板使用统计
export function getTemplateUsageById(templateId) {
  return request({
    url: `/api/intervention-template/${templateId}/usage-statistics`,
    method: 'get'
  })
}

// 导出模板
export function exportTemplates(params) {
  return request({
    url: '/api/intervention-template/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 导入模板
export function importTemplates(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/api/intervention-template/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}