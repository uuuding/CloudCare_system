import request from '@/utils/request'

/**
 * 分页查询服务调度列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getServiceSchedulePage(params) {
  return request({
    url: '/service-schedule/page',
    method: 'get',
    params: params
  })
}

/**
 * 根据ID查询服务调度详情
 * @param {Number} scheduleId 调度ID
 * @returns {Promise}
 */
export function getServiceScheduleById(scheduleId) {
  return request({
    url: `/service-schedule/${scheduleId}`,
    method: 'get'
  })
}

/**
 * 创建服务调度
 * @param {Object} data 调度信息
 * @returns {Promise}
 */
export function createServiceSchedule(data) {
  return request({
    url: '/service-schedule',
    method: 'post',
    data: data
  })
}

/**
 * 更新服务调度
 * @param {Number} scheduleId 调度ID
 * @param {Object} data 调度信息
 * @returns {Promise}
 */
export function updateServiceSchedule(scheduleId, data) {
  return request({
    url: `/service-schedule/${scheduleId}`,
    method: 'put',
    data: data
  })
}

/**
 * 删除服务调度
 * @param {Number} scheduleId 调度ID
 * @returns {Promise}
 */
export function deleteServiceSchedule(scheduleId) {
  return request({
    url: `/service-schedule/${scheduleId}`,
    method: 'delete'
  })
}

/**
 * 根据老人ID查询服务调度列表
 * @param {Number} elderId 老人ID
 * @returns {Promise}
 */
export function getServiceSchedulesByElderId(elderId) {
  return request({
    url: `/service-schedule/elder/${elderId}`,
    method: 'get'
  })
}

/**
 * 根据服务人员ID查询服务调度列表
 * @param {Number} staffId 服务人员ID
 * @returns {Promise}
 */
export function getServiceSchedulesByStaffId(staffId) {
  return request({
    url: `/service-schedule/staff/${staffId}`,
    method: 'get'
  })
}

/**
 * 根据时间范围查询服务调度列表
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @returns {Promise}
 */
export function getServiceSchedulesByTimeRange(startTime, endTime) {
  return request({
    url: '/service-schedule/time-range',
    method: 'get',
    params: {
      startTime,
      endTime
    }
  })
}

/**
 * 根据状态查询服务调度列表
 * @param {Number} status 状态
 * @returns {Promise}
 */
export function getServiceSchedulesByStatus(status) {
  return request({
    url: `/service-schedule/status/${status}`,
    method: 'get'
  })
}

/**
 * 开始执行服务
 * @param {Number} scheduleId 调度ID
 * @returns {Promise}
 */
export function startService(scheduleId) {
  return request({
    url: `/service-schedule/${scheduleId}/start`,
    method: 'put'
  })
}

/**
 * 完成服务
 * @param {Number} scheduleId 调度ID
 * @param {String} remark 服务备注
 * @returns {Promise}
 */
export function completeService(scheduleId, remark) {
  return request({
    url: `/service-schedule/${scheduleId}/complete`,
    method: 'put',
    params: {
      remark
    }
  })
}

/**
 * 取消服务
 * @param {Number} scheduleId 调度ID
 * @param {String} remark 取消原因
 * @returns {Promise}
 */
export function cancelService(scheduleId, remark) {
  return request({
    url: `/service-schedule/${scheduleId}/cancel`,
    method: 'put',
    params: {
      remark
    }
  })
}

/**
 * 获取今日待执行的服务调度列表
 * @returns {Promise}
 */
export function getTodayPendingSchedules() {
  return request({
    url: '/service-schedule/today/pending',
    method: 'get'
  })
}

/**
 * 获取紧急服务调度列表
 * @returns {Promise}
 */
export function getUrgentSchedules() {
  return request({
    url: '/service-schedule/urgent',
    method: 'get'
  })
}

/**
 * 获取服务类型选项
 * @returns {Array}
 */
export function getServiceTypeOptions() {
  return [
    { value: 1, label: '日常护理' },
    { value: 2, label: '医疗服务' },
    { value: 3, label: '康复训练' },
    { value: 4, label: '心理疏导' },
    { value: 5, label: '营养配餐' },
    { value: 6, label: '清洁卫生' }
  ]
}

/**
 * 获取服务状态选项
 * @returns {Array}
 */
export function getServiceStatusOptions() {
  return [
    { value: 1, label: '待执行', type: 'warning' },
    { value: 2, label: '进行中', type: 'primary' },
    { value: 3, label: '已完成', type: 'success' },
    { value: 4, label: '已取消', type: 'danger' }
  ]
}

/**
 * 获取优先级选项
 * @returns {Array}
 */
export function getPriorityOptions() {
  return [
    { value: 1, label: '低', type: 'info' },
    { value: 2, label: '中', type: 'warning' },
    { value: 3, label: '高', type: 'danger' },
    { value: 4, label: '紧急', type: 'danger' }
  ]
}