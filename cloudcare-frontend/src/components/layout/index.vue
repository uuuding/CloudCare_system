<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <div class="sidebar-container" :class="{'is-collapse': isCollapse}">
      <div class="logo-container">
        <img src="@/assets/logo.png" alt="Logo" class="logo" />
        <h1 class="title">云护CloudCare</h1>
      </div>
      
      <!-- 菜单 -->
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :collapse="isCollapse"
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
          :is-collapse="isCollapse"
        />
      </el-menu>
    </div>
    
    <!-- 主区域 -->
    <div class="main-container">
      <!-- 顶部导航栏 -->
      <div class="navbar">
        <div class="left-menu">
          <!-- 折叠按钮 -->
          <div class="hamburger-container" @click="toggleSidebar">
            <el-icon :size="20">
              <component :is="isCollapse ? 'Expand' : 'Fold'" />
            </el-icon>
          </div>
          
          <!-- 面包屑 -->
          <breadcrumb class="breadcrumb-container" />
        </div>
        
        <div class="right-menu">
          <!-- 用户信息 -->
          <el-dropdown trigger="click">
            <div class="avatar-container">
              <el-avatar :size="30" :src="userInfo.avatar || defaultAvatar" />
              <span class="username">{{ userInfo.realName || userInfo.username }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goToProfile">
                  <el-icon><User /></el-icon>
                  <span>个人中心</span>
                </el-dropdown-item>
                <el-dropdown-item divided @click="logout">
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
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'
import SidebarItem from './SidebarItem.vue'
import Breadcrumb from '../breadcrumb/index.vue'
import {ArrowDown, SwitchButton, User} from "@element-plus/icons-vue";

// 默认头像
const defaultAvatar = new URL('@/assets/default-avatar.png', import.meta.url).href

// 路由实例
const router = useRouter()
const route = useRoute()

// 用户状态
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

// 获取路由
const routes = computed(() => {
  return router.options.routes.filter(route => {
    return !route.hidden && route.children
  })
})

// 当前激活的菜单
const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

// 跳转到个人中心
const goToProfile = () => {
  router.push('/user/profile')
}

// 退出登录
const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await userStore.logout()
    router.push('/login')
  }).catch(() => {})
}
</script>

<style>
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
  transition: width 0.28s;
  overflow-y: auto;
}

/* 折叠时的侧边栏样式 */
.app-wrapper .sidebar-container {
  width: 210px;
  transition: width 0.28s;
}

.app-wrapper .sidebar-container.is-collapse {
  width: 64px;
}

/* 确保折叠时菜单项的图标居中 */
.app-wrapper .sidebar-container.is-collapse .el-menu-item .el-icon,
.app-wrapper .sidebar-container.is-collapse .el-sub-menu .el-icon,
.app-wrapper .sidebar-container.is-collapse .el-menu--collapse .el-menu-item .el-icon,
.app-wrapper .sidebar-container.is-collapse .el-menu--collapse .el-sub-menu .el-icon {
  margin: 0;
  text-align: center;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 确保折叠时子菜单标题文字隐藏 */
.app-wrapper .sidebar-container.is-collapse .el-menu--collapse .el-sub-menu__title span,
.app-wrapper .sidebar-container.is-collapse .el-sub-menu__title span,
.app-wrapper .sidebar-container.is-collapse .el-menu-item span,
.app-wrapper .sidebar-container.is-collapse .el-menu--collapse span:not(.el-menu--popup span),
.app-wrapper .sidebar-container.is-collapse .el-menu--collapse .el-menu-item span,
.app-wrapper .sidebar-container.is-collapse .el-menu--collapse .el-sub-menu span,
.app-wrapper .sidebar-container.is-collapse .el-menu--collapse .el-sub-menu__title span,
.app-wrapper .sidebar-container.is-collapse .el-menu--collapse .el-menu-item span,
.app-wrapper .sidebar-container.is-collapse .el-menu--collapse .el-sub-menu .el-sub-menu__title span,
.app-wrapper .sidebar-container.is-collapse .el-menu--collapse .el-sub-menu .el-menu-item span {
  display: none !important;
}

/* 全局样式：确保所有折叠菜单中的文字都隐藏 */
.el-menu--collapse .el-menu-item span,
.el-menu--collapse .el-sub-menu__title span,
.el-menu--collapse .el-sub-menu .el-sub-menu__title span,
.el-menu--collapse .el-sub-menu .el-menu-item span {
  display: none !important;
  width: 0 !important;
  height: 0 !important;
  overflow: hidden !important;
  visibility: hidden !important;
}

/* 确保折叠状态下图标居中 */
.el-menu--collapse .el-menu-item,
.el-menu--collapse .el-sub-menu__title {
  justify-content: center !important;
}

.el-menu--collapse .el-menu-item .el-icon,
.el-menu--collapse .el-sub-menu__title .el-icon {
  margin: 0 !important;
}

/* 确保折叠时弹出的子菜单中的文字正确显示 */
.el-menu--vertical .el-menu-item span,
.el-menu--vertical .el-sub-menu__title span {
  display: inline-block !important;
  visibility: visible !important;
}

.app-wrapper .main-container {
  margin-left: 0;
  width: calc(100% - 210px);
  transition: all 0.28s;
}

.app-wrapper .sidebar-container.is-collapse + .main-container {
  width: calc(100% - 64px);
}

.sidebar-container::-webkit-scrollbar {
  display: none;
}

.logo-container {
  height: 60px;
  padding: 10px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b3649;
  overflow: hidden;
  transition: all 0.3s;
}

.logo {
  width: 32px;
  height: 32px;
  margin-right: 10px;
  transition: margin-right 0.3s;
}

.is-collapse .logo {
  margin-right: 0;
}

.title {
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  margin: 0;
  white-space: nowrap;
  transition: opacity 0.3s;
}

.is-collapse .title {
  display: inline-block;
  opacity: 0;
  width: 0;
}

.sidebar-menu {
  border-right: none;
  width: 100%;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 100%;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  transition: margin-left 0.28s;
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 15px;
}

.left-menu {
  display: flex;
  align-items: center;
}

.hamburger-container {
  line-height: 46px;
  height: 100%;
  float: left;
  cursor: pointer;
  transition: background 0.3s;
  padding: 0 15px;
}

.hamburger-container:hover {
  background: rgba(0, 0, 0, 0.025);
}

.breadcrumb-container {
  margin-left: 15px;
}

.right-menu {
  display: flex;
  align-items: center;
}

.avatar-container {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin: 0 5px;
  color: #606266;
}

.app-main {
  flex: 1;
  overflow: auto;
  padding: 20px;
  background-color: #f0f2f5;
}
</style>