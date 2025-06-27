import request from '@/utils/request'

/**
 * 获取家属互动记录列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getFamilyInteractionList(params) {
  return request({
    url: '/family-interaction/list',
    method: 'get',
    params
  })
}

/**
 * 分页查询家属互动记录
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getFamilyInteractionPage(params) {
  return request({
    url: '/family-interaction/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取家属互动记录详情
 * @param {Number} id 记录ID
 * @returns {Promise}
 */
export function getFamilyInteractionById(id) {
  return request({
    url: `/family-interaction/${id}`,
    method: 'get'
  })
}

/**
 * 新增家属互动记录
 * @param {Object} data 互动记录数据
 * @returns {Promise}
 */
export function createFamilyInteraction(data) {
  return request({
    url: '/family-interaction',
    method: 'post',
    data
  })
}

/**
 * 更新家属互动记录
 * @param {Object} data 互动记录数据
 * @returns {Promise}
 */
export function updateFamilyInteraction(data) {
  return request({
    url: '/family-interaction',
    method: 'put',
    data
  })
}

/**
 * 删除家属互动记录
 * @param {Number} id 记录ID
 * @returns {Promise}
 */
export function deleteFamilyInteraction(id) {
  return request({
    url: `/family-interaction/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除家属互动记录
 * @param {Array} ids 记录ID数组
 * @returns {Promise}
 */
export function batchDeleteFamilyInteraction(ids) {
  return request({
    url: '/family-interaction/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 更新互动记录状态
 * @param {Number} id 记录ID
 * @param {String} status 状态
 * @returns {Promise}
 */
export function updateInteractionStatus(id, status) {
  return request({
    url: `/family-interaction/status/${id}/${status}`,
    method: 'put'
  })
}

/**
 * 获取老人的家属列表
 * @param {Number} elderId 老人ID
 * @returns {Promise}
 */
export function getFamilyMembersByElderId(elderId) {
  return request({
    url: `/family-interaction/family-members/${elderId}`,
    method: 'get'
  })
}

/**
 * 获取互动统计数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInteractionStatistics(params) {
  return request({
    url: '/family-interaction/stats',
    method: 'get',
    params
  })
}