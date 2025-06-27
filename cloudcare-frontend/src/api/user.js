import request from '@/utils/request'

/**
 * 分页查询用户列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getUserPage(params) {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

/**
 * 获取用户详情
 * @param {Number} userId 用户ID
 * @returns {Promise}
 */
export function getUserInfo(userId) {
  return request({
    url: `/user/${userId}`,
    method: 'get'
  })
}

/**
 * 获取服务人员列表
 * @returns {Promise}
 */
export function getStaffList() {
  return request({
    url: '/user/staff',
    method: 'get'
  })
}

/**
 * 新增用户
 * @param {Object} data 用户信息
 * @returns {Promise}
 */
export function addUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data
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
    data
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
 * @returns {Promise}
 */
export function updateUserStatus(userId, status) {
  return request({
    url: `/user/status/${userId}/${status}`,
    method: 'put'
  })
}