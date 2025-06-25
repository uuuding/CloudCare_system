/**
 * 判断是否为外部链接
 * @param {string} path 路径
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * 判断是否为URL
 * @param {string} url URL地址
 * @returns {Boolean}
 */
export function isUrl(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * 判断是否为手机号
 * @param {string} str 手机号
 * @returns {Boolean}
 */
export function isPhone(str) {
  const reg = /^1[3-9]\d{9}$/
  return reg.test(str)
}

/**
 * 判断是否为邮箱
 * @param {string} str 邮箱
 * @returns {Boolean}
 */
export function isEmail(str) {
  const reg = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/
  return reg.test(str)
}

/**
 * 判断是否为身份证号
 * @param {string} str 身份证号
 * @returns {Boolean}
 */
export function isIdCard(str) {
  const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  return reg.test(str)
}

/**
 * 判断是否为IP地址
 * @param {string} ip IP地址
 * @returns {Boolean}
 */
export function isIP(ip) {
  const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
  return reg.test(ip)
}

/**
 * 判断是否为有效的用户名
 * @param {string} str 用户名
 * @returns {Boolean}
 */
export function isValidUsername(str) {
  const reg = /^[a-zA-Z0-9_-]{4,16}$/
  return reg.test(str)
}

/**
 * 判断是否为有效的密码
 * @param {string} str 密码
 * @returns {Boolean}
 */
export function isValidPassword(str) {
  const reg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,16}$/
  return reg.test(str)
}

/**
 * 判断是否为中文
 * @param {string} str 字符串
 * @returns {Boolean}
 */
export function isChinese(str) {
  const reg = /^[\u4e00-\u9fa5]+$/
  return reg.test(str)
}

/**
 * 判断是否为数字
 * @param {string} str 字符串
 * @returns {Boolean}
 */
export function isNumber(str) {
  const reg = /^\d+$/
  return reg.test(str)
}

/**
 * 判断是否为整数
 * @param {string} str 字符串
 * @returns {Boolean}
 */
export function isInteger(str) {
  const reg = /^-?\d+$/
  return reg.test(str)
}

/**
 * 判断是否为小数
 * @param {string} str 字符串
 * @returns {Boolean}
 */
export function isDecimal(str) {
  const reg = /^-?\d+\.\d+$/
  return reg.test(str)
}