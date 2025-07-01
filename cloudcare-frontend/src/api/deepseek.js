import request from '@/utils/request'

// Call DeepSeek Chat API
export function chatWithDeepSeek(data) {
  return request({
    url: '/api/deepseek/chat',
    method: 'post',
    data,
    timeout: 60000 // Set timeout to 1 minute
  })
}