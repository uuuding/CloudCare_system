import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getToken, isTokenExpired } from '@/utils/auth'
import { ElMessage } from 'element-plus'
import Layout from '@/components/layout/index.vue'

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', isPublic: true },
    hidden: true
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register/index.vue'),
    meta: { title: '注册', isPublic: true },
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'HomeFilled', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      }
    ]
  },
  {
    path: '/elderly-dashboard',
    component: Layout,
    redirect: '/elderly-dashboard/index',
    children: [
      {
        path: 'index',
        name: 'ElderlyDashboard',
        component: () => import('@/views/dashboard/elderly-dashboard.vue'),
        meta: { title: '老人首页', icon: 'HomeFilled', roles: ['ROLE_ELDERLY'] }
      }
    ]
  },
  {
    path: '/data-screen',
    name: 'DataScreen',
    component: () => import('@/views/dashboard/data-screen.vue'),
    meta: { title: '数据大屏', isPublic: false },
    hidden: true
  },
  {
    path: '/elderly-profile',
    component: Layout,
    redirect: '/elderly-profile/index',
    children: [
      {
        path: 'index',
        name: 'ElderlyProfile',
        component: () => import('@/views/elderly-profile/index.vue'),
        meta: { title: '老人档案管理', icon: 'Files', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      }
    ]
  },
  {
    path: '/health',
    component: Layout,
    redirect: '/health/alert',
    meta: { title: '健康管理模块', icon: 'MagicStick' },
    children: [
      {
        path: 'alert',
        name: 'HealthAlert',
        component: () => import('@/views/health/alert/index.vue'),
        meta: { title: '健康预警', icon: 'Warning', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      },
      {
        path: 'assessment',
        name: 'HealthAssessment',
        component: () => import('@/views/health/assessment/index.vue'),
        meta: { title: '健康评估', icon: 'TrendCharts', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_ELDERLY'] }
      },
      {
                path: 'profile-analysis/:id',
        name: 'ElderlyProfileAnalysis',
        component: () => import('@/views/health/profile-analysis/index.vue'),
        meta: { title: '老人画像分析', icon: 'DataAnalysis', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] },
        hidden: true
      },
      {
        path: 'knowledge-graph',
        name: 'KnowledgeGraph',
        component: () => import('@/views/health/knowledge-graph/index.vue'),
        meta: { title: '知识图谱', icon: 'Connection', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      }
    ]
  },
  {
    path: '/intervention',
    component: Layout,
    redirect: '/intervention/plan',
    meta: { title: '干预管理模块', icon: 'Tools', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] },
    children: [
      {
        path: 'plan',
        name: 'InterventionPlan',
        component: () => import('@/views/intervention-plan/index.vue'),
        meta: { title: '干预方案', icon: 'List', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      },

      {
        path: 'template',
        name: 'InterventionTemplate',
        component: () => import('@/views/intervention-template/index.vue'),
        meta: { title: '干预模板', icon: 'Document', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      }
    ]
  },
  {
    path: '/medical',
    component: Layout,
    redirect: '/medical/appointment',
    meta: { title: '医疗服务模块', icon: 'FirstAidKit' },
    children: [
      {
        path: 'appointment',
        name: 'Appointment',
        component: () => import('@/views/medical/appointment/index.vue'),
        meta: { title: '在线预约', icon: 'Calendar', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_ELDERLY'] }
      },
      {
        path: 'medical-record',
        name: 'MedicalRecord',
        component: () => import('@/views/medical/medical-record/index.vue'),
        meta: { title: '电子病历', icon: 'Document', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_ELDERLY'] }
      }
    ]
  },
  {
    path: '/elderly-service',
    component: Layout,
    redirect: '/elderly-service/institution',
    meta: { title: '养老服务模块', icon: 'House' },
    children: [
      {
        path: 'institution',
        name: 'Institution',
        component: () => import('@/views/elderly-service/institution/index.vue'),
        meta: { title: '机构管理', icon: 'House', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      },
      {
        path: 'service-schedule',
        name: 'ServiceSchedule',
        component: () => import('@/views/elderly-service/service-schedule/index.vue'),
        meta: { title: '服务调度', icon: 'Clock', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      },
      {
        path: 'geo-fence',
        name: 'GeoFence',
        component: () => import('@/views/elderly-service/geo-fence/index.vue'),
        meta: { title: '电子围栏', icon: 'Location', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      },
      {
        path: 'family-interaction',
        name: 'FamilyInteraction',
        component: () => import('@/views/elderly-service/family-interaction/index.vue'),
        meta: { title: '家属互动', icon: 'ChatDotRound', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_ELDERLY'] }
      }
    ]
  },
  {
    path: '/reports',
    component: Layout,
    redirect: '/reports/index',
    children: [
      {
        path: 'index',
        name: 'Reports',
        component: () => import('@/views/reports/index.vue'),
        meta: { title: '报表统计与分析', icon: 'PieChart', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      }
    ]
  },
  {
    path: '/device',
    component: Layout,
    redirect: '/device/index',
    children: [
      {
        path: 'index',
        name: 'Device',
        component: () => import('@/views/device/index.vue'),
        meta: { title: '设备管理', icon: 'Monitor', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      }
    ]
  },
  {
    path: '/sms',
    component: Layout,
    redirect: '/sms/send',
    meta: { title: '短信管理', icon: 'Message', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] },
    children: [
      {
        path: 'send',
        name: 'SmsSend',
        component: () => import('@/views/sms/send/index.vue'),
        meta: { title: '短信发送', icon: 'Promotion', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      },
      {
        path: 'template',
        name: 'SmsTemplate',
        component: () => import('@/views/sms/template/index.vue'),
        meta: { title: '短信模板', icon: 'Document', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      },
      {
        path: 'record',
        name: 'SmsRecord',
        component: () => import('@/views/sms/record/index.vue'),
        meta: { title: '发送记录', icon: 'List', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      },
      {
        path: 'balance',
        name: 'SmsBalance',
        component: () => import('@/views/sms/balance/index.vue'),
        meta: { title: '余额查询', icon: 'Coin', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR'] }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/elder-account',
    meta: { title: '用户与权限管理', icon: 'User', roles: ['ROLE_ADMIN'] },
    children: [
      {
        path: 'elder-account',
        name: 'ElderAccount',
        component: () => import('@/views/user/elder-account/index.vue'),
        meta: { title: '老人账号管理', icon: 'Avatar', roles: ['ROLE_ADMIN'] }
      },
      {
        path: 'doctor-account',
        name: 'DoctorAccount',
        component: () => import('@/views/user/doctor-account/index.vue'),
        meta: { title: '医生账号管理', icon: 'UserFilled', roles: ['ROLE_ADMIN'] }
      },
      {
        path: 'admin-user',
        name: 'AdminUser',
        component: () => import('@/views/user/admin-user/index.vue'),
        meta: { title: '系统用户管理', icon: 'Setting', roles: ['ROLE_ADMIN'] }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/user/profile/index.vue'),
        meta: { title: '个人中心', icon: 'User', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_ELDERLY'] }
      }
    ]
  },

  {
    path: '/help',
    component: Layout,
    redirect: '/help/index',
    children: [
      {
        path: 'index',
        name: 'Help',
        component: () => import('@/views/help/index.vue'),
        meta: { title: '使用帮助', icon: 'QuestionFilled', roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_ELDERLY'] }
      }
    ]
  },
  {
    path: '/system-log',
    component: Layout,
    redirect: '/system-log/index',
    children: [
      {
        path: 'index',
        name: 'SystemLog',
        component: () => import('@/views/system-log/index.vue'),
        meta: { title: '系统日志', icon: 'Document', roles: ['ROLE_ADMIN'] }
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
      next({ path: '/dashboard' })
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

            // 根据用户类型进行仪表盘重定向
            if ((to.path === '/' || to.path === '/dashboard') && userStore.userType === 2) {
              next('/doctor-dashboard')
              return
            }
            if ((to.path === '/' || to.path === '/dashboard') && userStore.userType === 3) {
              next('/elderly-dashboard')
              return
            }
            if (to.path === '/doctor-dashboard' && userStore.userType !== 2) {
              next('/dashboard')
              return
            }
            if (to.path === '/elderly-dashboard' && userStore.userType !== 3) {
              next('/dashboard')
              return
            }

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