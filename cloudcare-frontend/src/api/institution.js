import request from '@/utils/request'

/**
 * 分页查询机构信息列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInstitutionList(params) {
  return request({
    url: '/institution/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询机构信息
 * @param {Number} institutionId 机构ID
 * @returns {Promise}
 */
export function getInstitutionById(institutionId) {
  return request({
    url: `/institution/${institutionId}`,
    method: 'get'
  })
}

/**
 * 新增机构信息
 * @param {Object} data 机构信息
 * @returns {Promise}
 */
export function addInstitution(data) {
  return request({
    url: '/institution',
    method: 'post',
    data
  })
}

/**
 * 修改机构信息
 * @param {Object} data 机构信息
 * @returns {Promise}
 */
export function updateInstitution(data) {
  return request({
    url: '/institution',
    method: 'put',
    data
  })
}

/**
 * 批量删除机构信息
 * @param {Array} institutionIds 机构ID数组
 * @returns {Promise}
 */
export function deleteInstitution(institutionIds) {
  return request({
    url: `/institution/batch/${institutionIds.join(',')}`,
    method: 'delete'
  })
}

/**
 * 删除单个机构信息
 * @param {Number} institutionId 机构ID
 * @returns {Promise}
 */
export function deleteInstitutionById(institutionId) {
  return request({
    url: `/institution/${institutionId}`,
    method: 'delete'
  })
}

/**
 * 获取机构统计信息
 * @returns {Promise}
 */
export function getInstitutionStatistics() {
  return request({
    url: '/institution/statistics',
    method: 'get'
  })
}

/**
 * 获取床位统计信息
 * @returns {Promise}
 */
export function getBedStatistics() {
  return request({
    url: '/institution/bed-statistics',
    method: 'get'
  })
}

/**
 * 根据名称模糊查询机构
 * @param {String} name 机构名称
 * @returns {Promise}
 */
export function searchInstitutionByName(name) {
  return request({
    url: '/institution/search',
    method: 'get',
    params: { name }
  })
}

/**
 * 获取入住率排行榜
 * @param {Number} limit 排行榜数量
 * @returns {Promise}
 */
export function getOccupancyRanking(limit = 10) {
  return request({
    url: '/institution/occupancy-ranking',
    method: 'get',
    params: { limit }
  })
}

/**
 * 校验机构名称唯一性
 * @param {Number} institutionId 机构ID（可选）
 * @param {String} name 机构名称
 * @returns {Promise}
 */
export function checkInstitutionNameUnique(institutionId, name) {
  return request({
    url: '/institution/check-name',
    method: 'get',
    params: {
      institutionId,
      name
    }
  })
}

/**
 * 更新床位信息
 * @param {Number} institutionId 机构ID
 * @param {Number} bedTotal 总床位数
 * @param {Number} bedAvailable 可用床位数
 * @returns {Promise}
 */
export function updateBeds(institutionId, bedTotal, bedAvailable) {
  return request({
    url: '/institution/beds',
    method: 'put',
    params: {
      institutionId,
      bedTotal,
      bedAvailable
    }
  })
}