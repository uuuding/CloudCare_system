// 地图配置文件
export const MAP_CONFIG = {
  // 高德地图API密钥 - 请替换为您的实际密钥
  AMAP_KEY: 'c24b0e3fe1334b5acf3fddba1fd9974c', // 请替换为您申请的高德地图API密钥
  
  // 高德地图API版本
  AMAP_VERSION: '2.0',
  
  // 默认地图中心点（北京天安门）
  DEFAULT_CENTER: [116.397428, 39.90923],
  
  // 默认缩放级别
  DEFAULT_ZOOM: 13,
  
  // 地图样式
  MAP_STYLE: 'amap://styles/normal',
  
  // 地理编码配置
  GEOCODER_CONFIG: {
    city: '全国'
  },
  
  // 定位配置
  GEOLOCATION_CONFIG: {
    enableHighAccuracy: true,
    timeout: 10000,
    maximumAge: 60000
  }
}

// 地图API加载状态
let mapApiLoaded = false
let mapApiLoading = false
const loadPromises = []

/**
 * 加载高德地图API
 * @returns {Promise}
 */
export const loadAMapAPI = () => {
  return new Promise((resolve, reject) => {
    // 如果已经加载完成
    if (mapApiLoaded && window.AMap) {
      resolve(window.AMap)
      return
    }
    
    // 如果正在加载中，返回相同的Promise
    if (mapApiLoading) {
      loadPromises.push({ resolve, reject })
      return
    }
    
    mapApiLoading = true
    loadPromises.push({ resolve, reject })
    
    // 检查API密钥
    if (!MAP_CONFIG.AMAP_KEY || MAP_CONFIG.AMAP_KEY === 'YOUR_AMAP_KEY_HERE') {
      const error = new Error('地图API密钥未配置，请联系管理员')
      loadPromises.forEach(({ reject }) => reject(error))
      loadPromises.length = 0
      mapApiLoading = false
      return
    }
    
    // 创建script标签加载API
    const script = document.createElement('script')
    script.src = `https://webapi.amap.com/maps?v=${MAP_CONFIG.AMAP_VERSION}&key=${MAP_CONFIG.AMAP_KEY}&plugin=AMap.Geocoder,AMap.PlaceSearch,AMap.Scale,AMap.ToolBar,AMap.Marker,AMap.Polyline,AMap.Icon`
    
    script.onload = () => {
      mapApiLoaded = true
      mapApiLoading = false
      loadPromises.forEach(({ resolve }) => resolve(window.AMap))
      loadPromises.length = 0
    }
    
    script.onerror = () => {
      mapApiLoading = false
      const error = new Error('高德地图API加载失败')
      loadPromises.forEach(({ reject }) => reject(error))
      loadPromises.length = 0
    }
    
    document.head.appendChild(script)
  })
}

/**
 * 验证坐标格式
 * @param {string} coordinates - 坐标字符串 "lng,lat"
 * @returns {boolean}
 */
export const validateCoordinates = (coordinates) => {
  if (!coordinates || typeof coordinates !== 'string') {
    return false
  }
  
  const parts = coordinates.split(',')
  if (parts.length !== 2) {
    return false
  }
  
  const lng = parseFloat(parts[0].trim())
  const lat = parseFloat(parts[1].trim())
  
  // 检查经纬度范围
  return !isNaN(lng) && !isNaN(lat) && 
         lng >= -180 && lng <= 180 && 
         lat >= -90 && lat <= 90
}

/**
 * 解析坐标字符串
 * @param {string} coordinates - 坐标字符串 "lng,lat"
 * @returns {Array|null} - [lng, lat] 或 null
 */
export const parseCoordinates = (coordinates) => {
  if (!validateCoordinates(coordinates)) {
    return null
  }
  
  const parts = coordinates.split(',')
  return [parseFloat(parts[0].trim()), parseFloat(parts[1].trim())]
}

/**
 * 格式化坐标
 * @param {Array} coordinates - [lng, lat]
 * @param {number} precision - 精度，默认6位小数
 * @returns {string} - "lng,lat"
 */
export const formatCoordinates = (coordinates, precision = 6) => {
  if (!Array.isArray(coordinates) || coordinates.length !== 2) {
    return ''
  }
  
  const [lng, lat] = coordinates
  return `${lng.toFixed(precision)},${lat.toFixed(precision)}`
}