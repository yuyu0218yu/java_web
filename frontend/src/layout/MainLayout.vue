<template>
  <div class="layout-container">
    <el-container class="full-height">
      <!-- 侧边栏 -->
      <el-aside :width="sidebarWidth" class="sidebar" :class="{ 'is-collapse': isCollapse }">
        <div class="logo" @click="$router.push('/dashboard')">
          <transition name="logo-fade" mode="out-in">
            <div v-if="!isCollapse" class="logo-full" key="full">
              <div class="logo-icon">
                <el-icon><Lock /></el-icon>
              </div>
              <h2>张家界管理系统</h2>
            </div>
            <div v-else class="logo-mini" key="mini">
              <el-icon><Lock /></el-icon>
            </div>
          </transition>
        </div>
        <el-scrollbar class="menu-scrollbar">
          <el-menu
            :default-active="$route.path"
            class="sidebar-menu"
            router
            :collapse="isCollapse"
            :background-color="isDark ? '#0a0a0a' : '#ffffff'"
            :text-color="isDark ? '#a0a0a0' : '#303133'"
            :active-text-color="isDark ? '#1a1a1a' : '#409EFF'"
          >
            <!-- 动态菜单 -->
            <menu-item v-for="menu in menuStore.menus" :key="menu.id" :item="menu" />
          </el-menu>
        </el-scrollbar>
        
        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="toggleSidebar">
          <el-icon :class="{ 'rotate-180': isCollapse }">
            <DArrowLeft />
          </el-icon>
        </div>
      </el-aside>

      <!-- 主内容区 -->
      <el-container class="main-container">
        <!-- 顶部导航 -->
        <el-header class="header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">
                <el-icon><HomeFilled /></el-icon>
              </el-breadcrumb-item>
              <el-breadcrumb-item>{{ $route.meta.title || '仪表盘' }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <!-- 全屏按钮 -->
            <el-tooltip content="全屏" placement="bottom">
              <div class="header-action" @click="toggleFullscreen">
                <el-icon><FullScreen /></el-icon>
              </div>
            </el-tooltip>

            <!-- 主题切换 -->
            <el-tooltip :content="isDark ? '切换亮色' : '切换暗色'" placement="bottom">
              <div class="header-action" @click="toggleTheme">
                <el-icon>
                  <Moon v-if="isDark" />
                  <Sunny v-else />
                </el-icon>
              </div>
            </el-tooltip>
            
            <!-- 通知 -->
            <el-tooltip content="通知" placement="bottom">
              <el-badge :value="3" class="notification-badge">
                <div class="header-action">
                  <el-icon><Bell /></el-icon>
                </div>
              </el-badge>
            </el-tooltip>
            
            <!-- 用户下拉菜单 -->
            <el-dropdown trigger="click">
              <div class="user-info">
                <el-avatar :size="36" class="user-avatar">
                  {{ authStore.user?.username?.charAt(0)?.toUpperCase() || 'A' }}
                </el-avatar>
                <div class="user-details">
                  <span class="user-name">{{ authStore.user?.username || '管理员' }}</span>
                  <span class="user-role">{{ authStore.user?.roleName || '普通用户' }}</span>
                </div>
                <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/profile')">
                    <el-icon><User /></el-icon>
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-icon><Setting /></el-icon>
                    系统设置
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 主要内容 -->
        <el-main class="main-content">
          <router-view v-slot="{ Component, route }">
            <transition :name="transitionName" mode="out-in">
              <keep-alive :include="cachedViews">
                <component :is="Component" :key="route.path" />
              </keep-alive>
            </transition>
          </router-view>
        </el-main>
        
        <!-- 页脚 -->
        <el-footer class="footer">
          <span>© 2025 张家界后台管理系统</span>
          <span class="divider">|</span>
          <span>Version 1.0.0</span>
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useMenuStore } from '@/stores/menu'
import MenuItem from '@/components/MenuItem.vue'
import { 
  House, User, UserFilled, Lock, ArrowDown, Key, Avatar,
  HomeFilled, FullScreen, Bell, Setting, SwitchButton, DArrowLeft,
  Moon, Sunny
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const menuStore = useMenuStore()

// 主题切换
const isDark = ref(true)
const toggleTheme = () => {
  isDark.value = !isDark.value
  const html = document.documentElement
  if (isDark.value) {
    html.classList.add('dark')
    localStorage.setItem('theme', 'dark')
  } else {
    html.classList.remove('dark')
    localStorage.setItem('theme', 'light')
  }
}

onMounted(async () => {
  // 初始化主题
  const savedTheme = localStorage.getItem('theme')
  const html = document.documentElement
  if (savedTheme === 'light') {
    isDark.value = false
    html.classList.remove('dark')
  } else {
    isDark.value = true
    html.classList.add('dark')
  }
  
  // 初始化动态菜单
  await menuStore.init()
})

// 侧边栏折叠状态
const isCollapse = ref(false)
const sidebarWidth = computed(() => isCollapse.value ? '64px' : '220px')

// 缓存的视图
const cachedViews = ref(['Dashboard'])

// 过渡动画名称
const transitionName = ref('fade-slide')

// 切换侧边栏
const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

// 切换全屏
const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

// 监听路由变化，设置动画方向
watch(
  () => route.path,
  (to, from) => {
    const toDepth = to.split('/').length
    const fromDepth = from ? from.split('/').length : 0
    transitionName.value = toDepth < fromDepth ? 'fade-slide-right' : 'fade-slide'
  }
)

const handleLogout = () => {
  menuStore.reset()  // 重置菜单状态
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.full-height {
  height: 100%;
}

/* 侧边栏样式 */
.sidebar {
  background: var(--bg-sidebar);
  color: var(--sidebar-text);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1), background-color 0.3s;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
  border-right: 1px solid var(--border-color);
}

.sidebar.is-collapse {
  width: 64px;
}

/* Logo 样式 */
.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.02);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  cursor: pointer;
  transition: background 0.3s;
}

