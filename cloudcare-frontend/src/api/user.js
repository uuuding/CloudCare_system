import request from '@/utils/request'

/**
 * 获取医生列表
 * @returns {Promise}
 */
export function getDoctorList() {
  return request({
    url: '/user/doctors',
    method: 'get'
  })
}

/**
 * 获取老人列表
 * @returns {Promise}
 */
export function getElderList() {
  return request({
    url: '/user/elders',
    method: 'get'
  })
}

/**
 * 分页查询用户列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getUserPage(params) {
  return request({
    url: '/user/page',
    method: 'get',
    params: params
  })
}

/**
 * 根据ID获取用户详情
 * @param {Number} userId 用户ID
 * @returns {Promise}
 */
export function getUserById(userId) {
  return request({
    url: `/user/${userId}`,
    method: 'get'
  })
}

/**
 * 新增用户
 * @param {Object} data 用户信息
 * @returns {Promise}
 */
export function createUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data: data
  })
}

/**
 * 更新用户
 * @param {Object} data 用户信息
 * @returns {Promise}
 */
export function updateUser(data) {
  return request({
    url: '/user',
    method: 'put',
    data: data
  })
}

/**
 * 删除用户
 * @param {Number} userId 用户ID
 * @returns {Promise}
 */
export function deleteUser(userId) {
  return request({
    url: `/user/${userId}`,
    method: 'delete'
  })
}

/**
 * 重置用户密码
 * @param {Number} userId 用户ID
 * @returns {Promise}
 */
export function resetUserPassword(userId) {
  return request({
    url: `/user/reset-password/${userId}`,
    method: 'put'
  })
}

/**
 * 更新用户状态
 * @param {Number} userId 用户ID
 * @param {Number} status 状态（0：禁用，1：正常）
 * @returns {Promise}
 */
export function updateUserStatus(userId, status) {
  return request({
    url: `/user/status/${userId}/${status}`,
    method: 'put'
  })
}