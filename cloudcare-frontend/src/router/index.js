import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getToken, isTokenExpired } from '@/utils/auth'
import { ElMessage } from 'element-plus'

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', isPublic: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register/index.vue'),
    meta: { title: '注册', isPublic: true }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      }
    ]
  },
  {
    path: '/profile',
    component: () => import('@/layout/index.vue'),
    children: [
      {
        path: 'index',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'User' }
      }
    ]
  },
  {
    path: '/health',
    component: () => import('@/layout/index.vue'),
    redirect: '/health/record',
    meta: { title: '健康管理', icon: 'FirstAid' },
    children: [
      {
        path: 'record',
        name: 'HealthRecord',
        component: () => import('@/views/health/record/index.vue'),
        meta: { title: '健康记录', icon: 'Notebook' }
      },
      {
        path: 'warning',
        name: 'HealthWarning',
        component: () => import('@/views/health/warning/index.vue'),
        meta: { title: '健康预警', icon: 'Warning' }
      },
      {
        path: 'warning/detail/:id',
        name: 'HealthWarningDetail',
        component: () => import('@/views/health/warning/detail.vue'),
        meta: { title: '预警详情', activeMenu: '/health/warning' },
        hidden: true
      }
    ]
  },
  {
    path: '/elderly',
    component: () => import('@/layout/index.vue'),
    redirect: '/elderly/list',
    meta: { title: '老人管理', icon: 'User', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE'] },
    children: [
      {
        path: 'list',
        name: 'ElderlyList',
        component: () => import('@/views/elderly/list/index.vue'),
        meta: { title: '老人列表', icon: 'List' }
      },
      {
        path: 'detail/:id',
        name: 'ElderlyDetail',
        component: () => import('@/views/elderly/detail/index.vue'),
        meta: { title: '老人详情', activeMenu: '/elderly/list' },
        hidden: true
      }
    ]
  },
  {
    path: '/system',
    component: () => import('@/layout/index.vue'),
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'Setting', roles: ['ROLE_ADMIN'] },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'UserFilled' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理', icon: 'Lock' }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理', icon: 'Menu' }
      },
      {
        path: 'log',
        name: 'Log',
        component: () => import('@/views/system/log/index.vue'),
        meta: { title: '操作日志', icon: 'Document' }
      }
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/error/404.vue'),
    meta: { title: '404', isPublic: true },
    hidden: true
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    hidden: true
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 白名单路由
const whiteList = ['/login', '/register', '/404']

// 路由前置守卫
router.beforeEach(async (to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 云护理系统` : '云护理系统'
  
  // 判断是否有token
  const hasToken = getToken()
  
  if (hasToken) {
    // 如果已登录，则不允许访问登录页
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      // 判断token是否过期
      if (isTokenExpired()) {
        // token过期，清除用户信息并跳转到登录页
        const userStore = useUserStore()
        userStore.resetToken()
        ElMessage.error('登录已过期，请重新登录')
        next(`/login?redirect=${to.path}`)
      } else {
        // 判断是否已获取用户信息
        const userStore = useUserStore()
        const hasRoles = userStore.roles && userStore.roles.length > 0
        
        if (hasRoles) {
          // 判断是否有权限访问
          if (hasPermission(userStore.roles, to)) {
            next()
          } else {
            next('/404')
          }
        } else {
          try {
            // 获取用户信息
            await userStore.getInfo()
            
            // 判断是否有权限访问
            if (hasPermission(userStore.roles, to)) {
              next()
            } else {
              next('/404')
            }
          } catch (error) {
            // 获取用户信息失败，清除token并跳转到登录页
            userStore.resetToken()
            ElMessage.error(error.message || '获取用户信息失败')
            next(`/login?redirect=${to.path}`)
          }
        }
      }
    }
  } else {
    // 未登录
    if (whiteList.includes(to.path) || to.meta.isPublic) {
      // 白名单路由，直接访问
      next()
    } else {
      // 非白名单路由，重定向到登录页
      next(`/login?redirect=${to.path}`)
    }
  }
})

/**
 * 判断是否有权限访问路由
 * @param {Array} roles 用户角色
 * @param {Object} route 路由对象
 * @returns {Boolean}
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    // 如果路由没有设置roles字段，则视为所有人都可以访问
    return true
  }
}

export default router