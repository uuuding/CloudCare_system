import request from '@/utils/request'

/**
 * 分页查询系统日志列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSystemLogList(params) {
  return request({
    url: '/system-log/list',
    method: 'get',
    params
  })
}

/**
 * 获取系统日志统计信息
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSystemLogStats(params) {
  return request({
    url: '/system-log/stats',
    method: 'get',
    params
  })
}

/**
 * 获取系统日志详情
 * @param {Number} id 日志ID
 * @returns {Promise}
 */
export function getSystemLogDetail(id) {
  return request({
    url: `/system-log/${id}`,
    method: 'get'
  })
}

/**
 * 删除系统日志
 * @param {Number} id 日志ID
 * @returns {Promise}
 */
export function deleteSystemLog(id) {
  return request({
    url: `/system-log/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除系统日志
 * @param {Array} ids 日志ID数组
 * @returns {Promise}
 */
export function batchDeleteSystemLog(ids) {
  return request({
    url: '/system-log/batch-delete',
    method: 'delete',
    data: { ids }
  })
}

/**
 * 清空系统日志
 * @param {Object} params 清空条件
 * @returns {Promise}
 */
export function clearSystemLog(params) {
  return request({
    url: '/system-log/clear',
    method: 'delete',
    params
  })
}

/**
 * 导出系统日志
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportSystemLog(params) {
  return request({
    url: '/system-log/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 获取日志级别选项
 * @returns {Promise}
 */
export function getLogLevels() {
  return request({
    url: '/system-log/levels',
    method: 'get'
  })
}

/**
 * 获取日志模块选项
 * @returns {Promise}
 */
export function getLogModules() {
  return request({
    url: '/system-log/modules',
    method: 'get'
  })
}

/**
 * 获取最近的系统日志
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getRecentSystemLogs(limit = 10) {
  return request({
    url: '/system-log/recent',
    method: 'get',
    params: { limit }
  })
}

/**
 * 获取系统日志趋势数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSystemLogTrend(params) {
  return request({
    url: '/system-log/trend',
    method: 'get',
    params
  })
}