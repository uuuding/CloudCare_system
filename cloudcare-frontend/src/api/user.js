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
  })
}

/**
 * @param {Number} userId 用户ID
 * @returns {Promise}
 */
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
  return request({
    url: '/user',
    method: 'post',
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