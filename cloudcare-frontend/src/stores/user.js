import { defineStore } from 'pinia'
import { login, logout, getUserInfo } from '@/api/auth'
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
        const { data } = res
        this.token = data.token
        setToken(data.token)
        return Promise.resolve(res)
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
        
        const { userId, username, realName, avatar, roles, permissions } = data
        
        // 角色必须是非空数组
        if (!roles || roles.length <= 0) {
          return Promise.reject('用户没有任何权限')
        }
        
        this.userId = userId
        this.username = username
        this.realName = realName
        this.avatar = avatar
        this.roles = roles
        this.permissions = permissions
        
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
    updateUserInfo(userInfo) {
      const { realName, avatar } = userInfo
      if (realName) this.realName = realName
      if (avatar) this.avatar = avatar
    }
  }
})