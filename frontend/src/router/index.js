import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: { title: '仪表盘' }
  },
  {
    path: '/users',
    name: 'Users',
    component: () => import('@/views/Users.vue'),
    meta: { title: '用户列表' }
  },
  {
    path: '/roles',
    name: 'Roles',
    component: () => import('@/views/Roles.vue'),
    meta: { title: '角色管理' }
  },
  {
    path: '/permissions',
    name: 'Permissions',
    component: () => import('@/views/Permissions.vue'),
    meta: { title: '权限管理' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
