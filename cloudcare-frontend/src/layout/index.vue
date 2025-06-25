<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <div class="sidebar" :class="{ 'is-collapsed': isCollapsed }">
      <div class="logo-container">
        <img src="@/assets/logo.svg" alt="Logo" class="logo" />
        <h1 class="title" v-show="!isCollapsed">云护理系统</h1>
      </div>
      <el-scrollbar>
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapsed"
          :unique-opened="true"
          :collapse-transition="false"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          router
        >
          <sidebar-item
            v-for="route in routes"
            :key="route.path"
            :item="route"
            :base-path="route.path"
            :is-collapsed="isCollapsed"
          />
        </el-menu>
      </el-scrollbar>
    </div>
    
    <!-- 主区域 -->
    <div class="main-container">
      <!-- 头部 -->
      <div class="navbar">
        <div class="left-area">
          <!-- 折叠按钮 -->
          <div class="hamburger" @click="toggleSidebar">
            <el-icon :size="20">
              <component :is="isCollapse ? 'Expand' : 'Fold'" />
            </el-icon>
          </div>
          
          <!-- 面包屑 -->
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item 
              v-for="(item, index) in breadcrumbs" 
              :key="index"
              :to="item.path"
            >
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="right-menu">
          <!-- 全屏按钮 -->
          <div class="right-menu-item" @click="toggleFullScreen">
            <el-tooltip content="全屏" placement="bottom">
              <el-icon :size="18"><FullScreen /></el-icon>
            </el-tooltip>
          </div>
          
          <!-- 消息通知 -->
          <div class="right-menu-item">
            <el-badge :value="unreadCount" :max="99" :hidden="unreadCount === 0">
              <el-tooltip content="消息通知" placement="bottom">
                <el-icon :size="18" @click="showNotifications"><Bell /></el-icon>
              </el-tooltip>
            </el-badge>
          </div>
          
          <!-- 用户信息 -->
          <el-dropdown class="avatar-container" trigger="click">
            <div class="avatar-wrapper">
              <el-avatar :size="30" :src="userInfo.avatar || defaultAvatar" />
              <span class="username">{{ userInfo.realName || userInfo.username }}</span>
              <el-icon><CaretBottom /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goToProfile">
                  <el-icon><User /></el-icon>
                  <span>个人中心</span>
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
      
      <!-- 主内容区 -->
      <div class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <keep-alive>
              <component :is="Component" />
            </keep-alive>
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import SidebarItem from './components/SidebarItem.vue'
import Breadcrumb from './components/Breadcrumb.vue'

// 默认头像
const defaultAvatar = new URL('@/assets/default-avatar.png', import.meta.url).href

// 路由和用户状态
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 用户信息
const userInfo = computed(() => {
  return {
    username: userStore.username,
    realName: userStore.realName,
    avatar: userStore.avatar
  }
})

// 侧边栏折叠状态
const isCollapse = ref(false)

// 切换侧边栏折叠状态
const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

// 路由列表
const routes = computed(() => {
  // 过滤掉隐藏的路由和没有权限的路由
  const filterRoutes = (routes) => {
    return routes.filter(route => {
      if (route.hidden) return false
      if (route.meta && route.meta.roles && !route.meta.roles.some(role => userStore.roles.includes(role))) {
        return false
      }
      if (route.children) {
        route.children = filterRoutes(route.children)
        return route.children.length > 0
      }
      return true
    })
  }
  
  return filterRoutes(router.options.routes)
})

// 当前激活的菜单
const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

// 面包屑导航
const breadcrumbs = computed(() => {
  const result = []
  const getMatched = (route) => {
    if (route.meta && route.meta.title) {
      result.push({
        path: route.path,
        title: route.meta.title
      })
    }
    if (route.children && route.children.length > 0) {
      const matchedChild = route.children.find(child => route.path + '/' + child.path === activeMenu.value)
      if (matchedChild) {
        getMatched(matchedChild)
      }
    }
  }
  
  const matched = router.options.routes.find(r => r.path === '/')
  if (matched) {
    const currentRoute = matched.children.find(child => '/' + child.path === activeMenu.value)
    if (currentRoute) {
      getMatched(currentRoute)
    }
  }
  
  return result
})

// 全屏切换
const toggleFullScreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen()
    }
  }
}

// 未读消息数量
const unreadCount = ref(0)

// 获取未读消息数量
const getUnreadCount = () => {
  getUnprocessedWarningCount().then(res => {
    unreadCount.value = res.data
  })
}

// 显示通知
const showNotifications = () => {
  router.push('/health/warning')
}

// 跳转到个人中心
const goToProfile = () => {
  router.push('/profile/index')
}

// 退出登录
const handleLogout = () => {
  userStore.logout().then(() => {
    router.push('/login')
  })
}

// 监听路由变化
watch(() => route.path, () => {
  // 可以在这里处理路由变化的逻辑
})

// 页面加载时获取未读消息数量
onMounted(() => {
  getUnreadCount()
  // 定时刷新未读消息数量
  setInterval(() => {
    getUnreadCount()
  }, 60000) // 每分钟刷新一次
})
</script>

<style scoped>
.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
  display: flex;
}

.sidebar-container {
  width: 210px;
  height: 100%;
  background-color: #304156;
  transition: width 0.3s;
  overflow: hidden;
}

.sidebar-container.is-collapsed {
  width: 64px;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 15px;
  background-color: #2b3649;
}

.logo {
  width: 32px;
  height: 32px;
}

.title {
  margin-left: 10px;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.navbar {
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 15px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.left-area {
  display: flex;
  align-items: center;
}

.hamburger {
  padding: 0 15px;
  cursor: pointer;
}

.right-menu {
  display: flex;
  align-items: center;
}

.right-menu-item {
  padding: 0 12px;
  cursor: pointer;
  font-size: 18px;
}

.avatar-container {
  margin-left: 15px;
}

.avatar-wrapper {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin: 0 5px;
  font-size: 14px;
}

.app-main {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
  background-color: #f0f2f5;
}

/* 过渡动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>