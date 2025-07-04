import request from '@/utils/request'

// GPS轨迹相关API
export const gpsApi = {
  // 获取老人轨迹数据
  getElderlyTrack(elderlyId, startTime, endTime) {
    return request({
      url: `/gps/track/${elderlyId}`,
      method: 'get',
      params: {
        startTime,
        endTime
      }
    })
  },

  // 获取老人最新位置
  getLatestLocation(elderlyId) {
    return request({
      url: `/gps/latest/${elderlyId}`,
      method: 'get'
    })
  },

  // 绑定设备
  bindDevice(data) {
    return request({
      url: '/gps/bind',
      method: 'post',
      data
    })
  },

  // 获取设备绑定列表
  getBindings() {
    return request({
      url: '/gps/bindings',
      method: 'get'
    })
  },

  // 解绑设备
  unbindDevice(macid) {
    return request({
      url: `/gps/unbind/${macid}`,
      method: 'delete'
    })
  }
}

export default gpsApi