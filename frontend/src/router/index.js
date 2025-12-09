import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import request from '@/utils/request'
import { isTokenValidated, setTokenValidated, isValidationAttempted } from '@/utils/tokenState'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/welcome',
    children: [
      {
        path: 'welcome',
        name: 'Welcome',
        component: () => import('@/views/Welcome.vue'),
        meta: { title: '欢迎页', requiresAuth: true }
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '仪表盘', requiresAuth: true, requiredPermissions: ['dashboard:view'], requiredRoles: ['SUPER_ADMIN'] }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('@/views/Users.vue'),
        meta: { title: '用户列表', requiresAuth: true, requiredPermissions: ['user:view'], requiredRoles: ['ADMIN', 'SUPER_ADMIN'] }
      },
      {
        path: 'roles',
        name: 'Roles',
        component: () => import('@/views/Roles.vue'),
        meta: { title: '角色管理', requiresAuth: true, requiredPermissions: ['role:manage'], requiredRoles: ['ADMIN', 'SUPER_ADMIN'] }
      },
      {
        path: 'menus',
        name: 'Menus',
        component: () => import('@/views/Menus.vue'),
        meta: { title: '菜单管理', requiresAuth: true, requiredPermissions: ['menu:view'], requiredRoles: ['ADMIN', 'SUPER_ADMIN'] }
      },
      {
        path: 'depts',
        name: 'Depts',
        component: () => import('@/views/Depts.vue'),
        meta: { title: '部门管理', requiresAuth: true, requiredPermissions: ['dept:view'], requiredRoles: ['ADMIN', 'SUPER_ADMIN'] }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人中心', requiresAuth: true }
      },
      {
        path: 'generator',
        name: 'Generator',
        component: () => import('@/views/Generator.vue'),
        meta: { title: '代码生成器', requiresAuth: true, requiredRoles: ['SUPER_ADMIN'] }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  
  // 如果有 token 但还没验证过，且没有尝试过验证，先验证 token 有效性
  if (authStore.isAuthenticated && !isTokenValidated() && !isValidationAttempted()) {
    try {
      await request({ url: '/auth/userinfo', method: 'get' })
      setTokenValidated(true)
    } catch (error) {
      // token 无效或网络错误，标记为已尝试
      setTokenValidated(false)
      // 如果是 401 错误，request.js 会清除 token 并跳转登录页
      // 如果是网络错误（后端宕机），保留 token 但标记验证失败，避免循环请求
      if (error.response?.status === 401 && to.meta.requiresAuth !== false) {
        next('/login')
        return
      }
      // 网络错误时不跳转登录页，让用户可以继续使用缓存的登录状态
    }
  }
  
  // 如果访问登录/注册页面且已登录，重定向到仪表盘
  if ((to.name === 'Login' || to.name === 'Register') && authStore.isAuthenticated) {
    if (canAccessDashboard(authStore)) {
      next('/dashboard')
    } else {
      next('/welcome')
    }
    return
  }
  
  // 如果页面需要认证但用户未登录，重定向到登录页面
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
    return
  }
  
  // 检查角色权限
  if (to.meta.requiresAuth && authStore.isAuthenticated) {
    const userRole = authStore.user?.roleCode
    const userPermissions = authStore.permissions || []
    
    const hasAccess = checkAccess(to.meta, userRole, userPermissions)
    if (!hasAccess) {
      // 无权限：如果能进仪表盘则去仪表盘，否则去欢迎页
      if (canAccessDashboard(authStore)) {
        next('/dashboard')
      } else {
        next('/welcome')
      }
      return
    }
  }
  
  next()
})

function checkAccess(meta, userRole, userPermissions) {
  let hasAccess = false

  if (meta.requiredRoles && meta.requiredRoles.length > 0) {
    hasAccess = hasAccess || meta.requiredRoles.includes(userRole)
  }

  if (meta.requiredPermissions && meta.requiredPermissions.length > 0) {
    hasAccess = hasAccess || meta.requiredPermissions.some(perm =>
      userPermissions.includes(perm) || userPermissions.includes('*')
    )
  }

  if (!meta.requiredRoles && !meta.requiredPermissions) {
    hasAccess = true
  }

  return hasAccess
}

function canAccessDashboard(authStore) {
  if (!authStore?.user) return false
  const userRole = authStore.user?.roleCode
  const userPermissions = authStore.permissions || []
  return checkAccess(
    { requiredRoles: ['SUPER_ADMIN'], requiredPermissions: ['dashboard:view'] },
    userRole,
    userPermissions
  )
}

export default router
