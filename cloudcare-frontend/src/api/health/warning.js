import request from '@/utils/request'

// 健康预警API前缀
const api_prefix = '/api/health/warning'

/**
 * 分页查询健康预警列表
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function listHealthWarnings(query) {
  return request({
    url: `${api_prefix}/list`,
    method: 'get',
    params: query
  })
}

/**
 * 获取健康预警详情
 * @param {Number} warningId 预警ID
 * @returns {Promise}
 */
export function getHealthWarning(warningId) {
  return request({
    url: `${api_prefix}/${warningId}`,
    method: 'get'
  })
}

/**
 * 新增健康预警
 * @param {Object} data 健康预警信息
 * @returns {Promise}
 */
export function addHealthWarning(data) {
  return request({
    url: api_prefix,
    method: 'post',
    data: data
  })
}

/**
 * 修改健康预警
 * @param {Object} data 健康预警信息
 * @returns {Promise}
 */
export function updateHealthWarning(data) {
  return request({
    url: api_prefix,
    method: 'put',
    data: data
  })
}

/**
 * 删除健康预警
 * @param {Array} warningIds 预警ID数组
 * @returns {Promise}
 */
export function deleteHealthWarning(warningIds) {
  return request({
    url: `${api_prefix}/${warningIds}`,
    method: 'delete'
  })
}

/**
 * 处理健康预警
 * @param {Object} data 健康预警信息
 * @returns {Promise}
 */
export function processHealthWarning(data) {
  return request({
    url: `${api_prefix}/process`,
    method: 'put',
    data: data
  })
}

/**
 * 获取未处理的健康预警数量
 * @returns {Promise}
 */
export function getUnprocessedWarningCount() {
  return request({
    url: `${api_prefix}/unprocessed/count`,
    method: 'get'
  })
}

/**
 * 获取老人的健康预警列表
 * @param {Number} elderId 老人ID
 * @returns {Promise}
 */
export function getElderWarnings(elderId) {
  return request({
    url: `${api_prefix}/elder/${elderId}`,
    method: 'get'
  })
}

/**
 * 获取最近的健康预警列表
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getRecentWarnings(limit) {
  return request({
    url: `${api_prefix}/recent/${limit}`,
    method: 'get'
  })
}