// Token存储的键名
const TokenKey = 'CloudCare-Token'

/**
 * 获取Token
 * @returns {String} token值
 */
export function getToken() {
  return localStorage.getItem(TokenKey)
}

/**
 * 设置Token
 * @param {String} token token值
 */
export function setToken(token) {
  return localStorage.setItem(TokenKey, token)
}

/**
 * 移除Token
 */
export function removeToken() {
  return localStorage.removeItem(TokenKey)
}

/**
 * 解析JWT Token
 * @param {String} token JWT Token
 * @returns {Object} 解析后的Token信息
 */
export function parseToken(token) {
  if (!token) return null
  try {
    // JWT Token由三部分组成，用.分隔，第二部分是payload
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        })
        .join('')
    )
    return JSON.parse(jsonPayload)
  } catch (error) {
    console.error('解析Token失败:', error)
    return null
  }
}

/**
 * 检查Token是否过期
 * @returns {Boolean} 是否过期
 */
export function isTokenExpired() {
  const token = getToken()
  if (!token) return true
  
  const tokenInfo = parseToken(token)
  if (!tokenInfo) return true
  
  // exp是过期时间的时间戳（秒）
  const exp = tokenInfo.exp
  const now = Math.floor(Date.now() / 1000)
  
  return now >= exp
}