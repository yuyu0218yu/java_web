import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/scenic',
    name: 'ScenicList',
    component: () => import('@/views/scenic/List.vue'),
    meta: { title: '景点门票' }
  },
  {
    path: '/scenic/:id',
    name: 'ScenicDetail',
    component: () => import('@/views/scenic/Detail.vue'),
    meta: { title: '景点详情' }
  },
  {
    path: '/guide',
    name: 'GuideList',
    component: () => import('@/views/guide/List.vue'),
    meta: { title: '旅游攻略' }
  },
  {
    path: '/guide/:id',
    name: 'GuideDetail',
    component: () => import('@/views/guide/Detail.vue'),
    meta: { title: '攻略详情' }
  },
  {
    path: '/guide/publish',
    name: 'GuidePublish',
    component: () => import('@/views/guide/Publish.vue'),
    meta: { title: '发布攻略', requiresAuth: true }
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('@/views/Search.vue'),
    meta: { title: '搜索结果' }
  },
  {
    path: '/order/create',
    name: 'OrderCreate',
    component: () => import('@/views/order/Create.vue'),
    meta: { title: '订单确认', requiresAuth: true }
  },
  {
    path: '/order/success',
    name: 'OrderSuccess',
    component: () => import('@/views/order/Success.vue'),
    meta: { title: '预订成功', requiresAuth: true }
  },
  {
    path: '/user',
    name: 'UserCenter',
    component: () => import('@/views/user/Index.vue'),
    meta: { title: '个人中心', requiresAuth: true },
    children: [
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/user/Profile.vue'),
        meta: { title: '个人资料' }
      },
      {
        path: 'orders',
        name: 'UserOrders',
        component: () => import('@/views/user/Orders.vue'),
        meta: { title: '我的订单' }
      },
      {
        path: 'favorites',
        name: 'UserFavorites',
        component: () => import('@/views/user/Favorites.vue'),
        meta: { title: '我的收藏' }
      },
      {
        path: 'guides',
        name: 'UserGuides',
        component: () => import('@/views/user/Guides.vue'),
        meta: { title: '我的攻略' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 张家界旅游` : '张家界旅游'

  // 检查登录状态
  const authStore = useAuthStore()
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router
