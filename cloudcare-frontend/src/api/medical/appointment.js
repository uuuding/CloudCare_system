import request from '@/utils/request'

/**
 * 分页查询医疗预约列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAppointmentPage(params) {
  return request({
    url: '/api/medical/appointment/page',
    method: 'get',
    params: params
  })
}

/**
 * 根据ID查询医疗预约详情
 * @param {Number} appointmentId 预约ID
 * @returns {Promise}
 */
export function getAppointmentById(appointmentId) {
  return request({
    url: `/api/medical/appointment/${appointmentId}`,
    method: 'get'
  })
}

/**
 * 创建医疗预约
 * @param {Object} data 预约信息
 * @returns {Promise}
 */
export function createAppointment(data) {
  return request({
    url: '/api/medical/appointment',
    method: 'post',
    data: data
  })
}

/**
 * 更新医疗预约
 * @param {Number} appointmentId 预约ID
 * @param {Object} data 预约信息
 * @returns {Promise}
 */
export function updateAppointment(appointmentId, data) {
  return request({
    url: `/api/medical/appointment/${appointmentId}`,
    method: 'put',
    data: data
  })
}

/**
 * 删除医疗预约
 * @param {Number} appointmentId 预约ID
 * @returns {Promise}
 */
export function deleteAppointment(appointmentId) {
  return request({
    url: `/api/medical/appointment/${appointmentId}`,
    method: 'delete'
  })
}

/**
 * 更新预约状态
 * @param {Number} appointmentId 预约ID
 * @param {Number} status 新状态
 * @returns {Promise}
 */
export function updateAppointmentStatus(appointmentId, status) {
  return request({
    url: `/api/medical/appointment/${appointmentId}/status`,
    method: 'patch',
    params: { status }
  })
}

/**
 * 获取预约类型选项
 * @returns {Array}
 */
export function getAppointmentTypeOptions() {
  return [
    { value: 1, label: '体检' },
    { value: 2, label: '门诊' },
    { value: 3, label: '专科' },
    { value: 4, label: '急诊' }
  ]
}

/**
 * 获取预约状态选项
 * @returns {Array}
 */
export function getAppointmentStatusOptions() {
  return [
    { value: 1, label: '待确认' },
    { value: 2, label: '已确认' },
    { value: 3, label: '已完成' },
    { value: 4, label: '已取消' }
  ]
}

/**
 * 获取预约类型文本
 * @param {Number} type 预约类型
 * @returns {String}
 */
export function getAppointmentTypeText(type) {
  const options = getAppointmentTypeOptions()
  const option = options.find(item => item.value === type)
  return option ? option.label : '未知'
}

/**
 * 获取预约状态文本
 * @param {Number} status 预约状态
 * @returns {String}
 */
export function getAppointmentStatusText(status) {
  const options = getAppointmentStatusOptions()
  const option = options.find(item => item.value === status)
  return option ? option.label : '未知'
}