.logo:hover {
  background: rgba(255, 255, 255, 0.05);
}

.logo-full {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  width: 36px;
  height: 36px;
  background: var(--sidebar-active-bg);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--sidebar-active-text);
  font-size: 18px;
  box-shadow: 0 4px 12px var(--shadow-color);
}

.logo h2 {
  color: var(--sidebar-text);
  font-size: 15px;
  margin: 0;
  font-weight: 600;
  white-space: nowrap;
  letter-spacing: 1px;
}

.logo-mini {
  font-size: 24px;
  color: #667eea;
}

/* Logo 动画 */
.logo-fade-enter-active,
.logo-fade-leave-active {
  transition: all 0.2s ease;
}

.logo-fade-enter-from,
.logo-fade-leave-to {
  opacity: 0;
  transform: scale(0.9);
}

/* 菜单滚动区域 */
.menu-scrollbar {
  flex: 1;
  overflow: hidden;
}

/* 菜单样式 */
.sidebar-menu {
  border-right: none;
  padding: 12px 8px;
  background-color: transparent !important;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 100%;
}

.menu-item-animated {
  margin-bottom: 4px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.menu-item-animated:hover {
  background: var(--hover-bg) !important;
}

:deep(.el-menu-item) {
  color: var(--sidebar-text) !important;
}

:deep(.el-menu-item.is-active) {
  background: var(--sidebar-active-bg) !important;
  border-radius: 8px;
  color: var(--sidebar-active-text) !important;
}

:deep(.el-menu-item.is-active .el-icon) {
  color: var(--sidebar-active-text) !important;
}

.menu-icon {
  font-size: 18px;
  margin-right: 8px;
}

.menu-title {
  font-size: 14px;
  font-weight: 500;
}

/* 折叠按钮 */
.collapse-btn {
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  transition: all 0.3s;
  color: #a2a3b7;
}

.collapse-btn:hover {
  background: rgba(255, 255, 255, 0.05);
  color: #fff;
}

.collapse-btn .el-icon {
  font-size: 18px;
  transition: transform 0.3s;
}

.collapse-btn .el-icon.rotate-180 {
  transform: rotate(180deg);
}

/* 主容器 */
.main-container {
  display: flex;
  flex-direction: column;
  background: var(--bg-primary);
  transition: background-color 0.3s;
}

/* 顶部导航 */
.header {
  background-color: var(--bg-header);
  border-bottom: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 64px;
  box-shadow: var(--card-shadow);
  transition: background-color 0.3s, border-color 0.3s;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

:deep(.el-breadcrumb__inner) {
  color: var(--text-secondary) !important;
}

:deep(.el-breadcrumb__inner.is-link:hover) {
  color: var(--text-primary) !important;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: var(--text-primary) !important;
}

.header-action {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  color: var(--text-secondary);
}

.header-action:hover {
  background: var(--hover-bg);
  color: var(--text-primary);
}

.notification-badge :deep(.el-badge__content) {
  top: 8px;
  right: 12px;
}

/* 用户信息 */
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.user-info:hover {
  background: var(--hover-bg);
}

.user-avatar {
  background: var(--bg-secondary);
  color: var(--text-primary);
  font-weight: 600;
  border: 1px solid var(--border-color);
}

.user-details {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  line-height: 1.2;
}

.user-role {
  font-size: 12px;
  color: var(--text-secondary);
}

.dropdown-icon {
  color: #909399;
  transition: transform 0.3s;
}

.user-info:hover .dropdown-icon {
  transform: rotate(180deg);
}

/* 主内容区 */
.main-content {
  background-color: var(--bg-primary);
  padding: 20px;
  flex: 1;
  overflow-y: auto;
  transition: background-color 0.3s;
}

/* 页脚 */
.footer {
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-header);
  border-top: 1px solid var(--border-color);
  color: var(--text-secondary);
  font-size: 12px;
  transition: background-color 0.3s, border-color 0.3s;
}

.footer .divider {
  margin: 0 12px;
  color: var(--border-color);
}

/* 页面切换动画 */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-slide-right-enter-active,
.fade-slide-right-leave-active {
  transition: all 0.3s ease;
}

.fade-slide-right-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-slide-right-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
