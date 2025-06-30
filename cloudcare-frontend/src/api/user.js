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
    params, // 别忘了传参
  })
}

/**
 * 根据用户ID获取用户信息
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

/**
 * 更新用户头像
 * @param {String} avatar 头像URL
 * @returns {Promise}
 */
export function updateAvatar(avatar) {
  return request({
    url: '/user/avatar',
    method: 'put',
    data: { avatar }
  })
}