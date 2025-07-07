import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

// 创建axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API || '', // url = base url + request url
  timeout: 10000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    // 如果有token，则在请求头中添加token
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 如果是blob类型的响应（如文件下载），直接返回
    if (response.config.responseType === 'blob') {
      return response.data
    }
    
    const res = response.data

    // 检查是否是Result格式的响应 {success: true/false, code: number, message: '', data: any}
    if (res.hasOwnProperty('success')) {
      if (!res.success) {
        ElMessage({
          message: res.message || '系统错误',
          type: 'error',
          duration: 5 * 1000
        })
        return Promise.reject(new Error(res.message || '系统错误'))
      }
      // 对于Result格式的成功响应，直接返回
      return res
    }

    // 如果返回的状态码不是200，则判断为错误（针对非Result格式的响应）
    if (res.code && res.code !== 200) {
      ElMessage({
        message: res.message || '系统错误',
        type: 'error',
        duration: 5 * 1000
      })

      // 401: 未登录或token过期
      // 403: 无权限
      const whiteList = ['/api/auth/login', '/api/auth/register']
      if (res.code === 401 || res.code === 403) {
        if (whiteList.includes(response.config.url)) {
          return Promise.reject(new Error(res.message || '系统错误'))
        }
        // 重新登录
        ElMessageBox.confirm('您已登出，请重新登录', '确认登出', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const userStore = useUserStore()
          userStore.resetToken()
          router.push(`/login?redirect=${router.currentRoute.value.fullPath}`)
        })
      }
      return Promise.reject(new Error(res.message || '系统错误'))
    }
    
    // 对于其他格式的响应，直接返回
    return res
  },
  error => {
    let errorMessage = '请求失败'
    
    if (error.response) {
      // 服务器返回了错误状态码
      const status = error.response.status
      const data = error.response.data
      
      if (status === 500) {
        errorMessage = '服务器内部错误'
      } else if (status === 404) {
        errorMessage = '请求的资源不存在'
      } else if (status === 400) {
        errorMessage = '请求参数错误'
      } else {
        errorMessage = data?.message || `请求失败 (${status})`
      }
    } else if (error.request) {
      // 请求已发出但没有收到响应
      errorMessage = '网络连接失败，请检查网络'
    } else {
      // 其他错误
      errorMessage = error.message || '未知错误'
    }
    
    ElMessage({
      message: errorMessage,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service