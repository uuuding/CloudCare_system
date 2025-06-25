import request from '@/utils/request'

/**
 * 获取仪表盘统计数据
 * @returns {Promise}
 */
export function getDashboardStatistics() {
  return request({
    url: '/dashboard/statistics',
    method: 'get'
  })
}

/**
 * 获取健康预警趋势数据
 * @param {string} timeRange - 时间范围：week-本周, month-本月, year-全年
 * @returns {Promise}
 */
export function getWarningTrend(timeRange) {
  return request({
    url: '/dashboard/warning/trend',
    method: 'get',
    params: { timeRange }
  })
}

/**
 * 获取健康记录分布数据
 * @param {string} type - 分布类型：type-类型分布, level-等级分布
 * @returns {Promise}
 */
export function getRecordDistribution(type) {
  return request({
    url: '/dashboard/record/distribution',
    method: 'get',
    params: { type }
  })
}

/**
 * 获取系统公告列表
 * @param {number} limit - 获取条数
 * @returns {Promise}
 */
export function getAnnouncements(limit) {
  return request({
    url: '/dashboard/announcements',
    method: 'get',
    params: { limit }
  })
}

/**
 * 获取天气信息
 * @param {string} city - 城市名称
 * @returns {Promise}
 */
export function getWeatherInfo(city) {
  return request({
    url: '/dashboard/weather',
    method: 'get',
    params: { city }
  })
}