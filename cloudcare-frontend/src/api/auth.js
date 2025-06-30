import request from '@/utils/request'

/**
 * 用户登录
 * @param {Object} data 登录信息
 * @returns {Promise}
 */
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data: data
  })
}

/**
 * 用户注册
 * @param {Object} data 注册信息
 * @returns {Promise}
 */
export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data: data
  })
}

/**
 * 退出登录
 * @returns {Promise}
 */
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

/**
 * 获取当前登录用户信息
 * @returns {Promise}
 */
export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

/**
 * 修改密码
 * @param {Object} data 密码信息
 * @returns {Promise}
 */
export function updatePassword(data) {
  return request({
    url: '/user/password',
    method: 'put',
    data
  })
}

/**
 * 重置密码
 * @param {Object} data 用户信息
 * @returns {Promise}
 */
export function resetPassword(data) {
  return request({
    url: '/user/reset-password',
    method: 'post',
    data
  })
}

/**
 * 更新用户基本信息
 * @param {Object} data 用户信息
 * @returns {Promise}
 */
export function updateUserInfo(data) {
  return request({
    url: '/user/info',
    method: 'put',
    data
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
    params: { avatar }
  })
}

/**
 * 上传文件
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function uploadFile(formData) {
  return request({
    url: '/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}