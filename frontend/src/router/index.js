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
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: { title: '仪表盘', requiresAuth: true }
  },
  {
    path: '/users',
    name: 'Users',
    component: () => import('@/views/Users.vue'),
    meta: { title: '用户列表', requiresAuth: true }
  },
  {
    path: '/roles',
    name: 'Roles',
    component: () => import('@/views/Roles.vue'),
    meta: { title: '角色管理', requiresAuth: true }
  },
  {
    path: '/permissions',
    name: 'Permissions',
    component: () => import('@/views/Permissions.vue'),
    meta: { title: '权限管理', requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  // 如果访问登录页面且已登录，重定向到仪表盘
  if (to.name === 'Login' && authStore.isAuthenticated) {
    next('/dashboard')
    return
  }
  
  // 如果页面需要认证但用户未登录，重定向到登录页面
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
    return
  }
  
  next()
})

export default router
