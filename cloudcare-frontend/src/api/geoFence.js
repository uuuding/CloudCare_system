import request from '@/utils/request'

/**
 * 获取围栏列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getFenceList(params) {
  return request({
    url: '/geo-fence/list',
    method: 'get',
    params
  })
}

/**
 * 创建围栏
 * @param {Object} data 围栏信息
 * @returns {Promise}
 */
export function createFence(data) {
  return request({
    url: '/geo-fence/create',
    method: 'post',
    data
  })
}

/**
 * 更新围栏
 * @param {Number} id 围栏ID
 * @param {Object} data 围栏信息
 * @returns {Promise}
 */
export function updateFence(id, data) {
  return request({
    url: `/geo-fence/update/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除围栏
 * @param {Number} id 围栏ID
 * @returns {Promise}
 */
export function deleteFence(id) {
  return request({
    url: `/geo-fence/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 获取围栏详情
 * @param {Number} id 围栏ID
 * @returns {Promise}
 */
export function getFenceDetail(id) {
  return request({
    url: `/geo-fence/${id}`,
    method: 'get'
  })
}

/**
 * 获取围栏事件记录
 * @param {Number} fenceId 围栏ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getFenceEvents(fenceId, params) {
  return request({
    url: `/geo-fence/events/${fenceId}`,
    method: 'get',
    params
  })
}

/**
 * 获取所有围栏事件记录
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAllEvents(params) {
  return request({
    url: '/geo-fence/events/all',
    method: 'get',
    params
  })
}

/**
 * 标记事件为已读
 * @param {Number} eventId 事件ID
 * @returns {Promise}
 */
export function markEventAsRead(eventId) {
  return request({
    url: `/geo-fence/events/${eventId}/read`,
    method: 'post'
  })
}

/**
 * 获取老人最新位置
 * @param {Number} elderlyId 老人ID
 * @returns {Promise}
 */
export function getLatestLocation(elderlyId) {
  return request({
    url: `/geo-fence/location/${elderlyId}`,
    method: 'get'
  })
}

/**
 * 获取围栏统计信息
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getFenceStats(params) {
  return request({
    url: '/geo-fence/stats',
    method: 'get',
    params
  })
}

/**
 * 测试GPS推送
 * @param {Object} data GPS数据
 * @returns {Promise}
 */
export function testGpsPush(data) {
  return request({
    url: '/gps/push',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}

/**
 * 绑定设备
 * @param {Object} data 绑定信息
 * @returns {Promise}
 */
export function bindDevice(data) {
  return request({
    url: '/gps/bind',
    method: 'post',
    data
  })
}

/**
 * 获取设备绑定列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDeviceBindings(params) {
  return request({
    url: '/gps/bindings',
    method: 'get',
    params
  })
}

/**
 * 解绑设备
 * @param {String} macid 设备MAC地址
 * @returns {Promise}
 */
export function unbindDevice(macid) {
  return request({
    url: `/gps/unbind/${macid}`,
    method: 'delete'
  })
}

export default {
  getFenceList,
  createFence,
  updateFence,
  deleteFence,
  getFenceDetail,
  getFenceEvents,
  getAllEvents,
  markEventAsRead,
  getLatestLocation,
  getFenceStats,
  testGpsPush,
  bindDevice,
  getDeviceBindings,
  unbindDevice
}