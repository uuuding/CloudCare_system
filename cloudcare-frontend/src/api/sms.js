import request from '@/utils/request'

// 短信发送相关API
export const smsApi = {
  // 发送单条短信
  sendSms(data) {
    return request({
      url: '/sms/send',
      method: 'post',
      data
    })
  },

  // 批量发送短信
  sendBatchSms(data) {
    return request({
      url: '/sms/send/batch',
      method: 'post',
      data
    })
  },

  // 发送模板短信
  sendTemplateSms(data) {
    return request({
      url: '/sms/send/template',
      method: 'post',
      data
    })
  },

  // 批量发送模板短信
  sendBatchTemplateSms(data) {
    return request({
      url: '/sms/send/template/batch',
      method: 'post',
      data
    })
  },

  // 查询余额
  queryBalance() {
    return request({
      url: '/sms/balance',
      method: 'get'
    })
  },

  // 获取短信模板列表
  getTemplates() {
    return request({
      url: '/sms/templates',
      method: 'get'
    })
  },

  // 获取发送记录
  getSendRecords(params) {
    return request({
      url: '/sms/records',
      method: 'get',
      params
    })
  },

  // 获取发送统计
  getSendStats() {
    return request({
      url: '/sms/stats',
      method: 'get'
    })
  },

  // 获取发送记录
  getSendRecords(params) {
    return request({
      url: '/sms/records',
      method: 'get',
      params
    })
  },

  // 业务通知相关API
  notification: {
    // 发送注册欢迎短信
    sendWelcome(phone) {
      return request({
        url: '/sms/notification/welcome',
        method: 'post',
        data: { phone }
      })
    },

    // 发送登录安全提醒
    sendLoginSecurity(data) {
      return request({
        url: '/sms/notification/login-security',
        method: 'post',
        data
      })
    },

    // 发送消息通知
    sendMessageNotification(data) {
      return request({
        url: '/sms/notification/message',
        method: 'post',
        data
      })
    },

    // 发送服务通知
    sendServiceNotification(data) {
      return request({
        url: '/sms/notification/service',
        method: 'post',
        data
      })
    },

    // 发送就诊提醒
    sendAppointmentReminder(data) {
      return request({
        url: '/sms/notification/appointment',
        method: 'post',
        data
      })
    },

    // 发送设备提醒
    sendDeviceAlert(data) {
      return request({
        url: '/sms/notification/device',
        method: 'post',
        data
      })
    },

    // 发送紧急提醒
    sendEmergencyAlert(data) {
      return request({
        url: '/sms/notification/emergency',
        method: 'post',
        data
      })
    }
  }
}

export default smsApi