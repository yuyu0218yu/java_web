import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

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
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '仪表盘', requiresAuth: true }
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
        path: 'permissions',
        name: 'Permissions',
        component: () => import('@/views/Permissions.vue'),
        meta: { title: '权限管理', requiresAuth: true, requiredPermissions: ['permission:manage'], requiredRoles: ['ADMIN', 'SUPER_ADMIN'] }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人中心', requiresAuth: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  // 如果访问登录/注册页面且已登录，重定向到仪表盘
  if ((to.name === 'Login' || to.name === 'Register') && authStore.isAuthenticated) {
    next('/dashboard')
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
    
    let hasAccess = false
    
    // 检查角色权限（如果配置了）
    if (to.meta.requiredRoles && to.meta.requiredRoles.length > 0) {
      hasAccess = hasAccess || to.meta.requiredRoles.includes(userRole)
    }
    
    // 检查具体权限（如果配置了）
    if (to.meta.requiredPermissions && to.meta.requiredPermissions.length > 0) {
      hasAccess = hasAccess || to.meta.requiredPermissions.some(perm => 
        userPermissions.includes(perm) || userPermissions.includes('*')
      )
    }
    
    // 如果既没有配置角色要求也没有配置权限要求，默认允许访问
    if (!to.meta.requiredRoles && !to.meta.requiredPermissions) {
      hasAccess = true
    }
    
    if (!hasAccess) {
      // 可以重定向到403页面或仪表盘
      next('/dashboard')
      return
    }
  }
  
  next()
})

export default router
