import request from '@/utils/request'

// GPS设备管理API
export const gpsDeviceApi = {
  // 获取设备列表
  getDeviceList(params) {
    return request({
      url: '/api/gps-device/list',
      method: 'get',
      params
    })
  },

  // 获取设备详情
  getDeviceById(id) {
    return request({
      url: `/api/gps-device/${id}`,
      method: 'get'
    })
  },

  // 添加设备
  addDevice(data) {
    return request({
      url: '/api/gps-device',
      method: 'post',
      data
    })
  },

  // 更新设备
  updateDevice(data) {
    return request({
      url: '/api/gps-device',
      method: 'put',
      data
    })
  },

  // 删除设备
  deleteDevice(id) {
    return request({
      url: `/api/gps-device/${id}`,
      method: 'delete'
    })
  },

  // 绑定设备
  bindDevice(deviceId, elderlyId) {
    return request({
      url: `/api/gps-device/${deviceId}/bind/${elderlyId}`,
      method: 'post'
    })
  },

  // 解绑设备
  unbindDevice(deviceId) {
    return request({
      url: `/api/gps-device/${deviceId}/unbind`,
      method: 'post'
    })
  },

  // 获取设备最新位置
  getDeviceLocation(deviceId) {
    return request({
      url: `/api/gps-device/${deviceId}/location`,
      method: 'get'
    })
  },

  // 获取老人最新位置
  getElderlyLocation(elderlyId) {
    return request({
      url: `/api/gps-device/elderly/${elderlyId}/location`,
      method: 'get'
    })
  },

  // 获取老人位置历史
  getElderlyLocationHistory(elderlyId, params) {
    return request({
      url: `/api/gps-device/elderly/${elderlyId}/location-history`,
      method: 'get',
      params
    })
  }
}

// GPS报警管理API
export const gpsAlarmApi = {
  // 获取报警列表
  getAlarmList(params) {
    return request({
      url: '/api/gps-alarm/list',
      method: 'get',
      params
    })
  },

  // 获取报警详情
  getAlarmById(id) {
    return request({
      url: `/api/gps-alarm/${id}`,
      method: 'get'
    })
  },

  // 处理报警
  processAlarm(id, data) {
    return request({
      url: `/api/gps-alarm/${id}/process`,
      method: 'post',
      data
    })
  },

  // 获取未处理报警数量
  getUnprocessedCount() {
    return request({
      url: '/api/gps-alarm/unprocessed-count',
      method: 'get'
    })
  },

  // 获取报警统计
  getAlarmStats(params) {
    return request({
      url: '/api/gps-alarm/stats',
      method: 'get',
      params
    })
  }
}

// 电子围栏管理API
export const geoFenceApi = {
  // 获取围栏列表
  getFenceList(params) {
    return request({
      url: '/api/geo-fence/list',
      method: 'get',
      params
    })
  },

  // 根据老人ID获取围栏
  getFenceByElderlyId(elderlyId) {
    return request({
      url: `/api/geo-fence/elderly/${elderlyId}`,
      method: 'get'
    })
  },

  // 获取围栏详情
  getFenceById(id) {
    return request({
      url: `/api/geo-fence/${id}`,
      method: 'get'
    })
  },

  // 创建围栏
  createFence(data) {
    return request({
      url: '/api/geo-fence',
      method: 'post',
      data
    })
  },

  // 更新围栏
  updateFence(data) {
    return request({
      url: '/api/geo-fence',
      method: 'put',
      data
    })
  },

  // 删除围栏
  deleteFence(id) {
    return request({
      url: `/api/geo-fence/${id}`,
      method: 'delete'
    })
  },

  // 更新围栏状态
  updateFenceStatus(id, status) {
    return request({
      url: `/api/geo-fence/${id}/status`,
      method: 'put',
      data: { status }
    })
  },

  // 检查位置是否在围栏内
  checkLocationInFence(elderlyId, latitude, longitude) {
    return request({
      url: '/api/geo-fence/check-location',
      method: 'post',
      data: {
        elderlyId,
        latitude,
        longitude
      }
    })
  }
}

// GPS位置管理API
export const gpsLocationApi = {
  // 获取位置列表
  getLocationList(params) {
    return request({
      url: '/api/gps-location/list',
      method: 'get',
      params
    })
  },

  // 获取设备位置历史
  getLocationHistory(deviceImei, params) {
    return request({
      url: `/api/gps-location/device/${deviceImei}/history`,
      method: 'get',
      params
    })
  },

  // 获取老人位置历史
  getElderlyLocationHistory(elderlyId, params) {
    return request({
      url: `/api/gps-location/elderly/${elderlyId}/history`,
      method: 'get',
      params
    })
  }
}

// GPS数据推送测试API
export const gpsPushTestApi = {
  // 发送测试数据
  sendTestData(data) {
    return request({
      url: '/api/gps/receive',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
  }
}