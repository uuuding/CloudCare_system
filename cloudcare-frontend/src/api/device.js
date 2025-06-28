import request from '@/utils/request'

/**
 * 分页查询设备信息列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDeviceList(params) {
  return request({
    url: '/device/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询设备信息
 * @param {Number} deviceId 设备ID
 * @returns {Promise}
 */
export function getDeviceById(deviceId) {
  return request({
    url: `/device/${deviceId}`,
    method: 'get'
  })
}

/**
 * 新增设备信息
 * @param {Object} data 设备信息
 * @returns {Promise}
 */
export function addDevice(data) {
  return request({
    url: '/device',
    method: 'post',
    data
  })
}

/**
 * 修改设备信息
 * @param {Object} data 设备信息
 * @returns {Promise}
 */
export function updateDevice(data) {
  return request({
    url: '/device',
    method: 'put',
    data
  })
}

/**
 * 删除设备信息
 * @param {Array} deviceIds 设备ID数组
 * @returns {Promise}
 */
export function deleteDevice(deviceIds) {
  return request({
    url: `/device/${deviceIds.join(',')}`,
    method: 'delete'
  })
}

/**
 * 更新设备状态
 * @param {Number} deviceId 设备ID
 * @param {Number} status 设备状态
 * @returns {Promise}
 */
export function updateDeviceStatus(deviceId, status) {
  return request({
    url: '/device/status',
    method: 'put',
    params: {
      deviceId,
      status
    }
  })
}

/**
 * 获取设备统计信息
 * @returns {Promise}
 */
export function getDeviceStatistics() {
  return request({
    url: '/device/statistics',
    method: 'get'
  })
}

/**
 * 获取需要维护的设备列表
 * @returns {Promise}
 */
export function getMaintenanceRequiredDevices() {
  return request({
    url: '/device/maintenance-required',
    method: 'get'
  })
}

/**
 * 获取设备类型选项
 * @returns {Promise}
 */
export function getDeviceTypes() {
  return request({
    url: '/device/device-types',
    method: 'get'
  })
}

/**
 * 获取设备状态选项
 * @returns {Promise}
 */
export function getDeviceStatusOptions() {
  return request({
    url: '/device/device-status',
    method: 'get'
  })
}