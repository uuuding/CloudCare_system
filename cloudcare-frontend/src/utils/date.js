/**
 * 日期时间格式化工具函数
 */

/**
 * 格式化日期时间
 * @param {string|Date} date - 日期字符串或Date对象
 * @param {string} format - 格式化模板，默认 'YYYY-MM-DD HH:mm:ss'
 * @returns {string} 格式化后的日期字符串
 */
export function formatDateTime(date, format = 'YYYY-MM-DD HH:mm:ss') {
  if (!date) return ''
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

/**
 * 格式化日期
 * @param {string|Date} date - 日期字符串或Date对象
 * @returns {string} 格式化后的日期字符串 YYYY-MM-DD
 */
export function formatDate(date) {
  return formatDateTime(date, 'YYYY-MM-DD')
}

/**
 * 格式化时间
 * @param {string|Date} date - 日期字符串或Date对象
 * @returns {string} 格式化后的时间字符串 HH:mm:ss
 */
export function formatTime(date) {
  return formatDateTime(date, 'HH:mm:ss')
}

/**
 * 获取相对时间描述
 * @param {string|Date} date - 日期字符串或Date对象
 * @returns {string} 相对时间描述
 */
export function getRelativeTime(date) {
  if (!date) return ''
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  
  const now = new Date()
  const diff = now.getTime() - d.getTime()
  
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  const week = 7 * day
  const month = 30 * day
  const year = 365 * day
  
  if (diff < minute) {
    return '刚刚'
  } else if (diff < hour) {
    return `${Math.floor(diff / minute)}分钟前`
  } else if (diff < day) {
    return `${Math.floor(diff / hour)}小时前`
  } else if (diff < week) {
    return `${Math.floor(diff / day)}天前`
  } else if (diff < month) {
    return `${Math.floor(diff / week)}周前`
  } else if (diff < year) {
    return `${Math.floor(diff / month)}个月前`
  } else {
    return `${Math.floor(diff / year)}年前`
  }
}

/**
 * 判断是否为今天
 * @param {string|Date} date - 日期字符串或Date对象
 * @returns {boolean} 是否为今天
 */
export function isToday(date) {
  if (!date) return false
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return false
  
  const today = new Date()
  return d.toDateString() === today.toDateString()
}

/**
 * 判断是否为昨天
 * @param {string|Date} date - 日期字符串或Date对象
 * @returns {boolean} 是否为昨天
 */
export function isYesterday(date) {
  if (!date) return false
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return false
  
  const yesterday = new Date()
  yesterday.setDate(yesterday.getDate() - 1)
  return d.toDateString() === yesterday.toDateString()
}

/**
 * 获取日期范围描述
 * @param {string|Date} startDate - 开始日期
 * @param {string|Date} endDate - 结束日期
 * @returns {string} 日期范围描述
 */
export function getDateRangeText(startDate, endDate) {
  if (!startDate || !endDate) return ''
  
  const start = formatDate(startDate)
  const end = formatDate(endDate)
  
  if (start === end) {
    return start
  }
  
  return `${start} 至 ${end}`
}

/**
 * 获取当前时间戳
 * @returns {number} 当前时间戳
 */
export function getCurrentTimestamp() {
  return Date.now()
}

/**
 * 获取当前日期时间字符串
 * @param {string} format - 格式化模板
 * @returns {string} 当前日期时间字符串
 */
export function getCurrentDateTime(format = 'YYYY-MM-DD HH:mm:ss') {
  return formatDateTime(new Date(), format)
}

export default {
  formatDateTime,
  formatDate,
  formatTime,
  getRelativeTime,
  isToday,
  isYesterday,
  getDateRangeText,
  getCurrentTimestamp,
  getCurrentDateTime
}