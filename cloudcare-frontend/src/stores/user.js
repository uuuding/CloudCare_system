import { defineStore } from 'pinia'
import { login, logout, getUserInfo, updateUserInfo as updateUserInfoApi } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    userId: '',
    username: '',
    realName: '',
    avatar: '',
    roles: [],
    permissions: []
  }),
  
  getters: {
    isLogin: (state) => !!state.token,
    isAdmin: (state) => state.roles.includes('ROLE_ADMIN'),
    isDoctor: (state) => state.roles.includes('ROLE_DOCTOR'),
    isNurse: (state) => state.roles.includes('ROLE_NURSE'),
    isElderly: (state) => state.roles.includes('ROLE_ELDERLY'),
    isFamily: (state) => state.roles.includes('ROLE_FAMILY')
  },
  
  actions: {
    // 登录
    async login(userInfo) {
      const { username, password } = userInfo
      try {
        const res = await login({ username: username.trim(), password })
        console.log(res)
        const resData = res.data
        console.log(resData)

        if (res.code !== 200) {
          // 后端登录失败时，code不是200，抛出错误
          return Promise.reject(new Error(resData.msg || '登录失败'))
        }

        // 登录成功，保存token
        this.token = resData.token
        setToken(this.token)

        return Promise.resolve(resData)
      } catch (error) {
        return Promise.reject(error)
      }
    },
    
    // 获取用户信息
    async getInfo() {
      try {
        const res = await getUserInfo()
        const { data } = res
        
        if (!data) {
          return Promise.reject('验证失败，请重新登录')
        }
        
        const { userId, username, realName, avatar } = data
        
        this.userId = userId
        this.username = username
        this.realName = realName
        this.avatar = avatar

        
        return Promise.resolve(data)
      } catch (error) {
        return Promise.reject(error)
      }
    },
    
    // 退出登录
    async logout() {
      try {
        await logout()
        this.resetToken()
        return Promise.resolve()
      } catch (error) {
        return Promise.reject(error)
      }
    },
    
    // 重置token
    resetToken() {
      this.token = ''
      this.userId = ''
      this.username = ''
      this.realName = ''
      this.avatar = ''
      this.roles = []
      this.permissions = []
      removeToken()
    },
    
    // 更新用户信息
    async updateUserInfo(userInfo) {
      try {
        const res = await updateUserInfoApi(userInfo)
        if (res.code === 200) {
          // 更新本地状态
          const { realName, avatar, phone, email } = userInfo
          if (realName) this.realName = realName
          if (avatar) this.avatar = avatar
          if (phone) this.phone = phone
          if (email) this.email = email
        }
        return res
      } catch (error) {
        throw error
      }
    },
    
    // 更新头像
    updateAvatar(avatarUrl) {
      this.avatar = avatarUrl
    }
  }
})